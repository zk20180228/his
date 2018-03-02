package cn.honry.statistics.bi.operation.operatioNum.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.statistics.bi.operation.operatioNum.dao.OperationNumDao;
import cn.honry.statistics.bi.operation.operatioNum.service.OperationNumService;
import cn.honry.statistics.bi.operation.operatioNum.vo.OperationNumVo;
import cn.honry.statistics.finance.operationCost.dao.OperationCostDao;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;

@Service("operationNumService")
@Transactional
@SuppressWarnings({ "all" })
public class OperationNumServiceImpl implements OperationNumService{
	
	/** 门诊收费类型 **/
	@Autowired
	@Qualifier(value = "operationNumDao")
	private OperationNumDao operationNumDao;
	@Autowired
	@Qualifier(value = "innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	@Autowired
	@Qualifier(value = "operationCostDao")
	private OperationCostDao operationCostDao;
	@Override
	public OperationNumVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OperationNumVo arg0) {
		
	}

	@Override
	public String queryOperationNum(DateVo datevo, String[] dimStringArray,int dateType,String dimensionValue) {
		//组织参数list：list中的元素为map
		List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
		list=ResultUtils.prepareParamList(dimensionValue);
				
		//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
		Map<String,List<String>> map=new HashMap<String, List<String>>();
		map=ResultUtils.prepareParamMap(dimensionValue);
		
		
		
		//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
		String [] voArray=new String[]{"opNum"};
		//将维度种类拆分放入到数组中
		List<OperationNumVo> volist=operationNumDao.queryOperationNum(dimStringArray, list, dateType,datevo);
		List<String> listJson=new ArrayList<String>();
		for(int i=0;i<volist.size();i++){
			//查询出来的结果集的每一个对象转换为json
			String json=JSONUtils.toJson(volist.get(i));
			json=json.replace("opExecdept", "exec_dept");
			json=json.replace("opKind", "ops_kind");
			String json1=ResultUtils.getnewJson(json, dateType, voArray, volist.get(i).getTimeChose());
			//将json字符串添加到listJson中
			listJson.add(json1);
		}
		//获得最终的json字符串
		String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
		return result;
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
	public Map<String, String> operationTypeMap() {
		HashMap<String , String> degreeMap=new HashMap<String ,String>();
		List<BusinessDictionary> depList=innerCodeDao.getDictionary("operatetype");
		for (BusinessDictionary s : depList) {
			degreeMap.put(s.getEncode(), s.getName());
		}
		return degreeMap;
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

}
