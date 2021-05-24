package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

/****
 * 操作数据库连接对象的工具类 
 */

public class ConnUtil {
	private static DataSource ds;
	//这是一个每个用户都有的一个保存Connection对象的容器
	private static ThreadLocal<Connection> local = new ThreadLocal<>();	
	static {//静态代码块，特点：只运行一次。
		try {
			// 加载资源文件信息的文件对象
			Properties prop = new Properties();
			// 从mysql5.properties文件中加载信息
			prop.load(ConnUtil.class.getResourceAsStream("/mysql5.properties"));
			// 使用加载的信息创建数据源对象
			ds = BasicDataSourceFactory.createDataSource(prop);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 获得一个数据库连接对象的方法
	 * @return 连接对象
	 * @throws SQLException 
	 */
	public static Connection getConn() throws SQLException {
		Connection conn = local.get();	//从容器中取出连接对象
		if(conn == null || conn.isClosed()) {
			conn = ds.getConnection();
			local.set(conn);
		}
		return conn;
	}
	
	/***
	 * 关闭数据	库连接对象的方法
	 */
	public static void closeConn() {
		Connection conn = local.get();
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();	//释放连接
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			conn = null;
			local.set(null);
		}
	}
}
