package cn.honry.inpatient.cordon.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.cordon.vo.CordonVo;
/**
 * 护士站患者警戒线DAO
 * @author  lyy
 * @createDate： 2016年4月1日 下午5:01:31 
 * @modifier lyy
 * @modifyDate：2016年4月1日 下午5:01:31
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface CordonDAO extends EntityDao<InpatientInfo> {
	/**
	 * 病区与护士站树
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午4:22:31 
	 * @modifier lyy
	 * @modifyDate：2015年12月15日 下午4:22:31  
	 * @param deptId 护士站病区Id
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> findTree(String deptId);
	
	/**
	 * 连表查询数据(总条数)
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午5:14:43 
	 * @modifier lyy
	 * @modifyDate：2015年12月15日 下午5:14:43  
	 * @param entity 患者警戒线虚拟实体 deptId 登录科室
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotal(CordonVo entity,String deptId);
	/**
	 * 连表查询分页list数据
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午5:13:11 
	 * @modifier lyy
	 * @modifyDate：2015年12月15日 下午5:13:11
	 * @param entity 患者警戒线虚拟实体  page 当前页   rows 当前页条数  deptId 登录科室
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<CordonVo> getPage(CordonVo entity,String page, String rows,String deptId);

	/**
	 * 根据患者id去查住院主表的信息
	 * @author  lyy
	 * @createDate： 2016年4月1日 下午5:03:25 
	 * @modifier lyy
	 * @modifyDate：2016年4月1日 下午5:03:25
	 * @param：   id 患者主键
	 * @modifyRmk：  
	 * @version 1.0
	 */
	InpatientInfoNow getInpatientInfo(String id);
	/**
	 * 根据病区id统计该病区下的患者
	 * @author  lyy
	 * @createDate： 2016年4月1日 下午5:03:15 
	 * @modifier lyy
	 * @modifyDate：2016年4月1日 下午5:03:15
	 * @param：    id 病区id
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int countDept(String id);

}
