package cn.honry.inner.baseinfo.adviceToSystemclass.dao.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessAdvicetoSystemclass;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.adviceToSystemclass.dao.AdviceTScInInterDao;
import cn.honry.inner.baseinfo.adviceToSystemclass.vo.CodeSystemtypeVo;

@Repository("adviceTScInInterDao")
@SuppressWarnings({ "all" })
/**
 * 医嘱类型与系统类别关系维护Dao实现类
 *
 */
public class AdviceTScInInterDaoImpl extends HibernateEntityDao<BusinessAdvicetoSystemclass> implements AdviceTScInInterDao {
		
	@Resource(name = "sessionFactory")	
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 获得医嘱类型对应的系统类别
	 * @Author：hedong
	 * @CreateDate：2016年4月20日 下午3:57:31 
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016年4月20日 下午6:40:51 
	 * @ModifyRmk：  增加参数 bool
	 * @version： 1.0
	 * @param typeId：医嘱类型
	 * @param bool：是否添加"全部"选项
	 * @return：系统类别的集合
	 *
	 */
	@Override
	public List<CodeSystemtypeVo> querySystemTypesByTypeId(String typeId,boolean bool) {
		StringBuffer hql=new StringBuffer("select d.CODE_ENCODE as code,d.code_name as name from t_business_advicetosystemclass t "
				+ " INNER JOIN T_BUSINESS_DICTIONARY d ON d.CODE_TYPE = 'systemType' AND t.CLASSID = d.CODE_ENCODE where 1=1 and t.stop_flg = 0 and t.del_flg = 0 ");
		hql.append(" and t.typeid='"+typeId+"'");
		SQLQuery query = this.getSession().createSQLQuery(hql.toString()).addScalar("code").addScalar("name");
		List<CodeSystemtypeVo> kinds = query.setResultTransformer(Transformers.aliasToBean(CodeSystemtypeVo.class)).list();
		if(bool){
			if(kinds!=null&&kinds.size()>0){
				CodeSystemtypeVo vo = new CodeSystemtypeVo();
				vo.setCode("0");
				vo.setName("全部");
				kinds.add(0,vo);
			}
		}
		return kinds;
	}

	/**  
	 * 
	 * <p> 根据医嘱类别及项目类别判断对照关系是否存在 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2017年2月21日 下午3:14:49 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年2月21日 下午3:14:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: BusinessAdvicetoSystemclass
	 *
	 */
	@Override
	public BusinessAdvicetoSystemclass getAdvtoSysByAtSt(String aType, String encode) {
		String hql = "from BusinessAdvicetoSystemclass s where s.typeId = ? and s.classId = ? and s.stop_flg = 0 and s.del_flg = 0";
		List<BusinessAdvicetoSystemclass> list = this.find(hql, aType,encode);
		if(list!=null&&list.size()==1){
			return list.get(0);
		}
		return null;
	}
	
}
	
