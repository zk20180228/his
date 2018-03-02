package cn.honry.inner.baseinfo.stack.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.bean.model.Hospital;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.stack.dao.StackInInterDAO;
import cn.honry.inner.baseinfo.stack.service.StackInInterService;
import cn.honry.inner.baseinfo.stack.vo.StackAndStockInfoInInterVo;
import cn.honry.inner.baseinfo.stackInfo.dao.StackinfoInInterDAO;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("stackInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class StackServiceInInterImpl implements StackInInterService{
	@Autowired
	private StackInInterDAO stackInInterDAO;
	@Autowired
	private StackinfoInInterDAO stackinfoInInterDAO;
	@Autowired
	private DeptInInterDAO deptInInterDAO;//部门dao
	
	@Autowired
	@Qualifier(value="stackInInterDAO")
	private StackInInterDAO stackDAO;
	
	public void removeUnused(String id) {
		
	}

	
	public BusinessStack get(String id) {
		return stackInInterDAO.get(id);
	}

	
	public void batchDel(String tablName, String ids, String idName) {
		stackInInterDAO.batchDel(tablName, ids, idName);
	}

	
	public boolean save(BusinessStack entity, String stackInfosJson) throws Exception{
		return stackInInterDAO.saveOrUpdate(entity,stackInfosJson);
	}

	
	public List<BusinessStack> getPage(String page, String rows,BusinessStack entity) {
		return stackInInterDAO.getPage(page, rows,entity);
	}

	
	public int getTotal(BusinessStack entity) {
		return stackInInterDAO.getTotal(entity);
	}

	
	public void saveOrUpdate(BusinessStack entity) {
	}

	
	public List stackInfoName(String page, String rows) {
		return stackInInterDAO.stackInfoName(page,rows);
	}


	
	public void saveOrupdataBusinessStack(BusinessStack businessStack) {
		stackInInterDAO.saveOrupdataBusinessStack(businessStack);
	}

	
	public void deleteBusinessStack(BusinessStack businessStack) {
		stackInInterDAO.deleteBusinessStack(businessStack);
	}

	
	public BusinessStack getBusinessStackById(BusinessStack businessStack) {
		return stackInInterDAO.getBusinessStackById(businessStack);
	}

	@Override
	public List<TreeJson> findDeptTree(String id) {
		List<BusinessStack> businessStackList = new ArrayList<BusinessStack>();
		SysDepartment department = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if (StringUtils.isBlank(id)) {
			// 科室信息
			TreeJson pTreeJson = new TreeJson();
			if(department.getDeptName()==null){
				pTreeJson.setText("科室");
				treeJsonList.add(pTreeJson);
			}else{
				pTreeJson.setId(department.getId());
				pTreeJson.setText(department.getDeptName());
				pTreeJson.setIconCls("icon-branch");
				treeJsonList.add(pTreeJson);
			}
		}
		businessStackList = stackInInterDAO.getDepartmentById(department.getId());
		if (businessStackList != null && businessStackList.size() > 0) {
			for (BusinessStack businessStack : businessStackList) {
				TreeJson treeJson = new TreeJson();
				treeJson.setId(businessStack.getId());
				treeJson.setText(businessStack.getName());
				Map<String, String> attributes = new HashMap<String, String>();
				attributes.put("pid", department.getId());
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		return TreeJson.formatTree(treeJsonList);
	}
	
	public List<TreeJson> findTree() {
		User user2 = (User) SessionUtils.getCurrentUserFromShiroSession();
		SysDepartment department = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptId=department.getDeptCode();
		String userId=user2.getAccount();
		List<SysDepartment> sysDepartmentList =deptInInterDAO.findTree(true,"");
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		String sType="";
		TreeJson topTreeJson = new TreeJson();
		topTreeJson.setId("1");
		topTreeJson.setText("科室信息");
		topTreeJson.setIconCls("icon-branch");
		Map<String,String> tAttMap = new HashMap<String, String>();
		topTreeJson.setAttributes(tAttMap);
		treeJsonList.add(topTreeJson);
		if(sysDepartmentList!=null&&sysDepartmentList.size()>0){
			String curType="";
			for(SysDepartment sysDepartment : sysDepartmentList){
				TreeJson treeJson = new TreeJson();
				if(sysDepartment.getDeptType()!=null && !sysDepartment.getDeptType().equals(curType)){
					TreeJson typeTreeJson = new TreeJson();
					curType=sysDepartment.getDeptType();
					typeTreeJson.setId("type_"+curType);
					if(curType.equals("C")){
						typeTreeJson.setText("门诊");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-vcard");
					}else if(curType.equals("I")){
						typeTreeJson.setText("住院");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-application_side_list");
					}else if(curType.equals("F")){
						typeTreeJson.setText("财务");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-coins");
					}else if(curType.equals("L")){
						typeTreeJson.setText("后勤");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-report");
					}else if(curType.equals("PI")){
						typeTreeJson.setText("药库");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("T")){
						typeTreeJson.setText("医技");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-application_side_list");
					}else if(curType.equals("D")){
						typeTreeJson.setText("行政");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-client");
					}else if(curType.equals("P")){
						typeTreeJson.setText("药房");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-brick");
					}else if(curType.equals("N")){
						typeTreeJson.setText("护士站");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("S")){
						typeTreeJson.setText("科研");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("OP")){
						typeTreeJson.setText("手术");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("U")){
						typeTreeJson.setText("科室");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("O")){
						typeTreeJson.setText("其他");
						typeTreeJson.setState("closed");
						typeTreeJson.setIconCls("icon-bullet_home");
					}
					if("".equals(sType)){
						sType=curType;
					}else{
						sType+=","+curType;
					}
					Map<String,String> attributes = new HashMap<String, String>();
					attributes.put("pid","1");
					attributes.put("hasson","1");
					attributes.put("deptCode","code_"+curType);
					typeTreeJson.setAttributes(attributes);
					treeJsonList.add(typeTreeJson);							
				}
				
				treeJson.setId(sysDepartment.getId());
				treeJson.setText(sysDepartment.getDeptName());

				if(sysDepartment.getDeptHasson()==0){
					treeJson.setIconCls("icon-user_brown");
				}
				
				Map<String,String> attributes = new HashMap<String, String>();
				if(sysDepartment.getDeptLevel()==1){
					attributes.put("pid","type_"+curType);
				}else{
					attributes.put("pid",sysDepartment.getDeptParent());
				}
				attributes.put("isNo","0");
				attributes.put("hasson",sysDepartment.getDeptHasson().toString());
				attributes.put("deptCode",sysDepartment.getDeptCode().toString());
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
				List<BusinessStack> businessStackList = stackInInterDAO.getSysDepartmentById(sysDepartment.getId());
				if(businessStackList!=null){
					for(BusinessStack businessStack : businessStackList){
						TreeJson treeChJson = new TreeJson();
						treeChJson.setId(businessStack.getId());
						treeChJson.setText(businessStack.getName());
						Map<String,String> attributesCh = new HashMap<String, String>();
						attributesCh.put("pid",sysDepartment.getId());
						attributesCh.put("isNo","1");
						attributesCh.put("hasson","false");
						treeChJson.setAttributes(attributesCh);
						treeJsonList.add(treeChJson);
                   }
			    }
		     }
		  }
		if((","+sType+",").indexOf(",C,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_C");
			treeJson.setText("门诊");
			treeJson.setIconCls("icon-vcard");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_C");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",I,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_I");
			treeJson.setText("住院");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_I");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",F,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_F");
			treeJson.setText("财务");
			treeJson.setIconCls("icon-coins");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_F");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",L,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_L");
			treeJson.setText("后勤");
			treeJson.setIconCls("icon-report");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_L");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",PI,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_PI");
			treeJson.setText("药库");
			treeJson.setIconCls("icon-bullet_home");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_PI");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",T,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_T");
			treeJson.setText("医技");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_T");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",D,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_D");
			treeJson.setText("行政");
			treeJson.setIconCls("icon-client");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_D");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",P,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_P");
			treeJson.setText("药房");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_P");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",N,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_N");
			treeJson.setText("护士站");
			treeJson.setIconCls("icon-application_cascade");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_N");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",S,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_S");
			treeJson.setText("科研");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_S");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",OP,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_OP");
			treeJson.setText("手术");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_OP");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",U,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_U");
			treeJson.setText("科室");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_U");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		
		if((","+sType+",").indexOf(",O,")==-1){
			TreeJson treeJson = new TreeJson();
			treeJson.setId("type_O");
			treeJson.setText("其他");
			treeJson.setIconCls("icon-application_side_list");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("pid","1");
			attributes.put("hasson","0");
			attributes.put("deptCode","code_O");
			treeJson.setAttributes(attributes);
			treeJsonList.add(treeJson);
		}
		return TreeJson.formatTree(treeJsonList);
	}

	public String findDept(String id) {
		BusinessStack businessStack=stackDAO.get(id);
		return businessStack.getDeptId();
	}
	/**  
	 *  
	 * @Description：  保存组套
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-13 下午01:54:10  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-13 下午01:54:10  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public void saveRecipedetailStack(String type,BusinessStack businessStack,String json) {
		Gson gson = new Gson();  
		List<BusinessStackinfo> infoList = gson.fromJson(json, new TypeToken<List<BusinessStackinfo>>(){}.getType());
		List<BusinessStackinfo> businessStackinfoList = new ArrayList<BusinessStackinfo>();
		BusinessStack stack = new BusinessStack();
		Hospital hospital = new Hospital();
		hospital.setId(1);
		stack.setHospitalId(hospital);
		stack.setType(Integer.parseInt(type));
		stack.setDeptId("");//科室id
		stack.setName("");//组套名称
		stack.setPinYin("");
		stack.setWb("");
		stack.setInputCode("");
		stack.setParent("");//父级
		stack.setOrder(0);//排序
		stack.setLevel("");//层级
		stack.setPath("");//层级路径
		stack.setDoc("");//组套医师
		stack.setSource("");//组套来源 
		stack.setShareFlag(1);//是否共享
		stack.setRemark("");//备注
		stackInInterDAO.save(stack);
		BusinessStackinfo businessStackinfo = null;
		for(BusinessStackinfo info : infoList){
			businessStackinfo = new BusinessStackinfo();
			businessStackinfo.setStackId(stack);
			businessStackinfo.setStackInfoItemId("");
			businessStackinfo.setIsDrug(Integer.parseInt(type));
			businessStackinfo.setStackInfoNum(info.getStackInfoNum());
			businessStackinfo.setStackInfoUnit(info.getStackInfoUnit());
			businessStackinfo.setStackInfoDeptid(info.getStackInfoDeptid());
			businessStackinfo.setStackInfoRemark(info.getStackInfoRemark());
			businessStackinfo.setStackInfoOrder(1);
			businessStackinfo.setCombNo("");
			businessStackinfo.setTypeCode(info.getTypeCode());
			businessStackinfo.setFrequencyCode(info.getFrequencyCode());
			businessStackinfo.setUsageCode(info.getUsageCode());
			businessStackinfo.setOnceDose(info.getOnceDose());
			businessStackinfo.setDoseUnit(info.getDoseUnit());
			businessStackinfo.setDays(info.getDays());
			businessStackinfo.setMainDrug(info.getMainDrug());
			businessStackinfo.setItemNote(info.getItemNote());
			businessStackinfo.setDateBgn(info.getDateBgn());
			businessStackinfo.setDateEnd(info.getDateEnd());
			businessStackinfo.setRemark(info.getRemark());
			businessStackinfo.setRemarkComb(info.getRemarkComb());
			businessStackinfo.setIntervaldays(info.getIntervaldays());
			businessStackinfoList.add(businessStackinfo);
		}
		stackinfoInInterDAO.saveOrUpdateList(businessStackinfoList);
	}

	
	public int countDrugAndUndrug(String page, String rows) {
		return stackInInterDAO.countDrugAndUndrug(page,rows);
	}

	
	@Override
	public List<SysDepartment> queryDept(String name) {
		return stackInInterDAO.queryDept(name);
	}


	@Override
	public List<BusinessDictionary> packagingUnitcomboBox(String pid, String uid,String name,String stackId) {
		return stackInInterDAO.packagingUnitcomboBox(pid,uid,name,stackId);
	}


	@Override
	public List<SysDepartment> getDeptName(String queryName) {
		return stackInInterDAO.getDeptName(queryName);
	}
	
	/**
	 * 组套树
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 上午9:37:32
	 * @param： id 组套的id deptId 登录科室的id userid 登录人的id  stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<TreeJson> stackTree(String id,String deptId,String userId,String stackObject,String remark) {
		List<TreeJson> trJsons = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			Map<Integer, String> stackMap=new HashMap<Integer, String>();
			stackMap.put(0, "个人");
			stackMap.put(1, "科室");
			stackMap.put(2, "全院");
			
			TreeJson json=null;
			Map<String, String> map=null;
			for (int i = 0; i < 3; i++) {
				json=new TreeJson();
				map=new HashMap<String, String>();
				json.setId((i+1)+"");
				json.setText(stackMap.get(i));
				json.setState("closed");
				if(i==0){
					json.setIconCls("icon-03");
				}
				if(i==1){
					json.setIconCls("icon-03");
				}
				if(i==2){
					json.setIconCls("icon-2012081511913");
				}
				map.put("id", "root");
				map.put("isOpen", "0");
				map.put("infoId", "cc");
				json.setAttributes(map);
				trJsons.add(json);
			}
		}else{
			TreeJson tjson=null;
			List<BusinessStack> stackList = new ArrayList<BusinessStack>();
			if(!"1".equals(id)&&!"2".equals(id)&&!"3".equals(id)){
				BusinessStack businessStack = new BusinessStack();
				businessStack.setId(id);
				BusinessStack stack = stackInInterDAO.getBusinessStackById(businessStack);
				if(stack.getStackInpmertype()!=null){
					stackList= stackInInterDAO.getStackInfo(stack.getSource(),deptId,userId,stack.getStackObject().toString(),stack.getStackInpmertype().toString(),id);
				}
			}else{
				stackList= stackInInterDAO.getStackInfo(id,deptId,userId,stackObject,remark,"ROOT");
			}
			if(stackList!=null&&stackList.size()>0){
				Map<String, String> map = null;
				for (BusinessStack ck : stackList) {
					String stackInpmertype= "";
					if(ck.getStackInpmertype()==null){
						stackInpmertype = null;
					}else{
						stackInpmertype=ck.getStackInpmertype().toString();
					}
					List<BusinessStack> list = stackInInterDAO.getStackInfo(ck.getSource(),deptId,userId,ck.getStackObject().toString(),stackInpmertype,ck.getId());
					tjson=new TreeJson();
					map = new HashMap<String,String>();
					tjson.setId(ck.getId());
					tjson.setText(ck.getName());
					if(list.size()>0){ 
						tjson.setState("closed");
						map.put("isOPen", "0");
						map.put("stack", "1");
					}else{
						tjson.setState("open");
						map.put("isOPen", "1");
						map.put("stack", "1");
					}
					map.put("id", id);
					tjson.setAttributes(map);
					trJsons.add(tjson);
				}
			}
		}
		return trJsons;
	}
	/**
	 * 渲染频次
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月12日 上午9:09:35 
	 * @param：   
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessFrequency> getFreq() {
		return stackInInterDAO.getFreq();
	}
	/**
	 * 查看药品组套详情
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 下午2:59:13
	 * @param： id 组套的编号   drugstoreId 诊断的选择的药房 feelType 是否是收费类型
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<StackAndStockInfoInInterVo> queryStackInfoById(String id,String drugstoreId,String feelType) {
		return stackInInterDAO.queryStackInfoById(id,drugstoreId,feelType);
	}


	@Override
	public List<BusinessStack> getstackNameParam(String stackName,String stackObject,String remark) {
		return stackInInterDAO.getstackNameParam(stackName,stackObject,remark);
	}
}
