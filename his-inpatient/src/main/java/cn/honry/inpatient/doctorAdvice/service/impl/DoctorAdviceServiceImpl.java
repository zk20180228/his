package cn.honry.inpatient.doctorAdvice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.doctorAdvice.dao.DoctorAdviceDAO;
import cn.honry.inpatient.doctorAdvice.dao.InpatientDrugbilldetailDAO;
import cn.honry.inpatient.doctorAdvice.dao.InpatientExecbillDAO;
import cn.honry.inpatient.doctorAdvice.dao.UnDrugInfoDAO;
import cn.honry.inpatient.doctorAdvice.service.DoctorAdviceService;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
@Service("doctorAdviceService")
@Transactional
@SuppressWarnings({ "all" })
public class DoctorAdviceServiceImpl implements DoctorAdviceService {
	
	@Autowired
	@Qualifier(value = "doctorAdviceDAO")
	private DoctorAdviceDAO doctorAdviceDAO;
	@Autowired
	@Qualifier(value = "inpatientExecbillDAO")
	private InpatientExecbillDAO inpatientExecbillDAO;
	@Autowired
	@Qualifier(value = "inpatientDrugbilldetailDAO")
	private InpatientDrugbilldetailDAO inpatientDrugbilldetailDAO;
	
	@Autowired
	@Qualifier(value = "unDrugInfoDAO")
	private UnDrugInfoDAO unDrugInfoDAO;
	
	@Autowired
	@Qualifier(value = "undrugInInterDAO")
	private UndrugInInterDAO undrugInInterDAO;
	
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	
	@Autowired
	@Qualifier(value="drugInfoInInerDAO")
	private DrugInfoInInerDAO drugInfoInInerDAO;

	@Override
	public void removeUnused(String id) {		

	}
	@Autowired
	@Qualifier(value="inprePayDAO")
	private InprePayDAO inprePayDAO;
	
	public void setInprePayDAO(InprePayDAO inprePayDAO) {
		this.inprePayDAO = inprePayDAO;
	}

