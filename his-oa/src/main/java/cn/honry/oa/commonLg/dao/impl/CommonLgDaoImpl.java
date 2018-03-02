package cn.honry.oa.commonLg.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaCommon;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.commonLg.dao.CommonLgDao;
import cn.honry.oa.commonLg.vo.CommonVo;
import cn.honry.oa.menumanager.vo.MenuVo;

@Repository("commonLgDao")
public class CommonLgDaoImpl extends HibernateEntityDao<OaCommon> implements CommonLgDao {
	
	 // 为父类HibernateDaoSupport注入sessionFactory的值
		@Resource(name = "sessionFactory")
		public void setSuperSessionFactory(SessionFactory sessionFactory) {
			super.setSessionFactory(sessionFactory);
		}

		@Override
		public List<OaCommon> findMyCommon(String account , String tableCode) throws Exception{
			String hql = "from OaCommon t where 1=1 ";
			if (StringUtils.isNotBlank(account)) {
				hql+=" and t.userAccount = '"+account+"' ";
			}
			if (StringUtils.isNotBlank(tableCode)) {
				hql+=" and t.tableCode = '"+tableCode+"' ";
			}
			List<OaCommon> list=this.getSession().createQuery(hql).list();
			if(list!=null&&list.size()>0){
				return list;
			}
			return new ArrayList<OaCommon>();
		}

		@Override
		public void delCommonById(String id) throws Exception {
			String sql="delete from T_OA_COMMON where id='"+id+"'";
			this.getSession().createSQLQuery(sql).executeUpdate();			
		}

		@Override
		public OaCommon findById(String id) throws Exception {
			String hql="from OaCommon t where t.id= '"+id+"'";
			List<OaCommon> list=this.getSession().createQuery(hql).list();
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
			return new OaCommon();
		}

		@Override
		public List<OaCommon> findFrom(String account) {
			String sql = "select distinct(t.tablename ) tableName , t.tablecode tableCode from t_oa_common t where t.useraccount = '"+account+"'";
			SQLQuery sqlQuery=super.getSession().createSQLQuery(sql)
					.addScalar("tableName").addScalar("tableCode");
			List<OaCommon> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(OaCommon.class)).list();
			if(list.size() > 0 && list != null){
				return list;
			}
			return new ArrayList<OaCommon>();
		}

		@Override
		public List<CommonVo> findCommon(String account, String tableCode) {
			String sql = "select t.id value , t.common text from t_oa_common t where t.useraccount = '"+account+"'" + " and t.tablecode = '"+tableCode+"'" ;
			SQLQuery sqlQuery=super.getSession().createSQLQuery(sql)
					.addScalar("value").addScalar("text");
			List<CommonVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(CommonVo.class)).list();
			if(list.size() > 0 && list != null){
				return list;
			}
			return new ArrayList<CommonVo>();
		}
		

}
