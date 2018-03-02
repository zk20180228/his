package cn.honry.statistics.bi.bistac.personnelInformation.service;

import java.util.List;

import cn.honry.statistics.bi.bistac.personnelInformation.vo.PersonnelInformationVo;


public interface PersonnelInformationService {
	/**  
	 * 
	 * 学历
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<PersonnelInformationVo> queryEducation(String deptCode) throws Exception;
	/**  
	 * 
	 * 职称
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<PersonnelInformationVo> queryTitle(String deptCode) throws Exception;
	/**  
	 * 
	 * 性别
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<PersonnelInformationVo> querySex(String deptCode) throws Exception;
	/**  
	 * 
	 * 年龄
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<PersonnelInformationVo> queryAge(String deptCode) throws Exception;
}
