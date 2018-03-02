package cn.honry.inner.statistics.registerInfoGzltj.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
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
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.registerInfoGzltj.service.InnerRegisterInfoGzltjService;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInfoVo;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInnerVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * 挂号工作量统计预处理Service实现类
 * @author user
 *
 */
@Transactional
@Service("innerRegisterInfoGzltjService")
@SuppressWarnings({"all"})
public class InnerRegisterInfoGzltjServiceImpl implements
		InnerRegisterInfoGzltjService {

	@Autowired
	@Qualifier(value = "innerRegisterInfoGzltjDao")
	private InnerRegisterInfoGzltjDao innerRegisterInfoGzltjDao;
	
	/**
	 * 将挂号工作量统计数据存入mongodb中
	 * @param menuAlias
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * @param date 开始时间(格式为:YYYY-MM-DD)
	 */
	public void init(String menuAlias,String type,String date){
		//根据日期获取当天的统计数据,存入mongodb中
		MongoLog mong = new MongoLog();
		mong.setCountStartTime(new Date());
		List<RegisterInnerVo> list = innerRegisterInfoGzltjDao.queryRegister(date);
		if(list==null||list.size()==0){
			return;
		}
		List<DBObject> voList = new ArrayList<DBObject>();
		String dateXq = list.get(0).getDateXq();
		for (RegisterInnerVo innerVo : list) {
			RegisterInfoVo vo = new RegisterInfoVo();
			vo.setDeptCode(innerVo.getDeptCode());
			vo.setDeptName(innerVo.getDeptName());
			Integer count = innerVo.getNum();
			Double totCost = innerVo.getCost();
			vo.setCost(totCost);
			vo.setNum(count);
			switch (dateXq) {
			case "1"://周日
				vo.setSunNum(count);
				vo.setSunCost(totCost);
				break;
			case "2"://周一
				vo.setMonNum(count);
				vo.setMonCost(totCost);
				break;
			case "3"://周二	
				vo.setTueNum(count);
				vo.setTueCost(totCost);
				break;
			case "4"://周三
				vo.setWedNum(count);
				vo.setWedCost(totCost);
				break;
			case "5"://周四
				vo.setThuNum(count);
				vo.setThuCost(totCost);
				break;
			case "6"://周五
				vo.setFriNum(count);
				vo.setFriCost(totCost);
				break;
			case "7"://周六
				vo.setSatNum(count);
				vo.setSatCost(totCost);
				break;
			}
			BasicDBObject obj = new BasicDBObject();
			String json = JSONUtils.toJson(vo);
			obj.append("date", date);
			obj.append("value", json);
			obj.append("deptCode", vo.getDeptCode());
			voList.add(obj);
		}
		DBObject query = new BasicDBObject();
		query.put("date", date);
		new MongoBasicDao().remove(menuAlias+"_DAY", query);//删除原来的数据
		new MongoBasicDao().insertDataByList(menuAlias+"_DAY", voList);//添加新数据
		Date d = DateUtils.parseDateY_M_D(date);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setMenuType(menuAlias+"_DAY");
		mong.setCountEndTime(new Date());
		mong.setTotalNum(voList.size());
		mong.setCreateTime(new Date());
		innerRegisterInfoGzltjDao.save(mong);//保存日志
		
		//更新当月的统计数据
		init_MonthOrYear(menuAlias, "2", date);
		//更新当年的统计数据
		init_MonthOrYear(menuAlias, "3", date);
	}
	
	/**
	 * 计算当月或当年的数据
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 开始时间
	 */
	public void init_MonthOrYear(String menuAlias,String type,String date){
		//更新当月或当年的统计数据(从_DAY或_Month表中查询数据,重新计算)
		MongoLog mong = new MongoLog();
		mong.setCountStartTime(new Date());
		
		String dateM = date.substring(0, 7);//当月
		String dateY = date.substring(0, 4);//当年
		List<RegisterInfoVo> list =new ArrayList<>();
		BasicDBObject obj1= new BasicDBObject();
		BasicDBObject obj2= new BasicDBObject();
		String queryMenuAlias=menuAlias;//查询时用的表名称
		if("2".equals(type)){//按月统计
			obj1.append("date", new BasicDBObject("$gte",dateM+"-01"));//当月的1号
			obj2.append("date", new BasicDBObject("$lte",DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.addMonth(DateUtils.parseDateY_M_D(dateM+"-01"), 1), -1))));//当月的最后一天
			queryMenuAlias+="_DAY";//按月统计时,从日表中查询数据
		}
		if("3".equals(type)){//按年统计
			obj1.append("date", new BasicDBObject("$gte",dateY+"-01"));//当年的1月
			obj2.append("date", new BasicDBObject("$lte",dateY+"-12"));//当年的12月
			queryMenuAlias+="_MONTH";//按年统计时,从月表中查询数据
		}
		BasicDBList condList = new BasicDBList(); 
		condList.add(obj1);
		condList.add(obj2);
		BasicDBObject where= new BasicDBObject();
		where.append("$and", condList);
		
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMenuAlias, where);
		if(cursor!=null){
			while(cursor.hasNext()){
				Object object = cursor.next().get("value");
				if(object!=null){
					String s = object.toString();
					try {
						RegisterInfoVo vo = JSONUtils.fromJson(s, new TypeToken<RegisterInfoVo>(){});
						list.add(vo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
		Map<String,RegisterInfoVo> map = new HashMap<>();
		if(list.size()==0){
			return;
		}
		for (RegisterInfoVo vo : list) {
			String deptCode = vo.getDeptCode();//科室code
			RegisterInfoVo infoVo = map.get(deptCode);
			if(infoVo!=null){
				infoVo.setNum(infoVo.getNum()+vo.getNum());
				infoVo.setCost(infoVo.getCost()+vo.getCost());
				infoVo.setMonNum(infoVo.getMonNum()+vo.getMonNum());
				infoVo.setMonCost(infoVo.getMonCost()+vo.getMonCost());
				infoVo.setTueNum(infoVo.getTueNum()+vo.getTueNum());
				infoVo.setTueCost(infoVo.getTueCost()+vo.getTueCost());
				infoVo.setThuNum(infoVo.getThuNum()+vo.getThuNum());
				infoVo.setThuCost(infoVo.getThuCost()+vo.getThuCost());
				infoVo.setWedNum(infoVo.getWedNum()+vo.getWedNum());
				infoVo.setWedCost(infoVo.getWedCost()+vo.getWedCost());
				infoVo.setFriNum(infoVo.getFriNum()+vo.getFriNum());
				infoVo.setFriCost(infoVo.getFriCost()+vo.getFriCost());
				infoVo.setSunNum(infoVo.getSunNum()+vo.getSunNum());
				infoVo.setSunCost(infoVo.getSunCost()+vo.getSunCost());
				infoVo.setSatNum(infoVo.getSatNum()+vo.getSatNum());
				infoVo.setSatCost(infoVo.getSatCost()+vo.getSatCost());
			}else{
				map.put(deptCode, vo);
			}
		}
		List<DBObject> voList = new ArrayList<>();
		for(RegisterInfoVo vo:map.values()){
			BasicDBObject obj = new BasicDBObject();
			if("2".equals(type)){
				obj.append("date", dateM);
			}else if("3".equals(type)){
				obj.append("date", dateY);
			}
			String json = JSONUtils.toJson(vo);
			obj.append("value", json);
			obj.append("deptCode", vo.getDeptCode());
			voList.add(obj);
		}
		DBObject query = new BasicDBObject();
		Date d = null;
		if("2".equals(type)){
			query.put("date", dateM);
			d=DateUtils.parseDateY_M(dateM);
			menuAlias+="_MONTH";
		}
		if("3".equals(type)){
			query.put("date", dateY);
			d=DateUtils.parseDateY(dateY);
			menuAlias+="_YEAR";
		}
		new MongoBasicDao().remove(menuAlias, query);//删除原来的数据
		new MongoBasicDao().insertDataByList(menuAlias, voList);//添加新数据
		
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setMenuType(menuAlias);
		mong.setCountEndTime(new Date());
		mong.setTotalNum(voList.size());
		mong.setCreateTime(new Date());
		innerRegisterInfoGzltjDao.save(mong);//保存日志
	}
}
