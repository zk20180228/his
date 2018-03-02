package cn.honry.statistics.bi.outpatient.outpatientPassengers.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.vo.OutpatientFeeTypeVo;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.dao.PassengersDAO;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.service.PassengersService;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;

@Service("passengersService")
@Transactional
@SuppressWarnings({"all"})
public class PassengersServiceImpl implements PassengersService{
	
	@Autowired
	@Qualifier(value="passengersDAO")
	private PassengersDAO passengersDAO;

	@Override
	public List<DimensionVO> findDimensionList(DimensionVO dimensionVO) {
		return passengersDAO.findDimensionList(dimensionVO);
	}

	@Override
	public List<District> findDistrict() {
		return passengersDAO.findDistrict();
	}

	@Override
	public List<SysDepartment> findAllDept() {
		return passengersDAO.findAllDept();
	}

	@Override
	public String queryPassengersoadDatagrid(DateVo datevo,String dimensionString, int dateType, String dimensionValue) {
		//组织参数list：list中的元素为map
		List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
		list=ResultUtils.prepareParamList(dimensionValue);
		
		//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
		Map<String,List<String>> map=new HashMap<String, List<String>>();
		map=ResultUtils.prepareParamMap(dimensionValue);
		
		//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
		String [] voArray=new String[]{"sumPeople","ordinaryPeople","emerGencyPeople"};
		//将维度种类拆分放入到数组中
		String [] diArrayKey=dimensionString.split(",");
		List<DimensionVO> volist=passengersDAO.queryPassengersoadDatagrid(diArrayKey, list, dateType,datevo);
		
		HashMap<String, DimensionVO> oftMap= new HashMap<String ,DimensionVO>();
		
		//最终list
		List<DimensionVO> oftList=new ArrayList<DimensionVO>();
		Map<String, String> map1=new HashMap<String,String>();
		Map<String,List<DimensionVO>> map2=new HashMap<String,List<DimensionVO>>();
		//遍历得到的list
		for (DimensionVO o : volist) {
			//将所有的维度除了年龄条件组成key 如果map1中没有改key，则将该key存入map1,并且将该记录放入list中存入map2
			if(map1.get(o.getDept()+o.getYears()+o.getRegion()+o.getSex())!="1"||map1.get(o.getDept()+o.getYears()+o.getRegion()+o.getSex())==null){
				List<DimensionVO> li=new ArrayList<DimensionVO>();
				map1.put(o.getDept()+o.getYears()+o.getRegion()+o.getSex(), "1");
				li.add(o);
				map2.put(o.getDept()+o.getYears()+o.getRegion()+o.getSex(), li);
			}else{
				//如果map1中已有该key,则map2通过key得到value即集合，并且将该条对象存入该集合，该集合在存入map2中
				List<DimensionVO> li=map2.get(o.getDept()+o.getYears()+o.getRegion()+o.getSex());
				li.add(o);
				map2.put(o.getDept()+o.getYears()+o.getRegion()+o.getSex(), li);
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
							for(Entry<String, List<DimensionVO>> outmap : map2.entrySet()){
								//map2同一个key下集合中的元素维度都相同，只有年龄不同 所以可以新建一个对象维度是任意一个元素的 设置维度
								DimensionVO vo = new DimensionVO();
								vo.setDept(outmap.getValue().get(0).getDept());
								vo.setRegion(outmap.getValue().get(0).getRegion());
								vo.setSex(outmap.getValue().get(0).getSex());
								vo.setYears(outmap.getValue().get(0).getYears());
								Integer sumPeople = 0;
								Integer emerGencyPeople = 0;
								Integer ordinaryPeople = 0;
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
										sumPeople= sumPeople+outmap.getValue().get(j).getSumPeople();
										emerGencyPeople=sumPeople+outmap.getValue().get(j).getSumPeople();
										ordinaryPeople = ordinaryPeople + outmap.getValue().get(j).getOrdinaryPeople();
									}
									//将得到指标set进新建的该vo
									vo.setSumPeople(sumPeople);
									vo.setOrdinaryPeople(ordinaryPeople);
									vo.setEmerGencyPeople(emerGencyPeople);
								}
								//将从新得到的vo添加进最终的list集合
								oftList.add(vo);
							}
						}
					}
				}
			}
		}else{
			for (DimensionVO o : volist) {
				oftList.add(o);
			}
		}
		
		
		
		
		List<String> listJson=new ArrayList<String>();
		for(int i=0;i<oftList.size();i++){
			//查询出来的结果集的每一个对象转换为json
			String json=JSONUtils.toJson(oftList.get(i));
			json=json.replace("dept", "dept_code");
			json=json.replace("sex", "patient_sex");
			json=json.replace("region", "city_code");
			json=json.replace("age", "patient_age");
			json=json.replace("ageUnit", "patient_ageunit");
			String json1=ResultUtils.getnewJson(json, dateType, voArray, oftList.get(i).getYears());
			//将json字符串添加到listJson中
			listJson.add(json1);
		}
		//获得最终的json字符串
		String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
		return result;
	}

	@Override
	public List<BIBaseDistrict> queryCity() {
		return passengersDAO.queryCity();
	}

}
