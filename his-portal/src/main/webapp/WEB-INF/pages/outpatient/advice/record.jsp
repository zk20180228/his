<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body style="margin:0px;padding:0px">
		<form id="recordFrom" method="post">
			<div style="padding:5px 5px 5px 5px;">
				<input id="cz" type="radio" value="0" checked="checked" name="diagnoseType">
				初诊
				<input id="fz" type="radio" value="1" name="diagnoseType">
				复诊
				<input id="jz" type="radio" value="3" name="diagnoseType">
				急诊&nbsp;
				发病日期:
				<input id="diagnoseDate" class="Wdate" type="text" name="diagnoseDate" data-options="showSeconds:true" onClick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:130px;border: 1px solid #95b8e7;border-radius: 5px;" class="changeskin"/>
				<shiro:hasPermission name="${menuAlias}:function:saveRecordModel">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#windowOpenCase').window('open')" data-options="iconCls:'icon-save'" style="width:120px;">保存模板</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:importRecor">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openWindowCase()" data-options="iconCls:'icon-add'" style="width:120px;">导入模板</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:saveRecord">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveRecord()" data-options="iconCls:'icon-save'" style="width:126px;">患者病历保存</a>
				</shiro:hasPermission>
			</div>
			<div style="padding:0px 5px 5px 5px;">
				<table class="tableCss" width="100%" style="border-right:1px solid #95b8e7x;border-top:1px solid #95b8e7;border-left:1px solid #95b8e7;">
					<tr>
						<td class="tableLabel">
							主诉:
						</td>
						<td colspan="4">
							<input style="width: 90%; height: 50px" id="maindesc" name="maindesc" onkeydown="KeyDown(1)"  class="easyui-textbox" data-options="required:true" />
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(1)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							过敏史:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 50px" id="allergichistory" name="allergichistory" onkeydown="KeyDown(2)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(2)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							家族遗传史:
						</td>
						<td colspan="4">
							<input class="easyui-textbox" style="width: 90%; height: 50px" id="heredityHis" name="heredityHis" onkeydown="KeyDown(3)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(3)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							现病史:
						</td>
						<td colspan="4">
							<input class="easyui-textbox" style="width: 90%; height: 50px" id="presentillness" name="presentillness" onkeydown="KeyDown(4)" />
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(4)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td rowspan="3" class="tableLabel">
							体格检查:
						</td>
						<td>
							体温：
						</td>
						<td>
							<input class="easyui-numberbox" id="temperature" name="temperature" onkeydown="KeyDown(5)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(5)" data-options="iconCls:'icon-add'"></a>
						</td>
						<td>
							脉搏：
						</td>
						<td>
							<input class="easyui-numberbox" id="pulse" name="pulse" onkeydown="KeyDown(6)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(6)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td>
							呼吸：
						</td>
						<td>
							<input class="easyui-numberbox" id="breathing" name="breathing" onkeydown="KeyDown(7)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(7)" data-options="iconCls:'icon-add'"></a>
						</td>
						<td>
							血压：
						</td>
						<td>
							<input class="easyui-textbox" type="text" id="bloodPressure" name="bloodPressure" onkeydown="KeyDown(8)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(8)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 45px" id="physicalExamination" name="physicalExamination" onkeydown="KeyDown(9)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(9)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							校验检查:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 50px" id="checkresult" name="checkresult" onkeydown="KeyDown(10)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(10)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							诊断检查:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" data-options="required:true" style="width: 90%; height: 50px" id="diagnose1" name="diagnose1" onkeydown="KeyDown(11)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(11)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							医嘱建议:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 50px" id="advice" name="advice" onkeydown="KeyDown(12)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(12)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							病史和特征:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 50px" id="historyspecil" name="historyspecil" onkeydown="KeyDown(13)"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openTreeWindow(13)" data-options="iconCls:'icon-add'"></a>
						</td>
					</tr>
				</table>
			</div>
		</form>
		<div id="recordWin" class="easyui-window" title="病历模板" style="width:660px;height:400px" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,resizable:false,center:true,closed:true">   
		    <div class="easyui-layout" data-options="fit:true"  style="width:100%;height:100%;">   
			    <div data-options="region:'west',split:false" style="width:135px;">
			    	<div id="treeId"></div>
			    </div>   
			    <div data-options="region:'center',fit:true,border:false">
		    		<table id="dgTable"></table>
			    </div>   
			</div> 
		</div>
		<div id="recordTypeWin" class="easyui-window" title="模板" data-options="modal:true,closed:true,iconCls:'icon-add',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:60%;height:40%;">
	    	<div id="treeWindowId"></div>
		</div>
		<div id="windowOpenCase" class="easyui-window" title="病历分类" data-options="modal:true,closed:true,iconCls:'icon-add',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:490px;height:73px;">
			<table id="listw" class="tableCss">
				<tr align="center">
					<td class="tableLabel">病历类型:</td>
					<td>
						<select id="recordType" class="easyui-combobox" name="recordTypes" style="width:200px;">   
						    <option value="3">个人病历</option> 
						    <option value="2">科室病历</option>  
						    <option value="1">全院病历</option>   
						</select>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveRecordModel()" data-options="iconCls:'icon-save'">保存模板</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
			</table> 
		</div>
		<script type="text/javascript">
			/**  
			 *  
			 * 打开病历模板
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 * @param:type 1待诊2已诊
			 *
			 */
			function openWindowCase(){
				$('#recordWin').window('open');
				//加载模板树
			   	$('#treeId').tree({    
				    url:"<%=basePath%>outpatient/medicalRecordModel/medicalRecordTree.action",
				    method:'get',
				    animate:true,
				    lines:true,
				    formatter:function(node){//统计节点总数
				    	var s = node.text;
						if (node.children){
							if(node.children.length!=0){
								s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
							}
						}
						return s;
					},onClick:function(node){//单击节点
						$('#dgTable').datagrid('load', {
							recordType: node.id
						});
					}
				});
				
				//加载模板表格
				$('#dgTable').datagrid({    
				   	striped:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					fitColumns:false,
					rownumbers:true,
					fit:true,
					url:"<%=basePath%>outpatient/medicalRecordModel/medicalRecordList.action",
				    columns:[[
				        {field:'maindesc',title:'主诉',width:50},    
				        {field:'allergichistory',title:'过敏史',width:50},    
				        {field:'heredityHis',title:'家族遗传史',width:50},
				        {field:'historyspecil',title:'病史和特征',width:50},  
				        {field:'presentillness',title:'现病史',width:50},  
				        {field:'temperature',title:'体温',width:50},  
				        {field:'pulse',title:'脉搏',width:50},  
				        {field:'breathing',title:'呼吸',width:50},  
				        {field:'bloodPressure',title:'血压',width:50},  
				        {field:'physicalExamination',title:'体格检查',width:50},
				        {field:'checkresult',title:'检查检验结果',width:50},
				        {field:'diagnose1',title:'诊断1',width:50},   
				        {field:'advice',title:'医嘱建议',width:50},   
				        {field:'remark',title:'备注',width:50}  
				    ]],
				    onDblClickRow: function (rowIndex, rowData) {//双击查看
						$('#maindesc').textbox('setValue',rowData.maindesc);
						$('#allergichistory').textbox('setValue',rowData.allergichistory);
						$('#heredityHis').textbox('setValue',rowData.heredityHis);
						$('#diagnose1').textbox('setValue',rowData.diagnose1);
						$('#presentillness').textbox('setValue',rowData.presentillness);
						$('#temperature').numberbox('setValue',rowData.temperature);
						$('#pulse').numberbox('setValue',rowData.pulse);
						$('#breathing').numberbox('setValue',rowData.breathing);
						$('#bloodPressure').textbox('setValue',rowData.bloodPressure);
						$('#physicalExamination').textbox('setValue',rowData.physicalExamination);
						$('#checkresult').textbox('setValue',rowData.checkresult);
						$('#advice').textbox('setValue',rowData.advice);
						$('#historyspecil').textbox('setValue',rowData.historyspecil);
						$('#recordWin').window('close');
					}
				});  
			}
		
			/**  
			 *  
			 * 打开病历模板单项
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function openTreeWindow(type){
				$('#recordTypeWin').window('open');
				$('#treeWindowId').tree({    
				    url:"<%=basePath%>outpatient/medicalRecordModel/medicalRecordOtherTree.action?type="+type,
				    method:'get',
				    animate:true,
				    lines:true,
				    formatter:function(node){//统计节点总数
				    	var s = node.text;
						if (node.children){
							if(node.children.length!=0){
								s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
							}
						}
						return s;
					}
					,onClick:function(node){//单击节点
						if(node.attributes.isNO=="1"){
							if(type==1){
								if($('#maindesc').textbox('getValue')!=""){
									var maindescs = $('#maindesc').textbox('getValue');
									$('#maindesc').textbox('setValue',maindescs+','+node.text);
								}else if($('#maindesc').textbox('getValue')==""){
									$('#maindesc').textbox('setValue',node.text);
								}
								$('#recordTypeWin').window('close');
							}else if(type==2){
								if($('#allergichistory').textbox('getValue')!=""){
									var allergichistory = $('#allergichistory').textbox('getValue');
									$('#allergichistory').textbox('setValue',allergichistory+','+node.text);
								}else if($('#allergichistory').textbox('getValue')==""){
									$('#allergichistory').textbox('setValue',node.text);
								}
								$('#recordTypeWin').window('close');
							}else if(type==3){
								if($('#heredityHis').textbox('getValue')!=""){
									var heredityHis = $('#heredityHis').textbox('getValue');
									$('#heredityHis').textbox('setValue',heredityHis+','+node.text);
								}else if($('#heredityHis').textbox('getValue')==""){
									$('#heredityHis').textbox('setValue',node.text);
								}
								$('#recordTypeWin').window('close');
							}else if(type==4){
								if($('#presentillness').textbox('getValue')!=""){
									var presentillness = $('#presentillness').textbox('getValue');
									$('#presentillness').textbox('setValue',presentillness+','+node.text);
								}else if($('#presentillness').textbox('getValue')==""){
									$('#presentillness').textbox('setValue',node.text);
								}
								$('#recordTypeWin').window('close');
							}else if(type==5){
								$('#temperature').numberbox('setValue',node.text);
								$('#recordTypeWin').window('close');
							}else if(type==6){
								$('#pulse').numberbox('setValue',node.text);
								$('#recordTypeWin').window('close');
							}else if(type==7){
								$('#breathing').numberbox('setValue',node.text);
								$('#recordTypeWin').window('close');
							}else if(type==8){
								$('#bloodPressure').textbox('setValue',node.text);
								$('#recordTypeWin').window('close');
							}else if(type==9){
								if($('#physicalExamination').textbox('getValue')!=""){
									var physicalExamination = $('#physicalExamination').textbox('getValue');
									$('#physicalExamination').textbox('setValue',physicalExamination+','+node.text);
								}else if($('#physicalExamination').textbox('getValue')==""){
									$('#physicalExamination').textbox('setValue',node.text);
								}
								$('#recordTypeWin').window('close');
							}else if(type==10){
								if($('#checkresult').textbox('getValue')!=""){
									var checkresult = $('#checkresult').textbox('getValue');
									$('#checkresult').textbox('setValue',checkresult+','+node.text);
								}else if($('#checkresult').textbox('getValue')==""){
									$('#checkresult').textbox('setValue',node.text);
								}
								$('#recordTypeWin').window('close');
							}else if(type==11){
								if($('#diagnose1').textbox('getValue')!=""){
									var diagnose1 = $('#diagnose1').textbox('getValue');
									$('#diagnose1').textbox('setValue',diagnose1+','+node.text);
								}else if($('#diagnose1').textbox('getValue')==""){
									$('#diagnose1').textbox('setValue',node.text);
								}
								$('#recordTypeWin').window('close');
							}else if(type==12){
								if($('#advice').textbox('getValue')!=""){
									var advice = $('#advice').textbox('getValue');
									$('#advice').textbox('setValue',advice+','+node.text);
								}else if($('#advice').textbox('getValue')==""){
									$('#advice').textbox('setValue',node.text);
								}
								$('#recordTypeWin').window('close');
							}else if(type==13){
								if($('#historyspecil').textbox('getValue')!=""){
									var historyspecil = $('#historyspecil').textbox('getValue');
									$('#historyspecil').textbox('setValue',historyspecil+','+node.text);
								}else if($('#historyspecil').textbox('getValue')==""){
									$('#historyspecil').textbox('setValue',node.text);
								}
								$('#recordTypeWin').window('close');
							}
						}else{
							$.messager.alert('提示','请选择具体病历');
						}
					}
				});
			}
			
			/**  
			 *  
			 * 保存门诊病历
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function saveRecord(){
				var clinicNo = $('#clinicNoTdId').html();
				var diagnoseDate = $('#diagnoseDate').val();
				if(diagnoseDate==null||diagnoseDate==""){
					$.messager.alert('提示','请选择发病时间！');
				}else if(clinicNo==null||clinicNo==""){
					$.messager.alert('提示','当前没有患者，无法添加病历！');
				}else{
					$('#recordFrom').form('submit', {
						url:"<%=basePath%>outpatient/medicalrecord/medicalrecordSave.action",
						dataType:'json',
						onSubmit:function(param) {
							if (!$('#recordFrom').form('validate')) {
								$.messager.progress('close');
								$.messager.show({
									title : '提示信息',
									msg : '验证没有通过,不能提交表单!'
								});
								return false;
							}
							param.clinicCode = clinicNo;
							param.diagnoseDate = diagnoseDate;
							$.messager.progress({text:'保存中，请稍后...',modal:true});
						},
						success : function(data) {
							var dataMap = $.parseJSON(data);
							$.messager.alert('提示',dataMap.resCode);
							$.messager.progress('close');
						},
						error : function(data) {
							$.messager.alert('提示','保存失败！');
							$.messager.progress('close');
						}
					});
				}
			}
			
			/**  
			 *  
			 * 保存门诊病历模板
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function saveRecordModel(){
				$('#recordFrom').form('submit', {
					url : "<%=basePath%>outpatient/medicalRecordModel/saveMedicalRecord.action",
					dataType : 'json',
					onSubmit : function(param) {
						$.messager.progress('close');
						if (!$('#recordFrom').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
						param.recordType = $('#recordType').combobox('getValue');
						$.messager.progress({text:'保存中，请稍后...',modal:true});
					},
					success : function(data) {
						$.messager.progress('close');
						var dataMap = $.parseJSON(data);
						$.messager.alert('提示',dataMap.resCode);
						$('#windowOpenCase').window('close');
					},
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');
						$('#windowOpenCase').window('close');
					}
				});
			}
		</script>
	</body>
</html>