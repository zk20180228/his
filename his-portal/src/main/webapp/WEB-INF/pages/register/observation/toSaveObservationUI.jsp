<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>门诊留观</title>
	<%@ include file="/common/metas.jsp" %>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="margin: 0px;padding: 0px;">
<div class="easyui-layout" style="width:100%;height:100%;text-align:center;">
   <div data-options="region:'center'" style="border-top:0px">
		<div id="divLayout" class="easyui-layout" fit=true >
			<div data-options="region:'center',split:false,border:false" style="width:50%;height:100%;text-align:center;">
			 <form id="form" method="post" style="margin-left:auto;margin-right:auto;margin-top:25px;"  data-options="novalidate:false">
				 <fieldset style="width:854px;border:0px;margin-left:auto;margin-right:auto;">
						<div style="margin-left:auto;margin-right:auto;">
							<table  style="border:0px solid black;margin-left:auto;margin-right:auto">
								<tr >
									<td style="padding: 5px 0px;font-size:14px;">就诊卡号:
										<input class="easyui-textbox" id="card_No"  style="width:180px;" data-options="prompt:'输入就诊卡号回车查询'"/>
							        	<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryPatient()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 30px" >查询</a>
							        </td>
								</tr>
							</table>
						</div>
						<div id="p" class="easyui-panel" title="挂号信息" style="margin-left:auto;margin-right:auto;">
							<table id="reglist" class="easyui-datagrid" style="width:100%;margin-left:auto;margin-right:auto;" data-options="striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
								<thead >
									<tr>
										<th data-options="field:'patientName',width:'10%'">
											姓名
										</th>																			
										<th data-options="field:'patientSex',width:'5%'">性别</th>																			
										<th data-options="field:'clinicCode',width:'18%'">门诊号</th>																			
										<th
											data-options="field:'dept',width:'15%'">
											挂号科室
										</th>
										<th
											data-options="field:'expxrt',width:'15%'">
											挂号专家</th>
										<th
											data-options="field:'type',width:'15%'">
											挂号类别
										</th>
										<th
											data-options="field:'regDate',width:'20%'">
											挂号日期
										</th>
									</tr>
								</thead>
							</table>
						</div>
				 </fieldset>	   
			       <fieldset style="width:899px;border:0px;margin-left:auto;margin-right:auto;">
						<table id="tabletable" style="font-size: 14px;margin-left:auto;margin-right:auto;" class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black">
							<tr>
								<td class="honry-lable">姓名：</td>
								<td id ="patientName">
								</td>
								<td class="honry-lable">性别：</td>
								<td id ="sex">
								</td>
							</tr>
				  			<tr>
					  			<td class="honry-lable">年龄：</td>
								<td id ="age">
								</td>
								<td class="honry-lable">出生日期：</td>
								<td id="birthday">
								</td>
				 		  </tr>
				 		 <tr>
								<td class="honry-lable" style="width: 25%">证件类型：</td>
								<td style="width: 25%" id ="identityType">
								</td>
								<td class="honry-lable">证件号码：</td>
								<td id="identityCode">
								</td>
				 		 </tr>
						<tr>
							<td class="honry-lable">地址：</td>
							<td id ="address">
							</td>
							<td class="honry-lable">合同单位：</td>
								<td id ="unionUnit">
								</td>
						</tr>
						<tr>
							<td class="honry-lable" >门诊号：</td>
							      <td id ="outpatientCode">
							      </td>
							<td class="honry-lable">病历号：</td>
							<td id ="patient_No">
							</td>
						</tr>
						<tr>
							<td class="honry-lable">责任医师：</td>
							<td>
									<input id="doctorCode" name="observation.doctorCode"  class="easyui-combobox" />
									<input id="doctorName" name="observation.doctorName"  type="hidden"  />
							</td>
							<td class="honry-lable">责任护士：</td>
							<td>	
									<input id="cardNo" name="observation.cardNo"  type="hidden"/>
									<input id="patientNo" name="observation.patientNo"  type="hidden"/>
									<input id="nurseCode" name="observation.nurseCode"  class="easyui-combobox"  />
									<input id="nurseName" name="observation.nurseName"  type="hidden"  />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">开始时间：</td>
							<td>
								<input id="observationIntime"   name="observation.observationIntime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" />
							</td>
							
							<td class="honry-lable">结束时间：</td>
							<td>
								<input id="observationOuttime"   name="observation.observationOuttime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" />
							</td>
						</tr>
<!-- 						<tr> -->
<!-- 							<td class="honry-lable">留观天数：</td> -->
<!-- 					    	<td id ="observationDays"> -->
<!-- 					    		<input id="observationDays" name="observation.observationDays"  type="hidden"/> -->
<!-- 				    		</td> -->
				    		
