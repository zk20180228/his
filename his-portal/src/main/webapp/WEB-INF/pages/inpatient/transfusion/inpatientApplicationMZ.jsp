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
<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
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
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<form id="baocun" method="post"  >
			<div data-options="region:'center',border:false" style="width: 100%;height: 100%;padding:5px" align="center">
				<table style="width: 100%;height: 10%;">
					<tr>
						<td style="text-align: center"><font size="6">输血申请单</font></td>
					</tr>
					<tr >
						<td style="padding-left: 10px;">
							病历号：
							<input id="zhuyuanhao"  class="easyui-textbox" data-options="required:true">
							<input id="patientNo" name="patientNo" type="hidden">
							<input id="clinicNo" name="clinicNo" type="hidden">
							<input id="deptCode" name="deptCode" type="hidden">
							<input id="deptName" name="deptName" type="hidden">
							<input id="sexName" name="sexName" type="hidden">
							<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							<shiro:hasPermission name="${menuAlias}:function:readCard">
								<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
							</shiro:hasPermission>
				        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
				        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
							</shiro:hasPermission>
						</td>
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
								<td>
									<input id="xingming" name="name" class="easyui-textbox" data-options="required:true" readonly="readonly">
								</td>
								<td class="honry-lable">
									性别：
								</td>
								<td>
									<input id="sexCode" name="sexCode1" class="easyui-textbox" data-options="required:true" readonly="readonly">
								<input id="sexCodeHidden" name="sexCode" type="hidden">
								</td>
								</tr>
								<tr>
								<td class="honry-lable">
									年龄：
								</td>
								<td>
									<input id="age" name="age" class="easyui-textbox"  readonly="readonly">
									<span id="reportAgeunit" ></span>
								</td>
								<td class="honry-lable">
									科室：
								</td>
								<td>
									<input id="keshi"  readonly="readonly"class="easyui-textbox">
								</td>
							</tr>
						
						<tr>
							<td class="honry-lable">
								临床诊断：
							</td>
							<td>
								<div id="lczd"><input id="linchuang" name="diagnose" class="easyui-combobox" data-options="required:true"></input>
								<input id="diagnoseName" name="diagnoseName" type="hidden"></input>
								<a href="javascript:delSelectedData('linchuang');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></div>
							</td>
							<td class="honry-lable">
								是否报销：
							</td>
							<td>
								<input id="baoxiao" class="easyui-combobox" name="isCharge"  style="width: 150px;" 
								data-options="required:true,editable:false,
									valueField: 'id',
									textField: 'value',
									data:[
									{id:'1',value:'是'},
									{id:'2',value:'否'}]">
							</td>
							</tr>
							<tr>
							<td class="honry-lable">
									用血目的：
								</td>
								<td>
									<input id="yongxue" name="bloodAim" class="easyui-combobox" data-options="required:true">
								</td>
							<td class="honry-lable">
								预定血型：
							</td>
						
							<td>
								<input id="yudingxuexing" name="bloodKind" class="easyui-combobox" data-options="required:true">
							</td>
								</tr>
						<tr>
							<td class="honry-lable">
								血液成分：
							</td>
							<td>
								<input id="xueyechengfen" name="bloodTypeCode" class="easyui-combobox" data-options="required:true">
								<a href="javascript:delSelectedData('xueyechengfen');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="honry-lable">
									RH(D)：
								</td>
								<td>
									<select id="rh" name="rh" class="easyui-combobox" style="width: 150px;" data-options="required:true">   
									    <option value="0">待查</option>   
									    <option value="1">阴性</option>  
									    <option value="2">阳性</option>   
									</select>
								</td>
						</tr>
						<tr>
							<td  class="honry-lable">
								预订血量：
							</td>
							<td>
								<input class="easyui-numberbox" id='yuding' name="quantity" style="width: 50px;" data-options="required:true,missingMessage:'只能输入数字'"> 　　
								<input class="easyui-textbox" id="xueliang" name="stockUnit" style="width: 50px;"  value="ml" readonly="readonly" data-options="required:true">
							</td>
							<td  class="honry-lable">
									输血性质：
								</td>
								<td>
									<input id="shuxuexingzhi" name="quality" class="easyui-combobox" data-options="required:true">
								</td>
								</tr>
								<tr>
								<td class="honry-lable">
									受血者属地：
								</td>
								<td>
									<select id="shudi" name="insource" class="easyui-combobox" style="width: 150px;" data-options="required:true">   
									    <option value="0">本市</option>   
									    <option value="1">外埠</option>   
									</select>
								</td>
						
							<td class="honry-lable">
								预定输血日期：
							</td>
							<td>
		       						<input  id="orderTime" name="orderTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'{%y+1}-%M-%d'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							</tr>
						<tr>
							<td class="honry-lable">
								既往输血史：
							</td>
							<td colspan="3">
								<input id="isaneFlgHidden" type="hidden" name="bloodhistory" data-options="required:true"/>
								<input type="radio" id="yes"
									onclick="javascript:onclickBoxmazui()" name="bloodhistoryName"/>
								是　　　
								<input type="radio" id="no"
									onclick="javascript:onclickBoxmazui()" name="bloodhistoryName" />
								否
							</td>
						</tr>
							<tr>
								<td colspan="4">受血者血性检验信息</td>
							</tr>
							<tr>
							<td class="honry-lable">
								血型 ：
							</td>
							<td>
								<input id="xuexing" name="patientBloodkind" class="easyui-combobox" >
							</td>
							<td class="honry-lable">
								血红蛋白：
							</td>
							<td>
								<input id="CodeSex" name="hematin" class="easyui-numberbox" data-options="required:true">g/L
							</td>
							</tr>
							<tr>
							<td class="honry-lable">
								 HCT：
							</td>
							<td>
								<input id="CodeSex" name="hct" class="easyui-numberbox" data-options="required:true">
							</td>
							<td class="honry-lable">
								血小板：
							</td>
							<td>
								<input id="CodeSex" name="platelet" class="easyui-numberbox" data-options="required:true">
								x10 <sup>9</sup>/L
							</td>
							</tr>
							<tr>
							<td class="honry-lable">
								ALT：
							</td>
							<td>
								<input id="CodeSex" name="alt" class="easyui-numberbox" data-options="required:true">
								U/L
							</td>
							<td class="honry-lable">
								HbsAg：
							</td>
							<td>
								<select id="rh" name="hbsag" class="easyui-combobox" style="width: 150px;" data-options="required:true">   
									    <option value="0">待查</option>   
									    <option value="1">阴性</option>   
									    <option value="2">阳性</option>   
								</select>
							</td>
							</tr>
							<tr>
							<td  class="honry-lable">
								Anti-HCV：
							</td>
							<td>
								<select id="rh" name="antiHcv" class="easyui-combobox" style="width: 150px;" data-options="required:true">   
									    <option value="0">待查</option>   
									    <option value="1">阴性</option>   
									    <option value="2">阳性</option>   
								</select>
							</td>
							<td  class="honry-lable">
								Anti-HIV1/2：
							</td>
							<td>
								<select id="rh" name="antiHiv" class="easyui-combobox" style="width: 150px;" data-options="required:true">   
									    <option value="0">待查</option>   
									    <option value="1">阴性</option>   
									    <option value="2">阳性</option>   
								</select>
							</td>
							</tr>
							<tr>
							<td  class="honry-lable">
								梅毒：
							</td>
							<td>
								<select id="rh" name="lues" class="easyui-combobox" style="width: 150px;" data-options="required:true">   
									    <option value="0">待查</option>   
									    <option value="1">阴性</option>   
									    <option value="2">阳性</option>   
								</select>
							</td>
							
							<td class="honry-lable">
								孕产情况：
							</td>
							<td>
								<input value="孕0产0" name="pregnant" class="easyui-textbox" >
							</td>
							</tr>
				</table>
				<table style="width: 100%;">
							<tr >
						    	<td ><input id="templateSubmit" type="checkbox" onclick="clickclick()">自定义诊断名称&nbsp;
						    		<span id="lczds" style="display: none;"><input id="lczdText" disabled="disabled" name="diagnoses" class="easyui-textbox" data-options="required:true" dvalue="${inpatientApplyNow.diagnoseName}" ></input></span> 
						    	</td>
						    	<td style="color:Blue;text-align: right;" class="inpatientApplicationMZ">申请医师：
						    		<input style="width: 15%;" id="shenqingyishi" name="applyDocCode" class="easyui-combobox" readonly="readonly" data-options="required:true">
						    		<input type="hidden" id="applyDocName" name="applyDocName" >
						    		主治医师：
						    		<input style="width: 15%;" id="zhuzhiyishi" name="chargeDocCode" data-options="required:true">
						    		<input type="hidden" id="chargeDocName" name="chargeDocName" >
						    		<a href="javascript:delSelectedData('zhuzhiyishi');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						    		申请时间：
						    		<input id="shijian" name="applyTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						    	</td>
							</tr>
							
				</table>
				<table style="width: 100%;">
					<br>
					<tr >
							 <shiro:hasPermission name="${menuAlias}:function:save"> 
							<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" >保存</a>
							</shiro:hasPermission> 
							<shiro:hasPermission name="${menuAlias}:function:cancel">
							&nbsp;<a href="javascript:void(0)" onclick="closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >取消</a>
							</shiro:hasPermission> 
					</tr>
				</table>
			</div>
		</form>
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:800;height:500;padding:5" data-options="modal:true, closed:true" >   
						<table id="infoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
						</table>  
		   			 </div>  
