package cn.honry.inpatient.bill.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.bill.dao.BillclassDAO;
import cn.honry.inpatient.bill.dao.BilllistDAO;
import cn.honry.inpatient.bill.service.BillclassService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("billclassService")
@Transactional
public class BillclassServiceImpl implements BillclassService {
	@Autowired
	private BillclassDAO billclassDAO;
	@Autowired
	private BilllistDAO billlistDAO;

	@Override
	public void removeUnused(String id) {
		billclassDAO.del(id, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id, "摆药分类管理", "UPDATE","T_DRUG_BILLCLASS", OperationUtils.LOGACTIONUPDATE);
		billlistDAO.deleteBypid(id);
		OperationUtils.getInstance().conserve("pid = " + id, "摆药分类明细","UPDATE", "T_DRUG_BILLLIST", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public DrugBillclass get(String id){
		return billclassDAO.get(id);
	}

	@Override
	public void saveOrUpdate(DrugBillclass entity) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils
				.getCurrentUserDepartmentFromShiroSession();
		if (entity != null) {
			if (entity.getId() != null && entity.getId() != "") {
				entity.setUpdateUser("");
				entity.setUpdateTime(new Date());
			} else {
				entity.setId(null);
				entity.setCreateUser(user.getAccount());
				entity.setCreateDept(dept.getDeptName());
				entity.setCreateTime(new Date());
			}
			billclassDAO.save(entity);
		}
	}

	@Override
	public void saveOrUpdate(Map<String, String> parameterMap,String infoJson)throws Exception {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils
				.getCurrentUserDepartmentFromShiroSession();
		//摆药分类Json转成list
		List<DrugBillclass> modelList = null;
		try {
			infoJson=infoJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			modelList =JSONUtils.fromJson(infoJson,  new TypeToken<List<DrugBillclass>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//药品用法Json转成list
		List<BusinessDictionary> useageList = null;
		try {
			String infoJson1=parameterMap.get("ypyfid").replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			useageList =JSONUtils.fromJson(infoJson1,  new TypeToken<List<BusinessDictionary>>(){}, "yyyy-MM-dd hh:mm:ss");
			System.out.println(useageList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//药品药剂Json转成list
		List<BusinessDictionary> dosageform = null;
		try {
			String infoJson2=parameterMap.get("ypyjid").replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			dosageform =JSONUtils.fromJson(infoJson2,  new TypeToken<List<BusinessDictionary>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//药品性质Json转成list
		List<BusinessDictionary> drugproperties = null;
		try {
			String infoJson3=parameterMap.get("ypxzid").replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			drugproperties =JSONUtils.fromJson(infoJson3,  new TypeToken<List<BusinessDictionary>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//药品类别Json转成list
		List<BusinessDictionary> drugtype = null;
		try {
			String infoJson4=parameterMap.get("yplbid").replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			drugtype =JSONUtils.fromJson(infoJson4,  new TypeToken<List<BusinessDictionary>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//医嘱类别Json转成list
		List<InpatientKind> InpatientKindlist = null;
		try {
			String infoJson5=parameterMap.get("yzlbid").replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			InpatientKindlist =JSONUtils.fromJson(infoJson5,  new TypeToken<List<InpatientKind>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
	//摆药分类
	for (DrugBillclass entity : modelList) {
			if (entity != null) {
				entity.setId(null);
				entity.setCreateUser(user.getAccount());
				if(dept!=null){
					entity.setCreateDept(dept.getDeptCode());
				}
				entity.setCreateTime(new Date());
				String applyNumber = billclassDAO.getSequece(
						"SEQ_BILLCLASS_CODE").toString();// 根据sequence
															// 获取applyNumber
				entity.setBillclassCode(applyNumber);
				OperationUtils.getInstance().conserve(null, "摆药分类管理",
						"INSERT_INTO", "T_DRUG_BILLCLASS",
						OperationUtils.LOGACTIONINSERT);
				billclassDAO.save(entity);
			}
		//医嘱类别
		for (InpatientKind kind : InpatientKindlist) {
			DrugBilllist drugBilllist = new DrugBilllist();
			drugBilllist.setId(null);
			drugBilllist.setDrugBillclass(entity);
			drugBilllist.setTypeCode(kind.getTypeCode());//医嘱类别
			drugBilllist.setCreateUser(user.getAccount());
			if(dept!=null){
				drugBilllist.setCreateDept(dept.getDeptCode());
			}
			drugBilllist.setCreateTime(new Date());
			billlistDAO.save(drugBilllist);
		}
		//药品用法
		for (BusinessDictionary useage : useageList) {
			DrugBilllist drugBilllist = new DrugBilllist();
			drugBilllist.setId(null);
			drugBilllist.setDrugBillclass(entity);
			drugBilllist.setUsageCode(useage.getEncode());//用法
			drugBilllist.setCreateUser(user.getAccount());
			if(dept!=null){
				drugBilllist.setCreateDept(dept.getDeptCode());
			}
			drugBilllist.setCreateTime(new Date());
			billlistDAO.save(drugBilllist);
		}
		//药品类别
		for (BusinessDictionary drug : drugtype) {
			DrugBilllist drugBilllist = new DrugBilllist();
			drugBilllist.setId(null);
			drugBilllist.setDrugBillclass(entity);
			drugBilllist.setDrugType(drug.getEncode());//药品类别
			drugBilllist.setCreateUser(user.getAccount());
			if(dept!=null){
				drugBilllist.setCreateDept(dept.getDeptCode());
			}
			drugBilllist.setCreateTime(new Date());
			billlistDAO.save(drugBilllist);
		}
		//药品性质
		for (BusinessDictionary drugproper : drugproperties) {
			DrugBilllist drugBilllist = new DrugBilllist();
			drugBilllist.setId(null);
			drugBilllist.setDrugBillclass(entity);
			drugBilllist.setDrugQuality(drugproper.getEncode());//药品性质
			drugBilllist.setCreateUser(user.getAccount());
			if(dept!=null){
				drugBilllist.setCreateDept(dept.getDeptCode());
			}
			drugBilllist.setCreateTime(new Date());
			billlistDAO.save(drugBilllist);
		}
		//药品药剂
		for (BusinessDictionary codeDosageform : dosageform) {
			DrugBilllist drugBilllist = new DrugBilllist();
			drugBilllist.setId(null);
			drugBilllist.setDrugBillclass(entity);
			drugBilllist.setDoseModelCode(codeDosageform.getEncode());//药剂
			drugBilllist.setCreateUser(user.getAccount());
			if(dept!=null){
				drugBilllist.setCreateDept(dept.getDeptCode());
			}
			drugBilllist.setCreateTime(new Date());
			billlistDAO.save(drugBilllist);
		}
		OperationUtils.getInstance().conserve(null, "摆药分类明细",
				"INSERT_INTO", "T_DRUG_BILLLIST",
				OperationUtils.LOGACTIONINSERT);
	}	
}

	@Override
	public List<DrugBillclass> getPage(String page, String rows,
			DrugBillclass billclassSerc)throws Exception {
		return billclassDAO.getPage(page, rows, billclassSerc);
	}

	@Override
	public int getTotal(DrugBillclass billclassSerc)throws Exception {
		return billclassDAO.getTotal(billclassSerc);
	}

	/**
	 * 
	 * @description：查询修改的那一条
	 * @author：ldl
	 * @createDate：2015-10-19
	 * @modifyRmk：
	 * @version 1.0
	 */
	@Override
	public List<DrugBillclass> findBillEdit(String id)throws Exception {
		List<DrugBillclass> lst = billclassDAO.findBillEdit(id);
		return lst;
	}

	/**
	 * 
	 * @description：查询修改的那一条(子表)
	 * @author：ldl
	 * @createDate：2015-10-20
	 * @modifyRmk：
	 * @version 1.0
	 */
	@Override
	public List<DrugBilllist> findBillInfoEdit(String id) {
		List<DrugBilllist> lst = billclassDAO.findBillInfoEdit(id);
		return lst;
	}

	@Override
	public void saveOrUpdateInfo(String billInfoJson, String billId) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils
				.getCurrentUserDepartmentFromShiroSession();
		DrugBillclass drugBillclass = new DrugBillclass();
		drugBillclass.setId(billId);
		Gson gson = new Gson();
		List<DrugBilllist> billlistList = null;
		try {
			billlistList = gson.fromJson(billInfoJson,
					new TypeToken<List<DrugBilllist>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (DrugBilllist billlist : billlistList) {
			if (billlist != null) {
				if (StringUtils.isNotBlank(billlist.getId())) {
					billlist.setUpdateTime(new Date());
					billlist.setUpdateUser(user.getAccount());
					OperationUtils.getInstance().conserve(billlist.getId(),
							"摆药分类管理", "UPDATE", "T_DRUG_BILLLIST",
							OperationUtils.LOGACTIONUPDATE);
				} else {
					billlist.setId(null);
					billlist.setDrugBillclass(drugBillclass);
					billlist.setCreateTime(new Date());
					billlist.setCreateDept(dept.getDeptName());
					billlist.setCreateUser(user.getAccount());
					OperationUtils.getInstance().conserve(null, "摆药分类管理",
							"INSERT_INTO", "T_DRUG_BILLLIST",
							OperationUtils.LOGACTIONINSERT);
				}
				billlistDAO.save(billlist);
			}
		}
	}

	@Override
	public List<DrugBillclass> getPageList(String page, String rows,
			DrugBillclass drugBillclass) {
		List<DrugBillclass> list = billclassDAO.getPageList(page, rows,
				drugBillclass);
		return list;
	}

	@Override
	public List<DrugBillclass> getBillclassCode() {
		return billclassDAO.getBillclassCode();
	}

	@Override
	public List<DrugBillclass> queryDrugBillclassCode(String Code) {
		List<DrugBillclass> list = billclassDAO.queryDrugBillclassCode(Code);
		return list;
	}

}
