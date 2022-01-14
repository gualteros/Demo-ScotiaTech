package com.scotiademo.mslegacyrouting.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class BdLogMessageFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        if (iLoggingEvent.getMessage() != null &&
                (iLoggingEvent.getMessage().contains("script")
                        || iLoggingEvent.getMessage().contains("exec")
                        || iLoggingEvent.getMessage().contains("execute")
                        || iLoggingEvent.getMessage().contains("use")
                        || iLoggingEvent.getMessage().contains("add")
                        || iLoggingEvent.getMessage().contains("like")
                        || iLoggingEvent.getMessage().contains("insert")
                        || iLoggingEvent.getMessage().contains("input")
                        || iLoggingEvent.getMessage().contains("update")
                        || iLoggingEvent.getMessage().contains("delete")
                        || iLoggingEvent.getMessage().contains("select")
                        || iLoggingEvent.getMessage().contains("call")
                        || iLoggingEvent.getMessage().contains("create")
                        || iLoggingEvent.getMessage().contains("drop")
                        || iLoggingEvent.getMessage().contains("alter")
                        || iLoggingEvent.getMessage().contains("passwd=")
                        || iLoggingEvent.getMessage().contains("password=")
                        || iLoggingEvent.getMessage().contains("passwd")
                        || iLoggingEvent.getMessage().contains("%")
                        || iLoggingEvent.getMessage().contains("$")
                        || iLoggingEvent.getMessage().contains("?")
                        || iLoggingEvent.getMessage().contains("@")
                        || iLoggingEvent.getMessage().contains("*")
                        || iLoggingEvent.getMessage().contains("\n\r"))) {
            return FilterReply.DENY;
        } else {
            return FilterReply.ACCEPT;
        }

    }
}
