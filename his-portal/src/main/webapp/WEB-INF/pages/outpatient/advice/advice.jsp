<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body style="margin:0px;padding:0px">
		<input id="focusInpId" type="hidden" value="0"/>
		<div id="advEl" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false,border:false" style="height:77px;">
				<div id="adProjectDivId" style="padding:5px 5px 5px 5px;">
					<table>
						<tr>
							<td width="80px" align="right">项目类别：</td>
							<td width="173px"><input id="adProjectTdId" style="width:165px;"></td>
							<td width="70px" align="right">名称：</td>
							<td width="160px"><input id="adProjectNameTdId" style="width:130px;"/><a href="javascript:delSelectedData();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td width="70px" align="right">数量：</td>
							<td width="160px"><input id="adProjectNumTdId" class="easyui-numberspinner" data-options="min:1,max:100,disabled:true" style="width:130px;"/></td>
							<td width="70px" align="right">单位：</td>
							<td align="center"><input id="adProjectUnitTdId" class="easyui-combobox" data-options="disabled:true,editable:false" style="width:130px;"/></td>
						</tr>
					</table>
				</div>
				<div id="adWestMediDivId" style="padding:0px 5px 5px 5px;display:none;">
					<table>
						<tr>
							<td width="80px" align="right" style="word-break: keep-all;">每次用量：</td>
							<td width="50px"><input id="adWestMediDosMaxTdId" class="easyui-numberbox" data-options="min:0.01,max:999,precision:2" style="width:50px;"/></td>
							<td width="32px" id="adWestMediMinUnitTdId"></td>
							<td width="6px">=</td>
							<td width="50px"><input id="adWestMediDosMinTdId" class="easyui-numberbox" data-options="precision:2" readonly="readonly" style="width:50px;"/></td>
							<td width="32px" id="adWestMediDosDosaTdId"></td>
							<td width="70px" align="right" style="word-break: keep-all;">频次：</td>
							<td width="160px" nowrap><input id="adWestMediFreTdId" class="easyui-combobox" data-options="" style="width:130px;"/><a href="javascript:delAdWestMediFreTdId();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td width="70px" align="right" style="word-break: keep-all;">用法：</td>
							<td width="160px" nowrap><input id="adWestMediUsaTdId" class="easyui-combobox" data-options="" style="width:130px;"/><a href="javascript:delAdWestMediUsaTdId();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td width="70px" align="right" style="word-break: keep-all;">备注：</td>
							<td width="160px" nowrap><input id="adWestMediRemTdId" class="easyui-combobox" data-options="" style="width:130px;"/><a href="javascript:delAdWestMediRemTdId();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td width="50px" align="right" style="word-break: keep-all;">皮试：</td>
							<td width="90px"><input id="adWestMediSkiTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>
							<td width="50px" align="right" style="word-break: keep-all;">加急：</td>
							<td width="10px"><input id="adWestMediUrgTdId" type="checkbox" onclick="onClickIsUrgent('adWestMediUrgTdId')"/></td>
						</tr>
					</table>
				</div>
				<div id="adChinMediDivId" style="padding:0px 5px 5px 5px;display:none;">
					<table>
						<tr>
							<td width="80px" align="right">付数：</td>
							<td width="173px"><input id="adChinMediNumTdId" class="easyui-numberbox" data-options="min:1,max:999" style="width:165px;"/></td>
							<td width="70px" align="right">用法：</td>
							<td width="160px"><input id="adChinMediUsaTdId" class="easyui-combobox" data-options="" style="width:130px;"/><a href="javascript:delAdChinMediUsaTdId();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td width="70px" align="right">备注：</td>
							<td width="160px"><input id="adChinMediRemTdId" class="easyui-combobox" data-options="" style="width:130px;"/><a href="javascript:delAdChinMediRemTdId();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
						</tr>
					</table>
				</div>
				<div id="adNotDrugDivId" style="padding:0px 5px 5px 5px;display:none;">
					<table>
						<tr>
							<td width="80px" align="right">执行科室：</td>
							<td width="173px"><input id="adNotDrugExeTdId" data-options="" class="easyui-combobox"  style="width:165px;"/>
							<td width="70px" align="right" id="adNotDrugInsJudgeTitleTdId">部位：</td>
							<td width="160px" id="adNotDrugInsJudgeTextTdId"><input id="adNotDrugInsTdId" class="easyui-combobox" data-options="" style="width:130px;"/><a href="javascript:delAdNotDrugInsTdId();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td width="70px" align="right" id="adNotDrugSamJudgeTitleTdId">样本类型：</td>
							<td width="160px" id="adNotDrugSamJudgeTextTdId"><input id="adNotDrugSamTdId" class="easyui-combobox" data-options="" style="width:130px;"/><a href="javascript:delAdNotDrugSamTdId();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td width="70px" align="right">备注：</td>
							<td width="160px"><input id="adNotDrugRemTdId" class="easyui-combobox" data-options="" style="width:130px;"/><a href="javascript:delAdNotDrugRemTdId();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td width="50px" align="right">加急：</td>
							<td width="10px"><input id="adNotDrugUrgTdId" type="checkbox" onclick="onClickIsUrgent('adNotDrugUrgTdId')" style="width:10px;"/></td>
						</tr>
					</table>
				</div>
			</div>
			<div data-options="region:'west',title:'组套信息',split:false,collapsed:true" style="width:15%;">
				<div style="padding-top:5px;padding-left:5px;padding-bottom:5px;">
					<input id="adStackTreeSearch" data-options="buttonText:'查询',buttonIcon:'icon-search',prompt:'组套查询'" style="width:180px;"/>
