package cn.honry.base.bean.model;

import java.util.ArrayList;
/**
 * 报表以javaBean的形式作为数据源,测试用例实体
 * @author  hedong
 * @date 创建时间：2017年2月17日
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
public class IReportJavaBeanDemo {
    private String testField1;//测试字段1
    private String testField2;//测试字段1
    private ArrayList<User> users;//子表数据
	public String getTestField1() {
		return testField1;
	}
	public void setTestField1(String testField1) {
		this.testField1 = testField1;
	}
	public String getTestField2() {
		return testField2;
	}
	public void setTestField2(String testField2) {
		this.testField2 = testField2;
	}
	public ArrayList<User> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}   
       
}
