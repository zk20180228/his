package cn.honry.inpatient.inoroutstandard.service;

import java.util.List;

import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.bean.model.InoroutStandardDetail;
import cn.honry.utils.TreeJson;


public interface InorOutStandardService {
	/**  
	 * <p>根据id获取出入径标准 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 下午1:58:20 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 下午1:58:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @return
	 * InoroutStandard
	 */
	InoroutStandard getStandard(String id);
	/**  
	 * <p>根据id获取出入径明细 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 下午1:58:38 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 下午1:58:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @return
	 * InoroutStandardDetail
	 */
	InoroutStandardDetail getStandardDetial(String id);
	/**  
	 * <p>保存或更新标准 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 下午1:59:04 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 下午1:59:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param arg0
	 * void
	 */
	void saveStandard(InoroutStandard arg0);
	/**  
	 * <p>保存或更新明细 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 下午1:59:21 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 下午1:59:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param detail
	 * void
	 */
	void saveStandardDetial(InoroutStandardDetail detail);
	/**  
	 * <p>根据标代码获取标准 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 下午1:59:35 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 下午1:59:35 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code
	 * @return
	 * List<InoroutStandard>
	 */
	List<InoroutStandard> getStandardList(String code);
	/**  
	 * <p>根据标准代码和版本号获取明细 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 下午1:59:57 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 下午1:59:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code
	 * @param versionNO
	 * @return
	 * List<InoroutStandardDetail>
	 */
	List<InoroutStandardDetail> getStandardDetialList(String code,String versionNO);
	/**  
	 * <p>标准树 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 下午2:00:19 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 下午2:00:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * List<TreeJson>
	 */
	TreeJson getSandardTree();
	void delStandard(String ids);
	void delStandardDetail(String ids);
}
