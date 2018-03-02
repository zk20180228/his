package cn.honry.inner.vo;

/**  
 *  
 * @className：MenuVO
 * @Description： 查询menu列表
 * @Author：gaotiantian
 * @CreateDate：2017-4-20 上午11:06:30  
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-20 上午11:06:30  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class MenuVO {
	private String id;	//id,编号
	private String relativeId; //关联id，比如说查询医生时要把相应科室的id也查出来，之后做级联的时候需要用
	private String name;
	private String type;	//父类，根据这个值对数据进行分类
	private String code;
	private String inputCode;
	private String pinyin;
	private String wb;
	private String userDeptId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRelativeId() {
		return relativeId;
	}
	public void setRelativeId(String relativeId) {
		this.relativeId = relativeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getUserDeptId() {
		return userDeptId;
	}
	public void setUserDeptId(String userDeptId) {
		this.userDeptId = userDeptId;
	}
	
}

