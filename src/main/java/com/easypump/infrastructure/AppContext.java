package com.easypump.infrastructure;

import com.easypump.infrastructure.security.JwtAuthorizationFilter;
import com.easypump.infrastructure.utility.DateUtil;
import com.easypump.model.common.AuditEntity;
import com.easypump.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AppContext implements ApplicationContextAware {

    private static ApplicationContext context;
    private static final ThreadLocal<User> APP_USER = new ThreadLocal<>();

    @Getter
    @Setter
    private static List<JwtAuthorizationFilter.RequestSecurity> requestSecurities;

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        AppContext.context = context;
    }

    public static User getLoggedInUser() {
        return APP_USER.get();
    }

    public static void setLoggedInUser(User loggedInUser) {
        APP_USER.set(loggedInUser);
    }

    public static <T extends AuditEntity> void stamp(T entity) {
        User user = AppContext.getLoggedInUser();
        Date now = DateUtil.now();
        entity.setModifiedBy(user);
        entity.setModifiedOn(now);
        if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(user);
        }
        if (entity.getCreatedOn() == null) {
            entity.setCreatedOn(now);
        }
    }

}
