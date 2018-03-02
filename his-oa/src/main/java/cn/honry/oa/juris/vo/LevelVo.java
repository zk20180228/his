package cn.honry.oa.juris.vo;

/**级别**/
public class LevelVo {

	/**级别编码**/
	private String code;
	/**级别名称**/
	private String name;
	/**该级别是否选中**/
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