	@Override
	public InpatientInfo get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientInfo entity) {
		
	}


	@Override
	public List<TreeJson> treeDrugExes(String id) throws Exception {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){//根节点
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<InpatientKind> inpatientKindList = doctorAdviceDAO.treeDrugExe();
			for(InpatientKind inpatientKind:inpatientKindList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(inpatientKind.getTypeCode());
				cTreeJson.setText(inpatientKind.getTypeName());
				cTreeJson.setState("closed");
				cAttMap.put("pid", "root");
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}else if("$".equals(id.substring(0, 1))){
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<BusinessDictionary> codeUseageList = innerCodeDao.getDictionary("useage");
			for(BusinessDictionary useage:codeUseageList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(useage.getEncode());
				cTreeJson.setText(useage.getName());
				cTreeJson.setState("open");
				cAttMap.put("pid", id);
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}else {
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<BusinessDictionary> codeDrugtypeList = innerCodeDao.getDictionary("drugType");
			for(BusinessDictionary codeDrugtype:codeDrugtypeList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId("$"+codeDrugtype.getEncode());
				cTreeJson.setText(codeDrugtype.getName());
				cTreeJson.setState("closed");
				cAttMap.put("pid", id);
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}
		return treeJsonList;
	}
	
	@Override
	public List<InpatientKind> treeDrugExe() throws Exception {
		List<InpatientKind> inpatientKindList = doctorAdviceDAO.treeDrugExe();
		return inpatientKindList;
	}

	@Override
	public List<TreeJson> treeNoDrugExe(String id) {
	
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();		
		if(StringUtils.isBlank(id)){//根节点
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<InpatientKind> inpatientKindList = doctorAdviceDAO.treeDrugExe();
			for(InpatientKind inpatientKind:inpatientKindList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(inpatientKind.getTypeCode());
				cTreeJson.setText(inpatientKind.getTypeName());
				cTreeJson.setState("closed");
				cAttMap.put("pid", "root");
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
				
			}
		}else {
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			List<BusinessDictionary> codeSystemtypeList = innerCodeDao.getDictionary("systemType");
			for(BusinessDictionary codeSystemtype:codeSystemtypeList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(codeSystemtype.getEncode());
				cTreeJson.setText(codeSystemtype.getName());
				cTreeJson.setState("open");
				cAttMap.put("pid", id);
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}
		return treeJsonList;
	}
    
	/**  
	 *  
	 * @Description：  加载执行单树
	 * @Author：qh
	 * @CreateDate：2017-04-01 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List queryDocAdvExe(String ids,String billName) throws Exception {
		List<TreeJson> pTreeJsonList = new ArrayList<TreeJson>();
		TreeJson pTreeJson = new TreeJson();
		pTreeJson.setId("1");
		pTreeJson.setState("close");
		pTreeJson.setText("执行单列表");
			List<InpatientExecbill> inpatientExecbillList = doctorAdviceDAO.queryDocAdvExe(ids,billName);
			List<TreeJson> ctreeJsonList = new ArrayList<TreeJson>();
			if(inpatientExecbillList!=null&&inpatientExecbillList.size()>0){
				for(InpatientExecbill bill:inpatientExecbillList){
					TreeJson cTreeJson = new TreeJson();
					Map<String,String>  cAttMap = new HashMap<String, String>();
					cTreeJson.setId(bill.getBillNo());
					cTreeJson.setText(bill.getBillName());
					cTreeJson.setState("open");
					cAttMap.put("pid", "1");
					cTreeJson.setAttributes(cAttMap);
					ctreeJsonList.add(cTreeJson);		
				}
				pTreeJson.setChildren(ctreeJsonList);
			}
		pTreeJsonList.add(pTreeJson);
		return pTreeJsonList;
	}

	/**  
	 *  
	 * @Description：  添加&修改
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-16 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public void saveOrUpdateInpatientExecbill(InpatientExecbill entity) throws Exception {
		SysDepartment  dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		entity.setHospitalId(HisParameters.CURRENTHOSPITALID);//医院id
		entity.setAreaCode(inprePayDAO.getDeptArea(dept.getDeptCode()));//科室所在院区
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateTime(DateUtils.getCurrentTime());
			String deptcode=dept.getDeptCode();
			if("N".equals(dept.getDeptType())){
				entity.setNurseCellCode(deptcode);
				entity.setNurseCellName(dept.getDeptName());
			}else{
				List<DepartmentContact> listC = doctorAdviceDAO.queryDepartmentContact(deptcode);
				if(listC.size()>0){
					entity.setNurseCellCode(listC.get(0).getDeptCode());
					entity.setNurseCellName(listC.get(0).getDeptName());
				}else{
					entity.setNurseCellCode(deptcode);
					entity.setNurseCellName(dept.getDeptName());
				}
			}
			entity.setCreateDept(deptcode);
			//随机数
			StringBuilder str=new StringBuilder();//定义变长字符串
			Random random=new Random();
			//随机生成数字，并添加到字符串
			for(int i=0;i<8;i++){
				str.append(random.nextInt(10));
			}
			//将字符串转换为数字并输出
			String num=str.toString();
			entity.setBillNo(num);
			inpatientExecbillDAO.save(entity);
			OperationUtils.getInstance().conserve(null,"医嘱执行单设置","INSERT INTO","T_INPATIENT_EXECBILL",OperationUtils.LOGACTIONINSERT);
		}else{
			entity.setUpdateTime(DateUtils.getCurrentTime());
			inpatientExecbillDAO.update(entity);
			OperationUtils.getInstance().conserve(entity.getId(),"医嘱执行单设置","UPDATE","T_INPATIENT_EXECBILL",OperationUtils.LOGACTIONUPDATE);
		}
	}
	/**  
	 *  
	 * @Description：  保存表格中新增的医嘱执行单数据
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-19 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public void saveOrUpdateInpatientDrugbilldetail(String str,String billNo) throws Exception {			
			User user = (User)SessionUtils.getCurrentUserFromShiroSession();
			SysDepartment  dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
			String deptcode=dept.getDeptCode();			
			JSONArray jsonArray = JSONArray.fromObject(str);
			String typeCode = null;
			String drugType = null;
			String usageCode = null;
			String billType = null;
			String a = null;
			String b = null;			
			for (int i = 0; i < jsonArray.size(); i++) {
				InpatientDrugbilldetail inpatientDrugbilldetail = new InpatientDrugbilldetail();
				inpatientDrugbilldetail.setId(null);
				JSONObject jsonObj = JSONObject.fromObject(jsonArray.getString(i));
				typeCode = jsonObj.getString("typeCodeId");	
				billType = jsonObj.getString("billType");
				if("1".equals(billType)){
					drugType = jsonObj.getString("drugTypeId");
					String aa=drugType.substring(drugType.lastIndexOf("_")+1);
					a=aa.substring(1);
					usageCode = jsonObj.getString("usageCodeId");
					b=usageCode.substring(usageCode.lastIndexOf("_")+1);
					InpatientDrugbilldetail inpatientDrugbilldetail1 = new InpatientDrugbilldetail();
					inpatientDrugbilldetail1.setBillNo(billNo);
					inpatientDrugbilldetail1.setTypeCode(typeCode);
					inpatientDrugbilldetail1.setDrugType(a);
					inpatientDrugbilldetail1.setUsageCode(b);
					List<InpatientDrugbilldetail> inpatientDrugbilldetailList = inpatientDrugbilldetailDAO.queryInpatientDrugbilldetail(inpatientDrugbilldetail1);	
					for(int j=0;j<inpatientDrugbilldetailList.size();j++){
						this.delDrugbilldetail(inpatientDrugbilldetailList.get(j).getId());
					}
					inpatientDrugbilldetail.setDrugType(a);
				}else{
					drugType = jsonObj.getString("drugTypeId");
					b = jsonObj.getString("usageCodeId");
					InpatientDrugbilldetail inpatientDrugbilldetail1 = new InpatientDrugbilldetail();
					inpatientDrugbilldetail1.setBillNo(billNo);
					inpatientDrugbilldetail1.setTypeCode(typeCode);
					inpatientDrugbilldetail1.setDrugType(drugType);
					inpatientDrugbilldetail1.setUsageCode(b);
					List<InpatientDrugbilldetail> inpatientDrugbilldetailList = inpatientDrugbilldetailDAO.queryInpatientDrugbilldetail(inpatientDrugbilldetail1);	
					for(int j=0;j<inpatientDrugbilldetailList.size();j++){
						this.delDrugbilldetail(inpatientDrugbilldetailList.get(j).getId());
					}
					inpatientDrugbilldetail.setDrugType(drugType);					
				}
				inpatientDrugbilldetail.setBillNo(billNo);
				inpatientDrugbilldetail.setTypeCode(typeCode);			
				inpatientDrugbilldetail.setUsageCode(b);
				inpatientDrugbilldetail.setBillType(billType);
				inpatientDrugbilldetail.setCreateUser(user.getAccount());
				inpatientDrugbilldetail.setCreateTime(DateUtils.getCurrentTime());
				inpatientDrugbilldetail.setUpdateUser(user.getAccount());
				inpatientDrugbilldetail.setUpdateTime(DateUtils.getCurrentTime());
				inpatientDrugbilldetail.setNurseCellCode(deptcode);
				//护士站名称
				inpatientDrugbilldetail.setNurseCellName(dept.getDeptName());
				List<InpatientKind> list = doctorAdviceDAO.treeDrugExeByCode(inpatientDrugbilldetail.getTypeCode());
				//医嘱类型
				inpatientDrugbilldetail.setTypeName(list.get(0).getTypeName());
				if("1".equals(inpatientDrugbilldetail.getBillType())){
					inpatientDrugbilldetail.setDrugTypeName(innerCodeDao.getDictionaryByCode("drugType",inpatientDrugbilldetail.getDrugType()).getName());
					inpatientDrugbilldetail.setUsageName(innerCodeDao.getDictionaryByCode("useage", inpatientDrugbilldetail.getUsageCode()).getName());
				}else{
					inpatientDrugbilldetail.setDrugTypeName(innerCodeDao.getDictionaryByCode("systemType",inpatientDrugbilldetail.getDrugType()).getName());
					inpatientDrugbilldetail.setUsageName(undrugInInterDAO.getCode(inpatientDrugbilldetail.getUsageCode()).getName());
				}
				inpatientDrugbilldetail.setBillName(doctorAdviceDAO.queryDocAdvExeByNo(inpatientDrugbilldetail.getBillNo()).get(0).getBillName());
				
				inpatientDrugbilldetail.setHospitalId(HisParameters.CURRENTHOSPITALID);//医院id
				inpatientDrugbilldetail.setAreaCode(inprePayDAO.getDeptArea(dept.getDeptCode()));
				inpatientDrugbilldetailDAO.save(inpatientDrugbilldetail);
				OperationUtils.getInstance().conserve(null,"医嘱执行单设置","INSERT INTO","T_INPATIENT_DRUGBILLDETAIL",OperationUtils.LOGACTIONINSERT);	
			}																
	}
	/**  
	 *  
	 * @Description：  删除执行单类型
	 * @Author：qh
	 * @CreateDate：2017-04-01 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void del(String id) throws Exception {
		InpatientDrugbilldetail inpatientDrugbilldetail=new InpatientDrugbilldetail();
		inpatientDrugbilldetail.setBillNo(id);
		List<InpatientDrugbilldetail> list = doctorAdviceDAO.queryAllBillDetail(inpatientDrugbilldetail);
		String user = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		for(InpatientDrugbilldetail inp:list){
			inp.setDel_flg(1);
			inp.setDeleteTime(DateUtils.getCurrentTime());
			inp.setDeleteUser(user);
			doctorAdviceDAO.updateDrugBillDetail(inp);
		}
		List<InpatientExecbill> list2 = doctorAdviceDAO.queryDocAdvExeByNo(id);
		for(InpatientExecbill inpatientExecbill:list2){
			String idd=inpatientExecbill.getId();
			inpatientExecbillDAO.del(idd,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		}
	}

	@Override
	public List<InpatientDrugbilldetail> queryInpatientDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception {
		List<InpatientDrugbilldetail> inpatientDrugbilldetailList = inpatientDrugbilldetailDAO.queryInpatientDrugbilldetail(inpatientDrugbilldetail);		
		return inpatientDrugbilldetailList;
	}

	@Override
	public void delDrugbilldetail(String id) throws Exception {
		inpatientDrugbilldetailDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id,"医嘱执行单设置","UPDATE","T_INPATIENT_DRUGBILLDETAIL",OperationUtils.LOGACTIONDELETE);
	}

	@Override
	public List<DrugUndrug> queryUndrugInfo(String page,String rows,DrugUndrug undrug) throws Exception {
		return unDrugInfoDAO.queryUndrugInfo(page,rows,undrug);
	}

	@Override
	public int getTotal(DrugUndrug undrug) throws Exception{
		return unDrugInfoDAO.getTotal(undrug);
	}

	@Override
	public List<DrugUndrug> queryUndrugInfo() throws Exception {
		return unDrugInfoDAO.queryUndrugInfo();
	}

	@Override
	public List<InpatientDrugbilldetail> queryDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail, String page,String rows) throws Exception {
			
		return  inpatientDrugbilldetailDAO.queryDrugbilldetail(inpatientDrugbilldetail,page, rows);
	}

	@Override
	public int getTotalBilldetail(InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception {
		return  inpatientDrugbilldetailDAO.getTotalBilldetail(inpatientDrugbilldetail);
	}

	@Override
	public List<InpatientExecbill> queryDocAdvExe2(String ids,String billName) throws Exception {
		return doctorAdviceDAO.queryDocAdvExe(ids,billName);
	}

	@Override
	public void saveDrugBillDetail(InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception {
		Date updateTime = inpatientDrugbilldetail.getUpdateTime();
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();
		SysDepartment  dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
		String deptcode=dept.getDeptCode();	
		inpatientDrugbilldetail.setNurseCellCode(deptcode);
		//护士站名称
		inpatientDrugbilldetail.setNurseCellName(dept.getDeptName());
		inpatientDrugbilldetail.setCreateUser(user.getAccount());
		inpatientDrugbilldetail.setCreateTime(DateUtils.getCurrentTime());
		inpatientDrugbilldetail.setUpdateUser(user.getAccount());
		inpatientDrugbilldetail.setUpdateTime(DateUtils.getCurrentTime());
		
		inpatientDrugbilldetail.setAreaCode(inprePayDAO.getDeptArea(deptcode));
		inpatientDrugbilldetail.setHospitalId(HisParameters.CURRENTHOSPITALID);
		doctorAdviceDAO.saveDrugBillDetail(inpatientDrugbilldetail);
	}

	@Override
	public List queryDrugType() throws Exception {
		List<BusinessDictionary> codeDrugtypeList = innerCodeDao.getDictionary("drugType");
		return codeDrugtypeList;
	}

	@Override
	public List queryDrugUsage() throws Exception {
		List<BusinessDictionary> codeUseageList = innerCodeDao.getDictionary("useage");
		return codeUseageList;
	}

	@Override
	public List queryAllUndrug() throws Exception {
		List list=unDrugInfoDAO.queryAllUndrug();
		return list;
	}

	@Override
	public List queryAllBillDetail (
			InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception {
	List list=doctorAdviceDAO.queryAllBillDetail(inpatientDrugbilldetail);
		return list;
	}

	@Override
	public void updateDrugBillDetail (
			InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception {
		String id=inpatientDrugbilldetail.getId();
		InpatientDrugbilldetail oInpatientDrugbilldetail = doctorAdviceDAO.findInpatientDrugbilldetailById(id);
		oInpatientDrugbilldetail.setBillType(inpatientDrugbilldetail.getBillType());
		oInpatientDrugbilldetail.setTypeCode(inpatientDrugbilldetail.getTypeCode());
		oInpatientDrugbilldetail.setTypeName(inpatientDrugbilldetail.getTypeName());
		oInpatientDrugbilldetail.setDrugType(inpatientDrugbilldetail.getDrugType());
		oInpatientDrugbilldetail.setDrugTypeName(inpatientDrugbilldetail.getDrugTypeName());
		oInpatientDrugbilldetail.setUsageCode(inpatientDrugbilldetail.getUsageCode());
		oInpatientDrugbilldetail.setUsageName(inpatientDrugbilldetail.getUsageName());
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();
		oInpatientDrugbilldetail.setUpdateUser(user.getAccount());
		oInpatientDrugbilldetail.setUpdateTime(DateUtils.getCurrentTime());
		doctorAdviceDAO.updateDrugBillDetail(oInpatientDrugbilldetail);
	}
	
}
