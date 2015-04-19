package cn.imqiba.watch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import cn.imqiba.account.AccountEight;
import cn.imqiba.account.IAccount;
import cn.imqiba.config.ServerConfig;
import redis.clients.jedis.Jedis;

public class WatchAccountTimer extends TimerTask
{
	private Logger logger = Logger.getLogger(Class.class);
	
	protected String m_host = null;
	protected int m_port = 0;
	
	public WatchAccountTimer(String host, int port)
	{
		m_host = host;
		m_port = port;
	}
	
	@Override
	public void run()
	{
		ServerConfig serverConfig = new ServerConfig();
		try
		{
			serverConfig.parser(System.getProperty("user.dir") + "/server_config.xml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String dbUri = serverConfig.getServerUri() + "/" + serverConfig.getDBName();
		String dbUser = serverConfig.getDBUser();
		String dbPasswd = serverConfig.getDBPasswd();
		
		Connection conn = null;
		Statement stmt = null;
		
		Jedis redis = new Jedis(m_host, m_port);
		redis.connect();
		
		if(redis.isConnected())
		{
			long accountCount = redis.llen("account:pool");
			logger.info("account count = " + accountCount);
			if(accountCount < 10)
			{
				long startAccount = 0;
				long stopAccount = 0;
				int uin = 0;
				
				try
				{
					Class.forName("org.postgresql.Driver");
					
					conn = DriverManager.getConnection(dbUri, dbUser, dbPasswd);
					stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					
					String queryAccountSql = "select account_pos, account_digit from account_record where account_digit = 'eight'";
					ResultSet result = stmt.executeQuery(queryAccountSql);
					result.next();
					
					String accountPosStr = result.getString("account_pos");
					startAccount = Long.parseLong(accountPosStr);
					stopAccount = startAccount + serverConfig.getMakeCount();
					
					result.updateInt("account_pos", (int)startAccount + serverConfig.getMakeCount());
					result.updateRow();
					
					String queryUinSql = "select uin_pos from uin_record";
					result = stmt.executeQuery(queryUinSql);
					result.next();
					String uinPosStr = result.getString("uin_pos");
					uin = Integer.parseInt(uinPosStr);
					
					stmt.close();
					conn.close();
				}
				catch( Exception e )
				{
					logger.error( e.getClass().getName()+" : "+ e.getMessage() );
					redis.close();
					return;
				}
				
				IAccount accountMaker = new AccountEight();
				
				long wantAccountCount = stopAccount - startAccount;
				long arrNormalAccount[] = new long[(int)wantAccountCount];
				long arrPrettyAccount[] = new long[(int)wantAccountCount];
				String arrMatchMode[] = new String[(int)wantAccountCount];
				
				accountMaker.MakeAccount(startAccount, stopAccount, arrNormalAccount, arrPrettyAccount, arrMatchMode);
				
				List<String> accountList = new ArrayList<String>();
				for(int i = 0; i < arrNormalAccount.length; ++i)
				{
					if(arrNormalAccount[i] <= 0)
					{
						logger.info("make normal account = " + i);
						break;
					}
					
					accountList.add(arrNormalAccount[i] + ":" + (++uin));
				}
				
				try
				{
					Class.forName("org.postgresql.Driver");
					
					conn = DriverManager.getConnection(dbUri, dbUser, dbPasswd);
					stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					
					String updateUinSql = "update uin_record set uin_pos = " + uin;
					stmt.execute(updateUinSql);
					
					for(int i = 0; i < arrPrettyAccount.length; ++i)
					{
						if(arrPrettyAccount[i] <= 0)
						{
							break;
						}
						String insertPrettySql = "insert into account_pretty_eight values(DEFAULT, " + arrPrettyAccount[i] + ", '" + arrMatchMode[i] + "')";
						logger.info("account_pretty_eight = " + arrPrettyAccount[i] + ", matchmode = " + arrMatchMode[i]);
						stmt.executeUpdate(insertPrettySql);
					}
					
					stmt.close();
					conn.close();
				}
				catch( Exception e )
				{
					logger.error( e.getClass().getName()+" : "+ e.getMessage() );
					redis.close();
					return;
				}
				
				for(String account : accountList)
				{
					redis.rpush("account:pool", account);
				}
			}
		}
		
		redis.close();
	}

}
