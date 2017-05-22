package com.passwordboss.android.logback;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.spi.FilterReply;

public class CrashlyticsAppender extends AppenderBase<ILoggingEvent> {
    private PatternLayoutEncoder mEncoder;
    private boolean mGuard = false;

    @Override
    protected synchronized void append(ILoggingEvent loggingEvent) {
        if (mGuard) return;
        try {
            mGuard = true;
            if (!isStarted()) return;
            String layoutString = mEncoder.getLayout().doLayout(loggingEvent);
            if (getFilterChainDecision(loggingEvent) == FilterReply.DENY) return;
            int priority;
            switch (loggingEvent.getLevel().levelInt) {
                case Level.ALL_INT:
                case Level.TRACE_INT:
                    priority = Log.VERBOSE;
                    break;
                case Level.DEBUG_INT:
                    priority = Log.DEBUG;
                    break;
                case Level.INFO_INT:
                    priority = Log.INFO;
                    break;
                case Level.WARN_INT:
                    priority = Log.WARN;
                    break;
                case Level.ERROR_INT:
                case Level.OFF_INT:
                default:
                    priority = Log.ERROR;
                    break;
            }
            Crashlytics.log(priority, loggingEvent.getLoggerName(), layoutString);
        } finally {
            mGuard = false;
        }
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        mEncoder = encoder;
    }

    @Override
    public void start() {
        if (mEncoder == null || mEncoder.getLayout() == null) {
            addError("No layout set for the appender named [" + name + "].");
            return;
        }
        super.start();
    }
}