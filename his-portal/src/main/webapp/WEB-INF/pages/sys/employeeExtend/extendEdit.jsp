<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<body>
	<div id="cc" class="easyui-layout" fit="true">   
	 <div style="width:100%">
			<form id="editForm" method="post" enctype="multipart/form-data">
				<input type="hidden" id="id" name="extend.id" value="${extend.id }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0"  style="width:100%;padding:5px">
					<tr>
						<td class="honry-lable">
							<span>所属部门：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="inputCss"  id="department"  value="${extend.department}" name="extend.department" required="true" placeHolder="回车选择部门"></input>
							<input id="deptCode" name="extend.deptCode" value="${extend.deptCode}"  type="hidden">
							<a href="javascript:delDatas('departmentCode','department','deptCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable">
							<span>部门编号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="departmentCode" class="easyui-textbox" value="${extend.departmentCode}" name="extend.departmentCode" data-options="editable:false"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>学部：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="division" class="easyui-textbox"  value="${extend.division}" name="extend.division"></input>
						</td>
						<td class="honry-lable">
							<span>学部编号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="divisionCode" class="easyui-textbox" value="${extend.divisionCode}" name="extend.divisionCode" ></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>总支：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="generalbranch" class="easyui-textbox" value="${extend.generalbranch}" name="extend.generalbranch"  ></input>
						</td>
						<td class="honry-lable">
							<span>总支编号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="generalbranchCode" class="easyui-textbox" value="${extend.generalbranchCode}" name="extend.generalbranchCode"  ></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>工号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="employeeJobNo" class="easyui-textbox" value="${extend.employeeJobNo}" name="extend.employeeJobNo" data-options="required:true" ></input>
						</td>
						<td class="honry-lable">
							<span>姓名：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="employeeName" class="easyui-textbox" value="${extend.employeeName}" name="extend.employeeName" data-options="required:true" ></input>
						</td>
					</tr>
					<tr>
				  		<td class="honry-lable">
							<span>性别：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="employeeSexName" class="easyui-combobox" value="${extend.employeeSexName}" name="extend.employeeSexName"></input>
							<input id="employeeSexCode" type="hidden" value="${extend.employeeSexCode}" name="extend.employeeSexCode"></input>
							<a href="javascript:delData('employeeSexName',['employeeSexCode']);"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable">
							<span>证件号码：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="employeeIdentityCard" class="easyui-textbox" value="${extend.employeeIdentityCard}" name="extend.employeeIdentityCard" data-options="validType:'idEntityCard'"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>出生日期：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="employeeBirthday" value="${extend.employeeBirthday}" name="extend.employeeBirthday" class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<td class="honry-lable">
							<span>年龄：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="employeeAge" class="easyui-numberbox" value="${extend.employeeAge}" name="extend.employeeAge" data-options="min:1,max:100,precision:0" ></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>人员职称：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="titleName" class="easyui-combobox"  value="${extend.titleName}" name="extend.titleName"></input>
							<input id="titleCode" name="extend.titleCode" value="${extend.titleCode}"  type="hidden">
							<input id="titleType" name="extend.titleType" value="${extend.titleType}"  type="hidden">
							<input id="titleLevel" name="extend.titleLevel" value="${extend.titleLevel}"  type="hidden">
							<input id="titleTypeName" name="extend.titleTypeName" value="${extend.titleTypeName}"  type="hidden">
							<a href="javascript:delData('titleName',['titleCode','titleType','titleLevel','titleTypeName']);"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable">
							<span>人员职务：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="dutiesName" class="easyui-combobox"  value="${extend.dutiesName}" name="extend.dutiesName"></input>
							<input id="dutiesCode" name="extend.dutiesCode" value="${extend.dutiesCode}"  type="hidden">
							<input id="dutiesType" name="extend.dutiesType" value="${extend.dutiesType}"  type="hidden">
<%-- 							<input id="dutiesLevel" name="extend.dutiesLevel" value="${extend.dutiesLevel}"  type="hidden"> --%>
							<input id="dutiesTypeName" name="extend.dutiesTypeName" value="${extend.dutiesTypeName}"  type="hidden">
							<a href="javascript:delData('dutiesName',['dutiesCode','dutiesType','dutiesTypeName']);"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>人员类型：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="employeeTypeName" class="easyui-combobox"  value="${extend.employeeTypeName}" name="extend.employeeTypeName"></input>
							<input id="employeeType" name="extend.employeeType" value="${extend.employeeType}"  type="hidden">
							<a href="javascript:delData('titleName',['employeeType','employeeTypeName']);"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable">
							<span>人员级别：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="dutiesLevel" class="easyui-combobox"  value="${extend.dutiesLevel}" name="extend.dutiesLevel" ></input>
