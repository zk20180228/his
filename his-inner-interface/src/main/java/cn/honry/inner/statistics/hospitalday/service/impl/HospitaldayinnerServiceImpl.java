package cn.honry.inner.statistics.hospitalday.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessYzcxzhcx;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.hospitalday.dao.HospitaldayinnerDao;
import cn.honry.inner.statistics.hospitalday.service.HospitaldayinnerService;
import cn.honry.inner.statistics.hospitalday.vo.HospitaldayVo;
import cn.honry.inner.statistics.hospitalday.vo.ResultVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service("hospitaldayinnerService")
@Transactional
@SuppressWarnings({"all"})
public class HospitaldayinnerServiceImpl implements HospitaldayinnerService{
	//驱动程序就是之前在classpath中配置的jdbc的驱动程序jar中
    public static final String drive = "oracle.jdbc.driver.OracleDriver";
    
    private Logger logger=Logger.getLogger(HospitaldayinnerService.class);
    /**
     * 连接地址
     * jdbc:oracle
     */
    public static final String url = "jdbc:oracle:thin:@129.1.169.38:1521:his2";
    /**
     * 用户 密码
     */
    public static final String DBUSER="zdxxc";
    public static final String password="ZDYFYDataETL2018";
	@Autowired
	@Qualifier(value = "hospitaldayinnerDao")
	private HospitaldayinnerDao hospitaldayinnerDao;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	private DeptInInterService deptInInterService;
	
