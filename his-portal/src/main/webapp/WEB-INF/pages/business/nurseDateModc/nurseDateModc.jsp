<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
function submit(){
	$.messager.confirm('此修改影响到医嘱的分解，请慎重操作！','是否继续',function(r){
		if(r){
			$('#form1').form('submit',{
				url:"<%=basePath%>nursestation/nurseDateModc/nurseDateModcSave.action",
				success:function(data){
					if(data=="success"){
						$.messager.alert('提示','保存成功');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}else{
						$.messager.alert('提示','保存失败');
					}
				}
			})
		}else{
			return;
		}
	})
}
function init(){
	var time =$('#time').val();
	if(time!="1"){
		$('#time').val("1");
	}
}
		
</script>
</head>
<body style="width: 100%;height: 100%;">
	<form id="form1" method="post" onSubmit="return checkEmpty(form)">
		<div id="main" align="center" style="margin-top: 100">
			<div style="width:500;margin-top: 20"><font style="font-size: 30">${deptName }医嘱分解时间</font></div>
			<div style="width:500;margin-top: 20"><font style="font-size: 20;color:red">${deptName }的分解时间：当日${s_time }到次日${e_time }</font></div>
			<fieldset style="width: 600;height:65;margin-top: 20">
				<legend>设置分解时间</legend>
				<div>  
		            <table class="honry-table">  
		                <tr>
			                <td class="honry-lable" style="width: 250px">分解时间：</td>
			                <td >
				                <select name="time" id="time">
				                	<option value="1">当日12:00到次日12:00</option>
				                	<option value="2">当日00:00到次日00:00</option>
				                </select>
			                </td>
		                </tr>  
<!-- 		                <tr> -->
<!-- 			                <td><button>初始化</button></td> -->
<!-- 			                <td><button id="save" style="float: right">保存</button></td> -->
<!-- 		                </tr>   -->
		            </table>  
	            </div> 
			</fieldset>
			<shiro:hasPermission name="${menuAlias}:function:save">
				<a href="javascript:init()" id="csh" class="easyui-linkbutton" data-options="iconCls:'icon-init'" style="margin-top: 40;width:80;height:26;">初始化</a>
				<a href="javascript:submit()" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="margin-top: 40">保&nbsp;存&nbsp;</a>
			</shiro:hasPermission>
		    
		<div style="width:400;margin-right: 70px;margin-top: 20"><span >注意：本系统均采用24小时制</span></div>
		</div>
	</form>
 </body>
 <script type="text/javascript">
 var nowLoginDept="${nowLoginDept}";
 if(nowLoginDept!=null&&nowLoginDept!=''){
 	 $("body").setLoading({
 			id:"body",
 			isImg:false,
 			text:"请选择登录科室"
 	});
 }
 
 </script>
</html>