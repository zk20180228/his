package cn.honry.statistics.finance.pharmacyRefund.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.finance.pharmacyRefund.dao.RefundStatDao;
import cn.honry.statistics.finance.pharmacyRefund.vo.RefundVo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("refundStatDao")
@SuppressWarnings({ "all" })
public class RefundStatDaoImpl extends HibernateEntityDao<InpatientCancelitem> implements RefundStatDao {
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate; 
	
	// 把数据存入mongodb中
	public static final String TABLENAME = "MZYFTFTJ";//门诊药房退费统计
	
	
	@Override
	public List<RefundVo> query(List<String> invoiceInfoPartName,List<String> cancelPartName,String feeStatCode, String beginDate, String endDate,String page,String rows) {
		
		String sql = jointStr(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate);		
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		sql = "Select * From(Select t.*,Rownum rn From ( "+sql+") t Where Rownum <= "+start*count+") Where rn >"+(start-1)*count;
		List<RefundVo> list = namedParameterJdbcTemplate.query(sql,new RowMapper<RefundVo>() {
			@Override
			public RefundVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RefundVo vo = new RefundVo();
				vo.setConfirmDate(rs.getDate("confirmDate"));
				vo.setFeeStatCode(rs.getString("feeStatCode"));
				vo.setInvoiceNo(rs.getString("invoiceNo"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setRefundMoney(rs.getDouble("refundMoney"));
				vo.setSendWin(rs.getString("sendWin"));
				return vo;
			}
		});
		
		if(list != null && list.size() > 0){
			
			return list;
		}
		
		return new ArrayList<RefundVo>();
	}
	
	
	public String jointStr(List<String> invoiceInfoPartName,List<String> cancelPartName,String feeStatCode, String beginDate, String endDate){
		
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < invoiceInfoPartName.size(); i++) {
				if(i!=0){
					sb.append(" UNION ALL ");
				}
			for (int j = 0; j < cancelPartName.size(); j++) {
				if(j!=0){
					sb.append(" UNION ALL ");
				}
				sb.append(" Select t.Bill_No As invoiceNo,t.Name As patientName,t.Invo_Code As feeStatCode, t.CONFIRM_DATE as confirmDate, ");
				sb.append(" Sum(t.Sale_Price * t.Quantity * Nvl(t.Days, 1)) refundMoney,i.Drug_Window As sendWin ");
				sb.append(" from  ").append(cancelPartName.get(j)).append(" t, ").append(invoiceInfoPartName.get(i)).append(" i ");
				sb.append("  Where t.Apply_Flag = 1 And t.Drug_Flag = 1 And  t.Bill_No = i.Invoice_No ");
				if(StringUtils.isNotBlank(feeStatCode)){
					sb.append(" And t.Invo_Code In ("+feeStatCode+") ");
				}
				if(StringUtils.isNotBlank(beginDate)){
					sb.append(" and t.confirm_date >= to_date('"+beginDate+"','yyyy-MM-dd hh24:mi:ss') "); 
				}
				if(StringUtils.isNotBlank(endDate)){
					sb.append(" and t.confirm_date < to_date('"+endDate+"','yyyy-MM-dd hh24:mi:ss') "); 
				}
				sb.append(" Group By t.Bill_No, t.Invo_Code, t.Name,t.Confirm_Date,i.Drug_Window ");
			}
			
		}	
		
		return sb.toString();
	}

	@Override
	public int queryTotal(List<String> invoiceInfoPartName,List<String> cancelPartName,String feeStatCode, String beginDate, String endDate) {
		
		String sql = jointStr(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate);	
		sql = "select count(1)  from ( "+sql+" )";
		Integer total = namedParameterJdbcTemplate.getJdbcOperations().queryForObject(sql, java.lang.Integer.class);
		
		return total;
	}


