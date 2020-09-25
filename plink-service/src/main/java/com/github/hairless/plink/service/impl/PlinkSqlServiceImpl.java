package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import com.github.hairless.plink.service.PlinkSqlService;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: silence
 * @date: 2020/9/24
 */
@Slf4j
@Service
public class PlinkSqlServiceImpl implements PlinkSqlService {
    public static final String PLINK_SQL_PARSER_CLASS_NAME = "com.github.hairless.plink.sql.util.PlinkSqlParser";

    public static final String SQL_CORE_JAR_FILE = "/module/sql/plink-sql-core-0.2.0-SNAPSHOT.jar";
    public static final String SQL_SHAPE_DIR_PATH = "/module/sql/shape/";

    private final ClassLoader sqlBaseClassLoader;

    public PlinkSqlServiceImpl() throws Exception {
        String userDir = System.getProperty("user.dir");
        List<URL> sqlClassPathUrlList = new ArrayList<>();
        //plink sql core jar
        sqlClassPathUrlList.add(new File(userDir + SQL_CORE_JAR_FILE).toURI().toURL());

        //plink sql shape jars
        File sqlShapeDir = new File(userDir + SQL_SHAPE_DIR_PATH);
        File[] sqlShapeJars = sqlShapeDir.listFiles();
        if (sqlShapeJars != null) {
            for (File sqlShapeJar : sqlShapeJars) {
                sqlClassPathUrlList.add(sqlShapeJar.toURI().toURL());
            }
        }
        //获取扩展类加载器
        ClassLoader extClassLoader = ClassLoader.getSystemClassLoader().getParent();
        //将扩展类加载器设置为sql类加载器的父加载器，防止sql shape引入第3方依赖冲突
        sqlBaseClassLoader = new URLClassLoader(sqlClassPathUrlList.toArray(new URL[0]), extClassLoader);


    }

    /**
     * 调用plink-sql-core模块的{@link com.github.hairless.plink.sql.util.PlinkSqlParser}
     * PlinkSqlParser.create(sql).getSqlParseInfo()
     * @param sql flink sql
     * @return SqlParseInfo {@link com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo}
     */
    @Override
    public SqlParseInfo parse(String sql) {
        ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(sqlBaseClassLoader);

            //exec PlinkSqlParser.create(sql).getSqlParseInfo()
            Class<?> plinkSqlParserClass = sqlBaseClassLoader.loadClass(PLINK_SQL_PARSER_CLASS_NAME);
            Object plinkSqlParser = plinkSqlParserClass.getMethod("create", String.class).invoke(null, sql);
            return (SqlParseInfo)plinkSqlParserClass.getMethod("getSqlParseInfo").invoke(plinkSqlParser);

        } catch (InvocationTargetException e) {
            throw new PlinkRuntimeException("sql parse error", e.getTargetException());
        } catch (Exception e) {
            throw new PlinkRuntimeException("sql parse error", e);
        } finally {
            Thread.currentThread().setContextClassLoader(originClassLoader);
        }
    }

}
