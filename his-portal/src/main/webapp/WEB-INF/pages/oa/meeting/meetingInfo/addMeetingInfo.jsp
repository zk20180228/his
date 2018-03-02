<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>会议室编辑</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px; padding: 0px">
		<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',border:false,fit:true">
			<div style="padding:5px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="oaMeetingInfo.id" value="${oaMeetingInfo.id }">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
						<tr>
							<td class="honry-lable">所属院区：</td>
			    			<td class="honry-info">
			    			<input class="easyui-combobox" style="width:95%" id="areaCode" name="oaMeetingInfo.areaCode" value="${oaMeetingInfo.areaCode }" 
			    				data-options="required:true,panelHeight:'80',editable:false"
			    				missingMessage="请选择院区"/>
<!--    			    		valueField: 'value',textField: 'text',data:[{value: '郑东院区',text: '郑东院区'},{value: '河医院区',text: '河医院区'},{value: '惠济院区',text: '惠济院区'}]"  -->
			    			</td>
		    			</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室编号：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="meetCode" name="oaMeetingInfo.meetCode" value="${oaMeetingInfo.meetCode }" data-options="required:true" missingMessage="请输入编号" /></td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室名称：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="meetName" name="oaMeetingInfo.meetName" value="${oaMeetingInfo.meetName }" data-options="required:true" missingMessage="请输入名称" /></td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室地点：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="meetPlace" name="oaMeetingInfo.meetPlace" value="${oaMeetingInfo.meetPlace }" data-options="required:true" missingMessage="请输入名称" /></td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">容纳人数：</td>
			    			<td class="honry-info"><input class="easyui-numberbox" style="width:95%" id="meetNumber" name="oaMeetingInfo.meetNumber" value="${oaMeetingInfo.meetNumber }"  /></td>
			    		</tr>
		    			<tr>
			    			<td class="honry-lable">会议室状态：</td>
			    			<td class="honry-info">
<%-- 			    			<input class="easyui-textbox" style="width:95%" id="meetState" name="oaMeetingInfo.meetState" value="${oaMeetingInfo.meetState }" data-options="required:true" missingMessage="请输入名称" /> --%>
			    			<input class="easyui-combobox" style="width:95%" id="meetState" name="oaMeetingInfo.meetState" value="${oaMeetingInfo.meetState }" 
			    					data-options="required:true,panelHeight:'60',editable:false, valueField: 'value',textField: 'text',data:[{value: 'Y',text: '正常'},{value: 'N',text: '维修'}]"
			    					missingMessage="请选择会议室状态"/>
			    			</td>
			    		</tr>
		    			<tr>
			    			<td class="honry-lable">会议室类型：</td>
			    			<td class="honry-info">
			    				<input id="meetType" name="oaMeetingInfo.meetType" style="width:95%" value="${oaMeetingInfo.meetType }"  
			    					class="easyui-combobox" readonly="readonly"/>
			    			</td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室管理员：</td>
			    			<td class="honry-info">
			    				<input id="meetAdminCode" type="hidden" value="${oaMeetingInfo.meetAdminCode }" name="oaMeetingInfo.meetAdminCode" />
			    				<input class="easyui-combobox" id="meetAdmin" style="width:95%"
								value="${oaMeetingInfo.meetAdmin }"
								name="oaMeetingInfo.meetAdmin" data-options="required:true" missingMessage="请选择会议室管理员" ></input>
			    			</td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室设备情况：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="meetEquipment" name="oaMeetingInfo.meetEquipment" value="${oaMeetingInfo.meetEquipment }" /></td>
			    		</tr>
