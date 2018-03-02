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
	<div id="listeidt" class="easyui-panel" style="height:auto;padding: 5px 5px 0px 25px">
			<form id="infEditForm" method="post">
				<div>
					<font style="padding-left: 45%;font-size: 28px" class="title">医院感染报告卡</font>
				</div>
				<div  style=" padding: 3px 3px;" class="sub_title">
					&nbsp;<font style="font-size: 14px">卡片编号：</font>
					<input class="easyui-textbox" name="infectious.report_no" value="${infectious.report_no}">
					<font style="margin-left: 3%;font-size: 14px">报卡类别：</font>
					<input id="reportType" value="${infectious.report_type}"  />
					<input id="reportTypes" name="infectious.report_type" value="${infectious.report_type}" type="hidden"/>
					<font style="font-size: 14px;margin-left: 3%;">病历号：</font>
					<input class="inputCss" name="infectious.medicalrecord_id" id="medicalrecord" value="${infectious.medicalrecord_id}" >
					<a href="javascript:void(0)" onclick="searchFromcx()"
										class="easyui-linkbutton" iconCls="icon-search">查询</a><br>
				</div>
					<input type="hidden" id="id" name="infectious.id" value="${infectious.id }">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:100%;">
						<tr>
							<td class="honry-lable"  style="width:150px">
							患者姓名：
							</td>
							<td style="width:150px">
								<input class="easyui-textbox" name="infectious.patient_name" id="patientNameId"
								style="width:200px"	value="${infectious.patient_name}"
									/>
							</td>
							<td  class="honry-lable">
								患者家长姓名：
							</td>
							<td>
								<input class="easyui-textbox" name="infectious.patient_parents" id="patientParents"
								style="width:200px"	value="${infectious.patient_parents}"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								证件类型：
							</td>
							<td>
								<input class="easyui-combobox" name="infectious.certificates_type" id="CodeCertificate" 
								style="width:200px"	value="${infectious.certificates_type}" />
							</td>
							<td class="honry-lable">
								证件号码：
							</td>
							<td>
								<input class="easyui-textbox" name="infectious.certificates_no"  id="certificatesNo"
									style="width:200px" value="${infectious.certificates_no}"/>
							</td>
						</tr>
						<tr>
						    <td class="honry-lable">
								性别：
							</td>
							<td>
								<input type="hidden" id="sexHid" value="${infectious.report_sex }" >
								<input style="width:200px" id="CodeSex" name="infectious.report_sex"/>
							</td>
							<td  class="honry-lable">
								联系电话：
							</td>
							<td>
								<input id="telephone"class="easyui-textbox" name="infectious.telephone" 
								style="width:200px"	value="${infectious.telephone}"data-options="validType:'phoneRex',required:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								患者职业：
							</td>
							<td>
								<input id="patientProfession"   name="infectious.patient_profession" 
								style="width:200px" value="${infectious.patient_profession}"  />
								<a href="javascript:delSelectedData('patientProfession');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="honry-lable">
								其他职业：
							</td>
							<td>
								<input class="easyui-textbox" id="otherProfession" name="infectious.other_profession" 
									value="${infectious.other_profession}" style="width:200px" disabled="disabled" />
							</td>
						</tr>
						<tr>
						   <td class="honry-lable">
								工作单位：
							</td>
							<td>
								<input class="easyui-textbox" name="infectious.work_place"  id="workPlace"
									style="width:200px" value="${infectious.work_place}" data-options="disabled:${save }" />
							</td>
							<td  class="honry-lable">
								病人来源：
							</td>
							<td>
							<input id="CodeBrlydq" name="infectious.home_area" 
								style="width:200px"value="${infectious.home_area}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								出生日期：
							</td>
							<td colspan="3">
								<input id="reportBirthday" name="infectious.report_birthday" value="${infectious.report_birthday}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:changeAge})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<font style="font-size: 12px" class="birth">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如出生日期不详，实足年龄：</font>
								<input class="easyui-numberbox" name="infectious.report_age"  id="reportAge"
									value="${infectious.report_age}" style="width: 50px"  data-options="min:0,max:120,precision:0"/>
									<font style="font-size: 12px" class="birth">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年龄单位：</font>
									<input id="agedw" name="infectious.report_ageunit" value="${infectious.report_ageunit}" />
									<font style="font-size: 14px;color: red" class="birthTip">&nbsp;&nbsp;&nbsp;出生日期，实足年龄必填其中一个!</font>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								现住址：
							</td>
							<td  colspan="3">
							<div style="text-align: 25px; height: 25px;">
								<input id="patientCitys" name="infectious.home_couty" value="${infectious.home_couty}" type="hidden"/>
								<input   id="homeones" value="${oneCode }" style="width: 130px"  data-options="prompt:'省/直辖市',disabled:${save }"/>
								<input  id="hometwos" value="${twoCode }" style="width: 130px"  data-options="prompt:'市',disabled:${save }"/>
								<input   id="homethrees" value="${threeCode }" style="width: 130px"  data-options="prompt:'县',disabled:${save }"/>
								<input  id="homefours" value="${fourCode }" style="width: 130px"  data-options="prompt:'区',disabled:${save }"/>
							</div>
							<div style="margin-top:5px;text-align: 25px; height: 25px;">
								<input class="easyui-textbox" id="homeTown"   name="infectious.home_town" data-options="disabled:${save }" value="${infectious.home_town}"style="width:200px"  />
							<span>乡（镇,乡）</span>
								<input  class="easyui-textbox" id="homeAddress"   data-options="disabled:${save }" name="infectious.home_address" value="${infectious.home_address}"style="width:200px"  />
							<span>村（门牌号）</span>
							</div>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								入院日期：
							</td>
							<td>
								<input id="into_day" name="infectious.into_day" value="${infectious.into_day}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+10}-%M-%d %H:%m:%s'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="honry-lable">
								床号：
							</td>
							<td>
								<input class="easyui-textbox" id="ded_no" name="infectious.ded_no" 
								style="width:200px"value="${infectious.ded_no}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								入院诊断：
							</td>
							<td colspan="6">
								<textarea class="easyui-textbox" data-options="multiline:'true'" style="width: 100%;height: 50px;font-size: 14px"  
									name="infectious.intodiagnosis"  >${infectious.intodiagnosis }</textarea>
							</td>
						</tr>	
						
						<tr>
							<td class="honry-lable">
								感染日期：
							</td>
							<td>
								<input id="infection_day" name="infectious.infection_day" value="${infectious.infection_day}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+10}-%M-%d %H:%m:%s'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="honry-lable">
								感染部位：
							</td>
							<td colspan="6">
								<input id="infectionsite" name="infectious.infectionsite" value="${infectious.infectionsite}" class="easyui-textbox"  style="width:200px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								感染诊断：
							</td>
							<td colspan="6">
								<textarea class="easyui-textbox" data-options="multiline:'true'" style="width: 100%;height: 50px;font-size: 14px"  
									name="infectious.infectiondiagnosis"  >${infectious.infectiondiagnosis }</textarea>
							</td>
						</tr>	
						<tr>
							<td class="honry-lable">
								感染预后：
							</td>
							<td>
								<input style="width:200px" id="afterinfection" name="infectious.afterinfection" value="${infectious.afterinfection}"/>
							</td>
							<td class="honry-lable">
								易感因素：
							</td>
							<td colspan="4">
								<input style="width:200px" id="dactor" name="infectious.dactor" value="${infectious.dactor}"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								病原学检查：
							</td>
							<td colspan="1">
								<input style="width:200px" id="etiological" name="infectious.etiological" value="${infectious.etiological}"/>
							</td>
							<td class="honry-lable">
								标本名称：
							</td>
							<td colspan="4">
								<input id="specimen" name="infectious.specimen" value="${infectious.specimen}" class="easyui-textbox" style="width:200px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								病原体：
							</td>
							<td colspan="6">
								<textarea class="easyui-textbox" data-options="multiline:'true'" style="width: 100%;height: 50px;font-size: 14px"  
									name="infectious.pathogen">${infectious.pathogen }</textarea>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								手术名称：
							</td>
							<td colspan="6">
								<input id="itemName0_" name="infectious.operation_name" class="easyui-combogrid" value="${infectious.operation_name}"  style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								手术日期：
							</td>
							<td>
								<input id="operation_day" name="infectious.operation_day" value="${infectious.operation_day}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+10}-%M-%d %H:%m:%s'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="honry-lable">
								切口类型：
							</td>
							<td colspan="4">
								<input style="width:200px" id="incisiontype" name="infectious.incisiontype" value="${infectious.incisiontype}"/>
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
								<input class="easyui-textbox"  id="medicine_name"  name="infectious.medicine_name"  value="${infectious.medicine_name}"
								style="width:200px"/>
							</td>
							<td class="honry-lable">
								剂量：
							</td>
							<td colspan="4">
								<input class="easyui-textbox"  id="medicine_dose" name="infectious.medicine_dose"  value="${infectious.medicine_dose}"
								style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								给药方式：
							</td>
							<td colspan="1">
								<input class="easyui-textbox"  id="medicine_mode" name="infectious.medicine_mode" value="${infectious.medicine_mode}" style="width:200px;"/>
							</td>
							<td class="honry-lable" width="140px">
								用药频数：
							</td>
							<td colspan="3">
								<input class="easyui-textbox"  id="medicine_frequency"name="infectious.medicine_frequency" value="${infectious.medicine_frequency}"
								style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								应用时间：
							</td>
							<td colspan="6">
								<input id="medicine_begin_day" name="infectious.medicine_begin_day" value="${infectious.medicine_begin_day}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+10}-%M-%d %H:%m:%s',maxDate:'#F{$dp.$D(\'medicine_end_day\')}'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							至
								<input id="medicine_end_day" name="infectious.medicine_end_day" value="${infectious.medicine_end_day}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+10}-%M-%d %H:%m:%s',minDate:'#F{$dp.$D(\'medicine_begin_day\')}'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						<tr>
						<tr>
							<td class="honry-lable">
								备注：
							</td>
							<td colspan="6">
							<textarea class="easyui-textbox" data-options="multiline:'true'"" style="width: 100%;height: 50px;font-size: 12px"  
									name="infectious.remarks">${infectious.remarks}</textarea>
							</td>
						</tr>
				</table>
			</form>	
		<div style="text-align: center; padding: 5px;height:150px ">
			<a href="javascript:submit(0);" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:clear();" class="easyui-linkbutton"
				data-options="iconCls:'icon-clear'">清除</a>
			<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
		<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;" data-options="modal:true, closed:true">  
		&nbsp;<input class="inputCss"    id="medicalSex" data-options="prompt:'请输入姓名、证件号'">
		<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a><br>
	     	<table id="infoDatagrid"  fit="true" data-options="fitColumns:true,singleSelect:true">   
			</table>  
		</div>
		<div id="roleaddUserdiv"></div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script>
		var selsctage="";//选择的出生日期时算出年龄
		var operatNameMap=new Map();//手术名称翻页渲染
		//默认加载
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
			
		});
		
		//查询
		 function searchFrom(){
			var medicalSex = $('#medicalSex').val();
			$('#infoDatagrid').datagrid('load', {    
				medicalSex: medicalSex  
			});
		}

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
		    textField:'name',
		    onHidePanel:function(none){
		        var data = $(this).combobox('getData');
		        var val = $(this).combobox('getValue');
		        var result = true;
		        for (var i = 0; i < data.length; i++) {
		            if (val == data[i].encode) {
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
		        keys[keys.length] = 'encode';
		        keys[keys.length] = 'name';
		        keys[keys.length] = 'pinyin';
		        keys[keys.length] = 'wb';
		        keys[keys.length] = 'inputCode';
		        return filterLocalCombobox(q, row, keys);
		    }
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
		 *
		 */
		$('#reportType').combotree({
				data:[{"id":1,"text":"初次报告"},{"id":2,"text":"订正报告","iconCls":"icon-book","state":"open","children":[{"id":3,"text":"变更诊断"},{"id":4,"text":"死亡"},{"id":5,"text":"填卡错误"}]}],
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
					required: true, 
				    disabled:false
				}); 
			}else{
				$('#otherProfession').textbox({ 
					required: true, 
				    disabled:true
				}); 
				$('#otherProfession').textbox('setValue','');
			}
		},
		onLoadSuccess: function(node){
			var texts=$('#patientProfession').combobox('getText');
			if (texts=="其他") {
				$('#otherProfession').textbox({ 
					required: true, 
				    disabled:false
				}); 
			}else{
				$('#otherProfession').textbox({ 
					required: true, 
				    disabled:true
				}); 
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
	//表单提交submit信息
  	function submit(){
		//单独验证身份证号是否正确
  		if($('#CodeCertificate').combobox('getText').indexOf("身份证")>=0 && !isIdCardNo($("#certificatesNo").val())){
			return;
		}
		var reportBirthday=$('#reportBirthday').val();
		var reportAge=$('#reportAge').numberbox('getValue');
		if (reportBirthday == null || reportBirthday =="") {
			var ageUnit = $('#agedw').combobox('setValue', '001');
			if (reportAge == null || reportAge == "") {
				$.messager.alert('提示','请填写年龄或出生日期');
				close_alert();
				return;
			}else if(ageUnit == null || ageUnit == ""){
				$.messager.alert('提示','请选择年龄单位');
				close_alert();
                return;
			}else{
				$('#infEditForm').form('submit',{
			  		url:"<c:url value='/publics/hospInfectious/saveOrUpdateInfectious.action'/>",
			  		queryParams:{
			  			'bigDecimal':reportAge
			  		},
			  		 onSubmit:function(){
			  			if(!$('#infEditForm').form('validate')){
							$.messager.show({  
								title:'提示信息' ,   
								msg:'验证没有通过,不能提交表单!'  
							}); 
							close_alert();
							return false ;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});
					     return $(this).form('validate');  
					 },  
					success:function(data){
						$.messager.progress('close');
						if(data=="success"){
							$.alert('提示','保存成功',function(){
								window.close();
								//实现刷新
								window.opener.reloads(); 
							});
					   }else {
						   $.messager.alert('提示','保存失败');
					  	}
					 },
					error:function(date){
						$.messager.progress('close');
						$.messager.alert('提示','保存失败');
					}
			  	});
			}
		}else {
			if (reportAge==""||reportAge==null) {
				$('#infEditForm').form('submit',{
			  		url:"<c:url value='/publics/hospInfectious/saveOrUpdateInfectious.action'/>",
			  		onSubmit:function(){ 
			  			if(!$('#infEditForm').form('validate')){
							$.messager.show({  
								title:'提示信息' ,   
								msg:'验证没有通过,不能提交表单!'  
							}); 
							close_alert();
							return false ;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});
					     return $(this).form('validate');  
					 }, 
					success:function(data){
						$.messager.progress('close');
						if(data=="success"){
							$.alert('提示','保存成功',function(){
								window.close();
								//实现刷新
								window.opener.reloads(); 
							});
					   }else {
						   $.messager.alert('提示','保存失败');
					  	}
					 },
					error:function(date){
						$.messager.progress('close');
						$.messager.alert('提示','保存失败');
					}
			  	});
			}else{
				$('#infEditForm').form('submit',{
			  		url:"<c:url value='/publics/hospInfectious/saveOrUpdateInfectious.action'/>",
			  		onSubmit:function(){ 
			  			if(!$('#infEditForm').form('validate')){
							$.messager.show({  
								title:'提示信息' ,   
								msg:'验证没有通过,不能提交表单!'  
							}); 
							close_alert();
							return false ;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});
					     return $(this).form('validate');  
					 }, 
					 queryParams:{
						 'bigDecimal':reportAge
					 },
					 onBeforeLoad:function(p){
					 },
					success:function(data){
						$.messager.progress('close');
						if(data=="success"){
							$.alert('提示','保存成功',function(){
								window.close();
							//实现刷新
							window.opener.reloads(); 
							});
					   }else {
						   $.messager.alert('提示','保存失败');
					  	}
					 },
					error:function(date){
						$.messager.progress('close');
						$.messager.alert('提示','保存失败');
					}
			  	});
			}
			
		}
  	}

	//清除所填信息
	function clear() {
		$('#infEditForm').form('clear');
	}
	function closeLayout() {
		window.close();
	}
	$.extend($.fn.validatebox.defaults.rules, {
		  phoneRex: {
		    validator: function(value){
		    var rex=/^1[3-9]+\d{9}$/;
		    //var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
		    //区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
		    //电话号码：7-8位数字： \d{7,8
		    //分机号：一般都是3位数字： \d{3,}
		     //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
		    var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
		    if(rex.test(value)||rex2.test(value))
		    {
		      return true;
		    }else
		    {
		       return false;
		    }
		      
		    },
		    message: '请输入正确电话或手机格式'
		  }
	});
	
	  
           function searchFromcx(){
     		  var medicalrecord = $.trim($('#medicalrecord').val());
     			if(medicalrecord==null||$.trim(medicalrecord)==''){
     				$.messager.alert('提示','请正确录入病历号');
     				close_alert();
     				$("#medicalrecord").textbox('textbox').focus();
     				return;
     			}
     			$.ajax({
     				url : '<%=basePath%>publics/hospInfectious/queryByMedicalrecord.action?medicalrecord='+medicalrecord,
     				type:'post',
     				success: function(data) {
     					var infectious=data;
     					if(infectious.length>1){
     						$("#diaInpatient").window('open');
     						$("#infoDatagrid").datagrid({
     							url:'<%=basePath%>publics/hospInfectious/queryByMedicalrecord.action?medicalrecord='+medicalrecord,    
     							  columns:[[    
     	     						        {field:'medicalrecordId',title:'病历号',width:'30%',align:'center'} ,  
     	     						        {field:'patientSex',title:'性别',width:'15%',align:'center',formatter:function(value,row,index){
     	     						        	return sexMap.get(value);
     								        }} ,
     	     						        {field:'patientName',title:'姓名',width:'15%',align:'center'} ,   
     	     						        {field:'patientCertificatesno',title:'证件号',width:'40%',align:'center'} 
     	     						    ]] ,
     	     						    onDblClickRow:function(rowIndex, rowData){
     	     						    	$('#patientNameId').textbox('setValue',rowData.patientName);
     	     						    	$('#medicalrecord').val(rowData.medicalrecordId);
     	     							 	$('#patientParents').textbox('setValue',rowData.patientMother);
     	     							 	$('#CodeSex').combobox('setValue',rowData.patientSex);
     	     							 	$('#CodeCertificate').combobox('setValue',rowData.patientCertificatestype);
     	     							 	$('#certificatesNo').textbox('setValue',rowData.patientCertificatesno);
     	     							 	$('#patientProfession').combobox('setValue',rowData.patientOccupation);
     	     							 	$('#reportAgeunit').combobox('setValue',rowData.reportAgeunit);
     	     							 	$('#workPlace').textbox('setValue',rowData.patientWorkunit);
     	     							 	$('#telephone').textbox('setValue',rowData.patientPhone);
     	     							 	$('#reportAge').textbox('setValue',rowData.reportAge);
     	     							    $('#reportBirthday').val(rowData.patientBirthday);
     	     							    $("#diaInpatient").window('close');
     	     						    }
     	     						});
     	     					}else if(infectious.length==1){
     	     						$('#patientNameId').textbox('setValue',infectious[0].patientName);
     	     					 	$('#patientParents').textbox('setValue',infectious[0].patientMother);
     	     					 	$('#CodeSex').combobox('setValue',infectious[0].patientSex);
     	     					 	$('#CodeCertificate').combobox('setValue',infectious[0].patientCertificatestype);
     	     					 	$('#certificatesNo').textbox('setValue',infectious[0].patientCertificatesno);
     	     					 	$('#patientProfession').combobox('setValue',infectious[0].patientOccupation);
     	     					 	$('#workPlace').textbox('setValue',infectious[0].patientWorkunit);
     	     					 	$('#telephone').textbox('setValue',infectious[0].patientPhone);
     	     					    $('#reportBirthday').val(infectious[0].patientBirthday);
     	     					}else{
     	     						$.messager.alert('提示','您输入的病历号有误');
     	     						close_alert();
     	     					    $('#patientNameId').textbox('setValue','');
     	     					 	$('#patientParents').textbox('setValue','');
     	     					 	$('#CodeSex').combobox('setValue','');
     	     					 	$('#CodeCertificate').combobox('setValue','');
     	     					 	$('#certificatesNo').textbox('setValue','');
     	     					 	$('#patientProfession').combobox('setValue','');
     	     					 	$('#workPlace').textbox('setValue','');
     	     					 	$('#telephone').textbox('setValue','');
     	     					    $('#reportBirthday').val('');
     					}
     				}
     			});
     		} 
	  
	  
	 
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
	
	function changeAge(){
			 var dateStr=$('#reportBirthday').val();
			 var date=getDate(dateStr);
	    	 var now = new Date();
		     var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
		     var agess=now.getFullYear()-date.getFullYear();
		     selsctage=agess;
		     if (agess<14) {
		    	 $('#patientParents').textbox({    
		    		    required: true    
		    		});
		    	 var patientParents= $('#patientParents').val();
		    	 if(patientParents==null || patientParents==''){
			    	 $.messager.alert('提示','请填写患者家长姓名'); 
		    	 }
			}else {
				$('#patientParents').textbox({    
	    		    required: false    
	    		});
			}
	}
	//字符串转日期格式，strDate要转为日期格式的字符串
	function getDate(strDate){
		  var date = eval('new Date(' + strDate.replace(/\d+(?=-[^-]+$)/, 
		   function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');
		  return date;
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
// 	 function onSelect(d) {
//          if (deadDate < infectDate) {
//          	$.messager.alert('提示','发病日期应小于死亡日期');
//          	close_alert();
//              //只要选择了日期，不管是开始或者结束都对比一下，如果结束小于开始，则清空结束日期的值并弹出日历选择框
//              return;
//          }
// 	            if (deadDate < diagnosisDate) {
// 	            	$.messager.alert('提示','诊断时间应小于死亡日期');
// 	            	close_alert();
// 	                //只要选择了日期，不管是开始或者结束都对比一下，如果结束小于开始，则清空结束日期的值并弹出日历选择框
// 	               	return;
// 	            }
// 	  }
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>