package cn.honry.oa.documentManage.dao.Imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DocManage;
import cn.honry.base.bean.model.HospitalFileManager;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.documentManage.dao.DocumentManageDao;
@Repository("documentManageDao")
public class DocumentManageDaoImp extends HibernateEntityDao<DocManage> implements DocumentManageDao {

	
	//为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/* 获取文档管理表中所有的数据*/
	@SuppressWarnings("deprecation")
	@Override
	public List<DocManage> queryDcoManage(DocManage docManage1, String rows, String page) {
		
		StringBuffer hql = new StringBuffer("from DocManage t where 0=0 ");
		hql = getHql(docManage1, hql);
		List<DocManage> list = super.getPage(hql.toString(), page, rows);
		if(list != null && list.size() != 0){
			return  list;
		}
		return new ArrayList<DocManage>();
	}

	/* 删除文档中的数据*/
	@Override
	public void deleteDcoManage(String id) {
		DocManage docManage = new DocManage();
		docManage.setId(id);
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(docManage);
			tx.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}

	/* 修改文档中的数据*/
	@Override
	public void updateDcoManage(DocManage docManage) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(docManage);
			tx.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}

	/* 添加文档中的数据*/
	@Override
	public void insertDocument(DocManage docManage) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(docManage);
			tx.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
	}

	/* 获取文档管理表中回显的数据*/
	@Override
	public DocManage getListDocument(String id) {
		String hql="From DocManage where id = "+id;
//		DocManage docManage =(DocManage) this.getHibernateTemplate().find(hql);
		DocManage docManage2 = super.get(id);
		return docManage2;
	}

	/* 根据文档名称查询数据*/
	@Override
	public ArrayList<DocManage> getListByName(String documentName) {
		// TODO Auto-generated method stub
		String hql="From DocManage where docName = '"+documentName+"'";
		ArrayList<DocManage> list =(ArrayList<DocManage>) this.getHibernateTemplate().find(hql);
		return list;	
	}

	/* 根据科室查询数据*/
	@Override
	public ArrayList<DocManage> getListByDeptName(String deptCode) {
		// TODO Auto-generated method stub
		String hql="From DocManage where uploadDept = '"+deptCode+"'";
		ArrayList<DocManage> list =(ArrayList<DocManage>) this.getHibernateTemplate().find(hql);
		return list;
	}

	/* 根据科室查询数据*/
	@Override
	public ArrayList<DocManage> getListByUserName(String queryName) {
		// TODO Auto-generated method stub
		String q = "%"+queryName+"%";
		String hql="From DocManage where createUser like '"+q+"'";
		ArrayList<DocManage> list =(ArrayList<DocManage>) this.getHibernateTemplate().find(hql);
		return list;
	}
	
	
	
	private StringBuffer getHql(DocManage docManage, StringBuffer hql) {
		if (docManage.getDocName()!= null) {
			hql.append("and( docName like ");
			hql.append("'"+"%"+docManage.getDocName()+"%"+"'");
		}
		if(docManage.getCreateUser()!=null){
			hql.append(" or createUser like ");
			hql.append("'"+"%"+docManage.getCreateUser()+"%"+"')");
		}
		if(docManage.getDeptType()!=null){
			hql.append(" and deptType = ");
			hql.append("'"+docManage.getDeptType()+"'");
		}	
		if(docManage.getUploadDept()!=null){
			hql.append(" and uploadDept = ");
			hql.append("'"+docManage.getUploadDept()+"'");
		}
		
		return hql;
	}

}
