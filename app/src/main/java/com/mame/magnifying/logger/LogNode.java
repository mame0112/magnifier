package com.mame.magnifying.logger;

/**
 * Created by kosukeEndo on 2018/01/03.
 */

public interface LogNode {
    public void println(int priority, String tag, String msg, Throwable tr);
}
