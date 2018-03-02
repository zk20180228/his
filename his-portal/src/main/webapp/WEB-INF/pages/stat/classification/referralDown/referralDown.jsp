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
<style>
	.tableCss td{
		padding:5px 6px;
	}
</style>
<script type="text/javascript">
	$(function(){
		$('#deptSQ').combobox({   
			url:'<%=basePath%>publics/consultation/queryDeptList.action?menuAlias=${menuAlias}',  
		    valueField:'deptCode',    
		    textField:'deptName',
		    multiple:false,
		    editable:true,
		    required:true,
		    mode:'remote',
		});
		$('#cnslDoccd') .combobox({    
		    url:'<%=basePath%>publics/consultation/queryLoginUserDept.action',    
		    valueField:'jobNo',    
		    textField:'name',
		    multiple:false,
		    editable:true,
		    required:true,
		    mode:'remote',
		    onLoadSuccess:function(){
		    	var code=$('#cnslDoccd').combobox('getValue');	
				var data = $('#cnslDoccd').combobox('getData');
				for(var i=0;i<data.length;i++){
					if(data[i].jobNo==code){
						$('#cnslDoccd').combobox('setValue',data[i].jobNo);
					}
				    }
		           }
	    });
	})		
</script>
</head>
<body>
<div id="divLayout"  class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%;">
	<div data-options="region:'west',border:true"  title="申请转诊患者列表" style="width:30%;height:100%">
			<div style="width: 100%;height: 35px;padding-top: 5px;">
				<div style="margin-left: 10px;">
					<input id="cc" class="easyui-textbox"    
 							 data-options="prompt:'姓名,住院流水号,病历号,科室名'"  style="width:35%"/>  
   					&nbsp;<input id="combo" class="easyui-combobox"    
 							 style="width:25%;margin-left:10px" data-options="valueField:'id',textField:'text',data: [{id: '1',text: '全部',selected:true },{id: '2',text: '姓名'},{id: '3',text: '住院流水号'},{id: '4',text: '病历号'},{id: '5',text: '科室名'}]" />  
					&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				</div>
			</div>
		<table class="easyui-datagrid" data-options="fitColumns:true,singleSelect:true" style="width:100%;">   
	    	<thead>   
	        	<tr>   
	            	<th data-options="field:'name',width:'20%'">患者姓名</th>   
	            	<th data-options="field:'time1',width:'20%'">住院流水号/门诊号</th>   
	            	<th data-options="field:'time2',width:'20%'">科室</th>  
	            	<th data-options="field:'zengJian',width:'20%'">转归状态</th> 
	            	<th data-options="field:'zengJianPer',width:'20%'">申请状态</th>  
	        	</tr>   
	    	</thead>   

	    </table>
	</div>
	<div data-options="region:'center',border:false" style="height:100%">
		<form style="width: 100%;height:100%">
	    		<table  class="honry-table" id="nuilfcheditTable" cellpadding="1" cellspacing="1" border="1px solid black"  style="width: 100%;height:100%">
  				<tr>
		    		<td align="center" colspan="6" style="border-top:0 !important;"><font size="6" class="empWorkTit">向下分诊申请</font></td>
		    	</tr>
<!-- 		    	<tr> -->
<!-- 		    		<td class="tableLabel">主转机构：</td> -->
<!-- 					<td colspan="3"><input id="name" name="name"  class="easyui-textbox" ></td> -->
<!-- 					<td class="tableLabel">分诊机构：</td> -->
<!-- 					<td colspan="3"><input id="name" name="name"  class="easyui-textbox" ></input></td> -->
<!-- 					<td colspan="2"><input type="checkbox" /> 按地域选择</td> -->
<!-- 		    	</tr> -->
<!-- 		    	<tr> -->
<!-- 		    		<td class="tableLabel" style="width:12%">住院流水号/门诊号：</td> -->
<!-- 					<td style="width:10%"></td> -->
<!-- 					<td class="tableLabel" style="width:10%">患者姓名：</td> -->
<!-- 					<td style="width:10%"></td> -->
<!-- 					<td class="tableLabel" style="width:9%">性别：</td> -->
<!-- 					<td  style="width:10%"></td> -->
<!-- 					<td class="tableLabel" style="width:9%">年龄：</td> -->
<!-- 					<td style="width:10%" ></td> -->
<!-- 					<td class="tableLabel" style="width:10%">出生日期：</td> -->
<!-- 					<td  style="width:10%"></td> -->
<!-- 		    	</tr> -->
<!-- 		    	<tr> -->
<!-- 		    		<td class="tableLabel" style="width:12%">诊断：</td> -->
<!-- 					<td colspan="9"><input class="easyui-textbox" style="width: 86%; height: 100px"/></td> -->
<!-- 		    	</tr> -->
<!-- 		    	<tr> -->
<!-- 		    		<td class="tableLabel" style="width:12%">病情描述：</td> -->
<!-- 					<td colspan="9"><input class="easyui-textbox" style="width: 86%; height: 150px"/></td> -->
<!-- 		    	</tr> -->
<!-- 		    	<tr> -->
<!-- 		    		<td class="tableLabel" style="width:12%">备注：</td> -->
<!-- 					<td colspan="9"><input class="easyui-textbox" style="width: 86%; height: 100px"/></td> -->
<!-- 		    	</tr> -->
				<tr>
					<td class="honry-lable" style="height:55px">转诊医疗机构：</td>
	    			<td   colspan = "3"><input id="name" name="name"  class="easyui-combobox" >
	    			&nbsp; <label><input type="checkbox" /> 按地域选择</label>
	    			</td>
	    			<td class="honry-lable">申请机构：</td>
	    			<td  ><input id="name" name="name"  class="easyui-textbox" ></input></input></td>
	    			
   				</tr>
				<tr>
					<td class="honry-lable" style="height:55px">住院流水号/门诊号:</td>
	    			<td >
	    				<input id="patientName" class="easyui-textbox"  readonly="readonly" />
	    			</td>
					<td class="honry-lable" >患者姓名:</td>
