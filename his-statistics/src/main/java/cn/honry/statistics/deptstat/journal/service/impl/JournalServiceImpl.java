package cn.honry.statistics.deptstat.journal.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.statistics.deptstat.journal.dao.JournalDao;
import cn.honry.statistics.deptstat.journal.service.JournalService;
import cn.honry.statistics.deptstat.journal.vo.JournalVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service("journalService")
@Transactional
@SuppressWarnings({ "all" })
public class JournalServiceImpl implements JournalService {

	/** 
	* @Fields journalDao : 住院日报DAO
	*/ 
	@Autowired
	@Qualifier(value = "journalDao")
	private JournalDao journalDao;
	/** 
	* @Fields mbDao : mongodbDAO 
	*/ 
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;

	@Override
	public List<JournalVo> queryJournals(String time, String dept) throws Exception {
		List<JournalVo> list = new ArrayList<JournalVo>();
		String[] depts = dept.split(",");
		if (depts.length == 0) {
			return list;
		}
		String sTime = time + " 00:00:00";
		String eTime = time + " 23:59:59";
		List<String> deptName = journalDao.queryDepts(dept);// 科室名称list
		List<Integer> inNum = journalDao.queryInOutNum(sTime, eTime, dept, 1);// 入院人数list
		List<Integer> exInNum = journalDao.queryInOutNum(sTime, eTime, dept, 2);// 转入人数list
		List<Integer> exOutNum = journalDao.queryInOutNum(sTime, eTime, dept, 3);// 转出人数list
		List<Integer> nowNum = journalDao.queryNowNum(eTime, dept);// 现有人数list
		String maxDateString = journalDao.getMaxOutDateFromOnLineInpateint();//住院主表在线表中的最大出院时间
		String tableName = "t_inpatient_info";
		if (StringUtils.isNotBlank(maxDateString)) {
			Date max = DateUtils.parseDateY_M_D_H_M_S(maxDateString);
			Date eDate = DateUtils.parseDateY_M_D_H_M_S(eTime);
			if (max.after(eDate)) {
				tableName = "t_inpatient_info_now";
			}
		}
		List<Integer> outNum = journalDao.queryOutNum(sTime, eTime, dept, tableName);// 出院人数list
		
		List<Integer> oldNum = new ArrayList<Integer>();// 原有人数list
		
		for (int i = 0; i < depts.length; i++) {// 原有人数 = 现有人数 + 出院人数 ＋ 转入人数 - 入院人数 - 转入人数
			Integer old = 0;
			if(nowNum != null && outNum != null && exOutNum != null && exInNum != null 
					&& nowNum.size() >= i && outNum.size() >= i && exOutNum.size() >= i && exInNum.size() >= i 
					&& nowNum.get(i) != null && outNum.get(i) != null && exOutNum.get(i) != null && exInNum.get(i) != null){
				old = nowNum.get(i) + outNum.get(i) + exOutNum.get(i) - inNum.get(i) - exInNum.get(i);
				if (old < 0) {
					old = 0;
				}
			}
			oldNum.add(old);
			
		}
		List<Integer> realBedNum = journalDao.queryBedNum(dept, 1);// 实占床位list
		List<Integer> openBedNum = journalDao.queryBedNum(dept, 2);// 开放床位list
		List<Double> rateOfBed = new ArrayList<Double>();// 病床使用率list
		for (int i = 0; i < depts.length; i++) {// 病床使用率 = 实占床位 / 开放床位
			Double out = 0D;
			if(realBedNum != null && openBedNum != null && realBedNum.size() >= i  && openBedNum.size() >= i && realBedNum.get(i) != 0 && openBedNum.get(i) != 0){
				BigDecimal bg = new BigDecimal(realBedNum.get(i).doubleValue())
				.divide(new BigDecimal(openBedNum.get(i).doubleValue()), 2,
						RoundingMode.UP);
				out = bg.doubleValue();
			}
			rateOfBed.add(out);
		}
		List<Integer> criticallyNum = journalDao.queryCriticallyOrGrateOneNum(
				eTime, dept, 1);// 危重list
		List<Integer> grateOneNum = journalDao.queryCriticallyOrGrateOneNum(
				eTime, dept, 2);// 一级护理list
		List<Integer> extraBedNum = journalDao.queryBedNum(dept, 3);// 加床list
		List<Integer> emptyBedNum = journalDao.queryBedNum(dept, 5);// 空床list
		for (int i = 0; i < depts.length; i++) {// 
			JournalVo vo = new JournalVo(deptName.get(i), oldNum.get(i),
					inNum.get(i), outNum.get(i), exInNum.get(i),
					exOutNum.get(i), nowNum.get(i), realBedNum.get(i), null,
					openBedNum.get(i), rateOfBed.get(i), criticallyNum.get(i),
					grateOneNum.get(i), extraBedNum.get(i), emptyBedNum.get(i));
			list.add(vo);
		}
		return list;
	}

