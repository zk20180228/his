package cn.honry.inner.system.quartzTiming.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.bean.model.TimingRules;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.outpatient.schedule.dao.ScheduleInInterDAO;
import cn.honry.inner.outpatient.scheduleModle.dao.ScheduleModleInInterDAO;
import cn.honry.inner.system.pretreatment.service.PretreatmentService;
import cn.honry.inner.system.quartzTiming.service.QuartzTiminginnerService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.RedisUtil;
@Transactional
@Service("quartzTiminginnerService")
@SuppressWarnings({"all"})
public class QuartzTiminginnerServiceImpl implements QuartzTiminginnerService{
	
	@Resource
	private RedisUtil redis;
	@Autowired
	@Qualifier(value = "scheduleInInterDAO")
	private ScheduleInInterDAO scheduleInInterDAO;//排班dao
	@Autowired
	@Qualifier(value = "scheduleModleInInterDAO")
	private ScheduleModleInInterDAO scheduleModleInInterDAO;//排班模板dao
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService innerCodeService;

	@Autowired
	@Qualifier(value = "pretreatmentService")
	private PretreatmentService pretreatmentService;
	

	public RegisterScheduleNow get(String arg0) {
		return null;
	}

	public void removeUnused(String arg0) {
		
	}

	public void saveOrUpdate(RegisterScheduleNow arg0) {
		
	}

