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
<title>病案归档</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/datagrid-detailview.js"></script>
<script type="text/javascript">
var sexMap = new Map();
$(function(){
	$('#dd').dialog('close');
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	setTimeout(function(){
		$('#list').datagrid({
			view:detailview,
	        fit:true,
	        fit:true,
	        rownumbers:true,
	        pagination:true,
	        pageSize:20,
			pageList:[20,30,50,80,100],
	        striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,
	        url: "<%=basePath%>emrs/recordmain/getInpatient.action",
	        detailFormatter:function(index,row){
	            return '<div style="padding:2px"><table id="edvc-' + index + '"></table></div>';  
	        },  
	        onExpandRow:function(index,row){
	            $('#edvc-'+index).datagrid({  
	            	url: "<%=basePath%>emrs/recordmain/getEmrMainByNo.action",
	            	queryParams:{inpatientNo : row.inpatientNo},
	                rownumbers:true,
		            striped:true,
		            border:true,
		            checkOnSelect:true,
		            selectOnCheck:false,
		            singleSelect:true,
	                height:'auto',  
	                columns:[[  
	                    {field:'emrType',title:'病历分类',width:'15%',halign:'center',align:'right'},  
	                    {field:'emrState',title:'状态',width:'10%',halign:'center',align:'right'},  
	                    {field:'emrScore',title:'评分',width:'10%',halign:'center',align:'right'},  
	                    {field:'emrLevel',title:'评分等级',width:'15%',halign:'center',align:'right'},  
	                    {field:'emrHigherDoc',title:'上级医师',width:'10%',halign:'center',align:'right'},  
	                    {field:'emrHigherTime',title:'主任医师',width:'10%',halign:'center',align:'right'},  
	                ]],  
	                onResize:function(){  
	                    $('#edv').datagrid('fixDetailRowHeight',index);  
	                },  
	                onLoadSuccess:function(){  
	                    setTimeout(function(){  
	                        $('#edv').datagrid('fixDetailRowHeight',index);  
	                    },0);  
	                }  
	            }); 
	            $('#edv').datagrid('fixDetailRowHeight',index);  
	        }
	    }); 
	},500);
});
function searchFrom(){
	$('#list').datagrid('reload',{deptCode:deptCode});
}
function submitApply(){
	var inpatientNo = $('#inpatientNo').val();
	$.ajax({
		url:"<%=basePath %>emrs/recordmain/saveEmrRecordMain.action",
		data:{"inpatientNo":inpatientNo},
		success:function(result){
			$.messager.alert('提示',result.resMsg,'info',function(){
				closeDialog();
				window.location.href = '<%=basePath %>emrs/recordmain/toViewMaintenanceList.action';
			});
			return;
		}
		,error: function(){
			$.messager.alert('提示','网络异常,请稍后重试...');
			return;
		}		
	});
}
//申请单个，申请前会加载病历首页
function application(){
	var row = $('#list').datagrid('getSelected');
	Adddilog('查看病案首页', "<%=basePath %>emrs/recordmain/toEmrFirstView.action?inpatientNo="+row.inpatientNo, '900px', '500px');
}
//一次申请多个，不会加载病历首页
function applyMany(){
	$.messager.confirm('提示','该功能将不能查看病案首页!并将直接提交病案申请...',function(r){
		if(r){
			var rows = $('#list').datagrid('getChecked');
			if(rows==null||rows.length<0){
				$.messager.alert('提示','请选择要提交申请的病历!');
				return ;
			}
			var inpatientNo = "";
			for(var i=0;i<rows.length;i++){
				if(inpatientNo!=""){
					inpatientNo += ',';
				}
				inpatientNo += rows[i].inpatientNo;
			}
			$.ajax({
				url:"<%=basePath %>emrs/recordmain/saveEmrRecordMain.action",
				data:{"inpatientNo":inpatientNo},
				success:function(result){
					$.messager.alert('提示',result.resMsg,function(){
						window.location.href = "<%=basePath %>emrs/recordmain/toViewMaintenanceList.action";
					});
					return;
				}
				,error: function(){
					$.messager.alert('提示','网络异常,请稍后重试...');
					return;
				}		
			});
		}else{
			return ;
		}
	});
	
}
/**
*加载模式窗口
*/
function Adddilog(title, url, width, height) {
	$('#dd').dialog({    
	    title: title,    
	    width: width,    
	    height: height,    
	    closed: false,    
	    cache: false,    
	    href: url,    
	    modal: true,
	   });
	$('#dd').window('center')
}
function closeDialog(){
	$('#dd').dialog('close');
}
///性别渲染
function formatterSex(value,row,index){
	return sexMap.get(value);
}
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;"fit=true>
		 <div data-options="region:'north',border:false" style="width:100%;height:40px;">
				<form id="search" method="post">
					<table style="width:100%;height:100%;border:none; padding:5px;" data-options="fit:true">
						<tr>
							<td  style="width: 300px;">住院号：<input class="easyui-textbox"ID="specCharName" name="specChar.specCharName"/>
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="application()" class="easyui-linkbutton" iconCls="icon-application_osx_add">申请</a>
								<a href="javascript:void(0)" onclick="applyMany()" class="easyui-linkbutton" iconCls="icon-application_osx_add">申请多个</a>
							</td>
							
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center',border:false" >
			<table id="list" fit="true" style="width:100%;" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'inpatientNo',width:'15%'">住院号</th>
						<th data-options="field:'patientName',width:'15%'">患者姓名</th>
						<th data-options="field:'reportSex',width:'8%',formatter:formatterSex">性别</th>
						<th data-options="field:'reportBirthday',width:'15%'">出生日期</th>
						<th data-options="field:'reportAge',width:'8%'">年龄</th>
						<th data-options="field:'inDate',width:'15%'">入院日期</th>
						<th data-options="field:'outDate',width:'15%'">出院日期</th>
					</tr>
				</thead>
			</table>
		</div>
		<div id="dd" class="easyui-dialog"></div>
</body>
</html>