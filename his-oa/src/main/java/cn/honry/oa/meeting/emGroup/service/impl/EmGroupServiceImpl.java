package cn.honry.oa.meeting.emGroup.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.oa.meeting.emGroup.dao.EmGroupDao;
import cn.honry.oa.meeting.emGroup.service.EmGroupService;
import cn.honry.oa.meeting.emGroup.vo.EmGroupVo;
import cn.honry.oa.meeting.emGroup.vo.GroupVo;

@Transactional
@Service("emGroupService")
public class EmGroupServiceImpl implements EmGroupService{

	@Resource
	private EmGroupDao emGroupDao;
	public void setEmGroupDao(EmGroupDao emGroupDao) {
		this.emGroupDao = emGroupDao;
	}
	
	@Override
	public List<GroupVo> loadGroup(String text) {
		
		return emGroupDao.loadGroup(text);
	}

	@Override
	public List<EmGroupVo> groupList(String id, String employee_name,String employee_jobon, String dept_name, String page, String rows) {
		
		return emGroupDao.groupList(id, employee_name, employee_jobon, dept_name, page, rows);
	}

	@Override
	public int groupCount(String id, String employee_name,String employee_jobon, String dept_name) {
		
		return emGroupDao.groupCount(id, employee_name, employee_jobon, dept_name);
	}

	@Override
	public void delEmployeeById(String id) {
		
		emGroupDao.delEmployeeById(id);
	}

	@Override
	public void addEmp(String id, String text, String employee_jobon) {
		
		emGroupDao.addEmp(id, text,employee_jobon);
	}

	@Override
	public List<GroupVo> loadDept(String id) {
	
		return emGroupDao.loadDept(id);
	}

}
