package cn.honry.inpatient.clinicalPathVsICD.service;

import java.util.List;

import cn.honry.base.bean.model.PathVsIcd;

public interface ClinicalPathVsICDService {

	Integer queryClinicalPathVsICDNum(String keyWord, String modelId);

	List<PathVsIcd> queryClinicalPathVsICD(String keyWord,String modelId,String page, String rows);

	void saveOrUpdate(PathVsIcd pathVsIcd);

	PathVsIcd findPathwayVsICDById(String id);
}
