package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiRegisterBookinginfo entity. @author MyEclipse Persistence Tools
 */

public class BiRegisterBookinginfo implements java.io.Serializable {

	// Fields

	private String id;
	private String clinicCode;
	private Date preDate;
	private String cardNo;
	private String schemaNo;
	private String preDeptCode;
	private String preDeptName;
	private String preDoctCode;
	private String preDoct;
	private String noonCode;
	private String noonName;
	private String reglevlName;
	private String reglevlCode;
	private Date beginTime;
	private Date endTime;
	private String seeFlag;
	private String bookingName;
	private String bookingIdenno;
	private String bookingRelaPhone;
	private Date bookingBirthday;
	private String bookingSex;
	private String bookingAge;
	private String bookingAgeunitCode;
	private String bookingAgeunit;
	private String bookingAddress;
	private String bookingAppflag;
	private String bookingOrderno;
	private String bookingIsfirst;
	private String bookingStatus;
	private String sourceType;
	private String confirmOpcd;
	private Date confirmDate;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;

	// Constructors

	/** default constructor */
	public BiRegisterBookinginfo() {
	}

	/** minimal constructor */
	public BiRegisterBookinginfo(String clinicCode, Date preDate) {
		this.clinicCode = clinicCode;
		this.preDate = preDate;
	}

