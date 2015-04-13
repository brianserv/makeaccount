package cn.imqiba.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class RedisConfig
{
	private Logger logger = Logger.getLogger(Class.class);
	
	public RedisConfig()
	{
	}
	
	public class RedisServerInfo
	{
		public String m_serverAddress;
		public int m_serverPort;
	}

	protected List<RedisServerInfo> m_redisList = new ArrayList<RedisServerInfo>();
	
	public List<RedisServerInfo> getRedisList()
	{
		return m_redisList;
	}
	
	public boolean parser(String path) throws Exception
	{
		SAXReader reader = new SAXReader();
		InputStream stream = null;
		try
		{
			stream = new FileInputStream(path);
		} 
		catch (FileNotFoundException e)
		{
			logger.error( e.getClass().getName()+" : "+ e.getMessage() );
			return false;
		}
		
		try
		{
			Document doc = reader.read(stream);
			//获取根结点
			Element server = doc.getRootElement();
			
			List<?> nodeList = server.elements("node");
			for(Object obj : nodeList)
			{
				Element node = (Element)obj;
				
				RedisServerInfo redisServerInfo = new RedisServerInfo();
				redisServerInfo.m_serverAddress = node.attributeValue("server_address");
				redisServerInfo.m_serverPort = Integer.parseInt(node.attributeValue("server_port"));
				
				m_redisList.add(redisServerInfo);
			}
				
		}
		catch(DocumentException e)
		{
			logger.error( e.getClass().getName()+" : "+ e.getMessage() );
			return false;
		}
		
		return true;
	}
}
