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
<title>设备采购计划申报</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
<div id="cc" class="easyui-layout" fit="true"> 
    		<div id="tab" class="easyui-tabs" data-options="border:false" style="width: 100%">   
			    <div  title="已申报" >   
					<table style="padding:7px 7px 5px 7px;width:100%;height:5%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
								<span>办公用途：</span>
								<input class="easyui-textbox" id="used"  data-options="prompt:'请输入办公用途'" style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备分类:</span>
								<input id="typed" class="easyui-textbox" id="sorted" data-options="prompt:'请输入设备分类'" style="width: 130px;"/>
								&nbsp;&nbsp;
								<span>类别代码：</span>
								<input class="easyui-textbox" id="coded"  data-options="prompt:'请输入类别代码'" style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备名称：</span>
								<input class="easyui-textbox" id="named"  data-options="prompt:'请输入设备名称'" style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>状态：</span>
								<input class="easyui-combobox" id="stated"  editable='false' style="width:100px;height:25px"
				                   data-options="valueField: 'value',textField: 'label',data: [{
				                   label: '全部',
				                   value: '2',
				                   selected:true},
				                   {label: '启用',
				                   value: '0',
				                   },
				                   {label: '停用',
				                   value: '1',
				                   },]"                  
				                 />
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchFromYSB()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchReloadYSB()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:91%;margin-top: 3px;" >
						<table id="declared" class="easyui-datagrid"    
						        data-options="idField:'id',rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,
						        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId'">   
						    <thead>   
						        <tr>   
						      		<th data-options="field:'ck',checkbox:true" ></th>
						           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
						            <th data-options="field:'applName',width:150,align:'center'">申报人</th>   
						            <th data-options="field:'officeName',width:200,align:'center'">办公用途</th>   
						            <th data-options="field:'classCode',width:150,align:'center'">类别代码</th>   
						            <th data-options="field:'className',width:150,align:'center'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'center'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'planPrice',width:150,align:'center'">计划单价(元)</th>   
						            <th data-options="field:'planNum',width:150,align:'center'">计划数量</th>   
						            <th data-options="field:'planTotal',width:150,align:'center'">计划总价</th>   
						            <th data-options="field:'applDate',width:150,align:'center'">申报时间</th>   
						            <th data-options="field:'stop_flg',width:100,align:'center',formatter:stateFormatter">状态</th>   
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
								&nbsp;&nbsp;
								<span>设备分类:</span>
								<input id="typing" class="easyui-textbox"  data-options="prompt:'请输入设备分类'" style="width: 130px;"/>
								&nbsp;&nbsp;
								<span>类别代码：</span>
								<input class="easyui-textbox" id="coding"  data-options="prompt:'请输入类别代码'" style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备名称：</span>
								<input class="easyui-textbox" id="naming"  data-options="prompt:'请输入设备名称'" style="width: 150px;"/>
								&nbsp;&nbsp;
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchFromSBZ()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchReloadSBZ()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:91%;margin-top: 3px;" >
						<table id="declaring"  class="easyui-datagrid"   
					        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
					        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId1'">   
					   		 <thead>   
						        <tr>   
						           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
						            <th data-options="field:'applName',width:150,align:'center'">申报人</th>   
						            <th data-options="field:'officeName',width:200,align:'center'">办公用途</th>   
					           		<th data-options="field:'classCode',width:150,align:'center'">类别代码</th>   
						            <th data-options="field:'className',width:150,align:'center'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'center'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'planPrice',width:150,align:'center'">计划单价(元)</th>   
						            <th data-options="field:'planNum',width:150,align:'center'">计划数量</th>   
						            <th data-options="field:'planTotal',width:150,align:'center'">计划总价</th>   
						            <th data-options="field:'applDate',width:150,align:'center'">申报时间</th>   
						        </tr>   
					    	</thead>   
						</table>  
					</div>
			    </div>   
			    <div title="草稿箱" ">   
					<table   style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
								<span>办公用途：</span>
								<input class="easyui-textbox" id="useCG"  data-options="prompt:'请输入办公用途'" style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备分类:</span>
								<input id="typeCG" class="easyui-textbox" data-options="prompt:'请输入设备分类'" style="width: 130px;"/>
								&nbsp;&nbsp;
								<span>类别代码：</span>
								<input class="easyui-textbox" id="codeCG"  data-options="prompt:'请输入类别代码'" style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备名称：</span>
								<input class="easyui-textbox" id="nameCG"  data-options="prompt:'请输入设备名称'" style="width: 150px;"/>
								&nbsp;&nbsp;
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchFromCGX()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchReloadCGX()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:91%;margin-top: 3px;" >
						<table id="drafts" class="easyui-datagrid"   
					        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
					        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId2'">   
					   		 <thead>   
						        <tr>  
						        	<th data-options="field:'ck',checkbox:true" ></th>
						           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
						            <th data-options="field:'applName',width:150,align:'center'">申报人</th>   
						            <th data-options="field:'officeName',width:200,align:'center'">办公用途</th>
					           		<th data-options="field:'classCode',width:150,align:'center'">类别代码</th>   
						            <th data-options="field:'className',width:150,align:'center'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'center'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'planPrice',width:150,align:'center'">计划单价(元)</th>   
						            <th data-options="field:'planNum',width:150,align:'center'">计划数量</th>   
						            <th data-options="field:'planTotal',width:150,align:'center'">计划总价</th>   
						            <th data-options="field:'applDate',width:150,align:'center'">申报时间</th>   
						        </tr>   
						    </thead>   
						</table>  
					</div>
			    </div>   
			    <div  title="未批准" ">   
					<table style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
							<span>办公用途：</span>
							<input class="easyui-textbox" id="useNo"  data-options="prompt:'请输入办公用途'" style="width: 150px;"/>
							&nbsp;&nbsp;
							<span>设备分类:</span>
							<input id="typeNo" class="easyui-textbox"  data-options="prompt:'请输入设备分类'" style="width: 130px;"/>
							&nbsp;&nbsp;
							<span>类别代码：</span>
							<input class="easyui-textbox" id="codeNo"  data-options="prompt:'请输入类别代码'" style="width: 150px;"/>
							&nbsp;&nbsp;
							<span>设备名称：</span>
							<input class="easyui-textbox" id="nameNo"  data-options="prompt:'请输入设备名称'" style="width: 150px;"/>
							&nbsp;&nbsp;
							&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="searchFromWPZ()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="searchReloadWPZ()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
				<div style="width:100%;height:91%;margin-top: 3px;" >
					<table  id="unratified" class="easyui-datagrid"   
				        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
				        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId3'">   
					    <thead>   
					        <tr>   
				           		<th data-options="field:'ck',checkbox:true" ></th>
				           		<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
					            <th data-options="field:'applName',width:150,align:'center'">申报人</th>   
					            <th data-options="field:'officeName',width:200,align:'center'">办公用途</th>  
					            <th data-options="field:'classCode',width:150,align:'center'">类别代码</th>   
					            <th data-options="field:'className',width:150,align:'center'">设备分类</th>   
					            <th data-options="field:'deviceName',width:200,align:'center'">设备名称</th>   
					            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
					            <th data-options="field:'planPrice',width:150,align:'center'">计划单价(元)</th>   
					            <th data-options="field:'planNum',width:150,align:'center'">计划数量</th>   
					            <th data-options="field:'planTotal',width:150,align:'center'">计划总价</th>   
					            <th data-options="field:'applDate',width:150,align:'center'">申报时间</th>   
					        </tr>   
					    </thead>   
					</table>  
				</div>
			</div>  
	</div>  
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申报计划</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="stop(1)" class="easyui-linkbutton" data-options="iconCls:'icon-database_stop',plain:true">停用</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" onclick="stop(0)" class="easyui-linkbutton" data-options="iconCls:'icon-database_start',plain:true">启用</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloadYSB()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId1">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申报计划</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloadSBZ()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId2">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申报计划</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="editCG()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" onclick="delCG()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloadCGX()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId3">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申报计划</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="editWPZ()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" onclick="delWPZ()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloadWPZ()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="add"></div>
<script type="text/javascript">
	$('#tab').tabs({
		onSelect: function(title,index){
			var state;
			if(title=="已申报"){
				state = 3;
				$("#declared").datagrid({
					url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}else if(title=="申报中"){
				state =1;
				$("#declaring").datagrid({
					url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}else if(title=="草稿箱"){
				state=0;
				$("#drafts").datagrid({
					url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}else if(title=="未批准"){
				state=2;
				$("#unratified").datagrid({
					url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}
		}
	})
	function editCG(){
		var row = $('#drafts').datagrid('getSelected'); //获取当前选中行     
        if(row != null){
    		Adddilog("修改","<%=basePath %>assets/assetsPurchase/editAssets.action?id="+row.id);
           	$('#drafts').datagrid('reload');
   		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
	}
	function editWPZ(){
		var row = $('#unratified').datagrid('getSelected'); //获取当前选中行     
        if(row != null){
    		Adddilog("修改","<%=basePath %>assets/assetsPurchase/editAssets.action?id="+row.id);
           	$('#unratified').datagrid('reload');
   		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
	}
	function delCG(){
		 //选中要删除的行
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
						url: "<c:url value='assets/assetsPurchase/delAssets.action'/>?id="+ids,
						type:'post',
						success: function() {
							$.messager.alert('提示',data.resMsg);
							$('#drafts').datagrid('reload');
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
	function delWPZ(){
		 //选中要删除的行
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
						url: "<c:url value='assets/assetsPurchase/delAssets.action'/>?id="+ids,
						type:'post',
						success: function() {
							$.messager.alert('提示',data.resMsg);
							$('#unratified').datagrid('reload');
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
						url : "<%=basePath %>assets/assetsPurchase/stopAssets.action?id="+ids+"&flag="+flag,
						type:'post',
						success: function(data) {
							$.messager.alert('提示',data.resMsg);
							$('#declared').datagrid('reload');
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
	function reloadYSB(){
		//实现刷新栏目中的数据
		 $("#declared").datagrid("reload");
	}
	function reloadSBZ(){
		//实现刷新栏目中的数据
		 $("#declaring").datagrid("reload");
	}
	function reloadCGX(){
		//实现刷新栏目中的数据
		 $("#drafts").datagrid("reload");
	}
	function reloadWPZ(){
		//实现刷新栏目中的数据
		 $("#unratified").datagrid("reload");
	}
	/**
	 * 查询
	 */
	function searchFromYSB() {
		var used = $('#used').textbox('getValue');
		var typed = $('#typed').textbox('getValue');
		var coded = $('#coded').textbox('getValue');
		var named = $('#named').textbox('getValue');
		var stated = $('#stated').combobox('getValue');
		$("#declared").datagrid({
			url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action',
			queryParams:{officeName:used,
				className:typed,
				classCode:coded,
				deviceName:named,
				stop_flg:stated,
				state:3},
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	}
	function searchReloadYSB() {
		$('#used').textbox('setValue','');
		$('#typed').textbox('setValue','');
		$('#coded').textbox('setValue','');
		$('#named').textbox('setValue','');
		$('#stated').combobox('setValue',2);
		searchFromYSB()
	}
	/**
	 * 查询
	 */
	function searchFromSBZ() {
		var used = $('#using').textbox('getValue');
		var typed = $('#typing').textbox('getValue');
		var coded = $('#coding').textbox('getValue');
		var named = $('#naming').textbox('getValue');
		$("#declaring").datagrid({
			url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action',
			queryParams:{officeName:used,
				className:typed,
				classCode:coded,
				deviceName:named,
				state:1},
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	}
	function searchReloadSBZ() {
		$('#using').textbox('setValue','');
		$('#typing').textbox('setValue','');
		$('#coding').textbox('setValue','');
		$('#naming').textbox('setValue','');
		searchFromSBZ()
	}
	/**
	 * 查询
	 */
	function searchFromCGX() {
		var used = $('#useCG').textbox('getValue');
		var typed = $('#typeCG').textbox('getValue');
		var coded = $('#codeCG').textbox('getValue');
		var named = $('#nameCG').textbox('getValue');
		$("#drafts").datagrid({
			url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action',
			queryParams:{officeName:used,
				className:typed,
				classCode:coded,
				deviceName:named,
				state:0},
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	}
	function searchReloadCGX() {
		$('#useCG').textbox('setValue','');
		$('#typCeG').textbox('setValue','');
		$('#codeCG').textbox('setValue','');
		$('#nameCG').textbox('setValue','');
		searchFromCGX()
	}
	/**
	 * 查询
	 */
	function searchFromWPZ() {
		var used = $('#useNo').textbox('getValue');
		var typed = $('#typeNo').textbox('getValue');
		var coded = $('#codeNo').textbox('getValue');
		var named = $('#nameNo').textbox('getValue');
		$("#unratified").datagrid({
			url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action',
			queryParams:{officeName:used,
				className:typed,
				classCode:coded,
				deviceName:named,
				state:2},
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	}
	function searchReloadWPZ() {
		$('#useNo').textbox('setValue','');
		$('#typeNo').textbox('setValue','');
		$('#codeNo').textbox('setValue','');
		$('#nameNo').textbox('setValue','');
		searchFromWPZ() 
	}
	function add(){
		Adddilog("申报计划","<%=basePath %>assets/assetsPurchase/assetsApplyAdd.action");
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
	function stateFormatter(value,row,index){
		   if(value==1){
			   return "停用";
		   }else if(value==0){
			   return "启用";
		   }
	}
</script>
</body>
</html>