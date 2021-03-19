package com.github.hairless.plink.sql.connector.hive;

import static com.github.hairless.plink.sql.connector.hive.options.HiveOptions.HIVE_CONF_DIR;
import static com.github.hairless.plink.sql.connector.hive.options.HiveOptions.HIVE_DATABASE;
import static com.github.hairless.plink.sql.connector.hive.options.HiveOptions.HIVE_TABLE;
import static com.github.hairless.plink.sql.connector.hive.options.HiveOptions.HIVE_VERSION;
import static org.apache.flink.table.catalog.config.CatalogConfig.IS_GENERIC;
import static org.apache.flink.table.catalog.hive.HiveCatalog.isEmbeddedMetastore;
import static org.apache.flink.table.catalog.hive.util.HiveTableUtil.getHadoopConfiguration;
import static org.apache.flink.util.StringUtils.isNullOrWhitespaceOnly;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.flink.api.java.hadoop.mapred.utils.HadoopUtils;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connectors.hive.HiveDynamicTableFactory;
import org.apache.flink.connectors.hive.HiveTableSink;
import org.apache.flink.table.api.ValidationException;
import org.apache.flink.table.catalog.CatalogTable;
import org.apache.flink.table.catalog.ObjectIdentifier;
import org.apache.flink.table.catalog.config.CatalogConfig;
import org.apache.flink.table.catalog.exceptions.CatalogException;
import org.apache.flink.table.catalog.hive.descriptors.HiveCatalogValidator;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.factories.FactoryUtil.TableFactoryHelper;
import org.apache.flink.table.filesystem.FileSystemOptions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.mapred.JobConf;

/**
 * @author chaixiaoxue
 * @date 2021/3/19 10:40
 */
public class PlinkHiveDynamicTableFactory extends HiveDynamicTableFactory {

    public static final String IDENTIFIER = "plink-hive";

    public PlinkHiveDynamicTableFactory() {
        super(null);
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        final Set<ConfigOption<?>> options = new HashSet<>();
        options.add(HIVE_CONF_DIR);
        options.add(HIVE_VERSION);
        options.add(HIVE_DATABASE);
        options.add(HIVE_TABLE);
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        final Set<ConfigOption<?>> options = new HashSet<>();
        options.add(FileSystemOptions.SINK_PARTITION_COMMIT_POLICY_KIND);
        return options;
    }

    private static CatalogTable removeIsGenericFlag(Context context) {
        Map<String, String> newOptions = new HashMap<>(context.getCatalogTable().getOptions());
        boolean isGeneric = Boolean.parseBoolean(newOptions.remove(IS_GENERIC));
        // temporary table doesn't have the IS_GENERIC flag but we still consider it generic
        if (!isGeneric && !context.isTemporary()) {
            throw new ValidationException(
                "Hive dynamic table factory now only work for generic table.");
        }
        return context.getCatalogTable().copy(newOptions);
    }

    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        return super.createDynamicTableSource(context);
    }

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        final TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);

        final ReadableConfig tableOptions = helper.getOptions();
        String hiveDir = tableOptions.getOptional(HIVE_CONF_DIR).get();
        HiveConf hiveConf = createHiveConf(hiveDir,null);

        boolean isGeneric =
            Boolean.parseBoolean(
                context.getCatalogTable().getOptions().get(CatalogConfig.IS_GENERIC));
        JobConf jobConf = new JobConf(hiveConf);
        jobConf.set(HiveCatalogValidator.CATALOG_HIVE_VERSION,tableOptions.getOptional(HIVE_VERSION).get());
        ObjectIdentifier of = ObjectIdentifier.of("default_catalog", tableOptions.getOptional(HIVE_TABLE).get(),
            tableOptions.getOptional(HIVE_TABLE).get());
        if (!isGeneric && !context.isTemporary()) {
            return new HiveTableSink(
                context.getConfiguration(),
                jobConf,
                of,
                context.getCatalogTable());
        } else {
            return FactoryUtil.createTableSink(
                null,
                context.getObjectIdentifier(),
                removeIsGenericFlag(context),
                context.getConfiguration(),
                context.getClassLoader(),
                context.isTemporary());
        }
    }

    private static HiveConf createHiveConf(@Nullable String hiveConfDir, @Nullable String hadoopConfDir) {
        // create HiveConf from hadoop configuration with hadoop conf directory configured.
        Configuration hadoopConf = null;
        if (isNullOrWhitespaceOnly(hadoopConfDir)) {
            for (String possibleHadoopConfPath : HadoopUtils.possibleHadoopConfPaths(new org.apache.flink.configuration.Configuration())) {
                hadoopConf = getHadoopConfiguration(possibleHadoopConfPath);
                if (hadoopConf != null) {
                    break;
                }
            }
        } else {
            hadoopConf = getHadoopConfiguration(hadoopConfDir);
        }
        if (hadoopConf == null) {
            hadoopConf = new Configuration();
        }
        HiveConf hiveConf = new HiveConf(hadoopConf, HiveConf.class);
        if (hiveConfDir != null) {
            Path hiveSite = new Path(hiveConfDir, "hive-site.xml");
            if (!hiveSite.toUri().isAbsolute()) {
                // treat relative URI as local file to be compatible with previous behavior
                hiveSite = new Path(new File(hiveSite.toString()).toURI());
            }
            try (InputStream inputStream = hiveSite.getFileSystem(hadoopConf).open(hiveSite)) {
                hiveConf.addResource(inputStream, hiveSite.toString());
                // trigger a read from the conf so that the input stream is read
                isEmbeddedMetastore(hiveConf);
            } catch (IOException e) {
                throw new CatalogException("Failed to load hive-site.xml from specified path:" + hiveSite, e);
            }
        }
        return hiveConf;
    }
}
