package cn.honry.inner.inpatient.inpatientOrder.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.DeptDateModc;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugSplit;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.inpatient.execUndrug.dao.UnDrugOrderExecInInterDao;
import cn.honry.inner.inpatient.execdrug.dao.DrugExecOrderInInterDao;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.inpatient.info.vo.FeeInInterVo;
import cn.honry.inner.inpatient.inpatientOrder.dao.InpatientOrderInInterDao;
import cn.honry.inner.inpatient.inpatientOrder.dao.InpatientOrderNowInInterDao;
import cn.honry.inner.inpatient.inpatientOrder.service.InpatientOrderInInterService;
import cn.honry.inner.inpatient.inpatientOrder.service.InpatientOrderNowInInterService;
import cn.honry.inner.inpatient.inpatientOrder.vo.InpatientOrderInInterVO;
import cn.honry.inner.inpatient.inpatientOrder.vo.MsgInInterVO;
import cn.honry.inner.inpatient.kind.service.InpatientKindInInterService;
import cn.honry.inner.nursestation.nurseDateModc.dao.NurseDateModcInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.technical.mat.service.MatOutPutInInterService;
import cn.honry.inner.technical.mat.vo.OutputInInterVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.SessionUtils;

/**
 * @author Administrator
 *
 */
/**
 * @author zl
 * @createDate： 2016年4月22日 下午2:31:14
 * @modifier liujl yueyf
 * @modifyDate：2016年4月22日 下午2:31:14
 * @param：
 * @modifyRmk：
 * @version 1.0
 */
@Service("inpatientOrderNowInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientOrderNowInInterServiceImpl implements InpatientOrderNowInInterService {
	@Autowired
	@Qualifier(value = "inpatientOrderNowInInterDao")
	private InpatientOrderNowInInterDao inpatientOrderNowInInterDao;
	@Override
	public InpatientOrderNow get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientOrderNow arg0) {
		
	}
	
	
	
}
