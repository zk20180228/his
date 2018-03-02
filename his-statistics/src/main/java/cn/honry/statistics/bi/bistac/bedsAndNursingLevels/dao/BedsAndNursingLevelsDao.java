package cn.honry.statistics.bi.bistac.bedsAndNursingLevels.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.vo.BedsAndNursingLevelsVo;

public interface BedsAndNursingLevelsDao extends EntityDao<BedsAndNursingLevelsVo>{
	/**  
	 * 
	 * 床位使用情况统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年5月23日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年5月23日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<BedsAndNursingLevelsVo> queryBeds( ) throws Exception;
	/**  
	 * 
	 * 护理级别情况统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年5月23日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年5月23日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	public List<BedsAndNursingLevelsVo> queryNursingLevels() throws Exception;
}
