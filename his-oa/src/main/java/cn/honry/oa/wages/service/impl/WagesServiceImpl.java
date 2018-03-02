package cn.honry.oa.wages.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.bean.model.DrugSpedrug;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.Hospital;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientMedicalrecord;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.bean.model.TecTerminalApply;
import cn.honry.base.bean.model.User;
import cn.honry.base.bean.model.OaWages;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.frequency.dao.FrequencyInInterDAO;
import cn.honry.inner.baseinfo.hospital.dao.HospitalInInterDAO;
import cn.honry.inner.baseinfo.stack.dao.StackInInterDAO;
import cn.honry.inner.baseinfo.stackInfo.dao.StackinfoInInterDAO;
import cn.honry.inner.drug.drugSpedrug.dao.SpedrugInInterDAO;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.drug.undrugZtinfo.dao.UndrugZtinfoInInterDAO;
import cn.honry.inner.patient.idcard.dao.IdcardInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.user.dao.UserInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.technical.terminalApply.dao.TerminalApplyInInterDAO;
import cn.honry.inner.vo.ComboGroupVo;
import cn.honry.inner.vo.StatVo;
import cn.honry.oa.wages.dao.WagesDAO;
import cn.honry.oa.wages.service.WagesService;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/** 
* @Description: 工资管理ServiceImpl
* @author zx
* @date 2017年7月17日
*  
*/
@Service("wagesService")
@Transactional
@SuppressWarnings({ "all" })
public class WagesServiceImpl implements WagesService{
	@Autowired
	@Qualifier(value = "wagesDAO")
	private WagesDAO wagesDAO;//工资管理
	/** 
	* @Description: 工资账号是否存在
	* @param weagesAccount 工资账号
	* @throws Exception
	* @return List<SysEmployee>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public List<SysEmployee> isExistWadges(String weagesAccount) throws Exception{
		return wagesDAO.isExistWadges(weagesAccount);
	}
	/** 
	* @Description: 修改工资查询密码 
	* @param weagesAccount 工资账号
	* @param password 密码
	* @throws Exception
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public String  initPasswordToWeages(String weagesAccount, String password) throws Exception{
		return wagesDAO.initPasswordToWeages(weagesAccount,password);
		
	}
	/** 
	* @Description: 查询原密码 
	* @param weagesAccount 查询账号
	* @param weagesPassword 密码
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public SysEmployee checkAccount(String weagesAccount, String weagesPassword) throws Exception{
		return wagesDAO.checkAccount(weagesAccount,weagesPassword);
	}
	/** 
	* @Description: 查询工资数据
	* @param wageAccount 工资账号
	* @param name 身份证号
	* @param wagesTime 查询时间
	* @param page
	* @param rows
	* @throws Exception
	* @return List<OaWages>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public Map<String, Object> listWagesQuery(String wageAccount, String name, String wagesTime, String page, String rows) throws Exception{
		return wagesDAO.listWagesQuery(wageAccount,name,wagesTime,page,rows);
	}
	/** 
	* @Description: 总条数 
	* @param wageAccount 工资账号
	* @param name 身份证号
	* @param wagesTime 查询时间
	* @throws Exception
	* @return int    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public int getTotal(String wageAccount, String name, String wagesTime) throws Exception{
		return wagesDAO.getTotal(wageAccount,name,wagesTime);
	}
	/** 
	* @Description: 批量插入数据
	* @param oaWagesList 工资数据集合
	* @throws HibernateException
	* @throws SQLException
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public void insertWagesByBatch(List<OaWages> oaWagesList) throws HibernateException, SQLException {
		wagesDAO.saveBatch(oaWagesList);
	}
	
	/** 
	* @Description: 导出
	* @param list 工资数据集合
	* @param fUtil 文件
	* @throws Exception
	* @return FileUtil    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public FileUtil export(List<OaWages> list, FileUtil fUtil) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		for (OaWages model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getWagesAccount()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getName()) + ",";
			record += dateFormat.format(model.getWagesTime()) + ",";
			record = CommonStringUtils.trimToEmpty(model.getDeptName()) + ",";
			record = CommonStringUtils.trimToEmpty(model.getCategory()) + ",";
			record += model.getPostPay()+ ",";
			record += model.getBasePay()+ ",";
			record += model.getNursinTeach()+ ",";
			record += model.getAchievements()+ ",";
			record += model.getNursinTeaching()+ ",";
			record += model.getKeepThink()+ ",";
			record += model.getHealthAllowance()+ ",";
			record += model.getOnlyChildFee()+ ",";
			record += model.getHygieneFee()+ ",";
			record += model.getPHDFee()+ ",";
			record += model.getSubsidyFee()+ ",";
			record += model.getIncreased()+ ",";
			record += model.getIncreasing()+ ",";
			record += model.getTotalShould()+ ",";
			record += model.getDeductRent()+ ",";
			record += model.getHousingFund()+ ",";
			record += model.getBoardingFee()+ ",";
			record += model.getMedicalInsurance()+ ",";
			record += model.getOverallPlanning()+ ",";
			record += model.getUnemploymentInsurance()+ ",";
			record += model.getDeductWages()+ ",";
			record += model.getHeatingCosts()+ ",";
			record += model.getAccountEeceivable()+ ",";
			record += model.getTotalActual()+ ",";
			record += CommonStringUtils.trimToEmpty(model.getProvidentFundAccount()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getIDCard()) + ",";
			fUtil.write(record);

		}
		return fUtil;
	}
	
	/** 
	* @Description: 查询所有的数据用于导出 
	* @param wagesAccount 工资账号
	* @param wagesName 身份账号
	* @param wagesTime 工资月份
	* @throws Exception
	* @return List<OaWages>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public List<OaWages> listWagesQueryForExport(String wagesAccount, String wagesName, String wagesTime) throws Exception{
		return wagesDAO.listWagesQueryForExport(wagesAccount,wagesName,wagesTime);
	}
	/** 
	* @Description: 加载工资管理树 
	* @throws Exception
	* @return List<TreeJson>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public List<TreeJson> queryColumns() throws Exception{
		SysRole rolse = (SysRole) SessionUtils.getCurrentUserLoginRoleFromShiroSession();
		SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String alias ="";
		String deptCode="";
		if(rolse!=null&&StringUtils.isNotBlank(rolse.getAlias())){
			alias = rolse.getAlias();
		}
		if(dept!=null && StringUtils.isNotBlank(dept.getDeptCode())){
			deptCode = dept.getDeptCode();
		}
		List<TreeJson> list =new ArrayList<TreeJson>();
		TreeJson treeJson = new TreeJson();
		List<TreeJson> clist=new ArrayList<TreeJson>();
		treeJson.setId("root");
		treeJson.setText("工资信息管理");
		treeJson.setState("close");
		Map<String,String> cmap=new HashMap<String,String>();
		cmap.put("pid","root");
		if("superManager".equals(alias)||"6003".equals(deptCode)){
			List<TreeJson> tlist=new ArrayList<TreeJson>();
			TreeJson ctreeJson =new TreeJson();
			ctreeJson.setId("personnel");
			ctreeJson.setText("人事处");
			ctreeJson.setState("open");
			ctreeJson.setAttributes(cmap);
			clist.add(ctreeJson);
		}
		TreeJson rtreeJson =new TreeJson();
		rtreeJson.setId("empolyee");
		rtreeJson.setText("员工自助查询");
		rtreeJson.setState("open");		
		clist.add(rtreeJson);
		treeJson.setChildren(clist);
		list.add(treeJson);
		return list;
	}
	/** 
	* @Description: 验证账户密码
	* @param wagesAccount 工资账号
	* @param weagesPassword 密码
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public SysEmployee checkAccountByAId(String wagesAccount, String weagesPassword) throws Exception{
		return wagesDAO.checkAccountByAId(wagesAccount,weagesPassword);
	}
	/** 
	* @Description: 查询是否已经设置过密码
	* @param account 工资账号
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public SysEmployee checkAccountForInit(String account) throws Exception{
		return wagesDAO.checkAccountForInit(account);
	}
	/** 
	* @Description: 查询当前登陆人的身份证信息 
	* @param account 账号
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public SysEmployee getIcdFoUser(String account) throws Exception{
		return wagesDAO.getIcdFoUser(account);

	}

}
