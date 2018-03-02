package cn.honry.statistics.bi.bistac.personnelInformation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.bi.bistac.personnelInformation.dao.PersonnelInformationDAO;
import cn.honry.statistics.bi.bistac.personnelInformation.service.PersonnelInformationService;
import cn.honry.statistics.bi.bistac.personnelInformation.vo.PersonnelInformationVo;
@Service("personnelInformationService")
@Transactional
@SuppressWarnings({"all"})
public class personnelInformationServiceImpl implements PersonnelInformationService{
	@Autowired
	@Qualifier(value = "personnelInformationDAO")
	private PersonnelInformationDAO personnelInformationDAO;
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
	@Override
	public List<PersonnelInformationVo> queryEducation(String deptCode) throws Exception {
		return personnelInformationDAO.queryEducation(deptCode);
	}
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
	@Override
	public List<PersonnelInformationVo> queryTitle(String deptCode) throws Exception {
		return personnelInformationDAO.queryTitle(deptCode);
	}
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
	@Override
	public List<PersonnelInformationVo> querySex(String deptCode) throws Exception {
		return personnelInformationDAO.querySex(deptCode);
	}
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
	@Override
	public List<PersonnelInformationVo> queryAge(String deptCode) throws Exception {
		return personnelInformationDAO.queryAge(deptCode);
	}

}
