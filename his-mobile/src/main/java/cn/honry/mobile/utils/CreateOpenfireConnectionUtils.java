package cn.honry.mobile.utils;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration.Builder;

import cn.honry.utils.ReaderProperty;

/**
 * 获取openfire连接工具类
 * @author houzq
 *
 * cn.honry.mobile.utils
 * 2017年5月16日
 */
public class CreateOpenfireConnectionUtils {
	
	private static ReaderProperty readerProperty =  new ReaderProperty("config.properties");
	private static AbstractXMPPConnection connection = null;
	//日志文件参数
	private static Logger logger=Logger.getLogger(CreateOpenfireConnectionUtils.class);
	public static AbstractXMPPConnection getOpenfireConnection(){
		try {
			Builder builder = XMPPTCPConnectionConfiguration.builder();
			builder.setSecurityMode(SecurityMode.disabled);
			XMPPTCPConnectionConfiguration configuration = (XMPPTCPConnectionConfiguration)builder
					.setServiceName(readerProperty.getValue_String("openFireServer_serviceName"))
					.setHost(readerProperty.getValue_String("openFireServer_host"))
					.setPort(readerProperty.getValue_int("openFireServer_port")).build();
			connection = new XMPPTCPConnection(configuration);
			connection.connect();
			connection.login(readerProperty.getValue_String("openFireServer_AdminUserName"), readerProperty.getValue_String("openFireServer_AdminPassword"));
		} catch (SmackException | IOException | XMPPException e) {
			System.err.println("获取openfire连接出现异常"+e);
			logger.error(e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return connection;
	}
	public static AbstractXMPPConnection getOpenfireConnection(String username){
		try {
			Builder builder = XMPPTCPConnectionConfiguration.builder();
			builder.setSecurityMode(SecurityMode.disabled);
			XMPPTCPConnectionConfiguration configuration = (XMPPTCPConnectionConfiguration)builder
					.setServiceName(readerProperty.getValue_String("openFireServer_serviceName"))
					.setHost(readerProperty.getValue_String("openFireServer_host"))
					.setPort(readerProperty.getValue_int("openFireServer_port")).build();
			connection = new XMPPTCPConnection(configuration);
			connection.connect();
			connection.login(username,username+"123");
		} catch (SmackException | IOException | XMPPException e) {
			System.err.println("获取openfire连接出现异常"+e);
			logger.error(e);
			e.printStackTrace();
		}
		return connection;
	}
	
	public static void closeOpenfireConnection(AbstractXMPPConnection connection){
		if (connection != null) {
	        try {
	        	if(connection.isConnected()){
	        		connection.disconnect();
	        	}
	        } catch (Exception e) {
	        	System.err.println("关闭openfire连接出现异常"+e);
				logger.error(e);
				e.printStackTrace();
	        }
	    }
	}
	
}
