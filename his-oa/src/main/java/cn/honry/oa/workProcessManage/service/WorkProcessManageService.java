package cn.honry.oa.workProcessManage.service;

import java.util.List;

import cn.honry.oa.workProcessManage.vo.ProcessInfoVo;
import cn.honry.oa.workProcessManage.vo.WorkProcessManageVo;

public interface WorkProcessManageService {
	
		/**
		 * 
		 * <p> 跳转到工作流程管理页面时查询左侧的父栏目</p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年7月18日 上午11:37:43
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月18日 上午11:37:43
		 * @ModifyRmk:
		 * @version: V1.0
		 * @return
		 * @throws:Exception
		 *
		 */
		public List<WorkProcessManageVo> fatherMenuList()throws Exception;
	
		/**
		 * 
		 * <p>根据父栏目code:展现左侧栏目的--子栏目 </p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年7月18日 上午11:38:42
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月18日 上午11:38:42
		 * @ModifyRmk:
		 * @version: V1.0
		 * @param menuCode 栏目的code
		 * @throws:Exception
		 */
		public List<WorkProcessManageVo> spreadSonMenu(String menuCode)throws Exception;
	
		/**
		 * 
		 * <p>根据子栏目code，查询子栏目下的流程详情，倒序 </p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年7月18日 上午11:38:42
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月18日 上午11:38:42
		 * @ModifyRmk:
		 * @version: V1.0
		 * @param menuCode 栏目的code
		 * @throws:Exception
		 */
		public List<ProcessInfoVo> queryProcessList(String menuCode)throws Exception;
		
		/**
		 * 
		 * <p>根据流程详情的id，查询流程说明 </p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年7月18日 上午11:38:42
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月18日 上午11:38:42
		 * @ModifyRmk:
		 * @version: V1.0
		 * @param menuCode 栏目的code
		 * @throws:Exception
		 */
		public String  queryProcessInfo(String processId)throws Exception;
	
}
