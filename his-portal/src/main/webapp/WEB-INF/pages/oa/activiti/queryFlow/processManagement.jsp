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
<title>事务办理</title>
<style>
*{
	box-sizing:border-box;
}
</style>
</head>
<body>
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'center'" style="width: 100%;height: 100%">
	    	<div id="tt" class="easyui-tabs" data-options="fit:true">
				<div title="待处理业务" style="width: 100%;height: 100%;margin-bottom: 20px">
					<div style="width: 100%;height: 25px;padding: 5px;position: absolute;left: 0;">
						时间:<input id="pendingStartTime" class="Wdate" type="text" name="startTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'pendingEndTime\')}'})" style="height:22px;width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						至<input id="pendingEndTime" class="Wdate" type="text" name="endTime"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'pendingStartTime\')}'})" style="height:22px;width:180px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
						标题:<input id="pendingTitle" class="easyui-textbox"/>
						<a href="javascript:void(0)" onclick="searchPending()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<a href="javascript:void(0)" onclick="reloadPending()" class="easyui-linkbutton" style="height:25px;" iconCls="reset">重置</a>
					</div>
					<div  style="width: 100%;height: 100%;padding-top: 35px;">
						<table id="pendingBussiness" style="width: 100%;height: 100%;"></table>
					</div>	
				</div>
				<div title="审批记录" style="width: 100%;height: 100%">
					<div style="width: 100%;height: 25px;padding: 5px;position: absolute;left: 0;">
						时间:<input id="approveStartTime" class="Wdate" type="text" name="startTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'approveEndTime\')}'})" style="height:22px;width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						至<input id="approveEndTime" class="Wdate" type="text" name="endTime"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'approveStartTime\')}'})" style="height:22px;width:180px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
						标题:<input id="approveTitle" class="easyui-textbox"/>
						<a href="javascript:void(0)" onclick="searchApprove()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<a href="javascript:void(0)" onclick="reloadApprove()" class="easyui-linkbutton" style="height:25px;" iconCls="reset">重置</a>
					</div>
					<div  style="width: 100%;height: 100%;padding-top: 35px;">
						<table id="approveRecord" style="width: 100%;height: 100%;"></table>
					</div>	
				</div>
				<div title="业务催办" style="width: 100%;height: 100%">
					<div style="width: 100%;height: 25px;padding: 5px;position: absolute;left: 0;">
						时间:<input id="shoudaoStartTime" class="Wdate" type="text" name="startTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'shoudaoEndTime\')}'})" style="height:22px;width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						至<input id="shoudaoEndTime" class="Wdate" type="text" name="endTime"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'shoudaoStartTime\')}'})" style="height:22px;width:180px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
						标题:<input id="shoudaoTitle" class="easyui-textbox"/>
						<a href="javascript:void(0)" onclick="searchShoudao()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<a href="javascript:void(0)" onclick="reloadShoudao()" class="easyui-linkbutton" style="height:25px;" iconCls="reset">重置</a>
					</div>
					<div  style="width: 100%;height: 100%;padding-top: 35px;">
						<table id="shoudaoFinish" style="width: 100%;height: 100%;"></table>
					</div>	
				</div>
			</div>
	    </div>
	</div>
	<div id="dialogDivId"></div>