	/**  
	 *  
	 * @Description：  定时任务处理业务的逻辑方法  数据迁移  排班，预约挂号，挂号，住院病床收费
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-11 下午05:35:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-11 下午05:35:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void quartzTiming(){
		String StrDate = DateUtils.getDate();//获得当前时间String类型
		List<HospitalParameter> codeAnewayList =scheduleInInterDAO.getParameter("schdeuleDate");
		int number=Integer.parseInt(codeAnewayList.get(0).getParameterValue());
		System.err.println(StrDate+" 排班模板信息，开始迁移,共"+number+"天的数据:");

		for(int i=0;i<number;i++){
			List<RegisterScheduleNow> list=new ArrayList<RegisterScheduleNow>();
			Date day=DateUtils.addDay(DateUtils.getCurrentTime(),i);
			String noe=DateUtils.formatDateY_M_D(day);
			int weekNumber2=DateUtils.getWeekOfDay(DateUtils.formatDateY_M_D(day));
			
			List<RegisterSchedulemodel> modelList = scheduleModleInInterDAO.getmodelList(weekNumber2);
			if(modelList!=null&&modelList.size()>0){
				int retVal = 0;
				for(RegisterSchedulemodel model : modelList){
					RegisterScheduleNow schedule = new RegisterScheduleNow();
					schedule.setId(null);
					schedule.setScheduleClass(model.getModelClass());//1挂号排班2工作排班
					schedule.setScheduleWorkdept(model.getModelWorkdept());
					schedule.setModelId(model.getId());//模板编号
					schedule.setType(model.getModeType());
					schedule.setDepartment(model.getDepartment());
					schedule.setClinic(model.getClinic());
					schedule.setDoctor(model.getModelDoctor());
					schedule.setWeek(weekNumber2);
					schedule.setDate(day);
					schedule.setMidday(model.getModelMidday());
					schedule.setLimit(model.getModelLimit());
					schedule.setPreLimit(model.getModelPrelimit());
					schedule.setPhoneLimit(model.getModelPhonelimit());
					if(model.getModelNetlimit()==null){
						schedule.setNetLimit(0);
					}else{
						schedule.setNetLimit(model.getModelNetlimit());
					}
					schedule.setSpeciallimit(model.getModelSpeciallimit());
					schedule.setStartTime(model.getModelStartTime());
					schedule.setEndTime(model.getModelEndTime());
					schedule.setAppFlag(model.getModelAppflag());
					schedule.setIsStop(2);
					schedule.setRemark(model.getModelRemark());
					schedule.setReggrade(model.getModelReggrade());
					schedule.setCreateTime(new Date());
					schedule.setScheduleDeptname(model.getDeptName());//科室名字
					schedule.setScheduleClinicname(model.getClinName());
					schedule.setScheduleDoctorname(model.getEmpName());
					schedule.setScheduleMiddayname(model.getCodeName());
					String arg=schedule.getScheduleWorkdept()+"-"+schedule.getDoctor()+"-"+schedule.getMidday();
					boolean isNotSave = scheduleInInterDAO.getboolean(null,schedule.getDepartment(),schedule.getWeek(),schedule.getDoctor(),schedule.getMidday(),day);
					if(!isNotSave){
						list.add(schedule);
						retVal+=1;
					}
					if(i==0){//当天的数据
						try {
							redis.remove("MZGH");//门诊挂号号源信息
							redis.removePattern(noe+"*");//预约挂号患者信息
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				scheduleInInterDAO.saveOrUpdateList(list);
				System.err.println(DateUtils.formatDateY_M_D_H_M_S(new Date()) +"第"+(i+1)+"天:"+noe+"的排班模板信息，迁移成功,共计"+modelList.size()+"条数据,迁移数据"+retVal+"条!");
			}else{
				System.err.println("没有"+noe+"的排班模板信息，无需迁移！");
			}
		}
		
		System.out.println(StrDate+"排班模板信息迁移结束");
	}
	
	/**
	 * 执行预处理
	 * @param menuAlias 栏目别名
	 * @param type 类型(1-日;2-月;3-年)
	 * @param date 开始时间(格式为:YYYY-MM-DD)
	 */
	public void pretreatmentExecute(String menuAlias,Integer type,String date){
		
		//1.根据栏目别名、类型查询处理日志中状态为0(处理失败)的记录,将处理状态加到缓存中,重新计算处理,处理完成后修改处理状态;
		List<MongoLog> mongoLogList = pretreatmentService.getMongoLog(menuAlias, type+"");
		String key ="pretreState";
		if(mongoLogList!=null && mongoLogList.size()>0){
			for (MongoLog mongoLog : mongoLogList) {
				Date time = mongoLog.getStartTime();//开始日期
				if(time==null){
					continue;
				}
				String sdate = "";
				switch (type) {
				case 1:
					sdate = DateUtils.formatDateY_M_D(time);
					break;
				case 2:
					sdate= DateUtils.formatDateY_M(time);
					break;
				case 3:
					sdate=sdate = DateUtils.formatDateY(time);
					break;
				}
				String field=menuAlias+"_"+type+"_"+sdate;
				Integer hget=null;
				try {
					hget = (Integer)redis.hget(key, field);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if(hget!=null && hget==2){//处理中
					continue;
				}else{
					try {
						redis.hset(key, field, 2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					pretreatmentService.saveorUpdateHandingStatus(menuAlias, type+"", sdate, 2, 0);//修改数据库中的处理状态
				}
				//重新计算
				try {
					pretreatmentService.mongoHanding(menuAlias, type+"", sdate);
					//修改处理状态
					redis.hset(key, field, 0);
					pretreatmentService.saveorUpdateHandingStatus(menuAlias, type+"", sdate, 1, 0);
				} catch (Exception e) {
					try {
						redis.hset(key, field, 1);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					pretreatmentService.saveorUpdateHandingStatus(menuAlias, type+"", sdate, 0, 0);
					e.printStackTrace();
				}
			}
		}
		//2.根据栏目别名、类型和开始时间从缓存中获取相应的处理状态:
		// (1).如果处理状态不存在,向缓存和数据库中添加相应的数据;
		// (2).处理状态存在时,如果处理状态为:2(处理中)跳过;如果状态为0(失败)或1(成功)则修改处理状态为2并重新计算
		// (3).处理完成后,修改处理状态.
		
		String newDate=date;
		if(type==2){//按月执行
			newDate=date.substring(0,7);
		}
		if(type==3){//按年执行
			newDate=date.substring(0,4);
		}
		String field= menuAlias+"_"+type+"_"+newDate;
		Integer hget=null;
		try {
			hget = (Integer) redis.hget(key, field);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(hget!=null && hget==2){
			return;
		}else{
			try {
				redis.hset(key, field, 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			pretreatmentService.saveorUpdateHandingStatus(menuAlias, type+"", newDate, 2, 0);
		}
		try {
			pretreatmentService.mongoHanding(menuAlias, type+"", date);
			//修改处理状态
			redis.hset(key, field, 0);
			pretreatmentService.saveorUpdateHandingStatus(menuAlias, type+"", newDate, 1, 0);
		} catch (Exception e) {
			try {
				redis.hset(key, field, 1);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			pretreatmentService.saveorUpdateHandingStatus(menuAlias, type+"", newDate, 0, 0);
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取有效的、开启状态的预处理定时规则
	 * @return 
	 */
	public List<TimingRules> getRulesList(){
		return pretreatmentService.getRulesList();
	}
	
	/**
	 * 删除三天前的执行成功的日志
	 */
	public void delMongoLog(){
		pretreatmentService.delMongoLog();
	}
}
