<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style type="text/css">
	.tableCss {
		border-collapse: collapse;
		border-spacing: 0;
		border-left: 1px solid #95b8e7;
		border-top: 1px solid #95b8e7;
		width: 100%;
	}
	
	.tableLabel {
		text-align: right;
		width: 100px;
	}
	
	.tableCss td {
		border-right: 1px solid #95b8e7;
		border-bottom: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space: nowrap;
	}
	</style>
	<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});

	
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">   
		<div data-options="region:'north',split:false,border:false" style="height:40px;padding:7px 5px 0px 5px;">
			<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a>
			<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
			<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
			<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
			<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
			<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
			<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a>
			<!-- <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-back'" onclick="closeTab()">退出</a> -->
			
		</div>   
		<div data-options="region:'center',border:false">
			<div id="tt" class="easyui-tabs" data-options="fit:true" style="width:100%;height:100%;">   
			    <div title="挂号信息查询">   
					<div class="easyui-layout" data-options="fit:true">   
						<div data-options="region:'north',split:false,border:false" style="height:46px;padding:5px 5px 5px 5px">
							<table>
								<tr>
									<td>查询条件:</td>
									<td style="width:110px;">
										<select id="ect" class="easyui-combobox" style="width:100px;">   
										    <option value="0">全部</option>   
										    <option value="1">挂号科室</option>   
										    <option value="2">挂号医生</option>   
										    <option value="3">挂号级别</option>   
										    <option value="4">合同单位</option>   
										</select>
									</td>
									<td style="width:90px;">
										<select id="ecv" class="easyui-combobox" style="width:83px;">   
											<option value="0">相等</option>   
										    <option value="1">相似</option>   
										</select>
									</td>
									<td style="width:130px;"><input class="easyui-textbox" id="para" style="width:120px;"></td>
									<td>挂号日期:</td>
									<td style="width:150px;">
										<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;margin-top:6"/>
										<a href="javascript:void(0)" onclick="delTime('startTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									</td>
									<td style="width:10px;"><span style="margin-right:7">至</span></td>
									<td style="width:150px;" class="synEndTime">
										<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;margin-top:6"/>
									<a href="javascript:void(0)"  onclick="delTime('endTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true">
									</a>
									</td>
									<td>
										<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" id="searchEdButId" onclick="searchEd()">查询</a>
										<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</td>
								</tr>
							</table>
						</div>   
