package cn.honry.inner.inpatient.nurseInfoPack.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.VidExecdrugBedname;
import cn.honry.base.bean.model.VidExecdrugBednameKs;
import cn.honry.base.bean.model.VidExecundrugBedname;
import cn.honry.base.bean.model.VidExecundrugBednameKs;
import cn.honry.base.bean.model.VidInfoOrder;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.inpatient.nurseInfoPack.dao.NurseInfoPackInInterDao;

@Repository("nurseInfoPackInInterDao")
@SuppressWarnings({"all"})
public class NurseInfoPackInInterDaoImpl extends HibernateEntityDao<VidInfoOrder> implements NurseInfoPackInInterDao {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private  DeptInInterDAO departmentDAO;

	
	public VidInfoOrder getIdbyORder(String id) {
	   String hql=" from VidInfoOrder v where v.stop_flg=0  and v.del_flg=0 and v.orderId='"+id+"'";
       List<VidInfoOrder> lst = super.find(hql, null);
       if(lst!=null&&lst.size()>0){
    	   return lst.get(0);
       }
		return new VidInfoOrder();
	}


	//医嘱审核页面第一层显示
	public List<InpatientInfo> getAlls(InpatientInfo info,String typeName,String deptId,String type) {
		String hql=" from InpatientInfo o where o.stop_flg=0 and o.del_flg=0  "
				+ " and o.inpatientNo in ";
		if("1".equals(typeName)){
			hql+=" ( select r.inpatientNo from InpatientOrder r where r.stop_flg=0 and r.del_flg=0 and r.decmpsState = 1 and r.subtblFlag !=1 and ((r.moStat=0 and (r.confirmFlag != 1 or r.confirmFlag is null)) or (r.moStat=3 and (r.dcConfirmFlag != 1 or r.dcConfirmFlag is null))))";
		}
		else{
			hql+=" ( select r.inpatientNo from InpatientOrder r where r.stop_flg=0 and r.del_flg=0 and r.decmpsState = 0 and r.subtblFlag !=1 and ((r.moStat=0 and (r.confirmFlag != 1 or r.confirmFlag is null)) or (r.moStat=3 and (r.dcConfirmFlag != 1 or r.dcConfirmFlag is null))))";
		}
		//如果按照左侧树查询 直接取住院流水号查询
		if(StringUtils.isNotBlank(info.getInpatientNo())){
			hql+=" and o.inpatientNo in ('"+info.getInpatientNo()+"') ";
		}
		if("N".equals(type)){
			//hql+="  and o.bedId in (select b.id from InpatientBedinfo b where b.nurseCellCode='"+deptId+"')";
			hql+="  and o.nurseCellCode='"+deptId+"'";
		}
		else{
			hql+=" and o.deptCode in (select tdc.deptId "
					+ "from DepartmentContact tdc "
					+ "where tdc.id in (select dc.pardeptId "
					+ "from DepartmentContact dc "
					+ "where dc.deptId ='"+deptId+"'"
					+ "and dc.referenceType = '03'))";
		}
		List<InpatientInfo> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientInfo>();
	}
	/**
	 * 带条件分页查询显示长期医嘱+住院登记表
	 * @author zhenglin  2015-12-25
	 * @param vidInfoOrder
	 * @param rows
	 * @param page
	 * @return
	 */
	public List<InpatientOrder> viewInfo(InpatientOrder vidInfoOrder, String rows,String page) {
		String hql=joint(vidInfoOrder);
		List<InpatientOrder> list = find(hql);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientOrder>();
	}

	public List<InpatientOrder> getPages(String hql, String page,String rows) {
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		StatelessSession session=getHibernateTemplate().getSessionFactory().openStatelessSession();
		Query query = session.createQuery(hql);
		List<InpatientOrder> list = query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientOrder>();
	}
	/**
	 * 获得长期总数
	 * @author zhenglin 2015-12-25
	 * @param vidInfoOrder
	 * @return
	 */
	public int getTotal(InpatientOrder vidInfoOrder) {
		String hql=joint(vidInfoOrder);
		return super.getTotal(hql);
	}
	
	
	/**
	 * 总数和带条件分页查询hql
	 * zhenglin 2015-12-25
	 */
	public String joint(InpatientOrder vidInfoOrder){
		String hql="from InpatientOrder v where v.del_flg=0 "
				+ "and v.stop_flg=0 and v.inpatientNo='"+vidInfoOrder.getInpatientNo()+"' "
				+ "and v.decmpsState = 1 and v.subtblFlag !=1 and ((v.moStat=0 and (v.confirmFlag != 1 or v.confirmFlag is null)) or (v.moStat=3 and (v.dcConfirmFlag != 1 or v.dcConfirmFlag is null))) order by v.combNo,v.sortId  ";
		return hql;
	}


//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	/**
	 * 总数和带条件分页查询hql 临时医嘱
	 * zhenglin 2015-12-25
	 */
	public List<InpatientOrder> viewInfos(InpatientOrder vidInfoOrder, String rows,
			String page) {
		String hql=joints(vidInfoOrder);
		List<InpatientOrder> list = find(hql);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientOrder>();
	}
	
