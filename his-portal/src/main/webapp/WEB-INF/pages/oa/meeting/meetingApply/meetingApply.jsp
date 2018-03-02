<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>会议申请</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
			$(function(){
				/**
				 * 会议室下拉
				 */
				$('.meetNameSelect').combobox({
					url : "<%=basePath%>meeting/meetingInfo/findMeetingRoom.action",   
				    valueField:'meetName',    
				    textField:'meetName',
				    filter:function(q,row){
				    	var keys = new Array();
						keys[keys.length] = 'meetName';
						return filterLocalCombobox(q, row, keys);
				    }
				});
				/**
				 * 申请人下拉
				 */
				$('.meetingApplicantSelect').combobox({    
					url:'<%=basePath%>baseinfo/employee/employeeCombobox.action',   
				    valueField:'name',    
				    textField:'name',
				    filter:function(q,row){
				    	var keys = new Array();
						keys[keys.length] = 'name';
						return filterLocalCombobox(q, row, keys);
				    }
				});
				
				
				
				
				
				var winH=$("body").height();
				$('#list').height(winH-78-30-27-26);
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onLoadSuccess: function (data) {//默认选中
			            var rowData = data.rows;
			            approving('0', rowData);
			            approving('1', rowData);
			            approving('2', rowData);
			            approving('3', rowData);
// 			            $.each(rowData, function (index, value) {
// 			            	if(value.id == id){
// 			            		$('#list').datagrid('checkRow', index);
// 			            	}
// 			            });
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
						}
			        },onDblClickRow: function (rowIndex, rowData) {//双击查看
						if(getIdUtil('#list').length!=0){
						   	 window.open("${pageContext.request.contextPath}/meeting/meetingApply/showMeetingDetail.action?id="+getIdUtil('#list'),'detailWindow',' left=273,top=49,width='+ (screen.availWidth -505) +',height='+ (screen.availHeight-152));
						   	}
					},
					/* rowStyler: function(index,row){
						if(row.meetingApptype==1){//成功
							 return 'background-color:#98FB98;color:black;';
						}else if(row.meetingApptype==2){//拒绝
							 return 'background-color:#FF0000;color:black;';
						}
						if(row.meetingApptype==3){//撤销
							 return 'background-color:#9400D3;color:black;';
						}
					 }, */
					onBeforeLoad:function(){
						//GH 2017年2月17日 翻页时清空前页的选中项
						$('#list').datagrid('clearChecked');
						$('#list').datagrid('clearSelections');
					}
				});
			});
			
			/**
			 * 加载tab内容
			 * 申请状态0:申请状态,1:审核成功状态,2:申请拒绝状态,3:撤销状态
			 * @author  zxh
			 * @date 2017-07-17 9:25
			 * @version 1.0
			 */
			function approving(listId, dataDetail){
				$('#list'+listId).datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					loadFilter : function(data){
						//过滤数据
						var value={
							total:0,
							rows:[]
							};
						var x=0;
						for (var i = 0; i < data.length; i++) {  
							if(data[i].meetingApptype==listId){
								value.rows[x++]=data[i];
							}
						}
						value.total = x;
						return value;
					},
					onLoadSuccess: function (data) {//默认选中
// 			            var rowData = data.rows;
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
						}
			        },onDblClickRow: function (rowIndex, rowData) {//双击查看
						if(getIdUtil('#list'+listId).length!=0){
						   	 window.open("${pageContext.request.contextPath}/meeting/meetingApply/showMeetingDetail.action?id="+getIdUtil('#list'+listId),'detailWindow',' left=273,top=49,width='+ (screen.availWidth -505) +',height='+ (screen.availHeight-152));
						   	}
					},
					onBeforeLoad:function(){
						//GH 2017年2月17日 翻页时清空前页的选中项
						$('#list'+listId).datagrid('clearChecked');
						$('#list'+listId).datagrid('clearSelections');
					}
				});
				$('#list'+listId).datagrid('loadData',dataDetail);
			}
			
				/**
				 * 格式化复选框
				 * 申请状态0:申请状态,1:审核成功状态,2:申请拒绝状态,3:撤销状态
				 * @author  zxh
				 * @date 2017-07-17 9:25
				 * @version 1.0
				 */
				function formatApptype(val,row,index){
					if ("0" == val){
						return '申请中';
					}else if("1" == val){
						return '审核成功';
					}else if("2" == val){
						return '申请拒绝';
					}else{
						return '已撤销';
					}
				}	
				function formatMeetState(val,row,index){
					if ("Y" == val){
						return '正常';
					} else {
						return '维修';
					}
				}
				
				//添加。弹窗
				function add(){
					//添加时。清空页面
// 					clearValue(0);
					window.open("${pageContext.request.contextPath}/meeting/meetingApply/addMeetingApply.action",'newwindow',' left=273,top=49,width='+ (screen.availWidth -505) +',height='+ (screen.availHeight-152));
				}
				
				function edit(editId){
					var row = $('#list'+editId).datagrid('getSelected'); //获取当前选中行     
		            if(row != null){
		            	if(row.meetingApptype==1){
							$.messager.alert('提示信息','申请成功数据不可修改');
							return false; 
						}else{
							window.open("${pageContext.request.contextPath}/meeting/meetingApply/editMeetingApply.action?id="+row.id,'newwindow',' left=273,top=49,width='+ (screen.availWidth -505) +',height='+ (screen.availHeight-152));
		                   	$('#list').datagrid('reload');
						}
			   		}else{
			   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
			   		}
				}
				//距离当前多少天的日期
				function GetDateStr(AddDayCount) {
					var dd = new Date();
					dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
					var y = dd.getFullYear();
					var m = dd.getMonth()+1;//获取当前月份的日期
					if(Number(m)<10){
					       m = "0"+m;
					     }
				     var d = dd.getDate();
				     if(Number(d)<10){
				       d = "0"+d;
				     }
				     var h = dd.getHours();
				     if(Number(h)<10){
					       h = "0"+h;
					     }
				     var min = dd.getMinutes();
				     if(Number(min)<10){
					       min = "0"+min;
					     }
				     var s = dd.getSeconds();
				     if(Number(s)<10){
					       s = "0"+s;
					     }
					return y+"-"+m+"-"+d+" "+h+":"+min+":"+s;
				}
				function del(delId){
				 //选中要删除的行
                   var iid = $('#list'+delId).datagrid('getChecked');
                  	if (iid.length > 0) {//选中几行的话触发事件	                        
				 	$.messager.confirm('确认', '您当前选中【'+iid.length+'】条数据,确定要全部删除吗?', function(res){//提示是否删除
						if (res){
							var ids = '';
							for(var i=0; i<iid.length; i++){
								if(iid[i].meetingApptype==1){//审批通过
									if(iid[i].meetingEndtime<GetDateStr(0)){//会议结束时间小于当前时间，不能删除=====审批通过
										continue;
									}
								}
								if(ids!=''){
									ids += ',';
								}
								ids += iid[i].id;
							};
							if(ids!=''){
								$.ajax({
									url: "<c:url value='/meeting/meetingApply/delMeetingApply.action'/>?id="+ids,
									type:'post',
									success: function() {
										$.messager.alert('提示信息','删除成功');
										$('#list').datagrid('reload');
									}
								});										
							}else{
								$.messager.alert('提示信息','会议结束时间小于当前时间，不能删除');
							}
						}
                       });
                   }else{
                   	$.messager.alert('警告！','请选择要删除的信息！','warning');
                   	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
                   }
				}
				
				/**
				 * 撤销
				 */
				function redo(redoId){
				 //选中要撤销的行
                   var iid = $('#list'+redoId).datagrid('getChecked');
                  	if (iid.length > 0) {//选中几行的话触发事件	                        
				 	$.messager.confirm('确认', '您当前选中【'+iid.length+'】条数据,确定要全部撤销吗?', function(res){//提示是否撤销
						if (res){
							var ids = '';
							for(var i=0; i<iid.length; i++){
								if(iid[i].meetingApptype==1){//审批通过
									if(iid[i].meetingEndtime<GetDateStr(0)){//会议结束时间小于当前时间，不能撤销=====审批通过
										continue;
									}
								}
								if(ids!=''){
									ids += ',';
								}
								ids += iid[i].id;
							};
							if(ids!=''){
								$.ajax({
									url: "<c:url value='/meeting/meetingApply/redoMeetingApply.action'/>?id="+ids,
									type:'post',
									success: function() {
										$.messager.alert('提示信息','撤销成功');
										$('#list').datagrid('reload');
									}
								});		
							}else{
								$.messager.alert('提示信息','会议结束时间小于当前时间，不能撤销');
							}
															
						}
                       });
                   }else{
                   	$.messager.alert('警告！','请选择要撤销的信息！','warning');
                   	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
                   }
				}
	         	function reload(){
					//实现刷新栏目中的数据
					 $("#list").datagrid("reload");
				}
	         	
				function searchReload() {
					$('#queryMeetingName').textbox('setValue',"");
					$('#queryMeetingApplicant').combobox('setValue','');
					$('#queryMeetingApplicant').combobox('setText','');
					$('#querymeetName').combobox('setValue','');
					$('#querymeetName').combobox('setText','');
					$('#meetingNameMore').textbox('setValue',"");
					$('#meetingApplicantMore').textbox('setValue',"");
					$('#startTimeMore').val("");
					$('#endTimeMore').val("");
					$('#meetNameMore').textbox('setValue',"");
					$('#meetingApptypeMore').combobox('setValue',"");
					$('#meetingApptypeMore').combobox('setText',"");
					$('#meetingAttendanceMore').textbox('setValue',"");
					$('#meetingDeptMore').textbox('setValue',"");
					searchFrom();
				}
				
				/**
				 * 高级查询弹窗
				 */
				function searchFromMore(){
					$('#windowSrearchMore').window('open').window('resize',{width:'250px',height:'500px',top: top,left:left});
// 					var date = new Date();
// 					var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
// 					var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
// 					+ (date.getMonth() + 1);
// 					var strDate = date.getFullYear() + '-' + month + '-' + day;
// 					//$('#idcardNo').textbox('setValue',$('#idcardNoss').val());
					$('#meetingNameMore').textbox('setValue',"");
					$('#meetingApplicantMore').textbox('setValue',"");
					$('#startTimeMore').val("");
					$('#endTimeMore').val("");
					$('#meetNameMore').textbox('setValue',"");
					$('#meetingApptypeMore').combobox('setValue',"");
					$('#meetingApptypeMore').combobox('setText',"");
					$('#meetingAttendanceMore').textbox('setValue',"");
					$('#meetingDeptMore').textbox('setValue',"");
// 					$('#priIdcardNo').textbox('setValue',$('#idcardNo').val());
				}
				/**
				 * 回车键查询
				 * @author  zxh
				 * @date 2017-07-19
				 * @version 1.0
				 */
				$(window).keydown(function(event) {
				      if(event.keyCode == 13) {
				    	  searchFrom(0);
				      }
				});
				
				function searchFrom(flag) {
					$('#windowSrearchMore').window('close');
					if(flag == 0){
						var queryMeetingName = $.trim($('#queryMeetingName').val());
						var queryMeetingApplicant = $.trim($('#queryMeetingApplicant').combobox('getValue'));
						var querymeetName = $.trim($('#querymeetName').combobox('getValue'));
// 						var querymeetingApptype = $.trim($('#querymeetingApptype').combobox('getValue'));
						
						$('#list').datagrid('load', {
							meetingName : queryMeetingName,
							meetingApplicant : queryMeetingApplicant,
							meetName : querymeetName,
// 							meetingApptype : querymeetingApptype,
						});
					}else if(flag = 1){
						var meetingNameMore = $.trim($('#meetingNameMore').val());
						var meetingApplicantMore = $.trim($('#meetingApplicantMore').combobox('getValue'));
						var startTimeMore = $.trim($('#startTimeMore').val());
						var endTimeMore = $.trim($('#endTimeMore').val());
						if(""!=startTimeMore&&""!=endTimeMore){
						}else if(""==startTimeMore&&""==endTimeMore){
						}else{
							$.messager.alert('提示信息','开始时间或者结束时间为空');
							return false;
						}
						if(startTimeMore>endTimeMore){
							$.messager.alert('提示信息','开始时间大于结束时间');
							return false;
						}
						var meetNameMore = $.trim($('#meetNameMore').combobox('getValue'));
// 						var meetingApptypeMore = $.trim($('#meetingApptypeMore').val());
						var meetingAttendanceMore = $.trim($('#meetingAttendanceMore').val());
						var meetingDeptMore = $.trim($('#meetingDeptMore').val());
						$('#list').datagrid('load', {
							meetingName : meetingNameMore,
							meetingApplicant : meetingApplicantMore,
							meetingStarttime : startTimeMore,
							meetingEndtime : endTimeMore,
							meetName : meetNameMore,
// 							meetingApptype : meetingApptypeMore,
							meetingAttendance : meetingAttendanceMore,
							meetingDept : meetingDeptMore,
						});
					}
				}
				
				function closeSrearchMore(){
					$('#windowSrearchMore').window('close');
				}
				/**
				 * 动态添加LayOut
				 * @author  liujl
				 * @param title 标签名称
				 * @param url 跳转路径
				 * @date 2015-05-21
				 * @modifiedTime 2015-6-18
				 * @modifier liujl
				 * @version 1.0
				 */
				function AddOrShowEast(title, url) {
					var eastpanel=$('#panelEast'); //获取右侧收缩面板
					if(eastpanel.length>0){ //判断右侧收缩面板是否存在
						//重新装载右侧面板
				   		$('#divLayout').layout('panel','east').panel({
	                           href:url 
	                    });
					}else{//打开新面板
						$('#divLayout').layout('add', {
							region : 'east',
							width : '31%',
							split : true,
							href : url,
							closable : true
						});
					}
				}
				//用法
				function formatdescription(val,row,index){
					for(var i=0;i<setCodeUseagedata.length;i++){
						if(val==setCodeUseagedata[i].encode){
							return setCodeUseagedata[i].name;
						}
					}
				}	
	</script>
