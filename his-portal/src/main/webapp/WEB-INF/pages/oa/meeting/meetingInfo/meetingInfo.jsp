<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>会议室维护</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			
			$(function(){
				var winH=$("body").height();
				$('#list').height(winH-78-30-27-26);
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onLoadSuccess: function (data) {//默认选中
			            var rowData = data.rows;
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
						   	    AddOrShowEast('EditForm',"<c:url value='/meeting/meetingInfo/showMeetingRoom.action'/>?id="+getIdUtil('#list'));
						   	}
					},
					onBeforeLoad:function(){
						//GH 2017年2月17日 翻页时清空前页的选中项
						$('#list').datagrid('clearChecked');
						$('#list').datagrid('clearSelections');
					}
				});
			});
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
				/**
				 * 格式化复选框
				 * @author  zxh
				 * @date 2017-07-17 9:25
				 * @version 1.0
				 */
				function formatCheckBox(val,row,index){
					if ("Y" == val){
						return '是';
					} else {
						return '否';
					}
				}	
				function formatMeetState(val,row,index){
					if ("Y" == val){
						return '正常';
					} else {
						return '维修';
					}
				}
				function add(){
					AddOrShowEast('添加',"<c:url value='/meeting/meetingInfo/addMeeting.action'/>");
					$('#roleWins').dialog('close'); 
					$('#roleWins').dialog('destroy');
				}
				function edit(){
					  if(getIdUtil("#list")!= null){
	                      	AddOrShowEast('编辑',"<c:url value='/meeting/meetingInfo/editMeeting.action'/>?id="+getIdUtil("#list"));
	                    	$('#roleWins').dialog('close'); 
	            			$('#roleWins').dialog('destroy');
			   		  }
				}
				function del(){
				 //选中要删除的行
                   var iid = $('#list').datagrid('getChecked');
                  	if (iid.length > 0) {//选中几行的话触发事件	                        
				 	$.messager.confirm('确认', '您当前选中【'+iid.length+'】条数据,确定要全部删除吗?', function(res){//提示是否删除
						if (res){
							var ids = '';
							for(var i=0; i<iid.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += iid[i].id;
							};
							$.ajax({
								url: "<c:url value='/meeting/meetingInfo/delMeeting.action'/>?id="+ids,
								type:'post',
								success: function() {
									$.messager.alert('提示信息','删除成功');
								$('#list').datagrid('reload');
								}
							});										
						}
                       });
                   }else{
                   	$.messager.alert('警告！','请选择要删除的信息！','warning');
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
					$('#queryMeetName').textbox('setValue','');
					$('#queryMeetPlace').textbox('setValue','');
					searchFrom();
				}
				
				
				function searchFrom() {
					var queryMeetName = $.trim($('#queryMeetName').val());
					var queryMeetPlace = $.trim($('#queryMeetPlace').val());
					$('#list').datagrid('load', {
						meetName : queryMeetName,
						meetPlace : queryMeetPlace
					});
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
				function formatunit(val,row,index){
					if(val == 'D'){
						return "天";
					}if(val == 'W'){
						return "周";
					}if(val == 'H'){
						return "小时";
					}if(val == 'ONCE'){
						return "仅一次";
					}if(val == 'T'){
						return "必须时";
					}if(val == 'M'){
						return "分钟";
					}
					
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
	<div data-options="region:'north',border:false" style="height: 30px;padding-top: 3px">
		<table >
			<tr>
				<td>
					会议室名称：<input type="text" id="queryMeetName" name="queryMeetName"  class="easyui-textbox" />
					会议室地点：<input type="text" id="queryMeetPlace" name="queryMeetPlace"  class="easyui-textbox" />
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
	</div>	
	<div data-options="region:'center',border:true">
		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/meeting/meetingInfo/queryMeeting.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'">
<%-- 		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/meeting/meetingInfo/showMeeting.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'"> --%>
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true" ></th>
					<th data-options="field:'areaCode'" width="9%">所属院区</th>
					<th data-options="field:'meetCode'" width="9%">会议室编号</th>
					<th data-options="field:'meetName'"width="9%">会议室名称</th>
					<th data-options="field:'meetPlace'"width="9%">会议室地点</th>
					<th data-options="field:'meetNumber'"width="9%">容纳人数</th>
					<th data-options="field:'meetState',formatter:formatMeetState"width="9%">会议室状态</th>
					<th data-options="field:'meetType'"width="9%">会议室类型</th>
					<th data-options="field:'meetAdmin'"width="9%">会议室管理员</th>
<!-- 					<th data-options="field:'meetProjector'"width="9%">是否有投影</th> -->
<!-- 					<th data-options="field:'meetSound'"width="9%">是否有音响</th> -->
					<th data-options="field:'meetEquipment'"width="9%">会议室设备情况</th>
					<th data-options="field:'meetIsapply',formatter:formatCheckBox"width="9%">是否可申请</th>
					<th data-options="field:'meetPhone'"width="9%">联系方式</th>
					<th data-options="field:'meetDescribe'"width="9%">会议室描述</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		<shiro:hasPermission name="${menuAlias}:function:edit">
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>