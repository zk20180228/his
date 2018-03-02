package cn.honry.oa.userSign.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.OaUserSign;
import cn.honry.base.bean.model.OaUserSignChange;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：UserSignDAO
 * @Description：  用户电子签章维护
 * @Author：aizhonghua
 * @CreateDate：2017-7-14 下午16:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-14 下午16:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface UserSignDAO extends EntityDao<OaUserSign>{

	/**  
	 *  
	 * @Description：  获取列表-获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getUserSignTotal(String search);

	/**  
	 *  
	 * @Description：  获取列表-获得显示信息
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OaUserSign> getUserSignRows(String page, String rows, String search);
	/**  
	 *  
	 * @Description：  获取个人签名列表-获得显示信息
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午17:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OaUserSign> getUserOneSignRows(String search,String account);
	/**  
	 *  
	 * @Description：  获取系统角色
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午12:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-14 下午12:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysRole> getSysRole(String page, String rows,String q);
	/**  
	 *  
	 * @Description：  获取系统角色记录数
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午12:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-14 下午12:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getSysRoleTotal(String q);
	
	/**  
	 *  
	 * @Description：  获取系统职称
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午12:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-14 下午12:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<BusinessDictionary> getBusinessDictionary(String page, String rows,String q);
	/**  
	 *  
	 * @Description：  获取系统角色记录数
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午12:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-14 下午12:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getBusinessDictionaryTotal(String q);
	/**  
	 *  
	 * @Description：  验证密码是否正确
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午17:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	OaUserSign getSignRow(String signid, String password,String account);
	/**
	 * 
	 * 
	 * <p>跟据员工账号查询电子签名信息 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月7日 上午9:17:44 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月7日 上午9:17:44 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param account 员工号
	 * @return:
	 *
	 */
	OaUserSign getElecSign(String account);
	/**
	 * @Description:通过员工号查询员工所能使用的签名
	 * @Author: donghe
	 * @CreateDate: 2017年7月20日
	 * @param:account-员工号
	 * @return:List<OaUserSign>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<OaUserSign> queryOaUserSigns(String account);
	/**
	 * @Description:通过id和版本号查询员工所能使用的签名
	 * @Author: donghe
	 * @CreateDate: 2017年7月20日
	 * @param:account-员工号
	 * @return:List<OaUserSign>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	OaUserSign queryOaUserSignByid(String id,String version);
	/**
	 * @Description:通过id和版本号查询记录表员工所能使用的签名
	 * @Author: donghe
	 * @CreateDate: 2017年7月20日
	 * @param:account-员工号
	 * @return:List<OaUserSignChange>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	OaUserSignChange queryOaUserSignChangeByid(String id,String version);
}
