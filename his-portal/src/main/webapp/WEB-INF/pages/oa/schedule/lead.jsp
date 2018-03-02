<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form'" >
	<table style="width:100%;height:95%">
	<tr>
	<td style='width:180px'>${schedule.title}</td>
	<td style="width:70px" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="cc" class="easyui-combobox" name="flag"  style="width:70px" /></td>
	</tr>
	<tr>
	<td  style='width:180px'>${schedule.backgroundColor} 至  </td>
	<td>${schedule.textColor}</td>
	</tr>
	<tr>
	<td style='width:180px'>
	<a href="javascript:sure()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确认</a>
	</td>
	<td>
	&nbsp;&nbsp;&nbsp;
	<a href="javascript:edit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">编辑</a>
	<a href="javascript:del();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete'">删除</a>
	</td>
	</tr>
	</table>
		<form id="editForm" method="post">
			<input type="hidden" id="id" name="schedule.id" value="${t}">
			<input type="hidden" id="isFinish" name="schedule.isFinish" value="${schedule.isFinish}">
		</form>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/keydown.js"></script>
	<script type="text/javascript">
	$(function(){
		$("#cc").combobox({
			 url:'<c:url value='/oa/agenda/agendaAction/queryFlag.action'/>',    
			    valueField:'id',    
			    textField:'text',
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
			        var f=$("#isFinish").val();
	                for (var item in val[f]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[f][item]);  
	                    }  
	                }  
	            }  
		})
		
		
	})
	
   function del(){
	   var t=$("#id").val();
	   $.messager.confirm('确认','您确认想要删除吗？',function(r){    
		    if (r){ 
		       $.messager.progress({text:'删除中，请稍后...',modal:true});
		 	   $.ajax({
		           type: "POST",
		           url: "<c:url value='/oa/agenda/agendaAction/del.action'/>",
		           data: {t:t},
		           dataType: "json",
		           success: function(data){
		        	    $.messager.progress('close');
						if("success"==data.resCode){
							$.messager.alert('提示',data.resMsg);
							 window.location.href="<c:url value='/oa/agenda/agendaAction/agendaActionToView.action'/>";
						}else{
							$.messager.alert('提示',data.resMsg);
							closeLayout();
						}
		        	   }
		           })  
		    }else{
		    	closeLayout();
		    	return ;
		    }    
		});  
   }
   
   function edit(){
	   var id=$("#id").val();
	   closeLayout();
	   Adddilog("编辑","<c:url value='/oa/agenda/agendaAction/toEditView.action'/>?t="+id,'530','465');
   }
   function sure(){
	   var isFinish=$("#cc").combobox("getValue");
	   var id=$("#id").val();
	   var oldIsFinish="${schedule.isFinish}";
	   if(oldIsFinish==1){
			$.messager.alert('提示','已标记为过期事件!'); 
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
	    return;
	   }
	   $.ajax({
             type: "POST",
             url: "<c:url value='/oa/agenda/agendaAction/updateFlag.action'/>",
             data: {"flag":isFinish,"t":id}
	   });
	   closeLayout();
   }
	
</script>
</body>
</html>