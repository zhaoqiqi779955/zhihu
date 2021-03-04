package utils.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** 使用JDBC + C3P0 访问 MySQL 
 * 
 * @author zhaoqiqi
 *
 */
public class JdbcPool {
	public static ComboPooledDataSource c3p0;

	static {
		c3p0 = new ComboPooledDataSource();
	}

	/**
	 * 从连接池中获取数据库连接
	 */
	public static Connection getConnection() throws Exception {
		Connection conn = c3p0.getConnection();
		return conn;
	}

	/**
	 * 关闭连接池
	 */
	public static void destroy() {
		c3p0.close();
	}

	public static void release(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		//释放资源

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}

