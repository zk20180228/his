package cn.honry.inner.outpatient.medicineList.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.inner.outpatient.medicineList.vo.DrugOrUNDrugVo;
import cn.honry.inner.outpatient.medicineList.vo.SpeNalVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
@Repository("medicineListInInterDAO")
@SuppressWarnings({ "all" })
public class MedicineListInInterDAOImpl extends HibernateEntityDao<OutpatientFeedetailNow> implements MedicineListInInterDAO{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private RedisUtil redis;
	@Override
	public List<OutpatientFeedetailNow> findByIds(String doctorId) {
		String hql = " from OutpatientFeedetailNow where id in ('"+doctorId+"') and del_flg=0 and stop_flg=0 ";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	@Override
	public RegistrationNow queryInfoByNo(String clinicCode) {
		String hql = " from RegistrationNow where clinicCode =? and del_flg=0 and stop_flg=0 and transType = 1 and inState = 0 ";
		List<RegistrationNow> infoList = super.find(hql, clinicCode);
		if(infoList==null||infoList.size()<=0){
			return new RegistrationNow();
		}
		return infoList.get(0);
	}

	@Override
	public OutpatientAccount getAccountByMedicalrecord(String midicalrecordId) {
		String hql = " from OutpatientAccount where medicalrecordId =? and del_flg=0 and stop_flg=0 ";
		List<OutpatientAccount> accountList = super.find(hql, midicalrecordId);
		if(accountList==null||accountList.size()<=0){
			return new OutpatientAccount();
		}
		return accountList.get(0);
	}

	@Override
	public List<OutpatientAccountrecord> queryAccountrecord(String midicalrecordId) {
		String hql = " from OutpatientAccountrecord where medicalrecordId =? and del_flg=0 and stop_flg=0 and trunc(operDate,'dd') = to_date('"+DateUtils.formatDateY_M_D(new Date())+"','yyyy-MM-dd')";
		List<OutpatientAccountrecord> accountrecordList = super.find(hql, midicalrecordId);
		if(accountrecordList==null||accountrecordList.size()<=0){
			return new ArrayList<OutpatientAccountrecord>();
		}
		return accountrecordList;
	}

	@Override
	public SysEmployee queryEmployee(String userId) {
		String hql = " from SysEmployee where jobNo =? and del_flg=0 and stop_flg=0 ";
		List<SysEmployee> employeeList = super.find(hql, userId);
		if(employeeList==null||employeeList.size()<=0){
			return new SysEmployee();
		}
		return employeeList.get(0);
	}

	@Override
	public List<SysDepartment> chargeImplementDepartmentList() {
		String hql = " from SysDepartment where del_flg=0 and stop_flg=0 and deptType = 'T' ";
		List<SysDepartment> departmentList = super.find(hql, null);
		if(departmentList==null||departmentList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return departmentList;
	}

	@Override
	public OutpatientFeedetail queryNOByclinicCode(String clinicCode) {
		String hql = " from OutpatientFeedetail where clinicCode = ?  and del_flg=0 and stop_flg=0  ";
		List<OutpatientFeedetail> outpatientFeedetailList = super.find(hql, clinicCode);
		if(outpatientFeedetailList==null||outpatientFeedetailList.size()<=0){
			return new OutpatientFeedetail();
		}
		return outpatientFeedetailList.get(0);
	}

	@Override
	public List<OutpatientRecipedetailNow> queryRecipedetailList(String recipeNo) {
		String hql = " from OutpatientRecipedetailNow where recipeNo in ('"+recipeNo+"')";
		List<OutpatientRecipedetailNow> recipedetailList = super.find(hql, null);
		if(recipedetailList==null||recipedetailList.size()<=0){
			return new ArrayList<OutpatientRecipedetailNow>();
		}
		return recipedetailList;
	}

	@Override
	public List<OutpatientFeedetail> queryFeedetailRecipeNo(String recipeNo) {
		String hql = " from OutpatientFeedetail where recipeNo = ? and drugFlag = '1' and del_flg=0 and stop_flg=0";
		List<OutpatientFeedetail> feedetailList = super.find(hql, recipeNo);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetail>();
		}
		return feedetailList;
	}

	@Override
	public SpeNalVo querySpeNalVoBy(String execDpcd, String id, Integer itemType) {
		SpeNalVo hget = null;
		try{
			hget = (SpeNalVo) redis.hget("specialterminal",execDpcd+"_"+id+"_"+itemType);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(hget!=null){
			return hget;
		}else{
			String hql = "select t.t_code as tCode,t.item_code as itemCode,t.item_type as itemType,a.dept_code as deptCode from  t_sto_terminal_spe t left join t_sto_terminal a on t.t_code = a.id where t.item_type =:itemType and t.item_code =:id and a.dept_code=:execDpcd and  a.close_flag = 0 "
					+ "    And t.DEL_FLG=0 And a.DEL_FLG=0 And t.STOP_FLG=0 And a.STOP_FLG=0  order by a.DRUG_QTY ";
			SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("tCode").addScalar("itemCode").addScalar("itemType",Hibernate.INTEGER).addScalar("deptCode");
			List<SpeNalVo> SpeNalVoList = queryObject.setResultTransformer(Transformers.aliasToBean(SpeNalVo.class)).setParameter("itemType", itemType).setParameter("id", id).setParameter("execDpcd", execDpcd).list();
			if(SpeNalVoList!=null&&SpeNalVoList.size()>0){
				try{
					redis.hset("specialterminal", execDpcd+"_"+id+"_"+itemType, SpeNalVoList.get(0));
				}catch(Exception e){
					e.printStackTrace();
				}
				return SpeNalVoList.get(0);
			}
			try{
				redis.hset("specialterminal", execDpcd+"_"+id+"_"+itemType, new SpeNalVo());
			}catch(Exception e){
				e.printStackTrace();
			}
			return new SpeNalVo();
		}
	}

	@Override
	public StoTerminal queryStoTerminalNo(String gettCode) {
		String hql = " from StoTerminal where id = ? and type=1  and  del_flg=0 and stop_flg=0  order by drugQty ";
		List<StoTerminal> stoTerminalList = super.find(hql, gettCode);
		if(stoTerminalList==null||stoTerminalList.size()<=0){
			return new StoTerminal();
		}
		return stoTerminalList.get(0);
	}

	@Override
	public StoTerminal queryStoTerminal(String execDpcd,String tjfs) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from StoTerminal where deptCode = ? and type=1  and  del_flg=0 and stop_flg=0 and property = 0 and closeFlag= 0 ");
		if("0".equals(tjfs)){//平均调剂方式
			sb.append(" order by sendQty ");
		}else{
			sb.append(" order by drugQty ");
		}
		List<StoTerminal> stoTerminalList = super.find(sb.toString(), execDpcd);
		if(stoTerminalList==null||stoTerminalList.size()<=0){
			return new StoTerminal();
		}
		return stoTerminalList.get(0);
	}

	@Override
	public OutpatientFeedetailNow queryDrugInfoList(String feedetailIdsArr) {
//		String hql = " from OutpatientFeedetail where id = '"+feedetailIdsArr+"' and del_flg=0 and stop_flg=0 ";
//		List<OutpatientFeedetail> feedetailList = super.find(hql, null);
		OutpatientFeedetailNow outpatientFeedetail = super.get(feedetailIdsArr);
//		if(feedetailList==null||feedetailList.size()<=0){
//			return new OutpatientFeedetail();
//		}
//		return feedetailList.get(0);
		return outpatientFeedetail;
	}

	@Override
	public DrugInfo queryDrugInfoById(String itemCode) {
		String hql = " from DrugInfo where code = ?  and del_flg=0 and stop_flg=0 ";
		List<DrugInfo> drugInfoList = super.find(hql, itemCode);
		if(drugInfoList==null||drugInfoList.size()<=0){
			return new DrugInfo();
		}
		return drugInfoList.get(0);
	}

	@Override
	public OutpatientFeedetailNow queryOutFeedetail(String id) {
//		String hql = " from OutpatientFeedetailNow where id = '"+id+"' and  del_flg=0 and stop_flg=0  ";
//		List<OutpatientFeedetailNow>  feedetailList1 = super.find(hql, null);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(OutpatientFeedetailNow.class);
		detachedCriteria.add(Restrictions.eq("id", id));
		detachedCriteria.add(Restrictions.eq("del_flg", 0));
		detachedCriteria.add(Restrictions.eq("stop_flg", 0));
		List<OutpatientFeedetailNow> feedetailList = (List<OutpatientFeedetailNow>) getHibernateTemplate().findByCriteria(detachedCriteria);
		if(feedetailList==null||feedetailList.size()<=0){
			return new OutpatientFeedetailNow();
		}
		return feedetailList.get(0);
	}

	@Override
	public void saveInvoiceFinance(String id, String invoiceNo,String invoiceType,String invoiceNoId) {
		if(StringUtils.isNotBlank(invoiceNoId)){
			String hql = "from FinanceInvoice  where id = ?";
			List<FinanceInvoice> find = super.find(hql, invoiceNoId);
			FinanceInvoice financeInvoice = find.get(0);
			String invoiceEndno = financeInvoice.getInvoiceEndno();
			String invoiceStartno = financeInvoice.getInvoiceStartno();
			String endNo = invoiceEndno.substring(1);
			int endNum = Integer.parseInt(endNo);
			int invoiceNum = Integer.parseInt(invoiceNo.substring(1));
			if(endNum-invoiceNum<1){//当已使用号大于等于终止号的时候
				String hql2 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where id = '"+invoiceNoId+"' and  invoiceType= '"+invoiceType+"' ";
				this.createQuery(hql2).setString(0,invoiceNo).setLong(1,-1 ).executeUpdate();
			}else {
				String hql3 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where id = '"+invoiceNoId+"' and  invoiceType= '"+invoiceType+"' ";
				this.createQuery(hql3).setString(0,invoiceNo).setLong(1,1 ).executeUpdate();
			}
		}else{
			//判断这个发票号是不是本号段最后一位
			String hql = "from FinanceInvoice  where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' order by invoiceStartno";
			List<FinanceInvoice> financeInvoiceList = super.find(hql, null);
			if(financeInvoiceList.size()>0){
				String invoiceEndno = financeInvoiceList.get(0).getInvoiceEndno();//得到终止号
				String invoiceStartno = financeInvoiceList.get(0).getInvoiceStartno();//得到开始号
				if(invoiceEndno.equals(invoiceNo)){//当等于终止号的时候相当最后一个
					String hql2 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' ";
					this.createQuery(hql2).setString(0,invoiceNo).setLong(1,-1 ).executeUpdate();
				}else if(invoiceStartno.equals(invoiceNo)){//当等于开始号的时候相当于第一个
					String hql3 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' ";
					this.createQuery(hql3).setString(0,invoiceNo).setLong(1,1 ).executeUpdate();
				}else{
					String hql4 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"'";
					this.createQuery(hql4).setString(0,invoiceNo).setLong(1,1 ).executeUpdate();
				}
			}
		}
	}
	

	@Override
	public Map<String, String> queryFinanceInvoiceNo(String id,String invoiceType) {
		//初始化发票号
		int invoiceNo = 0;
		//初始化发票字母后数字
		String invoiceNosq = "";
		//初始化发票号字母
		String invoiceNoas = "";
		//初始化发票号补0之后的号码
		String invoiceUsednoa ="";	
		//new一个map
		Map<String,String> map=new HashMap<String,String>();
		//根据领取人（员工ID）发票类型查询发票信息
		String hql = " from FinanceInvoice where invoiceGetperson= ? and invoiceType ='"+invoiceType+"'  and  del_flg=0 and stop_flg=0 and  invoiceUsestate in (0,1) order by invoiceStartno";
		List<FinanceInvoice> invoiceList = super.find(hql, id);
		if(invoiceList.size()>0){//判断是否已经领取发票
			//获得当前已使用号
			String invoiceNos = invoiceList.get(0).getInvoiceUsedno();
			//截取后面的数字
			invoiceNosq = invoiceNos.substring(1);
			//前面的字母
			invoiceNoas = invoiceNos.substring(0, 1);
			//转为int类型
			int invoiceNoa = Integer.parseInt(invoiceNosq);
			//加1是下一个要使用的还未使用的发票号
			invoiceNo = invoiceNoa+1;
			//获取补0的长度
			int lengths = 13-((invoiceNo+"").length());
			for(int a=0;a<lengths;a++){//循环补0
				invoiceUsednoa=invoiceUsednoa+"0";
			}
			//拼接发票号
			String invoiceUserNo = invoiceNoas + invoiceUsednoa + invoiceNo;
			map.put("resMsg", "success");
			map.put("resCode", invoiceUserNo);
		}else{
			map.put("resMsg", "error");
			map.put("resCode", "发票已用完请重新领取!");
		}
		return map;
	}
	
	@Override
	public Map<String, String> queryFinanceInvoiceNoByNum(String id, String invoiceType,int num){
		//初始化发票号
		int invoiceNo = 0;
		//初始化发票字母后数字
		String invoiceNosq = "";
		//初始化发票号字母
		String invoiceNoas = "";
		//初始化发票号补0之后的号码
		String invoiceUsednoa ="";	
		//发票号集合
		String invoiceNos = "";
		//当前领取发票数
		int a = 0;
		int nums = num;
		//new一个map
		Map<String,String> map=new HashMap<String,String>();
		//根据领取人（员工ID）发票类型查询发票信息
		String hql = " from FinanceInvoice where invoiceGetperson= ? and invoiceType = ?  and  del_flg=0 and stop_flg=0 and  invoiceUsestate in ('0','1') order by invoiceStartno ";
		List<FinanceInvoice> invoiceList = super.find(hql, id,invoiceType);
		if(invoiceList.size()>0){//判断是否已经领取发票
			for (FinanceInvoice f : invoiceList) {
				if(a==num){
					break;
				}
				//获得当前已使用号
				String invoiceUsedno = f.getInvoiceUsedno();
				//截取后面的数字
				invoiceNosq = invoiceUsedno.substring(1);
				//前面的字母
				invoiceNoas = invoiceUsedno.substring(0, 1);
				//转为int类型
				int invoiceNoa = Integer.parseInt(invoiceNosq);
				//终止发票号后面数字
				String invoiceEndno = f.getInvoiceEndno().substring(1);
				//转为int类型
				int invoiceEndNo = Integer.parseInt(invoiceEndno);
				//当前剩余发票数量
				int remain = invoiceEndNo-invoiceNoa;
				if(remain>=nums){
					for (int i = 1; i <= nums; i++) {
						a+=1;
						//加1是下一个要使用的还未使用的发票号
						invoiceNo = invoiceNoa+i;
						//获取补0的长度
						int lengths = 13-((invoiceNo+"").length());
						for(int b=0;b<lengths;b++){//循环补0
							invoiceUsednoa=invoiceUsednoa+"0";
						}
						//拼接发票号
						String invoiceUserNo = invoiceNoas + invoiceUsednoa + invoiceNo;
						invoiceUsednoa = "";
						map.put(invoiceUserNo, f.getId());//将发票号和该发票所在的组的id传回去，通过id修改已使用号
						if(i==nums){
							invoiceNos = invoiceNos + invoiceUserNo;
						}else{
							invoiceNos = invoiceNos + invoiceUserNo + ",";
						}
					}
					map.put("resMsg", "success");
					map.put("resCode", invoiceNos);
					return map;
				}else if(remain<nums&&nums-remain>0){
					for (int i = 1; i <= remain; i++) {
						a+=1;
						if(invoiceEndNo==invoiceNoa){
							break;
						}
						//加1是下一个要使用的还未使用的发票号
						invoiceNo = invoiceNoa+i;
						//获取补0的长度
						int lengths = 13-((invoiceNo+"").length());
						for(int b=0;b<lengths;b++){//循环补0
							invoiceUsednoa=invoiceUsednoa+"0";
						}
						//拼接发票号
						String invoiceUserNo = invoiceNoas + invoiceUsednoa + invoiceNo;
						invoiceUsednoa = "";
						map.put(invoiceUserNo, f.getId());//将发票号和该发票所在的组的id传回去，通过id修改已使用号
						if(i==nums){
							invoiceNos = invoiceNos + invoiceUserNo;
						}else{
							invoiceNos = invoiceNos + invoiceUserNo + ",";
						}
					}
					
					if(nums - remain<0){
						nums = nums;
					}else{
						nums = nums - remain;
					}
				}				
			}
			if(a==num){
				map.put("resMsg", "success");
				map.put("resCode", invoiceNos);
			}else{
				map.put("resMsg", "error");
				map.put("resCode", "发票不足、请领取发票!");
			}
		}else{
			map.put("resMsg", "error");
			map.put("resCode", "发票已用完请重新领取!");
		}
		return map;
	}

	@Override
	public MinfeeStatCode minfeeStatCodeByEncode(String encode) {
		String hql = " from MinfeeStatCode where minfeeCode = ?  and del_flg=0 and stop_flg=0 and reportCode = 'MZ01'";
		List<MinfeeStatCode> minfeeStatCodeList = super.find(hql, encode);
		if(minfeeStatCodeList==null||minfeeStatCodeList.size()<=0){
			return new MinfeeStatCode();
		}
		return minfeeStatCodeList.get(0);
	}
	
	public DrugOrUNDrugVo getDrugOrUNDrugVo (String itemCode,String drugFlag) {

		final StringBuffer s = new StringBuffer();
		if("1".equals(drugFlag)){//药品
			s.append("Select d.drug_code as drugOrUndrugCode,d.DRUG_ISTERMINALSUBMIT as issubmit ,0 as ispreorder,1 as isdrug,d.DRUG_CODE as equipmentNO  From t_drug_info d Where d.drug_code = '");
			s.append(itemCode).append("' and d.DEL_FLG=0 and d.STOP_FLG=0");
		}else{//非药品
			s.append("Select d.undrug_code as drugOrUndrugCode,d.UNDRUG_ISPREORDER as ispreorder ,d.UNDRUG_ISSUBMIT as issubmit,0 as isdrug,d.UNDRUG_EQUIPMENTNO as equipmentNO  From t_drug_undrug d Where d.undrug_code = '");
			s.append(itemCode).append("' and d.DEL_FLG=0 and d.STOP_FLG=0");
		}
		List<DrugOrUNDrugVo> list = (List<DrugOrUNDrugVo>)getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(s.toString()).addScalar("drugOrUndrugCode")
						.addScalar("ispreorder", Hibernate.INTEGER)
						.addScalar("issubmit",Hibernate.INTEGER)
						.addScalar("isdrug", Hibernate.INTEGER)
						.addScalar("equipmentNO");
				return query.setResultTransformer(Transformers.aliasToBean(DrugOrUNDrugVo.class)).list();
			}
		});
		if(list.size()>0&&list!=null){
			return list.get(0);
		}
			return new DrugOrUNDrugVo();
		
	}
	/**
	 * @Description 根据code获取发药终端
	 * @author  marongbin
	 * @createDate： 2016年11月24日 下午6:29:00 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public StoTerminal getStoTerminal(String code){
		String hql = " from StoTerminal where code = ? and del_flg=0 and stop_flg=0  ";
		List<StoTerminal> list = super.find(hql, code);
		if(list.size()>0&&list!=null){
			return list.get(0);
		}
			return new StoTerminal();
	}

	@Override
	public List<OutpatientFeedetailNow> getFeedetailByids(String feeID) {
		String hql = " from OutpatientFeedetailNow where id in('"+feeID+"') ";
		List<OutpatientFeedetailNow> list = super.find(hql,null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OutpatientFeedetailNow>();
	}

	@Override
	public BusinessContractunit queryCountByPaykindCode(String count) {
		String hql = " from BusinessContractunit where encode = '"+count+"' and del_flg=0 and stop_flg=0";
		List<BusinessContractunit> contractunit = super.find(hql, null);
		if(contractunit==null||contractunit.size()<=0){
			return new BusinessContractunit();
		}
		return contractunit.get(0);
	}

	@Override
	public FinanceInvoice getFinInvoiceById(String id) {
		String hql = "from FinanceInvoice where id = ?";
		List<FinanceInvoice> find = super.find(hql, id);
		if(find!=null&&find.size()>0){
			return find.get(0);
		}
		return null;
	}

	@Override
	public List<FinanceInvoice> getFinInvoice(String type, String code) {
		String hql = " from FinanceInvoice where invoiceGetperson= ? and invoiceType = ?  and  del_flg=0 and stop_flg=0 and  invoiceUsestate in ('0','1') order by invoiceStartno ";
		List<FinanceInvoice> list = super.find(hql, code,type);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	@Override
	public StoTerminal checkIshadUnsend(String clinicCode, String exeCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" Select s.Id as id,s.T_CODE as code,s.T_NAME as name  ");
		sb.append(" From t_Sto_Recipe_Now t, t_Sto_Terminal s ");
		sb.append(" Where t.Trans_Type = 1 And t.Recipe_State != 3 And t.Recipe_State != 4 And t.SEND_TERMINAL = s.t_Code ");
		sb.append(" And s.t_Type = 0 And s.Close_Flag = 0 And t.CLINIC_CODE = :clinicCode And s.DEPT_CODE = :exeCode");
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
		query.setParameter("clinicCode", clinicCode).setParameter("exeCode", exeCode);
		query.addScalar("id").addScalar("code").addScalar("name");
		List<StoTerminal> list = query.setResultTransformer(Transformers.aliasToBean(StoTerminal.class)).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateRecipedetialCharge(String receipeNO) {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		StringBuffer sb = new StringBuffer();
//		sb.append(" Update Set t.CHARGE_FLAG = 1,t.CHARGE_CODE = '"+account+"',t.CHARGE_DATE =  T_OUTPATIENT_RECIPEDETAIL_NOW t Where Recipe_No In ('') "
//				+ "And t.Del_Flg = 0 And t.Stop_Flg = 0 ");
		// TODO 带确定业务处理
		sb.append(" update OutpatientRecipedetailNow set chargeFlag = 1,chargeCode = ? ,chargeDate = ? where recipeNo in('"+receipeNO+"') and stop_flg=0 and del_flg=0");
		int excUpdateHql = this.excUpdateHql(sb.toString(), account,new Date());
	}

}
