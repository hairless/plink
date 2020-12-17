package com.github.hairless.plink.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.hairless.plink.model.exception.PlinkDataException;
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

    public static final String SQL_BASE_DIR = PlinkUtil.getPlinkHome() + "/module/sql";
    public static final String SQL_JAR_FILE = SQL_BASE_DIR + "/plink-sql.jar";
    public static final String SQL_CONNECTORS_DIR_PATH = SQL_BASE_DIR + "/connectors";
    public static final String SQL_FORMATS_DIR_PATH = SQL_BASE_DIR + "/formats";
    public static final String SQL_UDF_DIR_PATH = SQL_BASE_DIR + "/udf";

    private static volatile ClassLoader sqlBaseClassLoader;

    private static synchronized void initSqlBaseClassLoader() throws Exception {
        if (sqlBaseClassLoader == null) {
            List<URL> sqlClassPathUrlList = new ArrayList<>();
            //plink sql core jar
            File sqlCoreJarFile = new File(SQL_JAR_FILE);
            if (!sqlCoreJarFile.exists()) {
                throw new PlinkRuntimeException("sql core jar file not exist!,path=" + sqlCoreJarFile.getAbsolutePath() +
                        ",you can try 'mvn package' in plink-sql module");
            }
            sqlClassPathUrlList.add(sqlCoreJarFile.toURI().toURL());

            //plink sql connector jars
            sqlClassPathUrlList.addAll(FileUtil.listFileURLs(SQL_CONNECTORS_DIR_PATH));

            //plink sql format jars
            sqlClassPathUrlList.addAll(FileUtil.listFileURLs(SQL_FORMATS_DIR_PATH));

            //plink sql udf jars
            sqlClassPathUrlList.addAll(FileUtil.listFileURLs(SQL_UDF_DIR_PATH));

            //flink dependency jars
            sqlClassPathUrlList.addAll(FileUtil.listFileURLs(FlinkConfigUtil.getFlinkHome() + FlinkConfigUtil.LIB_SUFFIX));

            //获取扩展类加载器
            ClassLoader extClassLoader = ClassLoader.getSystemClassLoader().getParent();
            //将扩展类加载器设置为sql类加载器的父加载器，防止sql 插件引入第3方依赖冲突
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
            Throwable targetException = e.getTargetException();
            if (targetException.getClass().getSimpleName().equals("PlinkSqlParseException")) {
                JsonNode jsonNode = JsonUtil.parseObject(JsonUtil.toJSONString(targetException));
                throw new PlinkDataException(jsonNode.get("message").textValue(), jsonNode.get("pos"));
            }
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
            Object internal = JsonUtil.parseObject(JsonUtil.toJSONString(sqlDebugConfig), SqlDebugConfigClass);
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
