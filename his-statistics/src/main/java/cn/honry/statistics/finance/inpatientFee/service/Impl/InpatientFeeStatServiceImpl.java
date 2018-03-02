package cn.honry.statistics.finance.inpatientFee.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.statistics.finance.inpatientFee.dao.InpatientFeeStatDAO;
import cn.honry.statistics.finance.inpatientFee.service.InpatientFeeStatService;
import cn.honry.statistics.finance.inpatientFee.vo.FeeInfosVo;
import cn.honry.statistics.finance.inpatientFee.vo.InpatientInfosVo;

@Service("inpatientFeeStatService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientFeeStatServiceImpl implements InpatientFeeStatService{
	@Autowired
	@Qualifier(value = "inpatientFeeStatDAO")
	private InpatientFeeStatDAO inpatientFeeStatDAO;

	@Override
	public InpatientInfo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientInfo arg0) {
		
	}

	@Override
	public List<InpatientInfosVo> queryInpatientInfo(InpatientInfo inpatientInfoSerch, String a) throws Exception {
		List<InpatientInfosVo> inpatientInfoList = inpatientFeeStatDAO.queryInpatientInfo(inpatientInfoSerch,a);
		return inpatientInfoList;
	}

	@Override
	public List<InpatientMedicineList> queryMedicineList(String inpatientNo, String a) throws Exception {
		List<InpatientMedicineList> medicinelist = inpatientFeeStatDAO.queryMedicineList(inpatientNo,a);
		return medicinelist;
	}

	@Override
	public List<InpatientItemList> queryItemList(String inpatientNo, String a) throws Exception {
		List<InpatientItemList> itemList = inpatientFeeStatDAO.queryItemList(inpatientNo,a);
		return itemList;
	}

	@Override
	public List<InpatientInPrepay> queryInPrepay(String inpatientNo, String a) throws Exception {
		List<InpatientInPrepay> inPrepayList = inpatientFeeStatDAO.queryInPrepay(inpatientNo,a);
		return inPrepayList;
	}

	@Override
	public List<FeeInfosVo> queryFeeInfo(String inpatientNo, String a) throws Exception {
		List<FeeInfosVo> feeInfoList = inpatientFeeStatDAO.queryFeeInfo(inpatientNo,a);
		return feeInfoList;
	}

	@Override
	public List<InpatientBalanceHead> queryBalanceInfo(String inpatientNo, String a) throws Exception {
		List<InpatientBalanceHead> balanceList = inpatientFeeStatDAO.queryBalanceInfo(inpatientNo,a);
		return balanceList;
	}

	@Override
	public Map<String, String> queryEmployeeMap() throws Exception {
		return inpatientFeeStatDAO.queryEmployeeMap();
	}

	@Override
	public Map<String, String> queryFeeNameMap() throws Exception {
		return inpatientFeeStatDAO.queryFeeNameMap();
	}

	@Override
	public List<InpatientBalanceList> queryBalanceList(String inpatientNo,
			String a) throws Exception {
		return inpatientFeeStatDAO.queryBalanceList(inpatientNo, a);
	}

	@Override
	public List<InpatientFeeInfo> queryFeeInfo1(String inpatientNo, String a) throws Exception {
		return inpatientFeeStatDAO.queryFeeInfo1(inpatientNo, a);
	}
		
}
