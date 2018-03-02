package cn.honry.oa.personalAddressList.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OperationItem;
import cn.honry.base.bean.model.PersonalAddressList;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.oa.personalAddressList.dao.PersonalAddressListDao;
import cn.honry.oa.personalAddressList.service.PersonalAddressListService;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

/**  
 *  
 * @className：PersonalAddressListServiceImpl
 * @Description：  个人通讯录
 * @Author：zxl
 * @CreateDate：2017-7-17 下午18:56:31  
 * @Modifier：zxl
 * @ModifyDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("personalAddressListService")
@Transactional
@SuppressWarnings({ "all" })
public class PersonalAddressListServiceImpl implements PersonalAddressListService{

	@Autowired
	@Qualifier(value = "personalAddressListDao")
	private PersonalAddressListDao personalAddressListDao;


	/**  
	 *  获取通讯录树
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： groupCode 
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> QueryTree(String groupCode) {
		String userAccount =ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		List<TreeJson> treeJsonList=new ArrayList<TreeJson>();
		TreeJson noregisterTreeJson=null;
		TreeJson registerTreeJson=null;
		TreeJson CancelTreeJson=null;
		if(StringUtils.isBlank(groupCode)){
			//获取所有分组
			TreeJson topTreeJson = new TreeJson();
			topTreeJson.setId("root");
			topTreeJson.setText("个人通讯录");
			topTreeJson.setIconCls("icon-branch");
			topTreeJson.setState("open");
			treeJsonList.add(topTreeJson);
			List<PersonalAddressList> list=personalAddressListDao.queryAllGroup(groupCode,userAccount);
			for(int i=0;i<list.size();i++){
				noregisterTreeJson = new TreeJson();
				noregisterTreeJson.setId(list.get(i).getId());
				noregisterTreeJson.setText(list.get(i).getGroupName());
				noregisterTreeJson.setIconCls("icon-branch");
				Map<String,String> noregisterMap = new HashMap<String,String>();
				noregisterMap.put("pid", list.get(i).getParentCode());
				noregisterTreeJson.setAttributes(noregisterMap);
				noregisterTreeJson.setState("closed");
				treeJsonList.add(noregisterTreeJson);
			}
			return TreeJson.formatTree(treeJsonList);
		}
		return treeJsonList;
	}

	/**  
	 *  保存分组
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： personalAddress 个人通讯录实体 
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOrUpdateTree(PersonalAddressList personalAddress) {
		//获取当前登录用户
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		//获取当前登陆科室
		SysDepartment dept=(SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		//拼音五笔码的处理
		String str = personalAddressListDao.getSpellCode(personalAddress.getGroupName());
		String[] newStr=str.split("\\$");
		if(StringUtils.isBlank(personalAddress.getId())){//保存
			//添加分组
			personalAddress.setId(null);
			personalAddress.setCreateUser(user.getAccount());
			personalAddress.setCreateDept(dept.getDeptCode());
			personalAddress.setPerPinyin(newStr[0]);
			personalAddress.setPerWb(newStr[1]);
			if(!"root".equals(personalAddress.getParentCode())){
				PersonalAddressList  personal=personalAddressListDao.get(personalAddress.getParentCode());
				personalAddress.setParentUppath(personal.getParentUppath()+","+personalAddress.getParentCode());
				personalAddress.setBelongGroupName(personal.getGroupName());
			}else{
				personalAddress.setParentUppath("root");
			}
			personalAddress.setStop_flg(0);
			personalAddress.setDel_flg(0);
			personalAddress.setIfGroup(1);
			personalAddress.setCreateTime(new Date());
			personalAddress.setBelongUser(user.getAccount());
			personalAddressListDao.save(personalAddress);
			OperationUtils.getInstance().conserve(null, "个人通讯录", "INSERT INTO", "T_OA_PERSONALADDRESSLIST", OperationUtils.LOGACTIONINSERT);
		}else{//修改分组
			PersonalAddressList  personal=personalAddressListDao.get(personalAddress.getId());
			if(personal!=null){
				personal.setGroupName(personalAddress.getGroupName());
				personal.setPerPinyin(newStr[0]);
				personal.setPerWb(newStr[1]);
				personal.setBelongUser(user.getAccount());
				personalAddressListDao.update(personal);
				OperationUtils.getInstance().conserve(personalAddress.getId(), "个人通讯录", "UPDATE", "T_OA_PERSONALADDRESSLIST", OperationUtils.LOGACTIONUPDATE);
			}	
		}
	}

	/**  
	 *  删除分组
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id 分组id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void delGroup(String id) throws Exception {
		personalAddressListDao.delGroupById(id);
	}

	/**  
	 * 获取总条数
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 下午8:01:07 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 下午8:01:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:parentCode 上级id
	 * @param:queryName 封装查询参数
	 * @throws:
	 * @return: 
	 *
	 */	
	@Override
	public int getPersonalTotal(String parentCode, String queryName) {
		return personalAddressListDao.getPersonalTotal(parentCode,queryName);
	}

	/**  
	 * 获取当前页数据
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 下午8:01:07 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 下午8:01:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:parentCode 上级id
	 * @param:queryName 封装查询参数
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PersonalAddressList> getPersonalLists(String page, String rows,
			String parentCode, String queryName) {
		return personalAddressListDao.getPersonalLists(page,rows,parentCode,queryName);
	}

	/**  
	 *  保存个人信息
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： personalAddress 个人通讯录实体 
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void savePersonalAddress(PersonalAddressList personalAddress) {
		//获取当前登录用户
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		//获取当前登陆科室
		SysDepartment dept=(SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		//拼音五笔码的处理
		String str = personalAddressListDao.getSpellCode(personalAddress.getPerName());
	    int order =personalAddressListDao.getMaxOrder();
		String[] newStr=str.split("\\$");
		if(StringUtils.isBlank(personalAddress.getId())){//保存
			//添加个人信息
			personalAddress.setId(null);
			personalAddress.setCreateUser(user.getAccount());
			personalAddress.setCreateDept(dept.getDeptCode());
			personalAddress.setPerPinyin(newStr[0]);
			personalAddress.setPerWb(newStr[1]);
			if(!"root".equals(personalAddress.getParentCode())){
				PersonalAddressList  personal=personalAddressListDao.get(personalAddress.getParentCode());
				personalAddress.setParentUppath(personal.getParentUppath()+","+personalAddress.getParentCode());
				personalAddress.setBelongGroupName(personal.getGroupName());
			}else{
				personalAddress.setParentUppath("root");
			}
			personalAddress.setPerOrder(order+1);
			personalAddress.setStop_flg(0);
			personalAddress.setDel_flg(0);
			personalAddress.setIfGroup(0);
			personalAddress.setCreateTime(new Date());
			personalAddress.setBelongUser(user.getAccount());
			personalAddressListDao.save(personalAddress);
			OperationUtils.getInstance().conserve(null, "个人通讯录", "INSERT INTO", "T_OA_PERSONALADDRESSLIST", OperationUtils.LOGACTIONINSERT);
		}else{//修改个人信息
			PersonalAddressList  personal=personalAddressListDao.get(personalAddress.getId());
			if(personal!=null){
				personal.setPerPinyin(newStr[0]);
				personal.setPerWb(newStr[1]);
				personal.setPerName(personalAddress.getPerName());
				personal.setMobilePhone(personalAddress.getMobilePhone());
				personal.setPerSex(personalAddress.getPerSex());
				personal.setPerRemark(personalAddress.getPerRemark());
				personal.setPerBirthday(personalAddress.getPerBirthday());
				personal.setWorkPhone(personalAddress.getWorkPhone());
				personal.setPerEmail(personalAddress.getPerEmail());
				personal.setPerInputCode(personalAddress.getPerInputCode());
				personal.setPerAddress(personalAddress.getPerAddress());
				personalAddressListDao.update(personal);
				OperationUtils.getInstance().conserve(personalAddress.getId(), "个人通讯录", "UPDATE", "T_OA_PERSONALADDRESSLIST", OperationUtils.LOGACTIONUPDATE);
			}	
		}
		
	}

	/**  
	 * 根据id获取单条信息
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public PersonalAddressList personalAddressById(String id) {
		return personalAddressListDao.get(id);
	}

	/**  
	 * 根据id删除个人信息
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void delPersonalMes(String id) throws Exception{
		personalAddressListDao.delPersonalMes(id);
	}

	@Override
	public List<PersonalAddressList> findAllGroup(String parentCode) {
		String userAccount =ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		return personalAddressListDao.findAllGroup(userAccount);
	}

	@Override
	public void movePersonalToGroup(String id, String parentCode) throws Exception {
		//获取当前登录用户
		String userAccount =ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		PersonalAddressList  personal=new PersonalAddressList();
		if(StringUtils.isNotBlank(parentCode)&&!"root".equals(parentCode)){
			personal=personalAddressListDao.get(parentCode);
		}else{
			personal.setParentCode("root");
			personal.setParentUppath("root");
			personal.setGroupName("个人通讯录");
		}
		personalAddressListDao.movePersonalToGroup(id,personal);
				
	}
}