<!-- 						</tr> -->
						<tr>
							<td class="honry-lable">诊断：</td>
							<td colspan="3">
								<textarea class="easyui-textbox" style="width:90%;height:60px"id="diagnosis" name="observation.diagnosis" data-options="multiline:true"></textarea>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">转归：</td>
							<td colspan="3">
								<textarea class="easyui-textbox" style="width:90%;height:60px" id="lapseTo" name="observation.lapseTo" data-options="multiline:true"></textarea>
							</td>
						</tr>
			</table>
			</fieldset>
					</form>
					<div style="padding: 10px;height:80px; " >
						<a href="javascript:void(0)" id="baocun" class="easyui-linkbutton" onclick="submit()" data-options="iconCls:'icon-save'" style="margin-left:auto;margin-right:auto;" >保&nbsp;存&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
			</div>
		</div>
	</div>
</div>   	
</body>
<script type="text/javascript">
	
	var cardNo='';
	$(function(){
		bindEnterEvent('card_No',queryPatient,'easyui');
		$('#doctorCode').combobox({    
		    url:'${pageContext.request.contextPath}/observation/findDoctorCode.action',    
		    valueField:'id',    
		    textField:'text',
		    onSelect:function(record){
		    	$("#doctorName").val(record.text);
		    }
		}); 
		
		$('#nurseCode').combobox({    
		    url:'${pageContext.request.contextPath}/observation/findNurseCode.action',   
		    valueField:'id',    
		    textField:'text',
		    onSelect:function(record){
		    	$("#nurseName").val(record.text);
		    }   
		}); 
		
		
		
		$('#reglist').datagrid({    
			onLoadSuccess:function(){
				var rows=$('#reglist').datagrid("getRows");
				if(rows!=null&&rows!=undefined&&rows!=''&&rows.length>0){
					cardNo=$("#card_No").textbox('getText');
					$.ajax({
						url:"${pageContext.request.contextPath}/observation/findPatientInfoByCardNo.action",
						data:"cardNo="+cardNo,
						type:"post",
						success:function(backData){
							if(backData!=null&&backData!=undefined&&backData!=''){
								$("#cardNo").val(cardNo);
								
								$("#patientName").text(backData.patientName);
								$("#sex").text(backData.sex);
								var myDate = new Date();
								var nowYear=myDate.getFullYear();
								var b=0;
								if(backData.birthday!=null&&backData.birthday!=''){
									b=backData.birthday.substring(0,4);
									
									$("#age").text(nowYear-b);
								}
								$("#birthday").text(backData.birthday);
								$("#identityType").text(backData.identityType);
								$("#identityCode").text(backData.identityCode);
								$("#address").text(backData.address);
								$("#unionUnit").text(backData.unionUnit);
								$("#patient_No").text(backData.patientNo);
								
								$("#patientNo").val(backData.patientNo);
								
								$("#outpatientCode").text(rows[0].clinicCode);
							}
						}
					});
					
				}else{
					$("#patientName").text("");
					$("#sex").text("");
					$("#age").text("");
					$("#birthday").text("");
					$("#identityType").text("");
					$("#identityCode").text("");
					$("#address").text("");
					$("#unionUnit").text("");
					$("#patient_No").text("");
					
					$("#outpatientCode").text("");
					$.messager.alert('提示','患者没有有效的挂号信息！');    
				}
			}
			

		}) 		

	});
	
	function queryPatient(){
		cardNo=$("#card_No").textbox('getText');
		if(cardNo!=null&&cardNo!=undefined&&cardNo!=''){
			$("#reglist").datagrid( {
				url:"${pageContext.request.contextPath}/observation/findRegisterInfoByCardNo.action", 
				queryParams:{
					cardNo: cardNo
					}
			});
		}else{
			$.messager.alert('提示','请输入就诊卡号！');    
		}
		

	}
	
	
	
	function submit(){
		var rows=$('#reglist').datagrid("getRows");
		if(rows!=null&&rows!=''&&rows.length>0){
			var inTime=$("#observationIntime").val();	
			var outTime=$("#observationOuttime").val();
			if(inTime!=null&&outTime!=null&&inTime!=''&&outTime!=''){
				
				if(inTime>outTime){
					$.messager.alert("提示","开始时间不能大于结束时间！");
					return ;
				}
				$('#form').form('submit', {    
				    url:"${pageContext.request.contextPath}/observation/saveObservation.action",    
				    success:function(data){
				    	if(data=='"true"'){
				    		$.messager.alert('提示','保存成功！');
				    		location.reload();
				    	}else{
				    		$.messager.alert('提示','保存失败,请稍后重试！'); 
				    	}   
				    }    
				}); 
			}else{
				$.messager.alert('提示','请录入完整的时间！'); 
				return ;
			}
		}else{
			$.messager.alert('提示','请先录入挂号信息！');   
			return ;
		}
		
	}
	
	
</script>
</html>