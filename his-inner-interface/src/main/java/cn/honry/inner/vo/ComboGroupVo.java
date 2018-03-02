package cn.honry.inner.vo;

/**  
 * 
 * <p> ComboBox分组Vo </p>
 * @Author: aizhonghua
 * @CreateDate: 2016年10月9日 下午5:13:43 
 * @Modifier: aizhonghua
 * @ModifyDate: 2016年10月9日 下午5:13:43 
 *
 */
public class ComboGroupVo implements java.io.Serializable {	
	
	private static final long serialVersionUID = 1L;
	
	/**编码**/
	private String code;
	/**名称**/
	private String name;
	/**组名**/
	private String organize;
	/**拼音**/
	private String pinyin;
	/**五笔**/
	private String wb;
	/**自定义码**/
	private String inputCode;
	/**时间点**/
	private String period;
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
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
	public String getOrganize() {
		return organize;
	}
	public void setOrganize(String organize) {
		this.organize = organize;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	
}
