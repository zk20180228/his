package cn.honry.inner.baseinfo.frequency.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.inner.baseinfo.frequency.dao.FrequencyInInterDAO;
import cn.honry.inner.baseinfo.frequency.service.FrequencyInInterService;
@Service("frequencyInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class FrequencyInInterServiceImpl implements FrequencyInInterService{
	@Autowired
	@Qualifier(value = "frequencyInInterDAO")
	private FrequencyInInterDAO  frequencyInInterDAOInfo;

	@Override
	public BusinessFrequency get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(BusinessFrequency arg0) {
		
	}

	@Override
	public List<BusinessFrequency> queryFrequencyList() {
		return frequencyInInterDAOInfo.queryFrequencyList();
	}

	/**  
	 * 
	 * <p> 根据编码及开立科室获取频次信息 </p>
	 * <p> 如果该科室下无对应编码的信息则返回全院频次对应编码的频次信息 </p>
	 * <p> 如果以上都不存在则返回null </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月27日 上午10:31:32 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月27日 上午10:31:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public BusinessFrequency getCodeAndDept(String frequencyCode,String deptCode) {
		return frequencyInInterDAOInfo.getCodeAndDept(frequencyCode,deptCode);
	}

}