	@Override
	public String getAllInpateintDeptCode() throws Exception {
		List<String> deptList = journalDao.getAllInpateintDeptCode();
		String depts = "";
		for (String dept : deptList) {
			if(StringUtils.isNotBlank(depts)){
				depts += "," + dept;
			}else {
				depts = dept;
			}
		}
		return depts;
	}

	@Override
	public boolean toMongoDB(String sTime, String eTime) throws Exception {
		Date sDate = DateUtils.parseDate(sTime, "yyyy-MM-dd");
		Date eDate = DateUtils.parseDate(eTime, "yyyy-MM-dd");
		boolean sign=false;
		do {
			sTime = DateUtils.formatDateY_M_D(sDate);
			String dept = this.getAllInpateintDeptCode();
			List<JournalVo> list = this.queryJournals(sTime, dept);
			if(list!=null && list.size()>0){
				for(JournalVo vo:list){
					Document document1=new Document();
					document1.append("time", sTime);
					document1.append("deptName", vo.getDeptName());
					Document document = new Document();
					document.append("deptName", vo.getDeptName());//科室名称
					document.append("oldNum", vo.getOldNum());//原有人数
					document.append("inNum", vo.getInNum());//入院人数
					document.append("outNum", vo.getOutNum());//出院人数
					document.append("exInNum", vo.getExInNum());//转入人数
					document.append("exOutNum", vo.getExOutNum());//转出人数
					document.append("nowNum", vo.getNowNum());//现有人数
					document.append("realBedNum", vo.getRealBedNum());//实占床位
					document.append("hangBedDays", vo.getHangBedDays());//挂床日期
					document.append("openBedNum", vo.getOpenBedNum());//开放床位
					document.append("rateOfBed", vo.getRateOfBed());//病床使用率
					document.append("criticallyNum", vo.getCriticallyNum());//危重病人
					document.append("grateOneNum", vo.getGrateOneNum());//一级护理
					document.append("extraBedNum", vo.getExtraBedNum());//加床
					document.append("emptyBedNum", vo.getEmptyBedNum());//空床
					new MongoBasicDao().update("ZYRBCOLLECTION", document1, document, true);
				}
			}
			sDate = DateUtils.addDay(sDate, 1);
		} while (sDate.before(eDate));
		sign=true;
		return sign;
	}

	@Override
	public List<JournalVo> fromMongoDB(String time, String dept) throws Exception {
		String tableName = "ZYRBCOLLECTION";//查询表
		if(StringUtils.isBlank(time) || StringUtils.isBlank(dept)){
			return null;
		}
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBList condList = new BasicDBList();
		bdObject.append("time", time);
		List<String> deptList = new ArrayList<String>();
		if (StringUtils.isBlank(dept)) {
			dept = this.getAllInpateintDeptCode();
		}
		deptList = journalDao.queryDepts(dept);
		for (String str : deptList) {
			condList.add(new BasicDBObject("deptName", str));
		}
		bdObject.put("$or", condList);
		DBCursor cursor = new MongoBasicDao().findAlldata(tableName, bdObject);
		DBObject dbCursor;
		List<JournalVo> list = new ArrayList<JournalVo>();
		
		while(cursor.hasNext()){
			dbCursor = cursor.next();
			String deptName = (String) dbCursor.get("deptName");//科室名称
			Integer oldNum = (Integer) dbCursor.get("oldNum");//原有人数
			Integer inNum = (Integer) dbCursor.get("inNum");//入院人数
			Integer outNum = (Integer) dbCursor.get("outNum");//出院人数
			Integer exInNum = (Integer) dbCursor.get("exInNum");//转入人数
			Integer exOutNum = (Integer) dbCursor.get("exOutNum");//转出人数
			Integer nowNum = (Integer) dbCursor.get("nowNum");//现有人数
			Integer realBedNum = (Integer) dbCursor.get("realBedNum");//实占床位
			Integer hangBedDays = (Integer) dbCursor.get("hangBedDays");//挂床日期
			Integer openBedNum = (Integer) dbCursor.get("openBedNum");//开放床位
			Double rateOfBed = (Double) dbCursor.get("rateOfBed");//病床使用率
			Integer criticallyNum = (Integer) dbCursor.get("criticallyNum");//危重病人
			Integer grateOneNum = (Integer) dbCursor.get("grateOneNum");//一级护理
			Integer extraBedNum = (Integer) dbCursor.get("extraBedNum");//加床
			Integer emptyBedNum = (Integer) dbCursor.get("emptyBedNum");//空床
			JournalVo vo = new JournalVo(deptName, oldNum, inNum, outNum, exInNum, exOutNum, nowNum, realBedNum, hangBedDays, openBedNum, rateOfBed, criticallyNum, grateOneNum, extraBedNum, emptyBedNum);
			list.add(vo);
		}
		return list;
	}

