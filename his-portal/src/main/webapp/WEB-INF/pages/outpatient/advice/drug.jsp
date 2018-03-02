<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body style="margin: 0px;padding: 0px">
		<div id="drugEl" class="easyui-layout" data-options="fit:true,border:false">   
			<div data-options="region:'north'" style="height:42px;border-top:0px;border-left:0px;">
				<div style="padding:5px 5px 5px 5px;">
					<table>
						<tr>
							<td>
								药品查询：
								<input id="drugName" name="drugName" style="width:200px;" />
								<a href="javascript:void(0)" onclick="searchFromDrug()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</td>
						</tr>
					</table>
				</div>
			</div>   
			<div data-options="region:'center',border:false">
				<div style="width:100%;height:100%;">
					<table id="listdurg">
						<thead>
							<tr>
<!-- 								<th data-options="field:'name',halign:'center'" style="width:18%">名称</th> -->
<!-- 								<th data-options="field:'drugNamepinyin',align:'right',halign:'center'" style="width:12%" >名称拼音码</th> -->
<!-- 								<th data-options="field:'drugNamewb',align:'right',halign:'center'" style="width:12%">名称五笔码</th> -->
<!-- 								<th data-options="field:'drugNameinputcode',align:'right',halign:'center'" >名称自定义码</th> -->
								<th data-options="field:'drugCommonname',align:'right',halign:'center'" style="width:12%">通用名称</th>
								<th data-options="field:'drugCnamepinyin',align:'right',halign:'center'" style="width:10%">通用名称拼音码</th>
								<th data-options="field:'drugCnamewb',align:'right',halign:'center'" style="width:10%">通用名称五笔码</th>
								<th data-options="field:'drugCnameinputcode',align:'right',halign:'center'" style="width:12%">通用名称自定义码</th>
								<th data-options="field:'spec',align:'right',halign:'center'" style="width:10%" >规格</th>
								<th data-options="field:'drugRetailprice',align:'right',halign:'center'" style="width:10%" >零售价</th>
<!-- 								<th data-options="field:'drugRemark',align:'right',halign:'center'" style="width:8%" >药品性质</th> -->
<!-- 								<th data-options="field:'childrenprice',align:'right',halign:'center'" style="width:10%" >儿童价</th> -->
							</tr>
						</thead>
					</table>
				</div>
			</div> 
		</div>
		<script type="text/javascript">
		$('#drugName').textbox({
			prompt:'药品名称,拼音,五笔,自定义'
		});
		//加载页面
		$('#listdurg').datagrid({
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
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath%>drug/info/queryDrug.action',
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
				var row = $('#listdurg').datagrid('getSelected'); //获取当前选中行	 
				if(row){
					AdddilogDrugs("查看药品","<%=basePath%>drug/info/viewDrugInfo.action?id="+row.id);
				}
			}
		});
		bindEnterEvent('drugName',searchFromDrug,'easyui');
		
		</script>
	</body>
</html>