package cn.honry.statistics.bi.statisticalSetting.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BiDimensionSet;
import cn.honry.base.bean.model.BiIndexSet;
import cn.honry.base.bean.model.BiStatSet;
import cn.honry.base.bean.model.BiSubsectionSet;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.statistics.bi.statisticalSetting.dao.BiIndexSetDAO;
import cn.honry.statistics.bi.statisticalSetting.dao.BiSubsectionSetDAO;
import cn.honry.statistics.bi.statisticalSetting.dao.StatisticalSettingDAO;
import cn.honry.statistics.bi.statisticalSetting.service.StatisticalSettingService;
import cn.honry.statistics.bi.statisticalSetting.vo.VoshowList;
import cn.honry.statistics.bi.statisticalSetting.vo.VtableName;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.reflect.TypeToken;
@Service("statisticalSettingService")
@Transactional
@SuppressWarnings({ "all" })
public class StatisticalSettingServiceImpl implements StatisticalSettingService{
	
	@Autowired
	@Qualifier(value = "statisticalSettingDAO")
	private StatisticalSettingDAO statisticalSettingDAO;
	@Autowired
	@Qualifier(value = "biIndexSetDAO")
	private BiIndexSetDAO biIndexSetDAO;
	@Autowired
	@Qualifier(value = "biSubsectionSetDAO")
	private BiSubsectionSetDAO biSubsectionSetDAO;

