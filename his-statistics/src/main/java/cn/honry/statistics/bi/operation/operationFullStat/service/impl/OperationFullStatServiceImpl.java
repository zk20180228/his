package cn.honry.statistics.bi.operation.operationFullStat.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.bi.operation.operationFullStat.dao.OperationFullStatDao;
import cn.honry.statistics.bi.operation.operationFullStat.service.OperationFullStatService;
import cn.honry.statistics.bi.operation.operationFullStat.vo.OperationFullStatVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.dao.OutpatientWorkloadDao;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;

@Service("operationFullStatService")
@Transactional
@SuppressWarnings({ "all" })
public class OperationFullStatServiceImpl implements OperationFullStatService{
	@Autowired
	@Qualifier(value = "operationFullStatDao")
	private OperationFullStatDao operationFullStatDao;

	@Override
	public OperationFullStatVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OperationFullStatVo arg0) {
		
	}

	@Override
	public String querytWordloadDatagrid(DateVo datevo, String[] dimStringArray, int dateType, String dimensionValue) {
		//组织参数list：list中的元素为map
				List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
				list=ResultUtils.prepareParamList(dimensionValue);
						
				//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
				Map<String,List<String>> map=new HashMap<String, List<String>>();
				map=ResultUtils.prepareParamMap(dimensionValue);
				
				//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
				String [] voArray=new String[]{"opNum","scale"};
				
				//将维度种类拆分放入到数组中
				List<OperationFullStatVo> volist=operationFullStatDao.queryOperationFullStatVo(dimStringArray, list, dateType,datevo);
				
				List<String> listJson=new ArrayList<String>();
				for(int i=0;i<volist.size();i++){
					//查询出来的结果集的每一个对象转换为json
					String json=JSONUtils.toJson(volist.get(i));
					json=json.replace("deptCode", "exec_dept");
					json=json.replace("opsDocd", "ops_docd");
					json=json.replace("itemName", "operation_id");
					json=json.replace("opsKind", "ops_kind");
					String json1=ResultUtils.getnewJson(json, dateType, voArray, volist.get(i).getTimeChose());
					//将json字符串添加到listJson中
					listJson.add(json1);
				}
				//获得最终的json字符串
				String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
				return result;
	}
}
