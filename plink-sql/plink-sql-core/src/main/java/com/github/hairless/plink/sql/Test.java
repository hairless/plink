package com.github.hairless.plink.sql;

import com.github.hairless.plink.sql.factory.CollectionTableFactory;
import org.apache.flink.types.Row;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: silence
 * @date: 2020/3/14
 */
public class Test {
    public static void main(String[] args) throws Exception {

        List<Row> sourceData = Stream.of(
                toRow(1, "1000", 2),
                toRow(3, "1000", 3),
                toRow(3, "2000", 4),
                toRow(1, "2", 2),
                toRow(2, "3000", 3)
        ).collect(Collectors.toList());
        CollectionTableFactory.initData(sourceData);
        String sourceDDL =
                "create table t1(a int comment '测试',b string,c int) with ( 'connector.type' = 'COLLECTION');" +
                        "create view temp_view as select * from t1;";
        String sinkDDL =
                "create table t2( a int,b string, c int) with ( 'connector.type' = 'COLLECTION');";
        String query =
                "insert into t1 select t1.a, t1.b, t1.a + 3 as c from temp_view t1;";

        String sql = sourceDDL + sinkDDL + query;
        SqlConfig config = new SqlConfig();
        config.setSql(sql);
        SqlJob sqlJob = new SqlJob(config);
        sqlJob.start();
    }

    public static Row toRow(Object... args) {
        Row row = new Row(args.length);
        for (int i = 0; i < args.length; i++) {
            row.setField(i, args[i]);
        }
        return row;
    }

}