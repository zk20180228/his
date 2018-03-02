package cn.honry.statistics.sys.outpatientInvoice.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.sys.outpatientInvoice.dao.OutpatientInvoiceDao;
import cn.honry.statistics.sys.outpatientInvoice.vo.InvoiceInfoVo;
import cn.honry.statistics.sys.outpatientInvoice.vo.OutpatientStaVo;

@Repository("invoiceDAO")
@SuppressWarnings({ "all" })
public class OutpatientInvoiceDaoImpl extends HibernateEntityDao<OutpatientFeedetail> implements OutpatientInvoiceDao{
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate; 
	
	@Override
	public InvoiceInfoVo queryInvoiceInfoVo(String invoiceNo) {
		
		StringBuffer sb = new StringBuffer();
		String[] reg = {"T_REGISTER_MAIN","T_REGISTER_MAIN_NOW"};
		String[] invs = {"T_FINANCE_INVOICEINFO","T_FINANCE_INVOICEINFO_NOW"};
		int m = 0;
		for (int i = 0; i < reg.length; i++) {
			for (int j = 0; j < invs.length; j++) {
				if(m!=0){
					sb.append(" UNION ");
				}
				sb.append(" select p.patient_name as name, ");
				sb.append(" p.patient_birthday as age, ");
				sb.append(" i.CLINIC_CODE as no, ");
				sb.append(" d.dept_name as dept, ");
				sb.append(" i.REG_DATE as dates, ");
				sb.append(" t.tot_cost as sumMoney, ");
				sb.append(" e.employee_name as emp ");
				sb.append("from ").append(reg[i]).append(" i ");
				sb.append(" left join t_patient p on i.MEDICALRECORDID = p.MEDICALRECORD_ID ");
				sb.append(" left join t_department d on i.DEPT_CODE = d.DEPT_CODE ");
				sb.append(" left join t_employee e on i.DOCT_CODE = e.EMPLOYEE_CODE ");
				sb.append(" left join ").append(invs[i]).append(" t on i.CLINIC_CODE = t.CLINIC_CODE ");
				sb.append(" where t.invoice_no = :invoiceNo and t.trans_type= 1 and i.del_flg=0 and t.del_flg=0 and t.cancel_flag = 1 ");
				m++;
			}
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invoiceNo", invoiceNo);
		List<InvoiceInfoVo> invoiceInfoVoList = namedParameterJdbcTemplate.query(sb.toString(), paramMap,new RowMapper<InvoiceInfoVo>() {
			@Override
			public InvoiceInfoVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InvoiceInfoVo vo = new InvoiceInfoVo();
				vo.setAge(rs.getDate("age"));
				vo.setDates(rs.getDate("dates"));
				vo.setDept(rs.getString("dept"));
				vo.setEmp(rs.getString("emp"));
				vo.setName(rs.getString("name"));
				vo.setNo(rs.getString("no"));
				vo.setSumMoney(rs.getDouble("sumMoney"));
				vo.setUser(rs.getString("user"));
				return vo;
			}
		});
		
		if(invoiceInfoVoList!=null&&invoiceInfoVoList.size()>0){
			
			return invoiceInfoVoList.get(0);
		  }
		
		return new InvoiceInfoVo();
	}

