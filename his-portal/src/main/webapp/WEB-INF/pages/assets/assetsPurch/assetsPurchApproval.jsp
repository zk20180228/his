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
<title>设备采购审批</title>
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
								<input class="easyui-textbox" id="used"  style="width: 150px;"/>
								<span>设备分类:</span>
								<input id="typed" class="easyui-textbox" id="sorted" style="width: 130px;"/>
								<span>类别代码：</span>
								<input class="easyui-textbox" id="coded"  style="width: 150px;"/>
								<span>设备名称：</span>
								<input class="easyui-textbox" id="named"  style="width: 150px;"/>
								<span>申报时间：</span>
								<input id="startTime1" class="Wdate" type="text" name="startTime" value="${startTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime1\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								至 
								<input id="endTime1" class="Wdate" type="text" name="endTime" value="${endTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime1\')}'})" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<a href="javascript:void(0)" onclick="searchFromedDSP()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReloadDSP()" class="easyui-linkbutton" iconCls="reset">重置</a>
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
						            <th data-options="field:'applName',width:150,align:'left'">申报人</th>   
						            <th data-options="field:'officeName',width:200,align:'left'">办公用途</th>   
						            <th data-options="field:'classCode',width:150,align:'left'">类别代码</th>   
						            <th data-options="field:'className',width:150,align:'left'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'left'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'purchPrice',width:150,align:'right'">采购单价(元)</th>   
						            <th data-options="field:'purchNum',width:150,align:'right'">采购数量</th>   
						            <th data-options="field:'purchTotal',width:150,align:'right'">采购总价(元)</th>   
						            <th data-options="field:'applDate',width:150,align:'center'">申报时间</th>   
						            <th data-options="field:'applState',width:100,align:'center',formatter:stateFormatter">状态</th>   
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
								<input class="easyui-textbox" id="using"  style="width: 150px;"/>
								<span>设备分类:</span>
								<input id="typing" class="easyui-textbox"  style="width: 130px;"/>
								<span>类别代码：</span>
								<input class="easyui-textbox" id="coding"  style="width: 150px;"/>
								<span>设备名称：</span>
								<input class="easyui-textbox" id="naming"  style="width: 150px;"/>
								<span>申报时间：</span>
								<input id="startTime2" class="Wdate" type="text" name="startTime" value="${startTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime2\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								至 
								<input id="endTime2" class="Wdate" type="text" name="endTime" value="${endTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime2\')}'})" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<a href="javascript:void(0)" onclick="searchFromedYSP()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReloadYSP()" class="easyui-linkbutton" iconCls="reset">重置</a>
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
						            <th data-options="field:'applName',width:150,align:'left'">申报人</th>   
						            <th data-options="field:'officeName',width:200,align:'left'">办公用途</th>   
					           		<th data-options="field:'classCode',width:150,align:'left'">类别代码</th>   
						            <th data-options="field:'className',width:150,align:'left'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'left'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'purchPrice',width:150,align:'right'">采购单价(元)</th>   
						            <th data-options="field:'purchNum',width:150,align:'right'">采购数量</th>   
						            <th data-options="field:'purchTotal',width:150,align:'right'">采购总价</th>   
						            <th data-options="field:'applDate',width:150,align:'center'">申报时间</th>
						            <th data-options="field:'applState',width:100,align:'center',formatter:stateFormatter">状态</th>   
						               
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
					url: '<%=basePath %>assets/assetsPurch/querySPAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true,
					queryParams: {
						startTime:$('#startTime1').val(),
						endTime :$('#endTime1').val()
					}
				})
				
			}else if(title=="已审批"){
				state =1;
				$("#declaring").datagrid({
					url: '<%=basePath %>assets/assetsPurch/querySPAssets.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true,
					queryParams: {
						startTime:$('#startTime2').val(),
						endTime :$('#endTime2').val()
					}
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
							url : "<%=basePath %>assets/assetsPurch/passAssets.action?id="+row.id,
							type:'post',
							success: function(data) {
								$('#declared').datagrid('reload');
								var dataMap = eval('('+data+')');; 
								$.messager.alert('提示',dataMap.resMsg);
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
								url : "<%=basePath %>assets/assetsPurch/noPassAssets.action",
								data:{reason:sendData,id:row.id},
								type:'post',
								success: function(data) {
									$('#declared').datagrid('reload');
									var dataMap = eval('('+data+')');; 
									$.messager.alert('提示',dataMap.resMsg);
								}
							});		
						}
					}else{
						$.messager.alert("操作提示", "不通过原因不能为空！", "warning");
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
						url : "<%=basePath %>assets/assetsPurch/seeReason.action?id="+row.id,
						type:'post',
						success: function(data) {
							$('#declaring').datagrid('reload');
							$.messager.alert('提示',data.resMsg);
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
							url: "<c:url value='assets/assetsPurch/delAssetsed.action'/>?id="+ids,
							type:'post',
							success: function() {
								$('#declaring').datagrid('reload');
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
		 //待审批  条件查询
		function searchFromedDSP() {
			var used = $('#used').textbox('getValue');
			var typed = $('#typed').textbox('getValue');
			var coded = $('#coded').textbox('getValue');
			var named = $('#named').textbox('getValue');
			var startTime = $('#startTime1').val();
			var endTime = $('#endTime1').val();
			 if(startTime==''||endTime==''){
				 $.messager.alert('提示','时间不能为空');
				 return false;
			 }
			$('#declared').datagrid('load', {
				officeName:used,
				className:typed,
				classCode:coded,
				deviceName:named,
				startTime:startTime,
				endTime:endTime
			});
		}
		 //待审批 重置
		function searchReloadDSP() {
			$('#used').textbox('setValue','');
			$('#typed').textbox('setValue','');
			$('#coded').textbox('setValue','');
			$('#named').textbox('setValue','');
			$('#startTime1').val("${startTime}");
			$('#endTime1').val("${endTime}");
			searchFromedDSP()
		}
		 
		 
		 //已审批  条件查询
		function searchFromedYSP() {
			var used = $('#using').textbox('getValue');
			var typed = $('#typing').textbox('getValue');
			var coded = $('#coding').textbox('getValue');
			var named = $('#naming').textbox('getValue');
			var startTime = $('#startTime2').val();
			var endTime = $('#endTime2').val();
			 if(startTime==''||endTime==''){
				 $.messager.alert('提示','时间不能为空');
				 return false;
			 }
			$('#declaring').datagrid('load', {
				officeName:used,
				className:typed,
				classCode:coded,
				deviceName:named,
				startTime:startTime,
				endTime:endTime
			});
		}
		 //已审批 重置
		function searchReloadYSP() {
			$('#using').textbox('setValue','');
			$('#typing').textbox('setValue','');
			$('#coding').textbox('setValue','');
			$('#naming').textbox('setValue','');
			$('#startTime2').val("${startTime}");
			$('#endTime2').val("${endTime}");
			searchFromedYSP()
		}
		
		function reloaded(){
			$('#declared').datagrid('reload');
		}
		function reloading(){
			$('#declaring').datagrid('reload');
		}
		
		function stateFormatter(value,row,index){		
			if(value=='0'){
				return '草稿';
			}else if(value=='1'){		
				return '申请,待审核';
			}else if(value=='2'){		
				return '未批准';
			}else{
				return '已申报';
			}			
		}
	</script>
</body>
</html>