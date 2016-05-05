package com.alipay.web.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class BasicDao {
	// 持有一个静态的数据库连接池对象
	private static DataSource DS;
	
	static{
		initDataSource("jdbc:mysql://192.168.1.13:3306/xztrip?useUnicode=true&characterEncoding=utf-8", "root", "1234", "com.mysql.jdbc.Driver", 5, 5, 5, 2000);
	}

	// 使用DBCP提供的BasicDataSource实现DataSource接口
	public static void initDataSource(String connectURI, String username,
			String password, String driverClass, int initialSize,
			int maxActive, int maxIdle, int maxWait) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClass);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(connectURI);
		ds.setInitialSize(initialSize);
		ds.setMaxActive(maxActive);
		ds.setMaxIdle(maxIdle);
		ds.setMaxWait(maxWait);
		DS = ds;
	}

	// 获得一个数据库连接
	public static Connection getConnection() {

		Connection con = null;
		if (DS != null) {
			try {
				con = DS.getConnection();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			// 将数据库连接的事物设置为不默认为自动Commit
			try {
				con.setAutoCommit(false);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			return con;
		}
		// 回收数据库连接时，直接使用con.close()即可
		return con;

	}

	// 回收数据库连接
	protected static void shutdownDataSource() throws SQLException {
		BasicDataSource bds = (BasicDataSource) DS;
		bds.close();
	}

}
