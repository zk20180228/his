package cn.honry.inner.mobile.dbFile.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FictitiousContact;
import cn.honry.base.bean.model.FictitiousDept;
import cn.honry.base.bean.model.MContactDBVersion;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.mobile.dbFile.dao.PersonInfoXmlDao;

@Repository("personInfoXmlDao")
@SuppressWarnings({ "all" })
public class PersonInfoXmlDaoImpl extends HibernateEntityDao<MContactDBVersion> implements PersonInfoXmlDao{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<FictitiousDept> findVirtualDept(String code_encode)
			throws Exception {
		String hql = "FROM FictitiousDept WHERE deptType = ? AND stop_flg = 0 AND del_flg = 0 AND deptCode not in('2072','2073')";//因员工数据重复问题 故先暂时排除 内科医学部 2072  内二医学部   2073
		List<FictitiousDept> list = super.find(hql, code_encode);
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<FictitiousDept>();
	}

	@Override
	public List<FictitiousContact> findActualDept(FictitiousDept tfd)
			throws Exception {
		String hql = "FROM FictitiousContact WHERE fictCode = ? AND type = ? AND stop_flg = 0 AND del_flg = 0";
		List<FictitiousContact> list = super.find(hql, tfd.getDeptCode(),tfd.getDeptType());
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<FictitiousContact>();
	}

	@Override
	public List<SysEmployee> findPersonInfo(String dept_code) throws Exception {
		String hql = "from SysEmployee where deptId.deptCode = ? AND stop_flg = 0 AND del_flg = 0 ";
		List<SysEmployee> list = super.find(hql, dept_code);
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<SysEmployee>();
	}

}
