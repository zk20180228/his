package cn.honry.inner.operation.arrange.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Result;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.honry.base.bean.model.OperationArrange;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.operation.arrange.dao.ArrangementInnerDAO;
import cn.honry.inner.operation.record.vo.OperationUserVo;
@Repository("arrangementInnerDAO")
@SuppressWarnings({"all"})
public class ArrangementInnerDAOImpl extends HibernateEntityDao<OperationArrange> implements ArrangementInnerDAO {

	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name="sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	
	
	/**
	 * @Description:通过手术序号得到人员信息
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月15日
	 * @param:id-手术序号
	 * @param:type-要查询的类型，巡回，洗手，助手医生，临时助手等
	 * @return:List<OperationUserVo>
	 * @Modifier:zhangjin
	 * @ModifyDate:2016-05-20
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationUserVo> getOperationUserList(String id,String type,String fore){
		StringBuffer sb=null;
		if("ap".equals(fore)){//手术申请的人员查询
			 sb = new StringBuffer();
			sb.append("select a.id as id,a.operation_id as operationId,a.role_code as roleCode,a.empl_code as emplCode,a.empl_name as emplName,FORE_FLAG as foreFlag");
			sb.append(" from t_operation_arrange a where a.operation_id='"+id+"' and a.role_code like'%"+type+"%' and a.del_flg=0 and stop_flg=0 and FORE_FLAG='2' order by a.role_code " );
			
		}
		//手术安排统计
		if("1".equals(fore) || "2".equals(fore)){
			 sb = new StringBuffer();
				sb.append("select a.id as id,a.operation_id as operationId,a.role_code as roleCode,a.empl_code as emplCode,a.empl_name as emplName,FORE_FLAG as foreFlag");
				sb.append(" from t_operation_arrange a where a.operation_id='"+id+"' and a.role_code like'%"+type+"%' and a.del_flg=0 and stop_flg=0 and FORE_FLAG='1' order by a.role_code " );
				
		}else if("3".equals(fore)||"5".equals(fore)){
			 sb = new StringBuffer();
				sb.append("select a.id as id,a.operation_id as operationId,a.role_code as roleCode,a.empl_code as emplCode,a.empl_name as emplName,FORE_FLAG as foreFlag");
				sb.append(" from t_operation_arrange a where a.operation_id='"+id+"' and a.role_code like'%"+type+"%' and a.del_flg=0 and stop_flg=0 and FORE_FLAG='2' order by a.role_code " );
		}else if("4".equals(fore)){
			 sb = new StringBuffer();
				sb.append("select a.id as id,a.operation_id as operationId,a.role_code as roleCode,a.empl_code as emplCode,a.empl_name as emplName,FORE_FLAG as foreFlag");
				sb.append(" from t_operation_arrange a where a.operation_id='"+id+"' and a.role_code like'%"+type+"%' and a.del_flg=0 and stop_flg=0 and FORE_FLAG='5' order by a.role_code " );
		}
		
		SQLQuery queryObject=this.getSession().createSQLQuery(sb.toString()).addScalar("id").addScalar("operationId")
				.addScalar("roleCode").addScalar("emplCode").addScalar("emplName").addScalar("foreFlag");
		List<OperationUserVo> OperationUserVoList =queryObject.setResultTransformer(Transformers.aliasToBean(OperationUserVo.class)).list();
	
		if(OperationUserVoList!=null&&OperationUserVoList.size()>0){
			return OperationUserVoList;
		}
		return new ArrayList<OperationUserVo>();
	}
}
