package cn.honry.statistics.bi.inpatient.clinicalPathway.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.CpMasterIndex;
import cn.honry.inner.baseinfo.code.service.impl.CodeInInterServiceImpl;
import cn.honry.statistics.bi.inpatient.clinicalPathway.dao.StaClinicalPathwayDao;
import cn.honry.statistics.bi.inpatient.clinicalPathway.service.StaClinicalPathwayService;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.ClinicalPathVo;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.InOutDetail;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.analysisClinicalVo;

@Service("staClinicalPathwayService")
@Transactional
@SuppressWarnings({ "all" })
public class StaClinicalPathwayServiceImpl implements StaClinicalPathwayService {

	@Autowired
	@Qualifier(value = "staClinicalPathwayDao")
	private StaClinicalPathwayDao staClinicalPathwayDao;
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterServiceImpl innerCodeService;
	
	public void setInnerCodeService(CodeInInterServiceImpl innerCodeService) {
		this.innerCodeService = innerCodeService;
	}

	@Override
	public CpMasterIndex get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(CpMasterIndex arg0) {
	}

	@Override
	public void test() {
		System.out.println("service");
		staClinicalPathwayDao.test();
	}

	@Override
	public List<ClinicalPathVo> inOutList(String page, String rows,
			String sTime, String eTime, String deptCodeTopL, String inOrOutTopL) {
		return staClinicalPathwayDao.inOutList(page,rows,sTime,eTime,deptCodeTopL,inOrOutTopL);
	}

	@Override
	public Integer inOutNum(String sTime, String eTime, String deptCodeTopL,
			String inOrOutTopL) {
		return staClinicalPathwayDao.inOutNum(sTime,eTime,deptCodeTopL,inOrOutTopL);
	}

	@Override
	public List<ClinicalPathVo> notEntryList(String page, String rows,
			String sTime, String eTime, String deptCodeBL) {
		return staClinicalPathwayDao.notEntryList(page,rows,sTime,eTime,deptCodeBL);
	}

	@Override
	public Integer notEntryNum(String sTime, String eTime, String deptCodeBL) {
		return staClinicalPathwayDao.notEntryNum(sTime,eTime,deptCodeBL);
	}

	@Override
	public List<ClinicalPathVo> variationOutList(String page, String rows,
			String sTime, String eTime, String deptCodeTR, String variationTR) {
		return staClinicalPathwayDao.variationOutList(page,rows,sTime,eTime,deptCodeTR,variationTR);
	}

	@Override
	public Integer variationOutNum(String sTime, String eTime,
			String deptCodeTR, String variationTR) {
		return staClinicalPathwayDao.variationOutNum(sTime,eTime,deptCodeTR,variationTR);
	}

	@Override
	public List<InOutDetail> inOutDetailList(String page, String rows,
			String sTime, String eTime, String deptCodeBR, String inOrOutBR,
			String sexCode) {
		return staClinicalPathwayDao.inOutDetailList(page,rows,sTime,eTime,deptCodeBR,inOrOutBR,sexCode);
	}

	@Override
	public Integer inOutDetailNum(String sTime, String eTime,
			String deptCodeBR, String inOrOutBR, String sexCode) {
		return staClinicalPathwayDao.inOutDetailNum(sTime,eTime,deptCodeBR,inOrOutBR,sexCode);
	}

	@Override
	public List<analysisClinicalVo> analysisClinicalList(String page,
			String rows, String sTime, String eTime, String deptCode) {
		//字典表中查出好转。治愈的code
		List<BusinessDictionary> list = innerCodeService.getDictionary("outcom");
		String betterCode = "";
		String cureCode = "";
		for(BusinessDictionary b : list){
			if("好转".equals(b.getName())){
				betterCode = b.getEncode();
			}
			if("治愈".equals(b.getName())){
				cureCode = b.getEncode();
			}
		}
		return staClinicalPathwayDao.analysisClinicalList(page,rows,sTime,eTime,deptCode,betterCode,cureCode);
	}

	@Override
	public Integer analysisClinicalNum(String sTime, String eTime,
			String deptCode) {
		//字典表中查出好转。治愈的code
		List<BusinessDictionary> list = innerCodeService.getDictionary("outcom");
		String betterCode = "";
		String cureCode = "";
		for(BusinessDictionary b : list){
			if("好转".equals(b.getName())){
				betterCode = b.getEncode();
			}
			if("治愈".equals(b.getName())){
				cureCode = b.getEncode();
			}
		}
		return staClinicalPathwayDao.analysisClinicalNum(sTime,eTime,deptCode,betterCode,cureCode);
	}

}
