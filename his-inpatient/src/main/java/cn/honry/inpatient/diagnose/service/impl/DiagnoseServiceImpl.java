package cn.honry.inpatient.diagnose.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDiagnose;
import cn.honry.base.bean.model.BusinessDiagnoseMedicare;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.diagnose.dao.DiagnoseDAO;
import cn.honry.inpatient.diagnose.dao.DiagnoseMedicareDAO;
import cn.honry.inpatient.diagnose.service.DiagnoseService;
import cn.honry.inpatient.diagnose.vo.DiagnoseVo;
import cn.honry.utils.ShiroSessionUtils;

@Service("diagnoseService")
@Transactional
@SuppressWarnings({ "all" })
public class DiagnoseServiceImpl implements DiagnoseService{
	

	@Autowired
	@Qualifier(value = "diagnoseDAO")
	private DiagnoseDAO diagnoseDAO;
	@Autowired
	@Qualifier(value = "diagnoseMedicareDAO")
	private DiagnoseMedicareDAO diagnoseMedicareDAO;
	@Override
	public void removeUnused(String id) {
	}
	@Override
	public void saveOrUpdate(BusinessDiagnose entity) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateUser(userId);
			entity.setCreateDept(deptId);
			entity.setCreateTime(new Date());
			diagnoseDAO.save(entity);
			OperationUtils.getInstance().conserve(null,"住院诊断","INSERT INTO","T_BUSINESS_DIAGNOSE",OperationUtils.LOGACTIONINSERT);
		}else{
			String id=entity.getId();
			BusinessDiagnose entity1=diagnoseDAO.getval(id);
			BusinessDiagnoseMedicare entity2=null;
			if(null==entity1.getId()){
				entity2=diagnoseMedicareDAO.getval(id);
				entity2.setDel_flg(1);
				diagnoseMedicareDAO.save(entity2);
				id=null;
			}
			entity1.setId(id);
			entity1.setInpatientNo(entity.getInpatientNo());
			entity1.setDiagKind(entity.getDiagKind());
			entity1.setIcdCode(entity.getIcdCode());
			entity1.setDiagName(entity.getDiagName());
			entity1.setDoctName(entity.getDoctName());
			entity1.setDiagDate(entity.getDiagDate());
			entity1.setMainFlay(entity.getMainFlay());
			if(entity2!=null&&null!=entity2.getId()){
				entity1.setCreateDept(entity2.getCreateDept());
				entity1.setCreateTime(entity2.getCreateTime());
				entity1.setCreateUser(entity2.getCreateUser());
			}
			entity1.setDel_flg(0);
			diagnoseDAO.save(entity1);
			OperationUtils.getInstance().conserve(entity.getId(),"住院诊断","UPDATE","T_BUSINESS_DIAGNOSE",OperationUtils.LOGACTIONINSERT);
		}
		
	}
	
	@Override
	public BusinessDiagnose get(String id) {
		return diagnoseDAO.get(id);
	}

	/**  
	 *  
	 * @Description：  根据住院流水号查询住院诊断
	 * @Author：zhangjin
	 * @CreateDate：2015-12-24 下午01:45:32  
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DiagnoseVo> queryDiagnoseBy(String inpatientNo) throws Exception {
		return diagnoseDAO.queryDiagnoseBy(inpatientNo);
	}
	/**  
	 *  
	 * @Description：  查询icd诊断码
	 * @Author：zhangjin
	 * @CreateDate：2015-12-24   
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DiagnoseVo> queryicdCode() throws Exception {
		
		return diagnoseDAO.queryicdCode();
	}
	/**  
	 *  
	 * @Description：  查询医保诊断码
	 * @Author：zhangjin
	 * @CreateDate：2015-12-24   
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DiagnoseVo> queryCode() throws Exception {
		
		return diagnoseDAO.queryCode();
	}

	@Override
	public List<DiagnoseVo> getPages(String page, String rows, BusinessDiagnose diagnose) throws Exception {
		
		return diagnoseDAO.getPages(page,rows,diagnose);
	}

	@Override
	public int getTotals(BusinessDiagnose diagnose) throws Exception {
		
		return diagnoseDAO.getTotals(diagnose);
	}
   
	@Override
	public List<InpatientInfoNow> queryByMedicall(String medicalNo) throws Exception {
		return diagnoseDAO.queryByMedicall(medicalNo);
	}
}
