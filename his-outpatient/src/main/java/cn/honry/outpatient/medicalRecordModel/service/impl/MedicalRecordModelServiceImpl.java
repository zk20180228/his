package cn.honry.outpatient.medicalRecordModel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessMedicalRecord;
import cn.honry.outpatient.medicalRecordModel.dao.MedicalRecordModelDAO;
import cn.honry.outpatient.medicalRecordModel.service.MedicalRecordModelService;
import cn.honry.utils.TreeJson;

/**  
 *  
 * @className：MedicalRecordAction 
 * @Description：  电子病历模版表
 * @Author：ldl
 * @CreateDate：2015-7-13   
 * @version 1.0
 *
 */
@Service("medicalRecordModelService")
@Transactional
@SuppressWarnings({ "all" })
public class MedicalRecordModelServiceImpl implements MedicalRecordModelService {
	
	@Autowired
	@Qualifier(value = "medicalRecordModelDAO")
	private MedicalRecordModelDAO medicalRecordModelDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public BusinessMedicalRecord get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(BusinessMedicalRecord entity) {
		
	}
	/**  
	 *  
	 * @Description：  保存电子病历模版表
	 * @Author：liudelin
	 * @ModifyDate：2015-7-13 下午05：43
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOrUpdateRecord(BusinessMedicalRecord entity) {
		medicalRecordModelDAO.saveOrUpdateRecord(entity);
	}
	/**  
	 *  
	 * @Description：  电子病历模版树
	 * @Author：liudelin
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> medicalRecordTree() {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		
			//根节点
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("0");
			pTreeJson.setText("模板信息");
			treeJsonList.add(pTreeJson);
			//院级组套
			TreeJson yTreeJson = new TreeJson();
			yTreeJson.setId("1");
			yTreeJson.setText("全院级模板");
			Map<String,String> yAttMap = new HashMap<String, String>();
			yAttMap.put("pid", "0");
			yTreeJson.setAttributes(yAttMap);
			treeJsonList.add(yTreeJson);
			//部门级组套
			TreeJson bTreeJson = new TreeJson();
			bTreeJson.setId("2");
			bTreeJson.setText("部门级模板");
			Map<String,String> bAttMap = new HashMap<String, String>();
			bAttMap.put("pid", "0");
			bTreeJson.setAttributes(bAttMap);
			treeJsonList.add(bTreeJson);
			//个人级组套
			TreeJson gTreeJson = new TreeJson();
			gTreeJson.setId("3");
			gTreeJson.setText("个人级模板");
			Map<String,String> gAttMap = new HashMap<String, String>();
			gAttMap.put("pid", "0");
			gTreeJson.setAttributes(gAttMap);
			treeJsonList.add(gTreeJson);
	
		return treeJsonList;
	}
	/**  
	 *  
	 * @Description：  电子病历模板列表
	 * @Author：liudelin
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */

