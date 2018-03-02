package cn.honry.statistics.sys.empWorkStats.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.dao.RegisterInfoGzltjDao;
import cn.honry.statistics.sys.empWorkStats.dao.EmpWorkStatsDao;
import cn.honry.statistics.sys.empWorkStats.service.EmpWorkStatsService;
import cn.honry.statistics.sys.empWorkStats.vo.EmpWorkStatsVo;
import cn.honry.statistics.sys.stop.dao.OutpatientStopDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Service("empWorkStatsService")
@Transactional
@SuppressWarnings({ "all" })
public class EmpWorkStatsServiceImpl implements EmpWorkStatsService{
	private final String[] register={"T_REGISTER_MAIN_NOW","T_REGISTER_MAIN"};
	@Autowired
	@Qualifier(value = "empWorkStatsDAO")
	private EmpWorkStatsDao empWorkStatsDAO;
	
	@Autowired
	@Qualifier(value="outpatientStopDao")
	private OutpatientStopDao outpatientStopDao;
	
	@Autowired
	@Qualifier(value = "registerInfoGzltjDAO")
	private RegisterInfoGzltjDao registerInfoGzltjDAO;
	
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
//	/** 参数管理接口 **/
//	@Autowired
//	@Qualifier(value = "parameterInnerDAO")
//	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public RegisterInfo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(RegisterInfo arg0) {
	}

	@Override
	public List<SysDepartment> deptCombobox() {
		return empWorkStatsDAO.deptCombobox();
	}

	@Override
	public List<SysEmployee> empCombobox(String ids) {
		return empWorkStatsDAO.empCombobox(ids);
	}

	@Override
	public Map<String,Object> queryList(String beginTime, String endTime,String dept, String emp,String menuAlias,String rows,String page) {
		Map<String,Object> returnMap=new HashMap();
		if(StringUtils.isBlank(beginTime)||StringUtils.isBlank(endTime)){
			returnMap.put("total", 0);
			returnMap.put("rows", new ArrayList<EmpWorkStatsVo>());
			return returnMap;
		}
		List<String> tnL =wordLoadDocDao.returnInTables(beginTime, endTime, register, "MZ");
		if(StringUtils.isBlank(dept)||StringUtils.isBlank(emp)){
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
			if(deptList == null || deptList.size() == 0){
				emp = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
				dept = "";
			}else if("all".equals(dept)&&deptList.size()<900){//当查询全部科室，并且科室数量小于900时，使用in查询，当大于900时使用子查询
				dept = "";
				for (SysDepartment d : deptList) {
					if(StringUtils.isNotBlank(dept)){
						dept += ",";
					}
					dept += d.getDeptCode();
				}
			}
		}
	
		List<EmpWorkStatsVo> list = empWorkStatsDAO.queryListNow(tnL, beginTime, endTime, dept, emp,menuAlias,rows,page);
		Integer total= empWorkStatsDAO.getPage(tnL, beginTime, endTime, dept, emp, menuAlias);
		returnMap.put("total", total);
		returnMap.put("rows", list);
		return returnMap;
	}


}
