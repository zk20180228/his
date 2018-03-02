package cn.honry.oa.hospitalFileManager.dao.Imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.HospitalFileManager;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.hospitalFileManager.dao.FileManageDao;
/**  
 * @Author：hedong
 * @CreateDate：2017-03-23
 * @version 1.0
 * @remark:平台异常信息记录
 */
@Repository("fileManageDao")
@SuppressWarnings({"all"})
public class FileManageDaoImpl extends HibernateEntityDao<HospitalFileManager> implements FileManageDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/* 获取档案表中数据 */
	@Override
	public List<HospitalFileManager> getList(
			HospitalFileManager hospitalFileManager, String page, String rows) {
		StringBuffer hql = new StringBuffer("from HospitalFileManager t where t.stop_flg = 0 and t.del_flg = 0");
		hql = getHql(hospitalFileManager, hql);
		List<HospitalFileManager> list = super.getPage(hql.toString(), page, rows);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<HospitalFileManager>();
	}
	
	/* 保存档案表中数据 */
	@Override
	public void insertFile(HospitalFileManager hospitalFileManager) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(hospitalFileManager);
			tx.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	/* 删除档案表中数据 */
	@Override
	public void deleteFile(String id) {
		HospitalFileManager hospitalFileManager = new HospitalFileManager();
		hospitalFileManager.setId(id);
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(hospitalFileManager);
			tx.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
	}

	/* 回显修改中的数据 */
	@Override
	public HospitalFileManager getListFile(String id) {
		String hql="From HospitalFileManager where id = "+id;
		HospitalFileManager hospitalFileManager = super.get(id);
		return hospitalFileManager;
	}
	
	/* 修改表中的数据 */
	@Override
	public void updateFile(HospitalFileManager hospitalFileManager) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(hospitalFileManager);
			tx.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	private StringBuffer getHql(HospitalFileManager hospitalFileManager, StringBuffer hql) {
		if (hospitalFileManager.getBorrow() != null) {
			hql.append(" and t.borrow = ");
			hql.append("'"+hospitalFileManager.getBorrow()+"'");
		}
		if(hospitalFileManager.getFileClassify()!=null){
			hql.append(" and t.fileClassify = ");
			hql.append("'"+hospitalFileManager.getFileClassify()+"'");
		}
		if(hospitalFileManager.getFileRank()!=null){
			hql.append(" and t.fileRank = ");
			hql.append("'"+hospitalFileManager.getFileRank()+"'");
		}
		if(hospitalFileManager.getFileType()!=null){
			hql.append(" and t.fileType = ");
			hql.append("'"+hospitalFileManager.getFileType()+"'");
		}
		if(hospitalFileManager.getFileStatus()!=null){
			hql.append(" and t.fileStatus = ");
			hql.append("'"+hospitalFileManager.getFileStatus()+"'");
		}
		if(hospitalFileManager.getDeptName()!=null){
			hql.append(" and t.deptName = ");
			hql.append("'"+hospitalFileManager.getDeptName()+"'");
		}
		if (StringUtils.isNotBlank(hospitalFileManager.getName())) {
			hql.append(" and(t.name like '%");
			hql.append(hospitalFileManager.getName());
			hql.append("%'");
		}
		if (StringUtils.isNotBlank(hospitalFileManager.getFileNumber())) {
			hql.append(" or t.fileNumber like '%");
			hql.append(hospitalFileManager.getFileNumber());
			hql.append("%')");
		}
		return hql;
	}


}
