package cn.honry.inpatient.warnLine.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.warnLine.vo.WarnLineVo;
/**
 * 全院警戒线设置dao
 * @author  lyy
 * @createDate： 2016年4月1日 下午2:50:08 
 * @modifier lyy
 * @modifyDate：2016年4月1日 下午2:50:08  
 * @modifyRmk：  
 * @version 1.0
 */
public interface WarnLineDAO extends EntityDao<InpatientInfo>{

	/**
	 * @Description:获取分页列表
	 * @Author：  kjh
	 * @CreateDate： 2015-6-29
	 * @param @param page 当前页   rows 当前页条数    entity 住院主表实体
	 * @return List<InpatientInfo>  返回一个list集合
	 * @version 1.0
	**/
	List<InpatientInfo> getPage(String page, String rows, InpatientInfo entity);
	/**
	 * @Description: 获取总条数
	 * @Author：kjh
	 * @CreateDate： 2015-6-29
	 * @Modifier：lyy
	 * @ModifyDate：2016-3-30下午08:32:12
	 * @param entity  住院主表实体
	 * @return int   返回一个int
	 * @version 1.0
	**/
	int getTotal(InpatientInfo entity);
	/**
	 * @Description： 连表查询数据
	 * @Author：lyy
	 * @CreateDate：2015-12-3 下午02:36:53  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-3 下午02:36:53  
	 * @param vo 患者警戒线虚拟实体  page 当前页   rows 当前页条数 
	 * @return List<WarnLineVo> 返回一个集合
	 * @ModifyRmk：  代码规范
	 * @version 1.0
	 */
	 List<WarnLineVo> listWarnLine(WarnLineVo vo, String page, String rows);
	 /**
	  * @Description：  总条数
	  * @Author：lyy
	  * @CreateDate：2015-12-3 下午03:20:00  
	  * @Modifier：lyy
	  * @ModifyDate：2015-12-3 下午03:20:00 
	  * @param vo  患者警戒线虚拟实体  
	  * @ModifyRmk：  代码规范 
	  * @version 1.0
	  */
	 int getTotalCount(WarnLineVo vo);
	 int getTotalCountBySql(WarnLineVo vo);
	 /**
	  * 根据病区id统计该病区下有多少个患者
	  * @author  lyy
	  * @createDate： 2016年4月1日 上午9:11:18 
	  * @modifier lyy
	  * @modifyDate：2016年4月1日 上午9:11:18 
	  * @param id    病区id
	  * @return  int 返回一个int类型值
	  * @ModifyRmk：  代码规范
	  * @version 1.0
	  */
	 int countDept(String id);
	 /**
      * 病区科室的科室树
      * @author  lyy
      * @createDate： 2015年12月16日 下午7:56:45 
      * @modifier lyy
      * @modifyDate：2015年12月16日 下午7:56:45 
      * @return  List<SysDepartment>  返回一个科室集合
      * @modifyRmk：  代码规范
      * @version 1.0
      */
	 List<SysDepartment> findTreeType();
	 /**
	  * 
	  * 
	  * <p>查询部门科室信息 </p>
	  * @Author: XCL
	  * @CreateDate: 2017年7月4日 上午9:45:27 
	  * @Modifier: XCL
	  * @ModifyDate: 2017年7月4日 上午9:45:27 
	  * @ModifyRmk:  
	  * @version: V1.0
	  * @param queryName 部门名称、部门code、五笔码、拼音码、自定义码
	  * @return:
	  *
	  */
	List<SysDepartment> getDeptName(String queryName);
	
}
