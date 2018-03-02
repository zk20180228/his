package cn.honry.inner.inpatient.inpatientOrder.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.DrugPreoutstore;
import cn.honry.base.bean.model.DrugSplit;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.bean.model.VidOrderBedname;
import cn.honry.base.bean.model.VidOrderBednameKs;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.inpatientOrder.dao.InpatientOrderInInterDao;
import cn.honry.inner.inpatient.inpatientOrder.vo.InpatientOrderInInterVO;
import cn.honry.inner.inpatient.inpatientOrder.vo.OrderInInterVO;
import freemarker.template.utility.StringUtil;


@Repository("inpatientOrderInInterDao")
@SuppressWarnings({"all"})
public class InpatientOrderInInterDaoImpl extends HibernateEntityDao<InpatientOrder> implements
		InpatientOrderInInterDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	//带条件分页查询
	public List<InpatientOrder> listOrder(InpatientOrder inpatientOrder,String rows, String page,String typeName) {
		String hql=joint(inpatientOrder);
		if(StringUtils.isNotBlank(typeName)){
			if("1".equals(typeName)){
				hql+=" and v.typeName !='长期医嘱' and v.typeName!='嘱托长嘱'";
			}
			else{
				hql+=" and (v.typeName ='长期医嘱' or v.typeName='嘱托长嘱')";
			}
		}
		return super.getPage(hql, page, rows);
	}

	//获取分页总数
	public int getTotal(InpatientOrder inpatientOrder) {
		String hql=joint(inpatientOrder);
		return super.getTotal(hql);
	}
	
	//sql语句 （查询所有）
	public String joint(InpatientOrder inpatientOrder){
		 String hql="from  InpatientOrder v where v.combNo in( select t.combNo from InpatientOrder t where t.id ='"+inpatientOrder.getId()+"')"
	 		+ "and v.inpatientNo in( select v1.inpatientNo from InpatientOrder v1 where v1.id='"+inpatientOrder.getId()+"')"
	 		+ "and v.patientNo in (select v2.patientNo from InpatientOrder v2 where v2.id='"+inpatientOrder.getId()+"')"
	 	    + "and v.subtblFlag=1 and v.mainDrug=0 and v.stop_flg=0 and v.del_flg=0";
	 return hql;
	}
	
	//2016-01-22   根据主医嘱审核附材医嘱
	public List<InpatientOrder> lllistOrder(InpatientOrder inpatientOrder) {
		String hql=joint(inpatientOrder);
		return super.findByObjectProperty(hql, null);
	}

	

	public List<InpatientOrder> getItemName() {
		 String hql = " from InpatientOrder i where i.stop_flg=0  and i.del_flg=0 and i.moStat=1 and i.subtblFlag!=1";
	     List<InpatientOrder> itemList = super.findByObjectProperty(hql, null);
		 if(itemList!=null&&itemList.size()>0){
			return itemList;
		 }
		   return new ArrayList<InpatientOrder>();
	}

	/**
	 * 根据id查询所有
	 */
	public InpatientOrder getOrderddInfoById(String exeId) {
		String hql=" from InpatientOrder i where i.stop_flg =0 and i.del_flg =0 and i.id='"+exeId+"'";
		List<InpatientOrder> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new InpatientOrder();
	}


	/**
	 * 根据条件来查询摆药明细表
	 */
	public DrugBilllist getListByProperty(String... exist) {
		
		String hql="SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE drugBillclass.id IN "
				+ "(SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE typeCode = '"+exist[1]+"') "
				+ "AND drugBillclass.id IN "
				+ "(SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE doseModelCode = '"+exist[5]+"' "
				+ "OR drugQuality = '"+exist[4]+"') "
				+ "AND drugBillclass.id IN "
				+ "(SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE usageCode = '"+exist[2]+"') "
				+ "AND drugBillclass.id IN "
				+ "(SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE drugType = '"+exist[3]+"') "
				+ "and drugBillclass.id != 'R' and drugBillclass.id in(select id "
				+ "from DrugBillclass where controlId in("
				+ "select id from OutpatientDrugcontrol where deptCode='"+exist[0]+"'))";
		List<DrugBilllist> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			DrugBilllist db;
			db = new DrugBilllist();
			Object a=list.get(0);
			DrugBillclass clas = new DrugBillclass();
			clas.setId(a.toString());
			db.setDrugBillclass(clas);
			db.setId("22232");
			return db;
		}
		return new DrugBilllist();
	}


	public DrugBillclass getClassById(DrugBillclass drugBillclass) {
		String hql="from DrugBillclass d where d.id='"+drugBillclass+"'";
        List<DrugBillclass> list =super.find(hql, null);
        if(list!=null&&list.size()>0){
        	return list.get(0);
        }
		return new DrugBillclass();
	}

	/**
	 * 通过住院流水号得到住院主表信息
	 */
	public InpatientInfo getInfoByInpatientNo(String inpatientNo) {
       String hql=" from InpatientInfo io where io.del_flg =0 AND  io.stop_flg=0 and io.inpatientNo='"+inpatientNo+"'";
		List<InpatientInfo> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new InpatientInfo();
	}

	/**
	 * 通过住院流水号得到住院主表信息
	 */
	public List<InpatientInfo> getInfosByInpatientNos(String inpatientNo) {
       String hql=" from InpatientInfo io where io.del_flg =0 and io.inpatientNo in ('"+inpatientNo.replaceAll(",", "','")+"')";
		List<InpatientInfo> list=super.find(hql, null);
       return list;
	}
	
	/***
	 * 根据频次名称查找频次数据
	 */
	public BusinessFrequency getListByName(String name) {
		String hql=" from BusinessFrequency i where i.del_flg=0 and i.stop_flg=0 and i.id='"+name+"'";
		List<BusinessFrequency> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new BusinessFrequency();
	}

	public SysDepartment department(String id) {
		String hql=" from SysDepartment t where t.id='"+id+"' and t.stop_flg=0 and t.del_flg=0";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new SysDepartment();
	}
	/**
	 * 渲染医嘱类别
	 */
	public List<InpatientKind> kindMap() {
		String hql=" from InpatientKind k where k.stop_flg=0 and k.del_flg=0";
		List<InpatientKind> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientKind>();
	}
	/**
	 * 渲染医生
	 */
	public List<User> userMap() {
		String hql=" from User u where u.stop_flg=0 and u.del_flg=0";
		List<User> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<User>();
	}
	//长期医嘱
	@Override
	public List<VidOrderBedname> getOrdersInquiryPage(String rows, String page, VidOrderBedname order,String deptId ) {
		String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidOrderBedname u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.nurseCellCode1='"+deptId+"'";
		 }
		 if(order.getTypeName()!=null&&order.getTypeName()!=""){
				hql+=" and u.typeCode='"+order.getTypeName()+"'";
		 }
		 if(order.getInpatientNo()!=null&&order.getInpatientNo()!=""){
				hql+=" and u.inpatientNo='"+order.getInpatientNo()+"'";
		 }
		 if(order.getMoStat()!=null&&order.getMoStat()!=null){
				hql+=" and u.moStat='"+order.getMoStat()+"'";
		 }
		 if(order.getItemName()!=null&&order.getItemName()!=""){
				hql+=" and u.itemName like '%"+order.getItemName()+"%'";
		 }
		 if(order.getEmcFlag()!=null&&order.getEmcFlag()!=null){
				hql+=" and u.emcFlag='"+order.getEmcFlag()+"'";
		 }
		 hql+=" and u.decmpsState=1  order by u.bedName ";
		 int start = Integer.parseInt(page==null?"1":page);
			int count = Integer.parseInt(rows==null?"20":rows);
			Query query = this.getSession().createQuery(hql);
		    List<VidOrderBedname> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		    if(list!=null&&list.size()>0){
				 return list;
			 }
		return new ArrayList<VidOrderBedname>();
	}
	//长期医嘱
	@Override
	public int getOrdersInquiryTotal(VidOrderBedname order,String deptId) {
		String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidOrderBedname u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.nurseCellCode1='"+deptId+"'";
		 }
		 if(order.getTypeName()!=null&&order.getTypeName()!=""){
				hql+=" and u.typeCode='"+order.getTypeName()+"'";
		 }
		 if(order.getInpatientNo()!=null&&order.getInpatientNo()!=""){
				hql+=" and u.inpatientNo='"+order.getInpatientNo()+"'";
		 }
		 if(order.getMoStat()!=null&&order.getMoStat()!=null){
				hql+=" and u.moStat='"+order.getMoStat()+"'";
		 }
		 if(order.getItemName()!=null&&order.getItemName()!=""){
				hql+=" and u.itemName like '%"+order.getItemName()+"%'";
		 }
		 if(order.getEmcFlag()!=null&&order.getEmcFlag()!=null){
				hql+=" and u.emcFlag='"+order.getEmcFlag()+"'";
		 }
		 hql+=" and u.decmpsState=1  order by u.bedName ";
		return super.getTotal(hql);
	}
	//长期医嘱ks
	@Override
	public List<VidOrderBednameKs> getOrdersInquiryPageks(String rows, String page, VidOrderBednameKs order,String deptId ) {
		String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidOrderBednameKs u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.deptId='"+deptId+"'";
		 }
		 if(order.getTypeName()!=null&&order.getTypeName()!=""){
				hql+=" and u.typeCode='"+order.getTypeName()+"'";
		 }
		 if(order.getInpatientNo()!=null&&order.getInpatientNo()!=""){
				hql+=" and u.inpatientNo='"+order.getInpatientNo()+"'";
		 }
		 if(order.getMoStat()!=null&&order.getMoStat()!=null){
				hql+=" and u.moStat='"+order.getMoStat()+"'";
		 }
		 if(order.getItemName()!=null&&order.getItemName()!=""){
				hql+=" and u.itemName like '%"+order.getItemName()+"%'";
		 }
		 if(order.getEmcFlag()!=null&&order.getEmcFlag()!=null){
				hql+=" and u.emcFlag='"+order.getEmcFlag()+"'";
		 }
		 hql+=" and u.decmpsState=1  order by u.bedName ";
		 int start = Integer.parseInt(page==null?"1":page);
			int count = Integer.parseInt(rows==null?"20":rows);
			Query query = this.getSession().createQuery(hql);
		    List<VidOrderBednameKs> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		    if(list!=null&&list.size()>0){
				 return list;
			 }
		return new ArrayList<VidOrderBednameKs>();
	}
	//长期医嘱ks
	@Override
	public int getOrdersInquiryTotalks(VidOrderBednameKs order,String deptId) {
		String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidOrderBednameKs u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.deptId='"+deptId+"'";
		 }
		 if(order.getTypeName()!=null&&order.getTypeName()!=""){
				hql+=" and u.typeCode='"+order.getTypeName()+"'";
		 }
		 if(order.getInpatientNo()!=null&&order.getInpatientNo()!=""){
				hql+=" and u.inpatientNo='"+order.getInpatientNo()+"'";
		 }
		 if(order.getMoStat()!=null&&order.getMoStat()!=null){
				hql+=" and u.moStat='"+order.getMoStat()+"'";
		 }
		 if(order.getItemName()!=null&&order.getItemName()!=""){
				hql+=" and u.itemName like '%"+order.getItemName()+"%'";
		 }
		 if(order.getEmcFlag()!=null&&order.getEmcFlag()!=null){
				hql+=" and u.emcFlag='"+order.getEmcFlag()+"'";
		 }
		 hql+=" and u.decmpsState=1  order by u.bedName ";
		return super.getTotal(hql);
	}
	
	@Override
	public List<InpatientKind> getOrdersType() {
		String hql=" from InpatientKind t where t.del_flg=0 and t.stop_flg=0";
		List<InpatientKind> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientKind>();
	}
	//临时医嘱
	@Override
	public List<VidOrderBedname> getOrdersInterimInquiryPage(String rows, String page, VidOrderBedname order,String deptId) {
		String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidOrderBedname u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.nurseCellCode1='"+deptId+"'";
		 }
		 if(order.getTypeName()!=null&&order.getTypeName()!=""){
				hql+=" and u.typeCode='"+order.getTypeName()+"'";
		 }
		 if(order.getInpatientNo()!=null&&order.getInpatientNo()!=""){
				hql+=" and u.inpatientNo='"+order.getInpatientNo()+"'";
		 }
		 if(order.getMoStat()!=null&&order.getMoStat()!=null){
				hql+=" and u.moStat='"+order.getMoStat()+"'";
		 }
		 if(order.getItemName()!=null&&order.getItemName()!=""){
				hql+=" and u.itemName like '%"+order.getItemName()+"%'";
		 }
		 if(order.getEmcFlag()!=null&&order.getEmcFlag()!=null){
				hql+=" and u.emcFlag='"+order.getEmcFlag()+"'";
		 }
		 hql+=" and u.decmpsState=0  order by u.bedName ";
		 int start = Integer.parseInt(page==null?"1":page);
			int count = Integer.parseInt(rows==null?"20":rows);
			Query query = this.getSession().createQuery(hql);
		    List<VidOrderBedname> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		    if(list!=null&&list.size()>0){
				 return list;
			 }
		return new ArrayList<VidOrderBedname>();
	}
	//临时医嘱
	@Override
	public int getOrdersInterimInquiryTotal(VidOrderBedname order,String deptId) {
		String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidOrderBedname u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.nurseCellCode1='"+deptId+"'";
		 }
		 if(order.getTypeName()!=null&&order.getTypeName()!=""){
				hql+=" and u.typeCode='"+order.getTypeName()+"'";
		 }
		 if(order.getInpatientNo()!=null&&order.getInpatientNo()!=""){
				hql+=" and u.inpatientNo='"+order.getInpatientNo()+"'";
		 }
		 if(order.getMoStat()!=null&&order.getMoStat()!=null){
				hql+=" and u.moStat='"+order.getMoStat()+"'";
		 }
		 if(order.getItemName()!=null&&order.getItemName()!=""){
				hql+=" and u.itemName like '%"+order.getItemName()+"%'";
		 }
		 if(order.getEmcFlag()!=null&&order.getEmcFlag()!=null){
				hql+=" and u.emcFlag='"+order.getEmcFlag()+"'";
		 }
		 hql+=" and u.decmpsState=0  order by u.bedName ";
		return super.getTotal(hql);
	}

	//临时医嘱
	@Override
	public List<VidOrderBednameKs> getOrdersInterimInquiryPageks(String rows, String page, VidOrderBednameKs order,String deptId) {
		String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidOrderBednameKs u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.deptId='"+deptId+"'";
		 }
		 if(order.getTypeName()!=null&&order.getTypeName()!=""){
				hql+=" and u.typeCode='"+order.getTypeName()+"'";
		 }
		 if(order.getInpatientNo()!=null&&order.getInpatientNo()!=""){
				hql+=" and u.inpatientNo='"+order.getInpatientNo()+"'";
		 }
		 if(order.getMoStat()!=null&&order.getMoStat()!=null){
				hql+=" and u.moStat='"+order.getMoStat()+"'";
		 }
		 if(order.getItemName()!=null&&order.getItemName()!=""){
				hql+=" and u.itemName like '%"+order.getItemName()+"%'";
		 }
		 if(order.getEmcFlag()!=null&&order.getEmcFlag()!=null){
				hql+=" and u.emcFlag='"+order.getEmcFlag()+"'";
		 }
		 hql+=" and u.decmpsState=0  order by u.bedName ";
		 int start = Integer.parseInt(page==null?"1":page);
			int count = Integer.parseInt(rows==null?"20":rows);
			Query query = this.getSession().createQuery(hql);
		    List<VidOrderBednameKs> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		    if(list!=null&&list.size()>0){
				 return list;
			 }
		return new ArrayList<VidOrderBednameKs>();
	}
	//临时医嘱
	@Override
	public int getOrdersInterimInquiryTotalks(VidOrderBednameKs order,String deptId) {
		String hql="";
		 if(StringUtils.isNotBlank(deptId)){
			 hql=" from VidOrderBednameKs u where u.stop_flg=0 and u.del_flg=0";
			 hql+=" and u.deptId='"+deptId+"'";
		 }
		 if(order.getTypeName()!=null&&order.getTypeName()!=""){
				hql+=" and u.typeCode='"+order.getTypeName()+"'";
		 }
		 if(order.getInpatientNo()!=null&&order.getInpatientNo()!=""){
				hql+=" and u.inpatientNo='"+order.getInpatientNo()+"'";
		 }
		 if(order.getMoStat()!=null&&order.getMoStat()!=null){
				hql+=" and u.moStat='"+order.getMoStat()+"'";
		 }
		 if(order.getItemName()!=null&&order.getItemName()!=""){
				hql+=" and u.itemName like '%"+order.getItemName()+"%'";
		 }
		 if(order.getEmcFlag()!=null&&order.getEmcFlag()!=null){
				hql+=" and u.emcFlag='"+order.getEmcFlag()+"'";
		 }
		 hql+=" and u.decmpsState=0  order by u.bedName ";
		return super.getTotal(hql);
	}
	
	@Override
	public List<SysDepartment> querydeptCombobox() {
		String hql = "from SysDepartment where del_flg=0 and stop_flg=0 ";
		List<SysDepartment> deptList = super.find(hql, null);
		if(deptList!=null && deptList.size()>0){
			return deptList;
		}
		return new ArrayList<SysDepartment>();
	}

	/**
	 * 渲染科室名称
	 */
	public List<SysDepartment> deptMap() {
		String hql=" from SysDepartment u where u.stop_flg=0 and u.del_flg=0";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}

	/**
	 * 根据住院流水号查询该患者医嘱信息
	 */
	public List<InpatientOrder> getOrderByInpatientNo(String inpatientNo,String type) {
		String hql=" from InpatientOrder o where o.stop_flg=0 and o.del_flg=0 and o.inpatientNo='"+inpatientNo+"'";
		if("1".equals(type)){
			hql+=" and o.decmpsState=1";
		}
		else{
			hql+=" and o.decmpsState=0";
		}
		List<InpatientOrder> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientOrder>();
	}

	@Override
	public List<InpatientInfo> getInpatientViewByNo(String inpatientNo) {
		 String hql=" from InpatientInfo io where io.del_flg =0 and io.stop_flg=0 and io.inpatientNo in('"+inpatientNo+"')";
			List<InpatientInfo> list=super.find(hql, null);
			if(list!=null&&list.size()>0){
				return list;
			}
	       return new ArrayList<InpatientInfo>();
	}
	/**
	 * 根据医嘱类别id查询医嘱类别信息
	 * @author  zl
	 * @createDate： 2016年4月18日 下午3:09:08 
	 * @modifier zl
	 * @modifyDate：2016年4月18日 下午3:09:08
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public InpatientKind getkindById(String id) {
		String hql=" from InpatientKind d where d.stop_flg=0 and d.del_flg=0 and d.id='"+id+"'";
		List<InpatientKind> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new InpatientKind();
	}

	/**
	 * 根据科室类型获取取药药房
	 * @author  zl
	 * @createDate： 2016年4月19日 上午9:11:31 
	 * @modifier zl
	 * @modifyDate：2016年4月19日 上午9:11:31
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public List<SysDepartment> getPmDept() {
		String hql=" from SysDepartment d where d.stop_flg=0 and d.del_flg=0 and d.deptType='P'";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
       return new ArrayList<SysDepartment>();
	}
	/**
	 * 根据药品id和科室id查询药品拆分表
	 * @author  zl
	 * @createDate： 2016年4月19日 上午10:51:40 
	 * @modifier zl
	 * @modifyDate：2016年4月19日 上午10:51:40
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public DrugSplit getSplitByDeptIdAndDrugId(String... ob) {
		String hql=" from DrugSplit s where s.stop_flg=0 and s.del_flg=0 and s.deptCode='"+ob[0]+"' and s.drugCode='"+ob[1]+"'";
		List<DrugSplit> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new DrugSplit();
	}
	/**
	 * 更新库存维护表
	 * @author  zl
	 * @createDate： 2016年4月21日 上午9:34:12 
	 * @modifier zl
	 * @modifyDate：2016年4月21日 上午9:34:12
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void saveDrugPreoutstore(DrugPreoutstore preoutstore) {
		Session session = this.getSession();
		if(preoutstore!=null){
			session.saveOrUpdate(preoutstore);
			session.merge(preoutstore);
		}
	}
	/**
	 * 根据主医嘱id查询其附材医嘱
	 * @author  liujl
	 * @createDate： 2016年4月8日 下午6:01:06 
	 * @modifier liujl
	 * @modifyDate：2016年4月26日 下午18:01:06
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientOrder> getSubOrderByOrder(InpatientOrder order) {
		//根据主医嘱id查询其附材医嘱
		String combNo=order.getCombNo();
		String hql="from  InpatientOrder v where v.combNo = '"+combNo+"'"
//		 		+ "and v.inpatientNo in( select v1.inpatientNo from InpatientOrder v1 where v1.id='"+id+"')"
//		 		+ "and v.patientNo in (select v2.patientNo from InpatientOrder v2 where v2.id='"+id+"')"
		 	    + "and v.subtblFlag=1 and v.mainDrug=0 and v.stop_flg=0 and v.del_flg=0";
		List<InpatientOrder> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientOrder>();
	}

	@Override
	public List<InpatientOrder> getOrdersByIds(String orderIds) {
		//根据多个id查询医嘱数组信息
		String hql=" from InpatientOrder i where i.stop_flg =0 and i.del_flg =0 and i.id in ('"+orderIds.replaceAll(",", "','")+"')";
		List<InpatientOrder> list=super.find(hql, null);
		return list==null?new ArrayList<InpatientOrder>():list;
	}

	@Override
	public void confirmOrder(InpatientOrder order) {
		//XXX:审核医嘱  有两个接口都为审核医嘱  confirmOrder(),ExecOrder()
		String hql="update InpatientOrder o set o.moStat=?,o.confirmFlag=?, o.confirmDate=?, o.confirmUsercd=? where o.moOrder=? and o.moStat=?";
		this.excUpdateHql(hql, order.getMoStat(),order.getConfirmFlag(),order.getConfirmDate(),order.getConfirmUsercd(),order.getMoOrder(),0);
	}
  /**
   * 更新医嘱表，停止作废的医嘱：记录停止审核人、审核时间
   * @author  liujl
   * @createDate： 2016年4月21日 上午9:33:38 
   * @modifier liujl
   * @modifyDate：2016年4月21日 上午9:33:38
   * @param：  moOrder：医嘱流水号，moState：医嘱状态，opteTime：操作时间
   * @modifyRmk：  
   * @version 1.0
   */
	@Override
	public void stopInvalidOrder(InpatientOrder order) {
		String hql="update InpatientOrder i set i.dcConfirmDate=?,i.dcConfirmOper=?,i.dcConfirmFlag=?"
				+" where i.del_flg=0"
				+" and i.moOrder=?"
				+" and i.moStat=?";
		this.excUpdateHql(hql, order.getDcConfirmDate(),order.getDcConfirmOper(),order.getDcConfirmFlag(),order.getMoOrder(),3);
	}

	@Override
	public void updateExecTime(InpatientOrderInInterVO orderVO) {
		String hql="update InpatientOrder i set i.dateCurmodc=?,i.dateNxtmodc=?"
				+" where i.del_flg=0"
				+" and i.moOrder=?"
				+" and i.moStat=?";
		this.excUpdateHql(hql, orderVO.getDateCurmodc(),orderVO.getDateNxtmodc(),orderVO.getMoOrder(),orderVO.getMoStat());
	}

	@Override
	public List<InpatientOrder> getOrdersByPatientIds(String patientIds) {
		//根据多个id查询医嘱数组信息
		if(patientIds.indexOf(",")!=-1){
			patientIds=patientIds.replaceAll(",", "','");
		}
		String hql=" from InpatientOrder i where i.stop_flg =? and i.del_flg =? and i.inpatientNo in ('"+patientIds+"') and i.decmpsState=?";
		List<InpatientOrder> list=super.find(hql, 0,0,1);
		return list==null?new ArrayList<InpatientOrder>():list;
	}

	@Override
	public InpatientOrder getOrderByMoOrder(String moOrder) {
		InpatientOrder order=null; 
		String hql="from  InpatientOrder v where moOrder=? and moStat<?";
		 List<InpatientOrder> list=super.find(hql, moOrder,2);
		 if(list.size()>0){
			 order=list.get(0);
		 }
		return order;
	}

	/**
	 *
	 * 根据患者住院号及医嘱类型获得医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月9日 下午6:14:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param patIds
	 * @param advType
	 * @return：
	 *
	 */
	@Override
	public List<InpatientOrder> getOrderListByPatIds(String patNos,String advType) {
		String hql=" from InpatientOrder i where i.inpatientNo in ('"+patNos.replaceAll(",", "','")+"') and i.decmpsState = ? and i.stop_flg=0 and i.del_flg=0 and i.subtblFlag !=1 and ((i.moStat=0 and (i.confirmFlag != 1 or i.confirmFlag is null)) or (i.moStat=3 and (i.dcConfirmFlag != 1 or i.dcConfirmFlag is null))) ";
		List<InpatientOrder> list = this.getSession().createQuery(hql).setInteger(0, Integer.parseInt(advType)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**
	 *
	 * 根据id获得医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月9日 下午6:14:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param patIds
	 * @param advType
	 * @return：
	 *
	 */
	@Override
	public List<InpatientOrder> getOrderById(String id) {
		id = "'"+id.replaceAll(",", "','")+"'";
		String hql = "from InpatientOrder o where o.id in ("+id+") and o.stop_flg = 0 and o.del_flg = 0";
		List<InpatientOrder> list = this.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**
	 *
	 * 根据id统计附材数量
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月9日 下午6:14:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param patIds
	 * @param advType
	 * @return：
	 *
	 */
	@Override
	public Integer findPanelById(String combNo) {
		String hql = "select count(id) from InpatientOrder o where o.combNo = '"+combNo+"' and o.subtblFlag = 1 and o.stop_flg = 0 and o.del_flg = 0";
		Object sum = getSession().createQuery(hql).uniqueResult();
		return sum==null?0:Integer.parseInt(sum.toString());
	}

	/**
	 *
	 * 查询分解记录
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月16日 下午8:02:02 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public int getOrderTotal(String inpatientNos) {
		String hql = initHql(inpatientNos);
		return this.getTotal(hql);
	}

	/**
	 *
	 * 查询分解记录
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月16日 下午8:02:02 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public List<InpatientOrder> getOrderPage(String inpatientNos,String page,String rows) {
		String hql = initHql(inpatientNos);
		return this.getPage(hql, page, rows);
	}
	
	private String initHql(String inpatientNos) {
		inpatientNos = inpatientNos.replaceAll(",", "','");
		String hql=" from InpatientOrder i where i.stop_flg = 0 and i.del_flg = 0 and i.decmpsState = 1 and i.moStat in (1,2) and i.inpatientNo in ('"+inpatientNos+"') order by inpatientNo,moDate desc,combNo,sortId";
		return hql;
	}

	/**
	 * 通过住院流水号得患者信息
	 * @param patientIds
	 * @return
	 */
	@Override
	public InpatientInfo getInfosByInpatientNo(String patientNo) {
		String hql=" from InpatientInfo io where io.del_flg =0 and io.inpatientNo = '"+patientNo+"'";
		List<InpatientInfo> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return null;
	}

	@Override
	public List<InpatientOrder> getOrdersByIdsAndInPatientNo(String orderIds,
			String patNos) {
		//根据多个id查询医嘱数组信息
		String hql=" from InpatientOrder i where i.stop_flg =0 and i.del_flg =0 and i.id in (?) or i.patientNo in (?)";
		List<InpatientOrder> list=super.find(hql, orderIds,patNos);
		return list==null?new ArrayList<InpatientOrder>():list;
	}

	@Override
	public String getBedName(String bedId) {
		//根据多个id查询医嘱数组信息
		String bedName=null;
		String hql=" from BusinessHospitalbed h where h.id in (select bedId from InpatientBedinfo b where b.id =?)";
		List<BusinessHospitalbed> list=super.find(hql, bedId);
		if(list!=null&&list.size()>0){
			bedName=list.get(0).getBedName();
		}
		return bedName;
	}

	/**
	 * 根据医嘱id获得医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月25日 上午9:02:02 
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016年5月25日 上午9:02:02 
	 * @ModifyRmk：
	 * @version： 1.0：
	 *
	 */
	@Override
	public List<OrderInInterVO> queryOrderByIds(String orderIds) {
		orderIds = orderIds.replaceAll(",", "','");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT d.id as id,d.INPATIENT_NO as inpatientNo FROM T_INPATIENT_EXECDRUG d WHERE d.ID in ('"+orderIds+"') ");
		sb.append("UNION ALL ");
		sb.append("SELECT u.id as id,u.INPATIENT_NO as inpatientNo FROM T_INPATIENT_EXECUNDRUG u WHERE u.ID in ('"+orderIds+"')");
		Query query = this.getSession().createSQLQuery(sb.toString()).addScalar("id").addScalar("inpatientNo");
		List<OrderInInterVO> list = query.setResultTransformer(Transformers.aliasToBean(OrderInInterVO.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
}
