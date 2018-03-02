<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>挂号排班</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<%@ include file="/common/metas.jsp" %>
	<style type="text/css">
		.panel-header{
			border-top:0;
		}
		.tabs-tool{
			border-right:0
		}
	</style>
</head>
<body style="margin: 0px;padding: 0px;">
	<div class="easyui-layout" data-options="fit:true">
			<div id="p" data-options="region:'west',split:true,border:true,title:'科室信息',tools:'#toolSMId'" style="width:18%;overflow: hidden;">
				<div id="el" class="easyui-layout" data-options="fit:true"> 
					<div data-options="region:'north',split:false,border:false" style="height:30px;">
						<input id="deptSearch" data-options="prompt:'科室名称'" class="easyui-combobox"/>
						<a href="javascript:clearComboboxValue('deptSearch');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
					</div>
					<div data-options="region:'center',border:false">
						<ul id="tDt">数据加载中...</ul>
						<div id="toolSMId">
							<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
							<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
							<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
						</div>
					</div> 
				</div>
			</div>
			<div data-options="region:'center',border:false" style="width: 82%;height: 100%;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
			        <div data-options="region:'north',split:false" style="width:100%;height:45px;border-top:0">	        
						<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;height: 100%;">
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
					<div class="easyui-droppable targetarea" data-options="region:'center',split:false,border:true" style="width:100%;border: 0px">
						<div id="tt" class="easyui-tabs" data-options="tabHeight:40,fit:true"style="border: 0px">
							<input type="hidden" id="deptId"/>
							<input type="hidden" id="deptName"/>
							<input type="hidden" id="dateTime"/>
							<c:forEach var="list" items="${dayXxList }" varStatus="status">
								<div title="${list.name }" data-options="fit:true">
									<c:if test="${status.index==0}">
										<input type="hidden" id="dateTimeInit" value="${dayXxList[0].id }"/>
									</c:if>
									<table id="${list.id }" style="width: 100%;border: 0px" data-options="toolbar:'#toolbarId'"></table>
								</div>
							</c:forEach>	
						</div>				
					</div>
				</div>
		</div> 
	</div>
	<div id="toolbarId">
		<a id="btnAdd" onclick="btnAdd()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a> 
		<a id="btnSave" onclick="btnSave()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		<a id="btnDelete" onclick="btnDelete()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		<a id="btnReload" onclick="btnReload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		<a id="btnMb" onclick="btnMb()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">模板</a>
		<span style='color:red;font-size:small;'>底色为红色代表该医生已经停诊</span>
	</div>	
	<script type="text/javascript">
		var midDayMap='';
		var tjMap=new Map();
		var empMap = null;
		var deptMap = null;
		var cliMap = null;
		var dateTime = "";
		//加载页面
		$(function(){
			$.ajax({
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=midday',
				type:'post',
				success: function(gradData) {
					midDayMap = gradData;
				}
			});	
			$.ajax({
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
				data:{"type":"stopReason"},
				type:'post',
				success: function(data) {
					for(var i=0;i<data.length;i++){
						tjMap.put(data[i].encode,data[i].name);
					}
				}
			});	
			$('#dateTime').val($('#dateTimeInit').val());
			dateTime = $('#dateTimeInit').val();
			$('#tt').tabs({
			  onSelect: function(title){
			  	var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
				if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
					var t = eastpanel.panel('options').title;
			  		if(t=="查看模板信息"){
			  			$('#divLayout').layout('remove','east');
			  		}
				};
				var dateTimeAxq = title.split("<br>");
				var dateTimeArr = dateTimeAxq[0].split(/['年''月''日']/);
				dateTime = dateTimeArr[0]+'-'+dateTimeArr[1]+'-'+dateTimeArr[2];
				$('#dateTime').val(dateTime);
				loadDatagrid(getSelected(),dateTime);
			  },
			  tools:[{
			  	id:'btnTb',
				iconCls:'icon-add',
				text: '同步',
				handler:function(){
						var deptId = $('#deptId').val();
                    	if(deptId==null||deptId==""){
                    		$.messager.alert("提示","请选择具体科室！");
                    		return;
                    	}
                    	$.messager.progress({text:'正在同步，请稍后...',modal:true});
                        $.ajax({
							url: "<c:url value='/outpatient/schedule/schedulSynch.action'/>?deptId="+deptId,
							type:'post',
							success: function(data) {
								$.messager.progress('close');
								var dataMap = data;
								if(dataMap.resMsg=="error"){
									$.messager.alert("提示",'同步失败,请联系管理员!');
								}else if(dataMap.resMsg=="notsynch"){
									$.messager.alert("提示",'信息全部存在无需同步!');
								}else if(dataMap.resMsg=="success"){
									$.messager.alert("提示",'同步成功,共计: '+dataMap.resCode+' 条记录!');
								}
								var dateTime = $('#dateTime').val();
								$('#'+dateTime).datagrid('reload');
							}
						});
					}
				}]
			});
			$('#btnTb').linkbutton('disable');
			loadDatagrid(getSelected(),dateTime);
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
				url:"<%=basePath%>register/newInfo/deptCombobox.action",
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
			});
		});
		var colorRow = 0;
		//加载可编辑表格
		function loadDatagrid(deptId,dateTime){
			$('#'+dateTime).datagrid({ 
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
		   		url: "<c:url value='/outpatient/schedule/querySchedule.action'/>?menuAlias=${menuAlias}&time="+new Date(),
			    queryParams:{deptId:deptId,dateTime:dateTime,search:$('#sName').val()},
			    onDblClickRow: function (rowIndex, rowData) {//双击查看
					if(getIdUtil('#'+dateTime).length!=0){
				   	    AddOrShowEast("查看","<c:url value='/outpatient/schedule/viewSchedule.action'/>?id="+getIdUtil('#'+dateTime),"580");
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
					for(var i = 0; i<data.total; i++){
						if(data.rows[i].stoprEason!=null){
							 $("[datagrid-row-index='" + i + "']").css({ "background-color": "red" });
						}						
					}
				},
			    columns:[[    
			        {field:'ck',checkbox:true},    
			        {field:'id',hidden:true},    
			        {field:'scheduleClass',title:'排班分类',width:'100',formatter:
			        	function(value,row,index){
				        	if(value==1){
				        		return '挂号排班';
							}else if(value==2){
								return '工作排班';
							}else{
								return value;
							}
		        		}
			        },
			        {field:'doctorId',title:'员工',width:'100',formatter:
				        	function(value,row,index){
				        	if (row.doctor){
								return empMap[row.doctor];
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
			        {field:'clinicId',title:'工作诊室',width:'150',formatter:
			        	function(value,row,index){
				        	if (row.clinic&&row.clinic!=null){
				        		return cliMap[row.clinic];
							} else {
								return value;
							}
		        		}
			        },    
			        {field:'midday',title:'午别',width:'50',formatter: 
			        	function(value,row,index){
				        	for(var i=0;i<midDayMap.length;i++){
			        			if(value==midDayMap[i].encode){
					        		return midDayMap[i].name;
					        	}
			        		}
			        	}
			        },    
			        {field:'startTime',title:'开始时间',width:'85'},
			        {field:'endTime',title:'结束时间',width:'85'},
			        {field:'limit',title:'挂号限额',width:'95'},   
			        {field:'newpeople',title:'已挂人数',width:'95',formatter:
			        	function(value){
							return value!=null ? value:0;
				        }	
			        }, 		        
			        {field:'preLimit',title:'预约限额',width:'95'},
			        {field:'wilpeople',title:'预约已挂',width:'95'},
			        {field:'phoneLimit',title:'电话限额',width:'95'},    
			        {field:'netLimit',title:'网络限额',width:'95'},
			        {field:'speciallimit',title:'特诊限额',width:'95'},
			        {field:'stoprEason',title:'停诊原因',width:'100',formatter:
			        	function(value,row,index){
				        	return tjMap.get(value);
			            }}
			    ]]
			});
			if(getSelected()==null||getSelected()==""){
				//禁用按钮
				$('#btnAdd').linkbutton('disable');
				$('#btnSave').linkbutton('disable');
				$('#btnDelete').linkbutton('disable');
				$('#btnReload').linkbutton('disable');
				$('#btnMb').linkbutton('disable');
			}else{
				//启用按钮
				$('#btnAdd').linkbutton('enable');
				$('#btnSave').linkbutton('enable');
				$('#btnDelete').linkbutton('enable');
				$('#btnReload').linkbutton('enable');
				$('#btnMb').linkbutton('enable');
			}
			$('#dateTime').val(dateTime);
			$('#'+dateTime).datagrid('reload');
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
   			var deptId = $('#deptId').val();
        	if(deptId==null||deptId==""){
        		$.messager.alert("提示","请选择具体科室！");
        		return;
        	}
        	var datagridId = $('#inpWeekEnId').val();
        	var dateTime = $('#dateTime').val();
		   	$('#'+dateTime).datagrid('load', {
		   		deptId:deptId,
		   		dateTime:dateTime,
				search: $('#sName').val()
			});
		}
		
		//加载部门树
	   	$('#tDt').tree({  
	   		//全部科室的action"<c:url value='/outpatient/scheduleModel/treeDeptSchedule.action'/>",
 	   		url:"<c:url value='/outpatient/webPreregister/getDeptTree.action'/>",
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
						$('#deptId').val(node.id);
						$("#deptName").val(node.text);
					}else{
						$('#deptId').val('');
					}
					loadDatagrid(getSelected(),$('#dateTime').val());
				}
			},
		    onLoadSuccess:function(node,data){
		    	/* if(data.length>0){//节点收缩
					$('#tDt').tree('collapseAll');
				    $('#tDt').tree('expand',$('#tDt').tree('find', 1).target);
				} */
		    }
		}); 
	  //清除科室信息查询下拉框，查询条件的值后,动态加载页面
	   	function clearComboboxValue(id){
	   		delSelectedData(id);
	   		delSelectedData('sName');
	   		refresh();
	   		$('#deptId').val("");
	   		var deptId = $('#deptId').val();
        	var datagridId = $('#inpWeekEnId').val();
        	var dateTime = $('#dateTime').val();
		   	$('#'+dateTime).datagrid('load', {
		   		deptId:deptId,
		   		dateTime:dateTime,
				search: $('#sName').val()
			});
	   	}
	   	function clearComboboxValue1(id){
	   		delSelectedData(id);
	   		var deptId = $('#deptId').val();
        	var datagridId = $('#inpWeekEnId').val();
        	var dateTime = $('#dateTime').val();
		   	$('#'+dateTime).datagrid('load', {
		   		deptId:deptId,
		   		dateTime:dateTime,
				search: $('#sName').val()
			});
	   	}
	   	//科室部门树操作
	   	function refresh(){//刷新树
	   		$('#tDt').tree('options').url ="<c:url value='/outpatient/webPreregister/getDeptTree.action'/>"; //"<c:url value='/outpatient/scheduleModel/treeDeptSchedule.action'/>";
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
					$('#btnTb').linkbutton('enable');
					return node.id;
				}else{
					$('#btnTb').linkbutton('disable');
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
						loadDatagrid(getSelected(),$('#dateTime').val());
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
					$('#deptId').val(node.id);
					$('#deptName').val(node.text);
					
				}else{
					$('#deptId').val('');
				}
				
				$('#divLayout').layout('remove','east');
				if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
					$('#deptId').val(node.id);
					$("#deptName").val(node.text);
				}else{
					$('#deptId').val('');
				}
				loadDatagrid(getSelected(),$('#dateTime').val());
			}
		}
		function btnAdd(){
			var deptId = $('#deptId').val();
        	if(deptId==null||deptId==""){
        		$.messager.alert("提示","请选择具体科室！");
        		return;
        	}
        	AddOrShowEast("添加排班信息","<c:url value='/outpatient/schedule/scheduleAdd.action'/>?deptId="+deptId+"&dateTime="+$('#dateTime').val()+"&deptName="+encodeURI(encodeURI($('#deptName').val())),"580");
		}
		function btnSave(){
			var deptId = $('#deptId').val();
        	if(deptId==null||deptId==""){
        		$.messager.alert("提示","请选择具体科室！");
        		return;
        	}
            var row = $('#'+dateTime).datagrid('getSelected'); //获取当前选中行             
            if(row!=null&&row.id!=null&&row.id!=''){
            	AddOrShowEast("修改排班信息","<c:url value='/outpatient/schedule/scheduleEdit.action'/>?id="+row.id,"580");
			}else{
				$.messager.alert("提示","请选择需要修改的信息！");
			}
		}
		function btnDelete(){
			var rows = $('#'+dateTime).datagrid('getChecked');
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
							url: "<c:url value='/outpatient/schedule/delSchedul.action'/>?id="+ids,
							type:'post',
							success: function(data) {
								if(data=="clincSumExp"){
									$.messager.progress('close');
									$.messager.alert("提示",'该医生已被挂号,暂不可删除!');
									return;
								}
								$.messager.progress('close');
								$.messager.alert("提示",'删除成功');
								$('#'+dateTime).datagrid('reload');
							}
						});
					}
            	});
        	}else{
				$.messager.alert("提示","请选择要删除的信息！");
        	}
		}
		function btnReload(){
			$('#'+dateTime).datagrid('reload');
		}
		function btnMb(){
			var deptId = $('#deptId').val();
        	if(deptId==null||deptId==""){
        		$.messager.alert("提示","请选择具体科室！");
        		return;
        	}
        	AddOrShowEast("查看模板信息","<c:url value='/outpatient/schedule/scheduleTemp.action'/>?deptId="+deptId+"&dateTime="+$('#dateTime').val(),"780");
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	
	
	</body>
</html>