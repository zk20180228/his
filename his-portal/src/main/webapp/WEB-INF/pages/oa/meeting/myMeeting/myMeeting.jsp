<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>我的会议</title>
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
// 				$('#list').height(winH-78-30-27-26);
				$('#notStart').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onLoadSuccess: function (data) {//默认选中
			            var rowData = data.rows;
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
			        },loadFilter : function(data){
						//过滤数据
						var value={
							total:0,
							rows:[]
							};
						var x=0;
						var value1={
							total:0,
							rows:[]
							};
						var x1=0;
						for (var i = 0; i < data.total; i++) {  
							if(data.rows[i].isEnd=="1"){//未开始会议
								value.rows[x++]=data.rows[i];
							}else if(data.rows[i].isEnd=="2"){//已结束会议
								value1.rows[x1++]=data.rows[i];
							}
						}
						value1.total = x1;
						haveEndList('haveEnd', value1);
						value.total = x;
						return value;
					},onDblClickRow: function (rowIndex, rowData) {//双击查看
						if(getIdUtil('#notStart').length!=0){
						   	 window.open("${pageContext.request.contextPath}/meeting/meetingApply/showMeetingDetail.action?id="+getIdUtil('#notStart'),'detailWindow',' left=273,top=49,width='+ (screen.availWidth -505) +',height='+ (screen.availHeight-152));
						   	}
					},
					onBeforeLoad:function(){
						//GH 2017年2月17日 翻页时清空前页的选中项
						$('#notStart').datagrid('clearChecked');
						$('#notStart').datagrid('clearSelections');
					}
				});
			});
			/**
			 * 加载tab内容
			 * @author  zxh
			 * @date 2017-07-17 9:25
			 * @version 1.0
			 */
			function haveEndList(listId, dataDetail){
				$('#'+listId).datagrid({
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
						for (var i = 0; i < data.total; i++) {  
							if(data.rows[i].isEnd=="2"){
								value.rows[x++]=data.rows[i];
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
						if(getIdUtil('#'+listId).length!=0){
						   	 window.open("${pageContext.request.contextPath}/meeting/meetingApply/showMeetingDetail.action?id="+getIdUtil('#'+listId),'detailWindow',' left=273,top=49,width='+ (screen.availWidth -505) +',height='+ (screen.availHeight-152));
						   	}
					},
					onBeforeLoad:function(){
						//GH 2017年2月17日 翻页时清空前页的选中项
						$('#'+listId).datagrid('clearChecked');
						$('#'+listId).datagrid('clearSelections');
					}
				});
				$('#'+listId).datagrid('loadData',dataDetail);
			}
	         	function reload(){
					//实现刷新栏目中的数据
					 $("#notStart").datagrid("reload");
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
				 * 回车键查询
				 * @author  zxh
				 * @date 2017-07-19
				 * @version 1.0
				 */
				$(window).keydown(function(event) {
				      if(event.keyCode == 13) {
				    	  searchFrom();
				      }
				});
				
				function searchFrom() {
						var queryMeetingName = $.trim($('#queryMeetingName').val());
						var queryMeetingApplicant = $.trim($('#queryMeetingApplicant').combobox('getValue'));
						var querymeetName = $.trim($('#querymeetName').combobox('getValue'));
// 						var querymeetingApptype = $.trim($('#querymeetingApptype').combobox('getValue'));
						
						$('#notStart').datagrid('load', {
							meetingName : queryMeetingName,
							meetingApplicant : queryMeetingApplicant,
							meetName : querymeetName,
// 							meetingApptype : querymeetingApptype,
						});
				}
				/**
				 *	预约情况周期渲染 
				 */
				function cricleFormatter(value, row, index){
					if("no"==row.meetingPeriodicity){
						var sTime = row.meetingStarttime.substring(0,10);
//		 				var eTime = row.meetingEndtime.substring(0,10);
						return sTime;
					}
					if("week"==row.meetingPeriodicity){
						var sTime = row.meetingStarttime.substring(0,10);
						var eTime = row.meetingEndtime.substring(0,10);
						value = value.replace("1","一").replace("2","二").replace("3","三").replace("4","四").replace("5","五").replace("6","六").replace("0","日")
						return sTime;
					}
					if("month"==row.meetingPeriodicity){
						var sTime = row.meetingStarttime.substring(0,10);
						var eTime = row.meetingEndtime.substring(0,10);
						return sTime;
					}
				}
				/**
				 *	预约情况周期渲染 
				 */
				 function modelFormatter(value, row, index){
						if("no"==row.meetingPeriodicity){
							return "非周期会议";
						}
						if("week"==row.meetingPeriodicity){
							return "周期会议（按周）";
						}
						if("month"==row.meetingPeriodicity){
							return "周期会议（按月）";
						}
					}
				 	/**
					 *	预约情况时间渲染 
					 */
					function timeFormatter(value, row, index){
						if("undefined"!=typeof(value)){
							return value.substring(11,19);
						}
					}
				 	/**
					 *	签到状态渲染 
					 *  签到标记：0-成功，1-迟到，8-未到
					 */
					function signStatusFormatter(value, row, index){
				 		if("0"==value){
				 			value = "准时签到";
				 		}else if("1"==value){
				 			value = "迟到";
				 		}else if("8"==value){
				 			value = "未到";
				 		}
				 		return value;
					}
				 	/**
					 *	提前签到时间渲染
					 */
					function signBeforeTimeFormatter(value, row, index){
				 		if(value!=null && value!=""){
					 		return value+"(分钟)";
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
					申请人：<input id="queryMeetingApplicant" class="easyui-combobox meetingApplicantSelect"></input>
					会议室：<input id="querymeetName" class="easyui-combobox meetNameSelect"></input>
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" style="height:25px;" iconCls="reset">重置</a>
    			</td>
			</tr>
		</table>
	</div>	
	<div data-options="region:'center',border:true">
		<div id="tabs" class="easyui-tabs" data-options="fit:true">   
		    <div title="未开始" data-options="fit:true">
				<table id="notStart" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/meeting/meetingApply/queryMyMeeting.action?applyFLag=3',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'id',hidden:true" ></th>
							<th data-options="field:'meetingName'" width="10%">会议名称</th>
							<th data-options="field:'meetName'"width="10%">会议室</th>
							<th data-options="field:'meetingApplicant'"width="6%">申请人</th>
							<th data-options="field:'meetingAttendance'"width="36%">出席人员</th>
							<th data-options="field:'meetingStarttime',formatter:timeFormatter"width="6%">开始时间</th>
							<th data-options="field:'meetingEndtime',formatter:timeFormatter"width="6%">结束时间</th>
							<th data-options="field:'meetingApplyweek',formatter:cricleFormatter"width="6%">会议日期</th>
							<th data-options="field:'meetingPeriodicity',formatter:modelFormatter"width="10%">周期模式</th>
							<th data-options="field:'signBeforeTime',formatter:signBeforeTimeFormatter"width="8%">提前签到时间</th>
						</tr>
					</thead>
				</table>
			</div>
		    <div title="已结束" data-options="fit:true">
				<table id="haveEnd" style="width:100%;" data-options="rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId0'">
					<thead>
						<tr>
							<th data-options="field:'id',hidden:true" ></th>
							<th data-options="field:'meetingName'" width="10%">会议名称</th>
							<th data-options="field:'meetName'"width="10%">会议室</th>
							<th data-options="field:'meetingApplicant'"width="6%">申请人</th>
							<th data-options="field:'meetingAttendance'"width="36%">出席人员</th>
							<th data-options="field:'meetingStarttime',formatter:timeFormatter"width="6%">开始时间</th>
							<th data-options="field:'meetingEndtime',formatter:timeFormatter"width="6%">结束时间</th>
							<th data-options="field:'meetingApplyweek',formatter:cricleFormatter"width="6%">会议日期</th>
							<th data-options="field:'meetingPeriodicity',formatter:modelFormatter"width="8%">周期模式</th>
							<th data-options="field:'signStatus',formatter:signStatusFormatter"width="4%">签到状态</th>
<!-- 							<th data-options="field:'signStatus'"width="10%">签到状态</th> -->
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>
</body>
</html>