	@Override
	public JournalVo sumVo(List<JournalVo> list) {
		JournalVo sumVo = new JournalVo();
		String deptName = "合计";//科室名称
		Integer oldNum = 0;//原有人数
		Integer inNum = 0;//入院人数
		Integer outNum = 0;//出院人数
		Integer exInNum = 0;//转入人数
		Integer exOutNum = 0;//转出人数
		Integer nowNum = 0;//现有人数
		Integer realBedNum = 0;//实占床位
		Integer hangBedDays = 0;//挂床日期
		Integer openBedNum = 0;//开放床位
		Double rateOfBed = 0d;//病床使用率
		Integer criticallyNum = 0;//危重病人
		Integer grateOneNum = 0;//一级护理
		Integer extraBedNum = 0;//加床
		Integer emptyBedNum = 0;//空床
		if (list != null && list.size() > 0) {
			for (JournalVo vo : list) {
				oldNum += vo.getOldNum() == null ? 0 : vo.getOldNum();//原有人数
				inNum += vo.getInNum() == null ? 0 : vo.getInNum();//入院人数
				outNum += vo.getOutNum() == null ? 0 : vo.getOutNum();//出院人数
				exInNum += vo.getExInNum() == null ? 0 : vo.getExInNum();//转入人数
				exOutNum += vo.getExOutNum() == null ? 0 : vo.getExOutNum();//转出人数
				nowNum += vo.getNowNum() == null ? 0 : vo.getNowNum();//现有人数
				realBedNum += vo.getRealBedNum() == null ? 0 : vo.getRealBedNum();//实占床位
				/*FIXME 此字段无法计算临时放空
				hangBedDays += vo.getHangBedDays() == null ? 0 : vo.getHangBedDays();//挂床日期
				*/
				hangBedDays = null;//挂床日期
				openBedNum += vo.getOpenBedNum() == null ? 0 : vo.getOpenBedNum();//开放床位
				criticallyNum += vo.getCriticallyNum() == null ? 0 : vo.getCriticallyNum();//危重病人
				grateOneNum += vo.getGrateOneNum() == null ? 0 : vo.getGrateOneNum();//一级护理
				extraBedNum += vo.getExtraBedNum() == null ? 0 : vo.getExtraBedNum();//加床
				emptyBedNum += vo.getEmptyBedNum() == null ? 0 : vo.getEmptyBedNum();//空床
			}
		}
		sumVo = new JournalVo(deptName, oldNum, inNum, outNum, exInNum, exOutNum, nowNum, realBedNum, hangBedDays, openBedNum, rateOfBed, criticallyNum, grateOneNum, extraBedNum, emptyBedNum);
		rateOfBed = 0D;
		if(sumVo.getRealBedNum() != 0 && sumVo.getOpenBedNum() != 0 ){//病床使用率 = 实占床位 / 开放床位
			BigDecimal bg = new BigDecimal(sumVo.getRealBedNum().doubleValue())
			.divide(new BigDecimal(sumVo.getOpenBedNum().doubleValue()), 2,
					RoundingMode.UP);
			rateOfBed = bg.doubleValue();
		}
		sumVo.setRateOfBed(rateOfBed);
		return sumVo;
	}

	@Override
	public FileUtil export(List<JournalVo> journalVos, FileUtil fUtil) throws IOException {
		if(journalVos == null || journalVos.isEmpty()){
			return fUtil;
		}
		for (JournalVo vo : journalVos) {
			String record="";
			record += StringUtils.isBlank(vo.getDeptName()) ? "," : vo.getDeptName() + ",";//科室名称
			record += vo.getOldNum() == null ? "0," : vo.getOldNum() + ",";//原有人数
			record += vo.getInNum() == null ? "0," : vo.getInNum() + ",";//入院人数
			record += vo.getExInNum() == null ? "0," : vo.getExInNum() + ",";//转入人数
			record += vo.getExOutNum() == null ? "0," : vo.getExOutNum() + ",";//转出人数
			record += vo.getOutNum() == null ? "0," : vo.getOutNum() + ",";//出院人数
			record += vo.getNowNum() == null ? "0," : vo.getNowNum() + ",";//现有人数
			record += vo.getRealBedNum() == null ? "0," : vo.getRealBedNum() + ",";//实占床位
			record += vo.getHangBedDays() == null ? "," : vo.getHangBedDays() + ",";//挂床日数 FIXME 此字段暂时无法计算，可计算此处需要修改赋值
			record += vo.getOpenBedNum() == null ? "0," : vo.getOpenBedNum() + ",";//开放床位
			record += vo.getRateOfBed() == null ? "0," : vo.getRateOfBed() + ",";//病床使用率
			record += vo.getCriticallyNum() == null ? "0," : vo.getCriticallyNum() + ",";//危重病人
			record += vo.getGrateOneNum() == null ? "0," : vo.getGrateOneNum() + ",";//一级护理
			record += vo.getExtraBedNum() == null ? "0," : vo.getExtraBedNum() + ",";//加床
			record += vo.getEmptyBedNum() == null ? "0" : vo.getEmptyBedNum() + "";//空床
			fUtil.write(record);
		}
		return fUtil;
	}

	@Override
	public List<JournalVo> queryDayReport(String begin,
			String depts, String menuAlias, String campus) throws Exception {
		
		return journalDao.queryDayReport(begin,  depts, menuAlias, campus);
	}

}
