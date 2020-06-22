package com.fengtu.data.hive.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * -扩展连接池，通用配置属性，可应用到所有数据源
 * @author yls
 * @Date 2019年10月12日
 *
 */
@Configuration
public class HiveDataSourceProperties {

	@Value("${spring.datasource.hive.url}")
	private String url;

	@Value("${spring.datasource.hive.type}")
	private String type;

	@Value("${spring.datasource.hive.username}")
	private String username;

	@Value("${spring.datasource.hive.password}")
	private String password;

	@Value("${spring.datasource.hive.driver-class-name}")
	private String drivceClassName;



	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDrivceClassName() {
		return drivceClassName;
	}

	public void setDrivceClassName(String drivceClassName) {
		this.drivceClassName = drivceClassName;
	}

	@Override
	public String toString() {
		return "HiveDataSourceProperties{" +
				"url='" + url + '\'' +
				", type='" + type + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", drivceClassName='" + drivceClassName + '\'' +
				'}';
	}
}