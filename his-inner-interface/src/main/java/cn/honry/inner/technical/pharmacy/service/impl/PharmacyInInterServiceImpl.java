package cn.honry.inner.technical.pharmacy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.StoTerminal;
import cn.honry.inner.technical.pharmacy.dao.PharmacyInInterDao;
import cn.honry.inner.technical.pharmacy.service.PharmacyInInterService;

/***
 * @Description:门诊终端维护Service实现
 * @author  wfj
 * @date 创建时间：2016年1月23日
 * @version 1.0
 * @parameter 
 * @since
 */
@Service("pharmacyInInterService")
@Transactional
@SuppressWarnings({"all"})
public class PharmacyInInterServiceImpl implements PharmacyInInterService {
	
	/** 注入本类sevice **/
	@Autowired
	@Qualifier(value="pharmacyInInterDao")
	private PharmacyInInterDao pharmacyInInterDao;
	
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public StoTerminal get(String id) {
		return pharmacyInInterDao.get(id);
	}


	
	@Override
	public List<StoTerminal> queryTerminalList(String type,String deptCode,StoTerminal stoTerminal,String page,String rows) {
		return pharmacyInInterDao.queryTerminalList(type,deptCode,stoTerminal,page,rows);
	}

	@Override
	public void saveOrUpdate(StoTerminal entity) {
		
	}
	
	@Override
	public int queryTerminalCount(String type,String deptCode,StoTerminal stoTerminal) {
		return pharmacyInInterDao.queryTerminalCount(type,deptCode,stoTerminal);
	}



}
