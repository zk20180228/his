package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class MApkVersion extends Entity{

	private Integer apkMinimumNum;//最低版本号
	
	private Integer apkCurrentVnum;//当前版本号
	
	private Integer apkNewestVnum;//最新版本号
	
	private String apkVersionName;//版本名称
	
	private Integer forceUpdateFlg;//是否强制更新 1:否 （不需强制更新）2：是（需强制更新）
	
	private Integer apkClearCache;//是否清理缓存 （1 是 2 否）信息维护。
	
	private String apkDownloadAddr;//apk下载地址
	
	private String apkDownloadQRAddr;//apk二维码下载地址
	
	private String apkDownloadFixedAddr;//apk固定下载地址

	public String getApkDownloadFixedAddr() {
		return apkDownloadFixedAddr;
	}

	public void setApkDownloadFixedAddr(String apkDownloadFixedAddr) {
		this.apkDownloadFixedAddr = apkDownloadFixedAddr;
	}

	public Integer getApkMinimumNum() {
		return apkMinimumNum;
	}

	public void setApkMinimumNum(Integer apkMinimumNum) {
		this.apkMinimumNum = apkMinimumNum;
	}

	public Integer getApkCurrentVnum() {
		return apkCurrentVnum;
	}

	public void setApkCurrentVnum(Integer apkCurrentVnum) {
		this.apkCurrentVnum = apkCurrentVnum;
	}

	public Integer getApkNewestVnum() {
		return apkNewestVnum;
	}

	public void setApkNewestVnum(Integer apkNewestVnum) {
		this.apkNewestVnum = apkNewestVnum;
	}

	public String getApkVersionName() {
		return apkVersionName;
	}

	public void setApkVersionName(String apkVersionName) {
		this.apkVersionName = apkVersionName;
	}

	public Integer getForceUpdateFlg() {
		return forceUpdateFlg;
	}

	public void setForceUpdateFlg(Integer forceUpdateFlg) {
		this.forceUpdateFlg = forceUpdateFlg;
	}

	public String getApkDownloadAddr() {
		return apkDownloadAddr;
	}

	public void setApkDownloadAddr(String apkDownloadAddr) {
		this.apkDownloadAddr = apkDownloadAddr;
	}

	public Integer getApkClearCache() {
		return apkClearCache;
	}

	public void setApkClearCache(Integer apkClearCache) {
		this.apkClearCache = apkClearCache;
	}

	public String getApkDownloadQRAddr() {
		return apkDownloadQRAddr;
	}

	public void setApkDownloadQRAddr(String apkDownloadQRAddr) {
		this.apkDownloadQRAddr = apkDownloadQRAddr;
	}
	
	
	
}
