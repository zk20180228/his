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
					<td style="text-align: center"><font size="6">输血申请单</font></td>
				</tr>
				<tr >
					<td nowrap="nowrap">
							<input id="zhuyuanhao" name="patientNo" value="${inpatientApplyNow.patientNo }" type="hidden" > 
							<input id="clinicNo" name="clinicNo" value="${inpatientApplyNow.clinicNo }" type="hidden">
							<input id="bedNo" name="bedNo" type="hidden" value="${inpatientApplyNow.bedNo }">
							<input id="nurseCellCode" name="nurseCellCode" type="hidden" value="${inpatientApplyNow.nurseCellCode }">
							<!--新添字段-->
							<input id="nurseCellName" name="nurseCellName" type="hidden" value="${inpatientApplyNow.nurseCellName }">
							<input id="applyDocName" name="applyDocName" type="hidden" value="${inpatientApplyNow.applyDocName }">
							<input id="chargeDocName" name="chargeDocName" type="hidden" value="${inpatientApplyNow.chargeDocName }">
							<input id="cancelName" name="cancelName" type="hidden" value="${inpatientApplyNow.cancelName }">
							<input id="approvalOperName" name="approvalOperName" type="hidden" value="${inpatientApplyNow.approvalOperName }">
							<input id="createUser" name="createUser" type="hidden" value="${inpatientApplyNow.createUser}">
							<input id="createTime" name="createTime" type="hidden" value="${inpatientApplyNow.createTime}">
							<input id="id" name="id" value="${inpatientApplyNow.id }" type="hidden">
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
								<input id="xingming" name="name" class="easyui-textbox" data-options="required:true" value="${inpatientApplyNow.name}" readonly="readonly">
							</td>
							<td class="honry-lable">
								性别：
							</td>
							<td>
								<input id="sexCode" name="sexCode1" class="easyui-textbox" data-options="required:true" value="${inpatientApplyNow.sexName}" readonly="readonly">
								<input id="sexCodeHidden" name="sexCode" type="hidden"  value="${inpatientApplyNow.sexCode}">
								<input id="sexCodeHidden" name="sexName" type="hidden"  value="${inpatientApplyNow.sexName}">
							</td>
							</tr>
							<tr>
							<td class="honry-lable">
								年龄：
							</td>
							<td>
								<input id="age" name="age" class="easyui-textbox" value="${inpatientApplyNow.age}" readonly="readonly">
								<span id="reportAgeunit" ></span>
							</td>
							<td class="honry-lable">
								科室：
							</td>
							<td>
								<input id="keshi" data-options="required:true" readonly="readonly"
									 value="${inpatientApplyNow.deptName}" class="easyui-textbox">
									 <input name="deptCode"  type="hidden"  value="${inpatientApplyNow.deptCode}" >
									  <input name="deptName"  type="hidden"  value="${inpatientApplyNow.deptName}" >
							</td>
						</tr>
					
					<tr>
						<td class="honry-lable">
							临床诊断：
						</td>
						<td>
							<div id="lczd"><input id="linchuang" name="diagnose" class="easyui-combobox" data-options="required:true" value="${inpatientApplyNow.diagnose}" ></input>
							<input id="diagnoseName" name="diagnoseName" type="hidden"></input>
							<a href="javascript:delSelectedData('linchuang');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></div>
						</td>
						<td class="honry-lable">
							是否报销：
						</td>
						<td>
							<input id="baoxiao"class="easyui-combobox" name="isCharge"  style="width: 150px;" value="${inpatientApplyNow.isCharge }" 
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
								<input id="yongxue" name="bloodAim" class="easyui-combobox" value="${inpatientApplyNow.bloodAim}" data-options="required:true">
							</td>
						<td class="honry-lable">
							预定血型：
						</td>
					
						<td>
							<input id="yudingxuexing" name="bloodKind" class="easyui-combobox" value="${inpatientApplyNow.bloodKind}" data-options="required:true">
						</td>
							</tr>
					<tr>
						<td class="honry-lable">
							血液成分：
						</td>
						<td>
							<input id="xueyechengfen" name="bloodTypeCode" class="easyui-combobox" value="${inpatientApplyNow.bloodTypeCode}" data-options="required:true">
							<a href="javascript:delSelectedData('xueyechengfen');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable">
								RH(D)：
							</td>
							<td>
								<input id="rh" name="rh" class="easyui-combobox" style="width: 150px;" value="${inpatientApplyNow.rh }" 
								data-options="required:true,editable:false,
									valueField: 'id',
									textField: 'value',
									data:[
									{id:'0',value:'待查'},
									{id:'1',value:'阴性'},
									{id:'2',value:'阳性'}]">
							</td>
					</tr>
					<tr>
						<td  class="honry-lable">
							预订血量：
						</td>
						<td>
							<input class="easyui-numberbox" id='quantity' name="quantity" value="${inpatientApplyNow.quantity}" style="width: 50px;" data-options="required:true,missingMessage:'只能输入数字'">　　
							<input class="easyui-textbox" id="xueliang" name="stockUnit" style="width: 50px;"  value="ml" readonly="readonly" data-options="required:true">
						</td>
						<td  class="honry-lable">
								输血性质：
							</td>
							<td>
								<input id="shuxuexingzhi" name="quality" class="easyui-combobox" value="${ inpatientApplyNow.quality}"  data-options="required:true">
							</td>
							</tr>
							<tr>
							<td class="honry-lable">
								受血者属地：
							</td>
							<td>
								<input id="shudi" name="insource" class="easyui-combobox" style="width: 150px;" value="${inpatientApplyNow.insource }" 
								data-options="required:true,editable:false,
									valueField: 'id',
									textField: 'value',
									data:[
									{id:'0',value:'本市'},
									{id:'1',value:'外埠'}]">
							</td>
					
						<td class="honry-lable">
							预定输血日期：
						</td>
						<td>
							<input id="orderTime"  name="orderTime" class="Wdate" value="${orderTime}" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;">	
						</td>
						</tr>
					<tr>
						<td class="honry-lable">
							既往输血史：
						</td>
						<td colspan="3">
							<input id="isaneFlgHidden" type="hidden" name="bloodhistory"  value="${inpatientApplyNow.bloodhistory}" data-options="required:true"/>
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
							血型：
						</td>
						<td>
							<input id="xuexing" name="patientBloodkind" value="${inpatientApplyNow.patientBloodkind}" class="easyui-combobox" >
						</td>
						<td class="honry-lable">
							血红蛋白：
						</td>
						<td>
							<input id="hematin" name="hematin" class="easyui-numberbox" value="${inpatientApplyNow.hematin}" data-options="required:true">g/L
						</td>
						</tr>
						<tr>
						<td class="honry-lable">
							 HCT：
						</td>
						<td>
							<input id="hct" name="hct" class="easyui-numberbox"  value="${inpatientApplyNow.hct}" data-options="required:true">
						</td>
						<td class="honry-lable">
							血小板：
						</td>
						<td>
							<input id="platelet" name="platelet" class="easyui-numberbox" value="${inpatientApplyNow.platelet}" data-options="required:true">
							x10 <sup>9</sup>/L
						</td>
						</tr>
						<tr>
						<td class="honry-lable">
							ALT：
						</td>
						<td>
							<input id="alt" name="alt" class="easyui-numberbox" value="${inpatientApplyNow.alt}" data-options="required:true">
							U/L
						</td>
						<td class="honry-lable">
							HbsAg：
						</td>
						<td>
							<input id="rh" name="hbsag" class="easyui-combobox" style="width: 150px;" value="${inpatientApplyNow.hbsag }" 
								data-options="required:true,editable:false,
									valueField: 'id',
									textField: 'value',
									data:[
									{id:'0',value:'待查'},
									{id:'1',value:'阴性'},
									{id:'2',value:'阳性'}]">
							
						</td>
						</tr>
						<tr>
						<td  class="honry-lable">
							Anti-HCV：
						</td>
						<td>
							<input id="rh" name="antiHcv" class="easyui-combobox" style="width: 150px;" value="${inpatientApplyNow.antiHcv }" 
								data-options="required:true,editable:false,
									valueField: 'id',
									textField: 'value',
									data:[
									{id:'0',value:'待查'},
									{id:'1',value:'阴性'},
									{id:'2',value:'阳性'}]">
						</td>
						<td  class="honry-lable">
							Anti-HIV1/2：
						</td>
						<td>
							<input id="rh" name="antiHiv" class="easyui-combobox" style="width: 150px;" value="${inpatientApplyNow.antiHiv }" 
								data-options="required:true,editable:false,
									valueField: 'id',
									textField: 'value',
									data:[
									{id:'0',value:'待查'},
									{id:'1',value:'阴性'},
									{id:'2',value:'阳性'}]">
						</td>
						</tr>
						<tr>
						<td  class="honry-lable">
							梅毒：
						</td>
						<td>
							<input id="rh" name="lues" class="easyui-combobox" style="width: 150px;"  value="${inpatientApplyNow.lues }" 
								data-options="required:true,editable:false,
									valueField: 'id',
									textField: 'value',
									data:[
									{id:'0',value:'待查'},
									{id:'1',value:'阴性'},
									{id:'2',value:'阳性'}]">
						</td>
						
						<td class="honry-lable">
							孕产情况：
						</td>
						<td>
							<input  name="pregnant" class="easyui-textbox" value="${inpatientApplyNow.pregnant }">
						</td>
						</tr>
			</table>
			<table style="width: 100%;">
						<tr >
					    	<td ><input id="templateSubmit" type="checkbox" onclick="clickclick()">自定义诊断名称&nbsp;
					    	<span id="lczds" style="display: none;"><input id="lczdText" name="diagnoses" class="easyui-textbox" data-options="required:true" value="${inpatientApplyNow.diagnoseName}" ></input></span> 
					    	</td>
					    	<td style="color:Blue;text-align: right;">申请医师：
					    		<input  id="shenqingyishi" name="applyDocCode" class="easyui-combobox"  value="${dname}" data-options="required:true">
					    		主治医师：
					    		<input  id="zhuzhiyishi" name="chargeDocCode" value="${inpatientApplyNow.chargeDocCode}"  data-options="required:true">
					    		<a href="javascript:delSelectedData('zhuzhiyishi');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
					    		申请时间：
					    		<input id="shijian" name="applyTime" value="${now}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd  HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>	
					    	</td>
						</tr>
			</table>
			<table style="width: 100%;">
				<br>
				<tr >
						 <shiro:hasPermission name="${menuAlias}:function:save"> 
						<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="text-align: right ;">保&nbsp;存&nbsp;</a>
						</shiro:hasPermission> 
						<shiro:hasPermission name="${menuAlias}:function:cancel">
						&nbsp;<a href="javascript:void(0)" onclick="closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" style="text-align: right ;">取&nbsp;消&nbsp;</a>
						</shiro:hasPermission>
				</tr>
			</table>
		</div>
	</form>
