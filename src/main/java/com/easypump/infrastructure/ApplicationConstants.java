package com.easypump.infrastructure;

import com.easypump.infrastructure.utility.Util;

public class ApplicationConstants {
    public static final String PLATFORM_TYPE_HEADER_KEY = "PLATFORM-TYPE";
    public static final String EMAIL_HEADER_KEY = "email";
    public static final String PASSWORD_HEADER_KEY = "password";
    public static final String JWT_SECRET_KEY = Util.getEnvProperty(EnvironmentVariable.EASY_PUMP_JWT_SECRET_KEY.getValue(), "q3t6w9z$C&F)J@NcQfTjWnZr4u7x!A%D*G-KaPdSgUkXp2s5v8y/B?E(H+MbQeTh");
    public static final String AUTHENTICATION_HEADER_NAME = "authentication";
    public static final String JWT_EXPIRATION_TIME_IN_MINUTES = Util.getEnvProperty(EnvironmentVariable.JWT_EXPIRATION_TIME_MINUTES.getValue(), "30");
    public static final String APP_USER_ID = "userId";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";

}
