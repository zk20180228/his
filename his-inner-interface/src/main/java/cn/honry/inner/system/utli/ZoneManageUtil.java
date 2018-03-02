package cn.honry.inner.system.utli;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import cn.honry.base.bean.model.SysDbAdmin;
import cn.honry.utils.ApplicationContextUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.RedisUtil;

public class ZoneManageUtil{
	private ZoneManageUtil(){
	}
	private static ZoneManageUtil instance;
	static {
		instance = new ZoneManageUtil();
	}
	public static ZoneManageUtil getInstance() {
		return instance;
	}
	RedisUtil redisUtil = (RedisUtil)ApplicationContextUtils.getBean("redisUtil");  

	/**  
	 * 
	 * <p> 获取当前参数下的分区名 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月21日 下午6:22:41 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月21日 下午6:22:41 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:dbName库名tableName表名value参数isTime是否为时间
	 * @throws:
	 * @return: String
	 *
	 */
	public String getZoneName(String dbName,String tableName, String value,boolean isTime) {
		try {
			if(StringUtils.isBlank(value)){
				return "";
			}
			String[] vArr = value.split("-");
			String realVal = "";
			if(isTime&&vArr.length==1){//向后一年
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = dateFormat.parse(value+"-01-01 00:00:00");
				Date realDate = DateUtils.addToDate(date, 1, 0, 0);
				String[] tealArr = DateUtils.formatDateY_M(realDate).split("-");
				realVal = tealArr[0];
			}else if(isTime&&vArr.length==2){//向后一月
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = dateFormat.parse(value+"-01 00:00:00");
				Date realDate = DateUtils.addToDate(date, 0, 1, 0);
				realVal = DateUtils.formatDateY_M(realDate);
			}else if(isTime&&vArr.length==3){//向后一天
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = dateFormat.parse(value+" 00:00:00");
				Date realDate = DateUtils.addDay(date, 1);
				realVal = DateUtils.formatDateY_M_D(realDate);
			}else{
				realVal = value;
			}
			String key = SysDbAdmin.class+"_CACHE_"+dbName+"_"+tableName+"_"+realVal;
			String ZoneName = (String) redisUtil.get(key);
			if(StringUtils.isNotBlank(ZoneName)){
				return " PARTITION("+ZoneName+")";
			}
			String keyVague = SysDbAdmin.class+"_CACHE_"+dbName+"_"+tableName+"_*";
			Set<byte[]> bSet = redisUtil.keys(keyVague);
			if(bSet.size()==0){
				return "";
			}
			List<String> sList = new ArrayList<String>();
			for (byte[] ss : bSet) {
				sList.add(new String(ss,"GB2312"));
			}
			Collections.sort(sList);
			String keyMin = sList.get(0);
			String ZoneNameMin = (String) redisUtil.get(keyMin);
			return StringUtils.isNotBlank(ZoneNameMin)?" PARTITION("+ZoneNameMin+")":"";
		} catch (Exception e) {
			return "";
		}
	}

