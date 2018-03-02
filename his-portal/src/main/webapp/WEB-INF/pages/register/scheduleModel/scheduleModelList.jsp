<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN>
<html>
<head>
	<title>挂号排班模板</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<%@ include file="/common/metas.jsp" %>
	<style type="text/css">
		.panel-header{
			border-top:0
		}
	</style>
</head>
<body style="margin: 0px;padding: 0px;">
	<div class="easyui-layout" data-options="fit:true">
		<div id="p" data-options="region:'west',split:true,border:false,title:'科室信息',tools:'#toolSMId',border:true" style="width:18%;overflow: hidden;">
		<div id="el" class="easyui-layout" data-options="fit:true"> 
		<div data-options="region:'north',split:false,border:false" style="height:30px;">
			<input id="deptSearch" data-options="prompt:'科室名称'" class="easyui-combobox" />
			<a href="javascript:clearComboboxValue('deptSearch');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> 
		</div>
				<div id="treeDiv"  data-options="region:'center',border:false">
					<ul id="tDt" >数据加载中...</ul> 
					<div id="toolSMId">
						<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
						<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
						<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
					</div> 
				</div>
				
			</div>
		</div>
		<div data-options="region:'center',border:false" style="width: 85%;height: 100%;border:0px" >
			<div id="divLayout" class="easyui-layout" data-options="fit:true">
		        <div data-options="region:'north',split:false,border:true" style="width:100%;height:45px;border-top:0">	        
					<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;height:100%">
						<tr>
							<td style="padding: 5px 15px;">查询条件：
								<input id="sName" class="easyui-textbox" data-options="prompt:'医生名称,诊室名称'"/>
								<a href="javascript:clearComboboxValue1('sName');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								<shiro:hasPermission name="${menuAlias}:function:query">
								&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" data-options="iconCls:'icon-search'">查询</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</div>			
				<div data-options="region:'center',split:false,border:true" style="width:100%;border:0px" >
					<div id="tt" class="easyui-tabs" data-options="fit:true" style="border:0px">
						<input type="hidden" id="inpDeptId"/>	
						<input type="hidden" id="inpDeptName"/>	
						<input type="hidden" id="inpWeekId" value="1"/>	
						<input type="hidden" id="inpWeekEnId" value="Monday"/>	
						<div title="星期一">
							<table id="Monday" style="width: 100%;"></table> 
						</div>
						<div title="星期二">
							<table id="Tuesday" style="width: 100%;"></table> 
						</div>
						<div title="星期三">
							<table id="Wednesday" style="width: 100%;"></table> 
						</div>
						<div title="星期四">
							<table id="Thursday" style="width: 100%;"></table> 
						</div>
						<div title="星期五">
							<table id="Friday" style="width: 100%;"></table>
						</div>
						<div title="星期六">
							<table id="Saturday" style="width: 100%;"></table> 
						</div>
						<div title="星期日">
							<table id="Sunday" style="width: 100%;"></table> 
						</div>
					</div>				
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var middayMap ='';
		var empMap = null;
		var deptMap = null;
		var cliMap = null;
		//加载页面
		$(function(){
			$.ajax({
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=midday',
				type:'post',
				success: function(gradData) {
					middayMap=gradData;
				}
			});	
			
			var xq = "Monday";
			var week ="";
			$('#tt').tabs({
			  onSelect: function(title){
				  var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
					if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
						var t = eastpanel.panel('options').title;
				  		if(t=="查看模板信息"){
				  			$('#divLayout').layout('remove','east');
				  		}
					};
			  	$('#infoJson').val("");
			  	$('#name').val("");
				if(title=="星期一"){
					xq = "Monday";
					week = 1;
					$('#inpWeekId').val(1);
				}else if(title=="星期二"){
					xq = "Tuesday";
					week = 2;
					$('#inpWeekId').val(2);
				}else if(title=="星期三"){
					xq = "Wednesday";
					week = 3;
					$('#inpWeekId').val(3);
				}else if(title=="星期四"){
					xq = "Thursday";
					week = 4;
					$('#inpWeekId').val(4);
				}else if(title=="星期五"){
					xq = "Friday";
					week = 5;
					$('#inpWeekId').val(5);
				}else if(title=="星期六"){
					xq = "Saturday";
					week = 6;
					$('#inpWeekId').val(6);
				}else if(title=="星期日"){
					xq = "Sunday";
					week = 7;
					$('#inpWeekId').val(7);
				}
				$('#inpWeekEnId').val(xq);
				loadDatagrid(getSelected(),xq,week);
			  }
			});
			loadDatagrid(getSelected(),xq,week);
			bindEnterEvent('sName',searchFrom,'easyui');
			$.ajax({
				url: "<c:url value='/outpatient/scheduleModel/queryEmpCodeAndNameMap.action'/>",
				type:'post',
				success: function(empData) {
					empMap = empData;
				}
			});	
			$.ajax({
				url: "<c:url value='/outpatient/scheduleModel/querydeptCodeAndNameMap.action'/>",
				type:'post',
				success: function(deptData) {
					deptMap = deptData;
				}
			});	
			$.ajax({
				url: "<c:url value='/baseinfo/clinic/queryClinicCodeAndNameMap.action'/>",
				type:'post',
				success: function(cliData) {
					cliMap = cliData;
				}
			});	
			var havSelect = true;
			$('#deptSearch').combobox({
				url: "<%=basePath%>register/newInfo/deptCombobox.action",
				valueField : 'deptCode',
				textField : 'deptName',
				filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'deptCode';
					keys[keys.length] = 'deptName';
					keys[keys.length] = 'deptPinyin';
					keys[keys.length] = 'deptWb';
					keys[keys.length] = 'deptInputcode';
					if(filterLocalCombobox(q, row, keys)){
						row.selected=true;
					}else{
						row.selected=false;
					}
					return filterLocalCombobox(q, row, keys);
			    },
				onLoadSuccess:function(data){
					if(data!=null && data.length==1){
						var code= data[0].deptCode;
						$('#deptSearch').combobox('select',code);
						selectNode(code);
					}
				},
				onSelect:function(rec){
					var code=rec.deptCode;
					havSelect = false;
				    selectNode(code);
					
				},
				onHidePanel:function(){
					var data = $(this).combobox('getData');
				    var val = $(this).combobox('getValue');
				    var result = true;
				    for (var i = 0; i < data.length; i++) {
				        if (val == data[i].deptCode) {
				            result = false;
				        }
				    }
				    if (result) {
				        $(this).combobox("clear");
				    }else{
				        $(this).combobox('unselect',val);
				        $(this).combobox('select',val);
				    }
					if(havSelect){
						var isOnly = 0;
						var onlyOne = null;
						for(var i = 0;i<$("#deptSearch").combobox("getData").length;i++){
							if($("#deptSearch").combobox("getData")[i].selected){
								isOnly++;
								onlyOne = $("#deptSearch").combobox("getData")[i];
							}
						}
						if((isOnly-1)==0){
							var depCode = onlyOne.deptCode;
							$('#deptSearch').combobox('setValue',deptMap[depCode]);
							$('#deptSearch').combobox('select',depCode);
							selectNode(depCode);
						}
					}
					havSelect=true;							
				},
			})
		});
		
		//加载可编辑表格
		function loadDatagrid(deptId,xq,week){
			$('#'+xq).datagrid({
				striped:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				fitColumns:false,
				pagination:true,
				rownumbers:true,
				fit:true,
				border:false,
				pageSize:20,
		   		pageList:[20,30,50,100],
			    url: "<c:url value='/outpatient/scheduleModel/querySchedulemodel.action'/>?menuAlias=${menuAlias}&time="+new Date(),
			    queryParams:{deptId:deptId,week:week,search: $('#sName').val()},
			    onDblClickRow: function (rowIndex, rowData) {//双击查看
					if(getIdUtil('#'+xq).length!=0){
				   	    AddOrShowEast("查看排版模版","<c:url value='/outpatient/scheduleModel/viewSchedulemodel.action'/>?id="+getIdUtil('#'+xq),"580");
				   	}
				},  
				onLoadSuccess : function(data){
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
				},
			    columns:[[    
			        {field:'ck',checkbox:true}, 
			        {field:'modelClass',title:'模板类型',width:'100',formatter:
			        	function(value,row,index){
				        	if(value==1){
				        		return '挂号排班模板';
							}else if(value==2){
								return '工作排班模板';
							}else{
								return value;
							}
		        		}
			        },
			        {field:'doctorId',title:'员工',width:'100',formatter:
			        	function(value,row,index){
				        	if (row.modelDoctor&&row.modelDoctor!=null){
				        		return empMap[row.modelDoctor];
							} else {
								return value;
							}
		        		}
			        }, 
			        {field:'department',title:'所在科室',width:'180',formatter:
			        	function(value,row,index){
				        	if (row.department&&row.department!=null){
				        		return deptMap[row.department];
							} else {
								return value;
							}
		        		}
			        },    
			        {field:'clinicId',title:'工作诊室',width:'180',formatter:
			        	function(value,row,index){
				        	if (row.clinic&&row.clinic!=null){
				        		return cliMap[row.clinic];
							} else {
								return value;
							}
		        		}
			        },    
			        {field:'modelMidday',title:'午别',width:'50',formatter: 
			        	function(value,row,index){
			        	for(var i=0;i<middayMap.length;i++){
		        			if(value==middayMap[i].encode){
				        		return middayMap[i].name;
				        	}
		        		}
		        	}
			        },    
			        {field:'modelStartTime',title:'开始时间',width:'85'},    
			        {field:'modelEndTime',title:'结束时间',width:'85'},    
			        {field:'modelLimit',title:'挂号限额',width:'100'},    
			        {field:'modelPrelimit',title:'预约限额',width:'100'},    
			        {field:'modelPhonelimit',title:'电话限额',width:'100'},    
			        {field:'modelNetlimit',title:'网络限额',width:'100'},
			        {field:'modelSpeciallimit',title:'特诊限额',width:'100'}
			    ]],  
				toolbar: [{
					id: 'btnAdd'+xq,
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                    	var inpDeptId = $('#inpDeptId').val();
                    	if(inpDeptId==null||inpDeptId==""){
                    		$.messager.alert("提示","请选择具体科室！");
                    		return;
                    	}
                    	var inpDeptName = $('#inpDeptName').val();
                    	AddOrShowEast("添加排班模板","<c:url value='/outpatient/scheduleModel/schedulemodelAdd.action'/>?deptId="+inpDeptId+"&week="+$('#inpWeekId').val()+"&deptName="+encodeURI(encodeURI($('#inpDeptName').val())),"580");
                    }
                },{
                    id: 'btnEdit'+xq,
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                    	var deptId = $('#inpDeptId').val();
                    	if(deptId==null||deptId==""){
                    		$.messager.alert("提示","请选择具体科室！");
                    		return;
                    	}
                    	var row = $('#'+xq).datagrid('getSelected'); //获取当前选中行             
	                    if(row!=null&&row.id!=null&&row.id!=''){
	                    	AddOrShowEast("修改排班模板","<c:url value='/outpatient/scheduleModel/schedulemodelEdit.action'/>?id="+row.id,"580");
	   					}else{
	   						$.messager.alert("提示","请选择需要修改的信息！");
	   					}
                    }
                },{
                    id: 'btnDelete'+xq,
                    text: '删除',
                    iconCls: 'icon-remove',
                    handler: function () {
                    	var rows = $('#'+xq).datagrid('getChecked');
                    	if (rows.length > 0) {//选中几行的话触发事件	                        
						 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
								if (res){
									$.messager.progress({text:'删除中，请稍后...',modal:true});
									var ids = '';
									for(var i=0; i<rows.length; i++){
										if(ids!=''){
											ids += ',';
										}
										ids += rows[i].id;
									};
									$.ajax({
										url: "<c:url value='/outpatient/scheduleModel/delSchedulemodel.action'/>?id="+ids,
										type:'post',
										success: function() {
											$.messager.progress('close');
											$.messager.alert("提示",'删除成功');
											$('#'+xq).datagrid('reload');
										}
									});
								}
                        	});
                    	}else{
							$.messager.alert("提示","请选择要删除的信息！");
                    	}
                    }
                },{
                    id: 'btnReload'+xq,
                    text: '刷新',
                    iconCls: 'icon-reload',
                    handler: function () {
                    	$('#'+xq).datagrid('reload');
                    }
                },{
                	id: 'btnMb'+xq,
                    text: '模板',
                    iconCls: 'icon-add',
                    handler: function () {
                    	var inpDeptId = $('#inpDeptId').val();
                    	if(inpDeptId==null||inpDeptId==""){
                    		$.messager.alert("提示","请选择具体科室！");
                    		return;
                    	}
                    	var pp = $('#tt').tabs('getSelected'); 
            			var tabTitle = pp.panel('options').title;
            			var weeken =0;
            			if(tabTitle=="星期一"){
        					weeken = 1;
        				}else if(tabTitle=="星期二"){
        					weeken = 2;
        				}else if(tabTitle=="星期三"){
        					weeken = 3;
        				}else if(tabTitle=="星期四"){
        					weeken = 4;
        				}else if(tabTitle=="星期五"){
        					weeken = 5;
        				}else if(tabTitle=="星期六"){
        					weeken = 6;
        				}else if(tabTitle=="星期日"){
        					weeken = 7;
        				}
                    	AddOrShowEast("查看模板信息","<%=basePath%>outpatient/scheduleModel/scheduleModelTemp.action?deptId="+deptId+"&weeken="+weeken,"780");
                    }	
                }]
			});
			if(getSelected()==null||getSelected()==""){
				//禁用按钮
				$('#btnAdd'+xq).linkbutton('disable');
				$('#btnEdit'+xq).linkbutton('disable');
				$('#btnDelete'+xq).linkbutton('disable');
				$('#btnReload'+xq).linkbutton('disable');
				$('#btnMb'+xq).linkbutton('disable');
			}else{
				//启用按钮
				$('#btnAdd').linkbutton('enable');
				$('#btnEdit'+xq).linkbutton('enable');
				$('#btnDelete'+xq).linkbutton('enable');
				$('#btnDelete'+xq).linkbutton('enable');
				$('#btnMb'+xq).linkbutton('enable');
			}
			$('#inpWeekId').val(week);
			$('#modelWeek').val(xq);
			$('#'+xq).datagrid('reload');
		}
   		
		/**
		 * 动态添加标签页
		 * @author  sunshuo
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2015-05-21
		 * @version 1.0
		 */
		 function AddOrShowEast(title,url,width) {
				var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
				if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
					$('#divLayout').layout('remove','east');
					$('#divLayout').layout('add', {
						title : title,
						region : 'east',
						width : width,
						href : url,
						closable : false,
						collapsible:false
					});
				} else {//打开新面板
					$('#divLayout').layout('add', {
						title : title,
						region : 'east',
						width : width,
						href : url,
						closable : false,
						collapsible:false
					});
				}
			}
		//查询
   		function searchFrom(){
   			var deptId = $('#inpDeptId').val();
        	if(deptId==null||deptId==""){
        		$.messager.alert("提示","请选择具体科室！");
        		return;
        	}
        	var datagridId = $('#inpWeekEnId').val();
        	var w = $('#inpWeekEnId').val();
		   	$('#'+w).datagrid('load', {
		   		deptId:deptId,
		   		week:$('#inpWeekId').val(),
				search: $('#sName').val()
			});
		}	
   		
		//加载部门树
	   	$('#tDt').tree({    
		    url:"<c:url value='/outpatient/webPreregister/getDeptTree.action'/>",//"<c:url value='/outpatient/scheduleModel/treeDeptSchedule.action'/>",
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children&&node.children.length>0){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
			},
			onClick: function(node){//点击节点
				if(node.children.length>0){
			   		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				}else{
					$('#divLayout').layout('remove','east');
					if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
						$('#inpDeptId').val(node.id);
						$('#inpDeptName').val(node.text);
					}else{
						$('#inpDeptId').val('');
					}
					$('#inpWeekId').val(1);
					$('#inpWeekEnId').val("Monday");
					$('#tt').tabs('select',"星期一");
					loadDatagrid(getSelected(),"Monday",1);
				}
			}
		}); 
