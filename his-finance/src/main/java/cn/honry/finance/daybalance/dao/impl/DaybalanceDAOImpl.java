package cn.honry.finance.daybalance.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.daybalance.dao.DaybalanceDAO;
import cn.honry.finance.daybalance.vo.AllPayTypeVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Repository("daybalanceDAO")
@SuppressWarnings({ "all" })
public class DaybalanceDAOImpl extends HibernateEntityDao<RegisterDaybalance> implements DaybalanceDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**  
	 *  
	 * @Description：  获得开始时间 (上一次的结算时间 ,如果没有返回当天的0点)
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Date getStartTime() {
		
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment sysDepartment = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		List<RegisterDaybalance> list=null;
		String hql=null;
		if(user!=null&&sysDepartment!=null){
			hql=" from RegisterDaybalance d  where d.del_flg=0  and d.stop_flg=0 and d.createUser = '"+user.getAccount()+"' and d.createDept = '"+sysDepartment.getDeptCode()+"'"
				+ " ORDER BY d.endTime DESC";
			list=super.findByObjectProperty(hql, null);
		}
		if(list!=null&&list.size()>0){
			return list.get(0).getEndTime();
		}else{
			hql = "Select Min(t.createtime) from t_register_main_now t where t.createuser = ? and t.stop_flg = 0 and t.del_flg = 0 and t.BALANCE_FLAG = 0 ";
			if(user!=null){
				SQLQuery sqlQuery = this.getSession().createSQLQuery(hql);
				sqlQuery.setParameter(0, user.getAccount());
				Date date = (Date) sqlQuery.uniqueResult();
				if(date != null){
					return date;
				}else{
					return DateUtils.parseDateY_M_D_H_M_S(DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+ " 00:00:00");
				}
			}else{
				return DateUtils.parseDateY_M_D_H_M_S(DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+ " 00:00:00");
			}
			
		}
		
		
	}

	@Override
	public String getNextBalanceNo() {
		String hql=" from RegisterDaybalance d ORDER BY d.balanceNo DESC ";
		List<RegisterDaybalance> list=super.findByObjectProperty(hql, null);
		if(list!=null&&list.size()>0){
			//获得序号
			String balanceNo = list.get(0).getBalanceNo();
			//获得字母部分
			String zm = balanceNo.replaceAll("[^a-zA-Z].+$", "");
			//获得数字部分
			String sz = balanceNo.replaceAll("[^0-9]", "");
			//获得数字部分的长度
			Integer szLen = sz.length();
			//计算返回值中数字部分　数字部分＋１
			Integer szNum = Integer.parseInt(sz)+1;
			//转换成字符
			String retNum = szNum.toString();
			//获得字符长度
			Integer retNumLen = retNum.length();
			//如果返回的字符长度小于原字符长度要进行拼接
			if(szLen-retNumLen>0){
				for (int i = 0; i < szLen-retNumLen; i++) {
					retNum = "0"+retNum;
				}
			}
			//返回拼接字符串
			return zm+retNum;
		}
		//如果表中没有数据初始化默认值
		return "A0000000000000000000000000000001";
	}

	@Override
	public String getSettlementByName(String stringzifei) {
		//TODO 结算类别
		return null;
	}
	@Override
	public AllPayTypeVo getInfoByTimeNow(Date startTime, Date endTime,String registrarId){
		StringBuffer sb = new StringBuffer();
		sb.append(" Select Sum(xjghsl) xjghsl,Sum(xjghze) xjghze,Sum(xjthsl) xjthsl,Sum(xjthze) xjthze,Sum(zyjghsl) zyjghsl,Sum(zyjghze) zyjghze,Sum(zyjthsl) zyjthsl,Sum(zyjthze) zyjthze,Sum(xykghsl) xykghsl,Sum(xykghze) xykghze,Sum(xykthsl) xykthsl,Sum(xykthze) xykthze, ");
		sb.append(" Sum(zpghsl) zpghsl,Sum(zpghze) zpghze,Sum(zpthsl) zpthsl,Sum(zpthze) zpthze,Sum(ylkghsl) ylkghsl,Sum(ylkghze) ylkghze,Sum(ylkthsl) ylkthsl,Sum(ylkthze) ylkthze,Sum(dfkghsl) dfkghsl,Sum(dfkghze) dfkghze,Sum(dfkthsl) dfkthsl,Sum(dfkthze) dfkthze, ");
		sb.append(" Sum(tcghsl) tcghsl,Sum(tcghze) tcghze,Sum(tcthsl) tcthsl,Sum(tcthze) tcthze,Sum(hpghsl) hpghsl,Sum(hpghze) hpghze,Sum(hpthsl) hpthsl,Sum(hpthze) hpthze,Sum(bxzhghsl) bxzhghsl,Sum(bxzhghze) bxzhghze,Sum(bxzhthsl) bxzhthsl,Sum(bxzhthze) bxzhthze, ");
		sb.append(" Sum(tjhzyhghsl) tjhzyhghsl,Sum(tjhzyhghze) tjhzyhghze,Sum(tjhzyhthsl) tjhzyhthsl,Sum(tjhzyhthze) tjhzyhthze,Sum(ynzhghsl) ynzhghsl,Sum(ynzhghze) ynzhghze,Sum(ynzhthsl) ynzhthsl,Sum(ynzhthze) ynzhthze From ( ");
		sb.append(" Select Decode(b.Mode_Code, 'CA', decode(t.trans_type,1,Count(1))) As xjghsl, ");
		sb.append(" Decode(b.Mode_Code, 'CA', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As xjghze, ");
		sb.append(" Decode(b.Mode_Code, 'CA', Decode(t.In_State, 2, decode(t.trans_type,2,Count(1)))) As xjthsl, ");
		sb.append(" Decode(b.Mode_Code, 'CA', Decode(t.In_State, 2, decode(t.trans_type,2,Sum(t.Reg_Fee)))) As xjthze, ");
		sb.append(" Decode(b.Mode_Code, 'AJ', decode(t.trans_type,1,Count(1))) As zyjghsl, ");
		sb.append(" Decode(b.Mode_Code, 'AJ', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As zyjghze, ");
		sb.append(" Decode(b.Mode_Code, 'AJ', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As zyjthsl, ");
		sb.append(" Decode(b.Mode_Code, 'AJ', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As zyjthze, ");
		sb.append(" Decode(b.Mode_Code, 'CD', decode(t.trans_type,1,Count(1))) As xykghsl, ");
		sb.append(" Decode(b.Mode_Code, 'CD', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As xykghze, ");
		sb.append(" Decode(b.Mode_Code, 'CD', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As xykthsl, ");
		sb.append(" Decode(b.Mode_Code, 'CD', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As xykthze, ");
		sb.append(" Decode(b.Mode_Code, 'CH',decode(t.trans_type,1,Count(1))) As zpghsl, ");
		sb.append(" Decode(b.Mode_Code, 'CH', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As zpghze, ");
		sb.append(" Decode(b.Mode_Code, 'CH', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As zpthsl, ");
		sb.append(" Decode(b.Mode_Code, 'CH', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As zpthze, ");
		sb.append(" Decode(b.Mode_Code, 'DB', decode(t.trans_type,1,Count(1))) As ylkghsl, ");
		sb.append(" Decode(b.Mode_Code, 'DB', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As ylkghze, ");
		sb.append(" Decode(b.Mode_Code, 'DB', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As ylkthsl, ");
		sb.append(" Decode(b.Mode_Code, 'DB', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As ylkthze, ");
		sb.append(" Decode(b.Mode_Code, 'HP', decode(t.trans_type,1,Count(1))) As dfkghsl, ");
		sb.append(" Decode(b.Mode_Code, 'HP', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As dfkghze, ");
		sb.append(" Decode(b.Mode_Code, 'HP', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As dfkthsl, ");
		sb.append(" Decode(b.Mode_Code, 'HP', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As dfkthze, ");
		sb.append(" Decode(b.Mode_Code, 'PB', decode(t.trans_type,1,Count(1))) As tcghsl, ");
		sb.append(" Decode(b.Mode_Code, 'PB', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As tcghze, ");
		sb.append(" Decode(b.Mode_Code, 'PB', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As tcthsl, ");
		sb.append(" Decode(b.Mode_Code, 'PB', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As tcthze, ");
		sb.append(" Decode(b.Mode_Code, 'PO', decode(t.trans_type,1,Count(1))) As hpghsl, ");
		sb.append(" Decode(b.Mode_Code, 'PO', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As hpghze, ");
		sb.append(" Decode(b.Mode_Code, 'PO', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As hpthsl, ");
		sb.append(" Decode(b.Mode_Code, 'PO', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As hpthze, ");
		sb.append(" Decode(b.Mode_Code, 'PS', decode(t.trans_type,1,Count(1))) As bxzhghsl, ");
		sb.append(" Decode(b.Mode_Code, 'PS', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As bxzhghze, ");
		sb.append(" Decode(b.Mode_Code, 'PS', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As bxzhthsl, ");
		sb.append(" Decode(b.Mode_Code, 'PS', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As bxzhthze, ");
		sb.append(" Decode(b.Mode_Code, 'TJ', decode(t.trans_type,1,Count(1))) As tjhzyhghsl, ");
		sb.append(" Decode(b.Mode_Code, 'TJ', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As tjhzyhghze, ");
		sb.append(" Decode(b.Mode_Code, 'TJ', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As tjhzyhthsl, ");
		sb.append(" Decode(b.Mode_Code, 'TJ', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As tjhzyhthze, ");
		sb.append(" Decode(b.Mode_Code, 'YS', decode(t.trans_type,1,Count(1))) As ynzhghsl, ");
		sb.append(" Decode(b.Mode_Code, 'YS', Decode(t.Ynregchrg, 1, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As ynzhghze, ");
		sb.append(" Decode(b.Mode_Code, 'YS', Decode(t.In_State, 2, decode(t.trans_type,1,Count(1)))) As ynzhthsl, ");
		sb.append(" Decode(b.Mode_Code, 'YS', Decode(t.In_State, 2, decode(t.trans_type,1,Sum(t.Reg_Fee)))) As ynzhthze ");
		sb.append(" From t_Register_Main_Now t Left Join t_Business_Paymode_now b On b.Invoice_No = t.Invoice_No ");
		sb.append(" Where t.Createuser = :registrarId  And t.Createtime >To_Date(:startTime, 'YYYY-MM-DD HH24:MI:SS') ");
		sb.append(" And b.trans_type = T.trans_type ");
		sb.append(" And t.Createtime <= To_Date(:endTime, 'YYYY-MM-DD HH24:MI:SS') And t.Del_Flg = 0 And t.Stop_Flg = 0 ");
		sb.append(" Group By b.Mode_Code,t.Ynregchrg,t.In_State,t.trans_type) ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("registrarId", registrarId);
		paramMap.put("startTime", DateUtils.formatDateY_M_D_H_M_S(startTime));
		paramMap.put("endTime", DateUtils.formatDateY_M_D_H_M_S(endTime));
		AllPayTypeVo allpay = namedParameterJdbcTemplate.queryForObject(sb.toString(), paramMap,new RowMapper<AllPayTypeVo>() {

			@Override
			public AllPayTypeVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				AllPayTypeVo vo = new AllPayTypeVo();
				vo.setCashRegAmount(rs.getDouble("xjghze"));
				vo.setCashRegNum(rs.getInt("xjghsl"));
				vo.setCashUnregAmount(rs.getDouble("xjthze"));
				vo.setCashUnregNum(rs.getInt("xjthsl"));
				vo.setTrunsferRegAmount(rs.getDouble("zyjghze"));
				vo.setTrunsferRegNum(rs.getInt("zyjghsl"));
				vo.setTrunsferUnregAmount(rs.getDouble("zyjthze"));
				vo.setTrunsferUnregNum(rs.getInt("zyjthsl"));
				vo.setCreditRegAmount(rs.getDouble("xykghze"));
				vo.setCreditRegNum(rs.getInt("xykghsl"));
				vo.setCreditUnregAmount(rs.getDouble("xykthze"));
				vo.setCreditUnregNum(rs.getInt("xykthsl"));
				vo.setCheckRegAmount(rs.getDouble("zpghze"));
				vo.setCheckRegNum(rs.getInt("zpghsl"));
				vo.setCheckUnregAmount(rs.getDouble("zpthze"));
				vo.setCheckUnregNum(rs.getInt("zpthsl"));
				vo.setUnionPayRegAmount(rs.getDouble("ylkghze"));
				vo.setUnionPayRegNum(rs.getInt("ylkghsl"));
				vo.setUnionPayUnregAmount(rs.getDouble("ylkthze"));
				vo.setUnionPayUnregNum(rs.getInt("ylkthsl"));
				vo.setAdvanceRegAmount(rs.getDouble("dfkghze"));
				vo.setAdvanceRegNum(rs.getInt("dfkghsl"));
				vo.setAdvanceUnregAmount(rs.getDouble("dfkthze"));
				vo.setAdvanceUnregNum(rs.getInt("dfkthsl"));
				vo.setBalanceRegAmount(rs.getDouble("tcghze"));
				vo.setBalanceRegNum(rs.getInt("tcghsl"));
				vo.setBalanceUnregAmount(rs.getDouble("tcthze"));
				vo.setBalanceUnregNum(rs.getInt("tcthsl"));
				vo.setDraftRegAmount(rs.getDouble("hpghze"));
				vo.setDraftRegNum(rs.getInt("hpghsl"));
				vo.setDraftUnregAmount(rs.getDouble("hpthze"));
				vo.setDraftUnregNum(rs.getInt("hpthsl"));
				vo.setInsuranceRegAmount(rs.getDouble("bxzhghze"));
				vo.setInsuranceRegNum(rs.getInt("bxzhghsl"));
				vo.setInsuranceUnregAmount(rs.getDouble("bxzhthze"));
				vo.setInsuranceUnregNum(rs.getInt("bxzhthsl"));
				vo.setTjhzyhRegAmount(rs.getDouble("tjhzyhghze"));
				vo.setTjhzyhRegNum(rs.getInt("tjhzyhghsl"));
				vo.setTjhzyhUnregAmount(rs.getDouble("tjhzyhthze"));
				vo.setTjhzyhUnregNum(rs.getInt("tjhzyhthsl"));
				vo.setInacountRegAmount(rs.getDouble("ynzhghze"));
				vo.setInacountRegNum(rs.getInt("ynzhghsl"));
				vo.setInacountUnregAmount(rs.getDouble("ynzhthze"));
				vo.setInacountUnregNum(rs.getInt("ynzhthsl"));
				return vo;
			}
		});
		return allpay;
	}

	@Override
	public int updateRegDaybalance(String balanceNo,String acount, Date startTime, Date endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append(" Update t_Register_Main_Now t Set t.Balance_Flag = 1,t.Balance_Opcd = :acount,t.Balance_Date = :nowTime,t.BALANCE_NO = :balanceNo ");
		sb.append(" Where t.Id In ( ");
		sb.append(" Select r.Id From t_Register_Main_Now r  ");
		sb.append(" Where r.Createuser = :acount And r.Createtime >To_Date(:startTime, 'YYYY-MM-DD HH24:MI:SS') ");
		sb.append(" And r.Createtime <=To_Date(:endTime, 'YYYY-MM-DD HH24:MI:SS')) ");
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("acount", acount);
		paramMap.put("startTime", DateUtils.formatDateY_M_D_H_M_S(startTime));
		paramMap.put("endTime", DateUtils.formatDateY_M_D_H_M_S(endTime));
		paramMap.put("balanceNo", balanceNo);
		paramMap.put("nowTime", new Date());
		int update = namedParameterJdbcTemplate.update(sb.toString(), paramMap);
		return update;
	}

	
}
