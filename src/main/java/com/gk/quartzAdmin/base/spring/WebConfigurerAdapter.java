package com.gk.quartzAdmin.base.spring;

import com.gk.quartzAdmin.base.SystemInit;
import com.gk.quartzAdmin.util.BeanManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * spring适配器
 * Date:  17/7/12 下午3:22
 */
@Configuration
public class WebConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
    }

    @Bean
    public BeanManager beanManager() {
        return new BeanManager();
    }

    @Bean
    public SystemInit systemInit() {
        return new SystemInit();
    }
}