//清除科室信息查询下拉框，查询条件的值后,动态加载页面
	   	function clearComboboxValue(id){
	   		delSelectedData(id);
	   		delSelectedData('sName');
	   		refresh();
	   		$('#inpDeptId').val("");
	   		var deptId = $('#inpDeptId').val();
        	var datagridId = $('#inpWeekEnId').val();
        	var w = $('#inpWeekEnId').val();
		   	$('#'+w).datagrid('load', {
		   		deptId:deptId,
		   		week:$('#inpWeekId').val(),
				search: $('#sName').val()
			});
	   	}
	   	function clearComboboxValue1(id){
	   		delSelectedData(id);
	   		var deptId = $('#inpDeptId').val();
        	var datagridId = $('#inpWeekEnId').val();
        	var w = $('#inpWeekEnId').val();
		   	$('#'+w).datagrid('load', {
		   		deptId:deptId,
		   		week:$('#inpWeekId').val(),
				search: $('#sName').val()
			});
	   	}
	   	//科室部门树操作
	   	function refresh(){//刷新树 
	   		$('#tDt').tree('options').url ="<c:url value='/outpatient/webPreregister/getDeptTree.action'/>";//"<c:url value='/outpatient/scheduleModel/treeDeptSchedule.action'/>"
			$('#tDt').tree('reload'); 
		}
	   	function expandAll(){//展开树
			$('#tDt').tree('expandAll');
		}
	   	function collapseAll(){//关闭树
	   		var root = $('#tDt').tree('find', 1);
	   		var ch = $('#tDt').tree('getChildren', root.target);
	   		ch.forEach(function(node){
				if(node.attributes.pid == '1'){
					$('#tDt').tree('collapse', node.target);
				}
	   		});
		}

		function getSelected(){//获得选中节点
			var node = $('#tDt').tree('getSelected');
			if (node){
				if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
					return node.id;
				}else{
					return "";
				}
			}else{
				return "";
			}
		}
		
		//查询树
		function searchTree(){
   			$.ajax({
				url:"<c:url value='/baseinfo/employee/searchTree.action'/>?searcht="+encodeURI(encodeURI($('#searchTree').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
				type:'post',
				success: function(data) {
					$('#tDt').tree('collapseAll');//单独展开一个节点,先收缩树
					var node = $('#tDt').tree('find',data);
					if(node!=null){
						$('#tDt').tree('expandTo', node.target).tree('select', node.target); //展开指定id的节点
						loadDatagrid(getSelected(),$('#inpWeekEnId').val(),$('#inpWeekId').val());
					}
				}
			});					
		}
		
		function selectNode(code){
			var node = $('#tDt').tree('find',code);
			if(node!=null){
				var pid= node.attributes.pid;
				var pnode= $('#tDt').tree('find',pid);
				$('#tDt').tree('expand', pnode.target).tree('scrollTo', node.target).tree('select',
						node.target);
				var pppid = pnode.attributes.pid;//虚拟科室节点
				var pppnode = $('#tDt').tree('find',pppid);//根节点
				$('#tDt').tree('expand', pppnode.target);
				$('#divLayout').layout('remove','east');
				if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
					$('#inpDeptId').val(node.id);
					$('#inpDeptName').val(node.text);
				}else{
					$('#inpDeptId').val('');
				}
				$('#inpWeekId').val(1);
				$('#inpWeekEnId').val("Monday");
				$('#tt').tabs('select',"星期一");
				loadDatagrid(getSelected(),"Monday",1);
			}
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>