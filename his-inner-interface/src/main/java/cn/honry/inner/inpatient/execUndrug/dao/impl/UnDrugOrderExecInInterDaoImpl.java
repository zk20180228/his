package cn.honry.inner.inpatient.execUndrug.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.execUndrug.dao.UnDrugOrderExecInInterDao;


@Repository("unDrugOrderExecInInterDao")
@SuppressWarnings({"all"})
public class UnDrugOrderExecInInterDaoImpl extends HibernateEntityDao<InpatientExecundrug> implements UnDrugOrderExecInInterDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 *  
	 * @Description：  分页查询 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientExecundrug> getPageInfo(String hql, String page,String rows) {
		return super.getPage(hql, page, rows);
	}

	/**  
	 *  
	 * @Description：  分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int getTotalInfo(String hql) {
		return super.getTotal(hql);
	}

	/**  
	 *  
	 * @Description：  查询医嘱执行药品信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientExecundrugNow> getList(String id) {
		id = id.replaceAll(",", "','");
		String hql = "FROM InpatientExecundrugNow e WHERE e.id IN ('"+id+"')";
		List<InpatientExecundrugNow> list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<InpatientExecundrug> getListNoExec(String patientIds,String pid,String did,String btime,String etime) {
		patientIds = "'"+patientIds.replaceAll(",", "','")+"'";
		String hql = "FROM InpatientExecundrug e WHERE e.inpatientNo in ("+patientIds+") and validFlag=? and execFlag=? ";
		if(StringUtils.isNotBlank(pid)){
			hql += " and e.undrugName like '%"+pid+"%' ";
		}
		if(StringUtils.isNotBlank(did)){
			hql += " and e.execDeptcd = '"+did+"' ";
		}
		if(StringUtils.isNotBlank(btime)){
			hql += " and to_char(e.execDate,'yyyy-MM-dd HH:mm:ss') >= '"+btime+"' ";
		}
		if(StringUtils.isNotBlank(etime)){
			hql += " and to_char(e.execDate,'yyyy-MM-dd HH:mm:ss') <= '"+etime+"' ";
		}
		List<InpatientExecundrug> list = super.find(hql,1,0);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientExecundrug>();
	
	}

	@Override
	public List<InpatientExecundrug> getListNoFeeExec(InpatientOrder order,
			Date stopTime) {
		String hql = "FROM InpatientExecundrug e WHERE e.inpatientNo =? and chargeFlag=? and chargeState=? and useTime=?";
		List<InpatientExecundrug> list = super.find(hql, order.getInpatientNo(),0,1,stopTime);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**
	 * 根据执行信息id获得药品执行记录
	 * @author  aizhonghua
	 * @createDate： 2016年5月25日 下午15:35:07 
	 * @modifier aizhonghua
	 * @modifyDate：2016年5月25日 下午15:35:07 
	 * @param：  exeId 住院流水号
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientExecundrug> getListNoExecByExeId(String exeId) {
		exeId = "'"+exeId.replaceAll(",", "','")+"'";
		String hql = "FROM InpatientExecundrug e WHERE e.id in ("+exeId+") and validFlag=? and execFlag=?";
		List<InpatientExecundrug> list = super.find(hql,1,0);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

}
