package cn.honry.inner.inpatient.kind.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.kind.dao.InpatientKindInInterDAO;

@Repository("inpatientKindInInterDAO")
@SuppressWarnings({ "all" })
public class InpatientKindInInterDAOImpl extends HibernateEntityDao<InpatientKind> implements InpatientKindInInterDAO {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 获取记录条数
	 * @Author liguikang
	 * @date 2016-03-23 
	 * @version 1.0
	 */
	@Override
	public int getTotal(InpatientKind kindInfo) {
		String hql = joint(kindInfo);
		return super.getTotal(hql);
	}
	
	
	@Override
	public List<InpatientKind> getPage(String page, String rows, InpatientKind kindInfo) {
		String hql = joint(kindInfo);
		return super.getPage(hql, page, rows);
	}
	
	/**
	 * 拼接查询条件
	 * @Author liguikang
	 * @date 2016-03-23 
	 * @version 1.0
	 */
	public String joint(InpatientKind kindInfo){
		String hql=" from InpatientKind as ik where ik.del_flg=0 and ik.stop_flg=0 ";
		if(StringUtils.isNotBlank(kindInfo.getTypeName())){
			hql+=" and ik.typeName like '%"+kindInfo.getTypeName()+"%'";
		}		
		return hql;
	}
		
	
	/**
	 * 获取列表信息
	 * @Author liguikang
	 * @date 2016-03-23 
	 * @version 1.0
	 */
	@Override
	public List<InpatientKind> queryKindInfo() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from InpatientKind i where i.del_flg=0 and i.stop_flg=0 ");
		List<InpatientKind> list=super.findByObjectProperty(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return null;
	}

	/**
	 * 查询所有符合条件的数据
	 * @Author liguikang
	 * @date 2016-03-23 
	 * @version 1.0
	 */
	@Override
	public int queryKindInfoEncode(String encode, String typeCode) {
		String hql="";
		if (typeCode==null||typeCode=="") {
			hql=" from InpatientKind as ik where  ik.typeCode='"+typeCode+"' and ik.del_flg=0 and ik.stop_flg=0 ";
		}else {
			hql=" from InpatientKind as ik where  ik.typeCode='"+typeCode+"' and ik.id!='"+typeCode+"' and ik.del_flg=0 and ik.stop_flg=0 ";
		}
		List<InpatientKind> list=super.findByObjectProperty(hql.toString(), null);
		return list.size();
	}
	
	/**  
	 *  
	 * 生成编号
	 * @Author：liguikang
	 * @date 2016-03-28   
	 * @version 1.0
	 *
	 */
	@Override
	public int getSequece(InpatientKind entity) {
		return 0;
	}

	@Override
	public String queryKindInfoByName(String name) {
		String hql=" from InpatientKind as ik where ik.typeName='"+name+"' and ik.del_flg=0 and ik.stop_flg=0 ";
		List<InpatientKind> list=super.findByObjectProperty(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0).getTypeCode();
		}
		return null;
	}

	@Override
	public InpatientKind getByCode(String typeCode) {
		String hql=" from InpatientKind ik where ik.typeCode=? and ik.del_flg=? and ik.stop_flg=? ";
		InpatientKind kind = (InpatientKind) super.excHqlGetUniqueness(hql, typeCode,0,0);
		if(kind!=null&&StringUtils.isNotBlank(kind.getId())){
			return kind;
		}
		return null;
	}

}

	




