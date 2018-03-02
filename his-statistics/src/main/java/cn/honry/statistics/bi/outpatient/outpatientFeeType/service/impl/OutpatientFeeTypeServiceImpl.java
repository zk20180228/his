package cn.honry.statistics.bi.outpatient.outpatientFeeType.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.dao.OutpatientFeeTypeDAO;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.service.OutpatientFeeTypeService;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.vo.OutpatientFeeTypeVo;
import cn.honry.statistics.finance.operationCost.dao.OperationCostDao;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;

@Service("outpatientFeeTypeService")
@Transactional
@SuppressWarnings({ "all" })
public class OutpatientFeeTypeServiceImpl implements OutpatientFeeTypeService{

	@Override
	public OutpatientFeeTypeVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OutpatientFeeTypeVo arg0) {
		
	}
	/** 门诊收费类型 **/
	@Autowired
	@Qualifier(value = "outpatientFeeTypeDAO")
	private OutpatientFeeTypeDAO outpatientFeeTypeDAO;
	@Autowired
	@Qualifier(value = "operationCostDao")
	private OperationCostDao operationCostDao;
	@Autowired
	@Qualifier(value = "innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	
	/***
	 * 得到门诊收费类型集合
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年7月26日 
	 * @version 1.0
	 */
	
	public String queryOutpatientFeeType(DateVo datevo, String[] dimStringArray,int dateType,String dimensionValue) {
		//组织参数list：list中的元素为map
		List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
		list=ResultUtils.prepareParamList(dimensionValue);
				
		//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
		Map<String,List<String>> map=new HashMap<String, List<String>>();
		map=ResultUtils.prepareParamMap(dimensionValue);
		
		
		
		//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
		String [] voArray=new String[]{"totCost","scale"};
		
		//将维度种类拆分放入到数组中
		List<OutpatientFeeTypeVo> volist=outpatientFeeTypeDAO.queryOutpatientFeeTypeVo(dimStringArray, list, dateType,datevo);
		HashMap<String, OutpatientFeeTypeVo> oftMap= new HashMap<String ,OutpatientFeeTypeVo>();
		
		//最终list
		List<OutpatientFeeTypeVo> oftList=new ArrayList<OutpatientFeeTypeVo>();
		Map<String, String> map1=new HashMap<String,String>();
		Map<String,List<OutpatientFeeTypeVo>> map2=new HashMap<String,List<OutpatientFeeTypeVo>>();
		//遍历得到的list
		for (OutpatientFeeTypeVo o : volist) {
			//将所有的维度除了年龄条件组成key 如果map1中没有改key，则将该key存入map1,并且将该记录放入list中存入map2
			if(map1.get(o.getAddress()+o.getDegree()+o.getDoctDept()+o.getPayway()+o.getTimeChose())!="1"||map1.get(o.getAddress()+o.getDegree()+o.getDoctDept()+o.getPayway()+o.getTimeChose())==null){
				List<OutpatientFeeTypeVo> li=new ArrayList<OutpatientFeeTypeVo>();
				map1.put(o.getAddress()+o.getDegree()+o.getDoctDept()+o.getPayway()+o.getTimeChose(), "1");
				li.add(o);
				map2.put(o.getAddress()+o.getDegree()+o.getDoctDept()+o.getPayway()+o.getTimeChose(), li);
			}else{
				//如果map1中已有该key,则map2通过key得到value即集合，并且将该条对象存入该集合，该集合在存入map2中
				List<OutpatientFeeTypeVo> li=map2.get(o.getAddress()+o.getDegree()+o.getDoctDept()+o.getPayway()+o.getTimeChose());
				li.add(o);
				map2.put(o.getAddress()+o.getDegree()+o.getDoctDept()+o.getPayway()+o.getTimeChose(), li);
			}
		}
		if(dimensionValue.contains("patient_age")){
			//循环所有的条件
			for(int i=0;i<list.size();i++){
				Map<String,List<String>> m=list.get(i);
				for(String li2:m.keySet()){
					//得到条件为age的
					if("patient_age".equals(li2)){
						List<String> lis2=m.get(li2);
						//循环条件为age的查询条件 例如
						for(int a=0;a<lis2.size();a++){
							String s=lis2.get(a);
							//如果查询条件为一个值，即前台传入10岁，将该条件改为10-10岁，方便处理
							if(!s.contains("-")){
								String s1=s.substring(0,s.length()-1);
								String s2=s.substring(s.length()-1,s.length());
								s=s1+"-"+s1+s2;
							}
							//分割字符串 得到数组[10,20]
							String[] str=s.substring(0,s.length()-1).split("-");
							//得到年龄 岁/月/天
							String ageUnit=s.substring(s.length()-1, s.length());
							//循环map2
							for(Entry<String, List<OutpatientFeeTypeVo>> outmap : map2.entrySet()){
								//map2同一个key下集合中的元素维度都相同，只有年龄不同 所以可以新建一个对象维度是任意一个元素的 设置维度
								OutpatientFeeTypeVo vo = new OutpatientFeeTypeVo();
								vo.setAddress(outmap.getValue().get(0).getAddress());
								//vo.setAgeUnit(outmap.getValue().get(0).getAgeUnit());
								vo.setDegree(outmap.getValue().get(0).getDegree());
								vo.setDoctDept(outmap.getValue().get(0).getDoctDept());
								vo.setPayway(outmap.getValue().get(0).getPayway());
								vo.setTimeChose(outmap.getValue().get(0).getTimeChose());
								Double totCost = 0.0;
								Double sca=0.0;
								for (int j = 0; j < outmap.getValue().size(); j++) {
									//设置年龄区间 例如10-20岁
									if(str[1].equals(str[0])){
										vo.setAge(str[0]+ageUnit);
									}else{
										vo.setAge(str[0]+"-"+str[1]+ageUnit);
									}
									//循环map2该key下对应集合的每一个元素的年龄和单位与查询条件的年龄单位比较，即年龄位于该区间内，单位相同 的对象处理指标
									if(Integer.parseInt(outmap.getValue().get(j).getAge())>=Integer.parseInt(str[0])&&
											Integer.parseInt(outmap.getValue().get(j).getAge())<=Integer.parseInt(str[1])&&
											outmap.getValue().get(j).getAgeUnit().equals(ageUnit)){
										//将每个符合条件对象的指标相加
										 totCost= totCost+outmap.getValue().get(j).getTotCost();
										 Double scale=outmap.getValue().get(j).getScale();
										 sca=sca+scale;
									}
									//将得到指标set进新建的该vo
									vo.setScale(sca);
									vo.setTotCost(totCost);
								}
								//将从新得到的vo添加进最终的list集合
								oftList.add(vo);
							}
						}
					}
				}
			}
		}else{
			for (OutpatientFeeTypeVo o : volist) {
				oftList.add(o);
			}
		}
		List<String> listJson=new ArrayList<String>();
		for(int i=0;i<oftList.size();i++){
			//查询出来的结果集的每一个对象转换为json
			String json=JSONUtils.toJson(oftList.get(i));
			json=json.replace("doctDept", "doct_dept");
			json=json.replace("address", "patient_nativeplace");
			json=json.replace("age", "patient_age");
			/*json=json.replace("degree", "employee_education_code");*/
			json=json.replace("payway", "mode_code");
			String json1=ResultUtils.getnewJson(json, dateType, voArray, oftList.get(i).getTimeChose());
			//将json字符串添加到listJson中
			listJson.add(json1);
		}
		//获得最终的json字符串
		String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
		return result;
		
	}
	
	/**
	 * @Description:科室id与科室name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月27日
	 * @param:
	 * @return Map<String , String> 科室id与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public Map<String, String> depMap() {
		HashMap<String , String> depMap=new HashMap<String ,String>();
		List<SysDepartment> depList=operationCostDao.depMentList();
		for (SysDepartment s : depList) {
			depMap.put(s.getDeptCode(), s.getDeptName());
		}
		return depMap;
	}
	/**
	 * @Description:地域code与科室name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月27日
	 * @param:
	 * @return Map<String , String> 地域code与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public Map<String, String> addressMap() {
		HashMap<String , String> addressMap=new HashMap<String ,String>();
		List<District> depList=outpatientFeeTypeDAO.getDistrictList(1, "");
		for (District s : depList) {
			addressMap.put(s.getCityCode(), s.getCityName());
		}
		return addressMap;
	}
	/**
	 * @Description:年龄code与年龄name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月27日
	 * @param:
	 * @return Map<String , String> 年龄code与年龄name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public Map<String, String> ageMap() {
		HashMap<String , String> ageMap=new HashMap<String ,String>();
		ageMap.put("1", "0-10");
		ageMap.put("2", "11-20");
		ageMap.put("3", "21-30");
		ageMap.put("4", "31-40");
		ageMap.put("5", "41-50");
		ageMap.put("6", "51-60");
		ageMap.put("7", "61-70");
		ageMap.put("8", "71-80");
		ageMap.put("9", "81-90");
		ageMap.put(	"10", "90以上");
		return ageMap;
	}
	
	/**
	 * @Description:学历code与学历name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月27日
	 * @param:
	 * @return Map<String , String> 学历code与学历name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public Map<String, String> degreeMap() {
		HashMap<String , String> degreeMap=new HashMap<String ,String>();
		List<BusinessDictionary> depList=innerCodeDao.getDictionary("degree");
		for (BusinessDictionary s : depList) {
			degreeMap.put(s.getEncode(), s.getName());
		}
		return degreeMap;
	}
	
	/**
	 * @Description:支付方式code与支付方式name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月27日
	 * @param:
	 * @return Map<String , String> 支付方式code与支付方式name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public Map<String, String> paywayMap() {
		HashMap<String , String> paywayMap=new HashMap<String ,String>();
		List<BusinessDictionary> depList=innerCodeDao.getDictionary("payway");
		for (BusinessDictionary s : depList) {
			paywayMap.put(s.getEncode(), s.getName());
		}
		return paywayMap;
	}

}
