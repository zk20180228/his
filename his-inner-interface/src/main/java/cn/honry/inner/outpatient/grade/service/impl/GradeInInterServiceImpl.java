package cn.honry.inner.outpatient.grade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.inner.outpatient.grade.dao.GradeInInterDAO;
import cn.honry.inner.outpatient.grade.service.GradeInInterService;

@Service("gradeInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class GradeInInterServiceImpl implements GradeInInterService{
	
	@Autowired
	@Qualifier(value = "gradeInInterDAO")
	private GradeInInterDAO gradeInInterDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterGrade get(String id) {
		return gradeInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(RegisterGrade entity) {
		
	}
	

	@Override
	public List<RegisterGrade> getPage(String page, String rows,
			RegisterGrade entity) {
		return gradeInInterDAO.getPage(entity, page, rows);
	}

	@Override
	public int getTotal(RegisterGrade registerGrade) {
		return gradeInInterDAO.getTotal(registerGrade);
	}
	
	@Override
	public List<RegisterGrade> getCombobox(String time,String q) {
		return gradeInInterDAO.getCombobox(time,q);
	}
}
