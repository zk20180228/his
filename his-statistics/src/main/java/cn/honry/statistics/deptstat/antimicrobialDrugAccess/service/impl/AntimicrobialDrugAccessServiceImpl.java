package cn.honry.statistics.deptstat.antimicrobialDrugAccess.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.deptstat.antimicrobialDrugAccess.dao.AntimicrobialDrugAccessDao;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.service.AntimicrobialDrugAccessService;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo.AntimicrobialDrugAccessVo;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.FileUtil;
@Service("antimicrobialDrugAccessService")
@Transactional
@SuppressWarnings({ "all" })
public class AntimicrobialDrugAccessServiceImpl  implements AntimicrobialDrugAccessService {
	@Autowired
	@Qualifier(value = "antimicrobialDrugAccessDao")
	private AntimicrobialDrugAccessDao antimicrobialDrugAccessDao;

	public void setAntimicrobialDrugAccessDao(
			AntimicrobialDrugAccessDao antimicrobialDrugAccessDao) {
		this.antimicrobialDrugAccessDao = antimicrobialDrugAccessDao;
	}

	@Override
	public List<AntimicrobialDrugAccessVo> queryType() {
		return antimicrobialDrugAccessDao.queryType();
	}

	@Override
	public List<AntimicrobialDrugAccessVo> queryAntimicrobialDrugAccess(
			String dept,String page,String rows,String menuAlias) {
		if(StringUtils.isBlank(page)){
			page="1";
		}
		if(StringUtils.isBlank(rows)){
			rows="20";
		}
		return antimicrobialDrugAccessDao.queryAntimicrobialDrugAccess(dept,page,rows,menuAlias);
	}

	@Override
	public int queryAntimicrobialDrugAccessTotal(String dept,String menuAlias) {
		return antimicrobialDrugAccessDao.queryAntimicrobialDrugAccessTotal(dept,menuAlias);
	}

	@Override
	public FileUtil exportList(List<AntimicrobialDrugAccessVo> list, FileUtil fUtil) {
		for (AntimicrobialDrugAccessVo model : list) {
			String record="";
				record =" "+model.getEname()==null?"":model.getEname()+ ",";
//				record += CommonStringUtils.trimToEmpty(model.getDeptName()) + ",";
				record += model.getEcode()==null?"":"JobNo:"+model.getEcode() + ",";
				record += model.getElevel()==null?"":model.getElevel()+ ",";
				record += model.getEaccess()==null?"":model.getEaccess()+ ",";
				try {
					fUtil.write(record);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return fUtil;
	}

	@Override
	public List<AntimicrobialDrugAccessVo> queryAntimicrobialDrugAccessFromDB(List<String> dept,
			String page, String rows,String menuAlias) {
		return antimicrobialDrugAccessDao.queryOperationProportionFromDB(dept,page,rows,menuAlias);
	}
	
}