<!-- 							<a href="javascript:delData('dutiesLevel');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> -->
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>民族：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="nationalName" class="easyui-combobox"  value="${extend.nationalName}" name="extend.nationalName" ></input>
							<input id="nationalCode" name="extend.nationalCode" value="${extend.nationalCode}"  type="hidden">
							<a href="javascript:delData('nationalName',['nationalCode']);"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable">
							<span>编制类型：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="organizationName" class="easyui-combobox"  value="${extend.organizationName}" name="extend.organizationName" ></input>
							<input id="organizationCode" name="extend.organizationCode" value="${extend.organizationCode}"  type="hidden">
							<a href="javascript:delData('organizationName',['organizationCode']);"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
				  		<td class="honry-lable">
							<span>政治面貌：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="politicalstatusName" class="easyui-combobox"  value="${extend.politicalstatusName}" name="extend.politicalstatusName" ></input>
							<input id="politicalstatusCode" name="extend.politicalstatusCode" value="${extend.politicalstatusCode}"  type="hidden">
							<a href="javascript:delData('politicalstatusName',['politicalstatusCode']);"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable">
							<span>手机号码：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id="employeeMobile" value="${extend.employeeMobile}" name="extend.employeeMobile" data-options="validType:'mobile'" missingMessage="请输入手机号码"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>决策组：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;" colspan="3">
							<input id="manageGroup"  value="${extend.manageGroup}" name="extend.manageGroup" ></input>
							<a href="javascript:delData('manageGroup');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
				</table>
				<div style="text-align:center;padding:5px">
					<c:if test="${empty extend.id}">					
					<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
					</c:if>
					<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
					<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
					<a href="javascript:closeDialogs()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
				</div>
			</form>
			<div id="selectUser"></div>
			<div id="selectDept"></div>
			  </div>
		</div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
	$(function(){
		$('#dutiesLevel').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '',
		    	name: '无级别'
			},{
		    	encode: '1',
		    	name: '一级'
			},{
				encode: '2',
				name: '二级'
			},{
				encode: '3',
				name: '三级'
			},{
				encode: '4',
				name: '四级'
			}]
		});
		//性别下拉框渲染
		$('#employeeSexName').combobox({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=sex",
			valueField : 'name',
			textField : 'name',
			multiple : false,
			onSelect : function(record){
				$('#employeeSexCode').val(record.encode);
			},
		});
		//职称
		$('#titleName').combobox({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryTitleContrast.action",
			valueField : 'titleName',
			textField : 'titleName',
			multiple : false,
			onSelect : function(record){
				$('#titleName').combobox('setText',record.titleName);
				$('#titleCode').val(record.titleCode);
				$('#titleLevel').val(record.titleLevel);
				$('#titleType').val(record.belongTypeCode);
				$('#titleTypeName').val(record.belongTypeName);
			},
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i++) {
					if (val == data[i].titleName) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'titleCode';
				keys[keys.length] = 'titleName';
				keys[keys.length] = 'titleLevel';
				keys[keys.length] = 'belongTypeCode';
				keys[keys.length] = 'belongTypeName';
				keys[keys.length] = 'titlePinyin';
				keys[keys.length] = 'titleWb';
				keys[keys.length] = 'titleInputCode';
				return filterLocalCombobox(q, row, keys);
			} 
		});
		//职务
		$('#dutiesName').combobox({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDutiesContrasts.action",
			valueField : 'dutiesName',
			textField : 'dutiesName',
			multiple : false,
			onSelect : function(record){
				$('#dutiesName').combobox('setText',record.dutiesName);
				$('#dutiesCode').val(record.dutiesCode);
// 				$('#dutiesLevel').val(record.dutiesLevel);
				$('#dutiesType').val(record.belongLevel);
				$('#dutiesTypeName').val(record.belongLevelName);
			},
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i++) {
					if (val == data[i].dutiesName) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'dutiesCode';
				keys[keys.length] = 'dutiesName';
// 				keys[keys.length] = 'dutiesLevel';
				keys[keys.length] = 'belongLevel';
				keys[keys.length] = 'belongLevelName';
				keys[keys.length] = 'dutiesPinyin';
				keys[keys.length] = 'dutiesWb';
				keys[keys.length] = 'dutiesInputCode';
				return filterLocalCombobox(q, row, keys);
			} 
		});
		
		//人员类型
		$('#employeeTypeName').combobox({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryEmployeeType.action",
			valueField : 'employeeTypeName',
			textField : 'employeeTypeName',
			multiple : false,
			onSelect : function(record){
				$('#employeeTypeName').combobox('setText',record.employeeTypeName);
				$('#employeeType').val(record.employeeType);
			},
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i++) {
					if (val == data[i].employeeTypeName) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'employeeType';
				keys[keys.length] = 'employeeTypeName';
				keys[keys.length] = 'employeePinyin';
				keys[keys.length] = 'employeeWb';
				keys[keys.length] = 'employeeInputCode';
				return filterLocalCombobox(q, row, keys);
			} 
		});
		
		//民族
		$('#nationalName').combobox({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=nationality",
			valueField : 'name',
			textField : 'name',
			multiple : false,
			onSelect : function(record){
				$('#nationalCode').val(record.encode);
			},
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i++) {
					if (val == data[i].name) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		//编制类型
		$('#organizationName').combobox({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=workstate",
			valueField : 'name',
			textField : 'name',
			multiple : false,
			onSelect : function(record){
				$('#organizationCode').val(record.encode);
			},
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i++) {
					if (val == data[i].name) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		//政治面貌
		$('#politicalstatusName').combobox({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=political",
			valueField : 'name',
			textField : 'name',
			multiple : false,
			onSelect : function(record){
				$('#politicalstatusCode').val(record.encode);
			},
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i++) {
					if (val == data[i].name) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		bindEnterEvent("department",popWinToDepartment);
	});
	//决策组
	$('#manageGroup').combobox({
		url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=manageGroup'/>",
		valueField:'encode',
		textField:'name',
		multiple:true,
		onLoadSuccess: function (list) {
			if(list!=undefined&&list!=null&&list.length>0){
				var len=list.length;
				var groupMap=new Map();
				for(var i=0;i<len;i++){
					groupMap.put(list[i].name,list[i].encode);
				}
				var selectGroup="${extend.manageGroup}";
				var selectedGroup=new Array();
				if(selectGroup!=null&&selectGroup!=''&&selectGroup!=undefined){
					var selectGroupArr=selectGroup.split(',');
					for(var i=0;i<selectGroupArr.length;i++){
						selectedGroup.push(groupMap.get(selectGroupArr[i]));
					}
					$('#manageGroup').combobox('setValues',selectedGroup);
				}
			}
	    },
	    filter: function(q, row){
	        var keys = new Array();
	        keys[keys.length] = 'encode';
	        keys[keys.length] = 'name';
	        keys[keys.length] = 'pinyin';
	        keys[keys.length] = 'wb';
	        keys[keys.length] = 'inputCode';
	        return filterLocalCombobox(q, row, keys);
		}
	}); 
	/**
	* 所属部门弹窗
	* @author  杜天亮
	* @date 2017年7月30日09:10:13
	* @version 1.0
	*/
	function popWinToDepartment(){
		popWinDeptCallBackFn = function(rowData){
			$('#departmentCode').textbox('setValue',rowData.deptCode);
			$('#department').val(rowData.deptName);
			$('#deptCode').val(rowData.deptCode);
		};
		var tempWinPath = "<%=basePath%>/popWin/popWinDepartment/toDepartmentPopWin.action";
		window.open (tempWinPath,'newwindow1',' left=200,top=150,width='+ (screen.availWidth -450) +',height='+ (screen.availHeight-310) 
			+',scrollbars,resizable=yes,toolbar=yes')
	}
	
	
	/**
	 * 表单提交
	 * @author  杜天亮
	 * @date 2017年7月31日14:24:37
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 */
	function submit(flg) {
		$('#editForm').form('submit', {
			url : '<%=basePath%>/baseinfo/employeeExtend/saveExtend.action',
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
			},
			success : function(data) {
				$.messager.progress('close');
				if(data=="jobNo"){
					$.messager.alert('提示',"此工号已存在,请重新录入!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				if (flg == 0) {
					$.messager.alert('提示',"保存成功！",'info',function(){
						//实现刷新
						window.opener.refreshdata(); 
						closeDialogs();
					});
				} else if (flg == 1) {
					//清除editForm
					$('#editForm').form('reset');
					//实现刷新
					window.opener.refreshdata(); 
				}
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示',"保存失败！",'info',function(){
					closeDialogs();
				});
			}
		});

	}
		/**
		 * 验证方法 移动电话和身份证号
		 * @author  zpty
		 * @date 2015-6-2 10:53
		 * @version 1.0
		 */
		$.extend($.fn.validatebox.defaults.rules, {
			mobile: {// 验证电话号码
				validator: function(value){
				var rex=/^1[3-9]+\d{9}$/;
				//var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				//区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
				//电话号码：7-8位数字： \d{7,8
				//分机号：一般都是3位数字： \d{3,}
				 //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
				var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				if(rex.test(value)||rex2.test(value))
				{
				  return true;
				}else
				{
				   return false;
				}
				  
				},
				message: '请输入正确电话或手机格式'
			},
			idEntityCard: {
				validator: function(value){
					// 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
					var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
					if(reg.test(value))
					{
					  return true;
					}else
					{
					   return false;
					}
				},
				message: '请输入正确身份证格式'
			}
		}); 
		//关闭dialog
		function closeDialogs() {
			window.close();
		}
		/**
		 * 清除页面填写信息
		 * @author  zpty
		 * @date 2015-6-2 10:53
		 * @version 1.0
		 */
		function clear(){
			$('#editForm').form('reset');
		}
		//清空下拉框
		function delData(combo){
			if(combo){
// 				$('#' + combo).combobox('setValue','');
				$('#' + combo).combobox('clear');
			}
		}
		//清空下拉框
		function delData(combo,inputs){
			if(combo){
				$('#' + combo).combobox('setValue','');
			}
			for(var i = 0; i < inputs.length; i++){
				$('#' + inputs[i]).val('');
			}
		}
		//清空下拉框
		function delDatas(text,input1,input2){
			if(text){
				$('#' + text).textbox('setValue','');
			}
			if(input1){
				$('#' + input1).val('');
			}
			if(input2){
				$('#' + input2).val('');
			}
		}
		
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>