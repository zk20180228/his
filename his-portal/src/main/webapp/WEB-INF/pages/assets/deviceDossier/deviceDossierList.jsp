<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>设备档案管理</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css"/>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.tabs-header{
	z-index: 10
}
</style>
</head>
	<body style="margin: 0px;padding: 0px;">
					<div data-options="fit:true" style="width:100%;height:100%;">
								<div id="tt" class="easyui-tabs"  data-options="fit:true" >
			    					<div title="设备明细"  style="width:100%;">
			    						<div id="divLayout" class="easyui-layout" fit=true>
			    						<div data-options="region:'north',split:false,border:false,iconCls:'icon-search'" >
										<table style="padding:5px 5px">
												<tr>
												<td align="right">
													办公用途：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" name="officeName" id="officeName" data-options="prompt:'请输入办公用途'" style="width:200px"/>
												</td>
												<td>
													设备分类：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" name="className" id="className" data-options="prompt:'请输入设备分类'" style="width:200px"/>
												</td>
												<td>
													类别代码：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" name="classCode" id="classCode" data-options="prompt:'请输入类别代码'" style="width:200px"/>
												</td>
												<td>
													设备名称：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" name="deviceName" id="deviceName" data-options="prompt:'请输入设备名称'" style="width:200px"/>
												</td>
												<td>
													条码号：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" name="deviceNo" id="deviceNo" data-options="prompt:'请输入条码号'" style="width:200px"/>
												</td>
												<td>
													<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
													<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
												</td>
											</tr>
										</table>
										</div>
										<div data-options="region:'center',split:false,border:false" style=" width: 100%;height: 95%;">
				    						 <table id="assetTpye" class="easyui-datagrid" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true,toolbar:'#toolbarId'">
												<thead>
													<tr>
														<th style="width: 10%;" data-options="field:'officeName'" align="center">办公用途</th>
														<th style="width: 10%;" data-options="field:'classCode'" align="center">类别代码</th>
														<th style="width: 10%;" data-options="field:'className'" align="center">设备分类</th>
														<th style="width: 10%;" data-options="field:'deviceName'" align="center">设备名称</th>
														<th style="width: 10%;" data-options="field:'meterUnit'" align="center">计量单位</th>
														<th style="width: 10%;" data-options="field:'purchPrice',formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="right">采购单价（元）</th>
														<th style="width: 10%;" data-options="field:'deviceNo'" align="center">条码号</th>
														<th style="width: 10%;" data-options="field:'useDeptName'" align="center">领用部门</th>
														<th style="width: 10%;" data-options="field:'useName'" align="center">领用人</th>
													</tr>
												</thead>
											</table> 
										</div>
										</div>
			   						</div>   
			    					<div title="设备资产"  style="width:100%;">
			    					<div id="divLayout2" class="easyui-layout" fit=true>
			    						<div data-options="region:'north',split:false,border:false,iconCls:'icon-search'" >
											<table style="padding:5px 5px">
												<tr>
													<td align="right">
														办公用途：
													</td>
													<td style="width:210px;">
														<input class="easyui-textbox" name="officeName" id="officeName2" data-options="prompt:'请输入办公用途'" style="width:200px"/>
													</td>
													<td>
														设备分类：
													</td>
													<td style="width:210px;">
														<input class="easyui-textbox" name="className" id="className2" data-options="prompt:'请输入设备分类'" style="width:200px"/>
													</td>
													<td>
														类别代码：
													</td>
													<td style="width:210px;">
														<input class="easyui-textbox" name="classCode" id="classCode2" data-options="prompt:'请输入类别代码'" style="width:200px"/>
													</td>
													<td>
														设备名称：
													</td>
													<td style="width:210px;">
														<input class="easyui-textbox" name="deviceName" id="deviceName2" data-options="prompt:'请输入设备名称'" style="width:200px"/>
													</td>
													<td>
														条码号：
													</td>
													<td style="width:210px;">
														<input class="easyui-textbox" name="deviceNo" id="deviceNo2" data-options="prompt:'请输入条码号'" style="width:200px"/>
													</td>
													<td>
														<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
														<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
													</td>
												</tr>
											</table>
										</div>
										<div data-options="region:'center',split:false,border:false" style=" width: 100%;height: 95%;">
			    						 <table id="assetValue" class="easyui-datagrid" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
											<thead>
												<tr>
													<th style="width: 10%;" data-options="field:'officeName'" align="center">办公用途</th>
													<th style="width: 7%;" data-options="field:'classCode'" align="center">类别代码</th>
													<th style="width: 8%;" data-options="field:'className'" align="center">设备分类</th>
													<th style="width: 10%;" data-options="field:'deviceName'" align="center">设备名称</th>
													<th style="width: 7%;" data-options="field:'meterUnit'" align="center">计量单位</th>
													<th style="width: 8%;" data-options="field:'purchPrice',formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="right">采购单价（元）</th>
													<th style="width: 7%;" data-options="field:'deviceNum'" align="right">采购数量</th>
													<th style="width: 8%;" data-options="field:'tranCost',formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="right">运费（元）</th>
													<th style="width: 8%;" data-options="field:'instCost',formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="right">安装费（元）</th>
													<th style="width: 8%;" data-options="field:'purchTotal',formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="right">采购总价（元）</th>
													<th style="width: 7%;" data-options="field:'depreciation'" align="right">折旧年限</th>
													<th style="width: 8%;" data-options="field:'newValue', formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="right">设备现值（元）</th>
												</tr>
											</thead>
										</table>    
			   						</div>
			   						</div>
			   					</div> 
							</div> 
					</div>
					<div id="toolbarId">
					<shiro:hasPermission name="${menuAlias}:function:edit">
						<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
					</shiro:hasPermission>
						<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
					</div>
