package cn.honry.statistics.doctor.registerInfoGzltj.vo;

/**  
 *  
 * @className：MenuButtonStateVO
 * @Description：  用于判断按钮状态的VO
 * @Author：aizhonghua
 * @CreateDate：2015-7-24 下午06:22:16  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-7-24 下午06:22:16  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class RegisterInfoGzltjVo {
	private String deptType;//科室分类
	private String dept;//科室
	private String deptId;//科室id
	private String expxrt;//医生
	private String expxrtId;//医生id
	private String title;//医生职称
	private String titleId;//医生职称id
	private Integer monNum=0;//周一数量
	private Double monCost=0.0;//周一金额
	private Integer tueNum=0;//周二数量
	private Double tueCost=0.0;//周二金额
	private Integer wedNum=0;//周三数量
	private Double wedCost=0.0;//周三金额
	private Integer thuNum=0;//周四数量
	private Double thuCost=0.0;//周四金额
	private Integer friNum=0;//周五数量
	private Double friCost=0.0;//周五金额
	private Integer satNum=0;//周六数量
	private Double satCost=0.0;//周六金额
	private Integer sunNum=0;//周日数量
	private Double sunCost=0.0;//周日金额
	private Integer num=0;//合计数量
	private Double cost=0.0;//合计金额
	
	public String getTitleId() {
		return titleId;
	}
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getExpxrtId() {
		return expxrtId;
	}
	public void setExpxrtId(String expxrtId) {
		this.expxrtId = expxrtId;
	}
	public String getDeptType() {
		return deptType;
	}
	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getExpxrt() {
		return expxrt;
	}
	public void setExpxrt(String expxrt) {
		this.expxrt = expxrt;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getMonNum() {
		return monNum;
	}
	public void setMonNum(Integer monNum) {
		this.monNum = monNum;
	}
	public Double getMonCost() {
		return monCost;
	}
	public void setMonCost(Double monCost) {
		this.monCost = monCost;
	}
	public Integer getTueNum() {
		return tueNum;
	}
	public void setTueNum(Integer tueNum) {
		this.tueNum = tueNum;
	}
	public Double getTueCost() {
		return tueCost;
	}
	public void setTueCost(Double tueCost) {
		this.tueCost = tueCost;
	}
	public Integer getWedNum() {
		return wedNum;
	}
	public void setWedNum(Integer wedNum) {
		this.wedNum = wedNum;
	}
	public Double getWedCost() {
		return wedCost;
	}
	public void setWedCost(Double wedCost) {
		this.wedCost = wedCost;
	}
	public Integer getThuNum() {
		return thuNum;
	}
	public void setThuNum(Integer thuNum) {
		this.thuNum = thuNum;
	}
	public Double getThuCost() {
		return thuCost;
	}
	public void setThuCost(Double thuCost) {
		this.thuCost = thuCost;
	}
	public Integer getFriNum() {
		return friNum;
	}
	public void setFriNum(Integer friNum) {
		this.friNum = friNum;
	}
	public Double getFriCost() {
		return friCost;
	}
	public void setFriCost(Double friCost) {
		this.friCost = friCost;
	}
	public Integer getSatNum() {
		return satNum;
	}
	public void setSatNum(Integer satNum) {
		this.satNum = satNum;
	}
	public Double getSatCost() {
		return satCost;
	}
	public void setSatCost(Double satCost) {
		this.satCost = satCost;
	}
	public Integer getSunNum() {
		return sunNum;
	}
	public void setSunNum(Integer sunNum) {
		this.sunNum = sunNum;
	}
	public Double getSunCost() {
		return sunCost;
	}
	public void setSunCost(Double sunCost) {
		this.sunCost = sunCost;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}

	
}
