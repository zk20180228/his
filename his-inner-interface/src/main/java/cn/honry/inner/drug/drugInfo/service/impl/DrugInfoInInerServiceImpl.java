package cn.honry.inner.drug.drugInfo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugInfo;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.drug.drugInfo.service.DrugInfoInInerService;
import cn.honry.utils.RedisUtil;
@Service("drugInfoInInerService")
@Transactional
@SuppressWarnings({"all"})
public class DrugInfoInInerServiceImpl implements DrugInfoInInerService {
	
	/**
	 * 缓存类
	 */
	@Resource
	private RedisUtil redis;
	
	@Autowired
	@Qualifier(value="drugInfoInInerDAO")
	private DrugInfoInInerDAO drugInfoInInerDAO;
	@Override
	public DrugInfo get(String id) {
		return drugInfoInInerDAO.get(id);
	}

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public void saveOrUpdate(DrugInfo id) {
		
	}
	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-10-29 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-29上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotal(DrugInfo drug) {
		try {
			if(drug==null){
				Long zcard = redis.zcard("drug-ids");
				if(zcard!=null){
					return zcard.intValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drugInfoInInerDAO.getTotal(drug);
	}
	/**  
	 *  
	 * @Description：  分页查询－获得列表数据
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-12-2上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugInfo> getPage(String page, String rows, DrugInfo drug) {
		try {
			if(drug==null){
				Integer j = Integer.valueOf(rows);
				Integer i=(Integer.valueOf(page)-1)*j;
				List<DrugInfo> list = redis.getDrugInfo(null, i, j);
				if(list.size()>0){
					return list;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return drugInfoInInerDAO.getPage(page,rows,drug);
	}

	@Override
	public String companyCode(String drugid) {
		String packagingunit = drugInfoInInerDAO.companyCode(drugid);
		return packagingunit;
	}
	/**
	 * 药品渲染
	 */
	public List<DrugInfo> getDrugCodeAndName(){
		return drugInfoInInerDAO.getDrugCodeAndName();
	}
	


}
