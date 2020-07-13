package com.github.hairless.plink.sql.factory;

import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.descriptors.DescriptorProperties;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.table.factories.StreamTableSinkFactory;
import org.apache.flink.table.factories.StreamTableSourceFactory;
import org.apache.flink.table.factories.TableSinkFactory;
import org.apache.flink.table.factories.TableSourceFactory;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.sources.TableSource;
import org.apache.flink.types.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.flink.table.descriptors.ConnectorDescriptorValidator.CONNECTOR_TYPE;

/**
 * @author: silence
 * @date: 2020/7/8
 */
public class CollectionTableFactory implements StreamTableSourceFactory<Row>, StreamTableSinkFactory<Row> {
    public static List<Row> SOURCE_DATA = new ArrayList<>();

    @Override
    public Map<String, String> requiredContext() {
        Map<String, String> context = new HashMap<>();
        context.put(CONNECTOR_TYPE, "COLLECTION");
        return context;
    }

    @Override
    public List<String> supportedProperties() {
        List<String> list = new ArrayList<>();
        list.add("*");
        return list;
    }

    @Override
    public TableSink<Row> createTableSink(TableSinkFactory.Context context) {
        DescriptorProperties properties = new DescriptorProperties();
        properties.putProperties(context.getTable().toProperties());
        TableSchema schema = properties.getTableSchema(Schema.SCHEMA);
        return new CollectionTableSink(schema);
    }


    @Override
    public TableSource<Row> createTableSource(TableSourceFactory.Context context) {
        DescriptorProperties properties = new DescriptorProperties();
        properties.putProperties(context.getTable().toProperties());
        TableSchema schema = properties.getTableSchema(Schema.SCHEMA);
        return new CollectionTableSource(schema, SOURCE_DATA);
    }

    public static void initData(List<Row> data) {
        SOURCE_DATA.addAll(data);
    }
}
