package com.github.hairless.plink.sql.connector.collection;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.format.DecodingFormat;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.connector.source.ScanTableSource;
import org.apache.flink.table.connector.source.SourceFunctionProvider;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;

import java.io.IOException;
import java.util.List;

/**
 * @author: silence
 * @date: 2020/7/8
 */
@Slf4j
public class CollectionTableSource implements ScanTableSource {
    private final List<String> dataList;
    private final DataType producedDataType;
    private final DecodingFormat<DeserializationSchema<RowData>> decodingFormat;

    public CollectionTableSource(List<String> dataList, DataType producedDataType, DecodingFormat<DeserializationSchema<RowData>> decodingFormat) {
        this.producedDataType = producedDataType;
        this.decodingFormat = decodingFormat;
        this.dataList = dataList;
    }

    @Override
    public DynamicTableSource copy() {
        return new CollectionTableSource(dataList, producedDataType, decodingFormat);
    }

    @Override
    public String asSummaryString() {
        return "collection source";
    }

    @Override
    public ChangelogMode getChangelogMode() {
        return ChangelogMode.insertOnly();
    }

    @Override
    public ScanRuntimeProvider getScanRuntimeProvider(ScanContext runtimeProviderContext) {
        DeserializationSchema<RowData> deserializationSchema = decodingFormat.createRuntimeDecoder(runtimeProviderContext, producedDataType);
        return SourceFunctionProvider.of(new ElementsSourceFunction(dataList, deserializationSchema), false);
    }

    public static class ElementsSourceFunction extends RichParallelSourceFunction<RowData> {
        private final List<String> dataList;
        private final DeserializationSchema<RowData> deserializationSchema;

        public ElementsSourceFunction(List<String> dataList, DeserializationSchema<RowData> deserializationSchema) {
            this.dataList = dataList;
            this.deserializationSchema = deserializationSchema;
        }


        @Override
        public void run(SourceContext<RowData> ctx) throws Exception {
            dataList.stream().map(String::getBytes).map(message -> {
                try {
                    return deserializationSchema.deserialize(message);
                } catch (IOException e) {
                    log.error("deserialize error: {}", new String(message));
                }
                return null;
            }).forEach(ctx::collect);
        }

        @Override
        public void cancel() {

        }
    }
}
