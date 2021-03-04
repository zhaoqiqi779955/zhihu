package utils.database;


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Administrator on 2017/6/24.
 */
public class JDBCUtil {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;
//静态代码块 初始化配置文件信息

  static {
        try {
        ClassLoader classLoader = JDBCUtil.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("db.propertise");
        Properties properties=new Properties();
            properties.load(resourceAsStream);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        //注册驱动，获取连接
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
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
