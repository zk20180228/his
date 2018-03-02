package cn.honry.statistics.bi.disease.diagnose.vo;

import java.io.Serializable;

/**
 * 
 * <p>病症与病种诊断Vo </p>
 * @Author: zhangkui
 * @CreateDate: 2017年12月9日 上午10:13:38 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月9日 上午10:13:38 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class DiagnoseVo implements Serializable {


	private static final long serialVersionUID = -7328458580006632876L;
	
	private String id;//id
	private String feature;//病情特征
	private String result;//病种诊断
	private String matchingDegree;//匹配度
	
	private Integer count;
	
	
	
	public DiagnoseVo() {
	}

	public DiagnoseVo(String feature, String result,String matchingDegree) {
		this.feature = feature;
		this.result = result;
		this.matchingDegree = matchingDegree;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMatchingDegree() {
		return matchingDegree;
	}
	public void setMatchingDegree(String matchingDegree) {
		this.matchingDegree = matchingDegree;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	
	
	
	

}