<!-- 					<a href="javascript:void(0)" onclick="searchStackTreeNodes()" class="easyui-linkbutton" iconCls="icon-search" style="height:24px;">查询</a> -->
				</div>
				<ul id="adStackTree"></ul>
			</div>
			<div data-options="region:'center',border:false">
				<div id="el" class="easyui-layout" data-options="fit:true,border:false">   
					<div data-options="region:'north',split:false" style="height:30px;">
						<div style="padding: 5px 0px 5px 5px;" class="adviceFontSize">
							<span style="font-size:12px"></span>
							<span style="font-size:12px">新开立：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#00FF00">&nbsp;&nbsp;</span>
							&nbsp;<span style="font-size:12px">已审核：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#4A4AFF">&nbsp;&nbsp;</span>
							&nbsp;<span style="font-size:12px">已执行：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#EEEE00">&nbsp;&nbsp;</span>
							&nbsp;<span style="font-size:12px">已作废：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#FF0000">&nbsp;&nbsp;</span>
							&nbsp;<span style="font-size:12px">省限制级：</span><span style="font-size:12px">X</span>
							&nbsp;<span style="font-size:12px">市限制级：</span><span style="font-size:12px">S</span>
							&nbsp;<span style="font-size:12px">知情同意书：</span><span style="font-size:12px">√</span>
							&nbsp;<span style="font-size:12px">需审核：</span><span style="display:inline-block;width:12px;height:12px;background-image:url(${pageContext.request.contextPath}/themes/system/images/button/shen1.png)"></span>
							&nbsp;<span style="font-size:12px">复制/粘贴：CTRL+C/CTRL+V</span>
						</div>
					</div>   
					<div data-options="region:'center',border:false">
						<table style="padding: 5px 5px 5px 5px;" id="adDgList" class="easyui-datagrid" data-options="striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fit:true">   
							 <thead frozen="true">  
								<tr>  
									<th data-options="field:'id',width:100,hidden:true">id*</th>
									<th data-options="field:'adviceNo',width:100,hidden:true">处方号*</th>
									<th data-options="field:'colour',width:24,align:'center',styler:functionColour,formatter:functionRowNum" ></th>
									<th data-options="field:'ck',checkbox:true" ></th>
									<th data-options="field:'limit',width:18" ></th>
									<th data-options="field:'adviceNameView',width:260">医嘱名称</th>  
								</tr>  
							</thead>
							<thead>   
								<tr>   
									<th data-options="field:'type',width:100,hidden:true">类型*</th>   
									<th data-options="field:'ty',width:100,hidden:true">是否为药品*</th>   
									<th data-options="field:'adviceType',width:100,hidden:true">医嘱类型</th>   
									<th data-options="field:'adviceId',width:100,hidden:true">医嘱名称Id*</th>   
									<th data-options="field:'adviceName',width:100,hidden:true">医嘱名称Hid*</th>   
									<th data-options="field:'adPrice',width:100,hidden:true">价格*</th>   
									<th data-options="field:'adPackUnitHid',width:100,hidden:true">包装单位*</th>   
									<th data-options="field:'adMinUnitHid',width:100,hidden:true">单位*</th>   
									<th data-options="field:'adDosaUnitHid',width:100,hidden:true">剂量*</th>   
									<th data-options="field:'adDosaUnitHidJudge',width:100,hidden:true">剂量*</th>   
									<th data-options="field:'adDrugBasiHid',width:100,hidden:true">基本剂量*</th>   
									<th data-options="field:'specs',width:100,hidden:true">规格*</th>   
									<th data-options="field:'sysType',width:100,hidden:true">系统类别*</th>   
									<th data-options="field:'drugType',width:100,hidden:true">药品类别*</th>   
									<th data-options="field:'minimumcost',width:100,hidden:true">最小费用代码*</th>   
									<th data-options="field:'packagingnum',width:100,hidden:true">包装数量*</th>   
									<th data-options="field:'nature',width:100,hidden:true">药品性质*</th>   
									<th data-options="field:'ismanufacture',width:100,hidden:true">自制药标志*</th>   
									<th data-options="field:'dosageform',width:100,hidden:true">剂型*</th>   
									<th data-options="field:'isInformedconsent',width:100,hidden:true">是否知情同意书*</th>   
									<th data-options="field:'auditing',width:100,hidden:true">是否需要审核*</th>   
									<th data-options="field:'group',width:30,formatter:functionGroup">组</th>   
									<th data-options="field:'totalNum',width:50">总量</th>   
									<th data-options="field:'totalUnitHid',width:100,hidden:true">总单位Id*1</th>   
									<th data-options="field:'totalUnitHidJudge',width:100,hidden:true">总单位Id*</th>   
									<th data-options="field:'totalUnit',width:50">总单位</th>   
									<th data-options="field:'dosageHid',width:100,hidden:true">每次用量H*</th>   
									<th data-options="field:'dosageMin',width:100,hidden:true">每次剂量M*</th>   
									<th data-options="field:'dosage',width:100">每次用量</th>   
									<th data-options="field:'unit',width:50">单位</th>   
									<th data-options="field:'setNum',width:50">付数</th>   
									<th data-options="field:'frequencyHid',width:100,hidden:true">频次Id*</th>   
									<th data-options="field:'frequency',width:100" >频次</th>   
									<th data-options="field:'usageNameHid',width:100,hidden:true">用法Id*</th>   
									<th data-options="field:'usageName',width:100">用法名称</th>   
									<th data-options="field:'injectionNum',width:80">院注次数</th>   
									<th data-options="field:'openDoctor',width:100">开立医生</th>   
									<th data-options="field:'executiveDeptHid',width:100,hidden:true">执行科室Id*</th>   
									<th data-options="field:'executiveDept',width:100,">执行科室</th>   
									<th data-options="field:'isUrgentHid',width:100,hidden:true">加急Id*</th>   
									<th data-options="field:'isUrgent',width:30">加急</th>   
									<th data-options="field:'inspectPartId',width:100,hidden:true">检查部位Id*</th>   
									<th data-options="field:'inspectPart',width:100">检查部位</th>   
									<th data-options="field:'sampleTeptHid',width:100,hidden:true">样本类型Id*</th>   
									<th data-options="field:'sampleTept',width:100">样本类型</th>   
									<th data-options="field:'minusDeptHid',width:100,hidden:true">扣库科室Id*</th>   
									<th data-options="field:'minusDept',width:100">扣库科室</th>   
									<th data-options="field:'remark',width:100">备注</th>   
									<th data-options="field:'inputPeop',width:100">录入人</th>   
									<th data-options="field:'openDept',width:100">开立科室</th>   
									<th data-options="field:'startTime',width:100,hidden:true">开立时间</th>   
									<th data-options="field:'endTime',width:100,hidden:true">停止时间</th>   
									<th data-options="field:'stopPeop',width:100">停止人</th>   
									<th data-options="field:'isSkinHid',width:100,hidden:true">是否需要皮试Id*</th>   
									<th data-options="field:'isSkin',width:120">皮试</th>
									<th data-options="field:'splitattr',width:120,hidden:true">拆分属性</th>
									<th data-options="field:'property',width:120,hidden:true">拆分属性维护</th>
								</tr>   
							</thead>   
						</table>
					</div> 
				</div>
			</div>
		</div>
		<div id="adviceListMenu" class="easyui-menu" style="width:100px; display: none;">
			<div id="adAddGroupBtnId" data-options="iconCls:'icon-application_link'" onclick="adAddGroup()">组合</div>
			<div id="adviceCopyBtnId" data-options="iconCls:'icon-application_double'" onclick="copyHis('adDgList')">复制医嘱</div>
			<div id="adviceEditPriceBtnId" data-options="iconCls:'icon-edit'" onclick="adviceEditPrice()">修改价格</div>
			<div id="adviceEditInjNumBtnId" data-options="iconCls:'icon-edit'" onclick="adviceEditInjNum()">修改院注次数</div>
			<div id="adviceSaveStackBtnId" data-options="iconCls:'icon-save'" onclick="adSaveGroup()">保存组套</div>
			<div id="adviceUpMoveBtnId" data-options="iconCls:'icon-up'" onclick="adviceUpMove()">上移</div>
			<div id="adviceDownMoveBtnId" data-options="iconCls:'icon-down'" onclick="adviceDownMove()">下移</div>
			<c:if test="${auditing }">
				<div id="adviceAuditingBtnId" data-options="iconCls:'icon-book_edit'" onclick="adviceAuditing()">审核</div>
			</c:if>
		</div>
		<div id="adviceListWin" align="center" class="easyui-window" data-options="modal:true,closed:true,closable:true,collapsible:false,minimizable:false,maximizable:false">
			<table>
				<tr style="height:5px"></tr>
				<tr>
					<td align="center">
						<div class="messager-icon messager-question"></div>
						请输入要修改的价格：
					</td>
				</tr>
				<tr>
					<td align="center">
						<input id="adviceListWinIputId" class="easyui-numberbox" data-options="precision:2,required:true" style="width:200px;"/>
					</td>
				</tr>
				<tr style="height:5px"></tr>
				<tr>
					<td align="center">
						<a href="javascript:adviceEditPriceSave();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
						<a href="javascript:adviceEditPriceCancel();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
					</td>
				</tr>
			</table>
		</div>
		<div id="adviceListInjNumWin" align="center" class="easyui-window" data-options="modal:true,closed:true,closable:true,collapsible:false,minimizable:false,maximizable:false">
			<table>
				<tr style="height:5px"></tr>
				<tr>
					<td align="right" style="width:70px">
						院注：
					</td>
					<td align="left" style="width:70px" id="adviceListInjNumWinYzId">
						
					</td>
					<td align="right" style="width:70px">
						每次：
					</td>
					<td align="left" style="width:70px" id="adviceListInjNumWinmcId">
						
					</td>
				</tr>
				<tr style="height:5px"></tr>
				<tr>	
					<td align="right">
						院注天数：
					</td>
					<td align="left" id="adviceListInjdayWinIputId">
						
					</td>
					<td align="right">
						院注次数：
					</td>
					<td align="left">
						<input id="adviceListInjNumWinIputId" class="easyui-numberbox" data-options="precision:0,max:99,required: true" style="width:50px;"/>
					</td>
				</tr>
				<tr style="height:5px"></tr>
				<tr>	
					<td align="center" colspan="4">
						<a href="javascript:adviceEditInjNumSave();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
					</td>
				</tr>
			</table>
		</div> 
		<div id="adviceAuditingWin" class="easyui-window" align="center" data-options="modal:true,closed:true,closable:true,collapsible:false,minimizable:false,maximizable:false">
			<div style="padding:5px 5px 5px 0px;">
				<table>
				<tr>
					<td style="width: 100px" align="right">
						审核状态：
						<input type="hidden" id="adviceAuditingWinId"/>
					</td>
					<td align="center">
						通过<input type="radio" id="adviceAuditingWinOk" checked="checked" name="start" value="1">&nbsp;
						未通过<input type="radio" id="adviceAuditingWinNo" name="start" value="0">
					</td>
				</tr>
				<tr>
					<td style="width: 100px" align="right">
						审核意见：
					</td>
					<td id="adviceAuditingWinIputId" align="center">
						<textarea class="easyui-validatebox" id="adviceAuditingRemarks" rows="4" cols="26" data-options="prompt:'审核意见'"></textarea>
					</td>
				</tr>
			</table>
			</div>
			<div>
				<a href="javascript:adviceAuditingSave();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
				<a href="javascript:adviceAuditingCancel();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			</div>
		</div>  
		<div id="stackSaveModleDivId" class="easyui-window" data-options="modal:true,closed:true,closable:true,collapsible:false,minimizable:false,maximizable:false"><jsp:include page="stackModel.jsp"></jsp:include></div>
		<script type="text/javascript">
		$(function(){
			 $("#adStackTreeSearch").textbox({onClickButton:function(){
				 searchStackTreeNodes();
			    }});
		});
		$('#adStackTreeSearch').textbox({
			prompt:'组套查询'
		});
		//医嘱信息列表事件
		$('#adDgList').datagrid({
			onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
				e.preventDefault(); //阻止浏览器捕获右键事件
				$(this).datagrid("selectRow", rowIndex); //根据索引选中该行
				var leng = $(this).datagrid("getRows").length;//当前list的长度
				var options = $('#adEndAdviceBtn').linkbutton('options');
				if(options.disabled){
					$('#adAddGroupBtnId').hide();
					$('#adviceCopyBtnId').hide();
					$('#adviceEditPriceBtnId').hide();
					$('#adviceSaveStackBtnId').hide();
					$('#adviceUpMoveBtnId').hide();
					$('#adviceDownMoveBtnId').hide();
					$('#adviceAuditingBtnId').hide();
					$('#adviceEditInjNumBtnId').hide();
					if(rowData.auditing==1){
						$('#adviceAuditingBtnId').show();
						$('#adviceListMenu').menu('show', {//显示右键菜单
							left: e.pageX,//在鼠标点击处显示菜单
							top: e.pageY
						});
					}
				}else{
					$('#adAddGroupBtnId').show();
					$('#adviceCopyBtnId').show();
					$('#adviceEditPriceBtnId').show();
					$('#adviceSaveStackBtnId').show();
					$('#adviceUpMoveBtnId').show();
					$('#adviceDownMoveBtnId').show();
					$('#adviceAuditingBtnId').hide();
					if(rowData.usageName!=null&&rowData.usageName!=''&&rowData.usageName.indexOf("注射")>0){
						 $('#adviceEditInjNumBtnId').show();
					}else{
						 $('#adviceEditInjNumBtnId').hide();
					}
					if(rowIndex==0){//如果为第一行不显示上移
						$('#adviceUpMoveBtnId').hide();
					}
					if(rowIndex==(leng-1)){//如果为最后一行行不显示下移
						$('#adviceDownMoveBtnId').hide();
					}
					$('#adviceListMenu').menu('show', {//显示右键菜单
						left: e.pageX,//在鼠标点击处显示菜单
						top: e.pageY
					});
				}
			},
			onCheck:function(index, row){//业务变更，组合号同时选中  2017-03-02 16:03 aizhonghua
				if(row.group!=null&&row.group!=''){
					var rows = $(this).datagrid('getRows');
					var ckArr = $(this).datagrid('getPanel').find('input');
					for(var i=0;i<rows.length;i++){
						if(row.group==rows[i].group){
							if(!$(ckArr[i+1]).is(":checked")){
								$(this).datagrid('checkRow',i);
							}
						}
					}
				}
			},
			onUncheck:function(index, row){//业务变更，组合号同时选中  2017-03-02 16:03 aizhonghua
				if(row.group!=null&&row.group!=''){
					var rows = $(this).datagrid('getRows');
					var ckArr = $(this).datagrid('getPanel').find('input');
					for(var i=0;i<rows.length;i++){
						if(row.group==rows[i].group){
							if($(ckArr[i+1]).is(":checked")){
								$(this).datagrid('uncheckRow',i);
							}
						}
					}
				}
			},
			onSelect:function(rowIndex,rowData){
				 var options = $('#adEndAdviceBtn').linkbutton('options');
				 if(options.disabled){
					 
				 }else{
					if(rowData.colour==1){
						disFunction();
						msgShow('提示','该医嘱信息已收费，无法修改！',3000);
						return;
					}
					if(rowData.colour==3){
						disFunction();
						msgShow('提示','该医嘱信息已作废，无法修改！',3000);
						return;
					}
					if(rowData.colour==5){
						disFunction();
						msgShow('提示','该医嘱信息已审核，无法修改！',3000);
						return;
					}
					enaFunction();
					var type = rowData.type;
					$('#adProjectTdId').combobox('select',type);
					if(type==sysTypeMap['中草药']){
						initCombobox(true,colorInfoUnitsMap,null,null);
					}else{
						initCombobox(false,colorInfoUnitsMap,rowData.adPackUnitHid,rowData.adMinUnitHid);
					}
					
					if(rowData.totalUnitHidJudge!=null&&rowData.totalUnitHidJudge!=''){
						$('#adProjectUnitTdId').combobox('select',rowData.totalUnitHidJudge);//单位
					}else{
						$('#adProjectUnitTdId').combobox('clear');
					}
					$('#adProjectNumTdId').numberspinner({precision:0});
					if(type==sysTypeMap['中成药']||type==sysTypeMap['西药']){//中药/西药
						$('#adProjectNumTdId').numberspinner('setValue',rowData.totalNum);//数量
						$('#adWestMediDosMaxTdId').numberbox('setValue',rowData.dosageHid);
						$('#adWestMediMinUnitTdId').text(colorInfoUnitsMap.get(rowData.adMinUnitHid));
						$('#adWestMediDosMinTdId').numberbox('setValue',rowData.dosageHid*rowData.adDrugBasiHid);
						$('#adWestMediDosDosaTdId').text(colorInfoUnitsMap.get(rowData.adDosaUnitHidJudge));
						if(rowData.frequencyHid!=null&&rowData.frequencyHid!=''){
							$('#adWestMediFreTdId').combobox('select',rowData.frequencyHid);
						}else{
							$('#adWestMediFreTdId').combobox('clear');
						}
						if(rowData.usageNameHid!=null&&rowData.usageNameHid!=''){
							$('#adWestMediUsaTdId').combobox('select',rowData.usageNameHid);
						}else{
							$('#adWestMediUsaTdId').combobox('clear');
						}
						$('#adWestMediRemTdId').combobox('clear');
						$('#adWestMediRemTdId').combobox('setText',rowData.remark);
						if(rowData.isUrgentHid==1){
							$('#adWestMediUrgTdId').prop("checked", true); 
						}else if(rowData.isUrgentHid==0){
							$('#adWestMediUrgTdId').prop("checked", false); 
						}
						if(rowData.isSkinHid!=null&&rowData.isSkinHid!=''){
							$('#adWestMediSkiTdId').combobox('select',rowData.isSkinHid);
						}else{
							$('#adWestMediSkiTdId').combobox('clear');
						}
					}else if(type==sysTypeMap['中草药']){
						$('#adProjectNumTdId').numberspinner({precision:3});
						$('#adProjectNumTdId').numberspinner('setValue',(rowData.totalNum/rowData.setNum).toFixed(3));//数量
						$('#adChinMediNumTdId').numberbox('setValue',rowData.setNum);
						if(rowData.usageNameHid!=null&&rowData.usageNameHid!=''){
							$('#adChinMediUsaTdId').combobox('select',rowData.usageNameHid);
						}else{
							$('#adChinMediUsaTdId').combobox('clear');
						}
						$('#adChinMediRemTdId').combobox('clear');
						$('#adChinMediRemTdId').combobox('setText',rowData.remark);
					}else{
						$('#adProjectNumTdId').numberspinner('setValue',rowData.totalNum);//数量
						if(rowData.executiveDeptHid!=null&&rowData.executiveDeptHid!=''){
							$('#adNotDrugExeTdId').combobox('select',rowData.executiveDeptHid);
						}else{
							$('#adNotDrugExeTdId').combobox('clear');
						}
						if(rowData.inspectPartId!=null&&rowData.inspectPartId!=''){
							$('#adNotDrugInsTdId').combobox('select',rowData.inspectPartId);
						}else{
							$('#adNotDrugInsTdId').combobox('clear');
						}
						if(rowData.sampleTeptHid!=null&&rowData.sampleTeptHid!=''){
							$('#adNotDrugSamTdId').combobox('select',rowData.sampleTeptHid);
						}else{
							$('#adNotDrugSamTdId').combobox('clear');
						}
						$('#adNotDrugRemTdId').combobox('clear');
						$('#adNotDrugRemTdId').combobox('setText',rowData.remark);
						if(rowData.isUrgentHid==1){
							$('#adNotDrugUrgTdId').prop("checked", true); 
						}else{
							$('#adNotDrugUrgTdId').prop("checked", false); 
						}
					}
				 }
			}
		});
		
		bindEnterEvent('adStackTreeSearch',searchStackTreeNodes,'easyui');
		
		/**  
		 *  
		 * 项目类别
		 * @Author：aizhonghua
		 * @CreateDate：2015-6-26 上午11:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2015-6-26 上午11:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		$('#adProjectTdId').combobox({
			editable:false,
			disabled:true,
			valueField:'code',	
			textField:'name',
			onShowPanel:function(none){
				var index = getIndexForAdDgList();
				if(index>=0){
					$('#adDgList').datagrid('unselectRow',index);
				}
			},
			onSelect:function(record){
				if(record!=null&&record!=''){
					$('#adProjectNameTdId').combogrid('grid').datagrid('load',{name:null,type:record.code,typeChi:record.name});
					if(record.code==sysTypeMap['中成药']||record.code==sysTypeMap['西药']){//中药/西药
						$('#adNotDrugDivId').hide();
						$('#adChinMediDivId').hide();
						$('#adWestMediDivId').show();
					}else if(record.code==sysTypeMap['中草药']){
						$('#adNotDrugDivId').hide();
						$('#adWestMediDivId').hide();
						$('#adChinMediDivId').show();
					}else{
						$('#adChinMediDivId').hide();
						$('#adWestMediDivId').hide();
						$('#adNotDrugDivId').show();
					}
					$('#adNotDrugInsJudgeTitleTdId').show();
					$('#adNotDrugInsJudgeTextTdId').show();
					$('#adNotDrugSamJudgeTitleTdId').show();
					$('#adNotDrugSamJudgeTextTdId').show();
					if(record.code==sysTypeMap['检查']){//检查
						$('#adNotDrugSamJudgeTitleTdId').hide();
						$('#adNotDrugSamJudgeTextTdId').hide();
					}else if(record.code==sysTypeMap['检验']){//检验
						$('#adNotDrugInsJudgeTitleTdId').hide();
						$('#adNotDrugInsJudgeTextTdId').hide();
					}
					clearProjectAux();
					clearProjectTextbox();
					enableProject();
					//添加功能事件
					functionInfo();
				}
			}
		}); 
		
		$('#adProjectNameTdId').combogrid({
			prompt:'请选择项目',
			disabled:true,
			panelWidth:650,
			panelHeight:325,
			rownumbers:true,
			pagination:true,
			mode:'remote',
			delay:800,
			textField: 'name',
			url:"<%=basePath%>outpatient/advice/queryViewInfo.action",
			queryParams:{name:null,type:null,typeChi:null},
			pageSize:20,
			pageList:[20,30,50,80,100],
			onBeforeLoad:function(param){
				var type = $('#adProjectTdId').combobox('getValue');
				if(type != null && type !=''){
					var typeChi = $('#adProjectTdId').combobox('getText');
					param.type = type;
					param.typeChi = typeChi;
				}else{
					return false;
				}
			},
			rowStyler: function(index,row){
				if (row.ty==1&&row.surSum<=0){//库存不足
					return 'background-color:#9D9DFF;color:#fff;';
				}
				if (row.stop_flg==1){//停用
					return 'background-color:#FF9191;color:#fff;';
				}
			},
			onClickRow:function(rowIndex, rowData){
				initAdviceInfo(rowData);
			},
			columns:[[	
				{field:'id',title:'id',width:60,hidden:true},	
				{field:'name',title:'通用名',width:200,halign:'center'},	
				{field:'ty',title:'类别',width:50,align:'right',halign:'center',formatter:funcType},	
				{field:'spec',title:'规格',width:80,align:'right',halign:'center'},
				{field:'price',title:'价格',width:70,align:'right',halign:'center'},
				{field:'insured',title:'医保标记',width:70,align:'right',halign:'center'},
				{field:'surSum',title:'库存可用数量',width:100,align:'right',halign:'center'},
				{field:'inputcode',title:'自定义码',width:100,align:'right',halign:'center'},
// 				{field:'commonName',title:'名称',width:200,align:'right',halign:'center'},
				{field:'inspectionsite',title:'检查检体',width:70,align:'right',halign:'center'},
				{field:'diseaseclassification',title:'疾病分类',width:80,formatter:forDiseaset,align:'right',halign:'center'},
				{field:'specialtyName',title:'专科名称',width:80,align:'right',halign:'center'},
				{field:'medicalhistory',title:'病史及检查',width:100,align:'right',halign:'center'},
				{field:'requirements',title:'检查要求',width:80,align:'right',halign:'center'},
				{field:'notes',title:'注意事项',width:80,align:'right',halign:'center'},
			]],
			keyHandler:{
				left: function(e){
					e.preventDefault();
					var pager = $(this).combogrid('grid').datagrid("getPager");
					var options = pager.pagination('options');
					var page = options.pageNumber;
					if(page>1){
						pager.pagination('select',options.pageNumber-1);
					}
				},
				right: function(e){
					e.preventDefault();
					var pager = $(this).combogrid('grid').datagrid("getPager")
					var options = pager.pagination('options');
					var total = options.total; 
					var max = Math.ceil(total/options.pageSize);
					var page = options.pageNumber;
					if(page<max){
						pager.pagination('select',options.pageNumber+1);
					}
				},
				up: function(e){
					e.preventDefault();
					var pClosed = $(this).combogrid('panel').panel('options').closed;
					if (pClosed) {
						$(this).combogrid('showPanel');
					}
					var grid = $(this).combogrid('grid');
					var rowSelected = grid.datagrid('getSelected');
					if (rowSelected != null) {
						var rowIndex = grid.datagrid('getRowIndex', rowSelected);
						if (rowIndex > 0) {
							rowIndex = rowIndex - 1;
							grid.datagrid('selectRow', rowIndex);
						}
					} else if (grid.datagrid('getRows').length > 0) {
						grid.datagrid('selectRow', 0);
					}
				},
				down: function(e){
					e.preventDefault();
					var pClosed = $(this).combogrid('panel').panel('options').closed;
					if (pClosed) {
						$(this).combogrid('showPanel');
					}
					var grid = $(this).combogrid('grid');
					var rowSelected = grid.datagrid('getSelected');
					if (rowSelected != null) {
						var totalRow = grid.datagrid('getRows').length;
						var rowIndex = grid.datagrid('getRowIndex', rowSelected);
						if (rowIndex < totalRow - 1) {
							rowIndex = rowIndex + 1;
							grid.datagrid('selectRow', rowIndex);
						}
					} else if (grid.datagrid('getRows').length > 0) {
						grid.datagrid('selectRow', 0);
					}
				},
				enter: function(e){
					e.preventDefault();
					var pClosed = $(this).combogrid('panel').panel('options').closed;
					if (!pClosed) {
						$(this).combogrid('hidePanel');
					}
					var rowData = $(this).combogrid('grid').datagrid('getSelected');
					if (rowData == null || rowData == undefined) {
						return;
					}else {
						initAdviceInfo(rowData);
					}
				},
				query: function(q,e){
					$(this).combogrid('grid').datagrid('load',{name:q,type:$(this).combobox('getValue'),typeChi:$(this).combobox('getText')});
					$(this).combogrid('setValue',q);
				}
			}
		});
		$('#adProjectNameTdId').combogrid('textbox').bind('focus',function(){
			$('#focusInpId').val(1);
			$('#adProjectNameTdId').combogrid('showPanel');
		});
		$('#adProjectNameTdId').combogrid('textbox').bind('blur',function(){
			$('#focusInpId').val(0);
		});
		
		
		/**  
		 *  
		 * 获得渲染数据
		 * @Author：aizhonghua
		 * @CreateDate：2015-6-26 上午11:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2015-6-26 上午11:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function functionInfo(){
			initCombobox(true,colorInfoUnitsMap,null,null);//单位
			$('#adWestMediFreTdId').combobox({//西药-中成药-频次
				data:colorInfoFrequencyList,
				disabled:false,
				editable:false,
				valueField:'encode',	
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
						var index = getIndexForAdDgList();
						var row = $('#adDgList').datagrid('getSelected');
						if(row!=null&&row.group!=null&&row.group!=''){
							var rows = $('#adDgList').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].group==row.group){
									var indexRow = $('#adDgList').datagrid('getRowIndex',rows[i]);
									$('#adDgList').datagrid('updateRow',{
										index: indexRow,
										row: {
											frequencyHid:record.encode,
											frequency:record.name
										}
									});
								}
							}
						}else{
							if(index>=0){
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										frequencyHid:record.encode,
										frequency:record.name
									}
								});
							}
						}
					}
				}
			});
			//给用法方法绑定弹窗事件
			bindEnterEvent('adWestMediFreTdId',popWinToBusinessFrequency,'easyui');
			$('#adWestMediUsaTdId').combobox({//西药-中成药-用法
				data:colorInfoUsageList,
				disabled:false,
				editable:false,
				valueField:'encode',	
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
						var index = getIndexForAdDgList();
						var row = $('#adDgList').datagrid('getSelected');
						if(record.name.indexOf("注射")>0&&row.injectionNum==null){
							adviceListInjNum(row);
						}else{
							if(record.name.indexOf("注射")>0&&row.injectionNum!=null){
								
							}else{
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										injectionNum:null,
									}
								});
							}
						}
						if(row!=null&&row.group!=null&&row.group!=''){
							var rows = $('#adDgList').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].group==row.group){
									var indexRow = $('#adDgList').datagrid('getRowIndex',rows[i]);
									$('#adDgList').datagrid('updateRow',{
										index: indexRow,
										row: {
											usageNameHid:record.encode,
											usageName:record.name
										}
									});
								}
							}
						}else{
							if(index>=0){
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										usageNameHid:record.encode,
										usageName:record.name
									}
								});
							}
						}
					}
				}
			});
			//给西药-中成药-用法方法绑定弹窗事件
			bindEnterEvent('adWestMediUsaTdId',popWinToUseage,'easyui');
			$('#adWestMediRemTdId').combobox({//西药-中成药-备注
				data:colorInfoRemarksList,
				disabled:false,
				editable:true,
				valueField:'encode',	
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
						var index = getIndexForAdDgList();
						if(index>=0){
							$('#adDgList').datagrid('updateRow',{
								index: index,
								row: {
									remark:record.name
								}
							});
						}
					}
				}
			});
			//给西药-中成药-备注方法绑定弹窗事件
			bindEnterEvent('adWestMediRemTdId',popWinToCodeOpendocadvmark,'easyui');
			$('#adWestMediRemTdId').combobox('textbox').bind('keyup',function(event){//西药-中成药-备注
				var retval = $('#adWestMediRemTdId').combobox('getText');
				var index = getIndexForAdDgList();
				if(index>=0){
					$('#adDgList').datagrid('updateRow',{
						index: index,
						row: {
							remark:retval
						}
					});
				}
			});
			$('#adWestMediSkiTdId').combobox({//西药-中成药-皮试
				data:[{id:1,name:'不需要皮试'},{id:2,name:'需要皮试，未做'},{id:3,name:'皮试阳'},{id:4,name:'皮试阴'}],//1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴
				disabled:false,
				editable:false,
				valueField:'id',	
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
						var index = getIndexForAdDgList();
						if(index>=0){
							$('#adDgList').datagrid('updateRow',{
								index: index,
								row: {
									isSkinHid:record.id,
									isSkin:record.name
								}
							});
						}
					}
				}
			});
			$('#adChinMediNumTdId').numberbox('textbox').bind('keyup',function(event){//草药-付数
				var index = getIndexForAdDgList();
				if(index>=0){
					var retVal = $('#adChinMediNumTdId').numberbox('getText');
					if(retVal>999){
						retVal=999;
					}
					if(retVal==null||retVal==''){
						retVal=0;
					}
					var row = $('#adDgList').datagrid('getSelected');
					if(row!=null&&row.group!=null&&row.group!=''){
						var rows = $('#adDgList').datagrid('getRows');
						for(var i=0;i<rows.length;i++){
							if(rows[i].group==row.group){
								var indexRow = $('#adDgList').datagrid('getRowIndex',rows[i]);
								$('#adDgList').datagrid('updateRow',{
									index: indexRow,
									row: {
										setNum:retVal,
										totalNum:(retVal*rows[i].dosageHid).toFixed(3)
									}
								});
							}
						}
					}else{
						if(index>=0){
							$('#adDgList').datagrid('updateRow',{
								index: index,
								row: {
									setNum:retVal,
									totalNum:(retVal*row.dosageHid).toFixed(3)
								}
							});
						}
					}
					$('#adChinMediNumTdId').numberbox('setText',retVal);
				}
			});
			$('#adChinMediUsaTdId').combobox({//中草药-用法
				data:colorInfoUsageList,
				disabled:false,
				editable:false,
				valueField:'encode',	
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
						var index = getIndexForAdDgList();
						var row = $('#adDgList').datagrid('getSelected');
						if(row!=null&&row.group!=null&&row.group!=''){
							var rows = $('#adDgList').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].group==row.group){
									var indexRow = $('#adDgList').datagrid('getRowIndex',rows[i]);
									$('#adDgList').datagrid('updateRow',{
										index: indexRow,
										row: {
											usageNameHid:record.encode,
											usageName:record.name
										}
									});
								}
							}
						}else{
							if(index>=0){
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										usageNameHid:record.encode,
										usageName:record.name
									}
								});
							}
						}
					}
				}
			});
			//给中草药用法方法绑定弹窗事件
			bindEnterEvent('adChinMediUsaTdId',popWinToChinMediUsa,'easyui');
			$('#adChinMediRemTdId').combobox({//中草药-备注
				data:colorInfoRemarksList,
				disabled:false,
				editable:true,
				valueField:'encode',	
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
						var index = getIndexForAdDgList();
						var row = $('#adDgList').datagrid('getSelected');
						if(row!=null&&row.group!=null&&row.group!=''){
							var rows = $('#adDgList').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].group==row.group){
									var indexRow = $('#adDgList').datagrid('getRowIndex',rows[i]);
									$('#adDgList').datagrid('updateRow',{
										index: indexRow,
										row: {
											remark:record.name
										}
									});
								}
							}
						}else{
							if(index>=0){
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										remark:record.name
									}
								});
							}
						}
					}
				}
			});
			$('#adChinMediRemTdId').combobox('textbox').bind('keyup',function(event){//中草药-备注
				var retval = $('#adChinMediRemTdId').combobox('getText');
				var index = getIndexForAdDgList();
				var row = $('#adDgList').datagrid('getSelected');
				if(row!=null&&row.group!=null&&row.group!=''){
					var rows = $('#adDgList').datagrid('getRows');
					for(var i=0;i<rows.length;i++){
						if(rows[i].group==row.group){
							var indexRow = $('#adDgList').datagrid('getRowIndex',rows[i]);
							$('#adDgList').datagrid('updateRow',{
								index: indexRow,
								row: {
									remark:retval
								}
							});
						}
					}
				}else{
					if(index>=0){
						$('#adDgList').datagrid('updateRow',{
							index: index,
							row: {
								remark:retval
							}
						});
					}
				}
			});
			bindEnterEvent('adChinMediRemTdId',popWinToCodeOpendocadvmarkForchi,'easyui');
			$('#adNotDrugExeTdId').combobox({});
			
			$('#adNotDrugExeTdId').combobox({//非药品-执行科室
				data:colorInfoExeDeptList,
				disabled:false,
				editable:false,
				valueField:'deptCode',	
				textField:'deptName',
				onSelect:function(record){
					if(record!=null&&record!=''){
						if(record!=null){
							var index = getIndexForAdDgList();
							if(index>=0){
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										executiveDeptHid:record.deptCode,
										executiveDept:record.deptName
									}
								});
							}
						}
					}
				}
			});
			bindEnterEvent('adNotDrugExeTdId',popWinToDept,'easyui');//绑定单位回车事件
			$('#adNotDrugInsTdId').combobox({//非药品-部位
				data:colorInfoCheckpointList,
				disabled:false,
				editable:false,
				valueField:'encode',	
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
						var index = getIndexForAdDgList();
						var row = $('#adDgList').datagrid('getSelected');
						if(row!=null&&row.group!=null&&row.group!=''){
							var rows = $('#adDgList').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].group==row.group){
									var indexRow = $('#adDgList').datagrid('getRowIndex',rows[i]);
									$('#adDgList').datagrid('updateRow',{
										index: indexRow,
										row: {
											inspectPartId:record.encode,
											inspectPart:record.name
										}
									});
								}
							}
						}else{
							if(index>=0){
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										inspectPartId:record.encode,
										inspectPart:record.name
									}
								});
							}
						}
					}
				}
			});
			//给检查部位或标本方法绑定弹窗事件
			bindEnterEvent('adNotDrugInsTdId',popWinToCheckpoint,'easyui');
			$('#adNotDrugSamTdId').combobox({//非药品-样本类型
				data:colorInfoSampleTeptList,
				disabled:false,
				editable:false,
				valueField:'encode',	
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
						var index = getIndexForAdDgList();
						var row = $('#adDgList').datagrid('getSelected');
						if(row!=null&&row.group!=null&&row.group!=''){
							var rows = $('#adDgList').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].group==row.group){
									var indexRow = $('#adDgList').datagrid('getRowIndex',rows[i]);
									$('#adDgList').datagrid('updateRow',{
										index: indexRow,
										row: {
											sampleTeptHid:record.encode,
											sampleTept:record.name
										}
									});
								}
							}
						}else{
							if(index>=0){
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										sampleTeptHid:record.encode,
										sampleTept:record.name
									}
								});
							}
						}
					}
				}
			});
			//给样本类型方法绑定弹窗事件
			bindEnterEvent('adNotDrugSamTdId',popWinToLaboratorysample,'easyui');
			$('#adNotDrugRemTdId').combobox({//非药品-备注
				data:colorInfoRemarksList,
				disabled:false,
				editable:true,
				valueField:'encode',	
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
						var index = getIndexForAdDgList();
						if(index>=0){
							$('#adDgList').datagrid('updateRow',{
								index: index,
								row: {
									remark:record.name
								}
							});
						}
					}
				}
			});
			$('#adNotDrugRemTdId').combobox('textbox').bind('keyup',function(event){//非药品-备注
				var retval = $('#adNotDrugRemTdId').combobox('getText');
				var index = getIndexForAdDgList();
				if(index>=0){
					$('#adDgList').datagrid('updateRow',{
						index: index,
						row: {
							remark:retval
						}
					});
				}
			});
			
			bindEnterEvent('adNotDrugRemTdId',popWinToCodeOpendocadvmarkFornotdrug,'easyui');
			$('#adProjectNumTdId').numberspinner({//数量数字输入框事件
				min:1,max:100,disabled:false,
				onChange:function(newValue,oldValue){
					var retVal = newValue;
					if(retVal==null||retVal==''){
						return;
					}
					var index = getIndexForAdDgList();
// 					var row = $('#adDgList').datagrid('getSelected');
// 					if(row.type==sysTypeMap['中草药']){
// 						$('#adDgList').datagrid('updateRow',{
// 							index: index,
// 							row: {
// 								dosageHid:retVal,
// 								dosage:retVal+colorInfoUnitsMap.get(row.adMinUnitHid),
// 								totalNum:(retVal*row.setNum).toFixed(3)
// 							}
// 						});
// 					}else{
						//业务变更 组合不参与数量调整 2017-03-02 16:03 aizhonghua
// 						if(row!=null&&row.group!=null&&row.group!=''){
// 							var rows = $('#adDgList').datagrid('getRows');
// 							for(var i=0;i<rows.length;i++){
// 								if(rows[i].group==row.group){
// 									var indexRow = $('#adDgList').datagrid('getRowIndex',rows[i]);
// 									$('#adDgList').datagrid('updateRow',{
// 										index: indexRow,
// 										row: {
// 											totalNum:parseInt(retVal)
// 										}
// 									});
// 								}
// 							}
// 						}else{
							if(index>=0){
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										totalNum:parseInt(retVal)
									}
								});
							}
// 						}
// 					}
				}
			});
			$('#adWestMediDosMaxTdId').numberbox('textbox').bind('keyup',function(event){//西药,中成药-每次用量-单位
				var index = getIndexForAdDgList();
				if(index>=0){
					var curVal = $('#adWestMediDosMaxTdId').numberbox('getText');
					if(curVal>999){
						curVal=999;
					}
					if(curVal==null||curVal==''){
						curVal=0;
					}
					var row = $('#adDgList').datagrid('getSelected');
					var dosage = (curVal*parseFloat(row.adDrugBasiHid)).toFixed(1);
					var retVal = curVal+colorInfoUnitsMap.get(row.adMinUnitHid)+"="+dosage+colorInfoUnitsMap.get(row.adDosaUnitHidJudge);
					$('#adWestMediDosMinTdId').numberbox('setText',dosage);
					$('#adDgList').datagrid('updateRow',{
						index: index,
						row: {
							dosageMin:dosage,
							dosageHid:curVal,
							dosage:retVal
						}
					});
				}
			}); 
		}
		
		/**  
		 *  
		 * 频次弹框
		 * @Author：aizhonghua
		 * @CreateDate：2015-6-26 上午11:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2015-6-26 上午11:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function popWinToBusinessFrequency(){
			var tempWinPath = "<%=basePath%>popWin/popWinBusinessFrequency/toBusinessFrequencyPopWin.action?textId=adWestMediFreTdId";
			var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
					
		}
		/**  
		 *  
		 * 用法弹框
		 * @Author：aizhonghua
		 * @CreateDate：2015-6-26 上午11:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2015-6-26 上午11:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function popWinToUseage(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=useage&textId=adWestMediUsaTdId";
			var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
		}
		
		/**  
		 *  
		 * 备注弹框
		 * @Author：aizhonghua
		 * @CreateDate：2015-6-26 上午11:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2015-6-26 上午11:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		 function popWinToCodeOpendocadvmark(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=opendocadvmark&textId=adWestMediRemTdId";
			var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
		 }
		
		 /**  
		  *  
		  * 打开中草药-用法界面弹框
		  * @Author：aizhonghua
		  * @CreateDate：2015-6-26 上午11:56:59  
		  * @Modifier：aizhonghua
		  * @ModifyDate：2015-6-26 上午11:56:59  
		  * @ModifyRmk：  
		  * @version 1.0
		  *
		  */
		 function popWinToChinMediUsa(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=useage&textId=adChinMediUsaTdId";
			var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
		 }
		 
		 /**  
		  *  
		  * 打开备注界面弹框
		  * @Author：aizhonghua
		  * @CreateDate：2015-6-26 上午11:56:59  
		  * @Modifier：aizhonghua
		  * @ModifyDate：2015-6-26 上午11:56:59  
		  * @ModifyRmk：  
		  * @version 1.0
		  *
		  */
		 function popWinToCodeOpendocadvmarkForchi(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=opendocadvmark&textId=adChinMediRemTdId";
			var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
		 }
		 
		 /**  
		  *  
		  * 打开检查部位或标本界面弹框
		  * @Author：aizhonghua
		  * @CreateDate：2015-6-26 上午11:56:59  
		  * @Modifier：aizhonghua
		  * @ModifyDate：2015-6-26 上午11:56:59  
		  * @ModifyRmk：  
		  * @version 1.0
		  *
		  */
		function popWinToCheckpoint(){
			var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=checkpoint&textId=adNotDrugInsTdId";
			var aaa=window.open (tempWinPath,'newwindow',' left=300,top=200,width='+ (screen.availWidth - 550) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
		}
		 
		 /**  
		  *  
		  * 打开样本类型界面弹框
		  * @Author：aizhonghua
		  * @CreateDate：2015-6-26 上午11:56:59  
		  * @Modifier：aizhonghua
		  * @ModifyDate：2015-6-26 上午11:56:59  
		  * @ModifyRmk：  
		  * @version 1.0
		  *
		  */
		function popWinToLaboratorysample(){
			var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=laboratorysample&textId=adNotDrugSamTdId";
			var aaa=window.open (tempWinPath,'newwindow',' left=300,top=200,width='+ (screen.availWidth - 550) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
					
		}
		 
		 /**  
		  *  
		  * 打开备注界面弹框
		  * @Author：aizhonghua
		  * @CreateDate：2015-6-26 上午11:56:59  
		  * @Modifier：aizhonghua
		  * @ModifyDate：2015-6-26 上午11:56:59  
		  * @ModifyRmk：  
		  * @version 1.0
		  *
		  */
		function popWinToCodeOpendocadvmarkFornotdrug(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=opendocadvmark&textId=adNotDrugRemTdId";
			var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
		}
		 
		 /**  
		  *  
		  * 回车弹出取药科室选择窗口
		  * @Author：aizhonghua
		  * @CreateDate：2015-6-26 上午11:56:59  
		  * @Modifier：aizhonghua
		  * @ModifyDate：2015-6-26 上午11:56:59  
		  * @ModifyRmk：  
		  * @param deptIsforregister 是否是挂号科室 1是 0否
		  * @version 1.0
		  *
		  */
		function popWinToDept(){
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=adNotDrugExeTdId";
			window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
		}
		 
		/**  
		 *  
		 * 组套查询
		 * @Author：aizhonghua
		 * @CreateDate：2015-6-26 上午11:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2015-6-26 上午11:56:59  
		 * @ModifyRmk：  
		 * @param deptIsforregister 是否是挂号科室 1是 0否
		 * @version 1.0
		 *
		 */
		function searchStackTreeNodes(){
			var searchText = $('#adStackTreeSearch').textbox('getValue');
			$("#adStackTree").tree("search", searchText);
		}
		 
		</script>
	</body>
</html>