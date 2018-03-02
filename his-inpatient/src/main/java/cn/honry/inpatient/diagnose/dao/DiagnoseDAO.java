package cn.honry.inpatient.diagnose.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessDiagnose;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.diagnose.vo.DiagnoseVo;

@SuppressWarnings({"all"})
public interface DiagnoseDAO extends EntityDao<BusinessDiagnose>{

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
	List<DiagnoseVo> queryDiagnoseBy(String inpatientNo);
	/**  
	 *  
	 * @Description：  查询icd诊断码
	 * @Author：zhangjin
	 * @CreateDate：2015-12-25 下午01:45:32  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DiagnoseVo> queryicdCode();
	/**  
	 *  
	 * @Description：  查询医保诊断码
	 * @Author：zhangjin
	 * @CreateDate：2015-12-25 下午01:45:32  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DiagnoseVo> queryCode();

	/**
	 * 
	 * 
	 * <p>分页查询诊断代码 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午3:46:22 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午3:46:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param page 当前页数
	 * @param rows 当前页条数
	 * @param diagnose 
	 * @return:
	 *
	 */
	List<DiagnoseVo> getPages(String page, String rows,BusinessDiagnose diagnose);

	/**
	 * 
	 * 
	 * <p>诊断代码获得总条数 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午3:47:29 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午3:47:29 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param diagnose
	 * @return:
	 *
	 */
	int getTotals(BusinessDiagnose diagnose);
	
	/**
	 * 
	 * 
	 * <p>再次获取icd医保信息 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午3:51:16 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午3:51:16 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @return:
	 *
	 */
	BusinessDiagnose getval(String id);

	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  dh
	 * @CreateDate： 2015-7-1
	 * @param @param id
	 * @param @return   
	 * @return InpatientInfo  
	 * @version 1.0
	**/
	List<InpatientInfoNow> queryByMedicall(String medicalNo);
}
