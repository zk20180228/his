<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>设备采购申报</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true"> 
    		<div id="tab" class="easyui-tabs" data-options="border:false" style="width: 100%">  
    		   <div  title="采购计划" > 
					<table style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td>
								<span>办公用途：</span>
								<input class="easyui-textbox" id="useCGJH"  data-options="prompt:'请输入办公用途'" style="width: 150px;"/>
								<span>设备分类:</span>
								<input class="easyui-textbox" id="typeCGJH" data-options="prompt:'请输入设备分类'" style="width: 130px;"/>
								<span>类别代码：</span>
								<input class="easyui-textbox" id="codeCGJH"  data-options="prompt:'请输入设备代码'" style="width: 150px;"/>
								<span>设备名称：</span>
								<input class="easyui-textbox" id="nameCGJH"  data-options="prompt:'请输入设备名称'" style="width: 150px;"/>
								<span>状态：</span>
								<input class="easyui-combobox" id="stateCGJH"  editable='false' style="width:100px;height:25px"
				                   data-options="valueField: 'value',textField: 'label',data: [{
				                   label: '全部',
				                   value: '',
				                   selected:true},
				                   {label: '完成',
				                   value: '完成',
				                   },
				                   {label: '未完成',
				                   value: '未完成',
				                   },]"                  
				                 />
								<a href="javascript:void(0)" onclick="searchFromedCGJH()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReloadCGJH()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:90%;margin-top: 5px;" >
						<table id="purchPlan" class="easyui-datagrid"    
						        data-options="idField:'id',rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,
						        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId4'">   
						    <thead>   
						        <tr>   
						      		<th data-options="field:'ck',checkbox:true" ></th>
						      		<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
						            <th data-options="field:'applName',width:120,align:'left'">申报人</th>
						            <th data-options="field:'officeCode',width:200,align:'left',hidden:true">办公用途代码</th>    
						            <th data-options="field:'officeName',width:200,align:'left'">办公用途</th>   
						            <th data-options="field:'classCode',width:120,align:'left'">类别代码</th>   
						            <th data-options="field:'className',width:200,align:'left'">设备分类</th>
						            <th data-options="field:'deviceCode',width:200,align:'left',hidden:true">设备名称代码</th>   
						            <th data-options="field:'deviceName',width:200,align:'left'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'planPrice',width:150,align:'right'">计划单价(元)</th>   
						            <th data-options="field:'planNum',width:150,align:'right'">计划数量</th> 
						            <th data-options="field:'planPriceTotal',width:150,align:'right'">计划总价(元)</th> 
						            <th data-options="field:'purchNum',width:150,align:'right'">已采购数量</th> 
						            <th data-options="field:'condition',width:100,align:'center'">完成情况</th>   
						        </tr>   
						    </thead>   
						</table>  
					</div>
			    </div> 
			    <div  title="已申报" > 
					<table style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
								<span>办公用途：</span>
								<input class="easyui-textbox" id="used"  data-options="prompt:'请输入办公用途'" style="width: 150px;"/>
								<span>设备分类:</span>
								<input id="typed" class="easyui-textbox" data-options="prompt:'请输入设备分类'" id="sorted" style="width: 130px;"/>
								<span>类别代码：</span>
								<input class="easyui-textbox" id="coded"  data-options="prompt:'请输入设备代码'" style="width: 150px;"/>
								<span>设备名称：</span>
								<input class="easyui-textbox" id="named"  data-options="prompt:'请输入设备名称'" style="width: 150px;"/>
								<a href="javascript:void(0)" onclick="searchFromedYSB()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReloadYSB()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:90%;margin-top: 5px;" >
						<table id="declared" class="easyui-datagrid"    
						        data-options="idField:'id',rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,
						        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId'">   
						    <thead>   
						        <tr>   
						      		<th data-options="field:'ck',checkbox:true" ></th>
						           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
						            <th data-options="field:'applName',width:120,align:'left'">申报人</th>   
						            <th data-options="field:'officeName',width:200,align:'left'">办公用途</th>   
						            <th data-options="field:'classCode',width:120,align:'left'">类别代码</th>   
						            <th data-options="field:'className',width:200,align:'left'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'left'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'purchPrice',width:150,align:'right'">采购单价(元)</th>   
						            <th data-options="field:'purchNum',width:100,align:'right'">采购数量</th>   
						            <th data-options="field:'tranCost',width:150,align:'right'">运费(元)</th>
						            <th data-options="field:'instCost',width:150,align:'right'">安装费(元)</th>  
						            <th data-options="field:'purchTotal',width:150,align:'right'">采购总价(元)</th>     
						            <th data-options="field:'applDate',width:200,align:'center'">申报时间</th>   
						        </tr>   
						    </thead>   
						</table>  
					</div>
			    </div>   
			    <div title="申报中">   
					<table style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
								<span>办公用途：</span>
								<input class="easyui-textbox" id="using"  data-options="prompt:'请输入办公用途'" style="width: 150px;"/>
								<span>设备分类:</span>
								<input id="typing" class="easyui-textbox" data-options="prompt:'请输入设备分类'" style="width: 130px;"/>
								<span>类别代码：</span>
								<input class="easyui-textbox" id="coding" data-options="prompt:'请输入设备代码'" style="width: 150px;"/>
								<span>设备名称：</span>
								<input class="easyui-textbox" id="naming" data-options="prompt:'请输入设备名称'" style="width: 150px;"/>
								<a href="javascript:void(0)" onclick="searchFromSBZ()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReloadSBZ()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:90%;margin-top: 5px;" >
						<table id="declaring"  class="easyui-datagrid"   
					        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
					        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId1'">   
					   		 <thead>   
						        <tr>   
						           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
						             <th data-options="field:'applName',width:120,align:'left'">申报人</th>   
						            <th data-options="field:'officeName',width:200,align:'left'">办公用途</th>   
						            <th data-options="field:'classCode',width:120,align:'left'">类别代码</th>   
						            <th data-options="field:'className',width:200,align:'left'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'left'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'purchPrice',width:150,align:'right'">采购单价(元)</th>   
						            <th data-options="field:'purchNum',width:100,align:'right'">采购数量</th>
						            <th data-options="field:'tranCost',width:150,align:'right'">运费(元)</th>
						            <th data-options="field:'instCost',width:150,align:'right'">安装费(元)</th>     
						            <th data-options="field:'purchTotal',width:150,align:'right'">采购总价(元)</th>   
						            <th data-options="field:'applDate',width:200,align:'center'">申报时间</th>   
						        </tr>   
					    	</thead>   
						</table>  
					</div>
			    </div>   
			    <div title="草稿箱">   
					<table   style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
								<span>办公用途：</span>
								<input class="easyui-textbox" id="useCG" data-options="prompt:'请输入办公用途'" style="width: 150px;"/>
								<span>设备分类:</span>
								<input id="typeCG" class="easyui-textbox" data-options="prompt:'请输入设备分类'" style="width: 130px;"/>
								<span>类别代码：</span>
								<input class="easyui-textbox" id="codeCG" data-options="prompt:'请输入设备代码'" style="width: 150px;"/>
								<span>设备名称：</span>
								<input class="easyui-textbox" id="nameCG" data-options="prompt:'请输入设备名称'" style="width: 150px;"/>
								<a href="javascript:void(0)" onclick="searchFromCGX()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReloadCGX()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div  style="width:100%;height:90%;margin-top: 5px;" >
						<table id="drafts" class="easyui-datagrid"   
					        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
					        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId2'">   
					   		 <thead>   
						        <tr>  
						        	<th data-options="field:'ck',checkbox:true" ></th>
						           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
						            <th data-options="field:'applName',width:120,align:'left'">申报人</th>   
						            <th data-options="field:'officeName',width:200,align:'left'">办公用途</th>   
						            <th data-options="field:'classCode',width:120,align:'left'">类别代码</th>   
						            <th data-options="field:'className',width:200,align:'left'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'left'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'purchPrice',width:150,align:'right'">采购单价(元)</th>   
						            <th data-options="field:'purchNum',width:100,align:'right'">采购数量</th>
						            <th data-options="field:'tranCost',width:150,align:'right'">运费(元)</th>
						            <th data-options="field:'instCost',width:150,align:'right'">安装费(元)</th>     
						            <th data-options="field:'purchTotal',width:150,align:'right'">采购总价(元)</th>   
						            <th data-options="field:'applDate',width:200,align:'center'">申报时间</th>   
						        </tr>   
						    </thead>   
						</table>  
					</div>
			    </div>   
			    <div  title="未批准">   
					<table style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
							<span>办公用途：</span>
							<input class="easyui-textbox" id="useNo" data-options="prompt:'请输入办公用途'"  style="width: 150px;"/>
							<span>设备分类:</span>
							<input id="typeNo" class="easyui-textbox" data-options="prompt:'请输入设备分类'"  style="width: 130px;"/>
							<span>类别代码：</span>
							<input class="easyui-textbox" id="codeNo" data-options="prompt:'请输入设备代码'"  style="width: 150px;"/>
							<span>设备名称：</span>
							<input class="easyui-textbox" id="nameNo" data-options="prompt:'请输入设备名称'"  style="width: 150px;"/>
							<a href="javascript:void(0)" onclick="searchFromWPZ()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:void(0)" onclick="searchReloadWPZ()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
				<div style="width:100%;height:90%;margin-top: 5px;" >
					<table  id="unratified" class="easyui-datagrid"   
				        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
				        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId3'">   
					    <thead>   
					        <tr>   
				           		<th data-options="field:'ck',checkbox:true" ></th>
				           		<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
					             <th data-options="field:'applName',width:120,align:'left'">申报人</th>   
						            <th data-options="field:'officeName',width:200,align:'left'">办公用途</th>   
						            <th data-options="field:'classCode',width:120,align:'left'">类别代码</th>   
						            <th data-options="field:'className',width:200,align:'left'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'left'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'purchPrice',width:150,align:'right'">采购单价(元)</th>   
						            <th data-options="field:'purchNum',width:100,align:'right'">采购数量</th>
						            <th data-options="field:'tranCost',width:150,align:'right'">运费(元)</th>
						            <th data-options="field:'instCost',width:150,align:'right'">安装费(元)</th>     
						            <th data-options="field:'purchTotal',width:150,align:'right'">采购总价(元)</th>   
						            <th data-options="field:'applDate',width:200,align:'center'">申报时间</th>   
					        </tr>   
					    </thead>   
					</table>  
				</div>
			</div>  
    	</div>   
	<div id="toolbarId">
			<a href="javascript:void(0)" onclick="reloadYSB()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId1">
			<a href="javascript:void(0)" onclick="reloadSBZ()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId2">
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="editCG()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" onclick="delCG()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloadCGX()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId3">
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="editWPZ()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" onclick="delWPZ()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloadWPZ()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId4">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申报设备</a>
		</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reloadCGJH()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="add"></div>
