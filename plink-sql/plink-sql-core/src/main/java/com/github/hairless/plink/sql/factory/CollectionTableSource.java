package com.github.hairless.plink.sql.factory;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.sources.StreamTableSource;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;

import java.util.List;

/**
 * @author: silence
 * @date: 2020/7/8
 */
public class CollectionTableSource implements StreamTableSource<Row> {
    TableSchema tableSchema;
    List<Row> data;

    public CollectionTableSource(TableSchema tableSchema, List<Row> data) {
        this.tableSchema = tableSchema;
        this.data = data;
    }

    @Override
    public boolean isBounded() {
        return true;
    }

    @Override
    public DataStream<Row> getDataStream(StreamExecutionEnvironment execEnv) {
        return execEnv.fromCollection(data);
    }

    @Override
    public DataType getProducedDataType() {
        return tableSchema.toRowDataType();
    }


    @Override
    public TableSchema getTableSchema() {
        return tableSchema;
    }

    @Override
    public String explainSource() {
        return null;
    }
}
