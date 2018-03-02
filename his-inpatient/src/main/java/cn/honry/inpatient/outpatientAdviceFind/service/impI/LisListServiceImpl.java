package cn.honry.inpatient.outpatientAdviceFind.service.impI;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inpatient.outpatientAdviceFind.dao.LisListDao;
import cn.honry.inpatient.outpatientAdviceFind.service.LisListservice;
import cn.honry.inpatient.outpatientAdviceFind.vo.LisInspectionSample;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceAnalysisVO;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceDetailVo;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceVo;



@Service("lisListservice")
@Transactional
@SuppressWarnings({"all"})
public class LisListServiceImpl  implements LisListservice {

	
	@Autowired 
	@Qualifier(value = "lisListDao")
	private LisListDao lisListDao;


	@Override
	public List<LisInspectionSample> findLisInfoPage(String page, String rows,String queryName,String type ,String id) {
		return lisListDao.findLisInfoPage(page,rows,queryName,type,id);
	}

	@Override
	public int findLisInfoTotal(String queryName,String type) {
		return lisListDao.findLisInfoTotal(queryName,type);
	}

	

	
	@Override
	public List<OutpatientAdviceDetailVo> findLisDetail(String id) {
		return lisListDao.findLisDetail(id);
	}

}
