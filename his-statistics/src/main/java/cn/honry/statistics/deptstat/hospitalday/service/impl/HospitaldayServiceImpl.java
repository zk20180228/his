package cn.honry.statistics.deptstat.hospitalday.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.statistics.deptstat.hospitalday.dao.HospitaldayDao;
import cn.honry.statistics.deptstat.hospitalday.service.HospitaldayService;
import cn.honry.statistics.deptstat.hospitalday.vo.ConfigVo;
import cn.honry.statistics.deptstat.hospitalday.vo.HospitaldayVo;
import cn.honry.statistics.finance.statistic.service.StatisticService;
import cn.honry.statistics.finance.statistic.vo.ResultVo;

@Service("hospitaldayService")
@Transactional
//@SuppressWarnings({"all"})
public class HospitaldayServiceImpl implements HospitaldayService{
	@Autowired
	@Qualifier(value = "hospitaldayDao")
	private HospitaldayDao hospitaldayDao;
	private StatisticService statisticService;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	@Autowired
	@Qualifier(value="statisticService")
	public void setStatisticService(StatisticService statisticService) {
		this.statisticService = statisticService;
	}
	
	private DeptInInterService deptInInterService;
	private final static String queryMong="DRJYSJ_DAY";
	@Autowired
	@Qualifier(value="deptInInterService")
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	@Override
	public void queryHospitaldayList(String startTime,String endTime,Integer type) {
		HospitaldayVo hospitaldayVo = new HospitaldayVo();
		String outpatientNum = "0";//门诊人次合计
		String outpatientNumHy = "0";//河医院区门诊人次
		String outpatientNumZd = "0";//郑东院区门诊人次
		String outpatientNumHj = "0";//惠济院区门诊人次
		String inpatientNum = "0" ;//在院人数
		String inpatientNumHy = "0" ;//在院人数河医
		String inpatientNumZd = "0" ;//在院人数郑东
		String inpatientNumHj = "0" ;//在院人数惠济
		String operationNum = "0" ;//手术人数
		String operationNumHy = "0" ;//手术人数河医
		String operationNumZd = "0" ;//手术人数郑东
		String operationNumHj = "0" ;//手术人数惠济
		String incomeCost = "0";//总收入
		String incomeCostHy = "0";//河医院区收入
		String incomeCostZd = "0";//郑东院区收入
		String incomeCostHj = "0";//惠济院区收入
		String leaveHospitalNum = "0";//出院人次
		String leaveHospitalNumHy = "0";//河医院区出院人次
		String leaveHospitalNumZd = "0";//郑东院区出院人次
		String leaveHospitalNumHj = "0";//惠济院区出院人次
		String inHospitalNum = "0";//入院人次
		String inHospitalNumHy = "0";//河医院区入院人次
		String inHospitalNumZd = "0";//郑东院区入院人次
		String inHospitalNumHj = "0";//惠济院区入院人次
		String drugProportion = "0";//药占比
		String drugProportionHy = "0";//河医院区药占比
		String drugProportionZd = "0";//郑东院区药占比
		String drugProportionHj = "0";//惠济院区药占比
		//1.查询门诊人次
		
//		Map<String, Object> toListViewMap = new HashMap<String, Object>();
		String key="MJZRCTJ_DAY";
    	BasicDBObject where = new BasicDBObject();
    	where.append("date", endTime);
    	String areaString = "";
    	DBCursor cursor = new MongoBasicDao().findAlldata(key, where);
    	while(cursor.hasNext()){
    		Object object = cursor.next().get("value");
    		if(object!=null){
    			String objectS=object.toString();
    			if(objectS.contains("viewVo\":{")){
    				int i = objectS.indexOf("areaOf");
    				areaString = objectS.substring(objectS.indexOf("[",i), objectS.indexOf("]",i)+1);
    			}
    		}
    	}
    	List<HospitaldayVo> list2 = null;
    	if(areaString!=null&&!"".equals(areaString)){
    		JSONArray jsonArray = JSONArray.fromObject(areaString);  
            list2 = (List) JSONArray.toCollection(jsonArray,  
            		HospitaldayVo.class);
            Double outpatientNum1 = 0.0;
            for (int i = 0; i < list2.size(); i++) {
            	outpatientNum1 = outpatientNum1 + Double.valueOf(list2.get(i).getValue());
            	if("河医院区".equals(list2.get(i).getName())){
            		outpatientNumHy = list2.get(i).getValue();//河医院区门诊人次
            	}else if("郑东院区".equals(list2.get(i).getName())){
            		outpatientNumZd = list2.get(i).getValue();//郑东院区门诊人次
            	}else if("惠济院区".equals(list2.get(i).getName())){
            		outpatientNumHj = list2.get(i).getValue();//惠济院区门诊人次
            	}
			}
            outpatientNum = outpatientNum1.toString();//门诊人次合计
    	}
        HospitaldayVo hospitaldayVo1 = new HospitaldayVo();
        hospitaldayVo1.setValue(outpatientNum);
        hospitaldayVo1.setName("门诊人次合计");
        hospitaldayVo1.setFlag("1h");
        HospitaldayVo hospitaldayVo2 = new HospitaldayVo();
        hospitaldayVo2.setValue(outpatientNumHy);
        hospitaldayVo2.setName("河医院区");
        hospitaldayVo2.setFlag("1");
        HospitaldayVo hospitaldayVo3 = new HospitaldayVo();
        hospitaldayVo3.setValue(outpatientNumZd);
        hospitaldayVo3.setName("郑东院区");
        hospitaldayVo3.setFlag("1");
        HospitaldayVo hospitaldayVo4 = new HospitaldayVo();
        hospitaldayVo4.setValue(outpatientNumHj);
        hospitaldayVo4.setName("惠济院区");
        hospitaldayVo4.setFlag("1");
		//2.在院人次
        List<HospitaldayVo> voss = hospitaldayDao.queryHospitaldayVoin(endTime);
        for (HospitaldayVo hospitaldayVo5 : voss) {
        	inpatientNum = hospitaldayVo5.getInpatientNum() ;//在院人数
        	inpatientNumHy = hospitaldayVo5.getInpatientNumHy()  ;//在院人数河医
        	inpatientNumZd = hospitaldayVo5.getInpatientNumZd()  ;//在院人数郑东
        	inpatientNumHj = hospitaldayVo5.getInpatientNumHj();//在院人数惠济
		}
		HospitaldayVo hospitaldayVo5 = new HospitaldayVo();
		hospitaldayVo5.setValue(inpatientNum);
		hospitaldayVo5.setName("在院人次合计");
		hospitaldayVo5.setFlag("2h");
        HospitaldayVo hospitaldayVo6 = new HospitaldayVo();
        hospitaldayVo6.setValue(inpatientNumHy);
        hospitaldayVo6.setName("河医院区");
        hospitaldayVo6.setFlag("2");
        HospitaldayVo hospitaldayVo7 = new HospitaldayVo();
        hospitaldayVo7.setValue(inpatientNumZd);
        hospitaldayVo7.setName("郑东院区");
        hospitaldayVo7.setFlag("2");
        HospitaldayVo hospitaldayVo8 = new HospitaldayVo();
        hospitaldayVo8.setValue(inpatientNumHj);
        hospitaldayVo8.setName("惠济院区");
        hospitaldayVo8.setFlag("2");
		//3.手术人次
        List<HospitaldayVo> vosss = hospitaldayDao.queryHospitaldayVoopear(endTime);
        for (HospitaldayVo hospitaldayVo9 : vosss) {
        	operationNum = hospitaldayVo9.getOperationNum() ;//手术人数
			operationNumHy = hospitaldayVo9.getOperationNumHy() ;//手术人数河医
			operationNumZd = hospitaldayVo9.getOperationNumZd() ;//手术人数郑东
			operationNumHj = hospitaldayVo9.getOperationNumHj() ;//手术人数惠济
		}
		HospitaldayVo hospitaldayVo9 = new HospitaldayVo();
		hospitaldayVo9.setValue(operationNum);
		hospitaldayVo9.setName("手术人次合计");
		hospitaldayVo9.setFlag("3h");
        HospitaldayVo hospitaldayVo10 = new HospitaldayVo();
        hospitaldayVo10.setValue(operationNumHy);
        hospitaldayVo10.setName("河医院区");
        hospitaldayVo10.setFlag("3");
        HospitaldayVo hospitaldayVo11 = new HospitaldayVo();
        hospitaldayVo11.setValue(operationNumZd);
        hospitaldayVo11.setName("郑东院区");
        hospitaldayVo11.setFlag("3");
        HospitaldayVo hospitaldayVo12 = new HospitaldayVo();
        hospitaldayVo12.setValue(operationNumHj);
        hospitaldayVo12.setName("惠济院区");
        hospitaldayVo12.setFlag("3");
		//4.收入
    	String key1="ZSRQKTJ_DAY";
    	String areaString1 = "";
    	BasicDBObject where1 = new BasicDBObject();
    	where1.append("date", endTime);
    	String s1="";
    	DBCursor cursor1 = new MongoBasicDao().findAlldata(key1, where1);
    	while(cursor1.hasNext()){
    		 s1=cursor1.next().get("value").toString();
    		 if(s1!=null){
     			String objectS=s1;
 				int i = objectS.indexOf("areaOfDay");
 				areaString1 = objectS.substring(objectS.indexOf("[",i), objectS.indexOf("]",i)+1);
     		}
    	}
    	List<HospitaldayVo> list3 = null;
    	if(areaString1!=null&&!"".equals(areaString1)){
    		JSONArray jsonArray = JSONArray.fromObject(areaString1);  
            list3 = (List) JSONArray.toCollection(jsonArray,  
            		HospitaldayVo.class);
            Double incomeCost1 = 0.0;
            for (int i = 0; i < list3.size(); i++) {
            	if("河医院区".equals(list3.get(i).getName())){
            		BigDecimal   b   =   new   BigDecimal(Double.valueOf(list3.get(i).getValue())/10000);  
                	double   f1   =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
                	incomeCostHy = f1+"万";//河医院区收入
            	}else if("郑东院区".equals(list3.get(i).getName())){
            		BigDecimal   b2   =   new   BigDecimal(Double.valueOf(list3.get(i).getValue())/10000);  
                	double   f2   =   b2.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
            		incomeCostZd = f2+"万";//郑东院区收入
            	}else if("惠济院区".equals(list3.get(i).getName())){
            		BigDecimal   b3   =   new   BigDecimal(Double.valueOf(list3.get(i).getValue())/10000);  
                	double   f3   =   b3.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
            		incomeCostHj = f3+"万";//惠济院区收入
            	}
        		incomeCost1 = incomeCost1 + Double.valueOf(list3.get(i).getValue());
			}
            BigDecimal   b   =   new   BigDecimal(incomeCost1/10000);  
        	double   f1   =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
        	if(f1!=0.0){
        		incomeCost = f1+"万";//总收入
        	}
    	}
    	HospitaldayVo hospitaldayVo13 = new HospitaldayVo();
    	hospitaldayVo13.setValue(incomeCost);
    	hospitaldayVo13.setName("总收入");
    	hospitaldayVo13.setFlag("4h");
        HospitaldayVo hospitaldayVo14 = new HospitaldayVo();
        hospitaldayVo14.setValue(incomeCostHy);
        hospitaldayVo14.setName("河医院区");
        hospitaldayVo14.setFlag("4");
        HospitaldayVo hospitaldayVo15 = new HospitaldayVo();
        hospitaldayVo15.setValue(incomeCostZd);
        hospitaldayVo15.setName("郑东院区");
        hospitaldayVo15.setFlag("4");
        HospitaldayVo hospitaldayVo16 = new HospitaldayVo();
        hospitaldayVo16.setValue(incomeCostHj);
        hospitaldayVo16.setName("惠济院区");
        hospitaldayVo16.setFlag("4");
		//5.出院人次
        List<HospitaldayVo> vossss = hospitaldayDao.queryHospitaldayVooutpa(endTime);
        for (HospitaldayVo hospitaldayVo17 : vossss) {
        	leaveHospitalNum = hospitaldayVo17.getLeaveHospitalNum() ;//出院人次
			leaveHospitalNumHy = hospitaldayVo17.getLeaveHospitalNumHy() ;//河医院区出院人次
			leaveHospitalNumZd = hospitaldayVo17.getLeaveHospitalNumZd() ;//郑东院区出院人次
			leaveHospitalNumHj = hospitaldayVo17.getLeaveHospitalNumHj() ;//惠济院区出院人次
		}
		HospitaldayVo hospitaldayVo17 = new HospitaldayVo();
		hospitaldayVo17.setValue(leaveHospitalNum);
		hospitaldayVo17.setName("出院人次合计");
		hospitaldayVo17.setFlag("5h");
        HospitaldayVo hospitaldayVo18 = new HospitaldayVo();
        hospitaldayVo18.setValue(leaveHospitalNumHy);
        hospitaldayVo18.setName("河医院区");
        hospitaldayVo18.setFlag("5");
        HospitaldayVo hospitaldayVo19 = new HospitaldayVo();
        hospitaldayVo19.setValue(leaveHospitalNumZd);
        hospitaldayVo19.setName("郑东院区");
        hospitaldayVo19.setFlag("5");
        HospitaldayVo hospitaldayVo20 = new HospitaldayVo();
        hospitaldayVo20.setValue(leaveHospitalNumHj);
        hospitaldayVo20.setName("惠济院区");
        hospitaldayVo20.setFlag("5");
		//6.入院人次
		List<HospitaldayVo> list = hospitaldayDao.queryHospitaldayList(null, null, endTime, endTime);
		inHospitalNum = list.get(0).getInHospitalNum();//入院人次
		inHospitalNumHy = list.get(0).getInHospitalNumHy();//河医院区入院人次
		inHospitalNumZd = list.get(0).getInHospitalNumZd();//郑东院区入院人次
		inHospitalNumHj = list.get(0).getInHospitalNumHj();//惠济院区入院人次
		HospitaldayVo hospitaldayVo21 = new HospitaldayVo();
		hospitaldayVo21.setValue(inHospitalNum);
		hospitaldayVo21.setName("入院人次合计");
		hospitaldayVo21.setFlag("6h");
        HospitaldayVo hospitaldayVo22 = new HospitaldayVo();
        hospitaldayVo22.setValue(inHospitalNumHy);
        hospitaldayVo22.setName("河医院区");
        hospitaldayVo22.setFlag("6");
        HospitaldayVo hospitaldayVo23 = new HospitaldayVo();
        hospitaldayVo23.setValue(inHospitalNumZd);
        hospitaldayVo23.setName("郑东院区");
        hospitaldayVo23.setFlag("6");
        HospitaldayVo hospitaldayVo24 = new HospitaldayVo();
        hospitaldayVo24.setValue(inHospitalNumHj);
        hospitaldayVo24.setName("惠济院区");
        hospitaldayVo24.setFlag("6");
		//7.药占比
		//(1)门诊
		Double hyMzyp = 0.0;
    	Double hyMz = 0.0;
    	Double zdMzyp = 0.0;
    	Double zdMz = 0.0;
    	Double hjMzyp = 0.0;
    	Double hjMz = 0.0;
		List<HospitaldayVo> list4 = hospitaldayDao.querymz(null, null, endTime, endTime);
		for (int i = 0; i < list4.size(); i++) {
			if(list4.get(i).getCost()!=null&&!"".equals(list4.get(i).getCost())){
				if("hyY".equals(list4.get(i).getFlag())){
					hyMzyp = Double.valueOf(list4.get(i).getCost());
				}else if("zdY".equals(list4.get(i).getFlag())){
					zdMzyp = Double.valueOf(list4.get(i).getCost());
				}else if("hjY".equals(list4.get(i).getFlag())){
					hjMzyp = Double.valueOf(list4.get(i).getCost());
				}else if("hyZJE".equals(list4.get(i).getFlag())){
					hyMz = Double.valueOf(list4.get(i).getCost());
				}else if("zdZJE".equals(list4.get(i).getFlag())){
					zdMz = Double.valueOf(list4.get(i).getCost());
				}else if("hjZJE".equals(list4.get(i).getFlag())){
					hjMz = Double.valueOf(list4.get(i).getCost());
				}
			}
		}
    	//(2)住院
    	List<SysDepartment> queryIdList = deptInInterService.findTree(false, "I");
    	Map<String, String> map = new  HashMap<String, String>();
    	String queryIds = "";//过滤后实际查询的科室
		for (int i = 0; i < queryIdList.size(); i++) {
			if (i == 0) {
				queryIds = queryIdList.get(i).getDeptCode();
			} else {
				queryIds = queryIds+","+queryIdList.get(i).getDeptCode();
			}
			map.put(queryIdList.get(i).getDeptCode(), queryIdList.get(i).getAreaCode());
		}
		
    	List<ResultVo> resultVos = statisticService.statisticDataByES(endTime,endTime,queryIds);
    	for (int i = 0; i < resultVos.size(); i++) {
    		resultVos.get(i).setDeptName(map.get(resultVos.get(i).getInhosDeptcode()));
		}
    	Double hyZyyp = 0.0;
    	Double hyZy = 0.0;
    	Double zdZyyp = 0.0;
    	Double zdZy = 0.0;
    	Double hjZyyp = 0.0;
    	Double hjZy = 0.0;
    	for (int i = 0; i < resultVos.size(); i++) {
    		if("1".equals(resultVos.get(i).getDeptName())){//河医院区
    			hyZyyp += resultVos.get(i).getTotCost01()+resultVos.get(i).getTotCost02()+resultVos.get(i).getTotCost03();
    			hyZy += resultVos.get(i).getTotCost01()+resultVos.get(i).getTotCost02()+resultVos.get(i).getTotCost03()+resultVos.get(i).getTotCost04()+resultVos.get(i).getTotCost05()
    					+resultVos.get(i).getTotCost07()+resultVos.get(i).getTotCost08()+resultVos.get(i).getTotCost09()+resultVos.get(i).getTotCost10()+resultVos.get(i).getTotCost11()
    					+resultVos.get(i).getTotCost12()+resultVos.get(i).getTotCost13()+resultVos.get(i).getTotCost14()+resultVos.get(i).getTotCost15()+resultVos.get(i).getTotCost16();
    		}else if("2".equals(resultVos.get(i).getDeptName())){//郑东院区
    			zdZyyp += resultVos.get(i).getTotCost01()+resultVos.get(i).getTotCost02()+resultVos.get(i).getTotCost03();
    			zdZy += resultVos.get(i).getTotCost01()+resultVos.get(i).getTotCost02()+resultVos.get(i).getTotCost03()+resultVos.get(i).getTotCost04()+resultVos.get(i).getTotCost05()
    					+resultVos.get(i).getTotCost07()+resultVos.get(i).getTotCost08()+resultVos.get(i).getTotCost09()+resultVos.get(i).getTotCost10()+resultVos.get(i).getTotCost11()
    					+resultVos.get(i).getTotCost12()+resultVos.get(i).getTotCost13()+resultVos.get(i).getTotCost14()+resultVos.get(i).getTotCost15()+resultVos.get(i).getTotCost16();
    		}else if("3".equals(resultVos.get(i).getDeptName())){//惠济院区
    			hjZyyp += resultVos.get(i).getTotCost01()+resultVos.get(i).getTotCost02()+resultVos.get(i).getTotCost03();
    			hjZy += resultVos.get(i).getTotCost01()+resultVos.get(i).getTotCost02()+resultVos.get(i).getTotCost03()+resultVos.get(i).getTotCost04()+resultVos.get(i).getTotCost05()
    					+resultVos.get(i).getTotCost07()+resultVos.get(i).getTotCost08()+resultVos.get(i).getTotCost09()+resultVos.get(i).getTotCost10()+resultVos.get(i).getTotCost11()
    					+resultVos.get(i).getTotCost12()+resultVos.get(i).getTotCost13()+resultVos.get(i).getTotCost14()+resultVos.get(i).getTotCost15()+resultVos.get(i).getTotCost16();
    		}
		}
    	if(hyZyyp!=0.0||hyZy!=0.0||zdZyyp!=0.0||zdZy!=0.0||hjZyyp!=0.0||hjZy!=0.0){
    		BigDecimal   b   =   new   BigDecimal(((hyZyyp+zdZyyp+hjZyyp+hyMzyp+zdMzyp+hjMzyp)/(hyMz+zdMz+hjMz+hyZy+zdZy+hjZy))*100);  
        	double   f1   =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
        	drugProportion = f1 +"";//药占比
        	BigDecimal   b2   =   new   BigDecimal(((hyZyyp+hyMzyp)/(hyMz+hyZy))*100);
        	double   f2   =   b2.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
    		drugProportionHy = f2+"";//河医院区药占比
    		if(zdZyyp!=0.0||zdMzyp!=0.0||zdZy!=0.0||zdMz!=0.0){
    			BigDecimal   b3   =   new   BigDecimal(((zdZyyp+zdMzyp)/(zdMz+zdZy))*100);
    			double   f3   =   b3.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
    			drugProportionZd = f3+"";//郑东院区药占比
    		}
    		if(hjZyyp!=0.0||hjMzyp!=0.0||hjMz!=0.0||hjZy!=0.0){
    			BigDecimal   b4   =   new   BigDecimal(((hjZyyp+hjMzyp)/(hjMz+hjZy))*100);
    			double   f4   =   b4.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
    			drugProportionHj = f4+"";//惠济院区药占比
    		}
    	}
    	HospitaldayVo hospitaldayVo25 = new HospitaldayVo();
    	hospitaldayVo25.setValue(drugProportion);
    	hospitaldayVo25.setName("药占比");
    	hospitaldayVo25.setFlag("7h");
        HospitaldayVo hospitaldayVo26 = new HospitaldayVo();
        hospitaldayVo26.setValue(drugProportionHy);
        hospitaldayVo26.setName("河医院区");
        hospitaldayVo26.setFlag("7");
        HospitaldayVo hospitaldayVo27 = new HospitaldayVo();
        hospitaldayVo27.setValue(drugProportionZd);
        hospitaldayVo27.setName("郑东院区");
        hospitaldayVo27.setFlag("7");
        HospitaldayVo hospitaldayVo28 = new HospitaldayVo();
        hospitaldayVo28.setValue(drugProportionHj);
        hospitaldayVo28.setName("惠济院区");
        hospitaldayVo28.setFlag("7");
        List<HospitaldayVo> vos = new ArrayList<HospitaldayVo>();
        vos.add(hospitaldayVo1);
        vos.add(hospitaldayVo2);
        vos.add(hospitaldayVo3);
        vos.add(hospitaldayVo4);
        vos.add(hospitaldayVo5);
        vos.add(hospitaldayVo6);
        vos.add(hospitaldayVo7);
        vos.add(hospitaldayVo8);
        vos.add(hospitaldayVo9);
        vos.add(hospitaldayVo10);
        vos.add(hospitaldayVo11);
        vos.add(hospitaldayVo12);
        vos.add(hospitaldayVo13);
        vos.add(hospitaldayVo14);
        vos.add(hospitaldayVo15);
        vos.add(hospitaldayVo16);
        vos.add(hospitaldayVo17);
        vos.add(hospitaldayVo18);
        vos.add(hospitaldayVo19);
        vos.add(hospitaldayVo20);
        vos.add(hospitaldayVo21);
        vos.add(hospitaldayVo22);
        vos.add(hospitaldayVo23);
        vos.add(hospitaldayVo24);
        vos.add(hospitaldayVo25);
        vos.add(hospitaldayVo26);
        vos.add(hospitaldayVo27);
        vos.add(hospitaldayVo28);
		hospitaldayVo.setOutpatientNum(outpatientNum);//门诊人次合计
		hospitaldayVo.setOutpatientNumHy(outpatientNumHy);//门诊人次河医院区
		hospitaldayVo.setOutpatientNumZd(outpatientNumZd);//门诊人次郑东院区
		hospitaldayVo.setOutpatientNumHj(outpatientNumHj);//门诊人次惠济院区
    	hospitaldayVo.setInpatientNum(inpatientNum);//在院人数
    	hospitaldayVo.setInpatientNumHy(inpatientNumHy);//在院人数河医
    	hospitaldayVo.setInpatientNumZd(inpatientNumZd);//在院人数郑东
    	hospitaldayVo.setInpatientNumHj(inpatientNumHj);//在院人数惠济
    	hospitaldayVo.setOperationNum(operationNum);//手术人数
    	hospitaldayVo.setOperationNumHy(operationNumHy);//手术人数河医
    	hospitaldayVo.setOperationNumZd(operationNumZd);//手术人数郑东
    	hospitaldayVo.setOperationNumHj(operationNumHj);//手术人数惠济
    	hospitaldayVo.setLeaveHospitalNum(leaveHospitalNum);//出院人次
    	hospitaldayVo.setLeaveHospitalNumHy(leaveHospitalNumHy);//河医院区出院人次
    	hospitaldayVo.setLeaveHospitalNumZd(leaveHospitalNumZd);//郑东院区出院人次
    	hospitaldayVo.setLeaveHospitalNumHj(leaveHospitalNumHj);//惠济院区出院人次
    	hospitaldayVo.setInHospitalNum(inHospitalNum);//入院人次
    	hospitaldayVo.setInHospitalNumHy(inHospitalNumHy);//河医院区入院人次
    	hospitaldayVo.setInHospitalNumZd(inHospitalNumZd);//郑东院区入院人次
    	hospitaldayVo.setInHospitalNumHj(inHospitalNumHj);//惠济院区入院人次
    	hospitaldayVo.setIncomeCost(incomeCost);//总收入
    	hospitaldayVo.setIncomeCostHy(incomeCostHy);//河医收入
    	hospitaldayVo.setIncomeCostZd(incomeCostZd);//郑东收入
    	hospitaldayVo.setIncomeCostHj(incomeCostHj);//惠济收入
    	hospitaldayVo.setDrugProportion(drugProportion);//药占比
    	hospitaldayVo.setDrugProportionHy(drugProportionHy);//河医院区药占比
    	hospitaldayVo.setDrugProportionZd(drugProportionZd);//郑东院区药占比
    	hospitaldayVo.setDrugProportionHj(drugProportionHj);//惠济院区药占比
    	DBObject query = new BasicDBObject();
		query.put("timeValue", endTime);//移除数据条件
		String menuAlias = "DRJYSJ";
		new MongoBasicDao().remove(menuAlias+"_DAY", query);//删除原来的数据
		
		if(list!=null && list.size()>0){
			List<DBObject> userList = new ArrayList<DBObject>();
			BasicDBObject obj = new BasicDBObject();
			obj.append("outpatientNum", outpatientNum);
			obj.append("outpatientNumHy", outpatientNumHy);
			obj.append("outpatientNumZd", outpatientNumZd);
			obj.append("outpatientNumHj", outpatientNumHj);
			
			obj.append("inpatientNum", inpatientNum);
			obj.append("inpatientNumHy", inpatientNumHy);
			obj.append("inpatientNumZd", inpatientNumZd);
			obj.append("inpatientNumHj", inpatientNumHj);
			
			obj.append("operationNum", operationNum);
			obj.append("operationNumHy", operationNumHy);
			obj.append("operationNumZd", operationNumZd);
			obj.append("operationNumHj", operationNumHj);
			
			obj.append("leaveHospitalNum", leaveHospitalNum);
			obj.append("leaveHospitalNumHy", leaveHospitalNumHy);
			obj.append("leaveHospitalNumZd", leaveHospitalNumZd);
			obj.append("leaveHospitalNumHj", leaveHospitalNumHj);
			
			obj.append("inHospitalNum", inHospitalNum);
			obj.append("inHospitalNumHy", inHospitalNumHy);
			obj.append("inHospitalNumZd", inHospitalNumZd);
			obj.append("inHospitalNumHj", inHospitalNumHj);
			
			obj.append("incomeCost", incomeCost);
			obj.append("incomeCostHy", incomeCostHy);
			obj.append("incomeCostZd", incomeCostZd);
			obj.append("incomeCostHj", incomeCostHj);
			
			obj.append("drugProportion", drugProportion);
			obj.append("drugProportionHy", drugProportionHy);
			obj.append("drugProportionZd", drugProportionZd);
			obj.append("drugProportionHj", drugProportionHj);
			obj.append("timeValue", endTime);
			userList.add(obj);
			new MongoBasicDao().insertDataByList(menuAlias+"_DAY", userList);
		}
		Date beginDate=new Date();
		wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_DAY", list, endTime);
	}
	@Override
	public Map<String, Object> init_YYMRHZ(String beginDate, String endDate,Integer type) {
//		HospitaldayVo hospitaldayVo = new HospitaldayVo();
		String outpatientNum = "0";//门诊人次合计
		String outpatientNumHy = "0";//河医院区门诊人次
		String outpatientNumZd = "0";//郑东院区门诊人次
		String outpatientNumHj = "0";//惠济院区门诊人次
		String inpatientNum = "0" ;//在院人数
		String inpatientNumHy = "0" ;//在院人数河医
		String inpatientNumZd = "0" ;//在院人数郑东
		String inpatientNumHj = "0" ;//在院人数惠济
		String operationNum = "0" ;//手术人数
		String operationNumHy = "0" ;//手术人数河医
		String operationNumZd = "0" ;//手术人数郑东
		String operationNumHj = "0" ;//手术人数惠济
		String incomeCost = "0";//总收入
		String incomeCostHy = "0";//河医院区收入
		String incomeCostZd = "0";//郑东院区收入
		String incomeCostHj = "0";//惠济院区收入
		String leaveHospitalNum = "0";//出院人次
		String leaveHospitalNumHy = "0";//河医院区出院人次
		String leaveHospitalNumZd = "0";//郑东院区出院人次
		String leaveHospitalNumHj = "0";//惠济院区出院人次
		String inHospitalNum = "0";//入院人次
		String inHospitalNumHy = "0";//河医院区入院人次
		String inHospitalNumZd = "0";//郑东院区入院人次
		String inHospitalNumHj = "0";//惠济院区入院人次
		String drugProportion = "0";//药占比
		String drugProportionHy = "0";//河医院区药占比
		String drugProportionZd = "0";//郑东院区药占比
		String drugProportionHj = "0";//惠济院区药占比
		BasicDBObject bdObject2 = new BasicDBObject();
    	bdObject2.append("timeValue", endDate);
		DBCursor cursorle = new MongoBasicDao().findAlldata("DRJYSJ_DAY", bdObject2);
		DBObject dbCursorle;
		while(cursorle.hasNext()){
			dbCursorle = cursorle.next();
			outpatientNum = (String) dbCursorle.get("outpatientNum") ;//门诊人次
			outpatientNumHy = (String) dbCursorle.get("outpatientNumHy") ;//河医院区门诊人次
			outpatientNumZd = (String) dbCursorle.get("outpatientNumZd") ;//郑东院区门诊人次
			outpatientNumHj = (String) dbCursorle.get("outpatientNumHj") ;//惠济院区门诊人次
			
			inpatientNum = (String) dbCursorle.get("inpatientNum") ;//在院人数
			inpatientNumHy = (String) dbCursorle.get("inpatientNumHy") ;//在院人数河医
			inpatientNumZd = (String) dbCursorle.get("inpatientNumZd") ;//在院人数郑东
			inpatientNumHj = (String) dbCursorle.get("inpatientNumHj") ;//在院人数惠济
			
			operationNum = (String) dbCursorle.get("operationNum") ;//手术人数
			operationNumHy = (String) dbCursorle.get("operationNumHy")  ;//手术人数河医
			operationNumZd = (String) dbCursorle.get("operationNumZd")  ;//手术人数郑东
			operationNumHj = (String) dbCursorle.get("operationNumHj") ;//手术人数惠济
			
			incomeCost = (String) dbCursorle.get("incomeCost");//总收入
			incomeCostHy = (String) dbCursorle.get("incomeCostHy");//河医院区收入
			incomeCostZd = (String) dbCursorle.get("incomeCostZd");//郑东院区收入
			incomeCostHj = (String) dbCursorle.get("incomeCostHj");//惠济院区收入
			
			leaveHospitalNum = (String) dbCursorle.get("leaveHospitalNum");//出院人次
			leaveHospitalNumHy = (String) dbCursorle.get("leaveHospitalNumHy");//河医院区出院人次
			leaveHospitalNumZd = (String) dbCursorle.get("leaveHospitalNumZd");//郑东院区出院人次
			leaveHospitalNumHj = (String) dbCursorle.get("leaveHospitalNumHj");//惠济院区出院人次
			
			inHospitalNum = (String) dbCursorle.get("inHospitalNum");//入院人次
			inHospitalNumHy = (String) dbCursorle.get("inHospitalNumHy");//河医院区入院人次
			inHospitalNumZd = (String) dbCursorle.get("inHospitalNumZd");//郑东院区入院人次
			inHospitalNumHj = (String) dbCursorle.get("inHospitalNumHj");//惠济院区入院人次
			
			drugProportion = (String) dbCursorle.get("drugProportion");//药品金额
			drugProportionHy = (String) dbCursorle.get("drugProportionHy");//河医院区药品金额
			drugProportionZd = (String) dbCursorle.get("drugProportionZd");//郑东院区药品金额
			drugProportionHj = (String) dbCursorle.get("drugProportionHj");//惠济院区药品金额
		}
		HospitaldayVo hospitaldayVo1 = new HospitaldayVo();
        hospitaldayVo1.setValue(outpatientNum);
        hospitaldayVo1.setName("门诊人次合计");
        HospitaldayVo hospitaldayVo2 = new HospitaldayVo();
        
        outpatientNumHy = (Double.valueOf(outpatientNum)-Double.valueOf(outpatientNumZd)-Double.valueOf(outpatientNumHj))+"";
        outpatientNumHy = outpatientNumHy.substring(0, outpatientNumHy.indexOf("."));
        hospitaldayVo2.setValue(outpatientNumHy);
        hospitaldayVo2.setName("河医院区");
        HospitaldayVo hospitaldayVo3 = new HospitaldayVo();
        hospitaldayVo3.setValue(outpatientNumZd);
        hospitaldayVo3.setName("郑东院区");
        HospitaldayVo hospitaldayVo4 = new HospitaldayVo();
        hospitaldayVo4.setValue(outpatientNumHj);
        hospitaldayVo4.setName("惠济院区");
    	
        HospitaldayVo hospitaldayVo5 = new HospitaldayVo();
		hospitaldayVo5.setValue(inpatientNum);
		hospitaldayVo5.setName("在院人次合计");
		inpatientNumHy = (Double.valueOf(inpatientNum)-Double.valueOf(inpatientNumZd)-Double.valueOf(inpatientNumHj))+"";
		inpatientNumHy = inpatientNumHy.substring(0, inpatientNumHy.indexOf("."));
        HospitaldayVo hospitaldayVo6 = new HospitaldayVo();
        hospitaldayVo6.setValue(inpatientNumHy);
        hospitaldayVo6.setName("河医院区");
        HospitaldayVo hospitaldayVo7 = new HospitaldayVo();
        hospitaldayVo7.setValue(inpatientNumZd);
        hospitaldayVo7.setName("郑东院区");
        HospitaldayVo hospitaldayVo8 = new HospitaldayVo();
        hospitaldayVo8.setValue(inpatientNumHj);
        hospitaldayVo8.setName("惠济院区");
        
        HospitaldayVo hospitaldayVo9 = new HospitaldayVo();
		hospitaldayVo9.setValue(operationNum);
		hospitaldayVo9.setName("手术人次合计");
		operationNumHy = (Double.valueOf(operationNum)-Double.valueOf(operationNumZd)-Double.valueOf(operationNumHj))+"";
		operationNumHy = operationNumHy.substring(0, operationNumHy.indexOf("."));
        HospitaldayVo hospitaldayVo10 = new HospitaldayVo();
        hospitaldayVo10.setValue(operationNumHy);
        hospitaldayVo10.setName("河医院区");
        HospitaldayVo hospitaldayVo11 = new HospitaldayVo();
        hospitaldayVo11.setValue(operationNumZd);
        hospitaldayVo11.setName("郑东院区");
        HospitaldayVo hospitaldayVo12 = new HospitaldayVo();
        hospitaldayVo12.setValue(operationNumHj);
        hospitaldayVo12.setName("惠济院区");
        
        incomeCostHy = (Double.valueOf(incomeCost)-Double.valueOf(incomeCostZd)-Double.valueOf(incomeCostHj))+"";
        
        drugProportionHy = (Double.valueOf(drugProportion)-Double.valueOf(drugProportionZd)-Double.valueOf(drugProportionHj))+"";
        String drugblHy = "0";
        String drugblZd = "0";
        String drugblHj = "0";
        if("0.0".equals(incomeCostHy)){
        	drugblHy = "0";
        }else{
        	drugblHy = (Double.valueOf(drugProportionHy)/Double.valueOf(incomeCostHy))*100+"";
        }
        if("0".equals(incomeCostZd)){
        	drugblZd = "0";
        }else{
        	drugblZd = (Double.valueOf(drugProportionZd)/Double.valueOf(incomeCostZd))*100+"";
        }
        if("0".equals(incomeCostHj)){
        	drugblHj = "0";
        }else{
        	drugblHj = (Double.valueOf(drugProportionHj)/Double.valueOf(incomeCostHj))*100+"";
        }
        String drugbl = "";
        if(incomeCost=="0"){
            drugbl = "0";
        }else{
        	drugbl = (Double.valueOf(drugProportion)/Double.valueOf(incomeCost))*100+"";
        }
        HospitaldayVo hospitaldayVo25 = new HospitaldayVo();
        BigDecimal   b10   =   new   BigDecimal(Double.valueOf(drugbl));  
    	double   f10   =   b10.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    	hospitaldayVo25.setValue(f10+"");
    	hospitaldayVo25.setName("药占比");
        HospitaldayVo hospitaldayVo26 = new HospitaldayVo();
        BigDecimal   b11   =   new   BigDecimal(Double.valueOf(drugblHy));  
    	double   f11   =   b11.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    	BigDecimal   drug1   =   new   BigDecimal(Double.valueOf(drugProportionHy)/10000);  
    	double   drug2   =   drug1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        hospitaldayVo26.setValue(drug2+"万,"+f11+"%");
        hospitaldayVo26.setName("河医院区");
        HospitaldayVo hospitaldayVo27 = new HospitaldayVo();
        BigDecimal   b12   =   new   BigDecimal(Double.valueOf(drugblZd));  
    	double   f12   =   b12.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    	BigDecimal   drug3   =   new   BigDecimal(Double.valueOf(drugProportionZd)/10000);  
    	double   drug4   =   drug3.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    	if(drug4==0.0&&f12==0.0){
    		hospitaldayVo27.setValue("0,0");
    	}else{
    		hospitaldayVo27.setValue(drug4+"万,"+f12+"%");
    	}
        //hospitaldayVo27.setValue(drug4+"万,"+f12+"%");
        hospitaldayVo27.setName("郑东院区");
        HospitaldayVo hospitaldayVo28 = new HospitaldayVo();
        BigDecimal   b13   =   new   BigDecimal(Double.valueOf(drugblHj));  
    	double   f13   =   b13.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    	BigDecimal   drug5   =   new   BigDecimal(Double.valueOf(drugProportionHj)/10000);  
    	double   drug6   =   drug5.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    	if(drug6==0.0&&f13==0.0){
    		hospitaldayVo28.setValue("0,0");
    	}else{
    		hospitaldayVo28.setValue(drug6+"万,"+f13+"%");
    	}
        //hospitaldayVo28.setValue(drug6+"万,"+f13+"%");
        hospitaldayVo28.setName("惠济院区");
        
        HospitaldayVo hospitaldayVo14 = new HospitaldayVo();
        HospitaldayVo hospitaldayVo13 = new HospitaldayVo();
        BigDecimal   b   =   new   BigDecimal(Double.valueOf(incomeCost)/10000);  
    	double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        hospitaldayVo13.setValue(f1+"");
        hospitaldayVo13.setName("总收入");
        BigDecimal   b1   =   new   BigDecimal(Double.valueOf(incomeCostHy)/10000);  
    	double   f2   =   b1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                 hospitaldayVo14.setValue(f2+"");
        hospitaldayVo14.setName("河医院区");
        HospitaldayVo hospitaldayVo15 = new HospitaldayVo();
        BigDecimal   b2   =   new   BigDecimal(Double.valueOf(incomeCostZd)/10000);  
    	double   f3   =   b2.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        hospitaldayVo15.setValue(f3+"");
        hospitaldayVo15.setName("郑东院区");
        HospitaldayVo hospitaldayVo16 = new HospitaldayVo();
        BigDecimal   b3   =   new   BigDecimal(Double.valueOf(incomeCostHj)/10000);  
    	double   f4   =   b3.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        hospitaldayVo16.setValue(f4+"");
        hospitaldayVo16.setName("惠济院区");
        
        HospitaldayVo hospitaldayVo17 = new HospitaldayVo();
		hospitaldayVo17.setValue(leaveHospitalNum);
		hospitaldayVo17.setName("出院人次合计");
        HospitaldayVo hospitaldayVo18 = new HospitaldayVo();
        leaveHospitalNumHy = (Double.valueOf(leaveHospitalNum)-Double.valueOf(leaveHospitalNumZd)-Double.valueOf(leaveHospitalNumHj))+"";
        leaveHospitalNumHy = leaveHospitalNumHy.substring(0, leaveHospitalNumHy.indexOf("."));
        hospitaldayVo18.setValue(leaveHospitalNumHy);
        hospitaldayVo18.setName("河医院区");
        HospitaldayVo hospitaldayVo19 = new HospitaldayVo();
        hospitaldayVo19.setValue(leaveHospitalNumZd);
        hospitaldayVo19.setName("郑东院区");
        HospitaldayVo hospitaldayVo20 = new HospitaldayVo();
        hospitaldayVo20.setValue(leaveHospitalNumHj);
        hospitaldayVo20.setName("惠济院区");
        
        HospitaldayVo hospitaldayVo21 = new HospitaldayVo();
		hospitaldayVo21.setValue(inHospitalNum);
		hospitaldayVo21.setName("入院人次合计");
        HospitaldayVo hospitaldayVo22 = new HospitaldayVo();
        inHospitalNumHy = (Double.valueOf(inHospitalNum)-Double.valueOf(inHospitalNumZd)-Double.valueOf(inHospitalNumHj))+"";
        inHospitalNumHy = inHospitalNumHy.substring(0, inHospitalNumHy.indexOf("."));
        hospitaldayVo22.setValue(inHospitalNumHy);
        hospitaldayVo22.setName("河医院区");
        HospitaldayVo hospitaldayVo23 = new HospitaldayVo();
        hospitaldayVo23.setValue(inHospitalNumZd);
        hospitaldayVo23.setName("郑东院区");
        HospitaldayVo hospitaldayVo24 = new HospitaldayVo();
        hospitaldayVo24.setValue(inHospitalNumHj);
        hospitaldayVo24.setName("惠济院区");
        
        List<HospitaldayVo> vos = new ArrayList<HospitaldayVo>();
        vos.add(hospitaldayVo1);
        vos.add(hospitaldayVo2);
        vos.add(hospitaldayVo3);
        vos.add(hospitaldayVo4);
        vos.add(hospitaldayVo5);
        vos.add(hospitaldayVo6);
        vos.add(hospitaldayVo7);
        vos.add(hospitaldayVo8);
        vos.add(hospitaldayVo9);
        vos.add(hospitaldayVo10);
        vos.add(hospitaldayVo11);
        vos.add(hospitaldayVo12);
        vos.add(hospitaldayVo13);
        vos.add(hospitaldayVo14);
        vos.add(hospitaldayVo15);
        vos.add(hospitaldayVo16);
        vos.add(hospitaldayVo17);
        vos.add(hospitaldayVo18);
        vos.add(hospitaldayVo19);
        vos.add(hospitaldayVo20);
        vos.add(hospitaldayVo21);
        vos.add(hospitaldayVo22);
        vos.add(hospitaldayVo23);
        vos.add(hospitaldayVo24);
        vos.add(hospitaldayVo25);
        vos.add(hospitaldayVo26);
        vos.add(hospitaldayVo27);
        vos.add(hospitaldayVo28);
        ConfigVo configVo = new ConfigVo();
        configVo.setType("pie");//饼状图
        configVo.setUnit("人次");
        configVo.setTitalinfo("");
        //门诊人次
        List<HospitaldayVo> value1 = new ArrayList<HospitaldayVo>();
        value1.add(hospitaldayVo2);
        value1.add(hospitaldayVo3);
        value1.add(hospitaldayVo4);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("data", value1);
        map2.put("total", hospitaldayVo1);
        map2.put("config", configVo);
        
        //在院人次
        List<HospitaldayVo> value2 = new ArrayList<HospitaldayVo>();
        value2.add(hospitaldayVo6);
        value2.add(hospitaldayVo7);
        value2.add(hospitaldayVo8);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("data", value2);
        map3.put("total", hospitaldayVo5);
        map3.put("config", configVo);
        
        //手术人次
        List<HospitaldayVo> value3 = new ArrayList<HospitaldayVo>();
        value3.add(hospitaldayVo10);
        value3.add(hospitaldayVo11);
        value3.add(hospitaldayVo12);
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("data", value3);
        map4.put("total", hospitaldayVo9);
        map4.put("config", configVo);
        
        //收入
        List<HospitaldayVo> value4 = new ArrayList<HospitaldayVo>();
        value4.add(hospitaldayVo14);
        value4.add(hospitaldayVo15);
        value4.add(hospitaldayVo16);
        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("data", value4);
        map5.put("total", hospitaldayVo13);
        ConfigVo configVo4 = new ConfigVo();
        configVo4.setType("pie");//饼状图
        configVo4.setUnit("万元");
        configVo4.setTitalinfo("");
        map5.put("config", configVo4);
        
        //出院人次
        List<HospitaldayVo> value5 = new ArrayList<HospitaldayVo>();
        value5.add(hospitaldayVo18);
        value5.add(hospitaldayVo19);
        value5.add(hospitaldayVo20);
        Map<String, Object> map6 = new HashMap<String, Object>();
        map6.put("data", value5);
        map6.put("total", hospitaldayVo17);
        map6.put("config", configVo);
        
        //入院人次
        List<HospitaldayVo> value6 = new ArrayList<HospitaldayVo>();
        value6.add(hospitaldayVo22);
        value6.add(hospitaldayVo23);
        value6.add(hospitaldayVo24);
        Map<String, Object> map7 = new HashMap<String, Object>();
        map7.put("data", value6);
        map7.put("total", hospitaldayVo21);
        map7.put("config", configVo);
        
        //药占比
        List<HospitaldayVo> value7 = new ArrayList<HospitaldayVo>();
        value7.add(hospitaldayVo26);
        value7.add(hospitaldayVo27);
        value7.add(hospitaldayVo28);
        Map<String, Object> map8 = new HashMap<String, Object>();
        map8.put("data", value7);
        map8.put("total", hospitaldayVo25);
        ConfigVo configVo7 = new ConfigVo();
        configVo7.setType("table");//表格
        configVo7.setUnit("%");
        configVo7.setTitalinfo("院区,院区药品收入,院区药占比");
        map8.put("config", configVo7);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resCode", "success");
        map.put("resMsg", "数据查询成功");
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("0", map2);//门诊人次
        map1.put("1", map3);//在院人次
        map1.put("2", map4);//手术人次
        map1.put("3", map5);//收入
        map1.put("4", map6);//出院人次
        map1.put("5", map7);//入院人次
        map1.put("6", map8);//药占比
        map.put("baseData", map1);
        
    	return map;
	}

