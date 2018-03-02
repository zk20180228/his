package cn.honry.inpatient.bill.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.bill.dao.OutpatientDrugcontrolDrugbillclassDAO;
import cn.honry.inpatient.bill.vo.OutpatientDrugcontrolDrugbillclass;
import cn.honry.utils.HisParameters;

@Repository("outpatientDrugcontrolDrugbillclassDAO")
@SuppressWarnings({ "all" })
public class OutpatientDrugcontrolDrugbillclassDAOImpl extends HibernateEntityDao<OutpatientDrugcontrolDrugbillclass> implements OutpatientDrugcontrolDrugbillclassDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	/**
	 * @Description:查询列表
	 * @Author：  dh
	 * @CreateDate： 2015-12-28
	 * @param @param page
	 * @param @param rows
	 * @param @param billclassSerc
	 * @param @return   
	 * @return List<OutpatientDrugcontrolDrugbillclass>  
	 * @version 1.0
	**/
	@Override
	public List<OutpatientDrugcontrolDrugbillclass> getPage(String page,String rows,
			OutpatientDrugcontrolDrugbillclass outpatientDrugcontrolDrugbillclass) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct b.billclass_code as billclassCode,"
				+ "b.billclass_name as billclassName,b.billclass_attr as billclassAttr,"
				+ "b.print_type as printType,b.valid_flag as validFlag,b.dept_code as deptCode,"
				+ "b.mark as mark from "+HisParameters.HISPARSCHEMAHISUSER+"t_outpatient_drugcontrol t"
				+ " inner join "+HisParameters.HISPARSCHEMAHISUSER+"t_drug_billclass b on t.billclass_id = b.billclass_id "
				+ "where t.dept_code = '"+outpatientDrugcontrolDrugbillclass.getDeptCode()+"' and b.del_flg = 0 "
				+ " and t.control_name = '"+outpatientDrugcontrolDrugbillclass.getControlName()+"' ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("billclassCode")
		.addScalar("billclassName").addScalar("billclassAttr").addScalar("printType").addScalar("validFlag",Hibernate.INTEGER)
		.addScalar("deptCode").addScalar("mark");
		queryObject.setResultTransformer(Transformers.aliasToBean(DrugBillclass.class));
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		queryObject.setFirstResult((start - 1) * count).setMaxResults(count);
		return queryObject.list();
	}
	/**
	 * @Description:查询列表总条数
	 * @Author：  dh
	 * @CreateDate： 2015-12-28
	 * @param @param page
	 * @param @param rows
	 * @param @param billclassSerc
	 * @Modifier：liujl
	 * @ModifyDate：2016-6-6 上午09:56:35  
	 * @ModifyRmk：  修改获取记录总条数方法
	 * @param @return   
	 * @return List<OutpatientDrugcontrolDrugbillclass>  
	 * @version 1.0
	**/
	@Override
	public int getTotal(OutpatientDrugcontrolDrugbillclass outpatientDrugcontrolDrugbillclass) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.billclass_code as billclassCode,"
				+ "b.billclass_name as billclassName,b.billclass_attr as billclassAttr,"
				+ "b.print_type as printType,b.valid_flag as validFlag,b.dept_code as deptCode,"
				+ "b.mark as mark from "+HisParameters.HISPARSCHEMAHISUSER+"t_outpatient_drugcontrol t"
				+ " inner join "+HisParameters.HISPARSCHEMAHISUSER+"t_drug_billclass b on t.billclass_id = b.billclass_id "
				+ "where t.dept_code = '"+outpatientDrugcontrolDrugbillclass.getDeptCode()+"' and b.del_flg = 0 "
				+ " and t.control_name = '"+outpatientDrugcontrolDrugbillclass.getControlName()+"' ");
		return super.getSqlTotal(sql.toString());
	}
			
}