	@Override
	public List<RefundVo> queryReport(List<String> invoiceInfoPartName,List<String> cancelPartName, 
	String feeStatCode, String beginDate,String endDate) {
		
		String sql = jointStr(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate);
		sql = "Select * From ( "+sql+") order by invoiceNo";
		
		List<RefundVo> list = namedParameterJdbcTemplate.query(sql,new RowMapper<RefundVo>() {
			@Override
			public RefundVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RefundVo vo = new RefundVo();
				vo.setConfirmDate(rs.getDate("confirmDate"));
				vo.setFeeStatCode(rs.getString("feeStatCode"));
				vo.setInvoiceNo(rs.getString("invoiceNo"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setRefundMoney(rs.getDouble("refundMoney"));
				vo.setSendWin(rs.getString("sendWin"));
				return vo;
			}
		});
		
		if(list != null && list.size() > 0){
			
			return list;
		}
		
		return new ArrayList<RefundVo>();
	}
	
	/**
	 * 
	 * @Description:将门诊药房退费统计数据从mongodb中读出
	 * @param feeStatCode 药品类别(发票科目类型)
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param page 当前页
	 * @param rows 每页显示的记录数
	 * @return
	 * List<RefundVO>
	 * @exception:
	 * @author:zhangkui
	 * @throws Exception 
	 * @time:2017年5月13日 上午10:38:04
	 */
	public List<RefundVo> queryByMongo(String feeStatCode, String beginDate, String endDate,String page,String rows) throws Exception{
		
		List<RefundVo> list = new ArrayList<RefundVo>();
		RefundVo r =null;
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		String[] feeStatCodeArray= feeStatCode.split(",");
		
		if(feeStatCodeArray!=null&&feeStatCodeArray.length==1){
			
			//查一种药品类型
			bdObject.append("feeStatCode",feeStatCode);
		}
		if(StringUtils.isNotBlank(beginDate)){
			
			//大于等于		
			bdObjectTimeS.put("confirmDate",new BasicDBObject("$gte",beginDate));
			condList.add(bdObjectTimeS);
		}
		if(StringUtils.isNotBlank(endDate)){
			
			//小于
			bdObjectTimeE.put("confirmDate",new BasicDBObject("$lt",endDate));
			condList.add(bdObjectTimeE);
		}
		if(StringUtils.isNotBlank(endDate)||StringUtils.isNotBlank(beginDate)){
			bdObject.put("$and", condList);
		}
		
		DBCursor cursor=null;
		if(rows==null||page==null){
			 cursor=new MongoBasicDao().findAlldataBySort(TABLENAME,bdObject,"confirmDate");
		}else{
			Integer rowsInt=Integer.valueOf(rows);
			Integer pageInt=Integer.valueOf(page);
			cursor=new MongoBasicDao().findAllDataSortBy(TABLENAME, "confirmDate", bdObject, rowsInt, pageInt);
		}
		while(cursor.hasNext()){
			r = new RefundVo();
			DBObject dbCursor = cursor.next();
			String  invoiceNo =(String) dbCursor.get("invoiceNo");
			String patientName = (String) dbCursor.get("patientName");
			feeStatCode=(String) dbCursor.get("feeStatCode");
			String confirmDateStr = (String) dbCursor.get("confirmDate");
			Date confirmDate = new SimpleDateFormat("yyyy-MM-dd").parse(confirmDateStr);
			String refundMoneyStr=(String)dbCursor.get("refundMoney");
			Double refundMoney=Double.valueOf(refundMoneyStr);
			String sendWin = (String) dbCursor.get("sendWin");
			String invocode=(String) dbCursor.get("invocode");
			
			r.setConfirmDate(confirmDate);
			r.setFeeStatCode(feeStatCode);
			r.setInvocode(invocode);
			r.setInvoiceNo(invoiceNo);
			r.setPatientName(patientName);
			r.setRefundMoney(refundMoney);
			r.setSendWin(sendWin);
			
			list.add(r);
		}
		
		return list;
	}

	/**
	 * 
	 * @Description:根据条件,在mongodb中查询总数量
	 * @param feeStatCode 药品种类
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 * int 总记录数
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年5月13日 下午3:01:42
	 */
	@Override
	public int queryByMongoCount(String feeStatCode, String beginDate,String endDate) {
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		String[] feeStatCodeArray= feeStatCode.split(",");
		
		if(feeStatCodeArray!=null&&feeStatCodeArray.length==1){
			
			//查一种药品类型
			bdObject.append("feeStatCode",feeStatCode);
		}
		if(StringUtils.isNotBlank(beginDate)){
			beginDate=beginDate+" 00:00:00";
			
			//大于等于		
			bdObjectTimeS.put("confirmDate",new BasicDBObject("$gte",beginDate));
			condList.add(bdObjectTimeS);
		}
		if(StringUtils.isNotBlank(endDate)){
			endDate=endDate+" 00:00:00";
			
			//小于
			bdObjectTimeE.put("confirmDate",new BasicDBObject("$lt",endDate));
			condList.add(bdObjectTimeE);
		}
		if(StringUtils.isNotBlank(endDate)||StringUtils.isNotBlank(beginDate)){
			bdObject.put("$and", condList);
		}
		
		return new MongoBasicDao().findAllCountBy(TABLENAME, bdObject).intValue();
	}
	
	
	
	
	
}
