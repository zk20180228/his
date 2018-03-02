package cn.honry.statistics.bi.outpatient.optRecipedetail.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.BiRegisterGrade;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.outpatient.optRecipedetail.dao.OptRecipedetailDao;
import cn.honry.statistics.bi.outpatient.optRecipedetail.service.OptRecipedetailService;
import cn.honry.statistics.bi.outpatient.optRecipedetail.vo.OptRecipedetailVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.dao.OutpatientWorkloadDao;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;

@Service("optRecipedetailService")
@Transactional
@SuppressWarnings({ "all" })
public class OptRecipedetailServiceImpl implements OptRecipedetailService {
	@Autowired
	@Qualifier(value = "optRecipedetailDao")
	private OptRecipedetailDao OptRecipedetailDao;
	private String timechose = "\"timeChose\"";

	@Override
	public BiRegister get(String arg0) {
		return null;
	}

	public OptRecipedetailDao getOptRecipedetailDao() {
		return OptRecipedetailDao;
	}

	public void setOptRecipedetailDao(OptRecipedetailDao optRecipedetailDao) {
		OptRecipedetailDao = optRecipedetailDao;
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(BiRegister arg0) {

	}

	@Override
	public List<SysDepartment> queryAllDept() {
		return OptRecipedetailDao.queryAllDept();
	}

	@Override
	public String querytOptRecipeDatagrid(DateVo datevo,
			String[] dimStringArray, int dateType, String dimensionValue) {

		// 组织参数list：list中的元素为map
		List<Map<String, List<String>>> list = new ArrayList<Map<String, List<String>>>();
		list = ResultUtils.prepareParamList(dimensionValue);

		// 组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map = ResultUtils.prepareParamMap(dimensionValue);

		// 组织参数：将Vo类中的维度字段按照页面的显示顺序排列好放入数组中
		String[] voArray = new String[] { "qty", "sprice", "an", "mom" };
		// 将维度种类拆分放入到数组中
		List<OptRecipedetailVo> volist = OptRecipedetailDao
				.querytOptRecipedetailDatagrid(dimStringArray, list, dateType,
						datevo);
		;
		List<String> listJson = new ArrayList<String>();
		Map<String, String> map1 = new HashMap();
		StringBuffer jsonfinal = new StringBuffer("[");
		for (int i = 0; i < volist.size(); i++) {
			// 查询出来的结果集的每一个对象转换为json

			String json = JSONUtils.toJson(volist.get(i));
			json = json.replace("deptDimensionality", "reg_dept_code");
			json = json.replace("doctorDimensionality", "doct_code");
			String json1 = ResultUtils.getnewJson(json, dateType, voArray,
					volist.get(i).getTimeChose());
			if (volist.get(i).getDoctorDimensionality() == null) {
				if (ifkey(map1, volist.get(i).getDeptDimensionality())) {

					String docfinal = map1.get(
							volist.get(i).getDeptDimensionality()).substring(
							0,
							map1.get(volist.get(i).getDeptDimensionality())
									.length() - 1)
							+ ","
							+ json1.substring(json1.indexOf("\"timeChose\""),
									json1.length());

					map1.put(volist.get(i).getDeptDimensionality(), docfinal);

				} else {

					map1.put(volist.get(i).getDeptDimensionality(), json1);
				}

			} else {
				if (ifkey(map1, volist.get(i).getDoctorDimensionality())) {

					String docfinal = map1.get(
							volist.get(i).getDoctorDimensionality()).substring(
							0,
							map1.get(volist.get(i).getDoctorDimensionality())
									.length() - 1)
							+ ","
							+ json1.substring(json1.indexOf("\"timeChose\""),
									json1.length());

					map1.put(volist.get(i).getDoctorDimensionality(), docfinal);

				} else {

					map1.put(volist.get(i).getDoctorDimensionality(), json1);
				}
			}

		}

		for (String v : map1.values()) {

			jsonfinal.append(v).append(",");
		}

		String jsonfinal1 = jsonfinal.substring(0, jsonfinal.length() - 1)
				+ "]";
		// System.out.println(jsonfinal1);
		// 获得最终的json字符串
		// String result = ResultUtils.getResult(datevo, dateType, listJson,
		// map,
		// voArray.length);
		return jsonfinal1;

	}

	public boolean ifkey(Map<String, String> map, String string) {

		for (String key : map.keySet()) {
			if (string.equals(key))
				return true;
		}

		return false;
	}

	@Override
	public List<BiBaseOrganization> queryDeptForBiPublic(String deptType) {
		return OptRecipedetailDao.queryDeptForBiPublic(deptType);

	}

}
