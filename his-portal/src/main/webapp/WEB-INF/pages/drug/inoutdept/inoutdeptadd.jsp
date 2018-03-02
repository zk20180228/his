<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'" >
				<div id="divLayoutadd" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
				<div data-options="region:'center',split:false,title:'药房药库出入库科室',iconCls:'icon-book'" style="padding:5px;">
				<input type="hidden" name="modelWeeks" id="modelWeeks"/>
						<table style = "width:100%">
							<tr align="center">
				    			<td>
				    				<shiro:hasPermission name="${menuAlias}:function:add">
				    				 <a id="btnAddUsers" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="openDepts(1)">批量添加科室</a>
				    				</shiro:hasPermission>
				    			</td>
				    		</tr>
						</table>
					<div id="tts" class="easyui-tabs" data-options="">
							<div title="入库" style="padding:10px">
								<form id="editForm" method="post">
									<input type="hidden" id="infoJson" name="infoJson"/>
									<table id="indepts"></table> 
								</form>	
							</div>
							<div title="出库" style="padding:10px">
								<form id="editForm" method="post">
									<table id="outdepts"></table> 
								</form>
							</div>
					</div>				
				</div>
			</div>
			</div>	
		</div>	
		<div id="addDepts"></div>
		<script type="text/javascript">
		//加载页面
		var divname = "${divname}";
		$(function(){
			var xq = "indepts";
			$('#tts').tabs({
			  onSelect: function(title){
			  	$('#infoJson').val("");
			  	$('#name').val("");
				if(title=="入库"){
					xq = "indepts";
				}else if(title=="出库"){
					xq = "outdepts";
				}
				loadEdatagrid(getSelected(),xq);
			  }
			});
			loadEdatagrid(getSelected(),xq);
		});
		
		//加载可编辑表格
		function loadEdatagrid(treId,xq){
			var modelWeeks = 1 ;
			if(xq=="indepts"){
				modelWeeks = 1;
			}else if(xq=="outdepts"){
				modelWeeks = 2;
			}
			$('#'+xq).edatagrid({   
				striped:true,
				checkOnSelect:true,
				selectOnCheck:true,
				singleSelect:false,
				fitColumns:false,
				pagination:true,
				rownumbers:true,
				pageSize:20,
		   		pageList:[20,30,50,100],
			    columns:[[    
			    	{field:'ck',checkbox:true},
			    	{field:'objectDept',title:'部门科室编码',hidden:true},
			        {field:'deptCode',title:'部门科室编码',width:'20%'},    
			        {field:'deptName',title:'部门科室名称',width:'20%'},  
			        {field:'remark',title:'备注',editor:{type:'textbox'},width:'20%'}
			    ]],  
				toolbar: [{
                    id: 'btnSave'+xq,
                    text: '保存',
                    iconCls: 'icon-save',
                    handler: function () {
                       	$('#'+xq).datagrid('acceptChanges');
                       	$('#infoJson').val(JSON.stringify($('#'+xq).datagrid("getRows")));
						$('#editForm').form('submit',{
					        url:"<c:url value='/drug/inoutdept/saveInoutdept.action'/>?deptId="+getSelected()+"&inoutdeptsId="+$("#tDeptId").val()+"&type="+modelWeeks,  
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
					        	$('#divLayout').layout('remove','east');
					        	$('#'+divname).edatagrid('reload');
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
                    	var ids = '';
			   			for(var i=0;i<rows.length;i++){   
			   				if(rows[i].id==null){//如果id为null 则为新添加行
			   					var dd = $('#'+xq).edatagrid('getRowIndex',rows[i]);//获得行索引
			   			  		$('#'+xq).edatagrid('deleteRow',dd);//通过索引删除该行
			   				}else{
			   					ids += rows[i].id + ",";
			   				}
						}
                    }
                }, '-', {
                    id: 'btnReload'+xq,
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                    	$('#divLayout').layout('remove','east');
                    }
                }]
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
			$('#modelWeeks').val(xq);
			$('#'+xq).edatagrid('reload');
		}
		
			//空格事件
	   		function KeyDown()  
			{  
			    if (event.keyCode == 32)  
			    { 
			        event.returnValue=false;  
			        event.cancel = true;  
			        openDepts();
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
			/**
			 * 加载科室树框及信息
			 * @author  lt
			 * @date 2015-06-26
			 * @version 1.0
			 */
			function openDepts(flg){
				if(flg==1){
					AddDeptdilogs("科室信息","<c:url value='/baseinfo/department/deptTree.action'/>?flag=1");
				}
				if(flg==2){
					var rows = $('#'+$('#modelWeeks').val()).edatagrid('getSelections');
                	if (rows.length > 0) {//选中几行的话触发事件	
						var ids = '';
						for(var i=0; i<rows.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += rows[i].id;
						};
						AddDeptdilogs("科室信息","<c:url value='/baseinfo/department/deptTree.action'/>?flag=2&inoutdeptsids="+ids);
					}else{
						$.messager.alert("操作提示", "请从列表中选择操作条目！","warning");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
					
				}
			}
			
	   		
		   	function delDetp(){//移除部门科室
		   		$.messager.confirm('确认', '确定要删除选中信息吗?', function(){//提示是否删除
					$.ajax({
						url: "<c:url value='/delTreeDepartment.action'/>?id="+getSelected(),
						type:'post',
						success: function() {
							$.messager.alert('提示','删除成功!');
							$('#tDt').tree('reload');
						}
					});										
				});
			}


		</script>
	</body>
</html>