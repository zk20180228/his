<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN>
<html>
<head>
	<title>医技排班模板</title>
	<%@ include file="/common/metas.jsp" %>
	<script type="text/javascript">
		var midDayMap=new Map();
		$.ajax({
		    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=midday",
			type:'post',
			success: function(data) {
				var type = data;
				for(var i=0;i<type.length;i++){
					midDayMap.put(type[i].encode,type[i].name);
				}
			}
		});
	</script>
</head>
<body style="margin: 0px;padding: 0px;">
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
		<div data-options="region:'north',split:false,border:false" style="height:45px;padding: 5px;width: 100%">
			<div style="padding:4px 5px 1px 5px;">
				<a id="seb" href="javascript:sebei()" class="easyui-linkbutton" data-options="iconCls:'icon-equipment'">设备</a>  
				<a id="xm" href="javascript:xiangmu()" class="easyui-linkbutton" data-options="iconCls:'icon-item'">项目</a>  
<!-- 				<a id="sc" href="javascript:del()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">删除</a>   -->
			</div>
		</div>   
		<div data-options="region:'center',border:false " style="height:100%;width: 100%;">
			<div class="easyui-layout" data-options="fit:true">
				<div id="p" data-options="region:'west',title:'科室信息'" style="width:18%;">
					<div id="toolSMId">
						<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
						<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
					</div>
					<div id="treeDiv"  style="width: 100%;">
						<ul id="tDt" >数据加载中...</ul>  
					</div>
				</div>
				<div data-options="region:'center',border:false" style="width:100%;height: 100%;" >
					<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<input type="hidden" id="nodeId" >
				        <div data-options="region:'north',split:false,border:true" style="width:100%;height:40px">	        
							<table cellspacing="0" cellpadding="0"  style="width: 100%;">
								<tr>
									<td style="padding: 5px 15px;">
										<input id="sName" class="easyui-textbox" data-options="prompt:'项目名称,设备名称'"/>
										&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" data-options="iconCls:'icon-search'">查询</a>
										<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</td>
								</tr>
							</table>
						</div>			
						<div data-options="region:'center',split:false,border:false" style="width:100%;" >
							<div id="tt" class="easyui-tabs" data-options="fit:true">
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
		</div>
	<script type="text/javascript">
		var deptMap=new Map();
		var clinicMap=new Map();
		//加载页面
		$(function(){
			//获取所有科室
			$.ajax({
				url:"<%=basePath%>/technical/medicalMineplate/getDept.action",
				type:'post',
				success: function(date) {
					for(var i=0;i<date.length;i++){
						deptMap.put(date[i].deptCode,date[i].deptName);
					}
					
				}
			});
			var xq = "Monday";
			var week ="";
			$('#tt').tabs({
			  onSelect: function(title){
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
			    url: "<c:url value='/technical/medicalMineplate/queryMedicalmodel.action'/>?menuAlias=${menuAlias}&time="+new Date(),
			    queryParams:{deptId:deptId,week:week,search: $('#sName').val()},
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
				},
				onBeforeLoad:function(param){
					$(this).datagrid('uncheckAll');
				},
			    onDblClickRow: function (rowIndex, rowData) {//双击查看
					if(getIdUtil('#'+xq).length!=0){
				   	    AddOrShowEast("查看排版模版","<c:url value='/technical/medicalMineplate/medicalMineplateEdit.action'/>?id="+getIdUtil('#'+xq),"580");
				   	}
				},   
			    columns:[[    
			        {field:'ck',checkbox:true}, 
			        {field:'modelClass',title:'模板类型',width:'100',formatter:
			        	function(value,row,index){
				        	if(value==1){
				        		return '项目排班模板';
							}else if(value==2){
								return '设备排班模板';
							}else{
								return value;
							}
		        		}
			        },
			        {field:'modelItemName',title:'项目名称',width:'100',
			        },    
			        {field:'modelDeptid',title:'科室',width:'150',formatter:
			        	function(value,row,index){
				        	if (value!=""&& value!=null){
				        		return deptMap.get(value);
							} else {
								return "";
							}
		        		}
			        },    
			       
			        {field:'modelMidday',title:'午别',width:'100',formatter: 
			        	function(value,row,index){
				        	if(value!=null&&value!=''){
				        		return midDayMap.get(value);
				        	}
			        	}
			        },    
			        {field:'modelPrelimit',title:'预约限额',width:'100'},    
			        {field:'modelNetlimit',title:'网络限额',width:'100'},
			        {field:'modelSpeciallimit',title:'特诊限额',width:'100'}
			    ]],  
				toolbar: [{
					id: 'btnAdd'+xq,
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                    	var inpId=$('#inpDeptId').val();//选择的项目;
                    	if(inpId==null||inpId==""){
                    		$.messager.alert("提示","请选择具体项目！");
                    		setTimeout(function(){
                    			$(".messager-body").window('close');
                    		},3500);
                    		return;
                    	}
                    	var nodeId = $('#nodeId').val();//判断选择是项目还是设备
                    	var inpDeptName = $('#inpDeptName').val();//项目名称
                    	AddOrShowEast("添加排班模板","<c:url value='/technical/medicalMineplate/medicalMineplateAdd.action'/>?nodeId="+nodeId+"&week="+$('#inpWeekId').val()+"&deptId="+inpId+"&deptName="+encodeURI(encodeURI($('#inpDeptName').val())),"580");
                    }
                },{
                    id: 'btnEdit'+xq,
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                    	var deptId = $('#inpDeptId').val();
                    	if(deptId==null||deptId==""){
                    		$.messager.alert("提示","请选择具体科室！");
                    		setTimeout(function(){
                    			$(".messager-body").window('close');
                    		},3500);
                    		return;
                    	}
                    	var row = $('#'+xq).datagrid('getSelected'); //获取当前选中行             
	                    if(row!=null&&row.id!=null&&row.id!=''){
	                    	AddOrShowEast("修改排班模板","<c:url value='/technical/medicalMineplate/medicalMineplateEdit.action'/>?id="+row.id,"580");
	   					}else{
	   						$.messager.alert("提示","请选择需要修改的信息！");
	   						setTimeout(function(){
	   							$(".messager-body").window('close');
	   						},3500);
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
										url: "<c:url value='/technical/medicalMineplate/medicalMineplatedel.action'/>?id="+ids,
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
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
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
                    		setTimeout(function(){
                    			$(".messager-body").window('close');
                    		},3500);
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
                    	AddOrShowEast("查看模板信息","<%=basePath%>/technical/medicalMineplate/medicalMineplatTemp.action?deptId="+deptId+"&weeken="+weeken,"780");
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
        		setTimeout(function(){
        			$(".messager-body").window('close');
        		},3500);
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
   		/**
		 * 重置
		 * @author huzhenguo
		 * @date 2017-03-21
		 * @version 1.0
		 */
		function clears(){
			$('#sName').textbox('setValue','');
			searchFrom();
		}
		//加载医技项目
	   	$('#tDt').tree({    
		    url:"<%=basePath%>/technical/medicalMine/medicalMinetree.action?lm="+"1",
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					if(node.children.length!=0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},
			onClick: function(node){//点击节点
				$('#divLayout').layout('remove','east');
				$('#inpDeptId').val(node.id);
				$('#inpDeptName').val(node.text);
				if(node.id=="1"||node.id=="2"){
					var msge = "";
					if(node.id=="1"){
						msge = "请选择项目！";
					}else{
						msge = "请选择设备";
					}
					$.messager.show({
						title:'提示',
						msg:msge
					});
					return;
				}
				var nodeId=node.attributes.pid;
				$("#nodeId").val(nodeId);
// 				if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
// 					$('#inpDeptId').val(node.id);
// 					$('#inpDeptName').val(node.text);
// 				}else{
// 					$('#inpDeptId').val('');
// 				}
				$('#inpWeekId').val(1);
				$('#inpWeekEnId').val("Monday");
				$('#tt').tabs('select',"星期一");
				loadDatagrid(getSelected(),"Monday",1);
			},
			onDblClick: function(node){
		   		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		    },
		    onBeforeCollapse:function(node){
				if(node.id=="1"){
					return false;
				}
		    }
		}); 
	   	

		function getSelected(){//获得选中节点
			var node = $('#tDt').tree('getSelected');
			if (node){
				return node.id;
// 				if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
// 					return node.id;
// 				}else{
// 					return "";
// 				}
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
		//查询设备
		function sebei(){
			$('#tDt').tree({    
			    url:"<%=basePath%>/technical/medicalMine/medicalMinetree.action?lm="+"2",
			    method:'get',
			    animate:true,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},
				onClick: function(node){//点击节点
					$('#divLayout').layout('remove','east');
					$('#inpDeptId').val(node.id);
					$('#inpDeptName').val(node.text);
					$('#inpWeekId').val(1);
					if(node.id=="1"||node.id=="2"){
						$.messager.show({
							title:'提示',
							msg:'请选择设备！'
						});
						return false;
					}
					var nodeId=node.attributes.pid;
					$("#nodeId").val(nodeId);//1是项目信息2设备信息
					
					$('#inpWeekEnId').val("Monday");
					$('#tt').tabs('select',"星期一");
					loadDatagrid(getSelected(),"Monday",1);
				},
				onDblClick: function(node){
			   		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			    },
			    onBeforeCollapse:function(node){
					if(node.id=="2"){
						return false;
					}
			    }
			}); 
		}
		//查询项目
		function xiangmu(){
			$('#tDt').tree({    
			    url:"<%=basePath%>/technical/medicalMine/medicalMinetree.action?lm="+"1",
			    method:'get',
			    animate:true,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},
				onClick: function(node){//点击节点
					$('#divLayout').layout('remove','east');
					$('#inpDeptId').val(node.id);
					$('#inpDeptName').val(node.text);
					$('#inpWeekId').val(1);
					if(node.id=="1"||node.id=="2"){
						$.messager.show({
							title:'提示',
							msg:'请选择项目！'
						});
						return;
					}
					var nodeId = node.attributes.pid;
					$("#nodeId").val(nodeId);//1是项目信息2设备信息
					$('#inpWeekEnId').val("Monday");
					$('#tt').tabs('select',"星期一");
					loadDatagrid(getSelected(),"Monday",1);
				},
				onDblClick: function(node){
			   		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			    },
			    onBeforeCollapse:function(node){
					if(node.id=="1"){
						return false;
					}
			    }
			}); 
		}
	</script>
	</body>
</html>