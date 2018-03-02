package cn.honry.statistics.bi.wdWin.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.BiBaseDictionary;
import cn.honry.base.bean.model.BiBaseEmployee;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.wdWin.dao.WdWinDao;
@Repository("wdWinDao")
@SuppressWarnings({ "all" })
public class WdWinDaoImpl  extends HibernateEntityDao<BIBaseDistrict> implements WdWinDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<BIBaseDistrict> findTree() {
		String hql="FROM BIBaseDistrict d WHERE d.validFlag = 1 ";
		hql = hql+" ORDER BY d.path";
		List<BIBaseDistrict> baseDistrictList=super.findByObjectProperty(hql, null);
		if(baseDistrictList!=null && baseDistrictList.size()>0){
			return baseDistrictList;
		}
		return new ArrayList<BIBaseDistrict>();
	}

	@Override
	public List<BIBaseDistrict> findTreeLevelFirst() {
		String hql="FROM BIBaseDistrict d WHERE d.validFlag = 1 and d.level=1 ";//第一层级的省 直辖市..
		hql = hql+" ORDER BY d.path";
		List<BIBaseDistrict> baseDistrictList=super.findByObjectProperty(hql, null);
		if(baseDistrictList!=null && baseDistrictList.size()>0){
			return baseDistrictList;
		}
		return new ArrayList<BIBaseDistrict>();
	}

	@Override
	public List<BIBaseDistrict> findTreeByParentId(String id) {
		String hql="FROM BIBaseDistrict d WHERE d.validFlag = 1 and d.parentId='"+id+"' ";//根据第一层级的id获得子节点信息
		hql = hql+" ORDER BY d.path";
		List<BIBaseDistrict> baseDistrictList=super.findByObjectProperty(hql, null);
		if(baseDistrictList!=null && baseDistrictList.size()>0){
			return baseDistrictList;
		}
		return new ArrayList<BIBaseDistrict>();
	}

	@Override
	public boolean isOpen(String cityCode) {
		String hql="FROM BIBaseDistrict d WHERE d.validFlag = 1 and d.parentId='"+cityCode+"' ";//根据第一层级的id获得子节点信息
		hql = hql+" ORDER BY d.path";
		List<BIBaseDistrict> baseDistrictList=super.findByObjectProperty(hql, null);
		if(baseDistrictList!=null && baseDistrictList.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public List<BiBaseOrganization> findTreeOrg(String deptTypes) {
		/*StringBuffer sb = new StringBuffer();
		sb.append(" select t.org_parent_name as orgParentName ,t.org_kind_code as orgKindCode from bi_base_organization_tmp t WHERE T.ORG_KIND_CODE!='ALL' group by t.org_parent_name,t.org_kind_code ORDER BY T.ORG_KIND_CODE");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("orgParentName").addScalar("orgKindCode");
		List<BiBaseOrganization> departmentList = queryObject.setResultTransformer(Transformers.aliasToBean(BiBaseOrganization.class)).list();
		if(departmentList!=null && departmentList.size()>0){
			return departmentList;
		}
		return new ArrayList<BiBaseOrganization>();*/
		
		String hql="";
		if(StringUtils.isNotBlank(deptTypes)){
			deptTypes=deptTypes.replaceAll(",", "','");
			hql="FROM BiBaseOrganization d WHERE d.orgKindCode!='ALL' and d.orgCode=d.orgParentCode  and  d.orgKindCode in ('"+ deptTypes +"') ";
		}else{
			hql="FROM BiBaseOrganization d WHERE d.orgKindCode!='ALL' and d.orgCode=d.orgParentCode ";
		}
		List<BiBaseOrganization> departmentList=this.find(hql, null);
		if(departmentList!=null && departmentList.size()>0){
			return departmentList;
		}
		return new ArrayList<BiBaseOrganization>();
	}

	@Override
	public List<BiBaseOrganization> findTreeOrgByParentId(String id) {
		//String hql="FROM BiBaseOrganization d WHERE d.orgKindCode='"+id+"' ";
		String hql="FROM BiBaseOrganization d WHERE d.orgParentCode='"+id+"' and d.orgCode!='"+id+"'";
		List<BiBaseOrganization> departmentList=this.find(hql, null);
		if(departmentList!=null && departmentList.size()>0){
			return departmentList;
		}
		return new ArrayList<BiBaseOrganization>();
	}

	@Override
	public List<BiBaseEmployee> queryEmpByDeptCode(String id) {
		String sql="select e.EMPLOYEE_NO as employeeNo,e.EMPLOYEE_NAME as employeeName  from BI_BASE_EMPLOYEE e where e.dept_code='"+id+"'";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql);
		queryObject.addScalar("employeeNo").addScalar("employeeName");
		List<BiBaseEmployee> bdl = queryObject.setResultTransformer(Transformers.aliasToBean(BiBaseEmployee.class)).list();
		if(bdl!=null&&bdl.size()>0){
			return bdl;
		}
		return new ArrayList<BiBaseEmployee>();
	}

	@Override
	public List<RegisterGrade> queryDocLevelForBiPublic() {
		String sql="select g.GRADE_CODE as code,g.GRADE_NAME as name from T_REGISTER_GRADE g ";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql);
		queryObject.addScalar("code").addScalar("name");
		List<RegisterGrade> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(RegisterGrade.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<RegisterGrade>();
	}

	@Override
	public List<BiBaseEmployee> queryDocForBiPublic() {
		String sql="select e.EMPLOYEE_NO as employeeNo,e.EMPLOYEE_NAME as employeeName from BI_BASE_EMPLOYEE e";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql);
		queryObject.addScalar("employeeNo").addScalar("employeeName");
		List<BiBaseEmployee> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(BiBaseEmployee.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<BiBaseEmployee>();
	}

	/**  
	 * @Description：  门诊科室
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22 
	 * @remark：  
	 * @version 1.0
	 */
	@Override
	public List<BiBaseOrganization> treeBaseOrgOut() {
		String hql=" select t.org_code as orgCode, t.org_name as orgName from bi_base_organization t where t.org_kind_code='C' ";
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("orgCode").addScalar("orgName");
		List<BiBaseOrganization> list=query.setResultTransformer(Transformers.aliasToBean(BiBaseOrganization.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	
	/**  
	 * @Description：  挂号级别
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22 
	 * @remark：  
	 * @version 1.0
	 */
	@Override
	public List<RegisterGrade> queryregcode() {
		String hql=" from RegisterGrade t where t.stop_flg=0 and del_flg=0 ";
		List<RegisterGrade> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

}
