package cn.honry.inpatient.bill.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.bill.dao.BilllistDAO;
import cn.honry.inpatient.bill.dao.InpatientKindQueryDAO;
import cn.honry.inpatient.bill.service.InpatientKindQueryService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.reflect.TypeToken;

@Service("inpatientKindQueryService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientKindQueryServiceImpl  implements InpatientKindQueryService {
	
	@Autowired
	@Qualifier(value="inpatientKindQueryDAO")
	private InpatientKindQueryDAO inpatientKindQueryDAO;
	
	@Autowired
	@Qualifier(value="billlistDAO")
	private BilllistDAO billlistDAO;
	
	@Override
	public void removeUnused(String id) {
	}

	@Override
	public InpatientKind get(String id) {
		InpatientKind sexs=inpatientKindQueryDAO.get(id);
		return sexs;
	}

	@Override
	public void saveOrUpdate(InpatientKind entity) {
	}

	@Override
	public List<InpatientKind> searchOrdercategoryList(
			InpatientKind inpatientKind) {
		List<InpatientKind>  lists=inpatientKindQueryDAO.searchInpatientKindList(inpatientKind);
		return lists;
	}

	@Override
	public int getTotal(InpatientKind inpatientKind) {
		return inpatientKindQueryDAO.getTotal(inpatientKind);
	}

	@Override
	public List<InpatientKind> likeSearch(String code) {
		return inpatientKindQueryDAO.likeSearch(code);
	}

	@Override
	public List<InpatientKind> getKind(String code) {
		return inpatientKindQueryDAO.getKind(code);
	}

	@Override
	public List<DrugBilllist> queryBilllist(String BillclassId) {
		return inpatientKindQueryDAO.queryBilllist(BillclassId);
	}
	
	
	
	@Override
	public List<DrugBillclass> queryBillclass(String BillclassId) {
		return inpatientKindQueryDAO.queryBillclass(BillclassId);
	}

	@Override
	public void saveOrUpdate(Map<String, String> parameterMap,String infoJson) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		//1.修改摆药单
		inpatientKindQueryDAO.UpdateBillclass(infoJson);
		//2.根据摆药id查询摆药明细
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
		List<DrugBilllist> drugBilllist = inpatientKindQueryDAO.queryBilllist(modelList.get(0).getId());
		DrugBilllist Billlist = new DrugBilllist();
		if(drugBilllist.size()>0){
			//修改摆药分类明细
			inpatientKindQueryDAO.deleteBilllist(modelList.get(0).getId());
			
			
			//医嘱类别
			for (InpatientKind kind : InpatientKindlist) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setTypeCode(kind.getTypeCode());//医嘱类别
				billlist.setCreateUser(drugBilllist.get(0).getCreateUser());
				billlist.setCreateDept(drugBilllist.get(0).getCreateDept());
				billlist.setCreateTime(drugBilllist.get(0).getCreateTime());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			//药品用法
			for (BusinessDictionary useage : useageList) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setUsageCode(useage.getEncode());//用法
				billlist.setCreateUser(drugBilllist.get(0).getCreateUser());
				billlist.setCreateDept(drugBilllist.get(0).getCreateDept());
				billlist.setCreateTime(drugBilllist.get(0).getCreateTime());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			//药品类别
			for (BusinessDictionary drug : drugtype) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setDrugType(drug.getEncode());//药品类别
				billlist.setCreateUser(drugBilllist.get(0).getCreateUser());
				billlist.setCreateDept(drugBilllist.get(0).getCreateDept());
				billlist.setCreateTime(drugBilllist.get(0).getCreateTime());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			//药品性质
			for (BusinessDictionary drugproper : drugproperties) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setDrugQuality(drugproper.getEncode());//药品性质
				billlist.setCreateUser(drugBilllist.get(0).getCreateUser());
				billlist.setCreateDept(drugBilllist.get(0).getCreateDept());
				billlist.setCreateTime(drugBilllist.get(0).getCreateTime());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			//药品药剂
			for (BusinessDictionary codeDosageform : dosageform) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setDoseModelCode(codeDosageform.getEncode());//药剂
				billlist.setCreateUser(drugBilllist.get(0).getCreateUser());
				billlist.setCreateDept(drugBilllist.get(0).getCreateDept());
				billlist.setCreateTime(drugBilllist.get(0).getCreateTime());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			
		}else{
			//医嘱类别
			for (InpatientKind kind : InpatientKindlist) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setTypeCode(kind.getTypeCode());//医嘱类别
				billlist.setCreateUser(user.getAccount());
				if(dept!=null){
					billlist.setCreateDept(dept.getDeptCode());
				}
				billlist.setCreateTime(new Date());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			//药品用法
			for (BusinessDictionary useage : useageList) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setUsageCode(useage.getEncode());//用法
				billlist.setCreateUser(user.getAccount());
				if(dept!=null){
					billlist.setCreateDept(dept.getDeptCode());
				}
				billlist.setCreateTime(new Date());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			//药品类别
			for (BusinessDictionary drug : drugtype) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setDrugType(drug.getEncode());//药品类别
				billlist.setCreateUser(user.getAccount());
				if(dept!=null){
					billlist.setCreateDept(dept.getDeptCode());
				}
				billlist.setCreateTime(new Date());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			//药品性质
			for (BusinessDictionary drugproper : drugproperties) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setDrugQuality(drugproper.getEncode());//药品性质
				billlist.setCreateUser(user.getAccount());
				if(dept!=null){
					billlist.setCreateDept(dept.getDeptCode());
				}
				billlist.setCreateTime(new Date());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			//药品药剂
			for (BusinessDictionary codeDosageform : dosageform) {
				DrugBilllist billlist = new DrugBilllist();
				billlist.setId(null);
				DrugBillclass DrugBillclass = new DrugBillclass();
				DrugBillclass.setId(modelList.get(0).getId());
				billlist.setDrugBillclass(DrugBillclass);
				billlist.setDoseModelCode(codeDosageform.getEncode());//药剂
				billlist.setCreateUser(user.getAccount());
				if(dept!=null){
					billlist.setCreateDept(dept.getDeptCode());
				}
				billlist.setCreateTime(new Date());
				billlist.setUpdateTime(new Date());
				billlist.setUpdateUser(user.getAccount());
				billlistDAO.save(billlist);
			}
			OperationUtils.getInstance().conserve(null, "摆药分类明细",
					"INSERT_INTO", "T_DRUG_BILLLIST",
					OperationUtils.LOGACTIONINSERT);
		}
	}

	@Override
	public List<DrugBilllist> queryTypeCode(String billssId) {
		return inpatientKindQueryDAO.queryTypeCode(billssId);
	}

	@Override
	public List<DrugBilllist> queryDrugType(String billssId) {
		return inpatientKindQueryDAO.queryDrugType(billssId);
	}

	@Override
	public List<DrugBilllist> queryDoseModelCode(String billssId) {
		return inpatientKindQueryDAO.queryDoseModelCode(billssId);
	}

	@Override
	public List<DrugBilllist> queryUsageCode(String billssId) {
		return inpatientKindQueryDAO.queryUsageCode(billssId);
	}

	@Override
	public List<DrugBilllist> queryDrugQuality(String billssId) {
		return inpatientKindQueryDAO.queryDrugQuality(billssId);
	}

	@Override
	public List<BusinessDictionary> likeSearch(String type, String code) {
		return inpatientKindQueryDAO.likeSearch(type, code);
	}
}
