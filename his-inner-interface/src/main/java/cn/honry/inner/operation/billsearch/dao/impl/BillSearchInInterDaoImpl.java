package cn.honry.inner.operation.billsearch.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.operation.billsearch.dao.BillSearchInInterDAO;
import cn.honry.inner.operation.billsearch.vo.OperationBillingInfoVo;
/***
 * 摆药单分类DAO实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */          
@Repository("billSearchInInterDAO")
@SuppressWarnings({ "all" })
public class BillSearchInInterDaoImpl extends HibernateEntityDao<OperationBillingInfoVo> implements BillSearchInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**   
	     根据id 查询登录病区关联的科室
	* @author：tangfeishuai
	* @createDate：2016-6-12 上午10:52:19  
	* @modifier：tangfeishuai
	* @modifyDate：2016-6-12 上午10:52:19  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<DepartmentContact> getDepConByPid(String pid) {
		String hql="select t1.dept_id as deptId,t1.dept_name as deptName from t_department_contact t1 where t1.pardept_id in (select t.id from t_department_contact t where  t.dept_id='"+pid+"' and t.reference_type='03') and t1.del_flg=0";
		SQLQuery query=getSession().createSQLQuery(hql).addScalar("deptId").addScalar("deptName");
		List<DepartmentContact> list=query.setResultTransformer(Transformers.aliasToBean(DepartmentContact.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<DepartmentContact>();
	}
	/**   
	    查询医院维护的所有摆药单分类
	* @author：tangfeishuai
	* @createDate：2016-6-12 上午10:52:19  
	* @modifier：tangfeishuai
	* @modifyDate：2016-6-12 上午10:52:19  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<DrugBillclass> getDrugBillclass() {
		String hql = "from DrugBillclass d where d.stop_flg=0 and d.del_flg=0" ;
		List<DrugBillclass> list = super.find(hql, null);
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<DrugBillclass>();
	}
	
	
}
