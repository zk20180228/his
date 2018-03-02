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
		<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form'" style="width:400px" >
			<input type="hidden" id="id" name="id" value="${patientBlack.id }">
			<form id="editForm" method="post" >
			
				<table class="honry-table patientBlackEditFont" cellpadding="0" cellspacing="1" border="0" align="center" style="border-left:0">
					<tr>
						<td class="honry-lable">
							<span>患者姓名:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="patientName" type="hidden" name="patientId" value="${patientBlack.patient.id}" style="width:60%"></input>
							<input id="patientNameText" editable="false" prompt="请回车查询患者" value="${patientBlack.patient.patientName}" style="width:60%" data-options="required:true" missingMessage="患者名称"></input>
							
							<a href="javascript:delSelectedData('patientNameText');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					  </tr>
					  <tr>	
						<td class="honry-lable">
							<span>病历号:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="medicalrecordId" class="easyui-textbox"  value="${patientBlack.medicalrecordId}" name="patientBlack.medicalrecordId" style="width:60%" readonly="readonly"></input>
						</td>
					  </tr>
					  <tr>
				  	    <td class="honry-lable">
							<span>类型:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="blacklistType" editable="false" value="${patientBlack.blacklistType}" name="patientBlack.blacklistType"  style="width:60%" ></input>
						</td>
					  </tr>
					  <tr>	
						<td class="honry-lable">
							<span>有效开始:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="blacklistStarttime" class="Wdate" type="text" value="${patientBlack.blacklistStarttimeView}" name="patientBlack.blacklistStarttime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'blacklistEndtime\')}'})" style="width:60%"/>
<%-- 							<input id="blacklistStarttime"  class="easyui-datebox" value="${patientBlack.blacklistStarttime}" name="patientBlack.blacklistStarttime" style="width:60%" data-options="onSelect:onSelect"></input> --%>
						</td>						
					  </tr>
					  
					   <tr>	
						<td class="honry-lable">
							<span>有效结束:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
<%-- 							<input id="blacklistEndtime" class="easyui-datebox"  value="${patientBlack.blacklistEndtime}" name="patientBlack.blacklistEndtime" style="width:60%" data-options="onSelect:onSelect"></input> --%>
							<input id="blacklistEndtime" class="Wdate" type="text" value="${patientBlack.blacklistEndtimeView}" name="patientBlack.blacklistEndtime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'{%y+1}-%M-%d',minDate:'#F{$dp.$D(\'blacklistStarttime\')}'})" style="width:60%"/>
						</td>						
					  </tr>
					   <tr>	
						<td class="honry-lable">
							<span>进入黑名单原因:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="blacklistIntoreason" class="easyui-textbox" value="${patientBlack.blacklistIntoreason}" name="patientBlack.blacklistIntoreason" style="width:60%" ></input>
						</td>						
					  </tr>
					   <tr>	
						<%-- <td class="honry-lable">
							<span style="font-size: 14">退出黑名单原因:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="blacklistOutreason"  type="text" value="${patientBlack.blacklistOutreason}" name="patientBlack.blacklistOutreason" style="width:60%" ></input>
						</td>	 --%>					
					  </tr>
				</table>
				 <div style="text-align:center;padding:5px">
			    	<c:if test="${empty patientBlack.id}">			    	
			    	<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    	<div id="patientBlackDiv"></div>
		</div>
		<script type="text/javascript">
			$(function(){
				/* $('#patientName').combogrid({    
				    panelWidth:430,    
				    idField:'id',  
				    textField:'patientName',
				    mode:'remote',
				    url:"<c:url value='/patient/patientBlack/queryIdcard.action'/>",    
				    columns:[[    
				        {field:'id',title:'id',hidden:true},    
				        {field:'patientName',title:'患者名称',formatter:function(value,row,index){
							if (row.patient){
								return row.patient.patientName;
							} else {
								return value;
							}
						},width:'15%'},  
						{field:'patientSex',title:'性别',width:'10%'},
				        {field:'idcardNo',title:'卡号',width:'25%'},    
				        {field:'patientCertificatesno',title:'证件号',width:'40%'},
				        {field:'medicalrecordId',title:'病历号',hidden:true},
				    ]] ,
				    onClickRow : function(rowIndex, rowData) {
				    	$("#medicalrecordId").textbox('setValue',rowData.medicalrecordId);
				    }
				}); */
				$('#patientNameText').textbox({
					
				})
				bindEnterEvent('patientNameText',popWinToEmp,'easyui');//绑定回车事件
			});
