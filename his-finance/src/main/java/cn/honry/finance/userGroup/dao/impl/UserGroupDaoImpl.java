package cn.honry.finance.userGroup.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.userGroup.dao.UserGroupDao;
import cn.honry.finance.userGroup.vo.EmployeeGroupVo;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("userGroupDao")
@SuppressWarnings({ "all" })
public class UserGroupDaoImpl extends HibernateEntityDao<FinanceUsergroup> implements UserGroupDao{
	
	private static final int List = 0;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	private EmployeeInInterDAO employeeDAO;
	@Override
	public List<FinanceUsergroup> queryUsergroup(FinanceUsergroup financeUsergroup) throws Exception {
		return whereJoin(financeUsergroup);
	}
	@Override
	public int getUsergroupCount(FinanceUsergroup financeUsergroup) throws Exception  {
		List<FinanceUsergroup> list = whereJoin(financeUsergroup);
		if(list==null){
			return 0;
		}
		return list.size();
	}
	private List<FinanceUsergroup> whereJoin(FinanceUsergroup financeUsergroup) throws Exception {
		List<FinanceUsergroup> financeUsergroupList = new ArrayList<FinanceUsergroup>(); 
		String sql="select d.groupName from FinanceUsergroup d where del_flg=0 and stop_flg=0 ";
		if(financeUsergroup!=null && StringUtils.isNotBlank(financeUsergroup.getGroupName())){
			sql+=" and (d.groupName  like '%"+financeUsergroup.getGroupName()+"%' or d.groupPinyin  like '%"+financeUsergroup.getGroupName()+"%' or d.groupWb  like '%"+financeUsergroup.getGroupName()+"%' or d.groupInputcode  like '%"+financeUsergroup.getGroupName()+"%')";
		}
		sql=sql+"group by d.groupName";
		Iterator it = getSession().createQuery(sql)
		.iterate();
		while(it.hasNext())
		{
			FinanceUsergroup fU = new FinanceUsergroup();
			String oc = it.next().toString();
			fU.setGroupName(oc);
			FinanceUsergroup fu=this.findGroup(oc);
			fU.setGroupPinyin(fu.getGroupPinyin());
			fU.setGroupWb(fu.getGroupWb());
			fU.setGroupInputcode(fu.getGroupInputcode());
			fU.setStackRemark(fu.getStackRemark());
			fU.setNo(fu.getNo());
			financeUsergroupList.add(fU);
		}
		return financeUsergroupList;
	}
	@Override
	public void delete(String ids)  throws Exception {
//		User user = WebUtils.getSessionUser();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr=format.format(new Date());
		StringBuilder sb = new StringBuilder();
		String idArg=this.stringUtils(ids);
		sb.append("update "+HisParameters.HISPARSCHEMAHISUSER+"T_FINANCE_USERGROUP t set DEL_FLG = 1 , DELETETIME=to_date('"+dateStr+"','yyyy/MM/dd')  where GROUP_ID in ("+idArg+") ");
		SQLQuery query = getSession().createSQLQuery(sb.toString());
		query.executeUpdate();
	}
	private String stringUtils(String ids){
		String idArg="";
		String[] idArgs=ids.split(",");
		if(idArgs.length>1){
			for(int i=0;i<idArgs.length;i++){
				idArg +="'"+idArgs[i]+"'"+",";
			}
			idArg=idArg.substring(0, idArg.length()-1);
		}else{
			ids=ids.substring(0, ids.length()-1);
			idArg="'"+ids+"'";
		}
		return idArg;
	}

