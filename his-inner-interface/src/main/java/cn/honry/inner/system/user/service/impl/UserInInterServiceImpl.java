package cn.honry.inner.system.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.Hospital;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.user.dao.UserInInterDAO;
import cn.honry.inner.system.user.service.UserInInterService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.MD5;
import cn.honry.utils.SessionUtils;

/**  
 *  
 * 内部接口：用户
 * @Author：aizhonghua
 * @CreateDate：2016-7-05 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("userInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class UserInInterServiceImpl implements UserInInterService{

	@Autowired
	@Qualifier(value = "userInInterDAO")
	private UserInInterDAO userInInterDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public User get(String id) {
		return userInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(User entity) {
		
	}

	@Override
	public Map<String, String> getUserMap() {
		List<User> userList = userInInterDAO.getAll();
		Map<String, String> map = new HashMap<String, String>();
		if(userList!=null&&userList.size()>0){
			for(User user : userList){
				map.put(user.getAccount(), user.getName());
			}
		}
		return map;
	}
	
	@Override
	public void saveOrUpdateeditInfo(User entity) {
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		String userId = user.getAccount();
		if(StringUtils.isBlank(entity.getId())){
			SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			String deptId = dept.getDeptCode();
			entity.setId(null);
			Hospital hospital = new Hospital();
			hospital.setId(HisParameters.CURRENTHOSPITALID);
			entity.setHospitalId(hospital);
			entity.setCode(userInInterDAO.getSeqByName("SEQ_USER_CODE"));
			String password = entity.getAccount() + "123";
			password = new MD5().MD5Encode(password).toString();
			entity.setPassword(password);
			entity.setDel_flg(0);
			entity.setCreateUser(userId);
			entity.setCreateDept(deptId);
			entity.setCreateTime(DateUtils.getCurrentTime());
			entity.setStop_flg(0);
			//手势密码默认为1
			String gesturePsw = "1";
			gesturePsw = new MD5().MD5Encode(gesturePsw).toString();
			entity.setGesturePsw(gesturePsw);
			userInInterDAO.save(entity);
		}else{
			User olduser = userInInterDAO.get(entity.getId());
			olduser.setName(entity.getName());
			olduser.setNickName(entity.getNickName());
			olduser.setAccount(entity.getAccount());
			olduser.setBirthday(entity.getBirthday());
			olduser.setSex(entity.getSex());
			olduser.setLastLoginTime(entity.getLastLoginTime());
			olduser.setPhone(entity.getPhone());
			olduser.setEmail(entity.getEmail());
			olduser.setType(entity.getType());
			olduser.setOrder(entity.getOrder());
			olduser.setStop_flg(entity.getStop_flg());
			olduser.setDel_flg(entity.getDel_flg());
			olduser.setUpdateUser(userId);
			olduser.setUpdateTime(new Date());
			olduser.setRemark(entity.getRemark());
			olduser.setUseStatus(entity.getUseStatus());
			userInInterDAO.update(olduser);
		}
	}

	@Override
	public List<User> getPage(String page, String rows, User user) {
		return userInInterDAO.getPage(page, rows, user);
	}
	
	@Override
	public int getTotal(User user) {
		return userInInterDAO.getTotal(user);
	}

	@Override
	public User getByCode(String code) {
		return userInInterDAO.getByCode(code);
	}
	/**
	 * @Description:获取所有员工信息
	 * @Author: zhangjin
	 * @CreateDate: 2016年11月8日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<SysEmployee> getuserMap() {
		return userInInterDAO.getuserMap();
	}

	/**
	 * 
	 * @Description 根据用户account 去查用户
	 * @author  zpty
	 * @createDate： 2017年3月28日 上午10:57:32 
	 * @param：  account 用户编码
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public User getByAccount(String account) {
		return userInInterDAO.getByAccount(account);
	}

	@Override
	public List<SysDepartment> getRelatedDeptAccount(String account) {
		return userInInterDAO.getRelatedDeptAccount(account);
	}
	
}
