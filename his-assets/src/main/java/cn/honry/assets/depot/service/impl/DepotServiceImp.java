package cn.honry.assets.depot.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.depot.dao.DepotDao;
import cn.honry.assets.depot.service.DepotService;
import cn.honry.base.bean.model.AssetsDepot;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * @author sgt
 *
 */
@Service("depotService")
@Transactional
@SuppressWarnings({ "all" })
public class DepotServiceImp implements DepotService{
	@Autowired
	private DepotDao depotDao;
	public DepotDao getDepotDao() {
		return depotDao;
	}
	public void setDepotDao(DepotDao depotDao) {
		this.depotDao = depotDao;
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	@Override
	public List<AssetsDepot> queryDepot(AssetsDepot Depot) {
		return depotDao.queryDepot(Depot);
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	@Override
	public List<AssetsDepot> queryDepotforXR() {
		return depotDao.queryDepotforXR();
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	@Override
	public int getDepotCount(AssetsDepot Depot) {
		return depotDao.getDepotCount(Depot);
	}
	/**
	 *  
	 * @Description：  增加数据&修改数据
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOrupdata(AssetsDepot depot) {
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(depot.getId())){//保存
			depot.setId(null);
			depot.setCreateDept(deptCode);
			depot.setCreateTime(new Date());
			depot.setCreateUser(userAccount);
		}else{
			depot.setUpdateTime(new Date());
			depot.setUpdateUser(userAccount);
		}
		depot.setDel_flg(0);
//		depot.setStop_flg(0);
		if(StringUtils.isBlank(depot.getId())){
			depot.setId(null);
			depotDao.save(depot);
			OperationUtils.getInstance().conserve(null, "仓库维护", "INSERT INTO", "T_ASSETS_DEPOT", OperationUtils.LOGACTIONINSERT);
		}else{
			depotDao.save(depot);
			OperationUtils.getInstance().conserve(depot.getId(), "仓库维护", "UPDATE", "T_ASSETS_DEPOT", OperationUtils.LOGACTIONUPDATE);
		}
	}
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	@Override
	public void delete(AssetsDepot depot) {
		depotDao.del(depot.getId(),ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(depot.getId(), "仓库维护", "UPDATE", "T_ASSETS_DEPOT", OperationUtils.LOGACTIONDELETE);
	}
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<AssetsDepot> findAll() {
		return depotDao.findAll();
	}
	@Override
	public void removeUnused(String id) {
		
	}
	@Override
	public AssetsDepot get(String id) {
		return depotDao.get(id);
	}
	@Override
	public void saveOrUpdate(AssetsDepot entity) {
		
	}
	@Override
	public String verification(AssetsDepot depot) {
		String name = depot.getDepotName().replace(" ", "");
		List<AssetsDepot> list;
		if(StringUtils.isNotBlank(depot.getId())){
			list = depotDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}else{
				if(list.size() == 1){
					if(depot.getId().equals(list.get(0).getId())){
						return "T";
					}
				}
			}
		}else{
			list = depotDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}
		}
		return "F";
	}
	@Override
	public void disableDepot(AssetsDepot depot) {
		String id=depot.getId();
		String[] ids=id.split(",");
		for(int i=0;i<ids.length;i++){
			depotDao.disableDepot(ids[i]);
		}
		OperationUtils.getInstance().conserve(depot.getId(), "仓库维护", "UPDATE", "T_ASSETS_DEPOT", OperationUtils.LOGACTIONUPDATE);
	}
	@Override
	public void enableDepot(AssetsDepot depot) {
		String id=depot.getId();
		String[] ids=id.split(",");
		for(int i=0;i<ids.length;i++){
			depotDao.enableDepot(ids[i]);
		}
		OperationUtils.getInstance().conserve(depot.getId(), "仓库维护", "UPDATE", "T_ASSETS_DEPOT", OperationUtils.LOGACTIONUPDATE);
	}

}
