<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>输血申请单</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
	<form id="baocun" method="post"  >
		<div data-options="region:'center',border:false" style="width: 100%;height: 100%;padding:5px" align="center">
			<table style="width: 100%;height: 10%;">
				<tr>
					<td style="text-align: center"><font size="10">输血申请单</font></td>
				</tr>
			</table>
			<table class="honry-table"  cellpadding="1" cellspacing="1"	border="1px solid black" style="width:100%;padding:5px">
						<tr>
							<td colspan="4">患者基本信息</td>
						</tr>
						<tr>
							<td class="honry-lable">
								姓名：
							</td>
							<td >
								<span>${inpatientApplyNow.name}</span>

							</td>
							<td class="honry-lable">
								性别：
							</td>
							<td>
								<span>${inpatientApplyNow.sexName}</span>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								年龄：
							</td>
							<td>
								<span>${inpatientApplyNow.age}</span>
							</td>
							<td class="honry-lable">
								科室：
							</td>
							<td>
								<span>${inpatientApplyNow.deptName}</span>
							</td>
						</tr>
					
					<tr>
						<td class="honry-lable">
							临床诊断：
						</td>
						<td>
							<span id="lczd"></span>
						</td>
						<td class="honry-lable">
							是否报销：
						</td>
						<td>
							<c:choose>
								<c:when test="${inpatientApplyNow.isCharge eq '1'}">
								<span>是</span>
								</c:when>
								<c:otherwise>
								<span>否</span>
								</c:otherwise>
							</c:choose>
						</td>
						</tr>
						<tr>
						<td class="honry-lable">
								用血目的：
							</td>
							<td>
								<span id="bloodAimId"></span>
							</td>
						<td class="honry-lable">
							预定血型：
						</td>
						<td>
							<span id="bloodKindId"></span>
						</td>
							</tr>
					<tr>
						<td class="honry-lable">
							血液成分：
						</td>
						<td>
							<span id="bloodTypeCodeId"></span>
						</td>
						<td class="honry-lable">
								RH(D)：
							</td>
							<td>
								<c:choose>
									<c:when test="${inpatientApplyNow.rh eq '0'}">
									<span>待查</span>
									</c:when>
									<c:when test="${inpatientApplyNow.rh eq '1'}">
									<span>阴性</span>
									</c:when>
									<c:otherwise>
									<span>阳性</span>
									</c:otherwise>
								  </c:choose>
							</td>
					</tr>
					<tr>
						<td  class="honry-lable">
							预订血量：
						</td>
						<td>
							<span>${inpatientApplyNow.quantity}&nbsp;${inpatientApplyNow.stockUnit}</span>
						</td>
						<td  class="honry-lable">
								输血性质：
							</td>
							<td>
								<span id="qualityId"></span>
							</td>
							</tr>
							<tr>
							<td class="honry-lable">
								受血者属地：
							</td>
							<td>
								<c:choose>
									<c:when test="${inpatientApplyNow.insource eq '0'}">
									<span>本市</span>
									</c:when>
									<c:otherwise>
									<span>外埠</span>
									</c:otherwise>
								</c:choose>
							</td>
					
						<td class="honry-lable">
							预定输血日期：
						</td>
						<td>
							<span id="orderTime"></span>
						</td>
						</tr>
					<tr>
						<td class="honry-lable">
							既往输血史：
						</td>
						<td colspan="3">
							<c:choose>
									<c:when test="${inpatientApplyNow.bloodhistory eq '0'}">
									<span>是</span>
									</c:when>
									<c:otherwise>
									<span>否</span>
									</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td colspan="4">受血者血性检验信息</td>
					</tr>
						<tr>
						<td class="honry-lable">
							血型：
						</td>
						<td>
							<span id="patientBloodkindId"></span>
						</td>
						<td class="honry-lable">
							血红蛋白：
						</td>
						<td>
							<span>${inpatientApplyNow.hematin}&nbsp;g/L</span>
						</td>
						</tr>
						<tr>
						<td class="honry-lable">
							 HCT：
						</td>
						<td>
							<span>${inpatientApplyNow.hct}</span>
						</td>
						<td class="honry-lable">
							血小板：
						</td>
						<td>
							<span>${inpatientApplyNow.platelet}&nbsp;x10 <sup>9</sup>/L</span>
						</td>
						</tr>
						<tr>
						<td class="honry-lable">
							ALT：
						</td>
						<td>
							<span>${inpatientApplyNow.alt}&nbsp;U/L</span>
							
						</td>
						<td class="honry-lable">
							HbsAg：
						</td>
						<td>
							<c:choose>
									<c:when test="${inpatientApplyNow.hbsag eq '0'}">
									<span>待查</span>
									</c:when>
									<c:when test="${inpatientApplyNow.rh eq '1'}">
									<span>阴性</span>
									</c:when>
									<c:otherwise>
									<span>阳性</span>
									</c:otherwise>
							</c:choose>
						</td>
						</tr>
						<tr>
						<td  class="honry-lable">
							Anti-HCV：
						</td>
						<td>
							<c:choose>
									<c:when test="${inpatientApplyNow.antiHcv eq '0'}">
									<span>待查</span>
									</c:when>
									<c:when test="${inpatientApplyNow.rh eq '1'}">
									<span>阴性</span>
									</c:when>
									<c:otherwise>
									<span>阳性</span>
									</c:otherwise>
							</c:choose>
						</td>
						<td  class="honry-lable">
							Anti-HIV1/2：
						</td>
						<td>
							<c:choose>
									<c:when test="${inpatientApplyNow.antiHiv eq '0'}">
									<span>待查</span>
									</c:when>
									<c:when test="${inpatientApplyNow.rh eq '1'}">
									<span>阴性</span>
									</c:when>
									<c:otherwise>
									<span>阳性</span>
									</c:otherwise>
							</c:choose>
						</td>
						</tr>
						<tr>
						<td  class="honry-lable">
							梅毒：
						</td>
						<td>
							<c:choose>
									<c:when test="${inpatientApplyNow.lues eq '0'}">
									<span>待查</span>
									</c:when>
									<c:when test="${inpatientApplyNow.rh eq '1'}">
									<span>阴性</span>
									</c:when>
									<c:otherwise>
									<span>阳性</span>
									</c:otherwise>
							</c:choose>
						</td>
						
						<td class="honry-lable">
							孕产情况：
						</td>
						<td>
							<span>${inpatientApplyNow.pregnant}</span>
						</td>
						</tr>
			</table>
			<table style="width: 100%;padding-top: 10px;">
						<tr >
							<td ><input id="templateSubmit" type="checkbox" onclick="clickclick()" readonly="readonly">自定义诊断名称&nbsp;
					    		<span id="lczds" style="display: none;">&nbsp;${inpatientApplyNow.diagnoseName}</input></span> 
					    	</td>
					    	<td style="text-align: right;">申请医师：
					    		<span>${inpatientApplyNow.applyDocName}</span>
					    	
					    	&nbsp;&nbsp;主治医师：
					    		<span>${inpatientApplyNow.chargeDocName}</span>
					    	
					    	&nbsp;&nbsp;申请时间：
					    		<span id="applyTime"></span>
					    		
					    	</td>
						</tr>
			</table>
		</div>
	</form>