</div>
<script type="text/javascript">
		var sss="";
		var deptMap="";
		var sexMap=new Map();
		function clickclick(){
			var c=$("#templateSubmit");
			if(c.is(":checked")){
				$('#lczds').show();
				 $('#linchuang').combobox({
					  disabled:true
						
				});
			}else{
				$('#lczds').hide();
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
			//临床诊断 没有方法
			 $('#linchuang').combobox({
				url:'<%=basePath %>publics/consultation/icdCombobox.action',
				valueField : 'code',
				textField : 'name',
				multiple : false,
				editable : true
			 });
			//性别渲染
			$.ajax({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						sexMap.put(v[i].encode,v[i].name);
					}
				}
			});
			
			//主治医生编码
			$('#zhuzhiyishi').combobox({
				url : '<%=basePath %>publics/consultation/queryLoginUserDept.action',
				valueField : 'jobNo',
				textField : 'name',
				multiple : false,
				editable : false,
				onSelect:function(record){
					$('#chargeDocName').val(record.name);
				}
			});
			//申请医生编码
			$('#shenqingyishi').combobox({
				url : '<%=basePath %>publics/consultation/queryLoginUserDept.action',
				valueField : 'jobNo',
				textField : 'name',
				multiple : false,
				editable : false,
				readonly:true,
				onLoadSuccess:function(){
					$('#applyDocName').val($('#shenqingyishi').combobox('getText'));
				}
				
			});
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
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodType'/>",
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
			
			var aa=$('#isaneFlgHidden').val();
			if(aa=='0'){
				$('#yes').attr("checked",'checked');
			}else{
				$('#no').attr("checked",'checked');
			}
			
			var lczd=$('#linchuang').combobox('getValue');
			if(lczd==''||lczd==null){
				$("#templateSubmit").attr("checked","checked");
				$('#lczds').show();
				 $('#linchuang').combobox({
					  disabled:true
						
				});
			}
				//给临床诊断绑定弹窗事件
				bindEnterEvent('linchuang',popWinToLinchuang,'easyui');
				//给血液成分绑定弹窗事件
				bindEnterEvent('xueyechengfen',popWinToXueyechengfen,'easyui');
			});
	
			//提交保存
			function submit() {
				var c=$("#templateSubmit");
				if(c.is(":checked")){
					var s = $('#lczdText').combobox('getText');
					$('#diagnoseName').val(s);
					$('#linchuang').combobox('setValue','');
				}else{
					var s = $('#linchuang').combobox('getText');
					$('#diagnoseName').val(s);
				}
				if($('#quantity').numberbox('getValue')==0){
					$.messager.alert('提示','预定血量不能为0，请重新填写');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					('#quantity').numberbox('setValue',0);
					return;
				}
				$('#baocun').form('submit',{
					url : "<%=basePath %>publics/InpatientApply/saveInpatientApply.action?state="+2,
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
			        		$.messager.alert("提示","保存成功");
			        		setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
			        		opener.location.reload();
				        	window.close();
			        	}else{
			        		$.messager.alert("提示","保存失败");
			        		opener.location.reload();
				        	window.close();
			        	}
			        	
			        },
					error : function(data) {
						$.messager.progress('close');
		        		$.messager.alert('提示','保存失败');
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
				
				var tempWinPath = "<%=basePath%>/popWin/popWinBusinessIcd10/toBusinessIcd10PopWinDB.action?textId=linchuang";
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
</script>	
</body>
</html>