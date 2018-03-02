package cn.honry.statistics.bi.bistac.bedsAndNursingLevels.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.dao.BedsAndNursingLevelsDao;
import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.service.BedsAndNursingLevelsService;
import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.vo.BedsAndNursingLevelsVo;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo.ListTotalIncomeStaticVo;

@Service("bedsAndNursingLevelsService")
@Transactional
@SuppressWarnings({"all"})
public class BedsAndNursingLevelsServiceImpl implements BedsAndNursingLevelsService{
	@Autowired
	@Qualifier(value = "bedsAndNursingLevelsDao")
	private BedsAndNursingLevelsDao bedsAndNursingLevelsDao;
	/**  
	 * 
	 * 床位使用情况统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年5月23日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年5月23日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<BedsAndNursingLevelsVo> queryBeds( ) throws Exception {
		return bedsAndNursingLevelsDao.queryBeds();
	}
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
	@Override
	public List<BedsAndNursingLevelsVo> queryNursingLevels() throws Exception {
		return bedsAndNursingLevelsDao.queryNursingLevels();
	}
}