</div>
 
<script type="text/javascript">
		var bloodType=new Map();
		var bloodaim=new Map();
		var bloodquality=new Map();
		var xueyechengfen=new Map();
		var diagnose="${inpatientApplyNow.diagnose}";
		$(function(){
			//血型、预定血型
			$.ajax({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodType'/> ",
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						bloodType.put(v[i].encode,v[i].name);
					}
					var sex="${inpatientApplyNow.bloodKind}";
					$('#bloodKindId').html(bloodType.get(sex));
					var patientBloodkind="${inpatientApplyNow.patientBloodkind}";
					$('#patientBloodkindId').html(bloodType.get(patientBloodkind));
				}
			});
				
			//用血目的
			$.ajax({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodaim'/>",
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						bloodaim.put(v[i].encode,v[i].name);
					}
					var sex="${inpatientApplyNow.bloodAim}";
					$('#bloodAimId').html(bloodaim.get(sex));
				}
			});
				
				//输血性质
				
			$.ajax({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodquality'/>",
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						bloodquality.put(v[i].encode,v[i].name);
					}
					var ss="${inpatientApplyNow.quality}";
					$('#qualityId').html(bloodquality.get(ss));
				}
			});
				
			//血液成分
			
			$.ajax({
				url : '<%=basePath%>publics/transfusion/queryInpatientComponent.action',
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						xueyechengfen.put(v[i].bloodTypeCode,v[i].bloodTypeName);
					}
					var ss="${inpatientApplyNow.bloodTypeCode}";
					$('#bloodTypeCodeId').html(xueyechengfen.get(ss));
				}
			});	
			if(diagnose==''||diagnose==null){
				$("#templateSubmit").attr("checked","checked");
				$('#lczds').show();
			}else{
				var nn="${inpatientApplyNow.diagnoseName}";
				$('#lczd').html(nn);
			}	
					
			$('#applyTime').html(dateFormatHMS("${inpatientApplyNow.applyTime}"));
			$('#orderTime').html(dateFormat("${inpatientApplyNow.orderTime}"));
		});
		function dateFormat(value,row,index){
			if(value!=null&&value!=''){
				var newDate=/\d{4}-\d{1,2}-\d{1,2}/g.exec(value);
				return newDate;
			}
		}
		function dateFormatHMS(value,row,index){
			if(value!=null&&value!=''){
				var newDate=/\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2}:\d{1,2}/g.exec(value);
				return newDate;
			}
		}
</script>	
</body>
</html>