	/** full constructor */
	public BiRegisterBookinginfo(String clinicCode, Date preDate,
			String cardNo, String schemaNo, String preDeptCode,
			String preDeptName, String preDoctCode, String preDoct,
			String noonCode, String noonName, String reglevlName,
			String reglevlCode, Date beginTime, Date endTime, String seeFlag,
			String bookingName, String bookingIdenno, String bookingRelaPhone,
			Date bookingBirthday, String bookingSex, String bookingAge,
			String bookingAgeunitCode, String bookingAgeunit,
			String bookingAddress, String bookingAppflag,
			String bookingOrderno, String bookingIsfirst, String bookingStatus,
			String sourceType, String confirmOpcd, Date confirmDate,
			String ext1, String ext2, String ext3, String ext4) {
		this.clinicCode = clinicCode;
		this.preDate = preDate;
		this.cardNo = cardNo;
		this.schemaNo = schemaNo;
		this.preDeptCode = preDeptCode;
		this.preDeptName = preDeptName;
		this.preDoctCode = preDoctCode;
		this.preDoct = preDoct;
		this.noonCode = noonCode;
		this.noonName = noonName;
		this.reglevlName = reglevlName;
		this.reglevlCode = reglevlCode;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.seeFlag = seeFlag;
		this.bookingName = bookingName;
		this.bookingIdenno = bookingIdenno;
		this.bookingRelaPhone = bookingRelaPhone;
		this.bookingBirthday = bookingBirthday;
		this.bookingSex = bookingSex;
		this.bookingAge = bookingAge;
		this.bookingAgeunitCode = bookingAgeunitCode;
		this.bookingAgeunit = bookingAgeunit;
		this.bookingAddress = bookingAddress;
		this.bookingAppflag = bookingAppflag;
		this.bookingOrderno = bookingOrderno;
		this.bookingIsfirst = bookingIsfirst;
		this.bookingStatus = bookingStatus;
		this.sourceType = sourceType;
		this.confirmOpcd = confirmOpcd;
		this.confirmDate = confirmDate;
		this.ext1 = ext1;
		this.ext2 = ext2;
		this.ext3 = ext3;
		this.ext4 = ext4;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClinicCode() {
		return this.clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}

	public Date getPreDate() {
		return this.preDate;
	}

	public void setPreDate(Date preDate) {
		this.preDate = preDate;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getSchemaNo() {
		return this.schemaNo;
	}

	public void setSchemaNo(String schemaNo) {
		this.schemaNo = schemaNo;
	}

	public String getPreDeptCode() {
		return this.preDeptCode;
	}

	public void setPreDeptCode(String preDeptCode) {
		this.preDeptCode = preDeptCode;
	}

	public String getPreDeptName() {
		return this.preDeptName;
	}

	public void setPreDeptName(String preDeptName) {
		this.preDeptName = preDeptName;
	}

	public String getPreDoctCode() {
		return this.preDoctCode;
	}

	public void setPreDoctCode(String preDoctCode) {
		this.preDoctCode = preDoctCode;
	}

	public String getPreDoct() {
		return this.preDoct;
	}

	public void setPreDoct(String preDoct) {
		this.preDoct = preDoct;
	}

	public String getNoonCode() {
		return this.noonCode;
	}

	public void setNoonCode(String noonCode) {
		this.noonCode = noonCode;
	}

	public String getNoonName() {
		return this.noonName;
	}

	public void setNoonName(String noonName) {
		this.noonName = noonName;
	}

	public String getReglevlName() {
		return this.reglevlName;
	}

	public void setReglevlName(String reglevlName) {
		this.reglevlName = reglevlName;
	}

	public String getReglevlCode() {
		return this.reglevlCode;
	}

	public void setReglevlCode(String reglevlCode) {
		this.reglevlCode = reglevlCode;
	}

	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSeeFlag() {
		return this.seeFlag;
	}

	public void setSeeFlag(String seeFlag) {
		this.seeFlag = seeFlag;
	}

	public String getBookingName() {
		return this.bookingName;
	}

	public void setBookingName(String bookingName) {
		this.bookingName = bookingName;
	}

	public String getBookingIdenno() {
		return this.bookingIdenno;
	}

	public void setBookingIdenno(String bookingIdenno) {
		this.bookingIdenno = bookingIdenno;
	}

	public String getBookingRelaPhone() {
		return this.bookingRelaPhone;
	}

	public void setBookingRelaPhone(String bookingRelaPhone) {
		this.bookingRelaPhone = bookingRelaPhone;
	}

	public Date getBookingBirthday() {
		return this.bookingBirthday;
	}

	public void setBookingBirthday(Date bookingBirthday) {
		this.bookingBirthday = bookingBirthday;
	}

	public String getBookingSex() {
		return this.bookingSex;
	}

	public void setBookingSex(String bookingSex) {
		this.bookingSex = bookingSex;
	}

	public String getBookingAge() {
		return this.bookingAge;
	}

	public void setBookingAge(String bookingAge) {
		this.bookingAge = bookingAge;
	}

	public String getBookingAgeunitCode() {
		return this.bookingAgeunitCode;
	}

	public void setBookingAgeunitCode(String bookingAgeunitCode) {
		this.bookingAgeunitCode = bookingAgeunitCode;
	}

	public String getBookingAgeunit() {
		return this.bookingAgeunit;
	}

	public void setBookingAgeunit(String bookingAgeunit) {
		this.bookingAgeunit = bookingAgeunit;
	}

	public String getBookingAddress() {
		return this.bookingAddress;
	}

	public void setBookingAddress(String bookingAddress) {
		this.bookingAddress = bookingAddress;
	}

	public String getBookingAppflag() {
		return this.bookingAppflag;
	}

	public void setBookingAppflag(String bookingAppflag) {
		this.bookingAppflag = bookingAppflag;
	}

	public String getBookingOrderno() {
		return this.bookingOrderno;
	}

	public void setBookingOrderno(String bookingOrderno) {
		this.bookingOrderno = bookingOrderno;
	}

	public String getBookingIsfirst() {
		return this.bookingIsfirst;
	}

	public void setBookingIsfirst(String bookingIsfirst) {
		this.bookingIsfirst = bookingIsfirst;
	}

	public String getBookingStatus() {
		return this.bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getSourceType() {
		return this.sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getConfirmOpcd() {
		return this.confirmOpcd;
	}

	public void setConfirmOpcd(String confirmOpcd) {
		this.confirmOpcd = confirmOpcd;
	}

	public Date getConfirmDate() {
		return this.confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getExt1() {
		return this.ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return this.ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return this.ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return this.ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

}