</body>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/97DP/WdatePicker.js"></script> --%>
<script>
//var userMap = new Map();
var procedureId="";
var remindnode="";

	$(function(){
		bindEnterEvent('pendingTitle',searchPending,'easyui');//绑定回车事件
		bindEnterEvent('approveTitle',searchApprove,'easyui');//绑定回车事件
		bindEnterEvent('shoudaoTitle',searchShoudao,'easyui');//绑定回车事件
// 		$.ajax({// 使用渲染字段，因此不用前台处理
<%-- 			url: "<%=basePath%>baseinfo/employee/getEmplMap.action", --%>
// 			type: 'post',
// 			async: false,
// 			success: function(data) {
// 				userMap = data;
// 			}
// 		});
// 		事务标题、申请人、申请时间、提交人、提交时间、当前环节、办理时限、催办次数
//============================待处理业务====================================================================
		$('#pendingBussiness').datagrid({
			url:'<%=basePath%>activiti/queryFlow/queryAgency.action',
			rownumbers:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			fit:true,
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			columns:[[    
		        {
					field: 'attr2',
					title: '事务标题',
					align: 'left',
					width:'20%',
					halign: 'center'
				}, {
					field: 'createuserName',
					title: '申请人',
					align: 'center',
					width:'10%',
					halign: 'center'
				}, {
					field: 'createTime',
					title: '申请时间',
					align: 'center',
					width:'10%',
					halign: 'center'
				}, {
					field: 'processStarterName',
					title: '提交人',
					align: 'center',
					width:'15%',
					halign: 'center'
				}, 
// 				{
// 					field: 'completeTime',
// 					title: '提交时间',
// 					align: 'center',
// 					width:'10%',
// 					halign: 'center'
// 				}, 
				{
					field: 'name',
					title: '当前环节',
					align: 'center',
					width:'10%',
					halign: 'center'
				}, 
// 				{
// 					field: 'attr4',
// 					title: '办理时限',
// 					align: 'center',
// 					width:'8%',
// 					halign: 'center'
// 				}, 
				{
					field: 'attr5',
					title: '催办次数',
					align: 'center',
					width:'5%',
					halign: 'center'
				}, {
					field: 'operation',
					title: '操作',
					align: 'center',
					width:'5%',
					halign: 'center'
				}
		    ]],
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
								operation : '<a class="sickCls1" onclick="shenpi(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'
							}
						});
					}
				$('.sickCls1').linkbutton({text:'办理',plain:false,width:'50px',height:'20px'}); 
				}
			}
		});
//============================审批记录====================================================================		
		$('#approveRecord').datagrid({
			url: '<%=basePath%>activiti/queryFlow/querylishi.action',
			rownumbers:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			fit:true,
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			columns:[[   
		        {
					field: 'attr2',
					title: '事务标题',
					align: 'left',
					width:'20%',
					halign: 'center'
				},{
					field: 'name',
					title: '审批环节 ',
					align: 'left',
					width:'10%',
					halign: 'center'
				}, {
					field: 'createuserName',
					title: '申请人',
					align: 'center',
					halign: 'center',
					width:'10%'
				}, {
					field: 'createTime',
					title: '申请时间',
					align: 'center',
					width:'10%',
					halign: 'center'
				}, {
					field: 'processStarterName',
					title: '提交人',
					align: 'center',
					halign: 'center',
					width:'10%'
				}, {
					field: 'completeTime',
					title: '提交时间',
					align: 'center',
					width:'10%',
					halign: 'center'
				}, {
					field: 'operation',
					title: '操作',
					align: 'center',
					width:'15%',
					halign: 'center'
				} 
				/* , {
					field: 'attr4',
					title: '办理时限',
					align: 'center',
					width:'5%',
					halign: 'center',
					formatter:dayFormatter
				} */
		    ]],
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
									operation : '<a class="sickCls6" onclick="query(\''+rows[i].processInstanceId+'\')" href="javascript:void(0)"></a>'
									+ '<a class="sickCls9" onclick="shenpixiangq(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'
									+ '<a class="sickCls8" onclick="recall(\''+rows[i].taskId+'\',\''+rows[i].completeTime+'\')" href="javascript:void(0)"></a>'
								}
							});
					}
				$('.sickCls6').linkbutton({text:'流转详情',plain:false,height:'20px'}); 
				$('.sickCls9').linkbutton({text:'审批详情',plain:false,height:'20px'}); 
				$('.sickCls8').linkbutton({text:'撤回',plain:false,width:'50px',height:'20px'}); 
				};
		    }
		});
