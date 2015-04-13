package cn.imqiba.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class ServerConfig {
	protected String m_serverUri = null;
	protected String m_dbName = null;
	protected String m_dbUser = null;
	protected String m_dbPasswd = null;
	protected int m_makeCount = 10000;
	private Logger logger = Logger.getLogger(Class.class);
	
	public String getServerUri()
	{
		return m_serverUri;
	}
	
	public String getDBName()
	{
		return m_dbName;
	}
	
	public String getDBUser()
	{
		return m_dbUser;
	}
	
	public String getDBPasswd()
	{
		return m_dbPasswd;
	}
	
	public int getMakeCount()
	{
		return m_makeCount;
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
			Element node = server.element("node");
			
			m_serverUri = node.attributeValue("server_uri");
			m_dbName = node.attributeValue("db_name");
			m_dbUser = node.attributeValue("db_user");
			m_dbPasswd = node.attributeValue("db_passwd");
			
			Element account = server.element("account");
			m_makeCount = Integer.parseInt(account.attributeValue("make_count"));
		}
		catch(DocumentException e)
		{
			logger.error( e.getClass().getName()+" : "+ e.getMessage() );
			return false;
		}
		
		return true;
	}
}
