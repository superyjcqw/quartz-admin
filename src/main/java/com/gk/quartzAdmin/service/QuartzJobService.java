package com.gk.quartzAdmin.service;

import com.alibaba.fastjson.JSONObject;
import com.gk.quartzAdmin.dao.DeletedJobDao;
import com.gk.quartzAdmin.dao.JobMonitorDao;
import com.gk.quartzAdmin.dao.QuartzJobDao;
import com.gk.quartzAdmin.entity.*;
import com.gk.quartzAdmin.util.DateUtil;
import com.gk.quartzAdmin.util.HttpUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

/**
 * job管理Service
 * Date:  17/7/11 下午5:28
 */
@Service
public class QuartzJobService {

    @Autowired
    private QuartzJobDao quartzJobDao;

    @Autowired
    private DeletedJobDao deletedJobDao;

    @Autowired
    private JobMonitorDao jobMonitorDao;

    @Autowired
    private AlarmContactsService alarmContactsService;

    /**
     * 任务分页集合
     * @param clusterName 集群名
     * @param triggerState 触发器状态
     * @param jobName 任务名
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @return 任务分页集合
     */
    public List<QuartzJob> list(String clusterName, String triggerState, String jobName, Integer pageIndex, Integer pageSize) {
        Integer beginIndex = (pageIndex - 1) * pageSize;
        List<QuartzJob> list = quartzJobDao.list(clusterName, QuartzClusterService.getCacheMap().get(clusterName).getInstanceName(), triggerState, jobName, beginIndex, pageSize);
        list.forEach(quartzJob -> {
            quartzJob.setStatusStr(QuartzJob.statusStrMap.get(quartzJob.getTriggerState()));
            quartzJob.setPrevFireTimeStr(DateUtil.format(new Date(quartzJob.getPrevFireTime()), DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
            quartzJob.setNextFireTimeStr(DateUtil.format(new Date(quartzJob.getNextFireTime()), DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
            quartzJob.setClusterName(clusterName);
            JobMonitor jobMonitor = jobMonitorDao.findOneJobMonitorByName(clusterName, quartzJob.getSchedName(), quartzJob.getJobName());
            if (jobMonitor != null) {
                quartzJob.setMaxExecuteTime(jobMonitor.getMaxExecuteTime());
                quartzJob.setAlarmContactsNames(alarmContactsService.getAlarmContactsName(jobMonitor.getAlarmContactsIds()));
            }

        });
        return list;
    }

    /**
     * 查询一个任务
     * @param clusterName 集群名
     * @param jobName 任务名
     * @return job
     */
    public QuartzJob findOne(String clusterName, String jobName) {
        QuartzJob quartzJob = quartzJobDao.findOne(clusterName, QuartzClusterService.getCacheMap().get(clusterName).getInstanceName(), jobName);
        JobMonitor jobMonitor = jobMonitorDao.findOneJobMonitorByName(clusterName, quartzJob.getSchedName(), quartzJob.getJobName());
        if (jobMonitor != null) {
            quartzJob.setMaxExecuteTime(jobMonitor.getMaxExecuteTime());
            quartzJob.setAlarmContactsIds(jobMonitor.getAlarmContactsIds());
        }
        return quartzJob;
    }

    /**
     * 获取包含选中的报警人
     * @param alarmContactsIds
     * @return
     */
    public List<AlarmContacts> getCheckedAlarmContacts(String alarmContactsIds) {
        List<AlarmContacts> alarmContactsList = new ArrayList<>();
        AlarmContactsService.getCacheAlarmContactsList().forEach(alarmContacts -> {
            AlarmContacts ac = new AlarmContacts();
            BeanUtils.copyProperties(alarmContacts, ac);
            alarmContactsList.add(ac);
        });
        alarmContactsList.forEach(alarmContacts -> {
            if (!StringUtils.isEmpty(alarmContactsIds)) {
                if (Arrays.asList(alarmContactsIds.split(",")).contains(String.valueOf(alarmContacts.getId()))) {
                    alarmContacts.setChecked(true);
                }
            }
        });
        return alarmContactsList;
    }


    /**
     * 查询已经点火的触发器集合
     * @param clusterName 集群名
     * @return 正在运行的任务
     */
    public List<FiredTrigger> firedTriggerList(String clusterName) {
        return quartzJobDao.firedTriggerList(clusterName, QuartzClusterService.getCacheMap().get(clusterName).getInstanceName());
    }

    /**
     * 查询任务总数
     * @param clusterName 集群名
     * @param triggerState 触发器状态
     * @param jobName 任务名
     * @return 任务总数
     */
    public Integer count(String clusterName, String triggerState, String jobName) {
        return quartzJobDao.count(clusterName, QuartzClusterService.getCacheMap().get(clusterName).getInstanceName(), triggerState, jobName);
    }

    /**
     * 新增任务
     * @param quartzJob job
     * @return 是否成功
     */
    public Boolean add(QuartzJob quartzJob) {
        boolean result = remoteAddJob(quartzJob);
        if (result) {
            addOrUpdateJobMonitor(quartzJob);
        }
        return result;
    }

    /**
     * 添加任务监控
     * @param quartzJob job
     */
    public void addOrUpdateJobMonitor(QuartzJob quartzJob) {
        JobMonitor jobMonitor = new JobMonitor();
        jobMonitor.setClusterName(quartzJob.getClusterName());
        jobMonitor.setInstanceName(QuartzClusterService.getCacheMap().get(quartzJob.getClusterName()).getInstanceName());
        jobMonitor.setJobName(quartzJob.getJobName());
        jobMonitor.setAlarmContactsIds(quartzJob.getAlarmContactsIds());
        jobMonitor.setMaxExecuteTime(quartzJob.getMaxExecuteTime() == null ? 0 : quartzJob.getMaxExecuteTime());
        jobMonitor.setCreateTime(new Date());
        jobMonitorDao.insertOrUpdateJobMonitor(jobMonitor);
    }

    /**
     * 编辑任务
     * @param quartzJob job
     * @return 是否成功
     */
    public Boolean edit(QuartzJob quartzJob) {
        boolean result = remoteEditJob(quartzJob);
        if (result) {
            addOrUpdateJobMonitor(quartzJob);
        }
        return result;
    }

    /**
     * 删除任务
     * @param quartzJob job
     * @return 是否成功
     */
    public Boolean delete(QuartzJob quartzJob) {
        QuartzJob deletedJob = findOne(quartzJob.getClusterName(), quartzJob.getJobName());
        boolean result = remoteDeleteJob(quartzJob);
        if (result && deletedJob != null) {
            deletedJob.setClusterName(quartzJob.getClusterName());
            deleteJobMonitor(quartzJob);
            addDeletedJob(deletedJob);
        }
        return result;
    }

    /**
     * 删除任务监控
     * @param quartzJob job
     */
    public void deleteJobMonitor(QuartzJob quartzJob) {
        JobMonitor jobMonitor = new JobMonitor();
        jobMonitor.setClusterName(quartzJob.getClusterName());
        jobMonitor.setInstanceName(QuartzClusterService.getCacheMap().get(quartzJob.getClusterName()).getInstanceName());
        jobMonitor.setJobName(quartzJob.getJobName());
        jobMonitorDao.deleteJobMonitor(jobMonitor);
    }

    /**
     * 添加已删除的任务
     * @param job 任务
     */
    public void addDeletedJob(QuartzJob job) {
        DeletedJob deletedJob = new DeletedJob();
        deletedJob.setInstanceName(job.getSchedName());
        deletedJob.setClusterName(job.getClusterName());
        deletedJob.setJobName(job.getJobName());
        deletedJob.setJobDesc(job.getDescription());
        deletedJob.setTriggerType(job.getTriggerType());
        deletedJob.setTriggerExpression(job.getTriggerType().equals("SIMPLE") ? String.valueOf(job.getRepeatInterval() / 1000) : job.getCronExpression());
        deletedJobDao.insertDeletedJob(deletedJob);

    }

    /**
     * 暂停任务
     * @param quartzJob job
     * @return 是否成功
     */
    public Boolean pause(QuartzJob quartzJob) {
        return remotePauseJob(quartzJob);
    }

    /**
     * 恢复任务
     * @param quartzJob job
     * @return 是否成功
     */
    public Boolean recover(QuartzJob quartzJob) {
        return remoteRecoverJob(quartzJob);
    }

    /**
     * 立即执行任务
     * @param quartzJob job
     * @return 是否成功
     */
    public Boolean execute(QuartzJob quartzJob) {
        return remoteExecuteJob(quartzJob);
    }

    /**
     * 远程编辑任务
     * @param job 任务
     * @return 是否成功
     */
    public boolean remoteEditJob(QuartzJob job) {
        Map<String, String> parameterMap = new HashMap<>();
        handleParams(parameterMap, job);
        return remoteRequestResult(job.getClusterName(), "/manage/modify", parameterMap);
    }

    /**
     * 远程删除任务
     * @param job 任务
     * @return 是否成功
     */
    public boolean remoteDeleteJob(QuartzJob job) {
        Map<String, String> parameterMap = new HashMap<>();
        handleParams(parameterMap, job);
        return remoteRequestResult(job.getClusterName(), "/manage/remove", parameterMap);
    }

    /**
     * 远程暂停任务
     * @param job 任务
     * @return 是否成功
     */
    public boolean remotePauseJob(QuartzJob job) {
        Map<String, String> parameterMap = new HashMap<>();
        handleParams(parameterMap, job);
        return remoteRequestResult(job.getClusterName(), "/manage/pause", parameterMap);
    }

    /**
     * 远程恢复任务
     * @param job 任务
     * @return 是否成功
     */
    public boolean remoteRecoverJob(QuartzJob job) {
        Map<String, String> parameterMap = new HashMap<>();
        handleParams(parameterMap, job);
        return remoteRequestResult(job.getClusterName(), "/manage/resume", parameterMap);
    }

    /**
     * 远程执行任务
     * @param job 任务
     * @return 是否成功
     */
    public boolean remoteExecuteJob(QuartzJob job) {
        Map<String, String> parameterMap = new HashMap<>();
        handleParams(parameterMap, job);
        return remoteRequestResult(job.getClusterName(), "/manage/run", parameterMap);
    }

    /**
     * 远程添加任务
     * @param job 任务
     * @return 是否成功
     */
    public boolean remoteAddJob(QuartzJob job) {
        Map<String, String> parameterMap = new HashMap<>();
        handleParams(parameterMap, job);
        return remoteRequestResult(job.getClusterName(), "/manage/add", parameterMap);
    }

    /**
     * 处理参数
     * @param parameterMap 参数集合
     * @param job 任务
     */
    private void handleParams(Map<String, String> parameterMap, QuartzJob job) {
        if (job.getJobName() != null) {
            parameterMap.put("jobName", job.getJobName());
        }
        if (job.getDescription() != null) {
            parameterMap.put("jobDesc", job.getDescription());
        }
        if (job.getTriggerType() != null) {
            parameterMap.put("triggerType", job.getTriggerType());
        }
        if (job.getTriggerExpression() != null) {
            parameterMap.put("triggerExpression", job.getTriggerExpression());
        }
    }

    /**
     * 远程请求
     * @param clusterName 集群名
     * @param path 路径
     * @param parameterMap 参数集合
     * @return 是否成功
     */
    public boolean remoteRequestResult(String clusterName, String path, Map<String, String> parameterMap) {
        for (int i = 0; i < 3; i++) {
            CloseableHttpResponse response = null;
            try {
                URIBuilder builder = new URIBuilder();
                builder.setScheme("http").setHost(QuartzClusterService.getCacheMap().get(clusterName).getRemoteNodeHost()).setPath(path);
                parameterMap.forEach((key, value) ->
                        builder.addParameter(key, value)
                );
                HttpGet get = new HttpGet(builder.build());
                response = HttpUtil.getHttpClient().execute(get);
                String responseText = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = JSONObject.parseObject(responseText);
                if (jsonObject.get("code").equals(0) && jsonObject.get("data").equals(0)) {
                    return true;
                }
                Thread.sleep(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                HttpUtil.closeQuietly(response);
            }
        }
        return false;
    }

    /**
     * 已删除的任务集合
     * @param clusterName 集群名
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @return 已删除的任务集合
     */
    public List<DeletedJob> deletedJobList(String clusterName, Integer pageIndex, Integer pageSize) {
        Integer beginIndex = (pageIndex - 1) * pageSize;
        return deletedJobDao.list(clusterName, beginIndex, pageSize);
    }

    /**
     * 已删除的任务总数
     * @param clusterName 集群名
     * @return 已删除的任务总数
     */
    public int deletedJobCount(String clusterName) {
        return deletedJobDao.count(clusterName);
    }

}

