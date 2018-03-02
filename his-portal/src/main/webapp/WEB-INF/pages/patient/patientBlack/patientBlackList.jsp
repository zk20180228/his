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
<title>患者黑名单</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			var balckList=null;//用于显示黑名单类型集合
			
			//加载页面
			$(function(){
				var id="${id}"; //存储数据ID
				//加缓冲时间1毫秒
					//添加datagrid事件及分页
					$('#list').datagrid({
						pagination:true,
				   		pageSize:20,
				   		pageList:[20,30,50,80,100],
				   		onBeforeLoad:function (param) {
				   			$(this).datagrid('uncheckAll');
				   			$.ajax({
								url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
								data:{"type" : "blacklisttype"},
								type:'post',
								success: function(blackListdata) {
									balckList =  blackListdata ;
									
								}
							});
				        },
						onLoadSuccess: function (data) {//默认选中
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
				           var rowData = data.rows;
				            $.each(rowData, function (index, value) {
				            	if(value.id == id){
				            		$("#list").datagrid("checkRow", index);
				            	}
				            });
				        },
				        onDblClickRow: function (rowIndex, rowData) {//双击查看
								if(getIdUtil("#list").length!=0){
									AddOrShowEast('查看',"<c:url value='/patient/patientBlack/viewPatientBlack.action'/>?patientBlackId="+getIdUtil("#list"),'440');
							   	}
							}    
					});
					 bindEnterEvent('queryName',searchFrom,'easyui');//患者姓名
				 /**
				 * 黑名单显示
				 */
				//患者黑名单类型下拉框
				//黑名单下拉框
				$('#blackType').combobox({    
					url : '<%=basePath %>baseinfo/pubCodeMaintain/querybkackList.action',
					valueField : 'encode',
					textField : 'name',
					multiple : false

				});
			});
			function add(){
				   AddOrShowEast('添加',"<c:url value='/patient/patientBlack/addPatientBlack.action'/>",'410');
			}
			function edit(){
				var row = $('#list').datagrid('getSelected'); //获取当前选中行         
	            if(row){
                   	AddOrShowEast('编辑','<%=basePath%>patient/patientBlack/editPatientBlack.action','410','post',
 						   {"id":row.id,"name":row.patient.patientName});
		   		}
			}
			function del(){
				 //选中要删除的行
                   var iid = $('#list').datagrid('getChecked');
                  	if (iid.length > 0) {//选中几行的话触发事件	                        
				 	$.messager.confirm('提示信息', '您确定要删除选中信息吗？', function(res){//提示是否删除
 						if (res){
							var ids = '';
							for(var i=0; i<iid.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += iid[i].id;
							};
							$.ajax({
								url: "<c:url value='/patient/patientBlack/delPatientBlack.action'/>",
								type:'post',
								data:{id:ids,reason:res},
								success: function() {
									$.messager.alert("操作提示","删除成功！");
								$('#list').datagrid('reload');
								}
							});										
 						}
                       });
                   }
			}
	
			function reload(){
				 $("#list").datagrid("reload");
			}
			function searchFrom() {
				var queryName = $('#queryName').textbox('getValue');
				var blackType = $('#blackType').combobox('getValue');
				$('#list').datagrid('load', {
					name : queryName,
					blackType:blackType
				});
			}
			/**
			 * 重置
			 * @author huzhenguo
			 * @date 2017-03-21
			 * @version 1.0
			 */
			function clears(){
				$('#queryName').textbox('setValue','');
				$('#blackType').combobox('setValue','');
				searchFrom();
			}
			/**
			 * 动态添加LayOut
			 * @author  liujl
			 * @param title 标签名称
			 * @param url 跳转路径
			 * @date 2015-05-21
			 * @version 1.0
			 */
			function AddOrShowEast(title, url,width,method,params) {
				if(!method){
					method="get";
				}
				if(!params){
					params={};
				}
				var eastpanel=$('#panelEast'); //获取右侧收缩面板
				if(eastpanel.length>0){ //判断右侧收缩面板是否存在
					$('#divLayout').layout('remove','east');
					$('#divLayout').layout('add', {
						title : title,
						region : 'east',
						width :width ,
						split : true,
						href : url,
						method:method,
						queryParams:params
					});
				}else{//打开新面板
					$('#divLayout').layout('add', {
						title : title,
						region : 'east',
						width :width ,
						split : true,
						href : url,
						method:method,
						queryParams:params
					});
				}
			}
			function blackListFamater(value,row,index){
				if(value!=null){
					for(var i=0;i<balckList.length;i++){
						if(value==balckList[i].encode){
							return balckList[i].name;
						}
					}	
				}
			}
			 
			/**
			 * 回车键查询
			 * @author  liujl
			 * @param flg 标识：0=查询；1=编辑
			 * @date 2015-05-27
			 * @version 1.0
			 */
			function KeyDown(flg){   
		    	if(flg==0){
				    if (event.keyCode == 13)  
				    { 
				        event.returnValue=false;  
				        event.cancel = true;  
				        searchFrom();  
				    }  
			    }
			} 
			/**
			 * 关闭编辑窗口
			 */
			function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
		</script>
		<style type="text/css">
		.panel-header{
			border-left:0;
		}
		#panelEast{
			border:0
		}
		.patientBlackEditFont .honry-lable{
			border-left:0
		}
		</style>
</head>
<body style="margin: 0px; padding: 0px">
		<div class="easyui-layout" class="easyui-layout" fit=true
			style="width: 100%; height: 100%; overflow-y: auto;">
			<div data-options="region:'center',border:false">
				<div id="divLayout" class="easyui-layout" fit=true
					style="width: 100%; height: 100%; overflow-y: auto;">
					<div data-options="region:'north'" style="border: 0;height:70px;width:100%;padding-top: 14px;">
			   			<form id="search" method="post">
							<table style="width: 100%; border: 1px solid #95b8e7; padding: 5px;" class="changeskin">
								<tr>
									<td style="width:450px;">
										查询条件：<input class="easyui-textbox" id="queryName" name="queryName" onkeydown="KeyDown(0)" data-options="prompt:'姓名,病历号'" style="width: 130px;"/>
										黑名单类型：<input id="blackType" class="easyui-combobox" name="blackType" editable="false"  style="width: 130px;"/>
										<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
										<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</td>
								</tr>
							</table>
						</form>
			   		</div>
			   		
					<div data-options="region:'center',border:true" style="height:90%;width:100%;margin-top:0px">
						<table id="list" style="width: 100%;"
							data-options="url:'${pageContext.request.contextPath}/patient/patientBlack/queryPatientBlack.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true, width : '5%'" ></th>
									<th data-options="field:'patient', width : '5%',formatter:function(value,row,index){
														if (row.patient){
															return row.patient.patientName;
														} else {
															return value;
														}
													}">患者姓名</th>
									<th data-options="field:'medicalrecordId', width : '8%'" >病历号</th>
									<th data-options="field:'blacklistType',width :'8%',formatter:blackListFamater" >类型</th>
									<th data-options="field:'blacklistStarttime', width : '9%'" >有效开始</th>
									<th data-options="field:'blacklistEndtime', width : '9%'" >有效结束</th>
									<th data-options="field:'blacklistIntoreason', width : '13%'" >进入黑名单原因</th>
<!-- 									<th data-options="field:'blacklistOutreason', width : '13%'" >退出黑名单原因</th> -->
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias}:function:add">
				<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete">	
				<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
				<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
	</body>
</html>