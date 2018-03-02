<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	</head>
	<body style="margin:0px;padding:0px">
		<div id="unDrugEl" class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'north'" style="height:42px;border-top:0px;border-left:0px;">
				<div style="padding:5px 5px 5px 5px;">
					<table>
						<tr>
							<td style="width: 500px;">非药品查询：
						 	<input id="undrugName" name="undrugName" style="width: 200px;" />
							<a href="javascript:void(0)" onclick="searchFromNondrug()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</td>
						</tr>
					</table>
				</div>
			</div>   
			<div data-options="region:'center',border:false">
				<div style="width:100%;height:100%;">
					<table id="listnon">
					<thead>
						<tr>
							<th data-options="field:'name',halign:'center'" style="width:15%">项目名称</th>
							<th data-options="field:'undrugPinyin',align:'right',halign:'center'" style="width:10%">拼音码</th>
							<th data-options="field:'undrugWb',align:'right',halign:'center'" style="width:10%">五笔码</th>
							<th data-options="field:'undrugInputcode',align:'right',halign:'center'" style="width:10%">自定义码</th>
							<th data-options="field:'defaultprice',align:'right',halign:'center'" style="width:10%">默认价</th>
							<th data-options="field:'undrugState',formatter:function(value,row,index){
																								if (value=='1'){
																									return '在用';
																								}if (value=='2'){
																									return '停用';
																								} if (value=='3'){
																									return '废弃';
																								} 
																							},align:'right',halign:'center'" style="width:8%">状态</th>
							<th data-options="field:'undrugIsownexpense',formatter:replaceTrueOrFalse,align:'right',halign:'center'" style="width:8%">自费</th>
							<th data-options="field:'undrugIsc',formatter:replaceTrueOrFalse,align:'right',halign:'center'" style="width:8%">组套</th>
							<th data-options="field:'undrugDiseaseclassification',formatter:diseasetypeFamater,align:'right',halign:'center'" style="width:8%">疾病分类</th>
<!-- 							<th data-options="field:'undrugRemark',align:'right',halign:'center'" style="width:10%">备注</th> -->
						</tr>
					</thead>
				</table>
				</div>
			</div> 
		</div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/keydown.js"></script>
		<script type="text/javascript">
		var diseaseclassificationMap = null;
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",
			data:{"type" : "diseasetype"},
			type:'post',
			success: function(diseasetypeData) {
				diseaseclassificationMap = diseasetypeData ;
			}
		});
		$('#undrugName').textbox({
			prompt:'名称,拼音,五笔,自定义'
		});
		//添加datagrid事件及分页
		$('#listnon').datagrid({
			method:'post',
			rownumbers:true,
			idField: 'id',
			striped:true,
			border:false,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			fit:true,
			pagination:true,
			url:"<%=basePath%>drug/undrug/queryunDrug.action",
			pageSize:20,
			pageList:[20,30,50,80,100],
			onLoadSuccess : function(data){
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
			},
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				var row = $('#listnon').datagrid('getSelected'); //获取当前选中行	 
				if(row){
					AdddilogUndrug("查看非药品","<%=basePath%>drug/undrug/viewUndrug.action?id="+row.id);
			}
			}
		});
		
		bindEnterEvent('undrugName',searchFromNondrug,'easyui');
		</script>
	</body>
</html>