package cn.honry.oa.wages.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.OaWages;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;

/** 
* @Description:工资管理DAO
* @author zx
* @date 2017年7月17日
*  
*/
@SuppressWarnings({"all"})
public interface WagesDAO extends EntityDao<OaWages>{
	/** 
	* @Description: 工资账号是否存在
	* @param weagesAccount 工资账号
	* @throws Exception
	* @return List<SysEmployee>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	List<SysEmployee> isExistWadges(String weagesAccount) throws Exception;
	/** 
	* @Description: 修改工资查询密码 
	* @param weagesAccount 工资账号
	* @param password 密码
	* @throws Exception
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	String initPasswordToWeages(String weagesAccount, String password) throws Exception;
	/** 
	* @Description: 查询原密码 
	* @param weagesAccount 查询账号
	* @param weagesPassword 密码
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	SysEmployee checkAccount(String weagesAccount, String weagesPassword) throws Exception;
	/** 
	* @Description: 查询工资数据
	* @param wageAccount 工资账号
	* @param name 身份证号
	* @param wagesTime 查询时间
	* @param page
	* @param rows
	* @throws Exception
	* @return List<OaWages>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	Map<String, Object> listWagesQuery(String wageAccount, String name, String wagesTime, String page, String rows) throws Exception;
	/** 
	* @Description: 总条数 
	* @param wageAccount 工资账号
	* @param name 身份证号
	* @param wagesTime 查询时间
	* @throws Exception
	* @return int    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	int getTotal(String wageAccount, String name, String wagesTime) throws Exception;
	/** 
	* @Description: 批量插入数据
	* @param oaWagesList 工资数据集合
	* @throws HibernateException
	* @throws SQLException
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	void saveBatch(List<OaWages> oaWagesList) throws HibernateException, SQLException;
	/** 
	* @Description: 查询所有的数据用于导出 
	* @param wagesAccount 工资账号
	* @param wagesName 身份账号
	* @param wagesTime 工资月份
	* @throws Exception
	* @return List<OaWages>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	List<OaWages> listWagesQueryForExport(String wagesAccount, String wagesName, String wagesTime) throws Exception;
	/** 
	* @Description: 验证账户密码
	* @param wagesAccount 工资账号
	* @param weagesPassword 密码
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	SysEmployee checkAccountByAId(String wagesAccount, String weagesPassword)throws Exception;
	/** 
	* @Description: 查询是否已经设置过密码
	* @param account 工资账号
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	SysEmployee checkAccountForInit(String account)throws Exception;
	/** 
	* @Description: 查询当前登陆人的身份证信息 
	* @param account 账号
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	SysEmployee getIcdFoUser(String account)throws Exception;
	
}
