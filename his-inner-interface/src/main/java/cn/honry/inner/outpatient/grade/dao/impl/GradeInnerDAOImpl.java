package cn.honry.inner.outpatient.grade.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.grade.dao.GradeInInterDAO;

@Repository("gradeInInterDAO")
@SuppressWarnings({ "all" })
public class GradeInnerDAOImpl extends HibernateEntityDao<RegisterGrade> implements GradeInInterDAO{
	
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<RegisterGrade> getPage(RegisterGrade entity, String page,
			String rows) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(RegisterGrade entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
		
	}

	public String joint(RegisterGrade entity){
		String hql="FROM RegisterGrade r WHERE  r.del_flg = 0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getCode())){
				String queryCode= entity.getCode().toUpperCase();
				hql = hql+" AND (upper(r.name) LIKE '%"+queryCode+"%'"+
					      " or upper(r.codePinyin) LIKE '%"+queryCode+"%'"+
					      " or upper(r.code) LIKE '%"+queryCode+"%'"+
					      " or upper(r.codeWb) LIKE '%"+queryCode+"%'"+
					      " or  upper(r.codeInputcode) LIKE '%"+queryCode+"%'" +
					      " )";
			}
			if(StringUtils.isNotBlank(entity.getName())){
				hql = hql+" and name = '"+entity.getName()+"'  ";
			}
		}
		return hql;
	}


	@Override
	public List<RegisterGrade> getCombobox(String time,String q) {
		String hql=null;
		 hql="FROM RegisterGrade r WHERE  r.del_flg = 0 ";
		 if(StringUtils.isNotBlank(q)){
			 hql+=" and ( r.name like '%"+q+"%'"+
					 " or r.codePinyin LIKE '%"+q.toUpperCase()+"%'"+
				      " or r.code LIKE '%"+q.toUpperCase()+"%'"+
				      " or r.codeWb LIKE '%"+q.toUpperCase()+"%'"+
				      " or  r.codeInputcode LIKE '%"+q.toUpperCase()+"%'" +
				      " )";
		 }
		 List<RegisterGrade> gradeList=new ArrayList<RegisterGrade>();
		  gradeList=super.findByObjectProperty(hql, null);
		if(gradeList.size()==0){
			 hql="FROM RegisterGrade r WHERE  r.del_flg = 0 ";
			  gradeList=super.findByObjectProperty(hql, null);
		}
		return gradeList;
	}

	
}
	


