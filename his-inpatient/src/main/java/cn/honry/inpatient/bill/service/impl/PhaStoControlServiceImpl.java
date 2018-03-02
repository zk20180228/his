package cn.honry.inpatient.bill.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.bill.dao.PhaStoControlDAO;
import cn.honry.inpatient.bill.service.PhaStoControlService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.reflect.TypeToken;
@Service("phaStoControlService")
@Transactional
public class PhaStoControlServiceImpl implements PhaStoControlService{
	@Autowired
	@Qualifier(value = "phaStoControlDAO")
	private PhaStoControlDAO phaStoControlDAO;
	@Override
	public void removeUnused(String id) {
		phaStoControlDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id, "摆药台设置", "UPDATE", "T_OUTPATIENT_DRUGCONTROL", OperationUtils.LOGACTIONUPDATE);
	}
	
	@Override
	public OutpatientDrugcontrol get(String id) {
		return phaStoControlDAO.get(id);
	}

	@Override
	public void saveOrUpdate(OutpatientDrugcontrol entity) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getId())){
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(dept.getDeptName());
				OperationUtils.getInstance().conserve(entity.getId(), "摆药台设置", "UPDATE", "T_OUTPATIENT_DRUGCONTROL", OperationUtils.LOGACTIONUPDATE);
			}else{
				entity.setId(null);
				entity.setCreateUser(user.getAccount());
				entity.setCreateTime(new Date());
				if(dept!=null){
					entity.setCreateDept(dept.getDeptCode());
				}
				OperationUtils.getInstance().conserve(null, "摆药台设置", "INSERT_INTO", "T_OUTPATIENT_DRUGCONTROL", OperationUtils.LOGACTIONINSERT);
			}
			phaStoControlDAO.save(entity);
		}
	}

	@Override
	public List<OutpatientDrugcontrol> getPage(String page, String rows,
			OutpatientDrugcontrol outpatientDrugcontrol) throws Exception {
		
		return phaStoControlDAO.getPage(page,rows,outpatientDrugcontrol);
	}
	
	@Override
	public int getTotal(OutpatientDrugcontrol outpatientDrugcontrol) throws Exception {
		return phaStoControlDAO.getTotal(outpatientDrugcontrol);
	}


	@Override
	public List<OutpatientDrugcontrol> getPageList(String page, String rows, 
			OutpatientDrugcontrol outpatientDrugcontrol) throws Exception {
		return phaStoControlDAO.getPageList(page,rows,outpatientDrugcontrol);
	}
	/**  
	 *  
	 * @Description：  保存
	 * @Author：dh
	 * @ModifyDate：2015-12-21 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOutpatientDrugcontrol(
			OutpatientDrugcontrol outpatientDrugcontrol) throws Exception {
		phaStoControlDAO.saveOutpatientDrugcontrol(outpatientDrugcontrol);
		OperationUtils.getInstance().conserve(null, "摆药台设置", "INSERT_INTO", "T_OUTPATIENT_DRUGCONTROL", OperationUtils.LOGACTIONINSERT);
	}
	/**  
	 *  
	 * @Description：  查询
	 * @Author：dh
	 * @ModifyDate：2015-12-28 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<OutpatientDrugcontrol> QueryOutpatientDrugcontrol(String page,
			String rows, OutpatientDrugcontrol outpatientDrugcontrol) throws Exception {
		List<OutpatientDrugcontrol> list=phaStoControlDAO.QueryOutpatientDrugcontrol(page, rows, outpatientDrugcontrol);
		return list;
	}
	/**  
	 *  
	 * @Description：总数
	 * @Author：dh
	 * @ModifyDate：2015-12-28 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotalOutpatientDrugcontrol(
			OutpatientDrugcontrol outpatientDrugcontrol) throws Exception {
		return phaStoControlDAO.getTotalOutpatientDrugcontrol(outpatientDrugcontrol);
	}
	//删除
	@Override
	public void delUpdate(String id) throws Exception {
		phaStoControlDAO.delUpdate(id);
		OperationUtils.getInstance().conserve(id,"摆药台","UPDATE","T_OUTPATIENT_DRUGCONTROL",OperationUtils.LOGACTIONDELETE);
	}

	@Override
	public void UpdateOutpatientDrugcontrol(String deptCode,
			String controlName, String showLevel, String controlAttr,
			String sendType, String mark) throws Exception {
	}

	@Override
	public void saveOrUpdate(String billClassIds,String deptCode,String infoJson) throws Exception {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		List<OutpatientDrugcontrol> modelList = null;
		try {
			infoJson=infoJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			modelList =JSONUtils.fromJson(infoJson,  new TypeToken<List<OutpatientDrugcontrol>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (OutpatientDrugcontrol entity : modelList) {
			if(entity != null){
				if(StringUtils.isNotBlank(entity.getId())){
					entity.setUpdateUser(user.getAccount());
					entity.setUpdateTime(new Date());
					OperationUtils.getInstance().conserve(entity.getId(),"摆药台管理","UPDATE","T_OUTPATIENT_DRUGCONTROL",OperationUtils.LOGACTIONUPDATE);
				}else{
					entity.setDeptCode(deptCode);
					entity.setId(null);
					entity.setCreateUser(user.getAccount());
					if(dept!=null){
						entity.setCreateDept(dept.getDeptCode());
					}
					entity.setCreateTime(new Date());
					OperationUtils.getInstance().conserve(null,"摆药台管理","INSERT_INTO","T_OUTPATIENT_DRUGCONTROL",OperationUtils.LOGACTIONINSERT);
				}
				phaStoControlDAO.save(entity);
				if(StringUtils.isNotBlank(billClassIds)){
					String[] pid_array=billClassIds.split(",");
					for (String pid : pid_array) {
						phaStoControlDAO.updateControlId(entity.getId(),pid);
					}
				}
			}
		}
	}
	
	@Override
	public void Update(String classId,String deptCode,String con,String infoJson) throws Exception {
		List<String> classbill= phaStoControlDAO.findDrugBillclass(con);
		if(StringUtils.isNotBlank(classId)){
			String[] pid_array=classId.split(",");
			for (String pid : pid_array) {
				classbill.remove(pid);
				phaStoControlDAO.updateControlId(con,pid);
			}
		}
		if(classbill.size()>0){
			for(int i=0;i<classbill.size();i++){
				phaStoControlDAO.updateClassBillcon(classbill.get(i));
			}
			
		}
		phaStoControlDAO.UpdateOutpatientDrugcontrol(deptCode,con,infoJson);
	}
	
	@Override
	public List<OutpatientDrugcontrol> QueryOutpatientDrugcontrolupdate(
			String id) throws Exception {
		return phaStoControlDAO.QueryOutpatientDrugcontrolupdate(id);
	}

	@Override
	public List<DrugBillclass> queryDrugBillclass(String controId) throws Exception {
		return phaStoControlDAO.queryDrugBillclass(controId);
	}
}
