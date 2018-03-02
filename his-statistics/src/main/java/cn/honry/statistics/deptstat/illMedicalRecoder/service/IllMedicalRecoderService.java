package cn.honry.statistics.deptstat.illMedicalRecoder.service;

import java.util.List;

import cn.honry.statistics.deptstat.illMedicalRecoder.vo.IllMedicalRecoderVo;


public interface IllMedicalRecoderService {

	//危重病历人数比例统计分析
	List<IllMedicalRecoderVo> queryIllMedicalRecoder(String deptCode,String menuAlias);
	/**
	 * 危重病历人数比例统计分析 预处理
	 * @param tnL 住院主表分区表
	 * @param mainL 诊断表
	 * @param begin
	 * @param end
	 */
	public void saveIllMedicalToDB(String begin,String end);
	
	List<IllMedicalRecoderVo> queryIllMedical(String startTime,String endTime,String deptCode,String menuAlias);
}
