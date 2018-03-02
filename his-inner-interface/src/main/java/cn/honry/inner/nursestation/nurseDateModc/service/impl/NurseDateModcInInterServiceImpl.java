package cn.honry.inner.nursestation.nurseDateModc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DeptDateModc;
import cn.honry.inner.nursestation.nurseDateModc.dao.NurseDateModcInInterDAO;
import cn.honry.inner.nursestation.nurseDateModc.service.NurseDateModcInInterService;

@Service("nurseDateModcInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class NurseDateModcInInterServiceImpl implements NurseDateModcInInterService {

	@Autowired
	@Qualifier(value = "nurseDateModcInInterDAO")
	private NurseDateModcInInterDAO nurseDateModcInInterDAO;
	

	/**
	 * 根据部门id查询医嘱分解时间设置
	 * @author  lyy
	 * @createDate： 2015年12月26日 上午10:15:40 
	 * @modifier lyy
	 * @modifyDate：2015年12月26日 上午10:15:40  
	 * @modifyRmk：  
	 * @param deptId 获得登录部门
	 * @version 1.0
	 */
	@Override
	public DeptDateModc findNurseDateModcByDeptId(String deptId) {
		return nurseDateModcInInterDAO.findNurseDateModcByDeptId(deptId);
	}


	@Override
	public void removeUnused(String id) {
		
	}


	@Override
	public DeptDateModc get(String id) {
		return nurseDateModcInInterDAO.get(id);
	}


	@Override
	public void saveOrUpdate(DeptDateModc entity) {
		
	}

}