</head>
<body style="margin: 0px;padding: 0px">
<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 38px;padding: 5px">
		<table width="100%">
			<tr>
				<td>
					会议名称：<input type="text" id="queryMeetingName" name="queryMeetingName"  class="easyui-textbox" />
<!-- 					申请人：<input id="queryMeetingApplicant" class="easyui-combobox meetingApplicantSelect"></input> -->
					会议室：<input id="querymeetName" class="easyui-combobox meetNameSelect"></input>
					开始时间：从
						<input id="startTimeMore" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1099-12-30 00:00:00',maxDate:'2099-12-30 00:00:00'})" 
							class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						到
						<input id="endTimeMore" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1099-12-30 00:00:00',maxDate:'2099-12-30 00:00:00'})" 
							class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
<!-- 					会议状态：<input class="easyui-combobox" id="querymeetingApptype" -->
<!-- 							data-options="panelHeight:'100',editable:false, valueField: 'value',textField: 'text',data:[{value: '0',text: '申请中'},{value: '1',text: '审核成功'},{value: '2',text: '申请拒绝'},{value: '3',text: '已撤销'}]"/> -->
					<a href="javascript:void(0)" onclick="searchFrom(1)" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
<!-- 					<a href="javascript:void(0)" onclick="searchFromMore()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-help">高级查询</a> -->
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" style="height:25px;" iconCls="reset">重置</a>
<!-- 					<a href="javascript:void(0)" onclick="apply()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-ok">申请</a> -->
<!-- 					<a href="javascript:void(0)" onclick="redo()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-redo">撤销</a> -->
    				<!-- <span style="background-color: #98FB98">&nbsp;</span>
    				<span>审核成功状态</span>&nbsp;
    				<span style="background-color: FF0000">&nbsp;</span>
    				<span>申请拒绝状态</span>&nbsp;
    				<span style="background-color: #9400D3">&nbsp;</span>
    				<span>撤销状态</span>&nbsp; -->
    			</td>
			</tr>
		</table>
	</div>	
	<div data-options="region:'center',border:true">
		<div id="tabs" class="easyui-tabs" data-options="fit:true">   
		    <div title="申请会议" data-options="fit:true">
