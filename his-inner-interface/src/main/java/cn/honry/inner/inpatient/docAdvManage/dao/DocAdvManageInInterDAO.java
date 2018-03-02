package cn.honry.inner.inpatient.docAdvManage.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.inpatient.docAdvManage.vo.ProInfoInInterVo;

public interface DocAdvManageInInterDAO extends EntityDao<InpatientOrder>{

	/**  
	 *  
	 * @Description： 查询项目列表信息
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-26   
	 * @version 1.0
	 *
	 */
	List<ProInfoInInterVo> querySysInfo(String name,String type,String sysTypeName,String id);

}
