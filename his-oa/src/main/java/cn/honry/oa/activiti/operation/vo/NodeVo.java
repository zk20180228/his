package cn.honry.oa.activiti.operation.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NodeVo {

	private int x;
    private int y;
    private int width;
    private int height;
    private String type;
    private String id;
    private String name;
    private String assignee;
    private String assigneeName;
    private Date startTime;
    private Date endTime;
    private List<EdgeVo> outgoings = new ArrayList<>();
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public List<EdgeVo> getOutgoings() {
		return outgoings;
	}
	public void setOutgoings(List<EdgeVo> outgoings) {
		this.outgoings = outgoings;
	}
    
}
