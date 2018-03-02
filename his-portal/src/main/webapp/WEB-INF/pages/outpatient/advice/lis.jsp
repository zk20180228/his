<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div class="easyui-layout" data-options="fit:true">   
		
			<div data-options="region:'west',split:false,border:true" style="width:400px;border-left:0" class="lis">
				<div data-options="region:'north'" style="padding: 5px;">
					<input id="lisQueryName" style="width: 111px">
					<a href="javascript:void(0)" onclick="lisSearchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</div>
				<table id="lisPatEdId" class="easyui-datagrid">
					<thead>
						<tr>
							<th data-options="field:'no',width:50">门诊号</th>
							<th data-options="field:'pro',width:100">项目</th>
							<th data-options="field:'name',width:80">姓名</th>
							<th data-options="field:'sex',width:35">性别</th>
							<th data-options="field:'age',width:35">年龄</th>
							<th data-options="field:'dept',width:80">送检科室</th>
						</tr>
					</thead>
				</table>
			</div>
			<div data-options="region:'center',border:false">
				<table id="lisInfoEdId">
					<thead>
						<tr>
							<th data-options="field:'code',width:80,halign:'center',align:'right'">项目编号</th>
							<th data-options="field:'name',width:180,halign:'center',align:'right'">项目名称</th>
							<th data-options="field:'result',width:50,halign:'center',align:'right',styler:stylerResult">结果</th>
							<th data-options="field:'state',width:50,halign:'center',align:'center',styler:stylerState,formatter:forState">状态</th>
							<th data-options="field:'lower',width:80,halign:'center',align:'right'">参考下限</th>
							<th data-options="field:'upper',width:80,halign:'center',align:'right'">参考上限</th>
							<th data-options="field:'scope',width:120,halign:'center',align:'right'">输出范围</th>
							<th data-options="field:'unit',width:70,halign:'center',align:'right'">单位</th>
							<th data-options="field:'number',width:80,halign:'center',align:'right'">数字结果</th>
						</tr>
					</thead>
				</table>
			</div> 
		</div>
		<script type="text/javascript">
			$('#lisQueryName').textbox({prompt:'回车查询'});
			setTimeout(function(){bindEnterEvent('lisQueryName',lisSearchFrom,'easyui');},100);
			$('#lisInfoEdId').datagrid({
				striped:true,
				border:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				fit:true,
				onLoadSuccess:function(data){
				}
			});
			$('#lisPatEdId').datagrid({
				striped:true,
				border:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:true,
				pageSize:20,
				pageList:[20,40,80],
				fit:true,
				url:"<%=basePath%>outpatient/advice/findLis.action",
				onLoadSuccess:function(data){
					$(this).datagrid('selectRow',0);
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
				onSelect:function(rowIndex, rowData){
					if(rowData){
						$('#lisInfoEdId').datagrid({
							url:"<%=basePath%>outpatient/advice/findLisDetail.action",
							queryParams: {
								id: rowData.no
							}
						});
					}else{
						$('#lisInfoEdId').datagrid('loadData',[]);
					}
				}
			});
			function stylerResult(value,row,index){
				if(parseInt(value)>parseInt(row.upper)){
					return 'color:#FF0000';
				}else if(parseInt(value)<parseInt(row.lower)){
					return 'color:#0080FF;';
				}else{
					return '';
				}
			}
			function stylerState(value,row,index){
				if(value==1){
					return 'background-color:#FF0000;color:#000000;';
				}else if(value==-1){
					return 'background-color:#0080FF;color:#000000;';
				}else{
					return '';
				}
			}
			function forState(value,row,index){
				if(value==1){
					return '↑';
				}else if(value==-1){
					return '↓';
				}else{
					return '';
				}
			}
			function lisSearchFrom(){
				var lisQueryName =$('#lisQueryName').textbox('getValue'); 
				$('#lisPatEdId').datagrid('load', {
					queryName:lisQueryName
				});
			}
		</script>
	</body>
</html>