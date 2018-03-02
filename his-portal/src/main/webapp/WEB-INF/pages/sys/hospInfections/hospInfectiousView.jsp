<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath%>/baseinfo/pubCodeMaintain/queryDictionary.action",
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
	<div id="listeidt" class="easyui-panel" style="height: 100%;padding: 5px 5px 0px 25px">
				<form id="infEditForm" method="post">
				<div>
					<font style="padding-left: 45%;font-size: 28px" class="title">医院感染报告卡</font>
				</div>
				<div  style=" padding: 3px 3px;" class="sub_title">
					&nbsp;<font style="font-size: 14px">卡片编号：</font>
					<input class="easyui-textbox" name="infectious.report_no" value="${infectious.report_no}" readonly="true">
					<font style="margin-left: 3%;font-size: 14px">报卡类别：</font>
					<input id="reportType" class="easyui-textbox" value="${infectious.report_type}" readonly="true" />
					<input id="reportTypes" name="infectious.report_type" value="${infectious.report_type}" type="hidden"/>
					<font style="font-size: 14px;margin-left: 3%;">病历号：</font>
					<input class="inputCss" name="infectious.medicalrecord_id" id="medicalrecord" value="${infectious.medicalrecord_id}" readonly="true" >
					<br>
				</div>
					<input type="hidden" id="id" name="infectious.id" value="${infectious.id }">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:100%;">
						<tr>
							<td class="honry-lable"  style="width:150px">
							患者姓名：
							</td>
							<td style="width:150px">
								<input class="easyui-textbox" id="patientNameId"
								style="width:200px"	value="${infectious.patient_name}" readonly="true"/>
							</td>
							<td  class="honry-lable">
								患者家长姓名：
							</td>
							<td>
								<input class="easyui-textbox" id="patientParents"
								style="width:200px"	value="${infectious.patient_parents}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								证件类型：
							</td>
							<td>
								<input class="easyui-combobox" id="CodeCertificate" 
								style="width:200px"	value="${infectious.certificates_type}" readonly="true"/>
							</td>
							<td class="honry-lable">
								证件号码：
							</td>
							<td>
								<input class="easyui-textbox" id="certificatesNo"
									style="width:200px" value="${infectious.certificates_no}" readonly="true"/>
							</td>
						</tr>
						<tr>
						    <td class="honry-lable">
								性别：
							</td>
							<td>
								<input type="hidden" id="sexHid" value="${infectious.report_sex }" >
								<input class="easyui-textbox" style="width:200px" id="CodeSex" readonly="true"/>
							</td>
							<td  class="honry-lable">
								联系电话：
							</td>
							<td>
								<input id="telephone"class="easyui-textbox" readonly="true" 
								style="width:200px"	value="${infectious.telephone}"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								患者职业：
							</td>
							<td>
								<input id="patientProfession" class="easyui-textbox"  readonly="true" 
								style="width:200px" value="${infectious.patient_profession}"  />
							</td>
							<td class="honry-lable">
								其他职业：
							</td>
							<td>
								<input class="easyui-textbox" id="otherProfession" readonly="true"
									value="${infectious.other_profession}" style="width:200px" />
							</td>
						</tr>
						<tr>
						   <td class="honry-lable">
								工作单位：
							</td>
							<td>
								<input class="easyui-textbox" id="workPlace" readonly="true"
									style="width:200px" value="${infectious.work_place}" />
							</td>
							<td  class="honry-lable">
								病人来源：
							</td>
							<td>
							<input id="CodeBrlydq" readonly="true"
								style="width:200px"value="${infectious.home_area}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								出生日期：
							</td>
							<td colspan="3">
								<input id="reportBirthday" readonly="true" value="${infectious.report_birthday}" class="Wdate" type="text" style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<font style="font-size: 12px" class="birth">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如出生日期不详，实足年龄：</font>
								<input class="easyui-numberbox" id="reportAge" readonly="true"
									value="${infectious.report_age}" style="width: 50px"/>
									<font style="font-size: 12px" class="birth">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年龄单位：</font>
									<input id="agedw" value="${infectious.report_ageunit}" readonly="true"/>
									<font style="font-size: 14px;color: red" class="birthTip">&nbsp;&nbsp;&nbsp;出生日期，实足年龄必填其中一个!</font>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								现住址：
							</td>
							<td  colspan="3">
							<div style="text-align: 25px; height: 25px;">
								<input id="patientCitys" value="${infectious.home_couty}" type="hidden"/>
								<input   id="homeones" value="${oneCode }" style="width: 130px" readonly="true" data-options="prompt:'省/直辖市'"/>
								<input  id="hometwos" value="${twoCode }" style="width: 130px" readonly="true" data-options="prompt:'市'"/>
								<input   id="homethrees" value="${threeCode }" style="width: 130px" readonly="true" data-options="prompt:'县'"/>
								<input  id="homefours" value="${fourCode }" style="width: 130px" readonly="true" data-options="prompt:'区'"/>
							</div>
							<div style="margin-top:5px;text-align: 25px; height: 25px;">
								<input class="easyui-textbox" id="homeTown" readonly="true" value="${infectious.home_town}"style="width:200px"  />
							<span>乡（镇,乡）</span>
								<input  class="easyui-textbox" id="homeAddress"  readonly="true" value="${infectious.home_address}"style="width:200px"  />
							<span>村（门牌号）</span>
							</div>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								入院日期：
							</td>
							<td>
								<input id="into_day" readonly="true" value="${infectious.into_day}" class="Wdate" type="text" style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="honry-lable">
								床号：
							</td>
							<td>
								<input class="easyui-textbox" id="ded_no" readonly="true"
								style="width:200px"value="${infectious.ded_no}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								入院诊断：
							</td>
							<td colspan="6">
								<textarea class="easyui-textbox" readonly="true" style="width: 100%;height: 50px;font-size: 14px">${infectious.intodiagnosis }</textarea>
							</td>
						</tr>	
						
						<tr>
							<td class="honry-lable">
								感染日期：
							</td>
							<td>
								<input id="infection_day" readonly="true" value="${infectious.infection_day}" class="Wdate" type="text" style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="honry-lable">
								感染部位：
							</td>
							<td colspan="6">
								<input id="infectionsite" readonly="true" value="${infectious.infectionsite}" class="easyui-textbox"  style="width:200px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								感染诊断：
							</td>
							<td colspan="6">
								<textarea class="easyui-textbox" readonly="true" style="width: 100%;height: 50px;font-size: 14px">${infectious.infectiondiagnosis }</textarea>
							</td>
						</tr>	
						<tr>
							<td class="honry-lable">
								感染预后：
							</td>
							<td>
								<input style="width:200px" id="afterinfection" readonly="true" value="${infectious.afterinfection}"/>
							</td>
							<td class="honry-lable">
								易感因素：
							</td>
							<td colspan="4">
								<input style="width:200px" id="dactor" readonly="true" value="${infectious.dactor}"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								手术名称：
							</td>
							<td>
								<input id="itemName0_" readonly="true" class="easyui-combogrid" value="${infectious.operation_name}"  style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								手术日期：
							</td>
							<td>
								<input id="operation_day" readonly="true" value="${infectious.operation_day}" class="Wdate" type="text" style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="honry-lable">
								切口类型：
							</td>
							<td colspan="4">
								<input style="width:200px" id="incisiontype" readonly="true" value="${infectious.incisiontype}"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								病原学检查：
							</td>
							<td colspan="1">
								<input style="width:200px" id="etiological" readonly="true" value="${infectious.etiological}"/>
							</td>
							<td class="honry-lable">
								标本名称：
							</td>
							<td colspan="4">
								<input id="specimen" readonly="true" value="${infectious.specimen}" class="easyui-textbox" style="width:200px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								病原体：
							</td>
							<td colspan="6">
								<textarea class="easyui-textbox" readonly="true" style="width: 100%;height: 50px;font-size: 14px">${infectious.pathogen }</textarea>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								抗菌药物应用情况：
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								药物名称：
							</td>
							<td colspan="1">
								<input class="easyui-textbox"  id="medicine_name" readonly="true" value="${infectious.medicine_name}"
								style="width:200px"/>
							</td>
							<td class="honry-lable">
								剂量：
							</td>
							<td colspan="4">
								<input class="easyui-textbox"  id="medicine_dose" readonly="true" value="${infectious.medicine_dose}"
								style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								给药方式：
							</td>
							<td colspan="1">
								<input class="easyui-textbox" id="medicine_mode" readonly="true" value="${infectious.medicine_mode}" style="width:200px;"/>
							</td>
							<td class="honry-lable" width="140px">
								用药频数：
							</td>
							<td colspan="3">
								<input class="easyui-textbox" id="medicine_frequency" readonly="true" value="${infectious.medicine_frequency}"
								style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								应用时间：
							</td>
							<td>
								<input id="medicine_begin_day" readonly="true" value="${infectious.medicine_begin_day}" class="Wdate" type="text"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							至
								<input id="medicine_end_day" readonly="true" value="${infectious.medicine_end_day}" class="Wdate" type="text" style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						<tr>
						<tr>
							<td class="honry-lable">
								备注：
							</td>
							<td colspan="6">
							<textarea class="easyui-textbox" readonly="true" style="width: 100%;height: 50px;font-size: 12px">${infectious.remarks}</textarea>
							</td>
						</tr>
				</table>
			</form>	
		<div style="text-align: center; padding: 5px">

			<a href="javascript:closeLayout();" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancel'">关闭</a> 
