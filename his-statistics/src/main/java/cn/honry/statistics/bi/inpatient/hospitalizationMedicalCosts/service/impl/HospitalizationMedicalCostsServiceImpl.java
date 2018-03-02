package cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.dao.HospitalizationMedicalCostsDao;
import cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.service.HospitalizationMedicalCostsService;
import cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.vo.HospitalizationMedicalCostsVo;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.vo.OutpatientFeeTypeVo;
import cn.honry.utils.JSONUtils;
@Service("hospitalizationMedicalCostsService")
@Transactional
@SuppressWarnings({ "all" })
public class HospitalizationMedicalCostsServiceImpl implements HospitalizationMedicalCostsService {
	@Autowired
	@Qualifier(value = "hospitalizationMedicalCostsDao")
	private HospitalizationMedicalCostsDao hospitalizationMedicalCostsDao;

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
	public List<HospitalizationMedicalCostsVo> querytDatagrid(int timeone,String nameString) {
		
		List<HospitalizationMedicalCostsVo> hList= hospitalizationMedicalCostsDao.querytDatagrid(timeone+"", nameString);
		//最终list
		List<HospitalizationMedicalCostsVo> oftList=new ArrayList<HospitalizationMedicalCostsVo>();
		Map<String, String> map1=new HashMap<String,String>();
		Map<String,List<HospitalizationMedicalCostsVo>> map2=new HashMap<String,List<HospitalizationMedicalCostsVo>>();
		//遍历得到的list
		for (HospitalizationMedicalCostsVo o : hList) {
			//将所有的维度除了年龄条件组成key 如果map1中没有改key，则将该key存入map1,并且将该记录放入list中存入map2
			if(map1.get(o.getDeptname())!="1"||map1.get(o.getDeptname())==null){
				List<HospitalizationMedicalCostsVo> li=new ArrayList<HospitalizationMedicalCostsVo>();
				map1.put(o.getDeptname(), "1");
				li.add(o);
				map2.put(o.getDeptname(), li);
			}else{
				//如果map1中已有该key,则map2通过key得到value即集合，并且将该条对象存入该集合，该集合在存入map2中
				List<HospitalizationMedicalCostsVo> li=map2.get(o.getDeptname());
				li.add(o);
				map2.put(o.getDeptname(), li);
			}
		}
		//循环map2
		for(Entry<String, List<HospitalizationMedicalCostsVo>> outmap : map2.entrySet()){
			HospitalizationMedicalCostsVo hospitalizationMedicalCostsVo= new HospitalizationMedicalCostsVo();
			DecimalFormat df= new DecimalFormat("#.##");
			//总金额
			double totcout=0.0;
			//各药金额
			double tot=0.0;
			int cou = 0;
			for (int j = 0; j < outmap.getValue().size(); j++) {
				hospitalizationMedicalCostsVo.setDeptname(outmap.getValue().get(j).getDeptname());
				tot = outmap.getValue().get(j).getTotcost();
				totcout+=outmap.getValue().get(j).getTotcost();
				String feecode=outmap.getValue().get(j).getFeecode();
				if("0".equals(feecode)){
					hospitalizationMedicalCostsVo.setXy(0d);
					hospitalizationMedicalCostsVo.setXypro(0+"%");
					hospitalizationMedicalCostsVo.setZcaoy(0d);
					hospitalizationMedicalCostsVo.setZcaoypro(0+"%");
					hospitalizationMedicalCostsVo.setZchengy(0d);
					hospitalizationMedicalCostsVo.setZchengypro(0+"%");
					hospitalizationMedicalCostsVo.setCoun(0);
				}else{
					if("01".equals(feecode)){
						hospitalizationMedicalCostsVo.setXy(tot);
						cou=outmap.getValue().get(j).getCoun();
						
					}
					if("02".equals(feecode)){
						hospitalizationMedicalCostsVo.setZcaoy(tot);
						cou=outmap.getValue().get(j).getCoun();
						
					}
					if("03".equals(feecode)){
						hospitalizationMedicalCostsVo.setZchengy(tot);
						cou=outmap.getValue().get(j).getCoun();
					}
				}
				
				
				hospitalizationMedicalCostsVo.setCoun((hospitalizationMedicalCostsVo.getCoun()==null?0:hospitalizationMedicalCostsVo.getCoun())+cou);
			}
			hospitalizationMedicalCostsVo.setTotcost(Double.valueOf(df.format(totcout)));
			if(totcout==0){
				hospitalizationMedicalCostsVo.setTotAvg(0+"元");
			}else{
				hospitalizationMedicalCostsVo.setTotAvg(df.format(totcout/cou)+"元");
			}
			if(hospitalizationMedicalCostsVo.getXy()==0.0){
				hospitalizationMedicalCostsVo.setXypro(0+"%");
			}else{
				hospitalizationMedicalCostsVo.setXypro(df.format((hospitalizationMedicalCostsVo.getXy()/totcout)*100)+"%");
			}
			if(hospitalizationMedicalCostsVo.getZcaoy()==0.0){
				hospitalizationMedicalCostsVo.setZcaoypro(0+"%");
			}else{
				hospitalizationMedicalCostsVo.setZcaoypro(df.format((hospitalizationMedicalCostsVo.getZcaoy()/totcout)*100)+"%");
			}
			if(hospitalizationMedicalCostsVo.getZchengy()==0.0){
				hospitalizationMedicalCostsVo.setZchengypro(0+"%");
			}else{
				hospitalizationMedicalCostsVo.setZchengypro(df.format((hospitalizationMedicalCostsVo.getZchengy()/totcout)*100)+"%");
			}
			oftList.add(hospitalizationMedicalCostsVo);
		}
		return oftList;
	}

//	@Override  
//	public String queryStatDate(String timeString,String nameString) {
//		timeString = "2012,2011,07";
//		String[] timeArr=timeString.split(",");
//		List<HospitalizationExpensesVo> info=new ArrayList<HospitalizationExpensesVo>();
//		Map<String,Object> map = new HashMap<String, Object>();
//		Map<String,Object> jsonMap = new HashMap<String, Object>();
//		
//			info = hospitalizationMedicalCostsDao.querytStatData(timeArr[0],"1",nameString);
//			
//			String[] categories=new String[info.size()];
//			Integer[] values=new Integer[info.size()];
//			Integer[] tb=new Integer[info.size()];
//			Integer[] hb=new Integer[info.size()];
//			int t=0;
//			for(HospitalizationExpensesVo temp:info){
//				categories[t]=temp.getDeptName();
//				values[t]=temp.getPassengers();
//				t++; 
//			}
//			map.put("categories", categories);
//			map.put("values", values);
//			
//			//查询同比
//			info = hospitalizationMedicalCostsDao.querytStatData(timeArr[1],"1",nameString);
//			int a=0;
//			for(HospitalizationExpensesVo temp:info){
//				tb[a]=temp.getPassengers();
//				t++; 
//			}
//			map.put("old", tb);
//			//查询环比
//			info = hospitalizationMedicalCostsDao.querytStatData(timeArr[2],"2",nameString);
//			int q=0;
//			for(HospitalizationExpensesVo temp:info){
//				hb[q]=temp.getPassengers();
//				q++; 
//			}
//			map.put("mom", hb);
//		String json=JSONUtils.toJson(map);
//		json=json.replace("null", "0");
//		return json;
//		return "";
//	}

	@Override
	public List<SysDepartment> queryAllDept() {
		return hospitalizationMedicalCostsDao.queryAllDept();
	}
}
