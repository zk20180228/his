package cn.honry.oa.activitiDept.service.impl;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.PersonalAddressList;
import cn.honry.base.bean.model.PublicAddressBook;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.oa.activitiDept.dao.ActivitiDeptDAO;
import cn.honry.oa.activitiDept.service.ActivitiDeptService;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

@Service("activitiDeptService")
@Transactional
@SuppressWarnings({ "all" })
public class ActivitiDeptServiceImpl implements ActivitiDeptService{

	@Autowired
	@Qualifier(value = "activitiDeptDAO")
	private ActivitiDeptDAO activitiDeptDAO;

	@Override
	public OaActivitiDept get(String id) {
		return activitiDeptDAO.get(id);
	}

	@Override
	public void removeUnused(String id) {
	}

	@Override
	public void saveOrUpdate(OaActivitiDept entity) {
	}

	@Override
	public List<SysDepartment> queryDept() {
		return activitiDeptDAO.queryDept();
	}

	@Override
	public List<OaActivitiDept> queryActivitiDept() {
		return activitiDeptDAO.queryActivitiDept();
	}

	@Override
	public void initialization(String[] butName) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		//首先把工作流科室表里面的数据删除掉(物理删除,否则会越来越多垃圾数据)
		activitiDeptDAO.delActivitiDept();
		//查询所有科室的map,将code,name,order放入map
		List<SysDepartment> deptlist=activitiDeptDAO.queryAllDept();
		Map<String , String> deptMap = new HashMap<String, String>();
		for(int i=0;i<deptlist.size();i++){
			deptMap.put(deptlist.get(i).getDeptCode()+"Code", deptlist.get(i).getDeptCode());
			deptMap.put(deptlist.get(i).getDeptCode()+"Name", deptlist.get(i).getDeptName());
			deptMap.put(deptlist.get(i).getDeptCode()+"Order", deptlist.get(i).getDeptOrder().toString());
		}
		//保存工作流科室数据进数据库
		List<OaActivitiDept> activitiDeptList = new ArrayList<OaActivitiDept>();
		for(int i=0;i<butName.length;i++){
			OaActivitiDept activitiDept = new OaActivitiDept();
			activitiDept.setDeptCode(butName[i]);
			activitiDept.setDeptName(deptMap.get(butName[i]+"Name"));
//			if(StringUtils.isNotBlank(deptMap.get(butName[i]+"Order"))){
//				activitiDept.setDeptOrder(Integer.valueOf(deptMap.get(butName[i]+"Order")));
//			}
			activitiDept.setCreateUser(longinUserAccount);
			activitiDept.setCreateTime(new Date());	
			activitiDeptList.add(activitiDept);
			if(i % 100 == 0 || i == butName.length-1){
				activitiDeptDAO.saveOrUpdateList(activitiDeptList);
				activitiDeptList.clear();
			}
		}
		OperationUtils.getInstance().conserve(null,"OA工作流管理","INSERT INTO","T_OA_ACTIVITI_DEPT",OperationUtils.LOGACTIONINSERT);
	}
	
	
	@Override
	public List<OaActivitiDept> getPage(String page, String rows,OaActivitiDept entity) {
		return activitiDeptDAO.getPage(page, rows,entity);
	}

	@Override
	public int getTotal(OaActivitiDept entity) {
		return activitiDeptDAO.getTotal(entity);
	}

	@Override
	public void saveActivitiDeptTree(String dId) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		//首先把工作流科室表里面的数据删查出来,不用保存的科室
		List<OaActivitiDept> activitiDeptList = activitiDeptDAO.queryActivitiDept();
		Map<String , String> deptMap = new HashMap<String, String>();
		for(int i=0;i<activitiDeptList.size();i++){
			deptMap.put(activitiDeptList.get(i).getDeptCode(), activitiDeptList.get(i).getDeptName());
		}
		//切割dId拿到数组
		String[] dIds = dId.split(",");
		for(int i=0;i<dIds.length;i++){//循环判断拿过来的科室ID
			//首先先查询出来id对应的科室
			SysDepartment department = activitiDeptDAO.queryDeptById(dIds[i]);
			if(department!=null){
				String ActiName=deptMap.get(department.getDeptCode());
				//如果ActiName为空,说明工作流科室表里面没有此科室,需要添加
				if(ActiName==null || "".equals(ActiName)){
					OaActivitiDept activitiDept = new OaActivitiDept();
					activitiDept.setDeptCode(department.getDeptCode());//code
					activitiDept.setDeptName(department.getDeptName());//名称
//					activitiDept.setDeptBrev(department.getDeptBrev());//简称
//					activitiDept.setDeptEname(department.getDeptEname());//英文
//					activitiDept.setDeptAddress(department.getDeptAddress());//部门地点
//					activitiDept.setDeptPinyin(department.getDeptPinyin());//拼音码
//					activitiDept.setDeptWb(department.getDeptWb());//五笔码
//					activitiDept.setDeptInputcode(department.getDeptInputcode());//自定义码
//					activitiDept.setDeptOrder(department.getDeptOrder());//排序
//					activitiDept.setDeptType(department.getDeptType());//部门分类
//					activitiDept.setDeptRemark(department.getDeptRemark());//备注
//					activitiDept.setAreaCode(department.getAreaCode());//院区编码
//					activitiDept.setAreaName(department.getAreaName());//院区名称
					activitiDept.setCreateUser(longinUserAccount);
					activitiDept.setCreateTime(new Date());
					activitiDeptDAO.save(activitiDept);
				}
			}
		}
		OperationUtils.getInstance().conserve(null,"OA工作流管理","INSERT INTO","T_OA_ACTIVITI_DEPT",OperationUtils.LOGACTIONINSERT);
	}

	@Override
	public void delActivitiDept(String dId) {
		String[] ids = dId.split(",");
		for(int i=0;i<ids.length;i++){
			activitiDeptDAO.delete(ids[i]);
		}
		OperationUtils.getInstance().conserve(dId,"OA工作流管理_工作流科室","UPDATE","T_OA_ACTIVITI_DEPT",OperationUtils.LOGACTIONDELETE);
	}
	
	/**  
	 *  获取工作流科室树
	 * @Author：zpty
	 * @CreateDate：2017-8-21 上午11:21:55 
	 * @param： did 
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> QueryTree(String type) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<OaActivitiDept> list=activitiDeptDAO.queryAllTreeDept();
		//加入树的根节点
		TreeJson pTreeJson = new TreeJson();
		pTreeJson.setId("root");
		pTreeJson.setText("工作流科室");
		Map<String,String> rAttributes = new HashMap<String, String>();
		pTreeJson.setAttributes(rAttributes);
		treeJsonList.add(pTreeJson);
		if (list!=null && list.size()>0) {
			for (OaActivitiDept dept : list) {
					TreeJson treeJson = new TreeJson();
					treeJson.setId(dept.getId());
					treeJson.setText(dept.getDeptName());
					Map<String,String> attributes = new HashMap<String, String>();
					attributes.put("pid",dept.getParentCode());
					treeJson.setAttributes(attributes);
					treeJsonList.add(treeJson);
			}
		}
		return TreeJson.formatTree(treeJsonList);
	}

	@Override
	public String searchDouble(OaActivitiDept activitiDept) {
		return activitiDeptDAO.searchDouble(activitiDept);
	}

	@Override
	public void saveActivitiDept(String dId, OaActivitiDept activitiDept) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String longinUserDept = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode();
		if(StringUtils.isNotBlank(activitiDept.getId()) && !"".equals(activitiDept.getId())){//如果实体id存在说明是修改方法
			//修改方法,层级不用改变,只改变内容
			OaActivitiDept oldDept=activitiDeptDAO.get(activitiDept.getId());//查出实体
			oldDept.setDeptCode(activitiDept.getDeptCode());//科室编码
			oldDept.setDeptName(activitiDept.getDeptName());//科室名称
			oldDept.setDeptOrder(activitiDept.getDeptOrder());//顺序号
			oldDept.setDeptType(activitiDept.getDeptType());//是否停用
			oldDept.setUpdateUser(longinUserAccount);
			oldDept.setUpdateTime(new Date());
			activitiDeptDAO.save(oldDept);//保存
			OperationUtils.getInstance().conserve(activitiDept.getId(),"OA工作流管理_工作流科室","UPDATE","T_OA_ACTIVITI_DEPT",OperationUtils.LOGACTIONDELETE);
		}else{//新增方法
			activitiDept.setId(null);
			//保存层级
			if(StringUtils.isNotBlank(dId) && !"".equals(dId) && !"root".equals(dId)){
				OaActivitiDept oldDept=activitiDeptDAO.get(dId);//查出父节点实体
				activitiDept.setParentCode(oldDept.getId());
				activitiDept.setParentUppath(oldDept.getParentUppath()+","+oldDept.getId());
			}else{
				activitiDept.setParentCode("root");
				activitiDept.setParentUppath("root");
			}
			activitiDept.setCreateUser(longinUserAccount);
			activitiDept.setCreateDept(longinUserDept);
			activitiDept.setCreateTime(new Date());
			activitiDeptDAO.save(activitiDept);//保存
			OperationUtils.getInstance().conserve(null,"OA工作流管理","INSERT INTO","T_OA_ACTIVITI_DEPT",OperationUtils.LOGACTIONINSERT);
		}
		
	}
	
}
