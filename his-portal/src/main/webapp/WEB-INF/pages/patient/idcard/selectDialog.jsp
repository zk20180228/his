<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
var sexMap=new Map();
var codeCertificateMap=new Map();
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
//证件类型 certificate
$.ajax({
		url: "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"certificate"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				codeCertificateMap.put(v[i].encode,v[i].name);
			}
		}
});
</script>
</head>
<body>
	<div style="padding: 0px 5px 5px 5px;height: 98%">
		<table id="selectDialoglist" style="width: 100%;hight:100%;" data-options="fit:true"></table>
	</div>
	
<script type="text/javascript">
	$(function(){
		
		//查询多条患者的数据窗口
		$('#selectDialoglist').edatagrid({
			striped:true,
			border:true,
			data:SearchFromData,
			selectOnCheck:false,rownumbers:true,idField: 'id',
   			fitColumns:true,singleSelect:true,checkOnSelect:false,
   			columns:[[{field:'idcardNo',title:'就诊卡号',width : '10%'},
   			          {field:'medicalrecordId',title:'病历号',width : '15%'},
			          {field:'patientName',title:'姓名', width : '15%'},
			          {field:'patientSex',title:'性别', width : '10%',
			        		formatter:function(value,row,index){
			        			return sexMap.get(value);
			        		}  
			          },
			          {field:'patientCertificatestype',title:'证件类型', width : '15%',
			        	  formatter:function(value,row,index){
			        		  return codeCertificateMap.get(value);
			        	  }
			          },
			          {field:'patientCertificatesno',title:'证件号', width : '34%'},
			  ]],onDblClickRow:function(rowIndex, rowData){
				  searchFrom(rowData.medicalrecordId);
				  $.messager.progress({text:'查询中，请稍后...',modal:true});
				  closeDialog();
			  }
		});
	});
</script>
</body>
</html>