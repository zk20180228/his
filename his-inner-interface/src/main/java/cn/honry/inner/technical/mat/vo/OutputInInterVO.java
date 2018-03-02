package cn.honry.inner.technical.mat.vo;

/**  
 *  
 * @className：OutputVO.java
 * @Author：luyanshou
 * @version 1.0
 *
 */
public class OutputInInterVO {

	private String storageCode;//执行科室(仓库)编码
	private String inpatientNo;//住院流水号(或门诊号)
	private String undrugItemCode;//非药品Id
	private String recipeNo;//处方号
	private Integer sequenceNo;//处方内流水号
	private Double applyNum;//申请数量
	private Double useNum;//执行数量(出库时,执行数量与申请数量相等;退库时,执行数量为患者使用的数量)
	private Integer transType;//交易类型
	private String flag;//类型("C"-住院,"I"-门诊)
	private String outListCode;//更新的出库流水号
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getUndrugItemCode() {
		return undrugItemCode;
	}
	public void setUndrugItemCode(String undrugItemCode) {
		this.undrugItemCode = undrugItemCode;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public Double getApplyNum() {
		return applyNum;
	}
	public void setApplyNum(Double applyNum) {
		this.applyNum = applyNum;
	}
	public Double getUseNum() {
		return useNum;
	}
	public void setUseNum(Double useNum) {
		this.useNum = useNum;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getOutListCode() {
		return outListCode;
	}
	public void setOutListCode(String outListCode) {
		this.outListCode = outListCode;
	}
	
}
