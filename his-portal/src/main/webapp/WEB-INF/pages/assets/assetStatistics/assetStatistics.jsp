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
<title>资产统计分析管理</title>
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
								<div id="tt" class="easyui-tabs" data-options="fit:true" >
			    					<div title="资产分类"  style="width:100%;">
										<table style="padding:5px 5px">
											<tr>
												<td align="right">
													办公用途：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" id="officeName" data-options="prompt:'请输入办公用途'" style="width:200px"/>
												</td>
												<td>
													设备分类：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" id="className" data-options="prompt:'请输入设备分类'" style="width:200px"/>
												</td>
												<td>
													类别代码：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" id="classCode" data-options="prompt:'请输入类别代码'" style="width:200px"/>
												</td>
												<td>
													设备名称：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" id="deviceName" data-options="prompt:'请输入设备名称'" style="width:200px"/>
												</td>
												<td>
													<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
													<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
												</td>
											</tr>
										</table>
										<div style="width: 100%;height: 90%;">
				    						 <table id="assetTpye" class="easyui-datagrid" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
												<thead>
													<tr>
														<th style="width: 14%;" data-options="field:'officeName'" align="center">办公用途</th>
														<th style="width: 14%;" data-options="field:'classCode'" align="center">类别代码</th>
														<th style="width: 14%;" data-options="field:'className'" align="center">设备分类</th>
														<th style="width: 14%;" data-options="field:'deviceName'" align="center">设备名称</th>
														<th style="width: 14%;" data-options="field:'meterUnit'" align="center">计量单位</th>
														<th style="width: 14%;" data-options="field:'deviceNum'" align="center">数量</th>
													</tr>
												</thead>
											</table> 
										</div>
										<table>
				   							<tr>
				   								<td style="padding:5px 5px">总计:<span id='totalType'></span></td>
				   							</tr> 
			   						 	</table> 
			   						</div>   
			    					<div title="领用部门"  style="width:100%;">   
										<table style="padding:5px 5px">
											<tr>
											<td style="width: 50px;">部门:</td>
										    <td  class="newMenu" style="width:160px;z-index:1;position: relative;">
													<div class="deptInput menuInput" style="margin-top:0px;">
														<input id="deptName" class="ksnew"  readonly/>
														<span></span>
													</div>
													<div id="m3" class="xmenu" style="display: none;">
														<div class="searchDept">
															<input type="text" name="searchByDeptName" placeholder="回车查询"/>
															<span class="searchMenu"><i></i>查询</span>
															<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
																<span class="a-btn-text">取消</span>
															</a>						
															<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
																<span class="a-btn-text">清空</span>
															</a>
															<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
																<span class="a-btn-text">确定</span>
															</a>
														</div>
														<div class="select-info" style="display:none">
															<label class="top-label">已选部门：</label>
															<ul class="addDept">
															</ul>
														</div>	
														<div class="depts-dl">
															<div class="addList">
															</div>
															<div class="tip" style="display:none">没有检索到数据</div>
														</div>	
													</div>
								    			</td>
												<td>
													<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
													<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
												</td>
											</tr>
										</table>
										<div style="width: 100%;height: 90%;">
											<table id="assetUse" class="easyui-datagrid" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
												<thead>
													<tr>
														<th style="width: 14%;" data-options="field:'createDept'" align="center">领用部门</th>
														<th style="width: 14%;" data-options="field:'createUser'" align="center">领用人</th>
														<th style="width: 14%;" data-options="field:'useDate'" align="center">领用时间</th>
														<th style="width: 14%;" data-options="field:'classCode'" align="center">类别代码</th>
														<th style="width: 14%;" data-options="field:'deviceName'" align="center">设备名称</th>
														<th style="width: 14%;" data-options="field:'deviceNo'" align="center">条码编号</th>
														<th style="width: 14%;" data-options="field:'meterUnit'" align="center">计量单位</th>
													</tr>
												</thead> 
											</table>
										</div>
										<table>
				   							<tr>
				   								<td style="padding:5px 5px">总计:<span id='total'></span></td>
				   							</tr> 
			   						 	</table> 
			    					</div>
			    					<div title="资产价值"  style="width:100%;">
										<table style="padding:5px 5px">
											<tr>
												<td align="right">
													办公用途：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" id="officeName2" data-options="prompt:'请输入办公用途'" style="width:200px"/>
												</td>
												<td>
													设备分类：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" id="className2" data-options="prompt:'请输入设备分类'" style="width:200px"/>
												</td>
												<td>
													类别代码：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" id="classCode2" data-options="prompt:'请输入类别代码'" style="width:200px"/>
												</td>
												<td>
													设备名称：
												</td>
												<td style="width:210px;">
													<input class="easyui-textbox" id="deviceName2" data-options="prompt:'请输入设备名称'" style="width:200px"/>
												</td>
												<td>
													<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
													<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
												</td>
											</tr>
										</table>
										<div style="width: 100%;height: 90%;">
			    						 <table id="assetValue" class="easyui-datagrid" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
											<thead>
												<tr>
													<th style="width: 10%;" data-options="field:'officeName'" align="center">办公用途</th>
													<th style="width: 7%;" data-options="field:'classCode'" align="center">类别代码</th>
													<th style="width: 8%;" data-options="field:'className'" align="center">设备分类</th>
													<th style="width: 10%;" data-options="field:'deviceName'" align="center">设备名称</th>
													<th style="width: 7%;" data-options="field:'meterUnit'" align="center">计量单位</th>
													<th style="width: 8%;" data-options="field:'purchPrice'" align="center">采购单价（元）</th>
													<th style="width: 7%;" data-options="field:'deviceNum'" align="center">采购数量</th>
													<th style="width: 8%;" data-options="field:'tranCost',formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="center">运费（元）</th>
													<th style="width: 8%;" data-options="field:'instCost',formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="center">安装费（元）</th>
													<th style="width: 8%;" data-options="field:'purchTotal',formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="center">采购总价（元）</th>
													<th style="width: 7%;" data-options="field:'depreciation'" align="center">折旧年限</th>
													<th style="width: 8%;" data-options="field:'newValue', formatter:function(val,rowData,rowIndex){ if(val!=null) return val.toFixed(2);}" align="center">设备现值（元）</th>
												</tr>
											</thead>
										</table>    
			   						</div>
			   						 <table>
			   							<tr>
			   								<td style="width:300px;padding:5px 5px">资产原值:<span id='usedValue'></span></td>
			   								<td style="width:300px;padding:5px 5px">资产现值:<span id='newValue'></span></td>
			   							</tr> 
			   						 </table> 
			   					</div> 
							</div> 
					</div>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
