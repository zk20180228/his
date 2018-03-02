package cn.honry.base.bean.model;

/**  
 * 
 * <p> </p>
 * @Author: mrb
 * @CreateDate: 2017年7月21日 下午4:53:19 
 * @Modifier: mrb
 * @ModifyDate: 2017年7月21日 下午4:53:19 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 * @return: 
 *
 */
public class InformationAttachDownAuth {
	private String id;
	/*
	 * 文章id
	 */
	private String informationId;
	/*
	 * 0:全部人员,1:按个人,2:级别,3:科室,4:职务
	 */
	private Integer type = 0;
	/*
	 * TYPE对应的code
	 */
	private String code;
	/*
	 *权限名称 
	 */
	private String name;
	/*
	 * 附件地址
	 */
	private String fileURL;
	/*
	 * 附件名称
	 */
	private String filename;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInformationId() {
		return informationId;
	}
	public void setInformationId(String informationId) {
		this.informationId = informationId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
