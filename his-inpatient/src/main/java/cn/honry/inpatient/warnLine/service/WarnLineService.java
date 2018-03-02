package cn.honry.inpatient.warnLine.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.warnLine.vo.WarnLineVo;
import cn.honry.utils.TreeJson;
/**
 * 全院警戒线设置Service
 * @author  lyy
 * @createDate： 2016年4月1日 下午2:49:47 
 * @modifier lyy
 * @modifyDate：2016年4月1日 下午2:49:47  
 * @modifyRmk：  
 * @version 1.0
 */
public interface WarnLineService extends BaseService<InpatientInfo> {

	/**
	 * @Description: 获取分页列表
	 * @Author：  kjh
	 * @CreateDate： 2015-6-29
	 * @Modifier：lyy
	 * @ModifyDate：2016-3-30下午08:32:12
	 * @param  page 当前页   rows 当前页条数    inpatientInfoSerch 住院主表实体
	 * @return List<InpatientInfo> 返回一个集合
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfo> getPage(String page, String rows,InpatientInfo inpatientInfoSerch) throws Exception;

	/**
	 * @Description: 获取总条数
	 * @Author：kjh
	 * @CreateDate： 2015-6-29
	 * @Modifier：lyy
	 * @ModifyDate：2016-3-30下午08:32:12
	 * @param inpatientInfoSerch  住院主表实体
	 * @return int   返回一个int
	 * @version 1.0
	 * @throws Exception 
	**/
	int getTotal(InpatientInfo inpatientInfoSerch) throws Exception;
	/**
	 * @Description： 连表查询数据
	 * @Author：lyy
	 * @CreateDate：2015-12-3 下午02:36:53  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-3 下午02:36:53  
	 * @param vo 患者警戒线虚拟实体  page 当前页   rows 当前页条数 
	 * @return List<InpatientInfo> 返回一个集合
	 * @ModifyRmk：  代码规范
	 * @version 1.0
	 * @throws Exception 
	 */
	List<WarnLineVo> getWarnLine(WarnLineVo vo, String page, String rows) throws Exception;
	/**
	 * @Description：患者警戒线设置总条数
	 * @Author：lyy
	 * @CreateDate：2015-12-3 下午03:20:00  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-3 下午03:20:00
	 * @param   vo 患者警戒线虚拟实体  
	 * @return int 返回一个int值
	 * @ModifyRmk：  代码规范
	 * @version 1.0
	 * @throws Exception 
	 */
	 int getTotalCount(WarnLineVo vo) throws Exception;
	 /**
	  * 住院部全院患者警戒线病区的树
	  * @author  lyy
	  * @createDate： 2015年12月19日 下午2:23:01 
	  * @modifier lyy
	  * @modifyDate：2015年12月19日 下午2:23:01
	  * @param    id   病区主键id 
	  * @return  返回一个List<TreeJson> list集合json
	  * @modifyRmk：  代码规范
	  * @version 1.0
	 * @throws Exception 
	  */
	 List<TreeJson> queryTreeNurse(String id) throws Exception;
	 /**
	  * 
	  * 
	  * <p>查询部门科室信息 </p>
	  * @Author: XCL
	  * @CreateDate: 2017年7月4日 上午9:45:06 
	  * @Modifier: XCL
	  * @ModifyDate: 2017年7月4日 上午9:45:06 
	  * @ModifyRmk:  
	  * @version: V1.0
	  * @param queryName 部门名称、部门code、五笔码、拼音码、自定义码
	  * @return:
	 * @throws Exception 
	  *
	  */
	List<SysDepartment> getDeptName(String queryName) throws Exception;
}
