package cn.imqiba.main;

import java.util.List;
import java.util.Timer;

import cn.imqiba.config.RedisConfig;
import cn.imqiba.watch.WatchAccountTimer;

public class MakeAccount {

	public static void main(String[] args) {
		RedisConfig redisConfig = new RedisConfig();
		try
		{
			redisConfig.parser(System.getProperty("user.dir") + "/redis_config.xml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		Timer timer = new Timer();
		List<RedisConfig.RedisServerInfo> redisList = redisConfig.getRedisList();
		for(RedisConfig.RedisServerInfo info : redisList)
		{
			WatchAccountTimer accountTimer = new WatchAccountTimer(info.m_serverAddress, info.m_serverPort);
			timer.schedule(accountTimer, 0, 60 * 1000);
		}
	}

}
