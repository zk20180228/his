package cn.honry.statistics.bi.bistac.temporary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.bi.bistac.temporary.dao.CheckListReportDAO;
import cn.honry.statistics.bi.bistac.temporary.service.CheckListReportService;
import cn.honry.statistics.bi.bistac.temporary.vo.CheckListReportVo;

/**  
 *  
 * @className：CheckListReportServiceImpl
 * @Description：门诊就医-检验单
 * @Author：gaotiantian
 * @CreateDate：2017-4-10 下午2:09:12 
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-10 下午2:09:12
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("CheckListReportService")
@Transactional
@SuppressWarnings({ "all" })
public class CheckListReportServiceImpl implements CheckListReportService {
	@Autowired
	@Qualifier(value = "CheckListReportDAO")
	private CheckListReportDAO CheckListReportDAO;

	@Override
	public List<CheckListReportVo> getCheckListReport(String clinicCode,
			String medicalrecordId) {
		// TODO Auto-generated method stub
		return CheckListReportDAO.getCheckListReport(clinicCode, medicalrecordId);
	}
}

