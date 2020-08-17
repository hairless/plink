package com.github.hairless.plink.sql.connector.collection;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.sinks.AppendStreamTableSink;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;

/**
 * @author: silence
 * @date: 2020/7/8
 */
@Slf4j
public class CollectionTableSink implements AppendStreamTableSink<Row> {
    public static final String OUT_SUFFIX = "&&OUT&&";
    private final String identifier;
    private final String tableName;
    private final TableSchema tableSchema;
    private final SerializationSchema<Row> serializationSchema;

    public CollectionTableSink(String identifier, String tableName, TableSchema tableSchema, SerializationSchema<Row> serializationSchema) {
        this.identifier = identifier;
        if (tableName.contains(OUT_SUFFIX)) {
            this.tableName = tableName.replace(OUT_SUFFIX, "");
        } else {
            this.tableName = tableName;
        }
        this.tableSchema = tableSchema;
        this.serializationSchema = serializationSchema;
    }

    @Override
    public DataStreamSink<?> consumeDataStream(DataStream<Row> dataStream) {
        return dataStream.addSink(new ElementsSinkFunction(identifier, tableName, serializationSchema));
    }

    @Override
    public DataType getConsumedDataType() {
        return tableSchema.toRowDataType();
    }

    @Override
    public TableSchema getTableSchema() {
        return tableSchema;
    }

    @Override
    public TableSink<Row> configure(String[] fieldNames, TypeInformation<?>[] fieldTypes) {
        return this;
    }

    public static class ElementsSinkFunction implements SinkFunction<Row> {
        private final String identifier;
        private final String tableName;
        private final SerializationSchema<Row> serializationSchema;

        public ElementsSinkFunction(String identifier, String tableName, SerializationSchema<Row> serializationSchema) {
            this.identifier = identifier;
            this.tableName = tableName;
            this.serializationSchema = serializationSchema;
        }

        @Override
        public void invoke(Row value, Context context) {
            log.debug("CollectionTableSink:{}:{}:{}", identifier, tableName, new String(serializationSchema.serialize(value)));
            CollectionDataWarehouse.insertOutputData(identifier, tableName, new String(serializationSchema.serialize(value)));
        }
    }
}
