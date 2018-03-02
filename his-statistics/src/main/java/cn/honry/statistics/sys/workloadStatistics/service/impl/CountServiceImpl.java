package cn.honry.statistics.sys.workloadStatistics.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.doctor.registerInfoGzltj.dao.RegisterInfoGzltjDao;
import cn.honry.statistics.sys.workloadStatistics.dao.CountDao;
import cn.honry.statistics.sys.workloadStatistics.service.CountService;
import cn.honry.statistics.sys.workloadStatistics.vo.CountVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;

@Service("countService")
@Transactional
@SuppressWarnings({ "all" })
public class CountServiceImpl implements CountService{
	
	@Resource
    private CountDao countDAO;
	
	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "registerInfoGzltjDAO")
	private RegisterInfoGzltjDao registerInfoGzltjDAO;
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO codeInInterDAO;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterInfo get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(RegisterInfo entity) {
		
	}
	
	/**
	 * 列表查询
	 * @param entity 查询条件封装实体类
	 * @author  wujiao
	 * @date 2015-08-25
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CountVo> getInfo(String seldept,String Stime,String Etime) {
		List<CountVo> countVoList=new ArrayList<CountVo>();
		
		//挂号list
		List<Registration> infoList=countDAO.findinfo(seldept,Stime,Etime);
		
		//科室list
		List<SysDepartment> deptList=countDAO.finddept();
		
		//级别list
		List<RegisterGrade> gradeList=countDAO.findgrade();
		
		//分诊list
		List<BusinessDictionary>  trigaeList=codeInInterDAO.getDictionary("triage");

		CountVo countVo=null;
		for (Registration info : infoList) {
			
					countVo = new CountVo();
					countVo.setId(info.getDeptCode());
					countVo.setDeptname(info.getDeptName());
					
					for(RegisterGrade title:gradeList){
						if(title.getCode().equals(info.getReglevlCode())){
							if("主任医师".equals(title.getName())){
								countVo.setZrnum(countVo.getZrnum()+1);
								countVo.setZrmoney(countVo.getZrmoney()+info.getRegFee());
							}if("医师".equals(title.getName())){
								countVo.setHsnum(countVo.getHsnum()+1);
								countVo.setHsmoney(countVo.getHsmoney()+info.getRegFee());
							}if("主治医师".equals(title.getName())){
								countVo.setZgnum(countVo.getZgnum()+1);
								countVo.setZgmoney(countVo.getZgmoney()+info.getRegFee());
							}if("副主任医师".equals(title.getName())){
								countVo.setFzrnum(countVo.getFzrnum()+1);
								countVo.setFzrmoney(countVo.getFzrmoney()+info.getRegFee());
							}
						}
					}
					for(BusinessDictionary triage:trigaeList){
						if(triage.getId().equals(info.getTriageType())){
							if("平诊".equals(triage.getName())){
								countVo.setPznum(countVo.getPznum()+1);
								countVo.setPzmoney(countVo.getPzmoney()+info.getRegFee());
							}
							if("优诊".equals(triage.getName())){
								countVo.setYznum(countVo.getYznum()+1);
								countVo.setYzmoney(countVo.getYzmoney()+info.getRegFee());
							}
							if("急诊".equals(triage.getName())){
								countVo.setJznum(countVo.getJznum()+1);
								countVo.setJzmoney(countVo.getJzmoney()+info.getRegFee());
							}
							if("预约".equals(triage.getName())){
								countVo.setYynum(countVo.getYynum()+1);
								countVo.setYymoney(countVo.getYymoney()+info.getRegFee());
							}
							if("过号".equals(triage.getName())){
								countVo.setGhnum(countVo.getGhnum()+1);
								countVo.setGhmoney(countVo.getGhmoney()+info.getRegFee());
							}
							if("复诊".equals(triage.getName())){
								countVo.setFznum(countVo.getFznum()+1);
								countVo.setFzmoney(countVo.getFzmoney()+info.getRegFee());
							}
							if("隐蔽".equals(triage.getName())){
								countVo.setYbnum(countVo.getYbnum()+1);
								countVo.setYbmoney(countVo.getYbmoney()+info.getRegFee());
							}
						}
					}
		
				countVo.setHjnum(countVo.getZrnum()+countVo.getZgnum()+countVo.getHsnum()+countVo.getFzrnum()+countVo.getPznum()
						+countVo.getYznum()+countVo.getJznum()+countVo.getYynum()+countVo.getGhnum()+countVo.getFznum()
						+countVo.getYbnum());
				countVo.setHjmoney(countVo.getZrmoney()+countVo.getZgmoney()+countVo.getHsmoney()+countVo.getFzrmoney()+countVo.getPzmoney()
						+countVo.getYzmoney()+countVo.getJzmoney()+countVo.getYymoney()+countVo.getGhmoney()+countVo.getFzmoney()+countVo.getYbmoney());
				
	        countVoList.add(countVo);
		}
		Map<String,CountVo> map = new HashMap<String,CountVo>();
		List<CountVo> voList=new ArrayList<CountVo>();
		CountVo newv = null;
		for(CountVo vo : countVoList){
			if(map.containsKey(vo.getId())){//有此记录需要累计相加
				newv = new CountVo();
				CountVo v = map.get(vo.getId());
				newv.setDeptname(v.getDeptname());
				newv.setZrnum(v.getZrnum()+vo.getZrnum());
				newv.setZrmoney(v.getZrmoney()+vo.getZrmoney());
				newv.setZgnum(v.getZgnum()+vo.getZgnum());
				newv.setZgmoney(v.getZgmoney()+vo.getZgmoney());
				newv.setFzrnum(v.getFzrnum()+vo.getFzrnum());
				newv.setFzrmoney(v.getFzrmoney()+vo.getFzrmoney());
				newv.setHsnum(v.getHsnum()+vo.getHsnum());
				newv.setHsmoney(v.getHsmoney()+vo.getHsmoney());
				newv.setPznum(v.getPznum()+vo.getPznum());
				newv.setPzmoney(v.getPzmoney()+vo.getPzmoney());
				newv.setYznum(v.getYznum()+vo.getYznum());
				newv.setYzmoney(v.getYzmoney()+vo.getYzmoney());
				newv.setJznum(v.getJznum()+vo.getJznum());
				newv.setJzmoney(v.getJzmoney()+vo.getJzmoney());
				newv.setYynum(v.getYynum()+vo.getYynum());
				newv.setYymoney(v.getYymoney()+vo.getYymoney());
				newv.setGhnum(v.getGhnum()+vo.getGhnum());
				newv.setGhmoney(v.getGhmoney()+vo.getGhmoney());
				newv.setFznum(v.getFznum()+vo.getFznum());
				newv.setFzmoney(v.getFzmoney()+vo.getFzmoney());
				newv.setYbnum(v.getYbnum()+vo.getYbnum());
				newv.setYbmoney(v.getYbmoney()+vo.getYbmoney());
				newv.setHjnum(v.getHjnum()+vo.getHjnum());
				newv.setHjmoney(v.getHjmoney()+vo.getHjmoney());
				
				
				map.put(vo.getId(), newv);
			}else{//没有此记录需要put该记录
				map.put(vo.getId(), vo);
			}
		}
		
		for(Map.Entry<String, CountVo> entry:map.entrySet()){
			
			voList.add(entry.getValue());
		}
		
		return voList;
	}

	/**
	 * @Description  根据科室查询：当科室为空时查询全部，不为空是直接根据科室code查询
	 * @author  marongbin
	 * @createDate： 2016年12月1日 下午4:07:17 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public Map<String,Object> getInfoNew(String deptCode,String stimew,String etimew,String page,String rows,String menuAlias)throws Exception{
		
		List<String> tnL = new ArrayList<String>();
		try{
			
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(stimew);
			Date eTime = DateUtils.parseDateY_M_D(etimew);
			
			//2.获取门诊数据保留时间S
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			if(DateUtils.compareDate(sTime, cTime)==-1){//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
				if(DateUtils.compareDate(eTime, cTime)==-1){//当结束时间小于挂号主表中的最小时间时，只查询分区表
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",stimew,etimew);
					
				}else{//查询主表和分区表
					//获取时间差（年）
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), stimew);
					
					//获取相差年份的分区集合 
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
					tnL.add(0,"T_REGISTER_MAIN_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_REGISTER_MAIN_NOW");
			}
		}catch(Exception e){
			tnL = new ArrayList<String>();
			throw new RuntimeException(e);
		}
		
		Map<String, String> triage = countDAO.getTriage();
		Map<String, String> grade = countDAO.getGrade();
		List<String> deptCodeList = new ArrayList<String>();
		if(StringUtils.isNotBlank(deptCode)&&"all".equals(deptCode)){
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
			if(deptList != null && deptList.size() < 900){
				deptCode = "";
				for (SysDepartment dept : deptList) {
					if(StringUtils.isNotBlank(deptCode)){
						deptCode += "," ;
					}
					deptCode += dept.getDeptCode(); 
				}
			}
		}
		
		List<CountVo> count = countDAO.getCount(grade,triage,deptCode,tnL, stimew, etimew,page,rows,menuAlias);
		for (CountVo vo : count) {
			vo.setHjnum(vo.getFznum()+vo.getFzrnum()+vo.getGhnum()+vo.getGhnum()+vo.getGjzmzjnum()+vo.getHsnum()+vo.getJsnum()+vo.getPznum()+
					vo.getSzmzjnum()+vo.getYbnum()+vo.getYbysnum()+vo.getYynum()+vo.getYznum()+vo.getZgnum()+vo.getZmzjnum()+vo.getZrnum()+vo.getZyysnum());
			vo.setHjmoney(vo.getFzmoney()+vo.getFzrmoney()+vo.getGhmoney()+vo.getGjzmzjmoney()+vo.getHsmoney()+vo.getJsmoney()+vo.getJzmoney()+vo.getPzmoney()+
					vo.getSzmzjmoney()+vo.getYbmoney()+vo.getYbysmoney()+vo.getYymoney()+vo.getYzmoney()+vo.getZgmoney()+vo.getZmzjmoney()+vo.getZrmoney()+vo.getZyysmoney());
		}
		
		String redKey = "KSGZLTJ"+stimew+"_"+etimew+"_"+deptCode;
		redKey=redKey.replaceAll(",", "-");
		Integer total = (Integer) redisUtil.get(redKey);
		if(total==null){
			total = countDAO.getTotal(grade,triage,deptCode,tnL, stimew, etimew,menuAlias);
			redisUtil.set(redKey, total);
		}
		
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", count);
		map.put("total", total);
		
		return map;
	}
	
	@Override
	public Map<String, Object> getInfoNow(String dept, String Stime,
	String Etime, String page, String rows, String menuAlias) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(dept)&&"all".equals(dept)){
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
			if(deptList != null && deptList.size() < 900){
				dept = "";
				for (SysDepartment deptCode : deptList) {
					if(StringUtils.isNotBlank(dept)){
						dept += "," ;
					}
					dept += deptCode.getDeptCode(); 
				}
			}
		}
		
		List<CountVo> list = countDAO.findInfo(dept, Stime, Etime, page, rows, menuAlias);
		Map<String, String> deptMmap = deptInInterService.querydeptCodeAndNameMap();
		
		for (CountVo vo : list) {
			vo.setDeptname(deptMmap.get(vo.getDeptname()));
			vo.setHjnum(vo.getFznum()+vo.getFzrnum()+vo.getGhnum()+vo.getGhnum()+vo.getGjzmzjnum()+vo.getHsnum()+vo.getJsnum()+vo.getPznum()+
					vo.getSzmzjnum()+vo.getYbnum()+vo.getYbysnum()+vo.getYynum()+vo.getYznum()+vo.getZgnum()+vo.getZmzjnum()+vo.getZrnum()+vo.getZyysnum());
			vo.setHjmoney(vo.getFzmoney()+vo.getFzrmoney()+vo.getGhmoney()+vo.getGjzmzjmoney()+vo.getHsmoney()+vo.getJsmoney()+vo.getJzmoney()+vo.getPzmoney()+
					vo.getSzmzjmoney()+vo.getYbmoney()+vo.getYbysmoney()+vo.getYymoney()+vo.getYzmoney()+vo.getZgmoney()+vo.getZmzjmoney()+vo.getZrmoney()+vo.getZyysmoney());
		
		}
		
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		int total = countDAO.findInfoTotal(dept, Stime, Etime, page, menuAlias);
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new BeanComparator("deptname"), false);
		Collections.sort(list, chain);
		map.put("rows", list);
		map.put("total", total);
		map.put("usercode", user.getAccount());
		map.put("menutype", menuAlias);
		
		return map;
	}

	/**
	 * @Description:根据开始时间，结束时间，科室编号，查询科室的工作量
	 * @param dept 科室编号
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param page 查询的页码
	 * @param rows 每页显示的记录数
	 * @param menuAlias 栏目的别名
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月28日 上午9:35:12
	 */
	public  Map<String,Object> listDeptWorkCountByMongo(String deptCode, String sTime,String eTime, String page, String rows, String menuAlias) throws Exception{
		
		return countDAO.listDeptWorkCountByMongo(deptCode, sTime, eTime,page,rows);
		
	}


}
