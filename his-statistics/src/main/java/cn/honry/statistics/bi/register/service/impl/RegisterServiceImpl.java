package cn.honry.statistics.bi.register.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.BiRegisterGrade;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.register.dao.RegisterDao;
import cn.honry.statistics.bi.register.service.RegisterService;
import cn.honry.statistics.bi.register.vo.RegisterVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;

@Service("registerService")
@Transactional
@SuppressWarnings({"all"})
public class RegisterServiceImpl implements RegisterService{

	@Autowired
	@Qualifier(value = "registerDao")
	private RegisterDao registerDao;
	
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
	public List<SysDepartment> queryAllDept() {
		
		return registerDao.queryAllDept();
	}

	@Override
	public List<BiRegisterGrade> queryAllGrade() {
		return registerDao.queryAllGrade();
	}


	@Override
	public String queryStatDate(String timeString, String nameString) {
		timeString ="2016,2015,07";
		String[] timeArr=timeString.split(",");
		List<RegisterVo> info=new ArrayList<RegisterVo>();
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> jsonMap=new HashMap<String,Object>();
		
			info=registerDao.queryStatDate(timeArr[0],"0",nameString);
		
		String[] categories=new String[info.size()];
		Integer[] values=new Integer[info.size()];
		Integer[] tb=new Integer[info.size()];
		Integer[] hb=new Integer[info.size()];
		int t=0;
		for(RegisterVo temp:info){
			categories[t]=temp.getDeptName();
			values[t]=temp.getRegisterPerson();
			t++;
		}
		map.put("categories", categories);
		map.put("values", values);
		
			//查询同比
			info=registerDao.queryStatDate(timeArr[1], "1", nameString);
			int a=0;
			for(RegisterVo temp:info){
				tb[a]=temp.getRegisterPerson();
				t++;
			}
			map.put("old", tb);
			//查询环比
			info=registerDao.queryStatDate(timeArr[2], "2", nameString);
			int q=0;
			for(RegisterVo temp:info){
				hb[q]=temp.getRegisterPerson();
				q++;
			}
			map.put("mom", hb);
		String json=JSONUtils.toJson(map);
			json=json.replace("null", "0");
			
		return json;
	}
	/**
	 * @param yearStart
	 * @param yearEnd
	 * @param quarterStart
	 * @param quarterEnd
	 * @param monthStart
	 * @param monthEnd
	 * @param dayStart
	 * @param dayEnd
	 * @param dimensionString
	 * @param dimensionOne
	 * @param dimensionTwo
	 * @param dimensionThree
	 * @return
	 */
	@Override
	public String queryregisterid(DateVo datevo, String[] dimStringArray, int dateType,
			String dimensionValue) {
		//组织参数list：list中的元素为map
		List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
		list=ResultUtils.prepareParamList(dimensionValue);
		
		//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
		Map<String,List<String>> map=new HashMap<String, List<String>>();
		map=ResultUtils.prepareParamMap(dimensionValue);
		
		//组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
		String [] voArray=new String[]{"registerPerson","fee"};
		//将维度种类拆分放入到数组中
		List<RegisterVo> volist=registerDao.queryregisterid(dimStringArray, list, dateType,datevo);
		List<String> listJson=new ArrayList<String>();
		for(int i=0;i<volist.size();i++){
			//查询出来的结果集的每一个对象转换为json
			String json=JSONUtils.toJson(volist.get(i));
			json=json.replace("deptName", "dept_code");
			json=json.replace("deptType", "reglevl_code");
			String json1=ResultUtils.getnewJson(json, dateType, voArray, volist.get(i).getTimeChose());
			//将json字符串添加到listJson中
			listJson.add(json1);
		}
		//获得最终的json字符串
		String result=ResultUtils.getResult(datevo,dateType,listJson,map,voArray.length);
		return result;
	}

	public List<BiBaseOrganization> queryDeptForBiPublic() {
		return registerDao.queryDeptForBiPublic();
	}

	@Override
	public List<BiBaseOrganization> queryreglevlForBiPublic() {
		return registerDao.queryreglevlForBiPublic();
	}

}




