package cn.honry.inner.drug.outstore.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.drug.outstore.dao.OutStoreInInterDAO;
import cn.honry.inner.drug.outstore.service.OutStoreInInterService;
import cn.honry.inner.drug.stockInfo.dao.BusinessStockInfoInInterDAO;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.ShiroSessionUtils;

@SuppressWarnings({ "all" })
@Service("outstoreInInterService")
@Transactional
public class OutStoreInInterServiceImpl implements OutStoreInInterService {
	/**
	 * 出库记录表DAO
	 */
	@Autowired
	private OutStoreInInterDAO outstoreDAO;
	/**
	 * 库存维护表DAOs
	 */
	@Autowired
	private BusinessStockInfoInInterDAO businessStockInfoInInterDAO;
	/**
	 * 键值对表DAO
	 */
	@Autowired
	private KeyvalueInInterDAO keyvalueDAO;
	
	/**
	 * 公共获得库存DAO
	 */
	@Autowired
	private BusinessStockInfoInInterDAO businessStockInfoDAO;
	/**
	 * 药品信息表DAO
	 */
	@Autowired
	@Qualifier(value = "drugInfoInInerDAO")
	private DrugInfoInInerDAO drugInfoDao;

	@Override
	public void removeUnused(String id) {
	}

	@Override
	public DrugOutstore get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(DrugOutstore entity) {
	}



	@Override
	public void withholdStock(String deptId, String drugId, double applyNum, Integer minunit) {
		if("1".equals(minunit)){
			DrugInfo drugInfo=drugInfoDao.getByCode(drugId);
			applyNum *=  drugInfo.getPackagingnum();
		}
		/**以下操作是修改storage表的预扣库存**/
		//List<DrugStorage> storageList=storageDAO.checkIgnoreGroupCode(drugId, deptId);
		/*2016-04-22 
		 * 预扣库存只扣仓库主表，不扣具体批次表 liujl start
		 * if(storageList.size()>0){
			for(DrugStorage drugStorage:storageList){
				drugStorage.setPreoutSum(drugStorage.getPreoutSum()==null ? applyNum : drugStorage.getPreoutSum() + applyNum);
				drugStorage.setPreoutCost(drugStorage.getPreoutCost() == null ? applyNum*(drugStorage.getRetailPrice()==null?0:drugStorage.getRetailPrice()) : drugStorage.getPreoutCost() + applyNum*(drugStorage.getRetailPrice()==null?0:drugStorage.getRetailPrice()));
				storageDAO.save(drugStorage);
			}
		} 
		*预扣库存只扣仓库主表，不扣具体批次表 liujl end 
		*/
		/**以下操作是修改storageInfo表的预扣库存**/
		DrugStockinfo drugStockinfoCurr=businessStockInfoInInterDAO.getStockInfo(deptId,drugId);
		drugStockinfoCurr.setPreoutSum(drugStockinfoCurr.getPreoutSum()==null ? applyNum:drugStockinfoCurr.getPreoutSum()+applyNum);
		drugStockinfoCurr.setPreoutCost(drugStockinfoCurr.getPreoutCost()==null ? applyNum*(drugStockinfoCurr.getRetailPrice()==null?0:drugStockinfoCurr.getRetailPrice()):drugStockinfoCurr.getPreoutCost()+applyNum*(drugStockinfoCurr.getRetailPrice()==null?0:drugStockinfoCurr.getRetailPrice()));
		drugStockinfoCurr.setUpdateTime(new Date());
		drugStockinfoCurr.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		businessStockInfoInInterDAO.save(drugStockinfoCurr);
		OperationUtils.getInstance().conserve(drugStockinfoCurr.getId(),"修改预扣库存","UPDATE","T_DRUG_STORAGE",OperationUtils.LOGACTIONINSERT);
	}
}