	@Override
	public List<BusinessMedicalRecord> queryMedicalRecordList(String id, String recordType) {
		List<BusinessMedicalRecord> lst = medicalRecordModelDAO.queryMedicalRecordList(id,recordType);
		return lst; 
	}
	/**  
	 *  
	 * @Description：  电子病历模版树(个别)
	 * @Author：liudelin
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> medicalRecordOtherTree(String type) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		
			//根节点
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("0");
			pTreeJson.setText("模板信息");
			treeJsonList.add(pTreeJson);
			//院级组套
			TreeJson yTreeJson = new TreeJson();
			yTreeJson.setId("1");
			yTreeJson.setText("全院级模板");
			Map<String,String> yAttMap = new HashMap<String, String>();
			yAttMap.put("pid", "0");
			yAttMap.put("isNO", "0");
			yTreeJson.setAttributes(yAttMap);
			treeJsonList.add(yTreeJson);
			List<BusinessMedicalRecord> ymedicalRecordList = medicalRecordModelDAO.medicalRecordOtherTree(1);
			if(ymedicalRecordList!=null&&ymedicalRecordList.size()>0){
				for(BusinessMedicalRecord medicalRecord : ymedicalRecordList){
					TreeJson ysTreeJson = new TreeJson();
					ysTreeJson.setId(medicalRecord.getId());
					if("1".equals(type)){
						ysTreeJson.setText(medicalRecord.getMaindesc());
					}else if("10".equals(type)){
						ysTreeJson.setText(medicalRecord.getCheckresult());
					}else if("2".equals(type)){
						ysTreeJson.setText(medicalRecord.getAllergichistory());
					}else if("3".equals(type)){
						ysTreeJson.setText(medicalRecord.getHeredityHis());
					}else if("4".equals(type)){
						ysTreeJson.setText(medicalRecord.getPresentillness());
					}else if("5".equals(type)){
						Double temperature = medicalRecord.getTemperature();
						String temperatures = temperature+"";
						ysTreeJson.setText(temperatures);
					}else if("6".equals(type)){
						Double pulse = medicalRecord.getPulse();
						String pulses = pulse+"";
						ysTreeJson.setText(pulses);
					}else if("7".equals(type)){
						Double breathing = medicalRecord.getBreathing();
						String breathings = breathing+"";
						ysTreeJson.setText(breathings);
					}else if("8".equals(type)){
						ysTreeJson.setText(medicalRecord.getBloodPressure());
					}else if("9".equals(type)){
						ysTreeJson.setText(medicalRecord.getPhysicalExamination());
					}else if("11".equals(type)){
						ysTreeJson.setText(medicalRecord.getDiagnose1());
					}else if("12".equals(type)){
						ysTreeJson.setText(medicalRecord.getAdvice());
					}else if("13".equals(type)){
						ysTreeJson.setText(medicalRecord.getHistoryspecil());
					}
					Map<String,String> ysAttMap = new HashMap<String, String>();
					ysAttMap.put("pid","1");
					ysAttMap.put("isNO","1");
					ysTreeJson.setAttributes(ysAttMap);
					treeJsonList.add(ysTreeJson);
				}
			}
			//部门级组套
			TreeJson bTreeJson = new TreeJson();
			bTreeJson.setId("2");
			bTreeJson.setText("部门级模板");
			Map<String,String> bAttMap = new HashMap<String, String>();
			bAttMap.put("pid", "0");
			bAttMap.put("isNO", "0");
			bTreeJson.setAttributes(bAttMap);
			treeJsonList.add(bTreeJson);
			List<BusinessMedicalRecord> bmedicalRecordList = medicalRecordModelDAO.medicalRecordOtherTree(2);
			if(bmedicalRecordList!=null&&bmedicalRecordList.size()>0){
				for(BusinessMedicalRecord medicalRecord : bmedicalRecordList){
					TreeJson bsTreeJson = new TreeJson();
					bsTreeJson.setId(medicalRecord.getId());
					if("1".equals(type)){
						bsTreeJson.setText(medicalRecord.getMaindesc());
					}else if("10".equals(type)){
						bsTreeJson.setText(medicalRecord.getCheckresult());
					}else if("2".equals(type)){
						bsTreeJson.setText(medicalRecord.getAllergichistory());
					}else if("3".equals(type)){
						bsTreeJson.setText(medicalRecord.getHeredityHis());
					}else if("4".equals(type)){
						bsTreeJson.setText(medicalRecord.getPresentillness());
					}else if("5".equals(type)){
						Double temperature = medicalRecord.getTemperature();
						String temperatures = temperature+"";
						bsTreeJson.setText(temperatures);
					}else if("6".equals(type)){
						Double pulse = medicalRecord.getPulse();
						String pulses = pulse+"";
						bsTreeJson.setText(pulses);
					}else if("7".equals(type)){
						Double breathing = medicalRecord.getBreathing();
						String breathings = breathing+"";
						bsTreeJson.setText(breathings);
					}else if("8".equals(type)){
						bsTreeJson.setText(medicalRecord.getBloodPressure());
					}else if("9".equals(type)){
						bsTreeJson.setText(medicalRecord.getPhysicalExamination());
					}else if("11".equals(type)){
						bsTreeJson.setText(medicalRecord.getDiagnose1());
					}else if("12".equals(type)){
						bsTreeJson.setText(medicalRecord.getAdvice());
					}else if("13".equals(type)){
						bsTreeJson.setText(medicalRecord.getHistoryspecil());
					}
					Map<String,String> bsAttMap = new HashMap<String, String>();
					bsAttMap.put("pid","2");
					bsAttMap.put("isNO","1");
					bsTreeJson.setAttributes(bsAttMap);
					treeJsonList.add(bsTreeJson);
				}
			}
			//个人级组套
			TreeJson gTreeJson = new TreeJson();
			gTreeJson.setId("3");
			gTreeJson.setText("个人级模板");
			Map<String,String> gAttMap = new HashMap<String, String>();
			gAttMap.put("pid", "0");
			gAttMap.put("isNO", "0");
			gTreeJson.setAttributes(gAttMap);
			treeJsonList.add(gTreeJson);
			List<BusinessMedicalRecord> gmedicalRecordList = medicalRecordModelDAO.medicalRecordOtherTree(3);
			if(gmedicalRecordList!=null&&gmedicalRecordList.size()>0){
				for(BusinessMedicalRecord medicalRecord : gmedicalRecordList){
					TreeJson gsTreeJson = new TreeJson();
					gsTreeJson.setId(medicalRecord.getId());
					if("1".equals(type)){
						gsTreeJson.setText(medicalRecord.getMaindesc());
					}else if("10".equals(type)){
						gsTreeJson.setText(medicalRecord.getCheckresult());
					}else if("2".equals(type)){
						gsTreeJson.setText(medicalRecord.getAllergichistory());
					}else if("3".equals(type)){
						gsTreeJson.setText(medicalRecord.getHeredityHis());
					}else if("4".equals(type)){
						gsTreeJson.setText(medicalRecord.getPresentillness());
					}else if("5".equals(type)){
						Double temperature = medicalRecord.getTemperature();
						String temperatures = temperature+"";
						gsTreeJson.setText(temperatures);
					}else if("6".equals(type)){
						Double pulse = medicalRecord.getPulse();
						String pulses = pulse+"";
						gsTreeJson.setText(pulses);
					}else if("7".equals(type)){
						Double breathing = medicalRecord.getBreathing();
						String breathings = breathing+"";
						gsTreeJson.setText(breathings);
					}else if("8".equals(type)){
						gsTreeJson.setText(medicalRecord.getBloodPressure());
					}else if("9".equals(type)){
						gsTreeJson.setText(medicalRecord.getPhysicalExamination());
					}else if("11".equals(type)){
						gsTreeJson.setText(medicalRecord.getDiagnose1());
					}else if("12".equals(type)){
						gsTreeJson.setText(medicalRecord.getAdvice());
					}else if("13".equals(type)){
						gsTreeJson.setText(medicalRecord.getHistoryspecil());
					}
					Map<String,String> gsAttMap = new HashMap<String, String>();
					gsAttMap.put("pid","3");
					gsAttMap.put("isNO","1");
					gsTreeJson.setAttributes(gsAttMap);
					treeJsonList.add(gsTreeJson);
				}
			}
		
		return treeJsonList;
	}
	

}
