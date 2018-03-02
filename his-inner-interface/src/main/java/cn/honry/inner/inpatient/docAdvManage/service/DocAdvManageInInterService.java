package cn.honry.inner.inpatient.docAdvManage.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.service.BaseService;
import cn.honry.inner.inpatient.docAdvManage.vo.ProInfoInInterVo;

public interface DocAdvManageInInterService extends BaseService<InpatientOrder>{

	/**  
	 *  
	 * @Description： 查询项目信息
	 * @Author：yeguanqun
	 * @param name：项目名称
	 * @param type：系统类别代码
	 * @param sysTypeName：系统类别名称
	 * @param id：项目id
	 * @CreateDate：2015-12-27   
	 * @version 1.0
	 *
	 */
	public List<ProInfoInInterVo> querySysInfo(String name,String type,String sysTypeName,String id);
	
}
