package com.github.hairless.plink.sql.connector.collection;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.sources.StreamTableSource;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: silence
 * @date: 2020/7/8
 */
@Slf4j
public class CollectionTableSource implements StreamTableSource<Row> {
    private final TableSchema tableSchema;
    private final List<Row> rowList;

    public CollectionTableSource(TableSchema tableSchema, List<Row> rowList) {
        this.tableSchema = tableSchema;
        this.rowList = rowList;
    }

    public CollectionTableSource(TableSchema tableSchema, List<String> dataList, DeserializationSchema<Row> deserializationSchema) {
        this.tableSchema = tableSchema;
        this.rowList = dataList.stream().map(s -> {
            try {
                return deserializationSchema.deserialize(s.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isBounded() {
        return true;
    }

    @Override
    public DataStream<Row> getDataStream(StreamExecutionEnvironment execEnv) {
        return execEnv.fromCollection(rowList);
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
