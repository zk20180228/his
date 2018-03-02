package cn.honry.statistics.bi.disease.diagnose.vo;

import java.io.Serializable;

/**
 * 
 * <p>病情特征下拉框渲染 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年12月9日 下午3:23:50 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月9日 下午3:23:50 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class FeaturesVo implements Serializable{


	private static final long serialVersionUID = 6762658600680149182L;
	
	//""是给的默认值，因为转换为json时，有些情况下，这些字段不存在，我不希望字段不存在，插件不是我写的，为了防止字段不存在时前台报错
	//插件区分每一个病情特征的依据是id,因此要保证id不能相同，否则多选会出问题
	private String  code="";
    private String  id="";
    private String  inputCode="";
    private String  name;//病情特征名字，只有这个属性在病情特征模块有效
	private String  pinyin="";
	private String  type="";
	private String  wb="";
	
	
	
	
	public FeaturesVo() {
		
	}
	
	
	public FeaturesVo(String name,String id) {
		this.name = name;
		this.id=id;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
   
	

}
