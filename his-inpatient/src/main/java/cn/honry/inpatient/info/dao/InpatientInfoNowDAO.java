package cn.honry.inpatient.info.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.EntityDao;


/**
 * ClassName: InpatientInfoNowDAO 
 * @Description: 住院登记表业务逻辑接口
 * @author lt
 * @date 2015-6-24
 */
public interface InpatientInfoNowDAO extends EntityDao<InpatientInfoNow>{

		/**
		 * 
		 * 
		 * <p>查询住院登记主表信息 </p>
		 * @Author: XCL
		 * @CreateDate: 2017年7月4日 上午11:51:11 
		 * @Modifier: XCL
		 * @ModifyDate: 2017年7月4日 上午11:51:11 
		 * @ModifyRmk:  
		 * @version: V1.0
		 * @param hql
		 * @param page
		 * @param rows
		 * @return:
		 *
		 */
	    List<InpatientInfoNow> getinfopage(String hql,String page,String rows);

	    /**
	     * 
	     * 
	     * <p>查询住院登记主表信息条数 </p>
	     * @Author: XCL
	     * @CreateDate: 2017年7月4日 上午11:51:21 
	     * @Modifier: XCL
	     * @ModifyDate: 2017年7月4日 上午11:51:21 
	     * @ModifyRmk:  
	     * @version: V1.0
	     * @param hql
	     * @return:
	     *
	     */
	    int getinfoTotal(String hql);
}