//===========================业务催办====================================================================		
		$('#shoudaoFinish').datagrid({
			url: '<%=basePath%>activiti/queryFlow/listWOcuiban.action',
			rownumbers:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			fit:true,
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			columns:[[
			{
				field: 'procedureName',
				title: '催办标题',
				align: 'left',
				width:'20%',
				halign: 'center'
			}, {
				field: 'reminderName',
				title: '催办人',
				align: 'center',
				width:'10%',
				halign: 'center'
			}, {
				field: 'remindenodeName',
				title: '催办环节',
				align: 'center',
				width:'10%',
				halign: 'center'
			},
			 {
				field: 'remindcontent',
				title: '催办内容',
				align: 'center',
				width:'10%',
				halign: 'center',
// 				formatter: userFormatter
			},{
				field: 'remidereTime',
				title: '回复时间',
				align: 'center',
				width:'10%',
				halign: 'center',
// 				formatter: userFormatter
			}, {
				field: 'remindreContent',
				title: '回复内容',
				align: 'center',
				width:'15%',
				halign: 'center'
			}, {
				field: 'operation',
				title: '操作',
				align: 'center',
				width:'10%',
				halign: 'center'
			} ]],
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
						if(rows[i].remindreStatus==1){
							if(rows[i].taskInfoId==null||rows[i].taskInfoId==''){
								$(this).datagrid('updateRow',{
									index: $(this).datagrid('getRowIndex',rows[i]),
									row: {
										operation : '<a class="sickCls3" onclick="yidu1(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'+
													'<a class="sickCls2" onclick="huifu(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'
									}
								});
							}else{
								$(this).datagrid('updateRow',{
									index: $(this).datagrid('getRowIndex',rows[i]),
									row: {
										operation : '<a class="sickCls3" onclick="yidu1(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'+
													'<a class="sickCls2" onclick="huifu(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'+
													'<a class="sickCls7" onclick="shenpi(\''+rows[i].taskInfoId+'\')" href="javascript:void(0)"></a>'
									}
								});
							}
						}else{
							if(rows[i].taskInfoId==null||rows[i].taskInfoId==''){
								$(this).datagrid('updateRow',{
									index: $(this).datagrid('getRowIndex',rows[i]),
									row: {
										operation : '<a class="sickCls4" onclick="yidu(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'+
													'<a class="sickCls2" onclick="huifu(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'
									}
								});
							}else{
								$(this).datagrid('updateRow',{
									index: $(this).datagrid('getRowIndex',rows[i]),
									row: {
										operation : '<a class="sickCls4" onclick="yidu(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'+
													'<a class="sickCls2" onclick="huifu(\''+rows[i].id+'\')" href="javascript:void(0)"></a>'+
													'<a class="sickCls7" onclick="shenpi(\''+rows[i].taskInfoId+'\')" href="javascript:void(0)"></a>'
									}
								});
							}
						}
					}
				$('.sickCls4').linkbutton({text:'未读',plain:false,width:'50px',height:'20px'}); 
				$('.sickCls3').linkbutton({text:'已读',plain:false,width:'50px',height:'20px'}); 
				$('.sickCls3').linkbutton('disable');
				$('.sickCls2').linkbutton({text:'回复',plain:false,width:'50px',height:'20px'}); 
				$('.sickCls7').linkbutton({text:'办理',plain:false,width:'50px',height:'20px'}); 
				};
			}
		});
		$('#tt').tabs({
			onSelect:function(title,index){
				if(index==0){//
					$('#pendingBussiness').datagrid("load",{
					});
				}else if(index==1){
					$('#approveRecord').datagrid("load",{
					});
				}else if(index==2){
					$('#shoudaoFinish').datagrid("load",{
					});
				}
			}
		});
	});
	function shenpixiangq(id){
		AddOrShowEast(id,"<%=basePath%>activiti/humanTask/viewTaskFormYj.action");
	}
	function yidu1(id){
		$.messager.alert('提示', "已经是已读状态！");
	}
	function huifu(id){
		procedureId = id;
		AddDeptdilogs("回复", "<%=basePath%>activiti/queryFlow/viewremind.action");
	}
	function cuibanSubmit(content){
		procedureId=procedureId;
		$.ajax({
			url: "<%=basePath%>activiti/queryFlow/cuibanHuifu.action",
				data:{processInstanceId:procedureId,remindcontent:content},
				type: 'post',
				async: false,
				success: function(data) {
					$.messager.alert('提示', data.resMsg);
					$('#shoudaoFinish').datagrid('reload');
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
	function yidu(id){
		$.ajax({
			url: "<%=basePath%>activiti/queryFlow/cuibanYidu.action",
				data:{processInstanceId:id},
				type: 'post',
				async: false,
				success: function(data) {
					$.messager.alert('提示', data.resMsg);
					$('#shoudaoFinish').datagrid('reload');
				}
			});
	}
	
	function shenpi(id){
		AddOrShowEast(id,"<%=basePath%>activiti/humanTask/viewTaskForm.action");
	}
	
// 	function userFormatter(value, row, index) {
// 		return userMap[value];
// 	}
	
	function dayFormatter(value, row, index) {
		return Number(value).toFixed(0);
	}
	
	function operateFormatterFinish(value, row, index) {
		return [
				'<div class="btn-group btn-group-xs" role="group">' +
				'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="query(\''+row.processInstanceId+'\')">详情</a>' +
				'</div>'
		]
	}
	
	function searchPending(){
		var startTime = $("#pendingStartTime").val();
		var endTime = $("#pendingEndTime").val();
		var title = $("#pendingTitle").val();
		$("#pendingBussiness").datagrid('load',{
			startTime : startTime,
			endTime : endTime,
			title : title
		});
	}
	
	function reloadPending(){
		$("#pendingStartTime").val("");
		$("#pendingEndTime").val("");
		$("#pendingTitle").val("");
		$("#pendingBussiness").datagrid('load',{
			startTime : "",
			endTime : "",
			title : ""
		});
	}
	
	function searchApprove(){
		var startTime = $("#approveStartTime").val();
		var endTime = $("#approveEndTime").val();
		var title = $("#approveTitle").val();
		$("#approveRecord").datagrid('load',{
			startTime : startTime,
			endTime : endTime,
			title : title
		});
	}
	
	function reloadApprove(){
		$("#approveStartTime").val("");
		$("#approveEndTime").val("");
		$("#approveTitle").val("");
		$("#approveRecord").datagrid('load',{
			startTime : "",
			endTime : "",
			title : ""
		});
	}
	
	
	function searchShoudao(){
		var startTime = $("#shoudaoStartTime").val();
		var endTime = $("#shoudaoEndTime").val();
		var title = $("#shoudaoTitle").val();
		$("#shoudaoFinish").datagrid('load',{
			startTime : startTime,
			endTime : endTime,
			title : title
		});
	}
	
	function reloadShoudao(){
		$("#shoudaoStartTime").val("");
		$("#shoudaoEndTime").val("");
		$("#shoudaoTitle").val("");
		$("#shoudaoFinish").datagrid('load',{
			startTime : "",
			endTime : "",
			title : ""
		});
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
	function query(processInstanceId){
		AddOrShowEast1(processInstanceId,"<%=basePath%>activiti/operation/viewHistory.action");
	}
	//是否撤回
	function recall(taskId,completeTime){
		$.messager.confirm('确认', '确定要撤回吗?', function(res){
			if (res){
				recallSure(taskId,completeTime);
			}
		});
	}
	//撤回
	function recallSure(taskId,completeTime){
		$.ajax({
			url: "<%=basePath%>activiti/operation/recallHistory.action",
				data:{processInstanceId:taskId,completeTime:completeTime},
				type: 'post',
				async: false,
				success: function(data) {
					$.messager.alert('提示', data);
					if("撤回成功!"==data){
						reloadApprove();
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
	function xuanzhongSubmit(flag){
		if(flag==3){
			$('#approveRecord').datagrid({
				method: 'post',
				fit:true,
				rownumbers: true,
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 40, 60, 100 ],
				url: '<%=basePath%>activiti/queryFlow/querylishi.action'
			});
			$('#tt').tabs('select',"审批记录");
		}
	}
</script>
</html>