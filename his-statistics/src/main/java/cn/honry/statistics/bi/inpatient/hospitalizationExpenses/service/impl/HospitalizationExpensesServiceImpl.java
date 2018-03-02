package cn.honry.statistics.bi.inpatient.hospitalizationExpenses.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.hospitalizationExpenses.dao.HospitalizationExpensesDao;
import cn.honry.statistics.bi.inpatient.hospitalizationExpenses.service.HospitalizationExpensesService;
import cn.honry.statistics.bi.inpatient.hospitalizationExpenses.vo.HospitalizationExpensesVo;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.vo.HospitalizationInformationVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
@Service("hospitalizationExpensesService")
@Transactional
@SuppressWarnings({ "all" })
public class HospitalizationExpensesServiceImpl implements HospitalizationExpensesService {
	@Autowired
	@Qualifier(value = "hospitalizationExpensesDao")
	private HospitalizationExpensesDao hospitalizationExpensesDao;

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
	public String querytDatagrid(DateVo datevo,String[] dimStringArray,int dateType,String dimensionValue) {
		//组织参数list：list中的元素为map
				List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
				list=ResultUtils.prepareParamList(dimensionValue);
						
				//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
				Map<String,List<String>> map=new HashMap<String, List<String>>();
				map=ResultUtils.prepareParamMap(dimensionValue);
				
				//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
				String [] voArray=new String[]{"totCost","drugs", "drugsPro" ,"noDrugs" ,"noDrugsPro", "passengers", "passengersAvg"};
				//将维度种类拆分放入到数组中
				List<HospitalizationExpensesVo> volist=hospitalizationExpensesDao.querytDatagrid(dimStringArray, list, dateType,datevo);
				List<String> listJson=new ArrayList<String>();
				for(int i=0;i<volist.size();i++){
					//查询出来的结果集的每一个对象转换为json
					String json=JSONUtils.toJson(volist.get(i));
					json=json.replace("deptname", "dept_code");
					String json1=ResultUtils.getnewJson(json, dateType, voArray, volist.get(i).getFeeDate());
					//将json字符串添加到listJson中
					listJson.add(json1);
				}
				//获得最终的json字符串
				String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
				return result;
	}

}
