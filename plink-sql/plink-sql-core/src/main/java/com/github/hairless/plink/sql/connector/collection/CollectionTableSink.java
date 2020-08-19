package com.github.hairless.plink.sql.connector.collection;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.format.EncodingFormat;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.sink.SinkFunctionProvider;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;

/**
 * @author: silence
 * @date: 2020/7/8
 */
@Slf4j
public class CollectionTableSink implements DynamicTableSink {
    public static final String OUT_SUFFIX = "&&OUT&&";
    private final String identifier;
    private final String tableName;
    private final DataType consumedDataType;
    private final EncodingFormat<SerializationSchema<RowData>> encodingFormat;

    public CollectionTableSink(String identifier, String tableName, DataType consumedDataType, EncodingFormat<SerializationSchema<RowData>> encodingFormat) {
        this.identifier = identifier;
        if (tableName.contains(OUT_SUFFIX)) {
            this.tableName = tableName.replace(OUT_SUFFIX, "");
        } else {
            this.tableName = tableName;
        }
        this.consumedDataType = consumedDataType;
        this.encodingFormat = encodingFormat;
    }

    @Override
    public ChangelogMode getChangelogMode(ChangelogMode requestedMode) {
        return requestedMode;
    }

    @Override
    public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
        SerializationSchema<RowData> serializationSchema = encodingFormat.createRuntimeEncoder(context, consumedDataType);
        return SinkFunctionProvider.of(new ElementsSinkFunction(identifier, tableName, serializationSchema));
    }

    @Override
    public DynamicTableSink copy() {
        return new CollectionTableSink(identifier, tableName, consumedDataType, encodingFormat);
    }

    @Override
    public String asSummaryString() {
        return "collection sink:" + tableName;
    }

    public static class ElementsSinkFunction implements SinkFunction<RowData> {
        private final String identifier;
        private final String tableName;
        private final SerializationSchema<RowData> serializationSchema;

        public ElementsSinkFunction(String identifier, String tableName, SerializationSchema<RowData> serializationSchema) {
            this.identifier = identifier;
            this.tableName = tableName;
            this.serializationSchema = serializationSchema;
        }

        @Override
        public void invoke(RowData value, Context context) {
            log.debug("CollectionTableSink:{}:{}:{}", identifier, tableName, new String(serializationSchema.serialize(value)));
            CollectionDataWarehouse.insertOutputData(identifier, tableName, new String(serializationSchema.serialize(value)));
        }
    }
}
