package cn.honry.patinent.idcardChange.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.PatientIdcardChange;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.patinent.idcardChange.dao.IdcardChangeDAO;

@Repository("idcardChangeDAO")
@SuppressWarnings({ "all" })
public class IdcardChangeDAOImpl extends HibernateEntityDao<PatientIdcardChange> implements IdcardChangeDAO{


	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<PatientIdcardChange> queryChange(String page,String rows,String patientId,String startTime,String endTime,String type) {
		String hql="from PatientIdcardChange t where t.patient='"+patientId+"' and t.stop_flg=0 and t.del_flg=0";
		if(StringUtils.isNotBlank(type)){
			hql+=" and t.changeStatus="+type;
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+=" and t.createTime >= to_Date('"+startTime+"','yyyy-mm-dd')";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+=" and t.createTime < to_Date('"+endTime+"','yyyy-mm-dd')";
		}
		hql+=" order by t.createTime desc";
		
		List<PatientIdcardChange> list =this.getPage(hql, page, rows);
		if(list.size()>0){
			return list;
		}else{
			return new ArrayList<PatientIdcardChange>();
		}
	}

	@Override
	public int getTotal(String patientId,String startTime, String endTime, String type) {
		String hql="from PatientIdcardChange t where t.patient='"+patientId+"' and t.stop_flg=0 and t.del_flg=0";
		if(StringUtils.isNotBlank(type)){
			hql+=" and t.changeStatus="+type;
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+=" and t.createTime >= to_Date('"+startTime+"','yyyy-mm-dd')";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+=" and t.createTime < to_Date('"+endTime+"','yyyy-mm-dd')";
		}
		hql+=" order by t.createTime desc";
		
		return this.getTotal(hql);
		
	}
	
	/**
	 * 查询用户Map
	 * @author zpty
	 * @CreateDate：2017-05-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<User> queryUserRecord() {
		String sql=" select u.USER_ACCOUNT as account ,u.USER_NAME as name  from T_SYS_USER u where u.stop_flg=0 and u.del_flg=0 ";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("account").addScalar("name");
		List<User> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(User.class)).list();
		if(bdl!=null&&bdl.size()>0){
			return bdl;
		}
		return new ArrayList<User>();
	}
}

