package cn.honry.inpatient.info.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;


/**
 * ClassName: InpatientBedInfoService 
 * @author lt
 * @date 2015-6-26
 */
public interface InpatientBedInfoService extends BaseService<InpatientBedinfoNow> {

	/**
	 * @Description:通过床号拿到InpatientBedinfo
	 * @Author：  lt
	 * @CreateDate： 2015-6-26
	 * @param @param bedId
	 * @return InpatientBedinfo  
	 * @version 1.0
	 * @throws Exception 
	**/
	InpatientBedinfoNow getByBedId(String bedId) throws Exception;
	
	/**
	 * @Description:获取床号list
	 * @Author：  lt
	 * @CreateDate： 2015-6-29
	 * @param @return   
	 * @return List<InpatientBedinfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientBedinfoNow> list() throws Exception;
	
	/**
	 * 
	 * 
	 * <p>获取所有病床信息 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:55:33 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:55:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws Exception 
	 *
	 */
	List<BusinessHospitalbed> bedlist() throws Exception;
	
	/**
	 * 
	 * 
	 * <p>获取所有部门科室间关系 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:56:21 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:56:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws Exception 
	 *
	 */
	List<DepartmentContact> deplist() throws Exception;
	
	/**
	 * 
	 * 
	 * <p>查询所有合同单位维护的代码和名称 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:56:59 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:56:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws Exception 
	 *
	 */
	List<BusinessContractunit> reglist() throws Exception;
	
	/**
	 * 
	 * 
	 * <p>根据病房使用记录的医生代码查询医生信息 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:59:45 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:59:45 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @return:
	 * @throws Exception 
	 *
	 */
	List<SysEmployee> houseDoclist(String id) throws Exception;
}
