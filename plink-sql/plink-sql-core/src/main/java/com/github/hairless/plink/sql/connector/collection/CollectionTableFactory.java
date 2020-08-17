package com.github.hairless.plink.sql.connector.collection;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.calcite.shaded.com.google.common.collect.Lists;
import org.apache.flink.table.catalog.CatalogTable;
import org.apache.flink.table.descriptors.FormatDescriptorValidator;
import org.apache.flink.table.factories.*;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.sources.TableSource;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.flink.table.descriptors.ConnectorDescriptorValidator.CONNECTOR_TYPE;

/**
 * @author: silence
 * @date: 2020/7/8
 */
@Slf4j
public class CollectionTableFactory implements StreamTableSourceFactory<Row>, StreamTableSinkFactory<Row> {
    public static final String COLLECTION = "collection";
    public static final String DATA = "data";
    public static final String IDENTIFIER = "identifier";

    @Override
    public Map<String, String> requiredContext() {
        Map<String, String> context = new HashMap<>();
        context.put(CONNECTOR_TYPE, COLLECTION);
        return context;
    }

    @Override
    public List<String> supportedProperties() {
        return Lists.newArrayList("*");
    }

    @Override
    public TableSink<Row> createTableSink(TableSinkFactory.Context context) {
        CatalogTable table = context.getTable();
        Map<String, String> tableProperties = table.toProperties();
        String tableName = context.getObjectIdentifier().getObjectName();
        String identifier = tableProperties.get(IDENTIFIER);
        SerializationSchema<Row> serializationSchema = getSerializationSchema(tableProperties);
        return new CollectionTableSink(identifier, tableName, table.getSchema(), serializationSchema);
    }


    @Override
    public TableSource<Row> createTableSource(TableSourceFactory.Context context) {
        Map<String, String> tableProperties = context.getTable().toProperties();
        DeserializationSchema<Row> deserializationSchema = getDeserializationSchema(tableProperties);
        String dataString = tableProperties.get(DATA);
        List<String> dataList = JSON.parseArray(dataString, String.class);
        return new CollectionTableSource(context.getTable().getSchema(), dataList, deserializationSchema);
    }

    private DeserializationSchema<Row> getDeserializationSchema(Map<String, String> properties) {
        String format_type = properties.get(FormatDescriptorValidator.FORMAT_TYPE);
        try {
            @SuppressWarnings("unchecked") final DeserializationSchemaFactory<Row> formatFactory = TableFactoryService.find(
                    DeserializationSchemaFactory.class,
                    properties,
                    this.getClass().getClassLoader());
            return formatFactory.createDeserializationSchema(properties);
        } catch (Exception e) {
            log.error("format {} not support", format_type);
            throw new RuntimeException("format " + format_type + " not support", e);
        }
    }

    private SerializationSchema<Row> getSerializationSchema(Map<String, String> properties) {
        Map<String, String> newProperties = new HashMap<>(properties);
        String format_type = properties.get(FormatDescriptorValidator.FORMAT_TYPE);
        if (StringUtils.isEmpty(format_type)) {
            newProperties.put(FormatDescriptorValidator.FORMAT_TYPE, "json");
        }
        try {
            @SuppressWarnings("unchecked") final SerializationSchemaFactory<Row> formatFactory = TableFactoryService.find(
                    SerializationSchemaFactory.class,
                    newProperties,
                    this.getClass().getClassLoader());
            return formatFactory.createSerializationSchema(newProperties);
        } catch (Exception e) {
            log.error("format {} not support", format_type);
            throw new RuntimeException("format " + format_type + " not support", e);
        }
    }
}
