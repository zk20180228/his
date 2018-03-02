<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<title>药品结存</title>
<%@ include file="/common/metas.jsp"%>
</head>
		<div class="easyui-layout" data-options="fit:true">
			<div id="p" data-options="region:'west',split:true" style="width:15%;height:100%;border-top:0" class="checkBalanceList">
				盘点科室：<input ID="deptList"  style="width:130px;"/>
				<a href="javascript:deleData()"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
				<ul id="tDt"></ul> 
				<input type="hidden" id="id"> 
				<br>
				<hr>
				<br>
				<ul id="tDtHis"></ul> 
				<input type="hidden" id="idHis"> 
			</div>
			<div data-options="region:'center',split:false,iconCls:'icon-book',border:true" style="border-top:0">
				<table id="list" data-options="url:'queryCheckDetail.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,toolbar:'#toolbarId',fit:true,">
					<thead>
						<tr>
							<th data-options="field:'checkNo',width :'9%'" >盘点流水号</th>
							<th data-options="field:'tradeName',width :'10%'" >药品名称</th>
							<th data-options="field:'specs',width :'7%'" >规格</th>
							<th data-options="field:'retailPrice',width :'7%',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}" >零售价</th>
							<th data-options="field:'wholesalePrice',width :'7%',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}" >批发价</th>
							<th data-options="field:'purchasePrice',width :'7%',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}" >购入价</th>
							<th data-options="field:'minUnit',width :'7%',formatter:minunitFamater" >最小单位</th>
							<th data-options="field:'packUnit',width :'7%',formatter:drugpackagingunitFamater" >包装单位</th>
							<th data-options="field:'packQty',width :'7%'" >包装数量</th>
							<th data-options="field:'placecode',width :'7%'" >货位号</th>
							<th data-options="field:'validDate',width :'10%'" >有效期</th>
							<th data-options="field:'fstoreNum',width :'7%'" >封账数量</th>
							<th data-options="field:'adjustNum',width :'7%'" >实际盘点数量</th>
					</thead>
				</table>				
			</div>	
		</div>	
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:balance">
			<a href="javascript:void(0)" onclick="update(1)" class="easyui-linkbutton" data-options="iconCls:'icon-jiecun'">结存</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:cancel">
			<a href="javascript:void(0)" onclick="update(2)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">取消</a>
		</shiro:hasPermission>
		</div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
		<script type="text/javascript">
		var drugpackagingunitMap = "";//包装单位Map
		var minunitMap = "";//最小单位Map
		//加载页面
			$(function(){
				//查询包装单位
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
					type:'post',
					success: function(drugpackagingunitdata) {					
						drugpackagingunitMap = drugpackagingunitdata;							
					}
				});
				//查询最小单位
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/queryMinunit.action",				
					type:'post',
					success: function(minunitdata) {					
						minunitMap = minunitdata;							
					}
				});
				//初始化盘点科室下拉
				$('#deptList').combobox({
					url : "<%=basePath%>drug/check/findDeptByYFYK.action",
					valueField : 'deptCode',
					textField : 'deptName',
					width : 130,
					onSelect : function(record){
						deptchange();
					},onHidePanel:function(none){
					    var data = $(this).combobox('getData');
					    var val = $(this).combobox('getValue');
					    var result = true;
					    for (var i = 0; i < data.length; i++) {
					        if (val == data[i].deptCode) {
					            result = false;
					        }
					    }
					    if (result) {
					        $(this).combobox("clear");
					    }else{
					        $(this).combobox('unselect',val);
					        $(this).combobox('select',val);
					    }
					},
					filter: function(q, row){
					    var keys = new Array();
					    keys[keys.length] = 'deptCode';
					    keys[keys.length] = 'deptName';
					    keys[keys.length] = 'deptPinyin';
					    keys[keys.length] = 'deptWb';
					    keys[keys.length] = 'deptInputcode';
					    return filterLocalCombobox(q, row, keys);
					}
				});
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
			   		pageSize:20,
			   		pageList:[20,30,50,100],
			   		onLoadSuccess:function(row, data){
			   			$('#list').datagrid('unselectAll');
						$('#list').datagrid('uncheckAll');
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
						}}
				});
			
				$('#tDt').tree({    
				    url:'<%=basePath %>drug/check/queryBalanceTree.action',
				    method:'post',
				    animate:true,
				    lines:true,
				    onSelect: function(node){//点击节点
				    	$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				    	var nodes = $('#tDtHis').tree('getSelected');
						if(nodes){
								$('#tDtHis').find('.tree-node-selected').removeClass('tree-node-selected');
								$("#id").val('');
						}
						var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
						if(drugStorageCode){
							$("#id").val(node.id);
							$('#list').datagrid('load', {
								pid: node.id,
								pidHis:null,
								deptCode: drugStorageCode
							});
						}else{
							$.messager.alert("操作提示", "请选择要结存的科室！", "warning");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
					}
			   	}); 
			   	//历史封账单树
					$('#tDtHis').tree({    
					    url:'<%=basePath %>drug/check/queryBalanceTreeHis.action',
					    method:'post',
					    animate:true,
					    lines:true,
					    onSelect: function(node){//点击节点
					    	$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
							var nodes = $('#tDt').tree('getSelected');
							if(nodes){
 								$('#tDt').find('.tree-node-selected').removeClass('tree-node-selected');
 								$("#id").val('');
							}
							var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
							if(drugStorageCode){
								$("#idHis").val(node.id);
								$('#list').datagrid('load', {
									pid: null,
									pidHis: node.id,
									deptCode: drugStorageCode
								});
							}else{
								$.messager.alert("操作提示", "请选择历史结存科室！", "warning");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}
						}
				}); 	
			});
			//包装单位 列表页 显示	
			function drugpackagingunitFamater(value,row,index){				
				if(value!=null&&value!=""){										
					if(drugpackagingunitMap[value]!=null&&drugpackagingunitMap[value]!=""){
						return drugpackagingunitMap[value];
					}
					return value;					
				}			
			}
			
			function deleData(){
				delSelectedData('deptList');
				deptchange();
			}
			//最小单位 列表页 显示	
			function minunitFamater(value,row,index){				
				if(value!=null&&value!=""){										
					if(minunitMap[value]!=null&&minunitMap[value]!=""){
						return minunitMap[value];
					}
					return value;					
				}			
			}
			function update(flag){
				$.messager.confirm('确认', '确定要执行此操作吗?', function(res) {
					if (res) {
						var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
						if($("#id").val()!=null&&$("#id").val()!=""&&$("#id").val()!=0){
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
							$.ajax({
							 	url:"<%=basePath %>drug/check/updateCheckStatic.action",
							 	data : {
							 		"id" : $("#id").val(),
									"flag" : flag,
									"deptCode" : drugStorageCode
								},
							 	type:'post',
							 	success:function(data){
							 		$.messager.progress('close');
							 		if(data=="yes"){
							 			$.messager.alert("操作提示", "操作成功！", "success");
							 			$('#deptList').combobox('setValue','');
							 			deptchange();
							 		}else{
							 			$('#deptList').combobox('setValue','');
							 			deptchange();
							 			$.messager.alert("操作提示", "操作失败！", "error");
							 		}
							 	}
							});	
						}else{
							if($("#idHis").val()!=null&&$("#idHis").val()!=""){
								$.messager.alert("操作提示", "历史封账单不可以结存或取消！", "warning");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}else{
								$.messager.alert("操作提示", "请选择左侧盘点单进行结存！", "warning");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}
						}
					}
				});
			}	
		   
		  //科室下拉框改变的时候重新加载封账单
			function deptchange(){
				var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
				//改变科室下拉后,重新加载封账单树和清空封账药品列表
				$('#tDt').tree({    
				    url:"<%=basePath %>drug/check/queryBalanceTree.action?deptCode="+drugStorageCode,
				});
				//历史封账单
				$('#tDtHis').tree({    
				    url:"<%=basePath %>drug/check/queryBalanceTreeHis.action?deptCode="+drugStorageCode,
				});
				//药品列表
				$('#list').datagrid('loadData',[]);
			}	

		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>