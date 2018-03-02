package cn.honry.hiasMongo.basic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoManager {
		private static MongoClient mongoClient = null;  
	
		private static MongoDatabase mongoDatabase = null;  
	    private static DB DBDatabase = null;  
		//类初始化时，自行实例化，饿汉式单例模式
	 	private static final MongoManager instance = new MongoManager();  
	  
		public static MongoDatabase getMongoDatabase() {
			return mongoDatabase;
		}

		public static void setMongoDatabase(MongoDatabase mongoDatabase) {
			MongoManager.mongoDatabase = mongoDatabase;
		}

		public static DB getDBDatabase() {
			return DBDatabase;
		}

		public static void setDBDatabase(DB dBDatabase) {
			DBDatabase = dBDatabase;
		}



		private Logger logger=Logger.getLogger(MongoManager.class);
		private DBCollection mongoCollection = null;
		private Properties prop = new Properties();
		private String replicaSetName;
		private String hostPrimary;//IP地址-主节点
		private int primaryPort;//端口号-主节点
		private String hostSecondary;//IP地址-备节点
		private int secondaryPort;//端口号-备节点
		private String hostArbiter;//IP地址-仲裁节点
		private int arbiterPort;//端口号-仲裁节点
		private String databaseName;//数据库名
		private int connectionsPerHost;//每个主机的连接数
		private int threadsAllowedToBlockForConnectionMultiplier;//线程队列数，它以上面connectionsPerHost值相乘的结果就是线程队列最大值。如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
		private int connectTimeout;//连接超时的毫秒。0是默认和无限
		private int maxWaitTime;//最大等待连接的线程阻塞时间 
		private int socketTimeout;//socket超时。0是默认和无限
		private boolean autoConnectRetry;//这个控制是否在一个连接时，系统会自动重试
		private boolean socketKeepAlive;//是否保持长链接
		
		/**
		 * 
		 * @author zxh
		 * @time 2017年3月24日
		 * 配置连接参数
		 * @return
		 */
		private MongoClientOptions getConfOptions() {
			return new MongoClientOptions.Builder().socketKeepAlive(socketKeepAlive) // 是否保持长链接
			.connectTimeout(connectTimeout) // 链接超时时间
			.socketTimeout(socketTimeout) // read数据超时时间
//			.autoConnectRetry(autoConnectRetry) // 是否重试机制
			.connectionsPerHost(connectionsPerHost) // 每个地址最大请求数
			.maxWaitTime(maxWaitTime) // 长链接的最大等待时间
			.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier) // 一个socket最大的等待请求数
			.build();
			}
		
		private MongoManager() {
			//mongodb连接参数。从配置文件中读取
			try{
		        if (mongoCollection == null) {
		        	FileInputStream in = null;   
		        	String path = Thread.currentThread().getContextClassLoader().getResource("mongo.properties").getPath();
					in = new FileInputStream(path);
					prop.load(in);
					replicaSetName = prop.getProperty("replicaSetName");//暂时无用到
					
					hostPrimary = prop.getProperty("hostPrimary");
					String mongoPortPrimary = prop.getProperty("primaryPort");
					primaryPort = Integer.parseInt(mongoPortPrimary);
					
					hostSecondary = prop.getProperty("hostSecondary");
					String mongoPortSecondary = prop.getProperty("secondaryPort");
					secondaryPort = Integer.parseInt(mongoPortSecondary);
					
					hostArbiter = prop.getProperty("hostArbiter");
					String mongoPorttArbiter = prop.getProperty("arbiterPort");
					arbiterPort = Integer.parseInt(mongoPorttArbiter);
					
					databaseName= prop.getProperty("databaseName");
					
					String mongoConPerHost = prop.getProperty("connectionsPerHost");
					connectionsPerHost = Integer.parseInt(mongoConPerHost);
					String mongoConnMultiplier = prop.getProperty("threadsAllowedToBlockForConnectionMultiplier");
					threadsAllowedToBlockForConnectionMultiplier = Integer.parseInt(mongoConnMultiplier);
					String mongoConnTimeOut = prop.getProperty("connectTimeout");
					connectTimeout = Integer.parseInt(mongoConnTimeOut);
					String mongoMaxWait = prop.getProperty("maxWaitTime");
					maxWaitTime = Integer.parseInt(mongoMaxWait);
					String mongoAutoConnTry = prop.getProperty("autoConnectRetry");
					if(mongoAutoConnTry.equals("0")){
						autoConnectRetry=true;
					}
					String mongoSocketKeep = prop.getProperty("socketKeepAlive");
					if(mongoSocketKeep.equals("0")){
						socketKeepAlive=true;
					}
					String mongoSocketTimeOut = prop.getProperty("socketTimeout");
					socketTimeout = Integer.parseInt(mongoSocketTimeOut);
					
					//将参数配置进连接池
					/*if (mongoCollection == null) {
						mongoClient = new MongoClient(new ServerAddress(host, port), getConfOptions());
					}
					return mongoClient;*/
					//hedong 20170331 改用集群Replica Set的方式
					ServerAddress dbAddrPrimary = new ServerAddress(hostPrimary,primaryPort);
			        ServerAddress dbAddrSencodary = new ServerAddress(hostSecondary,secondaryPort);
			        ServerAddress dbAddrArbiter = new ServerAddress(hostArbiter,arbiterPort);
			        
			        List<ServerAddress> serverAddrs = new ArrayList<ServerAddress>();
			        serverAddrs.add(dbAddrPrimary);
			        serverAddrs.add(dbAddrSencodary);
			        serverAddrs.add(dbAddrArbiter);
			        System.out.println("----"+serverAddrs);
		        	mongoClient = new MongoClient(serverAddrs, getConfOptions());
		        }
		        mongoDatabase = mongoClient.getDatabase(databaseName);
		        DBDatabase = mongoClient.getDB(databaseName);
			} catch (FileNotFoundException e) {
				logger.error("mongodb:MongoManager()", e);
			} catch (IOException e) {
				logger.error("mongodb:MongoManager()", e);
			}
		}
	 	
		public static MongoManager getInstance() {  
			return instance;  
		}  
}
