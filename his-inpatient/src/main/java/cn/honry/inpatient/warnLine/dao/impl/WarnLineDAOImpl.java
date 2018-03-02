 package cn.honry.inpatient.warnLine.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
import cn.honry.inpatient.warnLine.dao.WarnLineDAO;
import cn.honry.inpatient.warnLine.vo.WarnLineVo;
/**
 * 全院警戒线设置DAO实现类
 * @author  lyy
 * @createDate： 2016年4月1日 下午2:50:51 
 * @modifier lyy
 * @modifyDate：2016年4月1日 下午2:50:51  
 * @modifyRmk：  
 * @version 1.0
 */
@Repository("warnLineDAO")
@SuppressWarnings({"all"})
public class WarnLineDAOImpl extends HibernateEntityDao<InpatientInfo> implements WarnLineDAO{
	
	@Autowired
	private InpatientInfoDAO inpatientInfoDAO;
	@Autowired
	private DeptInInterDAO departmentDAO; 
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<InpatientInfo> getPage(String page, String rows, InpatientInfo entity) {
		return null;
	}
	
	@Override
	public int getTotal(InpatientInfo entity) {
		return 0;
	}
	/**
	 * @Description： 连表查询数据
	 * @Author：lyy
	 * @CreateDate：2015-12-3 下午02:36:53  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-3 下午02:36:53
	 *  @param vo 患者警戒线虚拟实体  page 当前页   rows 当前页条数 
	 * @return List<WarnLineVo> 返回一个集合  
	 * @ModifyRmk：  代码规范
	 * @version 1.0
	 */
	public List<WarnLineVo> listWarnLine(WarnLineVo vo, String page, String rows) {
		String nationCode = vo.getNationCode();
		String idcardNo = vo.getIdcardNo();
		StringBuilder sql = this.builderSql(vo);
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.addScalar("id").addScalar("inpatientNo").addScalar("medicalType")
			.addScalar("medicalrecordId").addScalar("idcardNo").addScalar("patientName")
			.addScalar("certificatesType").addScalar("certificatesNo")
			.addScalar("reportSex").addScalar("reportSexName").addScalar("deptCode").addScalar("deptName").addScalar("bedId")
			.addScalar("inDate",Hibernate.TIMESTAMP).addScalar("moneyAlert",Hibernate.DOUBLE)
			.addScalar("alterBegin",Hibernate.TIMESTAMP).addScalar("alterEnd",Hibernate.TIMESTAMP)
			.addScalar("freeCost",Hibernate.DOUBLE).addScalar("nurseCellCode").addScalar("nurseCellName");
		if(vo!=null){
			if(StringUtils.isNotBlank(vo.getNationCode())&&!"1".equals(vo.getNationCode())){
				query.setParameter("nationCode", nationCode);
			}
			if(StringUtils.isNotBlank(vo.getIdcardNo())){   //姓名,病历号,就诊卡号,床号
				query.setParameter("idcardNo", "%"+idcardNo+"%");
			}
		}
		query.setResultTransformer(Transformers.aliasToBean(WarnLineVo.class));
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		List<WarnLineVo> list= query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<WarnLineVo>();
	}
	
