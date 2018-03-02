package cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.dao;

import java.util.List;

import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.vo.OutpatientIndicatorsVO;

public interface OutpatientIndicatorsDao {
	/**  
	 * 
	 * <p>总挂号人数/就诊总人数（flag为true表示就诊总人数） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月7日 下午4:07:36 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月7日 下午4:07:36 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tableName 表名
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @param flag 是否是 就诊人数
	 * @param areaFlag 是否查询院区
	 * @return 
	 * @throws:
	 * @return: List<OutpatientIndicatorsVO>
	 *
	 */
	List<OutpatientIndicatorsVO> queryTotalOutpatientClinicVisits(String tableName,String sDate,String eDate,boolean flag,boolean areaFlag);
	/**  
	 * 
	 * <p> 门诊工作总时数</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月7日 下午4:12:15 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月7日 下午4:12:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tableName 表名
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @param areaFlag 是否查询院区
	 * @return
	 * @throws:
	 * @return: List<OutpatientIndicatorsVO>
	 *
	 */
	List<OutpatientIndicatorsVO> queryClinicWorkTotalTime(String tableName,String sDate,String eDate,boolean areaFlag);
	/**  
	 * 
	 * <p>总门诊收入 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月7日 下午4:15:03 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月7日 下午4:15:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tableName
	 * @param sDate
	 * @param eDate
	 * @param areaFlag 是否查询院区
	 * @return
	 * @throws:
	 * @return: List<OutpatientIndicatorsVO>
	 *
	 */
	List<OutpatientIndicatorsVO> queryTotalOutpatientIncome(String tableName,String sDate,String eDate,boolean areaFlag);
	/**  
	 * 
	 * <p>门诊入院人次（门、急诊） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月10日 下午1:53:55 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月10日 下午1:53:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tableName
	 * @param sDate
	 * @param eDate
	 * @param areaFlag 是否查询院区
	 * @return
	 * @throws:
	 * @return: List<OutpatientIndicatorsVO>
	 *
	 */
	List<OutpatientIndicatorsVO> queryTotalOutpatientAndEmergencyTime(String tableName,String sDate,String eDate,boolean areaFlag);
}
