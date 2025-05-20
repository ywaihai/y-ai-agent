package com.waihai.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 日志级别枚举类
 */
public enum LogLevelEnum {

    DEBUG("debug"),
    INFO("info"),
    WARNING("warn"),
    ERROR("error");

    private final String level;

    LogLevelEnum(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public static LogLevelEnum getEnumByLevel(String level) {
        if (StringUtils.isEmpty(level)) {
            return null;
        }
        for (LogLevelEnum logLevelEnum : LogLevelEnum.values()) {
            if (logLevelEnum.level.equals(level)) {
                return logLevelEnum;
            }
        }
        return null;
    }
}
