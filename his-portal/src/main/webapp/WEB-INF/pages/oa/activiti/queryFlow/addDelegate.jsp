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
<title>添加我的代理</title>
</head>
<body style="text-align:center">

<form id="userRepoForm" method="post" class="form-horizontal">
  <c:if test="${oadeInfo != null}">
  <input id="userRepo_id" type="hidden" name="oadeInfo.id" value="${oadeInfo.id}">
  </c:if><br>
  <div class="form-group">
	<label class="control-label col-md-4" for="bpm-process_name">委托人</label>
    <div class="col-sm-3">
      <input id="bpm-process_name" class="easyui-textbox" name="oadeInfo.attorneyName" value="${oadeInfo.attorneyName}" size="40" data-options="prompt:'请按回车查询'" style = "width:400px" ata-options="required:true" minlength="1" maxlength="20" placeHolder="回车选择人员">
    </div>
  </div>
  <div class="form-group">
	<label class="control-label col-md-4 "  for="bpm-process_bpmCategoryId" >开始时间</label>
    <div class="col-sm-3">
     	<input id="startTimeMore" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1099-12-30 00:00:00',maxDate:'2099-12-30 00:00:00'})" 
				 name="oadeInfo.startTime" value="${oadeInfo.startTime}" class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;width:400px"/>
    </div>
  </div>
  <div class="form-group">
	<label class="control-label col-md-4 "  for="bpm-process_bpmCategoryId" >结束时间</label>
    <div class="col-sm-3">
     	<input id="endTimeMore" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1099-12-30 00:00:00',maxDate:'2099-12-30 00:00:00'})" 
				 name="oadeInfo.endTime" value="${oadeInfo.endTime}" class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;width:400px"/>
    </div>
  </div>

  <div class="form-group">
	<label class="control-label col-md-4" for="bpm-process_bpmConfBaseId">业务名称</label>
    <div class="col-sm-3">
	  <input class = "easyui-combobox" id="bpm-process_proDefId" name="oadeInfo.processDefinitionId" value="${oadeInfo.processDefinitionId}" style="width:400px"/>
    </div>
  </div>
  <div class="form-group">
	<label class="control-label col-md-4" for="bpm-process_bpmConfBaseId">环节</label>
    <div class="col-sm-3">
	 <input class="easyui-combobox" id="process_bpmConfBaseId" name="oadeInfo.taskDefinitionKey" value = "${oadeInfo.taskDefinitionKey}"  style="width:400px"/> 
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-11">
      <button id="submitButton" type="button" onclick="submitFrom();" class="btn a-submit">保存</button>
	  &nbsp;&nbsp;&nbsp;
      <button type="button" onclick="closeDialogs();" class="btn a-cancel">关闭</button>
    </div>
  </div>
  <input type="hidden" name ="oadeInfo.processName" value = "${oadeInfo.processName}" id="processName"/>
  <input type="hidden" name ="oadeInfo.activityName" value = "${oadeInfo.activityName}" id="activityName"/>
  <input type="hidden" name ="oadeInfo.attorney" value = "${oadeInfo.attorney}" id="attorney"/>
</form>
<script type="text/javascript">
$(function(){
	var bpm=${bpmjson}
	//回车事件
	bindEnterEvent("bpm-process_name",popWinToEmp,"easyui");
	//
	$("#bpm-process_proDefId").combobox({
		url:"<%=basePath %>activiti/delegateInfo/queryDeleList.action",
		valueField:'processDefinitionId',    
	    textField:'processName',
	    mode:'local',
	    async:false,
	    
	});
 	$("#process_bpmConfBaseId").combobox({
		<%-- url: '<%=basePath %>activiti/delegateInfo/OaBpmConfNodeList.action?processDefinitionId='+proData, --%>
		data:bpm,
		valueField:'code',    
	    textField:'name',
	    onSelect:function(data1){
	    	$("#activityName").val(data1.name);
	    }
	}); 
})
$('#bpm-process_proDefId').combobox({
    onSelect:function(data){
    	$("#processName").val(data.processName);
    	$("#activityName").val("");
    	$("#process_bpmConfBaseId").combobox("clear")
		$.ajax({
			url: '<%=basePath %>activiti/delegateInfo/OaBpmConfNodeList.action?processDefinitionId='+data.processDefinitionId,
			type:'post',
			success: function(dataBase) {
				
				//$("#process_bpmConfBaseId").combobox("setValue","")
				/* proData=dataBase;
				var str = ""
				for(var i = 0 ;i<dataBase.length;i++){
					str += '<option value='+ dataBase[i]['code'] +' >'+ dataBase[i]['name']  +'</option>'
				}
				$("#bpm-process_bpmConfBaseId").html(str) */
				$("#process_bpmConfBaseId").combobox({
					data:dataBase,
					valueField:'code',    
				    textField:'name',
				    mode:'local',
				    onLoadSuccess:function(data){
				    	$("#process_bpmConfBaseId").combobox("clear")
				    },
				    onSelect:function(data1){
				    	$("#activityName").val(data1.name);
				    }
				
				});
			}
		});
		
    }
});
//关闭dialog
function closeDialogs() {
	window.close();  
}
function submitFrom() {
	var startTime=$('#startTimeMore').val();
	var endTime=$('#endTimeMore').val();
	if(startTime&&endTime){
	    if(startTime>endTime){
	      $.messager.alert("提示","开始时间不能大于结束时间！");
	      return ;
	    }
	  }
	
	$('#userRepoForm').form('submit', {
		url : "<c:url value='/activiti/delegateInfo/addMydelegate.action'/>",
		success : function(data) {
			if (data.resMsg='success') {
				var tmpId ="#Mydelegate";
				window.opener.$(tmpId).datagrid('reload');
				
				//window.opener.location.reload();
				$.messager.alert('提示',"保存成功",'info',function(){
					closeDialogs();
				});
			} else {
				$.messager.alert('提示',"保存失败",'info',function(){
					closeDialogs();
				});
			}
		},
		error : function(data) {
			$.messager.alert('提示',"保存失败",'info',function(){
				closeDialogs();
			});
		}
	});

}

function popWinToEmp(){
	popWinEmpCallBackFn = function(rowData){
		if(rowData.length>0){
			var employeeName = "";
			var employeeJobNo = "";
			for(var i=0;i<rowData.length;i++){
				
				if(employeeName!=""){
					employeeName = employeeName+"," 
				}
				if(employeeJobNo!=""){
					employeeJobNo = employeeJobNo+"," 
				}
				employeeName = employeeName + rowData[i].employeeName;
				employeeJobNo = employeeJobNo + rowData[i].employeeJobNo;
			}
			 $('#bpm-process_name').textbox("setValue",employeeName);
			 $('#attorney').val(employeeJobNo);  
		}else{
			 $('#bpm-process_name').textbox("setValue",rowData.employeeName);
			 $('#attorney').val(rowData.employeeJobNo);  
		}
	
};
 	window.open ("${pageContext.request.contextPath}/activiti/delegateInfo/addExtendView.action",'newwindow1',' left=100,top=10,width='+ (screen.availWidth) +',height='+ (screen.availHeight-100) 
			+',scrollbars,resizable=yes,toolbar=yes') 
}
</script>
</body>
</html>