<!-- 		    			<td ><input id="name" name="name"   type="text" style="border: 0; width: 70px" ></input></td> -->
	    			<td >
	    				<input id="patientName" class="easyui-textbox"  readonly="readonly" />
	    			</td>
					<td class="honry-lable" >性别:</td>
					<td ><input id="sexCombobox" class="easyui-combobox" name="inpatientInfo.reportSex" data-options="url :'<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ',valueField:'encode',textField:'name'"  />
					</td>
   				</tr>
   				<tr>
   					<td class="honry-lable" style="height:55px">年龄:</td>
	    			<td >
	    				<input id="reportAge" class="easyui-textbox"  readonly="readonly"/>
	    				<span id="reportAgeunit" readonly="readonly"></span>
					</td>
					<td class="honry-lable" >出生日期:</td>
	    			<td  colspan = "3"><input id="moStdt" name="moStdt" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/></td>
   				</tr>
   				<tr>
   					<td class="honry-lable">诊断：</td>
					<td colspan = "5"> 
							<input  id="cnslNote" name="cnslNote" class="easyui-textbox" data-options="multiline:true"  style="width:75%;height:100px">
					</td>
   				</tr>
   				<tr>
   					<td class="honry-lable">病情描述：</td>
					<td colspan = "5"> 
							<input  id="cnslNote" name="cnslNote" class="easyui-textbox" data-options="multiline:true"  style="width:75%;height:100px">
					</td>
   				</tr>
   				<tr>
   					<td class="honry-lable">备注：</td>
					<td colspan = "5"> 
						<input  id="cnslNote" name="cnslNote" class="easyui-textbox" data-options="multiline:true"  style="width:75%;height:100px">
					</td>
   				</tr>
		    	<tr>
		    		<td colspan="6"><label><input type="checkbox" /> 随带病案首页</label>&nbsp;&nbsp;<label><input type="checkbox" /> 附带电子病历</label>&nbsp;&nbsp;<label><input type="checkbox" /> 附带医嘱信息</label>&nbsp;&nbsp;<label><input type="checkbox" /> 附带生命体征信息<label></td>
		    	</tr>
		    	<tr>
					<td class="honry-lable" style="height:55px">申请科室:</td>
	    			<td ><input id="deptSQ"  name="deptCode" class="easyui-combobox" data-options="required:true"/></td>
					<td class="honry-lable" >申请医生:</td>
	    			<td ><input id="cnslDoccd"  name="cnslDoccd" class="easyui-combobox" data-options="required:true"/></td>
					<td class="honry-lable" >联系方式:</td>
	    			<td ><input id="patientName" class="easyui-textbox"  readonly="readonly" /></td>
   				</tr>
   				<tr>
   					<td class="honry-lable" style="height:55px">申请时间:</td>
	    			<td  colspan = "5"><input id="moStdt" name="moStdt" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/></td>
   				</tr>
		    	<tr>
		    		<td colspan="10" height="50" align="center">
		    			<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()" iconCls="icon-save" style="height:30px;margin-top:3px;padding:0 3px;">确认</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset" style="height:31px;margin-top:3px;padding:0 3px">重置</a>
					</shiro:hasPermission></td>
		    	</tr>
		    	
		    </table>
		</form>
	</div>
</div>
</body>
</html>