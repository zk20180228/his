package cn.honry.migrate.serviceManagement.service.Impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.LogServiceVo;
import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.migrate.logService.dao.LogServiceDao;
import cn.honry.migrate.serviceManagement.dao.ServiceManagementDao;
import cn.honry.migrate.serviceManagement.service.ServiceManagementService;
import cn.honry.migrate.synDateManage.dao.SynDateDao;
import cn.honry.utils.HisParameters;

@Service("serviceManagementService")
@Transactional
@SuppressWarnings({"all"})
public class ServiceManagementServiceImpl implements ServiceManagementService{
	
	@Autowired
	@Qualifier(value = "serviceManagementDao")
	private ServiceManagementDao serviceManagementDao;
	
	public void setServiceManagementDao(ServiceManagementDao serviceManagementDao) {
		this.serviceManagementDao = serviceManagementDao;
	}
	@Autowired
	@Qualifier("logServiceDao")
	private LogServiceDao logServiceDao;
	
	public void setLogServiceDao(LogServiceDao logServiceDao) {
		this.logServiceDao = logServiceDao;
	}
	@Autowired
	@Qualifier(value="synDateDao")
	private SynDateDao synDateDao;
	
	public void setSynDateDao(SynDateDao synDateDao) {
		this.synDateDao = synDateDao;
	}
	/**  
	 * 服务管理列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<ServiceManagement> queryServiceManagement(String code,String page,String rows,String menuAlias,String serviceType,String serviceState) {
		return serviceManagementDao.queryServiceManagement(code,page, rows, menuAlias,serviceType,serviceState);
	}
	/**  
	 * 服务管理列表查询(总条数)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int queryTotal(String code) {
		return serviceManagementDao.queryTotal(code);
	}
	
	/**  
	 * 服务管理 (添加/修改)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public Map<String,String> saveServiceManagement(ServiceManagement serviceManagement) throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		if(StringUtils.isBlank(serviceManagement.getId())){
			if(StringUtils.isNotBlank(serviceManagement.getCode())&&serviceManagement.getMasterprePare()!=null){
				if(serviceManagementDao.queryUnionList(serviceManagement.getName()+"-"+(serviceManagement.getMasterprePare()!=null&&serviceManagement.getMasterprePare()!=1?"S":"M"), serviceManagement.getMasterprePare(),null)){
					serviceManagement.setId(null);
					serviceManagement.setName(serviceManagement.getName()+"-"+(serviceManagement.getMasterprePare()!=null&&serviceManagement.getMasterprePare()!=1?"备":"主"));
//					serviceManagement.setCode("00"+synDateDao.maxCode("I_SERVE_MANAGE", "CODE"));
					serviceManagement.setCode(serviceManagement.getCode()+"-"+(serviceManagement.getMasterprePare()!=null&&serviceManagement.getMasterprePare()!=1?"S":"M"));
					serviceManagement.setSystem("Linux");
					serviceManagementDao.save(serviceManagement);
				}else{
					map.put("resCode", "error");
					map.put("resMsg", "服务重复 请重新添加");
					return map;
				}
			}else{
				throw new Exception("参数不全");
			}
		}else{
			if(StringUtils.isNotBlank(serviceManagement.getCode())&&serviceManagement.getMasterprePare()!=null){
				if(serviceManagementDao.queryUnionList(serviceManagement.getCode()+"-"+(serviceManagement.getMasterprePare()!=null&&serviceManagement.getMasterprePare()!=1?"S":"M"), serviceManagement.getMasterprePare(),serviceManagement.getId())){
					ServiceManagement serviceManagement1 = serviceManagementDao.getOnedata(serviceManagement.getId());
					serviceManagement.setCode(serviceManagement.getCode()+"-"+(serviceManagement.getMasterprePare()!=null&&serviceManagement.getMasterprePare()!=1?"S":"M"));
					serviceManagement1.setName(serviceManagement.getName()+"-"+(serviceManagement.getMasterprePare()!=null&&serviceManagement.getMasterprePare()!=1?"备":"主"));
					serviceManagement1.setState(serviceManagement.getState());
					serviceManagement1.setType(serviceManagement.getType());
					serviceManagement1.setIp(serviceManagement.getIp());
					serviceManagement1.setPort(serviceManagement.getPort());
					serviceManagement1.setHeartRate(serviceManagement.getHeartRate());
					serviceManagement1.setHeartUnit(serviceManagement.getHeartUnit());
					serviceManagement1.setHeartNewtime(serviceManagement.getHeartNewtime());
					serviceManagement1.setMasterprePare(serviceManagement.getMasterprePare());
					serviceManagement1.setRemarks(serviceManagement.getRemarks());
					serviceManagement1.setSystem("Linux");
					serviceManagementDao.save(serviceManagement1);
				}else{
					map.put("resCode", "error");
					map.put("resMsg", "服务重复 请重新添加");
					return map;
				}
			}else{
				throw new Exception("参数不全");
			}
		}
		return map;
	}
	
	/**  
	 * 服务管理  (删除)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void delServiceManagement(String id,String state) {
		ServiceManagement serviceManagement1 = serviceManagementDao.getOnedata(id);
		serviceManagement1.setState(Integer.parseInt(state));
		serviceManagementDao.save(serviceManagement1);
	}
	/**  
	 * 服务管理  (删除)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public ServiceManagement getOnedata(String id) {
		return serviceManagementDao.getOnedata(id);
	}
		@Override
	public void delService(String id) throws Exception {
		if(StringUtils.isNotBlank(id)){
			id=id.replaceAll(".*([';]+|(--)+).*","");
			serviceManagementDao.delServiceManagement(id);
		}else{
			throw new Exception("参数为空");
		}
	}
	
	@Override
	public List<ServiceManagement> queryServiceManagement(String queryCode) {
		return serviceManagementDao.queryServiceManagement(queryCode);
	}
	
	@Override
	public String sendRequest(String ip, String port,String serviceName)  throws Exception {
		
		String serverPath = "http://"+ip+":"+port+serviceName+"/migrate/ServiceManagement/reloadService.action?ip="+ip+"&port="+port;
		 String result="";
		try {
			HttpURLConnection conn = null;
			BufferedReader in = null;
			PrintWriter out = null;
			URL url = new URL(serverPath);
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setRequestMethod("POST");  
	        // 发送POST请求必须设置如下两行  
	        conn.setDoOutput(true);  
	        conn.setDoInput(true);  
	        conn.setUseCaches(false);
	     // 定义数据分隔线  
	        String BOUNDARY = "========7d4a6d158c9"; 
	     // 设置请求头参数  
	        conn.setRequestProperty("Charsert", "UTF-8");  
	        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
	        conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Referer", "http://"+ip+":"+port+serviceName+"/migrate/ServiceManagement/listServiceManagement.action?menuAlias=FWGL");
	        out = new PrintWriter(conn.getOutputStream());
            // flush输出流的缓冲
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
           
            while ((line = in.readLine()) != null) {
                result += line;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	@Override
	public List<LogServiceVo> queryLogService(String serviceCode, String STime,
			String Etime, String page, String rows, String menuAlias) {
		return logServiceDao.queryLogService(serviceCode, STime, Etime, page, rows, menuAlias);
	}
	
	@Override
	public int totalService(String serviceCode, String STime, String Etime) {
		return logServiceDao.totalService(serviceCode, STime, Etime);
	}

}
