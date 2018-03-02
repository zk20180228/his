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
<title>院长办公室</title>
<style type="text/css">
		#divLayout * {
			box-sizing: border-box;
		}
		*{
			margin: 0;
			padding: 0;
		}
		.tabs-panels {
			border-width: 0;
		}
	.panel.datagrid.easyui-fluid{
		height: 100% !important;
	}
</style>
</head>
<body>
	
	<div>
		<div id="tt" class="easyui-tabs" style="width:100%;height:100%;" data-options="fit:true">   
		    <div title="会议签到统计记录" style="width:100%;height:100%;"> 
		    	<div style="width:100%;height:5%;padding-top:6px">
					&nbsp;会议名称:<input class="easyui-textbox" style="width:150px" name="meetingName" id="meetingName"> 
					&nbsp;会议室:  <input id="cc1" name="meetingRoomName" >  
					&nbsp;会议状态: <select id="cc2" name="meetingStatusFlag" class="easyui-combobox" style="width:150px">
										 <option value=""></option> 
										 <option value="0">已结束</option>   
									     <option value="1">进行中</option>   
									     <option value="2">未开始</option>   
					               </select>  
					        &nbsp; <a href="javascript:void(0)"  id="query" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
							&nbsp;<a href="javascript:void(0)" id="reset"  class="easyui-linkbutton" iconCls="reset">重置</a>
				</div> 
				<div style="width:100%;height:94%;padding-top:2px">
					<table  id ="dg1"></table> 
				</div>
				
		    </div>   
		    <div title="会议组">   
		    	<div id="divLayout" style="width:100%;height:100%;">   
				   
				    <div class = "treeInfo borderColor" style="width: 15%;height: 100%; float: left;overflow-y: auto;border-right: 1px solid">
				    	<div style="width:100%;height:25px"><input id = "treeSearch"  style="width:100%;height:25px"  ></div> 
						<div>
							<ul class="easyui-tree" id="tDt" ></ul>  
						</div>
					</div> 
					
					<div class = "tableInfo" style="position: relative;width: 85%;height: 100%; float: left;">
					    <div style="width:100%;height:35px;position: absolute;top: 0;left: 0;    padding-top: 5px;">
					    			姓名:<input class="easyui-textbox"  id="employee_name" style="width:150px;height:25px"> 
					    			工号:<input class="easyui-textbox" id="employee_jobon" style="width:150px;height:25px">
					    			科室:
					    			<select id="cc" class="easyui-combobox"   name="dept_name" style="width:150px;height:25px">   

									</select>
								&nbsp; <a href="javascript:void(0)"  id="queryEmp" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px" onclick="queryEmp()">查询</a>
								&nbsp;<a href="javascript:void(0)" id="resetEmp"  class="easyui-linkbutton" iconCls="reset" onclick="resetEmp()">重置</a>
					   </div> 
					   
					    <div style="width:100%;height:100%;padding-top: 35px">
					   		 <table  id ="list"></table> 
					    </div>
					    
				   </div>
		    </div>
		</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		/**
		 * 会议室下拉
		 */
		$("#cc1").combobox({
			url : "${pageContext.request.contextPath}/meeting/meetingInfo/findMeetingRoom.action",   
		    valueField:'meetName',    
		    textField:'meetName'
		});
		
	
		
		//tree树加载完毕后默认选中第一个节点(easyui，好像有点问题，没成功)
		$('#tDt').tree({    
			    url:'${pageContext.request.contextPath}/meeting/emGroup/loadGroup.action'
			});  
		
		//tree树搜索
		 $("#treeSearch").textbox({
		        iconWidth: 20,
		        prompt:'名称,代码,拼音码,五笔码',
		        icons: [{
		            iconCls: 'icon-search',
		            handler: function (e) {                
		               var text= $("#treeSearch").textbox("getText");
		           	   $('#tDt').tree({
								queryParams : {
									text : text
								}
		               });
		            }
		        }]
		
		    });
		
		function searchTree(){
			 var text= $("#treeSearch").textbox("getText");
         	   $('#tDt').tree({
						queryParams : {
							text : text
						}
             });
		}
		//回车加载tree树
		bindEnterAndBlackEvent('treeSearch', searchTree,'easyui');
		
		//会议签到统计记录列表
		$('#dg1').datagrid(
						{
							url : '${pageContext.request.contextPath}/meeting/meetingSigned/meetingSignedList.action',
							fitColumns : true,
							idField : "id",
							pagination : true,
							striped : true,
							fit : true,
							border:false,
							pageList : [ 10, 20, 30, 40, 50 ],
							pageSize : 20,
							rownumbers : true,
							columns : [ [
									{
										field : 'id',
										checkbox : true,
										width : 100
									},
									{
										field : 'meetingName',
										title : '会议名称',
										width : 100,
										align : 'center'
									},
									{
										field : 'applyer',
										title : '申请人',
										width : 100,
										align : 'center'
									},
									{
										field : 'attendPersons',
										title : '出席人员',
										width : 350,
										align : 'center'
									},
									{
										field : 'meetingStartTime',
										title : '开始时间',
										width : 100,
										align : 'center'
									},
									{
										field : 'meetingEndTime',
										title : '结束时间',
										width : 100,
										align : 'center'
									},
									{
										field : 'meetingRoomName',
										title : '会议室',
										width : 100,
										align : 'center'
									},
									{
										field : 'meetingStatusFlag',
										title : '会议状态',
										width : 100,
										align : 'center',
										formatter : function(value, row, index) {
											if (row.meetingStatusFlag == '0') {
												return "已结束";
											} else if (row.meetingStatusFlag == '1') {
												return "进行中";
											} else {
												return "未开始";
											}
										}
									},
									{
										field : 'option',
										title : '操作',
										width : 100,
										align : 'center',
										formatter : function(value, row, index) {
											var id = "'" + row.id + "'";//加分号是为了，避免像"001"这样的数据到后台变为"1"
											if (row.meetingStatusFlag == '0') {
												return "<a onclick=look("
														+ id
														+ ") >查看</a>&nbsp;<a  onclick=del("
														+ id + ")>删除</a>";
											} else if (row.meetingStatusFlag == '1') {
												return "";
											} else {
												return "<a onclick=update("
														+ id
														+ ") >修改</a>&nbsp;<a   onclick=cancel("
														+ id + ") >撤销</a>";
											}
										}
									} ] ],
							toolbar : [
									{
										text : '会议申请',
										iconCls : 'icon-add',
										handler : function() {

											var bodyWidth =  $("body").width()
											var bodyHeight = $("body").height()
											var height = bodyHeight*1.1;
											var width = bodyWidth*0.7;
											var top = Math.round((window.screen.height - height) / 2)-45;
											var left = Math.round((window.screen.width - width) / 2);
											window.open(
															"${pageContext.request.contextPath}/meeting/meetingApply/addMeetingApply.action?flag=YZBGS",
															"_blank",
															"height="
																	+ height
																	+ ", width="
																	+ width
																	+ ", top="
																	+ top
																	+ ", left= "
																	+ left
																	+ ",status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no");
										}
									},
									'-',
									{
										text : '删除',
										iconCls : 'icon-remove',
										handler : function() {

											var ids = "";
											var rows = $("#dg1").datagrid("getSelections");
											if(rows.length<=0){
												$.messager.alert('提示', '请至少选择一行记录！');
												return ;
											}
											for (var i = 0; i < rows.length; i++) {
												if (i == rows.length - 1) {
													ids += rows[i].id;
												} else {
													ids += rows[i].id + ",";
												}
											}
											$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
											    if (r){    
											    	//删除
													del(ids);    
											    }    
											});
											
										}
									},
									'-',
									{
										text : '刷新',
										iconCls : 'icon-arrow_refresh',
										handler : function() {
											var meetingName = $('#meetingName')
													.textbox('getValue');
											var meetingRoomName = $('#cc1')
													.combobox('getText');//根据关键字模糊查询，不需要根据会议室编号
											var meetingStatusFlag = $('#cc2')
													.combobox('getValue');
											$('#dg1').datagrid({
																queryParams : {
																	meetingName : meetingName,
																	meetingRoomName : meetingRoomName,
																	meetingStatusFlag : meetingStatusFlag
																}
															});
										}
									} ],
						        	   onLoadSuccess: function(data){
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
						        	   }
						});

		
		$('#list').datagrid(
				{
					url : '${pageContext.request.contextPath}/meeting/emGroup/groupList.action',
					fitColumns : true,
					idField : "id",
					pagination : true,
					striped : true,
					fit : true,
					border:false,
					pageList : [ 10, 20, 30, 40, 50 ],
					pageSize : 20,
					rownumbers : true,
					columns : [ [
							{
								field : 'id',
								checkbox : true,
								width : 100
							},
							{
								field : 'employee_name',
								title : '姓名',
								width : 100,
								align : 'center'
							},
							{
								field : 'employee_jobon',
								title : '工号',
								width : 100,
								align : 'center'
							},
							{
								field : 'dept_name',
								title : '科室',
								width : 150,
								align : 'center'
							},
							{
								field : 'duties_name',
								title : '职务',
								width : 100,
								align : 'center'
							},
							{
								field : 'title_name',
								title : '职称',
								width : 100,
								align : 'center'
							},
							{
								field : 'employee_type_name',
								title : '人员类型',
								width : 100,
								align : 'center'
							},
							{
								field : 'creatUesr',
								title : '创建人',
								width : 100,
								align : 'center'
							},
							{
								field : 'creatTime',
								title : '创建时间',
								width : 100,
								align : 'center'
							},
							{
								field : 'option',
								title : '操作',
								width : 100,
								align : 'center',
								formatter : function(value, row, index) {
										var id = "'" + row.id + "'";//加分号是为了，避免像"001"这样的数据到后台变为"1"
										return "<a onclick=delEmp("+id+") >删除</a>";
									
								}
							} ] ],
					toolbar : [	{
						text : '添加成员',
						iconCls : 'icon-add',
						handler : function() {
							
							var node = $('#tDt').tree('getSelected');
							if(node==null||node.id=='ybGroup'){
								$.messager.alert('提示','请先选择组，再进行添加组员!');
								return false;
							}else{
								var id=node.id;
								var text=node.text;
								//弹出窗口
								var bodyWidth =  $("body").width()
								var bodyHeight = $("body").height()
								var height = bodyHeight*1.2;
								var width = bodyWidth*0.8;
								var top = Math.round((window.screen.height - height) / 2)-40;
								var left = Math.round((window.screen.width - width) / 2);
								window.open("${pageContext.request.contextPath}/meeting/emGroup/addEmpUI.action?id="+id+"&text="+encodeURI(text),"_blank",
												"height="
														+ height
														+ ", width="
														+ width
														+ ", top="
														+ top
														+ ", left= "
														+ left
														+ ",status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no");
							
							}
						}
					},
					'-',
					{
								text : '删除成员',
								iconCls : 'icon-remove',
								handler : function() {

									var ids = "";
									var rows = $("#list").datagrid("getSelections");
									if(rows.length<=0){
										$.messager.alert('提示', '请至少选择一行记录！');
										return ;
									}
									for (var i = 0; i < rows.length; i++) {
										if (i == rows.length - 1) {
											ids += rows[i].id;
										} else {
											ids += rows[i].id + ",";
										}
									}
									//删除
									delEmp(ids);


								}
							},
							'-',
							{
								text : '刷新',
								iconCls : 'icon-arrow_refresh',
								handler : function() {
									queryEmp();
								}
							} ],
				        	   onLoadSuccess: function(data){
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
				        	   }
				});
		
			//点击tree树节点，加载对应节点下的成员列表
			$('#tDt').tree({
					onClick: function(node){
						//加载员工列表
						loadEmpList(node.id);
					}
			});

		
		//查询
		$("#query").click(function() {
			var meetingName = $('#meetingName').textbox('getValue');
			var meetingRoomName = $('#cc1').combobox('getText');//根据关键字模糊查询，不需要根据会议室编号
			var meetingStatusFlag = $('#cc2').combobox('getValue');
			$('#dg1').datagrid("load",{
				'meetingName':meetingName,
				'meetingRoomName':meetingRoomName,
				'meetingStatusFlag':meetingStatusFlag
				});
		});

		//重置
		$("#reset").click(function() {
			$('#meetingName').textbox('setValue', '');
			$('#cc1').combobox('setText', '');//根据关键字模糊查询，不需要根据会议室编号
			$('#cc2').combobox('setValue', '');
			$('#meetingName').textbox('setText', '');
			$('#cc2').combobox('setText', '');
			$('#dg1').datagrid("load", {});
		});
		
		//科室下拉框
		$("#cc").combobox({
			url : "${pageContext.request.contextPath}/meeting/emGroup/loadDept.action",   
		    valueField:'id',    
		    textField:'text'
		});
		
		

	});

	//查看
	function look(id) {
		var bodyWidth =  $("body").width()
		var bodyHeight = $("body").height()
		var height = bodyHeight*1.2;
		var width = bodyWidth*0.5;
		var top = Math.round((window.screen.height - height) / 2)-45;
		var left = Math.round((window.screen.width - width) / 2);
		window.open("${pageContext.request.contextPath}/meeting/meetingSigned/toMeetSignedInfoUI.action?id="+id,"_blank",
						"height="
								+ height
								+ ", width="
								+ width
								+ ", top="
								+ top
								+ ", left= "
								+ left
								+ ",status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no");
	}

	//删除
	function del(id) {
		$.messager.confirm('确认','您确认想要删除该记录吗？',function(r){    
		    if (r){  
				$.ajax({
							url : "${pageContext.request.contextPath}/meeting/meetingSigned/delMeetingSigned.action",
							data : "id=" + id,
							type : "post",
							success : function(backData) {
								if ("true" == backData) {
									$.messager.alert('提示', '删除成功！');
									var meetingName = $('#meetingName').textbox('getValue');
									var meetingRoomName = $('#cc1').combobox('getText');//根据关键字模糊查询，不需要根据会议室编号
									var meetingStatusFlag = $('#cc2').combobox('getValue');
									$('#dg1').datagrid({
										queryParams : {
											meetingName : meetingName,
											meetingRoomName : meetingRoomName,
											meetingStatusFlag : meetingStatusFlag
										}
									});
								} else {
									$.messager.alert('提示', '删除失败！');
								}
							}
		
						});
		    }
		});
	}

	//修改
	function update(id) {

		//跳转到修改页面
		var bodyWidth = $("body").width()
		var bodyHeight = $("body").height()
		var height = bodyHeight * 1;
		var width = bodyWidth * 0.6;
		var top = Math.round((window.screen.height - height) / 2) - 45;
		var left = Math.round((window.screen.width - width) / 2);
		window.open("${pageContext.request.contextPath}/meeting/meetingApply/editMeetingApply.action?flag=YZBGS&id="+ id,
						"_blank",
						"height="
								+ height
								+ ", width="
								+ width
								+ ", top="
								+ top
								+ ", left= "
								+ left
								+ ",status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no");
	}

	//撤销
	function cancel(id) {
		$.messager.confirm('确认','您确认想要撤销该记录吗？',function(r){    
		    if (r){    
		    	//撤销，删除会议签到表中的记录
				$.ajax({
							url : "${pageContext.request.contextPath}/meeting/meetingApply/redoMeetingApply.action",
							data : "flag=YZBGS&id=" + id,
							type : "post",
							success : function(backData) {//重新加载当前页面，带条件
								var meetingName = $('#meetingName').textbox('getValue');
								var meetingRoomName = $('#cc1').combobox('getText');//根据关键字模糊查询，不需要根据会议室编号
								var meetingStatusFlag = $('#cc2').combobox('getValue');
								$('#dg1').datagrid({
									queryParams : {
										meetingName : meetingName,
										meetingRoomName : meetingRoomName,
										meetingStatusFlag : meetingStatusFlag
									}
								});
							}
						});    
		    }    
		});

	

	}

	//加载指定组的成员列表
	function loadEmpList(id) {
		var employee_name = $('#employee_name').textbox('getValue');
		var employee_jobon = $('#employee_jobon').textbox('getValue');
		var dept_name = $('#cc').combobox('getValue');
		//清除所选的行
        $('#list').datagrid("clearSelections");
		$('#list').datagrid("load", {
			'id' : id,
			'employee_name' : employee_name,
			'employee_jobon' : employee_jobon,
			'dept_name' : dept_name
		});
	}

	//根据条件查询指定节点id下的成员
	function queryEmp() {
		var node = $('#tDt').tree('getSelected');
		if (node != null && node.id != null && node.id != null) {
			loadEmpList(node.id);
		}
	}

	//高级查询：

	//重置
	function resetEmp() {
		$('#employee_name').textbox('setValue', '');
		$('#employee_jobon').textbox('setValue', '');
		$('#cc').combobox('setValue', '');
		queryEmp();
	}

	//删除emp
	function delEmp(id) {
		$.messager.confirm('确认','您确认想要删除该成员吗？',function(r){    
		    if (r){    
		    	$.ajax({
					url : "${pageContext.request.contextPath}/meeting/emGroup/delEmployee.action",
					data : "id=" + id,
					type : "post",
					success : function(backData) {
						if ("true" == backData) {
							queryEmp();
                            $('#cc').combobox('reload');
						} else {
							$.messager.alert('提示', '删除失败！');
						}
					}

				});
		    }    
		});
		
	}
</script>	
</html>