	@Override
	public void save(String ids,FinanceUsergroup financeUsergroup)  throws Exception {
		List<FinanceUsergroup> financeUsergroupList=new ArrayList<FinanceUsergroup>();
		String[] idArg=ids.split(",");
		if(idArg.length>0){
			for (int i = 0; i < idArg.length; i++) {
				SysEmployee sysEmployee=employeeDAO.get(idArg[i]);
				FinanceUsergroup fu = new FinanceUsergroup();
				fu.setEmployee(sysEmployee);
				fu .setGroupInputcode(financeUsergroup.getGroupInputcode());
				fu.setGroupName(financeUsergroup.getGroupName());
				fu.setStackRemark(financeUsergroup.getStackRemark());
				fu.setNo(String.valueOf(this.findMaxNo()));
				fu.setId(null);
				if(StringUtils.isNotEmpty(fu.getGroupName())){
					String str = super.getSpellCode(fu.getGroupName());
					int index=str.indexOf("$");
					String pinyin=str.substring(0,index);
					String wb=str.substring(index+1);
					fu.setGroupPinyin(pinyin);
					fu.setGroupWb(wb);
				}
				fu.setCreateTime(new Date());
				fu.setCreateDept("");
				fu.setCreateUser("");
				super.save(fu);
			}
		}
	}

	@Override
	public List<SysEmployee> findGroupEmployee(String groupName)  throws Exception {
		String hql="from SysEmployee s where s.id in (select t.employee.id from FinanceUsergroup t where t.groupName='"+groupName+"' and t.del_flg=0 and t.stop_flg=0 )";
		List<SysEmployee> list =employeeDAO.findByObjectProperty(hql, null);
		if(list==null||list.size()<=0){
			return new ArrayList<SysEmployee>();
		}
		return list;
	}
	
	@Override
	public List<EmployeeGroupVo> queryGroupEmployee(String groupName)  throws Exception {
		String sql = "select a.EMPLOYEE_ID as id,a.EMPLOYEE_NAME as name,a.EMPLOYEE_JOBNO as jobNo,b.DEPT_NAME as deptName from "+HisParameters.HISPARSCHEMAHISUSER+"T_EMPLOYEE a LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT b on a.dept_Id = b.DEPT_ID and b.DEL_FLG=0 and b.STOP_FLG=0 where a.DEL_FLG=0 and a.STOP_FLG=0";
			sql +=" and a.EMPLOYEE_ID in (select c.EMPLOYEE_ID from T_FINANCE_USERGROUP c where c.GROUP_NAME = '"+groupName+"' and c.DEL_FLG=0 and c.STOP_FLG=0 )";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("id").addScalar("name").addScalar("deptName").addScalar("jobNo");
		List<EmployeeGroupVo> employeeGroupList = queryObject.setResultTransformer(Transformers.aliasToBean(EmployeeGroupVo.class)).list();
		if(employeeGroupList==null||employeeGroupList.size()<=0){
			return new ArrayList<EmployeeGroupVo>();
		}
		return employeeGroupList;
	}
	
