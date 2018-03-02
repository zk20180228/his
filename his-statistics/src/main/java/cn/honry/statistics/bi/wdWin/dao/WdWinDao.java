package cn.honry.statistics.bi.wdWin.dao;

import java.util.List;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.BiBaseEmployee;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.dao.EntityDao;

public interface WdWinDao extends EntityDao<BIBaseDistrict>{

	List<BIBaseDistrict> findTree();

	List<BIBaseDistrict> findTreeLevelFirst();

	List<BIBaseDistrict> findTreeByParentId(String id);

	boolean isOpen(String cityCode);

	List<BiBaseOrganization> findTreeOrg(String deptTypes);

	List<BiBaseOrganization> findTreeOrgByParentId(String id);

	/**
	 * 根据科室code查询相应的员工
	 * @author tcj
	 * @param id
	 * @return
	 */
	List<BiBaseEmployee> queryEmpByDeptCode(String id);

	/**
	 * 查询挂号级别name、code
	 * @author tcj
	 * @return
	 */
	List<RegisterGrade> queryDocLevelForBiPublic();

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
