package cn.honry.base.bean.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * @Description  院长查询综合日报
 * @author    donghe
 * @CreateDate 2017-08-05
 * @version   1.0
 */
public class BusinessYzcxzhcx implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**平诊人次**/
	private BigDecimal pzNum;
	/**简易门诊人次**/
	private BigDecimal jymzNum;
	/**其他普通人次**/
	private BigDecimal qtptNum;
	/**知名专家人次**/
	private BigDecimal zmzjNum;
	/**教授人次**/
	private BigDecimal jsNum;
	/**副教授人次**/
	private BigDecimal fjsNum;
	/**急诊人次**/
	private BigDecimal jzNum;
	/**收费人次合计**/
	private BigDecimal sfhjNum;
	/**收费自费人次**/
	private BigDecimal sfzfNum;
	/**处方合计张数**/
	private BigDecimal cfhjNum;
	/**处方自费张数**/
	private BigDecimal cfzfNum;
	/**最大处方金额**/
	private BigDecimal zdcfCost;
	/**最小处方金额**/
	private BigDecimal zxcfCost;
	/**处方合计金额**/
	private BigDecimal cfhjCost;
	/**入院人次**/
	private BigDecimal ryNum;
	/**转科人次**/
	private BigDecimal zkNum;
	/**出院未结人次**/
	private BigDecimal cywjNum;
	/**出院已结人次**/
	private BigDecimal cyyjNum;
	/**在院合计人次**/
	private BigDecimal zyhjNum;
	/**在院自费人次**/
	private BigDecimal zyzfNum;
	/**在院农合人次**/
	private BigDecimal zynhNum;
	/**门诊药品收入**/
	private BigDecimal mzypCost;
	/**门诊医疗收入**/
	private BigDecimal mzylCost;
	/**住院药品收入**/
	private BigDecimal zyypCost;
	/**住院医疗收入**/
	private BigDecimal zyylCost;
	/**门诊手术台数**/
	private BigDecimal mzssNum;
	/**门诊手术金额**/
	private BigDecimal mzssCost;
	/**住院手术台数**/
	private BigDecimal zyssNum;
	/**住院手术金额**/
	private BigDecimal zyssCost;
	/**操作时间**/
	private Date operDate;
	/**编制床位数**/
	private BigDecimal bedBz;
	/**实际入院人次**/
	private BigDecimal sjryNum;
	/**挂号收入**/
	private BigDecimal ghsrCost;
	/**临时特殊挂号人次**/
	private BigDecimal tshNum;
	/**挂号合计人次**/
	private BigDecimal ghhjRs;
	/**0整体 2郑东 3惠济 0-2-3=河医院区**/
	private String yq;
	/**废弃--挂号合计人次**/
	private BigDecimal ghhjNum;
	/**手术台数合计**/
	private BigDecimal ssts;
	public BigDecimal getPzNum() {
		return pzNum;
	}
	public void setPzNum(BigDecimal pzNum) {
		this.pzNum = pzNum;
	}
	public BigDecimal getJymzNum() {
		return jymzNum;
	}
	public void setJymzNum(BigDecimal jymzNum) {
		this.jymzNum = jymzNum;
	}
	public BigDecimal getQtptNum() {
		return qtptNum;
	}
	public void setQtptNum(BigDecimal qtptNum) {
		this.qtptNum = qtptNum;
	}
	public BigDecimal getZmzjNum() {
		return zmzjNum;
	}
	public void setZmzjNum(BigDecimal zmzjNum) {
		this.zmzjNum = zmzjNum;
	}
	public BigDecimal getJsNum() {
		return jsNum;
	}
	public void setJsNum(BigDecimal jsNum) {
		this.jsNum = jsNum;
	}
	public BigDecimal getFjsNum() {
		return fjsNum;
	}
	public void setFjsNum(BigDecimal fjsNum) {
		this.fjsNum = fjsNum;
	}
	public BigDecimal getJzNum() {
		return jzNum;
	}
	public void setJzNum(BigDecimal jzNum) {
		this.jzNum = jzNum;
	}
	public BigDecimal getSfhjNum() {
		return sfhjNum;
	}
	public void setSfhjNum(BigDecimal sfhjNum) {
		this.sfhjNum = sfhjNum;
	}
	public BigDecimal getSfzfNum() {
		return sfzfNum;
	}
	public void setSfzfNum(BigDecimal sfzfNum) {
		this.sfzfNum = sfzfNum;
	}
	public BigDecimal getCfhjNum() {
		return cfhjNum;
	}
	public void setCfhjNum(BigDecimal cfhjNum) {
		this.cfhjNum = cfhjNum;
	}
	public BigDecimal getCfzfNum() {
		return cfzfNum;
	}
	public void setCfzfNum(BigDecimal cfzfNum) {
		this.cfzfNum = cfzfNum;
	}
	public BigDecimal getZdcfCost() {
		return zdcfCost;
	}
	public void setZdcfCost(BigDecimal zdcfCost) {
		this.zdcfCost = zdcfCost;
	}
	public BigDecimal getZxcfCost() {
		return zxcfCost;
	}
	public void setZxcfCost(BigDecimal zxcfCost) {
		this.zxcfCost = zxcfCost;
	}
	public BigDecimal getCfhjCost() {
		return cfhjCost;
	}
	public void setCfhjCost(BigDecimal cfhjCost) {
		this.cfhjCost = cfhjCost;
	}
	public BigDecimal getRyNum() {
		return ryNum;
	}
	public void setRyNum(BigDecimal ryNum) {
		this.ryNum = ryNum;
	}
	public BigDecimal getZkNum() {
		return zkNum;
	}
	public void setZkNum(BigDecimal zkNum) {
		this.zkNum = zkNum;
	}
	public BigDecimal getCywjNum() {
		return cywjNum;
	}
	public void setCywjNum(BigDecimal cywjNum) {
		this.cywjNum = cywjNum;
	}
	public BigDecimal getCyyjNum() {
		return cyyjNum;
	}
	public void setCyyjNum(BigDecimal cyyjNum) {
		this.cyyjNum = cyyjNum;
	}
	public BigDecimal getZyhjNum() {
		return zyhjNum;
	}
	public void setZyhjNum(BigDecimal zyhjNum) {
		this.zyhjNum = zyhjNum;
	}
	public BigDecimal getZyzfNum() {
		return zyzfNum;
	}
	public void setZyzfNum(BigDecimal zyzfNum) {
		this.zyzfNum = zyzfNum;
	}
	public BigDecimal getZynhNum() {
		return zynhNum;
	}
	public void setZynhNum(BigDecimal zynhNum) {
		this.zynhNum = zynhNum;
	}
	public BigDecimal getMzypCost() {
		return mzypCost;
	}
	public void setMzypCost(BigDecimal mzypCost) {
		this.mzypCost = mzypCost;
	}
	public BigDecimal getMzylCost() {
		return mzylCost;
	}
	public void setMzylCost(BigDecimal mzylCost) {
		this.mzylCost = mzylCost;
	}
	public BigDecimal getZyypCost() {
		return zyypCost;
	}
	public void setZyypCost(BigDecimal zyypCost) {
		this.zyypCost = zyypCost;
	}
	public BigDecimal getZyylCost() {
		return zyylCost;
	}
	public void setZyylCost(BigDecimal zyylCost) {
		this.zyylCost = zyylCost;
	}
	public BigDecimal getMzssNum() {
		return mzssNum;
	}
	public void setMzssNum(BigDecimal mzssNum) {
		this.mzssNum = mzssNum;
	}
	public BigDecimal getMzssCost() {
		return mzssCost;
	}
	public void setMzssCost(BigDecimal mzssCost) {
		this.mzssCost = mzssCost;
	}
	public BigDecimal getZyssNum() {
		return zyssNum;
	}
	public void setZyssNum(BigDecimal zyssNum) {
		this.zyssNum = zyssNum;
	}
	public BigDecimal getZyssCost() {
		return zyssCost;
	}
	public void setZyssCost(BigDecimal zyssCost) {
		this.zyssCost = zyssCost;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public BigDecimal getBedBz() {
		return bedBz;
	}
	public void setBedBz(BigDecimal bedBz) {
		this.bedBz = bedBz;
	}
	public BigDecimal getSjryNum() {
		return sjryNum;
	}
	public void setSjryNum(BigDecimal sjryNum) {
		this.sjryNum = sjryNum;
	}
	public BigDecimal getGhsrCost() {
		return ghsrCost;
	}
	public void setGhsrCost(BigDecimal ghsrCost) {
		this.ghsrCost = ghsrCost;
	}
	public BigDecimal getTshNum() {
		return tshNum;
	}
	public void setTshNum(BigDecimal tshNum) {
		this.tshNum = tshNum;
	}
	public BigDecimal getGhhjRs() {
		return ghhjRs;
	}
	public void setGhhjRs(BigDecimal ghhjRs) {
		this.ghhjRs = ghhjRs;
	}
	public String getYq() {
		return yq;
	}
	public void setYq(String yq) {
		this.yq = yq;
	}
	public BigDecimal getGhhjNum() {
		return ghhjNum;
	}
	public void setGhhjNum(BigDecimal ghhjNum) {
		this.ghhjNum = ghhjNum;
	}
	public BigDecimal getSsts() {
		return ssts;
	}
	public void setSsts(BigDecimal ssts) {
		this.ssts = ssts;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bedBz == null) ? 0 : bedBz.hashCode());
		result = prime * result
				+ ((cfhjCost == null) ? 0 : cfhjCost.hashCode());
		result = prime * result + ((cfhjNum == null) ? 0 : cfhjNum.hashCode());
		result = prime * result + ((cfzfNum == null) ? 0 : cfzfNum.hashCode());
		result = prime * result + ((cywjNum == null) ? 0 : cywjNum.hashCode());
		result = prime * result + ((cyyjNum == null) ? 0 : cyyjNum.hashCode());
		result = prime * result + ((fjsNum == null) ? 0 : fjsNum.hashCode());
		result = prime * result + ((ghhjNum == null) ? 0 : ghhjNum.hashCode());
		result = prime * result + ((ghhjRs == null) ? 0 : ghhjRs.hashCode());
		result = prime * result
				+ ((ghsrCost == null) ? 0 : ghsrCost.hashCode());
		result = prime * result + ((jsNum == null) ? 0 : jsNum.hashCode());
		result = prime * result + ((jymzNum == null) ? 0 : jymzNum.hashCode());
		result = prime * result + ((jzNum == null) ? 0 : jzNum.hashCode());
		result = prime * result
				+ ((mzssCost == null) ? 0 : mzssCost.hashCode());
		result = prime * result + ((mzssNum == null) ? 0 : mzssNum.hashCode());
		result = prime * result
				+ ((mzylCost == null) ? 0 : mzylCost.hashCode());
		result = prime * result
				+ ((mzypCost == null) ? 0 : mzypCost.hashCode());
		result = prime * result
				+ ((operDate == null) ? 0 : operDate.hashCode());
		result = prime * result + ((pzNum == null) ? 0 : pzNum.hashCode());
		result = prime * result + ((qtptNum == null) ? 0 : qtptNum.hashCode());
		result = prime * result + ((ryNum == null) ? 0 : ryNum.hashCode());
		result = prime * result + ((sfhjNum == null) ? 0 : sfhjNum.hashCode());
		result = prime * result + ((sfzfNum == null) ? 0 : sfzfNum.hashCode());
		result = prime * result + ((sjryNum == null) ? 0 : sjryNum.hashCode());
		result = prime * result + ((ssts == null) ? 0 : ssts.hashCode());
		result = prime * result + ((tshNum == null) ? 0 : tshNum.hashCode());
		result = prime * result + ((yq == null) ? 0 : yq.hashCode());
		result = prime * result
				+ ((zdcfCost == null) ? 0 : zdcfCost.hashCode());
		result = prime * result + ((zkNum == null) ? 0 : zkNum.hashCode());
		result = prime * result + ((zmzjNum == null) ? 0 : zmzjNum.hashCode());
		result = prime * result
				+ ((zxcfCost == null) ? 0 : zxcfCost.hashCode());
		result = prime * result + ((zyhjNum == null) ? 0 : zyhjNum.hashCode());
		result = prime * result + ((zynhNum == null) ? 0 : zynhNum.hashCode());
		result = prime * result
				+ ((zyssCost == null) ? 0 : zyssCost.hashCode());
		result = prime * result + ((zyssNum == null) ? 0 : zyssNum.hashCode());
		result = prime * result
				+ ((zyylCost == null) ? 0 : zyylCost.hashCode());
		result = prime * result
				+ ((zyypCost == null) ? 0 : zyypCost.hashCode());
		result = prime * result + ((zyzfNum == null) ? 0 : zyzfNum.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessYzcxzhcx other = (BusinessYzcxzhcx) obj;
		if (bedBz == null) {
			if (other.bedBz != null)
				return false;
		} else if (!bedBz.equals(other.bedBz))
			return false;
		if (cfhjCost == null) {
			if (other.cfhjCost != null)
				return false;
		} else if (!cfhjCost.equals(other.cfhjCost))
			return false;
		if (cfhjNum == null) {
			if (other.cfhjNum != null)
				return false;
		} else if (!cfhjNum.equals(other.cfhjNum))
			return false;
		if (cfzfNum == null) {
			if (other.cfzfNum != null)
				return false;
		} else if (!cfzfNum.equals(other.cfzfNum))
			return false;
		if (cywjNum == null) {
			if (other.cywjNum != null)
				return false;
		} else if (!cywjNum.equals(other.cywjNum))
			return false;
		if (cyyjNum == null) {
			if (other.cyyjNum != null)
				return false;
		} else if (!cyyjNum.equals(other.cyyjNum))
			return false;
		if (fjsNum == null) {
			if (other.fjsNum != null)
				return false;
		} else if (!fjsNum.equals(other.fjsNum))
			return false;
		if (ghhjNum == null) {
			if (other.ghhjNum != null)
				return false;
		} else if (!ghhjNum.equals(other.ghhjNum))
			return false;
		if (ghhjRs == null) {
			if (other.ghhjRs != null)
				return false;
		} else if (!ghhjRs.equals(other.ghhjRs))
			return false;
		if (ghsrCost == null) {
			if (other.ghsrCost != null)
				return false;
		} else if (!ghsrCost.equals(other.ghsrCost))
			return false;
		if (jsNum == null) {
			if (other.jsNum != null)
				return false;
		} else if (!jsNum.equals(other.jsNum))
			return false;
		if (jymzNum == null) {
			if (other.jymzNum != null)
				return false;
		} else if (!jymzNum.equals(other.jymzNum))
			return false;
		if (jzNum == null) {
			if (other.jzNum != null)
				return false;
		} else if (!jzNum.equals(other.jzNum))
			return false;
		if (mzssCost == null) {
			if (other.mzssCost != null)
				return false;
		} else if (!mzssCost.equals(other.mzssCost))
			return false;
		if (mzssNum == null) {
			if (other.mzssNum != null)
				return false;
		} else if (!mzssNum.equals(other.mzssNum))
			return false;
		if (mzylCost == null) {
			if (other.mzylCost != null)
				return false;
		} else if (!mzylCost.equals(other.mzylCost))
			return false;
		if (mzypCost == null) {
			if (other.mzypCost != null)
				return false;
		} else if (!mzypCost.equals(other.mzypCost))
			return false;
		if (operDate == null) {
			if (other.operDate != null)
				return false;
		} else if (!operDate.equals(other.operDate))
			return false;
		if (pzNum == null) {
			if (other.pzNum != null)
				return false;
		} else if (!pzNum.equals(other.pzNum))
			return false;
		if (qtptNum == null) {
			if (other.qtptNum != null)
				return false;
		} else if (!qtptNum.equals(other.qtptNum))
			return false;
		if (ryNum == null) {
			if (other.ryNum != null)
				return false;
		} else if (!ryNum.equals(other.ryNum))
			return false;
		if (sfhjNum == null) {
			if (other.sfhjNum != null)
				return false;
		} else if (!sfhjNum.equals(other.sfhjNum))
			return false;
		if (sfzfNum == null) {
			if (other.sfzfNum != null)
				return false;
		} else if (!sfzfNum.equals(other.sfzfNum))
			return false;
		if (sjryNum == null) {
			if (other.sjryNum != null)
				return false;
		} else if (!sjryNum.equals(other.sjryNum))
			return false;
		if (ssts == null) {
			if (other.ssts != null)
				return false;
		} else if (!ssts.equals(other.ssts))
			return false;
		if (tshNum == null) {
			if (other.tshNum != null)
				return false;
		} else if (!tshNum.equals(other.tshNum))
			return false;
		if (yq == null) {
			if (other.yq != null)
				return false;
		} else if (!yq.equals(other.yq))
			return false;
		if (zdcfCost == null) {
			if (other.zdcfCost != null)
				return false;
		} else if (!zdcfCost.equals(other.zdcfCost))
			return false;
		if (zkNum == null) {
			if (other.zkNum != null)
				return false;
		} else if (!zkNum.equals(other.zkNum))
			return false;
		if (zmzjNum == null) {
			if (other.zmzjNum != null)
				return false;
		} else if (!zmzjNum.equals(other.zmzjNum))
			return false;
		if (zxcfCost == null) {
			if (other.zxcfCost != null)
				return false;
		} else if (!zxcfCost.equals(other.zxcfCost))
			return false;
		if (zyhjNum == null) {
			if (other.zyhjNum != null)
				return false;
		} else if (!zyhjNum.equals(other.zyhjNum))
			return false;
		if (zynhNum == null) {
			if (other.zynhNum != null)
				return false;
		} else if (!zynhNum.equals(other.zynhNum))
			return false;
		if (zyssCost == null) {
			if (other.zyssCost != null)
				return false;
		} else if (!zyssCost.equals(other.zyssCost))
			return false;
		if (zyssNum == null) {
			if (other.zyssNum != null)
				return false;
		} else if (!zyssNum.equals(other.zyssNum))
			return false;
		if (zyylCost == null) {
			if (other.zyylCost != null)
				return false;
		} else if (!zyylCost.equals(other.zyylCost))
			return false;
		if (zyypCost == null) {
			if (other.zyypCost != null)
				return false;
		} else if (!zyypCost.equals(other.zyypCost))
			return false;
		if (zyzfNum == null) {
			if (other.zyzfNum != null)
				return false;
		} else if (!zyzfNum.equals(other.zyzfNum))
			return false;
		return true;
	}
}
