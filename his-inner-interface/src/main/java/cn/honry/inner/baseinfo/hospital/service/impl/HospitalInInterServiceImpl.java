package cn.honry.inner.baseinfo.hospital.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.Hospital;
import cn.honry.inner.baseinfo.hospital.dao.HospitalInInterDAO;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.utils.HisParameters;


@Service("hospitalInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class HospitalInInterServiceImpl implements HospitalInInterService{

	/**
	 * 查询员工信息 
	 * @author  liudelin
	 * @param 参数
	 * @date 2015-05-26 9:06
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Autowired
	@Qualifier(value = "hospitalInInterDAO")
	private HospitalInInterDAO hospitalDAO;
	
	@Override
	public List<Hospital> findAll() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("del_flg", 0);
		params.put("stop_flg", 0);
		List<Hospital>  departmentList = hospitalDAO.findByObjectProperty(params);
		return departmentList;
	}

	@Override
	public Hospital get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(Hospital arg0) {
	}

	/**  
	 * 
	 * <p> 根据code获取医院 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2017年2月20日 下午2:37:15 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年2月20日 下午2:37:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Hospital
	 *
	 */
	@Override
	public Hospital getHospitalByCode(String code) {
		return hospitalDAO.getHospitalByCode(code);
	}

	/**  
	 * 
	 * <p> 根据获取当前医院 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2017年2月20日 下午2:37:15 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年2月20日 下午2:37:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Hospital
	 *
	 */
	@Override
	public Hospital getPresentHospital() {
		return hospitalDAO.getHospitalByCode(HisParameters.CURRENTHOSPITALCODE);
	}
	
}