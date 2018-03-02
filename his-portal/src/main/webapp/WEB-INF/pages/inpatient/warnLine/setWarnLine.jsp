<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div id="p" class="easyui-panel"  style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
		<form id="editForm">
			<input type="hidden"" name="warnLineVo.id" value="${inpatientInfo.id}">
			<input type="hidden" id="alterType" name="warnLineVo.alterType" value="${inpatientInfo.alterType}">
			<br>
			金额警戒： <input id="checkboxMoney" type='checkbox' name='checkboxMoney' value=1>&nbsp;&nbsp;&nbsp;
		           时间警戒：   <input id="checkboxTime" type='checkbox' name='checkboxTime' value=1><br><br>
		    <div id="money" style="display: none">       
				预警金额：<input class="easyui-numberbox" name="warnLineVo.moneyAlert" value="${inpatientInfo.moneyAlert}"><br><br>
			</div>
			<div id="date" style="display: none">
				<table>
					<tr>
						<td>预警开始时间：
						<input id="alterBegin" name="warnLineVo.alterBegin" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
					</tr>
					<tr>
						<td>预警结束时间：
						<input id="alterEnd" name="warnLineVo.alterEnd" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
					</tr>
				</table>
			</div>
		</form>
		<br>
		<div style="text-align: center; padding: 5px">
			<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:cleard()" data-options="iconCls:'icon-clear'" class="easyui-linkbutton">重置</a>
			<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeDialog()" class="easyui-linkbutton">关闭</a>
		</div>	
	</div>
<script type="text/javascript">
       $('#alterEnd').val("${inpatientInfo.alterBegin}");
       $('#alterBegin').val("${inpatientInfo.alterEnd}");
		$('#checkboxMoney').click(function(){
		    var bischecked=$('#checkboxTime').is(':checked');
			if(bischecked == true){
				$('#checkboxTime').attr('checked',false);
			}
			$('#alterType').val("M");
			if($('#alterType').val()=="M"){
				$("#money").show();
				$("#date").hide();
			}else{
				$("#money").hide();
			}
			
		});
			$('#checkboxTime').click(function(){
				 var bischecked=$('#checkboxMoney').is(':checked');
				 if(bischecked == true){
					$('#checkboxMoney').attr('checked',false);
				}
				 $('#alterType').val("D");
				 if($('#alterType').val()=="D"){
					$("#date").show();
					$("#money").hide();
				 }else{
					$("#date").hide();
				}
			});
			//表单提交submit信息
		  	function submit(){
			  	$('#editForm').form('submit',{
			  		url:"<c:url value='/inpatient/warnLine/saveWarnLine.action'/>",
			  		 onSubmit:function(){ 
			  			var endTime=$('#alterEnd').val();
			  			var sTime=$('#alterBegin').val();
			  			if(endTime !="" && sTime==""){
			  				$.messager.alert('操作提示', '请填写开始预警时间！'); 
			  				setTimeout(function(){
			  					$(".messager-body").window('close');
			  				},3500);
	                        return false; 
			  			}if(endTime =="" && sTime!=""){
			  				$.messager.alert('操作提示', '请填写结束预警时间！');  
			  				setTimeout(function(){
			  					$(".messager-body").window('close');
			  				},3500);
	                        return false; 
			  			}
			  			if(!compareTime(sTime,endTime)){
			  				$.messager.alert('操作提示', '开始预警时间晚于结束预警时间！'); 
			  				setTimeout(function(){
			  					$(".messager-body").window('close');
			  				},3500);
	                        return false; 
			  			}
			  			$.messager.progress({text:'正在设置警戒线 ,请稍等...',modal:true});
					 },  
					success:function(data){
						$.messager.progress('close');
						  if(data=="no"){
							  $.messager.alert('提示',"设置失败!");
							  closeDialog();
						  }else if(data=="yes"){
							  $.messager.alert('提示',"设置成功！");
							  setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							  $('#infolist').datagrid('reload');
							  closeDialog();
						  }
					 },
					error:function(date){
						$.messager.progress('close');
						$.messager.alert('提示',"保存失败");
					}
			  	});
		  	}
	  //清除所填信息
		function cleard() {
			$('#editForm').form('reset');
		}
	  //比较时间
		function compareTime(sTime,endTime){
			var d1 = new Date(sTime.replace(/\-/g, "\/")); 
			var d2 = new Date(endTime.replace(/\-/g, "\/"));
			if(d1>d2){
				return false;
			}else{
				return true;
			}
		}
</script>
</body>
</html>
