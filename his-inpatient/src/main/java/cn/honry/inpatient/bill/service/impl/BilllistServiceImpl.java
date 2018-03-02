package cn.honry.inpatient.bill.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.bill.dao.BilllistDAO;
import cn.honry.inpatient.bill.service.BilllistService;
import cn.honry.utils.ShiroSessionUtils;
@Service("billlistService")
@Transactional
public class BilllistServiceImpl implements BilllistService{
	@Autowired
	private BilllistDAO billlistDAO;
	@Override
	public void removeUnused(String id) {
		billlistDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id,"摆药分类管理","UPDATE","T_DRUG_BILLLIST",OperationUtils.LOGACTIONDELETE);

	}

	@Override
	public DrugBilllist get(String id) {
		return billlistDAO.get(id);
	}
	@Override
	public void saveOrUpdate(DrugBilllist entity) {
		if(entity != null){
			if(entity.getId()!=null && entity.getId()!=""){
				entity.setUpdateUser("");
				entity.setUpdateTime(new Date());
				OperationUtils.getInstance().conserve(entity.getId(),"摆药分类管理","UPDATE","T_DRUG_BILLLIST",OperationUtils.LOGACTIONUPDATE);
			}else{
				entity.setCreateUser("");
				entity.setCreateDept("");
				entity.setCreateTime(new Date());
				OperationUtils.getInstance().conserve(null,"摆药分类管理","INSERT_INTO","T_DRUG_BILLLIST",OperationUtils.LOGACTIONINSERT);
			}
			billlistDAO.save(entity);
		}
	}

	@Override
	public List<DrugBilllist> getPage(String page, String rows,
			DrugBilllist billlistSerc) {
		return billlistDAO.getPage(page, rows, billlistSerc);
	}

	@Override
	public int getTotal(DrugBilllist billlistSerc) {
		return billlistDAO.getTotal(billlistSerc);
	}

}
