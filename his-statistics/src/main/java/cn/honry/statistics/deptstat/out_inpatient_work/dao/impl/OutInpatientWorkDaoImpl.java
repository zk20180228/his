package cn.honry.statistics.deptstat.out_inpatient_work.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.deptstat.out_inpatient_work.dao.OutInpatientWorkDao;
import cn.honry.statistics.deptstat.out_inpatient_work.vo.OutInpatientWorkVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.NumberUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("outInpatientWorkDao")
@SuppressWarnings({"all"})
public class OutInpatientWorkDaoImpl extends HibernateEntityDao<OutInpatientWorkVo> implements OutInpatientWorkDao {

	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public static final String TABLENAME = "KSTJ_MZZYGZTQDBB";//门诊住院工作同期对比表
	
	/**
	 * 病床信息表:T_BUSINESS_HOSPITALBED---->没有分区表，没有在线表  
	 * 挂号主表:T_REGISTER_MAIN----->有分区表 ,有在线表 ,走分区
	 * 住院表：T_INPATIENT_INFO----->没有分区表，有在线表
	 * @throws Exception 
	 */
	public List<OutInpatientWorkVo> outInpatientWorkList(String Btime,String Etime,String areaCode,List ghList,List zyList) throws Exception {
		String beginTime01=null;//查询前一年的开始时间
		String endTime01=null;//查询前一年的结束时间
		String BLastDay =null;//查询前一年的天数，用于计算平均值
		String beginTime02=null;//查询年的开始时间
		String endTime02=null;//查询年的结束时间
		String ELastDay=null;//查询年的天数，用于计算平均值
		List<String> areaCodeList=null;
		if(StringUtils.isNotBlank(areaCode)){
			String[] areaCodes=areaCode.split(",");
			areaCodeList= Arrays.asList(areaCodes);
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
		//前一年的开始时间，结束时间
		int beforeOne=Integer.parseInt(Etime.substring(0, 4))-1;//查询年的前一年的int类型
		if(!StringUtils.isNotBlank(Btime)){//当Btime为空时，Etime不为空，是查询指定的某月
			beginTime01 = beforeOne+Etime.substring(4, 7)+"-01"+" 00:00:00";//如2016-05-01 00:00:00
			BLastDay    = getLastDay(beforeOne+Etime.substring(4, 7));
			endTime01=dateFormat.format(DateUtils.addDay(dateFormat.parse(beforeOne+Etime.substring(4, 7)+"-"+BLastDay), 1))+" 00:00:00";//如2016-06-01 00:00:00
			
			beginTime02 =Etime+"-01"+" 00:00:00";//如2017-05-01 00:00:00
			ELastDay=getLastDay(Etime);
		
			//结束时间加一天，因为sql对于结束时间是<
			endTime02=dateFormat.format(DateUtils.addDay(dateFormat.parse(Etime+"-"+ELastDay), 1))+" 00:00:00";//如2016-06-01 00:00:00
			
			
		}else{
			beginTime01 = beforeOne+Btime.substring(4, 7)+"-01"+" 00:00:00";//如2016-01-01 00:00:00
			String lastDay01=getLastDay(beforeOne+Etime.substring(4, 7));//查询的前一年结束时间的月份的天数
			endTime01=dateFormat.format(DateUtils.addDay(dateFormat.parse( beforeOne+Etime.substring(4, 7)+"-"+lastDay01), 1))+" 00:00:00";//如2016-06-01 00:00:00
			
			Date date01=dateFormat2.parse(beginTime01);
			Date date02= dateFormat2.parse(endTime01);
			Long Btimes=date02.getTime()-date01.getTime();
			Integer Bdays =(int) (Btimes/(86400000));//得到天数24 * 60 * 60 * 1000=86400000
			BLastDay=Bdays.toString();//得到查询的时间区间
			
			beginTime02 = Btime+"-01"+" 00:00:00";//如2017-01-01 00:00:00
			String lastDay02=getLastDay(Etime);//查询年的结束时间的月份的天数
			endTime02=dateFormat.format(DateUtils.addDay(dateFormat.parse(Etime+"-"+lastDay02), 1))+" 00:00:00";//如2017-06-01 00:00:00
			
			Date date03=dateFormat2.parse(beginTime02);
			Date date04= dateFormat2.parse(endTime02);
			Long Etimes=date04.getTime()-date03.getTime();
			Integer Edays =(int) (Etimes/(86400000));//得到天数24 * 60 * 60 * 1000=86400000
			ELastDay=Edays.toString();//得到查询的时间区间
		}
		
		
		
		
		//开始拼接sql
		/*******************************************************开始计算门诊人次******************************************************************/
		StringBuffer sb01 = new StringBuffer();
		
		// R.EMERGENCY_FLAG=0--->
		//这条sql查询普通门诊的人次
		sb01.append(" SELECT T.DEPTNAME AS DEPTNAME ,SUM(T.NUM) AS NUM  FROM ( ");
		for(int i=0;i<ghList.size();i++){
			if(i!=0){
				sb01.append(" UNION ALL ");
			}
			sb01.append(" SELECT ");
			sb01.append(" DECODE(R.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME,COUNT (1) AS NUM ");
			sb01.append(" FROM ");
			sb01.append(ghList.get(i)).append(" R ");
			sb01.append(" WHERE ");
			sb01.append(" R.DEL_FLG = 0 AND R.STOP_FLG = 0 ");
			if(StringUtils.isNotBlank(Etime)){
				sb01.append(" AND R.REG_DATE >= TO_DATE (:beginTime, 'YYYY-MM-DD HH24:MI:SS') ");
				sb01.append(" AND R.REG_DATE < TO_DATE (:endTime, 'YYYY-MM-DD HH24:MI:SS') ");
			}
			sb01.append(" AND R.EMERGENCY_FLAG=:flag ");
			if(StringUtils.isNotBlank(areaCode)){
				sb01.append(" AND R.AREA_CODE in (:areaCode) ");
			}
			sb01.append(" GROUP BY ");
			sb01.append(" R.AREA_CODE ");
		}
		sb01.append(" ) T GROUP BY ");
		sb01.append(" T.DEPTNAME ");
		
		//分别求出前一年和查询年份的人数，按院区分组
		HashMap map01 = new HashMap();//前一年的map
		HashMap map02 = new HashMap();//查询所在年的map
		if(StringUtils.isNotBlank(beginTime01)){
			map01.put("beginTime", beginTime01);
		}
		if(StringUtils.isNotBlank(endTime01)){
			map01.put("endTime", endTime01);
		}
		if(StringUtils.isNotBlank(areaCode)){
			map01.put("areaCode", areaCodeList);
		}
		map01.put("flag", 0);
		if(StringUtils.isNotBlank(beginTime02)){
			map02.put("beginTime", beginTime02);
		}
		if(StringUtils.isNotBlank(endTime02)){
			map02.put("endTime", endTime02);
		}
		if(StringUtils.isNotBlank(areaCode)){
			map02.put("areaCode", areaCodeList);
		}
		map02.put("flag", 0);
		
		//前一年的list(院区，人次)
		List<OutInpatientWorkVo> outInpatientWorkVoList01= namedParameterJdbcTemplate.query(sb01.toString(),map01,new RowMapper<OutInpatientWorkVo>() {
			@Override
			public OutInpatientWorkVo mapRow(ResultSet rs, int r)
					throws SQLException {
				OutInpatientWorkVo vo=new OutInpatientWorkVo();
				vo.setProjectName(rs.getString("DEPTNAME"));
				vo.setNum(rs.getString("NUM"));
				return vo;
			}
			
		});
		
		//查询所在年的list(院区，人次)
		List<OutInpatientWorkVo> outInpatientWorkVoList02= namedParameterJdbcTemplate.query(sb01.toString(),map02,new RowMapper<OutInpatientWorkVo>() {
			@Override
			public OutInpatientWorkVo mapRow(ResultSet rs, int r)
					throws SQLException {
				OutInpatientWorkVo vo=new OutInpatientWorkVo();
				vo.setProjectName(rs.getString("DEPTNAME"));
				vo.setNum(rs.getString("NUM"));
				return vo;
			}
			
		});
		
		//算出门诊人次
		List<OutInpatientWorkVo> MZList = new ArrayList<OutInpatientWorkVo>();//门诊list集合
		String MZProName="门诊人次";
		int num01=0;//前一年人次
		int num02=0;//查询年的人次
		if(outInpatientWorkVoList01!=null){
			for(OutInpatientWorkVo v:outInpatientWorkVoList01){
				num01+= Integer.parseInt(v.getNum());
			}
		}
		if(outInpatientWorkVoList02!=null){
			for(OutInpatientWorkVo v:outInpatientWorkVoList02){
				num02+=Integer.parseInt(v.getNum());
			}
		}
		OutInpatientWorkVo MZVo = new OutInpatientWorkVo();//门诊人次
		MZVo.setProjectName(MZProName);//项目
		if(num01==0){//说明16年没数据
			MZVo.setBeginNum("-");//前一年人次
			if(num02==0){//说明查询年没数据
				MZVo.setEndNum("-");//查询年也没数据
				MZVo.setIncreaseNum("-");//增减数没数据
				MZVo.setIncreasePercent("-");//增减%没数据
				MZList.add(MZVo);
			}else{
				MZVo.setEndNum(new Integer(num02).toString());//查询年的人次(有人次)
				MZVo.setIncreaseNum("-");//增减数没数据
				MZVo.setIncreasePercent("-");//增减%没数据
				MZList.add(MZVo);
			}
		}else{
			MZVo.setBeginNum(new Integer(num01).toString());//前一年人次
			if(num02==0){//说明查询年没数据
				MZVo.setEndNum("-");//查询年也没数据
				MZVo.setIncreaseNum("-");//增减数没数据
				MZVo.setIncreasePercent("-");//增减%没数据
				MZList.add(MZVo);
			}else{
				MZVo.setEndNum(new Integer(num02).toString());//查询年的人次(有人次)
				String ct= new Integer(num02-num01).toString();

				MZVo.setIncreaseNum(ct);//增减数有数据
				//一些列转类型，为了防止计算三百分比时丢失精度
				Double mid= new Integer(num02-num01).doubleValue();
				Double dNum01=new Integer(num01).doubleValue();
				Double e= new Double((mid/dNum01)*100);
				MZVo.setIncreasePercent(NumberUtil.init().format(e, 1));

				MZList.add(MZVo);
			}
		}
		/****************************************************开始计算门诊实际工作日*******************************************************************/
		List<OutInpatientWorkVo> MZWList = new ArrayList<OutInpatientWorkVo>();//门诊实际工作日
		OutInpatientWorkVo workVo01 = new OutInpatientWorkVo();
		workVo01.setProjectName("门诊实际工作日");//项目
		workVo01.setBeginNum(BLastDay);//前一年
		workVo01.setEndNum(ELastDay);//查询年
		
		//转型计算
		Double cDay = Double.parseDouble(ELastDay)-Double.parseDouble(BLastDay);
		workVo01.setIncreaseNum(NumberUtil.init().format(cDay,0));//增减数
		Double bDay = Double.parseDouble(BLastDay);
		Double percent =(cDay/bDay)*100;
		workVo01.setIncreasePercent(NumberUtil.init().format(percent,1));

		MZWList.add(workVo01);
		
		//分别求出各个院区的门诊人次
		if(outInpatientWorkVoList01!=null&&outInpatientWorkVoList02!=null){
				for(OutInpatientWorkVo vo02:outInpatientWorkVoList02){
					OutInpatientWorkVo vo03 = new OutInpatientWorkVo();
					if(outInpatientWorkVoList01.size()>0){
						flag:for(OutInpatientWorkVo vo01:outInpatientWorkVoList01){
							if(vo02.getProjectName()!=null){
								if(vo02.getProjectName().equals(vo01.getProjectName())){
									vo03.setProjectName(vo02.getProjectName());
									vo03.setBeginNum(vo01.getNum());
									vo03.setEndNum(vo02.getNum());
									Double c= Double.parseDouble(vo02.getNum())-Double.parseDouble(vo01.getNum());
									Double n =Double.parseDouble(vo01.getNum());
									Double p =(c/n)*100;
									vo03.setIncreaseNum(NumberUtil.init().format(c, 0));//增减数
									vo03.setIncreasePercent(NumberUtil.init().format(p, 1));
									
									vo01.setFlag("ok");//ok随便定义的，用于记录该值已被计算,代表在outInpatientWorkVoList01中也出现了outInpatientWorkVoList02的元素
									break flag;//满足条件的就停止循环
								}else{//不满足，设置默认值
									vo03.setProjectName(vo02.getProjectName());
									vo03.setBeginNum("-");
									vo03.setEndNum(vo02.getNum());
									vo03.setIncreaseNum("-");
									vo03.setIncreasePercent("-");
								}
							}
						}
					}else{//当outInpatientWorkVoList01的长度为0
						vo03.setProjectName(vo02.getProjectName());
						vo03.setBeginNum("-");
						vo03.setEndNum(vo02.getNum());
						vo03.setIncreaseNum("-");
						vo03.setIncreasePercent("-");
					}
					OutInpatientWorkVo workVo02 = new OutInpatientWorkVo();
					workVo02.setProjectName(vo02.getProjectName());//院区名
					workVo02.setBeginNum(BLastDay);//前年工作日
					workVo02.setEndNum(ELastDay);//查询年工作日
					//转型计算
					Double cp = Double.parseDouble(ELastDay)-Double.parseDouble(BLastDay);
					Double bp = Double.parseDouble(BLastDay);
					Double pp =(cp/bp)*100;
					workVo02.setIncreaseNum(NumberUtil.init().format(cp, 0));//增减数
					workVo02.setIncreasePercent(NumberUtil.init().format(pp, 1));
					
					MZWList.add(workVo02);
					
					MZList.add(vo03);
				}
			
		}
		if(outInpatientWorkVoList01.size()>0){//遍历不在list02的院区
			for(OutInpatientWorkVo vo01:outInpatientWorkVoList01){
				if("ok".equals(vo01.getFlag())){//flag是oK,已经被计算过
					vo01.setFlag(null);//设置为原来的状态，不影响后边的使用
				}else{
					OutInpatientWorkVo vo03 = new OutInpatientWorkVo();
					vo03.setProjectName(vo01.getProjectName());
					vo03.setBeginNum(vo01.getNum());
					vo03.setEndNum("-");
					vo03.setIncreaseNum("-");
					vo03.setIncreasePercent("-");
					MZList.add(vo03);
	    /*******************************************************结束计算门诊人次******************************************************************/
					OutInpatientWorkVo workVo02 = new OutInpatientWorkVo();
					workVo02.setProjectName(vo01.getProjectName());//院区名
					workVo02.setBeginNum(BLastDay);//前年工作日
					workVo02.setEndNum(ELastDay);//查询年工作日
					//转型计算
					Double cp = Double.parseDouble(ELastDay)-Double.parseDouble(BLastDay);
					Double bp = Double.parseDouble(BLastDay);
					Double pp =(cp/bp)*100;
					workVo02.setIncreaseNum(NumberUtil.init().format(cp, 0));//增减数
					workVo02.setIncreasePercent(NumberUtil.init().format(pp, 1));
					MZWList.add(workVo02);
	    /*******************************************************结束计算门诊实际工作日******************************************************************/
				}
			}
			
		}

		
		/*******************************************************开始计算急诊人次******************************************************************/
		// 分别求出前一年和查询年份的人数，按院区分组
		HashMap map03 = new HashMap();// 前一年的map
		HashMap map04 = new HashMap();// 查询所在年的map
		if (StringUtils.isNotBlank(beginTime01)) {
			map03.put("beginTime", beginTime01);
		}
		if (StringUtils.isNotBlank(endTime01)) {
			map03.put("endTime", endTime01);
		}
		if (StringUtils.isNotBlank(areaCode)) {
			map03.put("areaCode", areaCodeList);
		}
		map03.put("flag", 1);
		if (StringUtils.isNotBlank(beginTime02)) {
			map04.put("beginTime", beginTime02);
		}
		if (StringUtils.isNotBlank(endTime02)) {
			map04.put("endTime", endTime02);
		}
		if (StringUtils.isNotBlank(areaCode)) {
			map04.put("areaCode", areaCodeList);
		}
		map04.put("flag", 1);
		//分别求出前一年和查询年份的人数，按院区分组
		
		//前一年的list(院区，人次)
		List<OutInpatientWorkVo> outInpatientWorkVoList03 = namedParameterJdbcTemplate.query(sb01.toString(), map03,new RowMapper<OutInpatientWorkVo>() {
							@Override
							public OutInpatientWorkVo mapRow(ResultSet rs, int r)
									throws SQLException {
								OutInpatientWorkVo vo = new OutInpatientWorkVo();
								vo.setProjectName(rs.getString("DEPTNAME"));
								vo.setNum(rs.getString("NUM"));
								return vo;
							}

						});
		
		// 查询所在年的list(院区，人次)
		List<OutInpatientWorkVo> outInpatientWorkVoList04= namedParameterJdbcTemplate.query(sb01.toString(), map04,new RowMapper<OutInpatientWorkVo>() {
							@Override
							public OutInpatientWorkVo mapRow(ResultSet rs, int r)
									throws SQLException {
								OutInpatientWorkVo vo = new OutInpatientWorkVo();
								vo.setProjectName(rs.getString("DEPTNAME"));
								vo.setNum(rs.getString("NUM"));
								return vo;
							}

						});
		
		//算出急诊人次
		List<OutInpatientWorkVo> JZList = new ArrayList<OutInpatientWorkVo>();//急诊人次List
		String JZProName="急诊人次";
		int num03=0;//前一年人次
		int num04=0;//查询年的人次
		if(outInpatientWorkVoList03!=null){
			for(OutInpatientWorkVo v:outInpatientWorkVoList03){
				num03+=Integer.parseInt(v.getNum());
			}
		}
		if(outInpatientWorkVoList04!=null){
			for(OutInpatientWorkVo v:outInpatientWorkVoList04){
				num04+=Integer.parseInt(v.getNum());
			}
		}
		OutInpatientWorkVo JZVo = new OutInpatientWorkVo();//急诊人次
		JZVo.setProjectName(JZProName);//项目
		if(num03==0){//说明16年没数据
			JZVo.setBeginNum("-");//前一年人次
			if(num04==0){//说明查询年没数据
				JZVo.setEndNum("-");//查询年也没数据
				JZVo.setIncreaseNum("-");//增减数没数据
				JZVo.setIncreasePercent("-");//增减%没数据
				JZList.add(JZVo);
			}else{
				JZVo.setEndNum(new Integer(num04).toString());//查询年的人次(有人次)
				JZVo.setIncreaseNum("-");//增减数没数据
				JZVo.setIncreasePercent("-");//增减%没数据
				JZList.add(JZVo);
			}
		}else{
			JZVo.setBeginNum(new Integer(num03).toString());//前一年人次
			if(num04==0){//说明查询年没数据
				JZVo.setEndNum("-");//查询年也没数据
				JZVo.setIncreaseNum("-");//增减数没数据
				JZVo.setIncreasePercent("-");//增减%没数据
				JZList.add(JZVo);
			}else{
				JZVo.setEndNum(new Integer(num04).toString());//查询年的人次(有人次)
				JZVo.setIncreaseNum(NumberUtil.init().format(new Integer(num04-num03).doubleValue(), 0));//增减数有数据
				//一些列转类型，为了防止计算三百分比时丢失精度
				Double mid= new Integer(num04-num03).doubleValue();
				Double dnum03=new Integer(num03).doubleValue();
				JZVo.setIncreasePercent(NumberUtil.init().format(new Double((mid/dnum03)*100), 1));//增减%有数据
				JZList.add(JZVo);
			}
		}
		
		
		//根据以上，算出各个院区的急诊人次
		if(outInpatientWorkVoList03!=null&&outInpatientWorkVoList04!=null){
						for(OutInpatientWorkVo vo04:outInpatientWorkVoList04){
							OutInpatientWorkVo vo05 = new OutInpatientWorkVo();
							if(outInpatientWorkVoList03.size()>0){
								flag:for(OutInpatientWorkVo vo03:outInpatientWorkVoList03){
									if(vo04.getProjectName()!=null){
										if(vo04.getProjectName().equals(vo03.getProjectName())){
											vo05.setProjectName(vo04.getProjectName());
											vo05.setBeginNum(vo03.getNum());
											vo05.setEndNum(vo04.getNum());
											Double c= Double.parseDouble(vo04.getNum())-Double.parseDouble(vo03.getNum());
											Double n =Double.parseDouble(vo03.getNum());
											Double p =(c/n)*100;
											vo05.setIncreaseNum(NumberUtil.init().format(c, 0));
											vo05.setIncreasePercent(NumberUtil.init().format(p, 1));
											
											vo03.setFlag("ok");
											break flag;//满足条件的就停止循环
										}else{//不满足，设置默认值
											vo05.setProjectName(vo04.getProjectName());
											vo05.setBeginNum("-");
											vo05.setEndNum(vo04.getNum());
											vo05.setIncreaseNum("-");
											vo05.setIncreasePercent("-");
										}
									}
								}
							}else{//outInpatientWorkVoList03的长度为0
								vo05.setProjectName(vo04.getProjectName());
								vo05.setBeginNum("-");
								vo05.setEndNum(vo04.getNum());
								vo05.setIncreaseNum("-");
								vo05.setIncreasePercent("-");
							}
							
							JZList.add(vo05);
						}
				}

		if(outInpatientWorkVoList03.size()>0){//遍历不在list02的院区
			for(OutInpatientWorkVo vo03:outInpatientWorkVoList03){
				if("ok".equals(vo03.getFlag())){//flag是oK
					vo03.setFlag(null);//设置到以前的状态，不影响后边的计算
				}else{
					OutInpatientWorkVo vo05 = new OutInpatientWorkVo();
					vo05.setProjectName(vo03.getProjectName());
					vo05.setBeginNum(vo03.getNum());
					vo05.setEndNum("-");
					vo05.setIncreaseNum("-");
					vo05.setIncreasePercent("-");
					
					JZList.add(vo05);
				}
			}
		}
		
		/*******************************************************结束计算急诊人次******************************************************************/
		
		/*******************************************************开始计算门急诊总人次******************************************************************/
		
		//根据以上，算出门诊总急诊人次
		OutInpatientWorkVo MJZVo = new OutInpatientWorkVo();//门急诊总人次
		List<OutInpatientWorkVo> MJZlist = new ArrayList<OutInpatientWorkVo>();//门急诊总人次list
		String MJZProName= "门急诊总人次";
		MJZVo.setProjectName(MJZProName);//项目
		String mzNum=MZVo.getBeginNum();//门诊人次，"-"或者字符串数字
		String jzNum=JZVo.getBeginNum();//急诊人次，"-"或者字符串数字
		if("-".equals(mzNum)){
			if("-".equals(jzNum)){
				MJZVo.setBeginNum("-");//前一年人次
			}else{
				MJZVo.setBeginNum(jzNum);//前一年人次
			}
		}else{
			if("-".equals(jzNum)){
				MJZVo.setBeginNum(mzNum);//前一年人次
			}else{
				MJZVo.setBeginNum(new Integer(Integer.parseInt(mzNum)+Integer.parseInt(jzNum)).toString());//前一年人次
			}
		}
		String meNum=MZVo.getEndNum();//门诊人次，"-"或者字符串数字
		String jeNum=JZVo.getEndNum();//急诊人次，"-"或者字符串数字
		if("-".equals(meNum)){
			if("-".equals(jeNum)){
				MJZVo.setEndNum("-");//前一年人次
			}else{
				MJZVo.setEndNum(jeNum);//前一年人次
			}
		}else{
			if("-".equals(jeNum)){
				MJZVo.setEndNum(meNum);//前一年人次
			}else{
				MJZVo.setEndNum(new Integer(Integer.parseInt(meNum)+Integer.parseInt(jeNum)).toString());//前一年人次
			}
		}
		
		if("-".equals(MJZVo.getBeginNum())||"-".equals(MJZVo.getEndNum())){
			MJZVo.setIncreaseNum("-");//增减数
			MJZVo.setIncreasePercent("-");//增减%
			MJZlist.add(MJZVo);
		}else{
			Double c =new Double(MJZVo.getEndNum())-new Double(MJZVo.getBeginNum());
			Double n= new Double(MJZVo.getBeginNum());
			Double p= (c/n)*100;
			MJZVo.setIncreaseNum(NumberUtil.init().format(c, 0));
			MJZVo.setIncreasePercent(NumberUtil.init().format(p, 1));
			MJZlist.add(MJZVo);
		}
		
		
		//门诊人次集合:MZList 急诊人次集合:JZList
		//遍历，分别得到每个院区的门急诊人次
		if(MZList!=null&&JZList!=null){
				for(int i=1;i<MZList.size();i++){//第一个是合计，因此从第二个遍历
					OutInpatientWorkVo vo07 = new OutInpatientWorkVo();
					OutInpatientWorkVo mz=MZList.get(i);
					if(JZList.size()>1){
						inner:for(int j=1;j<JZList.size();j++){
							if(StringUtils.isNotBlank(MZList.get(i).getProjectName())){
								if(MZList.get(i).getProjectName().equals(JZList.get(j).getProjectName())){
									OutInpatientWorkVo jz=JZList.get(j);
									vo07.setProjectName(mz.getProjectName());//项目
									String mz_Num=mz.getBeginNum();//门诊人次，"-"或者字符串数字
									String jz_Num=jz.getBeginNum();//急诊人次，"-"或者字符串数字
									if("-".equals(mz_Num)){
										if("-".equals(jz_Num)){
											vo07.setBeginNum("-");//前一年人次
										}else{
											vo07.setBeginNum(jz_Num);//前一年人次
										}
									}else{
										if("-".equals(jz_Num)){
											vo07.setBeginNum(mz_Num);//前一年人次
										}else{
											vo07.setBeginNum(new Integer(Integer.parseInt(mz_Num)+Integer.parseInt(jz_Num)).toString());//前一年人次
										}
									}
									String me_Num=mz.getEndNum();//门诊人次，"-"或者字符串数字
									String je_Num=jz.getEndNum();//急诊人次，"-"或者字符串数字
									if("-".equals(me_Num)){
										if("-".equals(je_Num)){
											vo07.setEndNum("-");//前一年人次
										}else{
											vo07.setEndNum(je_Num);//前一年人次
										}
									}else{
										if("-".equals(je_Num)){
											vo07.setEndNum(me_Num);//前一年人次
										}else{
											vo07.setEndNum(new Integer(Integer.parseInt(me_Num)+Integer.parseInt(je_Num)).toString());//前一年人次
										}
									}
									if("-".equals(vo07.getBeginNum())||"-".equals(vo07.getEndNum())){
										vo07.setIncreaseNum("-");//增减数
										vo07.setIncreasePercent("-");//增减%
									}else{
										Double c =new Double(vo07.getEndNum())-new Double(vo07.getBeginNum());
										Double n= new Double(vo07.getBeginNum());
										Double p= (c/n)*100;
										vo07.setIncreaseNum(NumberUtil.init().format(c, 0));
										vo07.setIncreasePercent(NumberUtil.init().format(p, 1));
									}
									
									jz.setFlag("ok");//代表该院区前年有，查询年也有
									break inner;
								}else{
									vo07=mz;
								}
							}
						}
					}else{//JZList的长度小于等于1
						vo07=mz;
					}
					MJZlist.add(vo07);
				}
			}
			
		//遍历JZList，得到查询年有，但是前年没有的院区
			if(JZList.size()>1){
				for(OutInpatientWorkVo jz : JZList){
					if("ok".equals(jz.getFlag())){
						jz.setFlag(null);//设置到原来的状态，不影响后边的计算
					}else{
						MJZlist.add(jz);
					}
				}
			}
		
		/*******************************************************结束计算门急诊总人次******************************************************************/
		
		/*******************************************************开始计算日平均门急诊人次******************************************************************/
		
			//根据,门急诊总人次,实际工作日得到--->日平均门急诊人次
		List<OutInpatientWorkVo> RAVGlist = new ArrayList<OutInpatientWorkVo>();
		
		if(MJZlist!=null){
			int i=1;
			for(OutInpatientWorkVo vo:MJZlist){
				OutInpatientWorkVo vo09 = new OutInpatientWorkVo();
				String bmath=vo.getBeginNum();
				if(!StringUtils.isNotBlank(bmath)){
					bmath="0";
				}
				
				String emath=vo.getEndNum();
				if(!StringUtils.isNotBlank(emath)){
					emath="0";				
				}
				if(i==1){
					vo09.setProjectName("日平均门急诊人次");
					if("-".equals(vo.getBeginNum())){
						vo09.setBeginNum("-");//前一年
					}else{
						Double bd=Double.parseDouble(bmath)/Double.parseDouble(BLastDay);
						vo09.setBeginNum(NumberUtil.init().format(bd, 1));//保留整数,不要忘了,前一年//四舍五入
					}
					if("-".equals(vo.getEndNum())){
						vo09.setEndNum("-");//查询年
					}else{
						Double bd=Double.parseDouble(emath)/Double.parseDouble(ELastDay);
						vo09.setEndNum(NumberUtil.init().format(bd, 1));//保留整数,不要忘了,前一年//四舍五入
					}
					if("-".equals(vo.getBeginNum())||"-".equals(vo.getEndNum())){
						vo09.setIncreaseNum("-");
						vo09.setIncreasePercent("-");
					}else{
						Double c =new Double(vo09.getEndNum())-new Double(vo09.getBeginNum());
						Double n= new Double(vo09.getBeginNum());
						Double p= (c/n)*100;
						vo09.setIncreaseNum(NumberUtil.init().format(c, 1));//增减数				
						vo09.setIncreasePercent(NumberUtil.init().format(p, 1));//增减%--->保留一位小数
					}
					
				}else{
					vo09.setProjectName(vo.getProjectName());
					if("-".equals(vo.getBeginNum())){
						vo09.setBeginNum("-");//前一年
					}else{
						Double bd=Double.parseDouble(bmath)/Double.parseDouble(BLastDay);
						vo09.setBeginNum(NumberUtil.init().format(bd, 1));//保留整数,不要忘了,前一年//四舍五入
					}
					if("-".equals(vo.getEndNum())){
						vo09.setEndNum("-");//查询年
					}else{
						Double bd=Double.parseDouble(emath)/Double.parseDouble(ELastDay);
						vo09.setEndNum(NumberUtil.init().format(bd, 1));//保留整数,不要忘了,前一年//四舍五入
					}
					if("-".equals(vo.getBeginNum())||"-".equals(vo.getEndNum())){
						vo09.setIncreaseNum("-");
						vo09.setIncreasePercent("-");
					}else{
						Double c =new Double(vo09.getEndNum())-new Double(vo09.getBeginNum());
						Double n= new Double(vo09.getBeginNum());
						Double p= (c/n)*100;
						vo09.setIncreaseNum(NumberUtil.init().format(c, 1));//增减数				
						vo09.setIncreasePercent(NumberUtil.init().format(p, 1));//增减%--->保留一位小数
					}
					
				}
				i++;
				
				RAVGlist.add(vo09);
			}
			
		}
		
	/*******************************************************结束计算日平均门急诊人次******************************************************************/
	
    /*******************************************************开始计算入院人次***********************************************************************/
		
		//拼接sql
		StringBuffer sb02 = new StringBuffer();
		sb02.append(" SELECT T.DEPTNAME AS DEPTNAME, SUM(T.NUM) AS NUM FROM ( ");
		for(int i=0;i<zyList.size();i++){
			if(i!=0){
				sb02.append(" UNION ALL ");
			}
			sb02.append(" SELECT ");
			sb02.append(" DECODE(P.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME,COUNT (1) AS NUM ");
			sb02.append(" FROM ");
			sb02.append(zyList.get(i)).append(" P ");
			sb02.append(" WHERE ");
			sb02.append(" P.DEL_FLG = 0 ");
			sb02.append(" AND P.STOP_FLG = 0 ");
			if(StringUtils.isNotBlank(Etime)){
				sb02.append(" AND P.IN_DATE >= TO_DATE (:beginTime, 'YYYY-MM-DD HH24:MI:SS') ");
				sb02.append(" AND P.IN_DATE < TO_DATE (:endTime, 'YYYY-MM-DD HH24:MI:SS') ");
			}
			if(StringUtils.isNotBlank(areaCode)){
				sb02.append(" AND P.AREA_CODE IN (:areaCode) ");
			}
			sb02.append(" GROUP BY ");
			sb02.append(" P.AREA_CODE ");
		}
		sb02.append(" ) T GROUP BY ");
		sb02.append(" T.DEPTNAME ");
		
		//分别求出前一年和查询年份的人数，按院区分组
		HashMap map05 = new HashMap();// 前一年的map
		HashMap map06 = new HashMap();// 查询所在年的map
		if (StringUtils.isNotBlank(beginTime01)) {
			map05.put("beginTime", beginTime01);
		}
		if (StringUtils.isNotBlank(endTime01)) {
			map05.put("endTime", endTime01);
		}
		if (StringUtils.isNotBlank(areaCode)) {
			map05.put("areaCode", areaCodeList);
		}
		
		if (StringUtils.isNotBlank(beginTime02)) {
			map06.put("beginTime", beginTime02);
		}
		if (StringUtils.isNotBlank(endTime02)) {
			map06.put("endTime", endTime02);
		}
		if (StringUtils.isNotBlank(areaCode)) {
			map06.put("areaCode", areaCodeList);
		}
		
		//分别求出前一年和查询年份的人数，按院区分组
		//前一年的list(院区，人次)
		List<OutInpatientWorkVo> outInpatientWorkVoList05 = namedParameterJdbcTemplate.query(sb02.toString(), map05,new RowMapper<OutInpatientWorkVo>() {
							@Override
							public OutInpatientWorkVo mapRow(ResultSet rs, int r)
									throws SQLException {
								OutInpatientWorkVo vo = new OutInpatientWorkVo();
								vo.setProjectName(rs.getString("DEPTNAME"));
								vo.setNum(rs.getString("NUM"));
								return vo;
							}

						});
		
		// 查询所在年的list(院区，人次)
		List<OutInpatientWorkVo> outInpatientWorkVoList06= namedParameterJdbcTemplate.query(sb02.toString(), map06,new RowMapper<OutInpatientWorkVo>() {
							@Override
							public OutInpatientWorkVo mapRow(ResultSet rs, int r)
									throws SQLException {
								OutInpatientWorkVo vo = new OutInpatientWorkVo();
								vo.setProjectName(rs.getString("DEPTNAME"));
								vo.setNum(rs.getString("NUM"));
								return vo;
							}

						});
		
		// 算出入院人次
		List<OutInpatientWorkVo> RYList = new ArrayList<OutInpatientWorkVo>();// 入院人次List
		String RYProName = "入院人次";
		int num05 = 0;// 前一年人次
		int num06 = 0;// 查询年的人次
		if (outInpatientWorkVoList05 != null) {
			for (OutInpatientWorkVo v : outInpatientWorkVoList05) {
				num05 +=Integer.parseInt(v.getNum());
			}
		}
		if (outInpatientWorkVoList06 != null) {
			for (OutInpatientWorkVo v : outInpatientWorkVoList06) {
				num06 += Integer.parseInt(v.getNum());
			}
		}
		OutInpatientWorkVo RYVo = new OutInpatientWorkVo();// 入院人次
		RYVo.setProjectName(RYProName);// 项目
		if(num05==0){//说明16年没数据
			RYVo.setBeginNum("-");//前一年人次
			if(num06==0){//说明查询年没数据
				RYVo.setEndNum("-");//查询年也没数据
				RYVo.setIncreaseNum("-");//增减数没数据
				RYVo.setIncreasePercent("-");//增减%没数据
				RYList.add(RYVo);
			}else{
				RYVo.setEndNum(new Integer(num06).toString());//查询年的人次(有人次)
				RYVo.setIncreaseNum("-");//增减数没数据
				RYVo.setIncreasePercent("-");//增减%没数据
				RYList.add(RYVo);
			}
		}else{
			RYVo.setBeginNum(new Integer(num05).toString());//前一年人次
			if(num06==0){//说明查询年没数据
				RYVo.setEndNum("-");//查询年也没数据
				RYVo.setIncreaseNum("-");//增减数没数据
				RYVo.setIncreasePercent("-");//增减%没数据
				RYList.add(RYVo);
			}else{
				RYVo.setEndNum(new Integer(num06).toString());//查询年的人次(有人次)
				Double str=new Integer(num06-num05).doubleValue();
				RYVo.setIncreaseNum(NumberUtil.init().format(str, 0));//增减数有数据
				//一些列转类型，为了防止计算三百分比时丢失精度
				Double mid= new Integer(num06-num05).doubleValue();
				Double dnum05=new Integer(num05).doubleValue();
				RYVo.setIncreasePercent(NumberUtil.init().format(new Double((mid/dnum05)*100), 1));//增减%有数据
				RYList.add(RYVo);
			}
		}
		
		if(outInpatientWorkVoList05!=null&&outInpatientWorkVoList06!=null){
								for(OutInpatientWorkVo vo06:outInpatientWorkVoList06){
									OutInpatientWorkVo vo10 = new OutInpatientWorkVo();
									if(outInpatientWorkVoList05.size()>0){
										flag:for(OutInpatientWorkVo vo05:outInpatientWorkVoList05){
											if(vo06.getProjectName()!=null){
												if(vo06.getProjectName().equals(vo05.getProjectName())){
													vo10.setProjectName(vo06.getProjectName());
													vo10.setBeginNum(vo05.getNum());
													vo10.setEndNum(vo06.getNum());
													Double c= Double.parseDouble(vo06.getNum())-Double.parseDouble(vo05.getNum());
													Double n =Double.parseDouble(vo05.getNum());
													Double p =(c/n)*100;
													vo10.setIncreaseNum(NumberUtil.init().format(c, 0));//增减数
													vo10.setIncreasePercent(NumberUtil.init().format(p, 0));
													
													vo05.setFlag("ok");//ok代表两个集合的交集元素
													break flag;//满足条件的就停止循环
												}else{//不满足，设置默认值
													vo10.setProjectName(vo06.getProjectName());
													vo10.setBeginNum("-");
													vo10.setEndNum(vo06.getNum());
													vo10.setIncreaseNum("-");
													vo10.setIncreasePercent("-");
												}
											}
										}
									}else{//outInpatientWorkVoList05的长度等于0
										vo10.setProjectName(vo06.getProjectName());
										vo10.setBeginNum("-");
										vo10.setEndNum(vo06.getNum());
										vo10.setIncreaseNum("-");
										vo10.setIncreasePercent("-");
									}
									RYList.add(vo10);
								}
				}
		
			if(outInpatientWorkVoList05.size()>0){
				for(OutInpatientWorkVo vo05:outInpatientWorkVoList05){
					if("ok".equals(vo05.getFlag())){
						vo05.setFlag(null);//回到原始状态，不影响后边的计算
					}else{
						OutInpatientWorkVo vo10 = new OutInpatientWorkVo();
						vo10.setProjectName(vo05.getProjectName());
						vo10.setBeginNum(vo05.getNum());
						vo10.setEndNum("-");
						vo10.setIncreaseNum("-");
						vo10.setIncreasePercent("-");
						RYList.add(vo10);
					}
				}
				
			}		
		
	/*******************************************************结束计算入院人次***********************************************************************/
	/*******************************************************开始计算出院人次***********************************************************************/
				//拼接sql
				StringBuffer sb03 = new StringBuffer();
				sb03.append(" SELECT T.DEPTNAME AS DEPTNAME, SUM(T.NUM) AS NUM FROM ( ");
				for(int i=0;i<zyList.size();i++){
					if(i!=0){
						sb03.append(" UNION ALL ");
					}
					sb03.append(" SELECT ");
					sb03.append(" DECODE(P.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME,COUNT (1) AS NUM ");
					sb03.append(" FROM ");
					sb03.append(zyList.get(i)).append(" P ");
					sb03.append(" WHERE ");
					sb03.append(" P.DEL_FLG = 0 ");
					sb03.append(" AND P.STOP_FLG = 0 ");
					if(StringUtils.isNotBlank(Etime)){
						sb03.append(" AND P.OUT_DATE >= TO_DATE (:beginTime, 'YYYY-MM-DD HH24:MI:SS') ");
						sb03.append(" AND P.OUT_DATE < TO_DATE (:endTime, 'YYYY-MM-DD HH24:MI:SS') ");
					}
					if(StringUtils.isNotBlank(areaCode)){
						sb03.append(" AND P.AREA_CODE IN (:areaCode) ");
					}
					sb03.append(" GROUP BY ");
					sb03.append(" P.AREA_CODE ");
				}
				sb03.append(" ) T GROUP BY ");
				sb03.append(" T.DEPTNAME");
				
				//分别求出前一年和查询年份的人数，按院区分组
				HashMap map07 = new HashMap();// 前一年的map
				HashMap map08 = new HashMap();// 查询所在年的map
				if (StringUtils.isNotBlank(beginTime01)) {
					map07.put("beginTime", beginTime01);
				}
				if (StringUtils.isNotBlank(endTime01)) {
					map07.put("endTime", endTime01);
				}
				if (StringUtils.isNotBlank(areaCode)) {
					map07.put("areaCode", areaCodeList);
				}
				
				if (StringUtils.isNotBlank(beginTime02)) {
					map08.put("beginTime", beginTime02);
				}
				if (StringUtils.isNotBlank(endTime02)) {
					map08.put("endTime", endTime02);
				}
				if (StringUtils.isNotBlank(areaCode)) {
					map08.put("areaCode", areaCodeList);
				}
				
				//分别求出前一年和查询年份的人数，按院区分组
				//前一年的list(院区，人次)
				List<OutInpatientWorkVo> outInpatientWorkVoList07 = namedParameterJdbcTemplate.query(sb03.toString(), map07,new RowMapper<OutInpatientWorkVo>() {
									@Override
									public OutInpatientWorkVo mapRow(ResultSet rs, int r)
											throws SQLException {
										OutInpatientWorkVo vo = new OutInpatientWorkVo();
										vo.setProjectName(rs.getString("DEPTNAME"));
										vo.setNum(rs.getString("NUM"));
										return vo;
									}

								});
				
				// 查询所在年的list(院区，人次)
				List<OutInpatientWorkVo> outInpatientWorkVoList08= namedParameterJdbcTemplate.query(sb03.toString(), map08,new RowMapper<OutInpatientWorkVo>() {
									@Override
									public OutInpatientWorkVo mapRow(ResultSet rs, int r)
											throws SQLException {
										OutInpatientWorkVo vo = new OutInpatientWorkVo();
										vo.setProjectName(rs.getString("DEPTNAME"));
										vo.setNum(rs.getString("NUM"));
										return vo;
									}

								});
				
				// 算出入院人次
				List<OutInpatientWorkVo> CYList = new ArrayList<OutInpatientWorkVo>();// 出院人次List
				String CYProName = "出院人次";
				int num07 = 0;// 前一年人次
				int num08 = 0;// 查询年的人次
				if (outInpatientWorkVoList07 != null) {
					for (OutInpatientWorkVo v : outInpatientWorkVoList07) {
						num07 += Integer.parseInt(v.getNum());
					}
				}
				if (outInpatientWorkVoList08 != null) {
					for (OutInpatientWorkVo v : outInpatientWorkVoList08) {
						num08 += Integer.parseInt(v.getNum());
					}
				}
				OutInpatientWorkVo CYVo = new OutInpatientWorkVo();// 出院人次
				CYVo.setProjectName(CYProName);// 项目
				if(num07==0){//说明16年没数据
					CYVo.setBeginNum("-");//前一年人次
					if(num08==0){//说明查询年没数据
						CYVo.setEndNum("-");//查询年也没数据
						CYVo.setIncreaseNum("-");//增减数没数据
						CYVo.setIncreasePercent("-");//增减%没数据
						CYList.add(CYVo);
					}else{
						CYVo.setEndNum(new Integer(num08).toString());//查询年的人次(有人次)
						CYVo.setIncreaseNum("-");//增减数没数据
						CYVo.setIncreasePercent("-");//增减%没数据
						CYList.add(CYVo);
					}
				}else{
					CYVo.setBeginNum(new Integer(num07).toString());//前一年人次
					if(num08==0){//说明查询年没数据
						CYVo.setEndNum("-");//查询年也没数据
						CYVo.setIncreaseNum("-");//增减数没数据
						CYVo.setIncreasePercent("-");//增减%没数据
						CYList.add(CYVo);
					}else{
						CYVo.setEndNum(new Integer(num08).toString());//查询年的人次(有人次)
						Double cy=new Integer(num08-num07).doubleValue();
						CYVo.setIncreaseNum(NumberUtil.init().format(cy, 0));//增减数有数据
						//一些列转类型，为了防止计算三百分比时丢失精度
						Double mid= new Integer(num08-num07).doubleValue();
						Double dnum07=new Integer(num07).doubleValue();
						CYVo.setIncreasePercent(NumberUtil.init().format(new Double((mid/dnum07)*100), 1));//增减%有数据
						CYList.add(CYVo);
					}
				}
				
				
				if(outInpatientWorkVoList07!=null&&outInpatientWorkVoList08!=null){
										for(OutInpatientWorkVo vo08:outInpatientWorkVoList08){
											OutInpatientWorkVo vo12 = new OutInpatientWorkVo();
											if(outInpatientWorkVoList07.size()>0){
												flag:for(OutInpatientWorkVo vo07:outInpatientWorkVoList07){
													if(vo08.getProjectName()!=null){
														if(vo08.getProjectName().equals(vo07.getProjectName())){
															vo12.setProjectName(vo08.getProjectName());
															vo12.setBeginNum(vo07.getNum());
															vo12.setEndNum(vo08.getNum());
															Double c= Double.parseDouble(vo08.getNum())-Double.parseDouble(vo07.getNum());
															Double n =Double.parseDouble(vo07.getNum());
															Double p =(c/n)*100;
															vo12.setIncreaseNum(NumberUtil.init().format(c, 0));//增减数
															vo12.setIncreasePercent(NumberUtil.init().format(p, 0));
															
															vo07.setFlag("ok");//两个集合的交集
															break flag;//满足条件的就停止循环
														}else{//不满足，设置默认值
															vo12.setProjectName(vo08.getProjectName());
															vo12.setBeginNum("-");
															vo12.setEndNum(vo08.getNum());
															vo12.setIncreaseNum("-");
															vo12.setIncreasePercent("-");
														}
													}
												}
											}else{//outInpatientWorkVoList07的 长度为0
												vo12.setProjectName(vo08.getProjectName());
												vo12.setBeginNum("-");
												vo12.setEndNum(vo08.getNum());
												vo12.setIncreaseNum("-");
												vo12.setIncreasePercent("-");
											}
											CYList.add(vo12);
										}
						
							}	
				
				//得到前年的集合中有，查询年没有的数据
				if(outInpatientWorkVoList07.size()>0){
					for(OutInpatientWorkVo vo07:outInpatientWorkVoList07){
						if("ok".equals(vo07.getFlag())){
							vo07.setFlag(null);//设置为null,不影响后边的计算
						}else{
							OutInpatientWorkVo vo12 = new OutInpatientWorkVo();
							vo12.setProjectName(vo07.getProjectName());
							vo12.setBeginNum(vo07.getNum());
							vo12.setEndNum("-");
							vo12.setIncreaseNum("-");
							vo12.setIncreasePercent("-");
							CYList.add(vo12);
						}
					}
				}
				
				
	/*******************************************************结束计算出院人次***********************************************************************/
				
				
	/*******************************************************开始计算平均住院天数*********************************************************************/
							
				//思路：查出4月是出院的人，计算每个人的的（出院-入院时间）相加/出院人次
							StringBuffer sb04 = new StringBuffer();
							//查询每个院区的住院天数
							sb04.append(" SELECT ");
							sb04.append(" T.DEPTNAME AS DEPTNAME, SUM(T.NUM) AS NUM ");
							sb04.append(" FROM ( ");
							for(int t=0;t<zyList.size();t++){
									if(t!=0){
										sb04.append(" UNION ALL ");
									}
									sb04.append(" SELECT ");
									sb04.append(" DECODE(I.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME, ROUND(SUM(I.OUT_DATE - I.IN_DATE),0) AS NUM ");
									sb04.append(" FROM ");
									sb04.append(zyList.get(t)).append(" I ");
									sb04.append("  WHERE ");
									sb04.append(" I.DEL_FLG = 0 AND I.STOP_FLG = 0");
									if(StringUtils.isNotBlank(Etime)){
										sb04.append(" AND I.OUT_DATE >= TO_DATE (:beginTime,'YYYY-MM-DD HH24:MI:SS')");
										sb04.append(" AND I.OUT_DATE < TO_DATE (:endTime,'YYYY-MM-DD HH24:MI:SS') ");
									}
									if(StringUtils.isNotBlank(areaCode)){
										sb04.append(" AND I.AREA_CODE IN (:areaCode) ");
									}
									sb04.append(" GROUP BY ");
									sb04.append(" I.AREA_CODE ");
							}
							sb04.append(" ) T");
							sb04.append(" GROUP BY ");
							sb04.append(" T.DEPTNAME ");
							
							//分别求出前一年和查询年份的住院天数，按院区分组
							HashMap map09 = new HashMap();// 前一年的map
							HashMap map10 = new HashMap();// 查询所在年的map
							if (StringUtils.isNotBlank(beginTime01)) {
								map09.put("beginTime", beginTime01);
							}
							if (StringUtils.isNotBlank(endTime01)) {
								map09.put("endTime", endTime01);
							}
							if (StringUtils.isNotBlank(areaCode)) {
								map09.put("areaCode", areaCodeList);
							}
							
							if (StringUtils.isNotBlank(beginTime02)) {
								map10.put("beginTime", beginTime02);
							}
							if (StringUtils.isNotBlank(endTime02)) {
								map10.put("endTime", endTime02);
							}
							if (StringUtils.isNotBlank(areaCode)) {
								map10.put("areaCode", areaCodeList);
							}
				
							//前一年的list(院区，院区的总住院天数)
							List<OutInpatientWorkVo> outInpatientWorkVoList09 = namedParameterJdbcTemplate.query(sb04.toString(), map09,new RowMapper<OutInpatientWorkVo>() {
												@Override
												public OutInpatientWorkVo mapRow(ResultSet rs, int r)
														throws SQLException {
													OutInpatientWorkVo vo = new OutInpatientWorkVo();
													vo.setProjectName(rs.getString("DEPTNAME"));
													vo.setNum(rs.getString("NUM"));
													return vo;
												}

											});
							
							// 查询所在年的list(院区，院区的总住院天数)
							List<OutInpatientWorkVo> outInpatientWorkVoList10= namedParameterJdbcTemplate.query(sb04.toString(), map10,new RowMapper<OutInpatientWorkVo>() {
												@Override
												public OutInpatientWorkVo mapRow(ResultSet rs, int r)
														throws SQLException {
													OutInpatientWorkVo vo = new OutInpatientWorkVo();
													vo.setProjectName(rs.getString("DEPTNAME"));
													vo.setNum(rs.getString("NUM"));
													return vo;
												}

											});
							
							// 算出平均住院天数
							List<OutInpatientWorkVo> ZYDList = new ArrayList<OutInpatientWorkVo>();// 平均住院天数List
							String ZYDProName = "平均住院天数";
							int num09 = 0;// 前一年总天数
							int num10 = 0;// 查询年的总天数
							if (outInpatientWorkVoList09 != null) {
								for (OutInpatientWorkVo v : outInpatientWorkVoList09) {
									num09 += Integer.parseInt(v.getNum());
								}
							}
							if (outInpatientWorkVoList10 != null) {
								for (OutInpatientWorkVo v : outInpatientWorkVoList10) {
									num10 += Integer.parseInt(v.getNum());
								}
							}
							OutInpatientWorkVo ZYDo = new OutInpatientWorkVo();// 平均住院天数
							ZYDo.setProjectName(ZYDProName);// 项目
							if(num09==0){//说明16年没数据
								ZYDo.setBeginNum("-");//前一年人次
								if(num10==0){//说明查询年没数据
									ZYDo.setEndNum("-");//查询年也没数据
									ZYDo.setIncreaseNum("-");//增减数没数据
									ZYDo.setIncreasePercent("-");//增减%没数据
									ZYDList.add(ZYDo);
								}else{
									double value = new Integer(num10).doubleValue();
									Double avg= value/(Double.parseDouble(CYList.get(0).getEndNum()));//求出查询一年的平均值
									ZYDo.setEndNum(NumberUtil.init().format(avg, 1));
									ZYDo.setIncreaseNum("-");//增减数没数据
									ZYDo.setIncreasePercent("-");//增减%没数据
									ZYDList.add(ZYDo);
								}
							}else{
								double value = new Integer(num09).doubleValue();
								Double avg= value/(Double.parseDouble(CYList.get(0).getBeginNum()));//求出查询一年的平均值
								ZYDo.setBeginNum(NumberUtil.init().format(avg, 1));//前一年的平均住院天数
								if(num10==0){//说明查询年没数据
									ZYDo.setEndNum("-");//查询年也没数据
									ZYDo.setIncreaseNum("-");//增减数没数据
									ZYDo.setIncreasePercent("-");//增减%没数据
									ZYDList.add(ZYDo);
								}else{
									double v = new Integer(num10).doubleValue();
									Double g= value/(Double.parseDouble(CYList.get(0).getEndNum()));//求出查询一年的平均值
									ZYDo.setEndNum(NumberUtil.init().format(g, 1));//查询年的平均住院天数
									Double dnf= new Double(ZYDo.getEndNum())-new Double(ZYDo.getBeginNum());
									String ccyy=NumberUtil.init().format(dnf, 1);
									ZYDo.setIncreaseNum(ccyy);//增减数有数据
									//一些列转类型，为了防止计算三百分比时丢失精度
									Double peret=(dnf/new Double(ZYDo.getBeginNum()))*100;
									ZYDo.setIncreasePercent(NumberUtil.init().format(peret, 1));//增减%有数据
									ZYDList.add(ZYDo);
								}
							}
							ArrayList<OutInpatientWorkVo> outInpatientWorkVoList11 = new ArrayList<OutInpatientWorkVo>();//里面放的是前一年的院区名，平均住院天数的实体
							
							//分别求各个院区的平均住院天数
							if(outInpatientWorkVoList09!=null){//遍历前一年的到前一年的平均值
								for(OutInpatientWorkVo v:outInpatientWorkVoList09){
									for(int k=1;k<CYList.size();k++){//从第二条遍历，因为第一条属于合计
										if(v.getProjectName().equals(CYList.get(k).getProjectName())){
											Double r = Double.parseDouble(v.getNum());//得到院区总住院时间
											Double t = Double.parseDouble(CYList.get(k).getBeginNum());//得到院区的住院人次
											OutInpatientWorkVo vo14 = new OutInpatientWorkVo();
											Double f = r/t;//得到平均住院天数
											vo14.setBeginNum(f.toString());
											vo14.setProjectName(v.getProjectName());
											outInpatientWorkVoList11.add(vo14);
										}
									}
								}
								
							}							
							
							List<OutInpatientWorkVo> outInpatientWorkVoList12 = new ArrayList<OutInpatientWorkVo>();//里面放的是查询年的院区名，平均住院天数的实体
							
							//分别求各个院区的平均住院天数
							if(outInpatientWorkVoList10!=null){//遍历前一年的到前一年的平均值
								for(OutInpatientWorkVo v:outInpatientWorkVoList10){
									for(int k=1;k<CYList.size();k++){//从第二条遍历，因为第一条属于合计
										if(v.getProjectName().equals(CYList.get(k).getProjectName())){
											Double r = Double.parseDouble(v.getNum());//得到院区总住院时间
											Double t = Double.parseDouble(CYList.get(k).getEndNum());//得到院区的住院人次
											OutInpatientWorkVo vo15 = new OutInpatientWorkVo();
											Double f = r/t;//得到平均住院天数
											vo15.setEndNum(f.toString());
											vo15.setProjectName(v.getProjectName());
											outInpatientWorkVoList12.add(vo15);
										}
									}
								}
								
							}
						
							//根据以上，算出各个院区的平均住院天数
							if(outInpatientWorkVoList11!=null&&outInpatientWorkVoList12!=null){
													for(OutInpatientWorkVo vo12:outInpatientWorkVoList12){
														OutInpatientWorkVo vo16 = new OutInpatientWorkVo();
														if(outInpatientWorkVoList11.size()>0){
															flag:for(OutInpatientWorkVo vo11:outInpatientWorkVoList11){
																if(vo12.getProjectName()!=null){
																	if(vo12.getProjectName().equals(vo11.getProjectName())){
																		vo16.setProjectName(vo12.getProjectName());
																		String bt=vo11.getBeginNum();
																		vo16.setBeginNum(NumberUtil.init().format(Double.parseDouble(bt), 1));
																		String et=vo12.getEndNum();
																		vo16.setEndNum(NumberUtil.init().format(Double.parseDouble(et), 1));
																		Double c= Double.parseDouble(vo12.getEndNum())-Double.parseDouble(vo11.getBeginNum());
																		Double n =Double.parseDouble(vo11.getBeginNum());
																		Double p =(c/n)*100;
																		vo16.setIncreaseNum(NumberUtil.init().format(c, 1));
																		vo16.setIncreasePercent(NumberUtil.init().format(p, 1));
																		
																		vo11.setFlag("ok");//两个集合的交集加标记
																		break flag;//满足条件的就停止循环
																	}else{//不满足，设置默认值
																		vo16.setProjectName(vo12.getProjectName());
																		vo16.setBeginNum("-");
																		String et=vo12.getEndNum();
																		vo16.setEndNum(NumberUtil.init().format(Double.parseDouble(et),1));
																		vo16.setIncreaseNum("-");
																		vo16.setIncreasePercent("-");
																	}
																}
															}
														}else{//outInpatientWorkVoList11的 长度为0
															vo16.setProjectName(vo12.getProjectName());
															vo16.setBeginNum("-");
															String et=vo12.getEndNum();
															vo16.setEndNum(NumberUtil.init().format(Double.parseDouble(et),1));
															vo16.setIncreaseNum("-");
															vo16.setIncreasePercent("-");
														}
														ZYDList.add(vo16);
													}
							}
							
							//看上边类似模块的代码注释
							if(outInpatientWorkVoList11.size()>0){
								for(OutInpatientWorkVo vo11:outInpatientWorkVoList11){
									if("ok".equals(vo11.getFlag())){
										vo11.setFlag(null);
									}else{
										OutInpatientWorkVo vo16 = new OutInpatientWorkVo();
										vo16.setProjectName(vo11.getProjectName());
										Double d= Double.parseDouble(vo11.getBeginNum());
										vo16.setBeginNum(NumberUtil.init().format(d, 1));
										vo16.setEndNum("-");
										vo16.setIncreaseNum("-");
										vo16.setIncreasePercent("-");
										ZYDList.add(vo16);
									}
								}
								
							}
							
	/*******************************************************结束计算平均住院天数**********************************************************************/		
		
	/*******************************************************开始计算病床使用率*********************************************************************/
							
							//表名：T_BUSINESS_HOSPITALBED
							//思路：
							//床位是占有状态	
							StringBuffer sb05 = new StringBuffer();
							sb05.append(" SELECT ");
							sb05.append(" DECODE(T.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME ,COUNT (1) AS NUM");
							sb05.append(" FROM ");
							sb05.append(" T_BUSINESS_HOSPITALBED T");
							sb05.append(" WHERE ");
							sb05.append(" T.BED_STATE = 4 ");
							if(StringUtils.isNotBlank(areaCode)){
								sb05.append(" AND T.AREA_CODE IN (:areaCode) ");
							}
							sb05.append(" GROUP BY ");
							sb05.append(" T.AREA_CODE");
							
							HashMap map11 = new HashMap();// 前一年的map
							if (StringUtils.isNotBlank(areaCode)) {
								map11.put("areaCode", areaCodeList);
							}
							
							//得到当天的每个院区的床位是占有状态的list
							List<OutInpatientWorkVo> outInpatientWorkVoList13 = namedParameterJdbcTemplate.query(sb05.toString(), map11,new RowMapper<OutInpatientWorkVo>() {
												@Override
												public OutInpatientWorkVo mapRow(ResultSet rs, int r)
														throws SQLException {
													OutInpatientWorkVo vo = new OutInpatientWorkVo();
													vo.setProjectName(rs.getString("DEPTNAME"));
													vo.setNum(rs.getString("NUM"));
													return vo;
												}

											});
						
							//所有床位
							StringBuffer sb06 = new StringBuffer();
							sb06.append(" SELECT ");
							sb06.append(" DECODE(T.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME ,COUNT (1) AS NUM");
							sb06.append(" FROM ");
							sb06.append(" T_BUSINESS_HOSPITALBED T");
							if(StringUtils.isNotBlank(areaCode)){
								sb06.append(" WHERE ");
								sb06.append(" T.AREA_CODE IN (:areaCode) ");
							}
							sb06.append(" GROUP BY ");
							sb06.append(" T.AREA_CODE");
							HashMap map12 = new HashMap();// 前一年的map
							if (StringUtils.isNotBlank(areaCode)) {
								map12.put("areaCode", areaCodeList);
							}
							
							//得到当天的每个院区的所有床位数量
							List<OutInpatientWorkVo> outInpatientWorkVoList14 = namedParameterJdbcTemplate.query(sb06.toString(), map11,new RowMapper<OutInpatientWorkVo>() {
												@Override
												public OutInpatientWorkVo mapRow(ResultSet rs, int r)
														throws SQLException {
													OutInpatientWorkVo vo = new OutInpatientWorkVo();
													vo.setProjectName(rs.getString("DEPTNAME"));
													vo.setNum(rs.getString("NUM"));
													return vo;
												}

											});
							
							//计算病床使用率(病床信息表只保存当天数据)	
							List<OutInpatientWorkVo>  BCIList= new ArrayList<OutInpatientWorkVo>();//病床使用率(%)
							OutInpatientWorkVo BCIVo = new OutInpatientWorkVo();//病床使用率(%)
							String BCIProName="病床使用率(%)";
							BCIVo.setProjectName(BCIProName);
							BCIVo.setBeginNum("-");
							int badCount=0;//床位总数
							int fbadCount=0;//占有状态的床位数
							if(outInpatientWorkVoList14!=null){
								for(OutInpatientWorkVo vw :outInpatientWorkVoList14){
									badCount=Integer.parseInt(vw.getNum());
								}
							}
							if(outInpatientWorkVoList13!=null){
								for(OutInpatientWorkVo vv :outInpatientWorkVoList13){
									fbadCount=Integer.parseInt(vv.getNum());
								}
							}
							if(badCount==0||fbadCount==0){
									BCIVo.setEndNum("-");
							}else{
								Double o= new Integer(badCount).doubleValue();
								Double u = new Integer(fbadCount).doubleValue();
								BCIVo.setEndNum(NumberUtil.init().format(new Double((u/o)*100),1));
							}
							BCIVo.setIncreaseNum("-");
							BCIVo.setIncreasePercent("-");
							BCIList.add(BCIVo);
							
							//分别求出各个院区的病床使用率
							if(outInpatientWorkVoList13!=null){
								for(OutInpatientWorkVo vo13:outInpatientWorkVoList13){
									OutInpatientWorkVo vo18 = new OutInpatientWorkVo();
									if(outInpatientWorkVoList14.size()>0){
										inner:for(OutInpatientWorkVo vo14:outInpatientWorkVoList14){
													if(vo13.getProjectName().equals(vo14.getProjectName())){
														vo18.setProjectName(vo13.getProjectName());
														vo18.setBeginNum("-");
														Double sumBad=Double.parseDouble(vo14.getNum());//院区的总床位数
														Double schBad = Double.parseDouble(vo13.getNum());//院区的占有床位数
														vo18.setEndNum(NumberUtil.init().format(new Double((schBad/sumBad)*100), 1));
														vo18.setIncreaseNum("-");
														vo18.setIncreasePercent("-");
														break inner;
													}
												}
									}else{
										vo18.setProjectName(vo13.getProjectName());
										vo18.setBeginNum("-");
										vo18.setEndNum("-");
										vo18.setIncreaseNum("-");
										vo18.setIncreasePercent("-");
									}
									BCIList.add(vo18);
								}
								
							}	
	/*******************************************************结束计算病床使用率**********************************************************************/	
							//对院区进行排序
							String[] yq={"河医院区","郑东院区","惠济院区"};
							HashMap<String,OutInpatientWorkVo> MJZlist_Map = new HashMap<String, OutInpatientWorkVo>();
							for(int y =1;y<MJZlist.size();y++){
								MJZlist_Map.put(MJZlist.get(y).getProjectName(), MJZlist.get(y));
							}
							ArrayList<OutInpatientWorkVo> F_MJZlist=new ArrayList<OutInpatientWorkVo>();
							for(int i=0;i<yq.length;i++){
								OutInpatientWorkVo v;
									if(i==0){
										F_MJZlist.add(MJZlist.get(0));
										 v=MJZlist_Map.get(yq[i]);
										if(v!=null){
											F_MJZlist.add(v);
										}
									}else{
										v=MJZlist_Map.get(yq[i]);
										if(v!=null){
											F_MJZlist.add(v);
										}
									}
									
							}
							
							HashMap<String,OutInpatientWorkVo> MZWList_Map = new HashMap<String, OutInpatientWorkVo>();
							for(int y =1;y<MZWList.size();y++){
								MZWList_Map.put(MZWList.get(y).getProjectName(), MZWList.get(y));
							}
							ArrayList<OutInpatientWorkVo> F_MZWList=new ArrayList<OutInpatientWorkVo>();
							for(int i=0;i<yq.length;i++){
								OutInpatientWorkVo v;
									if(i==0){
										F_MZWList.add(MZWList.get(0));
										v=MZWList_Map.get(yq[i]);
										if(v!=null){
											F_MZWList.add(v);
										}
									}else{
										v=MZWList_Map.get(yq[i]);
										if(v!=null){
											F_MZWList.add(v);
										}
									}
									
							}
							
							HashMap<String,OutInpatientWorkVo> RAVGlist_Map = new HashMap<String, OutInpatientWorkVo>();
							for(int y =1;y<RAVGlist.size();y++){
								RAVGlist_Map.put(RAVGlist.get(y).getProjectName(), RAVGlist.get(y));
							}
							ArrayList<OutInpatientWorkVo> F_RAVGlist=new ArrayList<OutInpatientWorkVo>();
							for(int i=0;i<yq.length;i++){
								OutInpatientWorkVo v;
									if(i==0){
										F_RAVGlist.add(RAVGlist.get(0));
										v=RAVGlist_Map.get(yq[i]);
										if(v!=null){
											F_RAVGlist.add(v);
										}
									}else{
										v=RAVGlist_Map.get(yq[i]);
										if(v!=null){
											F_RAVGlist.add(v);
										}
									}
								
							}
							
							HashMap<String,OutInpatientWorkVo> MZList_Map = new HashMap<String, OutInpatientWorkVo>();
							for(int y =1;y<MZList.size();y++){
								MZList_Map.put(MZList.get(y).getProjectName(), MZList.get(y));
							}
							ArrayList<OutInpatientWorkVo> F_MZList=new ArrayList<OutInpatientWorkVo>();
							for(int i=0;i<yq.length;i++){
								OutInpatientWorkVo v;
									if(i==0){
										F_MZList.add(MZList.get(0));
										v=MZList_Map.get(yq[i]);
										if(v!=null){
											F_MZList.add(v);
										}
										
									}else{
										v=MZList_Map.get(yq[i]);
										if(v!=null){
											F_MZList.add(v);
										}
									}
									
							}
							
							HashMap<String,OutInpatientWorkVo> JZList_Map = new HashMap<String, OutInpatientWorkVo>();
							for(int y =1;y<JZList.size();y++){
								JZList_Map.put(JZList.get(y).getProjectName(), JZList.get(y));
							}
							ArrayList<OutInpatientWorkVo> F_JZList=new ArrayList<OutInpatientWorkVo>();
							for(int i=0;i<yq.length;i++){
								OutInpatientWorkVo v;
									if(i==0){
										F_JZList.add(JZList.get(0));
										v=JZList_Map.get(yq[i]);
										if(v!=null){
											F_JZList.add(v);
										}
									}else{
										v=JZList_Map.get(yq[i]);
										if(v!=null){
											F_JZList.add(v);
										}
									}
									
							}
							
							HashMap<String,OutInpatientWorkVo> RYList_Map = new HashMap<String, OutInpatientWorkVo>();
							for(int y =1;y<RYList.size();y++){
								RYList_Map.put(RYList.get(y).getProjectName(), RYList.get(y));
							}
							ArrayList<OutInpatientWorkVo> F_RYList=new ArrayList<OutInpatientWorkVo>();
							for(int i=0;i<yq.length;i++){
								OutInpatientWorkVo v;
									if(i==0){
										F_RYList.add(RYList.get(0));
										v=RYList_Map.get(yq[i]);
										if(v!=null){
											F_RYList.add(v);
										}
									}else{
										v=RYList_Map.get(yq[i]);
										if(v!=null){
											F_RYList.add(v);
										}	
									}
									
							}

							HashMap<String,OutInpatientWorkVo> CYList_Map = new HashMap<String, OutInpatientWorkVo>();
							for(int y =1;y<CYList.size();y++){
								CYList_Map.put(CYList.get(y).getProjectName(), CYList.get(y));
							}
							ArrayList<OutInpatientWorkVo> F_CYList=new ArrayList<OutInpatientWorkVo>();
							for(int i=0;i<yq.length;i++){
								OutInpatientWorkVo v;
									if(i==0){
										F_CYList.add(CYList.get(0));
										v=CYList_Map.get(yq[i]);
										if(v!=null){
											F_CYList.add(v);
										}
									}else{
										v=CYList_Map.get(yq[i]);
										if(v!=null){
											F_CYList.add(v);
										}
									}
									
							}
							
							HashMap<String,OutInpatientWorkVo> BCIList_Map = new HashMap<String, OutInpatientWorkVo>();
							for(int y =1;y<BCIList.size();y++){
								BCIList_Map.put(BCIList.get(y).getProjectName(), BCIList.get(y));
							}
							ArrayList<OutInpatientWorkVo> F_BCIList=new ArrayList<OutInpatientWorkVo>();
							for(int i=0;i<yq.length;i++){
								OutInpatientWorkVo v;
									if(i==0){
										F_BCIList.add(BCIList.get(0));
										v=BCIList_Map.get(yq[i]);
										if(v!=null){
											F_BCIList.add(v);
										}
									}else{
										v=BCIList_Map.get(yq[i]);
										if(v!=null){
											F_BCIList.add(v);
										}
									}
							}
							
							HashMap<String,OutInpatientWorkVo> ZYDList_Map = new HashMap<String, OutInpatientWorkVo>();
							for(int y =1;y<ZYDList.size();y++){
								ZYDList_Map.put(ZYDList.get(y).getProjectName(), ZYDList.get(y));
							}
							ArrayList<OutInpatientWorkVo> F_ZYDList=new ArrayList<OutInpatientWorkVo>();
							for(int i=0;i<yq.length;i++){
								OutInpatientWorkVo v;
									if(i==0){
										F_ZYDList.add(ZYDList.get(0));
										v=ZYDList_Map.get(yq[i]);
										if(v!=null){
											F_ZYDList.add(v);
										}
									}else{
										v=ZYDList_Map.get(yq[i]);
										if(v!=null){
											F_ZYDList.add(v);
										}
									}
							}
							
						
							//最终list把以上各项放入要返回的list中
							ArrayList<OutInpatientWorkVo> list = new ArrayList<OutInpatientWorkVo>();			
							
							//list.addAll(MJZlist);//门急诊总人次list
							//对是院区的前边分别加指定空格
							for(OutInpatientWorkVo v:F_MJZlist){
								if(v.getProjectName().endsWith("院区")){
									v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
								}
								list.add(v);
							}
							
							//list.addAll(MZWList);//门诊实际工作日list
							for(OutInpatientWorkVo v:F_MZWList){
								if(v.getProjectName().endsWith("院区")){
									v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
								}
								list.add(v);
							}
							
							//list.addAll(RAVGlist);//日平均门急诊人次list
							for(OutInpatientWorkVo v:F_RAVGlist){
								if(v.getProjectName().endsWith("院区")){
									v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
								}
								list.add(v);
							}
							
							//list.addAll(MZList);//门诊人次list
							if(F_JZList.size()>1){//加这个是因为当急诊人次的长度是1时，加的空格会变成双倍，很郁闷
								for(OutInpatientWorkVo v:F_MZList){
									if(v.getProjectName().endsWith("院区")){
										v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
									}
									list.add(v);
								}
							}else{
								list.addAll(F_MZList);//门诊人次list
							}
							
							
							//list.addAll(JZList);//急诊人次list
							for(OutInpatientWorkVo v:F_JZList){
								if(v.getProjectName().endsWith("院区")){
									v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
								}
								list.add(v);
							}
							
							//list.addAll(RYList);//入院人次list
							for(OutInpatientWorkVo v:F_RYList){
								if(v.getProjectName().endsWith("院区")){
									v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
								}
								list.add(v);
							}
							
							//list.addAll(CYList);//出院人次list
							for(OutInpatientWorkVo v:F_CYList){
								if(v.getProjectName().endsWith("院区")){
									v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
								}
								list.add(v);
							}
							//list.addAll(BCIList);//病床使用率(%)list\
							for(OutInpatientWorkVo v:F_BCIList){
								if(v.getProjectName().endsWith("院区")){
									v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
								}
								list.add(v);
							}
							//list.addAll(ZYDList);//平均住院天数list
							
							for(OutInpatientWorkVo v:F_ZYDList){
								if(v.getProjectName().endsWith("院区")){
									v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
								}
								list.add(v);
							}
						
							return list;
		}
	
	
	
	//急诊人次：科室统计--门诊住院工作同期对比表    按月存：急诊人次，时间，院区，标记
	//入院人次：科室统计--门诊住院工作同期对比表    按月存：入院人次，时间，院区，标记
	//出院人次：科室统计--门诊住院工作同期对比表     按月存：出院人次,时间(月),时间和(出院时间-入院时间),院区，标记
	//病床使用率：科室统计--门诊住院工作同期对比表    按天：当天是占有状态的床位数 ，当天总的床位数，院区，标记
	public List<OutInpatientWorkVo> outInpatientWorkListByMongo(String Btime,String Etime,String areaCode) throws Exception{
		/*******************************************开始计算时间相关的****************************************************************/
		
		//以下的查询条件全是：>=开始时间，<结束时间
		String beginTime01=null;//查询前一年的开始时间
		String endTime01=null;//查询前一年的结束时间
		String BLastDay =null;//查询前一年的天数，用于计算平均值
		String beginTime02=null;//查询年的开始时间
		String endTime02=null;//查询年的结束时间
		String ELastDay=null;//查询年的天数，用于计算平均值
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//前一年的开始时间，结束时间
		
		int beforeOne=Integer.parseInt(Etime.substring(0, 4))-1;//查询年的前一年的int类型
		if(!StringUtils.isNotBlank(Btime)){//当Btime为空时，Etime不为空，是查询指定的某月
			beginTime01 = beforeOne+Etime.substring(4, 7);//如2016-05
			BLastDay    = getLastDay(beforeOne+Etime.substring(4, 7));
			endTime01=dateFormat.format(DateUtils.addDay(dateFormat.parse(beforeOne+Etime.substring(4, 7)+"-"+BLastDay), 1))+" 00:00:00";//如2016-06-01 00:00:00下个月的开始时间
			endTime01=endTime01.substring(0, 7);////如2016-06
			
			beginTime02 =Etime;//如2017-05
			ELastDay=getLastDay(Etime);
			
			//结束时间加一天，因为sql对于结束时间是<
			endTime02=dateFormat.format(DateUtils.addDay(dateFormat.parse(Etime+"-"+ELastDay), 1))+" 00:00:00";//如2017-06-01 00:00:00
			endTime02=endTime02.substring(0, 7);////如2017-06
			
		}else{
			beginTime01 = beforeOne+Btime.substring(4, 7)+"-01"+" 00:00:00";//如2016-01-01 00:00:00
			String lastDay01=getLastDay(beforeOne+Etime.substring(4, 7));//查询的前一年结束时间的月份的天数
			endTime01=dateFormat.format(DateUtils.addDay(dateFormat.parse( beforeOne+Etime.substring(4, 7)+"-"+lastDay01), 1))+" 00:00:00";//如2016-06-01 00:00:00
			
			Date date01=dateFormat2.parse(beginTime01);
			Date date02= dateFormat2.parse(endTime01);
			Long Btimes=date02.getTime()-date01.getTime();
			Integer Bdays =(int) (Btimes/(86400000));//得到天数//
			BLastDay=Bdays.toString();//得到查询的时间区间
			
			//以上代码是计算查询的时间区间(单位天数)
			beginTime01=beginTime01.substring(0,7);//2016-01,正式格式的开始时间
			endTime01=endTime01.substring(0, 7);//2016-06,正式格式的结束时间
			
			beginTime02 = Btime+"-01"+" 00:00:00";//如2017-01-01 00:00:00
			String lastDay02=getLastDay(Etime);//查询年的结束时间的月份的天数
			endTime02=dateFormat.format(DateUtils.addDay(dateFormat.parse(Etime+"-"+lastDay02), 1))+" 00:00:00";//如2017-06-01 00:00:00
			
			Date date03=dateFormat2.parse(beginTime02);
			Date date04= dateFormat2.parse(endTime02);
			Long Etimes=date04.getTime()-date03.getTime();
			Integer Edays =(int) (Etimes/(86400000));//得到天数
			ELastDay=Edays.toString();//得到查询的时间区间
			
			//以上代码是计算查询的时间区间(单位天数)
			beginTime02=beginTime02.substring(0,7);//2016-01,正式格式的开始时间
			endTime02=endTime02.substring(0, 7);//2016-06,正式格式的结束时间
			
		}
		/*******************************************结束计算时间相关的****************************************************************/
		
		/*******************************************开始构建mongo的查询条件****************************************************************/
		BasicDBObject bdObject01 = new BasicDBObject();//前一年的条件
		BasicDBObject bdObject01TimeS01 = new BasicDBObject();
		BasicDBObject bdObject01TimeE01 = new BasicDBObject();
		BasicDBList condList01 = new BasicDBList(); 
		
		BasicDBList condList = new BasicDBList();//共用
		
		BasicDBObject bdObject02 = new BasicDBObject();//查询年的条件
		BasicDBObject bdObject01TimeS02 = new BasicDBObject();
		BasicDBObject bdObject01TimeE02 = new BasicDBObject();
		BasicDBList condList02 = new BasicDBList(); 
		
		
		if(StringUtils.isNotBlank(areaCode)){
				String[] areaCodes=areaCode.split(",");//1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区'
				String codeName="河医院区";//默认
				for(String aCode: areaCodes){
					if("2".equals(aCode)){
						codeName="郑东院区";
					}else if("3".equals(aCode)){
						codeName="惠济院区";
					}
					condList.add(new BasicDBObject("projectName", codeName));
				}
				bdObject01.put("$or", condList);
				bdObject02.put("$or", condList);

		}
		
		if(StringUtils.isNotBlank(beginTime01)&&StringUtils.isNotBlank(beginTime02)){
			
			//大于等于开始时间
			bdObject01TimeS01.put("dateTime", new BasicDBObject("$gte",beginTime01));
			bdObject01TimeS02.put("dateTime", new BasicDBObject("$gte",beginTime02));
			condList01.add(bdObject01TimeS01);
			condList02.add(bdObject01TimeS02);
		}
		
		if(StringUtils.isNotBlank(endTime01)&&StringUtils.isNotBlank(endTime02)){
			
			//小于结束时间
			bdObject01TimeE01.put("dateTime", new BasicDBObject("$lt",endTime01));
			bdObject01TimeE02.put("dateTime", new BasicDBObject("$lt",endTime02));
			condList01.add(bdObject01TimeE01);
			condList02.add(bdObject01TimeE02);
		}
		if(StringUtils.isNotBlank(beginTime01)||StringUtils.isNotBlank(endTime01)){
			bdObject01.put("$and", condList01);
			bdObject02.put("$and", condList02);
		}
		/*******************************************结束构建mongo的查询条件****************************************************************/
		
		/**********************************************开始计算门诊人次*********************************************************/
		
		//门诊人次：科室统计--门诊住院工作同期对比表    按月存：门诊人次，挂号时间，院区，标记
		bdObject01.put("flag", "KSTJ_MZRC");//门诊人次标记
		bdObject02.put("flag", "KSTJ_MZRC");//门诊人次标记
		DBCursor cursor01=new MongoBasicDao().findAlldata(TABLENAME,bdObject01);
		DBCursor cursor02=new MongoBasicDao().findAlldata(TABLENAME,bdObject02);
		List<OutInpatientWorkVo> outInpatientWorkVoList001= new ArrayList<OutInpatientWorkVo>();
		List<OutInpatientWorkVo> outInpatientWorkVoList002= new ArrayList<OutInpatientWorkVo>();
		
		OutInpatientWorkVo MZVo = null;//门诊人次
		
		while (cursor01.hasNext()) {
			DBObject dbCursor = cursor01.next();
			MZVo = new OutInpatientWorkVo();
			MZVo.setNum(dbCursor.get("num").toString());
			MZVo.setProjectName(dbCursor.get("projectName").toString());
			outInpatientWorkVoList001.add(MZVo);
		}
		while (cursor02.hasNext()) {
			DBObject dbCursor = cursor02.next();
			MZVo = new OutInpatientWorkVo();
			MZVo.setNum(dbCursor.get("num").toString());
			MZVo.setProjectName(dbCursor.get("projectName").toString());
			outInpatientWorkVoList002.add(MZVo);
		}
		
		//去重，相同的院区要相加
		List<OutInpatientWorkVo> outInpatientWorkVoList01= new ArrayList<OutInpatientWorkVo>();
		Map<String,OutInpatientWorkVo> map01 = new LinkedHashMap<String, OutInpatientWorkVo>();
		List<OutInpatientWorkVo> outInpatientWorkVoList02= new ArrayList<OutInpatientWorkVo>();
		Map<String,OutInpatientWorkVo> map02 = new LinkedHashMap<String, OutInpatientWorkVo>();
		if(!StringUtils.isNotBlank(Btime)){//当Btime为null时,是不需要去重的(sql就是按月分组查的)
			outInpatientWorkVoList01=outInpatientWorkVoList001;
			outInpatientWorkVoList02=outInpatientWorkVoList002;
		}else{
			for(OutInpatientWorkVo v:outInpatientWorkVoList001){
				if(map01.containsKey(v.getProjectName())){//已存在
					OutInpatientWorkVo vo = map01.get(v.getProjectName());
					Double d = Double.parseDouble(vo.getNum());
					Double b = Double.parseDouble(v.getNum());
					vo.setNum(NumberUtil.init().format(new Double(d+b), 0));//让其结果想相加
					map01.put(vo.getProjectName(), vo);
				}else{
					OutInpatientWorkVo vo = new OutInpatientWorkVo();//从新创建一个，避免改变集合中元素的值
					vo.setProjectName(v.getProjectName());
					vo.setNum(v.getNum());
					map01.put(vo.getProjectName(), vo);
				}
			}
			
			//遍历map,存入新的list 
			Set<Entry<String,OutInpatientWorkVo>> entrySet01 = map01.entrySet();
			for(Entry e:entrySet01){
				outInpatientWorkVoList01.add((OutInpatientWorkVo)e.getValue());
			}
			
			for(OutInpatientWorkVo v:outInpatientWorkVoList002){
				if(map02.containsKey(v.getProjectName())){//已存在
					OutInpatientWorkVo vo = map02.get(v.getProjectName());
					Double d = Double.parseDouble(vo.getNum());
					Double b = Double.parseDouble(v.getNum());
					vo.setNum(NumberUtil.init().format(new Double(d+b), 0));//让其结果想相加
					map02.put(vo.getProjectName(), vo);
				}else{
					OutInpatientWorkVo vo = new OutInpatientWorkVo();//从新创建一个，避免改变集合中元素的值
					vo.setProjectName(v.getProjectName());
					vo.setNum(v.getNum());
					map02.put(vo.getProjectName(), vo);
				}
			}
			
			//遍历map,寸入新的list 
			Set<Entry<String,OutInpatientWorkVo>> entrySet02 = map02.entrySet();
			for(Entry e:entrySet02){
				outInpatientWorkVoList02.add((OutInpatientWorkVo)e.getValue());
			}
		}
		
		//复制orcale中对应的代码进行计算
		//算出门诊人次
		List<OutInpatientWorkVo> MZList = new ArrayList<OutInpatientWorkVo>();//门诊list集合
		MZVo = new OutInpatientWorkVo();//门诊人次
		String MZProName="门诊人次";
		int num01=0;//前一年人次
		int num02=0;//查询年的人次
		if(outInpatientWorkVoList01!=null){
			for(OutInpatientWorkVo v:outInpatientWorkVoList01){
				num01+=Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getNum()), 0));
			}
		}
		if(outInpatientWorkVoList02!=null){
			for(OutInpatientWorkVo v:outInpatientWorkVoList02){
				num02+=Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getNum()), 0));
			}
		}
		
		MZVo.setProjectName(MZProName);//项目
		
		if(num01==0){//说明16年没数据
			MZVo.setBeginNum("-");//前一年人次
			if(num02==0){//说明查询年没数据
				MZVo.setEndNum("-");//查询年也没数据
				MZVo.setIncreaseNum("-");//增减数没数据
				MZVo.setIncreasePercent("-");//增减%没数据
				MZList.add(MZVo);
			}else{
				MZVo.setEndNum(new Integer(num02).toString());//查询年的人次(有人次)
				MZVo.setIncreaseNum("-");//增减数没数据
				MZVo.setIncreasePercent("-");//增减%没数据
				MZList.add(MZVo);
			}
		}else{
			MZVo.setBeginNum(new Integer(num01).toString());//前一年人次
			if(num02==0){//说明查询年没数据
				MZVo.setEndNum("-");//查询年也没数据
				MZVo.setIncreaseNum("-");//增减数没数据
				MZVo.setIncreasePercent("-");//增减%没数据
				MZList.add(MZVo);
			}else{
				MZVo.setEndNum(new Integer(num02).toString());//查询年的人次(有人次)
				String ct= new Integer(num02-num01).toString();

				MZVo.setIncreaseNum(ct);//增减数有数据
				//一些列转类型，为了防止计算三百分比时丢失精度
				Double mid= new Integer(num02-num01).doubleValue();
				Double dNum01=new Integer(num01).doubleValue();
				Double e= new Double((mid/dNum01)*100);
				MZVo.setIncreasePercent(NumberUtil.init().format(e, 1));

				MZList.add(MZVo);
			}
		}
		/****************************************************开始计算门诊实际工作日*******************************************************************/
		List<OutInpatientWorkVo> MZWList = new ArrayList<OutInpatientWorkVo>();//门诊实际工作日
		OutInpatientWorkVo workVo01 = new OutInpatientWorkVo();
		workVo01.setProjectName("门诊实际工作日");//项目
		workVo01.setBeginNum(BLastDay);//前一年
		workVo01.setEndNum(ELastDay);//查询年
		
		//转型计算
		Double cDay = Double.parseDouble(ELastDay)-Double.parseDouble(BLastDay);
		workVo01.setIncreaseNum(NumberUtil.init().format(cDay,0));//增减数
		Double bDay = Double.parseDouble(BLastDay);
		Double percent =(cDay/bDay)*100;
		workVo01.setIncreasePercent(NumberUtil.init().format(percent,1));

		MZWList.add(workVo01);
		
			//分别求出各个院区的门诊人次
			if(outInpatientWorkVoList01!=null&&outInpatientWorkVoList02!=null){
						for(OutInpatientWorkVo vo02:outInpatientWorkVoList02){
							OutInpatientWorkVo vo03 = new OutInpatientWorkVo();
							if(outInpatientWorkVoList01.size()>0){
								flag:for(OutInpatientWorkVo vo01:outInpatientWorkVoList01){
									if(vo02.getProjectName()!=null){
										if(vo02.getProjectName().equals(vo01.getProjectName())){
											vo03.setProjectName(vo02.getProjectName());
											vo03.setBeginNum(vo01.getNum());
											vo03.setEndNum(vo02.getNum());
											Double c= Double.parseDouble(vo02.getNum())-Double.parseDouble(vo01.getNum());
											Double n =Double.parseDouble(vo01.getNum());
											Double p =(c/n)*100;
											vo03.setIncreaseNum(NumberUtil.init().format(c, 0));//增减数
											vo03.setIncreasePercent(NumberUtil.init().format(p, 1));
											
											vo01.setFlag("ok");//ok随便定义的，用于记录该值已被计算,代表在outInpatientWorkVoList01中也出现了outInpatientWorkVoList02的元素
											break flag;//满足条件的就停止循环
										}else{//不满足，设置默认值
											vo03.setProjectName(vo02.getProjectName());
											vo03.setBeginNum("-");
											vo03.setEndNum(vo02.getNum());
											vo03.setIncreaseNum("-");
											vo03.setIncreasePercent("-");
										}
									}
								}
							}else{//当outInpatientWorkVoList01的长度为0
								vo03.setProjectName(vo02.getProjectName());
								vo03.setBeginNum("-");
								vo03.setEndNum(vo02.getNum());
								vo03.setIncreaseNum("-");
								vo03.setIncreasePercent("-");
							}
							OutInpatientWorkVo workVo02 = new OutInpatientWorkVo();
							workVo02.setProjectName(vo02.getProjectName());//院区名
							workVo02.setBeginNum(BLastDay);//前年工作日
							workVo02.setEndNum(ELastDay);//查询年工作日
							//转型计算
							Double cp = Double.parseDouble(ELastDay)-Double.parseDouble(BLastDay);
							Double bp = Double.parseDouble(BLastDay);
							Double pp =(cp/bp)*100;
							workVo02.setIncreaseNum(NumberUtil.init().format(cp, 0));//增减数
							workVo02.setIncreasePercent(NumberUtil.init().format(pp, 1));
							
							MZWList.add(workVo02);
							
							MZList.add(vo03);
						}
					
			}
			if(outInpatientWorkVoList01.size()>0){//遍历不在list02的院区
					for(OutInpatientWorkVo vo01:outInpatientWorkVoList01){
						if("ok".equals(vo01.getFlag())){//flag是oK,已经被计算过
							vo01.setFlag(null);//设置为原来的状态，不影响后边的使用
						}else{
							OutInpatientWorkVo vo03 = new OutInpatientWorkVo();
							vo03.setProjectName(vo01.getProjectName());
							vo03.setBeginNum(vo01.getNum());
							vo03.setEndNum("-");
							vo03.setIncreaseNum("-");
							vo03.setIncreasePercent("-");
							MZList.add(vo03);
			    /*******************************************************结束计算门诊人次******************************************************************/
							OutInpatientWorkVo workVo02 = new OutInpatientWorkVo();
							workVo02.setProjectName(vo01.getProjectName());//院区名
							workVo02.setBeginNum(BLastDay);//前年工作日
							workVo02.setEndNum(ELastDay);//查询年工作日
							
							//转型计算
							Double cp = Double.parseDouble(ELastDay)-Double.parseDouble(BLastDay);
							Double bp = Double.parseDouble(BLastDay);
							Double pp =(cp/bp)*100;
							workVo02.setIncreaseNum(NumberUtil.init().format(cp, 0));//增减数
							workVo02.setIncreasePercent(NumberUtil.init().format(pp, 1));
							MZWList.add(workVo02);
			    /*******************************************************结束计算门诊实际工作日******************************************************************/
						}
					}
					
				}

		/*******************************************************结束计算门诊人次**********************************************************************/
		
		/**********************************************开始计算急诊人次*********************************************************/
				
			//入院人次：科室统计--门诊住院工作同期对比表    按月存：入院人次，时间，院区，标记
				bdObject01.remove("flag");//先删除之前的查询条件
				bdObject02.remove("flag");
				
				bdObject01.put("flag", "KSTJ_JZRC");//急诊人次标记
				bdObject02.put("flag", "KSTJ_JZRC");//急诊人次标记
				DBCursor cursor03=new MongoBasicDao().findAlldata(TABLENAME,bdObject01);
				DBCursor cursor04=new MongoBasicDao().findAlldata(TABLENAME,bdObject02);
				List<OutInpatientWorkVo> outInpatientWorkVoList003= new ArrayList<OutInpatientWorkVo>();
				List<OutInpatientWorkVo> outInpatientWorkVoList004= new ArrayList<OutInpatientWorkVo>();
				
				OutInpatientWorkVo JZVo=null;
				
				while (cursor03.hasNext()) {
					DBObject dbCursor = cursor03.next();
					JZVo = new OutInpatientWorkVo();
					JZVo.setNum(dbCursor.get("num").toString());
					JZVo.setProjectName(dbCursor.get("projectName").toString());
					outInpatientWorkVoList003.add(JZVo);
				}
				while (cursor04.hasNext()) {
					DBObject dbCursor = cursor04.next();
					JZVo = new OutInpatientWorkVo();
					JZVo.setNum(dbCursor.get("num").toString());
					JZVo.setProjectName(dbCursor.get("projectName").toString());
					outInpatientWorkVoList004.add(JZVo);
				}
				
				//去重，相同的院区要相加
				List<OutInpatientWorkVo> outInpatientWorkVoList03= new ArrayList<OutInpatientWorkVo>();
				Map<String,OutInpatientWorkVo> map03 = new LinkedHashMap<String, OutInpatientWorkVo>();
				List<OutInpatientWorkVo> outInpatientWorkVoList04= new ArrayList<OutInpatientWorkVo>();
				Map<String,OutInpatientWorkVo> map04 = new LinkedHashMap<String, OutInpatientWorkVo>();
				if(!StringUtils.isNotBlank(Btime)){//当Btime为null时,是不需要去重的(sql就是按月分组查的)
					outInpatientWorkVoList03=outInpatientWorkVoList003;
					outInpatientWorkVoList04=outInpatientWorkVoList004;
				}else{
					for(OutInpatientWorkVo v:outInpatientWorkVoList003){
						if(map03.containsKey(v.getProjectName())){//已存在
							OutInpatientWorkVo vo = map03.get(v.getProjectName());
							Double d = Double.parseDouble(vo.getNum());
							Double b = Double.parseDouble(v.getNum());
							vo.setNum(NumberUtil.init().format(new Double(d+b), 0));//让其结果想相加
							map03.put(vo.getProjectName(), vo);
						}else{
							OutInpatientWorkVo vo = new OutInpatientWorkVo();
							vo.setProjectName(v.getProjectName());
							vo.setNum(v.getNum());
							map03.put(vo.getProjectName(), vo);
						}
					}
					
					//遍历map,寸入新的list 
					Set<Entry<String,OutInpatientWorkVo>> entrySet01 = map03.entrySet();
					for(Entry e:entrySet01){
						outInpatientWorkVoList03.add((OutInpatientWorkVo)e.getValue());
					}
					
					for(OutInpatientWorkVo v:outInpatientWorkVoList004){
						if(map04.containsKey(v.getProjectName())){//已存在
							OutInpatientWorkVo vo = map04.get(v.getProjectName());
							Double d = Double.parseDouble(vo.getNum());
							Double b = Double.parseDouble(v.getNum());
							vo.setNum(NumberUtil.init().format(new Double(d+b), 0));//让其结果想相加
							map04.put(vo.getProjectName(), vo);
						}else{
							OutInpatientWorkVo vo = new OutInpatientWorkVo();
							vo.setProjectName(v.getProjectName());
							vo.setNum(v.getNum());
							map04.put(vo.getProjectName(), vo);
						}
					}
					
					//遍历map,寸入新的list 
					Set<Entry<String,OutInpatientWorkVo>> entrySet02 = map04.entrySet();
					for(Entry e:entrySet02){
						outInpatientWorkVoList04.add((OutInpatientWorkVo)e.getValue());
					}
				}
				
				//复制orcale中对应的代码进行计算
				//算出急诊人次
				List<OutInpatientWorkVo> JZList = new ArrayList<OutInpatientWorkVo>();//急诊人次List
				String JZProName="急诊人次";
				int num03=0;//前一年人次
				int num04=0;//查询年的人次
				if(outInpatientWorkVoList03!=null){
					for(OutInpatientWorkVo v:outInpatientWorkVoList03){
						num03+=Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getNum()), 0));
					}
				}
				if(outInpatientWorkVoList04!=null){
					for(OutInpatientWorkVo v:outInpatientWorkVoList04){
						num04+=Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getNum()), 0));
					}
				}
				JZVo = new OutInpatientWorkVo();//急诊人次
				JZVo.setProjectName(JZProName);//项目
				if(num03==0){//说明16年没数据
					JZVo.setBeginNum("-");//前一年人次
					if(num04==0){//说明查询年没数据
						JZVo.setEndNum("-");//查询年也没数据
						JZVo.setIncreaseNum("-");//增减数没数据
						JZVo.setIncreasePercent("-");//增减%没数据
						JZList.add(JZVo);
					}else{
						JZVo.setEndNum(new Integer(num04).toString());//查询年的人次(有人次)
						JZVo.setIncreaseNum("-");//增减数没数据
						JZVo.setIncreasePercent("-");//增减%没数据
						JZList.add(JZVo);
					}
				}else{
					JZVo.setBeginNum(new Integer(num03).toString());//前一年人次
					if(num04==0){//说明查询年没数据
						JZVo.setEndNum("-");//查询年也没数据
						JZVo.setIncreaseNum("-");//增减数没数据
						JZVo.setIncreasePercent("-");//增减%没数据
						JZList.add(JZVo);
					}else{
						JZVo.setEndNum(new Integer(num04).toString());//查询年的人次(有人次)
						JZVo.setIncreaseNum(NumberUtil.init().format(new Integer(num04-num03).doubleValue(), 0));//增减数有数据
						
						//一些列转类型，为了防止计算三百分比时丢失精度
						Double mid= new Integer(num04-num03).doubleValue();
						Double dnum03=new Integer(num03).doubleValue();
						JZVo.setIncreasePercent(NumberUtil.init().format(new Double((mid/dnum03)*100), 1));//增减%有数据
						JZList.add(JZVo);
					}
				}
				
				//根据以上，算出各个院区的急诊人次
				if(outInpatientWorkVoList03!=null&&outInpatientWorkVoList04!=null){
								for(OutInpatientWorkVo vo04:outInpatientWorkVoList04){
									OutInpatientWorkVo vo05 = new OutInpatientWorkVo();
									if(outInpatientWorkVoList03.size()>0){
										flag:for(OutInpatientWorkVo vo03:outInpatientWorkVoList03){
											if(vo04.getProjectName()!=null){
												if(vo04.getProjectName().equals(vo03.getProjectName())){
													vo05.setProjectName(vo04.getProjectName());
													vo05.setBeginNum(vo03.getNum());
													vo05.setEndNum(vo04.getNum());
													Double c= Double.parseDouble(vo04.getNum())-Double.parseDouble(vo03.getNum());
													Double n =Double.parseDouble(vo03.getNum());
													Double p =(c/n)*100;
													vo05.setIncreaseNum(NumberUtil.init().format(c, 0));
													vo05.setIncreasePercent(NumberUtil.init().format(p, 1));
													
													vo03.setFlag("ok");
													break flag;//满足条件的就停止循环
												}else{//不满足，设置默认值
													vo05.setProjectName(vo04.getProjectName());
													vo05.setBeginNum("-");
													vo05.setEndNum(vo04.getNum());
													vo05.setIncreaseNum("-");
													vo05.setIncreasePercent("-");
												}
											}
										}
									}else{//outInpatientWorkVoList03的长度为0
										vo05.setProjectName(vo04.getProjectName());
										vo05.setBeginNum("-");
										vo05.setEndNum(vo04.getNum());
										vo05.setIncreaseNum("-");
										vo05.setIncreasePercent("-");
									}
									
									JZList.add(vo05);
								}
						}

				if(outInpatientWorkVoList03.size()>0){//遍历不在list02的院区
					for(OutInpatientWorkVo vo03:outInpatientWorkVoList03){
						if("ok".equals(vo03.getFlag())){//flag是oK
							vo03.setFlag(null);//设置到以前的状态，不影响后边的计算
						}else{
							OutInpatientWorkVo vo05 = new OutInpatientWorkVo();
							vo05.setProjectName(vo03.getProjectName());
							vo05.setBeginNum(vo03.getNum());
							vo05.setEndNum("-");
							vo05.setIncreaseNum("-");
							vo05.setIncreasePercent("-");
							
							JZList.add(vo05);
						}
					}
				}
				
		/*******************************************************结束计算急诊人次******************************************************************/
				
		/*******************************************************开始计算门急诊总人次******************************************************************/
				
				//根据以上，算出门诊总急诊人次
				OutInpatientWorkVo MJZVo = new OutInpatientWorkVo();//门急诊总人次
				List<OutInpatientWorkVo> MJZlist = new ArrayList<OutInpatientWorkVo>();//门急诊总人次list
				String MJZProName= "门急诊总人次";
				MJZVo.setProjectName(MJZProName);//项目
				String mzNum=MZVo.getBeginNum();//门诊人次，"-"或者字符串数字
				String jzNum=JZVo.getBeginNum();//急诊人次，"-"或者字符串数字
				if("-".equals(mzNum)){
					if("-".equals(jzNum)){
						MJZVo.setBeginNum("-");//前一年人次
					}else{
						MJZVo.setBeginNum(jzNum);//前一年人次
					}
				}else{
					if("-".equals(jzNum)){
						MJZVo.setBeginNum(mzNum);//前一年人次
					}else{
						MJZVo.setBeginNum(new Integer(Integer.parseInt(mzNum)+Integer.parseInt(jzNum)).toString());//前一年人次
					}
				}
				String meNum=MZVo.getEndNum();//门诊人次，"-"或者字符串数字
				String jeNum=JZVo.getEndNum();//急诊人次，"-"或者字符串数字
				if("-".equals(meNum)){
					if("-".equals(jeNum)){
						MJZVo.setEndNum("-");//前一年人次
					}else{
						MJZVo.setEndNum(jeNum);//前一年人次
					}
				}else{
					if("-".equals(jeNum)){
						MJZVo.setEndNum(meNum);//前一年人次
					}else{
						MJZVo.setEndNum(new Integer(Integer.parseInt(meNum)+Integer.parseInt(jeNum)).toString());//前一年人次
					}
				}
				
				if("-".equals(MJZVo.getBeginNum())||"-".equals(MJZVo.getEndNum())){
					MJZVo.setIncreaseNum("-");//增减数
					MJZVo.setIncreasePercent("-");//增减%
					MJZlist.add(MJZVo);
				}else{
					Double c =new Double(MJZVo.getEndNum())-new Double(MJZVo.getBeginNum());
					Double n= new Double(MJZVo.getBeginNum());
					Double p= (c/n)*100;
					MJZVo.setIncreaseNum(NumberUtil.init().format(c, 0));
					MJZVo.setIncreasePercent(NumberUtil.init().format(p, 1));
					MJZlist.add(MJZVo);
				}
				
				
				//门诊人次集合:MZList 急诊人次集合:JZList
				//遍历，分别得到每个院区的门急诊人次
				if(MZList!=null&&JZList!=null){
						for(int i=1;i<MZList.size();i++){//第一个是合计，因此从第二个遍历
							OutInpatientWorkVo vo07 = new OutInpatientWorkVo();
							OutInpatientWorkVo mz=MZList.get(i);
							if(JZList.size()>1){
								inner:for(int j=1;j<JZList.size();j++){
									if(StringUtils.isNotBlank(MZList.get(i).getProjectName())){
										if(MZList.get(i).getProjectName().equals(JZList.get(j).getProjectName())){
											OutInpatientWorkVo jz=JZList.get(j);
											vo07.setProjectName(mz.getProjectName());//项目
											String mz_Num=mz.getBeginNum();//门诊人次，"-"或者字符串数字
											String jz_Num=jz.getBeginNum();//急诊人次，"-"或者字符串数字
											if("-".equals(mz_Num)){
												if("-".equals(jz_Num)){
													vo07.setBeginNum("-");//前一年人次
												}else{
													vo07.setBeginNum(jz_Num);//前一年人次
												}
											}else{
												if("-".equals(jz_Num)){
													vo07.setBeginNum(mz_Num);//前一年人次
												}else{
													vo07.setBeginNum(new Integer(Integer.parseInt(mz_Num)+Integer.parseInt(jz_Num)).toString());//前一年人次
												}
											}
											String me_Num=mz.getEndNum();//门诊人次，"-"或者字符串数字
											String je_Num=jz.getEndNum();//急诊人次，"-"或者字符串数字
											if("-".equals(me_Num)){
												if("-".equals(je_Num)){
													vo07.setEndNum("-");//前一年人次
												}else{
													vo07.setEndNum(je_Num);//前一年人次
												}
											}else{
												if("-".equals(je_Num)){
													vo07.setEndNum(me_Num);//前一年人次
												}else{
													vo07.setEndNum(new Integer(Integer.parseInt(me_Num)+Integer.parseInt(je_Num)).toString());//前一年人次
												}
											}
											if("-".equals(vo07.getBeginNum())||"-".equals(vo07.getEndNum())){
												vo07.setIncreaseNum("-");//增减数
												vo07.setIncreasePercent("-");//增减%
											}else{
												Double c =new Double(vo07.getEndNum())-new Double(vo07.getBeginNum());
												Double n= new Double(vo07.getBeginNum());
												Double p= (c/n)*100;
												vo07.setIncreaseNum(NumberUtil.init().format(c, 0));
												vo07.setIncreasePercent(NumberUtil.init().format(p, 1));
											}
											
											jz.setFlag("ok");//代表该院区前年有，查询年也有
											break inner;
										}else{
											vo07=mz;
										}
									}
								}
							}else{//JZList的长度小于等于1
								vo07=mz;
							}
							MJZlist.add(vo07);
						}
					}
					
					//遍历JZList，得到查询年有，但是前年没有的院区
					if(JZList.size()>1){
						for(OutInpatientWorkVo jz : JZList){
							if("ok".equals(jz.getFlag())){
								jz.setFlag(null);//设置到原来的状态，不影响后边的计算
							}else{
								MJZlist.add(jz);
							}
						}
					}
				
				/*******************************************************结束计算门急诊总人次******************************************************************/
				
				/*******************************************************开始计算日平均门急诊人次******************************************************************/
				
				//根据,门急诊总人次,实际工作日得到--->日平均门急诊人次
				List<OutInpatientWorkVo> RAVGlist = new ArrayList<OutInpatientWorkVo>();
				
				if(MJZlist!=null){
					int i=1;
					for(OutInpatientWorkVo vo:MJZlist){
						OutInpatientWorkVo vo09 = new OutInpatientWorkVo();
						String bmath=vo.getBeginNum();
						if(!StringUtils.isNotBlank(bmath)){
							bmath="0";
						}
						
						String emath=vo.getEndNum();
						if(!StringUtils.isNotBlank(emath)){
							emath="0";				
						}
						if(i==1){
							vo09.setProjectName("日平均门急诊人次");
							if("-".equals(vo.getBeginNum())){
								vo09.setBeginNum("-");//前一年
							}else{
								Double bd=Double.parseDouble(bmath)/Double.parseDouble(BLastDay);
								vo09.setBeginNum(NumberUtil.init().format(bd, 1));//保留整数,不要忘了,前一年//四舍五入
							}
							if("-".equals(vo.getEndNum())){
								vo09.setEndNum("-");//查询年
							}else{
								Double bd=Double.parseDouble(emath)/Double.parseDouble(ELastDay);
								vo09.setEndNum(NumberUtil.init().format(bd, 1));//保留整数,不要忘了,前一年//四舍五入
							}
							if("-".equals(vo.getBeginNum())||"-".equals(vo.getEndNum())){
								vo09.setIncreaseNum("-");
								vo09.setIncreasePercent("-");
							}else{
								Double c =new Double(vo09.getEndNum())-new Double(vo09.getBeginNum());
								Double n= new Double(vo09.getBeginNum());
								Double p= (c/n)*100;
								vo09.setIncreaseNum(NumberUtil.init().format(c, 1));//增减数				
								vo09.setIncreasePercent(NumberUtil.init().format(p, 1));//增减%--->保留一位小数
							}
							
						}else{
							vo09.setProjectName(vo.getProjectName());
							if("-".equals(vo.getBeginNum())){
								vo09.setBeginNum("-");//前一年
							}else{
								Double bd=Double.parseDouble(bmath)/Double.parseDouble(BLastDay);
								vo09.setBeginNum(NumberUtil.init().format(bd, 1));//保留整数,不要忘了,前一年//四舍五入
							}
							if("-".equals(vo.getEndNum())){
								vo09.setEndNum("-");//查询年
							}else{
								Double bd=Double.parseDouble(emath)/Double.parseDouble(ELastDay);
								vo09.setEndNum(NumberUtil.init().format(bd, 1));//保留整数,不要忘了,前一年//四舍五入
							}
							if("-".equals(vo.getBeginNum())||"-".equals(vo.getEndNum())){
								vo09.setIncreaseNum("-");
								vo09.setIncreasePercent("-");
							}else{
								Double c =new Double(vo09.getEndNum())-new Double(vo09.getBeginNum());
								Double n= new Double(vo09.getBeginNum());
								Double p= (c/n)*100;
								vo09.setIncreaseNum(NumberUtil.init().format(c, 1));//增减数				
								vo09.setIncreasePercent(NumberUtil.init().format(p, 1));//增减%--->保留一位小数
							}
							
						}
						i++;
						RAVGlist.add(vo09);
					}
					
				}
			/*******************************************************结束计算日平均门急诊人次******************************************************************/
			
			/*******************************************************开始计算入院人次***********************************************************************/	
				bdObject01.remove("flag");//先删除之前的查询条件
				bdObject02.remove("flag");
				
				bdObject01.put("flag", "KSTJ_RYRC");//入院人次标记
				bdObject02.put("flag", "KSTJ_RYRC");//入院人次标记
				DBCursor cursor05=new MongoBasicDao().findAlldata(TABLENAME,bdObject01);
				DBCursor cursor06=new MongoBasicDao().findAlldata(TABLENAME,bdObject02);
				List<OutInpatientWorkVo> outInpatientWorkVoList005= new ArrayList<OutInpatientWorkVo>();
				List<OutInpatientWorkVo> outInpatientWorkVoList006= new ArrayList<OutInpatientWorkVo>();
				
				OutInpatientWorkVo RYVo=null;
				
				while (cursor05.hasNext()) {
					DBObject dbCursor = cursor05.next();
					RYVo = new OutInpatientWorkVo();
					RYVo.setNum(dbCursor.get("num").toString());
					RYVo.setProjectName(dbCursor.get("projectName").toString());
					outInpatientWorkVoList005.add(RYVo);
				}
				while (cursor06.hasNext()) {
					DBObject dbCursor = cursor06.next();
					RYVo = new OutInpatientWorkVo();
					RYVo.setNum(dbCursor.get("num").toString());
					RYVo.setProjectName(dbCursor.get("projectName").toString());
					outInpatientWorkVoList006.add(RYVo);
				}
				
				//去重，相同的院区要相加
				List<OutInpatientWorkVo> outInpatientWorkVoList05= new ArrayList<OutInpatientWorkVo>();
				Map<String,OutInpatientWorkVo> map05 = new LinkedHashMap<String, OutInpatientWorkVo>();
				List<OutInpatientWorkVo> outInpatientWorkVoList06= new ArrayList<OutInpatientWorkVo>();
				Map<String,OutInpatientWorkVo> map06 = new LinkedHashMap<String, OutInpatientWorkVo>();
				if(!StringUtils.isNotBlank(Btime)){//当Btime为null时,是不需要去重的(sql就是按月分组查的)
					outInpatientWorkVoList05=outInpatientWorkVoList005;
					outInpatientWorkVoList06=outInpatientWorkVoList006;
				}else{
					for(OutInpatientWorkVo v:outInpatientWorkVoList005){
						if(map05.containsKey(v.getProjectName())){//已存在
							OutInpatientWorkVo vo = map05.get(v.getProjectName());
							Double d = Double.parseDouble(vo.getNum());
							Double b = Double.parseDouble(v.getNum());
							vo.setNum(NumberUtil.init().format(new Double(d+b), 0));//让其结果想相加
							map05.put(vo.getProjectName(), vo);
						}else{
							OutInpatientWorkVo vo= new OutInpatientWorkVo();
							vo.setProjectName(v.getProjectName());
							vo.setNum(v.getNum());
							map05.put(vo.getProjectName(), vo);
						}
					}
					//遍历map,寸入新的list 
					Set<Entry<String,OutInpatientWorkVo>> entrySet01 = map05.entrySet();
					for(Entry e:entrySet01){
						outInpatientWorkVoList05.add((OutInpatientWorkVo)e.getValue());
					}
					
					for(OutInpatientWorkVo v:outInpatientWorkVoList006){
						if(map06.containsKey(v.getProjectName())){//已存在
							OutInpatientWorkVo vo = map06.get(v.getProjectName());
							Double d = Double.parseDouble(vo.getNum());
							Double b = Double.parseDouble(v.getNum());
							vo.setNum(NumberUtil.init().format(new Double(d+b), 0));//让其结果想相加
							map06.put(vo.getProjectName(), vo);
						}else{
							OutInpatientWorkVo vo= new OutInpatientWorkVo();
							vo.setProjectName(v.getProjectName());
							vo.setNum(v.getNum());
							map06.put(vo.getProjectName(), vo);
						}
					}
					
					//遍历map,寸入新的list 
					Set<Entry<String,OutInpatientWorkVo>> entrySet02 = map06.entrySet();
					for(Entry e:entrySet02){
						outInpatientWorkVoList06.add((OutInpatientWorkVo)e.getValue());
					}
				}
				
				//复制上边oracle的代码
				// 算出入院人次
				List<OutInpatientWorkVo> RYList = new ArrayList<OutInpatientWorkVo>();// 入院人次List
				String RYProName = "入院人次";
				int num05 = 0;// 前一年人次
				int num06 = 0;// 查询年的人次
				if (outInpatientWorkVoList05 != null) {
					for (OutInpatientWorkVo v : outInpatientWorkVoList05) {
						num05 +=Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getNum()), 0));
					}
				}
				if (outInpatientWorkVoList06 != null) {
					for (OutInpatientWorkVo v : outInpatientWorkVoList06) {
						num06 += Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getNum()), 0));
					}
				}
				RYVo = new OutInpatientWorkVo();// 入院人次
				
				RYVo.setProjectName(RYProName);// 项目
				if(num05==0){//说明16年没数据
					RYVo.setBeginNum("-");//前一年人次
					if(num06==0){//说明查询年没数据
						RYVo.setEndNum("-");//查询年也没数据
						RYVo.setIncreaseNum("-");//增减数没数据
						RYVo.setIncreasePercent("-");//增减%没数据
						RYList.add(RYVo);
					}else{
						RYVo.setEndNum(new Integer(num06).toString());//查询年的人次(有人次)
						RYVo.setIncreaseNum("-");//增减数没数据
						RYVo.setIncreasePercent("-");//增减%没数据
						RYList.add(RYVo);
					}
				}else{
					RYVo.setBeginNum(new Integer(num05).toString());//前一年人次
					if(num06==0){//说明查询年没数据
						RYVo.setEndNum("-");//查询年也没数据
						RYVo.setIncreaseNum("-");//增减数没数据
						RYVo.setIncreasePercent("-");//增减%没数据
						RYList.add(RYVo);
					}else{
						RYVo.setEndNum(new Integer(num06).toString());//查询年的人次(有人次)
						Double str=new Integer(num06-num05).doubleValue();
						RYVo.setIncreaseNum(NumberUtil.init().format(str, 0));//增减数有数据
						
						//一些列转类型，为了防止计算三百分比时丢失精度
						Double mid= new Integer(num06-num05).doubleValue();
						Double dnum05=new Integer(num05).doubleValue();
						RYVo.setIncreasePercent(NumberUtil.init().format(new Double((mid/dnum05)*100), 1));//增减%有数据
						RYList.add(RYVo);
					}
				}
				
				if(outInpatientWorkVoList05!=null&&outInpatientWorkVoList06!=null){
					for(OutInpatientWorkVo vo06:outInpatientWorkVoList06){
						OutInpatientWorkVo vo10 = new OutInpatientWorkVo();
						if(outInpatientWorkVoList05.size()>0){
							flag:for(OutInpatientWorkVo vo05:outInpatientWorkVoList05){
								if(vo06.getProjectName()!=null){
									if(vo06.getProjectName().equals(vo05.getProjectName())){
										vo10.setProjectName(vo06.getProjectName());
										vo10.setBeginNum(vo05.getNum());
										vo10.setEndNum(vo06.getNum());
										Double c= Double.parseDouble(vo06.getNum())-Double.parseDouble(vo05.getNum());
										Double n =Double.parseDouble(vo05.getNum());
										Double p =(c/n)*100;
										vo10.setIncreaseNum(NumberUtil.init().format(c, 0));//增减数
										vo10.setIncreasePercent(NumberUtil.init().format(p, 0));
										
										vo05.setFlag("ok");//ok代表两个集合的交集元素
										break flag;//满足条件的就停止循环
									}else{//不满足，设置默认值
										vo10.setProjectName(vo06.getProjectName());
										vo10.setBeginNum("-");
										vo10.setEndNum(vo06.getNum());
										vo10.setIncreaseNum("-");
										vo10.setIncreasePercent("-");
									}
								}
							}
						}else{//outInpatientWorkVoList05的长度等于0
							vo10.setProjectName(vo06.getProjectName());
							vo10.setBeginNum("-");
							vo10.setEndNum(vo06.getNum());
							vo10.setIncreaseNum("-");
							vo10.setIncreasePercent("-");
						}
						RYList.add(vo10);
					}
				}

				if(outInpatientWorkVoList05.size()>0){
					for(OutInpatientWorkVo vo05:outInpatientWorkVoList05){
						if("ok".equals(vo05.getFlag())){
							vo05.setFlag(null);//回到原始状态，不影响后边的计算
						}else{
							OutInpatientWorkVo vo10 = new OutInpatientWorkVo();
							vo10.setProjectName(vo05.getProjectName());
							vo10.setBeginNum(vo05.getNum());
							vo10.setEndNum("-");
							vo10.setIncreaseNum("-");
							vo10.setIncreasePercent("-");
							RYList.add(vo10);
						}
					}
					
				}		
				
			/*******************************************************结束计算入院人次***********************************************************************/	
				
			/*******************************************************开始计算出院人次***********************************************************************/	
				bdObject01.remove("flag");//先删除之前的查询条件
				bdObject02.remove("flag");
				
				bdObject01.put("flag", "KSTJ_CYRC");//出院人次标记
				bdObject02.put("flag", "KSTJ_CYRC");//出院人次标记
				DBCursor cursor07=new MongoBasicDao().findAlldata(TABLENAME,bdObject01);
				DBCursor cursor08=new MongoBasicDao().findAlldata(TABLENAME,bdObject02);
				List<OutInpatientWorkVo> outInpatientWorkVoList007= new ArrayList<OutInpatientWorkVo>();
				List<OutInpatientWorkVo> outInpatientWorkVoList008= new ArrayList<OutInpatientWorkVo>();
				
				OutInpatientWorkVo CYVo=null;
				
				while (cursor07.hasNext()) {
					DBObject dbCursor = cursor07.next();
					CYVo = new OutInpatientWorkVo();
					CYVo.setNum(dbCursor.get("num").toString());
					CYVo.setProjectName(dbCursor.get("projectName").toString());
					
					//计算该月该院区的总的住院天数
					CYVo.setvSum(dbCursor.get("vSum").toString());
					outInpatientWorkVoList007.add(CYVo);
				}
				while (cursor08.hasNext()) {
					DBObject dbCursor = cursor08.next();
					CYVo = new OutInpatientWorkVo();
					CYVo.setNum(dbCursor.get("num").toString());
					CYVo.setProjectName(dbCursor.get("projectName").toString());
					
					//计算该月该院区的总的住院天数
					CYVo.setvSum(dbCursor.get("vSum").toString());
					outInpatientWorkVoList008.add(CYVo);
				}
				
				//去重，相同的院区要相加
				List<OutInpatientWorkVo> outInpatientWorkVoList07= new ArrayList<OutInpatientWorkVo>();
				Map<String,OutInpatientWorkVo> map07 = new LinkedHashMap<String, OutInpatientWorkVo>();
				List<OutInpatientWorkVo> outInpatientWorkVoList08= new ArrayList<OutInpatientWorkVo>();
				Map<String,OutInpatientWorkVo> map08 = new LinkedHashMap<String, OutInpatientWorkVo>();
				if(!StringUtils.isNotBlank(Btime)){//当Btime为null时,是不需要去重的(sql就是按月分组查的)
					outInpatientWorkVoList07=outInpatientWorkVoList007;
					outInpatientWorkVoList08=outInpatientWorkVoList008;
				}else{
					for(OutInpatientWorkVo v:outInpatientWorkVoList007){
						if(map07.containsKey(v.getProjectName())){//已存在
							OutInpatientWorkVo vo = map07.get(v.getProjectName());
							Double d = Double.parseDouble(vo.getNum());
							Double b = Double.parseDouble(v.getNum());
							vo.setNum(NumberUtil.init().format(new Double(d+b), 0));//让其结果想相加
							Double a=Double.parseDouble(vo.getvSum());
							Double c=Double.parseDouble(v.getvSum());
							vo.setvSum(NumberUtil.init().format(new Double(a+c), 0));
							map07.put(vo.getProjectName(), vo);
						}else{
							OutInpatientWorkVo vo = new OutInpatientWorkVo();
							vo.setProjectName(v.getProjectName());
							vo.setNum(v.getNum());
							vo.setvSum(v.getvSum());
							map07.put(vo.getProjectName(), vo);
						}
					}
				
					//遍历map,寸入新的list 
					Set<Entry<String,OutInpatientWorkVo>> entrySet01 = map07.entrySet();
					for(Entry e:entrySet01){
						outInpatientWorkVoList07.add((OutInpatientWorkVo)e.getValue());
					}
					
					for(OutInpatientWorkVo v:outInpatientWorkVoList008){
						if(map08.containsKey(v.getProjectName())){//已存在
							OutInpatientWorkVo vo = map08.get(v.getProjectName());
							Double d = Double.parseDouble(vo.getNum());
							Double b = Double.parseDouble(v.getNum());
							vo.setNum(NumberUtil.init().format(new Double(d+b), 0));//让其结果想相加
							Double a=Double.parseDouble(vo.getvSum());
							Double c=Double.parseDouble(v.getvSum());
							vo.setvSum(NumberUtil.init().format(new Double(a+c), 0));
							map08.put(vo.getProjectName(), vo);
						}else{
							OutInpatientWorkVo vo = new OutInpatientWorkVo();
							vo.setProjectName(v.getProjectName());
							vo.setNum(v.getNum());
							vo.setvSum(v.getvSum());
							map08.put(vo.getProjectName(), vo);
						}
					}
					
					//遍历map,寸入新的list 
					Set<Entry<String,OutInpatientWorkVo>> entrySet02 = map08.entrySet();
					for(Entry e:entrySet02){
						outInpatientWorkVoList08.add((OutInpatientWorkVo)e.getValue());
					}
				}
				
				//复制上边oracle的代码
				// 算出入院人次
				List<OutInpatientWorkVo> CYList = new ArrayList<OutInpatientWorkVo>();// 出院人次List
				String CYProName = "出院人次";
				int num07 = 0;// 前一年人次
				int num08 = 0;// 查询年的人次
				if (outInpatientWorkVoList07 != null) {
					for (OutInpatientWorkVo v : outInpatientWorkVoList07) {
						num07 += Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getNum()), 0));
					}
				}
				if (outInpatientWorkVoList08 != null) {
					for (OutInpatientWorkVo v : outInpatientWorkVoList08) {
						num08 += Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getNum()), 0));
					}
				}
				CYVo = new OutInpatientWorkVo();// 出院人次
				CYVo.setProjectName(CYProName);// 项目
				if(num07==0){//说明16年没数据
					CYVo.setBeginNum("-");//前一年人次
					if(num08==0){//说明查询年没数据
						CYVo.setEndNum("-");//查询年也没数据
						CYVo.setIncreaseNum("-");//增减数没数据
						CYVo.setIncreasePercent("-");//增减%没数据
						CYList.add(CYVo);
					}else{
						CYVo.setEndNum(new Integer(num08).toString());//查询年的人次(有人次)
						CYVo.setIncreaseNum("-");//增减数没数据
						CYVo.setIncreasePercent("-");//增减%没数据
						CYList.add(CYVo);
					}
				}else{
					CYVo.setBeginNum(new Integer(num07).toString());//前一年人次
					if(num08==0){//说明查询年没数据
						CYVo.setEndNum("-");//查询年也没数据
						CYVo.setIncreaseNum("-");//增减数没数据
						CYVo.setIncreasePercent("-");//增减%没数据
						CYList.add(CYVo);
					}else{
						CYVo.setEndNum(new Integer(num08).toString());//查询年的人次(有人次)
						Double cy=new Integer(num08-num07).doubleValue();
						CYVo.setIncreaseNum(NumberUtil.init().format(cy, 0));//增减数有数据
						
						//一些列转类型，为了防止计算三百分比时丢失精度
						Double mid= new Integer(num08-num07).doubleValue();
						Double dnum07=new Integer(num07).doubleValue();
						CYVo.setIncreasePercent(NumberUtil.init().format(new Double((mid/dnum07)*100), 1));//增减%有数据
						CYList.add(CYVo);
					}
				}
				
				if(outInpatientWorkVoList07!=null&&outInpatientWorkVoList08!=null){
					for(OutInpatientWorkVo vo08:outInpatientWorkVoList08){
						OutInpatientWorkVo vo12 = new OutInpatientWorkVo();
						if(outInpatientWorkVoList07.size()>0){
							flag:for(OutInpatientWorkVo vo07:outInpatientWorkVoList07){
								if(vo08.getProjectName()!=null){
									if(vo08.getProjectName().equals(vo07.getProjectName())){
										vo12.setProjectName(vo08.getProjectName());
										vo12.setBeginNum(vo07.getNum());
										vo12.setEndNum(vo08.getNum());
										Double c= Double.parseDouble(vo08.getNum())-Double.parseDouble(vo07.getNum());
										Double n =Double.parseDouble(vo07.getNum());
										Double p =(c/n)*100;
										vo12.setIncreaseNum(NumberUtil.init().format(c, 0));//增减数
										vo12.setIncreasePercent(NumberUtil.init().format(p, 0));
										
										vo07.setFlag("ok");//两个集合的交集
										break flag;//满足条件的就停止循环
									}else{//不满足，设置默认值
										vo12.setProjectName(vo08.getProjectName());
										vo12.setBeginNum("-");
										vo12.setEndNum(vo08.getNum());
										vo12.setIncreaseNum("-");
										vo12.setIncreasePercent("-");
									}
								}
							}
						}else{//outInpatientWorkVoList07的 长度为0
							vo12.setProjectName(vo08.getProjectName());
							vo12.setBeginNum("-");
							vo12.setEndNum(vo08.getNum());
							vo12.setIncreaseNum("-");
							vo12.setIncreasePercent("-");
						}
						CYList.add(vo12);
					}
	
				}	
			
				//得到前年的集合中有，查询年没有的数据
				if(outInpatientWorkVoList07.size()>0){
					for(OutInpatientWorkVo vo07:outInpatientWorkVoList07){
						if("ok".equals(vo07.getFlag())){
							vo07.setFlag(null);//设置为null,不影响后边的计算
						}else{
							OutInpatientWorkVo vo12 = new OutInpatientWorkVo();
							vo12.setProjectName(vo07.getProjectName());
							vo12.setBeginNum(vo07.getNum());
							vo12.setEndNum("-");
							vo12.setIncreaseNum("-");
							vo12.setIncreasePercent("-");
							CYList.add(vo12);
						}
					}
				}

			/*******************************************************结束计算出院人次***********************************************************************/	
				
		
			/*******************************************************开始计算平均住院天数***********************************************************************/	
				// 算出平均住院天数
				List<OutInpatientWorkVo> ZYDList = new ArrayList<OutInpatientWorkVo>();// 平均住院天数List
				String ZYDProName = "平均住院天数";
				int num09 = 0;// 前一年总天数
				int num10 = 0;// 查询年的总天数
				if (outInpatientWorkVoList07 != null) {
					for (OutInpatientWorkVo v : outInpatientWorkVoList07) {
						num09 += Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getvSum()), 0));
					}
				}
				if (outInpatientWorkVoList08 != null) {
					for (OutInpatientWorkVo v : outInpatientWorkVoList08) {
						num10 += Integer.parseInt(NumberUtil.init().format(Double.parseDouble(v.getvSum()), 0));
					}
				}
				OutInpatientWorkVo ZYDo = new OutInpatientWorkVo();// 平均住院天数
				ZYDo.setProjectName(ZYDProName);// 项目
				if(num09==0){//说明16年没数据
					ZYDo.setBeginNum("-");//前一年人次
					if(num10==0){//说明查询年没数据
						ZYDo.setEndNum("-");//查询年也没数据
						ZYDo.setIncreaseNum("-");//增减数没数据
						ZYDo.setIncreasePercent("-");//增减%没数据
						ZYDList.add(ZYDo);
					}else{
						double value = new Integer(num10).doubleValue();
						Double avg= value/(Double.parseDouble(CYList.get(0).getEndNum()));//求出查询一年的平均值
						ZYDo.setEndNum(NumberUtil.init().format(avg, 1));
						ZYDo.setIncreaseNum("-");//增减数没数据
						ZYDo.setIncreasePercent("-");//增减%没数据
						ZYDList.add(ZYDo);
					}
				}else{
					double value = new Integer(num09).doubleValue();
					Double avg= value/(Double.parseDouble(CYList.get(0).getBeginNum()));//求出查询一年的平均值
					ZYDo.setBeginNum(NumberUtil.init().format(avg, 1));//前一年的平均住院天数
					if(num10==0){//说明查询年没数据
						ZYDo.setEndNum("-");//查询年也没数据
						ZYDo.setIncreaseNum("-");//增减数没数据
						ZYDo.setIncreasePercent("-");//增减%没数据
						ZYDList.add(ZYDo);
					}else{
						double v = new Integer(num10).doubleValue();
						Double g= value/(Double.parseDouble(CYList.get(0).getEndNum()));//求出查询一年的平均值
						ZYDo.setEndNum(NumberUtil.init().format(g, 1));//查询年的平均住院天数
						Double dnf= new Double(ZYDo.getEndNum())-new Double(ZYDo.getBeginNum());
						String ccyy=NumberUtil.init().format(dnf, 1);
						ZYDo.setIncreaseNum(ccyy);//增减数有数据
						
						//一些列转类型，为了防止计算三百分比时丢失精度
						Double peret=(dnf/new Double(ZYDo.getBeginNum()))*100;
						ZYDo.setIncreasePercent(NumberUtil.init().format(peret, 1));//增减%有数据
						ZYDList.add(ZYDo);
					}
				}
				
				//前一年的list(院区，各个院区院区的平均住院天数)
				List<OutInpatientWorkVo> outInpatientWorkVoList11 = new ArrayList<OutInpatientWorkVo>();
				for(OutInpatientWorkVo vo07:outInpatientWorkVoList07){
					OutInpatientWorkVo v= new OutInpatientWorkVo();
					Double a = Double.parseDouble(vo07.getNum());//出院人次
					Double b = Double.parseDouble(vo07.getvSum());//该院区对应的总的住院天数
					Double c= b/a;//平均住院天数
					v.setBeginNum(c.toString());
					v.setProjectName(vo07.getProjectName());
					outInpatientWorkVoList11.add(v);
				}
				
				//查询年年的list(院区，各个院区院区的平均住院天数)
				List<OutInpatientWorkVo> outInpatientWorkVoList12= new ArrayList<OutInpatientWorkVo>();
				for(OutInpatientWorkVo vo08:outInpatientWorkVoList08){
					OutInpatientWorkVo v= new OutInpatientWorkVo();
					Double a = Double.parseDouble(vo08.getNum());//出院人次
					Double b = Double.parseDouble(vo08.getvSum());//该院区对应的总的住院天数
					Double c= b/a;//平均住院天数
					v.setEndNum(c.toString());
					v.setProjectName(vo08.getProjectName());
					outInpatientWorkVoList12.add(v);
				}
				
				//根据以上，算出各个院区的平均住院天数
				if(outInpatientWorkVoList11!=null&&outInpatientWorkVoList12!=null){
							for(OutInpatientWorkVo vo12:outInpatientWorkVoList12){
											OutInpatientWorkVo vo16 = new OutInpatientWorkVo();
											if(outInpatientWorkVoList11.size()>0){
												flag:for(OutInpatientWorkVo vo11:outInpatientWorkVoList11){
													if(vo12.getProjectName()!=null){
														if(vo12.getProjectName().equals(vo11.getProjectName())){
															vo16.setProjectName(vo12.getProjectName());
															String bt=vo11.getBeginNum();
															vo16.setBeginNum(NumberUtil.init().format(Double.parseDouble(bt), 1));
															String et=vo12.getEndNum();
															vo16.setEndNum(NumberUtil.init().format(Double.parseDouble(et), 1));
															Double c= Double.parseDouble(vo12.getEndNum())-Double.parseDouble(vo11.getBeginNum());
															Double n =Double.parseDouble(vo11.getBeginNum());
															Double p =(c/n)*100;
															vo16.setIncreaseNum(NumberUtil.init().format(c, 1));
															vo16.setIncreasePercent(NumberUtil.init().format(p, 1));
															
															vo11.setFlag("ok");//两个集合的交集加标记
															break flag;//满足条件的就停止循环
														}else{//不满足，设置默认值
															vo16.setProjectName(vo12.getProjectName());
															vo16.setBeginNum("-");
															String et=vo12.getEndNum();
															vo16.setEndNum(NumberUtil.init().format(Double.parseDouble(et),1));
															vo16.setIncreaseNum("-");
															vo16.setIncreasePercent("-");
														}
													}
												}
											}else{//outInpatientWorkVoList11的 长度为0
												vo16.setProjectName(vo12.getProjectName());
												vo16.setBeginNum("-");
												String et=vo12.getEndNum();
												vo16.setEndNum(NumberUtil.init().format(Double.parseDouble(et),1));
												vo16.setIncreaseNum("-");
												vo16.setIncreasePercent("-");
											}
											ZYDList.add(vo16);
							}
				  }
				
				//看上边类似模块的代码注释
				if(outInpatientWorkVoList11.size()>0){
					for(OutInpatientWorkVo vo11:outInpatientWorkVoList11){
						if("ok".equals(vo11.getFlag())){
							vo11.setFlag(null);
						}else{
							OutInpatientWorkVo vo16 = new OutInpatientWorkVo();
							vo16.setProjectName(vo11.getProjectName());
							Double d= Double.parseDouble(vo11.getBeginNum());
							vo16.setBeginNum(NumberUtil.init().format(d, 1));
							vo16.setEndNum("-");
							vo16.setIncreaseNum("-");
							vo16.setIncreasePercent("-");
							ZYDList.add(vo16);
						}
					}
					
				}
				
			/*******************************************************结束计算平均住院天数***********************************************************************/	
		
			/*******************************************************开始计算病床使用率***********************************************************************/	
				bdObject01.remove("flag");//先删除之前的查询条件
				bdObject01.remove("$and");
				bdObject01.put("flag", "KSTJ_BCSYL");//病床使用率标记
				
				DBCursor cursor09=new MongoBasicDao().findAlldata(TABLENAME,bdObject01);//因为只有当天的记录

				List<OutInpatientWorkVo> outInpatientWorkVoList13= new ArrayList<OutInpatientWorkVo>();//院区，占有状态的床位数
				List<OutInpatientWorkVo> outInpatientWorkVoList14= new ArrayList<OutInpatientWorkVo>();//院区，总的床位数
				OutInpatientWorkVo BCIVo=null;
				while (cursor09.hasNext()) {
					DBObject dbCursor = cursor09.next();
					BCIVo = new OutInpatientWorkVo();
					OutInpatientWorkVo o = new OutInpatientWorkVo();
					
					BCIVo.setNum(dbCursor.get("num").toString());
					BCIVo.setProjectName(dbCursor.get("projectName").toString());
					outInpatientWorkVoList13.add(BCIVo);
					
					o.setNum(dbCursor.get("vSum").toString());
					o.setProjectName(dbCursor.get("projectName").toString());
					outInpatientWorkVoList14.add(o);
					
				}
				
				//复制上边oracle的代码
				//计算病床使用率(病床信息表只保存当天数据)	
				List<OutInpatientWorkVo>  BCIList= new ArrayList<OutInpatientWorkVo>();//病床使用率(%)
			    BCIVo = new OutInpatientWorkVo();//病床使用率(%)
				String BCIProName="病床使用率(%)";
				BCIVo.setProjectName(BCIProName);
				BCIVo.setBeginNum("-");
				int badCount=0;//床位总数
				int fbadCount=0;//占有状态的床位数
				if(outInpatientWorkVoList14!=null){
					for(OutInpatientWorkVo vw :outInpatientWorkVoList14){
						badCount=Integer.parseInt(vw.getNum());
					}
				}
				if(outInpatientWorkVoList13!=null){
					for(OutInpatientWorkVo vv :outInpatientWorkVoList13){
						fbadCount=Integer.parseInt(vv.getNum());
					}
				}
				if(badCount==0||fbadCount==0){
						BCIVo.setEndNum("-");
				}else{
					Double o= new Integer(badCount).doubleValue();
					Double u = new Integer(fbadCount).doubleValue();
					BCIVo.setEndNum(NumberUtil.init().format(new Double((u/o)*100),1));
				}
				BCIVo.setIncreaseNum("-");
				BCIVo.setIncreasePercent("-");
				BCIList.add(BCIVo);
				
				//分别求出各个院区的病床使用率
				if(outInpatientWorkVoList13!=null){
					for(OutInpatientWorkVo vo13:outInpatientWorkVoList13){
						OutInpatientWorkVo vo18 = new OutInpatientWorkVo();
						if(outInpatientWorkVoList14.size()>0){
							inner:for(OutInpatientWorkVo vo14:outInpatientWorkVoList14){
										if(vo13.getProjectName().equals(vo14.getProjectName())){
											vo18.setProjectName(vo13.getProjectName());
											vo18.setBeginNum("-");
											Double sumBad=Double.parseDouble(vo14.getNum());//院区的总床位数
											Double schBad = Double.parseDouble(vo13.getNum());//院区的占有床位数
											vo18.setEndNum(NumberUtil.init().format(new Double((schBad/sumBad)*100), 1));
											vo18.setIncreaseNum("-");
											vo18.setIncreasePercent("-");
											break inner;
										}
									}
						}else{
							vo18.setProjectName(vo13.getProjectName());
							vo18.setBeginNum("-");
							vo18.setEndNum("-");
							vo18.setIncreaseNum("-");
							vo18.setIncreasePercent("-");
						}
						BCIList.add(vo18);
					}
					
				}	
				

				
			/*******************************************************结束计算病床使用率***********************************************************************/

				//对院区进行排序
				String[] yq={"河医院区","郑东院区","惠济院区"};
				HashMap<String,OutInpatientWorkVo> MJZlist_Map = new HashMap<String, OutInpatientWorkVo>();
				for(int y =1;y<MJZlist.size();y++){
					MJZlist_Map.put(MJZlist.get(y).getProjectName(), MJZlist.get(y));
				}
				ArrayList<OutInpatientWorkVo> F_MJZlist=new ArrayList<OutInpatientWorkVo>();
				for(int i=0;i<yq.length;i++){
					OutInpatientWorkVo v;
						if(i==0){
							F_MJZlist.add(MJZlist.get(0));
							 v=MJZlist_Map.get(yq[i]);
							if(v!=null){
								F_MJZlist.add(v);
							}
						}else{
							v=MJZlist_Map.get(yq[i]);
							if(v!=null){
								F_MJZlist.add(v);
							}
						}
						
				}
				
				HashMap<String,OutInpatientWorkVo> MZWList_Map = new HashMap<String, OutInpatientWorkVo>();
				for(int y =1;y<MZWList.size();y++){
					MZWList_Map.put(MZWList.get(y).getProjectName(), MZWList.get(y));
				}
				ArrayList<OutInpatientWorkVo> F_MZWList=new ArrayList<OutInpatientWorkVo>();
				for(int i=0;i<yq.length;i++){
					OutInpatientWorkVo v;
						if(i==0){
							F_MZWList.add(MZWList.get(0));
							v=MZWList_Map.get(yq[i]);
							if(v!=null){
								F_MZWList.add(v);
							}
						}else{
							v=MZWList_Map.get(yq[i]);
							if(v!=null){
								F_MZWList.add(v);
							}
						}
						
				}
				
				HashMap<String,OutInpatientWorkVo> RAVGlist_Map = new HashMap<String, OutInpatientWorkVo>();
				for(int y =1;y<RAVGlist.size();y++){
					RAVGlist_Map.put(RAVGlist.get(y).getProjectName(), RAVGlist.get(y));
				}
				ArrayList<OutInpatientWorkVo> F_RAVGlist=new ArrayList<OutInpatientWorkVo>();
				for(int i=0;i<yq.length;i++){
					OutInpatientWorkVo v;
						if(i==0){
							F_RAVGlist.add(RAVGlist.get(0));
							v=RAVGlist_Map.get(yq[i]);
							if(v!=null){
								F_RAVGlist.add(v);
							}
						}else{
							v=RAVGlist_Map.get(yq[i]);
							if(v!=null){
								F_RAVGlist.add(v);
							}
						}
					
				}
				
				HashMap<String,OutInpatientWorkVo> MZList_Map = new HashMap<String, OutInpatientWorkVo>();
				for(int y =1;y<MZList.size();y++){
					MZList_Map.put(MZList.get(y).getProjectName(), MZList.get(y));
				}
				ArrayList<OutInpatientWorkVo> F_MZList=new ArrayList<OutInpatientWorkVo>();
				for(int i=0;i<yq.length;i++){
					OutInpatientWorkVo v;
						if(i==0){
							F_MZList.add(MZList.get(0));
							v=MZList_Map.get(yq[i]);
							if(v!=null){
								F_MZList.add(v);
							}
							
						}else{
							v=MZList_Map.get(yq[i]);
							if(v!=null){
								F_MZList.add(v);
							}
						}
						
				}
				
				HashMap<String,OutInpatientWorkVo> JZList_Map = new HashMap<String, OutInpatientWorkVo>();
				for(int y =1;y<JZList.size();y++){
					JZList_Map.put(JZList.get(y).getProjectName(), JZList.get(y));
				}
				ArrayList<OutInpatientWorkVo> F_JZList=new ArrayList<OutInpatientWorkVo>();
				for(int i=0;i<yq.length;i++){
					OutInpatientWorkVo v;
						if(i==0){
							F_JZList.add(JZList.get(0));
							v=JZList_Map.get(yq[i]);
							if(v!=null){
								F_JZList.add(v);
							}
						}else{
							v=JZList_Map.get(yq[i]);
							if(v!=null){
								F_JZList.add(v);
							}
						}
						
				}
				
				HashMap<String,OutInpatientWorkVo> RYList_Map = new HashMap<String, OutInpatientWorkVo>();
				for(int y =1;y<RYList.size();y++){
					RYList_Map.put(RYList.get(y).getProjectName(), RYList.get(y));
				}
				ArrayList<OutInpatientWorkVo> F_RYList=new ArrayList<OutInpatientWorkVo>();
				for(int i=0;i<yq.length;i++){
					OutInpatientWorkVo v;
						if(i==0){
							F_RYList.add(RYList.get(0));
							v=RYList_Map.get(yq[i]);
							if(v!=null){
								F_RYList.add(v);
							}
						}else{
							v=RYList_Map.get(yq[i]);
							if(v!=null){
								F_RYList.add(v);
							}	
						}
						
				}

				HashMap<String,OutInpatientWorkVo> CYList_Map = new HashMap<String, OutInpatientWorkVo>();
				for(int y =1;y<CYList.size();y++){
					CYList_Map.put(CYList.get(y).getProjectName(), CYList.get(y));
				}
				ArrayList<OutInpatientWorkVo> F_CYList=new ArrayList<OutInpatientWorkVo>();
				for(int i=0;i<yq.length;i++){
					OutInpatientWorkVo v;
						if(i==0){
							F_CYList.add(CYList.get(0));
							v=CYList_Map.get(yq[i]);
							if(v!=null){
								F_CYList.add(v);
							}
						}else{
							v=CYList_Map.get(yq[i]);
							if(v!=null){
								F_CYList.add(v);
							}
						}
						
				}
				
				HashMap<String,OutInpatientWorkVo> BCIList_Map = new HashMap<String, OutInpatientWorkVo>();
				for(int y =1;y<BCIList.size();y++){
					BCIList_Map.put(BCIList.get(y).getProjectName(), BCIList.get(y));
				}
				ArrayList<OutInpatientWorkVo> F_BCIList=new ArrayList<OutInpatientWorkVo>();
				for(int i=0;i<yq.length;i++){
					OutInpatientWorkVo v;
						if(i==0){
							F_BCIList.add(BCIList.get(0));
							v=BCIList_Map.get(yq[i]);
							if(v!=null){
								F_BCIList.add(v);
							}
						}else{
							v=BCIList_Map.get(yq[i]);
							if(v!=null){
								F_BCIList.add(v);
							}
						}
				}
				
				HashMap<String,OutInpatientWorkVo> ZYDList_Map = new HashMap<String, OutInpatientWorkVo>();
				for(int y =1;y<ZYDList.size();y++){
					ZYDList_Map.put(ZYDList.get(y).getProjectName(), ZYDList.get(y));
				}
				ArrayList<OutInpatientWorkVo> F_ZYDList=new ArrayList<OutInpatientWorkVo>();
				for(int i=0;i<yq.length;i++){
					OutInpatientWorkVo v;
						if(i==0){
							F_ZYDList.add(ZYDList.get(0));
							v=ZYDList_Map.get(yq[i]);
							if(v!=null){
								F_ZYDList.add(v);
							}
						}else{
							v=ZYDList_Map.get(yq[i]);
							if(v!=null){
								F_ZYDList.add(v);
							}
						}
				}
				
				//最终list把以上各项放入要返回的list中
				ArrayList<OutInpatientWorkVo> list = new ArrayList<OutInpatientWorkVo>();			
				//list.addAll(MJZlist);//门急诊总人次list
				//对是院区的前边分别加指定空格，为了在前台显示有缩进
				for(OutInpatientWorkVo v:F_MJZlist){
					if(v.getProjectName().endsWith("院区")){
						v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
					}
					list.add(v);
				}
				//list.addAll(MZWList);//门诊实际工作日list
				for(OutInpatientWorkVo v:F_MZWList){
					if(v.getProjectName().endsWith("院区")){
						v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
					}
					list.add(v);
				}
				
				//list.addAll(RAVGlist);//日平均门急诊人次list
				for(OutInpatientWorkVo v:F_RAVGlist){
					if(v.getProjectName().endsWith("院区")){
						v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
					}
					list.add(v);
				}
				//list.addAll(MZList);//门诊人次list
				if(F_JZList.size()>1){//加这个是因为当急诊人次的长度是1时，加的空格会变成双倍，很郁闷
					for(OutInpatientWorkVo v:F_MZList){
						if(v.getProjectName().endsWith("院区")){
							v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
						}
						list.add(v);
					}
				}else{
					list.addAll(F_MZList);//门诊人次list
				}
				
				//list.addAll(JZList);//急诊人次list
				for(OutInpatientWorkVo v:F_JZList){
					if(v.getProjectName().endsWith("院区")){
						v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
					}
					list.add(v);
				}
				//list.addAll(RYList);//入院人次list
				for(OutInpatientWorkVo v:F_RYList){
					if(v.getProjectName().endsWith("院区")){
						v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
					}
					list.add(v);
				}
				//list.addAll(CYList);//出院人次list
				for(OutInpatientWorkVo v:F_CYList){
					if(v.getProjectName().endsWith("院区")){
						v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
					}
					list.add(v);
				}
				//list.addAll(BCIList);//病床使用率(%)list\
				for(OutInpatientWorkVo v:F_BCIList){
					if(v.getProjectName().endsWith("院区")){
						v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
					}
					list.add(v);
				}
				//list.addAll(ZYDList);//平均住院天数list
				for(OutInpatientWorkVo v:F_ZYDList){
					if(v.getProjectName().endsWith("院区")){
						v.setProjectName("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+v.getProjectName());
					}
					list.add(v);
				}
			
				return list;
	}
	
	 
	/**
	 * 
	 * <p>获取某月的最后一天 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年6月7日 下午8:11:00 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月5日 下午6:11:00 
	 * @ModifyRmk:  添加注释模板
	 * @version: V1.0
	 * @param date 长度大于：yyyy-MM-dd的格式
	 * @return 返回该月的最后一天的字符串表示形式，兼容闰年
	 * @throws Exception
	 *
	 */
	public String getLastDay(String date) throws Exception {
		 date= date.substring(0, 7);
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		 Date time =null;
		 time = dateFormat.parse(date);
		 Calendar  calendar =  Calendar.getInstance(); 
		 calendar.setTime(time);
		 final  int  lastDay  =   calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
		 Date   lastDate   =   calendar.getTime();  
	     lastDate.setDate(lastDay);  
	     SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		
	     return dateFormat1.format(lastDate).substring(8, 10);
	}
	
	
	
	
	
}