	@Override
	public void update(String ids, FinanceUsergroup financeUsergroup,String oldGroupName)  throws Exception {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();//获取登录人
		SysDepartment  loginDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
		List<FinanceUsergroup> financeUsergroupList=new ArrayList<FinanceUsergroup>();
		String[] idArg=ids.split(",");
		if(idArg[0]!=null && idArg[0] !=""){
			for (int i = 0; i < idArg.length; i++) {
				SysEmployee sysEmployee=employeeDAO.get(idArg[i]);												
						FinanceUsergroup fu = new FinanceUsergroup();
						fu.setEmployee(sysEmployee);
						fu .setGroupInputcode(financeUsergroup.getGroupInputcode());
						fu.setGroupName(financeUsergroup.getGroupName());
						fu.setStackRemark(financeUsergroup.getStackRemark());
						fu.setNo(financeUsergroup.getNo());
						fu.setId(null);
						if(StringUtils.isNotEmpty(fu.getGroupName())){
							String str = super.getSpellCode(fu.getGroupName());
							int index=str.indexOf("$");
							String pinyin=str.substring(0,index);
							String wb=str.substring(index+1);
							fu.setGroupPinyin(pinyin);
							fu.setGroupWb(wb);
						}
						fu.setCreateTime(new Date());
						fu.setCreateUser(user.getAccount());
						fu.setCreateDept(loginDept.getDeptCode());
						super.save(fu);									
			}
		}else{
			List<SysEmployee> sysEmployeeList=null;
			if(oldGroupName!=null && oldGroupName !=""){
				sysEmployeeList=this.findGroupEmployee(oldGroupName);//已经存到数据库的
			}else{
				sysEmployeeList=this.findGroupEmployee(financeUsergroup.getGroupName());//已经存到数据库的
			}
			if(sysEmployeeList.size()>0){
				for (int i = 0; i < sysEmployeeList.size(); i++) {
					FinanceUsergroup fu = new FinanceUsergroup();
					fu.setEmployee(sysEmployeeList.get(i));
					fu .setGroupInputcode(financeUsergroup.getGroupInputcode());
					fu.setGroupName(financeUsergroup.getGroupName());
					fu.setStackRemark(financeUsergroup.getStackRemark());
					fu.setNo(financeUsergroup.getNo());
					fu.setId(financeUsergroup.getId());
					if(StringUtils.isNotEmpty(fu.getGroupName())){
						String str = super.getSpellCode(fu.getGroupName());
						int index=str.indexOf("$");
						String pinyin=str.substring(0,index);
						String wb=str.substring(index+1);
						fu.setGroupPinyin(pinyin);
						fu.setGroupWb(wb);
					}
					fu.setUpdateTime(new Date());
					fu.setUpdateUser("");
					super.save(fu);
					getSession().flush();
					getSession().clear();
				}
			}else{
				FinanceUsergroup fu = new FinanceUsergroup();
				fu .setGroupInputcode(financeUsergroup.getGroupInputcode());
				fu.setGroupName(financeUsergroup.getGroupName());
				fu.setStackRemark(financeUsergroup.getStackRemark());
				fu.setNo(financeUsergroup.getNo());
				fu.setId(financeUsergroup.getId());
				if(StringUtils.isNotEmpty(fu.getGroupName())){
					String str = super.getSpellCode(fu.getGroupName());
					int index=str.indexOf("$");
					String pinyin=str.substring(0,index);
					String wb=str.substring(index+1);
					fu.setGroupPinyin(pinyin);
					fu.setGroupWb(wb);
				}
				fu.setUpdateTime(new Date());
				fu.setUpdateUser("");
				super.save(fu);
			}
			
		}
		
	}
	@Override
	public void delete(String employeeId, String groupName)  throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr=format.format(new Date());
		StringBuilder sb = new StringBuilder();
		String idArg=this.stringUtils(employeeId);
		sb.append("update "+HisParameters.HISPARSCHEMAHISUSER+"T_FINANCE_USERGROUP t set DEL_FLG = 1 , DELETETIME=to_date('"+dateStr+"','yyyy/MM/dd')  where EMPLOYEE_ID in("+employeeId+") and GROUP_NAME='"+groupName+"'");
		SQLQuery query = getSession().createSQLQuery(sb.toString());
		query.executeUpdate();
		
	}
	@Override
	public FinanceUsergroup findGroup(String groupName) throws Exception  {
		String hql="from FinanceUsergroup where groupName='"+groupName+"' and del_flg=0 and stop_flg=0 ";
		List<FinanceUsergroup> list =this.findByObjectProperty(hql, null);
		if(list==null||list.size()<=0){
			return new FinanceUsergroup();
		}
		return list.get(0);
	}
	@Override
	public List<FinanceUsergroup> findGroupAll(String groupName) throws Exception  {
		String hql="from FinanceUsergroup where groupName='"+groupName+"' and del_flg=0 and stop_flg=0 ";
		List<FinanceUsergroup> list =this.findByObjectProperty(hql, null);
		if(list==null||list.size()<=0){
			return   new ArrayList<FinanceUsergroup>();
		}
		return list;
	}
	@Override
	public int findMaxNo()  throws Exception {
		String sql="select max(t.group_no) from "+HisParameters.HISPARSCHEMAHISUSER+"t_finance_usergroup t ";
		Query countQuery = getSession().createSQLQuery(sql.toString());
		Object maxValue =  (countQuery.uniqueResult());
		if(maxValue==null){
			return 1;
		}
		Integer retVal = Integer.parseInt(maxValue.toString());
		return retVal+1;
	}
	
}