<!-- 					<div data-options="region:'north',border:false" style="height:50px;padding:5px 5px 0px 5px;"> -->
<!-- 						<table id="searchTab" style="width: 100%;"> -->
<!-- 							<tr> -->
<!-- 							<td style="width:80px;" align="right">查询条件:</td>  -->
<!-- 									<td style="width:100px;"> -->
<!-- 										<select id="ect" class="easyui-combobox" style="width:100px;">    -->
<!-- 										    <option value="0">全部</option>    -->
<!-- 										    <option value="1">挂号科室</option>    -->
<!-- 										    <option value="2">挂号医生</option>    -->
<!-- 										    <option value="3">挂号级别</option>    -->
<!-- 										    <option value="4">合同单位</option>    -->
<!-- 										</select> -->
<!-- 									</td> -->
<!-- 									<td style="width:80px;"> -->
<!-- 										<select id="ecv" class="easyui-combobox" style="width:80px;">    -->
<!-- 											<option value="0">不相似</option>    -->
<!-- 										    <option value="1">相似</option>    -->
<!-- 										</select> -->
<!-- 									</td> -->
<!-- 								<td style="width:100px;"><input class="easyui-textbox" id="para" ></td> -->
<!-- 								开始时间  -->
<!-- 								<td style="width:50px;" align="right">日期:</td> -->
<!-- 								<td style="width:110px;"> -->
<%-- 									<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="border: 1px solid #95b8e7;border-radius: 5px;"/> --%>
<!-- 								</td> -->
<!-- 								<td style="width:20px;" align="center"> -->
<!-- 									<a href="javascript:void(0)" onclick="delTime('startTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>	 -->
<!-- 								</td> -->
<!-- 								结束时间  -->
<!-- 								<td style="width:40px;" align="center">至</td> -->
<!-- 								<td style="width:110px;"> -->
<%-- 									<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="border: 1px solid #95b8e7;border-radius: 5px;"/> --%>
<!-- 								</td> -->
<!-- 								<td style="width:30px;" align="center"> -->
<!-- 									<a href="javascript:void(0)"  onclick="delTime('endTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> -->
<!-- 								</td> -->
<!-- 								<td> -->
<%-- 								<shiro:hasPermission name="${menuAlias}:function:query"> --%>
<!-- 									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" id="searchEdButId" onclick="searchEd()">查询</a> -->
<!-- 									<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a> -->
<%-- 								</shiro:hasPermission> --%>
<!-- 								</td> -->
<!-- 								<td style='text-align: right'> -->
<%-- 									<shiro:hasPermission name="${menuAlias}:function:query"> --%>
<!-- 										<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">前三天</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">前七天</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">前十五天</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a> -->
<!-- 										&nbsp;&nbsp; -->
<%-- 									</shiro:hasPermission> --%>
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 						</table> -->
<!-- 					</div> -->
						<div data-options="region:'center',border:false">
							<div class="easyui-layout" data-options="fit:true,border:false">   
								<div data-options="region:'north',split:false,border:false">
									<table class="tableCss" cellspacing="5" cellpadding="5" align="center">
										<tbody>
											<tr>
												<td class="tableLabel">病历号：</td>
												<td id="recordNoTdId"></td>
												<td class="tableLabel">门诊流水号：</td>
												<td id="registerNOTdId"></td>
												<td class="tableLabel">患者姓名：</td>
												<td id="patientNameTdId"></td>
												<td class="tableLabel">患者年龄：</td>
												<td id="patientAgeTdId"></td>
											</tr>
											<tr>
												<td class="tableLabel">挂号日期：</td>
												<td id="registerDateTdId"></td>
												<td class="tableLabel">身份证号码：</td>
												<td id="idCardTdId"></td>
												<td class="tableLabel">出生日期：</td>
												<td id="birthdayTdId"></td>
												<td class="tableLabel">结算类别：</td>
												<td id="payKindcodeTdId"></td>
											</tr>
											<tr>
												<td class="tableLabel">合同单位：</td>
												<td id="contractunitTdId"></td>
												<td class="tableLabel">医疗证号：</td>
												<td id="idcardNoTdId"></td>
												<td class="tableLabel">挂号级别：</td>
												<td id="gradeTdId"></td>
												<td class="tableLabel">挂号科室：</td>
												<td id="regdeptTdId"></td>
											</tr>
											<tr>
												<td class="tableLabel">挂号医生：</td>
												<td id="regdocTdId"></td>
												<td class="tableLabel">看诊科室：</td>
												<td id="seedeptTdId"></td>
												<td class="tableLabel">看诊医生：</td>
												<td id="seedocTdId"></td>
												<td class="tableLabel">挂号发票：</td>
												<td id="invoiceTdId"></td>
											</tr>
										</tbody>
									</table>
								</div>   
								<div data-options="region:'center',border:false">
									<table id="regisInfoDgId" data-options="rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fit:true">   
									    <thead>   
									        <tr>   
									            <th data-options="field:'recordNo',align:'center',width:'9%'">病历号</th>   
									            <th data-options="field:'patientName',align:'center',width:'9%'">患者姓名</th>   
									            <th data-options="field:'registerDate',align:'center',width:'9%'">挂号日期</th>   
									            <th data-options="field:'payKindcode',align:'center',width:'9%'">结算类别</th>   
									            <th data-options="field:'contractunit',align:'center',width:'9%'">合同单位</th>   
									            <th data-options="field:'grade',align:'center',width:'9%'">挂号级别</th>   
									            <th data-options="field:'regdept',align:'center',width:'9%'">挂号科室</th>   
									            <th data-options="field:'regdoc',align:'center',width:'9%'">挂号医生</th>   
									            <th data-options="field:'orderNo',align:'center',width:'9%'">看诊序列</th>   
									            <th data-options="field:'status',align:'center',width:'9%'">是否有效</th>   
									            <th data-options="field:'quitreason',align:'center',width:'9%'">退号时间</th>   
									        </tr>   
									    </thead>   
									</table>  
								</div>
							</div>
						</div> 
					</div>
			    </div>   
			    <div title="患者基本信息查询">  
			    	<div style="padding:5px 5px 5px 5px;"> 
				       	<table class="tableCss" cellspacing="5" cellpadding="5" align="center" >
							<tbody>
								<tr>
									<td class="tableLabel">姓名：</td>
									<td id="nameInfoTdId"></td>
									<td class="tableLabel">性别：</td>
									<td id="sexInfoTdId"></td>
									<td class="tableLabel">费用来源：</td>
									<td id="unitInfoTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">医保手册号：</td>
									<td id="handbookInfoTdId"></td>
									<td class="tableLabel">国籍：</td>
									<td id="nationalityInfoTdId"></td>
									<td class="tableLabel">民族：</td>
									<td id="nationInfoTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">出生日期：</td>
									<td id="birthdayInfoTdId"></td>
