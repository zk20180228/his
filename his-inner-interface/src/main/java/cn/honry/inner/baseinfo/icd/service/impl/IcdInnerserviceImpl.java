package cn.honry.inner.baseinfo.icd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessIcd;
import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.inner.baseinfo.icd.dao.IcdInInterDAO;
import cn.honry.inner.baseinfo.icd.service.IcdInnerService;
import cn.honry.utils.RedisUtil;
@Service("icdInnerService")
@Transactional
@SuppressWarnings({"all"})
public class IcdInnerserviceImpl implements IcdInnerService{
	
	@Resource
	private RedisUtil redis;
	
	@Autowired
	@Qualifier(value="icdInInterDAO")
	private IcdInInterDAO icdInInterDAO;

	@Override
	public BusinessIcd get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(BusinessIcd arg0) {
		
	}
	
	/**  
	 *  
	 * @Description：icd下拉框的方法
	 * @Author：zhangjin
	 * @CreateDate：2016-7-22  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessIcd> getCombobox(String ids) {
		return icdInInterDAO.getCombobox(ids);
	}
	
	/**
	 * 根据code获取Icd10
	 * @param code
	 * @return
	 */
	public BusinessIcd10 getIcd10ByCode(String code){
		if(StringUtils.isBlank(code)){
			return null;
		}
		BusinessIcd10 icd=null;
		try {
			icd = (BusinessIcd10) redis.hget("Icd10", code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(icd!=null){
			return icd;
		}else{
			icd = icdInInterDAO.getIcd10ByCode(code);
			try {
				if(icd!=null){
					redis.hset("Icd10", code, icd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return icd;
	}
	
	/**
	 * 获取Icd10数据
	 * @return
	 */
	public List<BusinessIcd10> getICD10Info(){
		List<BusinessIcd10> list=null;
		try {
			list = (List<BusinessIcd10>) redis.get("Icd10_queryAll");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list!=null){
			return list;
		}
		list=icdInInterDAO.getICD10Info(null);
		try {
			redis.set("Icd10_queryAll", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 及时查询获取Icd10数据
	 * @param q
	 * @return
	 */
	public List<BusinessIcd10> getIcd10ByQ(String q){
		List<BusinessIcd10> list=null;
		try {
			if(StringUtils.isBlank(q)){
				list = (List<BusinessIcd10>) redis.get("Icd10_queryAll");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list==null){
			list= icdInInterDAO.getICD10Info(q);
		}
		return list;
	}

}
