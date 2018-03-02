<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<style type="text/css">
.panel-header{
	border-top:0
}
</style>
</head>
<body>
<div id="divLayout" class="easyui-layout" fit="true">
	<input type="hidden" value="${user.id }" id="id"></input>
	<div id="centerPanelUser" data-options="region:'north',title:'用户信息',iconCls:'icon-book',border:false" style="height:223px;">
		<div class="easyui-layout" data-options="fit:true,border:false" style="height: 200px;">
			<div data-options="region:'north'" style="height: 40px;padding:5px 0px 5px 0px;border-top:0">
				<a href="javascript:btnModify();" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改密码</a> 
				<a href="javascript:btnEmp();" class="easyui-linkbutton" data-options="iconCls:'icon-user'">员工信息</a>
				<a href="javascript:btnEmpExt();" class="easyui-linkbutton" data-options="iconCls:'icon-user'">员工扩展信息</a>
				<a href="javascript:btnsign();" class="easyui-linkbutton" data-options="iconCls:'icon-text_signature'">个人签名</a>
				<a href="javascript:wagesEdit();" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">设置工资查询密码</a> 
			</div>
			<div data-options="region:'center',border:false" style="border: 0;">
				<table id="list" class="honry-table" style="height:150px;width: 100%;border: 0;">
					<tr>
						<td class="honry-lable" style="width: 12%">账号：</td>
						<td id="account" class="honry-view" style="width: 12%">${user.account}</td>
						<td class="honry-lable" style="width: 12%">姓名：</td>
						<td class="honry-view" style="width: 12%">${user.name}</td>
						<td class="honry-lable" style="width: 12%">昵称：</td>
						<td class="honry-view" style="width: 12%">${user.nickName}</td>
						<td class="honry-lable" style="width: 12%">性别：</td>
						<td id="sexTd" class="honry-view" style="width: 12%">
						<input type="hidden" id="sex" value="${user.sex }" /></td>
					</tr>
					<tr>
						<td class="honry-lable" style="width: 12%">出生日期：</td>
						<td id="ubir" class="honry-view" style="width: 12%"><input
							type="hidden" id="ubirt" value="${user.birthday }" /></td>
						<td class="honry-lable" style="width: 12%">电话：</td>
						<td class="honry-view" style="width: 12">${user.phone }</td>
						<td class="honry-lable" style="width: 12%">电子邮箱：</td>
						<td class="honry-view" style="width: 12%">${user.email }</td>
						<td class="honry-lable" style="width: 12%">工资账号：</td>
						<td class="honry-view" style="width: 12%">${employee.wagesAccount}</td>
					</tr>
					<tr>
						<td class="honry-lable" style="width: 12%">能否授权：</td>
						<td id="canAuthorizeTd" class="honry-view" style="width: 16%">
							<input type="hidden" id="canAuthorize" value="${user.canAuthorize }" />
						</td>
						<td class="honry-lable" style="width: 12%">最后一次登录时间：</td>
						<td class="honry-view" style="width: 12%">${user.lastLoginTime }</td>
						<td class="honry-lable" style="width: 12%">登录失败次数：</td>
						<td class="honry-view" style="width: 12%">${user.failedTimes }</td>
						<td class="honry-lable" style="width: 12%">移动端使用状态：</td>
						<td class="honry-view" id="userAppUsageStatusId" style="width: 12%">
							<input type="hidden" id="userAppUsageStatus" value="${user.userAppUsageStatus }" />
						</td>
						
					</tr>
					<tr>
						<td class="honry-lable" style="width: 12%">备注：</td>
						<td colspan="7" class="honry-view" style="width: 12%">${user.remark }</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div data-options="region:'center',border:false"  style="height: 55%;width:100%">
		<div class="easyui-layout" data-options="fit:true">
			<div id="centerPanelRole"  data-options="region:'west',title:'当前用户关联角色',iconCls:'icon-book'" style="width:60%;hight:100%">
				<table id="Rolelist" style="height: 100%" class="easyui-datagrid" data-options="url:'${pageContext.request.contextPath}/sys/getRoles.action',idField:'id',treeField:'name',animate:false,rownumbers:true,pagination:true,border:false,
					onLoadSuccess: function(data){
						//分页工具栏作用提示
						var pager = $(this).datagrid('getPager');
						var aArr = $(pager).find('a');
						var iArr = $(pager).find('input');
						$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
						for(var i=0;i<aArr.length;i++){
							$(aArr[i]).tooltip({
								content:toolArr[i],
								hideDelay:1
							});
							$(aArr[i]).tooltip('hide');
						}
					},onBeforeLoad:function(param){
						$(this).datagrid('uncheckAll');
					}">
					<thead>
						<tr>
							<th data-options="field:'name'"style="width: 15%">拥有角色名称</th>
							<th data-options="field:'alias'"style="width: 15%">角色别名</th>
							<th data-options="field:'description'"style="width: 15%">角色描述</th>
						</tr>
					</thead>
				</table>
			</div>   
			<div  data-options="region:'center',title:'当前用户关联科室',iconCls:'icon-book'" style="width:40%;hight:100%">
				<ul id="deptlist" >加载中...</ul>
			</div>
		</div>
	</div>
