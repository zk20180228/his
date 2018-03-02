package cn.honry.migrate.serviceManagement.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.LoggerFactory;


public class PingIpUtils {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(PingIpUtils.class);
    /**
     * @param ipAddress  ip地址
     * @param pingTimes  次数(一次ping,对方返回的ping的结果的次数)
     * @param timeOut    超时时间 单位ms(ping不通,设置的此次ping结束时间)
     * @return
     */
    public static boolean ping(String ipAddress, int pingTimes, int timeOut,String ststem) {
        BufferedReader in = null;
        String pingCommand = null;
        Runtime r = Runtime.getRuntime();
        String osName = System.getProperty("os.name");
        logger.info("项目所在系统是:" +osName);
        System.out.println("项目所在系统是:" +osName);
        if(osName.contains("Windows")){
            //将要执行的ping命令,此命令是windows格式的命令
            pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;
        }else{
            //将要执行的ping命令,此命令是Linux格式的命令
            //-c:次数,-w:超时时间(单位/ms)  ping -c 10 -w 0.5 192.168.120.206
            pingCommand = "ping " + " -c " + pingTimes + " -w " + timeOut + ipAddress;
        }
        try {
            //执行命令并获取输出
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connectedCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line,osName);
            }
            //如果出现类似=23 ms ttl=64(TTL=64 Windows)这样的字样,出现的次数=测试次数则返回真
            //return connectedCount == pingTimes;
            logger.info("ping通设备IP的次数为:" +connectedCount);
            System.out.println("ping通设备IP的次数为:" +connectedCount);
            return connectedCount >= 1 ? true : false;
        } catch (Exception ex) {
            ex.printStackTrace(); //出现异常则返回假
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //若line含有=18 ms ttl=64字样,说明已经ping通,返回1,否則返回0.
    private static int getCheckResult(String line,String osName) {
        if(osName.contains("Windows")){
            if(line.contains("TTL=")){
                return 1;
            }
        }else{
            if(line.contains("ttl=")){
                return 1;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        String ipAddress = "192.168.0.82";
        String osName = System.getProperty("os.name");
        ping(ipAddress, 5,5000,"Linux");
        System.out.println(osName);
    }
}
