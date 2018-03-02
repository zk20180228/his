package cn.honry.inner.inpatient.nurseApply.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientShiftApply;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.nurseApply.dao.NurseApplyInInterDao;
import cn.honry.utils.DateUtils;

@Repository("nurseApplyInInterDao")
@SuppressWarnings({ "all" })
public class NurseApplyInInterDaoImpl extends HibernateEntityDao<InpatientShiftApply> implements NurseApplyInInterDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public SysDepartment queryState(String deptId) {
		String hql="from SysDepartment d where d.deptCode='"+deptId+"'";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<InpatientInfo> infoo(String id, String deptId, String type) {
		String hql="from InpatientInfo o where o.stop_flg=0 and o.del_flg=0 ";
		Date date=new java.util.Date();
		Date date2=DateUtils.addDay(date, -200);
		SimpleDateFormat simFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<InpatientInfo> infos=new ArrayList<InpatientInfo>();
	    if(StringUtils.isNotBlank(id)){
	    	/**
	    	 * 本区患者
	    	 */
			if("1".equals(id)){
				if("N".equals(type)){
					//hql+=" and o.inState='I' and o.bedId in (select b.id from InpatientBedinfo b where b.nurseCellCode='"+deptId+"')";
					hql+=" and o.inState='I' and o.nurseCellCode='"+deptId+"'";
				}else{
					hql+=" and o.inState='I' and o.deptCode in (select tdc.deptId "
							+ "from DepartmentContact tdc "
							+ "where tdc.id in (select dc.pardeptId "
							+ "from DepartmentContact dc "
							+ "where dc.deptId ='"+deptId+"'"
							+ "and dc.referenceType = '03'))";
				}
				infos=super.find(hql, null);
				for (InpatientInfo in : infos) {
					in.setExtFlag2("1");
				}
			}if("2".equals(id)){  //转入患者new
				hql="";
				String hql2="from InpatientInfo o where o.stop_flg=0 and o.del_flg=0 and o.babyFlag=0 ";
				hql2+="and o.inpatientNo in(select aa.inpatientNo from "
						+ "InpatientShiftApply aa where (aa.shiftState=2)  "
						+ "and aa.newDeptCode in(select dc.deptId from "
						+ "DepartmentContact dc where dc.del_flg = 0 " 
						+ "and dc.pardeptId = (select c.id from "
						+ "DepartmentContact c where c.del_flg = 0 "
						+ "and c.deptId = '"+deptId+"' and c.referenceType = '03')))";
				String hql1="from InpatientInfo o where o.stop_flg=0 and o.del_flg=0 and o.babyFlag=0 ";
				hql1+="and o.inpatientNo in(select aa.inpatientNo from "
						+ "InpatientShiftApply aa where (aa.shiftState=1)  "
						+ "and aa.newDeptCode in(select dc.deptId from "
						+ "DepartmentContact dc where dc.del_flg = 0 " 
						+ "and dc.pardeptId = (select c.id from "
						+ "DepartmentContact c where c.del_flg = 0 "
						+ "and c.deptId = '"+deptId+"' and c.referenceType = '03')))";
				List<InpatientInfo> infos1=null;
				List<InpatientInfo> infos2=null;
				infos1=super.find(hql2, null);
				infos2=super.find(hql1, null);
				for (InpatientInfo in : infos1) {
					in.setExtFlag1("2");
				}
				for (InpatientInfo in : infos2) {
					in.setExtFlag1("1");
				}
				infos.addAll(infos1);
				infos.addAll(infos2);
				for (InpatientInfo in : infos) {
					in.setExtFlag2("2");
				}
			}if("3".equals(id)){  //转出患者old
				hql+="and o.inpatientNo in(select aa.inpatientNo from "
						+ "InpatientShiftApply aa where aa.shiftState=1 "
						+ "and aa.oldDeptCode in(select dc.deptId from "
						+ "DepartmentContact dc where dc.del_flg = 0 "
						+ "and dc.pardeptId = (select c.id from "
						+ "DepartmentContact c where c.del_flg = 0 "
						+ "and c.deptId = '"+deptId+"' and c.referenceType = '03')))";
				infos=super.find(hql, null);
				for (InpatientInfo in : infos) {
					in.setExtFlag2("3");
				}
			}if("4".equals(id)){ //出院200天之内可以召回  and to_char(o.balanceDate,'yyyy-MM-dd HH:mm:ss') between '"+simFormat.format(date2)+"' and '"+simFormat.format(date)+"'
				//hql+=" and o.inState='B' and o.bedId in (select b.id from InpatientBedinfo b where b.nurseCellCode='"+deptId+"')";
				hql+=" and o.inState='B' and o.nurseCellCode='"+deptId+"'";
				infos=super.find(hql, null);
				for (InpatientInfo in : infos) {
					in.setExtFlag2("4");
				}
			}
	    }
		if(infos.size()>0){
			return infos;
		}
		return new ArrayList<InpatientInfo>();
	}
}
