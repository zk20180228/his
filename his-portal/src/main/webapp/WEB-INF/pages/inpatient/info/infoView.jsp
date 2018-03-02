<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<div id="tt" class="easyui-tabs" data-options="" style="">
		<div title="第一页" style="padding:10px">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td  style="width: 50px">住院流水号:</td>
	    			<td  style="width: 200px" >${inpatientInfo.medicalrecordId}</td>
	    			<td  style="width: 50px">医疗类别:</td>
	    			<td   style="width: 200px">${inpatientInfo.medicalrecordId}</td>
	    		</tr>
	    		<tr>
					<td >病历号:</td>
	    			<td >${inpatientInfo.medicalrecordId}</td>
	    			<td >就诊卡号:</td>
	    			<td >${inpatientInfo.idcardNo}</td>
	    		</tr>
	    		<tr>
					<td >医疗证号:</td>
	    			<td  >${inpatientInfo.mcardNo}</td>
	    			<td >姓名:</td>
	    			<td  >${inpatientInfo.patientName}</td>
	    		</tr>
	    		<tr>
					<td >证件类型:</td>
	    			<td >${inpatientInfo.certificatesType}</td>
	    			<td >证件号码:</td>
	    			<td >${inpatientInfo.certificatesNo}</td>
	    		</tr>
	    		<tr>
					<td >性别:</td>
	    			<td  >${inpatientInfo.reportSex}</td>
	    			<td >出生日期:</td>
	    			<td  >${inpatientInfo.reportBirthday}</td>
	    		</tr>
	    		<tr>
					<td >年龄:</td>
	    			<td  >${inpatientInfo.reportAge}</td>
	    			<td >年龄单位(年月天):</td>
	    			<td  >${inpatientInfo.reportAgeunit}</td>
	    		</tr>
	    		<tr>
					<td >职业代码:</td>
	    			<td  >${inpatientInfo.profCode}</td>
	    			<td >工作单位:</td>
	    			<td  >${inpatientInfo.workName}</td>
	    		</tr>
	    		<tr>
					<td > 工作单位电话:</td>
	    			<td  >${inpatientInfo.workTel}</td>
	    			<td >单位编码:</td>
	    			<td  >${inpatientInfo.workZip}</td>
	    		</tr>
	    		<tr>
					<td >户口家庭住址:</td>
	    			<td  >${inpatientInfo.home}</td>
	    			<td >家庭电话:</td>
	    			<td  >${inpatientInfo.homeTel}</td>
	    		</tr>
	    		<tr>
	    			<td >户口或家庭邮编:</td>
	    			<td  >${inpatientInfo.homeZip}</td>
					<td >籍贯:</td>
					<td  >${inpatientInfo.dist}</td>
				</tr>
				<tr>	
	    			<td >出生地代码:</td>
	    			<td  >${inpatientInfo.birthArea}</td>
					<td >民族:</td>
	    			<td  >${inpatientInfo.nationCode}</td>
	    		</tr>
	    		<tr>	
	    			<td >联系人姓名:</td>
	    			<td  >${inpatientInfo.linkmanName}</td>
	    			<td >联系人电话:</td>
	    			<td  >${inpatientInfo.linkmanTel}</td>
	    		</tr>
	    		<tr>
					<td >联系人地址:</td>
	    			<td  >${inpatientInfo.linkmanAddress}</td>
					<td >关系:</td>
	    			<td  >${inpatientInfo.relaCode}</td>
	    		</tr>
	    		<tr>
	    			<td >婚姻状况:</td>
	    			<td  >${inpatientInfo.mari}</td>
					<td >国籍:</td>
	    			<td  >${inpatientInfo.counCode}</td>
	    		</tr>
	    		<tr>	
	    			<td >身高:</td>
	    			<td  >${inpatientInfo.height}</td>
					<td >体重:</td>
	    			<td  >${inpatientInfo.weight}</td>
	    		</tr>
	    		<tr>	
	    			<td >血压:</td>
	    			<td  >${inpatientInfo.bloodDress}</td>
					<td >血型编码:</td>
	    			<td >${inpatientInfo.bloodCode}</td>
	    		</tr>
	    		<tr>	
	    			<td >重大疾病标志:</td>
	    			<td  >${inpatientInfo.hepatitisFlag}</td>
					<td >过敏标志:</td>
	    			<td >${inpatientInfo.anaphyFlag}</td>
	    		</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
			</div>
		</div>
		
		<div title="第二页" style="padding:10px">
			<table id="secTable" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
			<tr>
				<td  style="height: 50px;width: 100px">入院日期:</td>
    			<td style="width: 200px">${inpatientInfo.inDate}</td>
    			<td style="width: 100px">科室代码:</td>
    			<td style="width: 200px">${inpatientInfo.deptCode}</td>
    		</tr>
    		<tr>
				<td >结算类别:</td>
    			<td>${inpatientInfo.paykindCode}</td>
    			<td >合同单位代码:</td>
    			<td>${inpatientInfo.pactCode}</td>
    		</tr>
    		<tr>
				<td >病床号:</td>
    			<td>${inpatientInfo.bedId}</td>
    			<td >护理单元代码:</td>
    			<td>${inpatientInfo.nurseCellCode}</td>
    		</tr>
    		<tr>
				<td >医师代码(住院):</td>
    			<td>${inpatientInfo.houseDocCode}</td>
    			<td >医师代码(主治):</td>
    			<td>${inpatientInfo.chargeDocCode}</td>
    		</tr>
    		<tr>
				<td >医师代码(主任):</td>
    			<td>${inpatientInfo.chiefDocCode}</td>
    			<td >护士代码（责任:</td>
    			<td>${inpatientInfo.dutyNurseCode}</td>
    		</tr>
    		<tr>
				<td >入院情况:</td>
    			<td>${inpatientInfo.inCircs}</td>
    			<td >诊断名称:</td>
    			<td>${inpatientInfo.diagName}</td>
    		</tr>
    		<tr>
				<td >入院途径:</td>
    			<td>${inpatientInfo.inAvenue}</td>
    			<td >入院来源:</td>
    			<td>${inpatientInfo.inSource}</td>
    		</tr>
    		<tr>
				<td >住院次数:</td>
    			<td>${inpatientInfo.inTimes}</td>
    			<td >婴儿标志:</td>
    			<td>${inpatientInfo.stopAcount}</td>
    		</tr>
    		<tr>
				<td >病案状态:</td>
    			<td>${inpatientInfo.babyFlag}</td>
				<td >住院登记:</td>
    			<td>${inpatientInfo.inState}</td>
    		</tr>
    		<tr>	
    			<td >是否请假:</td>
    			<td>${inpatientInfo.leaveFlag}</td>
				<td >出院日期（预约）:</td>
    			<td>${inpatientInfo.prepayOutdate}</td>
    		</tr>
    		<tr>	
    			<td >出院日期:</td>
    			<td>${inpatientInfo.outDate}</td>
				<td >转归代号:</td>
    			<td>${inpatientInfo.zg }</td>
    		</tr>
    		<tr>	
    			<td >开据医师:</td>
    			<td>${inpatientInfo.emplCode}</td>
				<td >是否在ICU:</td>
    			<td>${inpatientInfo.inIcu}</td>
    		</tr>
    		<tr>	
    			<td >病案是否送入病案室:</td>
    			<td>${inpatientInfo.casesendFlag}</td>
				<td >护理级别:</td>
    			<td>${inpatientInfo.tend }</td>
    		</tr>
    		<tr>	
    			<td style="height: 60px">病危:</td>
    			<td>${inpatientInfo.criticalFlag}</td>
				<td style="height: 60px">备注:</td>
    			<td>${inpatientInfo.remark}</td>
    		</tr>
		</table> 
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
		</div>			
		</div>
	</div>		
</body>
</html>