package cn.honry.inner.operation.record.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OperAtionRecord;
import cn.honry.base.bean.model.OperationArrange;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.operation.record.dao.RecordInInterDAO;
import cn.honry.inner.operation.record.vo.OpNameVo;
import cn.honry.inner.operation.record.vo.OperationUserVo;

/**  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("recordInInterDAO")
public class RecordInInterDAOImpl extends HibernateEntityDao<OperAtionRecord> implements RecordInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * @Description:通过手术序号得到人员信息
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月15日
	 * @param:id-手术序号
	 * @param:type-要查询的类型，巡回，洗手，助手医生，临时助手等
	 * @return:List<OperationArrange>
	 * @Modifier:zhangjin
	 * @ModifyDate:2016-05-20
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationArrange> getOperationUserList(String id,String type){
		String sql="from OperationArrange a where a.operationId='"+id+"' and a.roleCode like'%"+type+"%' and a.del_flg=0 and stop_flg=0 ";
		List<OperationArrange> list=super.find(sql, null);
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<OperationArrange>();
	}
	
	/**
	 * @Description:通过手术序号得到人员信息
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月15日
	 * @param:id-手术序号
	 * @param:fore-手术状态
	 * @param:type-要查询的类型，巡回，洗手，助手医生，临时助手等
	 * @return:List<OperationUserVo>
	 * @Modifier:zhangjin
	 * @ModifyDate:2016-05-20
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationUserVo> getOperationUserCancelList(String id,String type,String fore){
		StringBuffer sb = new StringBuffer();
		sb.append("select a.id as id,a.operation_id as operationId,a.role_code as roleCode,a.empl_code as emplCode,a.empl_name as emplName");
		sb.append(" from t_operation_arrange a where a.operation_id='"+id+"' and a.role_code like'%"+type+"%'  and a.del_flg=0 and stop_flg=0 " );
		if(StringUtils.isNotBlank(fore) && "3".equals(fore)){
			sb.append(" and a.fore_flag='2'");
		}
		sb.append(" order by a.role_code ");
		SQLQuery queryObject=this.getSession().createSQLQuery(sb.toString()).addScalar("id").addScalar("operationId")
																			.addScalar("roleCode").addScalar("emplCode").addScalar("emplName");
		List<OperationUserVo> OperationUserVoList =queryObject.setResultTransformer(Transformers.aliasToBean(OperationUserVo.class)).list();
		if(OperationUserVoList!=null&&OperationUserVoList.size()>0){
			return OperationUserVoList;
		}
		return new ArrayList<OperationUserVo>();
	}
	
	/**
	 * @Description:通过手术序号查询手术名称
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月15日
	 * @param:id-手术序号
	 * @return:List<OpNameVo>手术名称集合list
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OpNameVo> getOpNameVoCancleList(String id){
		StringBuffer sb = new StringBuffer();
		sb.append("select i.id as id,i.operation_id as operationId,i.item_name as itemname,i.ITEM_ID as itemId from t_operation_item i where i.operation_id='"+id+"' and i.del_flg=0 and stop_flg=0 and i.ITEM_FLAG=1");
		SQLQuery queryObject=this.getSession().createSQLQuery(sb.toString()).addScalar("id").addScalar("operationId").addScalar("itemName").addScalar("itemId");
		List<OpNameVo> OpNameVoList =queryObject.setResultTransformer(Transformers.aliasToBean(OpNameVo.class)).list();
		if(OpNameVoList!=null&&OpNameVoList.size()>0){
			return OpNameVoList;
		}
		return new ArrayList<OpNameVo>();
	}
	
	
}
