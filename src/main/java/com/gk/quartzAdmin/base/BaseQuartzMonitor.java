package com.gk.quartzAdmin.base;

import com.gk.quartzAdmin.dao.JobMonitorDao;
import com.gk.quartzAdmin.entity.JobMonitor;
import com.gk.quartzAdmin.service.AlarmContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 基础监控器
 * Date:  17/7/12 下午5:58
 */
public abstract class BaseQuartzMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseQuartzMonitor.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JobMonitorDao jobMonitorDao;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

    private ScheduledFuture<?> sendFuture;

    private long initialDelay = 3000L;

    private long period = 10000L;

    public void start() {
        init();
        sendFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                run();
            } catch (Exception e) {
                LOGGER.error("Unexpected error occur at run(), cause: " + e.getMessage(), e);
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public abstract void init();

    public abstract void run();

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void destroy() {
        try {
            sendFuture.cancel(true);
        } catch (Throwable t) {
            LOGGER.error("Unexpected error occur at cancel sender timer, cause: " + t.getMessage(), t);
        }
    }

    public List<String> getAlarmEmails(String clusterName, String instanceName, String jobName) {
        List<String> emails = new ArrayList<>();
        JobMonitor jobMonitor = jobMonitorDao.findOneJobMonitorByName(clusterName, instanceName, jobName);
        if (jobMonitor == null || StringUtils.isEmpty(jobMonitor.getAlarmContactsIds())) {//未配置报警人默认发给第报警人表里第一个报警人
            if (!CollectionUtils.isEmpty(AlarmContactsService.getCacheAlarmContactsList())) {
                emails.add(AlarmContactsService.getCacheAlarmContactsList().get(0).getEmail());
            }
        } else {
            Arrays.asList(jobMonitor.getAlarmContactsIds().split(",")).forEach(id ->
                    emails.add(AlarmContactsService.getCacheAlarmContactsMap().get(Integer.valueOf(id)).getEmail())
            );
        }
        return emails;
    }

    public void sendEmail(List<String> emails, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(((JavaMailSenderImpl) mailSender).getUsername());
        message.setSubject(subject);
        message.setText(text);
        emails.forEach(email -> {
            message.setTo(email);
            mailSender.send(message);
        });

    }

}
