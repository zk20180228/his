package cn.honry.statistics.bi.inpatient.hospitalizationInformation.service.impl;

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
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.dao.HospitalizationInformationDao;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.service.HospitalizationInformationService;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.vo.HospitalizationInformationVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
@Service("hospitalizationInformationService")
@Transactional
@SuppressWarnings({ "all" })
public class HospitalizationInformationServiceImpl implements HospitalizationInformationService {
	@Autowired
	@Qualifier(value = "hospitalizationInformationDao")
	private HospitalizationInformationDao hospitalizationInformationDao;

	@Override
	public BiRegister get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(BiRegister arg0) {
		
	}

	@Override
	public List<BiBaseOrganization> queryAllDept() {
		return hospitalizationInformationDao.queryAllDept();
	}

	@Override
	public String querytDatagrid(DateVo datevo,String[] dimStringArray,int dateType,String dimensionValue) {
		//组织参数list：list中的元素为map
		List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
		list=ResultUtils.prepareParamList(dimensionValue);
				
		//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
		Map<String,List<String>> map=new HashMap<String, List<String>>();
		map=ResultUtils.prepareParamMap(dimensionValue);
		
		//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
		String [] voArray=new String[]{"passengers"};
		//将维度种类拆分放入到数组中
		List<HospitalizationInformationVo> volist=hospitalizationInformationDao.querytDatagrid(dimStringArray, list, dateType,datevo);
		HashMap<String, HospitalizationInformationVo> oftMap= new HashMap<String ,HospitalizationInformationVo>();
		
		//最终list
		List<HospitalizationInformationVo> oftList=new ArrayList<HospitalizationInformationVo>();
		Map<String,List<HospitalizationInformationVo>> map2=new HashMap<String,List<HospitalizationInformationVo>>();
		
		//遍历得到的list
		for (HospitalizationInformationVo o : volist) {
			String id=""+o.getHome()+o.getInsource()+o.getPatientstatus()+o.getDeptName()+o.getInpatientTime();
			//将所有的维度除了年龄条件组成key 如果map1中没有改key，则将该key存入map1,并且将该记录放入list中存入map2
				List<HospitalizationInformationVo> li =new ArrayList<HospitalizationInformationVo>();
				li.add(o);
				map2.put(id, li);
		}
		//循环所有的条件
		for(int i=0;i<list.size();i++){
			Map<String,List<String>> m=list.get(i);
			for(String li2:m.keySet()){
				//得到条件为age的
				if("age".equals(li2)){
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
						HospitalizationInformationVo vo = null;
						//循环map2
						for(Entry<String, List<HospitalizationInformationVo>> outmap : map2.entrySet()){
							//map2同一个key下集合中的元素维度都相同，只有年龄不同 所以可以新建一个对象维度是任意一个元素的 设置维度
							vo = new HospitalizationInformationVo();
							vo.setHome(outmap.getValue().get(0).getHome());
							vo.setInsource(outmap.getValue().get(0).getInsource());
							vo.setPatientstatus(outmap.getValue().get(0).getPatientstatus());
							vo.setDeptName(outmap.getValue().get(0).getDeptName());
							vo.setInpatientTime(outmap.getValue().get(0).getInpatientTime());
							int totCost = 0;
							int sca=0;
							for (int j = 0; j < outmap.getValue().size(); j++) {
								//设置年龄区间 例如10-20岁
								if(str[1].equals(str[0])){
									vo.setReportBirthday(str[0]);
								}else{
									vo.setReportBirthday(str[0]+"-"+str[1]);
								}
								//循环map2该key下对应集合的每一个元素的年龄和单位与查询条件的年龄单位比较，即年龄位于该区间内，单位相同 的对象处理指标
								if(Integer.parseInt(outmap.getValue().get(j).getReportBirthday())>=Integer.parseInt(str[0])&&
										Integer.parseInt(outmap.getValue().get(j).getReportBirthday())<=Integer.parseInt(str[1])){
									//将每个符合条件对象的指标相加
									 totCost += totCost+outmap.getValue().get(j).getPassengers();
								}
								//将得到指标set进新建的该vo
								vo.setPassengers(sca);
							}
							//将从新得到的vo添加进最终的list集合
							oftList.add(vo);
						}
					}
				}
			}
		}

		
		List<String> listJson=new ArrayList<String>();
		for(int i=0;i<volist.size();i++){
			//查询出来的结果集的每一个对象转换为json
			String json=JSONUtils.toJson(volist.get(i));
			json=json.replace("deptName", "dept_code");
			json=json.replace("patientstatus", "status");
			json=json.replace("insourse", "in_sourse");
			json=json.replace("reportBirthday", "age");
			String json1=ResultUtils.getnewJson(json, dateType, voArray, volist.get(i).getInpatientTime());
			//将json字符串添加到listJson中
			listJson.add(json1);
		}
		//获得最终的json字符串
		String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
		return result;
	}

//	@Override
//	public String getAgePeakValue() {
//		List<HospitalizationInformationVo> list = hospitalizationInformationDao.getAgePeakValue();
//		return "";
//	}

//	@Override  
//	public Map<String,Object> queryStatDate(String timeString,String nameString) {
//		timeString = "2012,2011,07";
//		String[] timeArr=timeString.split(",");
//		List<HospitalizationInformationVo> info=new ArrayList<HospitalizationInformationVo>();
//		Map<String,Object> map = new HashMap<String, Object>();
//		Map<String,Object> jsonMap = new HashMap<String, Object>();
//		
//			info = hospitalizationInformationDao.querytStatData(timeArr[0],"1",nameString);
//			
//			String[] categories=new String[info.size()];
//			Integer[] values=new Integer[info.size()];
//			Integer[] tb=new Integer[info.size()];
//			Integer[] hb=new Integer[info.size()];
//			int t=0;
//			for(HospitalizationInformationVo temp:info){
//				categories[t]=temp.getDeptName();
//				values[t]=temp.getPassengers();
//				t++; 
//			}
//			map.put("categories", categories);
//			map.put("values", values);
//			
//			//查询同比
//			info = hospitalizationInformationDao.querytStatData(timeArr[1],"1",nameString);
//			int a=0;
//			for(HospitalizationInformationVo temp:info){
//				tb[a]=temp.getPassengers();
//				t++; 
//			}
//			map.put("old", tb);
//			//查询环比
//			info = hospitalizationInformationDao.querytStatData(timeArr[2],"2",nameString);
//			int q=0;
//			for(HospitalizationInformationVo temp:info){
//				hb[q]=temp.getPassengers();
//				q++; 
//			}
//			map.put("mom", hb);
//		
//		return map;
//
//	}

	@Override
	public List<BIBaseDistrict> getHome() {
		return hospitalizationInformationDao.getHome();
	}

	@Override
	public List<BiBaseOrganization> queryDeptForBiPublic(String deptType) {
		return hospitalizationInformationDao.queryDeptForBiPublic(deptType);
	}


}
