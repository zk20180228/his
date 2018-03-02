<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>门诊会诊申请</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
	var empMap="";
	var deptMap="";
	var qwerId="";
	var mainId="";
	var mainId1="";
	var birthday="";
	var status="";//会诊状态（1申请状态，2状态）
	var empId="${sname}";
	var medicalrecordId="";
	var now="${now}"//当前时间
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
	$(function(){
		//加载模板类型下拉框
		$('#cnslmodType').combobox({    
		    url:'<%=basePath%>publics/consultation/queryModeList.action?menuAlias=${menuAlias}',    
		    valueField:'id',    
		    textField:'text',
		    multiple:false
		});
		qwerId=$('#idid').val();
		if(qwerId!=null && qwerId!=""){
			$('#savesave').hide();
			$('#deldel').show();
			$('#editedit').show();
		}else{
			$('#savesave').show();
			$('#editedit').hide();
		}
		
		modelTreesLoad("${sname}","${deptId}");
		//加载患者树
		patientTreeload();
		
		//会诊科室(下拉框)
		$('#cnslDeptcd').combobox({   
			url:'<%=basePath%>publics/consultation/queryDeptList.action?menuAlias=${menuAlias}',
		    valueField:'deptCode',    
		    textField:'deptName',
		    multiple:false,
		    required:true,
		    mode:'remote',
		    onLoadSuccess:function(){
		    	var code=$('#cnslDeptcd').combobox('getValue');		
				var data = $('#cnslDeptcd').combobox('getData');
				for(var i=0;i<data.length;i++){
					if(data[i].deptCode==code){
						$('#cnslDeptcd').combobox('setValue',data[i].deptCode);
					}
				}
		           }
		});
		
		bindEnterEvent('cnslDeptcd',popWinToDeptCnsl,'easyui');//绑定回车事件
		//申请科室(下拉框)
		$('#deptCode').combobox({   
			url:'<%=basePath%>publics/consultation/queryDeptList.action?menuAlias=${menuAlias}',  
		    valueField:'deptCode',    
		    textField:'deptName',
		    multiple:false,
		    editable:true,
		    required:true,
		    mode:'remote',
		    onSelect:function(data){
		    	$('#docCode').combobox('setValue',"");
		    	$('#docCode').combobox('reload',"<%=basePath%>publics/consultation/queryLoginUserDept.action?deptCode="+data.deptCode);
		    },
		    onLoadSuccess:function(){
		    	var code=$('#deptCode').combobox('getValue');		
				var data = $('#deptCode').combobox('getData');
				for(var i=0;i<data.length;i++){
					if(data[i].deptCode==code){
						$('#deptCode').combobox('setValue',data[i].deptCode);
					}
				    }
		           }
		});
		bindEnterEvent('deptCode',popWinToDept,'easyui');//绑定回车事件
		 //会诊医师(下拉框)
		$('#cnslDoccd') .combobox({    
		    url:'<%=basePath%>publics/consultation/queryLoginUserDept.action',    
		    valueField:'jobNo',    
		    textField:'name',
		    multiple:false,
		    editable:true,
		    required:true,
		    mode:'remote',
		    onLoadSuccess:function(){
		    	var code=$('#cnslDoccd').combobox('getValue');	
				var data = $('#cnslDoccd').combobox('getData');
				for(var i=0;i<data.length;i++){
					if(data[i].jobNo==code){
						$('#cnslDoccd').combobox('setValue',data[i].jobNo);
					}
				    }
		           }
	    });
		bindEnterEvent('cnslDoccd',popWinToEmployee,'easyui');//绑定回车事件
		 //申请医师(下拉框)
		$('#docCode') .combobox({    
			url:'<%=basePath%>publics/consultation/queryLoginUserDept.action',  
		    valueField:'jobNo',    
		    textField:'name',
		    multiple:false,
		    editable:true,
		    required:true,
		    mode:'remote',
		    onLoadSuccess:function(){
		    	var code=$('#docCode').combobox('getValue');	
				var data = $('#docCode').combobox('getData');
				for(var i=0;i<data.length;i++){
					if(data[i].jobNo==code){
						$('#docCode').combobox('setValue',data[i].jobNo);
					}
				    }
		           }
	    });
		bindEnterEvent('docCode',popWinToEmployees,'easyui');//绑定回车事件
		
			//渲染表单中的挂号专家
			$.ajax({
				url: "<c:url value='/publics/consultation/queryEmpMapPublic.action'/>",
				type:'post',
				success: function(empData) {
					empMap = empData;
				}
			});
		
		
			//渲染表单中的挂号科室
			$.ajax({
				url: "<c:url value='/publics/consultation/querydeptComboboxs.action'/>", 
				success: function(deptData) {
					deptMap = deptData;
				}
			});
	

	});
	/**
	   * 单选框赋值
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function onclickCreateOrderFlag(){
		if($('#createOrderFlag').is(':checked')){
			$('#createOrderFlag').val(1);
		}else{
			$('#createOrderFlag').val(2);
		}
	}
	
	function applyList(medicalrecordId){
		/**
		   * 会诊记录点击事件
		   * @author  tuchuanjiang
		   * @version 1.0
		   */
		$('#listlist').datagrid({
			url:"<%=basePath %>publics/consultation/queryConsultationList.action?menuAlias=${menuAlias}&medicalrecordId="+medicalrecordId,
			onClickRow: function (rowIndex, rowData) {
				$('#editform').form('clear');
				mainId=rowData.id,
				status=rowData.cnslStatus,
				$.ajax({
					url:"<%=basePath%>publics/consultation/queryConsultationById.action?mainId="+mainId,
					success: function(consultationList){
						var consultationList = consultationList;
						var medicalrecordId=consultationList.patientNo;
						$('#confirmDoccd').val(consultationList.confirmDoccd);
						$('#idid').val(consultationList.id);
						$('#status').val(consultationList.cnslStatus);
						$('#cnslDeptcd').combobox('setValue',consultationList.cnslDeptcd);
						$('#cnslDoccd').combobox('setValue',consultationList.cnslDoccd);
						$('#deptCode').combobox('setValue',consultationList.deptCode);
						$('#docCode').combobox('setValue',consultationList.docCode);
						$('#docCode').combobox('reload',"<%=basePath%>publics/consultation/queryLoginUserDept.action");
						$('#cnslDate').val(consultationList.cnslDate);
						$('#createUser').val(consultationList.createUser);
						$('#createDept').val(consultationList.createDept);
						$('#createTime').val(consultationList.createTime);
						$('#moEddt').val(consultationList.moEddt);
						$('#applyDate').val(consultationList.applyDate);
						$('#moStdt').val(consultationList.moStdt);
						$('#cnslNote').textbox('setValue',consultationList.cnslNote);
						//会诊流水号
						$('#cnslNO').val(consultationList.cnslNO);
						//病历号
						$('#patientNo').val(consultationList.patientNo);
						//会诊地点
						$('#location').textbox('setValue',consultationList.location);
						//实际会诊日
						$('#cnslExdt').val(consultationList.cnslExdt);
						//会诊结果
						$('#cnslRslt').textbox('setValue',consultationList.cnslRslt);
						//检查结果
						$('#cnslNote2').textbox('setValue',consultationList.cnslNote2);
						//会诊记录
						$('#cnslRecord').textbox('setValue',consultationList.cnslRecord);
						//初步诊断意见
						$('#cnslNote3').textbox('setValue',consultationList.cnslNote3);
						//会诊状态
						$('#applystate').val(consultationList.cnslStatus);
						if(consultationList.createOrderFlag=="1"){
							$('#createOrderFlag').prop("checked","checked");
						}
						if(consultationList.urgentFlag=='0'){
							$('#putong').prop("checked","checked");
						}else if(consultationList.urgentFlag=='1'){
							$('#jinji').prop("checked","checked");
						}
						if(consultationList.cnslKind=='0'){
							$('#yisheng').prop("checked","checked");
						}else if(consultationList.cnslKind=='1'){
							$('#keshi').prop("checked","checked");
						}else if(consultationList.cnslKind=='2'){
							$('#yuanwai').prop("checked","checked");
						}
						$.ajax({
							url: "<%=basePath%>publics/consultation/queryMZPatientInfo.action?medId="+medicalrecordId,
							success: function(datalist) {		
								$('#patientName').textbox('setValue',datalist[0].name);
								$('#reportSex').textbox('setValue',datalist[0].sexName);
								var ages=DateOfBirth(datalist[0].birthday);
								birthday=datalist[0].birthday;
								 //年龄
								if(ages.get("nianling")=="0"){
										$('#reportAge').textbox('setValue',"0");	
									}else{
										$('#reportAge').textbox('setValue',ages.get("nianling"));
									}
									 //年龄单位
									$('#reportAgeunit').text(ages.get('ageUnits'));
								$('#deptAddress').textbox('setValue',datalist[0].deptAddress);
							}
						});
						var ppp=$('#idid').val();
						if(ppp!=null && ppp!=""){
							$('#savesave').hide();
							$('#editedit').show();
							$('#deldel').show();
							if(status==1){
								$('#shenhe').show();
							}else{
								$('#shenhe').hide();
							}
						}else{
							$('#savesave').show();
							$('#editedit').hide();
						}
					}
				});
			}
		});
	}
	
	/**
	   * 单选框赋值
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function onclickurgentFlag(id){
		if($('#putong').is(':checked')){
			$('#urgentFlag').val(2);
		}else if($('#jinji').is(':checked')){
			$('#urgentFlag').val(1);
		}
	}
	/**
	   * 单选框赋值
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function onclickCnslKind(id){
		if(id==1){
			$('#cnslKind').val(2);
		}else if(id==2){
			$('#cnslKind').val(1);
		}else if(id==3){
			$('#cnslKind').val(3);
		}
	}
	/**
	   * 按钮显示隐藏处理方法
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function clickclick(type){
		var c=$("#templateSubmit");
		if(c.is(":checked")){
			$("#show_mod_tr").hide();
			$("#mod_td").show();
			$("#ppp1").html("");
			$("#ppp2").append(c);
			$("#cnslmodName").textbox({required:'required'}); 
		}else{
			$("#show_mod_tr").show();
			$("#mod_td").hide();
			$("#ppp2").html("");
			$("#ppp1").append(c);
			$("#cnslmodName").textbox({required:false}); 
		}
		return false;
	}
	/**
	   * 会诊申请保存
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function save(){
		//单选、复选框赋值
		if($('#createOrderFlag').is(':checked')){
			$('#createOrderFlag').val(1);
		}else{
			$('#createOrderFlag').val(2);
		}
		if($('#putong').is(':checked')){
			$('#urgentFlag').val(2);
		}else if($('#jinji').is(':checked')){
			$('#urgentFlag').val(1);
		}
		if($('#yuanwai').is(':checked')){
			$('#cnslKind').val(2);
		}else if($('#keshi').is(':checked')){
			$('#cnslKind').val(1);
		}else if($('#yisheng').is(':checked')){
			$('#cnslKind').val(3);
		}
		if($('#patientName').textbox('getValue')==""){
			$.messager.alert("提示","请选择要进行会诊申请的患者");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if($('#moStdt').val()==""||$('#moStdt').val()==null){
			$.messager.alert("提示","请选择授权日期！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if($('#cnslDate').val()==""||$('#cnslDate').val()==null){
			$.messager.alert("提示","请选择会诊日期！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if($('#applyDate').val()==""||$('#applyDate').val()==null){
			$.messager.alert("提示","请选择申请日期！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if($('#moEddt').val()==""||$('#moEddt').val()==null){
			$.messager.alert("提示","请选择截止日期！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if($('#cnslExdt').val()==""||$('#cnslExdt').val()==null){
			$.messager.alert("提示","请选择实际会诊日期！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if($('#templateSubmit').is(':checked')&&($('#cnslmodType').combobox("getValue")=="")){
			$.messager.alert("提示","请选择模板类型！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		var docname=$('#docCode').combobox('getText');
		$('#docName').val(docname);
		
		if($('#templateSubmit').is(':checked')&&status!=2){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			$('#editform').form('submit',{
				url : "<%=basePath %>publics/consultation/editTemplate.action",
				data: $('#editform').serialize(),
				dataType:'json',
				success:function(){
					$.messager.progress('close');
					$.messager.alert('提示','保存模板成功！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				},
				error:function(data){
					$.messager.progress('close');
					$.messager.alert('提示','保存模板失败，请重新填写信息！');
					
				}
			});	
		}
		if(status!=2){
			//实际会诊日
			$('#cnslExdt').val("");
			//会诊结果
			$('#cnslRslt').textbox('setValue','');
			//检查结果
			$('#cnslNote2').textbox('setValue','');
			//会诊记录
			$('#cnslRecord').textbox('setValue','');
			//初步诊断意见
			$('#cnslNote3').textbox('setValue','');
		}
		$('#editform').form('submit',{
			url : "<%=basePath %>publics/consultation/editConsultationList.action",
			data: $('#editform').serialize(),
			success:function(data){
				$.messager.progress('close');
				exportout(data);  
				$.messager.alert('提示','保存成功！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				status=1;
				modelTreesLoad("${sname}","${deptId}");
				//加载患者树
				patientTreeload();
				$('#editform').form('clear');
				
				applyList();
			},
			error:function(data){
				$.messager.alert('提示','保存失败，请重新填写信息！');
				
			}
		});		
	}
	/**
	   * 会诊审核
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function shenhe(){
		//单选、复选框赋值
		if($('#createOrderFlag').is(':checked')){
			$('#createOrderFlag').val(1);
		}else{
			$('#createOrderFlag').val(2);
		}
		if($('#putong').is(':checked')){
			$('#urgentFlag').val(2);
		}else if($('#jinji').is(':checked')){
			$('#urgentFlag').val(1);
		}
		if($('#yuanwai').is(':checked')){
			$('#cnslKind').val(2);
		}else if($('#keshi').is(':checked')){
			$('#cnslKind').val(1);
		}else if($('#yisheng').is(':checked')){
			$('#cnslKind').val(3);
		}
		var docname=$('#docCode').combobox('getText');
		$('#docName').val(docname);
		$.messager.progress({text:'保存中，请稍后...',modal:true});
		$('#editform').form('submit',{
			url : "<%=basePath %>publics/consultation/shenheConsultationList.action",
						success:function(data){
							$.messager.progress('close');
							if(data!=null&&data!=''){
							    exportout(data);  
								$.messager.alert('提示','审核成功！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								$('#editform').form('clear');
								status=1;
								applyList();
							}
						},
						error:function(data){
							$.messager.progress('close');
							$.messager.alert('提示','审核失败，请重新填写信息！');
							
						}
		});	
	}
	function exportTable(){
		var mid=$('#idid').val();
		var mid1=$('#patientNo').val();
		if(mid!=null&&mid!=""){
			exportout(mid,mid1);
		}else{
			$.messager.alert("保存","请先保存会诊申请信息或选择需要打印的申请信息");
			setTimeout(function(){
				$(".messager-body").window('close');
			},1500);
			return false;
		}
	}
	function exportout(mid,mid1){
		var state="${state}";
		var timerStr = Math.random();
		window.open ("<%=basePath%>publics/consultation/printMZConsultationById.action?randomId="+timerStr+"&mainId="+mid+"&fileName=HZSQDMZ",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	//状态显示渲染
	function functionStatus(value,row,index){
		if(value==2){
			return "确认";
		}else{
			return "申请";
		}
	}
	//渲染科室		
	function functionDept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	//渲染人员
	function functionEmp(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
	function functionOrderFlag(value,row,index){
			if(value==1){
				return "能开立医嘱";
			}else{
				return "不能开立医嘱";
			}
	}
	/**
	   * 删除患者记录
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function deldel(){
		if(mainId!=null && mainId!=""){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			$.ajax({
				url:"<%=basePath%>publics/consultation/deldelById.action?mainId="+mainId,
				success:function(){
					if(date.resCode=='success'){
						$.messager.progress('close');
						$.messager.alert('提示','成功删除');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						$('#editform').form('clear');
						status=1;
						applyList();
					}else{
						$.messager.progress('close');
						$.messager.alert('提示','删除失败');
					}
					
				},
				error:function(){
					$.messager.progress('close');
					$.messager.alert('提示','删除失败');
					
				}
			});
		}else{
			$.messager.alert('提示','请选择要删除的记录');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	/**
	   * 加载患者信息
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function patientTreeload(){
		//加载患者树
		$('#tDt').tree({
				url : '<%=basePath%>publics/consultation/completeWaitTree.action',
				animate : true,
				lines : true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if(node.children.length>0){
						if (node.children) {
							s += '&nbsp;<span style=\'color:blue\'>('
									+ node.children.length + ')</span>';
						}
					}
					return s;
				},onDblClick : function(node) {
					if(node.attributes.pid!='root'){
						medicalrecordId=node.id;
						$('#editform').form('clear');
						clickclick();
						$('#savesave').show();
			 			$('#deldel').hide();
						$('#editedit').hide();
						$('#shenhe').hide();
						$('#patientNo').val(medicalrecordId);
						//查询会诊申请记录列表展示
						applyList(medicalrecordId);
						$('#applyDate').val(now);
						$('#docCode').combobox('setValue',"");
						$('#docCode').combobox('setValue',empId);
						$('#deptCode').combobox('setValue',"${deptId}");
						//患者姓名
						$('#patientName').textbox('setValue',node.text);
						//性别
						$('#reportSex').textbox('setValue',node.attributes.sexName);
						var ages=DateOfBirth(node.attributes.birthday);
						birthday=node.attributes.birthday;
						 //年龄
						if(ages.get("nianling")=="0"){
								$('#reportAge').textbox('setValue',"0");	
							}else{
								$('#reportAge').textbox('setValue',ages.get("nianling"));
							}
							 //年龄单位
							$('#reportAgeunit').text(ages.get('ageUnits'));
						 //科室地点
						$('#deptAddress').textbox('setValue',node.attributes.deptAddress);
					}
				}
		}); 
	}
	/**
	   * 加载科室模板树
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function keshiTreeload(deptId){
		//加载科室模板树
		$('#templateTwo').tree({
			url:'<%=basePath%>publics/consultation/consultationTemplateTreeDept.action?deptId='+deptId,
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if(node.children.length>0){
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},onClick : function(node){
				$('#savesave').show();
	 			$('#deldel').hide();
				$('#editedit').hide();
				$('#shenhe').hide();
				clickclick();
			    var cnslDeptcd=node.id;
			    $.ajax({
					url: "<%=basePath%>publics/consultation/queryConsultationTemplateDept.action?inpatientConsultationModel.id="+cnslDeptcd,
					success: function(data) {		
						var  plist=data;
						$('#docCode').combobox('setValue',empId);
						$('#applyDate').val(now);
						$('#cnslNote').textbox('setValue',plist.cnslNote);
						$('#deptCode').combobox('setValue',plist.deptCode);
					}
				});
			}
		});
	}
	/**
	   * 加载个人模板树
	   * @author  tuchuanjiang
	   * @version 1.0
	   */
	function yishengTreeload(sname){
		//加载个人模板树
		$('#templateOne').tree({
			url:'<%=basePath%>publics/consultation/consultationTemplateTreeEmp.action?sname='+sname,
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if(node.children.length>0){
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},onClick : function(node){
				clickclick();
				$('#shenhe').hide();
				$('#savesave').show();
	 			$('#deldel').hide();
				$('#editedit').hide();
			    var id=node.id;
			    $.ajax({
					url: "<%=basePath%>publics/consultation/queryConsultationTemplateDept.action?inpatientConsultationModel.id="+id,
					success: function(data) {		
						var  plist=data;
						$('#applyDate').val(now);
						$('#docCode').combobox('setValue',plist.docCode);
						$('#cnslNote').textbox('setValue',plist.cnslNote);
					}
				});
			}
		});
	}
	function modelTreesLoad(sname,deptId){
		$('#templateTwo').tree({
			url:'<%=basePath%>publics/consultation/consultationTemplateTrees.action?sname='+sname+'&deptId='+deptId,
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if(node.children.length>0){
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},onClick : function(node){
				 var father = $(this).tree("getParent",node.target);
				if(father!=null){
					if(father.id=='1'){
						clickclick();
						$('#shenhe').hide();
						$('#savesave').show();
			 			$('#deldel').hide();
						$('#editedit').hide();
					    var id=node.id;
					    $.ajax({
					    	url: "<%=basePath%>publics/consultation/queryConsultationTemplateDept.action?inpatientConsultationModel.id="+id,
							success: function(data) {
								var  plist=data;
								$('#applyDate').val(now);
								$('#docCode').combobox('setValue',plist.docCode);
								$('#cnslNote').textbox('setValue',plist.cnslNote);
							}
					    });
					}
					else if(father.id=='2'){
						$('#savesave').show();
			 			$('#deldel').hide();
						$('#editedit').hide();
						$('#shenhe').hide();
						clickclick();
					    var cnslDeptcd=node.id;
					    $.ajax({
							url: "<%=basePath%>publics/consultation/queryConsultationTemplateDept.action?inpatientConsultationModel.id="+cnslDeptcd,
							success: function(data) {		
								var  plist=data;
								$('#docCode').combobox('setValue',empId);
								$('#applyDate').val(now);
								$('#cnslNote').textbox('setValue',plist.cnslNote);
								$('#deptCode').combobox('setValue',plist.deptCode);
							}
						});
					}
				}
			
			}
			
		})		
	}
	//加载模式窗口
	function AdddilogModel(id,title,url,width,height) {
		$('#'+id).dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,
		    href: url,    
		    modal: true   
		});    
	}
	 /**
	   * 回车弹出会诊科室弹框
	   * @author  zhuxiaolu
	   * @param deptIsforregister 是否是挂号科室 1是 0否
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToDeptCnsl(){
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=cnslDeptcd&deptType=C,I,T";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		}
	
	
	   /**
		   * 回车弹出申请科室弹框
		   * @author  zhuxiaolu
		   * @param deptIsforregister 是否是挂号科室 1是 0否
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToDept(){
			    $('#docCode').combobox('setValue',"");
				var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=deptCode&deptType=C,I,T";
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
			}
	   /**
		   * 回车弹出会诊医师弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployee(){
				var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=cnslDoccd&employeeType=1";
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
				
			}
   		/**
		   * 回车弹出申请医师弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployees(){
			   var deptid=$('#deptCode').textbox('getValue');
			   if(deptid){
					var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=docCode&employeeType=1&deptIds="+deptid;
					window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
					
					
				}else{
					$.messager.alert("提示","请选择选择科室");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
		 }
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
.panel-header{
	border-top:0;
}
</style>
</head>
<body>
<div id="divLayout" class="easyui-layout" fit=true data-options="fit:true">
	<div data-options="region:'west',split:true" style="width:15%;height:100%;" title="门诊患者">
		<ul id="tDt"></ul>
	</div>
	<div data-options="region:'center',split:true,border:false" style="width:85%;height:100%;">
		<div class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'west',title:'会诊模板',split:true" style="width:10%;height: ">
		    	<ul id="templateTwo"></ul>
		    </div>  
			<div data-options="region:'center',title:'会诊申请单',split:true" style="width:85%;height:50%;">
				<form id="editform" method="post" style="width: 100%;height: 100%;">
					<div class="easyui-layout" data-options="fit:true,border:false">
						<div data-options="region:'north',border:false" style="width:100%;height: 100px;">
							<div class="easyui-layout" data-options="fit:true">
									<div data-options="region:'north',border:false" style="width: 100%;height: 100%;"> 
										<div class="easyui-layout" data-options="fit:true"> 
											<div data-options="region:'west',border:false" style="width:80%;">
												<table style="width:100%;height: 100%;text-align: center">
													<tr>
														<td style="text-align: center" rowspan="5" colspan="4" ><font size="6" class="consultationListFontSize">会诊单</font></td>
													</tr>
												</table>
											</div>
											<div data-options="region:'center',border:false" style="width:20%;">
												<table style="width:100%;height: 100%;text-align: left">
													 <tr>
														<td >
															<input type="hidden" id="bedNo" name="bedNo" >
															<input type="hidden" id="idid" name="id" >
															<input type="hidden" id="patientNo" name="patientNo" >
															<input id="urgentFlag" type="hidden" name="urgentFlag" value="${urgentFlag}" >
															<input type="radio" name="urgentFlagq" id="putong" onclick="javascript:onclickurgentFlag(0)">普通会诊
															<input type="radio" name="urgentFlagq" id="jinji" onclick="javascript:onclickurgentFlag(1)">紧急会诊
														</td>
													</tr>
													<tr>
														<td >
															<input type="hidden" name="cnslKind" id="cnslKind"/>
															<input type="radio" name="cnslKindd" id="yuanwai"  onclick="javascript:onclickCnslKind(1)">院外会诊
															<input type="radio" name="cnslKindd" id="keshi"  onclick="javascript:onclickCnslKind(2)">科室会诊
															<input type="radio" name="cnslKindd" id="yisheng"  onclick="javascript:onclickCnslKind(3)">医生会诊
														</td>
													</tr>
													<tr>
														<td>
															<input type="checkbox" name="createOrderFlag" id="createOrderFlag" onclick="onclickCreateOrderFlag()">能否开立医嘱       
														</td>
													</tr>
												</table>
											</div>
									</div> 
								</div> 
							</div>
						</div>
						<div data-options="region:'center',border:false" style="width:100%;height:100%;padding:0px;margin:0px;" align="center">
									<input id="inpatientNo" name="inpatientNo" type="hidden" >
								<input id="applystate" name="cnslStatus" type="hidden">
								<input id=confirmDoccd name="confirmDoccd" type="hidden">
								<table class="honry-table" cellpadding="10" cellspacing="10" style="width:99%;height:100%;">
									<tr>
										<input id="docName" name="docName" type="hidden">
										<input id="cnslNO" name="cnslNO" type="hidden">
										<input id="createTime" name="createTime" type="hidden">
										<input id="createUser" name="createUser" type="hidden">
										<input id="createDept" name="createDept" type="hidden">
										<td class="honry-lable">
											患者姓名：</td>
										<td>
											<input id="patientName" class="easyui-textbox"  readonly="readonly" />
										</td>
										<td class="honry-lable">
											性别：</td>
										<td>
											<input id="reportSex" class="easyui-textbox" readonly="readonly"/>
										</td>
										
										<td class="honry-lable">
											年龄：</td>
										<td>
											<input id="reportAge" class="easyui-textbox"  readonly="readonly"/>
											<span id="reportAgeunit" readonly="readonly"></span>
										</td>
										
										<td class="honry-lable">
											楼层：</td>
										<td>
											<input id="deptAddress" class="easyui-textbox"  readonly="readonly"/>
										</td>
									</tr>
									<tr>
										<td class="honry-lable">
											房间：</td>
										<td>
											<input id="bedwardName" class="easyui-textbox"  readonly="readonly"/>
										</td>
										<td class="honry-lable">
											床号：</td>
										<td>
											<input id="bedName" class="easyui-textbox"  readonly="readonly" />
										</td>
										<td class="honry-lable">
											会诊地点：</td>
										<td>
											<input id="location" class="easyui-textbox" name="location" data-options="required:true"/>
										</td>
										<td class="honry-lable">授权日期：</td>
										<td>
											<input id="moStdt" name="moStdt" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"/>
										</td>
									</tr>
									<tr>
										<td class="honry-lable">会诊科室：</td>
										<td>
											<input id="cnslDeptcd"  name="cnslDeptcd" data-options="required:true"/>
											<a href="javascript:delSelectedData('cnslDeptcd,cnslDoccd');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
										</td>
										<td class="honry-lable">会诊医师：</td>
										<td>
											<input id="cnslDoccd"  name="cnslDoccd" class="easyui-combobox" data-options="required:true"/>
											<a href="javascript:delSelectedData('cnslDoccd');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
										</td>
										<td class="honry-lable">申请科室：</td>
										<td>
											<input id="deptCode"  name="deptCode" data-options="required:true"/>
											<a href="javascript:delSelectedData('deptCode,docCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
										</td>
										<td class="honry-lable">
											  申请医师：</td>
										<td> 
											<input id="docCode" name="docCode" class="easyui-combobox" data-options="required:true"/>
											<a href="javascript:delSelectedData('docCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
										</td>
									</tr>
									<tr>
										<td class="honry-lable">会诊日期：</td>
										<td>
                                                <input id="cnslDate"  name="cnslDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1900-01-01 00:00:00',maxDate:'2099-12-31 23:59:59'})"  style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"/>
										</td>
										<td class="honry-lable">申请日期：</td>
										<td>
                                                <input id="applyDate"  name="applyDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1900-01-01 00:00:00',maxDate:'2099-12-31 23:59:59'})"  style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"/>
										</td>
										<td class="honry-lable">截止日期：</td>
										<td>
                                                <input id="moEddt" name="moEddt" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1900-01-01 00:00:00',maxDate:'2099-12-31 23:59:59'})"  style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"/>
										</td>
										<td class="honry-lable">实际会诊日：</td>
										<td colspan="5"> 
											<input id="cnslExdt" name="cnslExdt" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'1900-01-01 00:00:00',maxDate:'2099-12-31 23:59:59'})"  style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"/>
										</td>
									</tr>
									<tr style="height:40px;" id="show_mod_tr">
												<td class="honry-lable">保存模板</td>
												<td colspan="7" id="ppp1" style="border-right: 1px solid #95b8e7;">
													<input type="checkbox" id="templateSubmit" name="templateSubmit" onclick="clickclick()">
												</td>
									</tr>
									<tr style="height:40px;display: none;" id="mod_td">
										<td class="honry-lable">保存模板</td>
										<td id="ppp2">
										</td>
										<td class="honry-lable modbs">模板名称：</td>
										<td class="modbs"><input class="easyui-textbox" id="cnslmodName" name="inpatientConsultationModel.cnslmodName" ></td>
										<td class="honry-lable modbs">模板类型：</td>
										<td class="modbs" colspan="3">
                                                   <input id="cnslmodType"  name="inpatientConsultationModel.cnslmodType"  style="width:150px;"/> 
										</td>
									</tr>
								<tr>
									<td class="honry-lable">会诊摘要：</td>
									<td colspan = "7"> 
											<input  id="cnslNote" name="cnslNote" class="easyui-textbox" data-options="multiline:true"  style="width:900px;height:100px">
									</td>
									</tr>
									<tr>
										<td class="honry-lable">会诊结果：</td>
										<td  colspan="7">
											<input id="cnslRslt" class="easyui-textbox"  name="cnslRslt" data-options="multiline:true"   style="width:900px;height:100px"/>
										</td>
									</tr>
									<tr>
										<td class="honry-lable">检查结果：</td>
										<td  colspan="7">
									    	<input id="cnslNote2" name="cnslNote2"  class="easyui-textbox"  data-options="multiline:true"  style="width:900px;height:100px"/>
										</td>
									</tr>
									<tr>
										<td class="honry-lable">会诊记录：</td>
										<td colspan="7"> 
											<input id="cnslRecord" name="cnslRecord"  class="easyui-textbox" data-options="multiline:true"  style="width:900px;height:100px"/>
										</td>
									</tr>
									<tr>
										<td class="honry-lable">初步诊断意见：</td>
										<td colspan="7">
											<input id="cnslNote3" name="cnslNote3" class="easyui-textbox" data-options="multiline:true"  style="width:900px;height:100px"/>
										</td>
									</tr>		
							</table>
							<table align="center">
									<tr align="center">
										<td style="align:'right'">
											<shiro:hasPermission name="${menuAlias}:function:print">
												<a href="javascript:void(0)"  id="exportTable"  class="easyui-linkbutton" onclick="exportTable()" data-options="iconCls:'icon-printer'">打&nbsp;印&nbsp;</a>&nbsp;&nbsp;
											</shiro:hasPermission>
											<shiro:hasPermission name="${menuAlias}:function:save">
												<a href="javascript:void(0)"  id="savesave" style="display: none;" class="easyui-linkbutton" onclick="save()" data-options="iconCls:'icon-save'">保&nbsp;存&nbsp;</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="${menuAlias}:function:delete">
												<a href="javascript:void(0)"  id="deldel" style="display: none;" class="easyui-linkbutton" onclick="deldel()" data-options="iconCls:'icon-delete'">删&nbsp;除&nbsp;</a>&nbsp;&nbsp;
											</shiro:hasPermission>
											<shiro:hasPermission name="${menuAlias}:function:save">
												<a href="javascript:void(0)"  id="editedit" style="display: none;" class="easyui-linkbutton" onclick="save()" data-options="iconCls:'icon-edit'" >修&nbsp;改&nbsp;</a>&nbsp;&nbsp;
											</shiro:hasPermission>
											<shiro:hasPermission name="${menuAlias}:function:save">
												<a href="javascript:void(0)"  id="shenhe" style="display: none;" class="easyui-linkbutton" onclick="shenhe()" data-options="iconCls:'icon-save'" >审&nbsp;核&nbsp;</a>&nbsp;&nbsp;
											</shiro:hasPermission>
										</td>
									</tr>
							</table>
						</div>
					</div>
				</form>
			</div>
			<div  data-options="region:'south',title:'会诊记录',split:true" style="width:100%;height:35%;">
				<input type="hidden" id="status" name="status"/>
				<table id="listlist" class="easyui-datagrid" style="width:100%;"   
			        data-options="fitColumns:true,singleSelect:true,fit:true,border:false">   
					<thead>   
				        <tr>   
				            <th data-options="field:'cnslDeptcd',width:100,align:'center',formatter:functionDept">会诊科室</th>   
				            <th data-options="field:'cnslDoccd',width:100,align:'center',formatter:functionEmp">会诊专家</th>   
				            <th data-options="field:'docCode',width:100,align:'center',formatter:functionEmp">申请人</th>   
				            <th data-options="field:'applyDate',width:100,align:'center'">申请日期</th>   
					        <th data-options="field:'cnslNote3',width:200,align:'center'">会诊原因及病理诊断</th>   
				            <th data-options="field:'cnslRslt',width:100,align:'center'">会诊结果</th>   
				            <th data-options="field:'cnslStatus',width:100,align:'center',formatter:functionStatus">状态</th>   
				            <th data-options="field:'createOrderFlag',width:100,align:'center',formatter:functionOrderFlag">能否开立医嘱</th>   
				            <th data-options="field:'confirmDoccd',width:100,align:'center',formatter:functionEmp">审核人</th>   
				        </tr>   
					</thead>   
				</table>  
			</div> 
		</div>
	</div>
</div>
</body>
</html>