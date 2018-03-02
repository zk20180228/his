package cn.honry.statistics.bi.bistac.imStacData.service;


public interface InMonthTableService {
	/**  
	 * 
	 * 将处方明细表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月23日 下午8:14:30 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月23日 下午8:14:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableDataM(String begin,String end );
	/**  
	 * 
	 * 将门诊用药天数表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月24日 上午10:50:15 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月24日 上午10:50:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableDataM_YYTS(String begin,String end );
	/**  
	 * 
	 * 将医生用药金额表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月24日 上午11:44:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月24日 上午11:44:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableDataM_YSYYJE(String begin,String end );
	/**  
	 * 
	 * 将科室用药金额表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月24日 下午12:41:47 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月24日 下午12:41:47 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableDataM_KSYYJE(String begin,String end );
	/**  
	 * 
	 * 将门诊月药品金额，用药数量，人次表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月24日 下午2:24:59 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月24日 下午2:24:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableDataM_YPJE2(String begin,String end );
	/**  
	 * 
	 * 将住院的药品和非药品表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月27日 下午4:18:17 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月27日 下午4:18:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableDataM_DRUGANDNO(String begin,String end );
	/**  
	 * 
	 * 科室对比表（KSDBB）导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月6日 下午3:54:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月6日 下午3:54:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableData_KSDBB(String begin,String end );
}