<!-- 			<a href="javascript:printLayout();" class="easyui-linkbutton" -->
<!-- 				data-options="iconCls:'icon-save'" id="print">打印</a> -->
		</div>
	</div>
	<script>
	var selsctage="";//选择的出生日期时算出年龄
	$(function(){
		//初始化下拉框
		queryDistrictSJLDOne();
		queryDistrictSJLDTwo('');
		queryDistrictSJLDThree('');
		queryDistrictSJLDFour('',false);
		
		//如果是修改页面,进入的时候,让三级联动内每个输入框都可以下拉出值
		var oneCode=$('#homeones').combobox('getValue');
		queryDistrictSJLDTwo(oneCode);
		var twoCode=$('#hometwos').combobox('getValue');
		queryDistrictSJLDThree(twoCode);
		var threeCode=$('#homethrees').combobox('getValue');
		var fourCode=$('#homefours').combobox('getValue');
		if(fourCode==null||fourCode==""){
			$('#homefours').combobox({
				disabled:true
			});
		}else{
			queryDistrictSJLDFour(threeCode,true);
		}
	})
	
		//证件类型
		$('#CodeCertificate') .combobox({ 
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>",
		    valueField:'encode',    
		    textField:'name',
		    multiple:false,
		    editable:false,
		    onChange:function(post){
	    		$('#CodeCertificate').combobox('reload', "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>");
	    	}
		    
		});
		
	
		//病人来源 brlydq
		$('#CodeBrlydq') .combobox({ 
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=brlydq'/>",     
		    valueField:'encode',    
		    textField:'name',
		    multiple:false,
		    editable:false,
		});
		
		//患者职业
		$('#patientProfession') .combobox({ 
			url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=occupation'/>" ,   
		    valueField:'encode',    
		    textField:'name'
		});
			//适用性别
		$('#CodeSex').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			    valueField:'encode',    
			    textField:'name',
			    editable:false
			});
			var sexHid = $('#sexHid').val();
			if(sexHid!=null&&sexHid!=""){
				$('#CodeSex').combobox('setValue',sexHid);
			}
		
				/**
				 * 下拉框
				 * @author liuhl
				 * @date 2015-6-18
				 *
				 */
				$('#reportType').combotree({
						data:[{"id":1,"text":"初次报告"},{"id":2,"text":"订正报告","iconCls":"icon-book","state":"open","children":[{"id":3,"text":"变更诊断"},{"id":4,"text":"死亡"},{	"id":5,"text":"填卡错误"}]}],
						width:200,
						multiple:false,
						onChange: function(node){
			    			var texts=$('#reportType').combotree('getValue');
							document.getElementById("reportTypes").value=texts;
					}
				});
				$('#agedw').combotree({
						data:[{"id":1,"text":"岁"},{"id":2,"text":"月"},{"id":3,"text":"日"}],
						multiple:false,
						width:80
				});

				
				$('#patientProfession').combobox({
					 	url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=occupation'/>",  
						valueField:'encode',    
			    		textField:'name',
						width:200,
						onChange: function(node){
							var texts=$('#patientProfession').combobox('getText');
						if (texts=="其他") {
							$('#otherProfession').textbox({ 
							}); 
						}else{
							$('#otherProfession').textbox({ 
							}); 
							$('#otherProfession').textbox('setValue','');
						}
					},
					onLoadSuccess: function(node){
						var texts=$('#patientProfession').combobox('getText');
					if (texts=="其他") {
						$('#otherProfession').textbox({ 
						}); 
					}else{
						$('#otherProfession').textbox({ 
						}); 
					}
				}
				});
				if($('#patientProfession').combobox('getText')=="其他"){
			         $('#otherProfession').textbox({ 
							}); 
			   
			   }else{
			            $('#otherProfession').textbox({ 
							}); 
			   }

	function closeLayout() {
		window.close();
	}
