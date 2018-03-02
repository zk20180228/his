package cn.honry.statistics.deptstat.operationDeptLevel.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.operationDeptLevel.dao.InnerOperationDeptLevelDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.operationDeptLevel.dao.OperationDeptLevelDao;
import cn.honry.statistics.deptstat.operationDeptLevel.service.OperationDeptLevelService;
import cn.honry.statistics.deptstat.operationDeptLevel.vo.OperationDeptLevelVo;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("operationDeptLevelService")
@Transactional
@SuppressWarnings({"all"})
public class OperationDeptLevelServiceImpl implements OperationDeptLevelService{
	@Autowired
	@Qualifier(value = "operationDeptLevelDao")
	private OperationDeptLevelDao operationDeptLevelDao;
	@Autowired
	@Qualifier(value="innerOperationDeptLevelDao")
	private InnerOperationDeptLevelDao innerOperationDeptLevelDao;
	
	public void setInnerOperationDeptLevelDao(
			InnerOperationDeptLevelDao innerOperationDeptLevelDao) {
		this.innerOperationDeptLevelDao = innerOperationDeptLevelDao;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
   /**  
	 * 手术科室手术分级统计信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月17日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月17日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public Map<String,Object> queryOperationDeptLevel(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		boolean flag=new MongoBasicDao().isCollection("SSKSSSFJTJ_DAY");
		Map<String,Object> map=new HashMap<String,Object>();
		if(flag){
			map=operationDeptLevelDao.queryOperationDeptLevelMong(startTime,endTime,deptCode,menuAlias,page,rows);
		}else{
			map.put("rows", operationDeptLevelDao.queryOperationDeptLevel(startTime,endTime,deptCode,menuAlias,page, rows));
			map.put("total", operationDeptLevelDao.getTotalOperationDeptLevel(startTime,endTime,deptCode,menuAlias));
		}
		return map;
	}
   /**  
	 * 手术科室手术分级统计信息查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月17日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月17日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalOperationDeptLevel(String startTime,String endTime,String deptCode,String menuAlias) {
		boolean flag=new MongoBasicDao().isCollection("SSKSSSFJTJ");
		List<OutPatientMessageVo> list=new ArrayList<OutPatientMessageVo>();
		if(flag){
			
		}else{
			return operationDeptLevelDao.getTotalOperationDeptLevel(startTime,endTime,deptCode,menuAlias);
		}
		return list.size();
	}
		@Override
		public void init_SSKSSSFJTJ(String begin, String end, Integer type) {
			if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
				if(1==type){//日数据 dateformate:yyyy-MM-dd
						String menuAlias="SSKSSSFJTJ";
						innerOperationDeptLevelDao.initOperationDeptLeve(menuAlias, null, begin);
				}else if(2==type){//月数据 dateformate:yyyy-MM 
					Date beginDate=DateUtils.parseDateY_M(begin);
					Calendar ca=Calendar.getInstance();
					ca.setTime(beginDate);
					Date endDate=DateUtils.parseDateY_M(end);
					while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
						begin=DateUtils.formatDateY_M(ca.getTime())+"-01";//本月第一天
						ca.set(Calendar.DATE, ca.getActualMaximum(Calendar.DATE));
						end=DateUtils.formatDateY_M_D(ca.getTime());//本月最后一天
						ca.add(Calendar.DATE, 1);//下一月
					}
				}else if(3==type){//年数据dateformate:yyyy
					Date beginDate=DateUtils.parseDateY(begin);
					Date endDate=DateUtils.parseDateY(end);
					Calendar ca=Calendar.getInstance();
					ca.setTime(beginDate);
					String temp;
					while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
						begin=DateUtils.formatDateY(ca.getTime())+"-01";
						temp=begin.substring(0,4);
						end=temp+"-12";
						ca.add(Calendar.YEAR, 1);
					}
				}
			}
			
		}
	
}
