package cn.honry.statistics.drug.patientDispensing.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.statistics.drug.patientDispensing.dao.PatientDispensingDAO;
import cn.honry.statistics.drug.patientDispensing.service.PatientDispensingService;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("patientDispensingService")
@Transactional
@SuppressWarnings({ "all" })
public class PatientDispensingServiceImpl implements PatientDispensingService{
	@Autowired
	@Qualifier(value = "patientDispensingDAO")
	private PatientDispensingDAO patientDispensingDAO;

	@Override
	public DrugApplyout get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(DrugApplyout arg0) {
		
	}
	/**  
	 *  
	 * @Description：  查询患者树
	 * @Author：zhenglin
	 * @CreateDate：2015-12-22 下午05:37:12  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-8-22 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public String queryPatientTree(String deptId) {
		String type=patientDispensingDAO.queryState(deptId).getDeptType();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		List<InpatientInfoNow> infoList = patientDispensingDAO.queryPatient(deptId,type);
		//根节点
		TreeJson gTreeJson = new TreeJson();
		gTreeJson.setId("1");
		gTreeJson.setText("本区患者");
		gTreeJson.setIconCls("icon-house");
		Map<String,String> gAttMap = new HashMap<String, String>();
		gTreeJson.setAttributes(gAttMap);
		treeJsonList.add(gTreeJson);
		TreeJson fTreeJson = null;
		Map<String,String> fAttMap = null;
		if(infoList!=null&&infoList.size()>0){
			for(InpatientInfoNow infonfo : infoList){
				//二级节点(患者)
				fTreeJson = new TreeJson();
				fTreeJson.setId(infonfo.getInpatientNo());
				fTreeJson.setText(infonfo.getPatientName());
				if("3".equals(infonfo.getReportSex())||"1".equals(infonfo.getReportSex())){
					fTreeJson.setIconCls("icon-user_b");
				}
				else{
					fTreeJson.setIconCls("icon-user_female");
				}
				fTreeJson.setState("open");
				fAttMap = new HashMap<String, String>();
				fAttMap.put("pid", "1");
				fAttMap.put("no",infonfo.getInpatientNo());
				fTreeJson.setAttributes(fAttMap);
				treeJsonList.add(fTreeJson);
			}
		}
		treeJson =  TreeJson.formatTree(treeJsonList);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		return gson.toJson(treeJson);
	}

	@Override
	public List<VinpatientApplyout> queryVinpatientApplyout(String deptId,
			String type, String page, String rows,String tradeName,String inpatientNo,String endDate,String beginDate) {
		return patientDispensingDAO.queryVinpatientApplyout(deptId, type, page, rows,tradeName,inpatientNo,endDate,beginDate);
	}

	@Override
	public int qqueryVinpatientApplyoutTotal(String deptId, String type,String tradeName,String inpatientNo,String endDate,String beginDate) {
		return patientDispensingDAO.qqueryVinpatientApplyoutTotal(deptId, type,tradeName,inpatientNo,endDate,beginDate);
	}

	@Override
	public SysDepartment querySysDepartment(String id) {
		return patientDispensingDAO.querySysDepartment(id);
	}

	@Override
	public User queryUser(String id) {
		return patientDispensingDAO.queryUser(id);
	}

	@Override
	public DrugBillclass queryDrugBillclass(String id) {
		return patientDispensingDAO.queryDrugBillclass(id);
	}
}
