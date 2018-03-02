package cn.honry.hiasMongo.operateLog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.mongodb.BasicDBObject;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.utils.SessionUtils;
/**  
 * @Description： 操作日志(将操作记录保存至mongoDB)
 * @Author：hedong
 * @CreateDate：2017-03-27 
 * @version 1.0
 */
public final class OperateLogUtils {
	 public OperateLogUtils(){
		  
	 };
	 private static OperateLogUtils instance;
	 public static final String LOGACTIONINSERT="添加";//操作日志-操作行为-添加
	 public static final String LOGACTIONUPDATE="修改";//操作日志-操作行为-修改
	 public static final String LOGACTIONDELETE="删除";//操作日志-操作行为-删除
	 public static final String LOGACTIONCHECKOUT="结账";//操作日志-操作行为-结账
	 public static final String LOGACTIONINFO="挂号";//操作日志-操作行为-挂号
	 public static final String LOGACTIONRECALL="出院召回";//操作日志-操作行为-出院招回
	 public static final String LOGACTIONARRANGEMENT="安排手术";//操作日志-操作行为-安排手术
	 public static final String LOGACTIONYYARRANGEMENT="重新预约手术";//操作日志-操作行为-重新预约手术
	 public static final String LOGACTIONDELARRANGEMENT="取消安排手术";//操作日志-操作行为-取消安排手术
	 public static final String LOGACTIONCLINICDOC="门诊医嘱";//操作日志-操作行为-取消安排手术
	 public static final String LOGACTIONCLINICDOCSTACK="门诊医嘱组套保存";//操作日志-操作行为-门诊医嘱组套保存
	 public static final String LOGACTIONCLINICDOCAUDIT="门诊医嘱审核";//操作日志-操作行为-门诊医嘱审核
	 public static final String LOGACTIONMATERIALS="物资正常出库";//操作日志-操作行为-物资正常出库
	 public static final String LOGACTIONTECTERMINALAPPLY="医技终端确认";//操作日志-操作行为-医技终端确认
	 public static final String LOGACTIONTECTERMINALAPPLYCANCEL="医技终端确认取消";//操作日志-操作行为-医技终端确认取消
	 public static final String LOGACTIONTONMAINBACKAFTERDAYBALANCE="日结后退号";//操作日志-操作行为-退号
		
	 //静态块，初始化实例
	 static {
		    instance = new OperateLogUtils();
	 }
	 public static OperateLogUtils getInstance() {
		     return instance;
	 }
    /**  
	 * @Description：  保存操作日志信息至mongoDB
	 * @Author：hedong
	 * @CreateDate：2017-3-27
	 * @param: targetId 目标编码 可以为多个(用,隔开); menuName 栏目名称; sql 执行sql; tableName 操作表名 ; operateAction 操作行为
	 * @version 1.0
	 */
	public void conserveToMongo(String targetId,String menuName,String sql,String tableName,String operateAction){
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();//获取当前登录人
		SysDepartment dept=(SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取当前登录用户所属的科室
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String logDate = sf.format(new Date());

		MongoBasicDao mbDao = new MongoBasicDao();
		BasicDBObject bdbObject = new BasicDBObject();
		if(StringUtils.isNotBlank(operateAction)){
			bdbObject.put("LOG_ACTION",operateAction);//操作行为
		}else{
			bdbObject.put("LOG_ACTION","");//操作行为
		}
		
		bdbObject.put("LOG_USERID",user.getId());//操作用户
		if(dept!=null){
			bdbObject.put("LOG_DEPTID",dept.getDeptName());//操作部门
		}else{
			bdbObject.put("LOG_DEPTID","");//操作部门
		}
		
		if(StringUtils.isNotBlank(menuName)){
			bdbObject.put("LOG_MENUID",menuName);//操作栏目
		}else{
			bdbObject.put("LOG_MENUID","");//操作栏目
		}
		
		if(StringUtils.isNotBlank(sql)){
			bdbObject.put("LOG_SQL",sql);//操作SQL
		}else{
			bdbObject.put("LOG_SQL","");//操作SQL
		}
		
		if(StringUtils.isNotBlank(tableName)){
			bdbObject.put("LOG_TABLE",tableName);//操作表
		}else{
			bdbObject.put("LOG_TABLE","");//操作表
		}
		
		if(StringUtils.isNotBlank(targetId)){
			bdbObject.put("LOG_TARGETID",targetId);//目标编号
		}else{
			bdbObject.put("LOG_TARGETID","");//目标编号
		}
		
		bdbObject.put("LOG_TIME", logDate);//操作时间
		bdbObject.put("LOG_DESCRIPTION", "");//操作备注
		bdbObject.put("LOG_KEY1", "");//备用字段1
		bdbObject.put("LOG_KEY2", "");//备用字段2
		bdbObject.put("LOG_KEY3", "");//备用字段3
		bdbObject.put("LOG_KEY4", "");//备用字段4
		bdbObject.put("LOG_KEY5", "");//备用字段5
		
		mbDao.addData("T_SYS_USEROPERATION",bdbObject);
	}
  
  
}
