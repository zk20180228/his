<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历病历借阅审核</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		//加载页面
		$(function(){
			//添加datagrid事件及分页
			$('#list').datagrid({
				pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				queryParams : {'state' : '0'},
				onBeforeLoad:function(){
					$('#list').datagrid('clearChecked');
					$('#list').datagrid('clearSelections');
				},
				onDblClickRow:function(rowIndex, rowData){
					if(rowData.inoutState == '1'){
						$.messager.alert('提示','您选中的档案已借出！');
						setTimeout(function(){$(".messager-body").window('close')},3500);
					}else{
						AdddilogModel("<%=basePath%>emrs/emrRecInOut/toApproveView.action?id="+rowData.id,'600px','370px;');
					}
				},
				onLoadSuccess:function(row, data){
					//分页工具栏作用提示
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
				}
			});
		});
		//加载模式窗口
		function AdddilogModel(url,width,height) {
			$('#tempWin').dialog({	
				title: '审核申请',
				width: width,
				height: height,
				closed: false,
				cache: false,
				href: url,
				modal: true
			});  
		}
		//关闭模式窗口
		function closeDilog() {
			$('#tempWin').window('close');
		}

		//刷新
		function reload(){
			//实现刷新栏目中的数据
			$('#list').datagrid('unselectAll');
			$('#list').datagrid('reload');
		}
		function formatInoutState(value){
			if(value != '1'){
				return '可借阅';
			}else{
				return '已借出';
			}
		}
		
	</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;">
		<div data-options="region:'center',border:false" style="height: 100%">
			<input type="hidden" id="infoJson" name="infoJson"/>
			<table id="list" fit="true" data-options="url:'<%=basePath %>emrs/emrRecInOut/emrRecInOutList.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:false,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'inoutRecid',width:'15%'">档案编号</th>
						<th data-options="field:'cardId',width:'15%'">住院流水号</th>
						<th data-options="field:'patientName',width:'10%'">患者姓名</th>
						<th data-options="field:'appperson',width:'10%'">借阅申请人</th>
						<th data-options="field:'inoutAppdate',width:'10%'">借阅申请时间</th>
						<th data-options="field:'inoutState',formatter:formatInoutState,width:'10%'">档案状态</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="tempWin"></div>
</body>

