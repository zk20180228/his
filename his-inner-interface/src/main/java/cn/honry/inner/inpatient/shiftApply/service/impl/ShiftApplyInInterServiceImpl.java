package cn.honry.inner.inpatient.shiftApply.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientShiftApply;
import cn.honry.inner.inpatient.shiftApply.dao.ShiftApplyInInterDao;
import cn.honry.inner.inpatient.shiftApply.service.ShiftApplyInInterService;


@Service("shiftApplyInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class ShiftApplyInInterServiceImpl implements ShiftApplyInInterService {

	@Autowired
	@Qualifier(value = "shiftApplyInInterDao")
	private ShiftApplyInInterDao shiftApplyInInterDao;
	
	public void removeUnused(String id) {

	}

	//根据id获取一个对象
	public InpatientShiftApply get(String id) {
		return null;
	}

	/***
	 * 对于转科医嘱，添加或者修改相应的转科申请信息；对于护理医嘱，更新患者住院信息中的护理级别；对于饮食医嘱，更新患者住院信息中的饮食信息
	 */
	//修改，添加一个对象
	public void saveOrUpdate(InpatientShiftApply entity) {
		entity.setUpdateUser("");
		entity.setUpdateTime(new Date());
		shiftApplyInInterDao.save(entity);
	}
	/**
	 * 根据住院流水号查询患者是否有转科医嘱
	 */
	public InpatientShiftApply getApply(String inpatientno, String oldDeptCode) {
		return shiftApplyInInterDao.getApply(inpatientno,oldDeptCode);
	}

	@Override
	public void save(InpatientShiftApply inpatientShiftApply) {
		shiftApplyInInterDao.save(inpatientShiftApply);
	}

	@Override
	public Integer getHappenNo(String inpatientNo) {
		return shiftApplyInInterDao.getHappenNo(inpatientNo);
	}

}
