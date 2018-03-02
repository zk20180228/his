package cn.honry.inner.technical.mat.service;

import java.util.Map;

import cn.honry.inner.technical.mat.vo.OutputInInterVO;

/**  
 *  
 * @className：MatOutPutService.java
 * @Author：luyanshou
 * @version 1.0
 *
 */
public interface MatOutPutInInterService {

	/**
	 * 物资出库(或退库)时添加出库记录、出库申请并修改库存明细表和库管表相应的数量及金额
	 */
	public Map<String,Object> addRecord(OutputInInterVO out);
	
	/**
	 * 物资出库时判断非药品是否为物资,如果是物资,判断是否库存足够
	 */
	public Map<String,Object> judgeMat(OutputInInterVO out);
}
