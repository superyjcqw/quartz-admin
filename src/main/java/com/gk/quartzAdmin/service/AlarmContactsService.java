package com.gk.quartzAdmin.service;

import com.gk.quartzAdmin.dao.AlarmContactsDao;
import com.gk.quartzAdmin.entity.AlarmContacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

/**
 * 报警人Service
 * Date:  17/7/22 下午2:25
 */
@Service
public class AlarmContactsService {

    @Autowired
    private AlarmContactsDao alarmContactsDao;

    private static List<AlarmContacts> cacheAlarmContactsList = new ArrayList<>();
    private static Map<Integer, AlarmContacts> cacheAlarmContactsMap = new HashMap<>();

    /**
     * 初始化
     */
    public void init() {
        cacheAlarmContactsList = alarmContactsDao.listAllEmails();
        Map<Integer, AlarmContacts> alarmContactsMap = new HashMap<>();
        cacheAlarmContactsList.forEach(alarmContacts ->
                alarmContactsMap.put(alarmContacts.getId(), alarmContacts)
        );
        cacheAlarmContactsMap = alarmContactsMap;
    }

    /**
     * 获取邮件集合list
     * @return list
     */
    public static List<AlarmContacts> getCacheAlarmContactsList() {
        return cacheAlarmContactsList;
    }

    /**
     * 获取邮件集合map
     * @return map
     */
    public static Map<Integer, AlarmContacts> getCacheAlarmContactsMap() {
        return cacheAlarmContactsMap;
    }

    /**
     * 报警人分页集合
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @return 报警人分页集合
     */
    public List<AlarmContacts> list(Integer pageIndex, Integer pageSize) {
        Integer beginIndex = (pageIndex - 1) * pageSize;
        return alarmContactsDao.list(beginIndex, pageSize);
    }

    /**
     * 报警人总数
     * @return 报警人总数
     */
    public int count() {
        return alarmContactsDao.count();
    }

    /**
     * 根据id查找报警人
     * @param id 报警人id
     * @return 报警人对象
     */
    public AlarmContacts findById(Integer id) {
        return alarmContactsDao.findById(id);
    }

    /**
     * 删除报警人
     * @param id 报警人id
     */
    public void delete(Integer id) {
        alarmContactsDao.delete(id);
        init();
    }

    /**
     * 修改报警人
     * @param alarmContacts 报警人对象
     */
    public void update(AlarmContacts alarmContacts) {
        alarmContactsDao.update(alarmContacts);
        init();
    }

    /**
     * 添加报警人
     * @param alarmContacts 报警人对象
     */
    public void add(AlarmContacts alarmContacts) {
        alarmContacts.setStatus(AlarmContacts.ALARM_CONTACTS_NORMAL);
        alarmContacts.setCreateTime(new Date());
        alarmContactsDao.insert(alarmContacts);
        init();
    }

    /**
     * 根据ids获取报警人名
     * @param ids 报警人id集合
     * @return 报警人名(逗号隔开)
     */
    public String getAlarmContactsName(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return null;
        }
        String[] idsArray = ids.split(",");
        StringBuilder names = new StringBuilder("");
        for (int i = 0; i < idsArray.length; i++) {
            names.append(AlarmContactsService.getCacheAlarmContactsMap().get(Integer.parseInt(idsArray[i])).getName());
            if (i < idsArray.length - 1) {
                names.append(",");
            }
        }
        return names.toString();
    }

}
