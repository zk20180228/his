package cn.honry.oa.homePage.vo;

import java.io.Serializable;

/**
 * 
 * <p>加载栏目下的文章 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年7月26日 上午10:46:27 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月26日 上午10:46:27 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class ArticleVo implements Serializable{

	
	private static final long serialVersionUID = 2403297589060084575L;
	
	private String infoId;//文章的id
	private String infoTitle;//文章的标题
	private String infoPubTime;//文章的发布时间
	private String infoKeyWord;//关键字
	private String infoEditor;//作者
	private String updateUser;//修改人
	private String updateTime;//修改时间
	private String outTime;//过期时间
	
	private String imgPath;//文章对应的图片的路径
	
	
	
	
	
	
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	private Integer totalPage;//总页数
	
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public String getInfoTitle() {
		return infoTitle;
	}
	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}
	public String getInfoPubTime() {
		return infoPubTime;
	}
	public void setInfoPubTime(String infoPubTime) {
		this.infoPubTime = infoPubTime;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public String getInfoKeyWord() {
		return infoKeyWord;
	}
	public void setInfoKeyWord(String infoKeyWord) {
		this.infoKeyWord = infoKeyWord;
	}
	public String getInfoEditor() {
		return infoEditor;
	}
	public void setInfoEditor(String infoEditor) {
		this.infoEditor = infoEditor;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	
	
	

}