<%--			function keyUp() {  --%>
<%--				if(navigator.appName == "Microsoft Internet Explorer"){--%>
<%--			    	var keycode = event.keyCode; --%>
<%--		        }else{--%>
<%--			   	 	var keycode =  keyUp.caller.arguments[0].which;--%>
<%--		        }--%>
<%--				if(keycode==32){--%>
<%--					Adddilog("选择患者", "<c:url value='/patient/patientBlack/selecPatientUrl.action'/>",'patientBlackDiv');--%>
<%--				}--%>
<%--				--%>
<%--		    }--%>
			/**
			 * 表单提交
			 */
			function submit(flg) {
				var id="${patientBlack.id }";
				$('#editForm').form('submit', {
					
					url : "<c:url value='/patient/patientBlack/saveOrUpdatePatientBlack.action?id='/>"+id,
					onSubmit : function() {
						if (!$('#editForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
					},
					success : function(data) {
						if (flg == 0) {
							$.messager.alert("操作提示","保存成功！");
							$('#divLayout').layout('remove', 'east');
							$("#list").datagrid("reload");
						} else if (flg == 1) {
							//清除editForm
							$('#editForm').form('reset');
							$("#list").datagrid("reload");
						}
					},
					error : function(data) {
						$.messager.alert("操作提示","保存失败！","warning");
					}
				});
			}
			 //患者黑名单类型下拉框
			$('#blacklistType').combobox({
				url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=blacklisttype'/>",
				valueField:'encode',
				textField:'name',
				multiple:false,
				onLoadSuccess: function (list) {	    
				  $(list).each(function(){
					  if ($("#family").val() == this.id) {
					     $("#family").combobox("select",this.id);
					   }
				  });
			    }
			});
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
			/**
			 * 清除页面填写信息
			 * @author  zpty
			 * @date 2015-6-2 10:53
			 * @version 1.0
			 */
			function clear(){
				$('#editForm').form('clear');
			}
			
			/**
			* 回车弹出患者黑名单选择窗口
			* @author  zhuxiaolu
			* @param textId 页面上commbox的的id
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/
			function popWinToEmp(){
				popWinEmpCallBackFn = function(node){
			    	$("#patientName").val(node.id);
					 $('#patientNameText').textbox('setValue',node.patientName);
					 $("#medicalrecordId").textbox('setValue',node.medicalrecordId);
				};
				var tempWinPath = "<%=basePath%>popWin/popWinPatientBlack/toPatientBlackPopWin.action?textId=patientNameText";
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
			+',scrollbars,resizable=yes,toolbar=yes')
			}
			
			/*  function onSelect(d) {
			        var issd = this.id == 'blacklistStarttime', blacklistStarttime = issd ? d : new Date($('#blacklistStarttime').datebox('getValue')), blacklistEndtime = issd ? new Date($('#blacklistEndtime').datebox('getValue')) : d;
			            if (blacklistStarttime > blacklistEndtime) {
			            	$.messager.alert("操作提示","结束日期小于开始日期","warning");
			                //只要选择了日期，不管是开始或者结束都对比一下，如果结束小于开始，则清空结束日期的值并弹出日历选择框
			                $('#blacklistEndtime').datebox('setValue', '').datebox('showPanel');
			            }
			        } */
		</script>
	</body>
</html>