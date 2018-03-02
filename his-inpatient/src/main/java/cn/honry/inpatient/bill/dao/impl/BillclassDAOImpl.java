package cn.honry.inpatient.bill.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.DataRightUtils;
import cn.honry.inpatient.bill.dao.BillclassDAO;
@Repository("billclassDAO")
@SuppressWarnings("all")
public class BillclassDAOImpl extends HibernateEntityDao<DrugBillclass> implements BillclassDAO{
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	OutpatientDrugcontrol outpatientDrugcontrol=new OutpatientDrugcontrol();
	
	
	public OutpatientDrugcontrol getOutpatientDrugcontrol() {
		return outpatientDrugcontrol;
	}

	public void setOutpatientDrugcontrol(OutpatientDrugcontrol outpatientDrugcontrol) {
		this.outpatientDrugcontrol = outpatientDrugcontrol;
	}

	@Override
	public List<DrugBillclass> getPage(String page, String rows,
			DrugBillclass billclassSerc) {
		String hql = "from DrugBillclass db where db.del_flg = 0 and db.stop_flg = 0";
		if(billclassSerc.getControlId()!=null){
			hql += "and db.controlId = '" + billclassSerc.getControlId() + "'";
		}
		hql += "order by db.createTime desc";
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(DrugBillclass billclassSerc) {
		String hql = "from DrugBillclass db where db.del_flg = 0 and db.stop_flg = 0";
		if(billclassSerc.getControlId()!=null){
			hql += "and db.controlId = '" + billclassSerc.getControlId() + "'";
		}
		hql += "order by db.createTime desc";
		return super.getTotal(hql);
	}
	/**   
	*  
	* @description：查询修改的那一条
	* @author：ldl
	* @createDate：2015-10-19
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<DrugBillclass> findBillEdit(String id) {
		id=id.replaceAll(",", "','");
		String hql = "from DrugBillclass where id in ('"+id+"') and del_flg = 0";
		List<DrugBillclass> billList = super.find(hql, null);
		if(billList==null||billList.size()<=0){
			return new ArrayList<DrugBillclass>();
		}
		return billList;
	}
	/**   
	*  
	* @description：查询修改的那一条(子表)
	* @author：ldl
	* @createDate：2015-10-20
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<DrugBilllist> findBillInfoEdit(String id) {
		id=id.replaceAll(",", "','");
		String hql = "from DrugBilllist where id in ('"+id+"') and del_flg = 0";
		List<DrugBilllist> billInfoList = super.find(hql, null);
		if(billInfoList==null||billInfoList.size()<=0){
			return new ArrayList<DrugBilllist>();
		}
		return billInfoList;
	}
	//查询摆药单分类
	@Override
	public List<DrugBillclass> getPageList(String page, String rows, DrugBillclass drugBillclass) {
		String sql="select b.BILLCLASS_ID as id,b.billclass_code as billclassCode,"
				+ "b.billclass_name as billclassName,b.billclass_attr as billclassAttr,"
				+ "b.print_type as printType,b.valid_flag as validFlag,b.dept_code as deptCode,"
				+ "b.mark as mark from t_outpatient_drugcontrol t"
				+ " inner join t_drug_billclass b on t.billclass_id = b.billclass_id "
				+ "where t.dept_code = '"+drugBillclass.getDeptCode()+"' and b.del_flg = 0 "
				+ " and t.control_name = '"+outpatientDrugcontrol.getControlName()+"' ";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("id").addScalar("billclassCode")
		.addScalar("billclassName").addScalar("billclassAttr").addScalar("printType").addScalar("validFlag",Hibernate.INTEGER)
		.addScalar("deptCode").addScalar("mark");
		queryObject.setResultTransformer(Transformers.aliasToBean(DrugBillclass.class));
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		queryObject.setFirstResult((start - 1) * count).setMaxResults(count);
		return queryObject.list();
	}
	
	@Override
	public int getTotalList(DrugBillclass billclassSerc) {
		String hql = "from DrugBillclass db where db.del_flg = 0 and db.stop_flg = 0 ";
		return super.getTotal(hql);
	}
	//摆药单分类代码查询
	@Override
	public List<DrugBillclass> getBillclassCode() {
		String sql = "select t.billclass_code as billclassCode from t_drug_billclass t where t.del_flg = 0";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("billclassCode");
	    List<DrugBillclass> list = queryObject.setResultTransformer(Transformers.aliasToBean(DrugBillclass.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<DrugBillclass> queryDrugBillclassCode(String Code) {
		String hql = "from DrugBillclass s where s.billclassCode=? and s.del_flg=0 and s.stop_flg = 0";
		List<DrugBillclass> drugBillclass = super.find(hql, Code);
		if(drugBillclass!=null && drugBillclass.size()>0){
			return drugBillclass;
		}
		return new ArrayList<DrugBillclass>();
	}
}
