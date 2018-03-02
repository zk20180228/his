<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<jsp:include page="/javascript/default.jsp"></jsp:include>

	<body>
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'" >
				<div id="divLayoutedit" class="easyui-layout" fit=true>
				<div data-options="region:'center',split:false,title:'药房药库出入库科室',iconCls:'icon-book'" style="padding:5px;">
				<input type="hidden" name="modelWeek" id="modelWeek"/>
					<div id="ttedit" class="easyui-tabs" data-options="">
							<div title="入库" style="padding:10px">
								<form id="editForm" method="post">
									<input type="hidden" id="infoJson" name="infoJson"/>
									<table id="indeptedit"></table> 
								</form>	
							</div>
							<div title="出库" style="padding:10px">
								<form id="editForm" method="post">
									<table id="outdeptedit"></table> 
								</form>
							</div>
					</div>				
				</div>
			</div>
			</div>	
		</div>	
		<div id="addDept"></div>
		<script type="text/javascript">
		//加载页面
		var Id = "${Id}";
		var divname = "${divname}";
		$(function(){
			var xq = "indeptedit";
			$('#ttedit').tabs({
			  onSelect: function(title){
			  	$('#infoJson').val("");
			  	$('#name').val("");
				if(title=="入库"){
					xq = "indeptedit";
				}else if(title=="出库"){
					xq = "outdeptedit";
				}
				loadEdatagrid(getSelected(),xq);
			  }
			});
			loadEdatagrid(getSelected(),xq);
		});
		
		//加载可编辑表格
		function loadEdatagrid(treId,xq){
			var modelWeek = 1 ;
			if(xq=="indeptedit"){
				modelWeek = 1;
			}else if(xq=="outdeptedit"){
				modelWeek = 2;
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
			    url: "<c:url value='/drug/inoutdept/inoutdeptedits.action'/>?Id="+Id+"&type="+modelWeek,
			    columns:[[    
			    	{field:'ck',checkbox:true},
			    	{field:'objectDept',title:'部门科室编码',hidden:true},
			        {field:'deptCode',title:'部门科室编码',width:'20%'},    
			        {field:'deptName',title:'部门科室名称',width:'20%'},  
			        {field:'remark',title:'备注',editor:{type:'textbox',options:{required:true}},width:'20%'}
			    ]],  
				toolbar: [{
                    id: 'btnSave'+xq,
                    text: '保存',
                    iconCls: 'icon-save',
                    handler: function () {
                       	$('#'+xq).datagrid('acceptChanges');
                       	$('#infoJson').val(JSON.stringify($('#'+xq).datagrid("getRows")));
						$('#editForm').form('submit',{
					        url:"<c:url value='/drug/inoutdept/saveInoutdept.action'/>?deptId="+getSelected()+"&inoutDeptId="+$("#tDeptId").val()+"&type="+modelWeek,  
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
                    id: 'btnReload'+xq,
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                    	$('#divLayout').layout('remove','east');
                    }
                }]
			});
			$('#modelWeek').val(xq);
			$('#'+xq).edatagrid('reload');
		}
		
			
			/**
			 * 添加弹出科室树
			 * @author  lt
			 * @param title 标签名称
			 * @param url 跳转路径
			 * @date 2015-06-25
			 * @version 1.0
			 */
			function AddDeptdilog(title, url) {
				$('#addDept').dialog({    
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
			 * 打开科室树框
			 * @author  lt
			 * @date 2015-06-26
			 * @version 1.0
			 */
			function openDeptDialog() {
				$('#addDept').dialog('open'); 
			}
			/**
			 * 加载科室树框及信息
			 * @author  lt
			 * @date 2015-06-26
			 * @version 1.0
			 */
			function openDept(flg){
				if(flg==1){
					AddDeptdilog("科室信息","<c:url value='/baseinfo/department/deptTree.action'/>?flag=1");
				}
				if(flg==2){
					var rows = $('#'+$('#modelWeek').val()).edatagrid('getSelections');
                	if (rows.length > 0) {//选中几行的话触发事件	
						var ids = '';
						for(var i=0; i<rows.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += rows[i].id;
						};
						AddDeptdilog("科室信息","<c:url value='/baseinfo/department/deptTree.action'/>?flg=2 &inoutdeptids="+ids);
					}else{
						$.messager.alert("操作提示", "请从列表中选择操作条目！","warning");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
					
				}
			}
			
	   		

		</script>
	</body>
</html>