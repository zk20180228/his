package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 图片维护
 * @author zxl	
 * Date:2015/05/20 15:30
 */

public class MenuIcon extends Entity {	
	
	/**图片路径**/
	private String picPath;
	
	/**图片名称**/
	private String picName;
	
	/**图片显示**/
	private String picShow;

	
	public String getPicShow() {
		return picShow;
	}

	public void setPicShow(String picShow) {
		this.picShow = picShow;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}
	
	
	
}