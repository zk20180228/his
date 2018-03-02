package cn.honry.statistics.bi.operation.operationCostList.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.bi.operation.operationCostList.dao.OperationCostListDao;
import cn.honry.statistics.bi.operation.operationCostList.service.OperationCostListService;
import cn.honry.statistics.bi.operation.operationCostList.vo.OperationCostListvo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.bi.outpatientcost.vo.OutpatientcostVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
@Service("operationCostListService")
@Transactional
@SuppressWarnings({"all"})
public class OperationCostListServiceImpl implements OperationCostListService{
	
	@Autowired
	@Qualifier("operationCostListDao")
	private OperationCostListDao operationCostListDao;

	@Override
	public OperationCostListvo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OperationCostListvo arg0) {
		
	}

	/**
	 * 加载统计
	 * @author zhangjin
	 * @createDate：2016/8/16
	 * @version 1.0
	 */
	@Override
	public String queryOperationCost(DateVo datevo, String[] dimStringArray,
			int dateType, String dimensionValue) {
			//组织参数list：list中的元素为map
			List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
			list=ResultUtils.prepareParamList(dimensionValue);
					
			//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
			Map<String,List<String>> map=new HashMap<String, List<String>>();
			map=ResultUtils.prepareParamMap(dimensionValue);
			
			//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
			String [] voArray=new String[]{"personNum","operationnNum","feeCost","feetb","feehb","personAvg","dateAvg","drugyzb"};
			//将维度种类拆分放入到数组中
			List<OperationCostListvo> volist=operationCostListDao.queryOperationCost(dimStringArray, list, dateType,datevo);
			//定义一个key为相同维度（不包括时间）value 为List<OutpatientcostVo>
			Map<String, List<OperationCostListvo>> outMap=new HashMap<String, List<OperationCostListvo>>();
			//所有维度
			Map<String, OperationCostListvo> outMap2=new HashMap<String, OperationCostListvo>();
			List<OperationCostListvo> newlist=new ArrayList<OperationCostListvo>();
			Map<String,String> map3=new HashMap<String, String>(); 
			for(int i=0;i<volist.size();i++){
				//保存相同维度（时间以外）
				if(outMap.get(volist.get(i).getKind())!=null&&outMap!=null){
					//添加
					List<OperationCostListvo> outlist=outMap.get(volist.get(i).getKind());
					outlist.add(volist.get(i));
					outMap.put(volist.get(i).getKind(), outlist);
				}else{
					List<OperationCostListvo> li=new ArrayList<OperationCostListvo>();
					li.add(volist.get(i));
					outMap.put(volist.get(i).getKind(),li );
				}
				//保存所有维度（时间以外）
				if(outMap2.get(volist.get(i).getKind()+volist.get(i).getTimeChose().replace("/", ""))==null||outMap==null){
					outMap2.put(volist.get(i).getKind()+volist.get(i).getTimeChose().replace("/", ""),volist.get(i) );
				}
				
			}
			
			for(String key:outMap.keySet()){
				//定义一个时间key 
				Map<String,OperationCostListvo> timeMap=new HashMap<String, OperationCostListvo>();
				for(int i=0;i<outMap.get(key).size();i++){
					timeMap.put(outMap.get(key).get(i).getTimeChose().replace("/", ""),outMap.get(key).get(i));
				}
				for(String timekey:timeMap.keySet()){
					String year=timekey.substring(0, 4);
					if(dateType==1){
						if(Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0 || Integer.parseInt(year) % 400 == 0){//闰年的判断规则
							   outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/366));
							}else{
								outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/365));
							}
						int timea1=Integer.parseInt(timekey)-1;
						if(timeMap.get(String.valueOf(timea1))!=null){
							double feehb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(String.valueOf(timea1)).getFeeCost());
							double feetb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(String.valueOf(timea1)).getFeeCost());
							outMap2.get(key+timekey).setFeetb(feetb);
							outMap2.get(key+timekey).setFeehb(feehb);
						}
					}else if(dateType==2){
						//闰年的判断规则
						int quarter=Integer.parseInt(timekey.substring(4, timekey.length()));//季度
						if(Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0 || Integer.parseInt(year) % 400 == 0){
							if(quarter==1||quarter==2){
								outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/91));
							}else if(quarter==3||quarter==4){
								outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/92));
							}
						}else{
							if(quarter==2){
								outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/91));
							}else if(quarter==3||quarter==4){
								outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/92));
							}else{
								outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/90));
							}
						}
						//同比
						int timea1=Integer.parseInt(timekey)-10;
						if(timeMap.get(String.valueOf(timea1))!=null){
							double feetb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(String.valueOf(timea1)).getFeeCost());
							outMap2.get(key+timekey).setFeetb(feetb);;
						}
						//环比
						int timea2=Integer.parseInt(timekey.replace("/",""))-1;
						String timehb=String.valueOf(timea2);
						if(timehb.substring(timehb.length()-1,timehb.length())=="0"){
							timehb=String.valueOf(timea2-6);
							if(timeMap.get(timehb)!=null){
								double feehb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(timehb).getFeeCost());
								outMap2.get(key+timekey).setFeehb(feehb);;
							}
						}
					}else if(dateType==3){
						//闰年的判断规则
						int month=Integer.parseInt(timekey.substring(4, timekey.length()));//季度
						if(Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0 || Integer.parseInt(year) % 400 == 0){
							if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
								 outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/31));
							}else if(month==2){
								 outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/29));
							}else{
								 outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/30));
							}
						}else{
							if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
								 outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/31));
							}else if(month==2){
								 outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/28));
							}else{
								 outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/30));
							}
						}
						//同比
						int timea1=Integer.parseInt(timekey.replace("/",""))-100;
						if(timeMap.get(String.valueOf(timea1))!=null){
							double feetb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(String.valueOf(timea1)).getFeeCost());
							outMap2.get(key+timekey).setFeetb(feetb);
						}
						//环比
						int timea2=Integer.parseInt(timekey)-1;
						String timehb=String.valueOf(timea2);
						if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
							timehb=String.valueOf(timea2-88);
							if(timeMap.get(timehb)!=null){
								double feehb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(timehb).getFeeCost());
								outMap2.get(key+timekey).setFeehb(feehb);
							}
						}
					}else if(dateType==4){
						//闰年的判断规则
						 int two =28;//二月
						if(Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0 || Integer.parseInt(year) % 400 == 0){
							   outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/1));
							   two+=1;
							}else{
								outMap2.get(key+timekey).setDateAvg(rount(timeMap.get(timekey).getFeeCost()/1));
							}
						//同比
						int timea1=Integer.parseInt(timekey)-10000;
						if(timeMap.get(String.valueOf(timea1))!=null){
							double feetb=rount(timeMap.get(timekey).getFeetb()/timeMap.get(String.valueOf(timea1)).getFeetb());
							outMap2.get(key+timekey).setFeetb(feetb);
						}
						//环比
						String month=timekey.substring(4, 6);
						String day=timekey.substring(6, 8);
						if("04".equals(month)||"06".equals(month)||"09".equals(month)||"11".equals(month)||"02".equals(month)){
							int timea2=Integer.parseInt(timekey)-1;
							String timehb=String.valueOf(timea2);
							if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
								timehb=String.valueOf(timea2-70);
								if(timeMap.get(timehb)!=null){
									double feehb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(timehb).getFeeCost());
									outMap2.get(key+timekey).setFeehb(feehb);
								}
							}
						}else if("03".equals(month)){
							if(two==28){
								int timea2=Integer.parseInt(timekey)-1;
								String timehb=String.valueOf(timea2);
								if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
									timehb=String.valueOf(timea2-73);
									if(timeMap.get(timehb)!=null){
										double feehb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(timehb).getFeeCost());
										outMap2.get(key+timekey).setFeehb(feehb);
									}
								}
							}else{
								int timea2=Integer.parseInt(timekey)-1;
								String timehb=String.valueOf(timea2);
								if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
									timehb=String.valueOf(timea2-72);
									if(timeMap.get(timehb)!=null){
										double feehb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(timehb).getFeeCost());
										outMap2.get(key+timekey).setFeehb(feehb);
									}
								}
							}
						}else if("01".equals(month)){
							int timea2=Integer.parseInt(timekey)-1;
							String timehb=String.valueOf(timea2);
							if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
								timehb=String.valueOf(timea2-8870);
								if(timeMap.get(timehb)!=null){
									double feehb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(timehb).getFeeCost());
									outMap2.get(key+timekey).setFeehb(feehb);
								}
							}
						}else{
							int timea2=Integer.parseInt(timekey)-1;
							String timehb=String.valueOf(timea2);
							if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
								timehb=String.valueOf(timea2-71);
								if(timeMap.get(timehb)!=null){
									double feehb=rount(timeMap.get(timekey).getFeeCost()/timeMap.get(timehb).getFeeCost());
									outMap2.get(key+timekey).setFeehb(feehb);
								}
							}
						}
					}
				}	
			}
			for(String newKey:outMap2.keySet()){
				newlist.add(outMap2.get(newKey));
			}
			
			List<String> listJson=new ArrayList<String>();
			for(int i=0;i<newlist.size();i++){
				//查询出来的结果集的每一个对象转换为json
				String json=JSONUtils.toJson(volist.get(i));
				json=json.replace("kind", "ops_kind");
				String json1=ResultUtils.getnewJson(json, dateType, voArray, newlist.get(i).getTimeChose());
				//将json字符串添加到listJson中
				listJson.add(json1);
			}
			//获得最终的json字符串
			String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
			return result;
	}

	private Double rount(double d) {
		double dou= Math.round(d*100)/100.0;
	return dou;
}
}
