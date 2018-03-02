<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>医技排班</title>
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
<style type="text/css">
	.datagrid-wrap{
		border-top:0
	}
</style>
</head>
<body style="margin: 0px;padding: 0px;">
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
		<div data-options="region:'north',split:false,border:false" style="height:40px;padding: 5px;width: 100%">
			<a id="seb" href="javascript:sebei()" class="easyui-linkbutton" data-options="iconCls:'icon-equipment'">设备</a>&nbsp;  
			<a id="xm" href="javascript:xiangmu()" class="easyui-linkbutton" data-options="iconCls:'icon-item'">项目</a>&nbsp;  
<!-- 			删除功能不可用故注销 -->
<!-- 			<a id="sc" href="javascript:del()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">删除</a>&nbsp;   -->
		</div>   
		<div data-options="region:'center',border:false" style="padding;height:95%;width: 100%;">
			<div class="easyui-layout" data-options="fit:true">
					<div id="p" data-options="region:'west',tools:'#toolSMId',border:true" style="width:18%;">
						<div id="toolSMId">
							<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
							<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
							<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
						</div>
<!-- 						<input class="easyui-textbox" data-options="prompt:'编码，名称，拼音码'" id="qr"> -->
						<ul id="tDt">数据加载中...</ul> 
					</div>
					<div data-options="region:'center',border:false" style="width: 82%;height: 100%;">
						<div id="divLayout" class="easyui-layout" data-options="fit:true">
						<input type="hidden" id="nodeId"><input id="inpDeptName" type="hidden">
					        <div data-options="region:'north',split:false" style="width:100%;height:40px">	        
								<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;">
									<tr>
										<td style="padding: 5px 5px;">
											<input id="sName" class="easyui-textbox" data-options="prompt:'项目名称,设备名称'"/>
											&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" data-options="iconCls:'icon-search'">查询</a>
												<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
										</td>
									</tr>
								</table>
							</div>			
							<div class="easyui-droppable targetarea" data-options="region:'center',split:false,border:false" style="width:100%;">
								<div id="tt" class="easyui-tabs" data-options="tabHeight:40,fit:true,border:false">
									<input type="hidden" id="deptId"/>
									<input type="hidden" id="deptName"/>
									<input type="hidden" id="rq"/>
									<c:forEach var="list" items="${dayXxList }" varStatus="status">
										<div title="${list.name }" data-options="fit:true">
											<c:if test="${status.index==0}">
												<input type="hidden" id="rqInit" value="${dayXxList[0].id}"/>
											</c:if>
											<table id="${list.id }" style="width: 100%;"></table>
										</div>
									</c:forEach>	
								</div>				
							</div>
						</div>
					</div> 
			</div>
		</div>   
	</div>  

	<script type="text/javascript">
	var deptMap=new Map();
	//加载页面
	$(function(){
		//获取所有科室
		$.ajax({
			url:"<%=basePath%>technical/medicalMineplate/getDept.action",
			type:'post',
			success: function(date) {
				for(var i=0;i<date.length;i++){
					deptMap.put(date[i].deptCode,date[i].deptName);
				}
				
			}
		});
		$('#rq').val($('#rqInit').val());
		var rq = $('#rqInit').val();
		$('#tt').tabs({
		  onSelect: function(title,index){
		  	var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
			if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
				var t = eastpanel.panel('options').title;
		  		if(t=="查看模板信息"){
		  			$('#divLayout').layout('remove','east');
		  		}
			};
			var rqAxq = title.split("<br>");
			var rqArr = rqAxq[0].split(/['年''月''日']/);
			rq = rqArr[0]+'-'+rqArr[1]+'-'+rqArr[2];
			$('#rq').val(rq);
			loadDatagrid(getSelected(),rq);
		  },
		  tools:[{
		  	id:'btnTb',
			iconCls:'icon-add',
			text: '同步',
			handler:function(){
					var deptId = $('#deptId').val();
                	if(deptId==null||deptId==""){
                		$.messager.alert("提示","请选择具体科室！");
                		setTimeout(function(){
                			$(".messager-body").window('close');
                		},3500);
                		return;
                	}
                    $.ajax({
						url: "<c:url value='/technical/medicalMine/medicalMineSynch.action'/>",
						data:{deptId:deptId},
						type:'post',
						success: function(data) {
							var dataMap = eval("("+data+")");
							if(dataMap.resMsg=="error"){
								$.messager.alert("提示",'同步失败,请联系管理员!');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}else if(dataMap.resMsg=="notsynch"){
								$.messager.alert("提示",'信息全部存在无需同步!');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}else if(dataMap.resMsg=="success"){
								$.messager.alert("提示",'同步成功,共计: '+dataMap.resCode+' 条记录!');
							}
							var rq = $('#rq').val();
							$('#'+rq).datagrid('reload');
						}
					});
				}
			}]
		});
		$('#btnTb').linkbutton('disable');
		loadDatagrid(getSelected(),rq);
		bindEnterEvent('sName',searchFrom,'easyui');
	});
	
	function getSelected(){//获得选中节点
		var node = $('#tDt').tree('getSelected');
		if (node){
			return node.id;
// 			if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
// 				$('#btnTb').linkbutton('enable');
// 				return node.id;
// 			}else{
// 				$('#btnTb').linkbutton('disable');
// 				return "";
// 			}
		}else{
			return "";
		}
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
	//查询
	function searchFrom(){
		var deptId = $('#deptId').val();
   	if(deptId==null||deptId==""){
   		$.messager.alert("提示","请选择具体科室！");
   		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
   		return;
   	}
   	var datagridId = $('#inpWeekEnId').val();
   	var rq = $('#rq').val();
   	$('#'+rq).datagrid('load', {
   		deptId:deptId,
   		rq:rq,
		search: $('#sName').val()
	});
	}
	//加载可编辑表格
	function loadDatagrid(deptId,rq){//项目ID和星期
		$('#'+rq).datagrid({
			striped:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			rownumbers:true,
			fit:true,
			border:true,
			pageSize:20,
	   		pageList:[20,30,50,100],
	   		url: "<c:url value='/technical/medicalMine/queryMedicalMine.action'/>?menuAlias=${menuAlias}&time="+new Date(),
		    queryParams:{deptId:deptId,rq:rq,search:$('#sName').val()},
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
		    onDblClickRow: function (rowIndex, rowData) {//双击查看
				if(getIdUtil('#'+rq).length!=0){
			   	    AddOrShowEast("查看","<c:url value='/technical/medicalMine/MedicalMineEdit.action'/>?id="+getIdUtil('#'+rq),"580");
			   	}
			},
			 columns:[[    
				        {field:'ck',checkbox:true}, 
				        {field:'modelClass',title:'排班分类',width:'100',formatter:
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
                id: 'btnAdd'+rq,
                text: '添加',
                iconCls: 'icon-add',
                handler: function () {
                	var inpId=$('#deptId').val();//选择的项目;
                	if(inpId==null||inpId==""){
                		$.messager.alert("提示","请选择具体项目！");
                		setTimeout(function(){
                			$(".messager-body").window('close');
                		},3500);
                		return;
                	}
                	var nodeId = $('#nodeId').val();//判断选择是项目还是设备
                	var inpDeptName = $('#inpDeptName').val();//项目名称
                	AddOrShowEast("添加排班信息","<c:url value='/technical/medicalMine/MedicalMineAdd.action'/>?deptId="+inpId+"&rq="+$('#rq').val()+"&nodeId="+nodeId+"&deptName="+encodeURI(encodeURI(inpDeptName)),"580");
                }
            },{
                id: 'btnSave'+rq,
                text:'修改',
                iconCls: 'icon-edit',
                handler: function () {
                	var inpId=$('#deptId').val();//选择的项目;
                	if(inpId==null||inpId==""){
                		$.messager.alert("提示","请选择具体项目！");
                		setTimeout(function(){
                			$(".messager-body").window('close');
                		},3500);
                		return;
                	}
                    var row = $('#'+rq).datagrid('getSelected'); //获取当前选中行             
                    if(row!=null&&row.id!=null&&row.id!=''){
                    	AddOrShowEast("修改排班信息","<c:url value='/technical/medicalMine/MedicalMineEdit.action'/>?id="+row.id,"580");
   					}else{
   						$.messager.alert("提示","请选择需要修改的信息！");
   						setTimeout(function(){
   							$(".messager-body").window('close');
   						},3500);
   					}
                }
            }, {
                id: 'btnDelete'+rq,
                text: '删除',
                iconCls: 'icon-remove',
                handler: function () {
                    var rows = $('#'+rq).datagrid('getChecked');
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
									url: "<c:url value='/technical/medicalMine/delMedicalMine.action'/>?id="+ids,
									type:'post',
									success: function() {
										$.messager.progress('close');
										$.messager.alert("提示",'删除成功');
										$('#'+rq).datagrid('reload');
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
            }, {
                id: 'btnReload'+rq,
                text: '刷新',
                iconCls: 'icon-reload',
                handler: function () {
                	$('#'+rq).datagrid('reload');
                }
            },{
                id: 'btnMb'+rq,
                text: '模板',
                iconCls: 'icon-add',
                handler: function () {
                	var deptId = $('#deptId').val();
                	if(deptId==null||deptId==""){
                		$.messager.alert("提示","请选择具体项目！");
                		setTimeout(function(){
                			$(".messager-body").window('close');
                		},3500);
                		return;
                	}
                	AddOrShowEast("查看模板信息","<c:url value='/technical/medicalMine/MedicalMineTemp.action'/>?deptId="+deptId+"&rq="+$('#rq').val(),"780");
                }
            }]
		});
		if(getSelected()==null||getSelected()==""){
			//禁用按钮
			$('#btnAdd'+rq).linkbutton('disable');
			$('#btnSave'+rq).linkbutton('disable');
			$('#btnDelete'+rq).linkbutton('disable');
			$('#btnReload'+rq).linkbutton('disable');
			$('#btnMb'+rq).linkbutton('disable');
		}else{
			//启用按钮
			$('#btnAdd'+rq).linkbutton('enable');
			$('#btnSave'+rq).linkbutton('enable');
			$('#btnDelete'+rq).linkbutton('enable');
			$('#btnReload'+rq).linkbutton('enable');
			$('#btnMb'+rq).linkbutton('enable');
		}
		$('#rq').val(rq);
		$('#'+rq).datagrid('reload');
	}
	
	//加载医技信息树
	$("#tDt").tree({
		 url:"<%=basePath%>/technical/medicalMine/medicalMinetree.action?lm="+"1",
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					if(node.children.length>0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},
			onClick: function(node){//点击节点
				$('#divLayout').layout('remove','east');
				$('#deptId').val(node.id);
				$('#inpDeptName').val(node.text);
				var nodeId=node.attributes.pid;
				$("#nodeId").val(nodeId);
//				if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
//					$('#inpDeptId').val(node.id);
//					$('#inpDeptName').val(node.text);
//				}else{
//					$('#inpDeptId').val('');
//				}
				$('#inpWeekId').val(1);
				$('#inpWeekEnId').val("Monday");
				$('#tt').tabs('select',"星期一");
				loadDatagrid(getSelected(),$('#rq').val());
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
	
	//查询设备
	function sebei(){
		$('#tDt').tree({    
		    url:"<%=basePath%>technical/medicalMine/medicalMinetree.action?lm="+"2",
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					if(node.children.length>0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},
			onClick: function(node){//点击节点
				$('#divLayout').layout('remove','east');
				$('#deptId').val(node.id);
				$('#inpDeptName').val(node.text);
				$('#inpWeekId').val(1);
				var nodeId=node.attributes.pid;
				$("#nodeId").val(nodeId);//1是项目信息2设备信息
				$('#inpWeekEnId').val("Monday");
				$('#tt').tabs('select',"星期一");
				loadDatagrid(getSelected(),$('#rq').val());
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
		    url:"<%=basePath%>technical/medicalMine/medicalMinetree.action?lm="+"1",
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					if(node.children.length>0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},
			onClick: function(node){//点击节点
				$('#divLayout').layout('remove','east');
				$('#deptId').val(node.id);
				$('#inpDeptName').val(node.text);
				$('#inpWeekId').val(1);
				var nodeId=node.attributes.pid;
				$("#nodeId").val(nodeId);//1是项目信息2设备信息
				$('#inpWeekEnId').val("Monday");
				$('#tt').tabs('select',"星期一");
				loadDatagrid(getSelected(),$('#rq').val());
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
					collapsible:false,
					border:false
				});
			} else {//打开新面板
				$('#divLayout').layout('add', {
					title : title,
					region : 'east',
					width : width,
					href : url,
					closable : false,
					collapsible:false,
					border:false
				});
			}
		}	
	</script>
	</body>
</html>