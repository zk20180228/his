package cn.honry.statistics.deptstat.kidneyDiseaseWithDept.service;


import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo.ItemVo;

public interface ItemVoService {
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
	public ItemVo quertItemVo(String date,String deptCode);
	/**  
	 * 
	 * 从mongo通过科室查询内容
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月6日 下午4:20:06 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月6日 下午4:20:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public ItemVo itemVos(String date,String deptCode);
	/**  
	 * 
	 * 初始化数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年9月8日 下午2:35:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年9月8日 下午2:35:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public void init_SBKSDBB(String begin,String end,Integer type) throws Exception;
}
