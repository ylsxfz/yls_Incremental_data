package com.fengtu.data.hive.impl;

import com.fengtu.data.hive.dao.HiveJdbcBaseDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * -测试hive连接
 * @author yls
 * @Date 2019年10月12日
 *
 */
@Repository
public class HiveServiceImpl extends HiveJdbcBaseDaoImpl {
	private final static Logger LOGGER = LoggerFactory.getLogger(HiveJdbcBaseDaoImpl.class);
	/**
	 * 获取hive数据库数据信息
	 * @return
	 */
	public DataSource getDataSource() {
		DataSource dataSource = this.getJdbcTemplate().getDataSource();
		return dataSource;
	}
	
	/**
	 * 测试获取hive数据库数据信息
	 * @return
	 */
	public String test() {
		String sql = "SELECT sname from test limit 1";
		String param = this.getJdbcTemplate().queryForObject(sql,String.class);
		return param;
	}
	
	public boolean uploadDataByHive(String sql ) {
		this.getJdbcTemplate().execute(sql);
		return true;
	}


	public boolean executeHiveSqlByDay(String taskName,String sql){
		try {
			this.getStatement().execute("set mapred.job.name = " + taskName);
			this.getStatement().execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			return false;
		}

	}
	
}