package cn.honry.statistics.bi.wdWin.service;

import java.util.List;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.BiBaseDictionary;
import cn.honry.base.bean.model.BiBaseEmployee;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;
@SuppressWarnings({"all"})
public interface WdWinService extends BaseService<BIBaseDistrict>{
	/**  
	 *  
	 * @Description： 查询地域树 
	 * @Author：hedong
	 * @CreateDate：2016-08-03 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param provinceOnly 
	 *
	 */
	List<TreeJson> QueryTreeDistrict(String id, String provinceOnly);
    /**
     * 查询科室树
     * @param id
     * @return
     */
	List<TreeJson> QueryTreeOrg(String id,String deptTypes);
	/**
	 * 查询医生树
	 * @param id
	 * @param deptTypes
	 * @return
	 */
	List<TreeJson> queryDeptEmpTree(String id, String deptTypes);
	/**
	 * 查询挂号级别
	 * @author tcj
	 * @return
	 */
	List<RegisterGrade> queryDocLevelForBiPublic();
	
	/**  
	 *  
	 * @Description：获得组织机构第一级节点 如 门诊 急诊...
	 * @Author：hedong
	 * @CreateDate：2016-08-17 
	 * @version 1.0
	 */
	List<BiBaseOrganization> findTreeOrg(String deptTypes);
	/**  
	 *  
	 * @Description：根据父节点deptCode获得子部门
	 * @Author：hedong
	 * @CreateDate：2016-08-17
	 * @version 1.0
	 */
	List<BiBaseOrganization> findTreeOrgByParentId(String orgCode);
	/**
	 * @Description：查询医生list
	 * @Author：tcj
	 * @CreateDate：2016-08-17
	 * @return
	 */
	List<BiBaseEmployee> queryDocForBiPublic();
	/**  
	 * @Description：  门诊科室
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22 
	 * @remark：  
	 * @version 1.0
	 */
	List<BiBaseOrganization> treeBaseOrgOut();
	/**  
	 * @Description：  挂号级别
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22 
	 * @remark：  
	 * @version 1.0
	 */
	List<RegisterGrade> queryregcode();
}
