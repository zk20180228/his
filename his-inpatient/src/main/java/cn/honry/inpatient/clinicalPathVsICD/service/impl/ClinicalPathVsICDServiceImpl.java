package cn.honry.inpatient.clinicalPathVsICD.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.ModelDict;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.base.bean.model.PathVsIcd;
import cn.honry.inpatient.clinicalPathVsICD.dao.ClinicalPathVsICDDao;
import cn.honry.inpatient.clinicalPathVsICD.service.ClinicalPathVsICDService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Service("clinicalPathVsICDService")
@Transactional
@SuppressWarnings({"all"})
public class ClinicalPathVsICDServiceImpl implements ClinicalPathVsICDService {

	@Autowired
	@Qualifier("clinicalPathVsICDDao")
	private ClinicalPathVsICDDao clinicalPathVsICDDao;

	@Override
	public Integer queryClinicalPathVsICDNum(String keyWord,String modelId) {
		return clinicalPathVsICDDao.queryClinicalPathVsICDNum(keyWord,modelId);
	}
	@Override
	public List<PathVsIcd> queryClinicalPathVsICD(String keyWord,String modelId, String page, String rows) {
		return clinicalPathVsICDDao.queryClinicalPathVsICD(keyWord,modelId,page,rows);
	}
	@Override
	public void saveOrUpdate(PathVsIcd pathVsIcd) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isNotBlank(pathVsIcd.getId())){//有id,更新
			pathVsIcd.setUpdateUser(longinUserAccountCode);
			pathVsIcd.setUpdateTime(DateUtils.getCurrentTime());
			pathVsIcd.setHospitalId(HisParameters.CURRENTHOSPITALID.toString());
			clinicalPathVsICDDao.save(pathVsIcd);
		}else{//没有id，添加
			pathVsIcd.setId(null);//穿过来的ID是“”，将他设置为空
			pathVsIcd.setCreateUser(longinUserAccountCode);
			pathVsIcd.setCreateTime(DateUtils.getCurrentTime());
			pathVsIcd.setHospitalId(HisParameters.CURRENTHOSPITALID.toString());
			clinicalPathVsICDDao.save(pathVsIcd);//自动生成ID
		}
		
	}
	@Override
	public PathVsIcd findPathwayVsICDById(String id) {
		return clinicalPathVsICDDao.findPathwayVsICDById(id);//自动生成ID
	}
	
}
