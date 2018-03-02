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
    		<div id="tt" class="easyui-tabs" data-options="border:false" style="width: 100%">   
			     <div  title="待审批" >   
					<table style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
								<span>办公用途：</span>
								<input class="easyui-textbox" id="used"  data-options="prompt:'请输入办公用途'"  style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备分类:</span>
								<input id="typed" class="easyui-textbox" data-options="prompt:'请输入设备分类'"  id="sorted" style="width: 130px;"/>
								&nbsp;&nbsp;
								<span>设备代码：</span>
								<input class="easyui-textbox" id="coded"  data-options="prompt:'请输入设备代码'" style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备名称：</span>
								<input class="easyui-textbox" id="named"  data-options="prompt:'请输入设备名称'" style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>申报时间：</span>
								<input id="timeBegin" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
								<span>-</span>
								<input id="timeEnd" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchFromYSB()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchReloadYSB()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:91%;margin-top: 5px;" >
						<table id="declared" class="easyui-datagrid"    
						        data-options="idField:'id',rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,
						        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId'">   
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
			    <div title="已审批">   
					<table style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
								<span>办公用途：</span>
								<input class="easyui-textbox" id="using"  data-options="prompt:'请输入办公用途'" style="width: 150px;"/>
								&nbsp;
								<span>设备分类:</span>
								<input id="typing" class="easyui-textbox"  data-options="prompt:'请输入设备分类'" style="width: 130px;"/>
								&nbsp;
								<span>设备代码：</span>
								<input class="easyui-textbox" id="coding"  data-options="prompt:'请输入设备代码'" style="width: 150px;"/>
								&nbsp;
								<span>设备名称：</span>
								<input class="easyui-textbox" id="naming"  data-options="prompt:'请输入设备名称'" style="width: 150px;padding-top: 3px;"/>
								&nbsp;
								<span>申报时间：</span>
								<input id="timingBegin" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
								<span>-</span>
								<input id="timingEnd" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
								&nbsp;
								<span>状态：</span>
								<input class="easyui-combobox" id="stating"  editable='false' style="width:100px;height:25px"
				                   data-options="valueField: 'value',textField: 'label',data: [{
				                   label: '全部',
				                   value: '4',
				                   selected:true},
				                   {label: '未通过',
				                   value: '2',
				                   },
				                   {label: '通过',
				                   value: '3',
				                   },]"                  
				                 />
				                 &nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchFromSBZ()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								&nbsp;
								<a href="javascript:void(0)" onclick="searchReloadSBZ()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:91%;margin-top: 5px;" >
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
						            <th data-options="field:'applState',width:100,align:'center',formatter:stateFormatter"">状态</th>   
						               
						        </tr>   
					    	</thead>   
						</table>  
					</div>
			    </div>   
		</div>   
	</div>  
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="pass()" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true">通过</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="unratified()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">不通过</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloaded()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId1">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="view()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查看原因</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloading()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<script type="text/javascript">
	$('#tt').tabs({
		onSelect: function(title,index){
			var state;
			if(title=="待审批"){
				state = 0;
				$("#declared").datagrid({
					url: '<%=basePath %>assets/assetsPurchase/querySPAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}else if(title=="已审批"){
				state =1;
				$("#declaring").datagrid({
					url: '<%=basePath %>assets/assetsPurchase/querySPAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}
		}
	})
		function pass(){
			var row = $('#declared').datagrid('getSelected'); //获取当前选中行    
			if(row != null){
				$.messager.confirm('确认', '确定通过该申请吗?', function(res){//
					if (res){
						$.ajax({
							url : "<%=basePath %>assets/assetsPurchase/passAssets.action?id="+row.id,
							type:'post',
							success: function(data) {
								$.messager.alert('提示',data.resMsg);
								$('#declared').datagrid('reload');
							}
						});										
					}
		        });
	   		}else{
	   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
	   		}
			
		}
		function unratified(){
			var row = $('#declared').datagrid('getSelected'); //获取当前选中行     
			if(row != null){
				$.messager.prompt('提示信息','请输入不通过的原因:',function(sendData) {
					if (sendData) {
						if (sendData != null|| $.trim(sendData) != "") {
							$.ajax({
								url : "<%=basePath %>assets/assetsPurchase/noPassAssets.action",
								data:{reason:sendData,id:row.id},
								type:'post',
								success: function(data) {
									$.messager.alert('提示',data.resMsg);
									$('#declared').datagrid('reload');
								}
							});		
						}
					}
				});
			}else{
				$.messager.alert("操作提示", "请选择一条记录！", "warning");
			}
		}
		function view(){
			var row = $('#declaring').datagrid('getSelected'); //获取当前选中行   
			if(row != null){
				if(row.applState=='3'){
					$.messager.alert('提示','已通过');
				}else if(row.applState=='2'){
					$.ajax({
						url : "<%=basePath %>assets/assetsPurchase/seeReason.action?id="+row.id,
						type:'post',
						success: function(data) {
							console.info(data);
							$.messager.alert('提示',data.resMsg);
							$('#declaring').datagrid('reload');
						}
					});	
				}
			}else{
				$.messager.alert("操作提示", "请选择一条记录！", "warning");
			}
		}
		function del(){
			 //选中要删除的行
	        var iid = $('#declaring').datagrid('getChecked');
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
							url: "<c:url value='assets/assetsPurchase/delAssetsed.action'/>?id="+ids,
							type:'post',
							success: function() {
								$.messager.alert('提示','删除成功');
								$('#declaring').datagrid('reload');
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
		/**
		 * 查询
		 */
		function searchFromYSB() {
			var used = $('#used').textbox('getValue');
			var typed = $('#typed').textbox('getValue');
			var coded = $('#coded').textbox('getValue');
			var named = $('#named').textbox('getValue');
			var timeBegin = $('#timeBegin').val();
			var timeEnd = $('#timeEnd').val();
			$("#declared").datagrid({
				url: '<%=basePath %>assets/assetsPurchase/querySPAssets.action',
				queryParams:{officeName:used,
					className:typed,
					classCode:coded,
					deviceName:named,
					timeBegin:timeBegin,
					timeEnd:timeEnd,
					state:0},
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
			$('#timeBegin').val('');
			$('#timeBegin').val('');
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
			var timeBegin = $('#timingBegin').val();
			var timeEnd = $('#timingEnd').val();
			var stating = $('#stating').combobox('getValue');
			$("#declaring").datagrid({
				url: '<%=basePath %>assets/assetsPurchase/querySPAssets.action',
				queryParams:{officeName:used,
					className:typed,
					classCode:coded,
					deviceName:named,
					timeBegin:timeBegin,
					timeEnd:timeEnd,
					applState:stating,
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
			$('#timeBegin').val('');
			$('#timeBegin').val('');
			$('#stating').combobox('setValue','');
			searchFromSBZ()
		}
		function reloaded(){
			$('#declared').datagrid('reload');
		}
		function reloading(){
			$('#declaring').datagrid('reload');
		}
		function stateFormatter(value,row,index){
		   if(value==2){
			   return "未通过";
		   }else if(value==3){
			   return "通过";
		   }
		}
	</script>
</body>
</html>