	public List<InpatientOrder> getPager(String hql, String page,String rows) {
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		StatelessSession session=getHibernateTemplate().getSessionFactory().openStatelessSession();
		Query query = session.createQuery(hql);
		List<InpatientOrder> list = query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientOrder>();
	}
	/**
	 * 获得总数  临时医嘱
	 * @author zhenglin 2015-12-25
	 * @param vidInfoOrder
	 * @return
	 */
	public int getTotals(InpatientOrder vidInfoOrder) {
		String hql=joints(vidInfoOrder);
		return super.getTotal(hql);
	}
	
	/**
	 * 临时医嘱
	 * 总数和带条件分页查询hql
	 * zhenglin 2015-12-25
	 */
	public String joints(InpatientOrder vidInfoOrder){
		String hql="from InpatientOrder v where v.del_flg=0 and v.stop_flg=0";
		
		if(StringUtils.isNotBlank(vidInfoOrder.getInpatientNo())){//按照住院流水号查询
				hql+=" and v.inpatientNo='"+vidInfoOrder.getInpatientNo()+"'";
		}if(StringUtils.isNotBlank(vidInfoOrder.getDrugType()) && !"0".equals(vidInfoOrder.getDrugType())){//按照药品类型查询
			if("1".equals(vidInfoOrder.getDrugType())){//中成药
				hql+=" and v.className='中成药'";
			}if("2".equals(vidInfoOrder.getDrugType())){//西药
				hql+=" and v.className='西药'";		
			}if("3".equals(vidInfoOrder.getDrugType())){//检查
				hql+=" and v.className='检查'";
			}if("4".equals(vidInfoOrder.getDrugType())){//检验
				hql+=" and v.labCode is not null";
			}
		}
		hql+=" and  v.decmpsState = 0 and v.subtblFlag !=1 and ((v.moStat=0 and (v.confirmFlag != 1 or v.confirmFlag is null)) or (v.moStat=3 and (v.dcConfirmFlag != 1 or v.dcConfirmFlag is null))) order by v.combNo,v.sortId  ";
		return hql;
	}
	/**  
	 * @Description：  患者树 zhenglin
	 */
	@Override
	public List<InpatientInfo> queryPatient(String deptId,String type) {
		String hql="from InpatientInfo i where i.stop_flg=0  AND i.del_flg=0  and"
				+ " i.inpatientNo in(select r.inpatientNo from InpatientOrder r where r.stop_flg=0 and r.del_flg=0 ) ";
		if(StringUtils.isNotBlank(deptId)){
			if("N".equals(type)){
				//hql+=" and i.bedId in (select b.id from InpatientBedinfo b where b.nurseCellCode='"+deptId+"')";
				hql+=" and i.nurseCellCode='"+deptId+"'";
			}
			else{
				hql+=" and i.deptCode in (select tdc.deptId "
						+ "from DepartmentContact tdc "
						+ "where tdc.id in (select dc.pardeptId "
						+ "from DepartmentContact dc "
						+ "where dc.deptId ='"+deptId+"'"
						+ "and dc.referenceType = '03'))";
			}
		}
		List<InpatientInfo> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfo>();
	}


	//查询全部-----------------zhenglin
	public List<VidInfoOrder> allList(VidInfoOrder vdd) {
		String hql=" from VidInfoOrder v where v.stop_flg=0 and v.del_flg=0 ";
		if(StringUtils.isNotBlank(vdd.getOrderId())){
			hql+=" and v.orderId='"+vdd.getOrderId()+"'";
		}
	    List<VidInfoOrder> list=super.find(hql, null);
	    if(list!=null&&list.size()>0){
	    	return list;
	    }
		return new ArrayList<VidInfoOrder>();
	}


