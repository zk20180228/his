package cn.honry.inner.technical.mat.dao;

import java.util.List;

import cn.honry.base.bean.model.MatApply;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.bean.model.MatStockInfo;
import cn.honry.base.bean.model.MatStockdetail;

/**  
 *  
 * @className：MatApplyDAO.java
 * @Author：luyanshou
 * @version 1.0
 *
 */
public interface MatOutPutInInterDAO {

	/**
	 * 根据住院流水号查询患者Id
	 */
	public String getPatientID(String inpatientNo);
	
	/**
	 * 根据非药品编号查询物资编号
	 */
	public String getItemCode(String undrugItemCode);
	
	/**
	 * 根据门诊号查询患者编号
	 */
	public String getPatientIdByNo(String no);
	
	/**
	 * 根据查询条件查询出库记录
	 */
	public List<MatOutput> getMatOutput(MatOutput t1,MatOutput t2);
	
	/**
	 * 根据物资编码查询当前库存总数
	 */
	public Integer getSum(String itemCode,String storageCode);
	
	/**
	 * 根据物资编码查询当前库存预扣库存 
	 * @param itemCode
	 * @param storageCode
	 * @return
	 */
	public Integer getpreoutSum(String itemCode,String storageCode);
	
	/**
	 * 根据物资编码和仓库查询库存明细
	 */
	public List<MatStockdetail> getList(String itemCode,String storageCode);
	
	/**
	 * 添加出库申请
	 */
	public void add(MatOutput m);
	
	/**
	 * 添加申请
	 */
	public void add(MatApply m);
	
	/**
	 *返回相应的序列,用于生成 申请单号、申请流水号、出库流水号、出库单号等
	 * @param seq
	 * @return
	 */
	public String getMaxNo(String seq);
	
	/**
	 * 查询库存明细
	 */
	public MatStockdetail getMatStockdetail(MatStockdetail t);
	
	/**
	 * 查询物资库管表记录
	 */
	public MatStockInfo getMatStockInfo(MatStockInfo t);
	
	/**
	 * 更新库存明细表记录
	 */
	public void updateStockdetail(MatStockdetail t);
	/**
	 * 更新库管表记录
	 */
	public void updateStockInfo(MatStockInfo t);
}
