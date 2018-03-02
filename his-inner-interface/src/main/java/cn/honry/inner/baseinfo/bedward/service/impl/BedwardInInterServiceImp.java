package cn.honry.inner.baseinfo.bedward.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.inner.baseinfo.bedward.dao.BedwardInInterDAO;
import cn.honry.inner.baseinfo.bedward.service.BedwardInInterService;
@Service("bedwardInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class BedwardInInterServiceImp implements BedwardInInterService{
	private Logger logger=Logger.getLogger(BedwardInInterServiceImp.class);
	
	@Autowired
	@Qualifier(value = "bedwardInInterDAO")
	private BedwardInInterDAO bedwardDAO;
	
	
	
	/**
	 * 根据ID查询病房信息
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 * @param id
	 * @return
	 */
	@Override
	public BusinessBedward getBedwardById(String id) {
		return bedwardDAO.getBedwardById(id);
	}


	@Override
	public BusinessBedward get(String arg0) {
		return null;
	}


	@Override
	public void removeUnused(String arg0) {
		
	}


	@Override
	public void saveOrUpdate(BusinessBedward arg0) {
		
	}
	
	

}