<!-- 		    会议申请标识 -->
				<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/meeting/meetingApply/queryMeetingApply.action?applyFLag=1',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'">
		<%-- 		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/meeting/meetingInfo/showMeeting.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'"> --%>
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" ></th>
							<th data-options="field:'meetingName'" width="10%">会议名称</th>
							<th data-options="field:'meetingApplicant'"width="10%">申请人</th>
							<th data-options="field:'meetingAttendance'"width="40%">出席人员</th>
							<th data-options="field:'meetingStarttime'"width="10%">开始时间</th>
							<th data-options="field:'meetingEndtime'"width="10%">结束时间</th>
							<th data-options="field:'meetName'"width="10%">会议室</th>
							<th data-options="field:'meetingApptype',formatter:formatApptype"width="10%">申请状态</th>
						</tr>
					</thead>
				</table>
			</div>
		    <div title="待批准会议" data-options="fit:true">
				<table id="list0" style="width:100%;" data-options="rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId0'">
		<%-- 		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/meeting/meetingInfo/showMeeting.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'"> --%>
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" ></th>
							<th data-options="field:'meetingName'" width="10%">会议名称</th>
							<th data-options="field:'meetingApplicant'"width="10%">申请人</th>
							<th data-options="field:'meetingAttendance'"width="40%">出席人员</th>
							<th data-options="field:'meetingStarttime'"width="10%">开始时间</th>
							<th data-options="field:'meetingEndtime'"width="10%">结束时间</th>
							<th data-options="field:'meetName'"width="10%">会议室</th>
							<th data-options="field:'meetingApptype',formatter:formatApptype"width="10%">申请状态</th>
						</tr>
					</thead>
				</table>
			</div>
		    <div title="已批准会议" data-options="fit:true">
				<table id="list1" style="width:100%;" data-options="rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId1'">
		<%-- 		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/meeting/meetingInfo/showMeeting.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'"> --%>
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" ></th>
							<th data-options="field:'meetingName'" width="10%">会议名称</th>
							<th data-options="field:'meetingApplicant'"width="10%">申请人</th>
							<th data-options="field:'meetingAttendance'"width="40%">出席人员</th>
							<th data-options="field:'meetingStarttime'"width="10%">开始时间</th>
							<th data-options="field:'meetingEndtime'"width="10%">结束时间</th>
							<th data-options="field:'meetName'"width="10%">会议室</th>
							<th data-options="field:'meetingApptype',formatter:formatApptype"width="10%">申请状态</th>
						</tr>
					</thead>
				</table>
			</div>
		    <div title="未批准会议" data-options="fit:true">
				<table id="list2" style="width:100%;" data-options="rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId2'">
		<%-- 		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/meeting/meetingInfo/showMeeting.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'"> --%>
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" ></th>
							<th data-options="field:'meetingName'" width="10%">会议名称</th>
							<th data-options="field:'meetingApplicant'"width="10%">申请人</th>
							<th data-options="field:'meetingAttendance'"width="40%">出席人员</th>
							<th data-options="field:'meetingStarttime'"width="10%">开始时间</th>
							<th data-options="field:'meetingEndtime'"width="10%">结束时间</th>
							<th data-options="field:'meetName'"width="10%">会议室</th>
							<th data-options="field:'meetingApptype',formatter:formatApptype"width="10%">申请状态</th>
						</tr>
					</thead>
				</table>
			</div>
		    <div title="已撤销会议" data-options="fit:true">
				<table id="list3" style="width:100%;" data-options="rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId3'">
		<%-- 		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/meeting/meetingInfo/showMeeting.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'"> --%>
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" ></th>
							<th data-options="field:'meetingName'" width="10%">会议名称</th>
							<th data-options="field:'meetingApplicant'"width="10%">申请人</th>
							<th data-options="field:'meetingAttendance'"width="40%">出席人员</th>
							<th data-options="field:'meetingStarttime'"width="10%">开始时间</th>
							<th data-options="field:'meetingEndtime'"width="10%">结束时间</th>
							<th data-options="field:'meetName'"width="10%">会议室</th>
							<th data-options="field:'meetingApptype',formatter:formatApptype"width="10%">申请状态</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申请</a>
			<a href="javascript:void(0)" onclick="edit('')" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改申请</a>
			<a href="javascript:void(0)" onclick="redo('')" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true">撤销申请</a>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="del('')" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<div id="toolbarId0">
		<shiro:hasPermission name="${menuAlias}:function:add">
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申请</a>
			<a href="javascript:void(0)" onclick="edit(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改申请</a>
			<a href="javascript:void(0)" onclick="redo(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true">撤销申请</a>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="del(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<div id="toolbarId1">
		<shiro:hasPermission name="${menuAlias}:function:add">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申请</a>
		<shiro:hasPermission name="${menuAlias}:function:edit">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="edit(1)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改申请</a>
			<a href="javascript:void(0)" onclick="redo(1)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true">撤销申请</a>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="del(1)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<div id="toolbarId2">
		<shiro:hasPermission name="${menuAlias}:function:add">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申请</a>
		<shiro:hasPermission name="${menuAlias}:function:edit">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="edit(2)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改申请</a>
			<a href="javascript:void(0)" onclick="redo(2)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true">撤销申请</a>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="del(2)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<div id="toolbarId3">
		<shiro:hasPermission name="${menuAlias}:function:add">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">申请</a>
		<shiro:hasPermission name="${menuAlias}:function:edit">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="edit(3)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改申请</a>
			<a href="javascript:void(0)" onclick="redo(3)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true">撤销申请</a>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="del(3)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<div id="windowSrearchMore" class="easyui-window" title="高级查询列表" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:50%;height:320px;">
			<div data-options="region:'north'" style="width:90% ;padding:8px 5px 5px 5px;">
			    <table class="honry-table" cellpadding="0" cellspacing="0"   id="table1"
					border="0" style="margin-left:auto;margin-right:auto;margin-down:auto; overflow: auto;" >
					<tr>
						<td class="honry-lable">会议名称：</td>
						<td>
							<input id="meetingNameMore" class="easyui-textbox"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">申请人：</td>
						<td>
							<input id="meetingApplicantMore" class="easyui-combobox meetingApplicantSelect"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">开始时间：</td>
						<td>
						从
						<input id="startTimeMore" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1099-12-30 00:00:00',maxDate:'2099-12-30 00:00:00'})" 
							class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						到
						<input id="endTimeMore" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1099-12-30 00:00:00',maxDate:'2099-12-30 00:00:00'})" 
							class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">会议室：</td>
						<td>
							<input id="meetNameMore" class="easyui-combobox meetNameSelect"></input>
						</td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="honry-lable">会议状态：</td> -->
<!-- 						<td> -->
<!-- 							<input class="easyui-combobox" id="meetingApptypeMore" -->
<!-- 							data-options="panelHeight:'100',editable:false, valueField: 'value',textField: 'text',data:[{value: '0',text: '申请中'},{value: '1',text: '审核成功'},{value: '2',text: '申请拒绝'},{value: '3',text: '已撤销'}]"/> -->
<!-- 						</td> -->
<!-- 					</tr> -->
					<tr>
						<td class="honry-lable">参会人员：</td>
						<td>
							<input id="meetingAttendanceMore" class="easyui-textbox"></input>
						</td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="honry-lable">参会部门：</td> -->
<!-- 						<td> -->
<!-- 							<input id="meetingDeptMore" class="easyui-textbox"></input> -->
<!-- 						</td> -->
<!-- 					</tr> -->
				</table>
				<div style="text-align: center;padding: 5px">
					<a href="javascript:void(0)" onclick="searchFrom(1)" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="closeSrearchMore()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">关闭</a>
				</div>
			</div>
		</div>
</body>
</html>