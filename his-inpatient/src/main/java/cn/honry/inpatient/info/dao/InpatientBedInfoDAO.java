package cn.honry.inpatient.info.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;


/**
 * ClassName: InpatientBedInfoDAO 
 * @Description: 住院床位使用记录表接口
 * @author lt
 * @date 2015-6-26
 */
public interface InpatientBedInfoDAO extends EntityDao<InpatientBedinfoNow>{

	/**
	 * @Description:通过床号获取InpatientBedinfo
	 * @Author：  lt
	 * @CreateDate： 2015-6-26
	 * @param @param bedId
	 * @return InpatientBedinfo  
	 * @version 1.0
	**/
	InpatientBedinfoNow getByBedId(String bedId);
	/**
	 * @Description:获取床号list
	 * @Author：  lt
	 * @CreateDate： 2015-6-29
	 * @param @return   
	 * @return List<InpatientBedinfo>  
	 * @version 1.0
	**/
	List<InpatientBedinfoNow> list();
	
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
	 *
	 */
	List<BusinessHospitalbed> bedlist();
	
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
	 *
	 */
	List<DepartmentContact> deplist();
	
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
	 *
	 */
	List<BusinessContractunit> reglist();
	
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
	 *
	 */
	List<SysEmployee> bedinfolist(String id);
	/**  
	 *  
	 * @Description：  查询住院床位使用记录
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-21 上午11:57:26  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-21 上午11:57:26  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<InpatientBedinfoNow> queryBedinfo(String id);
	/**
	 * @Description:通过主键ID获取InpatientBedinfo
	 * @Author：  TCJ
	 * @CreateDate： 2016-1-6
	 * @return InpatientBedinfo  
	 * @version 1.0
	**/
	InpatientBedinfoNow queryBedInfoByMainID(String id);
	
}