</div>
<div id="add"></div>
<script type="text/javascript">
var sexMap=new Map();
$(function(){
	//性别
	
		$.ajax({
			url : "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
			data:{"type":"sex"},
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
				//性别
				if(sexMap.get($('#sex').val())){
					$('#sexTd').text(sexMap.get($('#sex').val()));
				}
			}
		});
	$("#deptlist").tree({
		url:"<%=basePath %>sys/userByDept.action",
	});
});
//是否授权
if($('#canAuthorize').val()==1){
	$('#canAuthorizeTd').text("授权");
}else{
	$('#canAuthorizeTd').text("否");
}
//移动端使用状态
 if($('#userAppUsageStatus').val()){
	 if($('#userAppUsageStatus').val()==0){
		    $('#userAppUsageStatusId').text("从未登录");
		}else if($('#userAppUsageStatus').val()==1){
		    $('#userAppUsageStatusId').text("登录过");
		}else if($('#userAppUsageStatus').val()==2){
		    $('#userAppUsageStatusId').text("已退出");
		}else{
			$('#userAppUsageStatusId').text("");
		}
 }else{
	 $('#userAppUsageStatusId').text("");
 }

if($('#ubirt').val() != null){
	$('#ubir').text($('#ubirt').val().substring(0,10));
}
//修改密码
function btnModify(){
	Adddilog("修改密码","<%=basePath %>sys/updatePwdList.action");
}
//个人签名
function btnsign(){
	var account = $('#account').text();
	<%-- parent.closableTab.addTab({
		'id':'IndexneesInfo',
		'name':'个人签名',
		'url':"<%=basePath%>oa/userSign/listUserOneSign.action?userAccount="+account,
		'closable':true,
		"newsInfo":true,
	}); --%>
	Adddilog2("个人签名","<%=basePath %>oa/userSign/oneUserSign.action?userAccount="+account);
}
function wagesEdit(){
	Adddilog("修改工资查询密码","<%=basePath %>oa/Wages/updatePwdList.action");
}
//用户信息
function btnEmp(){
	$.ajax({
		url: "<%=basePath%>sys/findEmpByuserCode.action",
		type:"post",
		success: function(data) {
			if(data=="0"){
				$.messager.alert("操作提示", "该用户没用关联员工！", "warning");
				close_alert();
			}else{
				Adddilog3("<c:url value='sys/setEmpByuserList.action'/>");
			}
		}
	});
	
}
//用户拓展信息
function btnEmpExt(){
	$.ajax({
		url: "<%=basePath%>sys/editExtend.action",
		type:"post",
		success: function(data) {
				Adddilog3("<c:url value='sys/editExtend.action'/>");
		}
	});
	
}
/**
 * 加载dialog
 */
function Adddilog(title, url) {
	$('#add').dialog({    
	    title: title,    
	    width: '500px',    
	    height:'190px',    
	    closed: false,    
	    cache: false,    
	    href: url,    
	    modal: true   
	   });    
}
function Adddilog2(title, url) {
	$('#add').dialog({    
	    title: title,    
	    width: '800px',    
	    height:'500px',    
	    closed: false,    
	    cache: false,    
	    href: url,    
	    modal: true   
	   });    
}
//加载dialog
function Adddilog3(url) {//是否有滚动条,是否居中显示,是否可以改变大小
	return window.open(url,'newwindow',' left=273,top=149,width='+ (screen.availWidth -605) +',height='+ (screen.availHeight-352));
}
/**
 * 打开dialog
 */
function openDialog() {
	$('#add').dialog('open'); 
}
/**
 * 关闭dialog
 */
function closeDialog() {
	$('#add').dialog('close');  
}
</script>
</body>
</html>