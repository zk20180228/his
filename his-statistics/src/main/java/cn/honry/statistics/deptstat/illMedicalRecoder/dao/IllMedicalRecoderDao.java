package cn.honry.statistics.deptstat.illMedicalRecoder.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.illMedicalRecoder.vo.IllMedicalRecoderVo;

public interface IllMedicalRecoderDao extends EntityDao<IllMedicalRecoderVo>{
	/**  
	 * 
	 * 危重病历人数比例统计分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<IllMedicalRecoderVo> queryIllMedicalRecoder(String deptCode,String menuAlias);
	/**  
	 * 
	 * 危重病历人数比例统计分析 从mongoDB查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<IllMedicalRecoderVo> queryIllMedicalRecoderForDB(String deptCode,String menuAlias);
	/**
	 * 危重病历人数比例统计分析 预处理
	 * @param tnL 住院主表分区表
	 * @param mainL 诊断表
	 * @param begin
	 * @param end
	 */
	public void saveIllMedicalToDB(List<String> tnL,List<String> mainL,String begin,String end);
	
	/**  
	 * 
	 * 危重病历人数比例统计分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tnL 
	 *
	 */
	public List<IllMedicalRecoderVo> queryIllMedical(List<String> tnL, String startTime,String endTime,String deptCode,String menuAlias);
}
