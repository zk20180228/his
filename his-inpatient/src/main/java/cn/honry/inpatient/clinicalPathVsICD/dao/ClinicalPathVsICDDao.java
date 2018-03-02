package cn.honry.inpatient.clinicalPathVsICD.dao;

import java.util.List;

import cn.honry.base.bean.model.PathVsIcd;
import cn.honry.base.dao.EntityDao;

public interface ClinicalPathVsICDDao extends EntityDao<PathVsIcd>{

	Integer queryClinicalPathVsICDNum(String keyWord, String modelId);

	List<PathVsIcd> queryClinicalPathVsICD(String keyWord,String modelId ,String page, String rows);

	PathVsIcd findPathwayVsICDById(String id);
}
