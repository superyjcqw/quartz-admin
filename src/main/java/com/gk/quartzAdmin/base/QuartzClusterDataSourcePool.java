package com.gk.quartzAdmin.base;

import com.gk.quartzAdmin.entity.QuartzCluster;
import com.gk.quartzAdmin.service.QuartzClusterService;
import com.google.common.collect.ImmutableMap;
import com.mysql.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * quartz集群数据源池
 * Date:  17/7/12 上午10:21
 */
@Service
public class QuartzClusterDataSourcePool {

    private static final Logger logger = LoggerFactory.getLogger(QuartzClusterDataSourcePool.class);

    /**
     * 所有集群的数据源的对象
     */
    private static Map<String, DataSource> dataSourceCache = new HashMap<String, DataSource>();


    @Autowired
    private QuartzClusterService quartzClusterService;


    /**
     * 启动时初始化数据源至缓存
     */
    public void init() {
        if (logger.isInfoEnabled()) {
            logger.info("开始初始化DataSourcePool");
        }
        List<QuartzCluster> quartzClusters = QuartzClusterService.getCacheList();
        if (!CollectionUtils.isEmpty(quartzClusters)) {
            quartzClusters.forEach(quartzCluster ->
                    addOrUpdateDataSourcePool(quartzCluster.getName(), quartzCluster.getDataSource())
            );
        }
        if (logger.isInfoEnabled()) {
            logger.info("初始化DataSourcePool结束");
        }
    }

    /**
     * 添加或更新到数据源池
     * @param name
     * @param dataSource
     */
    public void addOrUpdateDataSourcePool(String name, String dataSource) {
        Map<String, String> dbConfigMap = analysisDbConfig(dataSource);
        if (dbConfigMap != null) {
            dataSourceCache.put(name, generateDataSourceByConfig(dbConfigMap));
        }
    }

    /**
     * 删除数据源
     * @param name
     */
    public void delete(String name) {
        dataSourceCache.remove(name);
    }

    /**
     * 刷新数据源
     */
    public void refreshCache() {
        dataSourceCache.clear();
        if (logger.isInfoEnabled()) {
            logger.info("清空DataSourcePool");
        }
        this.init();
    }

    /**
     * 生成数据源
     * @param dbConfig 配置
     * @return 数据源
     */
    private SimpleDriverDataSource generateDataSourceByConfig(Map<String, String> dbConfig) {
        try {
            return new SimpleDriverDataSource(
                    new Driver(),
                    dbConfig.get("url"),
                    dbConfig.get("username"),
                    dbConfig.get("password")

            );
        } catch (SQLException e) {
            logger.error("创建数据源失败！msg : {}", e.getMessage());
        }
        return null;
    }

    /**
     * 分析数据源
     * @param dataSource 数据源连接串
     * @return map
     */
    private Map<String, String> analysisDbConfig(String dataSource) {
        try {
            if (StringUtils.isEmpty(dataSource)) {
                return null;
            }
            String[] dataSourceConfigs = dataSource.split("\n");
            ImmutableMap.Builder<String, String> stringObjectBuilder = new ImmutableMap.Builder<String, String>();
            for (String dataSourceConfig : dataSourceConfigs) {
                dataSourceConfig = dataSourceConfig.replaceAll("\r", "");
                String[] split = dataSourceConfig.trim().split("=");
                stringObjectBuilder.put(split[0], split[1]);

            }
            return stringObjectBuilder.build();
        } catch (Exception e) {
            logger.info("数据源配置格式不正确！");
            return null;
        }
    }

    /**
     * 获取JdbcTemplate
     * @param name
     * @return
     */
    public JdbcTemplate getJdbcTemplate(String name) {
        SimpleDriverDataSource simpleDriverDataSource = (SimpleDriverDataSource) dataSourceCache.get(name);
        if (simpleDriverDataSource == null) {
            QuartzCluster quartzCluster = quartzClusterService.findDetailByName(name);
            if (quartzCluster != null) {
                addOrUpdateDataSourcePool(quartzCluster.getName(), quartzCluster.getDataSource());
                simpleDriverDataSource = (SimpleDriverDataSource) dataSourceCache.get(name);
            }
        }
        return new JdbcTemplate(simpleDriverDataSource);
    }
}
