<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医嘱执行单设置</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
 		 	var userdataMap = "";//用户map	
 			//加载页面
			$(function(){
				loadDatagrid(null,1)
				/**
				 * 查询操作人
				 */
				$.ajax({
					url: '<%=basePath%>baseinfo/employee/getEmplMap.action',
					async:false,
					type:'post',
					success: function(userdata) {						
						userdataMap = userdata;							
					}
				});	
				//加载药品执行单tree					
				$('#tt').tree({    
				    url:'<%=basePath%>inpatient/doctorAdvice/treeBills.action',
				    method:'get',
				    animate:true,
				    lines:true,
				    formatter:function(node){//统计节点总数
						var s = node.text;					
						if (node.children.length>0){
							s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
						}
						return s;
					},
					onClick:function(node){
						$("#dg").datagrid("clearChecked");
						$('#divLayout').layout('remove','east');
						var billNo=(node.id);
						$.post('<%=basePath%>inpatient/doctorAdvice/queryAllBillDetail.action',{billNo:billNo},function(data){
							if(data==null||data.length==0||data==""){
							loadDatagrid(billNo,1)
	 							}else{
							var type=data[0].billType;
						    loadDatagrid(billNo,type)								
							}
						});						
					},onContextMenu: function(e, node){
						var nid =(node.id);
						if(nid==1){
						e.preventDefault();
						// 查找节点
						$('#tt').tree('select', node.target);
						// 显示快捷菜单
						$('#m1').menu('show', {
							left: e.pageX,
							top: e.pageY
						});
						}else{
							e.preventDefault();
							// 查找节点
							$('#tt').tree('select', node.target);
							// 显示快捷菜单
							$('#m2').menu('show', {
								left: e.pageX,
								top: e.pageY
							});
						}
					}
				}); 
						
	 		 	$('#UnDrugInfoViewListId').datagrid({					
	 		 		onDblClickRow:function(rowIndex, rowData){		 		 		
						var row = $('#UnDrugInfoViewListId').datagrid('getSelected');	
						var name = $('#docAdvType').combobox('getValue');
						var undrugSystype = $('#adProjectTdId').combobox('getValue');
						if(name==''){
							$.messager.alert('提示',"请先选择一个医嘱类型!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return;
						}
						if(undrugSystype==''){
							$.messager.alert('提示',"请先选择一个系统类别!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return;
						}
						$('#'+getTabId()).datagrid('appendRow',{
							typeCodeId:$('#docAdvType').combobox('getValue'),
							typeName: $('#docAdvType').combobox('getText'),
							drugTypeId:$('#adProjectTdId').combobox('getValue'),
							drugTypeName: $('#adProjectTdId').combobox('getText'),
							usageCodeId:row.code,
							usageName: row.name,
							billType:2
						});		
					}
				});	
	 		 
				//长期医嘱类型下拉框
	    		$('#docAdvType').combobox({
	    			url:'<%=basePath%>inpatient/docAdvManage/queryDocAdvType.action', 
				    valueField:'typeCode',    
				    textField:'typeName',
				    multiple:false
	    		});
				//系统类型下拉框 systemType
				$('#adProjectTdId') .combobox({    
					url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=systemType'/>", 
				    valueField:'encode',    
				    textField:'name',
				    multiple:false
				});
				
				bindEnterEvent('undrugName',searchFrom,'easyui');
			
			});
 		 	
			function loadDatagrid(billNo,billType){
				$('#dg').datagrid({  
					url:'<%=basePath%>inpatient/doctorAdvice/queryInpatientDrugbilldetail.action',
					queryParams:{'inpatientDrugbilldetail.billNo':billNo,'inpatientDrugbilldetail.billType':billType},	
					fit:true,
					singleSelect:true,
					method:'post',
					rownumbers:true,
					idField: 'id',
					striped:true,
					border:false,
					selectOnCheck:false,
					checkOnSelect:true,
					fitColumns:true,
					toolbar:"#toolbarId",
					pagination : true,//是否显示分页栏
					pageSize : 20,//每页显示的记录条数，默认为10  
					pageList : [ 20, 40,60,80,100 ],//可以设置每页记录条数的列表  
					columns:[[    
					          {field:'ck',width:"5%",checkbox : true},    
					          {field:'id',title:'执行单id',hidden:true},      
					          {field:'typeCode',title:'医嘱类型Id',hidden:true},      
					          {field:'typeName',title:'医嘱类型',width:"10%",},    
					          {field:'drugType',title:'项目类别Id',hidden:true},    
					          {field:'drugTypeName',title:'项目类别',width:"15%",},    
					          {field:'usageCode',title:'服用方法Id',hidden:true},    
					          {field:'usageName',title:'服用方法/非药品名称',width:"20%",},    
					          {field:'billType',title:'药品/非药品',hidden:true},    
					          {field:'updateUser',title:'当前操作员',width:"20%",formatter:funUser},
					          {field:'updateTime',title:'操作时间',width:"30%",}
					      ]],
					onDblClickRow: function (rowIndex, rowData) {//双击	
						$.messager.confirm('确认', '确定要删除本条记录吗?', function(res){//提示是否删除
						var row = $('#'+getTabId()).datagrid('getSelected');							
						if(row.updateTime==undefined){										
							var rowIndex = $('#'+getTabId()).datagrid('getRowIndex', row);
							$('#'+getTabId()).datagrid('deleteRow', rowIndex);
							$.post('<%=basePath%>inpatient/doctorAdvice/delDrugbilldetail.action?ids='+row.id);					
					        var nodeP = null;
					        var nodes = null;
					        if(row.usageCode==undefined){			        	
					        }else{
					        nodes= $('#tt').tree('getRoots');						       						        
					        for(var i=0;i<nodes.length;i++){
					        	if(nodes[i].id==row.typeCodeId){
					        		nodeP=nodes[i];
					        		break;
					        	}							        	
					        }
					        if(nodeP!=null){
					        	var chsP = null;
						        var chs = $('#tt').tree('getChildren',nodeP.target);
				        		for(var j=0;j<chs.length;j++){
				        				if(chs[j].id==row.drugTypeId){
					        				chsP = chs[j];
					        				break;
					        			}
				        		}
				        		if(chsP!=null){
				        			$('#tt').tree('append', {
 							        	parent: chsP.target,
 							        	data: [{
 							        		 id: row.usageCodeId,
 								     		 text: row.usageCode
 							        	}]
 							        });
				        			$('#tt').tree('expand',nodeP.target);
				        			$('#tt').tree('expand',chsP.target);
				        		} 
					        }
					        }
						}else{
						$.messager.confirm('确认', '确定要删除吗?', function(res){
							if (res){									
								var ids = row.id;																
								$.post('<%=basePath%>inpatient/doctorAdvice/delDrugbilldetail.action?ids='+ids,function(data){						
									  if(data=="error"){
										  $.messager.alert('提示','删除失败!');  
									  }else if(data=="success"){
										  $.messager.alert('提示','操作成功！');
										  setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
										  $('#'+getTabId()).datagrid('deleteRow',$('#'+getTabId()).datagrid('getRowIndex',row));
									  }else{
										  $.messager.alert('提示','出错了，操作失败!'); 
									  }
								 });
							  }							
		                   });
						}
					});
					}		
					});
			}	
			
			function searchFrom(){
				$('#UnDrugInfoViewListId').datagrid({
					method:'post',
					rownumbers:true,
					idField: 'id',
					striped:true,
					border:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					fitColumns:false,
					rownumbers:true,						
					pagination:true,	
					pageSize:20,
					pageList:[20,30,50,80,100],
					url:'<%=basePath%>inpatient/doctorAdvice/queryUndrugInfo.action?menuAlias=${menuAlias}',
					queryParams:{'undrug.name':$('#undrugName').val(),'undrug.undrugSystype':$('#adProjectTdId').combobox('getValue')}
	 		 	});	
			}
			$('#adProjectTdId').combobox({  
				onSelect:function(record){
					searchFrom();					
				}
			}); 
	
			//关闭当前页
			function out(){				
				self.parent.$('#tabs').tabs('close',"医嘱执行单设置");
			}
				
			//弹出添加窗口								
			function add(){
				AdddilogModel("addData-window","添加执行单",'<%=basePath%>inpatient/doctorAdvice/addDocAdvExeInfo.action?menuAlias=${menuAlias}','366px','266px');
			}
			
			//弹出修改窗口
			function upd(){
				var node=$("#tt").tree("getSelected");
				var ids=node.id;
				AdddilogModel("addData-window","修改执行单","<%=basePath%>inpatient/doctorAdvice/updDocAdvExeInfo.action?ids="+ids+"&menuAlias=${menuAlias}",'366px','266px');
			}
			/**
			 * 删除tab标签页
			 * @author  qh
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2017-04-01
			 * @version 1.0
			 */
			function del(){
				var node=$("#tt").tree("getSelected");
				var ids=node.id;
			    if (ids!=null) {//选中几行的话触发事件	                        
 					$.messager.confirm('确认', '确定要删除执行单吗?', function(res){//提示是否删除
 					if (res){
 						 $.ajax({
 							url: '<%=basePath%>inpatient/doctorAdvice/delDocAdvExe.action',
 							data:'ids='+ids+'&menuAlias=${menuAlias}',
 							type:'post',
 							success: function(data) {
 								if(data='success'){
 									$.messager.alert('提示','删除成功!'); 
 									setTimeout(function(){
 										$(".messager-body").window('close');
 									},3500);
 								   window.location="<%=basePath%>inpatient/doctorAdvice/docAdvExeInfos.action?menuAlias=${menuAlias}";			   			 
 								}else{
 									$.messager.alert('提示','删除失败!'); 
 								}
 							}
 						 });
 					  }
                   });
                }              
			}
			
			//加载模式窗口
			function AdddilogModel(id,title,url,width,height) {
				$('#'+id).dialog({    
				    title: title,
				    width: width,
				    height: height,
				    closed: false,
				    cache: false,
				    href: url,
				    modal: true
				});    
			}
			
			//关闭模式窗口
			function closeLayout(){
				$('#addData-window').window('close');
			}
			
			
			/**
			 *  
			 * @Description：操作人渲染
			 * @Author：donghe
			 * @CreateDate：2016-3-09 下午18:56:31  
			 *
			 */
			function funUser(value,row,index){
				if(value!=null&&value!=''){
					return userdataMap[value];
				}else{
					return null;
				}
			}
		
			//展开添加面板 添加bill
			function addBill(){
				var node= $('#tt').tree('getSelected');
				if(node==null){
					$.messager.alert('提示','请先选择项目执行单!'); 
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
				  return;
				}
				var idSelected=node.id;
				var billName=node.text;
			    var rows=$('#dg').datagrid('getRows');
			    if(rows==null||rows==""){
			      var billType="";
			    }else{
				var billType=rows[0].billType;	
			    }
				if (idSelected == null||idSelected==''|| idSelected == "1") {
					$.messager.alert('提示信息','请选择具体的执行单类型,再进行添加！');
					return false;
				}
			     AddOrShowEast('EditForm',"<%=basePath%>inpatient/doctorAdvice/viewAddBill.action",'post',{billNo:idSelected,billName:billName,billType:billType});
			}
			//删除
			function delBills(){
				var rows = $('#dg').datagrid('getChecked');
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
							 	url: "<%=basePath%>inpatient/doctorAdvice/delDrugbilldetail.action?ids="+ids,   
								type:'post',
								success: function(date) {
									$.messager.progress('close');
									if(date.resCode=='success'){
										$.messager.alert('提示信息','删除成功！');
										$('#dg').datagrid('reload');
									}else{
										$.messager.alert('提示信息','删除失败！');
									}
								}
							});
						}
			     });
			 }else{
				$.messager.alert('提示','请先选择要删除的执行单!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			  return;
			 }
			}
			//修改
			function editBill(){	
			var node= $('#tt').tree('getSelected');
			if(node==null){
				$.messager.alert('提示','请先选择项目执行单!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			  return;
			}
			var idSelected=node.id;
			var billName=node.text;
			var rows=$('#dg').datagrid('getChecked');
				if(rows.length==1){
					var id=rows[0].id;
					var typeCodeId=rows[0].typeCode;
					var drugTypeId=rows[0].drugType;
					var billType=rows[0].billType;
					var usageCodeId=rows[0].usageCode;
			        AddOrShowEast('EditForm',"<%=basePath%>inpatient/doctorAdvice/viewAddBill.action",'post',{billNo:idSelected,billName:billName,id:id,
			        	typeCodeId:typeCodeId,drugTypeId:drugTypeId,billType:billType,usageCodeId:usageCodeId});
				}else{
					$.messager.alert('提示信息','请选择一条记录进行修改');
				}
			}
			function view(){
				var treeObj = $('#tDt').tree('getSelected');
				var rows=$("#list").datagrid('getSelected');
				if(getIdUtil("#list").length!=0){
			        AddOrShowEast('EditForm',"<%=basePath%>baseinfo/financeFixedcharge/viewFinanceFixedcharge.action?id="+getIdUtil("#list")+"&treeId="+$('#treeId').val()+"&drugUndrugName="+encodeURI(UnDrug(rows.drugUndrug)));
					}
			}
			//刷新
			function reload(){
				//实现刷新栏目中的数据
				$('#dg').datagrid('reload');
			}
			/**
			 * 动态添加LayOut
			 * @author  qh
			 * @param title 标签名称
			 * @param url 跳转路径
			 * @date 2017-03-31
			 * @version 1.0
			 */
			function AddOrShowEast(title, url,method,params) {
				if(!method){
					method="get";
				}
				if(!params){
					params={};
				}
				var eastpanel=$('#panelEast'); //获取右侧收缩面板
				if(eastpanel.length>0){ //判断右侧收缩面板是否存在
					//重新装载右侧面板
			   		$('#divLayout').layout('panel','east').panel({
			                     href:url 
			              });
				}else{//打开新面板
					$('#divLayout').layout('add', {
						region : 'east',
						width : 405,
						split : true,
						href : url,
						method:method,
						queryParams:params,
						closable : true,
						border : false
					});
				}
			}
</script>
<style type="text/css">
	.panel-header{
		border-top:0;
	}
</style>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div id="p" data-options="region:'west',split:true" style="width: 18%;border-top:0;">	
					  <ul id="tt" style="width:100%;height:99.8%;"></ul>  							 
									<div data-options="region:'west',split:false,border:true" style="width:100%;height: 95px;display:none">
										<table cellspacing="0" cellpadding="0" border="0" data-options="fit:true">
											<tr style="margin-top:5px;">
												<td style="padding: 7px 5px;;" nowrap="nowrap">&nbsp;医嘱类型：</td>
												<td>
													<input id="docAdvType" class="easyui-combobox" style="width:110px;"/>
												</td>
											</tr>
											<tr>
												<td style="padding: 7px 5px;" nowrap="nowrap">&nbsp;项目类别：</td>
												<td>
													<input id="adProjectTdId" class="easyui-combobox" style="width:110px;">
												</td>
											</tr>
											<tr>
												<td style="padding: 7px 5px;" nowrap="nowrap">&nbsp;非药品名称：</td>
												<td>
													<input type="text" id="undrugName" style="width:110px;" class="easyui-textbox" data-options="prompt:'输入/回车查询'"/>																
												</td>								
											</tr>
										</table>
									</div>	
							 <div data-options="region:'center',split:false,iconCls:'icon-book',border:false">
								<table id="UnDrugInfoViewListId" class="easyui-datagrid" style="width:330px;height:500px;align:center;" data-options="fit:true">
								<thead>
									<tr>
										<th data-options="field:'code',hidden:true">非药品Id</th>
										<th data-options="field:'name'" style="width:300px;">名称</th>														
									</tr>
								</thead>
								</table>
							</div>															
			 <div id = "addData-window"></div>
		   <div id="m1" class="easyui-menu" style="width:120px;">
	         <div onclick="add()" data-options="iconCls:'icon-add'">添加</div>
          </div>
		   <div id="m2" class="easyui-menu" style="width:120px;">
	         <div onclick="del()" data-options="iconCls:'icon-remove'">移除</div>
	         <div onclick="upd()" data-options="iconCls:'icon-edit'">修改</div>
          </div>
		</div>
		<div data-options="region:'center'" style="border-top:0;">		
            <table id="dg"></table>
			<div id="toolbarId" >
			    <shiro:hasPermission name="${menuAlias}:function:add">
				<a href="javascript:void(0)" onclick="addBill()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	 			</shiro:hasPermission>
	 			<shiro:hasPermission name="${menuAlias}:function:edit"> 
				<a href="javascript:void(0)" onclick="editBill()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	 			</shiro:hasPermission> 
	 			<shiro:hasPermission name="${menuAlias}:function:delete"> 
				<a href="javascript:void(0)" onclick="delBills()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	 			</shiro:hasPermission> 		
				<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>			
		  </div>
	 </div>
</div>
</body>
</html>

