package com.github.hairless.plink.common.util;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import com.github.hairless.plink.sql.model.SqlDebugConfig;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/9/24
 */
@Slf4j
@Service
public class PlinkSqlUtil {
    public static final String PLINK_SQL_JOB_DRIVER_CLASS_NAME = "com.github.hairless.plink.sql.SqlJobDriver";
    public static final String PLINK_SQL_DEBUG_DRIVER_CLASS_NAME = "com.github.hairless.plink.sql.SqlDebugDriver";
    public static final String PLINK_SQL_PARSER_CLASS_NAME = "com.github.hairless.plink.sql.util.PlinkSqlParser";

    public static final String SQL_JAR_FILE = "/module/sql/plink-sql.jar";
    public static final String SQL_SHAPE_DIR_PATH = "/module/sql/shape/";

    private static volatile ClassLoader sqlBaseClassLoader;

    private static synchronized void initSqlBaseClassLoader() throws Exception {
        if (sqlBaseClassLoader == null) {
            String plinkHome = PlinkUtil.getPlinkHome();
            List<URL> sqlClassPathUrlList = new ArrayList<>();
            //plink sql core jar
            File sqlCoreJarFile = new File(plinkHome + SQL_JAR_FILE);
            if (!sqlCoreJarFile.exists()) {
                throw new PlinkRuntimeException("sql core jar file not exist!,path=" + sqlCoreJarFile.getAbsolutePath() +
                        ",you can try 'mvn package' in plink-sql module");
            }
            sqlClassPathUrlList.add(sqlCoreJarFile.toURI().toURL());

            //plink sql shape jars
            File sqlShapeDir = new File(plinkHome + SQL_SHAPE_DIR_PATH);
            if (!sqlShapeDir.exists()) {
                throw new PlinkRuntimeException("sql shape dir not exist,path=" + sqlShapeDir.getAbsolutePath() +
                        ",you can try 'mvn package' in plink-sql module");
            }
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
    }

    public static ClassLoader getSqlBaseClassLoader() throws Exception {
        if (sqlBaseClassLoader == null) {
            initSqlBaseClassLoader();
        }
        return sqlBaseClassLoader;
    }

    /**
     * 调用plink-sql-core模块的{@link com.github.hairless.plink.sql.util.PlinkSqlParser}
     * PlinkSqlParser.create(sql).getSqlParseInfo()
     *
     * @param sql flink sql
     * @return SqlParseInfo {@link SqlParseInfo}
     */
    public static SqlParseInfo parse(String sql) {
        ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader sqlBaseClassLoader = getSqlBaseClassLoader();
            Thread.currentThread().setContextClassLoader(sqlBaseClassLoader);

            //exec PlinkSqlParser.create(sql).getSqlParseInfo()
            Class<?> plinkSqlParserClass = sqlBaseClassLoader.loadClass(PLINK_SQL_PARSER_CLASS_NAME);
            Object plinkSqlParser = plinkSqlParserClass.getMethod("create", String.class).invoke(null, sql);

            SqlParseInfo newRes = new SqlParseInfo();
            Object origRes = plinkSqlParserClass.getMethod("getSqlParseInfo").invoke(plinkSqlParser);
            BeanUtils.copyProperties(newRes, origRes);
            return newRes;

        } catch (InvocationTargetException e) {
            throw new PlinkRuntimeException("sql parse error", e.getTargetException());
        } catch (Exception e) {
            throw new PlinkRuntimeException("sql parse error", e);
        } finally {
            Thread.currentThread().setContextClassLoader(originClassLoader);
        }
    }

    /**
     * 调用plink-sql-core模块的{@link com.github.hairless.plink.sql.SqlDebugDriver}
     * SqlDebugDriver.debug(sqlDebugConfig)
     *
     * @param sqlDebugConfig SqlDebugConfig
     * @return Map<String, List < String>>
     */
    public static Map<String, List<String>> debug(SqlDebugConfig sqlDebugConfig) {
        ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader sqlBaseClassLoader = getSqlBaseClassLoader();
            Thread.currentThread().setContextClassLoader(sqlBaseClassLoader);
            Class<?> plinkSqlParserClass = sqlBaseClassLoader.loadClass(PLINK_SQL_DEBUG_DRIVER_CLASS_NAME);
            Class<?> SqlDebugConfigClass = sqlBaseClassLoader.loadClass(SqlDebugConfig.class.getName());
            Object internal = JSON.parseObject(JSON.toJSONString(sqlDebugConfig), SqlDebugConfigClass);
            Object debugRes = plinkSqlParserClass.getMethod("debug", SqlDebugConfigClass).invoke(null, internal);
            return (Map<String, List<String>>) debugRes;
        } catch (InvocationTargetException e) {
            throw new PlinkRuntimeException("sql debug error", e.getTargetException());
        } catch (Exception e) {
            throw new PlinkRuntimeException("sql debug error", e);
        } finally {
            Thread.currentThread().setContextClassLoader(originClassLoader);
        }
    }

}
