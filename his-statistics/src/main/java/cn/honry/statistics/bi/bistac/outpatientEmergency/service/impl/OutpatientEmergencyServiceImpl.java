package cn.honry.statistics.bi.bistac.outpatientEmergency.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.hospitalday.vo.HospitaldayVo;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.mongoDataInit.dao.MongoDataInitDao;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.dao.OutpatientDocRecipeDao;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.service.OutpatientDocRecipeService;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo.OutpatientDocRecipeVo;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo.StatisticsVo;
import cn.honry.statistics.bi.bistac.outpatientEmergency.dao.OutpatientEmergencyDao;
import cn.honry.statistics.bi.bistac.outpatientEmergency.service.OutpatientEmergencyService;
import cn.honry.statistics.bi.bistac.outpatientEmergency.vo.OutpatientEmergencyVo;
import cn.honry.statistics.util.customVo.CustomVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("outpatientEmergencyService")
@Transactional
@SuppressWarnings({"all"})
public class OutpatientEmergencyServiceImpl implements OutpatientEmergencyService{
	@Autowired
	@Qualifier(value = "outpatientEmergencyDao")
	private OutpatientEmergencyDao outpatientEmergencyDao;
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Override
	public OutpatientEmergencyVo getDataInfoByTime(String date) throws Exception {
		List<HospitaldayVo> list = new ArrayList<HospitaldayVo>();
		//门诊收入
		//1.转换查询时间
		Date sTime = DateUtils.parseDateY_M_D(date);
		Date eTime = DateUtils.parseDateY_M_D(date);
		//2.获取门诊数据保留时间
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		//3.获得当前时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime = df.parse(df.format(new Date()));
		//4.获得在线库数据应保留最小时间
		Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
		//5.定义常量
		List<String> tnL = new ArrayList<String>();
		//6.判断是否查询分区
		if(DateUtils.compareDate(sTime, cTime)==-1){
			if(DateUtils.compareDate(eTime, cTime)==-1){//只查询分区表
				tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",date,date);
			}else{//查询在线表及分区表
				//获取时间差（年）
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), date);
				//获取相差年份的分区集合 
				tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
				tnL.add(0,"T_REGISTER_MAIN_NOW");
			}
		}else{
			//只查询在线表
			tnL.add("T_REGISTER_MAIN_NOW");
		}	
		return outpatientEmergencyDao.getDataInfoByTime(date,tnL);
	}
	
}
