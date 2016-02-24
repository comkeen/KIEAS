package kr.ac.uos.ai.ieas.db.dbHandler;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasAddress;


public class DataTransaction
{
	private static final String username = "root";
	private static final String password = "dlsrhdwlsmd";
	private static final String urlPostfix = "/capkorean?useUnicode=true& useUnicode=true&characterEncoding=euc_kr";
	public Connection connection = null;
	public static int connectionCount = 0;

	private boolean bool;
	
	public DataTransaction(CAPDBUtils capdbUtils)
	{
		try
		{
			this.bool = setConnectionTest();
		}
		catch (Exception e)
		{
			System.out.println("Error in Connection:" + e.toString());
			capdbUtils.connectionFail();
		}
		System.out.println("DB connection : " + bool);
	}

	public DataTransaction(boolean b) {
		// TODO Auto-generated constructor stub
	}

	public static BasicDataSource dataSource;

	public boolean setConnectionTest() throws SQLException
	{
		System.out.println("db connection start");
		String url = KieasAddress.DATABASE_SERVER_IP_LOCAL + urlPostfix;
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
					return false;
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
			return false;
		}
		return true;
	}
}
