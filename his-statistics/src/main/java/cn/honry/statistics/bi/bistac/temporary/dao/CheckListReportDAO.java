package cn.honry.statistics.bi.bistac.temporary.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.temporary.vo.CheckListReportVo;

/**  
 *  
 * @className：CheckListReportDAO
 * @Description：门诊就医-检验单
 * @Author：gaotiantian
 * @CreateDate：2017-4-10 下午2:09:12 
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-10 下午2:09:12
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface CheckListReportDAO extends EntityDao<CheckListReportVo> {
	/**  
	 *  
	 * 门诊就医-检验单
	 * @Author：gaotiantian
	 * @CreateDate：2017-4-10 下午2:09:12 
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-4-10 下午2:09:12 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<CheckListReportVo> getCheckListReport(String clinicCode,String medicalrecordId); 
}

