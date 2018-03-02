<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品执行单编辑页面</title>
</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'药品执行单明细',iconCls:'icon-form',fit:true" style="height: 100%;width: 100%" >
		<div style="padding:5px">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="inpatientDrugbilldetail.id" value="${inpatientDrugbilldetail.id}">
                <input type="hidden" id="typeName"  name="inpatientDrugbilldetail.typeName" >
                <input type="hidden" id="drugTypeName"  name="inpatientDrugbilldetail.drugTypeName" >
                <input type="hidden" id="billNo"  name="inpatientDrugbilldetail.billNo" value="${inpatientDrugbilldetail.billNo}" >
                <input type="hidden" id="billName"  name="inpatientDrugbilldetail.billName" value="${inpatientDrugbilldetail.billName}" >
                <input type="hidden" id="usageName"  name="inpatientDrugbilldetail.usageName" >
			    <table class="honry-table" style="width:390px;height: 240px; ;align:center;margin-left:auto;margin-right:auto;" >
			     	<tr style="margin-top:5px;">
						<td style="padding: 7px 5px;" class="changeskinbg" nowrap="nowrap">&nbsp;医嘱类型：</td>
						<td>
							<input id="docAdvType2" name="inpatientDrugbilldetail.typeCode" value="${inpatientDrugbilldetail.typeCode}" class="easyui-combobox" style="width:150px;" data-options="required:true" />
						</td>
					</tr>
			     	<tr style="margin-top:5px;">
						<td style="padding: 7px 5px;" class="changeskinbg" nowrap="nowrap">&nbsp;执行单类别：</td>
						<td>
							<input id="billType"  value="${inpatientDrugbilldetail.billType}"  name="inpatientDrugbilldetail.billType" class="easyui-combobox" style="width:150px;" data-options="required:true">
							</input>
						</td>
					</tr>
					<tr>
						<td style="padding: 7px 5px;" class="changeskinbg" nowrap="nowrap">&nbsp;项目类别：</td>
						<td>
							<input id="adProjectTdId2" name="inpatientDrugbilldetail.drugType" value="${inpatientDrugbilldetail.drugType}"  class="easyui-combobox" style="width:150px;" data-options="required:true" />
						</td>
					</tr>
				    <tr>
				    	<td  align="aligen" class="changeskinbg">服用方法/非药品名称：</td>
				    	<td>
				    	  <input id="usageName2" name="inpatientDrugbilldetail.usageCode" value="${inpatientDrugbilldetail.usageCode}"  class="easyui-combobox" style="width:150px;" data-options="required:true" />
				    	</td>
				    </tr>      
			    </table>
			    <div style="text-align:center;padding:5px;margin-top: 20px;">
			    	<a id='submits' href="javascript:submits()" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">批量添加</a>
			    	<a href="javascript:submit()" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clearEdit()" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	     	</form>
	    </div>
	</div>
