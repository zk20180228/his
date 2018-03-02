package cn.honry.outpatient.grade.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.grade.dao.GradeDAO;
import cn.honry.outpatient.grade.service.GradeService;
import cn.honry.utils.ShiroSessionUtils;

@Service("gradeService")
@Transactional
@SuppressWarnings({ "all" })
public class GradeServiceImpl implements GradeService{
	
	@Autowired
	@Qualifier(value = "gradeDAO")
	private GradeDAO gradeDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterGrade get(String id) {
		return gradeDAO.get(id);
	}

	@Override
	public void saveOrUpdate(RegisterGrade entity) {
		
	}
	
	@Override
	public void saveOrUpdagrade(RegisterGrade entity) {
		String pinyin="";
		String wb="";
		
		SysEmployee employee=ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		if(StringUtils.isBlank(entity.getId())){//保存
			//设置创建者的信息
//			entity.setEncode(employee.getTitle());
			entity.setCreateUser(employee.getJobNo());
			entity.setCreateDept(employee.getCreateDept());
			entity.setHospitalId(employee.getHospitalId());
			entity.setOrder(gradeDAO.getMaxOrder()+1l);
			
			
			entity.setId(null);
			String str = gradeDAO.getSpellCode(entity.getName());
			int index=str.indexOf("$");
			pinyin=str.substring(0,index);
			wb=str.substring(index+1);
			entity.setCodePinyin(pinyin);
			entity.setCodeWb(wb);
			entity.setCreateTime(new Date());
			entity.setDel_flg(0);
			entity.setStop_flg(0);
			
			OperationUtils.getInstance().conserve(null,"挂号级别","INSERT INTO","T_REGISTER_GRADE",OperationUtils.LOGACTIONINSERT);
		}else{//修改
			String str = gradeDAO.getSpellCode(entity.getName());
			int index=str.indexOf("$");
			pinyin=str.substring(0,index);
			wb=str.substring(index+1);
			entity.setCodePinyin(pinyin);
			entity.setCodeWb(wb);
			//设置创建者的信息
			entity.setUpdateUser(employee.getJobNo());
			entity.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(entity.getId(),"挂号级别","UPDATE","T_REGISTER_GRADE",OperationUtils.LOGACTIONUPDATE);

		}
		gradeDAO.save(entity);
	}

	@Override
	public List<RegisterGrade> queryAll() {
		return gradeDAO.queryAll();
	}

	@Override
	public List<RegisterGrade> getPage(String page, String rows,
			RegisterGrade entity) {
		return gradeDAO.getPage(entity, page, rows);
	}

	@Override
	public int getTotal(RegisterGrade registerGrade) {
		return gradeDAO.getTotal(registerGrade);
	}
	@Override
	public boolean save( String stackInfosJson) {
		return gradeDAO.saveOrUpdate(stackInfosJson);
	}

	@Override
	public void del(String id) {
		gradeDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id,"挂号级别","UPDATE","T_REGISTER_GRADE",OperationUtils.LOGACTIONDELETE);

	}

	@Override
	public List<RegisterGrade> getCombobox(String time) {
		return gradeDAO.getCombobox(time);
	}
	/**   
	*  
	* @description：查询修改的那一条级别信息
	* @author：ldl
	* @createDate：2015-10-15 上午09:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<RegisterGrade> findGradeEdit(String id) {
		List<RegisterGrade> lst = gradeDAO.findGradeEdit(id);
		return lst;
	}
	/**   
	*  
	* @description：唯一验证
	* @author：ldl
	* @createDate：2015-11-4 上午09:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<RegisterGrade> findGradeSize(String id) {
		List<RegisterGrade> lst = gradeDAO.findGradeSize(id);
		return lst;
	}
}