<!-- 		    			<tr> -->
<!-- 							<td class="honry-lable">是否有投影：</td> -->
<!-- 							<td> -->
<%-- 							<input class="easyui-combobox" style="width:95%" id="meetProjector" name="oaMeetingInfo.meetProjector" value="${oaMeetingInfo.meetProjector }" data-options="panelHeight:'60',valueField: 'value',textField: 'text',data:[{value: 'Y',text: '是'},{value: 'N',text: '否'}] "/> --%>
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 				    	<tr> -->
<!-- 							<td class="honry-lable">是否有音响：</td> -->
<!-- 			    			<td class="honry-info"> -->
<%-- 							<input class="easyui-combobox" style="width:95%" id="meetSound" name="oaMeetingInfo.meetSound" value="${oaMeetingInfo.meetSound }" data-options="panelHeight:'60',valueField: 'value',textField: 'text',data:[{value: 'Y',text: '是'},{value: 'N',text: '否'}] "/> --%>
<!-- 							</td> -->
<!-- 		    			</tr> -->
		    			<tr>
							<td class="honry-lable">是否可申请 ：</td>
			    			<td class="honry-info">
							<input class="easyui-combobox" style="width:95%" id="meetIsapply" name="oaMeetingInfo.meetIsapply" value="${oaMeetingInfo.meetIsapply }" 
								data-options="required:true,panelHeight:'60',editable:false, valueField: 'value',textField: 'text',data:[{value: 'Y',text: '是'},{value: 'N',text: '否'}]"
								missingMessage="请选择会议室是否可申请"/>
							</td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">联系方式 ：</td>
			    			<td class="honry-info">
			    			<input class="easyui-numberbox" style="width:95%" id="meetPhone" name="oaMeetingInfo.meetPhone" value="${oaMeetingInfo.meetPhone }"/>
							</td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">会议室描述：</td>
			    			<td>
			    			<textarea class="easyui-textbox" style="width:95%;height: 60px" id="meetDescribe"  name="oaMeetingInfo.meetDescribe"
								data-options="multiline:true">${oaMeetingInfo.meetDescribe }</textarea>
			    			</td>
		    			</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
