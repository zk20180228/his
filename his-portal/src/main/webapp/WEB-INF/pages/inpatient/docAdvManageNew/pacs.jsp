<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'west',split:false,border:false" style="width:340px;" class="pacs">
				<div data-options="region:'north',border:false" style="padding: 5px;">
					<input id="pacsQueryName" >
					&nbsp;&nbsp;<a href="javascript:void(0)" onclick="pacsSearchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</div>
				<table id="pacsPatEdId" class="easyui-datagrid">
					<thead>
						<tr>
							<th data-options="field:'no',width:100">病历号</th>
							<th data-options="field:'name',width:100">姓名</th>
							<th data-options="field:'sex',width:60">性别</th>
							<th data-options="field:'age',width:60">年龄</th>
						</tr>
					</thead>
				</table>
			</div>
			<div data-options="region:'center',border:false" class="pacsTable">
				<table id="pacsInfoEdId">
					<thead>
						<tr>
							<th data-options="field:'no',width:80,halign:'center',align:'left'">检查号</th>
							<th data-options="field:'type',width:80,halign:'center',align:'left'">检查类型</th>
							<th data-options="field:'part',width:100,halign:'center',align:'left'">检查部位</th>
							<th data-options="field:'use',width:100,halign:'center',align:'left'">送检人</th>
							<th data-options="field:'dept',width:150,halign:'center',align:'left'">送检科室</th>
							<th data-options="field:'device',width:150,halign:'center',align:'left'">检查设备</th>
							<th data-options="field:'cost',width:100,halign:'center',align:'right'">费用</th>
							<th data-options="field:'handle',width:120,halign:'center',align:'center'">操作</th>
							<th data-options="field:'dat',width:150,halign:'center',align:'left'">登记时间</th>
						</tr>
					</thead>
				</table>
			</div> 
		</div>
		<div id="pacsWinId">
			<div class="easyui-layout" data-options="fit:true">   
				<div data-options="region:'west',split:false" style="width:220px;padding-top:5px;padding-left:5px;">
					<table>
						<tr>
							<td align="right">姓名：</td>
							<td id="pacsWinNameId"></td>
						</tr>
						<tr>
							<td align="right">性别：</td>
							<td id="pacsWinSexId"></td>
						</tr>
						<tr>
							<td align="right">年龄：</td>
							<td id="pacsWinAgeId"></td>
						</tr>
						<tr>
							<td align="right">检查号：</td>
							<td id="pacsWinNoId"></td>
						</tr>
						<tr>
							<td align="right">检查类型：</td>
							<td id="pacsWinTypeId"></td>
						</tr>
						<tr>
							<td align="right">检查部位：</td>
							<td id="pacsWinPartId"></td>
						</tr>
						<tr>
							<td align="right">送检人：</td>
							<td id="pacsWinUserId"></td>
						</tr>
						<tr>
							<td align="right">送检科室：</td>
							<td id="pacsWinDeptId"></td>
						</tr>
						<tr>
							<td align="right">检查设备：</td>
							<td id="pacsWinDeviceId"></td>
						</tr>
						<tr>
							<td align="right">费用：</td>
							<td id="pacsWinCostId"></td>
						</tr>
						<tr>
							<td align="right">登记时间：</td>
							<td id="pacsWinDateId"></td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center'" style="padding:5px;background:#eee;text-align:center;"><img id="pacsImgId" style="vertical-align:middle;"/></div> 
			</div>
		</div> 
		<script type="text/javascript">
		$('#pacsQueryName').textbox({prompt:'回车查询'});
		setTimeout(function(){bindEnterEvent('pacsQueryName',pacsSearchFrom,'easyui');},100);
		$('#pacsInfoEdId').datagrid({
			striped:true,
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fit:true,
			onLoadSuccess:function(data){
				var rows = data.rows;
				for(var i=0;i<rows.length;i++){
					var index = $(this).datagrid('getRowIndex',rows[i]);
					$(this).datagrid('updateRow',{index:index,row:{handle:'<a class="cls" onclick="look('+index+')" href="javascript:void(0)" style="height:20"></a>'}});
				}
				$('.cls').linkbutton({text:'查看',plain:true,iconCls:'icon-application_form_magnify'}); 
			}
		});
		$('#pacsPatEdId').datagrid({
			striped:true,
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			pagination:true,
			pageSize:20,
			pageList:[20,40,80],
			fit:true,
			url:"<%=basePath%>baseinfo/outpatientAdvice/outpatientAdvicePacsList.action",
			queryParams:{type:"1"},
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
					$('#pacsInfoEdId').datagrid({
						url:"<%=basePath%>baseinfo/outpatientAdvice/outpatientPacsDetailList.action",
						queryParams: {
							id: rowData.id
						}
					});
				}else{
					$('#pacsInfoEdId').datagrid('loadData',[]);
				}
				
			}
		});
		function look(index){
			var row = $('#pacsPatEdId').datagrid('getSelected');
			var rows = $('#pacsInfoEdId').datagrid('getRows');
			$('#pacsWinId').window({
				title:rows[index].device+'影像',
				width:'100%',
				height:'100%',
				modal:true,
				border:true,
				collapsible:false,
				minimizable:false,
				maximizable:false
			});
			$('#pacsImgId').attr("src",'<%=basePath%>'+rows[index].image);
			$('#pacsWinNameId').html(row.name);
			$('#pacsWinSexId').html(row.sex);
			$('#pacsWinAgeId').html(row.age);
			$('#pacsWinNoId').html(rows[index].no);
			$('#pacsWinTypeId').html(rows[index].type);
			$('#pacsWinPartId').html(rows[index].part);
			$('#pacsWinUserId').html(rows[index].user);
			$('#pacsWinDeptId').html(rows[index].dept);
			$('#pacsWinDeviceId').html(rows[index].device);
			$('#pacsWinCostId').html(rows[index].cost);
			$('#pacsWinDateId').html(rows[index].date);
		}
		
		function pacsSearchFrom(){
			var pacsQueryName =$('#pacsQueryName').textbox('getValue'); 
			$('#pacsPatEdId').datagrid('load', {
				queryName:pacsQueryName,
				type:"1"
			});
		}
		</script>
	</body>
</html>