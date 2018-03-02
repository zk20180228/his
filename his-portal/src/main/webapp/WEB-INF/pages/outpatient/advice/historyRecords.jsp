<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div id="hisRecordEl" class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'west',title:'患者信息',split:false,border:false" style="width:20%;padding-top:5px;padding-left:5px">
				<input id="idCardNoHisRecord" style="width:110px;"/>
				<a href="javascript:void(0)" cardNo="" onclick="readThree()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>

<!-- 				<a href="javascript:void(0)" onclick="searchInfoHisRecord()" class="easyui-linkbutton" iconCls="icon-bullet_feed" style="height:24px;">读卡</a> -->
				<ul id="hisAdviceTreeRecord" style="padding-top:5px;"></ul>
			</div>   
			<div data-options="region:'center',title:'历史病历'" style="padding: 5px;">
				<table id="hisAdviceRecord" class="tableCss" width="100%" style="border-right:1px solid #95b8e7x;border-top:1px solid #95b8e7;border-left:1px solid #95b8e7;">
					<tr>
						<td class="tableLabel">
							主诉:
						</td>
						<td colspan="4">
							<input style="width: 90%; height: 50px" id="maindescRecord" name="maindesc"  class="easyui-textbox" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							过敏史:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 50px" id="allergichistoryRecord" name="allergichistory" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							家族遗传史:
						</td>
						<td colspan="4">
							<input class="easyui-textbox" style="width: 90%; height: 50px" id="heredityHisRecord" name="heredityHis" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							现病史:
						</td>
						<td colspan="4">
							<input class="easyui-textbox" style="width: 90%; height: 50px" id="presentillnessRecord" name="presentillness" readonly="readonly"/>
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
							<input class="easyui-numberbox" id="temperatureRecord" name="temperature" readonly="readonly"/>
						</td>
						<td>
							脉搏：
						</td>
						<td>
							<input class="easyui-numberbox" id="pulseRecord" name="pulse" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td>
							呼吸：
						</td>
						<td>
							<input class="easyui-numberbox" id="breathingRecord" name="breathing" readonly="readonly"/>
						</td>
						<td>
							血压：
						</td>
						<td>
							<input class="easyui-textbox" type="text" id="bloodPressureRecord" name="bloodPressure" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 45px" id="physicalExaminationRecord" name="physicalExamination" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							校验检查:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 50px" id="checkresultRecord" name="checkresult" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							诊断检查:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 50px" id="diagnose1Record" name="diagnose1" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							医嘱建议:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 50px" id="advice" name="adviceRecord" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="tableLabel">
							病史和特征:
						</td>
						<td colspan="4">
							<input  class="easyui-textbox" style="width: 90%; height: 50px" id="historyspecilRecord" name="historyspecil" readonly="readonly"/>
						</td>
					</tr>
				</table>		
			</div> 
		</div>
		<script type="text/javascript">	

			/*******************************开始读卡***********************************************/
			//定义一个事件（读卡）
			function readThree(){
				var card_value = app.read_ic();
				if(card_value=='0'||card_value==undefined||card_value==''){
					$.messager.alert('提示','此卡号['+card_value+']无效');
					return;
				}
				//填写就诊卡号
				$("#idCardNoHisRecord").textbox("setValue",card_value);
				searchInfoHisRecord();
			};
			/*******************************结束读卡***********************************************/

			$('#idCardNoHisRecord').textbox({
				prompt:'就诊卡号查询'
			});
			$("#hisAdviceTreeRecord").tree({
				animate:true,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children!=null&&node.children!=''&&node.children.length!=0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},onBeforeCollapse:function(node){
					if(node.id=="root"){
						return false;
					}
				},onDblClick:function(node){
					if(node.iconCls!='icon-reload'&&node.iconCls!='icon-table'){
						$.ajax({
							type:"post",
							url:"<%=basePath%>outpatient/advice/queryHistoryRecordsInfo.action",
							data:{clinicNo:node.id},
							success: function(dataMap) {
								if(dataMap.resMsg=='success'){
									if(dataMap.resCode.length==0){
										clearHistoryRecords();
										msgShow('提示','该挂号记录无医嘱信息！',3000);
										return;
									}
									queryHisAdviceRecord(dataMap.resCode[0]);
								}else{
									$.messager.alert('提示',dataMap.resCode);
								}
							},
							error: function(){
								$.messager.alert('提示','请求失败！');
							}
						});
					}else{//加载下一分区或表的历史数据
						if(node.iconCls!='icon-table'){
							$.ajax({
								type:"post",
								url:"<%=basePath%>outpatient/advice/queryHisAdviceNext.action",
								data:{id:node.attributes.isParDb,patientNo:node.id,para:node.attributes.time},
								success: function(dataMap) {
									if(dataMap.resMsg=='success'){
										if(dataMap.resCode.length>0){
											$('#hisAdviceTreeRecord').tree('insert', {
												after: node.target,
												data: dataMap.resCode
											});
										}else{
											msgShow('提示','没有更多信息了！',3000);
										}
										$('#hisAdviceTreeRecord').tree('remove',node.target);
									}else{
										msgShow('提示',dataMap.resCode,3000);
									}
								},
								error: function(){
									$.messager.alert('提示','请求失败！');
								}
							});
						}else{
							msgShow('提示','请选择具体医嘱信息！');
						}
					}
				}
			});
			bindEnterEvent('idCardNoHisRecord',searchInfoHisRecord,'easyui');
			/**  
			 *  
			 * 查询历史病历
			 * @Author：gaotiantian
			 * @CreateDate：2017-4-5 上午11:56:59  
			 * @Modifier：gaotiantian
			 * @ModifyDate：2017-4-5 上午11:56:59   
			 * @ModifyRmk：  
			 * @param 
			 * @version 1.0
			 *
			 */
			function searchInfoHisRecord(){
				var idCardNo = $('#idCardNoHisRecord').textbox('getText');
				if($.trim(idCardNo)==null||$.trim(idCardNo)==''){
					$.messager.alert('提示','请输入就诊卡号！',null,function(){});
					return;
				}
				$.ajax({
					type:"post",
					url:basePath+"outpatient/advice/searchInfoHid.action",
					data:{idCardNo:idCardNo},
					success: function(dataMap) {
						if(dataMap.resMsg=='success'){
							$('#hisAdviceTreeRecord').tree({data:JSON.parse(dataMap.resCode)});
						}else{
							$.messager.alert('提示',dataMap.resCode,null,function(){});
						}
					},
			       error: function(){
			       	$.messager.alert('提示','请求失败！',null,function(){});
			       }
				});
			}
			/**  
			 *  
			 * 双击左侧树右侧显示相应的病历信息
			 * @Author：gaotiantian
			 * @CreateDate：2017-4-5 上午11:56:59  
			 * @Modifier：gaotiantian
			 * @ModifyDate：2017-4-5 上午11:56:59   
			 * @ModifyRmk：  
			 * @param 
			 * @version 1.0
			 *
			 */
			function queryHisAdviceRecord(info){				
				$("#maindescRecord").textbox('setValue',info.mainDesc);
				$("#allergichistoryRecord").textbox('setValue',info.allergicHistory);
				$("#heredityHisRecord").textbox('setValue',info.heredityHis);
				$("#presentillnessRecord").textbox('setValue',info.presentIllness);
				$("#temperatureRecord").textbox('setValue',info.temperature);
				$("#pulseRecord").textbox('setValue',info.pulse);
				$("#breathingRecord").textbox('setValue',info.breathing);
				$("#bloodPressureRecord").textbox('setValue',info.bloodPressure);
				$("#physicalExaminationRecord").textbox('setValue',info.physicalExamination);
				$("#checkresultRecord").textbox('setValue',info.checkResult);
				$("#diagnose1Record").textbox('setValue',info.diagnose1);
				$("#adviceRecord").textbox('setValue',info.advice);
				$("#historyspecilRecord").textbox('setValue',info.historySpecil);			
			}
			/**  
			 *  
			 * 清空病历信息
			 * @Author：gaotiantian
			 * @CreateDate：2017-4-5 上午11:56:59  
			 * @Modifier：gaotiantian
			 * @ModifyDate：2017-4-5 上午11:56:59   
			 * @ModifyRmk：  
			 * @param 
			 * @version 1.0
			 *
			 */
			function clearHistoryRecords(){
				$("#maindescRecord").textbox('setValue','');
				$("#allergichistoryRecord").textbox('setValue','');
				$("#heredityHisRecord").textbox('setValue','');
				$("#presentillnessRecord").textbox('setValue','');
				$("#temperatureRecord").textbox('setValue','');
				$("#pulseRecord").textbox('setValue','');
				$("#breathingRecord").textbox('setValue','');
				$("#bloodPressureRecord").textbox('setValue','');
				$("#physicalExaminationRecord").textbox('setValue','');
				$("#checkresultRecord").textbox('setValue','');
				$("#diagnose1Record").textbox('setValue','');
				$("#adviceRecord").textbox('setValue','');
				$("#historyspecilRecord").textbox('setValue','');
			}
		</script>
</body>
</html>