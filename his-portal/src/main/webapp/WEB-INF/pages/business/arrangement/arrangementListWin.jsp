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
</head>
<body>
	<div style="width: 100%;height: 100%;">
		<form id="applyForm" method="post">
		<table class="honry-table" style="width:100%;height:100%;border: 1px;" id='sq' >
			<tr>
			 	<td style="text-align: right;width:15%;background-color: #E0ECFF;">合并疾病编号：</td>
				<td style="width:15%; "colspan="">
					<input class="easyui-textbox" id=uniteOpid name="operationApply.uniteOpid"  >
				</td>
				 <td style="text-align: right;width:15%;background-color: #E0ECFF;">合并疾病：</td>
				<td style="width:15%; ">
					<input class="easyui-textbox" id=uniteDisease name="operationApply.uniteDisease" style="width: 95%" data-options="required:true">
				</td>
				 <td style="text-align: right;width:15%;background-color: #E0ECFF;">手术分类：</td>
				<td style="width:15%;"><input id="opTypeWin" name="operationApply.opType"  class="easyui-combobox" size="20"></td>
			</tr>
			<tr>
			    <td style="text-align: right;width:15%;background-color: #E0ECFF;">手术室：</td>
				<td style="width:15%;">
					<input class="easyui-combobox" id="opRoom" name="operationApply.execDept"  >
				</td> 
				<td  style="text-align: right;width:15%;background-color: #E0ECFF;">是否自定义手术：</td>
			    <td style="width:15%;">
			    	<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 5px" >
			    		<input type="checkbox" id="operation" name="isOrNo" onclick="ssmcdy('operation')"  >
					</div>
					<div style="float:left;width:93%;height: 100%" onclick="kdfw('operation')"></div>
			    </td>
			     <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">是否自定义诊断：</td>
				<td style="width: 15%">
				<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 5px" >
					<input type="checkbox" id="operationZD"name="operationZD" onclick="sszd()" >
				</div>
				<div style="float:left;width:93%;height: 100%" onclick="kdfw('operationZD')"></div>
				</td>
			</tr>
			<tr id='trsqzd1'>
			    <td  style="text-align: right;background-color: #E0ECFF;width:15%;">术前诊断1：</td>
				<td colspan="5" style="width: 75%"><input class="easyui-combogrid" style="width: 95%" id="shoushuzd1" name="diagName" data-options="required:true">
				<a href="javascript:void(0)" id="ashoushuzd1" onclick="add('术前诊断','trsqzd',1,'shoushuzd','diagName','ashoushuzd','shoushuRemove')" class="easyui-linkbutton" data-options="iconCls:'icon-add'"></a></td>
			</tr>
			<tr id='trndss1'>
				<td  style="text-align: right;background-color: #E0ECFF;width: 15%">拟手术名称1：</td>
				<td colspan="5"tyle="width: 75%"><input class="easyui-combogrid" style="width: 95%;" id="nssmc1" name="itemName" data-options="required:true"> 
				  <a href="javascript:void(0)" id="anssmc1" onclick="add('拟手术名称','trndss','1','nssmc','itemName','anssmc','mincehngRemove')" class="easyui-linkbutton" data-options="iconCls:'icon-add'">
				</td>
			</tr>
			<tr>
				<td  style="text-align: right;background-color: #E0ECFF;width: 15%">预约时间：</td>
				<td style="width: 15% ">
				<input id="nishoushu" name="preDate" class="Wdate" type="text"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:00',onpicked:preDatePicked,minDate:'%y-%M-%d',maxDate:'{%y+1}-%M-%d'})"style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<td  style="text-align: right;background-color: #E0ECFF; width: 15%">预定用时：</td>
				<td style="width: 15%"><input id="duration" name="operationApply.duration" class="easyui-numberbox" 
				data-options="required:true,showSeconds:false,min:0,precision:1"></td>         
			   <td style="text-align: right;background-color: #E0ECFF;width: 15%">手术体位：</td>
				<td style="width: 15%"><input id="opertionposition" name="operationApply.opertionposition" class="easyui-combobox"></td>
			</tr>
			<tr>
			    <td  style="text-align: right;background-color: #E0ECFF;width: 15%">手术医生科室：</td>
				<td style="width: 15%"><input id="execDept" name="operationApply.opDoctordept" class="easyui-combobox" data-options="required:true">
				</td>
			    <td  style="text-align: right;background-color: #E0ECFF;width: 15%">手术医生 ：</td>
				<td style="width: 15%"><input id="ssysbm" name="operationApply.opDoctor" class="easyui-combobox" data-options="required:true"></td>
			    <td  style="text-align: right;background-color: #E0ECFF; width: 15%">指导医生 ：</td>
				<td style="width: 15%"><input id="guiDoctor" name="operationApply.guiDoctor" class="easyui-combobox" ></td>
			</tr>
			<tr>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;" nowrap="nowrap">是否需要巡回护士：</td>
				<td style="width: 15%">
				<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 5px" >
					<input id="isneedprep" type="checkbox" onclick="isneedprepabc()">
				</div>
				<div style="float:left;width:93%;height: 100%" onclick="kdfw('isneedprep')"></div>
				<input type="hidden" id="isneedprep1" name="operationApply.isneedprep"></td>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;" nowrap="nowrap">是否需要随台护士：</td>
				<td style="width: 15%">
				<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 5px" >
					<input id="isneedacco"  type="checkbox" onclick="isneedaccoabc()">
				</div>
				<div style="float:left;width:93%;height: 100%" onclick="kdfw('isneedacco')"></div>
				<input type="hidden" id="isneedacco1" name="operationApply.isneedacco">
				</td>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;">随台护士数：</td>
				<td style="width: 15%"> <input id="accoNurse" name="operationApply.accoNurse" class="easyui-numberbox" readonly="readonly"></td>
			</tr>
			<tr>
			    <td  style="text-align: right;background-color: #E0ECFF;width: 15%">巡回护士数：</td>
				<td style="width: 15%"><input id="prepNurse" name="operationApply.prepNurse" class="easyui-numberbox" readonly="readonly" ></td>
				<td  style="text-align: right;background-color: #E0ECFF;width: 15%">洗手护士数：</td>
				<td style="width: 15%"><input id="washNurse" name="operationApply.washNurse" class="easyui-numberbox" ></td>
				<td  style="text-align: right;background-color: #E0ECFF;width: 15%">助手数：</td>
				<td style="width: 15%"><input id="helperNum" name="operationApply.helperNum" class="easyui-numberbox" ></td>
			</tr>
			<tr id='trzs0'>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;">巡回护士1：</td>
				<td style="width: 15%"><input id="xunhui0" name="tour" class="easyui-combogrid" readonly="readonly" disabled="disabled" >
				</td>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;">洗手护士1：</td>
				<td style="width: 15%"><input id="xishou0" name="wash" class="easyui-combogrid" readonly="readonly" disabled="disabled" >
				</td>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">助手1：</td>
				<td style="width: 15%">
					<input id="zhushou0" name="thelper" class="easyui-combobox" readonly="readonly" disabled="disabled" >
				<a href="javascript:void(0)" id="afz0" onclick="addfz('trzs','0','afz')" class="easyui-linkbutton" data-options="iconCls:'icon-add'"></td>
			</tr>
			<tr>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">切口类型：</td>
				<td style="width: 15%"><input id="inciType" name="operationApply.inciType" class="easyui-combobox" ></td>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">感染类型：</td>
				<td style="width: 15%"><input id="infectType" name="operationApply.infectType" class="easyui-combobox" ></td>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;">手术注意事项：</td>
				<td style="width: 15%"><input id="opsNote" name="operationApply.opsNote" class="easyui-textbox" ></td>
			</tr>
			<tr>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">临床表现：</td>
				<td style="width: 15%"><input id="clinical" name="operationApply.clinical" class="easyui-textbox" ></td>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">手术禁忌症：</td>
				<td style="width: 15%"><input id="contraindication" name="operationApply.contraindication" class="easyui-textbox" ></td>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;">手术适应症：</td>
				<td style="width: 15%"><input id="indication" name="operationApply.indication" class="easyui-textbox" ></td>
			</tr>
			<tr>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">目前病人情况：</td>
				<td style="width: 15%"><input id="stitution" name="operationApply.stitution" class="easyui-textbox" ></td>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">术前准备情况：</td>
				<td style="width: 15%"><input id="preparation" name="operationApply.preparation" class="easyui-textbox" ></td>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;">可能的并发症：</td>
				<td style="width: 15%"><input id="complication" name="operationApply.complication" class="easyui-textbox" ></td>
			</tr>
			<tr>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">签字家属：</td>
				<td style="width: 15%;"><input id="folk" name="operationApply.folk"class="easyui-textbox" data-options="required:true" ></td>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">家属关系：</td>
				<td style="width: 15%"><input id="relaCode" name="operationApply.relaCode" class="easyui-combobox" ></td>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;">家属意见：</td>
				<td style="width: 15%"><input id="folkComment" name="operationApply.folkComment" class="easyui-textbox" ></td>
			</tr>
			<tr>
			<tr>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">手术规模：</td>
				<td style="width: 15%"><input id="degreeWin" name="operationApply.degree" class="easyui-combobox" ></td>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;">麻醉类型：</td>
				<td style="width: 15%">
					<input id="anesTypeWin" name="operationApply.anesType" class="easyui-combobox"/>
				</td>
				<td  style="text-align: right;width: 15%;background-color: #E0ECFF;">麻醉方式：</td>
				<td style="width: 15%">
					<input id="aneWayWin" name="operationApply.aneWay" class="easyui-combobox"/>
				</td>
			</tr>
			    <td  style="text-align: right;width: 15%;background-color: #E0ECFF;">台类型：</td>
				<td style="width: 15%"><input id="consoleTypeWin" name="operationApply.consoleType" class="easyui-combobox">
				    <input id="patientName" name="operationApply.name" type="hidden" >
					<input id="sex" name="operationApply.sex" type="hidden">
					<input id="reportAge" name="operationApply.age" type="hidden">
					<input id="deptCode" name="operationApply.inDept" type="hidden">
					<input id="bedNo" type="hidden" name="operationApply.bedNo" >
					<input id="inp" name="operationApply.clinicCode" type="hidden">
					<input id="medicalrecord1" name="operationApply.patientNo" type="hidden">
				</td>
				<td  style="text-align: right;width: 15%">是否加急：</td>
				<td style="width: 15%">
				<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 5px" >
					<input id="isurgent"  type="checkbox" onclick="isurgentabc()">
				</div>
				<div style="float:left;width:93%;height: 100%" onclick="kdfw('isurgent')"></div>
				<input type="hidden" name="operationApply.isurgent" id="isurgent1" >
				</td>
				<td style="text-align: right;width: 15%">是否同意自费项目：</td>
				<td style="width: 15%">
				<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 5px" >
					<input type="checkbox" id="zifei"onclick="sfzf()">
				</div>
				<div style="float:left;width:93%;height: 100%" onclick="kdfw('zifei')"></div>
				<input type="hidden" id="zifei1" name="operationApply.isownexpense">
				</td>
			</tr>
			<tr>
				<td  style="text-align: right;width: 15%">特殊手术：</td>
				<td style="width: 15%">
					<div  style="float:left;width: 6%;height: 100%" >
						<input type="checkbox" id="isspecial"  onclick="onclickBoxspecial()">
					</div>
					<div  style="float:left;width:94%;height: 100%" onclick="kdfw('isspecial')"></div>
					<input type="hidden" name="operationApply.isspecial" id="isspecial1">
				</td>
				<td style="text-align: right;width: 15%">是否需要病理检查：</td>
				<td style="width: 15%">
					<div align="center" style="float:left;width: 7%;height: 100%" >
						<input id="isneedpathology"  type="checkbox" onclick="isneedpathologyabc()">
					</div>
					<div style="float:left;width:93%;height: 100%" onclick="kdfw('isneedpathology')"></div>
					<input type="hidden" name="operationApply.isneedpathology" id="isneedpathology1"></td>
				<td style="text-align: right;width: 15%">是否自体血回输：</td>
				<td style="width: 15%">
				<div align="center" style="float:left;width: 7%;height: 100%" >
					<input id="isautoblood"  type="checkbox" onclick="isautobloodabc()">
				</div>
				<div style="float:left;width:93%;height: 100%" onclick="kdfw('isautoblood')"></div>
				<input type="hidden" name="operationApply.isautoblood" id="isautoblood1">
				</td>
			</tr>
			<tr>
			   <td  style="text-align: right;width: 15%">是否有菌：</td>
			    <td style="width: 15%">
			    <div align="center" style="float:left;width: 7%;height: 100%" >
				    <input type="checkbox" id="isgerm"  onclick="isgermabc()">
				</div>
				<div style="float:left;width:93%;height: 100%" onclick="kdfw('isgerm')"></div>
			    <input type="hidden" name="operationApply.isgerm" id="isgerm1">
				</td>
				<td  style="text-align: right;width: 15%">是否重症：</td>
				<td style="width: 15%">
				<div align="center" style="float:left;width: 7%;height: 100%" >
					<input id="isheavy"  type="checkbox" onclick="isheavyabc()">
				</div>
				<div style="float:left;width:93%;height: 100%" onclick="kdfw('isheavy')"></div>
				<input type="hidden" name="operationApply.isheavy" id="isheavy1"></td>
			    
			</tr>
			<tr>
				<td style="text-align: right;width: 15%;background-color: #E0ECFF;">特殊说明：</td>
				<td colspan="5" style="width: 75%">
					<textarea id="applyRemark" name="operationApply.applyRemark" cols="140" rows="3"></textarea>
				</td>
			</tr>
		</table>
		<div >
							<input type="button" value="审批" style="background-color:lime;" id="shenpi">
						</div>
						<div id="spxx" data-options="title:'审批信息'," style="width: 100%;display: none;" >
								 <table style="width: 100%;" class="honry-table">
										<tr >
											<td style="background-color:silver;width: 15%;">一级审批人：</td>
											<td style="width: 17%"><input class="easyui-combobox" id="apprDoctor" name="operationApply.apprDoctor" ></td>
											<td style="background-color:silver;width: 15%;">二级审批人：</td>
											<td style="width: 17%"><input class="easyui-combobox" id="apprDoctor2" name="operationApply.apprDoctor2" ></td>
											<td style="background-color:silver;width: 15%;">三级审批人：</td>
											<td style="width: 17%"><input class="easyui-combobox" id="apprDoctor3" name="operationApply.apprDoctor3" ></td>
										</tr>
										<tr>
											<td style="background-color:silver;width: 15%;">一级审批时间：</td>
											<td style="width: 17%">
											<input id="apprDate" name="operationApply.apprDate" class="Wdate" type="text"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:00'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" >
											</td>
											<td style="background-color:silver;width: 15%;">二级审批时间：</td>
											<td style="width: 17%">
											<input id="apprDate2" name="operationApply.apprDate2" class="Wdate" type="text"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:00'})"style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;">
											</td>
											<td style="background-color:silver;width: 15%;">三级审批时间：</td>
											<td style="width: 17%">
											<input id="apprDate3" name="operationApply.apprDate3" class="Wdate" type="text"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:00'})"style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;">
											</td>
										</tr>
										<tr>
											<td style="background-color:silver;width: 15%;">一级审批备注：</td>
											<td style="width: 17%"><input  id="apprRemark" name="operationApply.apprRemark" style="height: 30px;width: 85%"></td>
											<td style="background-color:silver;width: 15%;">二级审批备注：</td>
											<td style="width: 17%"><input  id="apprRemark2" name="operationApply.apprRemark2" style="height: 30px;width: 85%"></td>
											<td style="background-color:silver;width: 15%;">三级审批备注：</td>
											<td style="width: 17%;"><input  id="apprRemark3" name="operationApply.apprRemark3" style="height: 30px;width: 85% "></td>
										</tr>
								</table>
						</div>
		</form>
		<div style="margin: 5px 5px 5px;text-align: center;padding-top: 15px;">
	 	<shiro:hasPermission name="${menuAlias}:function:save"> 
			<a href="javascript:void(0)" onclick="saveCombine()" class="easyui-linkbutton" iconCls="icon-save">保&nbsp;存&nbsp;</a>
		 </shiro:hasPermission> 
		<div>
	</div>
	<script type="text/javascript">
	var patientNo = "${param.patientNo}";
	var ids = "${param.ids}";
	var deptId="${deptCname }";
	var nurseMap=new Map();//护士翻页渲染
	var diagMap=new Map();//诊断翻页渲染
	var operatNameMap=new Map();//手术名称翻页渲染
	
	/**
	 * @Description：预约时间 增加新日历空间选中事件
	 * @Author：hedong
	 * @CreateDate：2017-3-22
	 */
	 function preDatePicked(){
		//queryIfHaveConsole();
	} 
	
	/**  
	 *  
	 * @Description：是否需要巡回护士
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	function isneedprepabc(){
		var xunhui=$('[id^=xunhui]');
		if ($('#isneedprep').is(':checked')) {
			$('#isneedprep1').val(1);
			$("#prepNurse").textbox("readonly",false);
		}else {
			$('#isneedprep1').val(0);
			$("#prepNurse").textbox("readonly");
			xunhui.each(function(){
				$(this).combogrid("readonly",true);
				$(this).combogrid('clear');
				$("#prepNurse").textbox('clear');
				$(this).combogrid('disable');
			});
		}
	}
	
	/**  
	 *  
	 * @Description：是否需要随台护士
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	function isneedaccoabc(){
		if ($('#isneedacco').is(':checked')) {
			$('#isneedacco1').val(1);
			$("#accoNurse").textbox("readonly",false);
		}else {
			$('#isneedacco1').val(0);
			$("#accoNurse").textbox("readonly");
		}
	}
	/**  
	 *  
	 * @Description：是否自费
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	function sfzf(){
		if($("#zifei").is(":checked")){
			$("#zifei1").val(1);
		}else{
			$("#zifei1").val(0);
		}
	}
	
	/**  
	 *  
	 * @Description：是否自体血回输
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	 function isautobloodabc() {
	 	if ($('#isautoblood').is(':checked')) {
	 		$('#isautoblood1').val(1);
	 	}else {
	 		$('#isautoblood1').val(0);
	 	}
	 }
	
	/**  
	 *  
	 * @Description：是否需要病理检查
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	 function isneedpathologyabc() {
	 	if ($('#isneedpathology').is(':checked')) {
	 		$('#isneedpathology1').val(1);
	 	}else {
	 		$('#isneedpathology1').val(0);
	 	}
	 }
	
	/**  
	 *  
	 * @Description：单选按钮是否特殊手术:
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	 function onclickBoxspecial() {
	 	if ($('#isspecial').is(':checked')) {
	 		$('#isspecial').val(1);
	 	}else {
	 		$('#isspecial1').val(0);
	 	}
	 }
	
	/**  
	 *  
	 * @Description：是否重症
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	function isheavyabc(){
		if ($('#isheavy').is(':checked')) {
			$('#isheavy1').val(1);
		}else{
			$('#isheavy1').val(0);
		}
	}
	
	/**  
	 *  
	 * @Description：是否加急
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	function isurgentabc(){
		if ($('#isurgent').is(':checked')) {
			$('#isurgent1').val(1);
		}else {
			$('#isurgent1').val(0);
		}
	}
	
	/**  
	 *  
	 * @Description：是否有菌
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	 function isgermabc() {
	 	if ($('#isgerm').is(':checked')) {
	 		$('#isgerm1').val(1);
	 	}else {
	 		$('#isgerm1').val(0);
	 	}
	 }
	/**
	 * 手术台
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月16日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	$('#consoleWin').combobox({
		url:"<%=basePath%>operation/arrangement/getConsoleValid.action",
		editable : false,
		valueField:'consoleCode',    
	    textField:'consoleName'
	});
	
	/**  
	 *  
	 * @Description：家属关系
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	$("#relaCode").combobox({
		url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=relation",
		valueField:'encode',    
	    textField:'name',
    	editable : true,
	});
	 
	/**  
	 *  
	 * @Description：判断名字是否重复
	 * @Author：zhangjin
	 * @CreateDate：2016-5-25
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	$.extend($.fn.validatebox.defaults.rules, {  
		validName : { // 术前诊断
			zs: { // 助手名称
				validator : function(value, param) { 
					var mc = $('[id^=zhushou]');
					var b = 0
	   	 			mc.each(function(index,obj){
	   	 				if($(obj).combobox('getText') == value){
	   	 					b++;
	   	 				}
    				});
	   			return b>1?false:true;
				},  
			}
		}
	});
	
	//手术诊断
	$('#shoushuzd1').combogrid({
		url : '<%=basePath%>operation/operationList/icdCombobox2fy.action',
	 	idField : 'code',
	 	textField : 'name',
	 	multiple : false,
	 	editable : true,
	 	mode:"remote",
	 	pageList:[10,20,30,40,50],
		 pageSize:"10",
		 pagination:true,
	 	columns:[[    
	 	         {field:'code',title:'编码',width:'18%'},    
	 	         {field:'name',title:'名称',width:'20%'},    
	 	         {field:'pinyin',title:'拼音',width:'20%'},    
	 	         {field:'wb',title:'五笔',width:'18%'},
 	        	 {field:'inputcode',title:'自定义码',width:'18%'}
	 	     ]],  
		 onLoadSuccess: function (){
		      var id=$(this).prop("id");
		      if ($("#"+id).combogrid('getValue')&&diagMap.get($("#"+id).combogrid('getValue'))) {
	            	$("#"+id).combogrid('setText',diagMap.get($("#"+id).combogrid('getValue')));
	            } 
		  },
		  onHidePanel:function(none){
		  	    var val = $(this).combogrid('getValue');
		  	    var name = $(this).combogrid('getText');
		  	    if(validName(name)){
		    		 $(this).combogrid("clear")
				    	$.messager.alert("提示","诊断名称不能重复!");
			    		setTimeout(function(){
							$(".messager-body").window('close');
						},2000);
				    	
				    }
		     	if(name==val&&name!=null&&name!=""){
		     		if(!$('#operationZD').is(':checked')){
		     	    	$.messager.confirm('提示','该条信息在手术诊断信息下拉表格中不存在，是否更改为自定义诊断？',function(r){
		     	    		if(r){
		     	    			$("#operationZD").prop("checked",true);
		     	    			sszd();
		     	    		}
		     	    	})
		     		}
		     	}
			  }
	});
	
	//手术名称
	$('#nssmc1').combogrid({
		url : '<%=basePath %>operation/operationList/undrugComboboxfy.action',
		idField : 'code',
		textField : 'name',
		multiple : false,
		editable : true,
		mode:"remote",
		pageList:[10,20,30,40,50],
	 	pageSize:"10",
	 	pagination:true,
	 	multiple : false,
		columns:[[    
		         {field:'code',title:'编码',width:'18%'},    
		         {field:'name',title:'名称',width:'20%'},    
		         {field:'undrugPinyin',title:'拼音码',width:'20%'},
		         {field:'undrugWb',title:'五笔码',width:'18%'},    
		         {field:'undrugInputcode',title:'自定义码',width:'20%'}
		     ]],  
	 	onHidePanel:function(none){
		  	   var val = $(this).combogrid('getValue');
		  	   var name = $(this).combogrid('getText');
		  	   if(nssmcName(name)){
		  		    $(this).combogrid("clear");
			    	$.messager.alert("提示","手术名称不能重复!");
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
			    }
		     	if(name==val&&name!=null&&name!=""){
		     		if(!$('#operation').is(':checked')){
		     	    	$.messager.confirm('提示','该条信息在手术名称信息下拉表格中不存在，是否更改为自定义手术？',function(r){
		     	    		if(r){
		     	    			$("#operation").prop("checked",true);
		     	    			ssmcdy();
		     	    		}
		     	    	})
		     		}
		     	}
		  	},
	    onLoadSuccess: function () {
	    	var id=$(this).prop("id");
	        if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
            } 
        }  
	});
	
	//手术医生科室
	$('#execDept').combobox({
		url:'<%=basePath %>operation/operationList/querysysDeptmentkeshi.action',
		editable : true,
		valueField:'deptCode',
	    textField:'deptName',
	    mode:'local',
		multiple : false,
		editable : true,
	    onHidePanel:function(none){
		    var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].deptCode) {
		            result = false;
		        }
		    }
		    if (result) {
		        $(this).combobox("clear");
		    }else{
		        $(this).combobox('unselect',val);
		        $(this).combobox('select',val);
		    }
		},
		filter: function(q, row){
			var keys = new Array();
			keys[keys.length] = 'deptName';
			keys[keys.length] = 'deptPinyin';
			keys[keys.length] = 'deptWb';
			keys[keys.length] = 'deptInputcode';
			keys[keys.length] = 'deptCode';
			return filterLocalCombobox(q, row, keys);
		},
		onSelect:function(data){

		}
	});
	
	//手术医生
	$('#ssysbm').combobox({
		editable : true,
		valueField:'jobNo',    
	    textField:'name',
	    data:user,
	    filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'jobNo';
			keys[keys.length] = 'code';
			keys[keys.length] = 'name';
			keys[keys.length] = 'pinyin';
			keys[keys.length] = 'wb';
			keys[keys.length] = 'inputCode';
			return filterLocalCombobox(q, row, keys);
		},onSelect : function(record) {
	 		var id=$(this).prop("id");
	 		var value=$("#"+id).combobox("getValue");
	 		var ssysbm=$('#guiDoctor').combobox("getValue");
	 		var zs=$('[id^=zhushou]');
	 		if(value!=""&&value!=null){
	 			zs.each(function(){
	 	 			var zsval=$(this).combobox("getValue");
	 	 			if(zsval!=null&&zsval!=""){
	 	 				if(value==zsval){
	 	 					$("#"+id).combobox("clear");
	 	 	 				$.messager.alert("提示","手术医生与助手医生不能相同");
	 	 	 				setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
	 	 	 				return
	 	 	 			}
	 	 			}
	 	 		});
	 			if(ssysbm!=null&&ssysbm!=""){
	 				if(ssysbm==value){
	 					$("#"+id).combobox("clear");
	 	 				$.messager.alert("提示","手术医生不能与指导医生相同");
	 	 				setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
	 	 			}
	 			}
	 		}
		}
	});
	
	//指导医生
	$('#guiDoctor').combobox({
		editable : true,
		valueField:'jobNo',    
	    textField:'name',
	    data:user,
	    onSelect : function(record) {
			var id=$(this).prop("id");
	 		var value=$("#"+id).combobox("getValue");
	 		var ssysbm=$('#ssysbm').combobox("getValue");
	 		var zs=$('[id^=zhushou]');
	 		if(value!=""&&value!=null){
	 			zs.each(function(){
	 	 			var zsval=$(this).combobox("getValue");
	 	 			if(zsval!=null&&zsval!=""){
	 	 				if(value==zsval){
	 	 					$("#"+id).combobox("clear");
	 	 	 				$.messager.alert("提示","指导医生与助手医生不能相同");
	 	 	 				setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
	 	 	 				return
	 	 	 			}
	 	 			}
	 	 		});
	 			if(ssysbm!=null&&ssysbm!=""){
	 				if(ssysbm==value){
	 					$("#"+id).combobox("clear");
	 	 				$.messager.alert("提示","指导医生不能与手术医生相同");
	 	 				setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
	 	 			}
	 			}
	 		}
		},
	    filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'jobNo';
			keys[keys.length] = 'code';
			keys[keys.length] = 'name';
			keys[keys.length] = 'pinyin';
			keys[keys.length] = 'wb';
			keys[keys.length] = 'inputCode';
			return filterLocalCombobox(q, row, keys);
		}
	});
	
setTimeout(function(){
	$(function(){
		//审批按钮
		$(document).ready(function(e){
	 		$("#shenpi").click(function(e){
	 	 		$("#spxx").toggle();
		 	});
	 	});
		$('#medicalrecord1').val(patientNo);
		//手术室
		$('#opRoom').combobox({
			url:'<%=basePath%>operation/operationList/querysysDeptmentShi.action',
			editable : true,
			valueField:'deptCode',    
		    textField:'deptName',
		 	filter:function(q,row){//hedong 20170309 增加科室过滤
				 var keys = new Array();
				 keys[keys.length] = 'deptName';//部门名称
				 keys[keys.length] = 'deptCode';//系统编号
				 keys[keys.length] = 'deptPinyin';//部门拼音
				 keys[keys.length] = 'deptWb';//部门五笔
			     keys[keys.length] = 'deptInputCode';//自定义码
				return filterLocalCombobox(q, row, keys);
			}
		});
		$('#opRoom').combobox("setValue",deptId);
		 //助手数量控制
		 $("#helperNum").numberbox("textbox").bind('keyup',function(event){
			 var hnum=$('#helperNum').numberbox("getText");
			 kztrzs();
			var zs=$('[id^=zhushou]');
			 var reg = /^[1-9]\d*$/;
			zs.each(function(){
				var id=$(this).prop('id');
				var num=id.substring(7,8);
				if(parseInt(num)+1>hnum){
					$(this).combobox("readonly",true);
					$(this).combogrid('disable');
				}else{
					if(!reg.test(hnum)){
						$(this).combogrid("readonly",true);
						$(this).combogrid('disable');
					}else{
						$(this).combogrid("readonly",false);
						$(this).combogrid('enable');
					}
				}
			});
		});
		 //巡回数量控制
		 $("#prepNurse").numberbox("textbox").bind('keyup',function(event){
			 var hnum=$('#prepNurse').numberbox("getText");
			 kztrzs();
			var xunhui=$('[id^=xunhui]');
			var reg = /^[1-9]\d*$/;
			xunhui.each(function(){
				var id=$(this).prop('id');
				var num=id.substring(6,7);
				if(parseInt(num)+1>hnum){
					$(this).combogrid("readonly",true);
					$(this).combogrid('disable');
				}else{
					if(!reg.test(hnum)){
						$(this).combogrid("readonly",true);
						$(this).combogrid('disable');
					}else{
						$(this).combogrid("readonly",false);
						$(this).combogrid('enable');
					}
					
				}
			});
		});
		 //洗手数量控制
		 $("#washNurse").numberbox("textbox").bind('keyup',function(event){
			 var hnum=$('#washNurse').numberbox("getText");
			 kztrzs();
			var zs=$('[id^=xishou]');
			 var reg = /^[1-9]\d*$/;
			zs.each(function(){
				var id=$(this).prop('id');
				var num=id.substring(6,7);
				if(parseInt(num)+1>hnum){
					$(this).combogrid("readonly",true);
					$(this).combogrid('disable');
				}else{
					if(!reg.test(hnum)){
						$(this).combogrid("readonly",true);
						$(this).combogrid('disable');
					}else{
						$(this).combogrid("readonly",false);
						$(this).combogrid('enable');
					}
				}
			});
		});
		 
		//护士渲染
			$.ajax({
				url : '<%=basePath %>operation/operationList/ssSysEmployeeList.action',
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						nurseMap.put(v[i].jobNo,v[i].name);
					}
				}
			});
			//诊断翻页渲染
			$.ajax({
				url : '<%=basePath %>operation/operationList/icdCombobox2.action',
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						diagMap.put(v[i].code,v[i].name);
					}
				}
		  	});
			
			//手术名称翻页渲染
			$.ajax({
				url : '<%=basePath %>operation/operationList/undrugCombobox.action',
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						operatNameMap.put(v[i].code,v[i].name);
					}
				}
		  	});
	});
},500);
	
	//助手医生
	$('#zhushou0').combobox({
		editable : true,
		valueField:'jobNo',    
	    textField:'name',
	    validType:'zs',
	    data:user,
	    filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'jobNo';
			keys[keys.length] = 'code';
			keys[keys.length] = 'name';
			keys[keys.length] = 'pinyin';
			keys[keys.length] = 'wb';
			keys[keys.length] = 'inputCode';
			return filterLocalCombobox(q, row, keys);
		},
		 onSelect : function() {
			 var zsDoc=$('[id^=zhushou]');
		    	var id=$(this).prop("id");
		    	var ssysbm=$('#ssysbm').combobox("getValue");
	     		var gui=$('#guiDoctor').combobox("getValue");
	    		var m=new Map();
    			zsDoc.each(function(){
	    			var zsys2=$(this).combobox("getValue");
	    			if(m.get(zsys2)!='1'){
	    				m.put(zsys2,'1');
	    			}else{
    					$('#'+id).combobox('clear');
    					$.messager.alert("提示","助手重复!","info");
    					setTimeout(function(){
							$(".messager-body").window('close');
						},2000);
			    		return; 
	    			}
	    			if(ssysbm!=null&&ssysbm!=""){
	 	 				if(zsys2==ssysbm){
	 	 					$("#"+id).combobox("clear");
	 	 	 				$.messager.alert("提示","手术医生与助手医生不能相同");
	 	 	 				setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
	 	 	 				return
	 	 	 			}
	 	 			}
	    			if(gui!=null&&gui!=""){
		 				if(gui==zsys2){
		 					$("#"+id).combobox("clear");
		 	 				$.messager.alert("提示","助手医生不能与指导医生相同");
		 	 				setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
		 	 			}
		 			}
	    		});
	    	}
	});
	
	
	//洗手护士
	$('#xishou0').combogrid({
		url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
 		idField : 'jobNo',
 		textField : 'name',
 		mode:"remote",
 		panelAlign:'right',
 		panelWidth:325,
 		editable : true,
 		pageList:[10,20,30,40,50],
		pageSize:"10",
		pagination:true,
	 	columns:[[   
				{field:'jobNo',title:'工作号',width:'130'},
	 	         {field:'name',title:'名称',width:'160'} 
	 	        
 	     ]],  
 	    onSelect:function(rowIndex, rowData){
	    	if(xishouName(rowData.jobNo)){
		    		var xsId=$("#xs0").val();
		    		if(xsId){
		    			$('#xishou0').combogrid("clear");
				    	$.messager.alert("提示","护士信息不能重复!");
				    	setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
		    		}else{
		    			$('#xishou0').combogrid("clear");
				    	$.messager.alert("提示","护士信息不能重复!");
				    	setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
		    		}
			    }
	    	
	     },
	     onLoadSuccess: function () {
	    	    var id=$(this).prop("id");
	            if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
            } 
        }
	});
	//手术分类
	$('#opTypeWin').combobox({
		url:"<%=basePath%>operation/operationList/queryCodeOperatetype.action",
		editable : false,
	    valueField:'encode',    
	    textField:'name'
	});
	//巡回护士
	$('#xunhui0').combogrid({
		url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
 		idField : 'jobNo',
 		textField : 'name',
 		mode:"remote",
 		panelAlign:'left',
 		panelWidth:325,
 		 readonly:true,
 		editable : true,
 		pageList:[10,20,30,40,50],
		 pageSize:"10",
		 pagination:true,
	 	columns:[[   
				{field:'jobNo',title:'工作号',width:'130'},
	 	         {field:'name',title:'名称',width:'160'} 
	 	        
 	     ]],  
 	    onSelect:function(rowIndex, rowData){
	    	if(xunhuiName(rowData.jobNo)){
	    		$('#xunhui0').combogrid("clear");
		    	$.messager.alert("提示","护士信息不能重复!");
		    	setTimeout(function(){
					$(".messager-body").window('close');
				},3000);
		    	
		    }
	     },
	    onLoadSuccess: function () {
		    	var id=$(this).prop("id");
		       if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
	            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
	            } 
	      }  
	});
	
	/**  
	 *  
	 * @Description： 手术规模
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	$("#degreeWin").combobox({
		url:"<%=basePath%>operation/operationList/queryCodeScaleofoperation.action",
		valueField:'encode',    
	    textField:'name',
    	editable : false
	});
	
	//麻醉类别
	$("#anesTypeWin").combobox({
		url:"<%=basePath%>operation/operationList/queryCodeanesType.action",
		valueField:'encode',    
		textField:'name',
		editable : true,
	 	filter:function(q,row){//hedong 20170309 增加科室过滤
			 var keys = new Array();
			 keys[keys.length] = 'name';//部门名称
			 keys[keys.length] = 'encode';//系统编号
			 keys[keys.length] = 'pinyin';//部门拼音
			 keys[keys.length] = 'wb';//部门五笔
		     keys[keys.length] = 'inputCode';//自定义码
			return filterLocalCombobox(q, row, keys);
		}
	}); 
	
	//麻醉方式
	$("#aneWayWin").combobox({
		url : '<%=basePath %>operation/operationList/likeAneway.action',
		editable : false,
		valueField:'encode',    
	    textField:'name',
	});
	
	//手术台类别
	$("#consoleTypeWin").combobox({
		url : '<%=basePath %>operation/operationList/CodeconsoleType.action',
		valueField:'encode',    
	    textField:'name',
	    editable : false,
	});
	
	
	$('#opertionposition').combobox({
		url:"<%=basePath%>operation/operationList/queryOperationPosition.action",
		valueField:'encode',    
	    textField:'name',
	    editable : false
	});
	
	//切口类型
	$('#inciType').combobox({
		url: '<%=basePath %>operation/operationList/queryCodeIncitype.action',
		editable : true,
	    valueField:'encode',    
	    textField:'name',
	 	filter:function(q,row){//hedong 20170309 增加科室过滤
			 var keys = new Array();
			 keys[keys.length] = 'name';//部门名称
			 keys[keys.length] = 'encode';//系统编号
			 keys[keys.length] = 'pinyin';//部门拼音
			 keys[keys.length] = 'wb';//部门五笔
		     keys[keys.length] = 'inputCode';//自定义码
			return filterLocalCombobox(q, row, keys);
		}
	});
	
	//感染类型
	$('#infectType').combobox({
		url: '<%=basePath %>operation/operationList/queryCodeInfecttype.action',
		editable : true,
	    valueField:'encode',    
	    textField:'name',
	 	filter:function(q,row){//hedong 20170309 增加科室过滤
			 var keys = new Array();
			 keys[keys.length] = 'name';//部门名称
			 keys[keys.length] = 'encode';//系统编号
			 keys[keys.length] = 'pinyin';//部门拼音
			 keys[keys.length] = 'wb';//部门五笔
		     keys[keys.length] = 'inputCode';//自定义码
			return filterLocalCombobox(q, row, keys);
		}
	});
	
	/**
	 * @Description:添加术前诊断和拟手术名称
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月18日
	 * @param:type:添加的内容的类型
	 * @param:trId：行ID属性
	 * @param:index：下标
	 * @param:inputID：标签的ID属性
	 * @param:inputName：标签的name属性
	 * @param:addId:添加按钮的ID属性
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	var a = 2;//术前诊断
	var b = 2;//拟手术名称
	function add(type,trId,index,inputID,inputName,addId,removeId){
		var i = (type=="术前诊断"&&a)||(type=="拟手术名称"&&b);
		var htmlText = "<tr id=\""+trId+i+"\">"+
							"<td style=\"text-align: right;background-color: #E0ECFF;\">"+type+i+"："+"</td>"+
							"<td colspan=\"5\">"+
								"<input style=\"width: 95%;\" id=\""+inputID+i+"\" name=\""+inputName+"\"/>"+
								"<a href=\"javascript:void(0)\" id=\""+addId+i+"\" onclick=\"add('"+type+"','"+trId+"',"+i+",'"+inputID+"','"+inputName+"','"+addId+"','"+removeId+"')\"></a>"+
								"<a href=\"javascript:void(0)\" id=\""+removeId+i+"\" onclick=\"removeTr1('"+type+"','"+trId+"',"+i+",'"+removeId+"','"+addId+"')\"></a>"+
							"</td>"+
						"</tr>";
		$('#'+trId+""+index).after(htmlText);//添加一行
		$("#"+addId+i).linkbutton({
			iconCls:'icon-add'
		});
		$("#"+removeId+i).linkbutton({
			iconCls:'icon-remove'
		});
		if(type=="术前诊断"){//渲染添加的行
			if($("#operationZD").is(":checked")){
				$('#'+inputID+""+i).combogrid({
					data : [],
				 	idField : 'code',
				 	textField : 'name',
				 	multiple : false,
				 	editable : true,
				 	mode:"remote",
				 	pageList:[10,20,30,40,50],
					 pageSize:"10",
					 pagination:true,
				 	columns:[[    
				 	         {field:'code',title:'编码',width:'18%'},    
				 	         {field:'name',title:'名称',width:'20%'},    
				 	         {field:'pinyin',title:'拼音',width:'20%'},    
				 	         {field:'wb',title:'五笔',width:'18%'},
			 	        	 {field:'inputcode',title:'自定义码',width:'20%'}
				 	     ]],  
				 	    onHidePanel:function(none){
						  	   var name = $(this).combogrid('getText');
						  	   if(validName(name)){
					    		 $(this).combogrid("clear")
							    	$.messager.alert("提示","诊断名称不能重复!");
						    		setTimeout(function(){
										$(".messager-body").window('close');
									},2000);
							    	
							    }
						  	},
					  onLoadSuccess: function (){
	  				      var id=$(this).prop("id");
	  				      if ($("#"+id).combogrid('getValue')&&diagMap.get($("#"+id).combogrid('getValue'))) {
	  			            	$("#"+id).combogrid('setText',diagMap.get($("#"+id).combogrid('getValue')));
	  			            } 
	  			      }  
				}); 
			}else{
				$('#'+inputID+""+i).combogrid({
					url : '<%=basePath%>operation/operationList/icdCombobox2fy.action',
				 	idField : 'code',
				 	textField : 'name',
				 	multiple : false,
				 	editable : true,
				 	mode:"remote",
				 	pageList:[10,20,30,40,50],
					 pageSize:"10",
					 pagination:true,
				 	columns:[[    
				 	         {field:'code',title:'编码',width:'18%'},    
				 	         {field:'name',title:'名称',width:'20%'},    
				 	         {field:'pinyin',title:'拼音',width:'20%'},    
				 	         {field:'wb',title:'五笔',width:'18%'},
			 	        	 {field:'inputcode',title:'自定义码',width:'20%'}
				 	     ]],  
				     onHidePanel:function(none){
				    	 var val = $(this).combogrid('getValue');
					  	 var name = $(this).combogrid('getText');
				    	 if(validName(name)){
				    		 $(this).combogrid("clear")
						    	$.messager.alert("提示","诊断名称不能重复!");
					    		setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    	
						    }
					     	if(name==val&&name!=null&&name!=""){
					     		if(!$('#operationZD').is(':checked')){
					     	    	$.messager.confirm('提示','该条信息在手术诊断信息下拉表格中不存在，是否更改为自定义诊断？',function(r){
					     	    		if(r){
					     	    			$("#operationZD").prop("checked",true);
					     	    			sszd();
					     	    		}
					     	    	})
					     		}
					     	}
					  	},
					 onLoadSuccess: function (){
	  				      var id=$(this).prop("id");
	  				      if ($("#"+id).combogrid('getValue')&&diagMap.get($("#"+id).combogrid('getValue'))) {
	  			            	$("#"+id).combogrid('setText',diagMap.get($("#"+id).combogrid('getValue')));
	  			            } 
	  			      }  
				}); 
			}
		}else if(type=="拟手术名称"){//渲染添加的行
			if($("#operation").is(":checked")){
				$('#'+inputID+""+i).combogrid({
					data:[],
					idField : 'code',
					textField : 'name',
					multiple : false,
					editable : true,
					mode:"remote",
					pageList:[10,20,30,40,50],
				 	pageSize:"10",
				 	pagination:true,
				 	multiple : false,
					columns:[[    
					         {field:'code',title:'编码',width:'20%'},    
					         {field:'name',title:'名称',width:'20%'},    
					         {field:'undrugPinyin',title:'拼音码',width:'20%'},
					         {field:'undrugWb',title:'五笔码',width:'20%'},    
					         {field:'undrugInputcode',title:'自定义码',width:'20%'}
					     ]],  
				     onHidePanel:function(none){
					  	   var name = $(this).combogrid('getText');
					  	   if(nssmcName(name)){
					  		    $(this).combogrid("clear");
						    	$.messager.alert("提示","手术名称不能重复!");
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    }
					 },
				    onLoadSuccess: function () {
				    	var id=$(this).prop("id");
				        if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
			            } 
			        }  
				});
			}else{
				$('#'+inputID+""+i).combogrid({
					url : '<%=basePath %>operation/operationList/undrugComboboxfy.action',
					idField : 'code',
					textField : 'name',
					multiple : false,
					editable : true,
					mode:"remote",
					pageList:[10,20,30,40,50],
				 	pageSize:"10",
				 	pagination:true,
				 	multiple : false,
					columns:[[    
					         {field:'code',title:'编码',width:'20%'},    
					         {field:'name',title:'名称',width:'20%'},    
					         {field:'undrugPinyin',title:'拼音码',width:'20%'},
					         {field:'undrugWb',title:'五笔码',width:'20%'},    
					         {field:'undrugInputcode',title:'自定义码',width:'20%'}
					     ]],  
				 	onHidePanel:function(none){
					  	   var val = $(this).combogrid('getValue');
					  	   var name = $(this).combogrid('getText');
					  	   if(nssmcName(name)){
					  		    $(this).combogrid("clear");
						    	$.messager.alert("提示","手术名称不能重复!");
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    }
					     	if(name==val&&name!=null&&name!=""){
					     		if(!$('#operation').is(':checked')){
					     	    	$.messager.confirm('提示','该条信息在手术名称信息下拉表格中不存在，是否更改为自定义手术？',function(r){
					     	    		if(r){
					     	    			$("#operation").prop("checked",true);
					     	    			ssmcdy();
					     	    		}
					     	    	})
					     		}
					     	}
					  	},
				    onLoadSuccess: function () {
				    	var id=$(this).prop("id");
				        if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
			            } 
			        }  
				});
			}
			
		}
		if(index==1){
			$("#"+addId+index).hide();
		}else{
			$("#"+addId+(i-1)).hide();
			$("#"+removeId+(i-1)).hide();
		}
		(type=="术前诊断"&&a++)||(type=="拟手术名称"&&b++);
	}
	
	/**
	 * @Description:删除行
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月18日
	 * @param:trId：行ID属性
	 * @param:index：下标
	 * @param:aId:a标签的ID
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function removeTr1(type,trId,index,removeId,addId){
		$('#'+trId+index).remove();
		$('#'+removeId+(index-1)).show();
		$('#'+addId+(index-1)).show();
		(type=="术前诊断"&&a--)||(type=="拟手术名称"&&b--);
	}
	/**  
	 *  
	 * @Description：数量控制tr的条数
	 * @Author：zhangjin
	 * @CreateDate：2017-2-22
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	function kztrzs(){
			 var hnum=$('#washNurse').numberbox("getText");
			 var hnum3=$('#helperNum').numberbox("getText");
			 var hnum2=$('#prepNurse').numberbox("getText");
			 var big=0;
			 if(parseInt(!hnum?"0":hnum)>parseInt(!hnum2?'0':hnum2)&&parseInt(!hnum?'0':hnum)>parseInt(!hnum3?'0':hnum3)){
				 big=parseInt(hnum);
			 }else if(parseInt(!hnum2?'0':hnum2)>parseInt(!hnum?'0':hnum)&&parseInt(!hnum2?'0':hnum2)>parseInt(!hnum3?'0':hnum3)){
				 big=parseInt(hnum2);
			 }else{
				 big=parseInt(hnum3);
			 }
			 var trzs=$('[id^=trzs]');
			 trzs.each(function(){
				var trId=$(this).prop("id");
				var trnu=trId.substring(4,5);
				if((parseInt(trnu)+1)>big){
					$(this).remove();
					$('#afz'+(parseInt(trnu)-1)).show();
					$('#jafz'+(parseInt(trnu)-1)).show();
				}
				
			 });
			
	}
	/**
	 * @Description:添加护士和助手
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月18日
	 * @param:trId：行ID属性
	 * @param:index：下标
	 * @param:aId:a标签的ID
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function addfz(trId,index,aId){
		var nu = parseInt(index)+1;
		var nu2 = nu+1;
		var maxhel=$("#helperNum").numberbox("getValue");
		var maxpre=$("#prepNurse").numberbox("getValue");
		var maxwash=$("#washNurse").numberbox("getValue");
		max = maxhel>maxpre?maxhel:maxpre;
		max = max>maxwash?max:maxwash;
		if(max>=nu2){
			$("#"+trId+index).after("<tr id=\""+trId+nu+"\">"+
					"<td  style=\"text-align: right;background-color: #E0ECFF;width: 15%\">巡回护士"+nu2+"：</td>"+
					"<td style=\"width: 15%\"><input id=\"xunhui"+nu+"\" name=\"tour"+nu+"\">"+
					"</td>"+
					"<td  style=\"text-align: right;background-color: #E0ECFF;width: 15%\">洗手护士"+nu2+"：</td>"+
					"<td style=\"width: 15%\"><input id=\"xishou"+nu+"\" name=\"wash"+nu+"\">"+
					"</td>"+
					"<td  style=\"text-align: right;background-color: #E0ECFF;;width: 15%\">助手"+nu2+"：</td>"+
					"<td style=\"width: 15%\"><input id=\"zhushou"+nu+"\" name=\"thelper"+nu+"\" >"+
					"<a id=\""+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"addfz('"+trId+"','"+nu+"','"+aId+"')\"></a>"+
					"<a id=\"j"+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr2('"+trId+"','"+aId+"','"+index+"')\"></a></td>"+
				"</tr>"); 
			if(maxhel>=nu2){
				$('#zhushou'+nu).combobox({ 
					editable : true,
				    valueField:'jobNo',    
				    textField:'name', 
				    readonly:false,
			    	 validType:'zs',
			    	 data:user,
			    	 onSelect : function() {
			    		    var zs = $('[id^=zhushou]');
					    	var id=$(this).prop("id");
					    	var ssysbm=$('#ssysbm').combobox("getValue");
				     		var gui=$('#guiDoctor').combobox("getValue");
				    		var m=new Map();
				    		zs.each(function(){
				    			var val = $(this).combobox('getValue');
				    			if(m.get(val)!='1'){
				    				m.put(val,'1');
				    			}else{
				    				$('#'+id).combobox('clear');
						    		$.messager.alert("提示","助手重复!","info");
						    		setTimeout(function(){
										$(".messager-body").window('close');
									},2000);
						    		return; 
				    			}
				    			if(ssysbm!=null&&ssysbm!=""){
				 	 				if(val==ssysbm){
				 	 					$("#"+id).combobox("clear");
				 	 	 				$.messager.alert("提示","手术医生与助手医生不能相同");
				 	 	 				setTimeout(function(){
											$(".messager-body").window('close');
										},3000);
				 	 	 				return
				 	 	 			}
				 	 			}
				    			if(gui!=null&&gui!=""){
					 				if(gui==val){
					 					$("#"+id).combobox("clear");
					 	 				$.messager.alert("提示","助手医生不能与指导医生相同");
					 	 				setTimeout(function(){
											$(".messager-body").window('close');
										},3000);
					 	 			}
					 			}
				    		});
					    },filter:function(q,row){
							var keys = new Array();
							keys[keys.length] = 'jobNo';
							keys[keys.length] = 'code';
							keys[keys.length] = 'name';
							keys[keys.length] = 'pinyin';
							keys[keys.length] = 'wb';
							keys[keys.length] = 'inputCode';
							return filterLocalCombobox(q, row, keys);
						}
					});
			}else{
				$('#zhushou'+nu).combobox({ 
					editable : true,
				    valueField:'jobNo',    
				    textField:'name',
				    readonly:true,
			    	 validType:'zs',
			    	 data:user,
			    	 onSelect : function() {
			    		    var zs = $('[id^=zhushou]');
					    	var id=$(this).prop("id");
					    	var ssysbm=$('#ssysbm').combobox("getValue");
				     		var gui=$('#guiDoctor').combobox("getValue");
				    		var m=new Map();
				    		zs.each(function(){
				    			var val = $(this).combobox('getValue');
				    			if(m.get(val)!='1'){
				    				m.put(val,'1');
				    			}else{
				    				$('#'+id).combobox('clear');
						    		$.messager.alert("提示","助手重复!","info");
						    		setTimeout(function(){
										$(".messager-body").window('close');
									},2000);
						    		return; 
				    			}
				     			if(ssysbm!=null&&ssysbm!=""){
				 	 				if(val==ssysbm){
				 	 					$("#"+id).combobox("clear");
				 	 	 				$.messager.alert("提示","手术医生与助手医生不能相同");
				 	 	 				setTimeout(function(){
											$(".messager-body").window('close');
										},3000);
				 	 	 				return
				 	 	 			}
				 	 			}
				    			if(gui!=null&&gui!=""){
					 				if(gui==val){
					 					$("#"+id).combobox("clear");
					 	 				$.messager.alert("提示","助手医生不能与指导医生相同");
					 	 				setTimeout(function(){
											$(".messager-body").window('close');
										},3000);
					 	 			}
					 			}
				    		});
					    },
				    	filter:function(q,row){
							var keys = new Array();
							keys[keys.length] = 'jobNo';
							keys[keys.length] = 'code';
							keys[keys.length] = 'name';
							keys[keys.length] = 'pinyin';
							keys[keys.length] = 'wb';
							keys[keys.length] = 'inputCode';
							return filterLocalCombobox(q, row, keys);
						}
					});
			}
			if(maxwash>=nu2){
				$('#xishou'+nu).combogrid({ 
					url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'left',
			 		panelWidth:325,
			 		editable : true,
			 		pageList:[10,20,30,40,50],
					 pageSize:"10",
					 validType:'text',
					 pagination:true,
				 	columns:[[   
							{field:'jobNo',title:'工作号',width:'130'},
				 	         {field:'name',title:'名称',width:'160'} 
				 	        
			 	     ]],  
			 	    onSelect:function(rowIndex, rowData){
				    	if(xishouName(rowData.jobNo)){
				    		$('#xishou'+nu).combogrid("clear");
					    	$.messager.alert("提示","护士信息不能重复!");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    	
					    }
				     },
				    onLoadSuccess: function () {
				    	var id=$(this).prop("id");
				       if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
			            } 
			      }  
				}); 
			}else{
				$('#xishou'+nu).combogrid({ 
					url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'left',
			 		panelWidth:325,
			 		editable : true,
			 		readonly:true,
			 		pageList:[10,20,30,40,50],
					 pageSize:"10",
					 validType:'text',
					 pagination:true,
				 	columns:[[   
							{field:'jobNo',title:'工作号',width:'130'},
				 	         {field:'name',title:'名称',width:'160'} 
				 	        
			 	     ]],  
			 	    onSelect:function(rowIndex, rowData){
				    	if(xishouName(rowData.jobNo)){
				    		$('#xishou'+nu).combogrid("clear");
					    	$.messager.alert("提示","护士信息不能重复!");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    	
					    }
				     },
				    onLoadSuccess: function () {
				    	var id=$(this).prop("id");
				       if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
			            } 
			      }  
				}); 
			}
			if ($('#isneedprep').is(':checked')) {
				if(maxpre>=nu2){
					$('#xunhui'+nu).combogrid({
						url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
				 		idField : 'jobNo',
				 		textField : 'name',
				 		mode:"remote",
				 		panelAlign:'left',
				 		panelWidth:325,
				 		editable : true,
				 		pageList:[10,20,30,40,50],
						 pageSize:"10",
						 validType:'text',
						 pagination:true,
					 	columns:[[   
								{field:'jobNo',title:'工作号',width:'130'},
					 	         {field:'name',title:'名称',width:'160'} 
					 	        
				 	     ]],  
				 	    onHidePanel:function(none){
					 	      var val = $(this).combogrid('getValue');
						 	     if(xunhuiName(val)){
						 	    	$(this).combogrid('clear');
						 	    	$.messager.alert("提示","护士信息不能重复!");
						 	    	setTimeout(function(){
										$(".messager-body").window('close');
									},2000);
							    }
					 	 }, 
					    onLoadSuccess: function () {
					    	var id=$(this).prop("id");
					       if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
				            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
				            } 
				      }  
					});   
				}else{
					$('#xunhui'+nu).combogrid({
						url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
				 		idField : 'jobNo',
				 		textField : 'name',
				 		mode:"remote",
				 		panelAlign:'left',
				 		panelWidth:325,
				 		editable : true,
				 		readonly:true,
				 		pageList:[10,20,30,40,50],
						 pageSize:"10",
						 validType:'text',
						 pagination:true,
					 	columns:[[   
								{field:'jobNo',title:'工作号',width:'130'},
					 	         {field:'name',title:'名称',width:'160'} 
					 	        
				 	     ]],  
				 	    onHidePanel:function(none){
					 	      var val = $(this).combogrid('getValue');
						 	     if(xunhuiName(val)){
						 	    	$(this).combogrid('clear');
						 	    	$.messager.alert("提示","护士信息不能重复!");
						 	    	setTimeout(function(){
										$(".messager-body").window('close');
									},2000);
							    }
					 	 }, 
					    onLoadSuccess: function () {
					    	var id=$(this).prop("id");
					        if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
				            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
				            } 
				      }  
					});   
				}
			}else{
				$('#xunhui'+nu).combogrid({    
					url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'right',
			 		readonly:true,
			 		panelWidth:325,
			 		editable : true,
			 		pageList:[10,20,30,40,50],
					 pageSize:"10",
					 validType:'text',
					 pagination:true,
				 	columns:[[   
							{field:'jobNo',title:'工作号',width:'130'},
				 	         {field:'name',title:'名称',width:'160'}
				 	        
			 	     ]],  
			 	    onHidePanel:function(none){
				 	      var val = $(this).combogrid('getValue');
					 	     if(xunhuiName(val)){
					 	    	$(this).combogrid('clear');
					 	    	$.messager.alert("提示","护士信息不能重复!");
					 	    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    }
				 	 }, 
				    onLoadSuccess: function () {
				    	var id=$(this).prop("id");
				        if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
			            } 
			      	}  
				});
			}
			$('#'+aId+nu).linkbutton({    
			    iconCls: 'icon-add'   
			});  
			$('#j'+aId+nu).linkbutton({    
			    iconCls: 'icon-remove'   
			});  
			$('#'+aId+index).hide();
			$('#j'+aId+index).hide();
			indexfz=nu2;
		}else{
			$.messager.alert("提示","超过了安排人数");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
		}
		
	}
	
	/**
	 * @Description:删除行
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月18日
	 * @param:trId：行ID属性
	 * @param:index：下标
	 * @param:aId:a标签的ID
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function removeTr2(trId,aId,index){
		$('#'+trId+(parseInt(index)+1)).remove();
		$('#'+aId+index).show();
		$('#j'+aId+index).show();
	}
	
	/**
	 * @Description:保存按钮
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月18日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function saveCombine(){
		var sho=$("#nishoushu").val();
		if(!sho){
			$.messager.alert("提示","请选择预约时间");
			return;
		}
		if($("#nishoushu").val().length<19){
			$.messager.alert("提示","日期格式不正确 ，正确格式：2010-03-22 11:31:00");
			return;
		}
		 
		/**
		 *手术名称
		 */
		
		var idArr = $('[id^=nssmc]');
		var itemNameStr = "";
		idArr.each(function(){
			var idArrValue = $(this).prop('id');
			var id = idArrValue.substring(idArrValue.indexOf("_")+1);
			var newValue=$('#'+idArrValue).combogrid('getValue');
			var newText= $('#'+idArrValue).combogrid('getText');
			if(newValue){
				itemNameStr+=newText+","+newValue+"_add#";//添加
			}
		});
		/**
		 *诊断名称
		 */
		var zdArr = $('[id^=shoushuzd]');
		var diagNameStr = "";
		zdArr.each(function(){
			var zdArrValue = $(this).prop('id');
			var zdId = zdArrValue.substring(zdArrValue.indexOf("_")+1);
			var newValue = $('#'+zdArrValue).combogrid('getValue');
			var newText = $('#'+zdArrValue).combogrid('getText');
			if(newText){
				diagNameStr+=newText+","+newValue+"_add#";//添加
			}
		});
		/**
		 *洗手护士
		 */
		var xsArr = $('[id^=xishou]');
		var washStr = "";
		xsArr.each(function(){
			var xsArrValue = $(this).prop('id');
			var xsnum=xsArrValue.substring(6,7);
			var xsId = xsArrValue.substring(xsArrValue.indexOf("_")+1);
			var newValue = $('#'+xsArrValue).combogrid('getValue');
			var newText = $('#'+xsArrValue).combogrid('getText');
			if(newValue){
				if(newText==newValue){
					$("#"+xsArrValue).combogrid("clear");
					$.messager.alert("提示","洗手护士信息有误！");
				}else{
					washStr+= newText+","+xsnum+","+newValue+"_add#";//添加
				}
			}
		});
		/**
		 *巡回护士
		 */
		var xhArr = $('[id^=xunhui]');
		var tourStr = "";
		xhArr.each(function(){
			var xhArrValue = $(this).prop('id');
			var xhnum=xhArrValue.substring(6,7);
			var xhId = xhArrValue.substring(xhArrValue.indexOf("_")+1);
			var newValue = $('#'+xhArrValue).combogrid('getValue');
			var newText = $('#'+xhArrValue).combogrid('getText');
			if(newValue){
				if(newText==newValue){
					$("#"+xhArrValue).combogrid("clear");
					$.messager.alert("提示","巡回护士信息有误！");
				}else{
					tourStr+= newText+","+xhnum+","+newValue+"_add#";//添加
				}
			}
		});
		/**
		 *助手
		 */
		var zsArr = $('[id^=zhushou]');
		var thelperStr = "";
		zsArr.each(function(){
			var zsArrValue = $(this).prop('id');
			var zsnum=zsArrValue.substring(7,8);
			var zsId = zsArrValue.substring(zsArrValue.indexOf("_")+1);
			var newValue = $('#'+zsArrValue).combobox('getValue');
			var newText = $('#'+zsArrValue).combobox('getText');
			if(newValue){
				if(newText==newValue){
					$("#"+zsArrValue).combobox("clear");
					$.messager.alert("提示","助手信息有误！");
				}else{
					thelperStr+=newText+","+zsnum+","+newValue+"_add#";//添加
				}
				
			}
		});
		$('#applyForm').form('submit',{
			url:'<%=basePath%>operation/arrangement/operationCombine.action?menuAlias=${menuAlias}',
			onSubmit: function(param){  
				$.messager.progress('close');
				if(!$('#applyForm').form('validate')){
					return false;
				}
		        param.zhenDuanStr = diagNameStr;
		        param.itemNameStr = itemNameStr;
		        param.zsDocStr = thelperStr;
		        param.xiShouStr = washStr;
		        param.xunHuiStr = tourStr;
		        param.ids = ids;
		        $.messager.progress({text:'保存中，请稍后...',modal:true});
		    },
			success:function(data){
				if(data=="success"){
					$.messager.alert("提示","保存成功！");
					$('#list').datagrid('reload');
					$('#list').datagrid('clearChecked');
					$("#shousap").window('close');
					$.messager.progress('close');
					 $("#applyForm").form("reset");
					clearTime();
				}
				if(data=="error"){
					$.messager.alert("提示","保存失败！","info");
					$.messager.progress('close');
					return false;
				}
			}
		});
	}
	/**  
	 *  
	 * @Description：手术诊断是否自定义
	 * @Author：zhangjin
	 * @CreateDate：2016-11-2
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
	function sszd(){
		var en=$('[id^=shoushuzd]');
		if ($('#operationZD').is(':checked')) {
			en.each(function(index,obj){
				$(obj).combogrid("clear")
			    $(obj).combogrid({
			    	data:[],
				    mode:"local",
				    onBeforeLoad:function(){
			        	return false;
			        },
			        onHidePanel:function(none){
					  	   var name = $(this).combogrid('getText');
					  	   if(validName(name)){
				    		 $(this).combogrid("clear")
						    	$.messager.alert("提示","诊断名称不能重复!");
					    		setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    	
						    }
					  	}
				});  
				$(obj).combogrid('grid').datagrid('loadData',{total:0,rows:[]}); 
			});
	 	}else {
	 		//术前诊断1
	 		en.each(function(){
	 			$(this).combogrid({
	 				url : '<%=basePath%>operation/operationList/icdCombobox2fy.action',
	 			 	idField : 'code',
	 			 	textField : 'name',
	 			 	multiple : false,
	 			 	editable : true,
	 			 	mode:"remote",
	 			 	pageList:[10,20,30,40,50],
	 				 pageSize:"10",
	 				 pagination:true,
	 			 	columns:[[    
	 			 	         {field:'code',title:'编码',width:'18%'},    
	 			 	         {field:'name',title:'名称',width:'20%'},    
	 			 	         {field:'pinyin',title:'拼音',width:'20%'},    
	 			 	         {field:'wb',title:'五笔',width:'18%'},
	 		 	        	 {field:'inputcode',title:'自定义码',width:'20%'}
	 			 	     ]],  
					 onLoadSuccess: function (){
	  				      var id=$(this).prop("id");
	  				      if ($("#"+id).combogrid('getValue')&&diagMap.get($("#"+id).combogrid('getValue'))) {
	  			            	$("#"+id).combogrid('setText',diagMap.get($("#"+id).combogrid('getValue')));
	  			            } 
	  			      },
	  			    onHidePanel:function(none){
					  	   var val = $(this).combogrid('getValue');
					  	   var name = $(this).combogrid('getText');
					  	   if(validName(name)){
				    		 $(this).combogrid("clear")
						    	$.messager.alert("提示","诊断名称不能重复!");
					    		setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    	
						    }
					     	if(name==val&&name!=null&&name!=""){
					     		if(!$('#operationZD').is(':checked')){
					     	    	$.messager.confirm('提示','该条信息在手术诊断信息下拉表格中不存在，是否更改为自定义诊断？',function(r){
					     	    		if(r){
					     	    			$("#operationZD").prop("checked",true);
					     	    			sszd();
					     	    		}
					     	    	})
					     		}
					     	}
					  	},
					  	onBeforeLoad:function(){
				        	return true;
				        } 
			 	});
	 		})
	 	}
	}
	
	/**
	 * @Description:是否是自定义手术
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月18日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function ssmcdy(inputId){
		var niShouShu = $('[id^=nssmc]');
		if($('#operation').is(":checked")){
			niShouShu.each(function(index,obj){
				$(obj).combogrid("clear")
			    $(obj).combogrid({
			    	data:[],
				    mode:"local",
				    onBeforeLoad:function(){
			        	return false;
			        },
			        onHidePanel:function(none){
					  	   var name = $(this).combogrid('getText');
					  	   if(nssmcName(name)){
					  		    $(this).combogrid("clear");
						    	$.messager.alert("提示","手术名称不能重复!");
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    }
					 }
				});  
				$(obj).combogrid('grid').datagrid('loadData',{total:0,rows:[]}); 
			});
		}else{
			niShouShu.each(function(){
				$(this).combogrid({
					url : '<%=basePath %>operation/operationList/undrugComboboxfy.action',
					idField : 'code',
					textField : 'name',
					multiple : false,
					editable : true,
					mode:"remote",
					pageList:[10,20,30,40,50],
				 	pageSize:"10",
				 	pagination:true,
				 	multiple : false,
					columns:[[    
					         {field:'code',title:'编码',width:'20%'},    
					         {field:'name',title:'名称',width:'20%'},    
					         {field:'undrugPinyin',title:'拼音码',width:'20%'},
					         {field:'undrugWb',title:'五笔码',width:'20%'},    
					         {field:'undrugInputcode',title:'自定义码',width:'20%'}
					     ]],  
				    onLoadSuccess: function () {
				    	var id=$(this).prop("id");
				        if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
			            } 
			        },
			        onHidePanel:function(none){
					  	   var val = $(this).combogrid('getValue');
					  	   var name = $(this).combogrid('getText');
					  	   if(nssmcName(name)){
					  		    $(this).combogrid("clear");
						    	$.messager.alert("提示","手术名称不能重复!");
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    }
					     	if(name==val&&name!=null&&name!=""){
					     		if(!$('#operation').is(':checked')){
					     	    	$.messager.confirm('提示','该条信息在手术名称下拉表格中不存在，是否更改为自定义手术？',function(r){
					     	    		if(r){
					     	    			$("#operation").prop("checked",true);
					     	    			ssmcdy();
					     	    		}
					     	    	})
					     		}
					     	}
					  	},
			        onBeforeLoad:function(){
			        	return true;
			        } 
				});
			});
		}
	}
	
	//审批人
	 $("#apprDoctor").combobox({
			valueField : 'jobNo',
			textField : 'name',
			multiple : false,
			editable : true,
			data:user,
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
	//二级审批人
	 $('#apprDoctor2').combobox({
			valueField : 'jobNo',
			textField : 'name',
			multiple : false,
			editable : true,
			data:user,
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
	//三级审批人
	 $('#apprDoctor3').combobox({
			valueField : 'jobNo',
			textField : 'name',
			multiple : false,
			editable : true,
			data:user,
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
	/**  
	 *  
	 * @Description：过滤	
	 * @Author：zhangjin
	 * @CreateDate：2016-11-1
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
function filterLocalCombobox(q, row, keys){
	if(keys!=null && keys.length > 0){//
		for(var i=0;i<keys.length;i++){ 
			if(row[keys[i]]!=null&&row[keys[i]]!=''){
					var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
					if(istrue==true){
						return true;
					}
			}
		}
	}else{
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q.toUpperCase()) > -1;
	}
}
	
	
//巡回护士信息的验证
function xunhuiName(name){
	var mc = $('[id^=xunhui]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getValue') == name&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
			b++;
		}
	});
 	var c=b>1?true:false;
        if(!c){
     		var xs = $('[id^=xishou]');
        	var a = 0
 			xs.each(function(index,obj){
 				if($(obj).combogrid('getValue') == name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
 					a++;
 				}
				});
          	return a==1?true:false;
        }else{
     	   return true;
        }
}
//洗手护士信息的验证
function xishouName(name){
	var mc = $('[id^=xishou]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getValue') == name&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
			b++;
		}
	});
 	var c=b>1?true:false;
       if(!c){
    		var xs = $('[id^=xunhui]');
       		var a = 0
			xs.each(function(index,obj){
				if($(obj).combogrid('getValue') ==name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
					a++;
				}
			});
         	return a==1?true:false;
       }else{
    	   return true;
       }
}
//手术名称信息的验证
function nssmcName(name){
	var mc = $('[id^=nssmc]');
 	var b = 0
	mc.each(function(index,obj){
		if($(obj).combogrid('getText') == name&&$(obj).combogrid('getText')!=""&&$(obj).combogrid('getText')!=null){
			b++;
		}
	});
 	return b>1?true:false;
}
//诊断信息的验证
function validName(name){
	var mc = $('[id^=shoushuzd]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getText') == name&&$(obj).combogrid('getText')!=""&&$(obj).combogrid('getText')!=null){
			b++;
		}
	});
 	return b>1?true:false;
}	

/**
 * @Description：提取扩大复选框范围的方法
 * @Author：houzq
 * @CreateDate：2017-4-5
 */
 var i = '';
function kdfw(id){
	$('#'+id).prop('checked')==true?$('#'+id).prop('checked',false):$('#'+id).prop('checked',true);
	i = id;
	setTimeout('triggerclick()',500);
}
function triggerclick(){
	var a = $('#'+i).attr('onclick');
	eval(a);
}

/**
 * @Description:提示框自动消失
 * @Author: zhangjin
 * @CreateDate: 2017年2月10日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function alert_autoClose(title,msg,icon){  
	 var interval;  
	 var time=3500;  
	 var x=1;    //设置时间2s
	$.messager.alert(title,msg,icon,function(){});  
	 interval=setInterval(fun,time);  
	        function fun(){  
	      --x;  
	      if(x==0){  
	          clearInterval(interval);  
	  $(".messager-body").window('close');    
	       }  
	}; 
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
<style  type="text/css">
  .window .panel-header .panel-tool .panel-tool-close{
  	  	background-color: red;
  	}
 </style>
</body>