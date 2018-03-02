package cn.honry.finance.invoice.dao.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.StaleStateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.invoice.dao.FinanceInvoiceDao;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("financeInvoiceDao")
@SuppressWarnings({ "all" })
public class FinanceInvoiceDaoImp  extends HibernateEntityDao<FinanceInvoice> implements FinanceInvoiceDao{
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	};
	
	@Autowired
	@Qualifier(value = "employeeInInterDAO")
	private EmployeeInInterDAO employeeDAO;
	
	/**
	 *  查询所有符合条件的数据
	 * @author sgt
	 * @date 2015-06-15
	 * @version 1.0
	 * @param financeInvoice 实体
	 * @return
	 */
	
	@Override
	public List<FinanceInvoice> queryFinanceInvoice(FinanceInvoice financeInvoice) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from FinanceInvoice where del_flg=0 and stop_flg=0 and invoiceUsestate !=2");
		this.whereJoin(financeInvoice,hql);
		hql.append(" order by createtime desc ");
		return super.getPage(hql.toString(), financeInvoice.getPage(), financeInvoice.getRows());
	}
	/**
	 *  动态拼接条件
	 * @author sgt
	 * @date 2015-06-15
	 * @version 1.0
	 * @return
	 */
	private StringBuilder whereJoin(FinanceInvoice financeInvoice, StringBuilder hql) {
		if(StringUtils.isNotBlank(financeInvoice.getInvoiceGetperson())){
			hql.append(" and invoiceGetperson= '"+financeInvoice.getInvoiceGetperson()+"'");
		}
		if(StringUtils.isNotBlank(financeInvoice.getInvoiceType())){
			hql.append(" and invoiceType= '"+financeInvoice.getInvoiceType()+"'");
		}
		return hql;
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author sgt
	 * @date 2015-06-15
	 * @version 1.0
	 * @param financeInvoice 实体
	 * @return
	 */
	@Override
	public int getFinanceInvoiceCount(FinanceInvoice financeInvoice) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from FinanceInvoice where del_Flg=0 ");
		this.whereJoin(financeInvoice,hql);
		hql.append(" order by createtime desc ");
		return super.getTotal(hql.toString());
	}
	
	public FinanceInvoiceStorage getByid(String id){
		String hql="from FinanceInvoiceStorage where del_flg=0 and id=?";
		List<FinanceInvoiceStorage> list = super.find(hql.toString(), id);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	/**
	 *  增加数据
	 * @author sgt
	 * @date 2015-06-15
	 * @version 1.0
	 * @param financeInvoice 实体
	 * @return
	 */
	@Override
	public void saveFinanceInvoice(FinanceInvoice financeInvoice,String num){
		//获取当前登录科室
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		//获取当前登录人员
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		//获取发票类型
		String invoiceType = financeInvoice.getInvoiceType();
		//获取领取人
		String invoiceGetperson = financeInvoice.getInvoiceGetperson();
		List<FinanceInvoiceStorage> fislist = this.findMaxStartNo(invoiceType);
		int parseInt=Integer.parseInt(num);
		FinanceInvoice fi = null;
		for (FinanceInvoiceStorage fis : fislist) {
			fi = new FinanceInvoice();
			// 获取入库表中发票开始号
			String startNos = fis.getInvoiceStartno();
			Integer lens = startNos.length();
			String startNoStrens = startNos.toString().substring(0, 1);
			String startNoStrs = startNos.toString().substring(1, startNos.toString().length());
			int useNos = Integer.parseInt(startNoStrs);
			// 获取入库表中发票终止号
			String endNos = fis.getInvoiceEndno();
			Integer lenes = endNos.length();
			String startNoStrenes = endNos.toString().substring(0, 1);
			String startNoStres = endNos.toString().substring(1, endNos.toString().length());
			int useNoe = Integer.parseInt(startNoStres);

			// 获取入库表中发票已用号
			String invoiceUsedno = fis.getInvoiceUsedno();
			Integer lenu = invoiceUsedno.length();
			String startNoStrenu = invoiceUsedno.toString().substring(0, 1);
			String startNoStru = invoiceUsedno.toString().substring(1, invoiceUsedno.toString().length());
			int useNou = Integer.parseInt(startNoStru);
			if (invoiceUsedno != null) {
				if(parseInt==0){
					break;
				}
				if(parseInt<(useNoe-useNou)){
					//获取开始号
					int i=useNou+1;
					String substr=i+"";
					int lengths=substr.toString().length();
					for (int j = 0; j < lenu-lengths-1; j++) {
						substr="0"+substr;
					}
					String strAlls=startNoStrenu+substr;
					//获取终止号
					int s=useNou+parseInt;
					String substrs=s+"";
					int length=substrs.toString().length();
					for (int j = 0; j < lenu-length-1; j++) {
						substrs="0"+substrs;
					}
					String strAllj=startNoStrenu+substrs;
					fi.setInvoiceUsestate(0);
					fi.setInvoiceGetperson(invoiceGetperson);
					fi.setInvoiceGettime(DateUtils.getCurrentTime());
					fi.setInvoiceType(invoiceType);
					fi.setInvoiceStartno(strAlls);
					fi.setInvoiceEndno(strAllj);
					fi.setInvoiceUsedno(invoiceUsedno);
					fi.setCreateDept(dept.getDeptCode());
					fi.setCreateUser(user.getAccount());
					fi.setCreateTime(DateUtils.getCurrentTime());
					super.save(fi);
					break;
				}else if(parseInt>=(useNoe-useNou)){
					int i=useNou+1;
					String substr=i+"";
					int lengths=substr.toString().length();
					for (int j = 0; j < lenu-lengths-1; j++) {
						substr="0"+substr;
					}
					String strAlls=startNoStrenu+substr;
					fi.setInvoiceUsestate(0);
					fi.setInvoiceGetperson(invoiceGetperson);
					fi.setInvoiceGettime(DateUtils.getCurrentTime());
					fi.setInvoiceType(invoiceType);
					fi.setInvoiceStartno(strAlls);
					fi.setInvoiceEndno(endNos);
					fi.setInvoiceUsedno(invoiceUsedno);
					fi.setCreateDept(dept.getDeptCode());
					fi.setCreateUser(user.getAccount());
					fi.setCreateTime(DateUtils.getCurrentTime());
					super.save(fi);
					parseInt=parseInt-(useNoe-useNou);
				}
			}else{
				if(parseInt<(useNoe-useNos)){
					int i=useNos+parseInt;
					String substr=i+"";
					int lengths=substr.toString().length();
					for (int j = 0; j < lenu-lengths-1; j++) {
						substr="0"+substr;
					}
					String strAlls=startNoStrenu+substr;
					fi.setInvoiceUsestate(0);
					fi.setInvoiceGetperson(invoiceGetperson);
					fi.setInvoiceGettime(DateUtils.getCurrentTime());
					fi.setInvoiceType(invoiceType);
					fi.setInvoiceStartno(startNos);
					fi.setInvoiceEndno(strAlls);
					fi.setInvoiceUsedno(invoiceUsedno);
					fi.setCreateDept(dept.getDeptCode());
					fi.setCreateUser(user.getAccount());
					fi.setCreateTime(DateUtils.getCurrentTime());
					super.save(fi);
					break;
				}else if(parseInt>=(useNoe-useNos)){
					int i=useNos;
					String substr=i+"";
					int lengths=substr.toString().length();
					for (int j = 0; j < lenu-lengths-1; j++) {
						substr="0"+substr;
					}
					String strAlls=startNoStrenu+substr;
					fi.setInvoiceUsestate(0);
					fi.setInvoiceGetperson(invoiceGetperson);
					fi.setInvoiceGettime(DateUtils.getCurrentTime());
					fi.setInvoiceType(invoiceType);
					fi.setInvoiceStartno(strAlls);
					fi.setInvoiceEndno(endNos);
					fi.setInvoiceUsedno(invoiceUsedno);
					fi.setCreateDept(dept.getDeptCode());
					fi.setCreateUser(user.getAccount());
					fi.setCreateTime(DateUtils.getCurrentTime());
					super.save(fi);
					parseInt=parseInt-(useNoe-useNos);
				}
			}
		}
	}
	
	public void updateFinanceInvoiceStorage(FinanceInvoice financeInvoice, String num) throws Exception{
			//获取当前登录的用户
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			//获取发票类型
			String invoiceType = financeInvoice.getInvoiceType();
			List<FinanceInvoiceStorage> fislist = this.findMaxStartNo(invoiceType);
			int parseInt=Integer.parseInt(num);
			for (FinanceInvoiceStorage fis : fislist) {
				// 获取入库表中发票开始号
				String startNos = fis.getInvoiceStartno();
				Integer lens = startNos.length();
				String startNoStrens = startNos.toString().substring(0, 1);
				String startNoStrs = startNos.toString().substring(1, startNos.toString().length());
				int useNos = Integer.parseInt(startNoStrs);
				// 获取入库表中发票终止号
				String endNos = fis.getInvoiceEndno();
				Integer lenes = endNos.length();
				String startNoStrenes = endNos.toString().substring(0, 1);
				String startNoStres = endNos.toString().substring(1, endNos.toString().length());
				int useNoe = Integer.parseInt(startNoStres);
	
				// 获取入库表中发票已用号
				String invoiceUsedno = fis.getInvoiceUsedno();
				Integer lenu = invoiceUsedno.length();
				String startNoStrenu = invoiceUsedno.toString().substring(0, 1);
				String startNoStru = invoiceUsedno.toString().substring(1, invoiceUsedno.toString().length());
				int useNou = Integer.parseInt(startNoStru);
				if (invoiceUsedno != null) {
					if(parseInt<(useNoe-useNou)){
						int i=useNou+parseInt;
						String substr=i+"";
						int lengths=substr.toString().length();
						for (int j = 0; j < lenu-lengths-1; j++) {
							substr="0"+substr;
						}
						String strAlls=startNoStrenu+substr;
						//修改已用号
						fis.setInvoiceUsedno(strAlls);
						//修改人
						fis.setUpdateUser(user.getAccount());
						//修改时间
						fis.setUpdateTime(DateUtils.getCurrentTime());
						super.update(fis);
						break;
					}else if(parseInt>=(useNoe-useNou)){
						parseInt=parseInt-(useNoe-useNou);
						fis.setInvoiceUsedno(fis.getInvoiceEndno());
						fis.setInvoiceUseState(1);
						//修改人
						fis.setUpdateUser(user.getAccount());
						//修改时间
						fis.setUpdateTime(DateUtils.getCurrentTime());
						super.update(fis);
					}
				}else{
					if(parseInt<(useNoe-useNou)){
						int i=useNou+parseInt;
						String substr=i+"";
						int lengths=substr.toString().length();
						for (int j = 0; j < lenu-lengths-1; j++) {
							substr="0"+substr;
						}
						String strAlla=startNoStrenu+substr;
						fis.setInvoiceUsedno(strAlla);
						//修改人
						fis.setUpdateUser(user.getAccount());
						//修改时间
						fis.setUpdateTime(DateUtils.getCurrentTime());
						super.update(fis);
						break;
					}else if(parseInt>=(useNoe-useNou)){
						parseInt=parseInt-(useNoe-useNou);
						fis.setInvoiceUsedno(fis.getInvoiceEndno());
						fis.setInvoiceUseState(1);
						//修改人
						fis.setUpdateUser(user.getAccount());
						//修改时间
						fis.setUpdateTime(DateUtils.getCurrentTime());
						super.update(fis);
					}
				}
			}
	}
	/**
	 *  人员树
	 * @author sgt
	 * @date 2015-06-16
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<Map<String, Object>> employeeTree() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT employee_id,employee_name,dept_id,'1' AS dept_path FROM "+HisParameters.HISPARSCHEMAHISUSER+"t_employee  WHERE dept_id IN ('3','1') ");
		sql.append(" UNION ");
		sql.append(" SELECT dept_id,dept_name,dept_parent,dept_path  FROM "+HisParameters.HISPARSCHEMAHISUSER+"t_department WHERE dept_id IN ('3','1') ORDER BY dept_path ");
		return this.getSession().createSQLQuery(sql.toString())
		.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	@Override
	public List<FinanceInvoice> findByGetPerson(String invoiceGetperson,
			String invoiceType) {
		String hql="from FinanceInvoice where invoiceType='"+invoiceType+"' and del_flg=0 and stop_flg=0 and invoiceUsestate!=2";
		if(!"null".equals(invoiceGetperson) ){
			hql =hql+" and invoiceGetperson='"+invoiceGetperson+"'";
		}
		List<FinanceInvoice> list =this.findByObjectProperty(hql, null);
		if(list==null||list.size()<=0){
			return   new ArrayList<FinanceInvoice>();
		}
		return list;
	}
	@Override
	public List<FinanceInvoiceStorage> findMaxStartNo(String invoiceType) {
		String hql="select t.id as id,t.invoice_type as invoiceType,t.invoice_code as invoiceCode,Min(t.Invoice_Startno) as invoiceStartno,t.Invoice_Endno as invoiceEndno,t.Invoice_Usedno as invoiceUsedno,"
				+" t.Invoice_Usestate as invoiceUseState,t.createUser as createUser,t.createTime as createTime, t.version as version from t_Finance_Instorage t Where t.invoice_type=:invoiceType and t.invoice_usestate!=1 and t.del_flg = 0 and t.stop_flg = 0 "
				+" Group By Invoice_Startno, Id,Invoice_Type,invoice_code,Invoice_Endno,Invoice_Usedno,Invoice_Usestate,createUser,createTime,t.version   order by  Invoice_Endno   ";
		SQLQuery queryObject=this.getSession().createSQLQuery(hql).addScalar("id").addScalar("invoiceType").addScalar("invoiceCode").addScalar("invoiceStartno")
				.addScalar("invoiceEndno").addScalar("invoiceUsedno").addScalar("createTime",Hibernate.DATE).addScalar("createUser").addScalar("invoiceUseState",Hibernate.INTEGER).addScalar("version", Hibernate.INTEGER);
		queryObject.setParameter("invoiceType", invoiceType);
		List<FinanceInvoiceStorage> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(FinanceInvoiceStorage.class)).list();
		if(bdl!=null&&bdl.size()>0){
			return bdl;
		}
		return new ArrayList<FinanceInvoiceStorage>();
	}
	/**
	 *  人员为挂号员的数据
	 * @author wj
	 * @date 2015-12-03
	 * @version 1.0
	 * @param Manufacturer
	 * @return
	 */
	@Override
	public List<SysEmployee> getAllEmp(SysEmployee sysEmployee) {
		String hql="FROM SysEmployee e WHERE e.del_flg = 0 AND e.stop_flg = 0 and (e.canCharge=1 or type=3) ";
		if(sysEmployee!=null && StringUtils.isNotBlank(sysEmployee.getName())){			
			hql+=" and (e.name like '%"+sysEmployee.getName()+"%' or e.pinyin like '%"+sysEmployee.getName()+"%' or e.wb like '%"+sysEmployee.getName()+"%' or e.inputCode like '%"+sysEmployee.getName()+"%' or e.jobNo like '%"+sysEmployee.getName()+"%')";
		}
		List<SysEmployee> empList = employeeDAO.findByObjectProperty(hql, null);
		if(empList!=null&&empList.size()>0){
			return empList;
		}
		return new ArrayList<SysEmployee>();
	}
	
	//查找财务发票
	@Override
	public Map<String,Object> findFinanceInvoice(String id, String invoiceType, int size) {
		int invoiceNo = 0;
		Map<String,Object> map=new HashMap<String,Object>();
		String hql = " from FinanceInvoice where invoiceGetperson= '"+id+"' and invoiceType ='"+invoiceType+"' and  del_flg=0 and stop_flg=0 and  invoiceUsestate in ('0','1') order by invoiceStartno";
		List<FinanceInvoice> invoiceList = super.find(hql, null);
		String invoiceNos = invoiceList.get(0).getInvoiceUsedno();//获得当前已使用号
		String invoiceNosq = invoiceNos.substring(1);//截取后面的数字
		String invoiceNoas = invoiceNos.substring(0, 1);//前面的字母
		int invoiceNoa = Integer.parseInt(invoiceNosq);//转为int类型
		String invoiceUserNo = "";
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= size; i++) {
			invoiceNo = invoiceNoa+i;//加1是下一个要使用的还未使用的发票号
			String invoiceNosh = invoiceNo+"";
			int lengths = 13-invoiceNosh.length();
			String invoiceUsednoa ="";
			for(int a=0;a<lengths;a++){
				invoiceUsednoa=invoiceUsednoa+"0";
			}
			invoiceUserNo = invoiceNoas + invoiceUsednoa + invoiceNosh;
			list.add(invoiceUserNo);
		}
		
		
		if(invoiceNo==0){
			map.put("resMsg", "error");
			map.put("resCode", "发票已用完请重新领取!");
		}else{
			map.put("resMsg", "success");
			map.put("resCode", list);
		}
		return map;
	}
	@Override
	public String sumfinance(String num, String invoiceType) {
		String hql="select t.id as id,t.invoice_type as invoiceType,Min(t.Invoice_Startno) as invoiceStartno,t.Invoice_Endno as invoiceEndno,t.Invoice_Usedno as invoiceUsedno,"
				+" t.Invoice_Usestate as invoiceUseState from t_Finance_Instorage t Where t.invoice_type='"+invoiceType+"' and t.invoice_usestate!=1 and t.del_flg = 0 and t.stop_flg = 0 "
				+" Group By Invoice_Startno, Id,Invoice_Type,Invoice_Endno,Invoice_Usedno,Invoice_Usestate   order by  Invoice_Startno   ";
		SQLQuery queryObject=this.getSession().createSQLQuery(hql).addScalar("id").addScalar("invoiceType").addScalar("invoiceStartno")
				.addScalar("invoiceEndno").addScalar("invoiceUsedno").addScalar("invoiceUseState",Hibernate.INTEGER);
		return null;
	}
	@Override
	public List<FinanceInvoiceStorage> findFinanceInvoiceStorageBytype(String invoiceType) {
		String hql = " from  FinanceInvoiceStorage where invoiceType = ? and invoiceUseState = 0 and stop_flg = 0 and del_flg = 0 order by invoiceEndno Asc";
		List<FinanceInvoiceStorage> list = (List<FinanceInvoiceStorage>) this.getHibernateTemplate().find(hql, invoiceType);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
}
