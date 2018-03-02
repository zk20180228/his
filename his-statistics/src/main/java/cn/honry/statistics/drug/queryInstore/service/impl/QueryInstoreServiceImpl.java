package cn.honry.statistics.drug.queryInstore.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugInStore;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugSupplycompany;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.statistics.drug.queryInstore.dao.QueryInstoreDao;
import cn.honry.statistics.drug.queryInstore.service.QueryInstoreService;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.RedisUtil;

@Service("queryInstoreService")
@Transactional
@SuppressWarnings({ "all" })
public class QueryInstoreServiceImpl implements QueryInstoreService{

	@Autowired
	@Qualifier(value = "queryInstoreDao")
	private QueryInstoreDao queryInstoreDao;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Override
	public DrugInStore get(String arg0) {
		return null;
	}


	@Override
	public void saveOrUpdate(DrugInStore arg0) {
		
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	/**  
	 *  
	 * @Description：  药品下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<DrugInfo> getComboboxdrug() throws Exception {
		return queryInstoreDao.getComboboxdrug();
	}

	/***
	 * 药品入库查询(统计)记录
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public List<DrugInStore> queryInstore(String Stime, String Etime,
			String drug, String page, String rows,String company,String user) throws Exception {
		return queryInstoreDao.queryInstore(Stime, Etime, drug, page, rows,company,user);
	}

	/***
	 * 药品入库查询(统计)条数
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public int queryInstoreTotle(String Stime, String Etime, String drug,String company,String user) throws Exception {
		String redKey = "YPRKCX:"+Stime+"_"+Etime+"_"+drug+"_"+company+"_"+user;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			totalNum = queryInstoreDao.queryInstoreTotle(Stime, Etime, drug,company,user);
			redisUtil.set(redKey, totalNum);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		
		return totalNum;
	}

	/**  
	 *  
	 * @Description：  生产厂家下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<DrugSupplycompany> getComboboxCompany() throws Exception {
		return queryInstoreDao.getComboboxCompany();
	}

	/**  
	 *  
	 * @Description：  人员下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<User> getComboboxUser() throws Exception {
		return queryInstoreDao.getComboboxUser();
	}

	/***
	 * 药品入库查询(统计)记录--导出用
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public List<DrugInStore> queryInstoreExp(String Stime, String Etime,
			String drug,String company,String user) throws Exception {
		return queryInstoreDao.queryInstoreExp(Stime, Etime, drug,company,user);
	}
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	**/
	@Override
	public FileUtil export(List<DrugInStore> list, FileUtil fUtil) throws Exception {
		Map<String, String> map = queryInstoreDao.getCompanyName();
		if (map != null) {
			for (DrugInStore model : list) {
				String record="";
				record = CommonStringUtils.trimToEmpty(model.getTradeName()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getSpecs()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getPackUnit()) + ",";
				record += model.getInNum() + ",";
				record += model.getRetailPrice() + ",";
				record += model.getPurchasePrice() + ",";
				record += model.getPurchaseCost() + ",";
				record += CommonStringUtils.trimToEmpty(map.get(model.getCompanyCode())) + ",";
				record += CommonStringUtils.trimToEmpty(model.getBatchNo()) + ",";
				record += model.getValidDate() + ",";
				record += CommonStringUtils.trimToEmpty(model.getInvoiceNo()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getDeliveryNo());
				try {
					fUtil.write(record);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			for (DrugInStore model : list) {
				String record="";
					record = CommonStringUtils.trimToEmpty(model.getTradeName()) + ",";
					record += CommonStringUtils.trimToEmpty(model.getSpecs()) + ",";
					record += CommonStringUtils.trimToEmpty(model.getPackUnit()) + ",";
					record += model.getInNum() + ",";
					record += model.getRetailPrice() + ",";
					record += model.getPurchasePrice() + ",";
					record += model.getPurchaseCost() + ",";
					record += CommonStringUtils.trimToEmpty("") + ",";
					record += CommonStringUtils.trimToEmpty(model.getBatchNo()) + ",";
					record += model.getValidDate() + ",";
					record += CommonStringUtils.trimToEmpty(model.getInvoiceNo()) + ",";
					record += CommonStringUtils.trimToEmpty(model.getDeliveryNo());
					try {
						fUtil.write(record);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
		return fUtil;
	}

	@Override
	public Map<String, String> getCompanyName() throws Exception {
		return queryInstoreDao.getCompanyName();
	}
}
