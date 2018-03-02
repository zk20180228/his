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
<title></title>
<script type="text/javascript">
var processid = '${rowsid}';
$(function(){
	$('#cuibanFinish').datagrid({
		method: 'post',
		fit:true,
		rownumbers: true,
		pagination : true,
		pageSize : 20,
		pageList : [ 20,40,60,100 ],
		url: '<%=basePath%>activiti/queryFlow/getMyCuiBanByProecssId.action',
		queryParams:{rowsid : processid},
		onLoadSuccess:function(data){
			var pager = $(this).datagrid('getPager');
			var aArr = $(pager).find('a');
			var iArr = $(pager).find('input');
			$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
			for(var i=0;i<aArr.length;i++){
				$(aArr[i]).tooltip({
					content:toolArr[i],
					hideDelay:1
				});
				$(aArr[i]).tooltip('hide');
			}
// 			var rows = data.rows;
// 			if(rows.length>0){
// 				for(var i=0;i<rows.length;i++){
// 					$(this).datagrid('updateRow',{
// 						index: $(this).datagrid('getRowIndex',rows[i]),
// 						row: {
// 							operation : '<a class="sickCls9" onclick="deletecui(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'
// 						}
// 					});
// 				}
// 			$('.sickCls9').linkbutton({text:'删除',plain:false,width:'50px',height:'20px'}); 
// 			}
		}
	});
});
function deletecui(id){
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	    if (r){    
	    	$.ajax({
				url : '<%=basePath%>activiti/queryFlow/deleteMyCuiBan.action',
				data : {rowsid : id},
				success : function(result){
					if(result.resCode=="success"){
						$.messager.alert('提示','删除成功!','info');
						$('#cuibanFinish').datagrid('reload');
					}else{
						$.messager.alert('提示','删除失败!','info');
					}
				},
				error : function(){
					$.messager.alert('提示','网络异常，请稍后重试...','info');
					return ;
				}
				
			});
	    }    
	});
}
function reminderstatus(value){
	var text = '';
	switch (value) {
		case 0:
			text = '未读';
			break;
		case 1:
			text = '已读';
			break;
		default:
			text='未读';
			break;
		}
		return text;
}
function returnstatus(value){
	var text = '';
	if(value==null){
		text = '未回';
	}else{
		text = '已回';
	}
	return text;
}
</script>
</head>
<body>
	<div style="width:100%;height:100%;">
		<table id="cuibanFinish" class="easyui-datagrid" style="width:100%;height:100%;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false">
			<thead>
				<tr>
					<th data-options="field:'procedureName',width:'22%'" align="left" halign="center">事务标题</th>
					<th data-options="field:'remindTime',width:'12%'" align="left" halign="center">催办时间</th>
					<th data-options="field:'reminderNum',width:'5%'" align="center" halign="center">催办次数</th>
					<th data-options="field:'remindenodeName',width:'12%'" align="center" halign="center">催办环节</th>
					<th data-options="field:'remindcontent',width:'10%'" align="center" halign="center">催办内容</th>
					<th data-options="field:'reminderdName',width:'8%'" align="center" halign="center">被催办人员</th>
					<th data-options="field:'remindreStatus',formatter:reminderstatus,width:'5%'" align="center" halign="center">催办状态</th>
					<th data-options="field:'remidereTime',formatter:returnstatus,width:'5%'" align="center" halign="center">回复状态</th>
					<th data-options="field:'remindreContent',width:'13%'" align="center" halign="center">回复内容</th>
<!-- 					<th data-options="field:'operation',width:'10%'" align="center" halign="center">操作</th> -->
				</tr>
			</thead>
		</table>
	</div>
</body>
</html>