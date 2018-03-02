package cn.honry.outpatient.medicalrecord.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OutpatientMedicalrecord;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.medicalrecord.dao.MedicalrecordDAO;
import cn.honry.outpatient.medicalrecord.service.MedicalrecordService;
import cn.honry.outpatient.recipedetail.dao.RecipedetailDAO;

/**
 *
 * @Title: MedicalrecordServiceImpl.java
 * @Description：医嘱ServiceImpl
 * @Author：aizhonghua
 * @CreateDate：2016年4月18日 上午8:50:10 
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version： 1.0：
 * TODO：紧急任务，暂未做代码规范
 *
 */
@Service("medicalrecordService")
@Transactional
@SuppressWarnings({ "all" })
public class MedicalrecordServiceImpl implements MedicalrecordService{

	@Autowired
	@Qualifier(value = "medicalrecordDAO")
	private MedicalrecordDAO medicalrecordDAO;
	@Autowired
	@Qualifier(value = "recipedetailDAO")
	private RecipedetailDAO recipedetailDAO;//门诊处方DAO
	
	/**  
	 *  
	 * @Description：添加&修改
	 *@Author：wujiao
	 * @CreateDate：2015-7-9 上午15:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-7-9 上午15:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOrUpdatemedicalrecord(OutpatientMedicalrecord medicalrecord) {
		medicalrecordDAO.saveOrUpdatemedicalrecord(medicalrecord);
		OperationUtils.getInstance().conserve(null,"门诊病例","INSERT INTO","T_BUSINESS_MEDICALRECORD",OperationUtils.LOGACTIONINSERT);
	}

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public OutpatientMedicalrecord get(String id) {
		return medicalrecordDAO.get(id);
	}

	@Override
	public void saveOrUpdate(OutpatientMedicalrecord entity) {
		
	}

	/**  
	 * 根据门诊号和病历号查询患者是否有病历信息
	 * @Description：  获得护士站
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public boolean findMedicalByNoAndCode(String patientNo, String clinicCode) {
		return medicalrecordDAO.findMedicalByNoAndCode(patientNo,clinicCode);
	}
	
	/**  
	 *  
	 * @Description：获取执行科室ID
	 * @Author：huangbiao
	 * @CreateDate：2016-3-119上午11:56:59  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public List getDeptId(String clinicCode) {
		return medicalrecordDAO.getDeptId(clinicCode);
	}
	
	/**  
	 *  
	 * @Description： 根据门诊号和病历号查询所有的处方号
	 * @Author：tangfeishuai
	 * @CreateDate：2016-03-23下午15:20:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<String> findExecDpcdByCodeAndPatino(String clinicCode, String patientNo) {
		return medicalrecordDAO.findExecDpcdByCodeAndPatino(clinicCode, patientNo);
	}
	
	/**  
	 *  
	 * @Description： 根据门诊号和病历号查询所有的处方号
	 * @Author：wanxing
	 * @CreateDate：2016-03-09 下午18:20:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<String> findRecipeNo(String clinicCode, String patientNo) {
		return recipedetailDAO.findRecipeNo(clinicCode, patientNo);
	}
	
}
