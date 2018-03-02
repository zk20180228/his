package cn.honry.inner.system.utli;

import cn.honry.hiasMongo.operateLog.utils.OperateLogUtils;
import cn.honry.inner.system.operation.dao.OperationDAO;
import cn.honry.utils.ApplicationContextUtils;

/**  
 *  
 * @Description： 操作日志
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public final class OperationUtils {
	
	private OperationUtils(){
	}
	
	private static OperationUtils instance;
	
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
	     instance = new OperationUtils();
	}
	
	public static OperationUtils getInstance() {
	     return instance;
	}
	
	OperationDAO operationDAO = (OperationDAO)ApplicationContextUtils.getBean("operationDAO");  
	
	/**  
	 *  
	 * @Description：  
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-17 下午02:09:26  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-17 下午02:09:26  
	 * @ModifyRmk：  
	 * @param: id 目标编码 可以为多个(用,隔开); menuName 栏目名称; sql 执行sql; tableName 操作表名 ; operateAction 操作行为
	 * @version 1.0
	 *
	 */
	public void conserve(String id,String menuName,String sql,String tableName,String operateAction){
		/*User user = (User)SessionUtils.getCurrentUserFromShiroSession();//获取当前登录人
		SysDepartment dept=(SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取当前登录用户所属的科室
		SysUseroperation operation = new SysUseroperation();
		operation.setUser(user);//操作用户
		operation.setAction(operateAction);//操作行为
		if(dept!=null){
			operation.setDeptId(dept.getDeptName());//操作科室
		}
		operation.setMenuId(menuName);//操作栏目
		operation.setSql(sql);//操作sql
		operation.setTable(tableName);//操作表名
		operation.setTargetId(id);//目标编码
		operation.setTime(new Date());//操作时间
		operationDAO.save(operation);*/
		//hedong 20170331 改用mongodb方式
		OperateLogUtils.getInstance().conserveToMongo(id, menuName, sql, tableName, operateAction);//hedongMongodbceshi
	}
	
}