<script type="text/javascript">
		var sss="";
		var deptMap="";
		function clickclick(){
			var c=$("#templateSubmit");
			if(c.is(":checked")){
				$("#lczdText").textbox({
					 disabled:false
				});
				$('#lczds').show();
				 $('#linchuang').combobox({
					  disabled:true
						
				});
			}else{
				$('#lczds').hide();
				$("#lczdText").textbox({
					 disabled:true
				});
				//临床诊断 没有方法
				 $('#linchuang').combobox({
					disabled:false
				 });
			}
		}
		//既往输血史
		function onclickBoxmazui(){
			if ($('#yes').is(':checked')) {
				$('#isaneFlgHidden').val('0');
			}
			if ($('#no').is(':checked')) {
				$('#isaneFlgHidden').val('1');
			}
		}
		$(function(){
			
			//临床诊断
			 $('#linchuang').combobox({
				 url:'<%=basePath %>publics/consultation/icdCombobox.action',
				valueField : 'code',
				textField : 'name',
				multiple : false,
				editable : true
			 });
			//主治医生编码
			$('#zhuzhiyishi').combobox({
				url : '<%=basePath %>publics/consultation/queryLoginUserDept.action',
				valueField : 'jobNo',
				textField : 'name',
				multiple : false,
				editable : false
			});
			//申请医生编码
			$('#shenqingyishi').combobox({
				url : '<%=basePath %>publics/consultation/queryLoginUserDept.action',
				valueField : 'jobNo',
				textField : 'name',
				multiple : false,
				editable : false
			});
			//当前登录人ID
			sss="${dname}";
			$('#shenqingyishi').combobox("setValue",sss);
			  var date = new Date();
			  var seperator1 = "-";
			  var seperator2 = ":";
			  var month = date.getMonth() + 1;
			  var strDate = date.getDate();
			  if (month >= 1 && month <= 9) {
				 month = "0" + month;
				}
				if (strDate >= 0 && strDate <= 9) {
					 strDate = "0" + strDate;
					 }
				 var now1 = date.getFullYear() + seperator1 + month + seperator1 + strDate
				 + " " + date.getHours() + seperator2 + date.getMinutes()
				 + seperator2 + date.getSeconds();
			$('#shijian').val(now1);
			bindEnterEvent('zhuyuanhao',query,'easyui');
			$('#xueyechengfen').combobox({
				url : '<%=basePath%>publics/transfusion/queryInpatientComponent.action',
				valueField : 'bloodTypeCode',
				textField : 'bloodTypeName',
				multiple : false,
				editable : false
			});
			//渲染表单中的挂号科室
				$.ajax({
				url: "<c:url value='/publics/consultation/querydeptComboboxs.action'/>", 
				type:'post',
				success: function(deptData) {
					deptMap = deptData;
				}
			});
		
			bindEnterEvent('zhuzhiyishi',popWinToEmployee,'easyui');//绑定回车事件
			//血型
			$('#xuexing').combobox({
				url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodType",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
				editable : false
			 });
			//预定血型
			$('#yudingxuexing').combobox({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodType'/>",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
				editable : false,

			 });
			//用血目的
			$('#yongxue').combobox({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodaim'/>",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
				editable : false
			 });
			//输血性质
			$('#shuxuexingzhi').combobox({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodquality'/>",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
				editable : false
			 });
			
			
				//给临床诊断绑定弹窗事件
				bindEnterEvent('linchuang',popWinToLinchuang,'easyui');
				//给血液成分绑定弹窗事件
				bindEnterEvent('xueyechengfen',popWinToXueyechengfen,'easyui');
			});
			
		/*******************************开始读卡***********************************************/
		//定义一个事件（读卡）
		function read_card_ic(){
			var card_value = app.read_ic();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			$.ajax({
				url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
				data:{idcardOrRe:card_value},
				type:'post',
				async:false,
				success: function(data) { 
					if(data==null||data==''){
						$.messager.alert('提示','此卡号无效');
						return;
					}
					$('#zhuyuanhao').textbox('setValue',data);
					query();
				}
			});
		};
		/*******************************结束读卡***********************************************/
		/*******************************开始读身份证***********************************************/
			//定义一个事件（读身份证）
			function read_card_sfz(){
				var card_value = app.read_sfz();
				if(card_value=='0'||card_value==undefined||card_value==''){
					$.messager.alert('提示','此卡号['+card_value+']无效');
					return;
				}
				$.ajax({
					url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
					data:{idcardOrRe:card_value},
					type:'post',
					async:false,
					success: function(data) {
						if(data==null||data==''){
							$.messager.alert('提示','此卡号无效');
							return;
						}
						$('#zhuyuanhao').textbox('setValue',data);
						query();
					}
				});
			};
		/*******************************结束读身份证***********************************************/
			function query(){
				$.messager.progress({text:"查询中,请稍等......",modal:true});
				var id = $('#zhuyuanhao').textbox('getValue');		
				if(id.length==0){
					$.messager.alert('提示','请正确输入病历号');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					$.messager.progress('close');
					return;
				}else{
					$.ajax({
			   			url: "<%=basePath%>publics/inpatientInfoList/queryrespatientInfoList.action?medId="+id,
						success: function(plist) {
							$.messager.progress('close');
							if(plist.length>1){
								$("#diaInpatient").window('open');
								$("#infoDatagrid").datagrid({
									data:plist,    
								    columns:[[    
								        {field:'midicalrecordId',title:'病历号',width:'20%',align:'center'} ,
								        {field:'clinicCode',title:'门诊号',width:'25%',align:'center'} ,
								        {field:'patientSexName',title:'性别',width:'10%',align:'center'} ,
								        {field:'patientName',title:'姓名',width:'25%',align:'center'} ,   
								        {field:'patientBirthday',title:'年龄',width:'20%',align:'center',formatter:ageformatter} 
								    ]] ,
								    onDblClickRow:function(rowIndex, rowData){
								    	$("#diaInpatient").window('close');
					    				$('#xingming').textbox('setValue',rowData.patientName);
										$('#sexCode').textbox('setText',rowData.patientSexName);
										$('#sexName').val(rowData.patientSexName);
						    			$('#sexCodeHidden').val(rowData.patientSex);
										$('#deptCode').val(rowData.deptCode);
										$('#clinicNo').val(rowData.clinicCode);
										var ages=DateOfBirth(rowData.patientBirthday);
										$('#age').textbox('setValue',ages.get("nianling"));
										$('#reportAgeunit').text(ages.get('ageUnits'));
										$('#patientNo').val(rowData.midicalrecordId);
										$('#keshi').textbox('setValue',rowData.deptName);
										$('#deptName').val(rowData.deptName);
										$('#zhuzhiyishi').combobox('setValue',rowData.doctCode);
										$('#chargeDocName').val(rowData.doctName);
								    }
								});
							}else if(plist.length==1){
					    				$('#xingming').textbox('setValue',plist[0].patientName);
					    				$('#sexCode').textbox('setText',plist[0].patientSexName);
										$('#sexCodeHidden').val(plist[0].patientSex);
										$('#sexName').val(plist[0].patientSexName);
										$('#patientNo').val(plist[0].midicalrecordId);
										var ages=DateOfBirth(plist[0].patientBirthday);
										$('#age').textbox('setValue',ages.get("nianling"));
										$('#reportAgeunit').text(ages.get('ageUnits'));
										$('#clinicNo').val(plist[0].clinicCode);
										$('#keshi').textbox('setValue',plist[0].deptName);
										$('#deptName').val(plist[0].deptName);
										$('#deptCode').val(plist[0].deptCode);
										$('#zhuzhiyishi').combobox('setValue',plist[0].doctCode);
										$('#chargeDocName').val(plist[0].doctName);
										
							}else{
							
										$.messager.alert('提示','患者没有挂号信息');
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
									
							}
						}
					}); 
				}
			}
			//提交保存
			function submit() {
				$('#chargeDocName').val($('#zhuzhiyishi').combobox('getText'));
				$('#applyDocName').val($('#shenqingyishi').combobox('getText'));
				var c=$("#templateSubmit");
				if(c.is(":checked")){
					var s = $('#lczdText').combobox('getText');
					$('#diagnoseName').val(s);
					$('#linchuang').combobox('setValue','');
				}else{
					var s = $('#linchuang').combobox('getText');
					$('#diagnoseName').val(s);
				}
				if($("#orderTime").val()==null||$("#orderTime").val()==""){
					$.messager.alert('提示','预定用血日期不能为空！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}	
				if($("#shijian").val()==null||$("#shijian").val()==""){
					$.messager.alert('提示','申请用血日期不能为空！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}	
				if($('#yuding').numberbox('getValue')==0){
					$.messager.alert('提示','预定血量不能为0，请重新填写');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					('#yuding').numberbox('setValue',0);
					return;
				}
				$('#baocun').form('submit',{
					url : "<%=basePath %>publics/InpatientApply/saveInpatientApply.action?state="+1,
					onSubmit:function(){
						if (!$('#baocun').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
						var age = $('#age').textbox('getValue')+$('#reportAgeunit').text();
						$('#age').textbox('setValue',age);
						$.messager.progress({text:'保存中，请稍后...',modal:true});
			        },  
			        success:function(data){  
			        	$.messager.progress('close');
			        	if(data==1){
			        		$.messager.alert('提示','保存成功');
			        		setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
			        	}else{
			        		$.messager.alert('提示','保存失败');
			        	}
			        	opener.location.reload();
			        	window.close();
			        },
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');	
					}
				});
			}	
			//渲染科室		
			function functionDept(value,row,index){
				if(value!=null&&value!=''){
					return deptMap[value];
				}
			}
			function closeLayout(){
				window.close();
			}
			
			/**
			 * 打开临床诊断界面弹框
			 * @author  zhuxiaolu
			 * @date 2015-5-25 10:53
			 * @version 1.0
			 */
			
			function popWinToLinchuang(){
				
				var tempWinPath = "<%=basePath%>/popWin/popWinBusinessIcd/toLCZDPopWinDB.action?textId=linchuang";
				var aaa=window.open (tempWinPath,'newwindow',' left=130,top=20,width='+ (screen.availWidth - 260) +',height='+(screen.availHeight-80) +',scrollbars,resizable=yes,toolbar=no')
						
			}
			
			
			/**
			 * 打开血液成分界面弹框
			 * @author  zhuxiaolu
			 * @date 2015-5-25 10:53
			 * @version 1.0
			 */
			
			function popWinToXueyechengfen(){
				
				var tempWinPath = "<%=basePath%>/popWin/popWinInpatientComponent/toXYCFPopWinDB.action?textId=xueyechengfen";
				var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
						
			}
			 /**
			   * 回车弹出主治医师弹框
			   * @author  zhuxiaolu
			   * @param textId 页面上commbox的的id
			   * @date 2016-03-22 14:30   
			   * @version 1.0
			   */
		   function popWinToEmployee(){
			    var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=zhuzhiyishi&employeeType=1";
					window.open (tempWinPath,'newwindow',' left=20,top=20,width='+ (screen.availWidth -50) +',height='+ (screen.availHeight-
  					80)+',scrollbars,resizable=yes,toolbar=yes')
				
			 }
			function getSex(index){
				return sexMap.get(index);
			}
			function ageformatter(value,row,index){
				if(value!=''&&value!=null){
	       			var arr=value.split("-");
					var y =arr[0];
					var m =arr[1];
					var d =arr[2];
					var agedate=DateOfBirth(y+"-"+m+"-"+d);
					return agedate.get("nianling")+agedate.get('ageUnits');
	       		}else{
	       			return value;
	       		}
		        	
			}
</script>	
</body>
</html>