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
		<script type="text/javascript">
		var pName ='';
		$(function(){
			$('#et').tree({    
				url : "<c:url value='/nursestation/nurse/getInpatientTree.action'/>",
				method : 'get',
				animate : true,
				lines : true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if (node.children) {
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},
				onClick : function(node){
					if(node.attributes.inpatientNo!=null&&node.attributes.inpatientNo!=''){
						pName = node.attributes.name;
						var confirmCodeId = $('#confirmCodeId').val();
						var startDate = $('#startId').datebox('getValue');
						var endDate = $('#endId').datebox('getValue');
						$('#listDrug').datagrid({
							url:'${pageContext.request.contextPath}/nursestation/nuiConfirm/queryNuiConfirm.action?menuAlias=${menuAlias}',
							queryParams:{drugFlag:1,cardNo:node.attributes.inpatientNo,confirmCode:confirmCodeId,startDate:startDate,endDate:endDate}
						});
						$('#listUnDrug').datagrid({
							url:'${pageContext.request.contextPath}/nursestation/nuiConfirm/queryNuiConfirm.action?menuAlias=${menuAlias}',
							queryParams:{drugFlag:2,cardNo:node.attributes.inpatientNo,confirmCode:confirmCodeId,startDate:startDate,endDate:endDate}
						});
					}
				},onLoadSuccess : function(node, data) {
					if(data.resCode=='error'){
						   $("body").setLoading({
								id:"body",
								isImg:false,
								text:data.resMsg
							});
					   }
				}
			});  
			$('#list').datagrid({
				pagination:false,
				onClickCell:function(rowIndex, field, value){
					var rows = $('#list').datagrid('getChecked');
					var node = $('#et').tree('getSelected');
					var startDate = $('#startId').datebox('getValue');
					var endDate = $('#endId').datebox('getValue');
					if(node!=null){
						if(node.attributes.inpatientNo!=null){
							if(rows!=null){
								var id = "";
								for(var i=0;i<rows.length;i++){
									if(id!==""){
										id +=",";
									}
									id += rows.id;
								}
								$('#listDrug').datagrid({
									url:'${pageContext.request.contextPath}/nursestation/nuiConfirm/queryNuiConfirm.action?menuAlias=${menuAlias}',
									queryParams:{drugFlag:1,cardNo:node.attributes.inpatientNo,confirmCode:2,startDate:startDate,endDate:endDate,single:id}
								});
								$('#listUnDrug').datagrid({
									url:'${pageContext.request.contextPath}/nursestation/nuiConfirm/queryNuiConfirm.action?menuAlias=${menuAlias}',
									queryParams:{drugFlag:2,cardNo:node.attributes.inpatientNo,confirmCode:2,startDate:startDate,endDate:endDate,single:id}
								});
							}
						}
					}
				}
			});
			$('#listDrug').datagrid({
				pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				queryParams:{drugFlag:1,cardNo:null,confirmCode:1,startDate:null,endDate:null,single:null}
			});
			$('#listUnDrug').datagrid({
				pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				queryParams:{drugFlag:2,cardNo:null,confirmCode:1,startDate:null,endDate:null,single:null}
			});
		});
		//执行未执行
		function clickRadio(state){
			var node = $('#et').tree('getSelected');
			var rows = $('#list').datagrid('getChecked');
			var id = "";
			if(rows!=null){
				for(var i=0;i<rows.length;i++){
					if(id!==""){
						id +=",";
					}
					id += rows.id;
				}
			}
			if(state==2){
				$('#confirmCodeId').val(2);
				$('#drugEdit').linkbutton('disable');
				$('#unDrugEdit').linkbutton('disable');
				if(node!=null&&node.attributes.inpatientNo!=null&&node.attributes.inpatientNo!=''){
					var startDate = $('#startId').datebox('getValue');
					var endDate = $('#endId').datebox('getValue');
					$('#listDrug').datagrid('reload',{drugFlag:1,cardNo:node.attributes.inpatientNo,confirmCode:2,startDate:startDate,endDate:endDate,single:id});
					$('#listUnDrug').datagrid('reload',{drugFlag:2,cardNo:node.attributes.inpatientNo,confirmCode:2,startDate:startDate,endDate:endDate,single:id});
				}
			}else{
				$('#confirmCodeId').val(1);
				$('#drugEdit').linkbutton('enable');
				$('#unDrugEdit').linkbutton('enable');
				if(node!=null&&node.attributes.inpatientNo!=null&&node.attributes.inpatientNo!=''){
					var startDate = $('#startId').datebox('getValue');
					var endDate = $('#endId').datebox('getValue');
					$('#listDrug').datagrid('reload',{drugFlag:1,cardNo:node.attributes.inpatientNo,confirmCode:1,startDate:startDate,endDate:endDate,single:id});
					$('#listUnDrug').datagrid('reload',{drugFlag:2,cardNo:node.attributes.inpatientNo,confirmCode:1,startDate:startDate,endDate:endDate,single:id});
				}
			}
		}
		function funPname(val,row){
			return pName;
		}
		//确认
		function edit(id,drugFlag){
			var rows = $('#'+id).datagrid('getChecked');
			if(rows!=null&&rows.length>0){
				$.messager.confirm('提示信息','是否确认执行选中信息？', function(res){//提示是否删除
					if (res){
						var ids = '';
						for(var i=0;i<rows.length;i++){
							if(ids!=''){
								ids+=',';
							}
							ids += rows[i].id;
						}
		 				$.ajax({
		 					url: "<c:url value='/nursestation/nuiConfirm/editNuiConfirm.action'/>",
		 					data:{id:ids,drugFlag:drugFlag},
		 					type:'post',
		 					success: function(data) {
		 						var dataMap = eval("("+data+")");
		 						$.messager.alert('提示',dataMap.resCode);
		 						setTimeout(function(){
		 							$(".messager-body").window('close');
		 						},3500);
		 					}
		 				});
					}
		    	});
			}else{
				$.messager.alert('提示',"请选择要执行的信息!");	
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;	
			}
		}
		</script>
	</head>
	<body>
		<div id="elo" class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'west',split:true" style="width:16%;border-right-width: 0px">
				<div id="el" class="easyui-layout" data-options="fit:true">   
					<div data-options="region:'north'" style="height:40%; ">
						<ul id="et"></ul>
					</div>   
					<div data-options="region:'center'" style="height:60%;border: 0px;">
						<table id="list" data-options="url:'${pageContext.request.contextPath}/nursestation/nuiConfirm/queryExecbill.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIdDrug',fit:true">
							<thead>
								<tr>
									<th data-options="field:'billName',width:211">执行单名称</th>
								</tr>
							</thead>
						</table>
					</div> 
				</div>
			</div>   
			<div data-options="region:'center'" style="width:80%;border-right-width: 0px;">
				<div id="cc" class="easyui-layout" data-options="fit:true">   
				    <div data-options="region:'north'" style="height:50px;border-left-width: 0px;">
						<table style="width:100%;padding-top: 15px;padding-left: 5px;">
							<tr style="width:100%;">
								<td>
									查询 : <input type="radio" name="execute" checked="checked" onclick="clickRadio(1)"> 未执行
									<input type="hidden" id="confirmCodeId" value="1">
									<input type="radio" name="execute" onclick="clickRadio(2)"> 已执行
									<input id="startId" type="text" class="easyui-datebox"></input>
									至
									<input id="endId" type="text" class="easyui-datebox"></input> 
								</td>
							</tr>
						</table>
				    </div>   
				    <div data-options="region:'center'" style="height:90%;border: 0px;">
				    	<div id="et" class="easyui-tabs" data-options="fit:true,border:false" style="border: 0px;">   
						    <div title="药品" data-options="fit:true,border:false" style="border: 0px;" > 
						    	<table id="listDrug" data-options="border:false,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIdDrug',fit:true">
									<thead>
										<tr>
											<th data-options="field:'ck',checkbox:true"></th>
											<th data-options="field:'pName',formatter:funPname,width:200">患者</th>
											<th data-options="field:'iName',width:350">医嘱名称[规格]</th>
											<th data-options="field:'combNo',width:200">组</th>
											<th data-options="field:'planExeDate',width:200">计划执行时间</th>
											<th data-options="field:'planDate',width:200">执行时间</th>
											<th data-options="field:'planUser',width:200">执行人</th>
										</tr>
									</thead>
								</table>
						    </div>   
						    <div title="非药品" data-options="fit:true,border:false">   
						         <table id="listUnDrug"  data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIdUnDrug',fit:true">
									<thead>
										<tr>
											<th data-options="field:'ck',checkbox:true"></th>
											<th data-options="field:'pName',formatter:funPname,width:200">患者</th>
											<th data-options="field:'iName',width:350">医嘱名称[规格]</th>
											<th data-options="field:'combNo',width:200">组</th>
											<th data-options="field:'planExeDate',width:200">计划执行时间</th>
											<th data-options="field:'planDate',width:200">执行时间</th>
											<th data-options="field:'planUser',width:200">执行人</th>
										</tr>
									</thead>
								</table>
						    </div>   
						</div> 
				    </div>   
				</div> 
			</div> 
		</div>
		<div id="toolbarIdDrug" >
			<shiro:hasPermission name="${menuAlias}:function:save">
				<a href="javascript:void(0)" id="drugEdit" onclick="edit('listDrug',1)" class="easyui-linkbutton" data-options="iconCls:'icon-confirm',plain:true">确认</a>
			</shiro:hasPermission>
		</div>
		<div id="toolbarIdUnDrug" >
			<shiro:hasPermission name="${menuAlias}:function:save">
				<a href="javascript:void(0)" id="unDrugEdit" onclick="edit('listUnDrug',2)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">确认</a>
			</shiro:hasPermission>
		</div>
		
	</body>
</html>