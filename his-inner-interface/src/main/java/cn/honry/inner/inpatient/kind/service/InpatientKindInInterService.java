package cn.honry.inner.inpatient.kind.service;
import java.util.List;

import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.service.BaseService;

public interface InpatientKindInInterService extends BaseService<InpatientKind>{
	/**  
	 *  
	 * 查询医嘱
	 * @author liguikang
	 * @CreateDate：2016-03-23  
	 * @version 1.0
	 *
	 */
	List<InpatientKind> getPage(String page, String rows,InpatientKind inpatientOrderSerch);

	/**  
	 *  
	 * 查询医嘱总条数
	 * @author：liguikang
	 * @CreateDate：2016-03-23   
	 * @version 1.0
	 *
	 */
	int getTotal(InpatientKind inpatientOrderSerch);
	
	
	/**  
	 *  
	 * 查询所有符合条件的数据
	 * @author：liguikang
	 * @CreateDate：2016-03-23 
	 * @version 1.0
	 *
	 */
	List<InpatientKind> queryKindInfo();
	
	

	/**
	 * 添加$修改
	 * @author  liguikang
	 * @date 2016-03-23
	 * @version 1.0
	 */
	void saveInpatientKind(InpatientKind entity);
	
	
	
	
	/**
	 * 删除
	 * @author  liguikang
	 * @date 2016-03-23
	 * @version 1.0
	 */
	void del(String ids);

	String queryKindInfoByName(String name);
}
