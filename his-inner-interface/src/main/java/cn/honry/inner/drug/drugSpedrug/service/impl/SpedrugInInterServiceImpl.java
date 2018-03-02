package cn.honry.inner.drug.drugSpedrug.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugSpedrug;
import cn.honry.inner.drug.drugSpedrug.dao.SpedrugInInterDAO;
import cn.honry.inner.drug.drugSpedrug.service.SpedrugInInterService;

/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("spedrugInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class SpedrugInInterServiceImpl implements SpedrugInInterService{

	@Autowired
	@Qualifier(value = "spedrugInInterDAO")
	private SpedrugInInterDAO spedrugInInterDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public DrugSpedrug get(String id) {
		return spedrugInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(DrugSpedrug entity) {
		
	}

	

}