	/**  
	 * 
	 * <p> 判断是否存在该分区 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月21日 下午6:22:41 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月21日 下午6:22:41 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:dbName库名tableName表名value参数isTime是否为时间
	 * @throws:
	 * @return: String
	 *
	 */
	public boolean isHaveZoneName(String dbName,String tableName, String value,boolean isTime) {
		try {
			if(StringUtils.isBlank(value)){
				return false;
			}
			String[] vArr = value.split("-");
			String realVal = "";
			if(isTime&&vArr.length==1){//向后一年
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = dateFormat.parse(value+"-01-01 00:00:00");
				Date realDate = DateUtils.addToDate(date, 1, 0, 0);
				String[] tealArr = DateUtils.formatDateY_M(realDate).split("-");
				realVal = tealArr[0];
			}else if(isTime&&vArr.length==2){//向后一月
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = dateFormat.parse(value+"-01 00:00:00");
				Date realDate = DateUtils.addToDate(date, 0, 1, 0);
				realVal = DateUtils.formatDateY_M(realDate);
			}else if(isTime&&vArr.length==3){//向后一天
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = dateFormat.parse(value+" 00:00:00");
				Date realDate = DateUtils.addDay(date, 1);
				realVal = DateUtils.formatDateY_M_D(realDate);
			}else{
				realVal = value;
			}
			String key = SysDbAdmin.class+"_CACHE_"+dbName+"_"+tableName+"_"+realVal;
			String ZoneName = (String) redisUtil.get(key);
			if(StringUtils.isNotBlank(ZoneName)){
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**  
	 * 
	 * <p> 获得该表第一个分区名 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月21日 下午6:22:41 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月21日 下午6:22:41 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:dbName库名tableName表名
	 * @throws:
	 * @return: String
	 *
	 */
	public String getFirstZoneName(String dbName, String tableName) {
		try {
			String keyVague = SysDbAdmin.class+"_CACHE_"+dbName+"_"+tableName+"_*";
			Set<byte[]> bSet = redisUtil.keys(keyVague);
			if(bSet.size()==0){
				return "";
			}
			List<String> sList = new ArrayList<String>();
			for (byte[] ss : bSet) {
				sList.add(new String(ss,"GB2312"));
			}
			Collections.sort(sList);
			return sList.get(0);
		} catch (Exception e) {
			return null;
		}
		
	}

	/**  
	 * 
	 * <p> 获取该参数前的全部分区名 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月21日 下午6:22:41 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月21日 下午6:22:41 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:dbName库名tableName表名value参数isTime是否为时间
	 * @throws:
	 * @return: String
	 *
	 */
	public List<String> getBeforeZoneName(String dbName, String tableName, String para) {
		try {
			String keyVague = SysDbAdmin.class+"_CACHE_"+dbName+"_"+tableName+"_*";
			Set<byte[]> bSet = redisUtil.keys(keyVague);
			if(bSet.size()==0){
				return null;
			}
			List<String> sList = new ArrayList<String>();
			for (byte[] ss : bSet) {
				sList.add(new String(ss,"GB2312"));
			}
			Collections.sort(sList);
			boolean isTrue = true;
			String key = SysDbAdmin.class+"_CACHE_"+dbName+"_"+tableName+"_"+para;
			List<String> rList = new ArrayList<String>();
			for(String name : sList){
				if(key.equals(name)){
					isTrue = false;
				}
				if(isTrue){
					rList.add(0, name);
				}
			}
			return rList;
		} catch (Exception e) {
			return null;
		}
	}

	/**  
	 * 
	 * <p> 获得最新分区集合 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午8:22:50 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午8:22:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:dbName数据库名tableName表名num需要获取的分区数量
	 * @throws:
	 * @return: List<String> 表名 PARTITION(分区名)
	 *
	 */
	public List<String> getZoneNameListForNum(String dbName, String tableName, int num) {
		try {
			String keyVague = SysDbAdmin.class+"_CACHE_"+dbName+"_"+tableName+"_*";
			Set<byte[]> bSet = redisUtil.keys(keyVague);
			if(bSet.size()==0){
				return new ArrayList<String>();
			}
			List<String> sList = new ArrayList<String>();
			for (byte[] ss : bSet) {
				sList.add(new String(ss,"GB2312"));
			}
			Collections.sort(sList);
			List<String> retList = new ArrayList<String>();
			for(int i=0;i<num;i++){
				if(sList.size()-i-1<0){
					break;
				}
				String zoneName = (String)redisUtil.get(sList.get(sList.size()-i-1));
				retList.add(tableName+" PARTITION("+zoneName+")");
			}
			return retList;
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}

	/**  
	 * 
	 * <p> 获得最新分区集合 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午8:22:50 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午8:22:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:dbName数据库名tableName表名sTime开始时间eTime结束时间
	 * @throws:
	 * @return: List<String> 表名 PARTITION(分区名)
	 *
	 */
	public List<String> getZoneNameListForDate(String dbName,String tableName, String sTime, String eTime) {
		List<String> dateArr = DateUtils.findDateContain(sTime,eTime);
		List<String> retList = new ArrayList<String>();
		for(int i=0;i<dateArr.size();i++){
			String zoneName = getZoneName(dbName, tableName, dateArr.get(i), true);
			if(StringUtils.isNotBlank(zoneName)){
				retList.add(tableName+zoneName);
			}
		}
		HashSet<String> h  = new HashSet<String>(retList); 
		retList.clear(); 
		retList.addAll(h);
		return retList;
	}
}
