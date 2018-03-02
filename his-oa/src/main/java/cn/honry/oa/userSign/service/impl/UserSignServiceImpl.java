package cn.honry.oa.userSign.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.OaUserSign;
import cn.honry.base.bean.model.OaUserSignChange;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.bean.model.User;
import cn.honry.oa.userSign.dao.UserSignDAO;
import cn.honry.oa.userSign.service.UserSignService;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * @className：UserSignServiceImpl
 * @Description：  用户电子签章维护
 * @Author：aizhonghua
 * @CreateDate：2017-7-14 下午16:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-14 下午16:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("userSignService")
@Transactional
@SuppressWarnings({ "all" })
public class UserSignServiceImpl implements UserSignService{

	@Autowired
	@Qualifier(value = "userSignDAO")
	private UserSignDAO userSignDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public OaUserSign get(String id) {
		return userSignDAO.get(id);
	}

	@Override
	public void saveOrUpdate(OaUserSign userSign) {
		userSignDAO.save(userSign);
	}

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
	@Override
	public int getUserSignTotal(String search) {
		return userSignDAO.getUserSignTotal(search);
	}

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
	@Override
	public List<OaUserSign> getUserSignRows(String page, String rows, String search) {
		return userSignDAO.getUserSignRows(page,rows,search);
	}

	/**  
	 *  
	 * @Description：  保存电子签章信息
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void save(OaUserSign userSign, File signInfoFile) {
		try {
			if(StringUtils.isNotBlank(userSign.getId())){
				OaUserSign sign = userSignDAO.get(userSign.getId());
				//变更记录表
				OaUserSignChange change = new OaUserSignChange();
				change.setId(null);
				change.setSignId(sign.getId());
				change.setOldSignPassword(sign.getSignPassword());
				change.setNewSignPassword(userSign.getSignPassword());
				change.setOldSignName(sign.getSignName());
				change.setNewSignName(userSign.getSignName());
				change.setOldSignInfo(sign.getSignInfo());
				change.setNewSignInfo(userSign.getSignInfo());
				change.setOldSignCategory(sign.getSignCategory());
				change.setNewSignCategory(userSign.getSignCategory());
				change.setOldSignType(sign.getSignType());
				change.setNewSignType(userSign.getSignType());
				change.setOldUserAcc(sign.getUserAcc());
				change.setNewUserAcc(userSign.getUserAcc());
				change.setOldUserAccName(sign.getUserAccName());
				change.setNewUserAccName(userSign.getUserAccName());
				change.setCreateTime(new Date());
				change.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				change.setVersion(sign.getVersion());
				userSignDAO.save(change);
				if(signInfoFile!=null){
					FileInputStream fis = new FileInputStream(signInfoFile);
					Blob signInfo = Hibernate.createBlob(fis);
					sign.setSignInfo(signInfo);
				}
				sign.setSignType(userSign.getSignType());
				sign.setSignName(userSign.getSignName());
				sign.setSignInputcode(userSign.getSignInputcode());
				sign.setSignDesc(userSign.getSignDesc());
//				User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
				
				sign.setSignPassword(userSign.getSignPassword());
				sign.setUserAcc(userSign.getUserAcc());
				sign.setUserAccName(userSign.getUserAccName());
				
				String pyWb = userSignDAO.getSpellCode(userSign.getSignName());
				if(pyWb!=null&&pyWb.contains("$")){
					String pw[] = pyWb.split("\\$");
					sign.setSignPinYin(pw[0]);
					sign.setSignWb(pw[1]);
				}
				sign.setVersion(sign.getVersion()+1);
				sign.setSignInputcode(StringUtils.isNotBlank(userSign.getSignInputcode())?userSign.getSignInputcode().toUpperCase():null);
				sign.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				sign.setUpdateTime(new Date());
				userSignDAO.save(sign);
			}else {
				userSign.setId(null);
				FileInputStream fis = new FileInputStream(signInfoFile);
				Blob signInfo = Hibernate.createBlob(fis);
				userSign.setSignInfo(signInfo);
				User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
				
//				userSign.setUserAcc(user.getAccount());
				String pyWb = userSignDAO.getSpellCode(userSign.getSignName());
				if(pyWb!=null&&pyWb.contains("$")){
					String pw[] = pyWb.split("\\$");
					userSign.setSignPinYin(pw[0]);
					userSign.setSignWb(pw[1]);
				}
				userSign.setSignInputcode(StringUtils.isNotBlank(userSign.getSignInputcode())?userSign.getSignInputcode().toUpperCase():null);
				userSign.setCreateUser(user.getAccount());
				SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				userSign.setCreateDept(dept==null?null:dept.getDeptCode());
				userSign.setCreateTime(new Date());
				userSign.setDel_flg(0);
				userSign.setStop_flg(0);
				userSign.setVersion(1);
				userSignDAO.save(userSign);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public List<SysRole> getSysRole(String page, String rows,String q) {
		return userSignDAO.getSysRole( page,  rows,q);
	}

	@Override
	public int getSysRoleTotal(String q) {
		return userSignDAO.getSysRoleTotal(q);
	}

	@Override
	public List<BusinessDictionary> getBusinessDictionary(String page,
			String rows,String q) {
		return userSignDAO.getBusinessDictionary(page, rows,q);
	}

	@Override
	public int getBusinessDictionaryTotal(String q) {
		return userSignDAO.getBusinessDictionaryTotal(q);
	}

	@Override
	public OaUserSign getSignRow(String signid, String password, String account) {
		return userSignDAO.getSignRow(signid, password, account);
	}

	@Override
	public void stop(String ids) {
		if(ids.indexOf(",")==-1){
			OaUserSign sign = userSignDAO.getSignRow(ids,null,null);
			sign.setStop_flg(1);
			sign.setDeleteTime(new Date());
			sign.setDeleteUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			userSignDAO.update(sign);
		}else{
			String [] idd = ids.split(",");
			for (String id : idd) {
				OaUserSign sign = userSignDAO.getSignRow(id,null,null);
				sign.setStop_flg(1);
				sign.setDeleteTime(new Date());
				sign.setDeleteUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				userSignDAO.save(sign);
			}
		}
	}

	@Override
	public void delete(String ids) {
		if(ids.indexOf(",")==-1){
			userSignDAO.del(ids, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		}else{
			String [] idd = ids.split(",");
			for (String id : idd) {
				userSignDAO.del(id, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			}
		}
		
	}

	@Override
	public List<OaUserSign> getUserOneSignRows(String search, String account) {
		return userSignDAO.getUserOneSignRows(search, account);
	}

	@Override
	public List<OaUserSign> queryOaUserSigns(String account) {
		return userSignDAO.queryOaUserSigns(account);
	}

	@Override
	public OaUserSign queryOaUserSignByid(String id, String version) {
		return userSignDAO.queryOaUserSignByid(id, version);
	}

	@Override
	public OaUserSignChange queryOaUserSignChangeByid(String id, String version) {
		return userSignDAO.queryOaUserSignChangeByid(id, version);
	}

	@Override
	public OaUserSign getElecSign(String account) {
		return userSignDAO.getElecSign(account);
	}

}
