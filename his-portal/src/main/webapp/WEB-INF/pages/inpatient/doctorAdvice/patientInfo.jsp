<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<div class="easyui-layout" style="width:100%;height:100%;" fit=true>  
		<div>		
				<fieldset style="width:787px;border:1px solid #95b8e7;margin-top:5px;">	
					<legend style="text-align:center;">患者信息</legend>		
					<table id="patientinfo" class="honry-table" style="width: 100%;align:center;"  data-options="method:'post',idField:'id',split:false">
						<thead> 
						<tr>
							<td class="honry-lable" width="25%">病历号  ：</td>
							<td id="hzinpatientNo" width="25%"></td>
							<td class="honry-lable" width="25%">姓名  ：</td>
							<td id="hzpatientName" width="25%"></td>
						</tr>
						<tr>
							<td class="honry-lable" width="25%">性别  ：</td>
							<td id="hzsex" width="25%"></td>
							<td class="honry-lable" width="25%">年龄  ：</td>
							<td id="hzreportAge" width="25%"></td>
						</tr>
						<tr>
							<td class="honry-lable" width="25%">入院日期  ：</td>
							<td id="hzinDate" width="25%"></td>
							<td class="honry-lable" width="25%">出院日期  ：</td>
							<td id="hzprepayOutdate" width="25%"></td>
						</tr>
						<tr>
							<td class="honry-lable" width="25%">状态  ：</td>
							<td id="hzinState" width="25%" colspan="3"></td>
						</tr>
						</thead>
					</table>					
				</fieldset>
				<fieldset style="width:787px;border:1px solid #95b8e7;margin-top:5px;">	
					<legend style="text-align:center;">住院信息</legend>		
					<table id="patientinfo" class="honry-table" style="width: 100%;align:center;"  data-options="method:'post',idField:'id',split:false">
						<thead> 						
						<tr>
							<td class="honry-lable" width="25%">入院科室  ：</td>
							<td id="hzdeptName" width="25%"></td>					
							<td class="honry-lable" width="25%">患者床号  ：</td>
							<td id="hzbedId" width="25%"><input type="hidden" name="ageUnit" id="ageUnit"></td>
						</tr>
						</thead>
					</table>					
				</fieldset>
				<fieldset style="width:787px;border:1px solid #95b8e7;margin-top:10px;">	
					<legend style="text-align:center;">患者费用信息</legend>		
					<table class="honry-table" style="width: 100%;"  data-options="method:'post',idField:'id',split:false">
						<thead> 
						<tr>
							<td class="honry-lable" width="25%">预交金  ：</td>
							<td id="hzprepayCost" width="25%"></td>
							<td class="honry-lable" width="25%">自费金额  ：</td>
							<td id="hzownCost" width="25%"></td>
						</tr>
						<tr>
							<td class="honry-lable" width="25%">总费用 ：</td>
							<td id="hztotCost" width="25%"></td>
							<td class="honry-lable" width="25%">余额 ：</td>
							<td id="hzfreeCost" width="25%"></td>
						</tr>						
						</thead>
					</table>					
				</fieldset>
				<fieldset style="width:787px;border:1px solid #95b8e7;margin-top:10px;">	
					<legend style="text-align:center;">公费信息</legend>		
					<table class="honry-table" style="width: 100%;"  data-options="method:'post',idField:'id',split:false">
						<thead> 
						<tr>
							<td class="honry-lable" width="25%">日限额  ：</td>
							<td id="hzdayLimit" width="25%"></td>
							<td class="honry-lable" width="25%">日限额累计  ：</td>
							<td id="hzlimitTot" width="25%"></td>
						</tr>
						<tr>
							<td class="honry-lable" width="25%">公费自付金额 ：</td>
							<td id="hzpayCost" width="25%" colspan="3"></td>						
						</tr>						
						</thead>
					</table>					
				</fieldset>
			</div>
			<input id="ids" value="${id}" type="hidden"/>
</div>
<script type="text/javascript">
				$(function(){
				    $.ajax({
						url: '<%=basePath%>inpatient/exitNofee/queryPatientInfo.action',
						data:'inpatientInfo.id='+$('#ids').val(),
						type:'post',
						datatype:'json',
						success: function(data) {							
							var json=data;	
							if(json.length==0){
								$.messager.alert('提示','用户患者不存在！'); 
								$("input").val("");
							}else{
							$("#hzpatientName").html(json[0].patientName);
							$("#hzinpatientNo").html(json[0].medicalrecordId);
							$("#hzsex").html(json[0].reportSexName);
							var ages=DateOfBirth(json[0].reportBirthday);
							$("#hzreportAge").html(ages.get("nianling")+ages.get('ageUnits'))
							$("#hzinDate").html(json[0].inDate);
							var date=json[0].outDate;
							if(date!=null&&date!=""){
								if(date.substring(0,4)==0002||date.substring(0,4)==0001){
									$('#hzprepayOutdate').html("");
								}else{
									$('#hzprepayOutdate').html(json[0].outDate);
								}
							}
							if(json[0].inState=='R'){
								$("#hzinState").html('住院登记');
							}else if(json[0].inState=='I'){
								$("#hzinState").html('病房接诊');
							}else if(json[0].inState=='B'){
								$("#hzinState").html('出院登记');
							}else if(json[0].inState=='O'){
								$("#hzinState").html('出院结算');
							}else if(json[0].inState=='P'){
								$("#hzinState").html('预约出院');
							}
							$("#hzprepayCost").html(json[0].prepayCost.toFixed(2));
							$("#hzpayCost").html(json[0].payCost.toFixed(2));
							$("#hzownCost").html(json[0].ownCost.toFixed(2));
							$("#hztotCost").html(json[0].totCost.toFixed(2));
							$("#hzpubCost").html(json[0].pubCost.toFixed(2));
							$("#hzfreeCost").html(json[0].freeCost.toFixed(2));
							$("#hzbedId").html(json[0].bedName);
							$("#hzdeptName").html(json[0].deptName);

							}
						}
					});
				    
				});
</script>
</body>
</html>