package cn.honry.inner.technical.pharmacy.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.technical.pharmacy.dao.PharmacyInInterDao;


@Repository("pharmacyInInterDao")
@SuppressWarnings({"all"})
public class PharmacyInInterDaoImpl extends HibernateEntityDao<StoTerminal> implements PharmacyInInterDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	private  DeptInInterDAO deptInInterDAO;

	@Override
	public List<StoTerminal> queryTerminalList(String type,String deptCode,StoTerminal stoTerminal,String page,String rows) {
		StringBuffer hql = new StringBuffer();
		String hqlString;
		if("2".equals(type)){
			 hql.append("from StoTerminal where stop_flg=0 and del_flg=0 ");
		}else{
			hql.append("from StoTerminal where type ="+type+" and deptCode='"+deptCode+"' and stop_flg=0 and del_flg=0 ");
		}
		if(stoTerminal!=null){
			if(StringUtils.isNotEmpty(stoTerminal.getId())){
				hql.append(" AND id ='"+stoTerminal.getId()+"'");
			}
			if(StringUtils.isNotEmpty(stoTerminal.getName())){
				String queryName = stoTerminal.getName();
				hql.append(" AND (code LIKE '%"+queryName+"%'"
						  + " OR name LIKE '%"+queryName.toUpperCase()+"%'");
				List<SysDepartment> deptList=deptInInterDAO.getDeptList(queryName);
				String deptIds="";
				if(deptList.size()>0){
					if(deptList.size()!=1){
						for (SysDepartment sysDepartment:deptList) {
							deptIds=deptIds+"'"+sysDepartment.getId()+"'"+",";
						}
						deptIds=deptIds.substring(0,deptIds.length()-1);
					}else{
						deptIds=deptIds+"'"+deptList.get(0).getId()+"'";
					}
					hql.append(" OR deptCode in( "+deptIds+")");
				}
				hql.append(")");
			}
		}
		hqlString = hql.toString();
		List<StoTerminal> bList = new ArrayList<StoTerminal>();
		if(StringUtils.isEmpty(page)){
			bList = super.find(hqlString, null);
		}else{
			bList = super.getPage(hqlString, page, rows);
		}
		return bList;
	}
	
	@Override
	public int queryTerminalCount(String type,String deptCode,StoTerminal stoTerminal) {
		String hql="from StoTerminal where type ="+type+"  and deptCode='"+deptCode+"' and stop_flg=0 and del_flg=0";
		return super.getTotal(hql);
	}


}
