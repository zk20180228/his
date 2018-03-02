package cn.honry.oa.userPortal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.OaUserPortal;
import cn.honry.base.bean.model.Schedule;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysInfo;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.oa.userPortal.dao.UserPortalDAO;
import cn.honry.oa.userPortal.service.UserPortalService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Service("userPortalService")
@Transactional
@SuppressWarnings({ "all" })
public class UserPortalServiceImpl implements UserPortalService{

	@Autowired
	@Qualifier(value = "userPortalDAO")
	private UserPortalDAO userPortalDAO;
	
	@Override
	public void saveOrUpdate(OaUserPortal entity) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setAccount(longinUserAccount);//保存的时候存入当前登录人
			entity.setHospitalCode(HisParameters.CURRENTHOSPITALID);//所属医院
			if(entity.getOrder()==null){
				entity.setOrder(-1);//组件顺序
			}
			entity.setCreateUser(longinUserAccount);
			entity.setCreateTime(new Date());
			userPortalDAO.save(entity);
			OperationUtils.getInstance().conserve(null,"OA系统首页管理","INSERT INTO","T_OA_USER_PORTAL",OperationUtils.LOGACTIONINSERT);

		}else{
			OaUserPortal oaUserPortal=userPortalDAO.getById(entity.getId());
			oaUserPortal.setWidget(entity.getWidget());//组件ID
			oaUserPortal.setName(entity.getName());//组件昵称
			if(entity.getOrder()!=null){
				oaUserPortal.setOrder(entity.getOrder());//组件顺序
			}else{
				oaUserPortal.setOrder(-1);
			}
			oaUserPortal.setUpdateTime(new Date());
			oaUserPortal.setUpdateUser(longinUserAccount);
			userPortalDAO.save(oaUserPortal);
			OperationUtils.getInstance().conserve(entity.getId(),"OA系统首页管理","UPDATE","T_OA_USER_PORTAL",OperationUtils.LOGACTIONUPDATE);
		}
	}

	@Override
	public void removeUnused(String id) {	
	}

	@Override
	public OaUserPortal get(String id) {
		OaUserPortal entity = userPortalDAO.getById(id);
		return entity;
	}

	@Override
	public void del(String id) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		OaUserPortal oaUserPortal=userPortalDAO.getById(id);
		oaUserPortal.setLocal(1);//0:正常,1:禁用
		oaUserPortal.setUpdateTime(new Date());
		oaUserPortal.setUpdateUser(longinUserAccount);
		userPortalDAO.save(oaUserPortal);
		OperationUtils.getInstance().conserve(id,"OA系统首页管理","UPDATE","T_OA_USER_PORTAL",OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public List<OaPortalWidget> queryPortalWidget() {
		return userPortalDAO.queryPortalWidget();
	}

	@Override
	public List<OaUserPortal> queryUserPortal(String longinUserAccount) {
		return userPortalDAO.queryUserPortal(longinUserAccount);
	}
	@Override
	public List<OaUserPortal> queryUserPortalAll(String longinUserAccount) {
		return userPortalDAO.queryUserPortalAll(longinUserAccount);
	}

	
	@Override
	public List<SysInfo> getList(SysInfo info) {
		return userPortalDAO.getList(info);
	}

	@Override
	public void moveWidget(String dataJson) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		//切割传过来的所有的组件ID字符串
		String[] widgetOrder = dataJson.split(",");
		//遍历组件数组,每一组分别处理
		for(int i=0;i<widgetOrder.length;i++){
			//切割组件ID字符串
			String[] widgetAndOrder = widgetOrder[i].split("\\|");
			String widget = widgetAndOrder[0];//取出组件ID
			String order = widgetAndOrder[1];//取出组件对应的顺序号
			OaUserPortal userPortal = new OaUserPortal();
			userPortal.setAccount(longinUserAccount);//条件:当前登录人
			userPortal.setWidget(widget);//条件,当前要修改的组件
			userPortal.setOrder(Integer.valueOf(order));//需要储存的顺序号
			userPortalDAO.updateWidget(userPortal);//保存组件顺序
		}
		OperationUtils.getInstance().conserve(dataJson,"OA系统首页管理","UPDATE","T_OA_USER_PORTAL",OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void initialization(String[] butName) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		//对数组中每个元素分别进行储存
		for(int i=0;i<butName.length;i++){
			//通过组件ID获取相应的组件
			OaPortalWidget portalWidget = userPortalDAO.queryPortalWidgetById(butName[i]);
			//获取所有用户
			List<User> userAccountList = userPortalDAO.queryAllUser();
			List<String> userAccount = new ArrayList<String>();
			for(int k=0;k<userAccountList.size();k++){
				userAccount.add(userAccountList.get(k).getAccount());
			}
			//获取所有当前组件ID的所有的员工数据
			List<OaUserPortal> portalUserList = userPortalDAO.queryPortalUser(butName[i]);
			List<String> portalUser = new ArrayList<String>();
			for(int k=0;k<portalUserList.size();k++){
				portalUser.add(portalUserList.get(k).getAccount());
			}
			//取出除了现有ID的员工以外的其他没有此组件的员工account
			if(portalUser!=null && portalUser.size()>0 && userAccount!=null && userAccount.size()>0){
				for(int m=0;m<userAccount.size();m++){
					if(portalUser.contains(userAccount.get(m))){
						userAccount.remove(m);
						m--;
					}
				}
			}
			//集合相减完毕,如果还有剩下的人员,就进行循环保存,如果没有,就直接通过
			if(userAccount!=null && userAccount.size()>0){
				List<OaUserPortal> portals = new ArrayList<OaUserPortal>();
				for(int j=0;j<userAccount.size();j++){
					//做一个个人组件的实体,用来最后保存用
					OaUserPortal userPortalSave = new OaUserPortal();
					userPortalSave.setWidget(portalWidget.getId());//组件ID
					userPortalSave.setName(portalWidget.getName());//昵称默认组件名称
					userPortalSave.setHospitalCode(HisParameters.CURRENTHOSPITALID);//所属医院
					userPortalSave.setCreateUser(longinUserAccount);
					userPortalSave.setAccount(userAccount.get(j));//循环存入人员
					userPortalSave.setCreateTime(new Date());
					userPortalSave.setOrder(-1);//顺序号默认为-1
					portals.add(userPortalSave);
					if(j % 100 == 0 || j == userAccount.size()-1){
						userPortalDAO.saveOrUpdateList(portals);
						portals.clear();
					}
				}
				OperationUtils.getInstance().conserve(null,"OA系统首页管理","INSERT INTO","T_OA_USER_PORTAL",OperationUtils.LOGACTIONINSERT);
			}
		}
	}

	@Override
	public void enableUserWidget(OaUserPortal userPortal) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isNotBlank(userPortal.getId())){
			OaUserPortal oaUserPortal=userPortalDAO.getById(userPortal.getId());
			oaUserPortal.setOrder(-1);//组件顺序
			oaUserPortal.setLocal(0);//个人组件状态
			oaUserPortal.setUpdateTime(new Date());
			oaUserPortal.setUpdateUser(longinUserAccount);
			userPortalDAO.save(oaUserPortal);
			OperationUtils.getInstance().conserve(userPortal.getId(),"OA系统首页管理","UPDATE","T_OA_USER_PORTAL",OperationUtils.LOGACTIONUPDATE);
		}
	}

	@Override
	public List<Information> getInformationList(String type) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//当前登录人
		return userPortalDAO.getInformationList(type,longinUserAccount);
	}

	@Override
	public List<Information> getInformationCheck() {
		SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();//当前登录人
		String deptId=employee.getDeptId().getId();
		if(StringUtils.isNotBlank(deptId)){
			SysDepartment dept = userPortalDAO.getDeptForId(deptId);
			if(dept!=null && StringUtils.isNotBlank(dept.getDeptCode())){
				employee.setDeptCode(dept.getDeptCode());
			}
		}
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();//当前登录人
		return userPortalDAO.getInformationCheck(employee,user);
	}

	@Override
	public String queryWidgetForUser(String longinUserAccount, String moudelId) {
		return userPortalDAO.queryWidgetForUser(longinUserAccount,moudelId);
	}

	@Override
	public List<Schedule> qeryScheduleList(String userAccount) {
		return userPortalDAO.qeryScheduleList(userAccount);
	}

	@Override
	public MApkVersion getVersion() {
		return userPortalDAO.getVersion();
	}

	@Override
	public List<OaTaskInfo> getListForTask(String account, String tenantId) {
		return userPortalDAO.getListForTask(account,tenantId);
	}
}
