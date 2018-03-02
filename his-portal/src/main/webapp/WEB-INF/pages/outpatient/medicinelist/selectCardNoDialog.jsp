<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div>
		<table id="selectDialoglist" style="width: 100%;"></table>
	</div>
	
<script type="text/javascript">
	$(function(){
		var sexMap=new Map();
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
		//查询当前登录科室的发药窗口
		$('#selectDialoglist').edatagrid({
			data:SearchFromData,
			selectOnCheck:false,rownumbers:true,idField: 'id',pageSize:20,
   			fitColumns:true,singleSelect:true,checkOnSelect:false,  pageNumber: 1, 
   			columns:[[{field:'medicalrecordId',title:'病历号',width : '25%'},
			          {field:'patientName',title:'姓名', width : '25%'},
			          {field:'patientSex',title:'性别', width : '10%',
			        		formatter:function(value,row,index){
			        			return sexMap.get(value);
			        		}  
			          },
			          {field:'patientCertificatesno',title:'身份证号', width : '35%'},
			  ]],onDblClickRow:function(rowIndex, rowData){
				  searchblhIdOrjzkh(rowData.medicalrecordId,'');
				  closeDialog();
			  }
		});
	});
</script>
</body>
</html>