package cn.honry.finance.contractunit.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.Hospital;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.finance.contractunit.dao.RegisterFeeDAO;
import cn.honry.finance.contractunit.service.RegisterFeeService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Service("registerFeeService")
@Transactional
@SuppressWarnings({ "all" })
public class RegisterFeeServiceImpl implements RegisterFeeService{
	
	@Autowired
	@Qualifier(value = "registerFeeDAO")
	private RegisterFeeDAO registerFeeDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterFee get(String id) {
		return registerFeeDAO.get(id);
	}

	@Override
	public void saveOrUpdate(RegisterFee entity) {
		
	}

	@Override
	public int getTotal(RegisterFee entity) {
		return registerFeeDAO.getTotal(entity);
	}
	@Override
	public List<RegisterFee> getPage(String page, String rows,RegisterFee entity) {
		return registerFeeDAO.getPage(entity, page, rows);
	}
	
	@Autowired 
	private HospitalInInterService hospitalInInterService;
	/**  
	 *  
	 * @Description：  挂号费维护添加&修改
	 * @Author：liudelin
	 * @CreateDate：2015-6-4 下午05:12:16  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-26下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void save(RegisterFee registerFee,String treeCode) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		if(StringUtils.isBlank(registerFee.getId())){//保存
			registerFee.setId(null);
			registerFee.setUnitId(treeCode);
			registerFee.setCreateTime(new Date());
			registerFee.setDel_flg(0);
			if(registerFee.getStop_flg()==null){
				registerFee.setStop_flg(0);

			}
			Hospital hospital=hospitalInInterService.getHospitalByCode(HisParameters.CURRENTHOSPITALCODE);
			registerFee.setHospitalId(hospital);
			registerFee.setCreateUser(userId);
			registerFee.setCreateDept(deptId);
			registerFee.setCreateTime(new Date());
			OperationUtils.getInstance().conserve(null,"合同单位管理","INSERT INTO","T_REGISTER_FEE",OperationUtils.LOGACTIONINSERT);
		}else{//修改
			registerFee.setUpdateUser(userId);
			registerFee.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(registerFee.getId(),"合同单位管理","UPDATE","T_REGISTER_FEE",OperationUtils.LOGACTIONUPDATE);
		}
		registerFeeDAO.save(registerFee);
	}
	/**  
	 *  
	 * @Description：  删除
	 * @Author：liudelin
	 * @CreateDate：2015-6-4 下午05:12:16  
	 * @Modifier：liudelin
	 * @ModifyDate：2015-6-4 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void del(String id) {
		registerFeeDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getId());
		OperationUtils.getInstance().conserve(id,"合同单位管理","UPDATE","T_REGISTER_FEE",OperationUtils.LOGACTIONDELETE);
	}
	/**  
	 *  
	 * @Description：  挂号级别
	 * @Author：ldl
	 * @CreateDate：2015-7-9  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterGrade> gradeFeeCombobox() {
		List<RegisterGrade> lst = registerFeeDAO.gradeFeeCombobox();
		return lst;
	}
	/**  
	 *  
	 * @Description：  查询费用修改数据
	 * @Author：ldl
	 * @CreateDate：2015-10-15  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterFee> findRegisterFee(String ids) {
		List<RegisterFee> lst = registerFeeDAO.findRegisterFee(ids);
		return lst;
	}
	/**  
	 * 
	 * @Description：   验证挂号级别是否存在
	 * @Author：lyy
	 * @CreateDate：2015-11-26 下午05:37:56  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-26 下午05:37:56  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public boolean queryFeeValidate(String unitId,String gradeId) {
		return registerFeeDAO.queryFeeValidate(unitId,gradeId);
	}

	@Override
	public Integer queryOrder(String unitId) {
		return registerFeeDAO.getOrderbyid(unitId);
	}

}
