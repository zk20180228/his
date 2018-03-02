package cn.honry.oa.juris.vo;

/**职务**/
public class DutiesVo {

	/**职务编码**/
	private String code;
	/**职务名称**/
	private String name;
	/**该职务是否选中**/
	private boolean select;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	
}
