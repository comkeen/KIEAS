package kr.ac.uos.ai.ieas.db.dbDriver;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class DataTransaction
{
	private static final String username = "root";
	private static final String password = "dlsrhdwlsmd";
	private static final String url = "jdbc:mysql://127.0.0.1:3306/capkorean?useUnicode=true& useUnicode=true&characterEncoding=euc_kr";
	public Connection connection = null;
	public static int connectionCount = 0;

	public DataTransaction(boolean setCon)
	{
		try
		{
			setConnectionTest();
		}
		catch (Exception e)
		{
			System.out.println("Error in Connection:" + e.toString());
		}
	}

	public static BasicDataSource dataSource;

	public void setConnectionTest() throws SQLException
	{
		try
		{
			if (dataSource == null)
			{
				dataSource = new BasicDataSource();
				String driver = "com.mysql.jdbc.Driver";                
				try
				{
					dataSource.setDriverClassName(driver);
					dataSource.setUrl(url);
					dataSource.setUsername(username);
					dataSource.setPassword(password);
					dataSource.setMaxIdle(10);                    
					if (connection == null || connection.isClosed()) 
					{
						System.out.println(" requeition CONNECTION WITH FIRST SERVER.");
						connection = dataSource.getConnection();
						connectionCount++;
					}
				}
				catch (SQLException e)
				{
					System.out.println("***Connection Requisition*** Could not connect to the database msg :" + e.getMessage());
				}
			}
			else
			{
				connection = dataSource.getConnection();
				connectionCount++;
			}
		}
		catch (Exception e)
		{
			System.out.println("open connection exception" + e);
		}
	}
}