	@Override
	public void init_DRJYSJ(String startTime, String endTime,
			Integer type) {
		List<HospitaldayVo> list = hospitaldayDao.queryList(startTime);
		DBObject query = new BasicDBObject();
		query.put("timeValue", startTime);//移除数据条件
		String menuAlias = "DRJYSJ";
		new MongoBasicDao().remove(menuAlias+"_DAY", query);//删除原来的数据
		
		if(list!=null && list.size()>0){
			List<DBObject> userList = new ArrayList<DBObject>();
			BasicDBObject obj = new BasicDBObject();
			String outpatientNum = "0";//门诊人次合计
			String outpatientNumHy = "0";//河医院区门诊人次
			String outpatientNumZd = "0";//郑东院区门诊人次
			String outpatientNumHj = "0";//惠济院区门诊人次
			String inpatientNum = "0" ;//在院人数
			String inpatientNumHy = "0" ;//在院人数河医
			String inpatientNumZd = "0" ;//在院人数郑东
			String inpatientNumHj = "0" ;//在院人数惠济
			String operationNum = "0" ;//手术人数
			String operationNumHy = "0" ;//手术人数河医
			String operationNumZd = "0" ;//手术人数郑东
			String operationNumHj = "0" ;//手术人数惠济
			String incomeCost = "0";//总收入
			String incomeCostHy = "0";//河医院区收入
			String incomeCostZd = "0";//郑东院区收入
			String incomeCostHj = "0";//惠济院区收入
			String leaveHospitalNum = "0";//出院人次
			String leaveHospitalNumHy = "0";//河医院区出院人次
			String leaveHospitalNumZd = "0";//郑东院区出院人次
			String leaveHospitalNumHj = "0";//惠济院区出院人次
			String inHospitalNum = "0";//入院人次
			String inHospitalNumHy = "0";//河医院区入院人次
			String inHospitalNumZd = "0";//郑东院区入院人次
			String inHospitalNumHj = "0";//惠济院区入院人次
			String drugProportion = "0";//药占比
			String drugProportionHy = "0";//河医院区药占比
			String drugProportionZd = "0";//郑东院区药占比
			String drugProportionHj = "0";//惠济院区药占比
			if(list.get(0).getOutpatientNum()!=null){
				outpatientNum = list.get(0).getOutpatientNum();
			}
			if(list.get(0).getOutpatientNumHy()!=null){
				outpatientNumHy = list.get(0).getOutpatientNumHy();
			}
			if(list.get(0).getOutpatientNumZd()!=null){
				outpatientNumZd = list.get(0).getOutpatientNumZd();
			}
			if(list.get(0).getOutpatientNumHj()!=null){
				outpatientNumHj = list.get(0).getOutpatientNumHj();
			}
			obj.append("outpatientNum", outpatientNum);
			obj.append("outpatientNumHy", outpatientNumHy);
			obj.append("outpatientNumZd", outpatientNumZd);
			obj.append("outpatientNumHj", outpatientNumHj);
			
			if(list.get(0).getInpatientNum()!=null){
				inpatientNum = list.get(0).getInpatientNum();
			}
			if(list.get(0).getInpatientNumHy()!=null){
				inpatientNumHy = list.get(0).getInpatientNumHy();
			}
			if(list.get(0).getInpatientNumZd()!=null){
				inpatientNumZd = list.get(0).getInpatientNumZd();
			}
			if(list.get(0).getInpatientNumHj()!=null){
				inpatientNumHj = list.get(0).getInpatientNumHj();
			}
			obj.append("inpatientNum", inpatientNum);
			obj.append("inpatientNumHy", inpatientNumHy);
			obj.append("inpatientNumZd", inpatientNumZd);
			obj.append("inpatientNumHj", inpatientNumHj);
			
			if(list.get(0).getOperationNum()!=null){
				operationNum = list.get(0).getOperationNum();
			}
			if(list.get(0).getOperationNumHy()!=null){
				operationNumHy = list.get(0).getOperationNumHy();
			}
			if(list.get(0).getOperationNumZd()!=null){
				operationNumZd = list.get(0).getOperationNumZd();
			}
			if(list.get(0).getOperationNumHj()!=null){
				operationNumHj = list.get(0).getOperationNumHj();
			}
			obj.append("operationNum", operationNum);
			obj.append("operationNumHy", operationNumHy);
			obj.append("operationNumZd", operationNumZd);
			obj.append("operationNumHj", operationNumHj);
			
			if(list.get(0).getLeaveHospitalNum()!=null){
				leaveHospitalNum = list.get(0).getLeaveHospitalNum();
			}
			if(list.get(0).getLeaveHospitalNumHy()!=null){
				leaveHospitalNumHy = list.get(0).getLeaveHospitalNumHy();
			}
			if(list.get(0).getLeaveHospitalNumZd()!=null){
				leaveHospitalNumZd = list.get(0).getLeaveHospitalNumZd();
			}
			if(list.get(0).getLeaveHospitalNumHj()!=null){
				leaveHospitalNumHj = list.get(0).getLeaveHospitalNumHj();
			}
			obj.append("leaveHospitalNum", leaveHospitalNum);
			obj.append("leaveHospitalNumHy", leaveHospitalNumHy);
			obj.append("leaveHospitalNumZd", leaveHospitalNumZd);
			obj.append("leaveHospitalNumHj", leaveHospitalNumHj);
			
			if(list.get(0).getInHospitalNum()!=null){
				inHospitalNum = list.get(0).getInHospitalNum();
			}
			if(list.get(0).getInHospitalNumHy()!=null){
				inHospitalNumHy = list.get(0).getInHospitalNumHy();
			}
			if(list.get(0).getInHospitalNumZd()!=null){
				inHospitalNumZd = list.get(0).getInHospitalNumZd();
			}
			if(list.get(0).getInHospitalNumHj()!=null){
				inHospitalNumHj = list.get(0).getInHospitalNumHj();
			}
			obj.append("inHospitalNum", inHospitalNum);
			obj.append("inHospitalNumHy", inHospitalNumHy);
			obj.append("inHospitalNumZd", inHospitalNumZd);
			obj.append("inHospitalNumHj", inHospitalNumHj);
			
			if(list.get(0).getIncomeCost()!=null){
				incomeCost = list.get(0).getIncomeCost();
			}
			if(list.get(0).getIncomeCostHy()!=null){
				incomeCostHy = list.get(0).getIncomeCostHy();
			}
			if(list.get(0).getIncomeCostZd()!=null){
				incomeCostZd = list.get(0).getIncomeCostZd();
			}
			if(list.get(0).getIncomeCostHj()!=null){
				incomeCostHj = list.get(0).getIncomeCostHj();
			}
			obj.append("incomeCost", incomeCost);
			obj.append("incomeCostHy", incomeCostHy);
			obj.append("incomeCostZd", incomeCostZd);
			obj.append("incomeCostHj", incomeCostHj);
			
			if(list.get(0).getDrugProportion()!=null){
				drugProportion = list.get(0).getDrugProportion();
			}
			if(list.get(0).getDrugProportionHy()!=null){
				drugProportionHy = list.get(0).getDrugProportionHy();
			}
			if(list.get(0).getDrugProportionZd()!=null){
				drugProportionZd = list.get(0).getDrugProportionZd();
			}
			if(list.get(0).getDrugProportionHj()!=null){
				drugProportionHj = list.get(0).getDrugProportionHj();
			}
			obj.append("drugProportion", drugProportion);
			obj.append("drugProportionHy", drugProportionHy);
			obj.append("drugProportionZd", drugProportionZd);
			obj.append("drugProportionHj", drugProportionHj);
			obj.append("timeValue", startTime);
			userList.add(obj);
			new MongoBasicDao().insertDataByList(menuAlias+"_DAY", userList);
		}
		Date beginDate=new Date();
		wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_DAY", list, startTime);
	}

	@Override
	public Map<String, Object> queryDate(String begin, String end,
			 String rows, String page) {
		Map<String, Object> map=new HashMap<String,Object>();
		if(new MongoBasicDao().isCollection(queryMong)&&StringUtils.isNotBlank(rows)&&StringUtils.isNotBlank(page)){
			map=hospitaldayDao.queryDate(begin, end, queryMong, rows, page);
		}else{
			map.put("total", 0);
			map.put("rows", new ArrayList<HospitaldayVo>());
		}
		return map;
	}
}
