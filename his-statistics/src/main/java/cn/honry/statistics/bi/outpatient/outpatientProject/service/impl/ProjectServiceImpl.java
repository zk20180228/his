package cn.honry.statistics.bi.outpatient.outpatientProject.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BiBaseDictionary;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.bi.outpatient.outpatientProject.dao.ProjectDAO;
import cn.honry.statistics.bi.outpatient.outpatientProject.service.ProjectService;
import cn.honry.statistics.bi.outpatient.outpatientTotCost.vo.TotCostDimension;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;

@Service("projectService")
@Transactional
@SuppressWarnings({"all"})
public class ProjectServiceImpl implements ProjectService{
	
	@Autowired
	@Qualifier(value="projectDAO")
	private ProjectDAO projectDAO;

	@Override
	public List<DimensionVO> findDimensionList(DimensionVO dimensionVO) {
		return projectDAO.findDimensionList(dimensionVO);
	}

	@Override
	public String queryProjectoadDatagrid(DateVo datevo,String dimensionString, int dateType, String dimensionValue) {
		//组织参数list：list中的元素为map
				List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
				list=ResultUtils.prepareParamList(dimensionValue);
				
				//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
				Map<String,List<String>> map=new HashMap<String, List<String>>();
				map=ResultUtils.prepareParamMap(dimensionValue);
				
				//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
				String [] voArray=new String[]{"sumCost","avgtotcost"};
				//将维度种类拆分放入到数组中
				String [] diArrayKey=dimensionString.split(",");
				List<TotCostDimension> volist=projectDAO.queryProjectoadDatagrid(diArrayKey, list, dateType,datevo);
				List<String> listJson=new ArrayList<String>();
				for(int i=0;i<volist.size();i++){
					//查询出来的结果集的每一个对象转换为json
					String json=JSONUtils.toJson(volist.get(i));
					json=json.replace("dept", "reg_dpcd");
					json=json.replace("codeName", "class_code");
					json=json.replace("emp", "doct_code");
					String json1=ResultUtils.getnewJson(json, dateType, voArray, volist.get(i).getYears());
					//将json字符串添加到listJson中
					listJson.add(json1);
				}
				//获得最终的json字符串
				String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
				return result;
	}

	@Override
	public List<BiBaseDictionary> getDictionary(String type) {
		return projectDAO.getDictionary(type);
	}
	
}
