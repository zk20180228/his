package cn.honry.statistics.finance.inoutstore.vo;


/**  
 *  封装查询条件VO
 * @Author:luyanshou
 * @version 1.0
 */
public class StoreSearchVo {

//	private Date beginTime;//开始时间
//	private Date endTime;//结束时间
	private String beginTime;//开始时间
	private String endTime;//结束时间
	private String code;//药品编码
	private Integer type=0;//入出库类型(0-出库;1-入库)
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String  getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
