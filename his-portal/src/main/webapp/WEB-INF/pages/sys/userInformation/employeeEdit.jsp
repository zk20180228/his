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
	<div id="panelEast" style="width:100%;height: 100%;"  >
	 <div style="width:100%;height: 100%;">
			<form id="editForm" method="post" enctype="multipart/form-data">
				<input type="hidden" id="id" name="id" value="${employee.id }">
				<input type="hidden" id="deptidname" name="deptid" value="${deptid}">
				<input type="hidden" id="hospitalId" name="employee.hospitalId.id" value="${employee.hospitalId.id }">
				<input type="hidden" id="code" name="employee.code" value="${employee.code }">
				<input type="hidden" id="order" name="employee.order" value="${employee.order }">
				<input type="hidden" id="createUser" name="employee.createUser" value="${employee.createUser }">
				<input type="hidden" id="createDept" name="employee.createDept" value="${employee.createDept }">
				<input type="hidden" id="createTime" name="employee.createTime" value="${employee.createTime }">
				<input type="hidden" id="userId" name="employee.userId.id" value="${employee.userId.id }">
				<input type="hidden" id="userName"  value="${userName }">
				<input id="typeId" name = "employee.type" value="${employee.type }"  type="hidden">
				<input id="workhidden" type="hidden" name="employee.workState" value="${employee.workState}"/>
				
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0"  style="width:100%;padding:5px">
					<tr>
						<td class="honry-lable">
							<span>所属部门：</span>&nbsp;&nbsp;
						</td>
						
						<td style="text-align: left;">
							${dname}
							<input id="deptId" name="employee.deptCode" value="${deptNameId}"  type="hidden">
						</td>
						<td class="honry-lable">
							<span>工作号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${employee.jobNo}
							<input type="hidden" id="jobNo" name="employee.jobNo" value="${employee.jobNo }">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>姓名：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="name" class="easyui-textbox" value="${employee.name}" name="employee.name"  data-options="required:true" missingMessage="请输入姓名"></input>
						</td>						
						<td class="honry-lable">
							<span>曾用名：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="oldName" class="easyui-textbox" value="${employee.oldName}" name="employee.oldName"  ></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>自定义码：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="inputCode" class="easyui-textbox" value="${employee.inputCode}" name="employee.inputCode"  ></input>
						</td>						
				  	    <td class="honry-lable">
							<span>性别：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="sex" class="easyui-combobox" value="${employee.sex}" name="employee.sex"  missingMessage="请输入选择性别"></input>
						</td>
					</tr>			
					<tr>
						<td class="honry-lable">
							<span>民族：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="family"  class="easyui-combobox"  value="${employee.family}" name="employee.family"  missingMessage="请输入选择民族" ></input>
							<a href="javascript:delSelectedData('family');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>						
				  	    <td class="honry-lable">
							<span>出生日期：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="birthday" value="${employee.birthdayView}" name="employee.birthday" class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
					</tr>			
					<tr>
						<td class="honry-lable">
							<span>学历：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="education" class="easyui-combobox" value="${employee.education}" name="employee.education"  missingMessage="请输入选择学历"></input>
						</td>						
				  	    <td class="honry-lable">
							<span>员工类型：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;" id="typeName">
							${employee.type }
						</td>
					</tr>		
					<tr>
						<td class="honry-lable">
							<span>职称：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${employee.title}
							<input id="title" type="hidden"  value="${employee.title}" name="employee.title" ></input>
						</td>						
				  	    <td class="honry-lable">
							<span>办公室：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox"  id="office" value="${employee.office}" name="employee.office"  ></input>
						</td>
					</tr>		
					<tr>
						<td class="honry-lable">
							<span>职务：</span>&nbsp;&nbsp;                                        
						</td>
						<td style="text-align: left;">
							${employee.post}
							<input id="post"  type="hidden" combobox"  value="${employee.post}" name="employee.post"  ></input>
						</td>						
				  	    <td class="honry-lable">
							<span>办公电话：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input  id="officePhone" class="easyui-textbox" value="${employee.officePhone}" name="employee.officePhone" ></input>
						</td>
					</tr>		
					<tr>
						<td class="honry-lable">
							<span>传真：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="fax" class="easyui-textbox" value="${employee.fax}" name="employee.fax"  ></input>
						</td>						
				  	    <td class="honry-lable">
							<span>移动电话：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id="mobile" value="${employee.mobile}" name="employee.mobile" data-options="validType:'mobile'" missingMessage="请输入电话"></input>
						</td>
					</tr>		
					<tr>
						<td class="honry-lable">
							<span>电子邮件：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input  id="email" class="easyui-textbox" value="${employee.email}" name="employee.email"  data-options="validType:'email'" missingMessage="请输入电子邮件"></input>
						</td>						
				  	    <td class="honry-lable">
							<span>身份证：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id="idEntityCard" value="${employee.idEntityCard}" name="employee.idEntityCard" data-options="validType:'idEntityCard'"  missingMessage="请填写身份证号"  ></input>
						</td>
					</tr>		
					<tr>
				  	    <td class="honry-lable">
							<span>工作状态：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;" id="work">
							${employee.workState}
						</td>
						 <td class="honry-lable">
							<span>工资账号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${employee.wagesAccount}
							<input type="hidden" id="wagesAccount" value="${employee.wagesAccount}" name="employee.wagesAccount"></input>
						</td>
					</tr>
					<tr>
							<td colspan="4">
							<span>能否改票据：</span>
				    			<input type="hidden" id="canEditBillh" name="employee.canEditBill" value="${employee.canEditBill }"/>
				    			<input type="checkBox" id="canEditBill" disabled="disabled" />
								&nbsp;&nbsp;&nbsp;&nbsp;<span>能否直接收费：</span>
				    			<input id="canChargeh" type="hidden" name="employee.canCharge" value="${employee.canCharge }"/>
				    			<input type="checkBox" id="canCharge" disabled="disabled" />
								&nbsp;&nbsp;&nbsp;&nbsp;<span>是否是专家：</span>
				    			<input id="isExperth" type="hidden" name="employee.isExpert" value="${employee.isExpert }"/>
				    			<input type="checkBox" id="isExpert" disabled="disabled"  />
				    			&nbsp;&nbsp;&nbsp;&nbsp;
			    			<span>是否是干部：</span>
				    			<input type="hidden" id="ifcadreh" name="employee.ifcadre" value="${employee.ifcadre }"/>
				    			<input type="checkBox" id="ifcadre" disabled="disabled" />
			    				&nbsp;&nbsp;&nbsp;&nbsp;<span>是否停用：</span>
				    			<input id="stop_flgh" type="hidden" name="employee.stop_flg" value="${employee.stop_flg }"/>
				    			<input type="checkBox" id="stop_flg" disabled="disabled" />
			    			</td>
					</tr>
					<tr>
						<td colspan="4">
							<span>电话是否可见：</span>
			    			<input type="hidden" id="mofficePhoneVisibleh" name="employee.mofficePhoneVisible" value="${employee.mofficePhoneVisible }"/>
			    			<input type="checkBox" id="mofficePhoneVisible" disabled="disabled" />
							&nbsp;&nbsp;&nbsp;&nbsp;<span>手机是否可见：</span>
			    			<input id="mmobileVisibleh" type="hidden" name="employee.mmobileVisible" value="${employee.mmobileVisible }"/>
			    			<input type="checkBox" id="mmobileVisible" disabled="disabled" />
							&nbsp;&nbsp;&nbsp;&nbsp;<span>邮箱是否可见：</span>
			    			<input id="memailVisibleh" type="hidden" name="employee.memailVisible" value="${employee.memailVisible }"/>
			    			<input type="checkBox" id="memailVisible" disabled="disabled" />
		    			</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>电子签名：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;"  colspan="3">
							<input type="file" name="fileEsign" id="fileFileName"
								onChange="onCheckESIGN(this)">
							<img id="copyEsign" alt="" onclick="showEsign('${employee.esign }');"> 
							<input id="hesign"style="width: 300px" type="text" >
								&nbsp;
							<input type="button" value="浏览"
								OnClick="JavaScript:$('#fileFileName').click();">
							<input id="esign" name="employee.esign" type="hidden"
								value="${employee.esign }">
							<!-- (上传格式:jpg图片,大小不超过1M) -->
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>照片：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;"  colspan="3">
							<input type="file" name="filePhoto" id="photoFile"
								onChange="onCheckPHOTO(this)">
							<img id="copyPhoto" alt="" onclick="showPhoto('${employee.photo }')"> 
							<input id="hphoto" style="width: 300px" type="text" >
							&nbsp;
							<input type="button" value="浏览"
								OnClick="JavaScript:$('#photoFile').click();">
							<input id="photo" name="employee.photo" type="hidden"
								value="${employee.photo }">
								
							<!-- (上传格式:jpg图片,大小不超过1M) -->
						</td>
					</tr>

					<tr>
						<td class="honry-lable">
							<span>备注：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;"  colspan="3">
						${employee.remark}
							<input type = "hidden" id="remark" value="${employee.remark}"
								name="employee.remark" style="width: 90%;height:70px"></input>
						</td>
					</tr>
				</table>
				 <div style="text-align:center;padding:5px">
			    	<c:if test="${empty employee.id}">			    	
			    	<a href="javascript:validCheck(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a href="javascript:validCheck(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
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
	var sexArray = null;
	var dutiesList=null;
	var fileServerURL = '${fileServerURL}';
	var stateMap = new Map();
	/**
		 * 页面加载
		 * @author  zpty
		 * @date 2015-6-2 10:53
		 * @Modifier：lt
		 * @ModifyDate：2015-10-28
	 	 * @ModifyRmk：加了上传图片操作
		 * @version 1.0
		 */
		$(function(){
			window.onbeforeunload = function() { 
			}
			var winH=$("body").height();
			$('#panelEast').height(winH);
			if($('#deptidname').val()!=null){
				$.ajax({ 
					   url : "<c:url value='/baseinfo/employee/deptComboBoxShow.action'/>?deptid="+$('#deptidname').val(),  
					   type:'post',
					   success:function(deptdata){
					    	var deptObj = deptdata;
					    	if(deptObj.length>0){
						    	$('#deptSelectName').textbox('setValue',deptObj[0].deptName);
						    	$('#deptId').val(deptObj[0].id);
					    	}
					    }
					});
			}
			//性别下拉框渲染
			$('#sex').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
				valueField : 'encode',
				textField : 'name',
				multiple : false
			});
			var typeList = null;
			$.ajax({//下拉框员工类型 
			      url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action" ,  
				    type : "post",  
					data:{"type":"empType"},
					success: function(list) { 
						typeList=list;
						if($('#typeId').val()!=null){
							var value =$('#typeId').val();
							for ( var i = 0; i < typeList.length; i++) {
								if (value == typeList[i].encode) {
									$('#typeName').text(typeList[i].name);
								}
							}
						}
				    }
		    });
			
			//判断是否编辑状态，编辑状态修改复选框选中状态
			if($('#id').val()!=''){
				if($('#canEditBillh').val()==1){
					$('#canEditBill').attr("checked", true); 
				}
				if($('#canChargeh').val()==1){
					$('#canCharge').attr("checked", true); 
				}
				if($('#isExperth').val()==1){
					$('#isExpert').attr("checked", true); 
				}
				if($('#ifcadreh').val()==1){
					$('#ifcadre').attr("checked", true); 
				}
				if($('#stop_flgh').val()==1){
					$('#stop_flg').attr("checked", true); 
				}
				if($('#mofficePhoneVisibleh').val()==0){
					$('#mofficePhoneVisible').attr("checked", true); 
				}
				if($('#mmobileVisibleh').val()==0){
					$('#mmobileVisible').attr("checked", true); 
				}
				if($('#memailVisibleh').val()==0){
					$('#memailVisible').attr("checked", true); 
				}
			}
			//实现模仿上传file控件  吧真实file控件隐藏
			$('#fileFileName').hide();
			$('#photoFile').hide();
			$('#hesign').hide();
			$('#hphoto').hide();
			
			//编辑时用为了只显示名称 而不是把路径显示出来
			var esign = $('#esign').val();
			var photo = $('#photo').val();
			if(esign!=null&&esign!=""){
				$("#copyEsign").attr("src",fileServerURL + esign);
			}
			if(photo!=null&&photo!=""){
				$("#copyPhoto").attr("src", fileServerURL+photo);
		}
			bindBlackEvent("account",keyUpUser);
// 			bindBlackEvent("deptSelectName",keyUpDept);
// 			bindEnterEvent('deptSelectName',keyUpDept,'easyui');
			/**
			  * 回车弹出事件高丽恒
			  * 2016-03-22 14:41
			  */
// 			 bindEnterEvent('family',popWinToCodeNationality,'easyui');
// 			 bindEnterEvent('title',popWinToCodeTitle,'easyui');
// 			 bindEnterEvent('post',popWinToCodeDuties,'easyui');		
	});
	
	
	/**
	  * 回车弹出事件高丽恒
	  * 2016-03-22 14:41
	  */
	function popWinToCodeNationality(){
		var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=nationality&textId=family";
		var aaa=window.open (tempWinPath,'newwindow1',' left=200,top=150,width='+ (screen.availWidth - 450) +',height='+ (screen.availHeight-310) +',scrollbars,resizable=yes,toolbar=no')
	}

	
	function popWinToCodeDuties(){
		var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=duties&textId=post";
		var aaa=window.open (tempWinPath,'newwindow1',' left=200,top=150,width='+ (screen.availWidth - 450) +',height='+ (screen.availHeight-310) +',scrollbars,resizable=yes,toolbar=no')
	}
	
		//提交时验证标志
		var checkFlag = "";
		var employeeType=null;
		$.ajax({//下拉框员工类型 
		      url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action" ,  
			    type : "post",  
				data:{"type":"empType"},
				success: function(list) { 
					employeeType=list;
			    }
	    });	
		//判断工作号唯一标识
		var isClickOk=true;
		//当光标移开焦点的时候进行工作号重复验证  
	    function validCheck(flg){ 
	    	$.messager.progress({text:'保存中，请稍后...',modal:true});
	    	var jobNo=$('#jobNo').val();
			var eid=$('#id').val();
	    	var wagesAccount = $('#wagesAccount').val();
	 	    if(wagesAccount==null || wagesAccount==''){
	 	    	$('#wagesAccount').val(jobNo);
	 	    	wagesAccount = $('#wagesAccount').val();
	 	    }
		    //使用Ajax异步验证组名是否重复  
    		jQuery.ajax({ 
    			 type : "post",  
    			    url : "<c:url value='/baseinfo/employee/queryExistJobNo.action'/>",
    			    data: {jobNot:jobNo,eid:eid},  
    			    dataType:'json',  
    			    success : function(data){    
    			           if($('#jobNo').val()==""){//当为空都不显示，因为Easyui的Validate里面已经自动方法限制  
    			        	   $.messager.progress('close');
    			        	   $("#message").empty();
    			           }  
    			           else if( data == "yes" ){ //当返回值为yes，表示在数据库中没有找到重复的组名  
    			        	   $("#message").empty();
    			        	   $.ajax({//下拉框员工类型 
    			     		      	url : "<c:url value='/baseinfo/employee/queryExistWagesAccount.action'/>",
    			     			    type : "post",  
    			     				data:{wagesAccount:wagesAccount,eid:eid},
    			     				success: function(isNot) { 
    			     					if(isNot =="yes"){
    			     						 submit(flg);
    			     					}else{
    			     						$.messager.progress('close');
   			     						 	$.messager.alert('提示',"该工资账号已被使用!");
   		    					            close_alert();
   		    					            return;
    			     					}
    			     			    }
    			     	  	  });	
    			        	  
    			        	}  
    			           else {  
    				            if($('#oldJobNo').val()==$('#jobNo').val()){
    				            	var isClickOk=true;
    				            	$.messager.progress('close');
    				            	$("#message").empty(); 
    				            }else{
    				            	var isClickOk=false;
    				            	$.messager.progress('close');
    					            $("#message").empty();  
    					            //$("#message").append("已使用"); 
    					            $.messager.alert('提示',"该工作号已被使用!");
    					            close_alert();
    					            return;
    				            }
    			          }  
    			        }  
    				});  
		};
		
		function keyUpUser() {  
				Adddilog("选择用户", "<c:url value='/baseinfo/employee/listUserUrl.action'/>",'selectUser');
			
	    }
		function keyUpDept(){
				Adddilog("选择部门","<c:url value='/baseinfo/employee/listDeptUrl.action'/>",'selectDept');
		}
		
		$.ajax({
			url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=workstate",
			async:false,
			success: function(data) {
				var deptType = data;
				for(var i=0;i<deptType.length;i++){
					stateMap.put(deptType[i].encode,deptType[i].name);
				}
			}
		});
		
		
	/**
	 * 表单提交
	 * @author  zpty
	 * @date 2015-6-2 10:53
	 * @Modifier：lt
	 * @ModifyDate：2015-10-28
	 * @ModifyRmk：加了上传图片操作
	 * @version 1.0
	 */
	function submit(flg) {
		 var flag;
// 	    if($('#userName').val()!=null&&""!=$('#userName').val()){
	    	flag=false;
// 	    }else{
// 	    	flag=createUsercheckBox.checked;
// 	    }
		$('#editForm').form('submit', {
			url : "<c:url value='/baseinfo/employee/saveOrUpdateEmployee.action'/>?flag="+flag,
			onSubmit : function() {
				if(isClickOk==false){//当组名重复被设置为false，如果为false就不提交，显示提示框信息不能重复  
						$.messager.progress('close');
                        $.messager.alert('操作提示', '工作号不能重复！','error');  
                        close_alert();
                        return false;  
                    }
// 		        	if($('#deptSelectName').textbox('getValue')==""){
// 		        		$.messager.progress('close');
// 		        		 $.messager.alert('操作提示', '部门不能为空！','error');  
// 		        		 close_alert();
// 	                     return false;  
// 					}
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
				if(checkFlag == "photoPatternNo"){
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '照片格式不正确,不能提交表单!'
					});
					return false;
				}
				if(checkFlag == "photoSizeNo"){
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '照片大小不正确,不能提交表单!'
					});
					return false;
				}
				if(checkFlag == "esignPatternNo"){
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '电子签名格式不正确,不能提交表单!'
					});
					return false;
				}
				if(checkFlag == "esignSizeNo"){
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '电子签名大小不正确,不能提交表单!'
					});
					return false;
				}
			},
			success : function(data) {
				$.messager.progress('close');
				if (flg == 0) {
					$.messager.alert('提示',"保存成功！",'info',function(){
					//实现刷新
					window.opener.upPage(basePath+"/sys/userInformation.action","用户信息"); 
					closeDialogs();
				});
				} else if (flg == 1) {
					//清除editForm
					$('#editForm').form('reset');
					//实现刷新
					window.opener.upPage(basePath+"/sys/userInformation.action","用户信息"); 
				}
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','保存失败！');
				closeDialogs();
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
		  },
       	  email:{  
                validator: function (value) {       //email验证	
                 return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(value);  
             },  
             message: '请输入有效的邮箱账号(例：123456@qq.com)'  
           }
		});
	/**
	 * 处理图片和签名图片的方法
	 * @author  lt
	 * @date 2015-10-30 10:53
	 * @version 1.0
	 */
	function onCheckPHOTO(filePicker) {
		var fName = document.getElementById("photoFile").value.toLowerCase();
		$("#copyPhoto").hide();
		$("#hphoto").show();
		$("#hphoto").val(fName);
		
		if (fName != null && fName != "") {
			var ftype = fName.toLowerCase().split(".");
			var fTypeName = ftype[ftype.length - 1];
			if (!fTypeName == '') {
				if (fTypeName != "jpg" && fTypeName != "jpeg"
						&& fTypeName != "png" && fTypeName != "gif") {
					$.messager.alert('提示',"上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！");
					close_alert();
					checkFlag = "photoPatternNo";//提交时验证代表格式不对
				} else {
					if (filePicker.files[0].size > 1 * 1024 * 1024) {
						$.messager.alert('提示',"上传的文件大小大于1M");
						close_alert();
						checkFlag = "photoSizeNo";//提交时验证代表大小不对
					}else{
						 checkFlag = "";
					}
				}
			} else {
				$.messager.alert('提示',"上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！");
				close_alert();
				checkFlag = "photoPatternNo";//提交时验证代表格式不对
			}
		}
	}
	function onCheckESIGN(filePicker) {
		var fName = document.getElementById("fileFileName").value;
		$("#copyEsign").hide();
		$("#hesign").val(fName);
		$("#hesign").show();
		if (fName != null && fName != "") {
			var ftype = fName.toLowerCase().split(".");
			var fTypeName = ftype[ftype.length - 1];
			if (!fTypeName == '') {
				if (fTypeName != "jpg" && fTypeName != "jpeg"
						&& fTypeName != "png" && fTypeName != "gif") {
					$.messager.alert('提示',"上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！");
					close_alert();
					checkFlag = "esignPatternNo";//提交时验证代表格式不对
				} else {
					if (filePicker.files[0].size > 1 * 1024 * 1024) {
						$.messager.alert('提示',"上传的文件大小不能大于1M，请重新上传！");
						close_alert();
						checkFlag = "esignSizeNo";//提交时验证代表大小不对
					}else{
						checkFlag = "";
					}
				}

			} else {
				$.messager.alert('提示',"上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！");
				close_alert();
				checkFlag = "esignPatternNo";//提交时验证代表格式不对
			}
		}

	}
		//加载dialog
		function Adddilog(title, url,id) {
			$('#'+id).dialog({    
			    title: title,    
			    width: '60%',    
			    height:'80%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true 
			   });  
		}
		//打开dialog
		function openDialog(id) {
			$('#'+id).dialog('open'); 
		}
		//关闭dialog
		function closeDialog(id) {
			$('#'+id).dialog('close');  
		}
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
		
		//复选框
		function checkBoxSelect(id,defalVal,selVal){
			var hiddenId=id+"h";
			if($('#'+id).is(':checked')){
				$('#'+hiddenId).val(selVal);
			}else{
				$('#'+hiddenId).val(defalVal);
			}
		}
		//展示电子签名
		function showEsign(url) {
			$("#esignImageWinImg").attr("src","<%=basePath%>" + url);
			$('#esignImageWin').window({
				title:$('#name').val(),   
			    width:600,    
			    height:400,    
			    modal:true
			}); 
		}
		//展示照片
		function showPhoto(url) {
			$("#photoImageWinImg").attr("src","<%=basePath%>" + url);
			$('#photoImageWin').window({
				title:$('#name').val(),   
			    width:600,    
			    height:400,    
			    modal:true
			}); 
		}