	@Override
	public List<OutpatientStaVo> findOutpatient(String invoiceNo) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select f.drug_flag as drugFlag, ");
		sb.append(" s.fee_stat_name as feeName, ");
		sb.append(" s.fee_stat_code as feeCode, ");
		sb.append(" f.item_name as itemName, ");
		sb.append(" f.unit_price as unitPrice, ");
		sb.append(" f.confirm_num as qty, ");
		sb.append(" f.tot_cost as money, ");
		sb.append(" e.employee_name as users ");
		sb.append(" from t_outpatient_feedetail f ");
		sb.append(" left join  t_business_dictionary m on f.fee_code = m.code_encode and m.code_type='drugMinimumcost' ");
		sb.append(" left join  t_charge_minfeetostat s on m.code_encode = s.minfee_code ");
		sb.append(" left join t_employee e on e.user_id = f.fee_cpcd ");
		sb.append(" where f.invoice_no = :invoiceNo and s.report_code = 'MZ01'  and f.trans_type=1 and f.pay_flag = 1 and f.cancel_flag = 1 group by f.drug_flag,fee_stat_name,fee_stat_code,item_name,unit_price,confirm_num,tot_cost,employee_name ");
		sb.append(" UNION ");
		sb.append(" select f.drug_flag as drugFlag, ");
		sb.append(" s.fee_stat_name as feeName, ");
		sb.append(" s.fee_stat_code as feeCode, ");
		sb.append(" f.item_name as itemName, ");
		sb.append(" f.unit_price as unitPrice, ");
		sb.append(" f.confirm_num as qty, ");
		sb.append(" f.tot_cost as money, ");
		sb.append(" e.employee_name as users ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW f ");
		sb.append(" left join  t_business_dictionary m on f.fee_code = m.code_encode and m.code_type='drugMinimumcost' ");
		sb.append(" left join  t_charge_minfeetostat s on m.code_encode = s.minfee_code ");
		sb.append(" left join t_employee e on e.user_id = f.fee_cpcd ");
		sb.append(" where f.invoice_no = :invoiceNo and s.report_code = 'MZ01'  and f.trans_type=1 and f.pay_flag = 1 and f.cancel_flag = 1 group by f.drug_flag,fee_stat_name,fee_stat_code,item_name,unit_price,confirm_num,tot_cost,employee_name ");

		
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
				 query.setParameter("invoiceNo", invoiceNo);
				 query.addScalar("drugFlag",Hibernate.INTEGER).addScalar("feeName").addScalar("feeCode").addScalar("itemName").addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE).addScalar("money",Hibernate.DOUBLE).addScalar("users");
		
		 List<OutpatientStaVo> staVoList = query.setResultTransformer(Transformers.aliasToBean(OutpatientStaVo.class)).list();
		
		 if(staVoList!=null&&staVoList.size()>0){
			
			 return staVoList;
		  }
		
		 return new ArrayList<OutpatientStaVo>();
	}

	@Override
	public InvoiceInfoVo queryInvoiceInfoVoOld(String invoiceNo) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select p.patient_name as name, ");
		sb.append(" p.patient_birthday as age, ");
		sb.append(" i.CLINIC_CODE as no, ");
		sb.append(" d.dept_name as dept, ");
		sb.append(" i.REG_DATE as dates, ");
		sb.append(" t.tot_cost as sumMoney, ");
		sb.append(" e.employee_name as emp ");
		sb.append(" from T_REGISTER_MAIN i ");
		sb.append(" left join t_patient p on i.MEDICALRECORDID = p.MEDICALRECORD_ID ");
		sb.append(" left join t_department d on i.DEPT_CODE = d.DEPT_CODE ");
		sb.append(" left join t_employee e on i.DOCT_CODE = e.EMPLOYEE_CODE ");
		sb.append(" left join T_FINANCE_INVOICEINFO t on i.CLINIC_CODE = t.CLINIC_CODE ");
		sb.append(" where t.invoice_no = :invoiceNo and t.trans_type= 1 and i.del_flg=0 and t.del_flg=0 and t.cancel_flag = 1 ");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invoiceNo", invoiceNo);
		List<InvoiceInfoVo> invoiceInfoVoList = namedParameterJdbcTemplate.query(sb.toString(), paramMap,new RowMapper<InvoiceInfoVo>() {
			@Override
			public InvoiceInfoVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InvoiceInfoVo vo = new InvoiceInfoVo();
				vo.setAge(rs.getDate("age"));
				vo.setDates(rs.getDate("dates"));
				vo.setDept(rs.getString("dept"));
				vo.setEmp(rs.getString("emp"));
				vo.setName(rs.getString("name"));
				vo.setNo(rs.getString("no"));
				vo.setSumMoney(rs.getDouble("sumMoney"));
				vo.setUser(rs.getString("emp"));
				return vo;
			}
		});
		
		if(invoiceInfoVoList!=null&&invoiceInfoVoList.size()>0){
			  
			return invoiceInfoVoList.get(0);
		  }
		
		return null;
	}

	@Override
	public InvoiceInfoVo queryInvoiceInfoVoNew(String invoiceNo) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select p.patient_name as name, ");
		sb.append(" p.patient_birthday as age, ");
		sb.append(" i.CLINIC_CODE as no, ");
		sb.append(" d.dept_name as dept, ");
		sb.append(" i.REG_DATE as dates, ");
		sb.append(" t.tot_cost as sumMoney, ");
		sb.append(" e.employee_name as emp ");
		sb.append(" from T_REGISTER_MAIN_NOW i ");
		sb.append(" left join t_patient p on i.MEDICALRECORDID = p.MEDICALRECORD_ID ");
		sb.append(" left join t_department d on i.DEPT_CODE = d.DEPT_CODE ");
		sb.append(" left join t_employee e on i.DOCT_CODE = e.EMPLOYEE_CODE ");
		sb.append(" left join T_FINANCE_INVOICEINFO_NOW t on i.CLINIC_CODE = t.CLINIC_CODE ");
		sb.append(" where t.invoice_no = :invoiceNo and t.trans_type= 1 and i.del_flg=0 and t.del_flg=0 and t.cancel_flag = 1 ");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invoiceNo", invoiceNo);
		List<InvoiceInfoVo> invoiceInfoVoList = namedParameterJdbcTemplate.query(sb.toString(), paramMap,new RowMapper<InvoiceInfoVo>() {
			@Override
			public InvoiceInfoVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InvoiceInfoVo vo = new InvoiceInfoVo();
				vo.setAge(rs.getDate("age"));
				vo.setDates(rs.getDate("dates"));
				vo.setDept(rs.getString("dept"));
				vo.setEmp(rs.getString("emp"));
				vo.setName(rs.getString("name"));
				vo.setNo(rs.getString("no"));
				vo.setSumMoney(rs.getDouble("sumMoney"));
				vo.setUser(rs.getString("emp"));
				return vo;
			}
		});
		
		if(invoiceInfoVoList!=null&&invoiceInfoVoList.size()>0){
			
			return invoiceInfoVoList.get(0);
		  }
		return null;
	}

	@Override
	public List<OutpatientStaVo> findOutpatientOld(String invoiceNo) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select f.drug_flag as drugFlag, ");
		sb.append(" s.fee_stat_name as feeName, ");
		sb.append(" s.fee_stat_code as feeCode, ");
		sb.append(" f.item_name as itemName, ");
		sb.append(" f.unit_price as unitPrice, ");
		sb.append(" f.qty as qty, ");
		sb.append(" f.tot_cost as money, ");
		sb.append(" e.employee_name as users ");
		sb.append(" from t_outpatient_feedetail f ");
		sb.append(" left join  t_business_dictionary m on f.fee_code = m.code_encode and m.code_type='drugMinimumcost' ");
		sb.append(" left join  t_charge_minfeetostat s on m.code_encode = s.minfee_code ");
		sb.append(" left join t_employee e on e.user_id = f.fee_cpcd ");
		sb.append(" where f.invoice_no = :invoiceNo and s.report_code = 'MZ01'  and f.trans_type=1 and f.pay_flag = 1 and f.cancel_flag = 1 group by f.drug_flag,fee_stat_name,fee_stat_code,item_name,unit_price,qty,tot_cost,employee_name ");

		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("invoiceNo", invoiceNo);
		List<OutpatientStaVo> list = namedParameterJdbcTemplate.query(sb.toString(), paraMap,new RowMapper<OutpatientStaVo>() {
			@Override
			public OutpatientStaVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				OutpatientStaVo vo = new OutpatientStaVo();
				vo.setDrugFlag(rs.getInt("drugFlag"));
				vo.setFeeCode(rs.getString("feeCode"));
				vo.setFeeName(rs.getString("feeName"));
				vo.setItemName(rs.getString("itemName"));
				vo.setMoney(rs.getDouble("money"));
				vo.setQty(rs.getDouble("qty"));
				vo.setUnitPrice(rs.getDouble("unitPrice"));
				vo.setUsers(rs.getString("users"));
			
				return vo;
			}
		});
		
		return list;
	}

	@Override
	public List<OutpatientStaVo> findOutpatientNew(String invoiceNo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select f.drug_flag as drugFlag, ");
		sb.append(" s.fee_stat_name as feeName, ");
		sb.append(" s.fee_stat_code as feeCode, ");
		sb.append(" f.item_name as itemName, ");
		sb.append(" f.unit_price as unitPrice, ");
		sb.append(" f.qty  as qty, ");
		sb.append(" f.tot_cost as money, ");
		sb.append(" e.employee_name as users ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW f ");
		sb.append(" left join  t_business_dictionary m on f.fee_code = m.code_encode and m.code_type='drugMinimumcost' ");
		sb.append(" left join  t_charge_minfeetostat s on m.code_encode = s.minfee_code ");
		sb.append(" left join t_employee e on e.employee_jobno = f.fee_cpcd ");
		sb.append(" where f.invoice_no = :invoiceNo and s.report_code = 'MZ01'  and f.trans_type=1 and f.pay_flag = 1 and f.cancel_flag = 1 group by f.drug_flag,fee_stat_name,fee_stat_code,item_name,unit_price,qty,tot_cost,employee_name ");
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("invoiceNo", invoiceNo);
		List<OutpatientStaVo> list = namedParameterJdbcTemplate.query(sb.toString(), paraMap,new RowMapper<OutpatientStaVo>() {
			@Override
			public OutpatientStaVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				OutpatientStaVo vo = new OutpatientStaVo();
				vo.setDrugFlag(rs.getInt("drugFlag"));
				vo.setFeeCode(rs.getString("feeCode"));
				vo.setFeeName(rs.getString("feeName"));
				vo.setItemName(rs.getString("itemName"));
				vo.setMoney(rs.getDouble("money"));
				vo.setQty(rs.getDouble("qty"));
				vo.setUnitPrice(rs.getDouble("unitPrice"));
				vo.setUsers(rs.getString("users"));
			
				return vo;
			}
		});
		
		return list;
	}

}
