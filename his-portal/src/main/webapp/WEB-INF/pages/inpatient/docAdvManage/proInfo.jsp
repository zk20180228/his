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
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<div>
		   <div style="padding: 5px 5px 0px 5px;">
				<table style="width: 100%; border: 1px solid #95b8e7; padding: 5px 5px 5px 5px;">
					<tr>
						<td style="width:270px;">项目查询:&nbsp;&nbsp;
					 		<input id="westMediInfoViewNameId" type="text" value="${inpatientOrder.itemName }" style="width: 190px;" />
					 		<input id="westMediInfoViewTypeId" type="hidden" value="${inpatientOrder.classCode }" style="width: 190px;" />
					 		<input id="westMediInfoViewTypeNameId" type="hidden" value="${inpatientOrder.className }" style="width: 190px;" />
					 		&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchWestMediInfo()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					 		&nbsp;&nbsp;<span style="color:red;">单击</span>列表查看详情,<span style="color:red;">双击</span>选择项目
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div>
			<div style="padding: 5px 5px 5px 5px;height:400px;">
				<table id="westMediInfoViewListId" class="easyui-datagrid" style="height:400px" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'name',styler:gradeStyler" style="">名称</th>
							<th data-options="field:'sysType',formatter:sysTypeFamater" style="" >类别</th>
							<th data-options="field:'specs'" style="">规格</th>							
							<th data-options="field:'defaultprice'" style="">默认价格</th>
							<th data-options="field:'drugPackagingunit',formatter:drugpackagingunitFamater" style="">单位</th>
							<th data-options="field:'drugGrade',title:'itemCode',formatter:speidFamater" style="">医保标记</th>
							<th data-options="field:'undrugIsprovincelimit',hidden:true" style="">是否省限制</th>
							<th data-options="field:'undrugIscitylimit',hidden:true" style="">是否市限制 </th>
							<th data-options="field:'undrugIsownexpense',hidden:true" style="">是否自费</th>
							<th data-options="field:'undrugIsspecificitems',hidden:true" style="">是否特定项目</th>
							<th data-options="field:'inputCode'" style="">自定义码</th>
							<th data-options="field:'drugCommonname'" style="">药品通用名</th>																		
							<th data-options="field:'drugId',formatter:drugStorageFamater" style="" >库存可用数量</th>
							<th data-options="field:'dept',formatter:implDepartmentFamater" style="" >执行科室</th>
							<th data-options="field:'inspectionSite'" style="" >检查检体</th>
							<th data-options="field:'diseaseClassification',formatter:diseasetypeFamater" style="" >疾病分类</th>
							<th data-options="field:'specialtyName'" style="" >专科名称</th>
							<th data-options="field:'medicalHistory'" style="" >病史及检查</th>
							<th data-options="field:'requirements'" style="" >检查要求</th>
							<th data-options="field:'notes'" style="" >注意事项</th>
							<th data-options="field:'gbcode',hidden:true" style="" >国家基本药物编码</th>
							<th data-options="field:'drugInstruction',hidden:true" style="" >说明书</th>
							<th data-options="field:'drugOncedosage',hidden:true" style="" >一次用量</th>
							<th data-options="field:'drugDoseunit',hidden:true,formatter:drugpdoseunitFamaters" style="" >剂量单位</th>
							<th data-options="field:'drugFrequency',hidden:true,formatter:drugfrequencyFamater" style="" >频次</th>
							<th data-options="field:'lowSum',hidden:true" style="" >最低库存</th>
							<th data-options="field:'drugDosageform',hidden:true" style="" >剂型代码</th>
							<th data-options="field:'drugType',hidden:true" style="" >药品类别</th>
							<th data-options="field:'drugNature',hidden:true" style="" >药品性质</th>
							<th data-options="field:'drugRetailprice',hidden:true" style="" >零售价</th>
							<th data-options="field:'remark',hidden:true" style="" >备注</th>
							<th data-options="field:'drugUsemode',hidden:true,formatter:drugusemodeFamater" style="" >使用方法</th>
							<th data-options="field:'drugBasicdose',hidden:true" style="" >基本剂量</th>							
							<th data-options="field:'undrugIsinformedconsent',hidden:true" style="" >是否知情同意书</th>														
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div>
			<div id="drugDetail" style="padding: 0px 5px 5px 5px;width:100%;display:none;" >
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:100%">
					<tr><td style="width:100px" class="detailColor">药品编码</td><td id="westMediInfoViewDrugCodeId" class="detailColor"></td><td style="width:100px;background-color:red;">药房名称:</td></tr>
					<tr><td style="width:100px" class="detailColor">药品名称:</td><td id="westMediInfoViewDrugNameId" class="detailColor"></td><td rowspan="6" id="westMediInfoViewPharId" style="background-color:red;"></td></tr>
					<tr><td style="width:100px" class="detailColor">自负比例:</td><td id="westMediInfoViewDrugScalId" class="detailColor"></td></tr>
					<tr><td style="width:100px" class="detailColor">国家基本药物编码:</td><td id="westMediInfoViewDrugGbcodeId" class="detailColor"></td></tr>
					<tr><td style="width:100px" class="detailColor">适用症:</td><td id="westMediInfoViewDrugApplId" class="detailColor"></td></tr>
					<tr><td style="width:100px" class="detailColor">使用限制等级:</td><td id="westMediInfoViewDrugGradId" class="detailColor"></td></tr>
					<tr><td style="width:100px" class="detailColor">药品说明书:</td><td id="westMediInfoViewDrugInstId" class="detailColor"></td></tr>
				</table>
			</div>
			<div id="unDrugDetail" style="padding: 0px 5px 5px 5px;width:100%;display:none;" >
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:100%">
					<tr><td style="width:100px">非药品编码</td><td id="unWestMediInfoViewDrugCodeId"></td><td style="width:100px;background-color:red;">执行科室:</td></tr>
					<tr><td style="width:100px">非药品名称:</td><td id="unWestMediInfoViewDrugNameId"></td><td rowspan="3" id="unWestMediInfoViewPharId" style="background-color:red;"></td></tr>
					<tr><td style="width:100px">自负比例:</td><td id="unWestMediInfoViewDrugScalId"></td></tr>
					<tr><td style="width:100px">适用症:</td><td id="unWestMediInfoViewDrugApplId"></td></tr>		
				</table>
			</div>
			<input id="userName" type="hidden" value="${inpatientOrder.docName }"/>
			<input id="deptId" type="hidden" value="${inpatientOrder.listDpcd }"/>
			<input id="recUserName" type="hidden" value="${inpatientOrder.recUsernm }"/>
			
		</div>
		<script type="text/javascript">
			var systemTypeMap = "";//系统类别Map			
			var druggradeMap = "";//药品等级Map			
			var diseasetypeList = "";//疾病分类Map			
