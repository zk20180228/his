<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div class="easyui-layout" data-options="fit:true">   
		
			<div data-options="region:'west',split:false,border:false" style="width:400px;" class="lis">
				<div data-options="region:'north',border:false" style="padding: 5px;">
					<input id="lisQueryName">
					&nbsp;&nbsp;<a href="javascript:void(0)"
					onclick="lisSearchFrom()" class="easyui-linkbutton"
					iconCls="icon-search">查询</a>
			</div>
				<table id="lisPatEdId" class="easyui-datagrid">
					<thead>
						<tr>
							<th data-options="field:'INPATIENT_ID',width:50">病历号</th>
							<th data-options="field:'TEST_ORDER_NAME',width:100">项目</th>
							<th data-options="field:'PATIENT_NAME',width:80">姓名</th>
							<th data-options="field:'PATIENT_SEX',width:35,	 formatter:forState">性别</th>
							<th data-options="field:'AGE_INPUT',width:35">年龄</th>
							<th data-options="field:'PATIENT_DEPT_NAME',width:80">送检科室</th>
						</tr>
					</thead>
				</table>
			</div>
			<div data-options="region:'center',border:false" class="lisTable">
				<table id="lisInfoEdId">
					<thead>
						<tr>
							<th data-options="field:'code',width:80,halign:'center',align:'left'">项目分类</th>
							<th data-options="field:'name',width:200,halign:'center',align:'left'">项目名称</th>
							<th data-options="field:'result',width:80,halign:'center',align:'right',styler:stylerResult">原始结果</th>
							<!-- <th data-options="field:'state',width:50,halign:'center',align:'center',styler:stylerState,formatter:forState">状态</th> -->
							<th data-options="field:'lower',width:100,halign:'center',align:'right'">详情结果</th>
							<th data-options="field:'upper',width:100,halign:'center',align:'right'">参考范围</th>
							<!-- <th data-options="field:'scope',width:120,halign:'center',align:'left'">输出范围</th> -->
							<th data-options="field:'unit',width:80,halign:'center',align:'left'">单位</th>
							<th data-options="field:'number',width:100,halign:'center',align:'right'">样品数量</th>
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
				url:"<%=basePath%>inpatient/lisAction/outpatientAdviceLisLis.action",
				queryParams:{id:id},
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
							url:"<%=basePath%>inpatient/lisAction/outpatientLisDetailLis.action",
							queryParams: {
								id: rowData.INSPECTION_ID
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
					return '男';
				}else if(value==2){
					return '女';
				}else{
					return '未知';
				}
			}
			function lisSearchFrom(){
				var lisQueryName =$('#lisQueryName').textbox('getValue'); 
				var id =$("#inpatientId").val()
				$('#lisPatEdId').datagrid('load', {
					queryName:lisQueryName,
					id:id
				});
			}
		</script>
	</body>
</html>