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
<title>药房药库出入库科室</title>
</head>
	<body>
		<div class="easyui-layout" data-options="fit:true">
			<div id="p" data-options="region:'west',split:true"  style="width:14%;height:100%;padding:5px;border-top:0">
				<ul id="tDt"></ul>  
			</div>
			<div data-options="region:'center'" style="width:83%;padding:5px;border-top:0" >
				<input type="hidden" name="modelWeek" id="modelWeek"/>
				<div id="tt" class="easyui-tabs" data-options="fit:true" >
						<div title="入库" style="width: 100%;">
							<form id="editForm" method="post" style="width: 100%;height: 100%;">
								<input type="hidden" id="infoJson" name="infoJson"/>
								<table id="indept" data-options="striped:true,
																border:false,
																checkOnSelect:true,
																selectOnCheck:true,
																singleSelect:false,
																fitColumns:false,
																pagination:true,
																rownumbers:true,
																nowrap: true,
																pageSize:20,
																fit:true,
															  	pageList:[20,30,50,100],onLoadSuccess:function(row, data){
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
																}}" class="easyui-datagrid"></table> 
							</form>	
						</div>
						<div title="出库" style="width: 100%;">
							<form id="editForm" method="post" style="width: 100%;height: 100%;">
								<table id="outdept" data-options="striped:true,
																border:false,
																checkOnSelect:true,
																selectOnCheck:true,
																singleSelect:false,
																fitColumns:false,
																pagination:true,
																rownumbers:true,
																nowrap: true,
																pageSize:20,
 																fit:true, 
															  	pageList:[20,30,50,100],onLoadSuccess:function(row, data){
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
																}}" class="easyui-datagrid"></table> 
							</form>
						</div>
				</div>				
			</div>	
		</div>	
		<div id="addDepts"></div>
		
		<style type="text/css">
		.window .panel-header .panel-tool a{
  	  			background-color: red;	
			}
		</style>
		<script type="text/javascript">
		var deptidName=null;
		//加载页面
		$(function(){
			var xq = "indept";
			$('#tt').tabs({
			  onSelect: function(title){
			  	$('#infoJson').val("");
			  	$('#name').val("");
				if(title=="入库"){
					xq = "indept";
				}else if(title=="出库"){
					xq = "outdept";
				}
				loadEdatagrid(getSelected(),xq);
			  }
			});
			loadEdatagrid(getSelected(),xq);
		});
		
		//加载可编辑表格
		function loadEdatagrid(treId,xq){
			var modelWeek = 1 ;
			if(xq=="indept"){
				modelWeek = 1;
			}else if(xq=="outdept"){
				modelWeek = 2;
			}
			$('#'+xq).datagrid({   
			    url:"<%=basePath%>drug/inoutdept/queryInoutdept.action?deptId="+treId+"&type="+modelWeek + "&menuAlias=${menuAlias}",
			    pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
			    columns:[[    
			    	{field:'ck',checkbox:true},
			        {field:'deptCode',title:'部门科室编码',width:'15%'},    
			        {field:'deptName',title:'部门科室名称',width:'15%'},  
			    ]],
			    onBeforeLoad:function (param) {
					var deptId=getSelected();//得到选择的科室code
					if(deptId == "" || deptId=="1"){
						return false;
					}
					$('#'+xq).datagrid('clearChecked');
					$('#'+xq).datagrid('clearSelections');
		        },
				toolbar: [{
					id: 'btnAdd'+xq,
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                    	openDepts(modelWeek);
                    	}
                }, '-',{
                    id: 'btnSave'+xq,
                    text: '保存',
                    iconCls: 'icon-save',
                    handler: function () {
                       	$('#'+xq).datagrid('acceptChanges');
                       	$('#infoJson').val(JSON.stringify($('#'+xq).datagrid("getRows")));
						$('#editForm').form('submit',{
					        url:"<%=basePath%>drug/inoutdept/saveInoutdept.action?deptId="+getSelected()+"&type="+modelWeek,
					        onSubmit:function(){ 
					        	if (!$(this).form('validate')) {
									$.messager.alert('提示',"验证没有通过,不能提交表单!");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									return false;
								}
					        	$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条  
					        },  
					        success:function(data){  
					        	$.messager.progress('close');
					        	$.messager.alert('提示','保存成功');
					        	$('#'+xq).edatagrid('reload');
					        },
							error : function(data) {
								$.messager.progress('close');
								$.messager.alert('提示',"保存失败！");	
							}			         
						  }); 
                    }
                }, '-', {
                    id: 'btnDelete'+xq,
                    text: '删除',
                    iconCls: 'icon-remove',
                    handler: function () {
                    	var rows = $('#'+xq).datagrid('getChecked');
                    	if(rows == null){
                            $.messager.alert('操作提示','请选中要删除的科室');
                            setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
                        }
                    	var ids = '';
			   			for(var i=0;i<rows.length;i++){   
			   				if(rows[i].id==null){//如果id为null 则为新添加行
			   					var dd = $('#'+xq).edatagrid('getRowIndex',rows[i]);//获得行索引
			   			  		$('#'+xq).edatagrid('deleteRow',dd);//通过索引删除该行
			   				}else{
			   					if(ids != ''){
			   						ids += ',';
			   					}
			   					ids += rows[i].id;
			   				}
						}
			   			if(ids!=null&&ids!=""){
			   				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
								if (res){
									$.ajax({
										url: "<%=basePath%>drug/inoutdept/delInoutdept.action?ids="+ids,
										type:'post',
										success: function() {
											$.messager.alert('提示','删除成功');
											$('#'+xq).edatagrid('reload');
										}
									});
								}
                      	  	});
			   			}
                    }
                }, '-', {
                    id: 'btnReload'+xq,
                    text: '刷新',
                    iconCls: 'icon-reload',
                    handler: function () {
                    	$('#'+xq).edatagrid('reload');
                    }
                }],onLoadSuccess:function(row, data){
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
    				}}
			});
			if(getSelected()==null||getSelected()==""){
				//禁用按钮
				$('#btnAddUser').linkbutton('disable');
				$('#btnAdd'+xq).linkbutton('disable');
				$('#btnSave'+xq).linkbutton('disable');
				$('#btnDelete'+xq).linkbutton('disable');
			}else{
				//启用按钮
				$('#btnAddUser').linkbutton('enable');
				$('#btnAdd'+xq).linkbutton('enable');
				$('#btnSave'+xq).linkbutton('enable');
				$('#btnDelete'+xq).linkbutton('enable');
			}
			$('#modelWeek').val(xq);
			$('#'+xq).edatagrid('reload');
		}
		
			//空格事件
	   		function KeyDown()  
			{  
			    if (event.keyCode == 32)  
			    { 
			        event.returnValue=false;  
			        event.cancel = true;  
			        openDept();
			    }  
			} 
	   		/**
			 * 加载科室树框及信息
			 * @author  lt
			 * @date 2015-06-26
			 * @version 1.0
			 */
			function openDepts(flg){
				if(flg==1){//入库
					AddDeptdilogs("科室信息","<%=basePath%>baseinfo/department/deptTree.action?flg=1&deptidName="+deptidName);
				}
				if(flg==2){//出库
					AddDeptdilogs("科室信息","<%=basePath%>baseinfo/department/deptTree.action?flg=2&deptidName="+deptidName);
				}
			}
			/**
			 * 添加弹出科室树
			 * @author  lt
			 * @param title 标签名称
			 * @param url 跳转路径
			 * @date 2015-06-25
			 * @version 1.0
			 */
			function AddDeptdilogs(title, url) {
				$('#addDepts').dialog({    
				    title: title,    
				    width: '30%',    
				    height:'90%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
			}
	   		
			//加载部门树
		   	$('#tDt').tree({    
			    url:"<%=basePath%>drug/storage/treeDrugstore.action?flag=1",
			    method:'get',
			    animate:true,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children.length>0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},onContextMenu: function(e,node){//添加右键菜单
					e.preventDefault();
					$(this).tree('select',node.target);
					if(node.attributes.pid=='root'){
						$('#editDiv').css("display","none");
						$('#delDiv').css("display","none");
						$('#tDtmm').menu('show',{
							left: e.pageX,
							top: e.pageY
						});
					}else{
						$('#editDiv').css("display","block");
						$('#delDiv').css("display","block");
						$('#tDtmm').menu('show',{
							left: e.pageX,
							top: e.pageY
						});
					}
				},onClick: function(node){//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					$('#infoJson').val("");
					deptidName=node.id;
					if(deptidName.indexOf("type_P") == -1){
						loadEdatagrid(node.id,$('#modelWeek').val());
					}
				}
			}); 
			function getSelected(){//获得选中节点
				var node = $('#tDt').tree('getSelected');
				if (node && (node.children==null || node.children=="")){
					var id = node.id;
					return id;
				}else{
					return "";
				}
			}
			/**
			 * 动态添加标签页
			 * @author  sunshuo
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2015-05-21
			 * @version 1.0
			 */
			 function AddOrShowEast(title, url) {
					var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
					if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
						//重新装载右侧面板
						$('#divLayout').layout('panel', 'east').panel({
							href : url
						});
					} else {//打开新面板
						$('#divLayout').layout('add', {
							region : 'east',
							width : 580,
							split : true,
							href : url,
							closable : true
						});
					}

				}
		</script>
	</body>
</html>