	@Autowired
	@Qualifier(value="deptInInterService")
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Override
	public void init_YYMRHZ(String beginDate, String endDate,String type) {
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
		
		Map<String, Object> toListViewMap = new HashMap<String, Object>();
		String key="MJZRCTJ_DAY";
    	BasicDBObject where = new BasicDBObject();
    	where.append("date", endDate);
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
        List<HospitaldayVo> voss = hospitaldayinnerDao.queryHospitaldayVoin(endDate);
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
        List<HospitaldayVo> vosss = hospitaldayinnerDao.queryHospitaldayVoopear(endDate);
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
    	where1.append("date", endDate);
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
        List<HospitaldayVo> vossss = hospitaldayinnerDao.queryHospitaldayVooutpa(endDate);
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
		List<HospitaldayVo> list = hospitaldayinnerDao.queryHospitaldayList(null, null, endDate, endDate);
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
		List<HospitaldayVo> list4 = hospitaldayinnerDao.querymz(null, null, endDate, endDate);
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
		
    	List<ResultVo> resultVos = hospitaldayinnerDao.queryzhuyanList(null,endDate);
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
		query.put("timeValue", endDate);//移除数据条件
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
			obj.append("timeValue", endDate);
			userList.add(obj);
			new MongoBasicDao().insertDataByList(menuAlias+"_DAY", userList);
		}
		Date beginDate1=new Date();
		wordLoadDocDao.saveMongoLog(beginDate1, menuAlias+"_DAY", list, endDate);
	}
	@Override
	public Map<String,String> saveBusinessYzcxzhcx(String date){
		Map<String,String> map=new HashMap<String,String>();
		try {
			 Connection conn = null;//表示数据库连接
	         Statement stmt= null;//表示数据库的更新
	         ResultSet result = null;//查询数据库 
			 logger.info("开始");
	         Class.forName(drive);//使用class类来加载程序
	         logger.info("开始连接38数据库");
	         conn =DriverManager.getConnection(url,DBUSER,password); //连接数据库
	         logger.info("连接38数据库成功");
	         //Statement接口要通过connection接口来进行实例化操作
	         stmt = conn.createStatement();
	         //执行SQL语句来查询数据库
	         result =stmt.executeQuery("SELECT t.PZ_NUM,t.JYMZ_NUM,t.QTPT_NUM,t.ZMZJ_NUM,"
	         		+ "t.JS_NUM,t.FJS_NUM,t.JZ_NUM,t.SFHJ_NUM,t.SFZF_NUM,t.CFHJ_NUM,t.CFZF_NUM,"
	         		+ "t.ZDCF_COST,t.ZXCF_COST,t.CFHJ_COST,t.RY_NUM,t.ZK_NUM,t.CYWJ_NUM,t.CYYJ_NUM,"
	         		+ "t.ZYHJ_NUM,t.ZYZF_NUM,t.ZYNH_NUM,t.MZYP_COST,t.MZYL_COST,t.ZYYP_COST,t.ZYYL_COST,"
	         		+ "t.MZSS_NUM,t.MZSS_COST,t.ZYSS_NUM,t.ZYSS_COST,t.OPER_DATE,t.BED_BZ,t.SJRY_NUM,"
	         		+ "t.GHSR_COST,t.TSH_NUM,t.GHHJ_RS,t.YQ,t.GHHJ_NUM,t.SSTS FROM zdhis.YZCX_ZHCX t where t.OPER_DATE = to_date('"+date+"', 'yyyy-mm-dd')");
	         List<BusinessYzcxzhcx> list = new ArrayList<BusinessYzcxzhcx>();
	         BusinessYzcxzhcx businessYzcxzhcx = null;
	         while(result.next()){//判断有没有下一行
	        	 logger.info("查询"+date+"的数据成功");
	        	 BusinessYzcxzhcx businessYzcxzhcx2 = hospitaldayinnerDao.queryBusinessYzcxzhcx(result.getDate(30), result.getString(36));
	        	 if(businessYzcxzhcx2==null){
	        		 //保存
	             	 businessYzcxzhcx = new BusinessYzcxzhcx();
	                 BigDecimal pzNum =result.getBigDecimal(1);
	                 businessYzcxzhcx.setPzNum(pzNum);
	                 BigDecimal jymzNum =result.getBigDecimal(2);
	                 businessYzcxzhcx.setJymzNum(jymzNum); 
	                 BigDecimal qtptNum =result.getBigDecimal(3);
	                 businessYzcxzhcx.setQtptNum(qtptNum);
	                 BigDecimal zmzjNum =result.getBigDecimal(4);
	                 businessYzcxzhcx.setZmzjNum(zmzjNum);
	                 BigDecimal jsNum =result.getBigDecimal(5);
	                 businessYzcxzhcx.setJsNum(jsNum);
	                 BigDecimal fjsNum =result.getBigDecimal(6);
	                 businessYzcxzhcx.setFjsNum(fjsNum);
	                 BigDecimal jzNum =result.getBigDecimal(7);
	                 businessYzcxzhcx.setJzNum(jzNum);
	                 BigDecimal sfhjNum =result.getBigDecimal(8);
	                 businessYzcxzhcx.setSfhjNum(sfhjNum);
	                 BigDecimal sfzfNum =result.getBigDecimal(9);
	                 businessYzcxzhcx.setSfzfNum(sfzfNum);
	                 BigDecimal cfhjNum =result.getBigDecimal(10);
	                 businessYzcxzhcx.setCfhjNum(cfhjNum);
	                 BigDecimal cfzfNum =result.getBigDecimal(11);
	                 businessYzcxzhcx.setCfzfNum(cfzfNum);
	                 BigDecimal zdcfCost =result.getBigDecimal(12);
	                 businessYzcxzhcx.setZdcfCost(zdcfCost);
	                 BigDecimal zxcfCost =result.getBigDecimal(13);
	                 businessYzcxzhcx.setZxcfCost(zxcfCost);
	                 BigDecimal cfhjCost =result.getBigDecimal(14);
	                 businessYzcxzhcx.setCfhjCost(cfhjCost);
	                 BigDecimal ryNum =result.getBigDecimal(15);
	                 businessYzcxzhcx.setRyNum(ryNum);
	                 BigDecimal zkNum =result.getBigDecimal(16);
	                 businessYzcxzhcx.setZkNum(zkNum);
	                 BigDecimal cywjNum =result.getBigDecimal(17);
	                 businessYzcxzhcx.setCywjNum(cywjNum);
	                 BigDecimal cyyjNum =result.getBigDecimal(18);
	                 businessYzcxzhcx.setCyyjNum(cyyjNum);
	                 BigDecimal zyhjNum =result.getBigDecimal(19);
	                 businessYzcxzhcx.setZyhjNum(zyhjNum);
	                 BigDecimal zyzfNum =result.getBigDecimal(20);
	                 businessYzcxzhcx.setZyzfNum(zyzfNum);
	                 BigDecimal zynhNum =result.getBigDecimal(21);
	                 businessYzcxzhcx.setZynhNum(zynhNum);
	                 BigDecimal mzypCost =result.getBigDecimal(22);
	                 businessYzcxzhcx.setMzypCost(mzypCost);
	                 BigDecimal mzylCost =result.getBigDecimal(23);
	                 businessYzcxzhcx.setMzylCost(mzylCost);
	                 BigDecimal zyypCost =result.getBigDecimal(24);
	                 businessYzcxzhcx.setZyypCost(zyypCost);
	                 BigDecimal zyylCost =result.getBigDecimal(25);
	                 businessYzcxzhcx.setZyylCost(zyylCost);
	                 BigDecimal mzssNum =result.getBigDecimal(26);
	                 businessYzcxzhcx.setMzssNum(mzssNum);
	                 BigDecimal mzssCost =result.getBigDecimal(27);
	                 businessYzcxzhcx.setMzssCost(mzssCost);
	                 BigDecimal zyssNum =result.getBigDecimal(28);
	                 businessYzcxzhcx.setZyssNum(zyssNum);
	                 BigDecimal zyssCost =result.getBigDecimal(29);
	                 businessYzcxzhcx.setZyssCost(zyssCost);
	                 java.sql.Date operDate =result.getDate(30);
	                 businessYzcxzhcx.setOperDate(operDate);
	                 BigDecimal bedBz =result.getBigDecimal(31);
	                 businessYzcxzhcx.setBedBz(bedBz);
	                 BigDecimal sjryNum =result.getBigDecimal(32);
	                 businessYzcxzhcx.setSjryNum(sjryNum);
	                 BigDecimal ghsrCost =result.getBigDecimal(33);
	                 businessYzcxzhcx.setGhsrCost(ghsrCost);
	                 BigDecimal tshNum =result.getBigDecimal(34);
	                 businessYzcxzhcx.setTshNum(tshNum);
	                 BigDecimal ghhjRs =result.getBigDecimal(35);
	                 businessYzcxzhcx.setGhhjRs(ghhjRs);
	                 String yq =result.getString(36);
	                 businessYzcxzhcx.setYq(yq);
	                 BigDecimal ghhjNum =result.getBigDecimal(37);
	                 businessYzcxzhcx.setGhhjNum(ghhjNum);
	                 BigDecimal ssts =result.getBigDecimal(38);
	                 businessYzcxzhcx.setSsts(ssts);
	                 hospitaldayinnerDao.save(businessYzcxzhcx);
	        	 }else{
	        		 //更新
	                 BigDecimal pzNum =result.getBigDecimal(1);
	                 businessYzcxzhcx2.setPzNum(pzNum);
	                 BigDecimal jymzNum =result.getBigDecimal(2);
	                 businessYzcxzhcx2.setJymzNum(jymzNum); 
	                 BigDecimal qtptNum =result.getBigDecimal(3);
	                 businessYzcxzhcx2.setQtptNum(qtptNum);
	                 BigDecimal zmzjNum =result.getBigDecimal(4);
	                 businessYzcxzhcx2.setZmzjNum(zmzjNum);
	                 BigDecimal jsNum =result.getBigDecimal(5);
	                 businessYzcxzhcx2.setJsNum(jsNum);
	                 BigDecimal fjsNum =result.getBigDecimal(6);
	                 businessYzcxzhcx2.setFjsNum(fjsNum);
	                 BigDecimal jzNum =result.getBigDecimal(7);
	                 businessYzcxzhcx2.setJzNum(jzNum);
	                 BigDecimal sfhjNum =result.getBigDecimal(8);
	                 businessYzcxzhcx2.setSfhjNum(sfhjNum);
	                 BigDecimal sfzfNum =result.getBigDecimal(9);
	                 businessYzcxzhcx2.setSfzfNum(sfzfNum);
	                 BigDecimal cfhjNum =result.getBigDecimal(10);
	                 businessYzcxzhcx2.setCfhjNum(cfhjNum);
	                 BigDecimal cfzfNum =result.getBigDecimal(11);
	                 businessYzcxzhcx2.setCfzfNum(cfzfNum);
	                 BigDecimal zdcfCost =result.getBigDecimal(12);
	                 businessYzcxzhcx2.setZdcfCost(zdcfCost);
	                 BigDecimal zxcfCost =result.getBigDecimal(13);
	                 businessYzcxzhcx2.setZxcfCost(zxcfCost);
	                 BigDecimal cfhjCost =result.getBigDecimal(14);
	                 businessYzcxzhcx2.setCfhjCost(cfhjCost);
	                 BigDecimal ryNum =result.getBigDecimal(15);
	                 businessYzcxzhcx2.setRyNum(ryNum);
	                 BigDecimal zkNum =result.getBigDecimal(16);
	                 businessYzcxzhcx2.setZkNum(zkNum);
	                 BigDecimal cywjNum =result.getBigDecimal(17);
	                 businessYzcxzhcx2.setCywjNum(cywjNum);
	                 BigDecimal cyyjNum =result.getBigDecimal(18);
	                 businessYzcxzhcx2.setCyyjNum(cyyjNum);
	                 BigDecimal zyhjNum =result.getBigDecimal(19);
	                 businessYzcxzhcx2.setZyhjNum(zyhjNum);
	                 BigDecimal zyzfNum =result.getBigDecimal(20);
	                 businessYzcxzhcx2.setZyzfNum(zyzfNum);
	                 BigDecimal zynhNum =result.getBigDecimal(21);
	                 businessYzcxzhcx2.setZynhNum(zynhNum);
	                 BigDecimal mzypCost =result.getBigDecimal(22);
	                 businessYzcxzhcx2.setMzypCost(mzypCost);
	                 BigDecimal mzylCost =result.getBigDecimal(23);
	                 businessYzcxzhcx2.setMzylCost(mzylCost);
	                 BigDecimal zyypCost =result.getBigDecimal(24);
	                 businessYzcxzhcx2.setZyypCost(zyypCost);
	                 BigDecimal zyylCost =result.getBigDecimal(25);
	                 businessYzcxzhcx2.setZyylCost(zyylCost);
	                 BigDecimal mzssNum =result.getBigDecimal(26);
	                 businessYzcxzhcx2.setMzssNum(mzssNum);
	                 BigDecimal mzssCost =result.getBigDecimal(27);
	                 businessYzcxzhcx2.setMzssCost(mzssCost);
	                 BigDecimal zyssNum =result.getBigDecimal(28);
	                 businessYzcxzhcx2.setZyssNum(zyssNum);
	                 BigDecimal zyssCost =result.getBigDecimal(29);
	                 businessYzcxzhcx2.setZyssCost(zyssCost);
	                 java.sql.Date operDate =result.getDate(30);
	                 businessYzcxzhcx2.setOperDate(operDate);
	                 BigDecimal bedBz =result.getBigDecimal(31);
	                 businessYzcxzhcx2.setBedBz(bedBz);
	                 BigDecimal sjryNum =result.getBigDecimal(32);
	                 businessYzcxzhcx2.setSjryNum(sjryNum);
	                 BigDecimal ghsrCost =result.getBigDecimal(33);
	                 businessYzcxzhcx2.setGhsrCost(ghsrCost);
	                 BigDecimal tshNum =result.getBigDecimal(34);
	                 businessYzcxzhcx2.setTshNum(tshNum);
	                 BigDecimal ghhjRs =result.getBigDecimal(35);
	                 businessYzcxzhcx2.setGhhjRs(ghhjRs);
	                 String yq =result.getString(36);
	                 businessYzcxzhcx2.setYq(yq);
	                 BigDecimal ghhjNum =result.getBigDecimal(37);
	                 businessYzcxzhcx2.setGhhjNum(ghhjNum);
	                 BigDecimal ssts =result.getBigDecimal(38);
	                 businessYzcxzhcx2.setSsts(ssts);
	                 hospitaldayinnerDao.update(businessYzcxzhcx2);
	        	 }
	         }
	         logger.info("保存数据到28");
	         this.init_DRJYSJinner(date, null, null);
	         result.close();//数据库先开后关
	         stmt.close();
	         conn.close();//关闭数据库
	         map.put("resCode", "info");
			 map.put("resMsg", "初始化成功");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "初始化失败");
			e.printStackTrace();
		}
		return map;
		
		
	}
	@Override
	public void init_DRJYSJinner(String startTime, String endTime, Integer type) {
		try {
			List<HospitaldayVo> list = hospitaldayinnerDao.queryListinner(startTime);
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
				MongoBasicDao mongoBasicDao = new MongoBasicDao();
				mongoBasicDao.insertDataByList(menuAlias+"_DAY", userList);
			}
			logger.info("保存数据到Mongo");
			Date beginDate=new Date();
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_DAY", list, startTime);
			//判断保存后的数据是否存在
			logger.info("开始判断保存后的数据是否存在");
			for (int i = 0; i < 10; i++) {
				List<BusinessYzcxzhcx> list2 = hospitaldayinnerDao.queryBusinessYzcxzhcx(startTime);
				if(list2.size()==0){
					try {
						logger.info("28数据库数据不存在");
						this.saveBusinessYzcxzhcx(startTime);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					logger.info("28数据库数据存在");
					BasicDBObject bdObject2 = new BasicDBObject();
			    	bdObject2.append("timeValue", startTime);
			    	MongoBasicDao mongoBasicDao = new MongoBasicDao();
					DBCursor cursorle = mongoBasicDao.findAlldata("DRJYSJ_DAY", bdObject2);
					DBObject dbCursorle;
					if(cursorle.hasNext()){
						logger.info("Mongo数据存在");
						break;
					}else{
						logger.info("Mongo数据不存在");
						this.init_DRJYSJinner(startTime, null, null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public List<BusinessYzcxzhcx> queryBusinessYzcxzhcxMaxdate() {
		return hospitaldayinnerDao.queryBusinessYzcxzhcxMaxdate();
	}
}
