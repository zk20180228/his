package cn.honry.finance.contractunit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.contractunit.dao.RegisterFeeDAO;
import cn.honry.inner.system.utli.DataRightUtils;




@Repository("registerFeeDAO")
@SuppressWarnings({ "all" })
public class RegisterFeeDAOImpl extends HibernateEntityDao<RegisterFee> implements RegisterFeeDAO {


	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**  
	 *  
	 * @Description：  挂号费维护列表
	 * @Author：liudelin
	 * @CreateDate：2015-6-4 下午05:12:16  
	 * @Modifier：liudelin
	 * @ModifyDate：2015-6-4 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotal(RegisterFee entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
	}
	
	@Override
	public List<RegisterFee> getPage(RegisterFee entity, String page,String rows) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}
	
	public String joint(RegisterFee entity){
		String hql="FROM RegisterFee d WHERE d.del_flg = 0  and d.stop_flg=0 AND "+DataRightUtils.connectHQLSentence("d")+" ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getRegisterGrade())){
				hql = hql+" AND d.registerGrade LIKE '%"+entity.getRegisterGrade()+"%'";
			}
			
			if(StringUtils.isNotBlank(entity.getUid())&&!"0".equals(entity.getUid())){
				hql = hql+" AND d.unitId ='"+entity.getUid()+"'";
			}
		}
		hql = hql +" order by registerFee,checkFee,treatmentFee,otherFee,order ";
		return hql;
	}

	@Override
	public List<RegisterFee> findTree() {
		return null;
	}
	/**  
	 *  
	 * @Description：  挂号级别
	 * @Author：ldl
	 * @CreateDate：2015-7-9  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterGrade> gradeFeeCombobox() {
		String hql = " from RegisterGrade where del_flg=0 and stop_flg=0  ";
		List<RegisterGrade> registerGradeList = super.find(hql, null);
		if(registerGradeList==null||registerGradeList.size()<=0){
			return new ArrayList<RegisterGrade>();
		}
		return registerGradeList;
	}
	/**  
	 *  
	 * @Description：  查询费用修改数据
	 * @Author：ldl
	 * @CreateDate：2015-10-15  下午14:06:16 
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-26 下午05:12:16   
	 * @ModifyRmk：  修改了sql
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterFee> findRegisterFee(String ids) {
		ids = ids.replaceAll(",", "','");
		String hql = "from RegisterFee where  del_flg = 0 and id="+ids;
		List<RegisterFee> registerFeeList = super.find(hql, null);
		if(registerFeeList==null||registerFeeList.size()<=0){
			return new ArrayList<RegisterFee>();
		}
		return registerFeeList;
	}
	/**  
	 * 
	 * @Description：   验证挂号级别是否存在
	 * @Author：lyy
	 * @CreateDate：2015-11-26 下午05:37:56  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-26 下午05:37:56  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public boolean queryFeeValidate(String unitId, String gradeId) {
		String  hql = " from RegisterFee f where f.del_flg=0 and f.stop_flg=0 and f.registerGrade='"+gradeId+"' and f.unitId ='"+unitId+"'";
		List<RegisterFee> feeList = super.find(hql, null);
		if(feeList!=null&&feeList.size()>0){
			return false;
		}else{
			return true;
		}
		
	}

	@Override
	public Integer getOrderbyid(String unitId) {
		String sql = " select NVL(Max(fee_order),0) from t_register_fee t Where t.unit_id = :unitId ";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter("unitId", unitId);
		Object order = query.uniqueResult();
		int parseInt = Integer.parseInt(order.toString());
		return parseInt;
	}
}
