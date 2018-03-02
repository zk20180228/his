package cn.honry.inner.oa.userSign.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaUserSign;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.oa.userSign.dao.UserSigninnerDao;

@Repository("userSigninnerDao")
@SuppressWarnings({"all"})
public class UserSigninnerDaoImpl extends HibernateEntityDao<OaUserSign> implements UserSigninnerDao {

	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name="sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<OaUserSign> queryOaUserSigns(String account,String signType) {
		String hql = "from OaUserSign s where s.del_flg = 0 and s.stop_flg = 0 ";
		if("1".equals(signType)){//个人签名
			hql +=" and t.signType='"+signType+"' and t.userAcc='"+account+"' ";
		}else if("2".equals(signType)){
			hql +=" and t.signType='"+signType+"' and t.userAcc like '%"+account+"%' ";
		}else{
			hql +=" and t.userAcc like '%"+account+"%' ";
		}
		List<OaUserSign> list=super.find(hql, null);
		if(list.size()>0){
			return list;
		}
		return new ArrayList<OaUserSign>();
	}
}
