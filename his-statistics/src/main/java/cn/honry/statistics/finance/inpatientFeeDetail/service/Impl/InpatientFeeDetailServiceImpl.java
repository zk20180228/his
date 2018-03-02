package cn.honry.statistics.finance.inpatientFeeDetail.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.inpatient.info.dao.InpatientInfoInInterDAO;
import cn.honry.statistics.finance.inpatientFeeDetail.dao.InpatientFeeDetailDAO;
import cn.honry.statistics.finance.inpatientFeeDetail.service.InpatientFeeDetailService;
import cn.honry.statistics.finance.inpatientFeeDetail.vo.CostDetailsVo;
import cn.honry.statistics.finance.inpatientFeeDetail.vo.FeeDetailVo;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
@Service("inpatientFeeDetailService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientFeeDetailServiceImpl implements InpatientFeeDetailService{
	@Autowired
	@Qualifier(value = "inpatientFeeDetailDAO")
	private InpatientFeeDetailDAO inpatientFeeDetailDAO;
	@Autowired
	private InpatientInfoInInterDAO inpatientInfoDAO;
	

	public void setInpatientInfoDAO(InpatientInfoInInterDAO inpatientInfoDAO) {
		this.inpatientInfoDAO = inpatientInfoDAO;
	}

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
	public List<FeeDetailVo> queryFeeInfo(InpatientInfo inpatientInfo) throws Exception {
		List<FeeDetailVo> feeList = inpatientFeeDetailDAO.queryFeeInfo(inpatientInfo);
		return feeList;
	}

	@Override
	public List<CostDetailsVo> queryCostDetails(InpatientInfo inpatientInfo) throws Exception {
		List<CostDetailsVo> detailList = inpatientFeeDetailDAO.queryCostDetails(inpatientInfo);
		return detailList;
	}

	@Override
	public List<TreeJson> queryPatientTree(String deptId, String flag) throws Exception {
		String type=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptType();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		SysDepartment sys= ShiroSessionUtils.getCurrentUserLoginNursingStationShiroSession();
		Map<String, String> map = new HashMap<String, String>();
		List<BusinessContractunit> list = inpatientInfoDAO.queryContractunit();
		if(list!=null&&list.size()>0){
			for(BusinessContractunit contractunit : list){
				map.put(contractunit.getEncode(), contractunit.getName());
			}
		}
		List<InpatientInfoNow> infoList = null;
		if(sys!=null){
			infoList = inpatientFeeDetailDAO.queryPatient(sys.getDeptCode(),type,flag);
		}
		//根节点
		TreeJson gTreeJson = new TreeJson();
		gTreeJson.setId("1");
		gTreeJson.setText("本区患者");
		gTreeJson.setIconCls("icon-house");
		Map<String,String> gAttMap = new HashMap<String, String>();
		gTreeJson.setAttributes(gAttMap);
		TreeJson fTreeJson = null;
		Map<String,String> fAttMap = null;
		if(infoList!=null&&infoList.size()>0){
			for(InpatientInfoNow infonfo : infoList){
				//二级节点(患者)
				fTreeJson = new TreeJson();
				fTreeJson.setId(infonfo.getInpatientNo());
				//渲染合同单位
				String pactCode=map.get(infonfo.getPactCode());
				if(StringUtils.isBlank(pactCode)){
					pactCode="自费";
				}
				fTreeJson.setText("【"+infonfo.getBedName()+"】【"+infonfo.getMedicalrecordId()+"】"+infonfo.getPatientName()+"【"+pactCode+"】");
				if("3".equals(infonfo.getReportSex())||"1".equals(infonfo.getReportSex())){
					fTreeJson.setIconCls("icon-user_b");
				}	
				else{
					fTreeJson.setIconCls("icon-user_female");
				}
				fTreeJson.setState("open");
				fAttMap = new HashMap<String, String>();
				fAttMap.put("pid", infonfo.getInState());
				fAttMap.put("no",infonfo.getInpatientNo());
				fTreeJson.setAttributes(fAttMap);
				treeJson.add(fTreeJson);
			}
			gTreeJson.setChildren(treeJson);
		}
		treeJsonList.add(gTreeJson);
		return treeJsonList;
	}

	@Override
	public InpatientInfoNow queryFeeInpatientInfo(String inpatientNo) throws Exception {
	 return	inpatientFeeDetailDAO.queryFeeInpatientInfo(inpatientNo);
	}

}
