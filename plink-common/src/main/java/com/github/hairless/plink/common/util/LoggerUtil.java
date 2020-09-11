package com.github.hairless.plink.common.util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author: silence
 * @date: 2020/6/29
 */
public class LoggerUtil {

    private final static LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    private final static Logger rootLogger = context.getLogger("ROOT");
    private final static String pattern = OptionHelper.substVars("${LOG_PATTERN}", context);

    public static Appender<ILoggingEvent> registerThreadFileAppender(String appenderName, String fileName) {
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setName(appenderName);
        fileAppender.setContext(context);
        fileAppender.setAppend(true);
        fileAppender.setPrudent(true);
        fileAppender.setFile(fileName);

        final String currentThreadName = Thread.currentThread().getName();
        fileAppender.addFilter(new Filter<ILoggingEvent>() {
            @Override
            public FilterReply decide(ILoggingEvent event) {
                if (currentThreadName.equals(event.getThreadName())) {
                    return FilterReply.ACCEPT;
                }
                return FilterReply.DENY;
            }
        });

        PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
        patternLayoutEncoder.setContext(context);
        patternLayoutEncoder.setPattern(pattern);
        patternLayoutEncoder.setCharset(StandardCharsets.UTF_8);
        patternLayoutEncoder.start();
        fileAppender.setEncoder(patternLayoutEncoder);
        fileAppender.start();
        rootLogger.addAppender(fileAppender);

        return fileAppender;
    }

    public static void rescindAppender(String appenderName) {
        Appender<ILoggingEvent> appender = rootLogger.getAppender(appenderName);
        rescindAppender(appender);
    }

    public static void rescindAppender(Appender<ILoggingEvent> appender) {
        appender.stop();
        rootLogger.detachAppender(appender);
    }


}
