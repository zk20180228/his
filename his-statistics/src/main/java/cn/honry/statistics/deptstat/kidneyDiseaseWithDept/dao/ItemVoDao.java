package cn.honry.statistics.deptstat.kidneyDiseaseWithDept.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.internalCompare2.vo.FicDeptVO;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo.ItemVo;

public interface ItemVoDao extends EntityDao<ItemVo>{
	/**  
	 * 
	 * 通过科室查询内容
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月2日 下午8:21:57 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月2日 下午8:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public ItemVo quertItemVo(List<String> tnLs,String begin,String end,String deptCode);
	/**  
	 * 
	 * 查所有门诊、住院、病区的真实科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月6日 下午3:16:30 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月6日 下午3:16:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<FicDeptVO> queryFicDeptVO();
}
