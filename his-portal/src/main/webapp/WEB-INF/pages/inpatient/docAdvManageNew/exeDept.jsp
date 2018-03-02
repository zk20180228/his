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
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css"/>
</head>
<body>
	<div id="cffc" class="easyui-layout" data-options="fit:true";>   
	    <div data-options="region:'north'" style="height:10%;width: 100%">
	    	<table style="width: 100%; border: 1px solid #95b8e7; padding: 5px 5px 5px 5px;">
				<tr>
					<td width="60px" align="right">科室类别：</td>
					<td width="100px">
		    			<input id="departmentType" class="easyui-combobox" style="width:140px;" data-options="    
								       valueField: 'id',    
								       textField: 'text',    									      
								       data: [{
											id: 'C',
											text: '门诊'
										},{
											id: 'I',
											text: '住院'
										},{
											id: 'F',
											text: '财务'
										},{
											id: 'L',
											text: '后勤'
										},{
											id: 'PI',
											text: '药库'
										},{
											id: 'T',
											text: '医技(终端)'
										},{
											id: '0',
											text: '其它'
										},{
											id: 'D',
											text: '机关(部门)'
										}
										,{
											id: 'P',
											text: '药房'
										}
										,{
											id: 'N',
											text: '护士站'
										}
										,{
											id: 'S',
											text: '科研'									
										}
										,{
											id: '',
											text: '全部',
											'selected':true
										}] 
									 " />   
					</td>
					<td style="width:430px;">科室名称：&nbsp;&nbsp;
				 		<input id="departmentInfoViewNameId" type="text" style="width: 190px;" value="${inpatientOrder.execDpnm}"/>					 	
				 		&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchInfos()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				 		&nbsp;&nbsp;双击选择科室
					</td>
				</tr>
			</table>
	    </div>   
	    <div data-options="region:'center'" style="width: 100%;height: 90%">
	    	<table id="deptiInfoViewListId" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
				<thead>
					<tr>
						<th data-options="field:'id',hidden:true" style="">部门Id</th>
						<th data-options="field:'deptName'" style="">部门名称</th>
						<th data-options="field:'deptBrev'" style="" >部门简称 </th>
						<th data-options="field:'deptEname'" style="">部门英文名</th>							
						<th data-options="field:'deptType'" style="">部门分类</th>																					
					</tr>
				</thead>
			</table>
	    </div>   
	</div> 
		<script type="text/javascript">
			
			$(function(){
				
			    $('#departmentInfoViewNameId').textbox({
			        prompt:'名称,拼音,五笔码,自定义码'
			    });				
				$('#deptiInfoViewListId').datagrid({	
					pagination:true,	
					pageSize:20,
					pageList:[20,30,50,80,100],
					url:'<%=basePath%>inpatient/docAdvManage/querySysDepartment.action',
					queryParams:{'departmentSerch.deptName':$('#departmentInfoViewNameId').val()},							
					onDblClickRow:function(rowIndex, rowData){						
					    	var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){
								var index = getIndexForAdDgListA();
								var row = $('#infolistA').datagrid('getSelected');
								if(row!=null&&row.combNo!=null&&row.combNo!=''){
									var rows = $('#infolistA').datagrid('getRows');
									for(var i=0;i<rows.length;i++){
										if(rows[i].combNo==row.combNo){
											var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
											$('#infolistA').datagrid('updateRow',{
												index: indexRow,
												row: {
													execDpcd:rowData.id,
													execDpnm:rowData.deptName,
													changeNo:1
												}
											});
										}
									}
								}else{
									if(index>=0){
										$('#infolistA').datagrid('updateRow',{
											index: index,
											row: {
												execDpcd:rowData.id,
												execDpnm:rowData.deptName,
												changeNo:1
											}
										});
									}
								}
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();
								var row = $('#infolistB').datagrid('getSelected');
								if(row!=null&&row.combNo!=null&&row.combNo!=''){
									var rows = $('#infolistB').datagrid('getRows');
									for(var i=0;i<rows.length;i++){
										if(rows[i].combNo==row.combNo){
											var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
											$('#infolistB').datagrid('updateRow',{
												index: indexRow,
												row: {
													execDpcd:rowData.id,
													execDpnm:rowData.deptName,
													changeNo:1
												}
											});
										}
									}
								}else{
									if(index>=0){
										$('#infolistB').datagrid('updateRow',{
											index: index,
											row: {
												execDpcd:rowData.id,
												execDpnm:rowData.deptName,
												changeNo:1
											}
										});
									}
								}
							}	
						if($('#adProjectTdId') .combobox('getValue')=='18'){
							$('#adProjectNameTdId').textbox('setValue',rowData.deptName+'转科');
							$('#adProjectNumTdId').numberspinner('setValue',1);						
							$('#adProjectUnitTdId').combobox('setValue','次');
						}							
						$('#adExeDeptTdId').textbox('setValue',rowData.deptName);
						$('#adExeDeptTd').val(rowData.id);
						$('#adExeDeptModlDivId').dialog('close');
					}
				});
				
				$('#departmentType').combobox({  
					onSelect:function(record){				
						$('#deptiInfoViewListId').datagrid('load', {
							'departmentSerch.deptType':record.id,
							'departmentSerch.deptName':$('#departmentInfoViewNameId').val()
						});					
					}
				}); 
				bindEnterEvent('departmentInfoViewNameId',searchInfos,'easyui');
			});
			
			function searchInfos(){
				$('#deptiInfoViewListId').datagrid('load', {
					'departmentSerch.deptType' : $("#departmentType").combobox('getValue'),
					'departmentSerch.deptName' : $('#departmentInfoViewNameId').val()
				});
			}
	
		</script>
	</body>
</html>
