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
	<div class="easyui-layout" class="easyui-layout" fit=true style="width: 100%; height: 100%;">
		<div data-options="region:'center',border:false">
		<input type="hidden" id="drugDeptCode" name="drugDeptCode" value="${drugDeptCode }">
			<div style="width: 100%;height:100%; border:none;" >
				<table id="sendlist" data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false"></table>
			</div>
		</div>
	</div>
	
<script type="text/javascript">
	var deptCode = $('#dept').val();
	$(function(){
		//查询当前登录科室的发药窗口
		$('#sendlist').edatagrid({
			url:'<%=basePath %>drug/pharmacyManagement/queryTerminal.action?type=0&pid='+getSelected(1),
			selectOnCheck:false,rownumbers:true,idField: 'id',pageSize:20,border:false,
   			fitColumns:true,singleSelect:true,checkOnSelect:false,  pageNumber: 1, 
   			columns:[[{field:'id',hidden:true},
   			          {field:'code',hidden:true},
			          {field:'name',title:'名称', width : '25%'},
			          {field:'property',title:'终端性质', width : '20%',
			        	  formatter: function(value,row,index){
			        		  if(value==0){
			        			  return '普通'; 
			        		  }else if(value==1){
			        			  return '专科';
			        		  }else if(value==2){
			        			  return '特殊';
			        		  }
			        	  }
					  },
			          {field:'closeFlag',title:'是否关闭', width : '20%',
						  formatter: function(value,row,index){
			        		  if(value==0){
			        			  return '开放'; 
			        		  }else if(value==1){
			        			  return '关闭';
			        		  }
			        	  }  
			          },
			  ]],onDblClickRow:function(rowIndex, rowData){
				  if(rowData.closeFlag==1){
					  $.messager.alert('友情提示','该发药窗口以关闭，请重新选择');  
				  }else{
					  add(rowIndex, rowData);
					  
				  }
			  }
		});
	});
	
	<%-- 双击选择配药台begin*****************************************************************************************--%>	
	function add(rowIndex, rowData){
		//判断配药台是否是占用状态
		$.post("<%=basePath %>outpatient/dosage/getStoTerminalState.action?stoType=0&id="+rowData.id+"&drugDeptCode="+deptCode,function(data){
			if(data.resCode == "error"){//后台IP验证不通过
				$.messager.alert('友情提示',data.resMsg);
			}else{//后台IP验证通过
				//更新配置药台mark字段为1，即是使用状态
				$.post("<%=basePath %>outpatient/dosage/updateStoTerminal.action?id="+rowData.id+"&drugDeptCode="+deptCode+"&flag=0",function(data){
					$('#tt').tabs('enableTab',0);
					$('#tt').tabs('enableTab',1);
					$('#reload').linkbutton('enable');
					$('#unreload').linkbutton('enable');
					$('#ordonnance').linkbutton('enable');
					$('#save').linkbutton('enable');
					$('#exitStoTerminal').linkbutton('enable');
					$('#medicalrecord').textbox({disabled:false}); 
					bindEnterEvent('medicalrecord',tabTree,'easyui');
					$('#sendWicket').val(rowData.code);
					/* $('#sendWicketName').val(rowData.name); */
					$('#now').text('当前药房：'+getSelected(4)+'，当前登录窗口：'+rowData.name);
					tabTree();
					$('#layout1').layout('collapse','west');
					closeDialog();
				});
			}
		});
	}
	function getId(parameter) {
		var row = $("#sendlist").datagrid("getSelections");
		var i = 0;
		if (parameter == 'single') {//获得单个id
			if (row.length < 1) {
				$.messager.alert('提示',"请选择一条记录！");
				return null;
			} else if (row.length > 1) {
				$.messager.alert('提示',"只能选择一条记录！");
				return null;
			} else {
				var id = "";
				for (i; i < row.length; i++) {
					id += row[i].id;
					return id;
				}
			}
		} else if (parameter == 'plurality') {//获得多个id
			if (row.length < 1) {
				$.messager.alert('提示',"请至少选择一条记录！");
				return null;
			} else {
				var ids = "";
				for (i; i < row.length; i++) {
					ids += row[i].id + ",";
				}
				return ids;
			}
		} else if (parameter == 'notNull') {//至少获得一个id
			var id = "";
			if (row.length < 1) {//如果没有选择数据，默认选中第一行数据
				$('#list').datagrid('selectRow', 0);
				var row = $("#list").datagrid("getSelections");
			}
			id += row[0].id;
			return id;
		} else {
			$.messager.alert('提示',"参数无效！");
			return null;
		}
	}
</script>
</body>
</html>