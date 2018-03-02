package cn.honry.inner.baseinfo.hospitalbed.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.hospitalbed.dao.HospitalbedInInterDAO;
import cn.honry.inner.baseinfo.hospitalbed.service.HospitalbedInInterService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.SessionUtils;
@Service("hospitalbedInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class HospitalbedInInterServiceImp implements HospitalbedInInterService{
	@Autowired
	@Qualifier(value = "hospitalbedInInterDAO")
	private HospitalbedInInterDAO hospitalbed;
	
	
	public HospitalbedInInterDAO getBedwardDAO() {
		return hospitalbed;
	}

	public void setBedwardDAO(HospitalbedInInterDAO hospitalbed) {
		this.hospitalbed = hospitalbed;
	}
	

	
	/**
	 * 根据ID查询
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 */
	@Override
	public BusinessHospitalbed getBedByid(String id) {
		return hospitalbed.get(id);
	}
	
	/**
	 * 保存与更新
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BusinessHospitalbed entity) {
		if(entity.getId() != "" && null != entity.getId()){
			hospitalbed.update(entity);
			OperationUtils.getInstance().conserve(entity.getId(), "病床管理", "UPDATE", "T_BUSINESS_HOSPITALBED", OperationUtils.LOGACTIONUPDATE);
		}else{
			hospitalbed.save(entity);
			OperationUtils.getInstance().conserve(null, "病床管理", "INSERT INTO", "T_BUSINESS_HOSPITALBED", OperationUtils.LOGACTIONINSERT);
			
		}
		hospitalbed.saveOrUpdate(entity);
	}
	
	/**
	 * 逻辑删除
	 * zhenglin
	 */
	public String deleteBedInfoById(String id) {
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		String retVal = hospitalbed.del(id,user.getId());
		OperationUtils.getInstance().conserve(id,"病床表","UPDATE","T_BUSINESS_HOSPITALBED",OperationUtils.LOGACTIONDELETE);
		return retVal;
	}
	
}