<!-- 									<td class="tableLabel">年龄：</td> -->
<!-- 									<td id="ageInfoTdId"></td> -->
									<td class="tableLabel">出生地：</td>
									<td id="birthplaceInfoTdId"></td>
									<td class="tableLabel">职业：</td>
									<td id="occupationInfoTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">证件类型：</td>
									<td id="certificatesTypeInfoTdId"></td>
									<td class="tableLabel">证件号：</td>
									<td id="certificatesNoInfoTdId"></td>
									<td class="tableLabel">工作单位：</td>
									<td id="workUnitInfoTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">单位电话：</td>
									<td id="workPhoneInfoTdId"></td>
									<td class="tableLabel">婚姻状况：</td>
									<td id="warriageInfoTdId"></td>
									<td class="tableLabel">籍贯：</td>
									<td id="nativeplaceInfoTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">家庭地址：</td>
									<td id="cityInfoTdId"></td>
									<td class="tableLabel">门牌号：</td>
									<td id="doornoInfoTdId"></td>
									<td class="tableLabel">家庭电话：</td>
									<td id="phoneInfoTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">联系人：</td>
									<td id="linkManInfoTdId"></td>
<!-- 									<td class="tableLabel">关系：</td> -->
<!-- 									<td id="linkrelationInfoTdId"></td> -->
									<td class="tableLabel">联系人地址：</td>
									<td id="linkaddressInfoTdId"></td>
									<td class="tableLabel">门牌号：</td>
									<td id="linkdoornoInfoTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">联系电话：</td>
									<td id="linkphoneInfoTdId"></td>
									<td class="tableLabel">电子邮箱：</td>
									<td id="emailInfoTdId"></td>
									<td class="tableLabel">母亲姓名：</td>
									<td id="motherInfoTdId"></td>
								</tr>
							</tbody>
						</table>
					</div>
			    </div>   
			    <div title="发票信息查询"> 
			    	<div style="width:100%;height:100%;">
				        <table id="invoiceEdgId" class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fit:true">   
						    <thead>   
						        <tr>   
						            <th data-options="field:'invoiceNo',align:'center',width:'5%'">发票号码</th>   
						            <th data-options="field:'priceCost',align:'center',width:'5%'">划价收费</th>   
						            <th data-options="field:'status',align:'center',width:'5%'">状态</th>   
						            <th data-options="field:'source',align:'center',width:'5%'">录入来源</th>   
						            <th data-options="field:'itemCode',align:'center',width:'5%'">项目编码</th>   
						            <th data-options="field:'itemName',align:'center',width:'5%'">项目名称</th>   
						            <th data-options="field:'group',align:'center',width:'5%'">组</th>   
						            <th data-options="field:'groupName',align:'center',width:'5%'">组名</th>   
						            <th data-options="field:'inOrderNo',align:'center',width:'5%'">内部序号</th>   
						            <th data-options="field:'seqNo',align:'center',width:'5%'">序列号</th>   
						            <th data-options="field:'spec',align:'center',width:'5%'">规格</th>   
						            <th data-options="field:'qty',align:'center',width:'5%'">数量</th>   
						            <th data-options="field:'unitPrice',align:'center',width:'5%'">单价</th>   
						            <th data-options="field:'totalAmount',align:'center',width:'5%'">总金额</th>   
						            <th data-options="field:'shoufuAmount',align:'center',width:'5%'">首付金额</th>   
						            <th data-options="field:'tallyAmount',align:'center',width:'5%'">记账金额</th>   
						            <th data-options="field:'billDept',align:'center',width:'5%'">开单科室</th>   
						            <th data-options="field:'billdoc',align:'center',width:'5%'">开单医生</th>   
						            <th data-options="field:'inputPerson',align:'center',width:'5%'">录入人</th>   
						            <th data-options="field:'inputTime',align:'center',width:'5%'">录入时间</th>   
						            <th data-options="field:'exeDept',align:'center',width:'5%'">执行科室</th>   
						            <th data-options="field:'receiver',align:'center',width:'5%'">收款员</th>   
						            <th data-options="field:'chargeDate',align:'center',width:'5%'">收费日期</th>   
						            <th data-options="field:'conExeTime',align:'center',width:'5%'">确认执行时间</th>   
						            <th data-options="field:'conExeDept',align:'center',width:'5%'">确认执行科室</th>   
						            <th data-options="field:'conExePerson',align:'center',width:'5%'">确认执行人</th>   
						        </tr>   
						    </thead>   
						</table>
					</div> 
			    </div> 
			    <div title="医嘱信息查询">   
			        <div class="easyui-layout" data-options="fit:true,border:false">   
						<div data-options="region:'west',split:false,split:true" style="width:300px;border-top:0">
							<ul id="etId"></ul>
						</div>   
						<div data-options="region:'center'" style="border-top:0">
							<table id="recipelEdgId" class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fit:true">   
							    <thead>   
							        <tr>   
							            <th data-options="field:'sysType',align:'center',width:'9%'">医嘱类型</th>   
							            <th data-options="field:'itemName',align:'center',width:'12%'">项目名称</th>   
							            <th data-options="field:'freName',align:'center',width:'12%'">频次</th>   
							            <th data-options="field:'usage',align:'center',width:'12%'">用法</th>   
							            <th data-options="field:'qty',align:'center',width:'9%'">数量</th>   
							            <th data-options="field:'unit',align:'center',width:'9%'">单位</th>   
							            <th data-options="field:'openDate',align:'center',width:'12%'">开始时间</th>   
							            <th data-options="field:'cancelDate',align:'center',width:'12%'">结束时间</th>   
							        </tr>   
							    </thead>   
							</table>
						</div> 
					</div>
			    </div>   
			</div> 
		</div> 
	</div>
	<script type="text/javascript">
	
	$(function(){
		 var endTime="${endTime}";
		 var startTime="${startTime}";
		 var date = new Date(endTime);
		 date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
		 endTime = date.Format("yyyy-MM-dd");
		$('#regisInfoDgId').datagrid({ 
            fit:true,
            rownumbers:true,
            striped:true,
            border:false,
            checkOnSelect:true,
            selectOnCheck:false,
            singleSelect:true,
            pagination:true,
			pageSize:20,
	   		pageList:[20,30,50,100],
            url: "<%=basePath%>statistics/syntheticalStat/queryRegisterInfo.action",
            queryParams:{startTime:startTime,endTime:endTime,type:null,para:null,vague:0},
            onLoadSuccess: function(data){
				//分页工具栏作用提示
				var pager = $(this).datagrid('getPager');
				var aArr = $(pager).find('a');
				var iArr = $(pager).find('input');
				$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
				for(var i=0;i<aArr.length;i++){
					$(aArr[i]).tooltip({
						content:toolArr[i],
						hideDelay:1
					});
					$(aArr[i]).tooltip('hide');
				}
            },
            onClickRow:function(rowIndex, rowData){
				$('#recordNoTdId').html(rowData.recordNo);
				$('#registerNOTdId').html(rowData.registerNo);
				$('#patientNameTdId').html(rowData.patientName);
				$('#patientAgeTdId').html(rowData.patientAge);
				$('#registerDateTdId').html(rowData.registerDate);
				$('#idCardTdId').html(rowData.idCard);
				var val=rowData.birthday;
				var date = new Date(val);
				var dateFormater = date.Format("yyyy-MM-dd");
				$('#birthdayTdId').html(dateFormater);
				$('#payKindcodeTdId').html(rowData.payKindcode);
				$('#contractunitTdId').html(rowData.contractunit);
				$('#idcardNoTdId').html(rowData.idcardNo);
				$('#gradeTdId').html(rowData.grade);
				$('#regdeptTdId').html(rowData.regdept);
				$('#regdocTdId').html(rowData.regdoc);
				$('#seedeptTdId').html(rowData.seedept);
				$('#seedocTdId').html(rowData.seedoc);
				$('#invoiceTdId').html(rowData.invoice);
				//清空患者基本信息
				clearInfo();
				//获取患者基本信息
				$.ajax({
					type:"post",
					url:"<%=basePath%>statistics/syntheticalStat/queryPatientInfo.action",
					data:{patientId:rowData.patientId},
					success: function(data) {
						$('#nameInfoTdId').html(data.name);
						$('#sexInfoTdId').html(sexMap.get(data.sex));
						$('#unitInfoTdId').html(data.unit);
						$('#handbookInfoTdId').html(data.handbook);
						$('#nationalityInfoTdId').html(data.nationality);
						$('#nationInfoTdId').html(data.nation);
						$('#birthdayInfoTdId').html(data.birthday);
						$('#patientAgeTdId').text(data.age);
						$('#birthplaceInfoTdId').html(data.birthplace);
						$('#occupationInfoTdId').html(data.occupation);
						$('#certificatesTypeInfoTdId').html(data.certificatesType);
						$('#certificatesNoInfoTdId').html(data.certificatesNo);
						$('#workUnitInfoTdId').html(data.workUnit);
						$('#workPhoneInfoTdId').html(data.workPhone);
						$('#warriageInfoTdId').html(data.warriage);
						$('#nativeplaceInfoTdId').html(data.nativeplace);
						$('#cityInfoTdId').html(data.city);
						$('#doornoInfoTdId').html(data.doorno);
						$('#phoneInfoTdId').html(data.phone);
						$('#linkManInfoTdId').html(data.linkMan);
						$('#linkrelationInfoTdId').html(data.linkrelation); 
						$('#linkaddressInfoTdId').html(data.linkaddress);
						$('#linkdoornoInfoTdId').html(data.linkdoorno);
						$('#linkphoneInfoTdId').html(data.linkphone);
						$('#emailInfoTdId').html(data.email);
						$('#motherInfoTdId').html(data.mother);
			        },
			        error: function(){
			        	$.messager.alert('提示','患者基本信息获取失败！');
			        }
				});
				//清空发票信息
				$('#invoiceEdgId').datagrid('loadData',[]);
				//获取发票信息
				$.ajax({
					type:"post",
					url:"<%=basePath%>statistics/syntheticalStat/queryInvoiceInfo.action",
					data:{registerNo:rowData.registerNo,tab:rowData.tab,startTime:startTime,endTime:endTime},
					success: function(dataMap) {
						if(dataMap.resMsg=='success'){
							$('#invoiceEdgId').datagrid('loadData',dataMap.resCode);
						}
					},
			        error: function(){
			        	$.messager.alert('提示','发票信息获取失败！');
			        }
				});
				//获取历史医嘱树
				$('#etId').tree('options').url = "<%=basePath%>statistics/syntheticalStat/queryMedicalTree.action?recordNo="+rowData.recordNo;
				$('#etId').tree('reload');
				//清空医嘱信息
				$('#recipelEdgId').datagrid('loadData',[]);
            }
		});
		$('#etId').tree({
			animate:true,
			lines:true,
			formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					s += (node.children.length==0)?'':'&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
			},
			onDblClick: function(node){//点击节点
				if(node.attributes.type=="0"){
					return false;
				}else{
					$.ajax({
						type:"post",
						url:"<%=basePath%>statistics/syntheticalStat/queryMedicalInfo.action",
						data:{registerNo:node.id,tab:node.attributes.tab},
						success: function(dataMap) {
							$('#recipelEdgId').datagrid('loadData',[]);
							if(dataMap.resMsg=='error'){
								$.messager.alert('提示',dataMap.resCode);
								return;
							}
							$('#recipelEdgId').datagrid('loadData',dataMap.resCode);
						},
				        error: function(){
				        	$.messager.alert('提示','医嘱信息获取失败！');
				        }
					});
		    	}
			}
		});
		bindEnterEvent('para',searchEd,'easyui');
	});
	
	/**  
	 *  
	 * 清空患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function clearInfo(){
		$('#nameInfoTdId').html('');
		$('#sexInfoTdId').html('');
		$('#unitInfoTdId').html('');
		$('#handbookInfoTdId').html('');
		$('#nationalityInfoTdId').html('');
		$('#nationInfoTdId').html('');
		$('#birthdayInfoTdId').html('');
		$('#ageInfoTdId').html('');
		$('#birthplaceInfoTdId').html('');
		$('#occupationInfoTdId').html('');
		$('#certificatesTypeInfoTdId').html('');
		$('#certificatesNoInfoTdId').html('');
		$('#workUnitInfoTdId').html('');
		$('#workPhoneInfoTdId').html('');
		$('#warriageInfoTdId').html('');
		$('#nativeplaceInfoTdId').html('');
		$('#cityInfoTdId').html('');
		$('#doornoInfoTdId').html('');
		$('#phoneInfoTdId').html('');
		$('#linkManInfoTdId').html('');
		$('#linkrelationInfoTdId').html('');
		$('#linkaddressInfoTdId').html('');
		$('#linkdoornoInfoTdId').html('');
		$('#linkphoneInfoTdId').html('');
		$('#emailInfoTdId').html('');
		$('#motherInfoTdId').html('');
	}
	
	/**  
	 *  
	 * 清空时间查询
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function delTime(id){
		$('#'+id).val('');
	}
	
	
	function searchFinal(Stime,Etime){
	   var startTime = $('#startTime').val();
	   var endTime = $('#endTime').val();
	   if(startTime!=null&&startTime!=''&&endTime!=null&&endTime!=''){
		   if(Stime&&Etime){
	           if(Stime>Etime){
	             $.messager.alert("提示","开始时间不能大于结束时间！");
	             return ;
	           }
	         }
			var type="";
			var vague="";
			
			var type_ = $('#ect').combobox('getText');//类型0全部1挂号科室2挂号医生3挂号级别4合同单位
			switch (type_){
			case "全部":
				type=0;
				break;
			case "挂号科室":
				type=1;
				break;
			case "挂号医生":
				type=2;
				break;
			case "挂号级别":
				type=3;
				break;
			case "合同单位":
				type=4;
				break;
			}
			
			var vague_ = $('#ecv').combobox('getText');//0相等，1相似
			switch(vague_){
			case "相等":
				vague=0;
				break;
			case "相似":
				vague=1;
				break;
			}
			
			var para = $('#para').textbox('getText');//文本值
		    //$('#regisInfoDgId').datagrid('loadData', { total: 0, rows: [] });  
			$('#regisInfoDgId').datagrid('reload',{"startTime":Stime,"endTime":Etime,"type":type,"para":para,"vague":vague});
			
			
	   }else{
		   $.messager.alert("提示","挂号日期不能为空！");
	   }
      
	 }
	/**  
	 *  
	 * 条件查询
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function searchEd(){
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		var date = new Date(endTime);
		date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
		var end = date.Format("yyyy-MM-dd");
		searchFinal(startTime,end);
		$('#endTime').val(endTime);
		
		
	}
	//距离当前多少天的日期
	 function GetDateStr(AddDayCount) {
		 var dd = new Date();
		 dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
		 var y = dd.getFullYear();
		 var m = dd.getMonth()+1;//获取当前月份的日期
		 if(Number(m)<10){
	         m = "0"+m;
	       }
	       var d = dd.getDate();
	       if(Number(d)<10){
	         d = "0"+d;
	       }
		 return y+"-"+m+"-"+d;
	}
	 //查询当天
	function searchOne(){
		var Stime = GetDateStr(0);
		var Etime = GetDateStr(1);
		$('#startTime').val( GetDateStr(0));
		$('#endTime').val( GetDateStr(0));
		searchFinal(Stime,Etime);
		$("#endTime").val( GetDateStr(0));	
		}
	 //查询前三天
	function searchThree(){
		var Stime = GetDateStr(-3);
		var Etime = GetDateStr(0);
		$('#startTime').val( GetDateStr(-3));
		$("#endTime").val( GetDateStr(-1));	
		searchFinal(Stime,Etime);
		$("#endTime").val( GetDateStr(-1));	
	}
	//查询前七天
	function searchSeven(){
		var Stime = GetDateStr(-7);
		var Etime = GetDateStr(0);
		$('#startTime').val( GetDateStr(-7));
		$("#endTime").val( GetDateStr(-1));	
		searchFinal(Stime,Etime);
		$("#endTime").val(GetDateStr(-1));	
	}
	//查询前15天 zhangkui 2017-04-17
	function searchFifteen(){
		var Stime = GetDateStr(-15);
		var Etime = GetDateStr(0);
		$('#startTime').val( GetDateStr(-15));
		$("#endTime").val( GetDateStr(-1));	
		searchFinal(Stime,Etime);
		$("#endTime").val(GetDateStr(-1));	
	}
	//上月
	function beforeMonth(){
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth();
        var nowMonth = month;
        var nowYear = year;
        if(month==0)
        {
            month=12;
            nowMonth = "01";
            year=year-1;
        }
        if (month < 10) {
            nowMonth = "0" +(month+1);
            month = "0" + month;
        }
        var Stime = year + "-" + month + "-" + "01";//上个月的第一天
        var lastDate = new Date(year, month, 0);
        var lastDay = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天
        var Etime= nowYear+"-"+nowMonth+"-01";

        $('#startTime').val(Stime);
        $('#endTime').val(Etime);
        searchFinal(Stime,Etime);
        $('#endTime').val(lastDay);

    }
	
	//日期格式转换
	Date.prototype.Format = function(fmt)   
	{   
	  var o = {   
	    "M+" : this.getMonth()+1,                 //月份   
	    "d+" : this.getDate(),                    //日   
	    "h+" : this.getHours(),                   //小时   
	    "m+" : this.getMinutes(),                 //分   
	    "s+" : this.getSeconds(),                 //秒   
	    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
	    "S"  : this.getMilliseconds()             //毫秒   
	  };   
	  if(/(y+)/.test(fmt))   
	    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	  for(var k in o)   
	    if(new RegExp("("+ k +")").test(fmt))   
	  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
	  return fmt;   
	} 
	//获取每月第一天
	function getCurrentMonthFirst(){
		 var date=new Date();
		 date.setDate(1);
		 return date.Format("yyyy-MM-dd");
	}
	//获取每月最后一天
	function getCurrentMonthLast(){
		 var date=new Date();
		 var currentMonth=date.getMonth();
		 var nextMonth=++currentMonth;
		 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
		 var oneDay=1000*60*60*24;
		 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
	} 
	//查询当月
	function searchMonth(){
		var Stime = getCurrentMonthFirst();
		//var Etime = getCurrentMonthLast();
		//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
		//2017-04-17新的
// 		var date=new Date();
// 		var Etime = date.Format("yyyy-MM-dd");
		var Etime = GetDateStr(1);	
		$('#startTime').val( Stime);
		$("#endTime").val( GetDateStr(0));	
		searchFinal(Stime,Etime);
		$("#endTime").val( GetDateStr(0));	
	}
	//查询当年
	function searchYear(){
		
		//var Etime = new Date().getFullYear()+"-12-31";
		//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
		//2017-04-17新的
// 		var date=new Date();
// 		var Etime = date.Format("yyyy-MM-dd");
		var Etime = GetDateStr(1);	
		var Stime = new Date().getFullYear()+"-01-01";
		$('#startTime').val(Stime);
		$("#endTime").val( GetDateStr(0));
		searchFinal(Stime,Etime);
		$("#endTime").val( GetDateStr(0));	
	}
	
	
	
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#startTime').val('${startTime }');
		$('#endTime').val('${endTime }');
		$('#ect').combobox('setValue','全部');
		$('#ecv').combobox('setValue','相等');
		$('#para').textbox('setValue','');
		//清空挂号信息查询的数据
		$('#recordNoTdId').html("");
		$('#registerNOTdId').html("");
		$('#patientNameTdId').html("");
		$('#patientAgeTdId').html("");
		$('#registerDateTdId').html("");
		$('#idCardTdId').html("");
		$('#birthdayTdId').html("");
		$('#payKindcodeTdId').html("");
		$('#contractunitTdId').html("");
		$('#idcardNoTdId').html("");
		$('#gradeTdId').html("");
		$('#regdeptTdId').html("");
		$('#regdocTdId').html("");
		$('#seedeptTdId').html("");
		$('#seedocTdId').html("");
		$('#invoiceTdId').html("");
		//清空患者基本信息
		clearInfo();
		//清空发票信息
		$('#invoiceEdgId').datagrid('loadData',[]);
		//清空医嘱信息
		$('#recipelEdgId').datagrid('loadData',[]);
		//清空医嘱树
		$('#etId').tree('loadData',{});
		searchEd();
	}
	
	/**  
	 *  
	 * 退出
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function closeTab(){
		window.parent.$('#tabs').tabs('close','门诊综合查询');
	}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>