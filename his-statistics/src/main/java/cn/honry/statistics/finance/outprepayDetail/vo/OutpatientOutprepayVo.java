package cn.honry.statistics.finance.outprepayDetail.vo;

import java.util.List;

import cn.honry.base.bean.business.Entity;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientOutprepay;

/** 
 * 财务：门诊充值明细打印用vo
 * @ClassName: OutpatientOutprepayVo 
 * @Description: 财务：门诊充值明细打印用vo
 * @author dtl
 * @date 2017年5月17日
 *  
*/
public class OutpatientOutprepayVo extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 
	* @Fields iPatientName : 患者姓名
	*/ 
	private String iPatientName;
	/** 
	* @Fields iMedicalrecordId : 病历号 
	*/ 
	private String iMedicalrecordId;
	/** 
	* @Fields iBeginDate : 开始时间 
	*/ 
	private String iBeginDate;
	/** 
	* @Fields iEndDate : 结束时间
	*/ 
	private String iEndDate;
	/** 
	* @Fields iIc : 就诊卡 
	*/ 
	private String iIc;
	/** 
	* @Fields iIdCard : 身份证
	*/ 
	private String iIdCard;
	/** 
	* @Fields outpatientOutprepays : 门诊充值明细记录
	*/ 
	private List<OutpatientOutprepay> outpatientOutprepays;
	/** 
	* @Fields outpatientAccountrecords : 门诊消费明细记录
	*/ 
	private List<OutpatientAccountrecord> outpatientAccountrecords;
	

	public String getiPatientName() {
		return iPatientName;
	}
	public void setiPatientName(String iPatientName) {
		this.iPatientName = iPatientName;
	}
	public String getiMedicalrecordId() {
		return iMedicalrecordId;
	}
	public void setiMedicalrecordId(String iMedicalrecordId) {
		this.iMedicalrecordId = iMedicalrecordId;
	}
	public String getiBeginDate() {
		return iBeginDate;
	}
	public void setiBeginDate(String iBeginDate) {
		this.iBeginDate = iBeginDate;
	}
	public String getiEndDate() {
		return iEndDate;
	}
	public void setiEndDate(String iEndDate) {
		this.iEndDate = iEndDate;
	}
	public String getiIc() {
		return iIc;
	}
	public void setiIc(String iIc) {
		this.iIc = iIc;
	}
	public String getiIdCard() {
		return iIdCard;
	}
	public void setiIdCard(String iIdCard) {
		this.iIdCard = iIdCard;
	}
	public List<OutpatientOutprepay> getOutpatientOutprepays() {
		return outpatientOutprepays;
	}
	public void setOutpatientOutprepays(
			List<OutpatientOutprepay> outpatientOutprepays) {
		this.outpatientOutprepays = outpatientOutprepays;
	}
	public List<OutpatientAccountrecord> getOutpatientAccountrecords() {
		return outpatientAccountrecords;
	}
	public void setOutpatientAccountrecords(
			List<OutpatientAccountrecord> outpatientAccountrecords) {
		this.outpatientAccountrecords = outpatientAccountrecords;
	}
	/**
	 * @param iPatientName 患者姓名
	 * @param iMedicalrecordId 病历号
	 * @param iBeginDate 开始时间
	 * @param iEndDate 结束时间
	 * @param iIc 就诊卡号
	 * @param iIdCard 身份证号
	 * @param outpatientOutprepays 充值明细
	 * @param outpatientAccountrecords 消费明细
	 */
	public OutpatientOutprepayVo(String iPatientName, String iMedicalrecordId,
			String iBeginDate, String iEndDate, String iIc, String iIdCard,
			List<OutpatientOutprepay> outpatientOutprepays,
			List<OutpatientAccountrecord> outpatientAccountrecords) {
		super();
		this.iPatientName = iPatientName;
		this.iMedicalrecordId = iMedicalrecordId;
		this.iBeginDate = iBeginDate;
		this.iEndDate = iEndDate;
		this.iIc = iIc;
		this.iIdCard = iIdCard;
		if (outpatientOutprepays != null && !outpatientOutprepays.isEmpty()) {
			this.outpatientOutprepays = outpatientOutprepays;
		}
		if (outpatientAccountrecords != null && !outpatientAccountrecords.isEmpty()) {
			this.outpatientAccountrecords = outpatientAccountrecords;
		}
	}
	
}