<script type="text/javascript">
var type=1;
//加载页面
$(function(){
	//资产分类
	 $("#assetTpye").datagrid({
			url:'<%=basePath %>assets/deviceDossier/queryAssetsDeviceDossier.action',
			queryParams:{
				officeName:$('#officeName').textbox("getText"),
				className:$('#className').textbox("getText"),
				classCode:$('#classCode').textbox("getText"),
				deviceName:$('#deviceName').textbox("getText"),
				deviceNo:$('#deviceNo').textbox("getText")
				},
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,100],
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

	$('#tt').tabs({
		onSelect: function(title,index){
			if(title=="设备明细"){
				type=1;
			}else{
				type=2;
				closeLayout();
				 $("#assetValue").datagrid({
					 	url: '<%=basePath %>assets/deviceDossier/queryDeviceDossierValue.action',
					 	queryParams:{
					 		officeName:$('#officeName2').textbox("getText"),
							className:$('#className2').textbox("getText"),
							classCode:$('#classCode2').textbox("getText"),
							deviceName:$('#deviceName2').textbox("getText"),
							deviceNo:$('#deviceNo2').textbox("getText")
					 		},
				 		pagination:true,
						pageSize:20,
						pageList:[20,30,50,100],
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
			}
		}
	});
})	

	function query(){
		$.messager.progress({text:'查询中，请稍后...',modal:true});
		if(type==1){
			 $("#assetTpye").datagrid('load',{
				 	officeName:$('#officeName').textbox("getText"),
					className:$('#className').textbox("getText"),
					classCode:$('#classCode').textbox("getText"),
					deviceName:$('#deviceName').textbox("getText"),
					deviceNo:$('#deviceNo').textbox("getText")
					}
			 );
		}else{
			 $("#assetValue").datagrid('load',{
				 	officeName:$('#officeName2').textbox("getText"),
					className:$('#className2').textbox("getText"),
					classCode:$('#classCode2').textbox("getText"),
					deviceName:$('#deviceName2').textbox("getText"),
					deviceNo:$('#deviceNo2').textbox("getText")
				 }
			 );
		}
		 $.messager.progress('close');
	}

	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-21
	 * @version 1.0
	 */
	function clears(){
		if(type==1){
			$("#officeName").textbox("setValue","");
			$("#className").textbox("setValue","");
			$("#classCode").textbox("setValue","");
			$("#deviceName").textbox("setValue","");
			$("#deviceNo").textbox("setValue","");
		}else{
			$("#officeName2").textbox("setValue","");
			$("#className2").textbox("setValue","");
			$("#classCode2").textbox("setValue","");
			$("#deviceName2").textbox("setValue","");
			$("#deviceNo2").textbox("setValue","");
		}
		query();
	}
  //修改
	function edit(){
		var rows = $('#assetTpye').datagrid('getSelected');
		if (rows != null && rows.length != 0) {
			closeLayout();
			AddOrShowEast('EditForm', '<%=basePath %>assets/deviceDossier/updateDeviceDossierUrl.action?id=' + rows.id);
		}else{
			$.messager.alert('操作提示',"请选择要修改的信息！");
			close_alert();
		}
	}
	//关闭Layout
	function closeLayout(){
		$('#divLayout').layout('remove', 'east');
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
		//获取右侧收缩面板
		var eastpanel=$('#panelEast'); 
		//判断右侧收缩面板是否存在
		if(eastpanel.length>0){
			//重新装载右侧面板
	   		$('#divLayout').layout('panel','east').panel({
                      href:url 
               });
		}else{
			$('#divLayout').layout('add', {
				region : 'east',
				width : 580,
				split : true,
				border : false,
				href : url,
				closable : true
			});
		}
			
	}
	//刷新
	function reload(){
		$('#assetTpye').datagrid('reload');
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>