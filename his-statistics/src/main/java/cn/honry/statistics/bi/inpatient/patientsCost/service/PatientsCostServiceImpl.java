/**
 * 
 */
package cn.honry.statistics.bi.inpatient.patientsCost.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.patientsCost.dao.PatientsCostDao;
import cn.honry.statistics.bi.inpatient.patientsCost.vo.PatientCostVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;

/**
 * 在院病人费用分析Service实现类
 * @author luyanshou
 *
 */
@Service("patientsCostService")
@Transactional
@SuppressWarnings({ "all" })
public class PatientsCostServiceImpl implements PatientsCostService {

	@Autowired
	@Qualifier(value = "patientsCostDao")
	private PatientsCostDao patientsCostDao;
	
	/**
	 * 获取住院下的科室信息
	 * 
	 */
	public List<SysDepartment> getDeptInfo(){
		List<SysDepartment> list = patientsCostDao.getDept();
		return list;
	}
	
	/**
	 * 查询统计费用名称列表
	 * 
	 */
	public List<MinfeeStatCode> getFeeName(){
		List<MinfeeStatCode> list = patientsCostDao.getFeeName();
		return list;
	}
	
	/**
	 * 获取统计结果
	 * @param datevo 时间条件
	 * @param disease 疾病类别
	 * @param cost 费用类别
	 * @param dept 科室
	 * @param n 统计方式(1-按年统计;2-按季统计;3-按月统计;4-按日统计)
	 * @return
	 */
	public String getResults(DateVo datevo,String disease,String cost,String dept,int n){
		List<PatientCostVo> itemCost;
		List<PatientCostVo> medicineCost;
		boolean flag= false;//用于判断cost(费用类别)是否包含多个值
		if(StringUtils.isBlank(cost)|| "全部".equals(cost)){
			cost="全部";
			 itemCost = patientsCostDao.getItemCost(dept, "", datevo, n);//非药品费用统计	
			 medicineCost = patientsCostDao.getMedicineCost(dept, "", datevo, n);//药品费用统计
		}else{
			if(cost.contains(",")){
				flag=true;
			}
			itemCost = patientsCostDao.getItemCost(dept, cost, datevo, n);//非药品费用统计
			medicineCost = patientsCostDao.getMedicineCost(dept, cost, datevo, n);//药品费用统计
		}
		Map<String,PatientCostVo> map =new HashMap<>();//用于比较非药品费用统计和药品费用统计结果,如果科室和时间相同则合并
		List<String> list = new ArrayList<>();//存放json格式数据
		
		//构造map的key和value
		if(medicineCost!=null && medicineCost.size()>0){
			for (PatientCostVo pat : medicineCost) {
				pat.setItemCost(0d);
				String dept_time = pat.getInhos_deptcode()+"-"+pat.getTimeChose();
				if(!flag){
					pat.setCost(cost);
				}else{
					dept_time+="-"+pat.getCost();
				}
				map.put(dept_time, pat);
			}
		}
		
			//比较非药品费用统计和药品费用统计结果如果科室和时间相同则合并
			if(itemCost!=null && itemCost.size()>0){
				for (PatientCostVo pat : itemCost) {
					pat.setMedicineCost(0d);
					String dept_time = pat.getInhos_deptcode()+"-"+pat.getTimeChose();
					if(!flag){
						pat.setCost(cost);
					}else{
						dept_time+="-"+pat.getCost();
					}
					PatientCostVo vo = map.get(dept_time);
					if(vo!=null){
						Double medic= vo.getMedicineCost();//药品费用
						pat.setMedicineCost(medic);//设置药品费用
						pat.setTotCost(medic+pat.getItemCost());//设置总费用
						map.remove(dept_time);
					}
				}
			}
		
		
		if(map.size()>0){//如果map仍不为空,将map中的value值放到itemCost的list列表中
			for(Map.Entry<String, PatientCostVo> entry :map.entrySet()){
				itemCost.add(entry.getValue());
			}
		}
		String[] columns= new String[]{"medicineCost","itemCost","totCost"};
		Map<String,List<String>> m= new HashMap<>();
		List<String> deptList = new ArrayList<String>();
		List<String> costList = new ArrayList<String>();
		if(StringUtils.isNotBlank(dept)){
			if("0".equals(dept)){//dept为0即选择全部
				List<SysDepartment> depts = patientsCostDao.getDept();
				for (SysDepartment sys: depts) {
					deptList.add(sys.getId());
				}
			}else{
				String [] depts=dept.split(",");
				for(int i=0;i<depts.length;i++){
					deptList.add(depts[i]);
				}
				if(depts.length==1){
					deptList.add(dept);
				}
			}
			m.put("inhos_deptcode", deptList);
		}
		if(StringUtils.isNotBlank(cost)){
			if(!flag){
				costList.add(cost);
			}else{
				String[] split = cost.split(",");
				for (String str : split) {
					costList.add(str);
				}
			}
			m.put("cost", costList);
		}
		for (PatientCostVo pat : itemCost) {
			String json = JSONUtils.toJson(pat);//转换为json数据
			String newJson = ResultUtils.getnewJson(json, n, columns, pat.getTimeChose());//获取替换后的json数据
			list.add(newJson);
		}
		String result = ResultUtils.getResult(datevo, n, list, m, 3);
		return result;
	}
}
