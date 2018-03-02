<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
	    <div style="padding: 5px 5px 0px 5px;">
				<form id="search" method="post">
					<table style="width:100%;border:1px solid #95b8e7;padding:10px;">
						<tr>
							<td style="padding: 0px 0px 0px 100px;">
								<a href="javascript:void(0)" onclick="searchFrom()"  class="easyui-linkbutton" >摆药单</a>
							</td>
						</tr>
					</table>
				</form>
		</div>   
	    <div title="摆药台列表" style="padding: 5px 5px 5px 5px;">
			<table id="phaStoContolList" data-options="toolbar:'#toolbarId'"></table>
		</div>  
	</div>
	
	<script type="text/javascript">
		function searchFrom(){
			var row = $('#phaStoContolList').datagrid("getSelections");
			var controlName = row[0].controlName;
			if(row==null||row==""){
				$.messager.alert('提示信息',"还未选择摆药台  ");
			}else{
				window.location.href="<%=basePath %>outpatient/Singlependulum.action?controlName="+controlName; 
			}
		}
		//页面加载
		$(function(){
			$('#phaStoContolList').datagrid({
				striped : true,
				checkOnSelect : true,
				selectOnCheck : true,
				singleSelect : true,
				fitColumns : false,
				pagination : true,
				rownumbers : true,
				pageSize : 10,
				pageList : [ 10, 20, 30, 50, 100 ],
				url : '<%=basePath %>inpatient/billpha/queryPhaStoConstrolView.action',
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'id',
					title : 'id',
					width : '10%',
					hidden : true
				}, {
					field : 'controlName',
					title : '摆药台名称',
					width : '12%'
				}, {
					field : 'controlAttr',
					title : '摆药台属性',
					width : '12%',
					formatter : function(value, row, index) {
						switch (value) {
						case 'G':
							text = '一般摆药';
							break;
						case 'S':
							text = '特殊药品摆药';
							break;
						case 'O':
							text = '出院带药摆药';
							break;
						default:
							text = '';
							break;
						}
						return text;
					}
				}, {
					field : 'sendType',
					title : '发送类型',
					width : '12%',
					formatter : function(value, row, index) {
						switch (value) {
						case 1:
							text = '集中发送';
							break;
						case 2:
							text = '临时发送';
							break;
						default:
							text = '';
							break;
						}
						return text;
					}
				}, {
					field : 'showLevel',
					title : '显示级别',
					width : '12%',
					formatter : function(value, row, index) {
						switch (value) {
						case 0:
							text = '显示科室汇总';
							break;
						case 1:
							text = '显示科室明细';
							break;
						case 2:
							text = '显示患者明细';
							break;
						default:
							text = '';
							break;
						}
						return text;
					}
				}, {
					field : 'mark',
					title : '备注',
					width : '12%'
				} ] ]
			});
		});
	</script>	
</body>
</html>