//			var drugdoseunitMap = "";//计量单位Map		
			var usemodeMap = "";//用法Map
			var drugpackagingunitMap = "";//包装单位Map
			var nonmedicineencodingMap="";//非药品单位Map
			
			//查询系统类别
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/querySystemtype.action",				
				type:'post',
				success: function(systemTypedata) {					
					systemTypeMap = systemTypedata;										
				}
			});	
			//查询包装单位
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
				type:'post',
				success: function(drugpackagingunitdata) {					
					drugpackagingunitMap= drugpackagingunitdata;										
				}
			});
			//查询非药品单位
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryNonmedicineencoding.action",				
				type:'post',
				success: function(nonmedicineencodingdata) {					
					nonmedicineencodingMap = nonmedicineencodingdata;										
				}
			});
		<%-- 	//查询计量单位
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
				type:'post',
				success: function(drugdoseunitdata) {					
					drugdoseunitMap = drugdoseunitdata;										
				}
			}); --%>
			//查询药品等级
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryDruggrade.action",				
				type:'post',
				success: function(druggradedata) {					
					druggradeMap = druggradedata;							
				}
			});	
			//查询疾病分类
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryDiseasetype.action",				
				type:'post',
				success: function(diseasetypedata) {					
					diseasetypeList = diseasetypedata;					
				}
			});	
				
			$(function(){
								
			    $('#westMediInfoViewNameId').textbox({
			        prompt:'名称,拼音,五笔码,自定义码'
			    });
			    			    			   
				$('#westMediInfoViewListId').datagrid({					
					url:'<%=basePath%>inpatient/docAdvManage/queryDrugOrUndrugInfos.action',
					queryParams:{'inpatientOrder.itemName':$('#westMediInfoViewNameId').val(),'inpatientOrder.classCode':$('#westMediInfoViewTypeId').val(),'inpatientOrder.className':$("#westMediInfoViewTypeNameId").val()},					
					onLoadSuccess:function(data){
						$("#drugDetail").hide();
						$("#unDrugDetail").hide();
					},
					onClickRow:function(rowIndex, rowData){
						if(rowData.ty=='1'){
							$("#drugDetail").show();
							$("#unDrugDetail").hide();
						}else{
							$("#drugDetail").hide();
							$("#unDrugDetail").show();
						}
						$('#westMediInfoViewDrugCodeId').text("");//药品编码
						$('#westMediInfoViewDrugNameId').text("");//药品名称
						$('#westMediInfoViewDrugScalId').text("");//自负比例
						$('#westMediInfoViewDrugGbcodeId').text("");//国家基本药物编码
						$('#westMediInfoViewDrugApplId').text("");//适用症
						$('#westMediInfoViewDrugGradId').text("");//使用限制等级
						$('#westMediInfoViewDrugInstId').text("");//药品说明书
						$('#westMediInfoViewPharId').text("");//药房	
						$('#unWestMediInfoViewDrugCodeId').text("");//非药品编码
						$('#unWestMediInfoViewDrugNameId').text("");//非药品名称
						$('#unWestMediInfoViewDrugScalId').text("");//非药品自负比例
						$('#unWestMediInfoViewDrugApplId').text("");//非药品适用症
						$('#unWestMediInfoViewPharId').text("");//执行科室
						$('#westMediInfoViewDrugCodeId').text(rowData.drugId);//药品编码
						$('#westMediInfoViewDrugNameId').text(rowData.name);//药品名称
						$('#westMediInfoViewDrugScalId').text('100%');//自负比例
						$('#westMediInfoViewDrugGbcodeId').text(rowData.gbcode);//国家基本药物编码
						$('#westMediInfoViewDrugApplId').text("");//适用症
						$('#westMediInfoViewDrugGradId').text(druggradeMap[rowData.drugGrade]!=null?druggradeMap[rowData.drugGrade]:'');//使用限制等级
						$('#westMediInfoViewDrugInstId').text(rowData.drugInstruction);//药品说明书
						if(rowData.gbcode!=""&&rowData.gbcode!=null&&rowData.ty!='0'){
							$(".detailColor").css('background-color','green');
						}else{
							$(".detailColor").removeAttr("style",{"background-color":"green"});	
						}				
						$('#westMediInfoViewPharId').text($('#pharmacyInputName').val());//药房
						$('#unWestMediInfoViewDrugCodeId').text(rowData.drugId);//非药品编码
						$('#unWestMediInfoViewDrugNameId').text(rowData.name);//药品名称
						$('#unWestMediInfoViewDrugScalId').text('100%');//非药品自负比例
						$('#unWestMediInfoViewDrugApplId').text("");//非药品适用症
						$('#unWestMediInfoViewPharId').text(implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'');//执行科室
					},
					onDblClickRow:function(rowIndex, rowData){																		
						var bool = false;
						if($('#adWestMediUrgTdId').is(':checked')){//判断复选框是否被选中
							bool = true;
						}
						var userId = $('#userid').val();
						var mytime = new Date().format("yyyy-MM-dd hh:mm:ss");
						var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							if(rowData.ty=='1'){//判断是药品还是非药品
								var drugNature= rowData.drugNature!=null?rowData.drugNature:'';
								$.ajax({
									url: "<%=basePath%>inpatient/docAdvManage/queryAdvdrugnature.action",		
									data:'proInfoVo.drugNature='+drugNature,
									type:'post',
									success: function(bdata) {
									var drugNatureData =  bdata;
									if(drugNatureData.length==0){																				
										if(rowData.drugRestrictionofantibiotic==3){//-----b
											$.ajax({
												url: "<%=basePath%>inpatient/docAdvManage/queryAuditInfo.action",		
												data:'userId='+userId+'&parameterCode='+'yzshzj',
												type:'post',
												success: function(auditdata) {
													if(auditdata==0){//-----a
														$.extend($.messager.defaults,{  
													        ok:"是",  
													        cancel:"否"  
													    });  
														jQuery.messager.confirm('确认信息',rowData.name+'开立需要上级医生审核,确认开立吗?',function(e){   
															if(e){ 
																if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
																	jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
																		if(event){  									
																			var lastIndex = $('#infolistA').datagrid('appendRow',{
																				changeNo:1,
																				id:'',
																				itemCode:'',//特殊标识
																				typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																				typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																				classCode:rowData.sysType,//系统类别Id
																				className:systemTypeMap[rowData.sysType],//系统类别名称
																				itemCode:rowData.itemCode,//医嘱Id
																				itemName:rowData.name,//医嘱名称
																				combNo:'',//组
																				qtyTot:1,//总量
																				priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																				drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																				packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量		
																				minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																				doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
//																				doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																				doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																				specs:rowData.specs!=null?rowData.specs:'',//规格
																				doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																				drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																				drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																				itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																				useDays:0,//付数
																				frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																				frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																				usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																				useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																				dateBgn:mytime,//开始时间 
																				dateEnd:'',//停止时间
																				moDate:mytime,//开立时间 
																				docCode:'',//开立医生代码
																				docName:$('#userName').val(),//开立医生名称
																				execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																				execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																				emcFlag:0,//加急标记
																				isUrgent:0,//急
																				labCode:'',//样本类型
																				itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																				//openDoctor:'当前医生',//
																				pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																				moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																				recUsernm:$('#recUserName').val(),//录入人
																				listDpcd:$('#deptId').val(),//开立科室 
																				updateUser:'',//停止人 
																				baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																				permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																				hypotest:2,//皮试Id
																				hypotestName:'需要皮试，未做',//皮试名称
																				itemType:rowData.ty,//药品非药品标识
																				moStat:-1,//医嘱状态
																				sortId:sortIdCreate()//顺序号							
																			}).datagrid('getRows').length-1;
																			$('#infolistA').datagrid('selectRow',lastIndex);						
																			$('#adWestMediModlDivId').dialog('close');
																		}else{ 
																			var lastIndex = $('#infolistA').datagrid('appendRow',{
																				changeNo:1,
																				id:'',
																				itemCode:'',//特殊标识
																				typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																				typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																				classCode:rowData.sysType,//系统类别Id
																				className:systemTypeMap[rowData.sysType],//系统类别名称
																				itemCode:rowData.itemCode,//医嘱Id
																				itemName:rowData.name,//医嘱名称
																				combNo:'',//组
																				qtyTot:1,//总量
																				priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																				drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																				packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																				minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																				doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
												//								doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																				doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																				specs:rowData.specs!=null?rowData.specs:'',//规格
																				doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																				drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																				drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																				itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																				useDays:0,//付数
																				frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																				frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																				usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																				useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																				dateBgn:mytime,//开始时间 
																				dateEnd:'',//停止时间
																				moDate:mytime,//开立时间 
																				docCode:'',//开立医生代码
																				docName:$('#userName').val(),//开立医生
																				execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																				execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																				emcFlag:0,//加急标记
																				isUrgent:0,//急
																				labCode:'',//样本类型
																				itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																				//openDoctor:'当前医生',//
																				pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																				moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																				recUsernm:$('#recUserName').val(),//录入人
																				listDpcd:$('#deptId').val(),//开立科室 
																				updateUser:'',//停止人 
																				baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																				permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																				hypotest:1,//皮试Id
																				hypotestName:'不需要皮试',
																				itemType:rowData.ty,//药品非药品标识
																				moStat:-1,//医嘱状态
																				sortId:sortIdCreate()//顺序号								
																			}).datagrid('getRows').length-1;
																			$('#infolistA').datagrid('selectRow',lastIndex);						
																			$('#adWestMediModlDivId').dialog('close');
																		}   
																		});
																}else{//不是西药，不用考虑皮试
																	var lastIndex = $('#infolistA').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		itemCode:'',//特殊标识
																		typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,//系统类别Id
																		className:systemTypeMap[rowData.sysType],//系统类别名称
																		itemCode:rowData.itemCode,//医嘱Id
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
	//																	doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算	
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间 
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		//openDoctor:'当前医生',//
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:'',//皮试Id
																		hypotestName:'',//皮试名称
																		itemType:rowData.ty,//药品非药品标识
																		moStat:-1,//医嘱状态
																		sortId:sortIdCreate()//顺序号								
																	}).datagrid('getRows').length-1;
																	$('#infolistA').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}															
															}
														});
													}else{
														if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
															jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
																if(event){  									
																	var lastIndex = $('#infolistA').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		itemCode:'',//特殊标识
																		typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,//系统类别id
																		className:systemTypeMap[rowData.sysType],//系统类别名称
																		itemCode:rowData.itemCode,//医嘱名称Id
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//																doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		//openDoctor:'当前医生',//
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:2,
																		hypotestName:'需要皮试，未做',
																		itemType:rowData.ty,//药品非药品标识
																		moStat:0,//医嘱状态
																		sortId:sortIdCreate()//顺序号							
																	}).datagrid('getRows').length-1;
																	$('#infolistA').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}else{ 
																	var lastIndex = $('#infolistA').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		itemCode:'',//特殊标识
																		typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,//系统类别Id
																		className:systemTypeMap[rowData.sysType],//系统类别名称
																		itemCode:rowData.itemCode,//医嘱名称Id
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//																doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）													
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间 
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		//openDoctor:'当前医生',//
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:1,//皮试Id
																		hypotestName:'不需要皮试',//皮试名称
																		itemType:rowData.ty,//药品非药品标识
																		moStat:0,//医嘱状态
																		sortId:sortIdCreate()//顺序号								
																	}).datagrid('getRows').length-1;
																	$('#infolistA').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}   
																});
														}else{
															var lastIndex = $('#infolistA').datagrid('appendRow',{
																changeNo:1,
																id:'',
																itemCode:'',//特殊标识
																typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																classCode:rowData.sysType,//医嘱类别Id
																className:systemTypeMap[rowData.sysType],//医嘱类别名称
																itemCode:rowData.itemCode,//医嘱名称Id
																itemName:rowData.name,//医嘱名称
																combNo:'',//组
																qtyTot:1,//总量
																priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//														doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																specs:rowData.specs!=null?rowData.specs:'',//规格
																doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																useDays:0,//付数
																frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',//用法Id		
																useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																dateBgn:mytime,//开始时间 
																dateEnd:'',//停止时间
																moDate:mytime,//开立时间 
																docCode:'',//开立医生代码
																docName:$('#userName').val(),//开立医生
																execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																emcFlag:0,//加急标记
																isUrgent:0,//急
																labCode:'',//样本类型
																itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																//openDoctor:'当前医生',//
																pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																recUsernm:$('#recUserName').val(),//录入人
																listDpcd:$('#deptId').val(),//开立科室 
																updateUser:'',//停止人 
																baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																hypotest:'',//皮试Id
																hypotestName:'',//皮试名称
																itemType:rowData.ty,//药品非药品标识
																moStat:0,//医嘱状态
																sortId:sortIdCreate()//顺序号						
															}).datagrid('getRows').length-1;
															$('#infolistA').datagrid('selectRow',lastIndex);						
															$('#adWestMediModlDivId').dialog('close');
														}														
													}//-----a														
												}
											});
										}
										else{
											if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
												jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
													if(event){  									
														var lastIndex = $('#infolistA').datagrid('appendRow',{
															changeNo:1,
															id:'',
															itemCode:'',//特殊标识
															typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
															typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
															classCode:rowData.sysType,
															className:systemTypeMap[rowData.sysType],
															itemCode:rowData.itemCode,//医嘱名称Id
															itemName:rowData.name,//医嘱名称
															combNo:'',//组
															qtyTot:1,//总量
															priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
															drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
															packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
															minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
															doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
			//												doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
															doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
															specs:rowData.specs!=null?rowData.specs:'',
															doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
															drugType:rowData.drugType!=null?rowData.drugType:'',
															drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
															itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
															useDays:0,//付数
															frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
															frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
															usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
															useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
															dateBgn:mytime,//开始时间 
															dateEnd:'',//停止时间
															moDate:mytime,//开立时间 
															docCode:'',//开立医生代码
															docName:$('#userName').val(),//开立医生
															execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
															execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
															emcFlag:0,//加急标记
															isUrgent:0,//急
															labCode:'',//样本类型
															itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
															//openDoctor:'当前医生',//
															pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
															moNote2:rowData.remark!=null?rowData.remark:'',//备注 
															recUsernm:$('#recUserName').val(),//录入人
															listDpcd:$('#deptId').val(),//开立科室 
															updateUser:'',//停止人 
															baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
															permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
															hypotest:2,//皮试Id
															hypotestName:'需要皮试，未做',//皮试名称
															itemType:rowData.ty,//药品非药品标识
															moStat:0,//医嘱状态
															sortId:sortIdCreate()//顺序号							
														}).datagrid('getRows').length-1;
														$('#infolistA').datagrid('selectRow',lastIndex);						
														$('#adWestMediModlDivId').dialog('close');
													}else{ 
														var lastIndex = $('#infolistA').datagrid('appendRow',{
															changeNo:1,
															id:'',
															itemCode:'',//特殊标识
															typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
															typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
															classCode:rowData.sysType,//系统类别Id
															className:systemTypeMap[rowData.sysType],//系统类别名称
															itemCode:rowData.itemCode,//医嘱名称Id
															itemName:rowData.name,//医嘱名称
															combNo:'',//组
															qtyTot:1,//总量
															priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
															drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
															packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
															minUnit:rowData.unit!=null?rowData.unit:'',//最小单位		
															doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//													doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
															doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
															specs:rowData.specs!=null?rowData.specs:'',//规格
															doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
															drugType:rowData.drugType!=null?rowData.drugType:'',
															drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
															itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
															useDays:0,//付数
															frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
															frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
															usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',//用法Id		
															useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
															dateBgn:mytime,//开始时间 
															dateEnd:'',//停止时间
															moDate:mytime,//开立时间 
															docCode:'',//开立医生代码
															docName:$('#userName').val(),//开立医生
															execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
															execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
															emcFlag:0,//加急标记
															isUrgent:0,//急
															labCode:'',//样本类型
															itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
															//openDoctor:'当前医生',//
															pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
															moNote2:rowData.remark!=null?rowData.remark:'',//备注 
															recUsernm:$('#recUserName').val(),//录入人
															listDpcd:$('#deptId').val(),//开立科室 
															updateUser:'',//停止人 
															baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
															permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
															hypotest:1,//皮试Id
															hypotestName:'不需要皮试',//皮试名称
															itemType:rowData.ty,//药品非药品标识
															moStat:0,//医嘱状态
															sortId:sortIdCreate()//顺序号							
														}).datagrid('getRows').length-1;
														$('#infolistA').datagrid('selectRow',lastIndex);						
														$('#adWestMediModlDivId').dialog('close');
													}   
													});
											}else{
												var lastIndex = $('#infolistA').datagrid('appendRow',{
													changeNo:1,
													id:'',
													itemCode:'',//特殊标识
													typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
													typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
													classCode:rowData.sysType,
													className:systemTypeMap[rowData.sysType],
													itemCode:rowData.itemCode,
													itemName:rowData.name,//医嘱名称
													combNo:'',//组
													qtyTot:1,//总量
													priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
													drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
													packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
													minUnit:rowData.unit!=null?rowData.unit:'',//最小单位		
													doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//											doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
													doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
													specs:rowData.specs!=null?rowData.specs:'',
													doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
													drugType:rowData.drugType!=null?rowData.drugType:'',
													drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
													itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
													useDays:0,//付数
													frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
													frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
													usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
													useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
													dateBgn:mytime,//开始时间 
													dateEnd:'',//停止时间
													moDate:mytime,//开立时间 
													docCode:'',//开立医生代码
													docName:$('#userName').val(),//开立医生
													execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
													execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
													emcFlag:0,//加急标记
													isUrgent:0,//急
													labCode:'',//样本类型
													itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
													//openDoctor:'当前医生',//
													pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
													moNote2:rowData.remark!=null?rowData.remark:'',//备注 
													recUsernm:$('#recUserName').val(),//录入人
													listDpcd:$('#deptId').val(),//开立科室 
													updateUser:'',//停止人 
													baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
													permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
													hypotest:'',//皮试Id
													hypotestName:'',//皮试名称
													itemType:rowData.ty,//药品非药品标识
													moStat:0,//医嘱状态
													sortId:sortIdCreate()//顺序号								
												}).datagrid('getRows').length-1;
												$('#infolistA').datagrid('selectRow',lastIndex);						
												$('#adWestMediModlDivId').dialog('close');
											}											
										}//-----b																																																														    
									}
									else{
										$.messager.alert('提示',"限制药品性质的药品不可以开立！");
									}
								}
								});
							}
							else{//非药品
								if($('#westMediInfoViewTypeId').val()=='402880ae51a4ade90151a4df08520005'){//类型为护理级别
									var lastIndex = $('#infolistA').datagrid('appendRow',{
										changeNo:1,
										id:'',
										itemCode:'',//特殊标识
										typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
										typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
										classCode:rowData.sysType,
										className:systemTypeMap[rowData.sysType],
										itemCode:rowData.itemCode,//医嘱Id
										itemName:rowData.name,//医嘱名称
										combNo:'',//组
										qtyTot:1,//总量
										priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
										drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
										packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
										minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
										doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:0,//每次量
										doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
										specs:rowData.specs!=null?rowData.specs:'',//规格
										doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
										drugType:rowData.drugType!=null?rowData.drugType:'',
										drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
										itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
										useDays:0,//付数
										frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
										frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
										usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
										useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
										dateBgn:mytime,//开始时间 
										dateEnd:'',//停止时间
										moDate:mytime,//开立时间 
										docCode:'',//开立医生代码
										docName:$('#userName').val(),//开立医生
										execDpcd:$('#nurseCellCode').val(),//执行科室 id
										execDpnm:implDepartmentMap[$('#nurseCellCode').val()]!=null?implDepartmentMap[$('#nurseCellCode').val()]:'',//执行科室 
										emcFlag:0,//加急标记
										isUrgent:0,//急
										labCode:'',//样本类型
										itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
										//openDoctor:'当前医生',//
										pharmacyCode:'',//扣库科室
										moNote2:rowData.remark!=null?rowData.remark:'',//备注 
										recUsernm:$('#recUserName').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室 
										updateUser:'',//停止人 
										baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
										permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
										hypotest:'',//皮试Id
										hypotestName:'',//皮试									
										itemType:rowData.ty,//药品非药品标识
										moStat:0,//医嘱状态
										sortId:sortIdCreate()//顺序号								
									}).datagrid('getRows').length-1;
									$('#infolistA').datagrid('selectRow',lastIndex);						
									$('#adWestMediModlDivId').dialog('close');
								}else{
									var lastIndex = $('#infolistA').datagrid('appendRow',{
										changeNo:1,
										id:'',
										itemCode:'',//特殊标识
										typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
										typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
										classCode:rowData.sysType,
										className:systemTypeMap[rowData.sysType],
										itemCode:rowData.itemCode,//医嘱Id
										itemName:rowData.name,//医嘱名称
										combNo:'',//组
										qtyTot:1,//总量
										priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
										drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
										packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
										minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
										doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:0,//每次量
										doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
										specs:rowData.specs!=null?rowData.specs:'',//规格
										doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
										drugType:rowData.drugType!=null?rowData.drugType:'',
										drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
										itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
										useDays:0,//付数
										frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
										frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
										usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
										useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
										dateBgn:mytime,//开始时间 
										dateEnd:'',//停止时间
										moDate:mytime,//开立时间 
										docCode:'',//开立医生代码
										docName:$('#userName').val(),//开立医生
										execDpcd:$('#deptId').val(),//执行科室 id
										execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
										emcFlag:0,//加急标记
										isUrgent:0,//急
										labCode:'',//样本类型
										itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
										//openDoctor:'当前医生',//
										pharmacyCode:'',//扣库科室
										moNote2:rowData.remark!=null?rowData.remark:'',//备注 
										recUsernm:$('#recUserName').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室 
										updateUser:'',//停止人 
										baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
										permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
										hypotest:'',//皮试Id
										hypotestName:'',//皮试									
										itemType:rowData.ty,//药品非药品标识
										moStat:0,//医嘱状态
										sortId:sortIdCreate()//顺序号								
									}).datagrid('getRows').length-1;
									$('#infolistA').datagrid('selectRow',lastIndex);						
									$('#adWestMediModlDivId').dialog('close');
								}								
							}									
						}
						else{//临时医嘱						
							if(rowData.ty=='1'){//判断是药品还是非药品															
								if(rowData.drugRestrictionofantibiotic==3){//-----b
									$.ajax({
										url: "<%=basePath%>inpatient/docAdvManage/queryAuditInfo.action",		
										data:'userId='+userId+'&parameterCode='+'yzshzj',
										type:'post',
										success: function(auditdata) {
											if(auditdata==0){//-----a
												$.extend($.messager.defaults,{  
											        ok:"是",  
											        cancel:"否"  
											    });  
												jQuery.messager.confirm('确认信息',rowData.name+'开立需要上级医生审核,确认开立吗?',function(e){   
													if(e){ 
														if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
															jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
																if(event){  									
																	var lastIndex = $('#infolistB').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		itemCode:'',//特殊标识
																		typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,
																		className:systemTypeMap[rowData.sysType],
																		itemCode:rowData.itemCode,//医嘱Id
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//																doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间 
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		//openDoctor:'当前医生',//
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:2,//皮试Id
																		hypotestName:'需要皮试，未做',//皮试名称
																		itemType:rowData.ty,//药品非药品标识
																		moStat:-1,//医嘱状态
																		sortId:sortIdCreate()//顺序号								
																	}).datagrid('getRows').length-1;
																	$('#infolistB').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}else{ 
																	var lastIndex = $('#infolistB').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		itemCode:'',//特殊标识
																		typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,
																		className:systemTypeMap[rowData.sysType],
																		itemCode:rowData.itemCode,
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//																doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间 
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		//openDoctor:'当前医生',//
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:1,//皮试Id
																		hypotestName:'不需要皮试',//皮试名称
																		itemType:rowData.ty,//药品非药品标识
																		moStat:-1,//医嘱状态
																		sortId:sortIdCreate()//顺序号								
																	}).datagrid('getRows').length-1;
																	$('#infolistB').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}   
																});
														}else{
															var lastIndex = $('#infolistB').datagrid('appendRow',{
																changeNo:1,
																id:'',
																itemCode:'',//特殊标识
																typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																classCode:rowData.sysType,
																className:systemTypeMap[rowData.sysType],
																itemCode:rowData.itemCode,//医嘱名称Id
																itemName:rowData.name,//医嘱名称
																combNo:'',//组
																qtyTot:1,//总量
																priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//														doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																specs:rowData.specs!=null?rowData.specs:'',//规格
																doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																drugType:rowData.drugType!=null?rowData.drugType:'',
																drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																useDays:0,//付数
																frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
																useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
																dateBgn:mytime,//开始时间 
																dateEnd:'',//停止时间
																moDate:mytime,//开立时间 
																docCode:'',//开立医生代码
																docName:$('#userName').val(),//开立医生
																execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																emcFlag:0,//加急标记
																isUrgent:0,//急
																labCode:'',//样本类型
																itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																//openDoctor:'当前医生',//
																pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																recUsernm:$('#recUserName').val(),//录入人
																listDpcd:$('#deptId').val(),//开立科室 
																updateUser:'',//停止人 
																baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																hypotest:'',//皮试Id
																hypotestName:'',//皮试名称
																itemType:rowData.ty,//药品非药品标识
																moStat:-1,//医嘱状态
																sortId:sortIdCreate()//顺序号								
															}).datagrid('getRows').length-1;
															$('#infolistB').datagrid('selectRow',lastIndex);						
															$('#adWestMediModlDivId').dialog('close');
														}												
													}
												});
											}else{
												if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
													jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
														if(event){  									
															var lastIndex = $('#infolistB').datagrid('appendRow',{
																changeNo:1,
																id:'',
																itemCode:'',//特殊标识
																typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																classCode:rowData.sysType,
																className:systemTypeMap[rowData.sysType],
																itemCode:rowData.itemCode,//医嘱名称Id
																itemName:rowData.name,//医嘱名称
																combNo:'',//组
																qtyTot:1,//总量
																priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//														doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																specs:rowData.specs!=null?rowData.specs:'',
																doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																drugType:rowData.drugType!=null?rowData.drugType:'',
																drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																useDays:0,//付数
																frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
																useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
																dateBgn:mytime,//开始时间 
																dateEnd:'',//停止时间
																moDate:mytime,//开立时间 
																docCode:'',//开立医生代码
																docName:$('#userName').val(),//开立医生
																execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																emcFlag:0,//加急标记
																isUrgent:0,//急
																labCode:'',//样本类型
																itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																//openDoctor:'当前医生',//
																pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																recUsernm:$('#recUserName').val(),//录入人
																listDpcd:$('#deptId').val(),//开立科室 
																updateUser:'',//停止人 
																baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																hypotest:2,
																hypotestName:'需要皮试，未做',
																itemType:rowData.ty,//药品非药品标识
																moStat:0,
																sortId:sortIdCreate()//顺序号								
															}).datagrid('getRows').length-1;
															$('#infolistB').datagrid('selectRow',lastIndex);						
															$('#adWestMediModlDivId').dialog('close');
														}else{ 
															var lastIndex = $('#infolistB').datagrid('appendRow',{
																changeNo:1,
																id:'',
																itemCode:'',//特殊标识
																typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																classCode:rowData.sysType,
																className:systemTypeMap[rowData.sysType],
																itemCode:rowData.itemCode,
																itemName:rowData.name,//医嘱名称
																combNo:'',//组
																qtyTot:1,//总量
																priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
			//													doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																specs:rowData.specs!=null?rowData.specs:'',
																doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																drugType:rowData.drugType!=null?rowData.drugType:'',
																drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																useDays:0,//付数
																frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
																useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
																dateBgn:mytime,//开始时间 
																dateEnd:'',//停止时间
																moDate:mytime,//开立时间 
																docCode:'',//开立医生代码
																docName:$('#userName').val(),//开立医生
																execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																emcFlag:0,//加急标记
																isUrgent:0,//急
																labCode:'',//样本类型
																itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																//openDoctor:'当前医生',//
																pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																recUsernm:$('#recUserName').val(),//录入人
																listDpcd:$('#deptId').val(),//开立科室 
																updateUser:'',//停止人 
																baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																hypotest:1,
																hypotestName:'不需要皮试',
																itemType:rowData.ty,//药品非药品标识
																moStat:0,
																sortId:sortIdCreate()//顺序号								
															}).datagrid('getRows').length-1;
															$('#infolistB').datagrid('selectRow',lastIndex);						
															$('#adWestMediModlDivId').dialog('close');
														}   
														});
												}else{
													var lastIndex = $('#infolistB').datagrid('appendRow',{
														changeNo:1,
														id:'',
														itemCode:'',//特殊标识
														typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
														typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
														classCode:rowData.sysType,
														className:systemTypeMap[rowData.sysType],
														itemCode:rowData.itemCode,
														itemName:rowData.name,//医嘱名称
														combNo:'',//组
														qtyTot:1,//总量
														priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
														drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
														packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
														minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
														doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//												doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
														doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
														specs:rowData.specs!=null?rowData.specs:'',
														doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
														drugType:rowData.drugType!=null?rowData.drugType:'',
														drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
														itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
														useDays:0,//付数
														frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
														frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
														usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
														useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
														dateBgn:mytime,//开始时间 
														dateEnd:'',//停止时间
														moDate:mytime,//开立时间 
														docCode:'',//开立医生代码
														docName:$('#userName').val(),//开立医生
														execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
														execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
														emcFlag:0,//加急标记
														isUrgent:0,//急
														labCode:'',//样本类型
														itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
														//openDoctor:'当前医生',//
														pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
														moNote2:rowData.remark!=null?rowData.remark:'',//备注 
														recUsernm:$('#recUserName').val(),//录入人
														listDpcd:$('#deptId').val(),//开立科室 
														updateUser:'',//停止人 
														baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
														permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
														hypotest:'',//皮试Id
														hypotestName:'',//皮试名称
														itemType:rowData.ty,//药品非药品标识
														moStat:0,//医嘱状态
														sortId:sortIdCreate()//顺序号								
													}).datagrid('getRows').length-1;
													$('#infolistB').datagrid('selectRow',lastIndex);						
													$('#adWestMediModlDivId').dialog('close');
												}												
											}//-----a														
										}
									});
								}
								else{
									if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
										jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
											if(event){  									
												var lastIndex = $('#infolistB').datagrid('appendRow',{
													changeNo:1,
													id:'',
													itemCode:'',//特殊标识
													typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
													typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
													classCode:rowData.sysType,
													className:systemTypeMap[rowData.sysType],
													itemCode:rowData.itemCode,
													itemName:rowData.name,//医嘱名称
													combNo:'',//组
													qtyTot:1,//总量
													priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
													drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
													packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
													minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
													doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//											doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
													doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
													specs:rowData.specs!=null?rowData.specs:'',
													doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
													drugType:rowData.drugType!=null?rowData.drugType:'',
													drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
													itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
													useDays:0,//付数
													frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
													frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
													usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
													useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
													dateBgn:mytime,//开始时间 
													dateEnd:'',//停止时间
													moDate:mytime,//开立时间 
													docCode:'',//开立医生代码
													docName:$('#userName').val(),//开立医生
													execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
													execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
													emcFlag:0,//加急标记
													isUrgent:0,//急
													labCode:'',//样本类型
													itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
													//openDoctor:'当前医生',//
													pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
													moNote2:rowData.remark!=null?rowData.remark:'',//备注 
													recUsernm:$('#recUserName').val(),//录入人
													listDpcd:$('#deptId').val(),//开立科室 
													updateUser:'',//停止人 
													baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
													permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
													hypotest:2,
													hypotestName:'需要皮试，未做',
													itemType:rowData.ty,//药品非药品标识
													moStat:0,
													sortId:sortIdCreate()//顺序号								
												}).datagrid('getRows').length-1;
												$('#infolistB').datagrid('selectRow',lastIndex);						
												$('#adWestMediModlDivId').dialog('close');
											}else{ 
												var lastIndex = $('#infolistB').datagrid('appendRow',{
													changeNo:1,
													id:'',
													itemCode:'',//特殊标识
													typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
													typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
													classCode:rowData.sysType,
													className:systemTypeMap[rowData.sysType],
													itemCode:rowData.itemCode,
													itemName:rowData.name,//医嘱名称
													combNo:'',//组
													qtyTot:1,//总量
													priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
													drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
													packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
													minUnit:rowData.unit!=null?rowData.unit:'',//最小单位		
													doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//											doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
													doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
													specs:rowData.specs!=null?rowData.specs:'',
													doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
													drugType:rowData.drugType!=null?rowData.drugType:'',
													drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
													itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
													useDays:0,//付数
													frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
													frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
													usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
													useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//零售价(药品)
													dateBgn:mytime,//开始时间 
													dateEnd:'',//停止时间
													moDate:mytime,//开立时间 
													docCode:'',//开立医生代码
													docName:$('#userName').val(),//开立医生
													execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
													execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
													emcFlag:0,//加急标记
													isUrgent:0,//急
													labCode:'',//样本类型
													itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
													//openDoctor:'当前医生',//
													pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
													moNote2:rowData.remark!=null?rowData.remark:'',//备注 
													recUsernm:$('#recUserName').val(),//录入人
													listDpcd:$('#deptId').val(),//开立科室 
													updateUser:'',//停止人 
													baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
													permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
													hypotest:1,
													hypotestName:'不需要皮试',
													itemType:rowData.ty,//药品非药品标识
													moStat:0,
													sortId:sortIdCreate()//顺序号								
												}).datagrid('getRows').length-1;
												$('#infolistB').datagrid('selectRow',lastIndex);						
												$('#adWestMediModlDivId').dialog('close');
											}   
											});
									}else{
										var lastIndex = $('#infolistB').datagrid('appendRow',{
											changeNo:1,
											id:'',
											itemCode:'',//特殊标识
											typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
											typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
											classCode:rowData.sysType,
											className:systemTypeMap[rowData.sysType],
											itemCode:rowData.itemCode,
											itemName:rowData.name,//医嘱名称
											combNo:'',//组
											qtyTot:1,//总量
											priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
											drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
											packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
											minUnit:rowData.unit!=null?rowData.unit:'',//最小单位		
											doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//									doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
											doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
											specs:rowData.specs!=null?rowData.specs:'',
											doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
											drugType:rowData.drugType!=null?rowData.drugType:'',
											drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
											itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
											useDays:0,//付数
											frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
											frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
											usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
											useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
											dateBgn:mytime,//开始时间 
											dateEnd:'',//停止时间
											moDate:mytime,//开立时间 
											docCode:'',//开立医生代码
											docName:$('#userName').val(),//开立医生
											execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
											execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
											emcFlag:0,//加急标记
											isUrgent:0,//急
											labCode:'',//样本类型
											itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
											//openDoctor:'当前医生',//
											pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
											moNote2:rowData.remark!=null?rowData.remark:'',//备注 
											recUsernm:$('#recUserName').val(),//录入人
											listDpcd:$('#deptId').val(),//开立科室 
											updateUser:'',//停止人 
											baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
											permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
											hypotest:'',//皮试Id
											hypotestName:'',//皮试名称
											itemType:rowData.ty,//药品非药品标识
											moStat:0,//医嘱状态
											sortId:sortIdCreate()//顺序号								
										}).datagrid('getRows').length-1;
										$('#infolistB').datagrid('selectRow',lastIndex);						
										$('#adWestMediModlDivId').dialog('close');
									}								
								}//-----b																																																														    
							}
							else{//非药品
								if($('#westMediInfoViewTypeId').val()=='402880ae51a4ade90151a4df08520005'){//类型为护理级别
									var lastIndex = $('#infolistB').datagrid('appendRow',{
										changeNo:1,
										id:'',
										itemCode:'',//特殊标识
										typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
										typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
										classCode:rowData.sysType,
										className:systemTypeMap[rowData.sysType],
										itemCode:rowData.itemCode,//医嘱Id
										itemName:rowData.name,//医嘱名称
										combNo:'',//组
										qtyTot:1,//总量
										priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
										drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
										packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
										minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
										doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:0,//每次量
										doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
										specs:rowData.specs!=null?rowData.specs:'',//规格
										doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
										drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
										drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
										itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
										useDays:0,//付数
										frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
										frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
										usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
										useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
										dateBgn:mytime,//开始时间 
										dateEnd:'',//停止时间
										moDate:mytime,//开立时间 
										docCode:'',//开立医生代码
										docName:$('#userName').val(),//开立医生
										execDpcd:$('#nurseCellCode').val(),//执行科室 id
										execDpnm:implDepartmentMap[$('#nurseCellCode').val()]!=null?implDepartmentMap[$('#nurseCellCode').val()]:'',//执行科室 
										emcFlag:0,//加急标记
										isUrgent:0,//急
										labCode:'',//样本类型
										itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
										//openDoctor:'当前医生',//
										pharmacyCode:'',//扣库科室
										moNote2:rowData.remark!=null?rowData.remark:'',//备注 
										recUsernm:$('#recUserName').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室 
										updateUser:'',//停止人 
										baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
										permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
										hypotest:'',//皮试Id
										hypotestName:'',//皮试									
										itemType:rowData.ty,//药品非药品标识
										moStat:0,//医嘱状态
										sortId:sortIdCreate()//顺序号								
									}).datagrid('getRows').length-1;
									$('#infolistB').datagrid('selectRow',lastIndex);						
									$('#adWestMediModlDivId').dialog('close');
								}else{
									var lastIndex = $('#infolistB').datagrid('appendRow',{
										changeNo:1,
										id:'',
										itemCode:'',//特殊标识
										typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
										typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
										classCode:rowData.sysType,
										className:systemTypeMap[rowData.sysType],
										itemCode:rowData.itemCode,//医嘱Id
										itemName:rowData.name,//医嘱名称
										combNo:'',//组
										qtyTot:1,//总量
										priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
										drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
										packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
										minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
										doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:0,//每次量
										doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
										specs:rowData.specs!=null?rowData.specs:'',//规格
										doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
										drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
										drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
										itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
										useDays:0,//付数
										frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
										frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
										usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
										useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
										dateBgn:mytime,//开始时间 
										dateEnd:'',//停止时间
										moDate:mytime,//开立时间 
										docCode:'',//开立医生代码
										docName:$('#userName').val(),//开立医生
										execDpcd:$('#deptId').val(),//执行科室 id
										execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
										emcFlag:0,//加急标记
										isUrgent:0,//急
										labCode:'',//样本类型
										itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
										//openDoctor:'当前医生',//
										pharmacyCode:'',//扣库科室
										moNote2:rowData.remark!=null?rowData.remark:'',//备注 
										recUsernm:$('#recUserName').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室 
										updateUser:'',//停止人 
										baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
										permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
										hypotest:'',//皮试Id
										hypotestName:'',//皮试									
										itemType:rowData.ty,//药品非药品标识
										moStat:0,//医嘱状态
										sortId:sortIdCreate()//顺序号								
									}).datagrid('getRows').length-1;
									$('#infolistB').datagrid('selectRow',lastIndex);						
									$('#adWestMediModlDivId').dialog('close');
								}								
							}
						}
					}
				});			   
				bindEnterEvent('westMediInfoViewNameId',searchWestMediInfo,'easyui');
			});
			
			//药品、非药品信息模糊查询
			function searchWestMediInfo(){
				$('#westMediInfoViewListId').datagrid('load', {
					'inpatientOrder.itemName' : $('#westMediInfoViewNameId').val(),
					'inpatientOrder.classCode':$('#westMediInfoViewTypeId').val(),
					'inpatientOrder.className':$("#westMediInfoViewTypeNameId").val()
				});
			}

			//药品使用限制等级为乙类的，在列表中以绿色字体标识
			function gradeStyler(value,row,index){			
				if(row.drugGrade=='402880a5506094360150609b225a0005'){					
					return 'color:green;';									
				}					
			}	
			//系统类别 列表页 显示		
			function sysTypeFamater(value,row,index){			
				if(value!=null&&value!=""){
					return systemTypeMap[value];									
				}			
			}
			//单位 列表页 显示	
			function drugpackagingunitFamater(value,row,index){	
				if(value!=null&&value!=""){
					if(row.ty=='1'){					
						if(drugpackagingunitMap[value]!=null&&drugpackagingunitMap[value]!=""){
							return drugpackagingunitMap[value];
						}
						return value;
					}
					if(row.ty=='0'){
						if(nonmedicineencodingMap[value]!=null&&nonmedicineencodingMap[value]!=""){
							return nonmedicineencodingMap[value];
						}
						return value;
					}
				}			
			}
	/* 		//剂量单位 列表页 显示	
	 		function drugpdoseunitFamater(value,row,index){	
				if(value!=null&&value!=""){	
					if(drugdoseunitMap[value]!=null&&drugdoseunitMap[value]!=""){
						return drugdoseunitMap[value];
					}
					return value;
				}			
			}  */
			//医保标志
			function speidFamater(value,row,index){	
				var retVal="";				
				if(value!=null&&value!=""){	
					return druggradeMap[value];
				}
				if(row.undrugIsprovincelimit==1){
					retVal=retVal+'X';
				}
				if(row.undrugIscitylimit==1){
					retVal=retVal+'S';
				}
				if(row.undrugIsownexpense==1){
					retVal=retVal+'Z';
				}
				if(row.undrugIsspecificitems==1){
					retVal=retVal+'T';
				}
				return retVal;
			}
			//执行科室 列表页 显示		
			function implDepartmentFamater(value,row,index){			
				if(value!=null&&value!=""){					
					return implDepartmentMap[value];									
				}			
			}
			//疾病分类 列表页 显示		
			function diseasetypeFamater(value,row,index){			
				if(value!=null&&value!=""){
					for(var i=0;i<diseasetypeList.length;i++){
						if(value==diseasetypeList[i].id){
							return diseasetypeList[i].name;					
						}
					}
				}			
			}
			//药品库存 列表页 显示		
			function drugStorageFamater(value,row,index){			
				if(value!=null&&value!=""){
					if(drugStorageMap[value]<=row.lowSum){
						return "<span style='color:red;'>"+drugStorageMap[value]+"</span>";						
					}else{				
						return drugStorageMap[value];	
					}
				}			
			}
			//药品频次列表页 显示		
			function drugfrequencyFamater(value,row,index){			
				if(value!=null&&value!=""){
					return frequencyMap[value];					
				}			
			}			
			//药品用法列表页 显示		
			function drugusemodeFamater(value,row,index){			
				if(value!=null&&value!=""){
					return usemodeMap[value];					
				}			
			}
		</script>
	</body>
</html>
