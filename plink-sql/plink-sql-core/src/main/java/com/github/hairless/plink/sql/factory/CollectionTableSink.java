package com.github.hairless.plink.sql.factory;

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
    TableSchema tableSchema;

    public CollectionTableSink(TableSchema tableSchema) {
        this.tableSchema = tableSchema;
    }
    @Override
    public DataStreamSink<?> consumeDataStream(DataStream<Row> dataStream) {
        return dataStream.addSink(new SinkFunction<Row>() {
            @Override
            public void invoke(Row value, Context context) throws Exception {
                System.out.println(value);
            }
        });
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
}
