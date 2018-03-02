package cn.honry.outpatient.webPreregister.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.outpatient.webPreregister.dao.WebPreregisterDao;
import cn.honry.outpatient.webPreregister.service.WebPreregisterService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.RedisUtil;

@Service("webPreregisterService")
@Transactional
@SuppressWarnings({ "all" })
public class WebPreregisterServiceImpl implements WebPreregisterService {

	@Resource
	private RedisUtil redis;
	
	private ExecutorService fixedThreadPool=Executors.newFixedThreadPool(5); 
	
	@Autowired
	@Qualifier(value = "webPreregisterDao")
	private WebPreregisterDao webPreregisterDao;
	
	public void setWebPreregisterDao(WebPreregisterDao webPreregisterDao) {
		this.webPreregisterDao = webPreregisterDao;
	}

	@Autowired
	private KeyvalueInInterDAO keyvalueInInterDAO;
	
	
	@Override
	public RegisterPreregisterNow get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(RegisterPreregisterNow arg0) {
		
	}

	/**
	 * 根据科室编码和日期查询某科室下医生的排班信息
	 * @param deptCode 科室编码
	 * @param rq 日期
	 * @param firstResult 开始位置
	 * @param rows 每页显示的记录数
	 * @return
	 */
	 
	public List<RegisterScheduleNow> getRegisterList(String deptCode,String rq,int firstResult,int rows)throws Exception{
		List<RegisterScheduleNow> list = webPreregisterDao.getRegisterList(deptCode, rq,firstResult,rows);
		for (RegisterScheduleNow reg : list) {
			Integer limit = reg.getNetLimit();
			String department = reg.getScheduleWorkdept();
			Integer midday = reg.getMidday();
			String field=department+"-"+reg.getDoctor()+"-"+midday;
			
			Boolean flag = redis.hexists(rq, field);// 判断缓存中是否存在指定的key-field字段
			if (!flag) {// 如果不存在,将网络限额存到缓存中
				Long hincr = redis.hincr(rq, field, limit.longValue());
				if (limit.longValue() != hincr) {// 避免同时操作
					redis.hincr(rq, field, -hincr);
				}
			}
			Long hget = (Long) redis.hincr(rq, field, 0L);
			reg.setNetLimit(hget.intValue());
		
		}
		return list;
	}
	
	/**
	 * 保存预约挂号信息
	 * @param reg
	 */
	public String savePreregister(RegisterPreregisterNow reg){
		String key = reg.getPreregisterDeptname();//虚拟字段,用于存放string类型的预约时间,作为key
		String dept = reg.getPreregisterDept();//科室code
		String doctor = reg.getPreregisterExpert();//专家code
		Integer midday = reg.getMidday();//午别
		String field= dept+"-"+doctor+"-"+midday;
		final String no = reg.getPreregisterCertificatesno();//患者身份证号
		final String newKey=key+"-"+field;
		RegisterPreregisterNow hget = (RegisterPreregisterNow) redis.hget(newKey, no);
		if(hget!=null){//已经挂过号
			return "2";
		}
		Long f = redis.hincr(key, field, 0L);
		if(f<=0){//号源已满
			return "0";
		}
		Long hincr = redis.hincr(key, field, -1L);
		if(hincr>=0){//已抢到预约号即 预约成功
			redis.hset(newKey,no, reg);
			fixedThreadPool.execute(new Runnable(){
				@Override
				public void run() {
					RegisterPreregisterNow preg=(RegisterPreregisterNow) redis.hget(newKey, no);
					webPreregisterDao.save(preg);
				}});
			return "1";
		}else{//号源已满
			//这里+1是对于两个患者同时抢最后一个号源的情况,有一个患者抢到号源,另一个未抢到,但号源变为了-1,应该+1,将号源数置为0
			redis.hincr(key, field, 1L);
			return "0";
		}
	}

	/**
	 * 获取预约编号
	 * @return
	 */
	public String getPreNo(){
		Integer val = keyvalueInInterDAO.getVal("RegisterPreregister");
		String no=DateUtils.getStringYear()+DateUtils.getStringMonth()
				+DateUtils.getStringDay()+String.format("%06d", val);
		return no;
	}
}
