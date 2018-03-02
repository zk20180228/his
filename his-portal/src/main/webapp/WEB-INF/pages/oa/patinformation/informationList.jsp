<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>栏目管理</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
/* #toolbarId { */
/* 	background-color: #ced3d8; */
/* } */
.layout-split-west{
	border-right: 5px solid #ced3d8;
}
#divLayout .datagrid-pager {
	width: 82%;
    position: fixed;
    bottom: 0px;
}
#divLayout .datagrid{
	height: 100% !important;
}

#divLayout .datagrid-wrap{
	height: 100% !important;
}	
</style>



<script type="text/javascript">
var cid="";
			$(function(){
				loadDatagrid(1,3);
// 				加载节点树					
				$('#tt').tree({    
				    url:'<%=basePath%>oa/patMenuManager/showTreeWithCodeForTital.action',
				    method:'get',
				    animate:true,
				    lines:true,
					onClick:function(node){
						var noid=(node.id);
						loadDatagrid(noid);
					},
					onLoadSuccess : function(node, data){
						$('#tt').tree('collapseAll');
					},
					onBeforeCollapse:function(node){
						if(node.id=="1"){
							return false;
						}
				    }
				}); 
				$('#dg').datagrid({
					method : 'post',
					toolbar:"#toolbarId",
					rownumbers:true,idField: 'id',striped:true,border:false,
				    checkOnSelect:true,selectOnCheck:false,singleSelect:true,
				    fitColumns:false,pagination:true,fit:true,
					columns:[[    
							  {field:'id',checkbox:'true'},
					          {field:'infoTitle',title:'标题',width:"20%"},    
					          {field:'infoKeyword',title:'关键词',width:"8%"},    
					          {field:'editorName',title:'作者',width:"8%"},    
					          {field:'infoPubtime',title:'发布时间',width:"10%"},    
					          {field:'writerName',title:'攥写人',width:"8%"},    
					          {field:'pubuserName',title:'发布人',width:"8%"},    
					          {field:'infoPubflag',title:'是否发布',width:"8%",
					        	  formatter:function(value,row,index){
					        	  if(value=="1"){
					        		  return "已发布";
					        	  }else if(value=="0"){
					        		  return "未发布";
					        	  }
					        	  return "草稿";
					          }},    
					          {field:'infoCheckFlag',title:'是否审核',width:"8%",
					        	  formatter:function(value,row,index){
					        	  if(value=="1"){
					        		  return "通过";
					        	  }else if(value=="2"){
						        	  return "退回";
					        	  }
					        	  return "未审核";
					          }},    
					          {field:'view',title:'浏览',width:"8%",align:"center",
					        	  formatter:function(value,row,index){
					        	  return "<button onclick=\"skimInformation('"+row.infoMenuid+"','"+row.id+"')\" type=\"button\" class=\"btn btn-success\">浏览</button>";
					          }},    
					          {field:'check',title:'审核',width:"8%",align:"center",
					        	  formatter:function(value,row,index){
					        		  if(row.infoCheckFlag!="0"){//已审核的显示灰色
					        			  return "<button onclick=\"\" type=\"button\" class=\"btn btn-default active\">已审核</button>";
					        		  }else{
							        	  return "<button onclick=\"checkInformation('"+row.infoMenuid+"','"+row.id+"','"+row.infoPubflag+"')\" type=\"button\" class=\"btn btn-info\">审核</button>";
					        		  }
					          }},    
					      ]],
						onBeforeLoad : function (node){
				    	  $('#dg').datagrid('uncheckAll');
				    	  $('#dg').datagrid('unselectAll');
				      }
				});
			}); 
			function jsonTimeStamp(milliseconds) {
			    if (milliseconds != "" && milliseconds != null
			            && milliseconds != "null") {
			        var datetime = new Date();
			        datetime.setTime(milliseconds);
			        var year = datetime.getFullYear();
			        var month = datetime.getMonth() + 1 < 10 ? "0"
			                + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
			        var date = datetime.getDate() < 10 ? "0" + datetime.getDate()
			                : datetime.getDate();
			        var hour = datetime.getHours() < 10 ? "0" + datetime.getHours()
			                : datetime.getHours();
			        var minute = datetime.getMinutes() < 10 ? "0"
			                + datetime.getMinutes() : datetime.getMinutes();
			        var second = datetime.getSeconds() < 10 ? "0"
			                + datetime.getSeconds() : datetime.getSeconds();
			        return year + "-" + month + "-" + date + " " + hour + ":" + minute
			                + ":" + second;
			    } else {
			        return "";
			    }
			}
			function loadDatagrid(cid){
				$("#toolbarId").show();
				var name = $('#sName').textbox('getText');
				var checkflag = $('#checkflag').combobox('getValue');
				var pubflag = $('#pubflag').combobox('getValue');
				$('#dg').datagrid({
					url:'<%=basePath%>oa/patInformation/getInfotmation.action',
					queryParams : {name :name,checkflag : checkflag,pubflag : pubflag,menuId : cid}
				});
			}	
			
			//加载标题模式窗口
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
			
			//关闭标题模式窗口
			
			//关闭窗口
			function closeLayout(){
				$('#addContent-window').window('close');
			}
			//弹出修改栏目页面
			function editContent(){
				var node= $('#dg').datagrid('getChecked');
				if(node.length > 0){
					AdddilogModel("addContent-window","栏目编辑",'<%=basePath%>oa/patMenuManager/editMenu.action?id='+node[0].id,'800px','900px');
				}else{
					$.messager.alert('提示','请先选择要修改的信息!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			 		 return;
				}

			}
			//删除
			function del(){
				var row = $('#dg').datagrid('getChecked');
				if(row==null||row.length<1||row==""){
					$.messager.alert('提示','请选择要删除的行','info');
					return ;
				}
				$.messager.confirm('确认','确定要删除勾选的文章吗？',function(r){
					if(r){
						var menuid = "";
						for(var i=0;i<row.length;i++){
							if(menuid!=""){
								menuid += ",";
							}
							menuid += row[i].id;
						}
						$.ajax({
							url : '<%=basePath%>oa/patInformation/delInformation.action',
							data: {menuId:menuid},
							success :function(r){
								reload();
								$.messager.alert('提示',r.resMsg,'info');
							},
							error : function(){
								reload()
								$.messager.alert('提示','网络繁忙,请稍后重试...','info');
							}
						});
					}
				});
			}
			//刷新
			function reload(){
				//实现刷新栏目中的数据
				$('#dg').datagrid('reload');
			}
			function add(){
				var tree = $('#tt').tree('getSelected');
				$.ajax({
					url : '<%=basePath%>oa/patInformation/judgeAuth.action',
					data : {menuId:tree.id,type:"1"},
					success : function(re){
						if(re.resCode=="success"){//进入浏览页面
							openNav('<%=basePath%>oa/patInformation/toAdd.action?menuId='+tree.id, '文章添加', 'informationaddid');
						}else{
							$.messager.alert('提示','您的权限不足!不可添加...','info');
							return ;
						}
					},
					error : function(){
						$.messager.alert('提示','网络繁忙,请稍后重试...','info');
					}
				});
			}
			function edit(){
				var tree = $('#tt').tree('getSelected');
				var row = $('#dg').datagrid('getSelected');
				if(row==null||row==""||row.length<1){
					$.messager.alert('提示','请选择要修改的信息','info');
					return ;
				}else if(row.infoCheckFlag==1){
					$.messager.alert('提示','该信息已通过审核,不可修改...','info');
					return ;
				}
				$.ajax({
					url : '<%=basePath%>oa/patInformation/judgeAuth.action',
					data : {menuId:tree.id,type:"1"},
					success : function(re){
						if(re.resCode=="success"){//进入浏览页面
							openNav("<%=basePath%>oa/patInformation/toEdit.action?menuId="+row.id, '文章修改', 'informationeditid');
						}else{
							$.messager.alert('提示','您的权限不足!不可修改...','info');
							return ;
						}
					},
					error : function(){
						$.messager.alert('提示','网络繁忙,请稍后重试...','info');
					}
				});
			}
			function skimInformation(menuCode,infoid){
				$.ajax({
					url : '<%=basePath%>oa/patInformation/judgeAuth.action',
					data : {menuId:menuCode,type:"3"},
					success : function(re){
						if(re.resCode=="success"){//进入浏览页面
							openNav("<%=basePath%>oa/patInformation/view.action?infoid="+infoid, '文章浏览', 'informationviewid');
						}else{
							$.messager.alert('提示','您没有查看权限!','info');
							return ;
						}
					},
					error : function(){
						$.messager.alert('提示','网络繁忙,请稍后重试...','info');
					}
				});
			}
			function checkInformation(menuCode,infoid,pubflag){
				if(pubflag!=1&&pubflag!=0){
					$.messager.alert('提示','草稿不可审核','info');
					return ;
				}
				$.ajax({
					url : '<%=basePath%>oa/patInformation/judgeAuth.action',
					data : {menuId:menuCode,type:"2"},
					success : function(re){
						if(re.resCode=="success"){//进入浏览页面
							openNav("<%=basePath%>oa/patInformation/toAudit.action?infoid="+infoid, '文章审核', 'informationAuditid');
						}else{
							$.messager.alert('提示','您没有审核权限!','info');
							return ;
						}
					},
					error : function(){
						$.messager.alert('提示','网络繁忙,请稍后重试...','info');
					}
				});
			}
			function refresh(){
				$('#tt').tree('reload');
			}
			function collapseAll() {//关闭树
				$('#tt').tree('collapseAll');
			}
			function expandAll() {//展开树
				$('#tt').tree('expandAll');
			}
			function reload(){
				$('#dg').datagrid('reload');
			}
			function searchFrom(){
				var node = $('#tt').tree('getSelected');
				if(node==null||node==""){
					$.messager.alert('提示','请选择栏目...','info');
					return ;
				}
				var name = $('#sName').textbox('getText');
				var checkflag = $('#checkflag').combobox('getValue');
				var pubflag = $('#pubflag').combobox('getValue');
				$('#dg').datagrid('load',{name :name,checkflag : checkflag,pubflag : pubflag,menuId : node.id});
			}
			function searchReload(){
				var node = $('#tt').tree('getSelected');
				$('#sName').textbox('setText','');
				$('#checkflag').combobox('setValue','');
				$('#pubflag').combobox('setValue','');
				loadDatagrid(node.id);
			}
</script> 
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div id="p" data-options="region:'west',title:'栏目',split:true,border:true,tools:'#toolSMId'" style="width:300px;">	
			  <ul id="tt" style="width:100%;height:60%;"></ul>  							 
			 <div id = "addData-window"></div>
			 <div id = "addContent-window"></div>
			 <div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
		</div>
		<div data-options="region:'center'" style="height:70%">	
			<div style="padding:5px 5px 0px 5px;">	        
				<table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin">
					<tr style="width:600px;">
						<td>
							<input class="easyui-textbox " id="sName" data-options="prompt:'输入标题、关键词、发布人、审核人、撰写人...'" style="width:225px"/> 
							是否审核:<input class="easyui-combobox" id="checkflag" data-options="valueField: 'label',
																								textField: 'value',
																								data: [{
																									label: '0',
																									value: '未审核'
																								},{
																									label: '1',
																									value: '通过'
																								},{
																									label: '2',
																									value: '退回'
																								}]" />
							是否发布:<input class="easyui-combobox" id="pubflag" data-options="valueField: 'label',
																								textField: 'value',
																								data: [{
																									label: '1',
																									value: '已发布'
																								},{
																									label: '0',
																									value: '未发布'
																								},{
																									label: '2',
																									value: '草稿'
																								}]" />
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
			</div>
			<div style="padding-top: 5px;height: 92%">
				  <table id="dg" style="height:auto" data-options = "{}"></table>
			</div>
			<div id="toolbarId" >
				<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
				<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
				<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
				<shiro:hasPermission name="${menuAlias}:function:delete">
					<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
		  </div>
	 </div>
</div>

</body>
</html>