<script type="text/javascript">
var page="${inpatientDrugbilldetail.billType}";
$(function(){ 
//编辑不可批量添加	
disSubmits();
//加载所有类型
$('#billType').combobox({
	url:"<%=basePath%>inpatient/doctorAdvice/queryAllType.action",
    valueField:'billType',    
    textField:'typeName',
    queryParams:{page:page},
    multiple:false
});
	$("#billType").combobox({
		onLoadSuccess:function(){
			n=$(this).combobox("getValue");
			if(n==1){
				$('#adProjectTdId2') .combobox({    
					url: "<%=basePath%>inpatient/doctorAdvice/queryDrugType.action", 
				    valueField:'encode',    
				    textField:'name',
				    multiple:false
				});
				$('#usageName2') .combobox({    
					url: "<%=basePath%>inpatient/doctorAdvice/queryDrugUsage.action?", 
				    valueField:'encode',    
				    textField:'name',
				    multiple:false
				});
			}else{
			//系统类型下拉框 systemType
			$('#adProjectTdId2') .combobox({    
				url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=systemType", 
			    valueField:'encode',    
			    textField:'name',
			    multiple:false
			});
			
			//记载非药品名称	
			$('#usageName2') .combobox({    
					url: "<%=basePath%>inpatient/doctorAdvice/queryAllUndrug.action?", 
				    valueField:'code',    
				    textField:'name',
				    multiple:false
				});
				
			}
		},
		onChange: function (n,o) {
			if(n==1){
				$('#adProjectTdId2') .combobox({    
					url: "<%=basePath%>inpatient/doctorAdvice/queryDrugType.action?", 
				    valueField:'encode',    
				    textField:'name',
				    multiple:false
				});
				
				$('#usageName2') .combobox({    
					url: "<%=basePath%>inpatient/doctorAdvice/queryDrugUsage.action?", 
				    valueField:'encode',    
				    textField:'name',
				    multiple:false
				});
			}else{
			//系统类型下拉框 systemType
			$('#adProjectTdId2') .combobox({    
				url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=systemType", 
			    valueField:'encode',    
			    textField:'name',
			    multiple:false
			});
			
			//记载非药品名称	
			$('#usageName2') .combobox({    
					url: "<%=basePath%>inpatient/doctorAdvice/queryAllUndrug.action?", 
				    valueField:'code',    
				    textField:'name',
				    multiple:false
				});
				
			}
		
		}
		});


//长期医嘱类型下拉框
$('#docAdvType2').combobox({
	url:'<%=basePath%>inpatient/docAdvManage/queryDocAdvType.action', 
    valueField:'typeCode',    
    textField:'typeName',
    multiple:false
});
	});
//清除所填信息
function clearEdit(){
	$('#editForm').form('clear');
}
//关闭
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
//保存
function submit(){
	var advType= $("#docAdvType2").combobox("getText");
	var adP=$("#adProjectTdId2").combobox("getText");
	var useName=$("#usageName2").combobox("getText");
	$("#drugTypeName").val(adP);
	$("#typeName").val(advType);
	$("#usageName").val(useName);
  	$('#editForm').form('submit',{
 		url: "<%=basePath%>inpatient/doctorAdvice/saveDrugBillDetail.action",
  		 onSubmit:function(){ 
		     return $(this).form('validate');  
		 },  
		success:function(){
			$.messager.alert('提示',"保存成功");
			closeLayout();
			 //实现刷新
			reloadList();
		 },
		error:function(data){
			$.messager.alert('提示',"保存失败");
		}
  	});
 }
//批量保存
function submits(){
	var advType= $("#docAdvType2").combobox("getText");
	var adP=$("#adProjectTdId2").combobox("getText");
	var useName=$("#usageName2").combobox("getText");
	$("#drugTypeName").val(adP);
	$("#typeName").val(advType);
	$("#usageName").val(useName);
  	$('#editForm').form('submit',{
 		url: "<%=basePath%>inpatient/doctorAdvice/saveDrugBillDetail.action",
  		 onSubmit:function(){ 
		     return $(this).form('validate');  
		 },  
		success:function(){
			$.messager.alert('提示',"保存成功");
			 //实现刷新
			reloadList();
			$('#docAdvType2').combobox('clear');  
			$('#docAdvType2').combobox('reload'); 
			$('#adProjectTdId2').combobox('clear');  
			$('#adProjectTdId2').combobox('reload'); 
			$('#usageName2').combobox('clear');  
			$('#usageName2').combobox('reload'); 
			$('#billType').combobox("readonly",true); 
		 },
		error:function(data){
			$.messager.alert('提示',"保存失败");
		}
  	});
}
	//刷新
	function reloadList(){
	//实现刷新栏目中的数据
	$('#dg').datagrid('reload');
	}
	//当修改时不可批量添加
	function disSubmits(){
		var selected=$("#id").val();
		if(selected!=null&&selected!=""){
			$("#submits").attr("disabled","disabled");
		}else{
			$("#submits").attr("disabled",false);
		}
}
</script>
</body>
</html>