package cn.honry.inpatient.delivery.service.impl;

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

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientStoMsg;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.apply.dao.InpatientStoMsgDAO;
import cn.honry.inpatient.bill.dao.BillclassDAO;
import cn.honry.inpatient.delivery.dao.DeliveryDAO;
import cn.honry.inpatient.delivery.dao.DeliveryNowDAO;
import cn.honry.inpatient.delivery.service.DeliveryService;
import cn.honry.inpatient.delivery.vo.DeliveryVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
/**
 * 摆药单业务层
 * @author  lyy
 * @createDate： 2015年12月28日 上午11:08:00 
 * @modifier lyy
 * @modifyDate：2015年12月28日 上午11:08:00  
 * @modifyRmk：  
 * @version 1.0
 */
@Service("deliveryService")
@Transactional
@SuppressWarnings({"all"})
public class DeliveryServiceImpl implements DeliveryService {
	@Autowired
	@Qualifier("deliveryDao")
	private DeliveryDAO deliveryDao;
	@Autowired
	@Qualifier("inpatientStoMsgDAO")
	private InpatientStoMsgDAO stomsgDao;
	@Autowired
	@Qualifier("billclassDAO")
	private BillclassDAO billclassDao;
	@Autowired
	@Qualifier("deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	@Autowired
	@Qualifier("deliveryNowDao")
	private DeliveryNowDAO deliveryNowDao;
	@Override
	public void removeUnused(String id) {
	}
	@Override
	public DrugApplyout get(String id) {
		return null;
	}
	@Override
	public void saveOrUpdate(DrugApplyout entity) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		entity.setApplyState(2);
		entity.setApplyBillcode("");
		entity.setUpdateUser(userId);
		entity.setUpdateTime(new Date());
		deliveryDao.save(entity);
		OperationUtils.getInstance().conserve(entity.getId(),"药品集中发送","UPDATE","T_DEPARTMENT_CONTACT",OperationUtils.LOGACTIONUPDATE);
		
	}
	/**
	 * 查询所有的摆药单分类
	 * @author  lyy
	 * @createDate： 2015年12月28日 上午10:56:15 
	 * @modifier lyy
	 * @modifyDate：2015年12月28日 上午10:56:15  
	 * @modifyRmk：  id 摆药分类id deptId 登录科室 
	 * @version 1.0
	 */
	@Override
	public List<TreeJson> queryBillclass(String id)throws Exception {
		SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			/**
			 * 加摆药单树的根节点
			 */
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("root");
			pTreeJson.setText("摆药单");
			treeJsonList.add(pTreeJson);
		}
		/**
		 * 病区集合id
		 */
		String deptCodeIds=""; 
		/**
		 * 病区id 
		 */
		String deptCode="";
		/**
		 * 患者编号集合
		 */
		String patientids="";
		/**
		 * 患者编号
		 */
		String pid="";
		/**
		 * 摆药单分类集合
		 */
		String billclasss="";
		/**
		*摆药单分类
		*/
		String drugbill="";
		