<%-- 			     <c:if test="${frequency.id==null }"> --%>
<!-- 			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a> -->
<%-- 			    </c:if> --%>
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
			    <div id="roleWins"></div>
	    	</form>
	    </div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<style type="text/css">
		.window .panel-header .panel-tool .panel-tool-close{
  	  		background-color: red;
  	  	}
	</style>
	<script type="text/javascript">
	$(function(){
		/**
		 * 会议室下拉
		 */
		$('#areaCode').combobox({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=hospitalArea'/>",   
		    valueField:'name',    
		    textField:'name',
		});
		//会议室类型
		$('#meetType').combobox({
		 	url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=meetType'/>", 
		    valueField:'name',     
		    textField:'name'
		});	
		//会议室管理员
		$('#meetAdmin').combobox({    
			url:'<%=basePath%>baseinfo/employee/employeeCombobox.action',   
		    valueField:'name',    
		    textField:'name',
		    filter:function(q,row){
		    	var keys = new Array();
				keys[keys.length] = 'name';
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'pinyin';
				return filterLocalCombobox(q, row, keys);
		    },
		    onSelect:function(record){
		    	$('#meetAdminCode').val(record.jobNo);
		    }
		});
	});
	
		function hospitalName(map){
			var code=$('#hospitalCode').val();
			var noCode=$('#nohospitalCode').val();
			if(code){
				var codes=code.split(',');
				var len=codes.length;
				var name='';
				for(var a=0;a<len;a++){
					if(map.get(codes[a])){
						name=name+map.get(codes[a])+',';
					}
				}
				$('#hospital').val(name.substring(0, name.length-1));
			}
			if(noCode){
				var codes1=noCode.split(',');
				var len=codes1.length;
				var name1='';
				for(var i=0;i<len;i++){
					if(map.get(codes1[i])){
						name1=name1+map.get(codes1[i])+',';
					}
					
				}
				$('#nonHospital').val(name1.substring(0, name1.length-1));
			}
		}
		/**
		 * 页面加载
		 * @author  zxh
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		 var hospitalMap=new Map();
	
		
	
		/**
		 * 表单提交
		 * @author  zxh
		 * @date 2017-07-15
		 * @version 1.0
		 */
		function submit(){ 
// 	 		var meetProjector=$('#meetProjector').combobox('getValue');//是否有投影
// 	 		var meetSound=$('#meetSound').combobox('getValue');//是否有音响
// 	 		var meetIsapply=$('#meetIsapply').combobox('getValue');//是否可申请
// 			var areaCode=$('#areaCode').val();//所属院区
// 			var meetName=$('#meetName').val();//会议室名称
// 			var meetNumber=$('#meetNumber').numberbox('getValue');//容纳人数
// 			var meetType=$('#meetType').val();//会议室类型
// 			var meetAdmin=$('#meetAdmin').val();//会议室管理员
// 			var meetPhone=$('#meetPhone').numberbox('getValue');//联系方式
		    $('#editForm').form('submit',{  
		        url:"<c:url value='/meeting/meetingInfo/saveMeetingInfo.action'/>",
		        onSubmit:function(param){
		        	if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
		        },  
		        success:function(data){ 
		        	if("success"==data){
			        	$.messager.alert('提示信息','保存成功！','ok');
		        		$('#divLayout').layout('remove','east');
		        		//实现刷新栏目中的数据
	                    $('#list').datagrid('reload');
		        	}else{
			        	$.messager.alert('提示信息','保存失败！'+data);
		        	}
		        },
				error : function(data) {
					$.messager.alert('提示信息','保存失败！','warning');
				}							         
		    });
	    }	    
	    
		/**
		 * 连续添加
		 * @author  zxh
		 * @date 2015-6-1 10:58
		 * @version 1.0
		 */
		function addContinue(){
					var encode=$('#encode').val();
					var frequencyid=$('#id').val();
					var hospital=$('#hospital').val();//适用医院
					if(''==hospital||null==hospital){
						 $.messager.alert('提示信息','可用医院不能为空');
						 setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						 return false;
					 }
					var nonHospital=$('#nonHospital').val();//不适用医院
					var order=$('#order').val();
					$.ajax({
						url: "<c:url value='/baseinfo/frequency/queryFrequencyEncode.action'/>",
						type:'post',
						data:{encodet:encode,frequencyid:frequencyid},
						success: function(data) {
							if (data=="0") {
							    $('#editForm').form('submit',{  
							        url:"<c:url value='/baseinfo/frequency/saveFrequency.action'/>", 
							        onSubmit:function(param){
										if (!$('#editForm').form('validate')) {
											$.messager.show({
												title : '提示信息',
												msg : '验证没有通过,不能提交表单!'
											});
											$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											return false;
										}
										param.ordert = order;    
							            param.hospitalt =hospital; 
							            param.nonHospitalt =nonHospital; 
							        },  
							        success:function(data){ 
							        	var arr=data.split(',');
							        	if(arr[0]=='Y'){
							        		$.messager.alert('提示信息',hospitalMap.get(arr[1])+'适用医院重复,请查看','warning');
							        		setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
							        		return false;
							        	}else if(arr[0]=='N'){
							        		$.messager.alert('提示信息',hospitalMap.get(arr[1])+'不适合医院重复,请检查','warning');
							        		setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
							        		return fasle;
							        	}else{
							        		$.messager.alert('提示信息','保存成功！');
								            //实现刷新栏目中的数据
								             clear();
								           //实现刷新栏目中的数据
						                     $('#list').datagrid('reload');
						                     add();
							        	}
							        		
							        },
									error : function(data) {
										$.messager.alert('提示信息','保存失败！');
									}							         
							    }); 
							}else{
								$('#encode').textbox('setText','');
								$.messager.alert('警告！','该代码已存在请重新填写！','warning');
							}
						}
					});	
				 
	    }	    
		/**
		 * 清除页面填写信息
		 * @author  zxh
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		function clear(){
			 var order = $('#order').val();
			$('#editForm').form('clear');
			$('#order').textbox('setText',order);
		}
		/**
		 * 关闭编辑窗口
		 * @author  zxh
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		/**
		 * 复选框选中
		 * @param defalVal 默认值
		 * @param selVal 选中值
		 * @author  zxh
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		function checkBoxSelect(obj,defalVal,selVal){
			var name = obj.id+"s";
			var element = document.getElementById(name);
			if(obj.checked==true){
				element.value=selVal;
			}else{
				element.value=defalVal;
			}
		}
		//加载模式窗口
		function Adddilog(title, url, width, height) {
			$('#roleWins').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		//适用医院选择弹窗
		function alertdialoghospital(){
			var hospitalCode=$('#hospitalCode').val();
			var nohospitalCode=$('#nohospitalCode').val();
			Adddilog("适用医院","<c:url value='/baseinfo/frequency/alertdilog.action'/>?type="+"yes"+"&hospitalCode="+hospitalCode+"&nohospitalCode="+nohospitalCode,'87%','88%');
		}
		//不适用医院选择弹窗
		function alertdialognonHospital(){
			var hospitalCode=$('#hospitalCode').val();
			var nohospitalCode=$('#nohospitalCode').val();
			Adddilog("不适用医院","<c:url value='/baseinfo/frequency/alertdilog.action'/>?type="+"no"+"&hospitalCode="+hospitalCode+"&nohospitalCode="+nohospitalCode,'87%','88%');
		}
		/**
		 * 打开用法界面弹框
		 * @author  zxh
		 * @date 2017-07-15
		 * @version 1.0
		 */
		
		function popWinToUseage(){
			
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?textId=useMode&type=useage";
			var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
					
		}
		
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>