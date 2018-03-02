package cn.honry.oa.juris.service;

import java.util.List;

import cn.honry.base.bean.model.OaJuris;
import cn.honry.base.service.BaseService;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;
import cn.honry.oa.juris.vo.EchoVo;
import cn.honry.oa.juris.vo.JurisVo;
import cn.honry.utils.TreeJson;

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
public interface JurisService extends BaseService<OaJuris>{

	/**  
	 * 保存流程权限
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveJuris(JurisVo vo);

	/**  
	 * 根据流程id获取流程权限对照key:权限分类编码(编码)value:{key:范围编码value:权限分类编码(编码)}
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	EchoVo getJurisMap(String id);
	
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
	List<String> getJurisList();
	
	/**  
	 * 获取有权限的流程分类树
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> getProcessTree();

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
