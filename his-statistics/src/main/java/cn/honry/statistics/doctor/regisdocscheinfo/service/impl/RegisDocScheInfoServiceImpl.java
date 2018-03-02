package cn.honry.statistics.doctor.regisdocscheinfo.service.impl;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.regisdocscheinfo.dao.RegisDocScheInfoDao;
import cn.honry.statistics.doctor.regisdocscheinfo.service.RegisDocScheInfoService;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

/**
 * @author  tangfeishuai
 * @date 创建时间：2016年5月27日 下午5:53:48
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@Service("regisDocScheInfoService")
@Transactional
@SuppressWarnings({"all"})
public class RegisDocScheInfoServiceImpl implements RegisDocScheInfoService{
	
	@Autowired
	@Qualifier(value="regisDocScheInfoDAO")
	private RegisDocScheInfoDao regisDocScheInfoDAO;
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;

	/**
	 * @Description:根据条件查询医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名； page 当前页数 ；  rows 分页条数
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<RegisDocScheInfoVo> getReRegisDocVoList(String deptName,String doctorName, String page, String rows,String begin,String end,String menuAlias)throws Exception {
		List<String> tnL;
		try {
			Date sTime = DateUtils.parseDateY_M_D(begin);
			Date eTime = DateUtils.parseDateY_M_D(end);
			//2.获取门诊数据保留时间
			//挂号排班的只保存当天在线表只保存当天的数据,因此注释掉这行代码
			//String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			
			//4.获得在线库数据应保留最小时间--挂号排班的只保存当天在线表只保存当天的数据,因此注释掉这行代码
			//Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, dTime)!=0){//在线表只保存当天的数据
				if(DateUtils.compareDate(eTime, dTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）--->结束时间小于当前时间
					
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(dTime),begin);//------>结束时间大于等于当前时间
					
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",yNum+1);
					tnL.add(0,"T_REGISTER_SCHEDULE_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_REGISTER_SCHEDULE_NOW");
			}
		} catch (Exception e) {
			tnL = new ArrayList<String>();
			throw new RuntimeException(e);
		} 
		if(StringUtils.isBlank(deptName)||"all".equals(deptName)){
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
			if(deptList == null || deptList.size() == 0){
				doctorName = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
				deptName = "";
			}else if(deptList.size()<900){
				deptName = "";
				for (SysDepartment sys : deptList) {
					if(StringUtils.isNotBlank(deptName)){
						deptName += ",";
					}
					deptName += sys.getDeptCode();
				}
			}
		}

		List<RegisDocScheInfoVo> voList = regisDocScheInfoDAO.getReRegisDocVoList(menuAlias,deptName,doctorName,page,rows,begin,end,tnL);
		
		return voList;
	}
	
	/**
	 * @Description:根据条件查询医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名； page 当前页数 ；  rows 分页条数
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getTotal(String deptName,String doctorName,String begin,String end,String menuAlias) {
				List<String> tnL;
				try {
					Date sTime = DateUtils.parseDateY_M_D(begin);
					Date eTime = DateUtils.parseDateY_M_D(end);
					
					//2.获取门诊数据保留时间
					//挂号排班的只保存当天在线表只保存当天的数据,因此注释掉这行代码
					//String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
					
					//3.获得当前时间
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dTime = df.parse(df.format(new Date()));
					
					//4.获得在线库数据应保留最小时间--挂号排班的只保存当天在线表只保存当天的数据,因此注释掉这行代码
					//Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
					tnL = new ArrayList<String>();
					
					//判断查询类型
					if(DateUtils.compareDate(sTime, dTime)!=0){//在线表只保存当天的数据
						if(DateUtils.compareDate(eTime, dTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）--->结束时间小于当前时间
						
							//获取需要查询的全部分区
							tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",begin,end);
						}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
							
							//获得时间差(年)
							int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(dTime),begin);//------>结束时间大于等于当前时间
							
							//获取相差年分的分区集合，默认加1
							tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",yNum+1);
							tnL.add(0,"T_REGISTER_SCHEDULE_NOW");
						}
					}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
						tnL.add("T_REGISTER_SCHEDULE_NOW");
					}
				} catch (Exception e) {
					tnL = new ArrayList<String>();
					throw new RuntimeException(e);
				} 
				if(StringUtils.isBlank(deptName)||"all".equals(deptName)){
					List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
					if(deptList == null || deptList.size() == 0){
						doctorName = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
						deptName = "";
					}else if(deptList.size()<900){
						deptName = "";
						for (SysDepartment sys : deptList) {
							if(StringUtils.isNotBlank(deptName)){
								deptName += ",";
							}
							deptName += sys.getDeptCode();
						}
					}
				}
				
				int voList = regisDocScheInfoDAO.getTotal(deptName,doctorName,begin,end,tnL,menuAlias);
		
				return voList;
	}


	/**
	 * @Description:根据条件查询所有医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<RegisDocScheInfoVo> getAllReRegisDocVoList(String doctorName,String begin,String end)throws Exception {
		
		/********************************2017-05-15修改查询的时间。在线表只保留当天的*************************************************************/
				List<String> tnL;
				try {

					Date sTime = DateUtils.parseDateY_M_D(begin);
					Date eTime = DateUtils.parseDateY_M_D(end);
					
					//2.获取门诊数据保留时间
					//挂号排班的只保存当天在线表只保存当天的数据,因此注释掉这行代码
					//String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
					//3.获得当前时间
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dTime = df.parse(df.format(new Date()));
					
					//4.获得在线库数据应保留最小时间--挂号排班的只保存当天在线表只保存当天的数据,因此注释掉这行代码
					//Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
					
					tnL = new ArrayList<String>();
					
					//判断查询类型
					if(DateUtils.compareDate(sTime, dTime)!=0){//在线表只保存当天的数据
						if(DateUtils.compareDate(eTime, dTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）--->结束时间小于当前时间
							
							//获取需要查询的全部分区
							tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",begin,end);
						}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
							
							//获得时间差(年)
							int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(dTime),begin);//------>结束时间大于等于当前时间
							
							//获取相差年分的分区集合，默认加1
							tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",yNum+1);
							tnL.add(0,"T_REGISTER_SCHEDULE_NOW");
						}
					}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
						tnL.add("T_REGISTER_SCHEDULE_NOW");
					}
				} catch (Exception e) {
					tnL = new ArrayList<String>();
					throw new RuntimeException(e);
				} 
				
				List<RegisDocScheInfoVo> voList = regisDocScheInfoDAO.getAllReRegisDocVoList(doctorName,begin,end,tnL);
				
				return voList;
	}
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	 * @throws Exception 
	**/
	@Override
	public FileUtil export(List<RegisDocScheInfoVo> list, FileUtil fUtil) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for (RegisDocScheInfoVo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getDeptName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDoctorName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getReglevlName()) + ",";
			record += regisDocScheInfoDAO.getWeekName(model.getWeekday()) + ",";
			record += regisDocScheInfoDAO.getNoonName(model.getNoonName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getEmpRemark()) + ",";
			record += dateFormat.format(model.getSeeDate()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getEmpPinyin()) + ",";
	
			fUtil.write(record);

		}
		return fUtil;
	}

	/**
	 * @Description:打印报表
	 * 
	**/
	@Override
	public List<RegisDocScheInfoVo> regisDocVoList(String deptName,
			String doctorName, String begin, String end,String menuAlias)throws Exception {
				List<String> tnL;
				try {

					Date sTime = DateUtils.parseDateY_M_D(begin);
					Date eTime = DateUtils.parseDateY_M_D(end);
					
					//2.获取门诊数据保留时间
					//挂号排班的只保存当天在线表只保存当天的数据,因此注释掉这行代码
					//String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
					
					//3.获得当前时间
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dTime = df.parse(df.format(new Date()));
					
					//4.获得在线库数据应保留最小时间
					//Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
					tnL = new ArrayList<String>();
					//判断查询类型
					if(DateUtils.compareDate(sTime, dTime)!=0){//在线表只保存当天的数据
						if(DateUtils.compareDate(eTime, dTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）--->结束时间小于当前时间
						
							//获取需要查询的全部分区
							tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",begin,end);
						}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
							
							//获得时间差(年)
							int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(dTime),begin);//------>结束时间大于等于当前时间
							
							//获取相差年分的分区集合，默认加1
							tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",yNum+1);
							tnL.add(0,"T_REGISTER_SCHEDULE_NOW");
						}
					}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
						tnL.add("T_REGISTER_SCHEDULE_NOW");
					}
				} catch (Exception e) {
					tnL = new ArrayList<String>();
					throw new RuntimeException(e);
				} 
				
				if(StringUtils.isBlank(deptName)||"all".equals(deptName)){
					List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
					if(deptList.size()<900){
						deptName = "";
						for (SysDepartment sys : deptList) {
							if(StringUtils.isNotBlank(deptName)){
								deptName += ",";
							}
							deptName += sys.getDeptCode();
						}
					}
				}
				
				List<RegisDocScheInfoVo> voList = regisDocScheInfoDAO.regisDocVoList(deptName,doctorName,begin,end,tnL,menuAlias);
			
				return voList;
	}

}
