package cn.honry.assets.supplier.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.supplier.dao.SupplierDao;
import cn.honry.assets.supplier.service.SupplierService;
import cn.honry.base.bean.model.AssetsSupplier;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * @author sgt
 *
 */
@Service("supplierService")
@Transactional
@SuppressWarnings({ "all" })
public class SupplierServiceImp implements SupplierService{
	@Autowired
	private SupplierDao supplierDao;
	public SupplierDao getSupplierDao() {
		return supplierDao;
	}
	public void setSupplierDao(SupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	@Override
	public List<AssetsSupplier> querySupplier(AssetsSupplier Supplier) {
		return supplierDao.querySupplier(Supplier);
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	@Override
	public List<AssetsSupplier> querySupplierforXR() {
		return supplierDao.querySupplierforXR();
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	@Override
	public int getSupplierCount(AssetsSupplier Supplier) {
		return supplierDao.getSupplierCount(Supplier);
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
	public void saveOrupdata(AssetsSupplier supplier) {
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(supplier.getId())){//保存
			supplier.setId(null);
			supplier.setCreateDept(deptCode);
			supplier.setCreateTime(new Date());
			supplier.setCreateUser(userAccount);
		}else{
			supplier.setUpdateTime(new Date());
			supplier.setUpdateUser(userAccount);
		}
		supplier.setDel_flg(0);
		supplier.setStop_flg(0);
		if(StringUtils.isBlank(supplier.getId())){
			supplier.setId(null);
			supplierDao.save(supplier);
			OperationUtils.getInstance().conserve(null, "供应商信息管理", "INSERT INTO", "T_ASSETS_SUPPLIER", OperationUtils.LOGACTIONINSERT);
		}else{
			supplierDao.save(supplier);
			OperationUtils.getInstance().conserve(supplier.getId(), "供应商信息管理", "UPDATE", "T_ASSETS_SUPPLIER", OperationUtils.LOGACTIONUPDATE);
		}
	}
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	@Override
	public void delete(AssetsSupplier supplier) {
		supplierDao.del(supplier.getId(),ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(supplier.getId(), "供应商信息管理", "UPDATE", "T_ASSETS_SUPPLIER", OperationUtils.LOGACTIONDELETE);
	}
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<AssetsSupplier> findAll() {
		return supplierDao.findAll();
	}
	@Override
	public void removeUnused(String id) {
		
	}
	@Override
	public AssetsSupplier get(String id) {
		return supplierDao.get(id);
	}
	@Override
	public void saveOrUpdate(AssetsSupplier entity) {
		
	}
	@Override
	public String verification(AssetsSupplier supplier) {
		String name = supplier.getName().replace(" ", "");
		List<AssetsSupplier> list;
		if(StringUtils.isNotBlank(supplier.getId())){
			list = supplierDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}else{
				if(list.size() == 1){
					if(supplier.getId().equals(list.get(0).getId())){
						return "T";
					}
				}
			}
		}else{
			list = supplierDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}
		}
		return "F";
	}

}
