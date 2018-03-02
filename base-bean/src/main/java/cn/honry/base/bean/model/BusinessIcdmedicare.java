package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BusinessIcdmedicare extends Entity{
	
	        //医保诊断代码
			private String icdCode;
			//医保诊断名称
			private Integer icdName;
			//医保诊断拼音
			private String icdspell;
			//合同单位代码 2市保 3省保
			private String packCode;
			
			public String getIcdCode() {
				return icdCode;
			}
			public void setIcdCode(String icdCode) {
				this.icdCode = icdCode;
			}
			public Integer getIcdName() {
				return icdName;
			}
			public void setIcdName(Integer icdName) {
				this.icdName = icdName;
			}
			public String getIcdspell() {
				return icdspell;
			}
			public void setIcdspell(String icdspell) {
				this.icdspell = icdspell;
			}
			public String getPackCode() {
				return packCode;
			}
			public void setPackCode(String packCode) {
				this.packCode = packCode;
			}
			

}
