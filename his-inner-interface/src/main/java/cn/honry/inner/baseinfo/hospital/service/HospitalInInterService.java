package cn.honry.inner.baseinfo.hospital.service;

import java.util.List;

import cn.honry.base.bean.model.Hospital;
import cn.honry.base.service.BaseService;

/**
 * 
 * @author  liudelin
 * @date 2015-5-26 9:08
 * @version 1.0
 * @param <Employee>
 */
public interface HospitalInInterService  extends BaseService<Hospital>{
	
	/**  
	 *  
	 * @Description： 查询全部
	 * @Author：liudelin
	 * @CreateDate：2015-6-18 上午12:02:16  
	 * @Modifier：liudelin
	 * @ModifyDate：2015-6-18 上午12:02:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */	
	List<Hospital> findAll();
	
	/**  
	 * 
	 * <p> 根据code获取医院 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2017年2月20日 下午2:37:15 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年2月20日 下午2:37:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Hospital
	 *
	 */
	Hospital getHospitalByCode(String code);
	
	/**  
	 * 
	 * <p> 根据获取当前医院 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2017年2月20日 下午2:37:15 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年2月20日 下午2:37:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Hospital
	 *
	 */
	Hospital getPresentHospital();

}