//加载页面
$(function(){
		//资产分类
			 $("#assetTpye").datagrid({
					url:'<%=basePath %>statistics/assetStatistics/querylistAssetsDevice.action',
					queryParams:{
						officeName:$('#officeName').textbox("getText"),
						className:$('#className').textbox("getText"),
						classCode:$('#classCode').textbox("getText"),
						deviceName:$('#deviceName').textbox("getText")
						},
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,100],
					onLoadSuccess: function(data){
						document .getElementById ("totalType").innerHTML=data.totalType
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
			if(title=="资产分类"){
				
			}else if(title=="领用部门"){
				//科室下拉
				$(".deptInput").MenuList({
					width :530, //设置宽度，不写默认为530，不要加单位
					height :400, //设置高度，不写默认为400，不要加单位
					menulines:2, //设置菜单每行显示几列（1-5），默认为2
					dropmenu:"#m3",//弹出层id，必须要写
					isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
					haveThreeLevel:false,//是否有三级菜单
					para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
					firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
				});
			  $("#assetUse").datagrid({
				  	url: '<%=basePath %>statistics/assetStatistics/queryAssetsDeviceUsec.action',
				  	queryParams:{
				  		officeName:$('#deptName').getMenuIds(),
				  		},
			  		pagination:true,
					pageSize:20,
					pageList:[20,30,50,100],
					onLoadSuccess: function(data){
						document .getElementById ("total").innerHTML=data.total
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
			}else{
				 $("#assetValue").datagrid({
					 	url: '<%=basePath %>statistics/assetStatistics/queryAssetsDeviceValue.action',
					 	queryParams:{
					 		officeName:$('#officeName2').textbox("getText"),
							className:$('#className2').textbox("getText"),
							classCode:$('#classCode2').textbox("getText"),
							deviceName:$('#deviceName2').textbox("getText")
					 		},
				 		pagination:true,
						pageSize:20,
						pageList:[20,30,50,100],
						onLoadSuccess: function(data){
							document .getElementById ("usedValue").innerHTML=data.used+"元";
							document .getElementById ("newValue").innerHTML=data.newV+"元";
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
		 $("#assetTpye").datagrid('load',{
			 	officeName:$('#officeName').textbox("getText"),
				className:$('#className').textbox("getText"),
				classCode:$('#classCode').textbox("getText"),
				deviceName:$('#deviceName').textbox("getText")
				}
		 );
		 $("#assetUse").datagrid('load',{officeName:$('#deptName').getMenuIds(),});
		 $("#assetValue").datagrid('load',{
			 	officeName:$('#officeName2').textbox("getText"),
				className:$('#className2').textbox("getText"),
				classCode:$('#classCode2').textbox("getText"),
				deviceName:$('#deviceName2').textbox("getText")
			 }
		 );
		 $.messager.progress('close');
	}

	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-21
	 * @version 1.0
	 */
	function clears(){
		$("#officeName").textbox("setValue","");
		$("#className").textbox("setValue","");
		$("#classCode").textbox("setValue","");
		$("#deviceName").textbox("setValue","");
		$("#officeName2").textbox("setValue","");
		$("#className2").textbox("setValue","");
		$("#classCode2").textbox("setValue","");
		$("#deviceName2").textbox("setValue","");
		$('#deptName').val('');
		$('#deptName').attr('name','');
		query();
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>