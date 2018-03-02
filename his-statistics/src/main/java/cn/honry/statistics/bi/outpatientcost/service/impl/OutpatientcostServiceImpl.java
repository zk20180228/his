package cn.honry.statistics.bi.outpatientcost.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BiOptFeedetail;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.bi.outpatientcost.dao.OutpatientcostDao;
import cn.honry.statistics.bi.outpatientcost.service.OutpatientcostService;
import cn.honry.statistics.bi.outpatientcost.vo.OutpatientcostVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
@Service("outpatientcostService")
@Transactional
@SuppressWarnings({"all"})
public class OutpatientcostServiceImpl implements OutpatientcostService{
	
	@Autowired
	@Qualifier(value="outpatientcostDao")
	private OutpatientcostDao outpatientcostDao;

	@Override
	public BiOptFeedetail get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(BiOptFeedetail arg0) {
		
	}

	/**
	 * @Description:页面加载时加载table
	 * @Author: zhangjin
	 * @CreateDate: 2016年7月15日
	 * @param:date：当前时间
	 * @return 
	 * @Modifier:zhangjin
	 * @ModifyDate:2016-8-22
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public String getOutpatientcostlist(DateVo datevo, String[] dimStringArray,
			int dateType, String dimensionValue) {
		//组织参数list：list中的元素为map
				List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
				list=ResultUtils.prepareParamList(dimensionValue);
						
				//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
				Map<String,List<String>> map=new HashMap<String, List<String>>();
				map=ResultUtils.prepareParamMap(dimensionValue);
				
				//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中['门诊人次','挂号费用','挂号费占比','医疗费用','医疗占比','药品费用','药品占比','合计','门诊人次费用']
				String [] voArray=new String[]{"trips","regFee","regRatio","undrugFee","undrugRatio","drugFee","drugRatio","totcost","avg"};
				//将维度种类拆分放入到数组中
				List<OutpatientcostVo> volist=outpatientcostDao.getOutpatientcostlist(dimStringArray, list, dateType,datevo);
				for(int i=0;i<volist.size();i++){
					double regRatio=rount(volist.get(i).getRegRatio());
					double undrugRatio=rount(volist.get(i).getUndrugRatio());
					double drugRatio=Math.round(volist.get(i).getDrugRatio()*100)/100.0;
					double avg=rount(volist.get(i).getAvg());
					volist.get(i).setRegRatio(regRatio);
					volist.get(i).setUndrugRatio(undrugRatio);
					volist.get(i).setDrugRatio(drugRatio);
					volist.get(i).setAvg(avg);
				}
				List<String> listJson=new ArrayList<String>();
				for(int i=0;i<volist.size();i++){
					//查询出来的结果集的每一个对象转换为json
					String json=JSONUtils.toJson(volist.get(i));
					json=json.replace("deptDimensionality", "dept_code");
					json=json.replace("feecode", "fee_stat_code");
					json=json.replace("regcode", "reglevl_code");
					String json1=ResultUtils.getnewJson(json, dateType, voArray, volist.get(i).getTimeChose());
					//将json字符串添加到listJson中
					listJson.add(json1);
				}
				//获得最终的json字符串
				String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
				return result;
	}
	/**
	 * @Description:页面加载时加载table
	 * @Author: zhangjin
	 * @CreateDate: 2016年8月4日
	 * @param:date：当前时间
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public String getOutpatientfeelist(DateVo datevo, String[] dimStringArray,
			int dateType, String dimensionValue) {
		//组织参数list：list中的元素为map
				List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
				list=ResultUtils.prepareParamList(dimensionValue);
						
				//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
				Map<String,List<String>> map=new HashMap<String, List<String>>();
				map=ResultUtils.prepareParamMap(dimensionValue);
				
				//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
				String [] voArray=new String[]{"trips","tripstb","tripshb","totcost","tottb","tothb","travgcost","travgcosttb","travgcosthb"};
				//将维度种类拆分放入到数组中
				List<OutpatientcostVo> volist=outpatientcostDao.getOutpatientfeelist(dimStringArray, list, dateType,datevo);
				//定义一个key为相同维度（不包括时间）value 为List<OutpatientcostVo>
				Map<String, List<OutpatientcostVo>> outMap=new HashMap<String, List<OutpatientcostVo>>();
				//所有维度
				Map<String, OutpatientcostVo> outMap2=new HashMap<String, OutpatientcostVo>();
				List<OutpatientcostVo> newlist=new ArrayList<OutpatientcostVo>();
				Map<String,String> map3=new HashMap<String, String>(); 
//				Map<String,Double> map2=new HashMap<String, Double>();//获取总的费用
				for(int i=0;i<volist.size();i++){
					//获取总的费用
//					if(map2.get(volist.get(i).getTimeChose())!=null&&map2!=null){
//						double de=map2.get(volist.get(i).getTimeChose())+volist.get(i).getTotcost();
//						map2.put(volist.get(i).getTimeChose(), de);
//					}else{
//						map2.put(volist.get(i).getTimeChose(), volist.get(i).getTotcost());
//					}
					//保存相同维度（时间以外）
					if(outMap.get(volist.get(i).getFeecode()+volist.get(i).getRegcode()
							+volist.get(i).getDeptDimensionality())!=null&&outMap!=null){
						//添加
						List<OutpatientcostVo> outlist=outMap.get(volist.get(i).getFeecode()+volist.get(i).getRegcode()+
								volist.get(i).getDeptDimensionality());
						outlist.add(volist.get(i));
						outMap.put(volist.get(i).getFeecode()+volist.get(i).getRegcode()+
								volist.get(i).getDeptDimensionality(), outlist);
					}else{
						List<OutpatientcostVo> li=new ArrayList<OutpatientcostVo>();
						li.add(volist.get(i));
						outMap.put(volist.get(i).getFeecode()+volist.get(i).getRegcode()+
								volist.get(i).getDeptDimensionality(),li );
					}
					//保存所有维度（时间以外）
					if(outMap2.get(volist.get(i).getFeecode()+volist.get(i).getRegcode()
							+volist.get(i).getDeptDimensionality()+volist.get(i).getTimeChose().replace("/", ""))==null||outMap==null){
						
						outMap2.put(volist.get(i).getFeecode()+volist.get(i).getRegcode()+
								volist.get(i).getDeptDimensionality()+volist.get(i).getTimeChose().replace("/", ""),volist.get(i) );
					}
					
				}
				
				for(String key:outMap.keySet()){
					//定义一个时间key 
					Map<String,OutpatientcostVo> timeMap=new HashMap<String, OutpatientcostVo>();
					for(int i=0;i<outMap.get(key).size();i++){
						timeMap.put(outMap.get(key).get(i).getTimeChose().replace("/", ""),outMap.get(key).get(i));
					}
					for(String timekey:timeMap.keySet()){
//						OutpatientcostVo timeOut=new OutpatientcostVo();
//						int trips=timeMap.get(timekey).getTrips();
						if(dateType==1){
							int timea1=Integer.parseInt(timekey)-1;
							if(timeMap.get(String.valueOf(timea1))!=null){
								double tripstb=rount(timeMap.get(timekey).getTrips()/timeMap.get(String.valueOf(timea1)).getTrips());
								double tottb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(String.valueOf(timea1)).getTotcost());
								double travgcosttb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(String.valueOf(timea1)).getTravgcost());
								outMap2.get(key+timekey).setTripstb(tripstb);
								outMap2.get(key+timekey).setTottb(tottb);
								outMap2.get(key+timekey).setTravgcosttb(travgcosttb);
								//
								outMap2.get(key+timekey).setTravgcost(rount(timeMap.get(timekey).getTravgcost()));
								//环比
								outMap2.get(key+timekey).setTripshb(tripstb);
								outMap2.get(key+timekey).setTothb(tottb);
								outMap2.get(key+timekey).setTravgcosthb(travgcosttb);
							}else{
								outMap2.get(key+timekey).setTravgcost(rount(timeMap.get(timekey).getTravgcost()));
							}
							//构成+费用
							outMap2.get(key+timekey).setTotcost(Math.round(timeMap.get(timekey).getTotcost()*100)/100.0);
//							outMap2.get(key+timekey).setCostgc(rount(timeMap.get(timekey).getTotcost()/map2.get(timekey)));
						}else if(dateType==2){
							int timea1=Integer.parseInt(timekey)-10;
							if(timeMap.get(String.valueOf(timea1))!=null){
								double tripstb=rount(timeMap.get(timekey).getTrips()/timeMap.get(String.valueOf(timea1)).getTrips());
								double tottb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(String.valueOf(timea1)).getTotcost());
								double travgcosttb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(String.valueOf(timea1)).getTravgcost());
								outMap2.get(key+timekey).setTripstb(tripstb);
								outMap2.get(key+timekey).setTottb(tottb);
								outMap2.get(key+timekey).setTravgcosttb(travgcosttb);
							}
							int timea2=Integer.parseInt(timekey.replace("/",""))-1;
							String timehb=String.valueOf(timea2);
							if(timehb.substring(timehb.length()-1,timehb.length())=="0"){
								timehb=String.valueOf(timea2-6);
								if(timeMap.get(timehb)!=null){
									double tripshb=rount(timeMap.get(timekey).getTrips()/timeMap.get(timehb).getTrips());
									double tothb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(timehb).getTotcost());
									double travgcosthb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(timehb).getTravgcost());
									outMap2.get(key+timekey).setTripshb(tripshb);
									outMap2.get(key+timekey).setTothb(tothb);
									outMap2.get(key+timekey).setTravgcosthb(travgcosthb);
								}
							}
//							outMap2.get(key+timekey).setCostgc(rount(timeMap.get(timekey).getTotcost()/map2.get(timekey)));
						}else if(dateType==3){
							int timea1=Integer.parseInt(timekey.replace("/",""))-100;
							
							if(timeMap.get(String.valueOf(timea1))!=null){
								double tripstb=rount(timeMap.get(timekey).getTrips()/timeMap.get(String.valueOf(timea1)).getTrips());
								double tottb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(String.valueOf(timea1)).getTotcost());
								double travgcosttb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(String.valueOf(timea1)).getTravgcost());
								outMap2.get(key+timekey).setTripstb(tripstb);
								outMap2.get(key+timekey).setTottb(tottb);
								outMap2.get(key+timekey).setTravgcosttb(travgcosttb);
							}
							int timea2=Integer.parseInt(timekey)-1;
							String timehb=String.valueOf(timea2);
							if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
								timehb=String.valueOf(timea2-88);
								if(timeMap.get(timehb)!=null){
									double tripshb=rount(timeMap.get(timekey).getTrips()/timeMap.get(timehb).getTrips());
									double tothb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(timehb).getTotcost());
									double travgcosthb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(timehb).getTravgcost());
									outMap2.get(key+timekey).setTripshb(tripshb);
									outMap2.get(key+timekey).setTothb(tothb);
									outMap2.get(key+timekey).setTravgcosthb(travgcosthb);
								}
							}
//							outMap2.get(key+timekey).setCostgc(rount(timeMap.get(timekey).getTotcost()/map2.get(timekey)));
						}else if(dateType==4){
							int timea1=Integer.parseInt(timekey)-10000;
							if(timeMap.get(String.valueOf(timea1))!=null){
								double tripstb=rount(timeMap.get(timekey).getTrips()/timeMap.get(String.valueOf(timea1)).getTrips());
								double tottb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(String.valueOf(timea1)).getTotcost());
								double travgcosttb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(String.valueOf(timea1)).getTravgcost());
								outMap2.get(key+timekey).setTripstb(tripstb);
								outMap2.get(key+timekey).setTottb(tottb);
								outMap2.get(key+timekey).setTravgcosttb(travgcosttb);
							}
							//环比
							int two =28;
							String year=timekey.substring(0, 4);
							String month=timekey.substring(4, 6);
							String day=timekey.substring(6, 8);
							if(Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0 
									|| Integer.parseInt(year) % 400 == 0){
							   two+=1;
							}
							if("04".equals(month)||"06".equals(month)||"09".equals(month)||"11".equals(month)||"02".equals(month)){
								int timea2=Integer.parseInt(timekey)-1;
								String timehb=String.valueOf(timea2);
								if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
									timehb=String.valueOf(timea2-70);
									if(timeMap.get(timehb)!=null){
										double tripshb=rount(timeMap.get(timekey).getTrips()/timeMap.get(timehb).getTrips());
										double tothb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(timehb).getTotcost());
										double travgcosthb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(timehb).getTravgcost());
										outMap2.get(key+timekey).setTripshb(tripshb);
										outMap2.get(key+timekey).setTothb(tothb);
										outMap2.get(key+timekey).setTravgcosthb(travgcosthb);
									}
								}
							}else if("03".equals(month)){
								if(two==28){
									int timea2=Integer.parseInt(timekey)-1;
									String timehb=String.valueOf(timea2);
									if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
										timehb=String.valueOf(timea2-73);
										if(timeMap.get(timehb)!=null){
											double tripshb=rount(timeMap.get(timekey).getTrips()/timeMap.get(timehb).getTrips());
											double tothb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(timehb).getTotcost());
											double travgcosthb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(timehb).getTravgcost());
											outMap2.get(key+timekey).setTripshb(tripshb);
											outMap2.get(key+timekey).setTothb(tothb);
											outMap2.get(key+timekey).setTravgcosthb(travgcosthb);
										}
									}
								}else{
									int timea2=Integer.parseInt(timekey)-1;
									String timehb=String.valueOf(timea2);
									if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
										timehb=String.valueOf(timea2-72);
										if(timeMap.get(timehb)!=null){
											double tripshb=rount(timeMap.get(timekey).getTrips()/timeMap.get(timehb).getTrips());
											double tothb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(timehb).getTotcost());
											double travgcosthb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(timehb).getTravgcost());
											outMap2.get(key+timekey).setTripshb(tripshb);
											outMap2.get(key+timekey).setTothb(tothb);
											outMap2.get(key+timekey).setTravgcosthb(travgcosthb);
										}
									}
								}
							}else if("01".equals(month)){
								int timea2=Integer.parseInt(timekey)-1;
								String timehb=String.valueOf(timea2);
								if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
									timehb=String.valueOf(timea2-8870);
									if(timeMap.get(timehb)!=null){
										double tripshb=rount(timeMap.get(timekey).getTrips()/timeMap.get(timehb).getTrips());
										double tothb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(timehb).getTotcost());
										double travgcosthb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(timehb).getTravgcost());
										outMap2.get(key+timekey).setTripshb(tripshb);
										outMap2.get(key+timekey).setTothb(tothb);
										outMap2.get(key+timekey).setTravgcosthb(travgcosthb);
									}
								}
							}else{
								int timea2=Integer.parseInt(timekey)-1;
								String timehb=String.valueOf(timea2);
								if(timehb.substring(timehb.length()-2,timehb.length())=="00"){
									timehb=String.valueOf(timea2-71);
									if(timeMap.get(timehb)!=null){
										double tripshb=rount(timeMap.get(timekey).getTrips()/timeMap.get(timehb).getTrips());
										double tothb=rount(timeMap.get(timekey).getTotcost()/timeMap.get(timehb).getTotcost());
										double travgcosthb=rount(timeMap.get(timekey).getTravgcost()/timeMap.get(timehb).getTravgcost());
										outMap2.get(key+timekey).setTripshb(tripshb);
										outMap2.get(key+timekey).setTothb(tothb);
										outMap2.get(key+timekey).setTravgcosthb(travgcosthb);
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
					String json=JSONUtils.toJson(newlist.get(i));
					json=json.replace("deptDimensionality", "dept_code");
					json=json.replace("feecode", "fee_stat_code");
					json=json.replace("regcode", "reglevl_code");
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
