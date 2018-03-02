package cn.honry.statistics.drug.patientDispensingOut.service.Impl;

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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.inpatient.info.dao.InpatientInfoInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;
import cn.honry.statistics.drug.patientDispensingOut.dao.PatientDispensingOutDAO;
import cn.honry.statistics.drug.patientDispensingOut.service.PatientDispensingOutService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("patientDispensingOutService")
@Transactional
@SuppressWarnings({ "all" })
public class PatientDispensingOutServiceImpl implements PatientDispensingOutService{
	@Autowired
	@Qualifier(value = "patientDispensingOutDAO")
	private PatientDispensingOutDAO patientDispensingOutDAO;
	@Autowired
	private InpatientInfoInInterDAO inpatientInfoDAO;
	
	public void setInpatientInfoDAO(InpatientInfoInInterDAO inpatientInfoDAO) {
		this.inpatientInfoDAO = inpatientInfoDAO;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
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
	 * @throws Exception 
	 *
	 */
	@Override
	public List<TreeJson> queryPatientTree(String deptId,String flag) throws Exception {
		String type=patientDispensingOutDAO.queryState(deptId).getDeptType();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		List<InpatientInfoNow> infoList = patientDispensingOutDAO.queryPatient(deptId,type,flag);
		//根节点
		TreeJson gTreeJson = new TreeJson();
		gTreeJson.setId("1");
		gTreeJson.setText("本区患者");
		gTreeJson.setIconCls("icon-house");
		Map<String, String> map = new HashMap<String, String>();
		List<BusinessContractunit> list = inpatientInfoDAO.queryContractunit();
		if(list!=null&&list.size()>0){
			for(BusinessContractunit contractunit : list){
				map.put(contractunit.getEncode(), contractunit.getName());
			}
		}
		Map<String,String> gAttMap = new HashMap<String, String>();
		gTreeJson.setAttributes(gAttMap);
		TreeJson fTreeJson = null;
		Map<String,String> fAttMap = null;
		if(infoList!=null&&infoList.size()>0){
			for(InpatientInfoNow infonfo : infoList){
				//二级节点(患者)
				fTreeJson = new TreeJson();
				fTreeJson.setId(infonfo.getInpatientNo());
				infonfo.setPactCode(map.get(infonfo.getPactCode()));
				if(StringUtils.isBlank(infonfo.getPactCode())){
					infonfo.setPactCode("自费");
				}
				if(infonfo.getBedName()==null){
					fTreeJson.setText("【"+infonfo.getMedicalrecordId()+"】"+infonfo.getPatientName()+"【"+infonfo.getPactCode()+"】");
				}else{
					fTreeJson.setText("【"+infonfo.getBedName()+"】【"+infonfo.getMedicalrecordId()+"】"+infonfo.getPatientName()+"【"+infonfo.getPactCode()+"】");
				}
				if("3".equals(infonfo.getReportSex())||"1".equals(infonfo.getReportSex())){
					fTreeJson.setIconCls("icon-user_b");
				}	
				else{
					fTreeJson.setIconCls("icon-user_female");
				}
				fTreeJson.setState("open");
				fAttMap = new HashMap<String, String>();
				fAttMap.put("pid", infonfo.getInState());
				fAttMap.put("no",infonfo.getInpatientNo());
				fTreeJson.setAttributes(fAttMap);
				treeJson.add(fTreeJson);
			}
			gTreeJson.setChildren(treeJson);
		}
		treeJsonList.add(gTreeJson);
		return treeJsonList;
	}

	@Override
	public List<VinpatientApplyout> queryVinpatientApplyout(String deptId,
			String type, String page, String rows,String tradeName,String inpatientNo,String etime,String stime,String flag,String sign) throws Exception {
		List<String> tnL = new ArrayList<String>();
		return patientDispensingOutDAO.queryVinpatientApplyout(tnL,deptId, type, page, rows,tradeName,inpatientNo,etime,stime,flag,sign);
	}

	@Override
	public int qqueryVinpatientApplyoutTotal(String deptId, String type,String tradeName,String inpatientNo,String etime,String stime,String flag) throws Exception {
		String redKey = "BQHZBYCX:"+stime+"_"+etime+"_"+deptId+"_"+type+"_"+tradeName+"_"+inpatientNo+"_"+flag;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			totalNum = patientDispensingOutDAO.qqueryVinpatientApplyoutTotal(null,deptId, type,tradeName,inpatientNo,etime,stime,flag);
			redisUtil.set(redKey, totalNum);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		return totalNum;
	}

	@Override
	public SysDepartment querySysDepartment(String id) throws Exception {
		return patientDispensingOutDAO.querySysDepartment(id);
	}

	@Override
	public User queryUser(String id) throws Exception {
		return patientDispensingOutDAO.queryUser(id);
	}

	@Override
	public DrugBillclass queryDrugBillclass(String id) throws Exception {
		return patientDispensingOutDAO.queryDrugBillclass(id);
	}

	@Override
	public List<InpatientInfoNow> querypatient(String flag,String medicalrecordId) throws Exception {
		return patientDispensingOutDAO.querypatient(flag, medicalrecordId);
	}
	@Override
	public Map<String, String> queryUnitMap() throws Exception {
		List<Object[]> list=patientDispensingOutDAO.queryUnitMap();
		Map<String,String> map=new HashMap<>();
		for(Object[] obj:list){
			map.put(obj[0].toString(), obj[1].toString());
		}
		return map;
	}

}