<script type="text/javascript">
	$('#tab').tabs({
		onSelect: function(title,index){
			var state;
			if(title=="已申报"){
				state = 3;
				$("#declared").datagrid({
					url: '<%=basePath %>assets/assetsPurch/queryAllAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}else if(title=="申报中"){
				state =1;
				$("#declaring").datagrid({
					url: '<%=basePath %>assets/assetsPurch/queryAllAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}else if(title=="草稿箱"){
				state=0;
				$("#drafts").datagrid({
					url: '<%=basePath %>assets/assetsPurch/queryAllAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}else if(title=="未批准"){
				state=2;
				$("#unratified").datagrid({
					url: '<%=basePath %>assets/assetsPurch/queryAllAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}else if(title=="采购计划"){
				$("#purchPlan").datagrid({
					url: '<%=basePath %>assets/assetsPurch/queryPurchPlan.action', 
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
			}
		}
	})
	//修改草稿 
	function editCG(){
		var row = $('#drafts').datagrid('getSelected'); //获取当前选中行     
        if(row != null){
    		Adddilog("修改","<%=basePath %>assets/assetsPurch/editAssets.action?id="+row.id);
           	$('#drafts').datagrid('reload');
   		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
	}
	//修改未批准
	function editWPZ(){
		var row = $('#unratified').datagrid('getSelected'); //获取当前选中行     
        if(row != null){
    		Adddilog("修改","<%=basePath %>assets/assetsPurch/editAssets.action?id="+row.id);
           	$('#unratified').datagrid('reload');
   		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
	}
	//删除草稿
	function delCG(){
        var iid = $('#drafts').datagrid('getChecked');
        if (iid.length > 0) {//选中几行的话触发事件	                        
		 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					var ids = '';
					for(var i=0; i<iid.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += iid[i].id;
					};
					$.ajax({
						url: "<c:url value='assets/assetsPurch/delAssets.action'/>?id="+ids,
						type:'post',
						success: function() {
							$('#drafts').datagrid('reload');
							$.messager.alert('提示','删除成功');
						}
					});										
				}
	        });
        }else{
  	    	 $.messager.alert('提示信息','请选择要删除的信息！');
	    	 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	     }	
	}
	//删除未批准 
	function delWPZ(){
        var iid = $('#unratified').datagrid('getChecked');
        if (iid.length > 0) {//选中几行的话触发事件	                        
		 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					var ids = '';
					for(var i=0; i<iid.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += iid[i].id;
					};
					$.ajax({
						url: "<c:url value='assets/assetsPurch/delAssets.action'/>?id="+ids,
						type:'post',
						success: function() {
							$("#unratified").datagrid("reload");
							$.messager.alert('提示','删除成功');
						}
					});										
				}
	        });
        }else{
  	    	 $.messager.alert('提示信息','请选择要删除的信息！');
	    	 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	     }	
	}
	//停用
	function stop(flag){
	  var iid = $('#declared').datagrid('getChecked');
	  if (iid.length > 0) {//选中几行的话触发事件	                        
		 	$.messager.confirm('确认', '确定操作选中信息吗?', function(res){//提示是否删除
				if (res){
					var ids = '';
					for(var i=0; i<iid.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += iid[i].id;
					};
					$.ajax({
						url : "<%=basePath %>assets/assetsPurch/stopAssets.action?id="+id+"&flag="+flag,
						type:'post',
						success: function() {
							$('#declared').datagrid('reload');
							$.messager.alert('提示','操作成功');
						}
					});										
				}
	        });
      }else{
	    	 $.messager.alert('提示信息','请选择要操作的信息！');
	    	 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	     }	
	}
	//刷新 采购计划
	function reloadCGJH(){
		 $("#purchPlan").datagrid("reload");
	}
	//刷新  已申报
	function reloadYSB(){
		 $("#declared").datagrid("reload");
	}
	//刷新 申报中
	function reloadSBZ(){
		 $("#declaring").datagrid("reload");
	}
	//刷新 草稿箱
	function reloadCGX(){
		 $("#drafts").datagrid("reload");
	}
	//刷新 未批准
	function reloadWPZ(){
		 $("#unratified").datagrid("reload");
	}
	 //采购计划 条件查询
	function searchFromedCGJH() {
		var used = $('#useCGJH').textbox('getValue');
		var typed = $('#typeCGJH').textbox('getValue');
		var coded = $('#codeCGJH').textbox('getValue');
		var named = $('#nameCGJH').textbox('getValue');
		var stated = $('#stateCGJH').combobox('getValue');
		$('#purchPlan').datagrid('load', {
			officeName:used,
			className:typed,
			classCode:coded,
			deviceName:named,
			stated:stated
		});
	}
	 //采购计划  重置
	function searchReloadCGJH() {
		$('#useCGJH').textbox('setValue','');
		$('#typeCGJH').textbox('setValue','');
		$('#codeCGJH').textbox('setValue','');
		$('#nameCGJH').textbox('setValue','');
		$('#stateCGJH').combobox('setValue','');
		searchFromedCGJH()
	}
	 //已申报 条件查询
	function searchFromedYSB() {
		var used = $('#used').textbox('getValue');
		var typed = $('#typed').textbox('getValue');
		var coded = $('#coded').textbox('getValue');
		var named = $('#named').textbox('getValue');
		$('#declared').datagrid('load', {
			officeName:used,
			className:typed,
			classCode:coded,
			deviceName:named,
			state:3
		});
	}
	 //已申报 重置
	function searchReloadYSB() {
		$('#used').textbox('setValue','');
		$('#typed').textbox('setValue','');
		$('#coded').textbox('setValue','');
		$('#named').textbox('setValue','');
		searchFromedYSB()
	}
	//申报中 条件查询
	function searchFromSBZ() {
		var used = $('#using').textbox('getValue');
		var typed = $('#typing').textbox('getValue');
		var coded = $('#coding').textbox('getValue');
		var named = $('#naming').textbox('getValue');
		$('#declaring').datagrid('load', {
			officeName:used,
			className:typed,
			classCode:coded,
			deviceName:named,
			state:1
		});
	}
	//申报中 重置
	function searchReloadSBZ() {
		$('#using').textbox('setValue','');
		$('#typing').textbox('setValue','');
		$('#coding').textbox('setValue','');
		$('#naming').textbox('setValue','');
		searchFromSBZ()
	}
	//草稿箱 条件查询
	function searchFromCGX() {
		var used = $('#useCG').textbox('getValue');
		var typed = $('#typeCG').textbox('getValue');
		var coded = $('#codeCG').textbox('getValue');
		var named = $('#nameCG').textbox('getValue');
		$('#drafts').datagrid('load', {
			officeName:used,
			className:typed,
			classCode:coded,
			deviceName:named,
			state:0
		});
	}
	//草稿箱 重置
	function searchReloadCGX() {
		$('#useCG').textbox('setValue','');
		$('#typeCG').textbox('setValue','');
		$('#codeCG').textbox('setValue','');
		$('#nameCG').textbox('setValue','');
		searchFromCGX()
	}
	//未批准 条件查询
	function searchFromWPZ() {
		var used = $('#useNo').textbox('getValue');
		var typed = $('#typeNo').textbox('getValue');
		var coded = $('#codeNo').textbox('getValue');
		var named = $('#nameNo').textbox('getValue');
		$('#unratified').datagrid('load', {
			officeName:used,
			className:typed,
			classCode:coded,
			deviceName:named,
			state:2
		});
	}
	//未批准 重置
	function searchReloadWPZ() {
		$('#useNo').textbox('setValue','');
		$('#typeNo').textbox('setValue','');
		$('#codeNo').textbox('setValue','');
		$('#nameNo').textbox('setValue','');
		searchFromWPZ() 
	}
	//跳转到采购申报页面
	function add(){
		var row = $('#purchPlan').datagrid('getSelected'); //获取当前选中行   
        if(row != null){
        	if(row.planNum==row.purchNum){
        		$.messager.alert("提示", "该设备已申报完，请重新申报采购计划！")
        	}else{
        		var overNum;
        		if(row.purchNum==null || row.purchNum=="" || row.purchNum==undefined){
        			overNum=row.planNum
        		}else{
        			overNum=row.planNum-row.purchNum
        		}
				Adddilog("采购申报","<%=basePath %>assets/assetsPurch/assetsPurchAdd.action?row="+encodeURIComponent(JSON.stringify(row))+"&overNum="+overNum);
        	}
   		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
	}
	function closeDialog(){
		$('#add').dialog('close');  
	}
	function Adddilog(title, url) {
		$('#add').dialog({    
		    title: title,    
		    width: '800px',    
		    height:'500px',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
	   });    
	}
</script>
</body>
</html>