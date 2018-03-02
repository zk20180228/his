package cn.honry.inner.material.adjustpricedetail.vo;

import java.io.Serializable;
import java.util.Date;

/**  
 *  物资调价单VO
 * @Author:luyanshou
 * @version 1.0
 */
public class AdjustInInterVo implements Serializable{

	private static final long serialVersionUID = 1L;

	/**调价明细流水号 **/
	private String adjustDetailCode;
	
	/**调价后零售价格 **/
	private Double salePrice;
	
	/**调价执行时间 **/
	private Date inureTime;
	
	/**调价单据号 **/
	private String adjustListCode;

	public String getAdjustDetailCode() {
		return adjustDetailCode;
	}

	public void setAdjustDetailCode(String adjustDetailCode) {
		this.adjustDetailCode = adjustDetailCode;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Date getInureTime() {
		return inureTime;
	}

	public void setInureTime(Date inureTime) {
		this.inureTime = inureTime;
	}

	public String getAdjustListCode() {
		return adjustListCode;
	}

	public void setAdjustListCode(String adjustListCode) {
		this.adjustListCode = adjustListCode;
	}
	
}
