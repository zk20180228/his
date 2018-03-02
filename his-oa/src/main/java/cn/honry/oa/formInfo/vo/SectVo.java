package cn.honry.oa.formInfo.vo;

import java.util.List;

public class SectVo {
	
	private String tag;
	private String text;
	private String col;
	private String row;
	private String type;
	private List<FielVo> fields;
	private List<MergVo> merge;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<FielVo> getFields() {
		return fields;
	}
	public void setFields(List<FielVo> fields) {
		this.fields = fields;
	}
	public List<MergVo> getMerge() {
		return merge;
	}
	public void setMerge(List<MergVo> merge) {
		this.merge = merge;
	}
	
}
