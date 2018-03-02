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
<title>我的事务</title>
<style>
*{
	box-sizing:border-box;
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'center'" style="width: 100%;height: 100%">
	    	<div id="tt" class="easyui-tabs" data-options="fit:true" style="width: 100%;height: 100%;">
				<div title="新建业务" style="width: 100%;height: 100%;position: relative;">
					<div id="layouttree" class="easyui-layout" data-options="fit:true">
						<div data-options="region:'west',title:'导航树',split:true,tools:'#toolSMId'" style="width:200px;">
							<ul id="tree"></ul>
							 <div id="toolSMId">
								<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
								<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
								<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
							</div>
						</div>
						<div data-options="region:'center'">
							<div style="width:100%;height:38px;position: absolute;top: 0;left: 0;z-index: 10;">
								<table style="width:100%;padding:5px;" class="changeskin">
									<tr>
										<td>
<%-- 											<input id="yewuGridType" class="easyui-combobox"  data-options=" url:'<%=basePath%>activiti/queryFlow/queryfenlei.action' ,prompt:'请选择事务类别..' ,valueField:'id',textField:'name',multiple:false" style="width:225px;"/> --%>
											<input id="yewuGridSearchTitle" class="easyui-textbox " data-options="prompt:'请输入事务标题...'" style="width:225px"/>
											<a href="javascript:void(0)" onclick="searchYewuGrid()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
											<a href="javascript:void(0)" onclick="reloadYewuGrid()" class="easyui-linkbutton" iconCls="reset">重置</a>
										</td>
									</tr>
								</table>
							</div>
							<div style="width:100%;height:100%; position: absolute;padding-top: 38px">
								<table id="yewuGrid" class="easyui-datagrid" style="width:100%;height:100%;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false">
									<thead>
										<tr>
											<th data-options="field:'categoryName',width:'15%'" align="left" halign="center">分类</th>
											<th data-options="field:'name',width:'30%'" align="left" halign="center">名称</th>
											<th data-options="field:'descn',width:'20%'" align="left" halign="center">描述</th>
											<th data-options="field:'operation',width:'15%'" align="center" halign="center">操作</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div title="草稿箱" style="width: 100%;height: 100%;position: relative;">
					<div style="width:100%;height:38px;position: absolute;top: 0;left: 0;z-index: 10;">
						<table style="width:100%;border:0px solid #95b8e7;padding:5px;" class="changeskin">
							<tr>
								<td>
									<input id="gaoGridSearchTitle" class="easyui-textbox " data-options="prompt:'请输入事务标题...'" style="width:225px"/>
									开始时间:<input id="gaoStartTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'gaoEndTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									结束时间:<input id="gaoEndTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'gaoStartTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									<a href="javascript:void(0)" onclick="searchGao()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="reloadGao()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div style="width:100%;height:100%; position: absolute;padding-top: 38px">
						<table id="gaoGrid" class="easyui-datagrid" style="width:100%;height:100%;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false" >
							<thead>
								<tr>
									<th data-options="field:'name',width:'25%'" align="left" halign="center">事务标题</th>
									<th data-options="field:'createTime',width:'15%'" align="center" halign="center">申请时间</th>
<!-- 									<th data-options="field:'createUser',width:'15%'" align="center" halign="center">申请人</th> -->
<!-- 									<th data-options="field:'expipationTime',width:'15%'" align="center" halign="center">办理时限</th> -->
									<th data-options="field:'operation',width:'8%'" align="center" halign="center">操作</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<div title="我的待办" style="width: 100%;height: 100%;position: relative;">
					<div style="width:100%;height:38px;position: absolute;top: 0;left: 0;z-index: 10;">
						<table style="width:100%;border:0px solid #95b8e7;padding:5px;" class="changeskin">
							<tr>
								<td>
									<input id="tuiGridSearchTitle" class="easyui-textbox " data-options="prompt:'请输入事务标题...'" style="width:225px"/>
									开始时间:<input id="tuiStartTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'tuiEndTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									结束时间:<input id="tuiEndTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'tuiStartTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									<a href="javascript:void(0)" onclick="searchTui()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="reloadTui()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div style="width:100%;height:100%; position: absolute;padding-top: 38px">
						<table id="tuiGrid" class="easyui-datagrid" style="width:100%;height:100%;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false" >
							<thead>
								<tr>
									<th data-options="field:'attr2',width:'25%'" align="left" halign="center">事务标题</th>
									<th data-options="field:'startTime',width:'15%'" align="center" halign="center">申请时间</th>
									<th data-options="field:'name',width:'15%'" align="center" halign="center">操作环节</th>
									<th data-options="field:'completeTime',width:'15%'" align="center" halign="center">操作时间</th>
									<th data-options="field:'operation',width:'8%'" align="center" halign="center">操作</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<div title="未结业务" style="width: 100%;height: 100%;position: relative;">
					<div style="width:100%;height:38px;position: absolute;top: 0;left: 0;z-index: 10;">
						<table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin">
							<tr>
								<td>
									<input id="demoGridSearchTitle" class="easyui-textbox " data-options="prompt:'请输入事务标题...'" style="width:225px"/>
									开始时间:<input id="demoStartTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'demoEndTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									结束时间:<input id="demoEndTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'demoStartTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									<a href="javascript:void(0)" onclick="searchdemo()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="reloaddemo()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div style="width:100%;height:100%; position: absolute;padding-top: 38px">
						<table id="demoGrid" data-options="fitColumns:false,singleSelect:true,fit:true,border:false">
							<thead>
								<tr>
									<th data-options="field:'attr2',width:'25%'" align="left" halign="center">事务标题</th>
									<th data-options="field:'name',width:'15%'" align="left" halign="center">当前环节</th>
									<th data-options="field:'reminderNum',width:'10%'" align="center" halign="center">催办次数</th>
									<th data-options="field:'startTime',width:'15%'" align="center" halign="center">申请时间</th>
<!-- 									<th data-options="field:'expirationTime',width:'15%'" align="center" halign="center">办理时限</th> -->
<!-- 									<th data-options="field:'completeStatus',width:'15%',formatter: stateFormatter" align="center" halign="center">状态</th> -->
									<th data-options="field:'operation',width:'20%'" align="center" halign="center">操作</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<div title="已结业务" style="width: 100%;height: 100%;position: relative;">
						<div style="width:100%;height:38px;position: absolute;top: 0;left: 0;z-index: 10;">
							<table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin" >
								<tr>
									<td>
										<input id="finishGridSearchTitle" class="easyui-textbox " data-options="prompt:'请输入事务标题...'" style="width:225px"/>
										开始时间:<input id="finishStartTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'finishEndTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
										结束时间:<input id="finishEndTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'finishStartTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
										<a href="javascript:void(0)" onclick="searchfinish()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
										<a href="javascript:void(0)" onclick="reloadfinish()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</td>
								</tr>
							</table>
						</div>
						<div style="width:100%;height:100%; position: absolute;padding-top: 38px">
							<table id="demoGridFinish" data-options="fitColumns:false,singleSelect:true,fit:true,border:false">
								<thead>
									<tr>
										<th data-options="field:'name',width:'25%'" align="left" halign="center">事务标题</th>
										<th data-options="field:'startTime',width:'15%'" align="center" halign="center">申请时间</th>
										<th data-options="field:'completeTime',width:'15%'" align="center" halign="center">完成时间</th>
										<th data-options="field:'reminderNum',width:'15%'" align="center" halign="center">催办次数</th>
										<th data-options="field:'endActId',width:'15%',formatter: zhuangtaiFormatter" align="center" halign="center">状态</th>
										<th data-options="field:'operation',width:'8%'" align="center" halign="center">操作</th>
									</tr>
								</thead>
							</table>
						</div>
				</div>
				<div title="我的催办" style="width: 100%;height: 100%;position: relative;">
					<div style="width:100%;height:38px;position: absolute;top: 0;left: 0;z-index: 10;">
						<table style="width:100%;border:0px solid #95b8e7;padding:5px;" class="changeskin">
							<tr>
								<td>
									<input id="cuibanGridSearchTitle" class="easyui-textbox " data-options="prompt:'请输入事务标题...'" style="width:225px"/>
									开始时间:<input id="cuibanStartTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'cuibanEndTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									结束时间:<input id="cuibanEndTime" class="Wdate" type="text" name="" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'cuibanStartTime\')}'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									<a href="javascript:void(0)" onclick="searchcuiban()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="reloadcuiban()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div style="width:100%;height:100%; position: absolute;padding-top: 38px">
						<table id="cuibanFinish" class="easyui-datagrid" style="width:100%;height:100%;" data-options="fitColumns:true,fit:true,border:false">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true" ></th> 
									<th data-options="field:'procedureName',width:'20%'" align="left" halign="center">事务标题</th>
									<th data-options="field:'remindTime',width:'10%'" align="left" halign="center">催办时间</th>
									<th data-options="field:'reminderNum',width:'5%'" align="center" halign="center">催办次数</th>
									<th data-options="field:'remindenodeName',width:'10%'" align="center" halign="center">催办环节</th>
									<th data-options="field:'remindcontent',width:'10%'" align="center" halign="center">催办内容</th>
									<th data-options="field:'reminderdName',width:'8%'" align="center" halign="center">被催办人员</th>
									<th data-options="field:'remindreStatus',formatter:reminderstatus,width:'5%'" align="center" halign="center">催办状态</th>
									<th data-options="field:'remidereTime',formatter:returnstatus,width:'5%'" align="center" halign="center">回复状态</th>
									<th data-options="field:'remindreContent',width:'13%'" align="center" halign="center">回复内容</th>
									<th data-options="field:'operation',width:'8%'" align="center" halign="center">操作</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<!-- <div title="我的代理" style="width: 100%;height: 100%;position: relative;">
					<div style="width:100%;height:38px;position: absolute;top: 0;left: 0;z-index: 10;">
						<table style="width:100%;border:0px solid #95b8e7;padding:5px;" class="changeskin">
							<tr>
								<td>
									<a href="javascript:void(0)" onclick="addMydelegateWin()" class="easyui-linkbutton" iconCls="icon-add">新建</a>
								</td>
							</tr>
						</table>
					</div>
					<div style="width:100%;height:100%; position: absolute;padding-top: 38px">
						<table id="Mydelegate" class="easyui-datagrid" style="width:100%;height:100%;" data-options="fitColumns:true,fit:true,border:false">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true" ></th> 
									<th data-options="field:'assingeName',width:'10%'" align="left" halign="center">委托人</th>
									<th data-options="field:'attorneyName',width:'10%'" align="left" halign="center">被委托人</th>
									<th data-options="field:'startTime',width:'13%'" align="center" halign="center">开始时间</th>
									<th data-options="field:'endTime',width:'13%'" align="center" halign="center">结束时间</th>
									<th data-options="field:'processName',width:'15%'" align="center" halign="center">流程定义</th>
									<th data-options="field:'activityName',width:'15%'" align="center" halign="center">任务</th>
									<th data-options="field:'status',width:'8%',formatter:deleInfoState" align="center" halign="center">状态</th>
									<th data-options="field:'operation',width:'10%'" align="center" halign="center">操作</th>
								</tr>
							</thead>
						</table>
					</div>
				</div> -->
			</div>
	    </div>
	</div>
	<div id="dialogDivId"></div>
	<script type="text/javascript">
		$(function (){
			bindEnterEvent('yewuGridSearchTitle',searchYewuGrid,'easyui');//绑定回车事件
// 			$('#yewuGridType').combobox('textbox').bind('focus',function(){
// 				$('#yewuGridType').combogrid('showPanel');
// 			});
			bindEnterEvent('gaoGridSearchTitle',searchGao,'easyui');//绑定回车事件
			bindEnterEvent('tuiGridSearchTitle',searchTui,'easyui');//绑定回车事件
			bindEnterEvent('demoGridSearchTitle',searchdemo,'easyui');//绑定回车事件
			bindEnterEvent('finishGridSearchTitle',searchfinish,'easyui');//绑定回车事件
			bindEnterEvent('cuibanGridSearchTitle',searchcuiban,'easyui');//绑定回车事件
			$('#tree').tree({
				url : '<%=basePath%>/oa/juris/getProcessTree.action',
				onClick : function(node){
					searchYewuGrid()
				},
				onLoadSuccess : function(node, data){
					var id = data[0].children[0].id;
					$('#tree').tree('select',$('#tree').tree('find',id).target);
				},
				onBeforeCollapse:function(node){
					if(node.id=="1"){
						return false;
					}
			    }
			});
			$('#gaoGrid').datagrid({
				fit:true,
				rownumbers: true,
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 40, 60, 100 ],
				pagePosition : 'bottom',
				url: '<%=basePath%>activiti/queryFlow/listOaKVRecord.action',
				onLoadSuccess:function(data){
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
					var rows = data.rows;
					if(rows.length>0){
						for(var i=0;i<rows.length;i++){
							$(this).datagrid('updateRow',{
								index: $(this).datagrid('getRowIndex',rows[i]),
								row: {
									operation : '<a class="sickCls1" onclick="deleteGao(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'+
												'<a class="sickCls2" onclick="add(\''+rows[i].category+'\',\''+rows[i].id+'\')" href="javascript:void(0)"></a>'
								}
							});
						}
					$('.sickCls1').linkbutton({text:'删除',plain:false,height:'20px'}); 
					$('.sickCls2').linkbutton({text:'编辑',plain:false,height:'20px'}); 
					}
				}
			});
			
			$('#yewuGrid').datagrid({
				rownumbers: true,
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 40, 60, 100 ],
				url: '<%=basePath%>oa/juris/getProcess.action',
				onLoadSuccess:function(data){
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
					var rows = data.rows;
					if(rows.length>0){
						for(var i=0;i<rows.length;i++){
							$(this).datagrid('updateRow',{
								index: $(this).datagrid('getRowIndex',rows[i]),
								row: {
									operation : '<a class="sickCls" onclick="faqi(\''+rows[i].id+'\',\''+(rows[i].topFlow==null?'':rows[i].topFlow)+'\')" href="javascript:void(0)"></a>'
								}
							});
						}
					$('.sickCls').linkbutton({text:'申请',plain:false,height:'20px'}); 
					}
				}
			});
			$('#tt').tabs({
				onSelect:function(title,index){
					if(index==0){
						searchYewuGrid()
					}else if(index==1){//草稿箱
						$('#gaoGrid').datagrid("load",{
						});
					}else if(index==2){
						$('#tuiGrid').datagrid({
							fit:true,
							rownumbers: true,
							pagination : true,
							pageSize : 20,
							pageList : [ 20, 40, 60, 100 ],
							url: '<%=basePath%>activiti/queryFlow/listtui.action',
							onLoadSuccess:function(data){
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
								var rows = data.rows;
								if(rows.length>0){
									for(var i=0;i<rows.length;i++){
										$(this).datagrid('updateRow',{
											index: $(this).datagrid('getRowIndex',rows[i]),
											row: {
												operation : '<a class="sickCls3" onclick="zhongzhi(\''+rows[i].processInstanceId+'\',\'tuiGrid\')" href="javascript:void(0)"></a>'+
															'<a class="sickCls4" onclick="edit(\''+rows[i].businessKey+'\')" href="javascript:void(0)"></a>'
											}
										});
									}
								$('.sickCls3').linkbutton({text:'终止',plain:false,height:'20px'}); 
								$('.sickCls4').linkbutton({text:'编辑',plain:false,height:'20px'}); 
								}
							}
						});
						
					}else if(index==3){//未结流程
						$('#demoGrid').datagrid({
							method: 'post',
							fit:true,
							rownumbers: true,
							singleSelect:true,
							pagination : true,
							pageSize : 20,
							pageList : [ 20, 40, 60, 100 ],
							url: '<%=basePath%>activiti/queryFlow/listWeiwan.action',
							onLoadSuccess:function(data){
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
								var rows = data.rows;
								if(rows.length>0){
									for(var i=0;i<rows.length;i++){
										$(this).datagrid('updateRow',{
											index: $(this).datagrid('getRowIndex',rows[i]),
											row: {
												operation : '<a class="sickCls5" onclick="zhongzhi(\''+rows[i].processInstanceId+'\',\'demoGrid\')" href="javascript:void(0)"></a>'+
															'<a class="sickCls6" onclick="cuiban(\''+rows[i].processInstanceId+'\',\''+rows[i].name+'\',\''+rows[i].attr2+'\',\''+rows[i].code+'\',\''+rows[i].assignee+'\',\''+rows[i].businessKey+'\')" href="javascript:void(0)"></a>'+
															'<a class="sickCls7" onclick="query(\''+rows[i].processInstanceId+'\')" href="javascript:void(0)"></a>'+
															'<a class="sickCls9" onclick="shenpixiangq(\''+rows[i].businessKey+'\')" href="javascript:void(0)"></a>'
															+
															'<a class="sickCls11" onclick="recall(\''+rows[i].processInstanceId+'\')" href="javascript:void(0)"></a>'
											}
										});
									}
								$('.sickCls5').linkbutton({text:'终止',plain:false,height:'20px'}); 
								$('.sickCls6').linkbutton({text:'催办',plain:false,height:'20px'}); 
								$('.sickCls7').linkbutton({text:'流转详情',plain:false,height:'20px'});
								$('.sickCls9').linkbutton({text:'审批详情',plain:false,height:'20px'});
								$('.sickCls11').linkbutton({text:'撤回',plain:false,height:'20px'}); 
								}
							}
						});
					}else if(index==4){//已结流程
						$('#demoGridFinish').datagrid({
							method: 'post',
							fit:true,
							singleSelect:true,
							rownumbers: true,
							pagination : true,
							pageSize : 20,
							pageList : [ 20, 40, 60, 100 ],
							url: '<%=basePath%>activiti/queryFlow/queryyijie.action',
							onLoadSuccess:function(data){
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
								var rows = data.rows;
								if(rows.length>0){
									for(var i=0;i<rows.length;i++){
										$(this).datagrid('updateRow',{
											index: $(this).datagrid('getRowIndex',rows[i]),
											row: {
												operation : '<a class="sickCls8" onclick="query(\''+rows[i].processInstanceId+'\')" href="javascript:void(0)"></a>'
															+'<a class="sickCls18" onclick="shenpixiangq(\''+rows[i].businessKey+'\')" href="javascript:void(0)"></a>'
											}
										});
									}
								$('.sickCls8').linkbutton({text:'流转详情',plain:false,height:'20px'}); 
								$('.sickCls18').linkbutton({text:'审批详情',plain:false,height:'20px'}); 
								}
							}
						});
					}else if(index==5){//我发起的催办
						$('#cuibanFinish').datagrid({
							method: 'post',
							fit:true,
							rownumbers: true,
							pagination : true,
							pageSize : 20,
							pageList : [ 20,40,60,100 ],
							url: '<%=basePath%>activiti/queryFlow/listcuiban.action',
							onLoadSuccess:function(data){
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
								var rows = data.rows;
								if(rows.length>0){
									for(var i=0;i<rows.length;i++){
										$(this).datagrid('updateRow',{
											index: $(this).datagrid('getRowIndex',rows[i]),
											row: {
												operation : '<a class="sickCls9" onclick="deletecui(\''+rows[i].procedureId+'\')" href="javascript:void(0)"></a>'
															+'<a class="sickCls10" onclick="detailHasten(\''+rows[i].procedureId+'\')" href="javascript:void(0)"></a>'
											}
										});
									}
								$('.sickCls10').linkbutton({text:'详情',plain:false,height:'20px'}); 
								}
							}
						});
					}
					<%-- else if(index==6){//我的代理
						$('#Mydelegate').datagrid({
							method: 'post',
							fit:true,
							rownumbers: true,
							pagination : true,
							pageSize : 20,
							pageList : [ 20,40,60,100 ],
							url: '<%=basePath%>activiti/delegateInfo/listMydelegate.action',
							onLoadSuccess:function(data){
								console.log(data);
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
								var rows = data.rows;
								if(rows.length>0){
									for(var i=0;i<rows.length;i++){
										$(this).datagrid('updateRow',{
											index: $(this).datagrid('getRowIndex',rows[i]),
											row: {
												operation : '<a class="sickCls11" onclick="deleteMydeledate(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'
															+'<a class="sickCls12" onclick="updateMydeledate(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'
											}
										});
									}
								$('.sickCls11').linkbutton({text:'删除',plain:false,width:'50px',height:'20px'}); 
								$('.sickCls12').linkbutton({text:'修改',plain:false,width:'50px',height:'20px'}); 
								}
							}
						});
					} --%>
				}
			});
		});
		function shenpixiangq(id){
			AddOrShowEast(id,"<%=basePath%>activiti/humanTask/viewTaskFormYj.action");
		}
		//是否撤回
		function recall(processInstanceId){
			$.messager.confirm('确认', '确定要撤回吗?', function(res){
				if (res){
					recallSure(processInstanceId);
				}
			});
		}
		//撤回
		function recallSure(processInstanceId){
			$.ajax({
				url: "<%=basePath%>activiti/operation/recall.action",
					data:{processInstanceId:processInstanceId},
					type: 'post',
					async: false,
					success: function(data) {
						$.messager.alert('提示', data);
						if("撤回成功!"==data){
							reloaddemo();
						}
					}
				});
		}
		function AddOrShowEast1(processInstanceId, url) {
			var w = $("body").width()*0.9
			var h = $("body").height()*0.9
			var processInstanceId = processInstanceId;
			var url = url;
			var name = '查看';
			var width = w;
			var height = h;
			var top = (window.screen.availHeight-30-height)/2;
			var left = (window.screen.availWidth-10-width)/2;
			if($("#winOpenFrom1").length<=0){  
				var form = "<form id='winOpenFrom1' action='"+url+"' method='post' target='"+name+"'>" +
						"<input type='hidden' id='winOpenFromInpId1' name='id'/></form>";
				$("body").append(form);
			} 
			$('#winOpenFromInpId1').val(processInstanceId);
			openWindow('about:blank',name,width,height,top,left);
			$('#winOpenFrom1').prop('action',url);
			$("#winOpenFrom1").submit();
		}
		function stateFormatter(value, row, index) {
			return '运行中';
		}
		function cuiban(processInstanceId,name,attr2,code,assignee,id) {
			processInstanceIdView=processInstanceId;
			nameView=name;
			attr2View=attr2;
			codeView=code;
			assigneeView=assignee;
			taskInfoIdView = id;
			AddDeptdilogs("催办信息", "<%=basePath%>activiti/queryFlow/viewremind.action");
		}
		function cuibanSubmit(remindcontent){
			var processInstanceId=processInstanceIdView;
			var name=nameView;
			var attr2=attr2View;
			var code=codeView;
			var assignee=assigneeView;
			var taskInfoId = taskInfoIdView;
			$.ajax({
				url: "<%=basePath%>activiti/queryFlow/savecuiban.action",
					data:{name:name,processInstanceId:processInstanceId,attr2:attr2,code:code,assignee:assignee,remindcontent:remindcontent,taskInfoId:taskInfoId},
					type: 'post',
					async: false,
					success: function(data) {
						$.messager.alert('提示', data.resMsg);
						$('#demoGrid').datagrid('reload');
					}
				});
		}
		function AddDeptdilogs(title, url) {
			$('#dialogDivId').dialog({    
			    title: title,    
			    width: '30%',    
			    height:'30%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			});    
		}
		function query(processInstanceId){
			AddOrShowEast1(processInstanceId,"<%=basePath%>activiti/operation/viewHistory.action");
		}
		function add(businessKey,id){
			AddOrShowEast(businessKey,"<%=basePath%>activiti/humanTask/viewVKForm.action?id="+ id);
		}
		function edit(businessKey){
			AddOrShowEast(businessKey,"<%=basePath%>activiti/humanTask/viewTaskForm.action");
		}
		function stateFormatterFinish(value, row, index) {
			return '完成';
		}
		
		function zhongzhi(processInstanceId,tableid) {
			$.messager.confirm('确认','您确认想要终止该条记录吗？',function(r){    
			    if (r){    
			    	$.ajax({
						url: "<%=basePath%>activiti/operation/endProcessInstance.action",
						data:{id:processInstanceId},
						type: 'post',
						async: false,
						success: function(data) {
							if(data.resMsg=="success"){
								$('#'+tableid).datagrid('reload');
//		 						setTimeout("load()",2000);
								$.messager.alert('提示', data.resCode);
							}else{
								$.messager.alert('提示', data.resCode);
							}
						}
					});
			    }    
			}); 
		};
		function load(){
			$('#gaoGrid').datagrid('reload')
			$('#tuiGrid').datagrid('reload')
		}
		
		/**新建业务搜索**/
		function searchYewuGrid(){
			var node = $('#tree').tree('getSelected');
			var name = $('#yewuGridSearchTitle').textbox('getText');//新建事务类别
			var category = null;
			var deptcode = null;
			if(node.id != 'root' && node.id != 'category' && node.id != 'deptcode'){
				if(node.attributes.pid == 'category'){
					category = node.id;
				}
				if(node.attributes.pid == 'deptcode'){
					deptcode = node.id;
				}
			}
			$('#yewuGrid').datagrid('load',{
				name : name,
				category:category,
				deptcode:deptcode
			});
		}
		function getAllTreeChild(node){
			var arr = new Array();
			var child = $('#tree').tree('getChildren',node.target);
			for (var i = 0; i < child.length; i++) {
				arr.push(child[i].id);
			}
			return arr;
		}
		/**新建业务重置**/
		function reloadYewuGrid(){
			$('#yewuGridSearchTitle').textbox('setText','');
// 			$('#yewuGridType').textbox('setText','');
			searchYewuGrid();
		}
		/**草稿箱搜索**/
		function searchGao(){
			var gaoTitle = $('#gaoGridSearchTitle').textbox('getText');//草稿标题
			var gaoStartTime = $('#gaoStartTime').val();//开始时间
			var gaoEndTime = $('#gaoEndTime').val();//结束时间
			$('#gaoGrid').datagrid('load',{category : gaoTitle,startTime : gaoStartTime,endTime : gaoEndTime});
		}
		/**草稿箱重置**/
		function reloadGao(){
			$('#gaoEndTime').val('');//结束时间
			$('#gaoGridSearchTitle').textbox('setText','');
			$('#gaoStartTime').val('');
			searchGao();
		}
		/**退件搜索**/
		function searchTui(){
			var tuiTitle = $('#tuiGridSearchTitle').textbox('getText');//草稿标题
			var tuiStartTime = $('#tuiStartTime').val();//开始时间
			var tuiEndTime = $('#tuiEndTime').val();//结束时间
			$('#tuiGrid').datagrid('load',{category : tuiTitle,startTime : tuiStartTime,endTime : tuiEndTime});
		}
		/**退件重置**/
		function reloadTui(){
			$('#tuiEndTime').val('');//结束时间
			$('#tuiGridSearchTitle').textbox('setText','');
			$('#tuiStartTime').val('');
			searchTui();
		}
		/**未结搜索**/
		function searchdemo(){
			var demoTitle = $('#demoGridSearchTitle').textbox('getText');//草稿标题
			var demoStartTime = $('#demoStartTime').val();//开始时间
			var demoEndTime = $('#demoEndTime').val();//结束时间
			$('#demoGrid').datagrid('load',{category : demoTitle,startTime : demoStartTime,endTime : demoEndTime});
		}
		/**未结重置**/
		function reloaddemo(){
			$('#demoEndTime').val('');//结束时间
			$('#demoGridSearchTitle').textbox('setText','');
			$('#demoStartTime').val('');
			searchdemo();
		}
		/**已结搜索**/
		function searchfinish(){
			var finishTitle = $('#finishGridSearchTitle').textbox('getText');//草稿标题
			var finishStartTime = $('#finishStartTime').val();//开始时间
			var finishEndTime = $('#finishEndTime').val();//结束时间
			$('#demoGridFinish').datagrid('load',{category : finishTitle,startTime : finishStartTime,endTime : finishEndTime});
		}
		/**已结重置**/
		function reloadfinish(){
			$('#finishEndTime').val('');//结束时间
			$('#finishGridSearchTitle').textbox('setText','');
			$('#finishStartTime').val('');
			searchfinish();
		}
		/**催办搜索**/
		function searchcuiban(){
			var cuibanTitle = $('#cuibanGridSearchTitle').textbox('getText');//草稿标题
			var cuibanStartTime = $('#cuibanStartTime').val();//开始时间
			var cuibanEndTime = $('#cuibanEndTime').val();//结束时间
			$('#cuibanFinish').datagrid('load',{
				param : cuibanTitle,
				startTime : cuibanStartTime,
				endTime : cuibanEndTime
				});
		}
		/**催办重置**/
		function reloadcuiban(){
			$('#cuibanEndTime').val('');//结束时间
			$('#cuibanGridSearchTitle').textbox('setText','');
			$('#cuibanStartTime').val('');
			searchcuiban();
		}
		function faqi(id,topFlow){
			if(topFlow!=''){//前置任务
				AddOrShowTopFlow(id,topFlow,"<%=basePath%>oa/extend/sickLeavePrivateList.action");
			}else{
				AddOrShowEast1(id,"<%=basePath%>activiti/operation/viewStartForm.action");
			}
		}
		function AddOrShowTopFlow(id,topFlow,url){
			var w = $("body").width()*0.9
			var h = $("body").height()*0.9
			var id = id;
			var url = url;
			var name = '查看';
			var width = w;
			var height = h;
			var top = (window.screen.availHeight-30-height)/2;
			var left = (window.screen.availWidth-10-width)/2;
			if($("#winOpenTopFlowFrom").length<=0){  
				var form = "<form id='winOpenTopFlowFrom' action='"+url+"' method='post' target='"+name+"'>" +
						"<input type='hidden' id='winOpenFromId' name='id'/>" +
						"<input type='hidden' id='winOpenFromTopFlow' name='topFlow'/>" +
				"</form>";
				$("body").append(form);
			} 
			$('#winOpenFromId').val(id);
			$('#winOpenFromTopFlow').val(topFlow);
			openWindow('about:blank',name,width,height,top,left);
			$('#winOpenTopFlowFrom').prop('action',url);
			$("#winOpenTopFlowFrom").submit();
		}
		function AddOrShowEast(id, url) {
			var w = $("body").width()*0.9
			var h = $("body").height()*0.9
			var id = id;
			var url = url;
			var name = '查看';
			var width = w;
			var height = h;
			var top = (window.screen.availHeight-30-height)/2;
			var left = (window.screen.availWidth-10-width)/2;
			if($("#winOpenFrom").length<=0){  
				var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
						"<input type='hidden' id='winOpenFromInpId' name='humanTaskId'/></form>";
				$("body").append(form);
			} 
			$('#winOpenFromInpId').val(id);
			openWindow('about:blank',name,width,height,top,left);
			$('#winOpenFrom').prop('action',url);
			$("#winOpenFrom").submit();
		}
		//打开窗口
		function openWindow(url,name,width,height,top,left){
			window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
		}
		function xuanzhongSubmit(flag){
			if(flag==1){
				$('#demoGrid').datagrid({
					method: 'post',
					fit:true,
					rownumbers: true,
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 40, 60, 100 ],
					url: '<%=basePath%>activiti/queryFlow/listWeiwan.action'
				});
				$('#tt').tabs('select',"未结业务");
			}else if(flag==2){
				$('#gaoGrid').datagrid({
					fit:true,
					rownumbers: true,
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 40, 60, 100 ],
					pagePosition : 'bottom',
					url: '<%=basePath%>activiti/queryFlow/listOaKVRecord.action'
				});
				$('#tt').tabs('select',"草稿箱");
			}else if(flag==3){
				$('#tuiGrid').datagrid({
					fit:true,
					rownumbers: true,
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 40, 60, 100 ],
					url: '<%=basePath%>activiti/queryFlow/listtui.action',
				});
			}
			
		}
		/*
		 *删除催办
		 */
		function deletecuiban(){
			var rows = $('#cuibanFinish').datagrid('getChecked');
			if(rows==null||rows.length<1){
				$.messager.alert('提示','请选择要删除的行...','info');
				return ;
			}
			$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			    if (r){    
			    	var rowsid = "";
					for(var i=0;i<rows.length;i++){
						if(rowsid!=""){
							rowsid += ",";
						}
						rowsid += rows[i].id;
					}
					$.ajax({
						url : '<%=basePath%>activiti/queryFlow/deleteMyCuiBan.action',
						data : {rowsid : rowsid},
						success : function(result){
							if(result.resCode=="success"){
								$.messager.alert('提示','删除成功!','info');
								$('#cuibanFinish').datagrid('reload');
							}else{
								$.messager.alert('提示','删除失败!','info');
							}
						},
						error : function(){
							$.messager.alert('提示','网络异常，请稍后重试...','info');
							return ;
						}
						
					});
			    }    
			});
		}
		/*
		 * 删除草稿箱
		 */
		function deleteGao(id){
			$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			    if (r){    
					$.ajax({
						url : '<%=basePath%>activiti/queryFlow/deleteMyGao.action',
						data : {rowsid : id},
						success : function(result){
							if(result.resCode=="success"){
								$('#gaoGrid').datagrid('reload');
								$.messager.alert('提示','删除成功!','info');
							}else{
								$.messager.alert('提示','删除失败!','info');
							}
						},
						error : function(){
							$.messager.alert('提示','网络异常，请稍后重试...','info');
							return ;
						}
						
					});
			    }    
			});
		}
		function zhuangtaiFormatter(value){
			var text = '';
			if(value==null){
				text = '人工终止';
			}else{
				text = '完成';
			}
			return text;
		}
		function reminderstatus(value){
			var text = '';
			switch (value) {
				case 0:
					text = '未读';
					break;
				case 1:
					text = '已读';
					break;
				default:
					text='未读';
					break;
				}
				return text;
		}
		function returnstatus(value){
			var text = '';
			if(value==null){
				text = '未回';
			}else{
				text = '已回';
			}
			return text;
		}
		/**
			删除催办
			id:流程编号(实例id)
		*/
		function deletecui(id){
			$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			    if (r){    
			    	$.ajax({
						url : '<%=basePath%>activiti/queryFlow/deleteMyCuiBanByProcessId.action',
						data : {rowsid : id},
						success : function(result){
							if(result.resCode=="success"){
								$.messager.alert('提示','删除成功!','info');
								$('#cuibanFinish').datagrid('reload');
							}else{
								$.messager.alert('提示','删除失败!','info');
							}
						},
						error : function(){
							$.messager.alert('提示','网络异常，请稍后重试...','info');
							return ;
						}
						
					});
			    }    
			});
		}
		/**
		催办详情
		id：id:流程编号(实例id)
		*/
		function detailHasten(id){
			AddOrShowEast(id,'<%=basePath%>activiti/queryFlow/toDetailHasten.action?rowsid='+id);
		}
		function AddOrShowEast(id,url) {
			var w = $("body").width()*0.9
			var h = $("body").height()*0.9
			var id = id;
			var url = url;
			var name = '查看';
			var width = w;
			var height = h;
			var top = (window.screen.availHeight-30-height)/2;
			var left = (window.screen.availWidth-10-width)/2;
			if($("#winOpenFrom").length<=0){  
				var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
						"<input type='hidden' id='winOpenFromInpId' name='humanTaskId'/></form>";
				$("body").append(form);
			}
			$('#winOpenFromInpId').val(id);
			openWindow('about:blank',name,width,height,top,left);
			$('#winOpenFrom').prop('action',url);
			$("#winOpenFrom").submit();
		}
		//打开窗口
		function openWindow(url,name,width,height,top,left){
			window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
		}
		function refresh(){
			$('#tree').tree('reload');
		}
		function collapseAll() {//关闭树
			$('#tree').tree('collapseAll');
		}
		function expandAll() {//展开树
			$('#tree').tree('expandAll');
		}
		
		//添加我的待处理弹窗
		function addMydelegateWin(){
			window.open("${pageContext.request.contextPath}/activiti/delegateInfo/addDelegateinfoView.action?",'newwindow',' left=260,top=49,width='+ (screen.availWidth -355) +',height='+ (screen.availHeight-152));
		}
		
		/**
		删除我的代理
		id:流程编号(实例id)
		*/
		function deleteMydeledate(id){
			$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			    if (r){    
			    	$.ajax({
						url : '<%=basePath%>activiti/delegateInfo/delMydelegate.action',
						data : {delInfoId : id},
						success : function(result){
							if(result.resCode=="success"){
								$.messager.alert('提示','删除成功!','info');
								$('#Mydelegate').datagrid('reload');
							}else{
								$.messager.alert('提示','删除失败!','info');
							}
						},
						error : function(){
							$.messager.alert('提示','网络异常，请稍后重试...','info');
							return ;
						}
						
					});
			    }    
			});
		}
		function updateMydeledate(id){
			window.open("${pageContext.request.contextPath}/activiti/delegateInfo/delegateinfoView.action?delInfoId="+id,'newwindow',' left=260,top=49,width='+ (screen.availWidth -355) +',height='+ (screen.availHeight-152));
		}
		
		function deleInfoState(value){
			if(value=="1"){
				return "有效";
			}else{
				return "无效";
			}
		}
	</script>
</body>
</html>