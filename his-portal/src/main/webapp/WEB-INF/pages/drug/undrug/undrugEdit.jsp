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
		<form id="hospitalForm" action="" method="post" >
				<div  style="padding: 20px; ">
					<input type="hidden" id="id" name="id" value="${drugUndrug.id}">
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width: 100%;">
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>项目名称:</span>&nbsp;&nbsp;
								<input id="undrugPinyin" type="hidden" name="undrugPinyin" value="${drugUndrug.undrugPinyin}">
								<input id="undrugWb" type="hidden" name="undrugWb" value="${drugUndrug.undrugWb}">
							</td>
							<td style="width:220px">
								<input id="hospitalID" class="easyui-textbox" value="${drugUndrug.name}" data-options="required:true,missingMessage:'请填写名称!'"  name="name">
							</td>
							<td class="honry-lable" style="width:220px">
								<span>自定义码:</span>&nbsp;&nbsp;
							</td>
							<td style="width:220px" >
								<input class="easyui-textbox" value="${drugUndrug.undrugInputcode}" name="undrugInputcode"  ></input>

							</td>
						</tr>
						 <tr>
							<td class="honry-lable" style="width:220px">
								<span>国家编码:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox"  value="${drugUndrug.undrugGbcode}" name="undrugGbcode"  ></input>
							</td>
							
							<td class="honry-lable" style="width:220px">
								<span>国际编码:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugGjcode}" name="undrugGjcode"  ></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>系统类别:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="CodeSystemtype"  value="${drugUndrug.undrugSystype}" name="undrugSystype" data-options="required:true,missingMessage:'请填写系统类别!'" ></input>
								<a href="javascript:delSelectedData('CodeSystemtype');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>专科名称:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugSpecialtyname}" name="undrugSpecialtyname"  ></input>
							</td>
							
						</tr>
						 <tr>
							<td class="honry-lable" style="width:220px">
								<span>执行科室:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="undrugDeptHidden" type="hidden" name="undrugDept" value="${drugUndrug.undrugDept}">
								<input id="undrugDept" class="easyui-combotree" value="${undrugDeptId}"></input>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>项目约束:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugItemlimit}" name="undrugItemlimit"  ></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>注意事项:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugNotes}" name="undrugNotes"  ></input>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>项目范围:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugScope}" name="undrugScope"  ></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>检查要求:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugRequirements}" name="undrugRequirements"  ></input>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>最小费用:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="CodeDrugminimumcost"  value="${drugUndrug.undrugMinimumcost}"  name="undrugMinimumcost" data-options="required:true,missingMessage:'请填写最小费用!'" ></input>
								<a href="javascript:delSelectedData('CodeDrugminimumcost');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>检查部位或标本:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="CodeCheckpoint" value="${drugUndrug.undrugInspectionsite}" name="undrugInspectionsite"  ></input>
								<a href="javascript:delSelectedData('CodeCheckpoint');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>病史检查:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugMedicalhistory}" name="undrugMedicalhistory"  ></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>默认价:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-numberbox"  value="${drugUndrug.defaultprice}" name="defaultprice" data-options="required:true,min:0,precision:4,missingMessage:'请填写默认价!'" ></input>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>儿童价:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-numberbox" value="${drugUndrug.childrenprice}" name="childrenprice" data-options="min:0,precision:4" ></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>特诊价:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-numberbox" value="${drugUndrug.specialprice}" name="specialprice" data-options="min:0,precision:4"></input>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>特诊比例:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugEmergencycaserate}" name="undrugEmergencycaserate"  ></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>其他价1:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="hospitalID" class="easyui-numberbox" value="${drugUndrug.undrugOtherpricei}"   name="undrugOtherpricei" data-options="min:0,precision:4">
							</td>
							<td class="honry-lable" style="width:220px">
								<span>其他价2:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="hospitalID" class="easyui-numberbox" value="${drugUndrug.undrugOtherpriceii}"   name="undrugOtherpriceii" data-options="min:0,precision:4">
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>单位:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-combobox" id="CodeNonmedicineencoding" value="${drugUndrug.unit}" name="unit" data-options="required:true,missingMessage:'请填写单位!'" ></input>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>规格:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.spec}" name="spec"  ></input>

							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>申请单位名称:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox"  value="${drugUndrug.undrugApplication}" name="undrugApplication"  ></input>
							</td>	
							<td class="honry-lable" style="width:220px">
								<span>疾病分类:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="CodeDiseasetype"   value="${drugUndrug.undrugDiseaseclassification}"  name="undrugDiseaseclassification"  ></input>
								<a href="javascript:delSelectedData('CodeDiseasetype');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>手术编码:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugOperationcode}" name="undrugOperationcode"  ></input>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>手术分类:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugOperationtype}" name="undrugOperationtype"  ></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>设备编号:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="cost" class="easyui-textbox" value="${drugUndrug.undrugEquipmentno}" name="undrugEquipmentno"  ></input>
							</td>
							<td class="honry-lable" style="width:220px">
								<span>手术规模:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input class="easyui-textbox" value="${drugUndrug.undrugOperationscale}" name="undrugOperationscale"  ></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>状态:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="undrugState" class="easyui-combobox" 
										value="${drugUndrug.undrugState}"   name="undrugState" data-options="required:true,missingMessage:'请填写状态!'">
							</td>
							<td class="honry-lable" style="width:220px">
								<span>有效范围:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px">
								<input id="undrugValidityrange" class="easyui-combobox" value="${drugUndrug.undrugValidityrange}"   name="undrugValidityrange" data-options="valueField: 'label',textField: 'value',
																							data: [{label: 0,value: '全部'},{label: 1,value: '门诊'},{label: 2,value: '住院'}]">
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:220px">
								<span>非药品编码:</span>&nbsp;&nbsp;
							</td>
							<td style="width:220px"  >
								<input id="drugCode" class="easyui-textbox" value="${drugUndrug.code}" data-options="required:true,missingMessage:'格式:以大写  \'F\' 开头,最多输入50位!'"  name="code">
							</td>
							<td class="honry-lable" style="width:220px">
								<span>样本类型:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;width:220px" colspan="3">
								<input id="CodeLaboratorysample" value="${drugUndrug.undrugLabsample}" name="undrugLabsample"  ></input>
								<a href="javascript:delSelectedData('CodeLaboratorysample');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							
						</tr> 
						<tr>
			    			<td colspan="4" style="width:880px">
			    				<span>是否省限制:&nbsp;</span>
			    				<input type="hidden" id="undrugIsprovincelimith" name="undrugIsprovincelimit" value="${drugUndrug.undrugIsprovincelimit }"/>
				    			<input type="checkBox"  id="undrugIsprovincelimit" onclick="javascript:checkboxlimit()"/>
				    			&nbsp;&nbsp;
				    			<span>是否自费:&nbsp</span>
				    			<input type="hidden" id="undrugIsownexpenseh" name="undrugIsownexpense" value="${drugUndrug.undrugIsownexpense}"/>
				    			<input type="checkBox" id="undrugIsownexpense"  onclick="javascript:checkboxexpense()"/>
				    			&nbsp;&nbsp;
				    			<span>是否特定项目:&nbsp</span>
				    			<input type="hidden" id="undrugIsspecificitemsh" name="undrugIsspecificitems" value="${drugUndrug.undrugIsspecificitems}"/>
				    			<input type="checkBox"  id="undrugIsspecificitems" onclick="javascript:checkBoxSelect('undrugIsspecificitems',0,1)"/>
				    			&nbsp;&nbsp;&nbsp;&nbsp;
				    			<span>是否对照:&nbsp</span>
			    				<input type="hidden" id="undrugCrontrasth" name="undrugCrontrast" value="${drugUndrug.undrugCrontrast}"/>
				    			<input type="checkBox" id="undrugCrontrast" onclick="javascript:checkBoxSelect('undrugCrontrast',0,1)"/>
				    			&nbsp;&nbsp;
				    			<span>是否组套:&nbsp;</span>
				    			<input type="hidden" id="undrugIsstackh" name="undrugIsstack" value="${drugUndrug.undrugIsstack}"/>
				    			<input type="checkBox" id="undrugIsstack"  onclick="javascript:checkBoxSelect('undrugIsstack',0,1)"/>
				    			&nbsp;&nbsp;&nbsp;&nbsp;
				    			<br>
				    			<span>是否市限制:&nbsp;</span>
				    			<input type="hidden" id="undrugIscitylimith" name="undrugIscitylimit" value="${drugUndrug.undrugIscitylimit}"/>
				    			<input type="checkBox" id="undrugIscitylimit" onclick="javascript:checkboxiscity()"/>
				    			&nbsp;&nbsp;
				    			<span>是否确认:&nbsp</span>
				    			<input type="hidden" id="undrugIssubmith" name="undrugIssubmit" value="${drugUndrug.undrugIssubmit}"/>
				    			<input type="checkBox"  id="undrugIssubmit" onclick="javascript:checkBoxSelect('undrugIssubmit',0,1)"/>
				    			&nbsp;&nbsp;
			    				<span>是否计划生育:&nbsp</span>
				    			<input type="hidden" id="undrugIsbirthcontrolh" name="undrugIsbirthcontrol" value="${drugUndrug.undrugIsbirthcontrol}"/>
				    			<input type="checkBox"  id="undrugIsbirthcontrol" onclick="javascript:checkBoxSelect('undrugIsbirthcontrol',0,1)"/>
				    			&nbsp;&nbsp;&nbsp;&nbsp;
				    			<span>是否甲类:&nbsp</span>
				    			<input type="hidden" id="undrugIsah" name="undrugIsa" value="${drugUndrug.undrugIsa}"/>
				    			<input type="checkBox" id="undrugIsa" onclick="javascript:checkBoxSelect('undrugIsa',0,1)"/>
				    			&nbsp;&nbsp;
				    			<span>是否乙类:&nbsp;</span>
			    				<input type="hidden" id="undrugIsbh" name="undrugIsb" value="${drugUndrug.undrugIsb }"/>
				    			<input type="checkBox"  id="undrugIsb" onclick="javascript:checkBoxSelect('undrugIsb',0,1)"/>
				    			&nbsp;&nbsp;&nbsp;&nbsp;
				    			<br/>
				    			<span>是否丙类:&nbsp;&nbsp;</span>
				    			<input type="hidden" id="undrugIsch" name="undrugIsc" value="${drugUndrug.undrugIsc}"/>
				    			<input type="checkBox" id="undrugIsc" onclick="javascript:checkBoxSelect('undrugIsc',0,1)"/>
				    			&nbsp;&nbsp;
				    			<span>是否需要预约:&nbsp</span>
				    			<input type="hidden" id="undrugIspreorderh" name="undrugIspreorder" value="${drugUndrug.undrugIspreorder}"/>
				    			<input type="checkBox"  id="undrugIspreorder" onclick="javascript:checkBoxSelect('undrugIspreorder',0,1)"/>
				    			&nbsp;&nbsp;
				    			<span>是否知情同意书:&nbsp</span>
				    			<input type="hidden" id="undrugIsinformedconsenth" name="undrugIsinformedconsent" value="${drugUndrug.undrugIsinformedconsent}"/>
				    			<input type="checkBox" id="undrugIsinformedconsent" onclick="javascript:checkBoxSelect('undrugIsinformedconsent',0,1)"/>
			    				
			    			</td>
			   			</tr>
						<tr >
							<td class="honry-lable">
								<span>备注:</span>&nbsp;&nbsp;
							</td>
							<td style="text-align: left;" colspan="3">
								<input class="easyui-textbox" type="text" value="${drugUndrug.undrugRemark }" name="undrugRemark"  style="width: 500px"></input>
							</td>
						</tr> 
					</table>
					<div style="text-align: center; padding: 5px">
						<c:if test="${empty drugUndrug.id}">
							<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
						</c:if>
						<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">确定</a>
						<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clearForm()" class="easyui-linkbutton">清空</a>
						<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeDialog()" class="easyui-linkbutton">关闭</a>
					</div>
				</div>
		</form>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
		<script type="text/javascript">
			 $(function() {
				//系统类别
				$('#CodeSystemtype') .combobox({  
				 	url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=systemType", 
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,
				    editable:false,
				    onChange:function(post){
			    		$('#CodeSystemtype').combobox('reload', "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=systemType&post="+post);
			    	}
				    
				});
					//最小费用
				$('#CodeDrugminimumcost') .combobox({  
				 url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=drugMinimumcost", 
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,
				    filter: function(q, row){
                        var keys = new Array();
                        keys[keys.length] = 'encode';
                        keys[keys.length] = 'name';
                        keys[keys.length] = 'pinyin';
                        keys[keys.length] = 'wb';
                        keys[keys.length] = 'inputCode';
                        return filterLocalCombobox(q, row, keys);
               }
				    
				});
					//單位
				$('#CodeNonmedicineencoding').combobox({
					url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=nonmedicineencoding", 
				    valueField:'name',    
				    textField:'name',
				    multiple:false,
				    filter: function(q, row){
                        var keys = new Array();
                        keys[keys.length] = 'encode';
                        keys[keys.length] = 'name';
                        keys[keys.length] = 'pinyin';
                        keys[keys.length] = 'wb';
                        keys[keys.length] = 'inputCode';
                        return filterLocalCombobox(q, row, keys);
             	  }
				})
				
					//疾病分类
				$('#CodeDiseasetype') .combobox({  
				 url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=diseasetype", 
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,
				    filter: function(q, row){
                        var keys = new Array();
                        keys[keys.length] = 'encode';
                        keys[keys.length] = 'name';
                        keys[keys.length] = 'pinyin';
                        keys[keys.length] = 'wb';
                        keys[keys.length] = 'inputCode';
                        return filterLocalCombobox(q, row, keys);
               }
				});
				
					//状态
				$('#undrugState') .combobox({  
				 valueField:'id',
				 textField:'value',
				 data: [{
				 id: 1,value: '在用'
				 },{
				 id: 2,value: '停用'
				 },{
				 id: 3,value: '废弃'
				 }]
				    
				});
				//判断是否编辑状态，编辑状态修改复选框选中状态
				if($('#undrugIsprovincelimith').val()==1){
					$('#undrugIsprovincelimit').attr("checked", true); 
					$('#undrugIsownexpense').prop('disabled',true); 
			        $('#undrugIscitylimit').prop('disabled',true); 
				}
				if($('#undrugIscitylimith').val()==1){
					$('#undrugIscitylimit').attr("checked", true); 
					$('#undrugIsprovincelimit').prop('disabled',true); 
			        $('#undrugIsownexpense').prop('disabled',true);
				}
				if($('#undrugIsownexpenseh').val()==1){
					$('#undrugIsownexpense').attr("checked", true); 
					$('#undrugIsprovincelimit').prop('disabled',true); 
			        $('#undrugIscitylimit').prop('disabled',true);
				}
				if($('#undrugIssubmith').val()==1){
					$('#undrugIssubmit').attr("checked", true); 
				}
				if($('#undrugIspreorderh').val()==1){
					$('#undrugIspreorder').attr("checked", true); 
				}
				if($('#undrugIsbirthcontrolh').val()==1){
					$('#undrugIsbirthcontrol').attr("checked", true); 
				}
				if($('#undrugIsspecificitemsh').val()==1){
					$('#undrugIsspecificitems').attr("checked", true); 
				}
				if($('#undrugIsinformedconsenth').val()==1){
					$('#undrugIsinformedconsent').attr("checked", true); 
				}
				if($('#undrugCrontrasth').val()==1){
					$('#undrugCrontrast').attr("checked", true); 
				}
				if($('#undrugIsah').val()==1){
					$('#undrugIsa').attr("checked", true); 
				}
				if($('#undrugIsbh').val()==1){
					$('#undrugIsb').attr("checked", true); 
				}
				if($('#undrugIsch').val()==1){
					$('#undrugIsc').attr("checked", true); 
				}
				if($('#undrugIsstackh').val()==1){
					$('#undrugIsstack').attr("checked", true); 
				}
				//初始化下拉框
				$('#undrugDept').combotree({
				url:"<%=basePath%>baseinfo/department/treeDepartmen.action",
				valueField:'id',textField:'text',
				onBeforeSelect:function(node, checked){
						if(node.id=='1'){
							$('#undrugDeptHidden').val('');
							$('#undrugDept').combotree('clear');
							return false;
						}
						if(node.id!='1'&&node.attributes.hasson=='1'){
							$('#undrugDeptHidden').val('');
							$('#undrugDept').combotree('clear');
							return false;
						}
					},
					onSelect: function(node){
						$('#undrugDeptHidden').val(node.id);
					}
				});
				idCombobox("CodeCheckpoint","checkpoint");
				idCombobox("CodeLaboratorysample","laboratorysample");
				
				idCombobox("CodeSystemtype","systemType");
				idCombobox("CodeDrugminimumcost","drugMinimumcost");
				//给系统类型方法绑定弹窗事件
				bindEnterEvent('CodeSystemtype',popWinToSystemtype,'easyui');
				//给最小费用方法绑定弹窗事件
				bindEnterEvent('CodeDrugminimumcost',popWinToDrugminimumcost,'easyui');
				//给检查部位或标本方法绑定弹窗事件
				bindEnterEvent('CodeCheckpoint',popWinToCheckpoint,'easyui');
				//给样本类型方法绑定弹窗事件
				bindEnterEvent('CodeLaboratorysample',popWinToLaboratorysample,'easyui');
			});
			function onClickOKbtn() {
				var url;
				url = "<%=basePath%>drug/undrug/saveOrupdataDrugUndrug.action";
				$('#hospitalForm').form('submit', {
					url : url,
					data : $('#hospitalForm').serialize(),
					dataType : 'json',
					onSubmit : function() {
						if (!$('#hospitalForm').form('validate')) {
							$.messager.alert('提示',"验证没有通过,不能提交表单!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
						var code=$('#drugCode');
						if(code.val().length>50||code.val().length<2){
							$.messager.alert('提示','药品编码长度为2-50位');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条 
					},
					success : function(data) {
						if(data=="repeat"){
							$.messager.alert('提示','药品编码重复,请重新输入');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$.messager.progress('close');
							return;
						}
						$.messager.progress('close');
						$('#list').datagrid('reload', "<%=basePath%>drug/undrug/queryunDrug.action");
						closeDialog();
						$.messager.alert('提示','保存成功');
					},
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败');
					}
				});
			}
			/**
			 *
			 *连续添加
			 *
			 */
			function addContinue() {
				if (addAndEdit == 0) {
					$('#hospitalForm').form('submit', {
						url : "<%=basePath%>drug/undrug/saveOrupdataDrugUndrug.action",
						data : $('#hospitalForm').serialize(),
						dataType : 'json',
						onSubmit : function() {
							if (!$('#hospitalForm').form('validate')) {
								$.messager.alert('提示',"验证没有通过,不能提交表单!");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								return false;
							}
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
						},
						success : function(data) {
							$.messager.progress('close');
							$('#list').datagrid('reload', "<%=basePath%>drug/undrug/queryunDrug.action");
							$.messager.alert('提示信息',"操作成功!");
							//清除hospitalForm
							$('#hospitalForm').form('reset');
						},
						error : function(data) {
							$.messager.progress('close');
							$.messager.alert('提示信息',"操作失败!");
						}
					});
				} else {
					$.messager.alert('提示信息',"添加按钮不能执行修改操作!");
				}
			}
			//清除所填信息
			function clearForm() {
				$('#hospitalForm').form('clear');
			}
			//复选框
			function checkBoxSelect(id,defalVal,selVal){
				var hiddenId=id+"h";
				if($('#'+id).is(':checked')){
					$('#'+hiddenId).val(selVal);
				}else{
					$('#'+hiddenId).val(defalVal);
				}
			}
			
			
			//复选框是否省限制
			function checkboxlimit(){
				if($('#undrugIsprovincelimit').is(':checked')){
					$('#undrugIsprovincelimith').val(1);
					$('#undrugIsownexpense').prop('disabled',true); 
					$('#undrugIscitylimit').prop('disabled',true); 
				}else{
					$('#undrugIsprovincelimith').val(0);
					$('#undrugIsownexpense').prop('disabled',false); 
					$('#undrugIscitylimit').prop('disabled',false); 
				}
			}
			//是否自费
			function checkboxexpense(){
			if($('#undrugIsownexpense').is(':checked')){
			   $('#undrugIsownexpenseh').val(1);
		       $('#undrugIsprovincelimit').prop('disabled',true); 
			   $('#undrugIscitylimit').prop('disabled',true); 
			}else{
			   $('#undrugIsownexpenseh').val(0);
		       $('#undrugIsprovincelimit').prop('disabled',false); 
			   $('#undrugIscitylimit').prop('disabled',false); 
			}
			}
			//是否市
			function checkboxiscity(){
			if($('#undrugIscitylimit').is(':checked')){
			   $('#undrugIscitylimith').val(1);
		       $('#undrugIsprovincelimit').prop('disabled',true); 
			   $('#undrugIsownexpense').prop('disabled',true); 
			}else{
			   $('#undrugIscitylimith').val(0);
		       $('#undrugIsprovincelimit').prop('disabled',false); 
			   $('#undrugIsownexpense').prop('disabled',false); 
			}
			}
		
//从xml文件中解析，读到下拉框
	function idCombobox(param0,param1){
				$('#'+param0).combobox({
				    url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type="+param1,
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,
				    onLoadSuccess: function() {//请求成功后
						$(list).each(function(){
							  if ($('#'+param0).val() == this.Id) {
								  $('#'+param0).combobox("select",this.Id);
							   }
						});
					},
					filter: function(q, row){
                        var keys = new Array();
                        keys[keys.length] = 'encode';
                        keys[keys.length] = 'name';
                        keys[keys.length] = 'pinyin';
                        keys[keys.length] = 'wb';
                        keys[keys.length] = 'inputCode';
                        return filterLocalCombobox(q, row, keys);
               }
				});
			}
	 /**
	  * 疾病分类回车弹出事件高丽恒
	  * 2016-03-22 14:41
	  */
	 bindEnterEvent('CodeDiseasetype',popWinToCodeDiseasetype,'easyui');
	function popWinToCodeDiseasetype(){
		var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?textId=CodeDiseasetype&type=diseasetype";
		var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	
	
	
	/**
	 * 打开系统类别界面弹框
	 * @author  zhuxiaolu
	 * @date 2015-5-25 10:53
	 * @version 1.0
	 */
	
	function popWinToSystemtype(){
		
		var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?textId=CodeSystemtype&type=systemType";
		var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
				
	}

	/**
	 * 打开最小费用界面弹框
	 * @author  zhuxiaolu
	 * @date 2015-5-25 10:53
	 * @version 1.0
	 */
	
	function popWinToDrugminimumcost(){
		
		var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=drugMinimumcost&textId=CodeDrugminimumcost";
		var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
				
	}
	
	/**
	 * 打开检查部位或标本界面弹框
	 * @author  zhuxiaolu
	 * @date 2015-5-25 10:53
	 * @version 1.0
	 */
	
	function popWinToCheckpoint(){
		
		var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=checkpoint&textId=CodeCheckpoint";
		var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
				
	}
		
		
	/**
	 * 打开样本类型界面弹框
	 * @author  zhuxiaolu
	 * @date 2015-5-25 10:53
	 * @version 1.0
	 */
	
	function popWinToLaboratorysample(){
		
		var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=laboratorysample&textId=CodeLaboratorysample";
		var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
				
	}

	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>