		List deptContactList=new ArrayList();
		if(!"N".equals(dept.getDeptType())){
			List<DepartmentContact> deptContact =deliveryDao.queryDeptContact(dept.getId());
			if(deptContact!=null&&deptContact.size()>0){
				for (DepartmentContact dc : deptContact) {
					deptContactList.add(dc.getDeptId());
				}
			}
		}else{
			 deptContactList.add(dept.getId());
		}
		List<InpatientInfoNow> infoList=deliveryDao.getQueryInpatientNo(deptContactList);
		if(infoList!=null&&infoList.size()>0){
			for (InpatientInfoNow info : infoList) {
				if(!"".equals(pid)){
					pid+="','";
				}
				pid+=info.getInpatientNo();
			}
			patientids=pid;
		}
		List<DrugApplyoutNow> applyList=deliveryDao.getQueryDrugApply(patientids);
		if(applyList!=null&&applyList.size()>0){
			for (DrugApplyoutNow apply : applyList) {
				if(!"".equals(drugbill)){
					drugbill+="','";
				}
				drugbill+=apply.getBillclassCode();
			}
			billclasss=drugbill;
		}
		List<DrugBillclass> billClassList=deliveryDao.queryBillclass(billclasss);
		if(billClassList != null && billClassList.size() > 0){
			for (DrugBillclass bill : billClassList) {
				TreeJson treeChJson = new TreeJson();
				treeChJson.setId(bill.getId());
				treeChJson.setText(bill.getBillclassName());
				Map<String,String> attributesCh = new HashMap<String, String>();
				attributesCh.put("pid", "root");
				treeChJson.setAttributes(attributesCh);
				treeJsonList.add(treeChJson);
			}
		}
		return TreeJson.formatTree(treeJsonList);
	}
	/**
	 * 集中发送带分页查询list集合
	 * @author  lyy
	 * @createDate： 2015年12月29日 上午10:56:15 
	 * @modifier lyy
	 * @modifyDate：2015年12月29日 上午10:56:15  
	 * @modifyRmk：   deliveryVo 集中发送虚拟实体   page 分页查询总页数    rows  总条数
	 * @version 1.0
	 */
	@Override
	public List<DeliveryVo> getPage(DeliveryVo deliveryVo, String page, String rows,String deptCode)throws Exception {
		return deliveryDao.getPage(deliveryVo, page, rows,deptCode);
	}
	/**
	 * 集中发送总条数
	 * @author  lyy
	 * @createDate： 2015年12月29日 上午10:56:15 
	 * @modifier lyy
	 * @modifyDate：2015年12月29日 上午10:56:15  
	 * @modifyRmk：  deliveryVo 集中发送虚拟实体   
	 * @version 1.0
	 */
	@Override
	public int getTotal(DeliveryVo deliveryVo,String deptCode)throws Exception {
		return deliveryDao.getTotal(deliveryVo,deptCode);
	}
	/**
	 * 获得药品集中发送,保存过程
	 * @author  lyy
	 * @createDate： 2015年12月30日 下午4:15:44 
	 * @modifier lyy
	 * @modifyDate：2015年12月30日 下午4:15:44  
	 * @modifyRmk：id 出库申请表的主键id数组  
	 * @version 1.0
	 */
	@Override
	public String applyOutUpdate(String id,Integer sendType)throws Exception {
		String userId=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String drugEdBill = deliveryDao.getSeqByNameorNum("SEQ_DRUG_APPLYOUT", 18); //根据sequence 获取摆药单号
		List idList=new ArrayList();
		String [] ids=id.split(",");
		String retVal = deliveryDao.applyOutUpdate(ids,drugEdBill,sendType,userId);
		if("ok".equals(retVal)){
			this.drugStoMsg(ids,sendType);
			OperationUtils.getInstance().conserve(id,"药品集中发送","UPDATE","T_DRUG_APPLYOUT_NOW",OperationUtils.LOGACTIONUPDATE);
			return drugEdBill;
		}else{
			return "1";
		}
	}
	/**
	 * 获得药品集中发送，打印后的修改方法   保存过程
	 * @author  lyy
	 * @createDate： 2016年4月19日 上午9:53:14 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 上午9:53:14
	 * @param：    ids  药品出库表中的主键id集合ids sendType 发送状态
	 * @modifyRmk：  
	 * @version 1.0
	 */ 
	public String applyOutUpdateStamp(String id,Integer sendType)throws Exception{
		String userId=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String retVal = deliveryDao.applyOutStamp(id,sendType,userId);
		String [] ids=id.split(",");
		this.drugStoMsg(ids,sendType);
		OperationUtils.getInstance().conserve(id,"药品集中发送","UPDATE","T_DRUG_APPLYOUT_NOW",OperationUtils.LOGACTIONUPDATE);
		return retVal;
	}
	/**
	 * 摆药通知单
	 * @author  lyy
	 * @createDate： 2016年4月21日 下午5:18:20 
	 * @modifier lyy
	 * @modifyDate：2016年4月21日 下午5:18:20
	 * @param：    id药品出库表中的主键id集合
	 * @modifyRmk：  
	 * @version 1.0
	 */
	private void drugStoMsg(String[] ids,Integer sendType)throws Exception{
		String deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId();
		List<DrugApplyoutNow> drugOutList =deliveryDao.queryDrugBill(ids);
		String billclassCode=null;
		String drugCode=null;
		if(drugOutList!=null&&drugOutList.size()>0){
			for (DrugApplyoutNow drugout : drugOutList) {
				billclassCode=drugout.getBillclassCode();
				drugCode=drugout.getDrugDeptCode();
				InpatientStoMsgNow stoMsgList=deliveryDao.queryStoMsg(billclassCode,drugCode,sendType);
				if(stoMsgList==null){
					InpatientStoMsgNow stoMsg=new InpatientStoMsgNow();
					stoMsg.setId(null);
					stoMsg.setBillclassCode(drugout.getBillclassCode());
					String drugclass=billclassDao.queryDrugBillclassCode(drugout.getBillclassCode()).get(0).getBillclassName();
					stoMsg.setBillclassName(drugclass);
					stoMsg.setDeptCode(drugout.getDeptCode());    //申请科室
					String deptName=deptInInterDAO.getDeptCode(drugout.getDeptCode()).getDeptName();
					stoMsg.setDeptName(deptName);
					stoMsg.setSendType(String.valueOf(sendType));
					stoMsg.setSendFlag("0");
					stoMsg.setMedDeptCode(drugout.getDrugDeptCode());    //取药科室
					stoMsg.setCreateTime(new Date());
					stoMsg.setCreateDept(deptId);
					stoMsg.setDel_flg(0);
					stoMsg.setStop_flg(0);
					stomsgDao.save(stoMsg);
				}
			}
		}
	}
	@Override
	public String queryApply(String id) throws Exception{
		SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		List<TreeJson> treeJsonList=new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			Map<Integer, String> mapApply=new HashMap<Integer,String>();
			mapApply.put(0, "住院摆药");
			mapApply.put(1, "住院退药");
			TreeJson tJson=null;
			Map<String, String> map=null;
			for (int i = 0; i < 2; i++) {
				tJson=new TreeJson();
				map=new HashMap<String,String>();
				tJson.setId((i+4)+"");
				tJson.setText(mapApply.get(i));
				tJson.setState("closed");
				if(i==0){
					tJson.setIconCls("icon-03");
				}
				if(i==1){
					tJson.setIconCls("icon-2012081511913");
				}
				map.put("id", "root");
				map.put("pid", "treeRoot");
				map.put("isOpen", "0");
				map.put("infoId", "cc");
				tJson.setAttributes(map);
				treeJsonList.add(tJson);
			}
		}else{
			
			/**
			 * 患者编号
			 */
			String pid="";
			List deptContactList=new ArrayList();
			if(!"N".equals(dept.getDeptType())){//登录的不是病区
				 deptContactList.add(dept.getId());
			}else{    //登录的是病区
				List<DepartmentContact> deptContact =deliveryDao.queryDeptContact(dept.getId());
				if(deptContact!=null&&deptContact.size()>0){
					for (DepartmentContact dc : deptContact) {
						deptContactList.add(dc.getDeptId());
					}
				}
			}
			List<InpatientInfoNow> infoList=deliveryDao.getQueryInpatientNo(deptContactList);
			List patientidlist=new ArrayList();
			if(infoList!=null&&infoList.size()>0){
				for (InpatientInfoNow info : infoList) {
					patientidlist.add(info.getInpatientNo());
				}
			}
			TreeJson treeChJson=null;
			List<DrugApplyoutNow> applyList=null;
			if("4".equals(id)){
				 applyList=deliveryDao.queryApplyOut(deptContactList);
			}
			else{
				 applyList=deliveryDao.queryReturnDrug(deptContactList);
			}
			if(applyList!=null&&applyList.size()>0){
				for (DrugApplyoutNow bill : applyList) {
					treeChJson = new TreeJson();
					treeChJson.setId(bill.getDrugedBill());
					treeChJson.setText(bill.getDrugedBill());
					Map<String,String> attributesCh = new HashMap<String, String>();
					attributesCh.put("pid", "root");
					treeChJson.setAttributes(attributesCh);
					treeJsonList.add(treeChJson);
				}	
			}
		}
		return JSONUtils.toJson(treeJsonList);
	}
	/**
	 * 总条数已发送状态
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午2:12:56 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午2:12:56
	 * @param：    deliverySerch 集中发送虚拟实体
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int getTotalBill(DeliveryVo deliverySerch)throws Exception {
		return deliveryDao.getTotalBill(deliverySerch);
	}
	/**
	 * 带分页查询已发送状态
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午2:13:00 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午2:13:00
	 * @param：   deliverySerch 集中发送虚拟实体   page 分页查询总页数    rows  总条数 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<DeliveryVo> getPageBill(DeliveryVo deliverySerch, String page, String rows)throws Exception {
		return deliveryDao.getPageBill(deliverySerch,page,rows);
	}
	/**
	 * 查询用户
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午3:53:16 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午3:53:16
	 * @param：    
	 * @modifyRmk：   原来是查的是员工表现在查的是用户表
	 * @version 1.0
	 */
	@Override
	public List<User> queryEmpName() {
		return deliveryDao.queryEmpName();
	}
	@Override
	public List<TreeJson> queryBillClassName(String id)throws Exception {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			/**
			 * 加摆药单树的根节点
			 */
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("root");
			pTreeJson.setText("摆药单");
			treeJsonList.add(pTreeJson);
		}
		List<DrugBillclass> billClassList=deliveryDao.queryBillClassName();
		if(billClassList != null && billClassList.size() > 0){
			for (DrugBillclass bill : billClassList) {
				TreeJson treeChJson = new TreeJson();
				treeChJson.setId(bill.getId());
				treeChJson.setText(bill.getBillclassName());
				Map<String,String> attributesCh = new HashMap<String, String>();
				attributesCh.put("pid", "root");
				attributesCh.put("code", bill.getBillclassCode());
				treeChJson.setAttributes(attributesCh);
				treeJsonList.add(treeChJson);
			}
		}
		return TreeJson.formatTree(treeJsonList);
	}
	@Override
	public List<DepartmentContact> queryDeptContact(String id)throws Exception {
		return deliveryDao.queryDeptContact(id);
	}
	@Override
	public List<DeliveryVo> iReportInvoiceBill(String tid, String drugedbill)throws Exception {
		return deliveryNowDao.iReportInvoiceBill(tid,drugedbill);
	}
}
