package com.gk.quartzAdmin.service;

import com.gk.quartzAdmin.dao.DispatchLogDao;
import com.gk.quartzAdmin.entity.DispatchLog;
import com.gk.quartzAdmin.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 调式日志Service
 * Date:  17/7/23 下午3:28
 */
@Service
public class DispatchLogService {

    @Autowired
    private DispatchLogDao dispatchLogDao;

    /**
     * 调度日志分页集合
     * @param clusterName 集群名
     * @param jobName 任务名
     * @param status 状态
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @return 调度日志分页集合
     */
    public List<DispatchLog> list(String clusterName, String jobName, Integer status, Integer pageIndex, Integer pageSize) {
        Integer beginIndex = (pageIndex - 1) * pageSize;
        List<DispatchLog> list = dispatchLogDao.list(clusterName, jobName, status, beginIndex, pageSize);
        list.forEach(log -> {
            log.setBeginTimeStr(DateUtil.format(new Date(log.getBeginTime()), DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
            log.setEndTimeStr(DateUtil.format(new Date(log.getEndTime()), DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
            if (log.getExceptionNoticeStatus() != null) {
                log.setExceptionNoticeStatusStr(log.getExceptionNoticeStatus().equals(DispatchLog.EXCEPTION_NOTICE_STATUS_NO) ? "未通知" : "已通知");
            }
            log.setStatusStr(DispatchLog.statusStrMap.get(log.getStatus()));
        });
        return list;
    }

    /**
     * 调度日志总数
     * @param clusterName 集群名
     * @param jobName 任务名
     * @param status 状态
     * @return 调度日志总数
     */
    public Integer count(String clusterName, String jobName, Integer status) {
        return dispatchLogDao.count(clusterName, jobName, status);
    }

}

