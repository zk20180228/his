package cn.honry.statistics.deptstat.diseaseSurveillance.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.diseaseSurveillance.vo.DiseaseSurveillanceVo;

public interface DiseaseSurveillanceDao extends EntityDao<DiseaseSurveillanceVo>{
	/**  
	 * 
	 * 重点疾病监测汇总
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<DiseaseSurveillanceVo> queryDiseaseSurveillance( );
}
