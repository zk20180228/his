<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
	<body>
		<div class="easyui-panel" data-options="title:'住院账户编辑',iconCls:'icon-form'">
			<div style="padding:10px">
	    		<form id="editForm" method="post">
					<!--<input type="hidden" id="id" name="account.id" value="${account.id }">
					<input type="hidden" id="createUser" name="account.createUser" value="${account.createUser }">
					<input type="hidden" id="createDept" name="account.createDept" value="${account.createDept }">
					<input type="hidden" id="createTime" name="account.createTime" value="${account.createTime }">
					-->
					<input type="hidden" id="accountId" name="inpatientAccount.id">
					<input type="hidden" id="detailOptype" name="repaydetail.detailOptype">
					<div style="text-align:left;padding:5px">
						<shiro:hasPermission name="${menuAlias}:function:disable">
					    	<a id="stop" href="javascript:stopFlg(1);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">停用</a>
					    </shiro:hasPermission>
					    <shiro:hasPermission name="${menuAlias}:function:enable">
					    	<a id="start" href="javascript:stopFlg(0);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">启用</a>
					    </shiro:hasPermission>
					    <shiro:hasPermission name="${menuAlias}:function:cancellation">
					    	<a id="zhuxiao" href="javascript:zhuxiao();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">注销账号</a>
					    </shiro:hasPermission>
					    <shiro:hasPermission name="${menuAlias}:function:clean">
					    	<a id="jieqing" href="javascript:total();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">结清账户</a>
					    </shiro:hasPermission>	
					    <shiro:hasPermission name="${menuAlias}:function:receivables">
					    	<a id="save" href="javascript:submit(0);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">收款</a>
					    </shiro:hasPermission>
					    <shiro:hasPermission name="${menuAlias}:function:return">
					    	<a id="back" href="javascript:backMoney();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">返还</a>
					    </shiro:hasPermission>
					    <shiro:hasPermission name="${menuAlias}:function:make">
					    	<a id="buda" href="javascript:latePlay();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">补打</a>
					    </shiro:hasPermission>	
					    	<a id="clear" href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">清屏</a>
					    <shiro:hasPermission name="${menuAlias}:function:edit">
					    	<a id="password" href="javascript:updatePwd();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',disabled:'true'">修改密码</a>
					    </shiro:hasPermission>
					    <shiro:hasPermission name="${menuAlias}:function:save">
					    	<a id="add" href="javascript:addAccount();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">添加账户</a>
						</shiro:hasPermission>
					</div>
					    	<!--<a id="clear" href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear',disabled:'true'">清屏</a>
					    	-->
					    	<!--<a id="add" href="javascript:addAccount();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',disabled:'true'">添加账户</a>
					 -->
					<fieldset style="padding: 1%">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
						<tr>
							<td style="width:10%">病历号：</td>
			    			<td style="width:15%"><input class="easyui-validatebox" id="idcardId" name="inpatientAccount.medicalrecordId" data-options="required:true" style="width: 290" missingMessage="请输入卡号" onkeyup="KeyDown1()"/></td>
			    			<td style="width:10%">姓名:</td>
			    			<td style="width:15%" id="patientName"></td>
							<td style="width:10%">性别：</td>
							<td style="width:15%" id="patientSex"></td>
							<td style="width:10%" >出生年月:</td>
			    			<td style="width:15%" id="patientBirthday"></td>
						</tr>
						<tr>	
											
							<td >证件号：</td>
							<td id="patientCertificatesno"></td>
							<td >证件类型:</td>
			    			<td id="patientCertificatestype"></td>
							<td >民族:</td>
			    			<td id="patientNation"></td>
			    			<td >电话:</td>
			    			<td id="patientPhone"></td>
		    			</tr>
		    			<tr>
							<td >籍贯:</td>
			    			<td id="patientNativeplace"></td>
			    			<td>医保号:</td>
			    			<td id="patientHandbook"></td>
							<td >账户名称:</td>
			    			<td><input class="easyui-validatebox" id="accountNameV" name="inpatientAccount.accountName" data-options="required:true" disabled="disabled" style="width: 290" missingMessage="请输入账户名称"/>
			    			<input type="hidden" name="inpatientAccount.inpatientNo" id="inpatientNo">
			    			</td>
			    			<td >账户类型:</td>
			    			<td><input class="easyui-validatebox" id="accountTypeV" name="inpatientAccount.accountType" data-options="required:true" disabled="disabled" style="width: 290" missingMessage="请输入账户类型"/></td>
						</tr>
						<tr>
							<!--<td >账户密码:</td>
			    			<td><input class="easyui-validatebox" type="password" id="accountPasswordV" name="account.accountPassword" data-options="required:true" disabled="disabled" style="width: 290" missingMessage="请输入账户密码"/></td>
			    			-->
			    			<td >账户备注:</td>
			    			<td colspan="7"><input class="easyui-validatebox" id="accountRemarkV" name="inpatientAccount.accountRemark" data-options="required:true" disabled="disabled" style="width: 290" missingMessage="请输入备注"/></td>
						</tr>
						<tr>
			    		</tr>
		    	</table>
		    	</fieldset>
		    	<br>
		    	<fieldset style="padding: 1%">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
						
		    			<!--<tr>
			    			 <td class="honry-lable">参考编号：</td>
			    			<td class="honry-info"><input class="easyui-validatebox" id="accountRefid" name="account.accountRefid" value="${account.accountRefid}" data-options="required:true" missingMessage="请输入参考编号"/></td>
			    		</tr>
						-->
						<!--<tr>
							<td class="honry-lable">名称:</td>
			    			<td><input class="easyui-validatebox" id="accountName" name="account.accountName" value="${account.accountName }" data-options="required:true" missingMessage="请输入名称"/></td>
		    			
							<td class="honry-lable">类型：</td>
							<td><input id="accountType" name="account.accountType" class="easyui-validatebox" data-options="required:true" value="${account.accountType}" style="width:90%;" missingMessage="请选择类型"/></td>
						</tr>						
						--><tr>
							<td class="honry-lable">支付方式:</td>
			    			<td><input class="easyui-combobox" id="detailPaytype" name="repaydetail.detailPaytype" value="${repaydetail.detailPaytype }" data-options="required:true,valueField: 'value',textField: 'label',data: [{label: '现金',value: '现金',selected:true},{label: '银联卡',value: '银联卡'},{label: '支票',value: '支票'}]"  missingMessage="请选择支付方式"/></td>
							<td class="honry-lable">金额：</td>
							<td><input id="detailDebitamount" name="repaydetail.detailDebitamount" class="easyui-validatebox" data-options="required:true" value="${repaydetail.detailDebitamount}" style="width:45%;" missingMessage="请输入金额" onkeyup="sumMoney()"/></td>
						</tr>
						
						<tr id="cheque" style="display: none">					
							<td class="honry-lable">开户单位:</td>
			    			<td><input class="easyui-validatebox" id="detailBankunit" name="repaydetail.detailBankunit" value="${account.accountBalance }" data-options="required:true" style="width:30%;" missingMessage="请输入开户单位"/></td>
			    			<td class="honry-lable">开户银行:</td>
			    			<td><input id="CodeBank" name="repaydetail.detailBank" value="${account.accountFrozencapital }" data-options="required:true" style="width:48%;" missingMessage="请输入开户银行" onkeydown="KeyDown(0,'CodeBank')"/></td>
		    			</tr>
		    			<tr id="cheque1" style="display: none">					
							<td class="honry-lable">银行账号:</td>
			    			<td><input class="easyui-validatebox" id="detailBankaccount" name="repaydetail.detailBankaccount" value="${account.accountBalance }" data-options="required:true" style="width:30%;"  missingMessage="请输入银行账号"/></td>
			    			<td class="honry-lable">小票:</td>
			    			<td><input class="easyui-validatebox" id="detailBankbillno" name="repaydetail.detailBankbillno" value="${account.accountFrozencapital }" data-options="required:true" style="width:45%;" missingMessage="请输入小票"/></td>
		    			</tr>
		    			<tr id="banck">					
							<td class="honry-lable">实缴:</td>
			    			<td><input class="easyui-validatebox" id="totalMoney" style="width:30%;" onkeyup="sumMoney()"/></td>
			    			<td class="honry-lable">找回:</td>
			    			<td><input class="easyui-validatebox" id="banckMoney" style="width:45%;" readonly="readonly"/></td>
		    			</tr>
		    			<!--
		    			<tr>
							
		    			</tr>
		    			<tr>
							<td class="honry-lable">冻结时间：</td>
							<td class="honry-info"><input id="accountFrozentime" name="account.accountFrozentime" class="easyui-datebox" data-options="required:true" value="${account.accountFrozentime}" style="width:90%;" missingMessage="请选择冻结时间"/></td>
						</tr>
						<tr>					
							<td class="honry-lable">解冻时间:</td>
			    			<td class="honry-info"><input class="easyui-datebox" id="accountUnfrozentime" name="account.accountUnfrozentime" value="${account.accountUnfrozentime }" data-options="required:true" style="width:90%;" missingMessage="请选择解冻时间"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">备注:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="accountRemark" name="account.accountRemark" value="${account.accountRemark }" data-options="multiline:true,required:true" style="width:95%;height:60px;"  missingMessage="请输入备注"/></td>
						</tr>
		    	--></table>
		    	</fieldset>
		    	</form>
		    	<br>
		    	<fieldset style="padding: 1%">
		    	<div id="ui" class="easyui-tabs" style="width: 100%; height: 500px;" >
	 				<div id="divLayout" class="easyui-layout" fit=true title="预存金">
					<div data-options="region:'center',split:false,title:'预存金',iconCls:'icon-book'" style="padding:10px;">
						<table id="alist" title="预存金" border="false" class="easyui-datagrid"
							data-options="method:'post',rownumbers:true,idField: 'id',border:true,singleSelect:false,pagination:true">
							<thead>
								<tr>
									<th field="ck" checkbox="true"></th>								
									<th data-options="field:'detailDebitamount'" style="width:6%">
										充值金额	
									</th>
									<th data-options="field:'detailCreditamount'" style="width:6%">
										消费金额
									</th>
									<th data-options="field:'detailPaytype'" style="width:6%">
										支付方式
									</th>
									<th data-options="field:'detailBankunit'" style="width:7%">
										开户单位
									</th>
									<th data-options="field:'detailBank'" style="width:7%">
										开户银行
									</th>
									<th data-options="field:'detailBankaccount'" style="width:9%">
										银行账号
									</th>
									<th data-options="field:'detailOptype',formatter:function(val,row){
														if (val == 1){
															return '付款';
														} else if (val == 2){
															return '返还';
														}else if (val == 3){
															return '补打';
														}else if (val == 4){
															return '结清账户';
														}
													}" style="width:9%">
										类型
									</th>
								</tr>
							</thead>
							
						</table>
					</div>
					</div>
					<div title="历史预存金">
					<jsp:include page="../../../pages/inpatient/account/historyAccountList.jsp"></jsp:include>
					</div>
					<div title="就诊卡信息">
					<jsp:include page="../../../pages/inpatient/account/idcardList.jsp"></jsp:include>
					</div>										
				</div>
				</fieldset>
		    	<!--
			    <div style="text-align:center;padding:5px">
			    	<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			    -->
	    	
	    </div>
	   
	</div>
	<script>	
		var sexList = "";
		var countryList = "";	
		var certiList = "";
		var nationList = "";
		/**
		 * 自动关闭弹出窗口
		 * @author  lt
		 * @date 2015-7-1
		 * @version 1.0
		 */
		 
		$(function(){
			$('#modifyPwd').window('close');  // close a window
			
		/**
		 * 点击下拉列表弹框
		 * @author  lt
		 * @date 2015-7-1
		 * @version 1.0
		 */	
			$('#detailPaytype').combobox({    
			    onChange:function(value){
			    	if(value == "支票"){
			    		$("#banck").hide();
				    	$("#cheque").show(); 
				    	$("#cheque1").show();
				    	//初始化下拉框
						idCombobox("CodeBank");
						$("#totalMoney").val("");
				    	$("#banckMoney").val("");
			    	}else if(value == "现金"){
			    		$("#banck").show();
			    		$("#cheque").hide(); 
				    	$("#cheque1").hide();
				    	$("#detailBankunit").val("");
				    	$("#CodeBank").combobox("setValue","");
				    	$("#detailBankaccount").val("");
				    	$("#detailBankbillno").val("");
			    	}else{
			    		$("#banck").hide();
			    		$("#cheque").hide(); 
				    	$("#cheque1").hide();
				    	$("#detailBankunit").val("");
				    	$("#CodeBank").combobox("setValue","");
				    	$("#detailBankaccount").val("");
				    	$("#detailBankbillno").val("");
				    	$("#totalMoney").val("");
				    	$("#banckMoney").val("");
			    	}
			    	
			    	/*if(value == "支票"){
						$('#chequePay').dialog({
						    title:"支付信息", 
							modal:true
						});
			    	} */
			    }   
			}); 
			
			$.ajax({
				url: "<c:url value='/likeSex.action'/>",
				type:'post',
				success: function(sexdata) {
					sexList = eval("("+ sexdata +")");
				}
			});
			$.ajax({
				url: "<c:url value='/outpatient/changeDeptLog/likeCertificate.action'/>",
				type:'post',
				success: function(certidata) {
					certiList = certidata;
				}
			});
			$.ajax({
				//2016年10月19日 GH 编码修改 版本更新 
				//url: "<c:url value='/likeNationality.action'/>",
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=nationality'/>",
				type:'post',
				success: function(nationdata) {
					nationList = nationdata;
				}
			});		
		});
		//从xml文件中解析，读到下拉框
		function idCombobox(param){
			$('#'+param).combobox({
			    url:"<c:url value='/comboBox.action'/>?str="+param,   
			    valueField:'id',
			    textField:'name',
			    multiple:false
			});
		}
		//下拉框的keydown事件   调用弹出窗口
		var CodeBankkeydown = $('#CodeBank').combobox('textbox'); 
		CodeBankkeydown.keyup(function(){
			KeyDown(0,"CodeBank");
		});
		function KeyDown(flg,tag){ 	    	
	    	if(flg==1){//回车键光标移动到下一个输入框
		    	if(event.keyCode==13){	
		    		event.keyCode=9;
		    	}
		    }
		    if(flg==0){	//空格键打开弹出窗口
			    if (event.keyCode == 32)  
			    { 
			        event.returnValue=false;  
			        event.cancel = true; 
			        if(tag=="CodeMedicaltype"){
			        	showWin("银行","<c:url value='/ComboxOut.action'/>?xml="+"CodeBank,0","50%","80%");
			        }
			    }
		    }
		} 
		
		/**
	 * 弹出框
	 * @author  lt
	 * @date 2015-07-1
	 * @version 1.0
	 */
		var win;	
		function showWin (title,url, width, height) {
		   	var content = '<iframe id="myiframe" src="' + url + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
		    var divContent = '<div id="treeDeparWin">';
		    win = $('<div id="treeDeparWin"><div/>').dialog({
		        content: content,
		        width: width,
		        height: height,
		        modal: true,
		        resizable:true,
		        shadow:true,
		        center:true,
		        title: title
		    });
		    win.dialog('open');
		} 
		
		/**
		 * 表单提交
		 * @author  lt
		 * @date 2015-7-1
		 * @version 1.0
		 */
		
		var record = new Array(); //用来记录返还过的 条款id；
		function submit(repaydetailId){
		//repaydetailId==0为收款操作  不等于0 为返还操作
			if(repaydetailId != 0){
				$('#detailPaytype').combobox({    
				   required: false 
				});  
				$('#detailDebitamount').validatebox({    
				    required: false  
				});
			}else{
				$("#detailOptype").val(1);
			}
			if($('#detailPaytype').combobox("getValue")!="支票"){
				$('#detailBankunit').validatebox({    
				    required: false  
				});
				$('#CodeBank').combobox({    
				    required: false  
				});
				$('#detailBankaccount').validatebox({    
				    required: false  
				});
				$('#detailBankbillno').validatebox({    
				    required: false  
				});
			}
			$('#editForm').form('submit',{  
			        url:"<c:url value='/inpatient/account/editInpatientAccountDetail.action'/>?repaydetailId="+repaydetailId, 
			        onSubmit:function(){ 
			            return $(this).form('validate');  
			        },  
			        success:function(data){
			        	if($("#detailOptype").val()==2){
			        		record.push(repaydetailId);
			        	}
			        	$.messager.alert('提示',"保存成功！");
			        	$('#alist').datagrid('reload');
			        	/*clearValue();*/
			        	$("#detailPaytype").combobox("setText","");
			        	$("#detailDebitamount").val("");
			        	$("#detailBankunit").val("");
				    	$("#CodeBank").combobox("setValue","");
				    	$("#detailBankaccount").val("");
				    	$("#detailBankbillno").val("");
				    	$('#detailBankunit').validatebox({    
						    required: true  
						});
						$('#CodeBank').combobox({  
						    required: true  
						});
						$('#detailBankaccount').validatebox({    
						    required: true  
						});
						$('#detailBankbillno').validatebox({    
						    required: true  
						});
						$("#cheque").hide(); 
				    	$("#cheque1").hide();
				    	 //返还完的条目高亮显示
			        	$('#alist').datagrid({
							rowStyler: function(index,row){
								for(var i=0;i<record.length;i++){
									if(row.id == record[i]){
										return 'background-color:#6293BB;color:#fff;';
									}
								}
							}
						});
			        },
					error : function(data) {
						$.messager.alert('提示',"保存失败！");	
					}			         
			    }); 
		}
		/**
		 * 清除页面填写信息
		 * @author  lt
		 * @date 2015-7-1 18:06
		 * @version 1.0
		 */
		function clear(){
			window.location="inpatient/account/addInpatientAccount.action";
		}
		/**
		 * 获取患者基本信息
		 * @author  lt
		 * @date 2015-7-1 10:53
		 * @version 1.0
		 */
		function getPatient() {
			$.ajax({
						url: "<c:url value='/inpatient/info/getInPatientInfoByMedical.action'/>?medicalNo="+$('#idcardId').val(),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
						type:'get',
						success: function(data) {
							var dataObj = eval("("+data+")");
							if(dataObj != null&&dataObj != ""){
								if(dataObj.account.id!="no"){
									viewValue(dataObj);
									if(dataObj.account.stop_flg!="1"){
										$('#stop').linkbutton('enable');
									//	$('#start').linkbutton('enable');
										$('#jieqing').linkbutton('enable');
										$('#zhuxiao').linkbutton('enable');
										$('#save').linkbutton('enable');
										$('#back').linkbutton('enable');
										$('#buda').linkbutton('enable');
										$('#password').linkbutton('enable');
										$('#clear').linkbutton('enable');
									}else{
										$.messager.alert("操作提示", "此账户已被停用！","warning");
										$('#start').linkbutton('enable');
										$('#detailDebitamount').attr("disabled","disabled");
										$('#detailPaytype').combobox({    
										   disabled:true  
										});  
										$('#detailDebitamount').validatebox({    
										    required: false  
										});
									}
									$('#alist').datagrid({
									    url:"<c:url value='/inpatient/account/queryInpatientRepayDetail.action'/>?menuAlias=${menuAlias}&ishis=0&accountId=" + dataObj.account.id,
									    //返还完的条目高亮显示
									    rowStyler: function(index,row){
									    var data = $('#alist').datagrid('getData');
										record = data.str;
										    if(record!=null){
										   		for(var i=0;i<record.length;i++){
													if(row.id == record[i]){
														return 'background-color:#6293BB;color:#fff;';
													}
												}
										    }
										}
									});  
									
									$('#ui').tabs({    
									    border:false,
									    onSelect:function(title){
									    	if(title == "预存金"){
										    	$("#alist").datagrid('options').url = "<c:url value='/inpatient/account/queryInpatientRepayDetail.action'/>?menuAlias=${menuAlias}&ishis=0&accountId=" + dataObj.account.id;//加载list
												$('#alist').datagrid('reload');    // 重新载入当前页面数据
									    	} 
									    	if(title == "历史预存金"){
										    	$("#hislist").datagrid('options').url = "<c:url value='/inpatient/account/queryInpatientRepayDetail.action'/>?menuAlias=${menuAlias}&ishis=1&accountId=" + dataObj.account.id;//加载list
												$('#hislist').datagrid('reload');    // 重新载入当前页面数据  
									    	}  
									    	if(title == "就诊卡信息"){
										    	$("#idcardList").datagrid('options').url = "<c:url value='/inpatient/queryIdCardByAccount.action'/>?menuAlias=${menuAlias}&idcardId=" + dataObj.account.idcardNo;//加载list
												$('#idcardList').datagrid('reload');    // 重新载入当前页面数据  
									    	}   
									    }  
									});
									 
									
								}else{
									$.messager.alert('提示',"此病历号患者还没有账户，请为此卡添加账户。");
									$('#add').linkbutton('enable');
									$('#accountNameV').removeAttr("disabled");
									$('#accountTypeV').removeAttr("disabled");
									$('#accountPasswordV').removeAttr("disabled");
									$('#accountRemarkV').removeAttr("disabled");
									$('#accountTypeV').validatebox({    
									    required: true  
									});
									$('#accountNameV').validatebox({    
									    required: true  
									});
									$('#accountPasswordV').validatebox({    
									    required: true  
									});
									$('#accountRemarkV').validatebox({    
									    required: true  
									});
									
									
									$('#detailDebitamount').validatebox({    
									    required: false  
									});
									$('#detailBankunit').validatebox({    
									    required: false  
									});
									$('#CodeBank').combobox({   
									    required: false  
									});
									$('#detailBankaccount').validatebox({    
									    required: false  
									});
									$('#detailBankbillno').validatebox({    
									    required: false  
									});
									$('#detailDebitamount').attr("disabled","disabled");
									$('#detailPaytype').combobox({    
									   disabled:true  
									});  
									
									clearValue();
									return;
								}
							}else{
								clearValue();
								$.messager.alert('提示',"住院登记信息没有此病历号，请重新输入。");
								return;
								
							}
						}
			});	
		} 
		/**
		 * 个人信息数据填充
		 * @author  lt
		 * @date 2015-7-1 10:53
		 * @version 1.0
		 */
	function viewValue(obj){
		$('#patientName').text(obj.patientName);
	    $("#patientSex").text(sexFamater(obj.reportSex));
	    $("#patientBirthday").text(obj.reportBirthday);	
	    $("#patientCertificatesno").text(obj.certificatesNo);
	    $("#patientCertificatestype").text(certiFamater(obj.certificatesType));	
	    $("#patientNation").text(nationFamater(obj.nationCode));
	    $("#patientPhone").text(obj.homeTel);
	    $("#patientNativeplace").text( obj.dist);	
	    $("#patientHandbook").text(obj.mcardNo);
	    $("#accountNameV").val(obj.account.accountName);
	    $("#accountTypeV").val(obj.account.accountType);
	    $("#accountId").val(obj.account.id);
	    $("#accountRemarkV").val(obj.account.accountRemark);
	    $("#inpatientNo").val(obj.inpatientNo);
	   // $("#accountPasswordV").val(obj.account.accountPassword);
	}
	/**
		 * 清空个人信息数据
		 * @author  lt
		 * @date 2015-7-1 10:53
		 * @version 1.0
		 */
	function clearValue(){
		$('#patientName').text("");
	    $("#patientSex").text("");
	    $("#patientBirthday").text("");	
	    $("#patientCertificatesno").text("");
	    $("#patientCertificatestype").text("");	
	    $("#patientNation").text("");
	    $("#patientPhone").text("");
	    $("#patientNativeplace").text( "");	
	    $("#patientHandbook").text("");
	    $("#accountNameV").val("");
	    $("#accountTypeV").val("");
	    $("#accountRemarkV").val("");
	    $("#inpatientNo").val("");
	    //$("#accountPasswordV").val("");
	}
	/**
		 * 添加账号
		 * @author  lt
		 * @date 2015-7-1 
		 * @version 1.0
		 */
	function addAccount(){
		$('#editForm').form('submit',{  
		        url:"<c:url value='/inpatient/account/editInpatientAccount.action'/>",  
		        onSubmit:function(){ 
		        	
		            if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
		        },  
		        success:function(data){ 
		        	$.messager.alert('提示',"保存成功！");
		        	window.location="<c:url value='/inpatient/account/addInpatientAccount.action'/>";
		        	/*clearValue();
		        	$("#detailPaytype").combobox("setText","");
		        	$("#detailDebitamount").val("");
		        	$('#list').datagrid('reload');*/
		        },
				error : function(data) {
					$.messager.alert('提示',"保存失败！");	
				}			         
		    }); 
	}
	/**
	 * 回车键查询
	 * @author  lt
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-07-1
	 * @version 1.0
	 */
	function KeyDown1()  
	{  
	    if (event.keyCode == 13)  
	    {  
	        event.returnValue=false;  
	        event.cancel = true;  
	        getPatient();  
	    }  
	} 
	/**
		 * 返还
		 * @author  lt
		 * @date 2015-7-1 10:53
		 * @version 1.0
		 */
	function backMoney(){
		var row = $("#alist").datagrid("getSelections"); 
		var i = 0;    
		var getid = ""; 
		var getDetailOptype;
		if(row.length!=1){
		    $.messager.alert("操作提示", "请选择一条用户记录！","warning");
		    return null;
		}else{ 
			for(i;i<row.length;i++){ 
				getid = row[i].id;
				getDetailOptype = row[i].detailOptype;
			}
		}
		if(getDetailOptype != 1){
		    $.messager.alert("操作提示", "只能对收款条目进行操作！","warning");
		    return null;
		}
		$("#detailOptype").val(2);
		submit(getid);
	}
	/**
		 * 补打
		 * @author  lt
		 * @date 2015-7-1
		 * @version 1.0
		 */
	function latePlay(){
		var row = $("#alist").datagrid("getSelections"); 
		var i = 0;    
		var getid = ""; 
		var getDetailOptype ="";
		if(row.length!=1){
		    $.messager.alert("操作提示", "请选择一条用户记录！","warning");
		    return null;
		}else{  
			for(i;i<row.length;i++){ 
				getid = row[i].id;
				getDetailOptype = row[i].detailOptype;
			}
		}
		if(getDetailOptype != 1){
		    $.messager.alert("操作提示", "只能对收款条目进行操作！","warning");
		    return null;
		}
		$("#detailOptype").val(3);
		submit(getid);
	}
	
	/**
		 * 结清账户
		 * @author  lt
		 * @date 2015-7-1
		 * @version 1.0
		 */
	function total(){
		$.ajax({
			url: "<c:url value='/inpatient/account/checkInpatientAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
			type:'post',
			success: function(data) {
			var dataObj = eval("("+data+")");
				if(dataObj!="yes"){
					$.messager.confirm('确认', '确定结清本账户吗?', function(res){//提示是否结清
					if (res){
						$.ajax({
							url: "<c:url value='/inpatient/account/totalInpatientAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
							type:'post',
							success: function(data) {
								$('#alist').datagrid('reload'); 
								$.messager.alert('提示',"账户已结清，账户金额为：" + data);
							}
							});	
						}
					});
				}else{
					$.messager.alert("操作提示", "此账户已结清！","warning");
				}
			}
		});	
		
		
	}
	
	/**
		 * 注销账户
		 * @author  lt
		 * @date 2015-7-1
		 * @version 1.0
		 */
	function zhuxiao(){
		
			$.ajax({
					url: "<c:url value='/inpatient/account/checkInpatientAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
					type:'post',
					success: function(data) {
					var dataObj = eval("("+data+")");
						if(dataObj!="yes"){
							$.ajax({
							url: "<c:url value='/inpatient/account/totalInpatientAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
							type:'post',
							success: function(data) {
								$.messager.confirm('确认', '确定注销本账户吗?', function(res){//提示是否结清
								if (res){
									$.ajax({
											url: "<c:url value='/inpatient/account/zhuxiaoInpatientAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
											type:'post',
											success: function(data) {
												var dataObj = eval("("+data+")");
												if(dataObj == "no"){
													$.messager.alert('提示',"注销失败！");
												}else if(dataObj == "yes"){
													$.messager.alert('提示',"注销成功！");
													window.location="<c:url value='/inpatient/account/addInpatientAccount.action'/>";
												}
											}
								});
								}
							});
							}
							});
						}else{
							$.messager.confirm('确认', '确定注销本账户吗?', function(res){//提示是否结清
								if (res){
									$.ajax({
											url: "<c:url value='/inpatient/account/zhuxiaoInpatientAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
											type:'post',
											success: function(data) {
												var dataObj = eval("("+data+")");
												if(dataObj == "no"){
													$.messager.alert('提示',"注销失败！");
												}else if(dataObj == "yes"){
													$.messager.alert('提示',"注销成功！");
													window.location="<c:url value='/inpatient/account/addInpatientAccount.action'/>";
												}
											}
								});
								}
							});
						}
					}
				});	
	}
	/**
		 * 启用，停用账户
		 * @author  lt
		 * @date 2015-7-1
		 * @version 1.0
		 */
	function stopFlg(stopFlg){
		var str = "";
			if(stopFlg==0){
				str = "启用";
			}else if(stopFlg==1){
				str = "停用";
			}
		
			$.ajax({
				url: 'inpatient/account/checkInpatientAccount.action?accountId='+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
				type:'post',
				success: function(data) {
					var dataObj = eval("("+data+")");
					if(dataObj == "no"){
						$.ajax({
							url: 'inpatient/account/totalInpatientAccount.action?accountId='+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
							type:'post',
							success: function(data) {
								$.messager.confirm('确认', '确定'+str+'本账户吗?', function(res){//提示是否结清
									if (res){
										$.ajax({
												url: 'inpatient/account/stopFlgInpatientAccount.action?stopFlg='+ stopFlg +'&accountId='+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
												type:'post',
												success: function(data) {
													var dataObj = eval("("+data+")");
													if(dataObj == "no"){
														$.messager.alert('提示',str + "失败！");
													}else if(dataObj == "yes"){
														$.messager.alert('提示',str + "成功！");
														window.location="inpatient/account/addInpatientAccount.action";
													}
												}
									});
									}
								});
							}
						});	
					}else if(dataObj == "yes"){
						$.messager.confirm('确认', '确定'+str+'本账户吗?', function(res){//提示是否结清
							if (res){
								$.ajax({
										url: 'inpatient/account/stopFlgInpatientAccount.action?stopFlg='+ stopFlg +'&accountId='+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
										type:'post',
										success: function(data) {
											var dataObj = eval("("+data+")");
											if(dataObj == "no"){
												$.messager.alert('提示',str + "失败！");
											}else if(dataObj == "yes"){
												$.messager.alert('提示',str + "成功！");
												window.location="inpatient/account/addInpatientAccount.action";
											}
										}
							});
							}
						});
					}
				}
		});	
	}
	/**
		 * 自动显示找零
		 * @author  lt
		 * @date 2015-7-2
		 * @version 1.0
		 */
	function sumMoney(){
		var totalMoney = $("#totalMoney").val();
		var detailDebitamount = $("#detailDebitamount").val();
		$("#banckMoney").val(totalMoney-detailDebitamount);
	}
	/**
	 * 性别列表页 显示
	 * @author  lt
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-06-29
	 * @version 1.0
	 */
	function sexFamater(value){
		if(value!=null){
			for(var i=0;i<sexList.length;i++){
				if(value==sexList[i].id){
					return sexList[i].name;
				}
			}	
		}
	}
	/**
	 * 证件类型列表页 显示
	 * @author  lt
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-06-29
	 * @version 1.0
	 */
	function certiFamater(value){
		if(value!=null){
			for(var i=0;i<certiList.length;i++){
				if(value==certiList[i].id){
					return certiList[i].name;
				}
			}
		}
			
	}
	/** 民族列表页 显示
	 * @author  lt
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-06-29
	 * @version 1.0
	 */
	function nationFamater(value){
		if(value!=null){
			for(var i=0;i<nationList.length;i++){
				if(value==nationList[i].id){
					return nationList[i].name;
				}
			}
		}	
	}
	</script>
	</body>
</html>