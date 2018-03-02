package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**  
 * 
 * <p>员工分类类型对照表</p>
 * @Author: dutianliang
 * @CreateDate: 2017年7月27日 上午10:54:28 
 * @Modifier: dutianliang
 * @ModifyDate: 2017年7月27日 上午10:54:28 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class EmployeeTypeContrast extends Entity{
	
	/**  
	 * 
	 * @Fields code : 分类code
	 *
	 */
	private String code;
	/**  
	 * 
	 * @Fields name : 分类name
	 *
	 */
	private String name;
	/**  
	 * 
	 * @Fields empTypeCode : 分类对应的类型编码
	 *
	 */
	private String empTypeCode;
	/**  
	 * 
	 * @Fields empTypeName : 分类对应的类型名称
	 *
	 */
	private String empTypeName;
	/**  
	 * 
	 * @Fields pinyin : 分类拼音
	 *
	 */
	private String pinyin;
	/**  
	 * 
	 * @Fields wb : 分类五笔
	 *
	 */
	private String wb;
	/**  
	 * 
	 * @Fields inputCode : 分类自定义码 
	 *
	 */
	private String inputCode;
	/**  
	 * 
	 * @Fields extc1 : 备用字段1
	 *
	 */
	private String extc1;
	/**  
	 * 
	 * @Fields extc2 : 备用字段2
	 *
	 */
	private String extc2;
	/**  
	 * 
	 * @Fields extc3 : 备用字段3
	 *
	 */
	private String extc3;
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
	public String getEmpTypeCode() {
		return empTypeCode;
	}
	public void setEmpTypeCode(String empTypeCode) {
		this.empTypeCode = empTypeCode;
	}
	public String getEmpTypeName() {
		return empTypeName;
	}
	public void setEmpTypeName(String empTypeName) {
		this.empTypeName = empTypeName;
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
	public String getExtc1() {
		return extc1;
	}
	public void setExtc1(String extc1) {
		this.extc1 = extc1;
	}
	public String getExtc2() {
		return extc2;
	}
	public void setExtc2(String extc2) {
		this.extc2 = extc2;
	}
	public String getExtc3() {
		return extc3;
	}
	public void setExtc3(String extc3) {
		this.extc3 = extc3;
	}

}
