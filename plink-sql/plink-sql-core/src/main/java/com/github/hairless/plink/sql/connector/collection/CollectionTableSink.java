package com.github.hairless.plink.sql.connector.collection;

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
public class CollectionTableSink implements AppendStreamTableSink<Row> {
    private final String tableName;
    private final TableSchema tableSchema;
    private final SerializationSchema<Row> serializationSchema;

    public CollectionTableSink(String tableName, TableSchema tableSchema, SerializationSchema<Row> serializationSchema) {
        this.tableName = tableName;
        this.tableSchema = tableSchema;
        this.serializationSchema = serializationSchema;
    }

    @Override
    public DataStreamSink<?> consumeDataStream(DataStream<Row> dataStream) {
        return dataStream.addSink(new ElementsSinkFunction(tableName, serializationSchema));
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
        private final SerializationSchema<Row> serializationSchema;
        private final String tableName;

        ElementsSinkFunction(String tableName, SerializationSchema<Row> serializationSchema) {
            this.tableName = tableName;
            this.serializationSchema = serializationSchema;
        }

        @Override
        public void invoke(Row value, Context context) {
            System.out.println(tableName + ":" + new String(serializationSchema.serialize(value)));
        }
    }
}