/***********************************************************************************/
		//民族下拉框
		$('#family').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=nationality'/>",
			valueField : 'encode',
			textField : 'name',
			editable:'false',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				if(filterLocalCombobox(q, row, keys)){
					row.selected=true;
				}else{
					row.selected=false;
				}
				return filterLocalCombobox(q, row, keys);
		    },
			onLoadSuccess:function(data){
				if(data!=null && data.length==1){
					var code= data[0].encode;
					$('#family').combobox('select',code);
				}
			},
			onSelect:function(rec){
				var code=rec.encode;
				havSelect = false;
			},
			onHidePanel:function(){
			 	var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].encode) {
			            result = false;
			        }
			    }
			    if (result) {
			        $(this).combobox("clear");
			    }else{
			        $(this).combobox('unselect',val);
			        $(this).combobox('select',val);
			    }
				if(havSelect){
					var isOnly = 0;
					var onlyOne = null;
					for(var i = 0;i<$("#family").combobox("getData").length;i++){
						if($("#family").combobox("getData")[i].selected){
							isOnly++;
							onlyOne = $("#family").combobox("getData")[i];
						}
					}
					if((isOnly-1)==0){
						var encode = onlyOne.encode;
						$('#family').combobox('setValue',deptMap[encode]);
						$('#family').combobox('select',encode);
					}
				}
				havSelect=true;							
			}
		});	
		
		window.onload=function(){
			var value =$('#workhidden').val();
			if(value!=null){
				$('#work').text(stateMap.get(value));
			}
		}
		//学历下拉框
		$('#education').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=degree'/>",
			valueField : 'encode',
			textField : 'name',
			editable:'false',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				if(filterLocalCombobox(q, row, keys)){
					row.selected=true;
				}else{
					row.selected=false;
				}
				return filterLocalCombobox(q, row, keys);
		    },
			onLoadSuccess:function(data){
				if(data!=null && data.length==1){
					var code= data[0].encode;
					$('#education').combobox('select',code);
				}
			},
			onSelect:function(rec){
				var code=rec.encode;
				havSelect = false;
			},
			onHidePanel:function(){
			 	var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].encode) {
			            result = false;
			        }
			    }
			    if (result) {
			        $(this).combobox("clear");
			    }else{
			        $(this).combobox('unselect',val);
			        $(this).combobox('select',val);
			    }
				if(havSelect){
					var isOnly = 0;
					var onlyOne = null;
					for(var i = 0;i<$("#education").combobox("getData").length;i++){
						if($("#education").combobox("getData")[i].selected){
							isOnly++;
							onlyOne = $("#education").combobox("getData")[i];
						}
					}
					if((isOnly-1)==0){
						var encode = onlyOne.encode;
						$('#education').combobox('setValue',deptMap[encode]);
						$('#education').combobox('select',encode);
					}
				}
				havSelect=true;							
			}
		});	
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>