	@Override
	public BiStatSet get(String arg0) {
		return statisticalSettingDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		statisticalSettingDAO.del(arg0, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(arg0, "统计图表信息", "UPDATE","T_STAT_SET", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void saveOrUpdate(BiStatSet BiStatSet) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		if(StringUtils.isBlank(BiStatSet.getId())){
			BiStatSet.setId(null);
			BiStatSet.setCreateUser(user.getAccount());
			BiStatSet.setCreateTime(new Date());
			if(dept==null){
			}else{
				BiStatSet.setCreateDept(dept.getDeptCode());
			}
			statisticalSettingDAO.save(BiStatSet);
			OperationUtils.getInstance().conserve(null, "统计图表信息","INSERT_INTO","T_STAT_SET",OperationUtils.LOGACTIONINSERT);
			
			String dimensionNumber = BiStatSet.getDimensionNumber();
			String stringarray[]=dimensionNumber.split(",");
			List<String> list = new ArrayList<String>();
			for(int i=0;i<stringarray.length;i++){
				if(stringarray[i].indexOf("_")==2){
					String s = "";
					for(int j=0;j<stringarray.length;j++){
						if(j<i&&stringarray[j].indexOf("_")==-1){
							s += stringarray[j];
						}
					}
					s+=stringarray[i];
					list.add(s);
				} 
			}
			for (int i = 0; i < list.size(); i++) {
				String dimensionNumber1 = list.get(i).replaceAll("_", "");
				List<BiIndexSet> indexlist = statisticalSettingDAO.quertIndexFiled(dimensionNumber, BiStatSet.getSetGroupid());
				String indexField = "";
				String subsectionField = "";
				List<String> listl = new ArrayList<String>();
				for (BiIndexSet biIndexSet : indexlist) {
					indexField+=biIndexSet.getIndexField().substring(0, 1);
					listl.add(biIndexSet.getIndexField());
				}
				List<BiSubsectionSet> subsectionlist = statisticalSettingDAO.quertSubsectionFiled(dimensionNumber, BiStatSet.getSetGroupid());
				for (BiSubsectionSet biSubsectionSet : subsectionlist) {
					subsectionField+=biSubsectionSet.getSubsectionField().substring(0, 1);
					listl.add(biSubsectionSet.getSubsectionField());
				}
				String field = indexField+subsectionField;
				
				//根据维度动态的创建表  判断横向维度的多少
				statisticalSettingDAO.createTable(field,listl,indexlist.get(0).getPolymerization());
				//插入数据
				
			}
		}else{
			BiStatSet.setCreateUser(user.getAccount());
			BiStatSet.setCreateTime(new Date());
			if(dept==null){
			}else{
				BiStatSet.setCreateDept(dept.getDeptCode());
			}
			BiStatSet.setUpdateTime(new Date());
			BiStatSet.setUpdateUser(user.getAccount());
			statisticalSettingDAO.save(BiStatSet);
			OperationUtils.getInstance().conserve(BiStatSet.getId(), "统计图表信息","UPDATE", "T_STAT_SET",OperationUtils.LOGACTIONUPDATE);
		}
	}
	
	@Override
	public List<BiStatSet> queryBiStatSetList(BiStatSet BiStatSet, String page,
			String rows) {
		return statisticalSettingDAO.queryBiStatSetList(BiStatSet, page, rows);
	}

	@Override
	public int queryBiStatSetTotal(BiStatSet BiStatSet) {
		return statisticalSettingDAO.queryBiStatSetTotal(BiStatSet);
	}

	@Override
	public List<VtableName> querytablename() {
		return statisticalSettingDAO.querytablename();
	}

	@Override
	public List<VtableName> queryColumnname(String tableName) {
		return statisticalSettingDAO.queryColumnname(tableName);
	}
 
	@Override
	public List<BiDimensionSet> queryBiDimensionSet(String dimensionNumber) {
		return statisticalSettingDAO.queryBiDimensionSet(dimensionNumber);
	}

	@Override
	public void saveIndexOrSubsection(String biSubsectionSetJson,
			String biIndexSetJson, String dimensionNumber) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		//分段Json转成list
		List<BiSubsectionSet> biSubsectionSetList = null;
		try {
			biSubsectionSetJson=biSubsectionSetJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			biSubsectionSetList =JSONUtils.fromJson(biSubsectionSetJson,  new TypeToken<List<BiSubsectionSet>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//指标Json转成list
		List<BiIndexSet> biIndexSetList = null;
		try {
			biIndexSetJson=biIndexSetJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			biIndexSetList =JSONUtils.fromJson(biIndexSetJson,  new TypeToken<List<BiIndexSet>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (BiSubsectionSet entity : biSubsectionSetList){
			if (entity != null) {
				entity.setId(null);
				entity.setDimensionNumber(dimensionNumber);
				List<BiSubsectionSet> list = statisticalSettingDAO.queryBiSubsectionSet(dimensionNumber);
				if(list==null || list.size()==0){
					entity.setSort(1);
				}else{
					entity.setSort(Integer.valueOf(list.get(0).getDimensionNumber()+1));
				}
				entity.setCreateUser(user.getAccount());
				if(dept==null){
				}else{
					entity.setCreateDept(dept.getDeptCode());
				}
				entity.setCreateTime(new Date());
				OperationUtils.getInstance().conserve(null, "统计分段设置表","INSERT_INTO", "BI_SUBSECTION_SET",OperationUtils.LOGACTIONINSERT);
				biSubsectionSetDAO.save(entity);
			}
		}
		for (BiIndexSet entity : biIndexSetList){
			if (entity != null) {
				entity.setId(null);
				entity.setDimensionNumber(dimensionNumber);
				List<BiIndexSet> list = statisticalSettingDAO.queryBiIndexSet(dimensionNumber);
				if(list==null || list.size()==0){
					entity.setSort(1);
				}else{
					entity.setSort(Integer.valueOf(list.get(0).getDimensionNumber()+1));
				}
				entity.setCreateUser(user.getAccount());
				if(dept==null){
				}else{
					entity.setCreateDept(dept.getDeptCode());
				}
				entity.setCreateTime(new Date());
				OperationUtils.getInstance().conserve(null, "统计指标设置表","INSERT_INTO", "BI_INDEX_SET",OperationUtils.LOGACTIONINSERT);
				biIndexSetDAO.save(entity);
			}
		}
	}

	@Override
	public List<VtableName> queryViewColumnName(String viewName) {
		return statisticalSettingDAO.queryViewColumnName(viewName);
	}
	
	@Override
	public List<Object> queryObject() {
		return statisticalSettingDAO.queryObject();
	}

	@Override
	public List<VoshowList> queryListShowList() {
		return statisticalSettingDAO.queryListShowList();
	}

}
