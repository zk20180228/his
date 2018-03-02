package cn.honry.inner.baseinfo.frequency.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.dao.EntityDao;

public interface FrequencyInInterDAO extends EntityDao<BusinessFrequency>{

	BusinessFrequency getCode(String drugFrequency);
	
	/**  
	 *  
	 * @Description： 查询频次List
	 * @Author：yeguanqun
	 * @CreateDate：2016-7-22   
	 * @version 1.0
	 *
	 */
	List<BusinessFrequency> queryFrequencyList();

	/**  
	 * 
	 * <p> 根据编码及开立科室获取频次信息 </p>
	 * <p> 如果该科室下无对应编码的信息则返回全院频次对应编码的频次信息 </p>
	 * <p> 如果以上都不存在则返回null </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月27日 上午10:31:32 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月27日 上午10:31:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	BusinessFrequency getCodeAndDept(String frequencyCode,String deptCode); 
	

}
