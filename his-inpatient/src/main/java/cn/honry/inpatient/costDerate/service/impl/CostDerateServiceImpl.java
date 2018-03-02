package cn.honry.inpatient.costDerate.service.impl;

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

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inpatient.costDerate.dao.CostDerateDao;
import cn.honry.inpatient.costDerate.service.CostDerateService;
import cn.honry.inpatient.costDerate.vo.DerateVo;
import cn.honry.inpatient.costDerate.vo.ThreeSearchVo;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
import cn.honry.utils.TreeJson;
@Service("costDerateService")
@Transactional
@SuppressWarnings({ "all" })
public class CostDerateServiceImpl implements CostDerateService {
	

	@Autowired
	@Qualifier(value = "costDerateDao")
	private CostDerateDao costDerateDao;
	@Autowired
	@Qualifier(value = "inpatientInfoDAO")
	private InpatientInfoDAO inpatientInfoDAO;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public InpatientDerate get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientDerate entity) {
	}
	/**  
	 * @Description：  根据病历号查询最新的接诊记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public DerateVo queryInpatientInfoObj(String medicalrecordId,String deptCode) throws Exception{
		return costDerateDao.queryInpatientInfoObj(medicalrecordId,deptCode);
	}
	/**  
	 * @Description：  根据住院流水号查询ThreeSarchVo的信息
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<ThreeSearchVo> queryThreeSearch(String inpatientNo) throws Exception{
		return costDerateDao.queryThreeSearch(inpatientNo);
	}
	/**  
	 * @Description：  根据住院流水号查询费用减免表中的记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientDerate> queryDerate(String inpatientNo) throws Exception{
		return costDerateDao.queryDerate(inpatientNo);
	}
	/**  
	 * @Description：  保存费用减免信息
	 * @Author：TCJ
	 * @CreateDate：2015-1-14
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public void saveDerateDategrid(String derateJson,String no) throws Exception {
		InpatientInfoNow info=inpatientInfoDAO.queryByInpatientNo(no);
		//设置患者封账状态
		info.setStopAcount(1);
		info.setUpdateTime(new Date());
		inpatientInfoDAO.save(info);
		int num=forward(no);
		List<InpatientDerate> derateList = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create()
				.fromJson(derateJson, new TypeToken<List<InpatientDerate>>(){}.getType());
		for(int i=0;i<derateList.size();i++){
			derateList.get(i).setTransType(1);
			derateList.get(i).setSequenceNo(0);
			derateList.get(i).setClinicNo(no);
			derateList.get(i).setBalanceState("0");
			derateList.get(i).setHappenNo(num+1);
			derateList.get(i).setCreateUser(derateList.get(i).getUpdateUser());
			derateList.get(i).setCreateTime(derateList.get(i).getUpdateTime());
		}
		InpatientInfoNow info1=inpatientInfoDAO.queryByInpatientNo(no);
		//设置患者开账状态
		info1.setStopAcount(0);
		info1.setUpdateTime(new Date());
		List<InpatientInfoNow> lst=new ArrayList<InpatientInfoNow>();
		lst.add(info1);
		inpatientInfoDAO.saveOrUpdateList(lst);
		costDerateDao.saveOrUpdateList(derateList);
	}
	/**  
	 * @Description：  保存费用减免信息前的处理(将表中的所有未做反处理的数据进行反处理),返回最大发生序号
	 * @Author：TCJ
	 * @CreateDate：2015-1-15
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	private int forward(String no) {
		int num=1;
		List<InpatientDerate>  ipDerate= costDerateDao.queryDerate(no);
		if(ipDerate!=null&&ipDerate.size()>0){
			for(int i=0;i<ipDerate.size();i++){
				ipDerate.get(i).setTransType(2);
				ipDerate.get(i).setDerateCost(ipDerate.get(i).getDerateCost()+(-ipDerate.get(i).getDerateCost()));
			}
			costDerateDao.saveOrUpdateList(ipDerate);
			num=ipDerate.get(0).getHappenNo();
		}
		return num;
	}
	/**  
	 * @Description：  查询最小费用名称Map
	 * @Author：TCJ
	 * @CreateDate：2015-1-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<MinfeeStatCode> quertFreeCodeMap() throws Exception{
		return costDerateDao.quertFreeCodeMap();
	}

	@Override
	public List<InpatientDerate> queryDerateMoneySum(String inpatientNo) throws Exception{
		return costDerateDao.queryDerateMoneySum(inpatientNo);
	}

	@Override
	public InpatientInfoNow querNurseCharge(String inpatientId) {
		return costDerateDao.querNurseCharge(inpatientId);
	}


	@Override
	public List<TreeJson> patientNarTree(String deptCode, String id) throws Exception{
		List<InpatientInfoNow> listDept=new ArrayList<InpatientInfoNow>();
		List<TreeJson> treeJsonList=new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){//根节点
			//
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("1");
			pTreeJson.setText("本区患者");
			treeJsonList.add(pTreeJson);
		}
		listDept=costDerateDao.patientNarTree(deptCode);
		if(listDept != null && listDept.size() > 0){
			for (InpatientInfoNow sysDept : listDept) {
				TreeJson treeJson = new TreeJson();
				treeJson.setId(sysDept.getMedicalrecordId());
				treeJson.setText(sysDept.getPatientName());
				Map<String,String> attributes=new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("inpatientNo", sysDept.getInpatientNo());
				attributes.put("idcardNo", sysDept.getIdcardNo());
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}		
		return TreeJson.formatTree(treeJsonList);
	}


	@Override
	public List<InpatientInfoNow> queryInpatientInfoList(String inpatientNo) throws Exception{
		return costDerateDao.queryInpatientInfoList(inpatientNo);
	}

	@Override
	public List<SysEmployee> empComboboxDerate() throws Exception{
		return costDerateDao.empComboboxDerate();
	}
	
	@Override
	public List<User> queryUserDerate() throws Exception{
		return costDerateDao.queryUserDerate();
	}

	@Override
	public List<User> queryUserDerates() throws Exception{
		return costDerateDao.queryUserDerates();
	}
}