// 	function printLayout(){
// 		var reportid = $("#id").val();//门诊号
// 		 var timerStr = Math.random();
<%-- 		  window.open ("<%=basePath%>publics/infectious/iReportToInfectiousDiseaseReport.action?randomId="+timerStr+"&REPORT_ID="+reportid+"&fileName=CRBBGK",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');  --%>
// 	}
/******** 三级联动 ************/
function queryDistrictSJLDOne() {
		$('#homeones').combobox({  
			url: "<c:url value='/baseinfo/district/queryDistrictTreeOne.action'/>?parId=",
			valueField:'cityCode',    
		    textField:'cityName',
		    multiple:false,
		    editable:true,
		    onSelect:function(node) {
				queryDistrictSJLDTwo(node.cityCode);
				$('#hometwos').combobox('setValue','');
				$('#homethrees').combobox('setValue','');
				$('#homefours').combobox('setValue','');
	        },onHidePanel:function(none){
	            var data = $(this).combobox('getData');
	            var val = $(this).combobox('getValue');
	            var result = true;
	            for (var i = 0; i < data.length; i++) {
	                if (val == data[i].cityCode) {
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
	            keys[keys.length] = 'cityCode';
	            keys[keys.length] = 'cityName';
	            keys[keys.length] = 'pinyin';
	            keys[keys.length] = 'wb';
	            keys[keys.length] = 'inputCode';
	            return filterLocalCombobox(q, row, keys);
	        }
		});
	}
function queryDistrictSJLDTwo(id) {
	if(id===''){
		$('#hometwos').combobox({
			data:[]
		})
		return;
	}
	$('#hometwos').combobox({  
		url: "<c:url value='/baseinfo/district/queryDistrictTreeTwo.action'/>?parId="+id,    
			valueField:'cityCode',    
		    textField:'cityName',
		    required:true,
		    multiple:false,
		    editable:true,
		    onSelect:function(node) {
	        	queryDistrictSJLDThree(node.cityCode);
	        	$('#homethrees').combobox('setValue','');
	        	$('#homefours').combobox('setValue','');
	        },onHidePanel:function(none){
	            var data = $(this).combobox('getData');
	            var val = $(this).combobox('getValue');
	            var result = true;
	            for (var i = 0; i < data.length; i++) {
	                if (val == data[i].cityCode) {
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
	            keys[keys.length] = 'cityCode';
	            keys[keys.length] = 'cityName';
	            keys[keys.length] = 'pinyin';
	            keys[keys.length] = 'wb';
	            keys[keys.length] = 'inputCode';
	            return filterLocalCombobox(q, row, keys);
	        }
	});
}
function queryDistrictSJLDThree(id) {
	if(id===''){
		$('#homethrees').combobox({
			data:[]
		})
		return;
	}
		$('#homethrees').combobox({  
			url: "<c:url value='/baseinfo/district/queryDistrictTreeThree.action'/>?parId="+id,    
				valueField:'cityCode',    
			    textField:'cityName',
			    required:true,
			    multiple:false,
			    editable:true,
			     onSelect:function(node) {
			     	var bool = false;
			     	if(node.cityName=="市辖区"){
			     		bool=true;
			     		$('#homefours').combobox({
							required: true,
							value:'',
							disabled:false
						});
			     	}else{
			     		$('#patientCitys').val(node.cityCode);
			     		$('#homefours').combobox({
							required: false,
							value:'',
							disabled:true
						});
			     	}				     	
		        	queryDistrictSJLDFour(node.cityCode,bool);
		        },onHidePanel:function(none){
		            var data = $(this).combobox('getData');
		            var val = $(this).combobox('getValue');
		            var result = true;
		            for (var i = 0; i < data.length; i++) {
		                if (val == data[i].cityCode) {
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
		            keys[keys.length] = 'cityCode';
		            keys[keys.length] = 'cityName';
		            keys[keys.length] = 'pinyin';
		            keys[keys.length] = 'wb';
		            keys[keys.length] = 'inputCode';
		            return filterLocalCombobox(q, row, keys);
		        }
		});
}
function queryDistrictSJLDFour(id,bool) {
	if(id===''){
		$('#homefours').combobox({
			data:[]
		})
		return;
	}

		$('#homefours').combobox({  
			url: "<c:url value='/baseinfo/district/queryDistrictTreeFour.action'/>?parId="+id,    
			valueField:'cityCode',    
			    textField:'cityName',
			    required:bool,   
			    multiple:false,
			    editable:true,
			    onSelect:function(node) {
			    	$('#patientCitys').val(node.cityCode);
			    },onHidePanel:function(none){
		            var data = $(this).combobox('getData');
		            var val = $(this).combobox('getValue');
		            var result = true;
		            for (var i = 0; i < data.length; i++) {
		                if (val == data[i].cityCode) {
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
		            keys[keys.length] = 'cityCode';
		            keys[keys.length] = 'cityName';
		            keys[keys.length] = 'pinyin';
		            keys[keys.length] = 'wb';
		            keys[keys.length] = 'inputCode';
		            return filterLocalCombobox(q, row, keys);
		        }
		});
}
/**
* 回车弹出地址一级选择窗口
* @author  zhuxiaolu
* @param textId 页面上commbox的的id
* @date 2016-03-22 14:30   
* @version 1.0
*/

function popWinToDistrict(){
	$('#hometwos').textbox('setValue','');
	$('#homethrees').textbox('setValue','');
	$('#homefours').textbox('setValue','');
	var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homeones&level=1&parentId=1";
	window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 

+',scrollbars,resizable=yes,toolbar=yes')
}
/**
* 回车弹出地址二级选择窗口
* @author  zhuxiaolu
* @param textId 页面上commbox的的id
* @date 2016-03-22 14:30   
* @version 1.0
*/

function popWinToDistrictTwos(){
	$('#homethrees').textbox('setValue','');
	$('#homefours').textbox('setValue','');
	var parentId=$('#homeones').textbox('getValue');
	if(!parentId){
		$.messager.alert('提示','请选择省/直辖市');  
		close_alert();
		
	}else{
		var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=hometwos&level=2&parentId="+parentId;
		window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
		+',scrollbars,resizable=yes,toolbar=yes')
	}
}
/**
* 回车弹出地址三级选择窗口
* @author  zhuxiaolu
* @param textId 页面上commbox的的id
* @date 2016-03-22 14:30   
* @version 1.0
*/

function popWinToDistrictThrees(){
	$('#homefours').textbox('setValue','');
	var parentId=$('#hometwos').textbox('getValue');
	if(!parentId){
		$.messager.alert('提示','请选择市');  
		close_alert();
		
	}else{
		var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homethrees&level=3&parentId="+parentId;
		window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
		+',scrollbars,resizable=yes,toolbar=yes')
	}
}
/**
* 回车弹出地址四级选择窗口
* @author  zhuxiaolu
* @param textId 页面上commbox的的id
* @date 2016-03-22 14:30   
* @version 1.0
*/

function popWinToDistrictFours(){
	var parentId=$('#homethrees').textbox('getValue');
	if(!parentId){
		$.messager.alert('提示','请选择县');  
		close_alert();
		
	}else{
		var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homefours&level=4&parentId="+parentId;
		window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
		+',scrollbars,resizable=yes,toolbar=yes')
	}
}

/**
 * 手术名称
 */
$('#itemName0_').combogrid({
	 url : '<%=basePath%>operation/operationList/undrugComboboxfy.action',
	    idField:'code',    
	    textField:'name',
	    mode:"remote",
		pageList:[10,20,30,40,50],
	 	pageSize:"10",
	 	panelWidth:325,
	 	pagination:true,
		columns:[[    
		         {field:'code',title:'编码',width:'120'},    
		         {field:'name',title:'名称',width:'160'}    
		     ]],  
		 onHidePanel:function(none){
		 	var val = $(this).combogrid('getText');
	    	//校验手术名称是否存在
		 	if(nssmcName1(val)){
	    		$(this).combogrid('clear');
		    	$.messager.alert("提示","手术名称不能重复！","info");
		    	setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
		    }
	    	//判断该手术是否为自定义手术
	    	var mc = $('[id^=itemName]');
	 	    var b=0;
	 	    var c=0;
		 	mc.each(function(index,obj){
		 		if($(obj).combogrid('getText') != $(obj).combogrid('getValue')&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
		 			b++;
		 		}
		 		if($(obj).combogrid('getText') == $(obj).combogrid('getValue')&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
		 			c++;
		 		}
		 	});
		 	var value = $(this).combogrid('getValue');
	 	     if(val==value&&value!=null&&value!=""){
	 	    	 if(b>0){
	 	    		$.messager.alert("提示","该手术为非自定义手术，请选择下拉列表里的数据！","info");
	 	    		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
	 	    		$(this).combogrid('clear');
	 	    	 }
	 	     }else{
	 	    	if(c>0){
	 	    		$.messager.alert("提示","该手术为自定义手术，请输入下拉列表不存在的数据！","info");
	 	    		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
	 	    		$(this).combogrid('clear');
	 	    	 }
	 	     }
	    	
	 	},
	    onLoadSuccess: function () {
	    		//翻页渲染
	    	    var id=$(this).prop("id");
	            if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
            }
	     //分页工具栏作用提示
			var p = $(this).combogrid('grid');
			var pager = p.datagrid('getPager');
			var aArr = $(pager).find('a');
			var iArr = $(pager).find('input');
			$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
			for(var i=0;i<aArr.length;i++){
				$(aArr[i]).tooltip({
					content:toolArr[i],
					hideDelay:1
				});
				$(aArr[i]).tooltip('hide');
			}

			
        }  
});
	//感染预后
	$('#afterinfection').combobox({
		valueField:'encode',    
	    textField:'name',
	    editable:false,
	    data: [{
	    	encode: '0',
	    	name: '治愈'
		},{
			encode: '1',
			name: '好转'
		},{
			encode: '2',
			name: '未愈'
		},{
			encode: '3',
			name: '死亡'
		}]
	});
	//病原学检查
	$('#etiological').combobox({
		valueField:'encode',    
	    textField:'name',
	    editable:false,
	    data: [{
	    	encode: '0',
	    	name: '是'
		},{
			encode: '1',
			name: '否'
		}]
	});
	//易感因素
	$('#dactor').combobox({
		valueField:'encode',    
	    textField:'name',
	    editable:false,
	    data: [{encode: '0',name: '糖尿病'},
	           {encode: '1',name: '抗生素'},
	           {encode: '2',name: '泌尿道插管'},
	           {encode: '3',name: '动静脉插管'},
	           {encode: '4',name: '肝硬化'},
	           {encode: '5',name: '慢性病'},
	           {encode: '6',name: '放疗'},
	           {encode: '7',name: '化疗'},
	           {encode: '8',name: '肥胖'},
	           {encode: '9',name: '人工装置'},
	           {encode: '10',name: '人工装置'},
	           {encode: '11',name: '肿瘤'},
	           {encode: '12',name: '引流管口'},
	           {encode: '13',name: '营养不良'},
	           {encode: '14',name: 'WBC计数<1.5*10^9/L'},
	           {encode: '15',name: '其他'}
	    ]
	});
	//切口类型
	$('#incisiontype').combobox({
		valueField:'encode',    
	    textField:'name',
	    editable:false,
	    data: [{
	    	encode: '0',
	    	name: 'I'
		},{
			encode: '1',
			name: 'II'
		},{
			encode: '2',
			name: 'III'
		}]
	});	  
	</script>
</body>
</html>