	 /**
	  * @Description：  总条数
	  * @Author：lyy
	  * @CreateDate：2015-12-3 下午03:20:00  
	  * @Modifier：lyy,liujl
	  * @ModifyDate：2015-12-3 下午03:20:00  2016-6-6 上午09:56:35  
	  * @param vo 患者警戒线虚拟实体 
	  * @return int 返回一个int类型值
	  * @ModifyRmk：  修改获取记录总条数方法
	  * @version 1.0
	  */
	@Override
	public int getTotalCount(WarnLineVo vo) {
		StringBuilder sql =this.builderSql(vo);
		return super.getSqlTotal(sql.toString());
	}
	/**
	 * 患者警戒线（全院）查询方法
	 * @author  lyy
	 * @createDate： 2016年4月28日 下午3:49:45 
	 * @modifier lyy
	 * @modifyDate：2016年4月28日 下午3:49:45
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	private StringBuilder builderSql(WarnLineVo vo){
		StringBuilder sql = new StringBuilder();
		String nationCode = vo.getNationCode();
		String idcardNo = vo.getIdcardNo();
		sql.append("select i.inpatient_id as id,"
				+ "i.inpatient_no as inpatientNo,"
				+ "i.Medical_Type as medicalType,"
				+"i.medicalrecord_id as medicalrecordId,"
				+ "i.IDCARD_NO as idcardNo,"
				+ "i.PATIENT_NAME as patientName," 
				+"i.certificates_type as certificatesType,"
				+ "i.certificates_no as certificatesNo,"
				+ "i.REPORT_SEX as reportSex," 
				+ "i.REPORT_SEX_NAME as reportSexName," 
				+"i.DEPT_CODE as deptCode,"
				+"i.DEPT_NAME as deptName,"
				+ "i.bed_name as bedId,"
				+ "i.IN_DATE as inDate,"
				+ "i.Money_Alert as moneyAlert," 
				+"i.ALTER_BEGIN as alterBegin,"
				+ "i.ALTER_END as alterEnd,"
				+ "nvl(i.free_cost,0.00) as freeCost,"
				+ "i.nurse_cell_code as nurseCellCode, "
				+ "i.NURSE_CELL_NAME as nurseCellName "
				+ "from t_inpatient_info_now i " 
				+" WHERE 1=1 and i.IN_STATE ='I' ");
		if(vo!=null){
			if(StringUtils.isNotBlank(vo.getNationCode())&&!"1".equals(vo.getNationCode())){
				sql.append(" AND i.nurse_cell_code = :nationCode ");
			}
			if(StringUtils.isNotBlank(vo.getIdcardNo())){   //姓名,病历号,就诊卡号,床号
				Pattern p= Pattern.compile("[\\u4e00-\\u9fa5]+");
				Matcher m=p.matcher(vo.getIdcardNo()); 
				sql.append(" AND (i.idcard_no LIKE :idcardNo or i.PATIENT_NAME like :idcardNo" +
						" or i.Medicalrecord_Id LIKE :idcardNo "+
						" or i.inpatient_no LIKE :idcardNo "+
						" or i.bedinfo_id LIKE :idcardNo or i.CERTIFICATES_NO LIKE :idcardNo) ");
			}
		}
		return sql;
	}
	public int getTotalCountBySql(WarnLineVo vo){

		StringBuilder sql = new StringBuilder();
		sql.append("select i.inpatient_id "
				+ "from t_inpatient_info_now i " 
				+"WHERE 1=1 and i.IN_STATE ='I' ");
		if(vo!=null){
			if(StringUtils.isNotBlank(vo.getNationCode())&&!"1".equals(vo.getNationCode())){
				sql.append(" AND i.nurse_cell_code = '"+vo.getNationCode()+"'");
			}
			if(StringUtils.isNotBlank(vo.getIdcardNo())){   //姓名,病历号,就诊卡号,床号
				Pattern p= Pattern.compile("[\\u4e00-\\u9fa5]+");
				Matcher m=p.matcher(vo.getIdcardNo()); 
				if(m.matches()==true){
					sql.append(" and i.PATIENT_NAME like '%"+vo.getIdcardNo()+"%'");
				}else{
					sql.append(" AND (i.idcard_no LIKE '%"+vo.getIdcardNo()+"%'" +
							"or i.Medicalrecord_Id LIKE '%"+vo.getIdcardNo()+"%'"+
							"or i.inpatient_no LIKE '%"+vo.getIdcardNo()+"%'"+
							"or i.bedinfo_id LIKE '%"+vo.getIdcardNo()+"%')");
				}
			}
		}
		 return super.getSqlTotal(sql.toString());
	}
	 /**
	  * 根据病区id统计该病区下有多少个患者
	  * @author  lyy
	  * @createDate： 2016年4月1日 上午9:11:18 
	  * @modifier lyy,liujl
	  * @modifyDate：2016年4月1日 上午9:11:18 2016-6-6 上午09:56:35  
	  * @param id    病区id
	  * @return  int 返回一个int类型值
	  * @ModifyRmk：  代码规范,修改获取记录总条数方法
	  * @version 1.0
	  */
	@Override
	public int countDept(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select i.PATIENT_NAME as patientName "
				+ "from t_inpatient_info_now i "
				+ "join t_business_hospitalbed h on i.bed_id = h.bed_id "
				+ "join t_patient_idcard idc on idc.idcard_no = i.idcard_no "
				+ "WHERE i.del_flg = 0 and  h.del_flg = 0 and i.nurse_cell_code='"+id+"'and i.in_state='I'");
		return super.getSqlTotal(sql.toString());
	}
	/**
     * 病区科室的科室树
     * @author  lyy
     * @createDate： 2015年12月16日 下午7:56:45 
     * @modifier lyy
     * @modifyDate：2015年12月16日 下午7:56:45 
	 * @return  List<SysDepartment> 返回一个部门集合
     * @modifyRmk：  
     * @version 1.0
     */
	@Override
	public List<SysDepartment> findTreeType() {
		String hql="From SysDepartment where deptType='N' and del_flg=0 and stop_flg=0";
		List<SysDepartment> listDeptContact=super.find(hql, null);
		if(listDeptContact!=null&&listDeptContact.size()>0){
			return listDeptContact;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<SysDepartment> getDeptName(String queryName) {
		String hql="From SysDepartment d where d.del_flg=0 and d.stop_flg=0 and d.deptType='N'";
		if(queryName!=null){    //根据部门名称、部门code、五笔码、拼音码、自定义码
			hql+=" and (d.deptName like '%"+queryName+"%' or d.deptCode like '%"+queryName+"%' "
				+ "or d.deptWb like '%"+queryName.toUpperCase()+"%'  or d.deptPinyin like '%"+queryName.toUpperCase()+"%' "
				+ "or d.deptInputcode like '%"+queryName.toUpperCase()+"%')";
			hql+="order by abs(length(d.deptName) - length('"+queryName+"')),abs(length(d.deptCode) - length('"+queryName+"')),abs(length(d.deptWb) - length('"+queryName+"')),"
					+ "abs(length(d.deptPinyin) - length('"+queryName+"')),abs(length(d.deptInputcode) - length('"+queryName+"'))";
		}
		List<SysDepartment> listDeptContact=super.find(hql, null);
		if(listDeptContact!=null&&listDeptContact.size()>0){
			return listDeptContact;
		}
		return new ArrayList<SysDepartment>();
	}
}
