package cn.honry.oa.agenda.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.Schedule;
import cn.honry.oa.agenda.dao.AgendaDao;
import cn.honry.oa.agenda.service.AgendaService;
import cn.honry.utils.ShiroSessionUtils;

@Service("agendaService")
@Transactional
@SuppressWarnings({ "all" })
public class AgendaServiceImpl implements AgendaService{

	@Autowired
	@Qualifier(value = "agendaDao")
	private AgendaDao agendaDao;
	
	@Override
	public List<Schedule> qeryScheduleList(String id) {
		return agendaDao.qeryScheduleList(id);
	}

	@Override
	public void save(Schedule schedule) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		schedule.setCreateUser(account);
		schedule.setCreateTime(new Date());
		schedule.setCreateDept(deptCode);
		schedule.setUserId(account);
		schedule.setIsFinish(0);
		agendaDao.save(schedule);
	}


	@Override
	public void del(String id) throws Exception {
		Schedule s = this.queryById(id);
		s.setDel_flg(1);
		s.setDeleteTime(new Date());
		s.setDeleteUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		agendaDao.update(s);
	}

	@Override
	public Schedule queryById(String id) {
		return agendaDao.queryById(id);
	}

	@Override
	public void update(Schedule schedule) throws Exception {
		agendaDao.update(schedule);
	}

	@Override
	public List<Schedule> queryScheduleList(String id,String page,String rows) {
		if(StringUtils.isBlank(page)){
			page="1";
		}
		if(StringUtils.isBlank(rows)){
			rows="20";
		}
		return agendaDao.queryScheduleList(id,page,rows);
	}

	@Override
	public int queryScheduleListTotal(String id) {
		return agendaDao.queryScheduleListTotal(id);
	}


}
