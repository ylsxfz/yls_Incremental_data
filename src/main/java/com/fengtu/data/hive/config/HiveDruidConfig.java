package com.fengtu.data.hive.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

/**
 * -配置hive数据源
 * @author yls
 * @Date 2019年10月12日
 *
 */
@Configuration
public class HiveDruidConfig {
 
	    private static final Logger LOGGER = LoggerFactory.getLogger(HiveDruidConfig.class);
	    
	    @Autowired
	    private HiveDataSourceProperties hiveDataSourceProperties;

	    @Value("${hiveconf-list}")
		private String hiveconfList;
 
	    @Bean("hiveDruidDataSource") //新建bean实例
	    @Qualifier("hiveDruidDataSource")//标识
	    public DataSource dataSource(){
	    	LOGGER.error(hiveconfList.toString());
	    	System.err.println(hiveconfList);

	    	hiveconfList = hiveconfList.replaceAll(" ","").trim();


	        DruidDataSource datasource = new DruidDataSource();
 
	        //配置数据源属性
	        datasource.setUrl(hiveDataSourceProperties.getUrl()+"?"+hiveconfList);
	        datasource.setUsername(hiveDataSourceProperties.getUsername());
	        datasource.setPassword(hiveDataSourceProperties.getPassword());
	        datasource.setDriverClassName(hiveDataSourceProperties.getDrivceClassName());
	        return datasource;
	    }
	    
}