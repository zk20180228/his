package cn.honry.inner.mobile.dbFile.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
public class PingConnectUtil {
	/** 配置文件地址 **/
	public static final String path = Thread.currentThread().getContextClassLoader().getResource("jdbc.properties").getPath();
	/** 文件上传访问地址 **/
	public static final String INFILEUPLOADURL;
	public static final String OUTFILEUPLOADURL;
//	public static final String MASTERNGINXIP;
	static{
		Properties pps = new Properties();
		try {
			pps.load(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		INFILEUPLOADURL = pps.getProperty("infileUploadUrl");
		OUTFILEUPLOADURL = pps.getProperty("outfileUploadUrl");
//		MASTERNGINXIP= pps.getProperty("masternginxip");
		System.out.println("\n\n内网代理服务器地址：INFILEUPLOADURL="+INFILEUPLOADURL);
		System.out.println("\n\n内网代理服务器地址：OUTFILEUPLOADURL="+OUTFILEUPLOADURL);
	}
	 public static String ping(String ipAddress, int timeOut) {  
	        BufferedReader in = null;  
	        Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是windows格式的命令  
	        String pingCommand = "ping " + ipAddress + " -w " + timeOut;  //w 超时时间
	        try {   
	        	// 执行命令并获取输出  
	            Process p = r.exec(pingCommand);   
	            if (p == null) {    
	                return "false";   
	            }
	            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数  
	            String line = null;  
	            Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);  
		        Matcher matcher = null;  
	            while((line = in.readLine()) != null) {
	            	matcher = pattern.matcher(line);
	                if(matcher.find()){
	                	return "true";
	                }
	            } 
	            //不匹配
	            return "false";
	        } catch (Exception ex) {   
	            ex.printStackTrace();   // 出现异常则返回假  
	            return "flase";  
	        } finally {   
	            try {    
	                in.close();   
	            } catch (IOException e) {    
	                e.printStackTrace();   
	            }  
	        }
	    }
	 
	 public static String getfileServerIP(HttpServletRequest request){
		    String outIP="0000000000"; 
		    String innerIP="111111111"; 
		    //外网ip
		    if(OUTFILEUPLOADURL.replace("http://", "").contains(":")){//有端口
		    	outIP=OUTFILEUPLOADURL.replace("http://", "");
		    	outIP=outIP.substring(0, outIP.indexOf(":"));
			}else{//无端口
				outIP=OUTFILEUPLOADURL.replace("http://", "");
				outIP=outIP.substring(0, outIP.indexOf("/"));
			}	
		    //内网ip
		    if(INFILEUPLOADURL.replace("http://", "").contains(":")){//有端口
		    	innerIP=INFILEUPLOADURL.replace("http://", "");
		    	innerIP=innerIP.substring(0, innerIP.indexOf(":"));
			}else{//无端口
				innerIP=INFILEUPLOADURL.replace("http://", "");
				innerIP=innerIP.substring(0, innerIP.indexOf("/"));
			}
		    //内网ip
		    if(request!=null){
				String remoteAddr=request.getHeader("Referer")!=null?request.getHeader("Referer").trim():"";
				System.out.println();
				System.out.println("#############################");
				System.out.println("request.getHeader(Referer)="+remoteAddr);
				System.out.println("#############################");
				System.out.println();
				if(remoteAddr!=null&&remoteAddr.contains(outIP)){
					System.out.println();
					System.out.println();
					System.out.println("return fileserviceIP="+outIP);
					System.out.println();
					System.out.println();
					return outIP;
				}
			}
			System.out.println();
			System.out.println();
			System.out.println("return fileserviceIP="+innerIP);
			System.out.println();
			System.out.println();
			return innerIP;
		}
	 
	 public static void main(String[] args) {
		System.out.println(ping("192.168.0,113", 200)); 
	}
	 

}
