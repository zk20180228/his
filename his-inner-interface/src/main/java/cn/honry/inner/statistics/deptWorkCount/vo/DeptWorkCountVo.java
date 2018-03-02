package cn.honry.inner.statistics.deptWorkCount.vo;

import java.io.Serializable;
/**
 * 
 * @Description:科室工作量统计(初始化数据到mongo的vo也是从mongo读数据的vo)
 * @author:zhangkui
 * @time:2017年6月28日 上午9:14:16
 */

public class DeptWorkCountVo implements Serializable{

	/**
	* @Description: serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1656587079409275160L;
	
	private String deptName;//科室名字
	private String deptCode;//科室编号
	private String regDate;//挂号日期
	private Integer gjjzmzjcount;//国家级知名专家，数量
	private Double gjjzmzjmoney;//金额
	private Integer sjzmzjcount;//省级知名专家
	private Double sjzmzjmoney;
	private Integer zmzjcount;//知名专家
	private Double zmzjmoney;
	private Integer jscount;//教授
	private Double jsmoney;
	private Integer fjscount;//副教授
	private Double fjsmoney;
	private Integer jymzcount;//简易门诊
	private Double jymzmoney;
	private Integer ybyscount;//一般医生
	private Double ybysmoney;
	private Integer zzyscount;//主治医生
	private Double zzysmoney;
	private Integer lnyzcount;//老年优诊
	private Double lnyzmoney;
	private Integer slzcfcount;//视力诊查费
	private Double slzcfmoney;
	private Integer jmjskcount;//居民健身卡
	private Double jmjskmoney;
	private Integer othercount;//其他
	private Double othermoney;
	private Integer jzcount;//急诊
	private Double jzmoney;
	private Integer yzcount;//优诊
	private Double yzmoney;
	private Integer yycount;//预约
	private Double yymoney;
	private Integer ghcount;//过号
	private Double ghmoney;
	private Integer fzcount;//复诊
	private Double fzmoney;
	private Integer pzcount;//平诊
	private Double pzmoney;
	
	private Integer totalcount;//合计:数量
	private Double totalmoney;//合计：金额
	
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getGjjzmzjcount() {
		return gjjzmzjcount;
	}
	public void setGjjzmzjcount(Integer gjjzmzjcount) {
		this.gjjzmzjcount = gjjzmzjcount;
	}
	public Double getGjjzmzjmoney() {
		return gjjzmzjmoney;
	}
	public void setGjjzmzjmoney(Double gjjzmzjmoney) {
		this.gjjzmzjmoney = gjjzmzjmoney;
	}
	public Integer getSjzmzjcount() {
		return sjzmzjcount;
	}
	public void setSjzmzjcount(Integer sjzmzjcount) {
		this.sjzmzjcount = sjzmzjcount;
	}
	public Double getSjzmzjmoney() {
		return sjzmzjmoney;
	}
	public void setSjzmzjmoney(Double sjzmzjmoney) {
		this.sjzmzjmoney = sjzmzjmoney;
	}
	public Integer getZmzjcount() {
		return zmzjcount;
	}
	public void setZmzjcount(Integer zmzjcount) {
		this.zmzjcount = zmzjcount;
	}
	public Double getZmzjmoney() {
		return zmzjmoney;
	}
	public void setZmzjmoney(Double zmzjmoney) {
		this.zmzjmoney = zmzjmoney;
	}
	public Integer getJscount() {
		return jscount;
	}
	public void setJscount(Integer jscount) {
		this.jscount = jscount;
	}
	public Double getJsmoney() {
		return jsmoney;
	}
	public void setJsmoney(Double jsmoney) {
		this.jsmoney = jsmoney;
	}
	public Integer getFjscount() {
		return fjscount;
	}
	public void setFjscount(Integer fjscount) {
		this.fjscount = fjscount;
	}
	public Double getFjsmoney() {
		return fjsmoney;
	}
	public void setFjsmoney(Double fjsmoney) {
		this.fjsmoney = fjsmoney;
	}
	public Integer getJymzcount() {
		return jymzcount;
	}
	public void setJymzcount(Integer jymzcount) {
		this.jymzcount = jymzcount;
	}
	public Double getJymzmoney() {
		return jymzmoney;
	}
	public void setJymzmoney(Double jymzmoney) {
		this.jymzmoney = jymzmoney;
	}
	public Integer getYbyscount() {
		return ybyscount;
	}
	public void setYbyscount(Integer ybyscount) {
		this.ybyscount = ybyscount;
	}
	public Double getYbysmoney() {
		return ybysmoney;
	}
	public void setYbysmoney(Double ybysmoney) {
		this.ybysmoney = ybysmoney;
	}
	public Integer getZzyscount() {
		return zzyscount;
	}
	public void setZzyscount(Integer zzyscount) {
		this.zzyscount = zzyscount;
	}
	public Double getZzysmoney() {
		return zzysmoney;
	}
	public void setZzysmoney(Double zzysmoney) {
		this.zzysmoney = zzysmoney;
	}
	public Integer getLnyzcount() {
		return lnyzcount;
	}
	public void setLnyzcount(Integer lnyzcount) {
		this.lnyzcount = lnyzcount;
	}
	public Double getLnyzmoney() {
		return lnyzmoney;
	}
	public void setLnyzmoney(Double lnyzmoney) {
		this.lnyzmoney = lnyzmoney;
	}
	public Integer getSlzcfcount() {
		return slzcfcount;
	}
	public void setSlzcfcount(Integer slzcfcount) {
		this.slzcfcount = slzcfcount;
	}
	public Double getSlzcfmoney() {
		return slzcfmoney;
	}
	public void setSlzcfmoney(Double slzcfmoney) {
		this.slzcfmoney = slzcfmoney;
	}
	public Integer getJmjskcount() {
		return jmjskcount;
	}
	public void setJmjskcount(Integer jmjskcount) {
		this.jmjskcount = jmjskcount;
	}
	public Double getJmjskmoney() {
		return jmjskmoney;
	}
	public void setJmjskmoney(Double jmjskmoney) {
		this.jmjskmoney = jmjskmoney;
	}
	public Integer getJzcount() {
		return jzcount;
	}
	public void setJzcount(Integer jzcount) {
		this.jzcount = jzcount;
	}
	public Double getJzmoney() {
		return jzmoney;
	}
	public void setJzmoney(Double jzmoney) {
		this.jzmoney = jzmoney;
	}
	public Integer getYzcount() {
		return yzcount;
	}
	public void setYzcount(Integer yzcount) {
		this.yzcount = yzcount;
	}
	public Double getYzmoney() {
		return yzmoney;
	}
	public void setYzmoney(Double yzmoney) {
		this.yzmoney = yzmoney;
	}
	public Integer getYycount() {
		return yycount;
	}
	public void setYycount(Integer yycount) {
		this.yycount = yycount;
	}
	public Double getYymoney() {
		return yymoney;
	}
	public void setYymoney(Double yymoney) {
		this.yymoney = yymoney;
	}
	public Integer getGhcount() {
		return ghcount;
	}
	public void setGhcount(Integer ghcount) {
		this.ghcount = ghcount;
	}
	public Double getGhmoney() {
		return ghmoney;
	}
	public void setGhmoney(Double ghmoney) {
		this.ghmoney = ghmoney;
	}
	public Integer getFzcount() {
		return fzcount;
	}
	public void setFzcount(Integer fzcount) {
		this.fzcount = fzcount;
	}
	public Double getFzmoney() {
		return fzmoney;
	}
	public void setFzmoney(Double fzmoney) {
		this.fzmoney = fzmoney;
	}
	public Integer getPzcount() {
		return pzcount;
	}
	public void setPzcount(Integer pzcount) {
		this.pzcount = pzcount;
	}
	public Double getPzmoney() {
		return pzmoney;
	}
	public void setPzmoney(Double pzmoney) {
		this.pzmoney = pzmoney;
	}
	public Integer getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(Integer totalcount) {
		this.totalcount = totalcount;
	}
	public Double getTotalmoney() {
		return totalmoney;
	}
	public void setTotalmoney(Double totalmoney) {
		this.totalmoney = totalmoney;
	}
	public Integer getOthercount() {
		return othercount;
	}
	public void setOthercount(Integer othercount) {
		this.othercount = othercount;
	}
	public Double getOthermoney() {
		return othermoney;
	}
	public void setOthermoney(Double othermoney) {
		this.othermoney = othermoney;
	}
}
