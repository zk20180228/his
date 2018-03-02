package cn.honry.oa.juris.dao;

import java.util.List;

import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaJuris;
import cn.honry.base.dao.EntityDao;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;

/** 
 * 
 * @Author：aizhonghua
 * @CreateDate：2018-2-1 下午20:32:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2018-2-1 下午20:32:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface JurisDAO extends EntityDao<OaJuris>{

	/**  
	 * 根据权限id删除流程权限
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void delJuris(String id);

	/**  
	 * 根据流程id获取流程权限
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OaJuris> getJurisByFlowCode(String id);
	
	/**  
	 * 获取有权限的流程id,返回id集合或null
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<String> getJurisListByJobNo(String jobNo);
	
	/**  
	 * 获取有权限的流程分类
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OaBpmCategory> getCategoryById(List<String> idList);
	
	/**  
	 * 获取有权限的流程科室
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OaActivitiDept> getActivitiDeptById(List<String> idList);

	/**  
	 * 获取有权限的流程列表-分页数据
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OaProcessVo> getProcessListByJuris(int page, int rows, String name, String category, String deptcode,List<String> idList);

	/**  
	 * 获取有权限的流程列表-总条数
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getProcessTotalByJuris(String name, String category, String deptcode,List<String> idList);

}
