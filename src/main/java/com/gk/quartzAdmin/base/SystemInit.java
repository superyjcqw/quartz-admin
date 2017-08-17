package com.gk.quartzAdmin.base;

import com.gk.quartzAdmin.service.AlarmContactsService;
import com.gk.quartzAdmin.service.QuartzClusterService;
import com.gk.quartzAdmin.util.BeanManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

/**
 * 系统初始化
 * Date:  17/7/12 下午8:01
 */
public class SystemInit {

    Map<String, BaseQuartzMonitor> monitorMap = BeanManager.getApplicationContext().getBeansOfType(BaseQuartzMonitor.class);

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        BeanManager.getBean(QuartzClusterService.class).init();
        BeanManager.getBean(QuartzClusterDataSourcePool.class).init();
        BeanManager.getBean(AlarmContactsService.class).init();
        monitorMap.values().forEach(monitor ->
                monitor.start()
        );
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        monitorMap.values().forEach(monitor ->
                monitor.destroy()
        );
    }

}

