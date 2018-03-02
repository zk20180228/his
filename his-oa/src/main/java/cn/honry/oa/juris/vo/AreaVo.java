package cn.honry.oa.juris.vo;

/**院区**/
public class AreaVo {

	/**院区编码**/
	private String code;
	/**院区名称**/
	private String name;
	/**该院区是否选中**/
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
