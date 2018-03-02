<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>模板选择</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div   style="height: 100%">
		<input id="erType" hidden="true" value="${erType }"/>
		<input id="patId" hidden="patId" value="${patId }"/>
			<table id="tempList"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'tempCode',width:'14%',align:'center'">编码</th>
						<th data-options="field:'tempName',width:'14%',align:'center'">名称</th>
						<th data-options="field:'tempErtype',width:'14%',align:'center',formatter:formatErtype">病历类型</th>
						<th data-options="field:'tempType',width:'14%',align:'center',formatter:formatType">模板分类</th>
						<th data-options="field:'tempDiag',width:'14%',align:'center'">诊断名称</th>
						<th data-options="field:'tempWritetype',width:'14%',align:'center',formatter:formatWritetype">书写类别</th>
						<th data-options="field:'strContent',hidden:'true' ,align:'center',">模板内容</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<script type="text/javascript">
	var tempErtypeList = "";
	
	//加载页面
	$(function(){
		var erType = $('#erType').val();
		var patId = $('#patId').val();
		$.ajax({
			url: "<%=basePath %>emrs/emrTemplate/tempErtypeFrmatter.action",
				type:'post',
				success: function(data) {
					tempErtypeList = data;
			}
		});
		$('#tempList').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath %>emrs/emrTemplate/queryTemplates.action?tempChkflg=1&erType='+erType,
			onLoadSuccess : function(data){
				if(data.total == 0){
					$.messager.alert("提示","该分类下暂无可用模板，请进入模板编辑添加！");
					setTimeout(function(){$(".messager-body").window('close')},3500);
				}
			},
			onDblClickRow: function(rowIndex,rowData){
				$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainEditView.action?erType='+rowData.tempErtype+'&ids='+rowData.id+'&patId='+patId);
				closeLayout(rowData.strContent);
			}
		});	
	});
	
	
		
	//渲染模板分类,0通用1科室2个人
	function formatType(val,row,index){
		if(val == '0'){
			return '通用'
		}
		if(val == '1'){
			return '科室'
		}
		if(val == '2'){
			return '个人'
		}
	}
	//书写类型  0不限次书写1仅首次单次书写2单次书写
	function formatWritetype(val,row,index){
		if(val == '0'){
			return '不限次书写'
		}
		if(val == '1'){
			return '仅首次单次书写'
		}
		if(val == '2'){
			return '单次书写'
		}
	}
	//渲染审签标志 0代签1已审核2退回
	function formatChkflg(val,row,index){
		if(val == '0'){
			return '代签'
		}
		if(val == '1'){
			return '已审核'
		}
		if(val == '2'){
			return '退回'
		}
	}
	//渲染类别
	function formatErtype(val,row,index){
		for(var i = 0;i < tempErtypeList.length;i++){
			if(val == tempErtypeList[i].encode){
				return tempErtypeList[i].name;
			}
		}
	}	
	/* 
	 * 关闭界面 并给编辑器赋值
	 */
	function closeLayout(content){
		$('#temWins').dialog('close'); 
// 		UE.getEditor('myTemplateDesign').reset();
// 		UE.getEditor('myTemplateDesign').setContent(content);
	}
	</script>
</body>