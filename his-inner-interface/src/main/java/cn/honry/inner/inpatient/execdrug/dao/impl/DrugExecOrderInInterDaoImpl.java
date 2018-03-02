package cn.honry.inner.inpatient.execdrug.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.execdrug.dao.DrugExecOrderInInterDao;


@Repository("drugExecOrderInInterDao")
@SuppressWarnings({"all"})
public class DrugExecOrderInInterDaoImpl extends HibernateEntityDao<InpatientExecdrug> implements DrugExecOrderInInterDao {

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
	public List<InpatientExecdrug> getPageInfo(String hql, String page,String rows) {
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
	public List<InpatientExecdrugNow> getList(String id) {
		id = id.replaceAll(",", "','");
		String hql = "FROM InpatientExecdrugNow e WHERE e.id IN (?)";
		List<InpatientExecdrugNow> list = super.find(hql, id);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**
	 * 保存医嘱执行档
	 * @author  zl
	 * @createDate： 2016年4月15日 上午9:34:02 
	 * @modifier zl
	 * @modifyDate：2016年4月15日 上午9:34:02
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void saveExDrugList(List<InpatientExecdrug> execdrug) {
		Session session = this.getSession();
		if (execdrug != null) {
			for (InpatientExecdrug inpatientExecdrug : execdrug) {
				System.out.println(inpatientExecdrug.getId());
				session.saveOrUpdate(inpatientExecdrug);
				session.flush();
			}
		}
	}

	@Override
	public List<InpatientExecdrug> getListNoExec(String patientIds,String pid,String did,String btime,String etime) {
		patientIds = "'"+patientIds.replaceAll(",", "','")+"'";
		String hql = "FROM InpatientExecdrug e WHERE e.inpatientNo in ("+patientIds+") and validFlag=? and execFlag=? ";
		if(StringUtils.isNotBlank(pid)){
			hql += " and e.drugName like '%"+pid+"%' ";
		}
		if(StringUtils.isNotBlank(did)){
			hql += " and e.pharmacyCode = '"+did+"' ";
		}
		if(StringUtils.isNotBlank(btime)){
			hql += " and to_char(e.useTime,'yyyy-MM-dd HH:mm:ss') >= '"+btime+"' ";
		}
		if(StringUtils.isNotBlank(etime)){
			hql += " and to_char(e.useTime,'yyyy-MM-dd HH:mm:ss') <= '"+etime+"' ";
		}
		List<InpatientExecdrug> list = super.find(hql,1,0);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientExecdrug>();
	}

	@Override
	public List<InpatientExecdrug> getListNoFeeExec(InpatientOrder order,
			Date stopTime) {
		String hql = "FROM InpatientExecdrug e WHERE e.inpatientNo =? and chargeFlag=? and chargeState=? and useTime=?";
		List<InpatientExecdrug> list = super.find(hql, order.getInpatientNo(),0,1,stopTime);
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
	public List<InpatientExecdrug> getListNoExecByExeId(String exeId) {
		exeId = "'"+exeId.replaceAll(",", "','")+"'";
		String hql = "FROM InpatientExecdrug e WHERE e.id in ("+exeId+") and validFlag=? and execFlag=?";
		List<InpatientExecdrug> list = super.find(hql,1,0);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<InpatientExecdrug> getListExecdrug(String userCode,
			String typtCode, String drugType) {
		String hql = "FROM InpatientExecdrug e WHERE e.typeCode='"+typtCode+"' and e.drugType='"+drugType+"' and e.usageCode='"+userCode+"'";
		List<InpatientExecdrug> list = super.find(hql,null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<InpatientExecundrug> getListExecundrug(String userCode,
			String typtCode, String drugType) {
		String hql = "FROM InpatientExecundrug e WHERE e.typeCode='"+typtCode+"' and e.classCode='"+drugType+"' and e.undrugCode='"+userCode+"'";
		List<InpatientExecundrug> list = super.find(hql,null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public cn.honry.base.bean.model.SysDepartment SysDepartment(String id) {
		String hql = "FROM SysDepartment e WHERE e.id='"+id+"'";
		List<cn.honry.base.bean.model.SysDepartment> list = super.find(hql,null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public String execdrugHql(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo){
		 String hql="from InpatientExecdrug c where c.del_flg=0 and c.stop_flg=0  ";
		 if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)){
			 hql+=" and to_char(c.moDate ,'yyyy-MM-dd hh24:mi:ss') >= '"+beginDate+"' ";
			 hql+=" and to_char(c.moDate ,'yyyy-MM-dd hh24:mi:ss') <= '"+endDate+"' ";
		 }
		 if(StringUtils.isNotBlank(validFlag)){
				if("1".equals(validFlag)){
					hql+=" and c.validFlag=1";
				}
				else{
					hql+="  and c.validFlag!=1";
				}
		 }
		 if(StringUtils.isNotBlank(inpatientNo)){
			 hql+=" and c.inpatientNo='"+inpatientNo+"'";
		 }
		 if(StringUtils.isNotBlank(drugedFlag)){
			 if("1".equals(drugedFlag)){
				hql+=" and c.drugedFlag=3";
			 }
			 else{
				hql+="  and c.drugedFlag!=3";
			 }
		 }
		hql+= " and (c.typeCode,c.usageCode,c.drugType) in (select b.typeCode,b.usageCode,b.drugType from "
		 		+ "InpatientDrugbilldetail b where b.del_flg=0 and b.stop_flg=0 and (b.billNo) in "
		 		+ "(select a.billNo from InpatientExecbill a where a.billNo='"+billNo+"' and a.del_flg=0 and a.stop_flg=0))";
		return hql;
	}
	
	@Override
	public List<InpatientExecdrug> queryExecdrugpage(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,
			String page, String rows,String inpatientNo) {
		 String hql=execdrugHql(billNo,validFlag,drugedFlag,beginDate,endDate,inpatientNo);
	    int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
	    List<InpatientExecdrug> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
	    if(list!=null&&list.size()>0){
			 return list;
		 }
		return new ArrayList<InpatientExecdrug>();
	}

	@Override
	public int queryExecdrugToatl(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo) {
		String hql=execdrugHql(billNo,validFlag,drugedFlag,beginDate,endDate,inpatientNo);
		return super.getTotal(hql);
	}

	@Override
	public List<InpatientExecundrug> queryExecundrugpage(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,
			String page, String rows,String inpatientNo) {
		String hql=execundrugHql(billNo,validFlag,drugedFlag,beginDate,endDate,inpatientNo);
	    int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
	    List<InpatientExecundrug> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
	    if(list!=null&&list.size()>0){
			 return list;
		 }
		return new ArrayList<InpatientExecundrug>();
	}
	@Override
	public int queryExecundrugToatl(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo) {
		String hql=execundrugHql(billNo,validFlag,drugedFlag,beginDate,endDate,inpatientNo);
		return super.getTotal(hql);
	}
	public String execundrugHql(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo){
		 String hql="from InpatientExecundrug c where c.del_flg=0 and c.stop_flg=0  ";
		 if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)){
			 hql+=" and to_char(c.moDate ,'yyyy-MM-dd hh24:mi:ss') >= '"+beginDate+"' ";
			 hql+=" and to_char(c.moDate ,'yyyy-MM-dd hh24:mi:ss') <= '"+endDate+"' ";
		 }
		 if(StringUtils.isNotBlank(validFlag)){
				if("1".equals(validFlag)){
					hql+=" and c.validFlag=1";
				}
				else{
					hql+="  and c.validFlag!=1";
				}
		 }
		 if(StringUtils.isNotBlank(inpatientNo)){
			 hql+=" and c.inpatientNo='"+inpatientNo+"'";
		 }
		 hql+= " and (c.typeCode,c.undrugCode,c.classCode) in (select b.typeCode,b.usageCode,b.drugType from "
 		+ "InpatientDrugbilldetail b where b.del_flg=0 and b.stop_flg=0 and (b.billNo) in "
 		+ "(select a.billNo from InpatientExecbill a where a.billNo='"+billNo+"' and a.del_flg=0 and a.stop_flg=0))";
		 
		return hql;
	}
	@Override
	public InpatientExecbill queryBillName(String billNo) {
		String hql = "FROM InpatientExecbill e WHERE e.billNo='"+billNo+"'";
		List<InpatientExecbill> list = super.find(hql,null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
