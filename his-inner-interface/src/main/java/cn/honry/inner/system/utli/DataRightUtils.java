package cn.honry.inner.system.utli;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.WebUtils;

/**  
 *  
 * @className：DataRightUtils 
 * @Description：  数据权限工具类 
 * @Author：aizhonghua
 * @CreateDate：2015-10-26 下午03:29:51  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-10-26 下午03:29:51  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class DataRightUtils {
	
	public static final String DATARIGHTGR="GR";//数据权限 - 个人
	public static final String DATARIGHTKS="KS";//数据权限 - 科室
	public static final String DATARIGHTQY="QY";//数据权限 - 全院
	public static final String ROLEADMIN="admin";//角色 - admin
	
	/**  
	 *  
	 * @Description：  获得拼接的sql语句
	 * @Author：aizhonghua
	 * @CreateDate：2015-10-26 下午03:32:24  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-10-26 下午03:32:24  
	 * @ModifyRmk：  
	 * @param menuAlias 栏目别名      tableAlias 表的别名
	 * @version 1.0
	 *
	 */
	public static String connectSQLSentence(String tableAlias){
		String menuAlias=getMenuAlias();
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();
		if(ROLEADMIN.equals(user.getAccount())){//如果登录用户为admin 默认全院权限
			return " 1=1 ";
		}else{//如果登录用户不为admin
			if(StringUtils.isBlank(menuAlias)){//如果栏目别名为空  默认为科室权限  2016-03-03由科室权限改为全院权限
				if(StringUtils.isBlank(tableAlias)){//如果别名为空
					return " 1=1 ";
					//return " CREATEDEPT = '"+ShiroShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"' ";
				}else{//如果别名不为空
					return " 1=1 ";
					//return " "+tableAlias+".CREATEDEPT = '"+ShiroShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"' ";
				}
			}else{//如果栏目别名不为空 
				@SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String,String>)SessionUtils.getCurrentUserLoginRoleDataAliasFromShiroSession();
				if(map==null){//如果角色没有数据权限 默认为全院权限
					return " 1=1 ";
				}
				String s = map.get(menuAlias);
				if(StringUtils.isBlank(s)){//如果数据权限为空  默认为科室权限  2016-03-03由科室权限改为全院权限
					if(StringUtils.isBlank(tableAlias)){//如果别名为空
						return " 1=1 ";
						//return " CREATEDEPT = '"+ShiroShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"' ";
					}else{//如果别名不为空
						return " 1=1 ";
						//return " "+tableAlias+".CREATEDEPT = '"+ShiroShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"' ";
					}
				}else{//如果数据权限不为空  判断所拥有的数据权限
					if(DATARIGHTGR.equals(s)){//拥有个人权限
						if(StringUtils.isBlank(tableAlias)){//如果别名为空
							return " CREATEUSER = '"+user.getId()+"' ";
						}else{//如果别名不为空
							return " "+tableAlias+".CREATEUSER = '"+user.getId()+"' ";
						}
					}else if(DATARIGHTKS.equals(s)){//拥有科室权限
						SysDepartment dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
						if(StringUtils.isBlank(tableAlias)){//如果别名为空
							return " CREATEDEPT = '"+dept.getId()+"' ";
						}else{//如果别名不为空
							return " "+tableAlias+".CREATEDEPT = '"+dept.getId()+"' ";
						}
					}else if(DATARIGHTQY.equals(s)){//拥有全院权限
						return " 1=1 ";
					}else{//拥有科室权限
						SysDepartment dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
						if(StringUtils.isBlank(tableAlias)){//如果别名为空
							return " CREATEDEPT = '"+dept.getId()+"' ";
						}else{//如果别名不为空
							return " "+tableAlias+".CREATEDEPT = '"+dept.getId()+"' ";
						}
					}
				}
			}
		}
	}
	
	/**  
	 *  
	 * @Description：   获得拼接的hql语句
	 * @Author：aizhonghua
	 * @CreateDate：2015-10-26 下午03:32:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-10-26 下午03:32:52  
	 * @ModifyRmk：  
	 * @param menuAlias 栏目别名      tableAlias 表的别名
	 * @version 1.0
	 *
	 */
	public static String connectHQLSentence(String tableAlias){
		String menuAlias=getMenuAlias();
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();
		if(ROLEADMIN.equals(user.getAccount())){//如果登录用户为admin 默认全院权限
			return " 1=1 ";
		}else{//如果登录用户不为admin
			if(StringUtils.isBlank(menuAlias)){//如果栏目别名为空  默认为科室权限 2016-03-03由科室权限改为全院权限
				if(StringUtils.isBlank(tableAlias)){//如果别名为空
					return " 1=1 ";
					//return " createDept = '"+ShiroShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"' ";
				}else{//如果别名不为空
					return " 1=1 ";
					//return " "+tableAlias+".createDept = '"+ShiroShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"' ";
				}
			}else{//如果栏目别名不为空 
				@SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String,String>)SessionUtils.getCurrentUserLoginRoleDataAliasFromShiroSession();
				if(map==null){//如果角色没有数据权限 默认为全院权限
					return " 1=1 ";
				}
				String s = map.get(menuAlias);
				if(StringUtils.isBlank(s)){//如果数据权限为空 默认为科室权限 2016-03-03由科室权限改为全院权限
					if(StringUtils.isBlank(tableAlias)){//如果别名为空
						return " 1=1 ";
						//return " createDept = '"+ShiroShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"' ";
					}else{//如果别名不为空
						return " 1=1 ";
						//return " "+tableAlias+".createDept = '"+ShiroShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"' ";
					}
				}else{//如果数据权限不为空  判断所拥有的数据权限
					if(DATARIGHTGR.equals(s)){//拥有个人权限
						if(StringUtils.isBlank(tableAlias)){//如果别名为空
							return " createUser = '"+user.getId()+"' ";
						}else{//如果别名不为空
							return " "+tableAlias+".createUser = '"+user.getId()+"' ";
						}
					}else if(DATARIGHTKS.equals(s)){//拥有科室权限
						SysDepartment dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
						if(StringUtils.isBlank(tableAlias)){//如果别名为空
							return " createDept = '"+dept.getId()+"' ";
						}else{//如果别名不为空
							return " "+tableAlias+".createDept = '"+dept.getId()+"' ";
						}
					}else if(DATARIGHTQY.equals(s)){//拥有全院权限
						return " 1=1 ";
					}else{//拥有科室权限
						SysDepartment dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
						if(StringUtils.isBlank(tableAlias)){//如果别名为空
							return " createDept = '"+dept.getId()+"' ";
						}else{//如果别名不为空
							return " "+tableAlias+".createDept = '"+dept.getId()+"' ";
						}
					}
				}
			}
		}
	}
	/**
	 * 获取ActionContext中的menuAlias参数
	 * @author yueyaofu
	 * @return
	 */
	public static String getMenuAlias(){
		return WebUtils.getRequest().getParameter("menuAlias");
	}
}
