package cn.honry.inpatient.diagnose.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessDiagnose;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.diagnose.vo.DiagnoseVo;


public interface DiagnoseService extends BaseService<BusinessDiagnose>{

	/**  
	 *  
	 * @Description：  根据住院流水号查询住院诊断
	 * @Author：aizhonghua
	 * @CreateDate：2015-9-2 下午01:45:32  
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-12-27 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<DiagnoseVo> queryDiagnoseBy(String inpatientNo) throws Exception;
	/**  
	 *  
	 * @Description：  查询icd诊断码
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-12-27 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<DiagnoseVo> queryicdCode() throws Exception;
	/**  
	 * 查询诊断代码
	 * @Description：  查询诊断代码
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-12-27 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<DiagnoseVo> queryCode() throws Exception;
	
	/**
	 * 
	 * 
	 * <p>查询医保诊断码 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午3:50:26 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午3:50:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param page
	 * @param rows
	 * @param diagnose
	 * @return:
	 * @throws Exception 
	 *
	 */
	List<DiagnoseVo> getPages(String page, String rows,BusinessDiagnose diagnose) throws Exception;
	
	/**
	 * 
	 * 
	 * <p>获取总条数（医保诊断码） </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午3:45:05 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午3:45:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param diagnose
	 * @return:
	 * @throws Exception 
	 *
	 */
	int getTotals(BusinessDiagnose diagnose) throws Exception;
    
	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  dh
	 * @CreateDate： 2015-7-1
	 * @param @param id
	 * @param @return   
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoNow> queryByMedicall(String medicalNo) throws Exception;

}
