package com.github.hairless.plink.sql.connector.collection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.hairless.plink.sql.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.connector.format.DecodingFormat;
import org.apache.flink.table.connector.format.EncodingFormat;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.factories.*;
import org.apache.flink.table.types.DataType;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: silence
 * @date: 2020/7/8
 */
@Slf4j
public class CollectionTableFactory implements DynamicTableSourceFactory, DynamicTableSinkFactory {
    public static final String COLLECTION = "collection";
    public static final ConfigOption<String> DEFAULT_FORMAT = ConfigOptions
            .key("format")
            .stringType()
            .defaultValue("json")
            .withDescription("Defines the format identifier for encoding data. " +
                    "The identifier is used to discover a suitable format factory.");

    public static final ConfigOption<String> DATA = ConfigOptions
            .key("data")
            .stringType()
            .noDefaultValue()
            .withDescription("Required 'data' from which the table is read");
    public static final ConfigOption<String> IDENTIFIER = ConfigOptions
            .key("identifier")
            .stringType()
            .noDefaultValue()
            .withDescription("Required 'identifier' from which the table is write");

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        ReadableConfig tableOptions = helper.getOptions();
        String identifier = tableOptions.get(IDENTIFIER);
        String tableName = context.getObjectIdentifier().getObjectName();
        DataType consumedDataType = context.getCatalogTable().getSchema().toPhysicalRowDataType();
        EncodingFormat<SerializationSchema<RowData>> encodingFormat = helper.discoverEncodingFormat(
                SerializationFormatFactory.class, DEFAULT_FORMAT);
        return new CollectionTableSink(identifier, tableName, consumedDataType, encodingFormat);
    }

    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        ReadableConfig tableOptions = helper.getOptions();
        String dataString = tableOptions.get(DATA);
        List<String> dataList = JsonUtil.parseObject(dataString, new TypeReference<List<String>>() {
        });
        DataType producedDataType = context.getCatalogTable().getSchema().toPhysicalRowDataType();
        DecodingFormat<DeserializationSchema<RowData>> decodingFormat = helper.discoverDecodingFormat(
                DeserializationFormatFactory.class, DEFAULT_FORMAT);
        return new CollectionTableSource(dataList, producedDataType, decodingFormat);
    }

    @Override
    public String factoryIdentifier() {
        return COLLECTION;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        return Collections.emptySet();
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        return Stream.of(DEFAULT_FORMAT, DATA, IDENTIFIER).collect(Collectors.toSet());
    }
}