	public List<SysDepartment> queryPharmacyByDept() {
		String hql = "FROM SysDepartment d WHERE d.id in " +
				 "(SELECT dc.deptId FROM DepartmentContact dc WHERE dc.id IN " +
				 "(SELECT c.pardeptId FROM DepartmentContact c WHERE  c.referenceType = '01') "
				 + "AND dc.referenceType = '01') ";
	List<SysDepartment> depyList = departmentDAO.findByObjectProperty(hql,null);
	if(depyList!=null&&depyList.size()>0){
		return depyList;
	}
	return new ArrayList<SysDepartment>();
	}

	/**
	 * 查询医嘱信息根据药品类型来查询（药品/非药品）
	 */
	public List<InpatientOrder> allListByYao(InpatientOrder order,String begin,String dateend,String deptId,String type,String rows,String page) {
		String hql=getResolveDataSql(order, begin, dateend, deptId, type);
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"10":rows);
		Query query = this.getSession().createQuery(hql);
	    List<InpatientOrder> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		 if(list!=null&&list.size()>0){
			 return list;
		 }
		return new ArrayList<InpatientOrder>();
	}
	
	public int getResolveDataTotal(InpatientOrder order,String begin,String dateend,String deptId,String type) {
		String hql=getResolveDataSql(order, begin, dateend, deptId, type);
		return super.getTotal(hql);
	}
	
	public String getResolveDataSql(InpatientOrder order,String begin,String dateend,String deptId,String type){
		 String hql=" from InpatientOrder v where v.del_flg=0 and v.stop_flg=0  and v.moStat=1 ";
			 //根据医嘱类型查询（药品/非药品）
			 if(StringUtils.isNotBlank(order.getClassName())){
				 if(Integer.parseInt(order.getClassName())==1){
					 hql+=" and v.className='非药品'";
				 }
				 else if(Integer.parseInt(order.getClassName())==0){
					 hql+=" and (v.className !='非药品' or v.className is null)";
				 }
			 }
			 //根据时间段来查询
			 if(StringUtils.isNotBlank(begin) && StringUtils.isNotBlank(dateend)){
				hql+=" and  to_char(v.moDate,'yyyy-MM-dd HH:mm:ss') between '"+begin+"' and '"+dateend+"'";
			 }
			 //根据医嘱名字来查询
			 if(StringUtils.isNotBlank(order.getItemName())){
				 hql+=" and v.itemName='"+order.getItemName()+"'";
			 }
			 //根据药房来查询
			 if(StringUtils.isNotBlank(order.getPharmacyCode())){
				 hql+=" and v.pharmacyCode='"+order.getPharmacyCode()+"'";
			 }
			 //根据患者数来查询
			 if(StringUtils.isNotBlank(order.getInpatientNo())){
				 hql+=" and v.inpatientNo in('"+order.getInpatientNo()+"')";
			 }
			 if(StringUtils.isNotBlank(deptId)){
				 if("N".equals(type)){
					 hql+=" and v.inpatientNo in ("
					 		+ "select f.inpatientNo from InpatientInfo "
					 		//+ "f where f.stop_flg=0 and f.del_flg=0 and"
					 	//	+ " f.bedId in (select b.id from InpatientBedinfo b "
					 		//+ "where b.nurseCellCode='"+deptId+"' and b.stop_flg=0 and b.del_flg=0)"
					 		
					 		+ " f where f.nurseCellCode ='"+deptId+"'"
					        + ")";
				 }
				 else{
					 hql+=" and v.deptCode in (select tdc.deptId "
							+ "from DepartmentContact tdc "
							+ "where tdc.id in (select dc.pardeptId "
							+ "from DepartmentContact dc "
							+ "where dc.deptId ='"+deptId+"'"
							+ "and dc.referenceType = '03'))'";
				 }
			 }
			 hql+=" and (v.typeName='长期医嘱' or v.typeName='嘱托长嘱') "
			 		+ "and v.subtblFlag !=1 order by v.inpatientNo";
			 return hql;
	}
	/**
	 * 获取系统参数表根据参数名称
	 */
	public HospitalParameter getByHosInfoByName(String name) {                   //parameterCode
		String hql="from HospitalParameter h where  h.parameterCode='"+name+"'";
		StatelessSession session=getHibernateTemplate().getSessionFactory().openStatelessSession();
		Query query = session.createQuery(hql);
		List<HospitalParameter> list=query.list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new HospitalParameter();
	}
	
	public VidInfoOrder getOrderByOrderId(String id) {
		String hql=" from VidInfoOrder r where r.stop_flg=0 and r.del_flg=0 ";
		if(StringUtils.isNotBlank(id)){
			hql+=" and r.orderId='"+id+"'";
		}
		List<VidInfoOrder> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new VidInfoOrder();
	}
	//医嘱类别下拉框
	public List<InpatientKind> getCombobox() {
		 String hql=" from InpatientKind i where i.stop_flg=0 and i.del_flg=0";
		 List<InpatientKind> list=super.find(hql, null);
		 if(list!=null&&list.size()>0){
			 return list;
		 }
		return new ArrayList<InpatientKind>();
	}
	//分页查询药嘱执行档ks
	public List<VidExecdrugBednameKs> execDruglistks(VidExecdrugBednameKs execdrug,String page, String rows,String deptId) {
	    String hql=execDrugKsHql(execdrug,deptId);
	    int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
	    List<VidExecdrugBednameKs> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
	    if(list!=null&&list.size()>0){
			 return list;
		 }
		return new ArrayList<VidExecdrugBednameKs>();
	}
	//统计药嘱执行档ks
	public int execDrugTotalks(VidExecdrugBednameKs execdrug,String deptId) {
		String hql=execDrugKsHql(execdrug,deptId);
		return super.getTotal(hql);
	}
	//药嘱执行档查询sql及查询条件
		public String execDrugKsHql(VidExecdrugBednameKs execdrug,String deptId){
			 String hql="";
			 if(StringUtils.isNotBlank(deptId)){
				 hql=" from VidExecdrugBednameKs u where u.stop_flg=0 and u.del_flg=0";
				 hql+=" and u.deptId='"+deptId+"'";
			 }
			 	if(StringUtils.isNotBlank(execdrug.getTypeCode())){
					hql+=" and u.typeCode='"+execdrug.getTypeCode()+"'";
				}
				if(StringUtils.isNotBlank(execdrug.getDrugName())){
					hql+=" and u.drugName like'%"+execdrug.getDrugName()+"%'";
				}
				if(StringUtils.isNotBlank(execdrug.getInpatientNo())){
					hql+=" and u.inpatientNo ='"+execdrug.getInpatientNo()+"'";
				}
				if(execdrug.getValidFlag() != null){
					if(execdrug.getValidFlag()==1){
						hql+=" and u.validFlag=1";
					}
					else{
						hql+="  and u.validFlag!=1";
					}
				}
				if(execdrug.getDrugedFlag() != null){
					if(execdrug.getDrugedFlag()==1){
						hql+=" and u.drugedFlag=3";
					}
					else{
						hql+="  and u.drugedFlag!=3";
					}
				}
			 hql+=" order by u.bedName ";
			return hql;
		}
	//分页查询药嘱执行档
	public List<VidExecdrugBedname> execDruglist(VidExecdrugBedname execdrug,String page, String rows,String deptId) {
	    String hql=execDrugHql(execdrug,deptId);
	    int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
	    List<VidExecdrugBedname> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
	    if(list!=null&&list.size()>0){
			 return list;
		 }
		return new ArrayList<VidExecdrugBedname>();
	}
	//统计药嘱执行档
	public int execDrugTotal(VidExecdrugBedname execdrug,String deptId) {
		String hql=execDrugHql(execdrug,deptId);
		return super.getTotal(hql);
	}
	
	//药嘱执行档查询sql及查询条件
	public String execDrugHql(VidExecdrugBedname execdrug,String deptId){
		 String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidExecdrugBedname u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.nurseCellCode1='"+deptId+"'";
		 }
		 	if(StringUtils.isNotBlank(execdrug.getTypeCode())){
				hql+=" and u.typeCode='"+execdrug.getTypeCode()+"'";
			}
			if(StringUtils.isNotBlank(execdrug.getDrugName())){
				hql+=" and u.drugName like'%"+execdrug.getDrugName()+"%'";
			}
			if(StringUtils.isNotBlank(execdrug.getInpatientNo())){
				hql+=" and u.inpatientNo ='"+execdrug.getInpatientNo()+"'";
			}
			if(execdrug.getValidFlag() != null){
				if(execdrug.getValidFlag()==1){
					hql+=" and u.validFlag=1";
				}
				else{
					hql+="  and u.validFlag!=1";
				}
			}
			if(execdrug.getDrugedFlag() != null){
				if(execdrug.getDrugedFlag()==1){
					hql+=" and u.drugedFlag=3";
				}
				else{
					hql+="  and u.drugedFlag!=3";
				}
			}
		 hql+=" order by u.bedName ";
		return hql;
	}
	//药嘱执行档分页
	public List<InpatientExecdrug> execDrugPage(String hql, String page,String rows) {
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
		List<InpatientExecdrug> list = query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientExecdrug>();
	}
	
	
	//分页查询非药品执行档
	public List<VidExecundrugBedname> execUnDrugList(VidExecundrugBedname execundrug, String page, String rows,String deptId) {
		String hql=execUnDrugHql(execundrug,deptId);
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
	    List<VidExecundrugBedname> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
	    if(list!=null&&list.size()>0){
			 return list;
		 }
		return new ArrayList<VidExecundrugBedname>();
	}
	//统计非药品执行档
	public int execUnDrugTotal(VidExecundrugBedname execundrug,String deptId) {
		String hql=execUnDrugHql(execundrug,deptId);
		return super.getTotal(hql);
	}
	//非药品执行档查询sql及查询条件
	public String execUnDrugHql(VidExecundrugBedname execUndrug,String deptId){
		 String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidExecundrugBedname u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.nurseCellCode1='"+deptId+"'";
		 }
		 if(StringUtils.isNotBlank(execUndrug.getTypeCode())){
				hql+=" and u.typeCode='"+execUndrug.getTypeCode()+"'";
			}
			if(StringUtils.isNotBlank(execUndrug.getInpatientNo())){
				hql+=" and u.inpatientNo='"+execUndrug.getInpatientNo()+"'";
			}
			if(StringUtils.isNotBlank(execUndrug.getUndrugName())){
				hql+=" and u.undrugName like '%"+execUndrug.getUndrugName()+"%'";
			}
			if(execUndrug.getValidFlag() != null){
				if(execUndrug.getValidFlag()==1){
					hql+=" and u.validFlag=1";
				}
				else{
					hql+=" and u.validFlag!=1";
				}
		}
		 hql+=" order by u.bedName ";
		return hql;
	}
	//分页查询非药品执行档
		public List<VidExecundrugBednameKs> execUnDrugListks(VidExecundrugBednameKs execundrug, String page, String rows,String deptId) {
			String hql=execUnDrugksHql(execundrug,deptId);
			int start = Integer.parseInt(page==null?"1":page);
			int count = Integer.parseInt(rows==null?"20":rows);
			Query query = this.getSession().createQuery(hql);
		    List<VidExecundrugBednameKs> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		    if(list!=null&&list.size()>0){
				 return list;
			 }
			return new ArrayList<VidExecundrugBednameKs>();
		}
		//统计非药品执行档
		public int execUnDrugTotalks(VidExecundrugBednameKs execundrug,String deptId) {
			String hql=execUnDrugksHql(execundrug,deptId);
			return super.getTotal(hql);
		}
		public String execUnDrugksHql(VidExecundrugBednameKs execUndrug,String deptId){
			 String hql="";
			 if(StringUtils.isNotBlank(deptId)){
				 hql=" from VidExecundrugBednameKs u where u.stop_flg=0 and u.del_flg=0";
				 hql+=" and u.deptId='"+deptId+"'";
			 }
			 if(StringUtils.isNotBlank(execUndrug.getTypeCode())){
					hql+=" and u.typeCode='"+execUndrug.getTypeCode()+"'";
			}
			if(StringUtils.isNotBlank(execUndrug.getInpatientNo())){
				hql+=" and u.inpatientNo='"+execUndrug.getInpatientNo()+"'";
			}
			if(StringUtils.isNotBlank(execUndrug.getUndrugName())){
				hql+=" and u.undrugName like '%"+execUndrug.getUndrugName()+"%'";
			}
			if(execUndrug.getValidFlag() != null){
				if(execUndrug.getValidFlag()==1){
					hql+=" and u.validFlag=1";
				}
				else{
					hql+=" and u.validFlag!=1";
				}
		}
		hql+=" order by u.bedName ";
		return hql;
		}
	/**
	 * 渲染患者
	 */
	public List<InpatientInfo> infos() {
		String hql=" from InpatientInfo o where o.del_flg=0 and o.stop_flg=0";
		List<InpatientInfo> list=super.find(hql, null);
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<InpatientInfo>();
	}

}
