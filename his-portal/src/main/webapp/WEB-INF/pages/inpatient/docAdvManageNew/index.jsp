<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医嘱管理（新）</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/themes/advice/advice.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/themes/advice/inpatientAdvice.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
	var sexMap=new Map();//性别Map
	var implDepartmentMap = "";//执行科室Map
	var colorInfoUnitsMap = new Map();//单位Map
	var usemodeMap = "";//用法Map
	var colorInfoCheckpointMap = new Map();//检查部位Map
	var colorInfoSampleTeptMap = new Map();//样本类型Map
	var frequencyMap = "";//频次map
	var drugdoseunitMaps = "";//剂量单位Map
	var drugpackagingunitMap = "";//包装单位Map
	var mindataMaps = "";//最小单位Map
	var nonmedicineencodingMap="";//非药品单位Map
	var sign;//判断是长期还是临时
	var	freeMap=new Map();//频次map
	var id="";
	//查看超级管理员是否选择登录科室
	var currentLoginDept="${currentLoginDept}";
	$(function(){
		//医嘱  历史医嘱  选项卡
		$('#tt1').tabs({
			onSelect:function(title,index){
				if(index==1){
					$('#reformAdviceBtn').menubutton('disable');//重整医嘱
					var html = $('#hisAdviceTabDivId').html();
					if(html==''){
						var inpatientNo = null;
						var node = $('#tDt').tree('getSelected');
						if(node==null){
						}else{
							if(node.attributes.inpatientNo==""||node.attributes.inpatientNo==null){
							}else{
								inpatientNo=node.attributes.inpatientNo;
							}
						}
						$('#tt1').tabs('getTab',1).panel('refresh','<%=basePath%>inpatient/docAdvManage/hisAdvice.action');
					}
					$('#adPrintAdviceBtn').linkbutton('enable');
				}else if(index==2){
					$('#reformAdviceBtn').menubutton('disable');//重整医嘱
					$('#adPrintAdviceBtn').linkbutton('disable');//打印医嘱
					var html = $('#drugTabDivId').html();
					if(html==''){
						$('#tt1').tabs('getTab',2).panel('refresh','<%=basePath%>inpatient/docAdvManage/drug.action');
					}
					$('#adPrintAdviceBtn').linkbutton('disable');
				}else if(index==3){
					$('#reformAdviceBtn').menubutton('disable');//重整医嘱
					$('#adPrintAdviceBtn').linkbutton('disable');//打印医嘱
					var html = $('#unDrugTabDivId').html();
					if(html==''){
						$('#tt1').tabs('getTab',3).panel('refresh','<%=basePath%>inpatient/docAdvManage/unDrug.action');
					}
				}else if(index==0){
					var inpatientId = $("#inpatientId").val();
					if(inpatientId==null||inpatientId==''){
						return;
					}
					$('#reformAdviceBtn').menubutton('enable');//重整医嘱
					$('#adPrintAdviceBtn').linkbutton('enable');//打印医嘱
				}else if(index==4){
					var html = $('#lisView').html();
					if(html==''){
						$('#tt1').tabs('getTab',4).panel('refresh','<%=basePath%>inpatient/docAdvManage/lisView.action?id='+id);
					}
					$('#reformAdviceBtn').menubutton('enable');//重整医嘱
					$('#adPrintAdviceBtn').linkbutton('enable');//打印医嘱
				}else if(index==5){
					var html = $('#pacsView').html();
					if(html==''){
						$('#tt1').tabs('getTab',5).panel('refresh','<%=basePath%>inpatient/docAdvManage/pacsView.action');
					}
					$('#reformAdviceBtn').menubutton('enable');//重整医嘱
					$('#adPrintAdviceBtn').linkbutton('enable');//打印医嘱
				}else if(index==6){
					var html = $('#medicalRecord').html();
					if(html==''){
						$('#tt1').tabs('getTab',6).panel('refresh','<%=basePath%>inpatient/docAdvManage/viewSelectEme.action?menuAlias=${menuAlias}');
					}
					$('iframe').attr('src',src="<%=basePath %>emrs/writeMedRecord/toEmrMainListViewForInpatient.action?menuAlias=${menuAlias}");
					$('#reformAdviceBtn').menubutton('enable');//重整医嘱
					$('#adPrintAdviceBtn').linkbutton('enable');//打印医嘱
				}else if(index==7){
					var html = $('#outpatientDetail').html();
					if(html==''){
						$('#tt1').tabs('getTab',7).panel('refresh','<%=basePath%>inpatient/docAdvManage/outpatientDetail.action?menuAlias=${menuAlias}');
					}
				}
				
			}
		});
		//初始化页面  医嘱选项卡
		$('#tt1').tabs('getSelected').panel('refresh','<%=basePath%>inpatient/docAdvManage/advice.action');
		setTimeout(function(){
			var html = $('#hisAdviceTabDivId').html();
			if(html==''){
				$('#tt1').tabs('getTab',1).panel('refresh','<%=basePath%>inpatient/docAdvManage/hisAdvice.action');
			}
		},100);
		setTimeout(function(){
			var html = $('#drugTabDivId').html();
			if(html==''){
				$('#tt1').tabs('getTab',2).panel('refresh','<%=basePath%>inpatient/docAdvManage/drug.action');
			}
		},200);
		setTimeout(function(){
			var html = $('#unDrugTabDivId').html();
			if(html==''){
				$('#tt1').tabs('getTab',3).panel('refresh','<%=basePath%>inpatient/docAdvManage/unDrug.action');
			}
		},300);
		setTimeout(function(){
			var html = $('#lisView').html();
			if(html==''){
				$('#tt1').tabs('getTab',4).panel('refresh','<%=basePath%>inpatient/docAdvManage/lisView.action?id='+id);
			}
		},400);
		setTimeout(function(){
			var html = $('#pacsView').html();
			if(html==''){
				$('#tt1').tabs('getTab',5).panel('refresh','<%=basePath%>inpatient/docAdvManage/pacsView.action');
			}
		},500);
		if(currentLoginDept==null||currentLoginDept==''){
			setTimeout(function(){
				var html = $('#medicalRecord').html();
				if(html==''){
					$('#tt1').tabs('getTab',6).panel('refresh','<%=basePath%>inpatient/docAdvManage/viewSelectEme.action?menuAlias=${menuAlias}');
				}
			},600);
		}
		setTimeout(function(){
			var html = $('#outpatientDetail').html();
			if(html==''){
				$('#tt1').tabs('getTab',7).panel('refresh','<%=basePath%>inpatient/docAdvManage/outpatientDetail.action?menuAlias=${menuAlias}');
			}
		},700);
		//名称下拉
		$('#adProjectNameTdId').combogrid('disable');
		
		//加载患者树(左侧患者树)
		$('#tDt').tree({
				url : '<%=basePath%>inpatient/doctorAdvice/treeDoctorAdvice.action',
				method : 'post',
				animate : true, 
				lines : true,
				onlyLeafCheck:true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if(node.attributes.dateState!=null&&node.attributes.dateState!=''){
						s += node.attributes.dateState;
					}
					if(node.attributes.leaveFlag!=null&&node.attributes.leaveFlag!=''){
						s += node.attributes.leaveFlag;
					} 	
					if (node.children.length>0) {					 						
						s += '<span style=\'color:blue\'>('
								+ node.children.length + ')</span>';					
					}					
					return s;
				},onClick : function(node){	
					//加载渲染
					loadAjax();
					if(node.attributes.pid=='root'){
						return;
					}
					$("#adNameId").text('');	
					$("#id").val(node.id);	
					$("#adSexId").text('');
					$("#adFreeCostId").text('');
					$("#inpatientNo").val('');
					$("#patientNo").val('');
					$("#deptCode").val('');
					$("#nurseCellCode").val('');
					$("#babyFlag").val('');
					$("#pid").val('');
					$("#createOrderFlag").val('');
					$("#adPactId").text('');
					$("#adNameId").text(node.attributes.name);
					//性别渲染
					$.ajax({
						url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",		
						data:{type:"sex"},
						type:'post',
						success: function(data) {
							var v = data;
							for(var i=0;i<v.length;i++){
								sexMap.put(v[i].encode,v[i].name);
							}
							$("#adSexId").text(sexMap.get(node.attributes.reportSex));
						}
					});
					var freeCost = 0;
					freeCost = node.attributes.freeCost
					$("#adFreeCostId").text(parseFloat(freeCost).toFixed(2));//余额
					if(node.attributes.freeCost<"0"){
						$("#adFreeCostId").css('color','red');
					}else if(node.attributes.freeCost>"0"){
						$("#adFreeCostId").css('color','black');
					}
					var totCost = node.attributes.totCost;
					$("#adTotCostId").text(parseFloat(totCost).toFixed(2));//费用总额
					$("#adPrepayCostId").text(node.attributes.prepayCost);//预交金总额
					$("#inpatientNo").val(node.attributes.inpatientNo);
					$("#patientNo").val(node.attributes.medicalrecordId);
					$("#deptCode").val(node.attributes.deptCode);
					$("#nurseCellCode").val(node.attributes.nurseCellCode);
					$("#babyFlag").val(node.attributes.babyFlag);
					$("#pid").val(node.attributes.pid);//判断是哪个节点的患者
					$("#createOrderFlag").val(node.attributes.createOrderFlag);
					$('#adPrintAdviceBtn').menubutton('enable');//打印医嘱
					var ycf = document.getElementById("adProjectDivId").style.display;
					if(ycf=="none"){
						$('#reformAdviceBtn').menubutton('enable');//重整医嘱
					}else{
						$('#reformAdviceBtn').menubutton('disable');//重整医嘱
					}
					if(node.attributes.pactCode=='自费'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','red');
					}
					if(node.attributes.pactCode=='省离休干部'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','green');
					}
					if(node.attributes.pactCode=='河南省医疗保险'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','blue');
					}
					if(node.attributes.pactCode=='郑州市医疗保险'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','#CDCD00');
					}
					if(node.attributes.pactCode=='省生育保险'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','orange');
					}
					if(node.attributes.pactCode=='郑州铁路医疗保险'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','grey');
					}
					if(node.attributes.pactCode=='省保健局保险'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','pink');
					}
					if(node.attributes.pactCode=='省级新农合平台'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','purple');
					}
					if(node.attributes.pactCode=='新农合【自费】'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','brown  ');
					}
					if(node.attributes.pactCode=='能源化工医保'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','maroon');
					}
					if(node.attributes.pactCode=='异地就医(试点)'){
						$("#adPactId").text(node.attributes.pactCode);
						$("#adPactId").css('color','lavender');
					}
					$("#inpatientId").val(node.id);
					id=$("#inpatientId").val();
					inpatientNo=node.attributes.inpatientNo;					
					var qqs = $('#tt1').tabs('getSelected');				
					var tabs = qqs.panel('options');
					if(tabs.title=='医嘱'){
						if($("#comboboxId").val()==1){//开立状态
							var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){
								recordId = '02';
								loadTabs(inpatientNo,recordId);
							}else{
								recordId = '06';
								loadTabs(inpatientNo,recordId);
							}								
						}else{
							recordId = null;
							$('#infolistA').datagrid({
								data: []
							});
							loadTabs(inpatientNo,recordId);
						}
					}else if(tabs.title=='历史医嘱'){//历史医嘱
						var qqb = $('#tttb').tabs('getSelected');				
						var tabb = qqb.panel('options');
						if(tabb.title=='长期医嘱'){							
							loadDatagridC(1,inpatientNo,null);
						}else{							
							loadDatagridD(0,inpatientNo,null);		
						}
					}else if(tabs.title=='电子病历'){//病案首页
						$('#tt1').tabs('getTab',6).panel('refresh','<%=basePath%>inpatient/docAdvManage/viewSelectEme.action?menuAlias=${menuAlias}');
					}else if(tabs.title=='LIS查询'){//LIS查询
						<%-- $('#tt1').tabs('getTab',4).panel('refresh','<%=basePath%>inpatient/lisAction/outpatientAdviceLisLis.action?id='+$("#inpatientId").val()); --%>
						$('#lisPatEdId').datagrid({
							striped:true,
							border:true,
							checkOnSelect:true,
							selectOnCheck:false,
							singleSelect:true,
							pagination:true,
							pageSize:20,
							pageList:[20,40,80],
							fit:true,
							url:"<%=basePath%>inpatient/lisAction/outpatientAdviceLisLis.action",
							queryParams:{id:$("#inpatientId").val()},
							onLoadSuccess:function(data){
								$(this).datagrid('selectRow',0);
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
							onSelect:function(rowIndex, rowData){
								if(rowData){
									$('#lisInfoEdId').datagrid({
										url:"<%=basePath%>inpatient/lisAction/outpatientLisDetailLis.action",
										queryParams: {
											id: rowData.INSPECTION_ID
										}
									});
								}else{
									$('#lisInfoEdId').datagrid('loadData',[]);
								}
							}
						})
					}
				},onContextMenu : function(e, node) {
					e.preventDefault();
					$(this).tree('select',node.target);					
					var node = $('#tDt').tree('getSelected');
					if(node!=null){
						if(node.attributes.inpatientNo!=null){
							$('#patientinfoTree').css("display","block");
							$('#mm').menu('show',{
								left: e.pageX,
								top: e.pageY
							});	
						}
					}
				}
		});
		if(currentLoginDept==null||currentLoginDept==''){
			//加载发药科室
			$('#pharmacyCombobox').combobox({    
			    url:'<%=basePath%>publics/putGetDrug/PGDjsonForPutDeptID.action',
			    valueField:'id',    
			    textField:'deptName',
			    multiple:false,
			    editable:false,
			    onLoadSuccess: function (data) {
			    	if($('#pharmacyInputId').val()!=null&&$('#pharmacyInputId').val()!=''){
			    		$('#pharmacyCombobox').combobox('select', $('#pharmacyInputId').val());
			    	}else if (data.length > 0) {
	                    $('#pharmacyCombobox').combobox('select', data[0].id);
	                }
		        },
		        onSelect:function(record){
		        	$.post("<%=basePath%>inpatient/docAdvManage/savePharmacyInfo.action",
					        {"pharmacyId":record.id},
					        function(data){
					        	var dataMap = data;
					   			if(dataMap.resMsg=="error"){
					   				$.messager.alert('提示',dataMap.resCode);
					        	}else if(dataMap.resMsg=="success"){
					        		$('#pharmacyInputId').val(record.id);
					        		$('#pharmacyInputName').val(record.deptName);
					        	}else{
					        		$.messager.alert('提示',"未知错误,请联系管理员！");					     
					        	}
					   		}
					);
		        }
			});
		}
		$('#docAdvTypeFilt').combobox({  
			onSelect:function(record){
				recordId=record.id;	
				var qq = $('#tt1').tabs('getSelected');				
				var tab = qq.panel('options');
				if(inpatientNo==null||inpatientNo==''){
					$.messager.alert('提示',"请选择患者！");	
					return false;
				}else{
				if(tab.title=='医嘱'){
					var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){
						loadDatagridA(1,inpatientNo,recordId);
					}else if(tab.title=='临时医嘱'){
						loadDatagridB(0,inpatientNo,recordId);
					}
				}else if(tab.title=='历史医嘱'){
					var qq = $('#tttb').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='临时医嘱'){
						var node = $('#tDt').tree('getSelected');
						if(node==null){
						}else{
							if(node.attributes.inpatientNo==""||node.attributes.inpatientNo==null){
							}else{
								loadDatagridD(0,node.attributes.inpatientNo,recordId);	
							}
						}
			    	}else{
			    		var node = $('#tDt').tree('getSelected');
						if(node==null){
						}else{
							if(node.attributes.inpatientNo==""||node.attributes.inpatientNo==null){
							}else{
								loadDatagridC(1,node.attributes.inpatientNo,recordId);	
							}
						}
			    	}
				}
			}
		}
		});
	});
	
	function adStartAdvice(){
		var pid = $("#pid").val();
		$('#reformAdviceBtn').menubutton('disable');//重整医嘱
		var createOrderFlag = $("#createOrderFlag").val();
		if(pid=='3'&&createOrderFlag=='0'){
			$.messager.alert('提示',"该患者会诊申请未允许开立医嘱!");
			setTimeout(function(){
				$(".messager-body").window('close');
			 },3500);
			return; 
		}
		var inpatientId = $("#inpatientId").val();
		if(inpatientId==null||inpatientId==''){
			$.messager.alert('提示',"患者信息不存在,请点击获取患者开立医嘱!");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
		var pharmacyInputId = $('#pharmacyInputId').val();
		if(pharmacyInputId==null||pharmacyInputId==''){
			$.messager.alert('提示',"请选择药房!");
			setTimeout(function(){
				$(".messager-body").window('close');
			 },3500);
			return;
		}	
		$('#tt1').tabs('select','医嘱');
		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='长期医嘱'){				
			loadDatagridA(1,inpatientNo,'02');
			$("#long").show();
			$("#tem").hide();
			$('#docAdvTypeFilt').combobox('disable');
			$('#comboboxId').val(1);
			var rowsA = $('#infolistA').datagrid('getRows');
			if(rowsA!=null&&rowsA.length>0){
				var leng = rowsA.length;
				for(var i=0;i<leng;i++){
					$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowsA[0]));
				}
			}	
			$('#longDocAdvType').combobox('disable');
		}else if(tab.title=='临时医嘱'){
			loadDatagridB(0,inpatientNo,'06');
			$('#temDocAdvType').combobox().next('span').find('input').focus();
			$("#tem").show();
			$("#long").hide();
			$('#docAdvTypeFilt').combobox('disable');
			$('#comboboxId').val(1);
			var rows = $('#infolistB').datagrid('getRows');
			if(rows!=null&&rows.length>0){
				var leng = rows.length;
				for(var i=0;i<leng;i++){
					$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',rows[0]));
				}
			}	
		}								 					 
		$('#adEndAdviceBtn').menubutton('enable');//退出开立
		$('#adSaveAdviceBtn').menubutton('enable');//保存医嘱
		$('#adDelAdviceBtn').menubutton('enable');//删除医嘱
		$('#adStopAdviceBtn').menubutton('enable');//停止医嘱
		var userid=$("#userid").val();
		$.ajax({
			url: "<%=basePath%>inpatient/docAdvManage/queryAuditInfo.action",		
			data:{userId:userid},
			type:'post',
			success: function(auditdata) {
				if(auditdata>0){
					$('#adAuditAdviceBtn').menubutton('enable');//审核医嘱
				}
			}
		});
		$('#adHerbMedicineBtn').menubutton('enable');//草药查询
		$('#adAddGroupBtn').menubutton('enable');//添加组套
		$('#adCancelGroupBtn').menubutton('enable');//取消组套
		$('#adSaveGroupBtn').menubutton('enable');//保存组套
		
		
		$('#adStackTree').tree({    
			url:"<%=basePath%>nursestation/nurseCharge/stackAndStackInfoForTree.action",
		 	queryParams:{drugType:"2",type:"1"},
		    onClick:function(node){
		    	var qq = $('#ttta').tabs('getSelected');
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){
					if($('#longDocAdvType').combobox('getValue')==''){
						$.messager.alert('提示',"请先选择医嘱类型！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
				}else if(tab.title=='临时医嘱'){
					if($('#temDocAdvType').combobox('getValue')==''){
						$.messager.alert('提示',"请先选择医嘱类型！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
				}
		    	if(node.attributes.isOPen=="1"){
		    		AdddilogModel("stackModleDivId","【"+node.text+"】详情信息","<%=basePath%>inpatient/docAdvManage/viewStackInfoNew.action?inpatientOrder.id="+node.id,'80%','50%');
		    	}else{
		    		$(this).tree(node.state==='closed'?'expand':'collapse',node.target);
		    	}
		    }
		});
		$('#cc').layout('expand','west');//展开组套信息
		$('#adProjectDivId').show();
		$('#adWestMediDivId').show();
		enableProject();//启用项目类别区域的textbox
		clearProjectTextbox();//清空项目类别区域的textbox
    	functionInfo();//添加功能事件	
    	
    	controlFormat();//加载控件数据
	}
	//加载标签页
	function loadTabs(inpatientNo,recordId){			
		if($("#inpatientId").val()!=""){
			var qq = $('#ttta').tabs('getSelected');				
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){
				loadDatagridA(1,inpatientNo,recordId);
			}else{
				loadDatagridB(0,inpatientNo,recordId);
			}
		}
		$('#ttta').tabs({
		    border:false,
		    onSelect:function(title,index){				    				    	
		    	var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){	
					var rows = $('#infolistB').datagrid('getRows');
					var array = new Array();
					for(var i=0;i<rows.length;i++){
						if(rows[i].changeNo==1){
							array.push(rows[i]);
						}
					}
					if(array.length>0){
						$.extend($.messager.defaults,{  
					        ok:"是",  
					        cancel:"否"  
					    });  
						jQuery.messager.confirm('提示','有尚未保存的医嘱，是否进行保存?',function(e){   
							if(e){ 
								adSaveAdvice(array);
							}
						});							
					} 
					$("#long").show();
					$("#tem").hide();
					$("#endTimeTitle").show();
					$("#endTimeText").show();
					if($("#comboboxId").val()==1){
						loadDatagridA(1,inpatientNo,'02');
					}else{
						loadDatagridA(1,inpatientNo,recordId);
					}						
					loadDatagridC(1,inpatientNo,null);
					$('#reformAdviceBtn').menubutton('enable');//重整医嘱
					$('#adHerbMedicineBtn').menubutton('disable');//草药查询
				}else{
					$('#reformAdviceBtn').menubutton('disable');//重整医嘱
					$('#adHerbMedicineBtn').menubutton('disable');//草药查询
					var rowsA = $('#infolistA').datagrid('getRows');
					var arrayA = new Array();
					for(var i=0;i<rowsA.length;i++){
						if(rowsA[i].changeNo==1){
							arrayA.push(rowsA[i]);
						}
					}
					if(arrayA.length>0){
						$.extend($.messager.defaults,{  
					        ok:"是",  
					        cancel:"否"  
					    });  
						jQuery.messager.confirm('提示','有尚未保存的医嘱，是否进行保存?',function(e){   
							if(e){ 
								adSaveAdvice(arrayA);
							}
						});							
					} 
					$("#long").hide();
					$("#tem").show();
					$("#endTimeTitle").hide();
					$("#endTimeText").hide();
					if($("#comboboxId").val()==1){
						loadDatagridB(0,inpatientNo,'06');
					}else{
						loadDatagridB(0,inpatientNo,recordId);
					}
					loadDatagridD(0,inpatientNo,null);
				}
				$('#longDocAdvType').combobox('setValue','');
		    	$('#temDocAdvType').combobox('setValue','');
		    	$('#adProjectTdId').combobox('setValue','');	
				clearProjectTextbox();//清空项目类别区域的textbox
		    	if($("#docAdvTypeFilt").combobox('getValue')!=null){
		    		recordId = $("#docAdvTypeFilt").combobox('getValue');
		    	}
		    }
		});
	}
	//初始化单位下拉框
	function initCombobox(bool,map,unit,minUnit){
		var dataint = null;
		if(bool){		
			var retVal = "[";
			if(map!=null){			
				map.each(function(key,value,index){
					if(retVal.length>1){
						retVal+=",";
					}
					retVal+="{\"encode\":\""+key+"\",\"name\":\""+value+"\"}";
				})
			}
			retVal +="]";
			dataint = eval("("+retVal+")");
		}else{
			var retVal = "[";
			var unit1 = 'B'+unit;
			var minUnit1 = 'Z'+minUnit;
			if(unit==minUnit && unit!=null && unit!=''){
				retVal+="{\"encode\":\""+unit1+"\",\"name\":\""+drugpackagingunitMap[unit]+"\"}";
			}else{
				if(unit!=null && unit!=''){
					retVal+="{\"encode\":\""+unit1+"\",\"name\":\""+drugpackagingunitMap[unit]+"\"}";
				}					
				if(minUnit!=null){
					retVal+=",{\"encode\":\""+minUnit1+"\",\"name\":\""+mindataMaps[minUnit]+"\"}";
				}
			}
			if(retVal=="["){
				map.each(function(key,value,index){
					if(value=="次"){
						if(retVal.length==1){
							retVal+="{\"encode\":\""+key+"\",\"name\":\""+value+"\"}";
						}
					}
				});
			}
			retVal +="]";
			dataint = eval("("+retVal+")");
		}			
		$('#adProjectUnitTdId').combobox({
			data:dataint,    
		    valueField:'encode',    
		    textField:'name',
		    editable:false,
		    disabled:false,
		    onSelect:function(record){
		    	if(record==''||record==null||record=='undefined'){
		    	}else{
		    		var index = getIndexForAdDgList();
	   		    	var indexA = getIndexForAdDgListA();
					if(indexA>=0){
						var row = $('#infolistA').datagrid('getSelected');
						if(row!=null){
							var index55 = $('#infolistA').datagrid('getRowIndex',row);
							if(record.encode.indexOf('Z')!=-1){
								//查询
								$.ajax({
									url: "<%=basePath%>inpatient/docAdvManage/querysplitDrug.action",
									async:false,
									type:'post',
									success: function(judgeMap) {
										var data = null;
										data = judgeMap;
										if(row.splitattr!=data["splitDrug"]){//不可拆分
											$('#adProjectUnitTdId').combobox('select',row.priceUnit);
											msgShow('提示','【'+row.itemName+'】不可拆分！',5000);
										}else{
											if($.inArray(row.property,data["splitDrugArr"])==-1){//不可拆分
												$('#adProjectUnitTdId').combobox('select',row.priceUnit);
												msgShow('提示','【'+row.itemName+'】拆分属性维护不可拆分！',5000);
											}else{
												$('#infolistA').datagrid('updateRow',{
													index: indexA,
													row: {
														priceUnit:record.encode,
														changeNo:1
													}
												});
											}
										}
									}
								});
							}else{
								$('#infolistA').datagrid('updateRow',{
									index: indexA,
									row: {
										priceUnit:record.encode,
										changeNo:1
									}
								});
								
							}
						}
					}
					if(index>=0){
						var row = $('#infolistB').datagrid('getSelected');
						if(row!=null){
							var index55 = $('#infolistB').datagrid('getRowIndex',row);
							if(record.encode.indexOf('Z')!=-1){
								//查询
								$.ajax({
									url: "<%=basePath%>inpatient/docAdvManage/querysplitDrug.action",
									async:false,
									type:'post',
									success: function(judgeMap) {
										var data = null;
										data = judgeMap;
										if(row.splitattr!=data["splitDrug"]){//不可拆分
											$('#adProjectUnitTdId').combobox('select',row.priceUnit);
											msgShow('提示','【'+row.itemName+'】不可拆分！',5000);
										}else{
											if($.inArray(row.property,data["splitDrugArr"])==-1){//不可拆分
												$('#adProjectUnitTdId').combobox('select',row.priceUnit);
												msgShow('提示','【'+row.itemName+'】拆分属性维护不可拆分！',5000);
											}else{
												$('#infolistB').datagrid('updateRow',{
													index: index,
													row: {									
														priceUnit:record.encode,
														changeNo:1
													}
												});
												changeCostunitB();
											}
										}
									}
								});
							}else{
								$('#infolistB').datagrid('updateRow',{
									index: index,
									row: {
										priceUnit:record.encode,
										changeNo:1
									}
								});
								changeCostunitB();
							} 
						}
					}
		    	}
		    }
		});
	}
	//包装单位的判断
	function changeCostunitB(retVal,row){
		var rows = $('#infolistB').datagrid('getRows');
		$("#fypCost1").text("");
		$("#ssCost1").text("");
		$("#hljbCost1").text("");
		$("#msyzCost1").text("");
		$("#zcyCost1").text("");
		$("#xyCost1").text("");
		$("#shoushuCost1").text("");
		$("#jcCost1").text("");
		$("#jyCost1").text("");
		$("#yycyCost1").text("");
		$("#zcCost1").text("");
		$("#zkCost1").text("");
		$("#zcaoyCost1").text("");
		$("#hzCost1").text("");
		$("#bqCost1").text("");
		$("#qtCost1").text("");
		$("#jlCost1").text("");
		$("#zlCost1").text("");
		for(var i=0;i<rows.length;i++){
			if(rows[i].moStat=='0'){
				if(rows[i].classCode=="17"){//中成药
					$("#zcyCost").show();
					if(rows[i].priceUnit.indexOf("B")!=-1){
						var zcyCost1 = parseFloat($('#zcyCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
						$("#zcyCost1").text(zcyCost1.toFixed(2));
					}else{
						var zcyCost1 = parseFloat($('#zcyCost1').text()+0)+(rows[i].itemPrice*rows[i].qtyTot)/rows[i].packQty;
						$("#zcyCost1").text(zcyCost1.toFixed(2));
					}
				}else if(rows[i].classCode=="18"){//西药
					$("#xyCost").show();
					if(rows[i].priceUnit.indexOf("B")!=-1){
						var xyCost1 = parseFloat($('#xyCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
						$("#xyCost1").text(xyCost1.toFixed(2));
					}else{
						var xyCost1 = parseFloat($('#xyCost1').text()+0)+(rows[i].itemPrice*rows[i].qtyTot)/rows[i].packQty;
						$("#xyCost1").text(xyCost1.toFixed(2));
					}
				}else if(rows[i].classCode=="16"){//中草药
					$("#zcaoyCost").show();
					if(rows[i].priceUnit.indexOf("B")!=-1){
						var zcaoyCost1 = parseFloat($('#zcaoyCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
						$("#zcaoyCost1").text(zcaoyCost1.toFixed(2));
					}else{
						var zcaoyCost1 = parseFloat($('#zcaoyCost1').text()+0)+(rows[i].itemPrice*rows[i].qtyTot)/rows[i].packQty;
						$("#zcaoyCost1").text(zcaoyCost1.toFixed(2));
					}
				}else if(rows[i].classCode=="09"){//非药品
					$("#fypCost").show();
					var fypCost1 = parseFloat($('#fypCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#fypCost1").text(fypCost1.toFixed(2));
				}else if(rows[i].classCode=="13"){//膳食
					$("#ssCost").show();
					var ssCost1 = parseFloat($('#ssCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#ssCost1").text(ssCost1.toFixed(2));
				}else if(rows[i].classCode=="14"){//护理级别
					$("#hljbCost").show();
					var hljbCost1 = parseFloat($('#hljbCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#hljbCost1").text(hljbCost1.toFixed(2));
				}else if(rows[i].classCode=="15"){//描述医嘱
					$("#msyzCost").show();
					var msyzCost1 = parseFloat($('#msyzCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#msyzCost1").text(msyzCost1.toFixed(2));
				}else if(rows[i].classCode=="06"){//手术
					$("#shoushuCost").show();
					var shoushuCost1 = parseFloat($('#shoushuCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#shoushuCost1").text(shoushuCost1.toFixed(2));
				}else if(rows[i].classCode=="07"){//检查
					$("#jcCost").show();
					var jcCost1 = parseFloat($('#jcCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#jcCost1").text(jcCost1.toFixed(2));
				}else if(rows[i].classCode=="08"){//检验
					$("#jyCost").show();
					var jyCost1 = parseFloat($('#jyCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#jyCost1").text(jyCost1.toFixed(2));
				}else if(rows[i].classCode=="10"){//预约出院
					$("#yycyCost").show();
					var yycyCost1 = parseFloat($('#yycyCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#yycyCost1").text(yycyCost1.toFixed(2));
				}else if(rows[i].classCode=="11"){//转床
					$("#zcCost").show();
					var zcCost1 = parseFloat($('#zcCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#zcCost1").text(zcCost1.toFixed(2));
				}else if(rows[i].classCode=="12"){//转科
					$("#zkCost").show();
					var zkCost1 = parseFloat($('#zkCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#zkCost1").text(zkCost1.toFixed(2));
				}else if(rows[i].classCode=="04"){//会诊
					$("#hzCost").show();
					var hzCost1 = parseFloat($('#hzCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#hzCost1").text(hzCost1.toFixed(2));
				}else if(rows[i].classCode=="01"){//病情
					$("#bqCost").show();
					var bqCost1 = parseFloat($('#bqCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#bqCost1").text(bqCost1.toFixed(2));
				}else if(rows[i].classCode=="02"){//其他
					$("#qtCost").show();
					var qtCost1 = parseFloat($('#qtCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#qtCost1").text(qtCost1.toFixed(2));
				}else if(rows[i].classCode=="03"){//计量
					$("#jlCost").show();
					var jlCost1 = parseFloat($('#jlCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#jlCost1").text(jlCost1.toFixed(2));
				}else if(rows[i].classCode=="05"){//治疗
					$("#zlCost").show();
					var zlCost1 = parseFloat($('#zlCost1').text()+0)+rows[i].itemPrice*rows[i].qtyTot;
					$("#zlCost1").text(zlCost1.toFixed(2));
				}
			}
		}
	}
	//加载dialog
	function AdddilogDrugs(title, url) {
		$('#addun').dialog({	
			title: title,	
			width:'70%',	
			height:'70%',	
			closed: false,	
			cache: false,	
			href: url,	
			modal: true   
		});	
	}
	//添加功能事件
	function functionInfo(){
		initCombobox(true,colorInfoUnitsMap,null,null);//单位
	}
	//查看患者信息弹出窗口
	function patientinfoTree() {
		var node = $('#tDt').tree('getSelected');
		AdddilogModel("addData-window","患者信息","<%=basePath%>inpatient/doctorAdvice/inpatientInfos.action?id="+node.attributes.inpatientNo,'43%','58%');
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
		    modal: true,
		    resizable:true
		});    
	}
	/**
 	* 刷新树
    * @author  yeguanqun
    * @date 2016-4-12 10:53
    * @version 1.0
    */
   	function refresh(){
		$('#tDt').tree('reload'); 
	}
	/**
 	* 展开树
    * @author  yeguanqun
    * @date 2016-4-12 10:53
    * @version 1.0
    */
	function expandAll(){
		$('#tDt').tree('expandAll');
	}
	/**
 	* 关闭树
    * @author  yeguanqun
    * @date 2016-4-12 10:53
    * @version 1.0
    */
   	function collapseAll(){
		$('#tDt').tree('collapseAll');
	}
	//关闭时间点弹出窗口
   	function closeSpeFreLayout(){
		$('#adSpeFreData-window').window('close');
	}
   	/**
 	* 渲染加载Map
    * @author  donghe
    * @date 2016-4-12 10:53
    * @version 1.0
    */
   	function loadAjax(){
    	//查询包装单位
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",		
			data:{type:'packunit'},
			type:'post',
			success: function(drugpackagingunitdata) {					
				drugpackagingunitMap= drugpackagingunitdata;	
			}
		});
		//查询最小单位
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",	
			data:{type:'minunit'},
			type:'post',
			success: function(mindata) {					
				mindataMaps = mindata;										
			}
		});
    	//查询计量单位
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",	
			data:{type:'doseUnit'},
			type:'post',
			success: function(drugdoseunitdata) {					
				drugdoseunitMaps = drugdoseunitdata;										
			}
		});
    	//频次
		if(currentLoginDept==null||currentLoginDept==''){
	    	$.ajax({
				url:'<%=basePath%>baseinfo/frequency/queryFrequencyGroup.action',    				
				type:'post',
				success: function(frequencydata) {					  
					frequencyMap = frequencydata;
					for(var i=0;i<frequencyMap.length;i++){
						freeMap.put(frequencyMap[i].code,frequencyMap[i].name);
					}
				}
			});
		}
		//执行科室
		$.ajax({
			url: "<%=basePath%>inpatient/docAdvManage/queryImplDepartment.action",				
			type:'post',
			success: function(implDepartmentdata) {					
				implDepartmentMap = implDepartmentdata;					
			}
		});
		//获得单位
		$.ajax({
			url: '<%=basePath%>inpatient/docAdvManage/queryUnitsList.action',
			type:'post',
			async:false,
			success: function(data) {						
				colorInfoUnitsList = data;
				if(colorInfoUnitsList!=null&&colorInfoUnitsList.length>0){
					for(var i=0;i<colorInfoUnitsList.length;i++){
						colorInfoUnitsMap.put(colorInfoUnitsList[i].encode,colorInfoUnitsList[i].name);
					}
				}
			}
		});
		//用法
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",		
			data:{type:'useage'},		
			type:'post',
			success: function(usemodedata) {					
				usemodeMap = usemodedata;					
			}
		});
		//获得检查部位
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",		
			data:{type:'checkpoint'},
			type:'post',
			success: function(data) {
				colorInfoCheckpointMap = data;				
			}
		});
		//获得样本类型
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",		
			data:{type:'laboratorysample'},
			type:'post',
			success: function(data) {					
				colorInfoSampleTeptMap = data;					
			}
		}); 
   	}
  //检查部位 列表页 显示		
	function colorInfoCheckpointMapFamater(value,row,index){			
		if(value!=null&&value!=""){					
			return colorInfoCheckpointMap[value];									
		}			
	}
  //样本类型 列表页 显示		
	function colorInfoSampleTeptMapFamater(value,row,index){			
		if(value!=null&&value!=""){					
			return colorInfoSampleTeptMap[value];									
		}			
	}
  //总价单位 列表页 显示		
	function colorInfoUnitsMapFamater(value,row,index){	
		if(row.itemType=='1'){
			if(value!=null&&value!=""){
				return colorInfoUnitsMap.get(value);									
			}
		}
		if(row.itemType=='2'||row.itemType=='0'){
			if(value!=null&&value!=""){
				return value;									
			}
		}
	}
  //添加颜色标识
	function functionColour(value,row,index){
		if(row.moStat==0){					
			return 'background-color:#00FF00;';									
		}	
		if(row.moStat==1){					
			return 'background-color:#4A4AFF;';									
		}
		if(row.moStat==2){					
			return 'background-color:#EEEE00;';									
		}
		if(row.moStat==3){					
			return 'background-color:red;';									
		}
		if(row.moStat==4){					
			return 'background-color:grey;';										
		}
		if(row.moStat==-1){					
			return 'background-color:#00FF00;';										
		}
		if(row.moStat==-3){					
			return 'background-color:#00FF00;';										
		}
	}
	//行号生成
	function functionRowNum(value,row,index){
		return index+1;
	}
	//启用项目类别区域的textbox
	function enableProject(){
		$('#adProjectNameTdId').combogrid('disable');						
		$('#adProjectNumTdId').numberspinner('enable');
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
			if(row.ty=='2'){
				
				return value;
			}
		}			 
	}
	//单位 列表页 显示	
	function drugpackagingunitFamaters(value,row,index){	
		if(value!=null&&value!=""){
			if(row.itemType=='1'){
				if(drugpackagingunitMap[value]!=null&&drugpackagingunitMap[value]!=""){
					return drugpackagingunitMap[value]; 
				}
				return value;
			}
			if(row.itemType=='0'){
				if(nonmedicineencodingMap[value]!=null&&nonmedicineencodingMap[value]!=""){
					return nonmedicineencodingMap[value];
				}
				return value;
			}
			if(row.itemType=='2'){
				return value;
			}
		}			 
	}
	//清空项目类别区域的textbox
	function clearProjectTextbox(){
		$('#adProjectNameTdId').textbox('clear');
		$('#adProjectNumTdId').numberspinner('clear');
		$('#adProjectUnitTdId').combobox('clear');
		$('#adNotDrugInsTdId').combobox('clear');
		$('#adNotDrugSamTdId').combobox('clear');
		$('#adExeDeptTdId').textbox('clear');
		$('#startTime').val('');
		$('#endTime').val('');		
		$('#adWestMediFreTdId').combobox('clear');
		$('#adWestMediUsaTdId').combobox('clear'); 
		$('#adWestMediDosMaxTdId').numberbox('clear');
		$('#adWestMediDosMinTdId').numberbox('clear');
		$('#adWestMediSkiTdId').combobox('clear');
		$('#adChinMediFreTdId').combobox('clear');
		$('#adChinMediUsaTdId').combobox('clear');
		$('#adChinMediNumTdId').numberbox('clear');
		$('#adChinMediRemTdId').combobox('clear');
	}
	//长期医嘱
	function loadDatagridA(decmpsState,inpatientNo,recordId){			
		$("#infolistA").datagrid({ 
			url:'<%=basePath%>inpatient/docAdvManage/queryInpatientOrder.action?menuAlias=${menuAlias}&inpatientOrder.decmpsState='+decmpsState+'&inpatientOrder.inpatientNo='+inpatientNo+'&recordId='+recordId,
			fit:true,
			singleSelect:true,
			method:'post',
			rownumbers:false,		
			striped:true,
			border:true,
			selectOnCheck:false,
			checkOnSelect:true,
			fitColumns:false, 
			onLoadSuccess:function(data){
				var row = $('#infolistA').datagrid('getRows');
				var rows = $('#infolistA').datagrid('getRows');
				$("#fypCost").hide();
				$("#ssCost").hide();
				$("#hljbCost").hide();
				$("#msyzCost").hide();
				$("#zcyCost").hide();
				$("#xyCost").hide();
				$("#shoushuCost").hide();
				$("#jcCost").hide();
				$("#jyCost").hide();
				$("#yycyCost").hide();
				$("#zcCost").hide();
				$("#zkCost").hide();
				$("#zcaoyCost").hide();
				$("#hzCost").hide();
				$("#bqCost").hide();
				$("#qtCost").hide();
				$("#jlCost").hide();
				$("#zlCost").hide();
				
				$("#fypCost1").text("");
				$("#ssCost1").text("");
				$("#hljbCost1").text("");
				$("#msyzCost1").text("");
				$("#zcyCost1").text("");
				$("#xyCost1").text("");
				$("#shoushuCost1").text("");
				$("#jcCost1").text("");
				$("#jyCost1").text("");
				$("#yycyCost1").text("");
				$("#zcCost1").text("");
				$("#zkCost1").text("");
				$("#zcaoyCost1").text("");
				$("#hzCost1").text("");
				$("#bqCost1").text("");
				$("#qtCost1").text("");
				$("#jlCost1").text("");
				$("#zlCost1").text("");
				if(row.length>0){
					for(var q=0;q<row.length;q++){
						if(row[q].moStat=='-1'){
							var itemName = "";
							var img = "<div style='float:left;margin-top:1px;margin-left:-2px;width:15px;height:12px;background:url("+basePath+"/themes/system/images/button/shen1.png);background-repeat:no-repeat;'></div>";
							itemName = img+row[q].itemName;
							$('#infolistA').datagrid('updateRow',{
								index: q,
								row: {
									itemNameView:itemName,//医嘱名称
								}
							});
						}else{
							$('#infolistA').datagrid('updateRow',{
								index: q,
								row: {
									itemNameView:row[q].itemName,//医嘱名称
								}
							});
						}
						var a=0;
						for(var i=0;i<rows.length;i++){
							if(row[q].combNo==rows[i].combNo && row[q].combNo!=null && row[q].combNo!=''){
								a++;
							}
						}
						if(a==1){
							$('#infolistA').datagrid('updateRow',{
								index: q,
								row: {
									combNoFlag:''
								}
							});	
						}else if(a==2){
							for(var j=0;j<rows.length;j++){
								if(row[q].combNo==rows[j].combNo){
									if(row[q].sortId==rows[j].sortId){
										if(j==0){
											$('#infolistA').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});									
										}else if(j!=0 && (row[q].combNo!=rows[j-1].combNo)){
											$('#infolistA').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j==rows.length-1){
											$('#infolistA').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});												
										}else if((j!=rows.length-1) && (row[q].combNo!=rows[j+1].combNo)){
											$('#infolistA').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}									
									}								
								}
							}						
						}else if(a>2){
							for(var j=0;j<rows.length;j++){
								if(row[q].combNo==rows[j].combNo){
									if(row[q].sortId==rows[j].sortId){
										if(j==0){
											$('#infolistA').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j!=0 && row[q].combNo!=rows[j-1].combNo){
											$('#infolistA').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j==rows.length-1){
											$('#infolistA').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}else if(j!=rows.length-1 && row[q].combNo!=rows[j+1].combNo){
											$('#infolistA').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}else{
											$('#infolistA').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┃'
												}
											});	
										}
									}
								}
							}
						}
					}	
				}					
			},				
		});
	}
	//临时医嘱
	function loadDatagridB(decmpsState,inpatientNo,recordId){
		$("#infolistB").datagrid({
			url:'<%=basePath%>inpatient/docAdvManage/queryInpatientOrder.action?menuAlias=${menuAlias}&inpatientOrder.decmpsState='+decmpsState+'&inpatientOrder.inpatientNo='+inpatientNo+'&recordId='+recordId,
			fit:true,
			singleSelect:true,
			method:'post',
			rownumbers:false,
			striped:true,
			border:true,
			selectOnCheck:false,
			checkOnSelect:true,
			fitColumns:false, 
			onLoadSuccess:function(data){
				var row = $('#infolistB').datagrid('getRows');
				var rows = $('#infolistB').datagrid('getRows');
				$("#fypCost").hide();
				$("#ssCost").hide();
				$("#hljbCost").hide();
				$("#msyzCost").hide();
				$("#zcyCost").hide();
				$("#xyCost").hide();
				$("#shoushuCost").hide();
				$("#jcCost").hide();
				$("#jyCost").hide();
				$("#yycyCost").hide();
				$("#zcCost").hide();
				$("#zkCost").hide();
				$("#zcaoyCost").hide();
				$("#hzCost").hide();
				$("#bqCost").hide();
				$("#qtCost").hide();
				$("#jlCost").hide();
				$("#zlCost").hide();
				
				$("#fypCost1").text("");
				$("#ssCost1").text("");
				$("#hljbCost1").text("");
				$("#msyzCost1").text("");
				$("#zcyCost1").text("");
				$("#xyCost1").text("");
				$("#shoushuCost1").text("");
				$("#jcCost1").text("");
				$("#jyCost1").text("");
				$("#yycyCost1").text("");
				$("#zcCost1").text("");
				$("#zkCost1").text("");
				$("#zcaoyCost1").text("");
				$("#hzCost1").text("");
				$("#bqCost1").text("");
				$("#qtCost1").text("");
				$("#jlCost1").text("");
				$("#zlCost1").text("");
				if(row.length>0){
					for(var q=0;q<row.length;q++){
						$('#selectIndex').val($('#selectIndex').val()+q);
						if(row[q].moStat=='-1'){
							var itemName = "";
							var img = "<div style='float:left;margin-top:1px;margin-left:-2px;width:15px;height:12px;background:url("+basePath+"/themes/system/images/button/shen1.png);background-repeat:no-repeat;'></div>";
							itemName = img+row[q].itemName;
							$('#infolistB').datagrid('updateRow',{
								index: q,
								row: {
									itemNameView:itemName,//医嘱名称
								}
							});
						}else{
							$('#infolistB').datagrid('updateRow',{
								index: q,
								row: {
									itemNameView:row[q].itemName,//医嘱名称
								}
							});
						}
						if(row[q].moStat=='0'){
							if(row[q].classCode=="09"){//非药品
								$("#fypCost").show();
								if($('#fypCost1').text()==null||$('#fypCost1').text()==""){
									var fypCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#fypCost1").text(fypCost1.toFixed(2));
								}else{
									$("#fypCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#fypCost1').text()));
								}
							}else if(row[q].classCode=="13"){//膳食
								$("#ssCost").show();
								if($('#ssCost1').text()==null||$('#ssCost1').text()==""){
									var ssCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#ssCost1").text(ssCost1.toFixed(2));
								}else{
									$("#ssCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#ssCost1').text()));
								}
							}else if(row[q].classCode=="14"){//护理级别
								$("#hljbCost").show();
								if($('#hljbCost1').text()==null||$('#hljbCost1').text()==""){
									var hljbCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#hljbCost1").text(hljbCost1.toFixed(2));
								}else{
									$("#hljbCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#hljbCost1').text()));
								}
							}else if(row[q].classCode=="15"){//描述医嘱
								$("#msyzCost").show();
								if($('#msyzCost1').text()==null||$('#msyzCost1').text()==""){
									var msyzCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#msyzCost1").text(msyzCost1.toFixed(2));
								}else{
									$("#msyzCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#msyzCost1').text()));
								}
							}else if(row[q].classCode=="17"){//中成药
								$("#zcyCost").show();
								if($('#zcyCost1').text()==null||$('#zcyCost1').text()==""){
									var zcyCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#zcyCost1").text(zcyCost1.toFixed(2));
								}else{
									$("#zcyCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#zcyCost1').text()));
								}
							}else if(row[q].classCode=="18"){//西药
								$("#xyCost").show();
								if($('#xyCost1').text()==null||$('#xyCost1').text()==""){
									var xyCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#xyCost1").text(xyCost1.toFixed(2));
								}else{
									$("#xyCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#xyCost1').text()));
								}
							}else if(row[q].classCode=="06"){//手术
								$("#shoushuCost").show();
								if($('#shoushuCost1').text()==null||$('#shoushuCost1').text()==""){
									var shoushuCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#shoushuCost1").text(shoushuCost1.toFixed(2));
								}else{
									$("#shoushuCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#shoushuCost1').text()));
								}
							}else if(row[q].classCode=="07"){//检查
								$("#jcCost").show();
								if($('#jcCost1').text()==null||$('#jcCost1').text()==""){
									var jcCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#jcCost1").text(jcCost1.toFixed(2));
								}else{
									$("#jcCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#jcCost1').text()));
								}
							}else if(row[q].classCode=="08"){//检验
								$("#jyCost").show();
								if($('#jyCost1').text()==null||$('#jyCost1').text()==""){
									var jyCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#jyCost1").text(jyCost1.toFixed(2));
								}else{
									$("#jyCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#jyCost1').text()));
								}
							}else if(row[q].classCode=="10"){//预约出院
								$("#yycyCost").show();
								if($('#yycyCost1').text()==null||$('#yycyCost1').text()==""){
									var yycyCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#yycyCost1").text(yycyCost1.toFixed(2));
								}else{
									$("#yycyCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#yycyCost1').text()));
								}
							}else if(row[q].classCode=="11"){//转床
								$("#zcCost").show();
								if($('#zcCost1').text()==null||$('#zcCost1').text()==""){
									var zcCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#zcCost1").text(zcCost1.toFixed(2));
								}else{
									$("#zcCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#zcCost1').text()));
								}
							}else if(row[q].classCode=="12"){//转科
								$("#zkCost").show();
								if($('#zkCost1').text()==null||$('#zkCost1').text()==""){
									var zkCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#zkCost1").text(zkCost1.toFixed(2));
								}else{
									$("#zkCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#zkCost1').text()));
								}
							}else if(row[q].classCode=="16"){//中草药
								$("#zcaoyCost").show();
								if($('#zcaoyCost1').text()==null||$('#zcaoyCost1').text()==""){
									var zcaoyCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#zcaoyCost1").text(zcaoyCost1.toFixed(2));
								}else{
									$("#zcaoyCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#zcaoyCost1').text()));
								}
							}else if(row[q].classCode=="04"){//会诊
								$("#hzCost").show();
								if($('#hzCost1').text()==null||$('#hzCost1').text()==""){
									var hzCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#hzCost1").text(hzCost1.toFixed(2));
								}else{
									$("#hzCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#hzCost1').text()));
								}
							}else if(row[q].classCode=="01"){//病情
								$("#bqCost").show();
								if($('#bqCost1').text()==null||$('#bqCost1').text()==""){
									var bqCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#bqCost1").text(bqCost1.toFixed(2));
								}else{
									$("#bqCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#bqCost1').text()));
								}
							}else if(row[q].classCode=="02"){//其他
								$("#qtCost").show();
								if($('#qtCost1').text()==null||$('#qtCost1').text()==""){
									var qtCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#qtCost1").text(qtCost1.toFixed(2));
								}else{
									$("#qtCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#qtCost1').text()));
								}
							}else if(row[q].classCode=="03"){//计量
								$("#jlCost").show();
								if($('#jlCost1').text()==null||$('#jlCost1').text()==""){
									var jlCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#jlCost1").text(jlCost1.toFixed(2));
								}else{
									$("#jlCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#jlCost1').text()));
								}
							}else if(row[q].classCode=="05"){//治疗
								$("#zlCost").show();
								if($('#zlCost1').text()==null||$('#zlCost1').text()==""){
									var zlCost1 = row[q].itemPrice*row[q].qtyTot;
									$("#zlCost1").text(zlCost1.toFixed(2));
								}else{
									$("#zlCost1").text(row[q].itemPrice*row[q].qtyTot+parseFloat($('#zlCost1').text()));
								}
							}
						}
						
						var a=0;
						for(var i=0;i<rows.length;i++){
							if(row[q].combNo==rows[i].combNo && row[q].combNo!=null && row[q].combNo!=''){
								a++;
							}
						}
						if(a==1){
							$('#infolistB').datagrid('updateRow',{
								index: q,
								row: {
									combNoFlag:''
								}
							});	 
						}else if(a==2){
							for(var j=0;j<rows.length;j++){
								if(row[q].combNo==rows[j].combNo){
									if(row[q].sortId==rows[j].sortId){
										if(j==0){
											$('#infolistB').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});									
										}else if(j!=0 && (row[q].combNo!=rows[j-1].combNo)){
											$('#infolistB').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j==rows.length-1){
											$('#infolistB').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});												
										}else if((j!=rows.length-1) && (row[q].combNo!=rows[j+1].combNo)){
											$('#infolistB').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}									
									}								
								}
							}						
						}else if(a>2){
							for(var j=0;j<rows.length;j++){
								if(row[q].combNo==rows[j].combNo){
									if(row[q].sortId==rows[j].sortId){
										if(j==0){
											$('#infolistB').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j!=0 && row[q].combNo!=rows[j-1].combNo){
											$('#infolistB').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j==rows.length-1){
											$('#infolistB').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}else if(j!=rows.length-1 && row[q].combNo!=rows[j+1].combNo){
											$('#infolistB').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}else{
											$('#infolistB').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┃'
												}
											});	
										}																		
									}								
								}
							}
						}
					}	
				}					 
			}		 				
		});
	}
	//剂量单位 列表页 显示	
	function drugpdoseunitFamaters(value,row,index){	
		if(value!=null&&value!=""){	
			if(drugdoseunitMaps[value]!=null&&drugdoseunitMaps[value]!=""){
				return drugdoseunitMaps[value];
			}
			return value;
		}			
	}  
	//每次量剂量
	function doseOnceFamater(value,row,index){
		if(row.itemType=="1"){
			if((row.doseOnce!=''&&row.doseOnce!=null)||row.doseOnce=="0"){
				var doseOnces = "";
				if(row.baseDose=="0"){
					doseOnces = (row.doseOnce/1).toFixed(1)+mindataMaps[row.minUnit]+'='+row.doseOnce+drugdoseunitMaps[row.doseUnit];
				}else{
					doseOnces = (row.doseOnce/1).toFixed(1)+mindataMaps[row.minUnit]+'='+(row.doseOnce*row.baseDose)+drugdoseunitMaps[row.doseUnit];
				}
				return doseOnces;
			}else{
				return '';
			}
		}else{
			return '';
		}
	} 
	//执行科室 列表页 显示		
	function implDepartmentFamater(value,row,index){
		if(value!=null&&value!=""){
			return implDepartmentMap[value];
		}
	}
	//加载名称下拉列表
	function ProjectName(adProjectTdId,projectName,docAdvType){
		var systemTypeMap = "";//系统类别Map
		var druggradeMap = "";//药品等级Map			
		var diseasetypeList = "";//疾病分类Map			
		//查询系统类别
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",	
			data:{type:'systemType'},
			type:'post',
			success: function(systemTypedata) {					
				systemTypeMap = systemTypedata;										
			}
		});
		//查询非药品单位
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",	
			data:{type:'nonmedicineencoding'},
			type:'post',
			success: function(nonmedicineencodingdata) {					
				nonmedicineencodingMap = nonmedicineencodingdata;										
			}
		});
		//查询药品等级
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",	
			data:{type:'drugGrade'},
			type:'post',
			success: function(druggradedata) {					
				druggradeMap = druggradedata;							
			}
		});	
		//查询疾病分类
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",	
			data:{type:'diseasetype'},
			success: function(diseasetypedata) {					
				diseasetypeList = diseasetypedata;					
			}
		});
		$('#adProjectNameTdId').combogrid({
		    url:'<%=basePath%>inpatient/docAdvManage/queryDrugOrUndrugInfos.action', 
		    queryParams:{'inpatientOrder.classCode':adProjectTdId,'inpatientOrder.className':projectName,'inpatientOrder.typeCode':docAdvType},	
		    pagination:true,
    		required:true,
    		pageSize:10,
			pageList:[10,30,50,80,100],
    		panelWidth:800,
    		panelHeight:300,
            idField:'name',    
            textField:'name', 
            mode:'remote',
		    columns:[[    
		        {field:'name',title:'名称',width:200,styler:gradeStyler},    
		        {field:'sysType',title:'类别',width:100,formatter:sysTypeFamater},    
		        {field:'specs',title:'规格',width:120},    
		        {field:'defaultprice',title:'价格',width:100}, 
		        {field:'drugPackagingunit',title:'单位',width:60,formatter:drugpackagingunitFamater},    
		        {field:'drugGrade',title:'医保标记',width:100,formatter:speidFamater},    
		        {field:'undrugIsprovincelimit',title:'是否省限制',width:120,hidden:true},    
		        {field:'undrugIscitylimit',title:'是否市限制',width:100,hidden:true},
		        {field:'undrugIsownexpense',title:'是否自费',width:60,hidden:true},    
		        {field:'undrugIsspecificitems',title:'是否特定项目',width:100,hidden:true},    
		        {field:'inputCode',title:'自定义码',width:120},    
		        {field:'drugCommonname',title:'药品通用名',width:100},
		        {field:'storeSum',title:'库存可用数量',width:60},    
		        {field:'dept',title:'执行科室',width:100,formatter:implDepartmentFamater},    
		        {field:'inspectionSite',title:'检查检体',width:120},    
		        {field:'diseaseClassification',title:'疾病分类',width:100,width:100,formatter:diseasetypeFamater},
		        {field:'specialtyName',title:'专科名称',width:120},    
		        {field:'medicalHistory',title:'病史及检查',width:100},
		        {field:'requirements',title:'检查要求',width:60},    
		        {field:'notes',title:'注意事项',width:100},    
		        {field:'gbcode',title:'国家基本药物编码',width:120,hidden:true},    
		        {field:'drugInstruction',title:'说明书',width:100,hidden:true},
		        {field:'drugOncedosage',title:'一次用量',width:120,hidden:true},    
		        {field:'drugDoseunit',title:'剂量单位',width:100,hidden:true},
		        {field:'drugFrequency',title:'频次',width:60,hidden:true,formatter:drugfrequencyFamater},    
		        {field:'lowSum',title:'最低库存',width:100,hidden:true},    
		        {field:'drugDosageform',title:'剂型代码',width:120,hidden:true},    
		        {field:'drugType',title:'药品类别',width:100,hidden:true},
		        {field:'drugNature',title:'药品性质',width:120,hidden:true},    
		        {field:'drugRetailprice',title:'零售价',width:100,hidden:true},
		        {field:'remark',title:'备注',width:60,hidden:true},    
		        {field:'drugUsemode',title:'使用方法',width:100,hidden:true,formatter:drugusemodeFamater},    
		        {field:'drugBasicdose',title:'基本剂量',width:120,hidden:true},    
		        {field:'undrugIsinformedconsent',title:'是否知情同意书',width:100,hidden:true}
		    ]],
		    onClickRow:function(rowIndex, rowData){
				var bool = false;
				if($('#adWestMediUrgTdId').is(':checked')){//判断复选框是否被选中
					bool = true;
				}
				var userId = $('#userid').val();
				var qq = $('#ttta').tabs('getSelected');
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){
					if(rowData.ty=='1'){//药品
						if(rowData.storeSum>0){//判断库存
							var drugNature= rowData.drugNature!=null?rowData.drugNature:'';//药品性质
							//根据药品性质判断是否是限制药品
							$.ajax({
								url: "<%=basePath%>inpatient/docAdvManage/queryAdvdrugnature.action",		
								data:'proInfoVo.drugNature='+drugNature,
								type:'post',
								success: function(bdata) {
									var drugNatureData =  bdata;
									if(drugNatureData.length==0){
										if(rowData.drugRestrictionofantibiotic==3){  //1非抗菌药2无限制3职级限制4特殊管理     医生开立职级限制
											$.ajax({
												url: "<%=basePath%>inpatient/docAdvManage/queryAuditInfo.action",		
												data:{userId:userId},
												type:'post',
												success: function(auditdata) {
													if(auditdata==0){
														$.messager.confirm('确认信息',rowData.name+'开立需要上级医生审核,确认开立吗?',function(r){    
														    if(r){    							//0不需要皮试1青霉素皮试2原药皮试
														    	if((rowData.sysType=='18')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
														    		$.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(rr){ 
														    			if(rr){
														    				addDatagrid(rowData,2,-1);//2需要皮试
														    			}else{
														    				addDatagrid(rowData,1,-1);//1不需要皮试
														    			}
														    		});
														    	}else{//不是西药不需要考虑皮试
														    		addDatagrid(rowData,"",-1);//1不需要皮试
														    	}
														    }    
														});
													}else{//非医生开立职级限制
														if((rowData.sysType=='18')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
												    		$.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(rr){ 
												    			if(rr){
												    				addDatagrid(rowData,2,0);//2需要皮试
												    			}else{
												    				addDatagrid(rowData,1,0);//1不需要皮试
												    			}
												    		});
												    	}else{//不是西药不需要考虑皮试
												    		addDatagrid(rowData,"",0);//1不需要皮试
												    	}
													}	
												}
											});	
										}else{
											if((rowData.sysType=='18')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
									    		$.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(rr){ 
									    			if(rr){
									    				addDatagrid(rowData,2,0);//2需要皮试
									    			}else{
									    				addDatagrid(rowData,1,0);//1不需要皮试
									    			}
									    		});
									    	}else{//不是西药不需要考虑皮试
									    		addDatagrid(rowData,"",0);//1不需要皮试
									    	}
										}
									}else{
										$.messager.alert('提示',"限制药品性质的药品不可以开立！");
										setTimeout(function(){
											$(".messager-body").window('close');
										 },3500);
									}
								}
							});		
						}else{
							$.messager.alert('提示',"该药品库存不足不可以开立！");
							setTimeout(function(){
								$(".messager-body").window('close');
							 },3500);
						}
					}else if(rowData.ty=='0'){//非药品
						addDatagrid(rowData,null,0);
					}
				}else if(tab.title=='临时医嘱'){
					if(rowData.ty=='1'){//药品
						if(rowData.storeSum>0){//判断库存
							if(rowData.drugRestrictionofantibiotic==3){  //1非抗菌药2无限制3职级限制4特殊管理     医生开立职级限制
								$.ajax({
									url: "<%=basePath%>inpatient/docAdvManage/queryAuditInfo.action",		
									data:{userId:userId},
									type:'post',
									success: function(auditdata) {
										if(auditdata==0){
											$.messager.confirm('确认信息',rowData.name+'开立需要上级医生审核,确认开立吗?',function(r){    
											    if(r){    							//0不需要皮试1青霉素皮试2原药皮试
											    	if((rowData.sysType=='18')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
											    		$.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(rr){ 
											    			if(rr){
											    				addDatagridB(rowData,2,-1);//2需要皮试
											    			}else{
											    				addDatagridB(rowData,1,-1);//1不需要皮试
											    			}
											    		});
											    	}else{//不是西药不需要考虑皮试
											    		addDatagridB(rowData,"",-1);//1不需要皮试
											    	}
											    }    
											});
										}else{//非医生开立职级限制
											if((rowData.sysType=='18')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
									    		$.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(rr){ 
									    			if(rr){
									    				addDatagridB(rowData,2,0);//2需要皮试
									    			}else{
									    				addDatagridB(rowData,1,0);//1不需要皮试
									    			}
									    		});
									    	}else{//不是西药不需要考虑皮试
									    		addDatagridB(rowData,"",0);//1不需要皮试
									    	}
										}
									}
								});	
							}else{
								if((rowData.sysType=='18')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
						    		$.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(rr){ 
						    			if(rr){
						    				addDatagridB(rowData,2,0);//2需要皮试
						    			}else{
						    				addDatagridB(rowData,1,0);//1不需要皮试
						    			}
						    		});
						    	}else{//不是西药不需要考虑皮试
						    		addDatagridB(rowData,"",0);//1不需要皮试
						    	}
							}
						}else{
							$.messager.alert('提示',"该药品库存不足不可以开立！");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
					}else if(rowData.ty=='0'){//非药品
						addDatagridB(rowData,null,0);
					}
				}
		    }
		});
		function dd(){
			var today=new Date();
	        var yesterday_milliseconds=today.getTime()+1000*60*60*24*2;
	         
	        var yesterday=new Date();      
	        yesterday.setTime(yesterday_milliseconds);      
	            
	        var strYear=yesterday.getFullYear();   
	        var strDay=yesterday.getDate();   
	        var strMonth=yesterday.getMonth()+1;
	        var strHours= yesterday.getHours(); //获取系统时，
	        var strMinutes= yesterday.getMinutes(); //分
	        var strSeconds= yesterday.getSeconds(); //秒
	        if(strMonth<10){
	            strMonth="0"+strMonth;   
	        }
	        if(strDay<10){
	        	strDay="0"+strDay;   
	        }
	        if(strHours<10){
	        	strHours="0"+strHours;   
	        }
	        if(strMinutes<10){
	        	strMinutes="0"+strMinutes;   
	        }
	        if(strSeconds<10){
	        	strSeconds="0"+strSeconds;   
	        }
	        var strYesterday=strYear+"-"+strMonth+"-"+strDay+" "+strHours+":"+strMinutes+":"+strSeconds;   
	        return strYesterday;
		}
		//临时医嘱
		function addDatagridB(rowData,hypotest,mostate){
			var freeDays='QD';//  频次默认为立即用 用法默认为P.O；药品以外的医嘱对应的频次默认为每天
 			var usaged='';//用法
 			if(rowData.sysType=='16'||rowData.sysType=='17'||rowData.sysType=='18'){
 				freeDays='ST';
 				usaged='10';
 			}
			//判断是否需要皮试
			var hypotestName = "";
			if(hypotest=="2"){
				hypotestName="需要皮试，未做";
			}else if(hypotest=="1"){
				hypotestName="不需要皮试";
			}else{
				hypotestName ="";
			}
			var execDpcd="";//执行科室id
			var execDpnm="";//执行科室名字
			var pharmacyCode="";//扣库科室
			var unit = "";//单位
			if(rowData.ty=="1"){//药品
				execDpcd = rowData.dept!=null?rowData.dept:'';
				execDpnm = implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'';
				if(execDpcd==null||execDpcd==""){
					execDpcd = $('#nurseCellCode').val();
					execDpnm = implDepartmentMap[$('#nurseCellCode').val()]!=null?implDepartmentMap[$('#nurseCellCode').val()]:'';
				}
				pharmacyCode = $('#pharmacyInputId').val();
				if(rowData.drugPackagingunit!=null){
					unit = "B"+rowData.drugPackagingunit;
				}
			}else{//非药品
				if(rowData.drugPackagingunit!=null){
					unit = rowData.drugPackagingunit;
				}
				if(rowData.sysType=="14"){
					execDpcd = $('#nurseCellCode').val();
					execDpnm = implDepartmentMap[$('#nurseCellCode').val()]!=null?implDepartmentMap[$('#nurseCellCode').val()]:'';
				}else{
					execDpcd = $('#deptId').val();
					execDpnm = implDepartmentMap[execDpcd]!=null?implDepartmentMap[execDpcd]:'';
				}
				pharmacyCode = "";
			}
			var mytime = new Date().format("yyyy-MM-dd hh:mm:ss");
			var drugOncedosage = 0;
			var drugBasicdose = 0;
			if(rowData.drugOncedosage==0||rowData.drugOncedosage==null||rowData.drugOncedosage==""){
				drugOncedosage = 1;
			}else{
				drugOncedosage = rowData.drugOncedosage;
			}
			if(rowData.drugBasicdose==0||rowData.drugBasicdose==null||rowData.drugBasicdose==""){
				drugBasicdose = 1;
			}else{
				drugBasicdose = rowData.drugBasicdose;
			}
			var itemName = "";
			if(mostate=="-1"){
				var img = "<div style='float:left;margin-top:1px;margin-left:-2px;width:15px;height:12px;background:url("+basePath+"/themes/system/images/button/shen1.png);background-repeat:no-repeat;'></div>";
				itemName = img+rowData.name;
			}else{
				itemName = rowData.name;
			}
			var lastIndex = $('#infolistB').datagrid('appendRow',{
				changeNo:1,
				id:'',
				typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
				typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称		
				classCode:rowData.sysType,//系统类别Id
				className:systemTypeMap[rowData.sysType],//系统类别名称
				itemCode:rowData.itemCode,//医嘱Id
				itemName:rowData.name,//医嘱名称
				itemNameView:itemName,//医嘱名称
				combNo:'',//组
				qtyTot:1,//总量
				priceUnit:unit,//单位（总量单位）
				drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
				packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量		
				minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
				doseOnce:drugOncedosage,//每次量		
				doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
				specs:rowData.specs!=null?rowData.specs:'',//规格
				doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
				drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
				drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
				itemPrice:rowData.defaultprice!=null?rowData.defaultprice:'',//零售价(药品)
				useDays:1,//付数
				frequencyCode:freeDays,//频次代码
				frequencyName:freeMap.get(freeDays),//频次名称
				usageCode:usaged,//Id	
				useName:usemodeMap[usaged],//用法名称
				dateBgn:mytime,//开始时间 
				dateEnd:'',//停止时间
				moDate:mytime,//开立时间 
				docCode:'',//开立医生代码
				docName:$('#userName').val(),//开立医生名称
				execDpcd:execDpcd,//执行科室 id
				execDpnm:execDpnm,//执行科室 
				emcFlag:0,//加急标记
				isUrgent:0,//急
				labCode:'',//样本类型
				itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
				pharmacyCode:pharmacyCode,//扣库科室
				moNote2:'',//备注 
				recUsernm:$('#recUserName').val(),//录入人
				listDpcd:$('#deptId').val(),//开立科室 
				updateUser:'',//停止人 
				baseDose:drugBasicdose,//基本剂量
				permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
				hypotest:hypotest,//皮试Id
				hypotestName:hypotestName,//皮试名称
				itemType:rowData.ty,//药品非药品标识
				moStat:mostate,//医嘱状态
				sortId:sortIdCreate(),//顺序号
				splitattr:rowData.splitattr,//拆分属性
				property:rowData.property//药品拆分维护表属性
			}).datagrid('getRows').length-1;
			$('#infolistB').datagrid('selectRow',lastIndex);
			var infoA = $('#infolistB').datagrid('getSelections');
		}
		//长期医嘱
		function addDatagrid(rowData,hypotest,mostate){
			var freeDays='QD';//  频次默认为立即用 用法默认为P.O；药品以外的医嘱对应的频次默认为每天
 			var usaged='';//用法
 			if(rowData.sysType=='16'||rowData.sysType=='17'||rowData.sysType=='18'){
 				freeDays='ST';
 				usaged='10';
 			}
			//判断是否需要皮试
			var hypotestName = "";
			if(hypotest=="2"){
				hypotestName="需要皮试，未做";
			}else if(hypotest=="1"){
				hypotestName="不需要皮试";
			}else{
				hypotestName ="";
			}
			var execDpcd="";//执行科室id
			var execDpnm="";//执行科室名字
			var pharmacyCode="";//扣库科室
			var unit = "";//单位
			if(rowData.ty=="1"){
				execDpcd = rowData.dept!=null?rowData.dept:'';
				execDpnm = implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'';
				if(execDpcd==null||execDpcd==""){
					execDpcd = $('#nurseCellCode').val();
					execDpnm = implDepartmentMap[$('#nurseCellCode').val()]!=null?implDepartmentMap[$('#nurseCellCode').val()]:'';
				}
				pharmacyCode = $('#pharmacyInputId').val();
				if(rowData.drugPackagingunit!=null){
					unit = "B"+rowData.drugPackagingunit;
				}
			}else{
				if(rowData.drugPackagingunit!=null){
					unit = rowData.drugPackagingunit;
				}
				if(rowData.sysType=="14"){
					execDpcd = $('#nurseCellCode').val();
					execDpnm = implDepartmentMap[$('#nurseCellCode').val()]!=null?implDepartmentMap[$('#nurseCellCode').val()]:'';
				}else{
					execDpcd = $('#deptId').val();
					execDpnm = implDepartmentMap[execDpcd]!=null?implDepartmentMap[execDpcd]:'';
				}
				pharmacyCode = "";
			}
			var mytime = new Date().format("yyyy-MM-dd hh:mm:ss");
			var strYesterday=dd();
			var drugOncedosage = 0;
			var drugBasicdose = 0;
			if(rowData.drugOncedosage==0||rowData.drugOncedosage==null||rowData.drugOncedosage==""){
				drugOncedosage = 1;
			}else{
				drugOncedosage = rowData.drugOncedosage;
			}
			if(rowData.drugBasicdose==0||rowData.drugBasicdose==null||rowData.drugBasicdose==""){
				drugBasicdose = 1;
			}else{
				drugBasicdose = rowData.drugBasicdose;
			}
			var itemName = "";
			if(mostate=="-1"){
				var img = "<div style='float:left;margin-top:1px;margin-left:-2px;width:15px;height:12px;background:url("+basePath+"/themes/system/images/button/shen1.png);background-repeat:no-repeat;'></div>";
				itemName = img+rowData.name;
			}else{
				itemName = rowData.name;
			}
			var lastIndex = $('#infolistA').datagrid('appendRow',{
				changeNo:1,
				id:'',
				typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
				typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
				classCode:rowData.sysType,//系统类别Id
				className:systemTypeMap[rowData.sysType],//系统类别名称
				itemCode:rowData.itemCode,//医嘱Id
				itemName:rowData.name,//医嘱名称
				itemNameView:itemName,//医嘱名称
				combNo:'',//组
				qtyTot:1,//总量
				priceUnit: unit,//单位（总量单位）
				drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
				packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量		
						
				minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
				doseOnce:drugOncedosage,//每次量		
				doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
				specs:rowData.specs!=null?rowData.specs:'',//规格
				doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
				drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
				drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
				itemPrice:rowData.defaultprice!=null?rowData.defaultprice:'',//零售价(药品)
				useDays:1,//付数
				frequencyCode:freeDays,//频次代码
				frequencyName:freeMap.get(freeDays),//频次名称
				usageCode:usaged,//Id	
				useName:usemodeMap[usaged],//用法名称
				dateBgn:mytime,//开始时间 
				dateEnd:strYesterday,//停止时间
				moDate:mytime,//开立时间 
				docCode:'',//开立医生代码
				docName:$('#userName').val(),//开立医生名称
				execDpcd:execDpcd,//执行科室 id
				execDpnm:execDpnm,//执行科室
				emcFlag:0,//加急标记
				isUrgent:0,//急
				labCode:'',//样本类型
				itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
				pharmacyCode:pharmacyCode,//扣库科室
				moNote2:'',//备注 
				recUsernm:$('#recUserName').val(),//录入人
				listDpcd:$('#deptId').val(),//开立科室 
				updateUser:'',//停止人 
				baseDose:drugBasicdose,//基本剂量
				permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
				hypotest:hypotest,//皮试Id
				hypotestName:hypotestName,//皮试名称
				itemType:rowData.ty,//药品非药品标识
				moStat:mostate,//医嘱状态
				sortId:sortIdCreate(),//顺序号
				splitattr:rowData.splitattr,//拆分属性
				property:rowData.property//药品拆分维护表属性
			}).datagrid('getRows').length-1;
			$('#infolistA').datagrid('selectRow',lastIndex);
			var infoA = $('#infolistA').datagrid('getSelections');
		}
		
		//药品等级为乙类的，在列表中以绿色字体标识    
		function gradeStyler(value,row,index){			
			if(row.drugGrade=='2'){					
				return 'color:green;';									
			}					
			//判断库存  标记
			if(row.storeSum==0){
				return 'color:red;';
			}
		}
		//系统类别 列表页 显示		
		function sysTypeFamater(value,row,index){			
			if(value!=null&&value!=""){
				return systemTypeMap[value];									
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
	}
	//获得infolistA索引
	function getIndexForAdDgListA(){
		var row = $('#infolistA').datagrid('getSelected');
		if(row!=null && row.moStat<=0){
			return $('#infolistA').datagrid('getRowIndex',row);
		}else{
			return -1;
		}
	}
	//获得infolistB索引
	function getIndexForAdDgList(){
		var row = $('#infolistB').datagrid('getSelected');
		if(row!=null && row.moStat<=0){
			return $('#infolistB').datagrid('getRowIndex',row);
		}else{
			return -1;
		}
	}
	//执行科室弹出窗口
	function searchDeptInfo(){
		var row=null;
		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='长期医嘱'){
			row = $('#infolistA').datagrid('getSelected');
		}else if(tab.title=='临时医嘱'){
			row = $('#infolistB').datagrid('getSelected');
		}
		if(row!=null&&$('#adProjectTdId').combobox('getValue')!='16'&&$('#adProjectTdId').combobox('getValue')!='18'&&$('#adProjectTdId').combobox('getValue')!='17'){
			var deptName = encodeURI($('#adExeDeptTdId').textbox('getValue').trim());
			AdddilogModel("adExeDeptModlDivId","科室信息",'<%=basePath%>inpatient/docAdvManage/queryExeDept.action?inpatientOrder.execDpnm='+deptName,'45%','70%');
		}else{
			$.messager.alert('提示',"必须选择一条非药品医嘱！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}						
	}
	//最小单位 显示		
	function minFamater(value,row,index){			
		if(value!=null&&value!=""){
			return mindataMaps[value];					
		}			
	}		
	//退出开立
	function adEndAdvice(){	
		$('#adNameId').text('');//姓名
		$('#adSexId').text('');//性别
		$('#adFreeCostId').text('');//年龄
		$('#adPactId').text('');//合作单位
		$('#adProjectDivId').hide();
		$('#adWestMediDivId').hide();
		$('#adEndAdviceBtn').menubutton('disable');//退出开立
		$('#adSaveAdviceBtn').menubutton('disable');//保存医嘱
		$('#adDelAdviceBtn').menubutton('disable');//删除医嘱
		$('#adStopAdviceBtn').menubutton('disable');//停止医嘱
		$('#adAuditAdviceBtn').menubutton('disable');//审核医嘱
		$('#adHerbMedicineBtn').menubutton('disable');//草药查询
		$('#adAddGroupBtn').menubutton('disable');//添加组套
		$('#adCancelGroupBtn').menubutton('disable');//取消组套
		$('#adSaveGroupBtn').menubutton('disable');//保存组套
		$('#reformAdviceBtn').menubutton('enable');//重整医嘱
		$('#adStackTree').tree('loadData',[]);//清除组套Tree
		$("#cc").layout('collapse','west');//隐藏组套信息
		$("#comboboxId").val(0);
		clearDgAdDgList();//清空datagrid数据
		$('#docAdvTypeFilt').combobox('enable');
		clearProject();//清空项目类别区域信息
	}
	//保存组套
	function adSaveGroup(){
		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='长期医嘱'){	
			var rowsA = $('#infolistA').datagrid('getChecked');
			if(rowsA!=null&&rowsA.length>0){
				if(rowsA.length==1){
					$.messager.alert('提示',"至少选择两条记录添加组套!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return
				}else{
					$('#stackSaveModleDivId').show();
					$('#stackSaveModleDivId').dialog({    
					    title:'确认组套信息',    
					    width:'80%',    
					    height:'54%',    
					    closed: false,
					    closable:false,    
					    cache: false,
					    modal: true   
					});
					StackInfoSource();
					for(var i=0;i<rowsA.length;i++){
						var unitView = "";
						if(rowsA[i].itemType==0){
							unitView = rowsA[i].drugpackagingUnit;
						}else{
							unitView = drugpackagingunitMap[rowsA[i].drugpackagingUnit];
						}
						$('#adStackInfoSaveModelDgId').datagrid('appendRow',{
							id:rowsA[i].itemCode,//名称
							name:rowsA[i].itemName,//名称
							spec:rowsA[i].specs,//规格
							packagingnum:rowsA[i].qtyTot,//包装数量
							unit:rowsA[i].drugpackagingUnit,//单位
							unitView:unitView,//单位
							defaultprice:rowsA[i].itemPrice,//默认价
							frequencyCode:rowsA[i].frequencyCode,//频次编码
							frequencyCodeView:rowsA[i].frequencyName,//频次编码
							usageCode:rowsA[i].usageCode,//用法名称
							usageCodeView:rowsA[i].useName,//用法名称
							onceDose:rowsA[i].doseOnce,//每次服用剂量
							doseUnit:rowsA[i].doseUnit,//剂量单位
							doseUnitView:drugdoseunitMaps[rowsA[i].doseUnit],//剂量单位
							mainDrugshow:1,//主药标记
							dateBgn:rowsA[i].dateBgn,//医嘱开始时间
							dateEnd:rowsA[i].dateEnd,//医嘱结束时间
							itemNote:rowsA[i].itemNote,//检查部位
							days:rowsA[i].useDays,//草药付数
							remark:rowsA[i].moNote2,//备注
							isDrugShow:rowsA[i].ty,//是否药品
							isDrugShowView:rowsA[i].ty==1?'是':'否',
							stackInfoNum:rowsA[i].packQty//开立数量
						});
					} 
				}
			}else{
				$.messager.alert('提示',"请选择需要添加组套的信息!");
				setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				return;
			}
		}else if(tab.title=='临时医嘱'){
			var rows = $('#infolistB').datagrid('getChecked');
			if(rows!=null&&rows.length>0){
				if(rows.length==1){
					$.messager.alert('提示',"至少选择两条记录添加组套!");
					setTimeout(function(){
						$(".messager-body").window('close');
					 },3500);
					return
				}else{
					$('#stackSaveModleDivId').show();
					$('#stackSaveModleDivId').dialog({    
					    title:'确认组套信息',    
					    width:'80%',    
					    height:'54%',    
					    closed: false,
					    closable:false,    
					    cache: false,
					    modal: true   
					});
					StackInfoSource();
					for(var i=0;i<rows.length;i++){
						var unitView = "";
						if(rowsA[i].itemType==0){
							unitView = rowsA[i].drugpackagingUnit;
						}else{
							unitView = drugpackagingunitMap[rowsA[i].drugpackagingUnit];
						}
						$('#adStackInfoSaveModelDgId').datagrid('appendRow',{
							id:rows[i].itemCode,//名称
							name:rows[i].itemName,//名称
							spec:rows[i].specs,//规格
							packagingnum:rowsA[i].qtyTot,//包装数量
							unit:rows[i].drugpackagingUnit,//单位
							unitView:unitView,//单位
							defaultprice:rows[i].itemPrice,//默认价
							frequencyCode:rows[i].frequencyCode,//频次编码
							frequencyCodeView:rows[i].frequencyName,//频次编码
							usageCode:rows[i].usageCode,//用法名称
							usageCodeView:rows[i].useName,//用法名称
							onceDose:rows[i].doseOnce,//每次服用剂量
							doseUnit:rows[i].doseUnit,//剂量单位
							doseUnitView:drugdoseunitMaps[rows[i].doseUnit],//剂量单位
							mainDrugshow:1,//主药标记
							dateBgn:rows[i].dateBgn,//医嘱开始时间
							dateEnd:rows[i].dateEnd,//医嘱结束时间
							itemNote:rows[i].itemNote,//检查部位
							days:rows[i].useDays,//草药付数
							remark:rows[i].moNote2,//备注
							isDrugShow:rows[i].ty,//是否药品
							isDrugShowView:rows[i].ty==1?'是':'否',
							stackInfoNum:rows[i].packQty//开立数量
						});
					} 
				}
			}else{
				$.messager.alert('提示',"请选择需要添加组套的信息!");
				setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				return;
			}
		}				
	}
	//组合
	function adAddGroup(){
		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='长期医嘱'){	
			var rowsA = $('#infolistA').datagrid('getChecked');
			if(rowsA!=null&&rowsA.length>0){
				if(rowsA.length==1){
					$.messager.alert('提示',"无法对一条记录进行组合！");
					setTimeout(function(){
						$(".messager-body").window('close');
					 },3500);
					return;
				}
				for(var i=0;i<rowsA.length;i++){
					if(rowsA[i].moStat!=0){
						$.messager.alert('提示',"非开立医嘱状态不能组合！");
						return;
					}
				}
				var one = '';
				var combFlag = '';
				if(rowsA[0].combNo!=''&&rowsA[0].combNo!=null){
					one = rowsA[0].combNo;
					combFlag = '1';
				}else{
					var maxCombNo1 = parseInt(maxCombNo())+1;
					var len = maxCombNo().length-maxCombNo1.toString().length;
					var maxNo = '';
					for(var j=0;j<len;j++){
						maxNo = maxNo+'0';
					}
					one = maxNo+maxCombNo1;
					combFlag = '1';
				}
				var fre = null;//频次
				var usageName = null;//用法
				var sysType = null;//系统类别
				var executiveDeptHid = null;//系统类别
				var setNum = null;//付数
				var totalNum = null;//总量
				var totalNum1 = null;//总量
				var sampleTept = null;//样本类型
				var checkPoint =null;//部位
				for(var i=0;i<rowsA.length;i++){
					if(fre==null){
						fre = rowsA[i].frequencyCode;
					}
					if(rowsA[i].frequencyCode!=fre){
						$.messager.alert('提示',"所选记录的频次信息不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(usageName==null){
						usageName = rowsA[i].useName;
					}
					if(rowsA[i].useName!=usageName){
						$.messager.alert('提示',"所选记录的用法信息不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(sysType==null){
						sysType = rowsA[i].classCode;
					}
					if(rowsA[i].classCode!=sysType){
						$.messager.alert('提示',"所选记录的类别不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(executiveDeptHid==null){
						executiveDeptHid = rowsA[i].execDpcd;
					}
					if(rowsA[i].execDpcd!=executiveDeptHid){
						$.messager.alert('提示',"所选记录的执行科室不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(setNum==null){
						setNum = rowsA[i].useDays;
					}
					if(rowsA[i].useDays!=setNum){
						$.messager.alert('提示',"所选记录的付数不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(rowsA[i].itemType=='1'){
						if(rowsA[i].packQty==null){
							rowsA[i].packQty = 1;
						}
						if(totalNum==null){
							if(rowsA[i].qtyTot==rowsA[i].drugpackagingUnit){
								totalNum = rowsA[i].qtyTot*rowsA[i].packQty;
							}else{
								totalNum = rowsA[i].qtyTot;
							}									
						}
						if(rowsA[i].qtyTot==rowsA[i].drugpackagingUnit){
							totalNum1 = rowsA[i].qtyTot*rowsA[i].packQty;
						}else{
							totalNum1 = rowsA[i].qtyTot;
						}
						if(totalNum1!=totalNum){
							$.messager.alert('提示',"所选记录的总量不同，无法进行组合操作！");
							setTimeout(function(){
								$(".messager-body").window('close');
							 },3500);
							return;
						}
					}							
					if(sampleTept==null){
						sampleTept = rowsA[i].labCode;
					}
					if(rowsA[i].labCode!=sampleTept){
						$.messager.alert('提示',"所选记录的样本类型不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}
					if(checkPoint==null){
						checkPoint = rowsA[i].itemNote;
					}
					if(rowsA[i].itemNote!=checkPoint){
						$.messager.alert('提示',"所选记录的检查部位不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
				}
				var group = null;
				var datas = new Array();//存放数据的数组
				var datasHis = new Array();//存放数据的数组
				for(var i=0;i<rowsA.length;i++){
					datas[datas.length] = rowsA[i];
				}
				for(var i=0;i<rowsA.length;i++){
					$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowsA[i]));
				}					
				if(one!=null){
					var rowsHis = $('#infolistA').datagrid('getRows');
					for(var i=0;i<rowsHis.length;i++){
						if(rowsHis[i].combNo==one){
							datas[datas.length] = rowsHis[i];
						}else{
							datasHis[datasHis.length] = rowsHis[i];
						}
					}
					group = one;
				}else{
					var rowsHis = $('#infolistA').datagrid('getRows');
					for(var i=0;i<rowsHis.length;i++){
						datasHis[datasHis.length] = rowsHis[i];
					}
					group = groupVal;
					groupVal += 1;
				}
				var rwosNewHis = $('#infolistA').datagrid('getRows');
				var rwosNewHisLen = rwosNewHis.length;
				for(var i=0;i<rwosNewHisLen;i++){
					$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rwosNewHis[0]));
				}
				if(datasHis.length>0){
					for(var i=datasHis.length-1;i>=0;i--){
						$('#infolistA').datagrid('insertRow',{
							index: 0,	
							row:datasHis[i]
						});
					}
				}
                if(datas.length>0){
                	for(var i=0;i<datas.length;i++){
                		$('#infolistA').datagrid('insertRow',{
							index: i,	
							row:datas[i]
						});
                		$('#infolistA').datagrid('checkRow',i);
						$('#infolistA').datagrid('updateRow',{
							index: i,
							row: {
								combNo:group,
								changeNo:1,
								combFlag:combFlag
							}
						});
						if(i==0){
							$('#infolistA').datagrid('updateRow',{
								index: i,
								row: {
									combNoFlag:'┓'
								}
							});
						}else if(i>0 && i<datas.length-1){
							$('#infolistA').datagrid('updateRow',{
								index: i,
								row: {
									combNoFlag:'┃'
								}
							});								
						}else if(i==datas.length-1){
							$('#infolistA').datagrid('updateRow',{
								index: i,
								row: {
									combNoFlag:'┛'
								}
							});	
						}								
                	}
                }                   
			}else{
				$.messager.alert('提示',"请选择要组合的医嘱信息！");
				setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				return;			
			}
		}else if(tab.title=='临时医嘱'){//长期医嘱、临时医嘱判断
			var rows = $('#infolistB').datagrid('getChecked');
			if(rows!=null&&rows.length>0){
				if(rows.length==1){
					$.messager.alert('提示',"无法对一条记录进行组合！");
					setTimeout(function(){
						$(".messager-body").window('close');
					 },3500);
					return;
				}
				for(var i=0;i<rows.length;i++){
					if(rows[i].moStat!=0){
						$.messager.alert('提示',"非开立医嘱状态不能组合！");
						return;
					}
				}
				var one = '';
				var combFlag = '';
				if(rows[0].combNo!=''&&rows[0].combNo!=null){
					one = rows[0].combNo;
					combFlag = '1';
				}else{							
					var maxCombNo1 = parseInt(maxCombNo())+1;
					var len = maxCombNo().length-maxCombNo1.toString().length;
					var maxNo = '';
					for(var j=0;j<len;j++){
						maxNo = maxNo+'0';
					}
					one = maxNo+maxCombNo1;
					combFlag = '1';
				}
				var fre = null;//频次
				var usageName = null;//用法
				var sysType = null;//系统类别
				var executiveDeptHid = null;//系统类别
				var setNum = null;//付数
				var totalNum = null;//总量
				var totalNum1 = null;//总量
				var sampleTept = null;//样本类型
				var checkPoint =null;//部位
				for(var i=0;i<rows.length;i++){						
					if(fre==null){
						fre = rows[i].frequencyCode;
					}
					if(rows[i].frequencyCode!=fre){
						$.messager.alert('提示',"所选记录的频次信息不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(usageName==null){
						usageName = rows[i].useName;
					}
					if(rows[i].useName!=usageName){
						$.messager.alert('提示',"所选记录的用法信息不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(sysType==null){
						sysType = rows[i].classCode;
					}
					if(rows[i].classCode!=sysType){
						$.messager.alert('提示',"所选记录的类别不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(executiveDeptHid==null){
						executiveDeptHid = rows[i].execDpcd;
					}
					if(rows[i].execDpcd!=executiveDeptHid){
						$.messager.alert('提示',"所选记录的执行科室不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(setNum==null){
						setNum = rows[i].useDays;
					}
					if(rows[i].useDays!=setNum){
						$.messager.alert('提示',"所选记录的付数不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(rows[i].itemType=='1'){
						if(rows[i].packQty==null){
							rows[i].packQty = 1;
						}
						if(totalNum==null){
							if(rows[i].qtyTot==rows[i].drugpackagingUnit){
								totalNum = rows[i].qtyTot*rows[i].packQty;
							}else{
								totalNum = rows[i].qtyTot;
							}									
						}
						if(rows[i].qtyTot==rows[i].drugpackagingUnit){
							totalNum1 = rows[i].qtyTot*rows[i].packQty;
						}else{
							totalNum1 = rows[i].qtyTot;
						}
						if(totalNum1!=totalNum){
							$.messager.alert('提示',"所选记录的总量不同，无法进行组合操作！");
							setTimeout(function(){
								$(".messager-body").window('close');
							 },3500);
							return;
						}
					}							
					if(sampleTept==null){
						sampleTept = rows[i].labCode;
					}
					if(rows[i].labCode!=sampleTept){
						$.messager.alert('提示',"所选记录的样本类型不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
					if(checkPoint==null){
						checkPoint = rows[i].itemNote;
					}
					if(rows[i].itemNote!=checkPoint){
						$.messager.alert('提示',"所选记录的检查部位不同，无法进行组合操作！");
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;
					}
				}
				var group = null;
				var datas = new Array();//存放数据的数组
				var datasHis = new Array();//存放数据的数组
				for(var i=0;i<rows.length;i++){
					datas[datas.length] = rows[i];
				}
				for(var i=0;i<rows.length;i++){
					$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',rows[i]));
				}					
				if(one!=null){
					var rowsHis = $('#infolistB').datagrid('getRows');
					for(var i=0;i<rowsHis.length;i++){
						if(rowsHis[i].combNo==one){
							datas[datas.length] = rowsHis[i];
						}else{
							datasHis[datasHis.length] = rowsHis[i];
						}
					}
					group = one;
				}else{
					var rowsHis = $('#infolistB').datagrid('getRows');
					for(var i=0;i<rowsHis.length;i++){
						datasHis[datasHis.length] = rowsHis[i];
					}
					group = groupVal;
					groupVal += 1;
				}
				var rwosNewHis = $('#infolistB').datagrid('getRows');
				var rwosNewHisLen = rwosNewHis.length;
				for(var i=0;i<rwosNewHisLen;i++){
					$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',rwosNewHis[0]));
				}
				if(datasHis.length>0){
					for(var i=datasHis.length-1;i>=0;i--){
						$('#infolistB').datagrid('insertRow',{
							index: 0,	
							row:datasHis[i]
						});
					}
				}
                if(datas.length>0){
                	for(var i=0;i<datas.length;i++){
                		$('#infolistB').datagrid('insertRow',{
							index: i,	
							row:datas[i]
						});
                		$('#infolistB').datagrid('checkRow',i);
						$('#infolistB').datagrid('updateRow',{
							index: i,
							row: {
								combNo:group,
								changeNo:1,
								combFlag:combFlag
							}
						});
						if(i==0){
							$('#infolistB').datagrid('updateRow',{
								index: i,
								row: {
									combNoFlag:'┓'
								}
							});
						}else if(i>0 && i<datas.length-1){
							$('#infolistB').datagrid('updateRow',{
								index: i,
								row: {
									combNoFlag:'┃'
								}
							});								
						}else if(i==datas.length-1){
							$('#infolistB').datagrid('updateRow',{
								index: i,
								row: {
									combNoFlag:'┛'
								}
							});	
						}
                	}
                }
			}else{
				$.messager.alert('提示',"请选择要组合的医嘱信息！");
				setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				return;			
			}
		}								
	}
	//取消组合
	function adCancelGroup(){	
		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='长期医嘱'){	
			var rowsA = $('#infolistA').datagrid('getChecked');
			if(rowsA!=null&&rowsA.length>0){
				var datas = new Array();//存放需要拆分的组合的标记
				for(var i=0;i<rowsA.length;i++){					
					if(rowsA[i].combNo!=null&&rowsA[i].combNo!=''){
						if(datas.indexOf(rowsA[i].combNo)==-1){
							datas[datas.length] = rowsA[i].combNo;
						}
					}
				}
				if(datas.length>0){
					var datasDel = new Array();//存放需要拆分的组合
					var datasArr = new Array();//存放数据的数组
					var rowsHis = $('#infolistA').datagrid('getRows');
					$('#infolistA').datagrid('uncheckAll');
					for(var i=0;i<rowsHis.length;i++){
						for(var j=0;j<datas.length;j++){
							if(rowsHis[i].combNo==datas[j]){
								datasDel[datasDel.length] = rowsHis[i];
								$('#infolistA').datagrid('checkRow',$('#infolistA').datagrid('getRowIndex',rowsHis[i]));
							}
						}
					}
					for(var i=0;i<rowsA.length;i++){
						$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowsA[i]));							
					}							
					for(var i=0;i<rowsA.length;i++){
						var leng = $('#infolistA').datagrid('getRows').length;							
						$('#infolistA').datagrid('insertRow',{
							row:rowsA[i]
						});
						$('#infolistA').datagrid('updateRow',{
							index:leng,
							row: {
								combNo:"",
								changeNo:1,
								combFlag:'',
								combNoFlag:''
							}
						});
					}
				}else{
					$.messager.alert('提示',"没有符合拆分条件的组合！");
					setTimeout(function(){
						$(".messager-body").window('close');
					 },3500);
					return;
				}
			}else{
				$.messager.alert('提示',"请选择需要拆分的组合！");
				setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				return;
			}
		}else if(tab.title=='临时医嘱'){
			var rows = $('#infolistB').datagrid('getChecked');
			if(rows!=null&&rows.length>0){
				var datas = new Array();//存放需要拆分的组合的标记
				for(var i=0;i<rows.length;i++){					
					if(rows[i].combNo!=null&&rows[i].combNo!=''){
						if(datas.indexOf(rows[i].combNo)==-1){
							datas[datas.length] = rows[i].combNo;
						}
					}
				}
				if(datas.length>0){
					var datasDel = new Array();//存放需要拆分的组合
					var datasArr = new Array();//存放数据
					var rowsHis = $('#infolistB').datagrid('getRows');
					$('#infolistB').datagrid('uncheckAll');
					for(var i=0;i<rowsHis.length;i++){
						for(var j=0;j<datas.length;j++){
							if(rowsHis[i].combNo==datas[j]){
								datasDel[datasDel.length] = rowsHis[i];
								$('#infolistB').datagrid('checkRow',$('#infolistB').datagrid('getRowIndex',rowsHis[i]));
							}
						}
					}
					for(var i=0;i<rows.length;i++){
						$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',rows[i]));							
					}							
					for(var i=0;i<rows.length;i++){
						var leng = $('#infolistB').datagrid('getRows').length;
						$('#infolistB').datagrid('insertRow',{
							row:rows[i]
						});
						$('#infolistB').datagrid('updateRow',{
							index:leng,
							row: {
								combNo:"",
								changeNo:1,
								combFlag:'',
								combNoFlag:''
							}
						});
					}
				}else{
					$.messager.alert('提示',"没有符合拆分条件的组合！");
					setTimeout(function(){
						$(".messager-body").window('close');
					 },3500);
					return;
				}
			}else{
				$.messager.alert('提示',"请选择需要拆分的组合！");
				setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				return;
			}
		}			
	}
	//页面中最大的组合号
	function maxCombNo(){
		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='长期医嘱'){	
			var rows = $('#infolistA').datagrid('getRows');
			if(rows.length>0){
				var maxComnNo = rows[0].combNo;
				for(var i=1;i<rows.length;i++){												
					if(maxComnNo<rows[i].combNo){
						maxComnNo = rows[i].combNo;
					}											
				}
			}else{
				maxComnNo = '000000000000';
			}					
			return maxComnNo;
		}else if(tab.title=='临时医嘱'){
			var rows = $('#infolistB').datagrid('getRows');
			if(rows.length>0){
				var maxComnNo = rows[0].combNo;
				for(var i=1;i<rows.length;i++){												
					if(maxComnNo<rows[i].combNo){
						maxComnNo = rows[i].combNo;
					}											
				}
			}else{
				maxComnNo = '000000000000';
			}	
			return maxComnNo;
		}					
	}
	//删除医嘱
	function adDelAdvice(){
		$.messager.confirm('确认','是否确定删除医嘱?此操作不可撤销!',function(e){
			if(e){
				var qq = $('#ttta').tabs('getSelected');
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){	
					var rowA = $('#infolistA').datagrid('getChecked');
					if(rowA.length>1){
						var itemName = '';
						for(var i = 0;i<rowA.length;i++){
							if(rowA[i].id==null||rowA[i].id==''){
								$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowA[i]));
							}else{
								if(itemName==''){
									itemName=rowA[i].itemName;
								}else{
									itemName+='，'+rowA[i].itemName;
								}
								continue;
							}	
						}
						if(itemName!=''){
							$.messager.alert('提示',"医嘱"+itemName+"已保存，请逐条删除!");	
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
						return;
					}else if(rowA.length==1){
						if(rowA[0].moStat<=0){
							if(rowA[0].id==null||rowA[0].id==''){
								$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowA[0]));
							}else{
								$.ajax({
				 					url: '<%=basePath%>inpatient/docAdvManage/delDocAdvInfo.action',
				 					data:'inpatientOrder.combNo='+rowA[0].combNo,
				 					type:'post',
				 					success: function(data) {
				 						if(data.resCode='success'){
				 							$.messager.alert('提示',"删除成功！");
				 							setTimeout(function(){
												$(".messager-body").window('close');
											 },3500);
				 							$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowA[0]));
				 							reloadRow();
				 						}else{
				 							$.messager.alert('提示',"删除失败！");
				 						}
				 					}
				 				});
							}
						}else if(rowA[0].moStat==3){
							$.messager.alert('提示','医嘱'+rowA[0].itemName+'已作废不可删除或再作废！ ');
							setTimeout(function(){
								$(".messager-body").window('close');
							 },3500);
						}else if(rowA[0].moStat==1||rowA[0].moStat==2){	
							AdddilogModel("adStopData-window","停止医嘱","<%=basePath%>inpatient/docAdvManage/stopAdviceInfo.action",'18%','26%');
						}	
					}else{
						$.messager.alert('提示',"请选择要删除的医嘱信息!");	
						setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						return;	
					}
					reloadRow();
				}
				else if(tab.title=='临时医嘱'){
					var row = $('#infolistB').datagrid('getChecked');
					if(row.length>1){
						var itemName = '';
						for(var i = 0;i<row.length;i++){
							if(row[i].id==null||row[i].id==''){
								$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',row[i]));
							}else{
								if(itemName==''){
									itemName=row[i].itemName;
								}else{
									itemName+='，'+row[i].itemName;
								}									
								continue;
							}	
						}
						if(itemName!=''){
							$.messager.alert('提示',"医嘱"+itemName+"已保存，请逐条删除!");
							setTimeout(function(){
								$(".messager-body").window('close');
							 },3500);
						}	
						reloadRow();
						changeCostunitB();
						return;	
					}else if(row.length==1){
						if(row[0].moStat<=0){
							if(row[0].id==null||row[0].id==''){
								$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',row[0]));
							}else{										
								$.ajax({
				 					url: '<%=basePath%>inpatient/docAdvManage/delDocAdvInfo.action',
				 					data:'inpatientOrder.combNo='+row[0].combNo,
				 					type:'post',
				 					success: function(data) {
				 						if(data.resCode='success'){
				 							$.messager.alert('提示',"删除成功！");
				 							setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
				 							$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',row[0]));
				 						}else{
				 							$.messager.alert('提示',"删除失败！");
				 						}
				 					}
				 				});
							}
						}else if(row[0].moStat==3){
							$.messager.alert('提示','医嘱'+row[0].itemName+'已作废不可删除或再作废！ ');
							setTimeout(function(){
								$(".messager-body").window('close');
							 },3500);
						}else if(row[0].moStat==1||row[0].moStat==2){	
							AdddilogModel("adStopData-window","停止医嘱","<%=basePath%>inpatient/docAdvManage/stopAdviceInfo.action",'18%','26%');
						}
						changeCostunitB();
						reloadRow();
					}else{
						$.messager.alert('提示',"请选择要删除的医嘱信息!");	
						setTimeout(function(){
							$(".messager-body").window('close');
					 	},3500);
						return;
					}	
				}
			}
		});
	}
	//刷新行
	function reloadRow(){
		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='长期医嘱'){	
			 var rowsA = $('#infolistA').datagrid('getRows');
			 for(var i=0;i<rowsA.length;i++){
				 $('#infolistA').datagrid('refreshRow',$('#infolistA').datagrid('getRowIndex',rowsA[i])); 
			 }
		}else if(tab.title=='临时医嘱'){
			 var rows = $('#infolistB').datagrid('getRows');
			 for(var i=0;i<rows.length;i++){
				 $('#infolistB').datagrid('refreshRow',$('#infolistB').datagrid('getRowIndex',rows[i])); 
			 }
		}
	}
	function decreaseCost(rowData){
		if(rowData.classCode=="09"){//非药品
			var fypCost1 = parseFloat($('#fypCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(fypCost1=="0"){
				$("#fypCost").hide();
			}else{
				$("#fypCost1").text(fypCost1.toFixed(2));
			}
		}else if(rowData.classCode=="13"){//膳食
			var ssCost1 = parseFloat($('#ssCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(ssCost1=="0"){
				$("#ssCost").hide();
			}else{
				$("#ssCost1").text(ssCost1.toFixed(2));
			}
		}else if(rowData.classCode=="14"){//护理级别
			var hljbCost1 = parseFloat($('#hljbCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(hljbCost1=="0"){
				$("#hljbCost").hide();
			}else{
				$("#hljbCost1").text(hljbCost1.toFixed(2));
			}
		}else if(rowData.classCode=="15"){//描述医嘱
			var msyzCost1 = parseFloat($('#msyzCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(msyzCost1=="0"){
				$("#msyzCost").hide();
			}else{
				$("#msyzCost1").text(msyzCost1.toFixed(2));
			}
		}else if(rowData.classCode=="17"){//中成药
			var zcyCost1 = parseFloat($('#zcyCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(zcyCost1=="0"){
				$("#zcyCost").hide();
			}else{
				$("#zcyCost1").text(zcyCost1.toFixed(2));
			}
		}else if(rowData.classCode=="18"){//西药
			var xyCost1 = parseFloat($('#xyCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(xyCost1=="0"){
				$("#xyCost").hide();
			}else{
				$("#xyCost1").text(xyCost1.toFixed(2));
			}
		}else if(rowData.classCode=="06"){//手术
			var shoushuCost1 = parseFloat($('#shoushuCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(shoushuCost1=="0"){
				$("#shoushuCost").hide();
			}else{
				$("#shoushuCost1").text(shoushuCost1.toFixed(2));
			}
		}else if(rowData.classCode=="07"){//检查
			var jcCost1 = parseFloat($('#jcCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(jcCost1=="0"){
				$("#jcCost").hide();
			}else{
				$("#jcCost1").text(jcCost1.toFixed(2));
			}
		}else if(rowData.classCode=="08"){//检验
			var jyCost1 = parseFloat($('#jyCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(jyCost1=="0"){
				$("#jyCost").hide();
			}else{
				$("#jyCost1").text(jyCost1.toFixed(2));
			}
		}else if(rowData.classCode=="10"){//预约出院
			var yycyCost1 = parseFloat($('#yycyCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(yycyCost1=="0"){
				$("#yycyCost").hide();
			}else{
				$("#yycyCost1").text(yycyCost1.toFixed(2));
			}
		}else if(rowData.classCode=="11"){//转床
			var zcCost1 = parseFloat($('#zcCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(zcCost1=="0"){
				$("#zcCost").hide();
			}else{
				$("#zcCost1").text(zcCost1.toFixed(2));
			}
		}else if(rowData.classCode=="12"){//转科
			var zkCost1 = parseFloat($('#zkCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(zkCost1=="0"){
				$("#zkCost").hide();
			}else{
				$("#zkCost1").text(zkCost1.toFixed(2));
			}
		}else if(rowData.classCode=="16"){//中草药
			var zcaoyCost1 = parseFloat($('#zcaoyCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(zcaoyCost1=="0"){
				$("#zcaoyCost").hide();
			}else{
				$("#zcaoyCost1").text(zcaoyCost1.toFixed(2));
			}
		}else if(rowData.classCode=="04"){//会诊
			var hzCost1 = parseFloat($('#hzCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(hzCost1=="0"){
				$("#hzCost").hide();
			}else{
				$("#hzCost1").text(hzCost1.toFixed(2));
			}
		}else if(rowData.classCode=="01"){//病情
			var bqCost1 = parseFloat($('#bqCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(bqCost1=="0"){
				$("#bqCost").hide();
			}else{
				$("#bqCost1").text(bqCost1.toFixed(2));
			}
		}else if(rowData.classCode=="02"){//其他
			var qtCost1 = parseFloat($('#qtCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(qtCost1=="0"){
				$("#qtCost").hide();
			}else{
				$("#qtCost1").text(qtCost1.toFixed(2));
			}
		}else if(rowData.classCode=="03"){//计量
			var jlCost1 = parseFloat($('#jlCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(jlCost1=="0"){
				$("#jlCost").hide();
			}else{
				$("#jlCost1").text(jlCost1.toFixed(2));
			}
		}else if(rowData.classCode=="05"){//治疗
			var zlCost1 = parseFloat($('#zlCost1').text())-rowData.itemPrice*row[q].qtyTot;
			if(zlCost1=="0"){
				$("#zlCost").hide();
			}else{
				$("#zlCost1").text(zlCost1.toFixed(2));
			}
		}
	}
	//停止医嘱
 	function adStopAdvice(){
 		var qq = $('#ttta').tabs('getSelected');				
		 var tab = qq.panel('options');
		 if(tab.title=='长期医嘱'){
			var rows = $('#infolistA').datagrid('getRows');
			if(rows == null||rows == ""){
				$.messager.alert('提示',"该患者没有能停止的医嘱!");
				 return;
			}
		 }else if(tab.title=='临时医嘱'){
			 var rows = $('#infolistB').datagrid('getRows');
			 if(rows == null||rows == ""){
					$.messager.alert('提示',"该患者没有能停止的医嘱!");
					return;
			 }
		 }
		AdddilogModel("adStopData-window","停止医嘱","<%=basePath%>inpatient/docAdvManage/stopAdviceInfo.action",'18%','26%');														 											
 	}
	//时间点
	 function adTimePoints(){	
		 var fre = null;
		 var qq = $('#ttta').tabs('getSelected');				
		 var tab = qq.panel('options');
		 if(tab.title=='长期医嘱'){
			 var rowA = $('#infolistA').datagrid('getSelected');				 
			 if(rowA==null){
				 $.messager.alert('提示',"请选择一条医嘱!");	
				 setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				 return;
			 }else if(rowA.frequencyCode==null||rowA.frequencyCode==''){
				 $.messager.alert('提示',"所选择医嘱频次为空，不能进行特殊频次设置!");
				 setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				 return;
			 }else{
				 $.ajax({
					 url:'<%=basePath%>inpatient/docAdvManage/queryFrePeriod.action',
					 data:'inpatientOrder.frequencyCode='+rowA.frequencyCode,
					 type:'post',
					 success:function(data){
						 fre = data;	
						 if(parseInt(fre)>5){//放在ajax里面才能同步								 
							 $.messager.alert('提示',"所选择医嘱频次执行频率次数超过5次，不能进行特殊频次设置!");
							 setTimeout(function(){
								$(".messager-body").window('close');
							 },3500);
					 		 return;
					 	 }else{				 		 
					 		AdddilogModel("adSpeFreData-window","特殊频次",'<%=basePath%>inpatient/docAdvManage/speFreInfo.action?inpatientOrder.frequencyCode='+rowA.frequencyCode+'&inpatientOrder.frequencyName='+rowA.frequencyName+'&inpatientOrder.execTimes='+rowA.execTimes+'&inpatientOrder.execDose='+rowA.execDose+'&inpatientOrder.doseOnce='+rowA.doseOnce,'22%','43%');
					 	 }	
					 }
				 });				 	 				 
			 }	
		 }else{
			 var row = $('#infolistB').datagrid('getSelected');				 
			 if(row==null){
				 $.messager.alert('提示',"请选择一条医嘱!");	
				 setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				 return;
			 }else if(row.frequencyCode==null||row.frequencyCode==''){
				 $.messager.alert('提示',"所选择医嘱频次为空，不能进行特殊频次设置!");
				 setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
				 return;			 
			 }else{			
				 $.ajax({
					 url:'<%=basePath%>inpatient/docAdvManage/queryFrePeriod.action',
					 data:'inpatientOrder.frequencyCode='+row.frequencyCode,
					 type:'post',
					 success:function(data){
						 fre = data;							 
						 if(parseInt(fre)>5){//放在ajax里面才能同步	
							 $.messager.alert('提示',"所选择医嘱频次执行频率次数超过5次，不能进行特殊频次设置!");
							 setTimeout(function(){
								$(".messager-body").window('close');
							 },3500);
					 		 return;
					 	 }else{				 		 
					 		AdddilogModel("adSpeFreData-window","特殊频次",'<%=basePath%>inpatient/docAdvManage/speFreInfo.action?inpatientOrder.frequencyCode='+row.frequencyCode+'&inpatientOrder.frequencyName='+row.frequencyName+'&inpatientOrder.execTimes='+row.execTimes+'&inpatientOrder.execDose='+row.execDose+'&inpatientOrder.doseOnce='+row.doseOnce,'22%','43%');
					 	 }	
					 }
				 });				 	 				 
			 }	
		 }			 			 			 		 
	 }
	 /**
		 * 保存表格新增医嘱数据
		 * @author  ygq
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2015-12-29
		 * @version 1.0
		 */		 
		function adSaveAdvice(data){				
			
			var qq = $('#ttta').tabs('getSelected');				
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){		
				 var rows = '';
				 if(data!=null&&data.length>0){
					 rows = data;
				 }else{
					 rows = $('#infolistA').datagrid('getRows');
					 if(rows.length == 0){
						 $.messager.alert('提示','尚未添加任何医嘱内容，请添加后保存');
						 setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						 return ;
					 }
					 var array = new Array();
					 for(var i=0;i<rows.length;i++){
						 if(rows[i].changeNo==1){
							 array.push(rows[i]);
						 }
					 }
					rows = array;
				 }	
				 var name='';
				 for(var i =0;i<rows.length;i++){
					 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)==0.0){
						 if(name==''){
							 name = rows[i].itemName;
						 }else{
							 name = name+','+rows[i].itemName;
						 }						 
					 }					 				 
						 if(rows[i].id==null){
							 rows[i].id='';
						 }
						 if(rows[i].className==null){
							 rows[i].className='';
						 }
						 if(rows[i].combNo== null){
							 rows[i].combNo='';						 						 
						 }						 
						 if(rows[i].qtyTot== null){
							 rows[i].qtyTot='';						 						 
						 }
						 if(rows[i].packQty== null){
							 rows[i].packQty='';						 						 
						 }
						 if(rows[i].specs== null){
							 rows[i].specs='';						 						 
						 }
						 if(rows[i].drugpackagingUnit== null){
							 rows[i].drugpackagingUnit='';						 						 
						 }						 
						 if(rows[i].doseOnce== null){
							 rows[i].doseOnce='';						 						 
						 }					 
						 if(rows[i].priceUnit== null){
							 rows[i].priceUnit='';						 						 
						 }						 
						 if(rows[i].doseOnces== null){
							 rows[i].doseOnces='';						 						 
						 }
						 if(rows[i].doseUnit== null){
							 rows[i].doseUnit='';						 						 
						 }
						 if(rows[i].useDays== null){
							 rows[i].useDays='';						 						 
						 }	
						 if(rows[i].itemType== '1'){//药品校验
							 //如果药品本身单位和包装单位一致  则数量*包装单位
							 //priceUnit 单位（总量单位） drugpackagingUnit 包装单位 
							 //包装单位渲染加 'B' 最小单位渲染加'Z' 单位判断
							 if(rows[i].priceUnit==('B'+rows[i].drugpackagingUnit)&&rows[i].priceUnit!=''){
								 if(rows[i].doseOnce!=''&&rows[i].baseDose!=''&&rows[i].qtyTot!=''&&rows[i].packQty){
									 if((rows[i].doseOnce/1).toFixed(1)>rows[i].qtyTot*rows[i].packQty){
										 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');	
										 setTimeout(function(){
											$(".messager-body").window('close');
										 },3500);
										 return;
									 }
								 }								 
							 }else{
								 if(rows[i].doseOnce!=''&&rows[i].baseDose!=''&&rows[i].qtyTot!=''&&rows[i].packQty){
									 if((rows[i].doseOnce/1).toFixed(1)>rows[i].qtyTot){
										 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');
										 setTimeout(function(){
											$(".messager-body").window('close');
										 },3500);
										 return;
									 }
								 }								 
							 }
						    if(rows[i].classCode=='16'||rows[i].classCode=='17'||rows[i].classCode=='18'){//非药品不校验频次 hedong  2016-09-09  页面都无此输入项
							  if(rows[i].frequencyCode== null||rows[i].frequencyCode==''){
								 $.messager.alert('提示',rows[i].itemName+'频次为空，不能开立医嘱!');
								 setTimeout(function(){
									$(".messager-body").window('close');
								 },3500);
								 return;						 						 
							   } 
						     }
							 
							 if(rows[i].frequencyName== null){
								 rows[i].frequencyName='';						 						 
							 }
							 if(rows[i].classCode=='16'||rows[i].classCode=='17'||rows[i].classCode=='18'){//非药品不校验用法  hedong  2016-09-09  页面都无此输入项
								 if(rows[i].usageCode== null||rows[i].usageCode==''){
									 $.messager.alert('提示',rows[i].itemName+'用法为空，不能开立医嘱!');
									 setTimeout(function(){
										$(".messager-body").window('close');
									 },3500);
									 return;
								 }
							 }
							 
							 if(rows[i].useName== null){
								 rows[i].useName='';						 						 
							 }
							 if(rows[i].doseModelCode== null||rows[i].doseModelCode==''){
								 $.messager.alert('提示',rows[i].itemName+'剂型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].drugQuality== null||rows[i].drugQuality==''){
								 $.messager.alert('提示',rows[i].itemName+'性质为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].typeCode== null||rows[i].typeCode==''){
								 $.messager.alert('提示',rows[i].itemName+'医嘱类型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].classCode== null||rows[i].classCode==''){
								 $.messager.alert('提示',rows[i].itemName+'系统类型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].baseDose== null||rows[i].baseDose==''){
								 $.messager.alert('提示',rows[i].itemName+'基本剂量为空，不能开立医嘱!');
								 return;						 						 
							 }
						 }else{
							 if(rows[i].frequencyCode== null){
								 rows[i].frequencyCode='';						 						 
							 }
							 if(rows[i].frequencyName== null){
								 rows[i].frequencyName='';						 						 
							 }
							 if(rows[i].usageCode== null){
								 rows[i].usageCode='';						 						 
							 }
							 if(rows[i].useName== null){
								 rows[i].useName='';						 						 
							 }
							 if(rows[i].baseDose== null){
								 rows[i].baseDose=='';						 						 
							 }
						 }
						 if(rows[i].execDpcd== null){
							 rows[i].execDpcd='';						 						 
						 }
						 if(rows[i].execDpnm== null){
							 rows[i].execDpnm='';						 						 
						 }
						 if(rows[i].emcFlag== null){
							 rows[i].emcFlag='';						 						 
						 }
						 if(rows[i].labCode== null){
							 rows[i].labCode='';						 						 
						 }
						 if(rows[i].itemNote== null){
							 rows[i].itemNote='';						 						 
						 }
						 if(rows[i].moNote2== null){
							 rows[i].moNote2='';						 						 
						 }
						 if(rows[i].hypotest== null){
							 rows[i].hypotest='';						 						 
						 }
						 if(rows[i].dateBgn== null){
							 rows[i].dateBgn='';						 						 
						 }
						 if(rows[i].dateEnd== null){
							 rows[i].dateEnd='';						 						 
						 }
						 if(rows[i].permission== null){
							 rows[i].permission='';						 						 
						 }
						 if(rows[i].execTimes== null){
							 rows[i].execTimes='';						 						 
						 }	
						 if(rows[i].execDose== null){
							 rows[i].execDose='';						 						 
						 }	
						 if(rows[i].docName== null){
							 rows[i].docName='';						 						 
						 } 	
						 if(rows[i].combFlag== null){
							 rows[i].combFlag='';						 						 
						 } 
				 }
				 var inpatientNo = $("#inpatientNo").val();
				 var patientNo =$("#patientNo").val();
				 var deptCode = $("#deptCode").val();
				 var nurseCellCode = $("#nurseCellCode").val();
				 var babyFlag =$("#babyFlag").val();
				 var decmpsState = 1;
				 if(rows.length==0){
					 return;
				 }
				 if(rows[0].typeCode!='CZ'&&rows[0].typeCode!='ZC'){
					 decmpsState = 0;
				 }
				 var str = encodeURI(JSON.stringify(rows)); 
				 if(name!=''){
					$.messager.confirm('确认信息',name+'剂量太小,确认继续开立吗?',function(e){ 
						if(e){ 
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
							$.ajax({
								url: '<%=basePath%>inpatient/docAdvManage/saveInpatientOrder.action',
								data:'str='+str+'&inpatientNo='+inpatientNo+'&patientNo='+patientNo+'&deptCode='+deptCode+'&nurseCellCode='+nurseCellCode+'&babyFlag='+babyFlag+'&decmpsState='+decmpsState,
								type:'post',
								success: function(docadvdata) {	
									$.messager.progress('close');	// 如果提交成功则隐藏进度条
									var dataMap = eval("("+docadvdata+")");
									$.extend($.messager.defaults,{  
								        ok:"确定"
								    });  
						   			if(dataMap.resMsg=="error"){
						   				$.messager.alert('提示',dataMap.resCode);			   				
						        		
						        	}else if(dataMap.resMsg=="success"){
						        		$.messager.alert('提示',dataMap.resCode);	
						        		setTimeout(function(){
											$(".messager-body").window('close');
										 },3500);
						        		$('#infolistA').datagrid('reload');
						        	}else{
						        		$.messager.alert('提示','未知错误,请联系管理员!');
						        	
						        	}
								}
							});	
						}
					});
				 }else{		
					 $.messager.progress({text:'医嘱保存中，请稍后...',modal:true});	// 显示进度条
					 $.ajax({
							url: '<%=basePath%>inpatient/docAdvManage/saveInpatientOrder.action',
							data:'str='+str+'&inpatientNo='+inpatientNo+'&patientNo='+patientNo+'&deptCode='+deptCode+'&nurseCellCode='+nurseCellCode+'&babyFlag='+babyFlag+'&decmpsState='+decmpsState,
							type:'post',
							success: function(docadvdata) {	
								$.messager.progress('close');	// 如果提交成功则隐藏进度条
								var dataMap = eval("("+docadvdata+")");
								$.extend($.messager.defaults,{  
							        ok:"确定"
							    });  
					   			if(dataMap.resMsg=="error"){
					   				$.messager.alert('提示',dataMap.resCode);			   				
					        		
					        	}else if(dataMap.resMsg=="success"){
					        		$.messager.alert('提示',dataMap.resCode);	
					        		setTimeout(function(){
										$(".messager-body").window('close');
									 },3500);
					        		$('#infolistA').datagrid('reload');
					        	}else{
					        		$.messager.alert('提示','未知错误,请联系管理员!');
					        	
					        	}
							}
						});	 
				 }				 
			 }
			 else if (tab.title=='临时医嘱'){  				
				 var rows = '';
				 if(data!=null&&data.length>0){
					 rows = data;
				 }else{
					 rows = $('#infolistB').datagrid('getRows');
					 if(rows.length == 0){
						 $.messager.alert('提示','尚未添加任何医嘱内容，请添加后保存');	
						 setTimeout(function(){
							$(".messager-body").window('close');
						 },3500);
						 return ;
					 } 
					 var array = new Array();
					 for(var i=0;i<rows.length;i++){
						 if(rows[i].changeNo==1){
							 array.push(rows[i]);
						 }
					 }
					rows = array;	
				 }				
				 var name='';
				 for(var i =0;i<rows.length;i++){
					 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)==0.0){
						 if(name==''){
							 name = rows[i].itemName;
						 }else{
							 name = name+','+rows[i].itemName;
						 }						 
					 }
						 if(rows[i].id==null){
							 rows[i].id='';
						 }
						 if(rows[i].className==null){
							 rows[i].className='';
						 }
						 if(rows[i].combNo== null){
							 rows[i].combNo='';						 						 
						 }
						 if(rows[i].qtyTot== null){
							 rows[i].qtyTot='';						 						 
						 }
						 if(rows[i].packQty== null){
							 rows[i].packQty='';						 						 
						 }
						 if(rows[i].specs== null){
							 rows[i].specs='';						 						 
						 }
						 if(rows[i].drugpackagingUnit== null){
							 rows[i].drugpackagingUnit='';						 						 
						 }
						 if(rows[i].baseDose== null){
							 rows[i].baseDose='';						 						 
						 }
						 if(rows[i].doseOnce== null){
							 rows[i].doseOnce='';						 						 
						 }					 
						 if(rows[i].priceUnit== null){
							 rows[i].priceUnit='';						 						 
						 }
						 if(rows[i].doseOnces== null){
							 rows[i].doseOnces='';						 						 
						 }
						 if(rows[i].doseUnit== null){
							 rows[i].doseUnit='';						 						 
						 }
						 if(rows[i].useDays== null){
							 rows[i].useDays='';						 						 
						 }						 
						 if(rows[i].itemType== '1'){
							 if(rows[i].priceUnit==('B'+rows[i].drugpackagingUnit)&&rows[i].priceUnit!=''){
								 if(rows[i].doseOnce!=''&&rows[i].baseDose!=''&&rows[i].qtyTot!=''&&rows[i].packQty){
									 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)>rows[i].qtyTot*rows[i].packQty){
										 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');		
										 return;
									 }
								 }								 
							 }else{
								 if(rows[i].doseOnce!=''&&rows[i].baseDose!=''&&rows[i].qtyTot!=''&&rows[i].packQty){
									 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)>rows[i].qtyTot){
										 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');		
										 return;
									 }
								 }								 
							 }
							 if(rows[i].classCode=='16'||rows[i].classCode=='17'||rows[i].classCode=='18'){//非药品不校验频次 hedong  2016-09-09  页面都无此输入项
								  if(rows[i].frequencyCode== null||rows[i].frequencyCode==''){
									 $.messager.alert('提示',rows[i].itemName+'频次为空，不能开立医嘱!');		
									 return;						 						 
								   } 
							 }
							 
							 if(rows[i].frequencyName== null){
								 rows[i].frequencyName='';						 						 
							 }
							 if(rows[i].classCode=='16'||rows[i].classCode=='17'||rows[i].classCode=='18'){//非药品不校验用法  hedong  2016-09-09  页面都无此输入项
								 if(rows[i].usageCode== null||rows[i].usageCode==''){
									 $.messager.alert('提示',rows[i].itemName+'用法为空，不能开立医嘱!');		
									 return;
								 }
							 }
							 if(rows[i].useName== null){
								 rows[i].useName='';						 						 
							 }
							 if(rows[i].doseModelCode== null||rows[i].doseModelCode==''){
								 $.messager.alert('提示',rows[i].itemName+'剂型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].drugQuality== null||rows[i].drugQuality==''){
								 $.messager.alert('提示',rows[i].itemName+'性质为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].typeCode== null||rows[i].typeCode==''){
								 $.messager.alert('提示',rows[i].itemName+'医嘱类型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].classCode== null||rows[i].classCode==''){
								 $.messager.alert('提示',rows[i].itemName+'系统类型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].baseDose== null||rows[i].baseDose==''){
								 $.messager.alert('提示',rows[i].itemName+'基本剂量为空，不能开立医嘱!');
								 return;						 						 
							 }
						 }else{		
							 if(rows[i].frequencyCode== null){
								 rows[i].frequencyCode='';						 						 
							 }
							 if(rows[i].frequencyName== null){
								 rows[i].frequencyName='';						 						 
							 }
							 if(rows[i].usageCode== null){
								 rows[i].usageCode='';						 						 
							 }
							 if(rows[i].useName== null){
								 rows[i].useName='';						 						 
							 }
							 if(rows[i].baseDose== null){
								 rows[i].baseDose=='';						 						 
							 }
						 }
						 if(rows[i].execDpcd== null){
							 rows[i].execDpcd='';						 						 
						 }
						 if(rows[i].execDpnm== null){
							 rows[i].execDpnm='';						 						 
						 }
						 if(rows[i].emcFlag== null){
							 rows[i].emcFlag='';						 						 
						 }
						 if(rows[i].labCode== null){
							 rows[i].labCode='';						 						 
						 }
						 if(rows[i].itemNote== null){
							 rows[i].itemNote='';						 						 
						 }
						 if(rows[i].moNote2== null){
							 rows[i].moNote2='';						 						 
						 }
						 if(rows[i].hypotest== null){
							 rows[i].hypotest='';						 						 
						 }
						 if(rows[i].dateBgn== null){
							 rows[i].dateBgn='';						 						 
						 }
						 if(rows[i].dateEnd== null){
							 rows[i].dateEnd='';						 						 
						 }
						 if(rows[i].permission== null){
							 rows[i].permission='';						 						 
						 }
						 if(rows[i].execTimes== null){
							 rows[i].execTimes='';						 						 
						 }	
						 if(rows[i].execDose== null){
							 rows[i].execDose='';						 						 
						 }	
						 if(rows[i].docName== null){
							 rows[i].docName='';						 						 
						 } 
						 if(rows[i].combFlag== null){
							 rows[i].combFlag='';						 						 
						 } 			 
				 }
				 var inpatientNo = $("#inpatientNo").val();
				 var patientNo =$("#patientNo").val();
				 var deptCode = $("#deptCode").val();
				 var nurseCellCode = $("#nurseCellCode").val();
				 var babyFlag =$("#babyFlag").val();
				 var decmpsState = 0;
				 if(rows.length==0){
					 return;
				 }
				 if(rows[0].typeCode=='CZ'||rows[0].typeCode=='ZC'){
					 decmpsState = 1;
				 }
				 var str = encodeURI(JSON.stringify(rows));
				 if(name!=''){
					$.messager.confirm('确认信息',name+'剂量太小,确认继续开立吗?',function(e){ 
						if(e){ 	
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
							$.ajax({
								url: '<%=basePath%>inpatient/docAdvManage/saveInpatientOrder.action',
								data:'str='+str+'&inpatientNo='+inpatientNo+'&patientNo='+patientNo+'&deptCode='+deptCode+'&nurseCellCode='+nurseCellCode+'&babyFlag='+babyFlag+'&decmpsState='+decmpsState,
								type:'post',
								success: function(docadvdata) {	
									$.messager.progress('close');	// 如果提交成功则隐藏进度条
									var dataMap = eval("("+docadvdata+")");
									$.extend($.messager.defaults,{  
								        ok:"确定"
								    });  
						   			if(dataMap.resMsg=="error"){
						   				$.messager.alert('提示',dataMap.resCode);			   				
						        		
						        	}else if(dataMap.resMsg=="success"){
						        		$.messager.alert('提示',dataMap.resCode);	
						        		setTimeout(function(){
											$(".messager-body").window('close');
										 },3500);
						        		$('#infolistB').datagrid('reload');
						        	}else{
						        		$.messager.alert('提示','未知错误,请联系管理员!');
						        	
						        	}
								}
							});	
						}
					});
				 }else{		
					 $.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
					 $.ajax({
							url: '<%=basePath%>inpatient/docAdvManage/saveInpatientOrder.action',
							data:'str='+str+'&inpatientNo='+inpatientNo+'&patientNo='+patientNo+'&deptCode='+deptCode+'&nurseCellCode='+nurseCellCode+'&babyFlag='+babyFlag+'&decmpsState='+decmpsState,
							type:'post',
							success: function(docadvdata) {	
								$.messager.progress('close');	// 如果提交成功则隐藏进度条
								var dataMap = eval("("+docadvdata+")");
								$.extend($.messager.defaults,{  
							        ok:"确定"
							    });  
					   			if(dataMap.resMsg=="error"){
					   				$.messager.alert('提示',dataMap.resCode);			   				
					        		
					        	}else if(dataMap.resMsg=="success"){
					        		$.messager.alert('提示',dataMap.resCode);	
					        		setTimeout(function(){
										$(".messager-body").window('close');
									 },3500);
					        		$('#infolistB').datagrid('reload');
					        	}else{
					        		$.messager.alert('提示','未知错误,请联系管理员!');
					        	
					        	}
							}
						});	 
				 }				 
			 }
			 else{
				 $.messager.alert('提示',"无保存的记录！");
				 setTimeout(function(){
					$(".messager-body").window('close');
				 },3500);
			 }
		}
		/**
		 * 打开中草药备注界面弹框
		 * @author  zhuxiaolu
		 * @date 2015-5-25 10:53
		 * @version 1.0
		 */
		
		function popWinToChinMediRemTd(){
			 popWinCommCallBackFn = function(node){
					$('#adChinMediRemTdId').combobox('setValue',node.id);
					var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){	
						var index = getIndexForAdDgListA();
	    		    	var row = $('#infolistA').datagrid('getSelected');
						if(row!=null&&row.combNo!=null&&row.combNo!=''){
							var rows = $('#infolistA').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].combNo==row.combNo){
									var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
									$('#infolistA').datagrid('updateRow',{
										index: indexRow,
										row: {
											moNote2:node.name
										}
									});
								}
							}
						}else{
							if(index>=0){
								$('#infolistA').datagrid('updateRow',{
									index: index,
									row: {
										moNote2:node.name
									}
								});
							}
						}
					}else if(tab.title=='临时医嘱'){
						var index = getIndexForAdDgList();
	    		    	var row = $('#infolistB').datagrid('getSelected');
						if(row!=null&&row.combNo!=null&&row.combNo!=''){
							var rows = $('#infolistB').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].combNo==row.combNo){
									var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
									$('#infolistB').datagrid('updateRow',{
										index: indexRow,
										row: {
											moNote2:node.name
										}
									});
								}
							}
						}else{
							if(index>=0){ 
								$('#infolistB').datagrid('updateRow',{
									index: index,
									row: {
										moNote2:node.name
									}
								});
							}
						}
					}
			 };
			 
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?classNameTmp=CodeOpendocadvmark&textId=adChinMediRemTdId&type=opendocadvmark";
			var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
					
		}
		/**
		 * 打开西药中药备注界面弹框
		 * @author  zhuxiaolu
		 * @date 2015-5-25 10:53
		 * @version 1.0
		 */
		
		function popWinToCodeOpendocadvmark(){
			popWinCommCallBackFn = function(node){
   		    	$("#adWestMediRemTdId").textbox('setValue',node.name);
   		    	var index = getIndexForAdDgList();
   		    	var indexA=getIndexForAdDgListA();
   		    		if(indexA>=0){							
						$('#infolistA').datagrid('updateRow',{
						index: indexA,
							row: {								
								moNote2:node.name
							}
						});
					}
					if(index>=0){							
						$('#infolistB').datagrid('updateRow',{
						index: index,
							row: {									
								moNote2:node.name
							}
						});
					}
			};
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?classNameTmp=CodeOpendocadvmark&textId=adWestMediRemTdId&type=opendocadvmark";
			var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
		}
		/**
		 * 打开中非药品备注界面弹框
		 * @author  zhuxiaolu
		 * @date 2015-5-25 10:53
		 * @version 1.0
		 */
		
		function popWinToNotDrugRem(){
			popWinCommCallBackFn = function(node){
				$("#adNotDrugRemTdId").combobox('setValue',node.id);
				var index = getIndexForAdDgList();
   		    	var indexA=getIndexForAdDgListA();
	 		    if(indexA>=0){							
					$('#infolistA').datagrid('updateRow',{
					index: indexA,
						row: {								
							moNote2:node.name
						}
					});
				}
				if(index>=0){							
					$('#infolistB').datagrid('updateRow',{
					index: index,
						row: {									
							moNote2:node.name
						}
					});
				}
			};
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?classNameTmp=CodeOpendocadvmark&textId=adNotDrugRemTdId&type=opendocadvmark";
			var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')					
		}
		//加急连动
		   function onClickIsUrgent(id){
			   var qq = $('#ttta').tabs('getSelected');				
			   var tab = qq.panel('options');
			   if(tab.title=='长期医嘱'){	
				    var indexA = getIndexForAdDgListA();
					var rowA = $('#infolistA').datagrid('getSelected');						
					if(indexA>=0){
						if($('#'+id).is(':checked')){
							$('#infolistA').datagrid('updateRow',{
								index: indexA,
								row: {
									emcFlag:1,
									isUrgent:'加急',
									changeNo:1
								}
							});
						}else{
							$('#infolistA').datagrid('updateRow',{
								index: indexA,
								row: {
									emcFlag:0,
									isUrgent:'普通',
									changeNo:1
								}
							});
						}
					}
					else{
						$.messager.alert('提示',"请选中一条医嘱！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}	
			   }else if(tab.title=='临时医嘱'){
				   var index = getIndexForAdDgList();
					var row = $('#infolistB').datagrid('getSelected');						
					if(index>=0){
						if($('#'+id).is(':checked')){
							$('#infolistB').datagrid('updateRow',{
								index: index,
								row: {
									emcFlag:1,
									isUrgent:'加急',
									changeNo:1
								}
							});
						}else{
							$('#infolistB').datagrid('updateRow',{
								index: index,
								row: {
									emcFlag:0,
									isUrgent:'普通',
									changeNo:1
								}
							});
						}
					}
					else{
						$.messager.alert('提示',"请选中一条医嘱！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					} 
			   }				
			}
			//加急列表页 显示		
			function emcFamater(value,row,index){			
				if(row.emcFlag!=null&&row.emcFlag!=""){
					if(row.emcFlag==0){
						return '普通';
					}
					if(row.emcFlag==1){
						return '加急';
					}
				}			
			}
			 function skinFamater(value,row,index){			 	
					if(row.hypotest==1){
						return '不需要皮试';
					}
					if(row.hypotest==2){
						return '需要皮试，未做';
					}
					if(row.hypotest==3){
						return '皮试阳';
					}
					if(row.hypotest==4){
						return '皮试阴';
					}			
			 }
			 //控件数据加载
			 function controlFormat(){
				//长期医嘱类型下拉框
					$('#longDocAdvType').combobox({
						url:'<%=basePath%>inpatient/docAdvManage/queryDocAdvType.action', 
						queryParams:{
							'inpatientKind.fitExtent':2,
							'inpatientKind.decmpsState':1
						},
					    valueField:'typeCode',    
					    textField:'typeName',
					    multiple:false,
					    editable:false,
					    onSelect:function(data){
					    	$('#adProjectTdId').combobox('clear');
					    	$('#adProjectTdId').combobox('reload',"<%=basePath%>baseinfo/advice/querySystemTypesByTypeId.action?typeId="+data.typeCode);
					    	//名称下拉
							$('#adProjectNameTdId').combogrid('disable');
					    }
					});
					//临时医嘱类型下拉框
					$('#temDocAdvType').combobox({
						url:'<%=basePath%>inpatient/docAdvManage/queryDocAdvType.action', 
						queryParams:{
							'inpatientKind.fitExtent':2,
							'inpatientKind.decmpsState':0
						},
					    valueField:'typeCode',    
					    textField:'typeName',
					    multiple:false,
					    editable:false,
					    onSelect:function(data){
					    	$('#adProjectTdId').combobox('clear');
					    	$('#adProjectTdId').combobox('reload',"<%=basePath%>baseinfo/advice/querySystemTypesByTypeId.action?typeId="+data.typeCode);
					    	//名称下拉
							$('#adProjectNameTdId').combogrid('disable');
					    }
					});
					//系统类型下拉框
					$('#adProjectTdId').combobox({
						url:'<%=basePath%>inpatient/doctorAdvice/likeSystemtype.action', 
					    valueField:'code',    
					    textField:'name',
					    onSelect:function(data){
					    	var docAdvType = "";
					    	var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){	
								if($('#longDocAdvType').combobox('getValue')==''){
									$.messager.alert('提示',"请先选择医嘱类型！");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									$('#adProjectTdId') .combobox('setValue','');
									return;
								}
								docAdvType = $('#longDocAdvType').combobox('getValue');
							}else if(tab.title=='临时医嘱'){
								if($('#temDocAdvType').combobox('getValue')==''){
									$.messager.alert('提示',"请先选择医嘱类型！");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									$('#adProjectTdId') .combobox('setValue','');
									return;
								}
								docAdvType = $('#temDocAdvType').combobox('getValue');
							}			
							var record = $('#adProjectTdId').combobox('getValue');
							var recordName = $('#adProjectTdId').combobox('getText');
					    	if(record=='07'){//类型为检查			    		
						    	$('#bodyParts').show();	
						    	$('#bodyPartss').show();	
						    	$('#sampleType').hide();	
						    	$('#sampleTypes').hide();
						    }else{
						    	$('#bodyParts').hide();	
						    	$('#bodyPartss').hide();
						    	$('#sampleType').show();	
						    	$('#sampleTypes').show();
						    }			    	
					    	if(record=='12'){//类型为转科				    		
					    		$('#adNotDrugInsTdId').combobox('disable');
					    		$('#adNotDrugSamTdId').combobox('disable');
						    }		
					    	if(record!='12'){		    		
					    		$('#adNotDrugInsTdId').combobox('enable');
					    		$('#adNotDrugSamTdId').combobox('enable');
						    }
					    	if(record=='17'||record=='18'){//中药/西药
					    		$('#adNotDrugDivId').hide();
					    		$('#adChinMediDivId').hide();
					    		$('#adWestMediDivId').show();
					    	}else if(record=='16'){
					    		$('#adNotDrugDivId').hide();
					    		$('#adWestMediDivId').hide();
					    		$('#adChinMediDivId').show();
					    	}else{
					    		$('#adChinMediDivId').hide();
					    		$('#adWestMediDivId').hide();
					    		$('#adNotDrugDivId').show();
					    	}	
					    	if($('#adProjectTdId').combobox('getValue')==''){
								$('#adProjectNameTdId').combogrid('disable');
							}else{
								$('#adProjectNameTdId').combogrid('enable');
							}
							var adProjectTdId = record;
							var projectName = recordName;
							ProjectName(adProjectTdId,projectName,docAdvType);
					    	clearProjectTextbox();//清空项目类别区域的textbox
					    },onLoadSuccess:function(none){
					    	var code = "";
					    	for(var i=0;i<none.length;i++){
					    		if(code==""){
					    			code = none[i].code;
					    		}else{
					    			code += "," + none[i].code;
					    		}
					    	}
					    	if(code.indexOf("16")==-1){//判断是否有中草药
				    			$('#adHerbMedicineBtn').menubutton('disable');//草药查询
				    		}else{
				    			$('#adHerbMedicineBtn').menubutton('enable');//草药查询
				    		}
					    }	    
				    });
					//数量  数字输入框事件
					$('#adProjectNumTdId').numberspinner({
						onChange:function(newValue,oldValue){
							var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){	
								var indexA = getIndexForAdDgListA();
								if(indexA>=0){
									var retVal = newValue;
									if(retVal==null||retVal==''){
										return;
									}
									var row = $('#infolistA').datagrid('getSelected');
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistA').datagrid('getRows');				
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
												$('#infolistA').datagrid('updateRow',{
													index: indexRow,
													row: {
														qtyTot:parseInt(retVal),
														changeNo:1
													}
												});
											}
										}
									}else{
										$('#infolistA').datagrid('updateRow',{
											index: indexA,
											row: {
												qtyTot:parseInt(retVal),
												changeNo:1
											}
										});
									}
								}
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();
								if(index>=0){
									var retVal = newValue;
									if(retVal==null||retVal==''){
										return;
									}
									var row = $('#infolistB').datagrid('getSelected');
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistB').datagrid('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
												$('#infolistB').datagrid('updateRow',{
													index: indexRow,
													row: {
														qtyTot:parseInt(retVal),
														changeNo:1
													}
												});
												changeCostunitB();
											}
										}
									}else{							
										$('#infolistB').datagrid('updateRow',{
											index: index,
											row: {
												qtyTot:parseInt(retVal),
												changeNo:1
											}
										});
										changeCostunitB();
									}
								}
							}
						}
					});
					//频次下拉框adWestMediFreTdId
					$('#adWestMediFreTdId').combobox({    
					    url:'<%=basePath%>baseinfo/frequency/queryFrequencyGroup.action',
					    valueField:'code',    
					    textField:'name',
					    multiple:false,
					    editable:true,
					    onSelect:function(record){
					    	if(record!=null&&record!=''){
						    	var qq = $('#ttta').tabs('getSelected');				
								var tab = qq.panel('options');
								if(tab.title=='长期医嘱'){
									var index = getIndexForAdDgListA();
									if(index>=0){
										var row = $('#infolistA').datagrid('getSelected');
										if(row!=null&&row.combNo!=null&&row.combNo!=''){
											var rows = $('#infolistA').datagrid('getRows');
											for(var i=0;i<rows.length;i++){
												if(rows[i].combNo==row.combNo){
													var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
													$('#infolistA').datagrid('updateRow',{
														index: indexRow,
														row: {
															frequencyCode:record.code,
															frequencyName:record.name,
															changeNo:1
														}
													});
												}
											}
										}else{								
											$('#infolistA').datagrid('updateRow',{
												index: index,
												row: {
													frequencyCode:record.code,
													frequencyName:record.name,
													changeNo:1
												}
											});
										}
									}
								}else if(tab.title=='临时医嘱'){
									var index = getIndexForAdDgList();
									if(index>=0){
										var row = $('#infolistB').datagrid('getSelected');
										if(row!=null&&row.combNo!=null&&row.combNo!=''){
											var rows = $('#infolistB').datagrid('getRows');
											for(var i=0;i<rows.length;i++){
												if(rows[i].combNo==row.combNo){
													var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
													$('#infolistB').datagrid('updateRow',{
														index: indexRow,
														row: {
															frequencyCode:record.code,
															frequencyName:record.name,
															changeNo:1
														}
													});
												}
											}
										}else{								
											$('#infolistB').datagrid('updateRow',{
												index: index,
												row: {
													frequencyCode:record.code,
													frequencyName:record.name,
													changeNo:1
												}
											});
										}
									}
								}
								$('#shijiandianPc').text(record.period);
							}
						}
					});
					//用法下拉框
					$('#adWestMediUsaTdId') .combobox({    
					    url:'<%=basePath%>inpatient/doctorAdvice/likeUseage.action',    
					    valueField:'encode',    
					    textField:'name',
					    multiple:false,
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
					    },
					    onSelect:function(record){
							if(record!=null&&record!=''){
						    	var qq = $('#ttta').tabs('getSelected');				
								var tab = qq.panel('options');
								if(tab.title=='长期医嘱'){
									var index = getIndexForAdDgListA();
									if(index>=0){
										var row = $('#infolistA').datagrid('getSelected');
										if(row!=null&&row.combNo!=null&&row.combNo!=''){
											var rows = $('#infolistA').datagrid('getRows');
											for(var i=0;i<rows.length;i++){
												if(rows[i].combNo==row.combNo){
													var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
													$('#infolistA').datagrid('updateRow',{
														index: indexRow,
														row: {
															usageCode:record.encode,
															useName:record.name,
															changeNo:1
														}
													});
												}
											}
										}else{								
											$('#infolistA').datagrid('updateRow',{
												index: index,
												row: {
													usageCode:record.encode,
													useName:record.name,
													changeNo:1
												}
											});
										}
									}
								}else if(tab.title=='临时医嘱'){
									var index = getIndexForAdDgList();
									if(index>=0){
										var row = $('#infolistB').datagrid('getSelected');
										if(row!=null&&row.combNo!=null&&row.combNo!=''){
											var rows = $('#infolistB').datagrid('getRows');
											for(var i=0;i<rows.length;i++){
												if(rows[i].combNo==row.combNo){
													var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
													$('#infolistB').datagrid('updateRow',{
														index: indexRow,
														row: {
															usageCode:record.encode,
															useName:record.name,
															changeNo:1
														}
													});
												}
											}
										}else{								
											$('#infolistB').datagrid('updateRow',{
												index: index,
												row: {
													usageCode:record.encode,
													useName:record.name,
													changeNo:1
												}
											});
										}
									}
								}
					    	}
			   		    }
				    });
					//每次用量
					$('#adWestMediDosMaxTdId').numberbox('textbox').bind('keyup',function(event){//西药,中成药-每次用量-单位
						var indexA = getIndexForAdDgListA();
						var index = getIndexForAdDgList();
						if(indexA>=0){
							var curVal = $('#adWestMediDosMaxTdId').numberbox('getText');
			    			if(curVal>999){
			    				curVal=999;
			    			}
			    			if(curVal==null||curVal==''){
			    				curVal=0;
			    			}
			    			var row = $('#infolistA').datagrid('getSelected');
			    			var temp;
			    			if(row.baseDose=="0"){
			    				temp=1;
			    			}else{
			    				temp=row.baseDose;
			    			}
			    			var dosage = (curVal/1).toFixed(2);;
			    			var retVal = curVal+row.minUnit+"="+(dosage*temp)+row.doseUnit;
			    			$('#adWestMediDosMinTdId').numberbox('setText',dosage*temp);
			    			$('#infolistA').datagrid('updateRow',{
								index: indexA,
								row: {
									doseOnce:dosage,
									doseOnces:retVal,
									changeNo:1
								}
							});
						}
						if(index>=0){
							var curVal = $('#adWestMediDosMaxTdId').numberbox('getText');
			    			if(curVal>999){
			    				curVal=999;
			    			}
			    			if(curVal==null||curVal==''){
			    				curVal=0;
			    			}
			    			var row = $('#infolistB').datagrid('getSelected');
			    			var temp;
			    			if(row.baseDose=="0"){
			    				temp=1;
			    			}else{
			    				temp=row.baseDose;
			    			}
			    			var dosage = (curVal/1).toFixed(2);;
			    			var retVal = curVal+row.minUnit+"="+(dosage*temp)+row.doseUnit;
			    			$('#adWestMediDosMinTdId').numberbox('setText',dosage*temp);
			    			$('#infolistB').datagrid('updateRow',{
								index: index,
								row: {
									doseOnce:dosage,
									doseOnces:retVal,
									changeNo:1
								}
							});
						}
					}); 
					//西药中药备注可编辑
			    	$('#adWestMediRemTdId').combobox('textbox').bind('keyup',function(event){
			    		var retval = $('#adWestMediRemTdId').combobox('getText');
			    		var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var indexA = getIndexForAdDgListA();
							if(indexA>=0){
								$('#infolistA').datagrid('updateRow',{
									index: indexA,
									row: {
										moNote2:retval,
										changeNo:1
									}
								});
							}
						}else if(tab.title=='临时医嘱'){
							var index = getIndexForAdDgList();	    		
				    		if(index>=0){
								$('#infolistB').datagrid('updateRow',{
									index: index,
									row: {
										moNote2:retval,
										changeNo:1
									}
								});
							}
						}	    			    		
					});
			    	//西药中药备注下拉框
					$('#adWestMediRemTdId') .combobox({    
						url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=opendocadvmark'/>",
					    valueField:'encode',    
					    textField:'name',
					    multiple:false,
					    editable:true,
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
					    },
					    onSelect:function(record){		    	
					    	if(record!=null&&record!=''){
						    	var qq = $('#ttta').tabs('getSelected');				
								var tab = qq.panel('options');
								if(tab.title=='长期医嘱'){
									var index=getIndexForAdDgListA();
				   		    		if(index>=0){	
										$('#infolistA').datagrid('updateRow',{
										index: index,
											row: {								
												moNote2:record.name,
												changeNo:1
											}
										});
									}
								}else if(tab.title=='临时医嘱'){
									var index = getIndexForAdDgList();			   		    	
									if(index>=0){							
										$('#infolistB').datagrid('updateRow',{
										index: index,
											row: {									
												moNote2:record.name,
												changeNo:1
											}
										});
									}
								}
					    	}	   		    	
			   		    }
				    });	
			    	//是否皮试
					 $('#adWestMediSkiTdId') .combobox({  			 
						 onSelect:function(record){
						 	var indexA = getIndexForAdDgListA();
				    		var index = getIndexForAdDgList();
				    		if(indexA>=0){
								$('#infolistA').datagrid('updateRow',{
									index: indexA,
									row: {
										hypotest:record.id,
										hypotestName:record.text,
										changeNo:1
									}
								});
							}
				    		if(index>=0){
								$('#infolistB').datagrid('updateRow',{
									index: index,
									row: {
										hypotest:record.id,
										hypotestName:record.text,
										changeNo:1
									}
								});
							}
						 }
					 });
					//频次下拉框
						$('#adChinMediFreTdId') .combobox({    
							url:'<%=basePath%>baseinfo/frequency/queryFrequencyGroup.action',    
						    valueField:'code',    
						    textField:'name',
						    multiple:false,
						    onHidePanel:function(none){
						        var data = $(this).combobox('getData');
						        var val = $(this).combobox('getValue');
						        var result = true;
						        for (var i = 0; i < data.length; i++) {
						            if (val == data[i].code) {
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
						        keys[keys.length] = 'code';
						        keys[keys.length] = 'name';
						        keys[keys.length] = 'pinyin';
						        keys[keys.length] = 'wb';
						        keys[keys.length] = 'inputCode';
						        return filterLocalCombobox(q, row, keys);
						    },
						    onSelect:function(record){
						    	if(record!=null&&record!=''){
							    	var qq = $('#ttta').tabs('getSelected');				
									var tab = qq.panel('options');
									if(tab.title=='长期医嘱'){
										var index = getIndexForAdDgListA();
										if(index>=0){
											var row = $('#infolistA').datagrid('getSelected');
											if(row!=null&&row.combNo!=null&&row.combNo!=''){
												var rows = $('#infolistA').datagrid('getRows');
												for(var i=0;i<rows.length;i++){
													if(rows[i].combNo==row.combNo){
														var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
														$('#infolistA').datagrid('updateRow',{
															index: indexRow,
															row: {
																frequencyCode:record.code,
																frequencyName:record.name,
																changeNo:1
															}
														});
													}
												}
											}else{								
												$('#infolistA').datagrid('updateRow',{
													index: index,
													row: {
														frequencyCode:record.code,
														frequencyName:record.name,
														changeNo:1
													}
												});
											}
										}
									}else if(tab.title=='临时医嘱'){
										var index = getIndexForAdDgList();
										if(index>=0){
											var row = $('#infolistB').datagrid('getSelected');
											if(row!=null&&row.combNo!=null&&row.combNo!=''){
												var rows = $('#infolistB').datagrid('getRows');
												for(var i=0;i<rows.length;i++){
													if(rows[i].combNo==row.combNo){
														var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
														$('#infolistB').datagrid('updateRow',{
															index: indexRow,
															row: {
																frequencyCode:record.code,
																frequencyName:record.name,
																changeNo:1
															}
														});
													}
												}
											}else{								
												$('#infolistB').datagrid('updateRow',{
													index: index,
													row: {
														frequencyCode:record.code,
														frequencyName:record.name,
														changeNo:1
													}
												});
											}
										}
									}
						    	}
				   		    }
					    });
						$('#adChinMediUsaTdId').combobox({//中草药-用法
				    		url:'<%=basePath%>inpatient/doctorAdvice/likeUseage.action',  
				    		disabled:false,
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
						    },
							onSelect:function(record){
								if(record!=null&&record!=''){
									var qq = $('#ttta').tabs('getSelected');				
									var tab = qq.panel('options');
									if(tab.title=='长期医嘱'){	
										var index = getIndexForAdDgListA();
										if(index>=0){
						    		    	var row = $('#infolistA').datagrid('getSelected');
											if(row!=null&&row.combNo!=null&&row.combNo!=''){
												var rows = $('#infolistA').datagrid('getRows');
												for(var i=0;i<rows.length;i++){
													if(rows[i].combNo==row.combNo){
														var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
														$('#infolistA').datagrid('updateRow',{
															index: indexRow,
															row: {
																usageCode:record.encode,
																useName:record.name,
																changeNo:1
															}
														});
													}
												}
											}else{									
												$('#infolistA').datagrid('updateRow',{
													index: index,
													row: {
														usageCode:record.encode,
														useName:record.name,
														changeNo:1
													}
												});
											}
										}
									}else if(tab.title=='临时医嘱'){
										var index = getIndexForAdDgList();
										if(index>=0){
						    		    	var row = $('#infolistB').datagrid('getSelected');
											if(row!=null&&row.combNo!=null&&row.combNo!=''){
												var rows = $('#infolistB').datagrid('getRows');
												for(var i=0;i<rows.length;i++){
													if(rows[i].combNo==row.combNo){
														var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
														$('#infolistB').datagrid('updateRow',{
															index: indexRow,
															row: {
																usageCode:record.encode,
																useName:record.name,
																changeNo:1
															}
														});
													}
												}
											}else{									
												$('#infolistB').datagrid('updateRow',{
													index: index,
													row: {
														usageCode:record.encode,
														useName:record.name,
														changeNo:1
													}
												});
											}
										}
									}							
								}
			    		    }
				    	});
						$('#adChinMediNumTdId').numberbox('textbox').bind('keyup',function(event){//草药-付数
				    		var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){	
								var index = getIndexForAdDgListA();
				    			if(index>=0){
				    				var retVal = $('#adChinMediNumTdId').numberbox('getText');
					    			if(retVal>999){
					    				retVal=999;
					    			}
					    			if(retVal==null||retVal==''){
					    				retVal=0;
					    			}
					    			var row = $('#infolistA').datagrid('getSelected');
					    			var doseOnce = row.doseOnce;			    			
					    			if(row.doseOnce==''||row.doseOnce==null||row.doseOnce=='undefined'){			    				
					    				doseOnce = 1;
					    			}
					    			var tot = parseInt(retVal)*parseInt(doseOnce);
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistA').datagrid('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
												$('#infolistA').datagrid('updateRow',{
													index: indexRow,
													row: {
														useDays:parseInt(retVal),
														qtyTot:parseInt(tot),
														changeNo:1
													}
												});
											}
										}
									}else{								
											$('#infolistA').datagrid('updateRow',{
												index: index,
												row: {
													useDays:parseInt(retVal),
													qtyTot:parseInt(tot),
													changeNo:1
												}
											});								
									}
									$('#adProjectNumTdId').numberbox('setValue',tot);
					    			$('#adChinMediNumTdId').numberbox('setText',parseInt(retVal));
				    			}
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();
				    			if(index>=0){
				    				var retVal = $('#adChinMediNumTdId').numberbox('getText');
					    			if(retVal>999){
					    				retVal=999;
					    			}
					    			if(retVal==null||retVal==''){
					    				retVal=0;
					    			}
					    			var row = $('#infolistB').datagrid('getSelected');
					    			var doseOnce = row.doseOnce;			    			
					    			if(row.doseOnce==''||row.doseOnce==null||row.doseOnce=='undefined'){			    				
					    				doseOnce = 1;
					    			}
					    			var tot = parseInt(retVal)*parseInt(doseOnce);
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistB').datagrid('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
												$('#infolistB').datagrid('updateRow',{
													index: indexRow,
													row: {
														useDays:parseInt(retVal),
														qtyTot:parseInt(tot),
														changeNo:1
													}
												});
											}
										}
									}else{
										
											$('#infolistB').datagrid('updateRow',{
												index: index,
												row: {
													useDays:parseInt(retVal),
													qtyTot:parseInt(tot),
													changeNo:1
												}
											});
										
									}
									$('#adProjectNumTdId').numberbox('setValue',tot);
					    			$('#adChinMediNumTdId').numberbox('setText',parseInt(retVal));
				    			}
							}	    			
			    		});
						$('#adChinMediRemTdId').combobox({//中草药-备注
				    		url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=opendocadvmark'/>",  
				    		editable:true,
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
						    },
							onSelect:function(record){
								if(record!=null&&record!=''){
									var qq = $('#ttta').tabs('getSelected');				
									var tab = qq.panel('options');
									if(tab.title=='长期医嘱'){	
										var index = getIndexForAdDgListA();			    		    
										if(index>=0){
											$('#infolistA').datagrid('updateRow',{
												index: index,
												row: {
													moNote2:record.name,
													changeNo:1
												}
											});
										}							
									}else if(tab.title=='临时医嘱'){
										var index = getIndexForAdDgList();			    		    	
										if(index>=0){
											$('#infolistB').datagrid('updateRow',{
												index: index,
												row: {
													moNote2:record.name,
													changeNo:1
												}
											});
										}								
									}							
								}
			    		    }
				    	});
						$('#adChinMediRemTdId').combobox('textbox').bind('keyup',function(event){//中草药-备注
				    		var retval = $('#adChinMediRemTdId').combobox('getText');
				    		var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){	
								var index = getIndexForAdDgListA();			    		
								if(index>=0){
									$('#infolistA').datagrid('updateRow',{
										index: index,
										row: {
											moNote2:retval,
											changeNo:1
										}
									});
								}						
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();
								if(index>=0){
									$('#infolistB').datagrid('updateRow',{
										index: index,
										row: {
											moNote2:retval,
											changeNo:1
										}
									});
								}						
							}		    		
						});	
				bindEnterEvent('adExeDeptTdId',searchDeptInfo,'easyui');//为执行科室添加事件
				
				//非药品-部位检体
				$('#adNotDrugInsTdId').combobox({
					url: "<%=basePath%>inpatient/docAdvManage/queryCheckpoint.action",	
		    		disabled:false,
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
				    },
					onSelect:function(record){
						if(record!=null&&record!=''){
					    	var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){
								var indexA = getIndexForAdDgListA();
								if(indexA>=0){
									$('#infolistA').datagrid('updateRow',{
										index: indexA,
										row: {
											itemNote:record.encode,
											changeNo:1
										}
									});
								}  
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();		  		    	
								if(index>=0){
									$('#infolistB').datagrid('updateRow',{
										index: index,
										row: {
											itemNote:record.encode,
											changeNo:1
										}
									});
								}
							}
						}					
	    		    }
		    	});
				
				//检验-样本类型 
				   $('#adNotDrugSamTdId').combobox({
					    url: "<%=basePath%>inpatient/docAdvManage/querySampleTept.action", 
			    		disabled:false,
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
					    },
						onSelect:function(record){
							if(record!=null&&record!=''){
						    	var qq = $('#ttta').tabs('getSelected');				
								var tab = qq.panel('options');
								if(tab.title=='长期医嘱'){
									var index = getIndexForAdDgListA();
									if(index>=0){
										var row = $('#infolistA').datagrid('getSelected');
										if(row!=null&&row.combNo!=null&&row.combNo!=''){
											var rows = $('#infolistA').datagrid('getRows');
											for(var i=0;i<rows.length;i++){
												if(rows[i].combNo==row.combNo){
													var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
													$('#infolistA').datagrid('updateRow',{
														index: indexRow,
														row: {
															labCode:record.encode,
															changeNo:1
														}
													});
												}
											}
										}else{									
											$('#infolistA').datagrid('updateRow',{
												index: index,
												row: {
													labCode:record.encode,
													changeNo:1
												}
											});
										}
									}
								}else if(tab.title=='临时医嘱'){
									var index = getIndexForAdDgList();
									if(index>=0){
										var row = $('#infolistB').datagrid('getSelected');
										if(row!=null&&row.combNo!=null&&row.combNo!=''){
											var rows = $('#infolistB').datagrid('getRows');
											for(var i=0;i<rows.length;i++){
												if(rows[i].combNo==row.combNo){
													var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
													$('#infolistB').datagrid('updateRow',{
														index: indexRow,
														row: {
															labCode:record.encode,
															changeNo:1
														}
													});
												}
											}
										}else{									
											$('#infolistB').datagrid('updateRow',{
												index: index,
												row: {
													labCode:record.encode,
													changeNo:1
												}
											});
										}
									}
								}
					    	}
			   		    }
			    	});
				 //非药品备注可编辑
			    	$('#adNotDrugRemTdId').combobox('textbox').bind('keyup',function(event){
			    		var retval = $('#adNotDrugRemTdId').combobox('getText');
			    		var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var indexA = getIndexForAdDgListA();
							if(indexA>=0){
								$('#infolistA').datagrid('updateRow',{
									index: indexA,
									row: {
										moNote2:retval,
										changeNo:1
									}
								});
							}
						}else if(tab.title=='临时医嘱'){
							var index = getIndexForAdDgList();			    		
				    		if(index>=0){
								$('#infolistB').datagrid('updateRow',{
									index: index,
									row: {
										moNote2:retval,
										changeNo:1
									}
								});
							}
						}		    				    		
					});
				 //非药品备注下拉框
					$('#adNotDrugRemTdId') .combobox({    
						url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=opendocadvmark'/>",
					    valueField:'encode',    
					    textField:'name',
					    multiple:false,
					    editable:true,
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
					    },
					    onSelect:function(record){
					    	if(record!=null&&record!=''){
						    	var qq = $('#ttta').tabs('getSelected');				
								var tab = qq.panel('options');
								if(tab.title=='长期医嘱'){
									var indexA=getIndexForAdDgListA();
				   		    		if(indexA>=0){							
										$('#infolistA').datagrid('updateRow',{
										index: indexA,
											row: {								
												moNote2:record.name,
												changeNo:1
											}
										});
									}
								}else if(tab.title=='临时医嘱'){
									var index = getIndexForAdDgList();				   		    	
									if(index>=0){							
										$('#infolistB').datagrid('updateRow',{
										index: index,
											row: {									
												moNote2:record.name,
												changeNo:1
											}
										});
									}
								}
					    	}			   		    	
			   		    }
				    });	
			}
			 function changeCost(retVal,oldValue,row){
				 if(row.moStat=='0'){
						if(row.classCode=="09"){//非药品
							$("#fypCost").show();
							if($('#fypCost1').text()==null||$('#fypCost1').text()==""){
								$("#fypCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var fypCost1 = parseFloat($('#fypCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#fypCost1").text(fypCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="13"){//膳食
							$("#ssCost").show();
							if($('#ssCost1').text()==null||$('#ssCost1').text()==""){
								$("#ssCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var ssCost1 = parseFloat($('#ssCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#ssCost1").text(ssCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="14"){//护理级别
							$("#hljbCost").show();
							if($('#hljbCost1').text()==null||$('#hljbCost1').text()==""){
								$("#hljbCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var hljbCost1 = parseFloat($('#hljbCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#hljbCost1").text(hljbCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="15"){//描述医嘱
							$("#msyzCost").show();
							if($('#msyzCost1').text()==null||$('#msyzCost1').text()==""){
								$("#msyzCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var msyzCost1 = parseFloat($('#msyzCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#msyzCost1").text(msyzCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="17"){//中成药
							$("#zcyCost").show();
							if($('#zcyCost1').text()==null||$('#zcyCost1').text()==""){
								$("#zcyCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var zcyCost1 = parseFloat($('#zcyCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#zcyCost1").text(zcyCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="18"){//西药
							$("#xyCost").show();
							if($('#xyCost1').text()==null||$('#xyCost1').text()==""){
								$("#xyCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var xyCost1 = parseFloat($('#xyCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#xyCost1").text(xyCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="06"){//手术
							$("#shoushuCost").show();
							if($('#shoushuCost1').text()==null||$('#shoushuCost1').text()==""){
								$("#shoushuCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var shoushuCost1 = parseFloat($('#shoushuCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#shoushuCost1").text(shoushuCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="07"){//检查
							$("#jcCost").show();
							if($('#jcCost1').text()==null||$('#jcCost1').text()==""){
								$("#jcCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var jcCost1 = parseFloat($('#jcCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#jcCost1").text(jcCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="08"){//检验
							$("#jyCost").show();
							if($('#jyCost1').text()==null||$('#jyCost1').text()==""){
								$("#jyCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var jyCost1 = parseFloat($('#jyCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#jyCost1").text(jyCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="10"){//预约出院
							$("#yycyCost").show();
							if($('#yycyCost1').text()==null||$('#yycyCost1').text()==""){
								$("#yycyCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var yycyCost1 = parseFloat($('#yycyCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#yycyCost1").text(yycyCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="11"){//转床
							$("#zcCost").show();
							if($('#zcCost1').text()==null||$('#zcCost1').text()==""){
								$("#zcCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var zcCost1 = parseFloat($('#zcCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#zcCost1").text(zcCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="12"){//转科
							$("#zkCost").show();
							if($('#zkCost1').text()==null||$('#zkCost1').text()==""){
								$("#zkCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var zkCost1 = parseFloat($('#zkCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#zkCost1").text(zkCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="16"){//中草药
							$("#zcaoyCost").show();
							if($('#zcaoyCost1').text()==null||$('#zcaoyCost1').text()==""){
								$("#zcaoyCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var zcyCost1 = parseFloat($('#zcaoyCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#zcaoyCost1").text(zcyCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="04"){//会诊
							$("#hzCost").show();
							if($('#hzCost1').text()==null||$('#hzCost1').text()==""){
								$("#hzCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var hzCost1 = parseFloat($('#hzCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#hzCost1").text(hzCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="01"){//病情
							$("#bqCost").show();
							if($('#bqCost1').text()==null||$('#bqCost1').text()==""){
								$("#bqCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var bqCost1 = parseFloat($('#bqCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#bqCost1").text(bqCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="02"){//其他
							$("#qtCost").show();
							if($('#qtCost1').text()==null||$('#qtCost1').text()==""){
								$("#qtCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var qtCost1 = parseFloat($('#qtCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#qtCost1").text(qtCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="03"){//计量
							$("#jlCost").show();
							if($('#jlCost1').text()==null||$('#jlCost1').text()==""){
								$("#jlCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var jlCost1 = parseFloat($('#jlCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#jlCost1").text(jlCost1.toFixed(2));
								}
							}
						}else if(row.classCode=="05"){//治疗
							$("#zlCost").show();
							if($('#zlCost1').text()==null||$('#zlCost1').text()==""){
								$("#zlCost1").text(row.itemPrice*row.qtyTot);
							}else{
								if(oldValue==null||oldValue==""){
								}else{
									var zlCost1 = parseFloat($('#zlCost1').text())-row.itemPrice*parseInt(oldValue)+row.itemPrice*parseInt(retVal);
									$("#zlCost1").text(zlCost1.toFixed(2));
								}
							}
						}
					}
			 }
			//打印医嘱
			function adPrintAdvice(){
				var qq = $('#tt1').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='医嘱'){
					var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){
						var timerStr = Math.random();
						var inpatientNo = $("#inpatientNo").val(); 
						window.open ("<c:url value='/inpatient/docAdvManage/iReportToChangqiOrder.action?randomId='/>"+timerStr+"&inpatientNo="+inpatientNo+"&flag=1&fileName=changqiyizhudan",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}else if(tab.title=='临时医嘱'){
						var timerStr = Math.random();
						var inpatientNo = $("#inpatientNo").val();
						window.open ("<c:url value='/inpatient/docAdvManage/iReportToChangqiOrder.action?randomId='/>"+timerStr+"&inpatientNo="+inpatientNo+"&flag=0&fileName=changqiyizhudan",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}
				}else{
					var qq = $('#tttb').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){
						var timerStr = Math.random();
						var inpatientNo = $("#inpatientNo").val(); 
						window.open ("<c:url value='/inpatient/docAdvManage/iReportToChangqiOrderHis.action?randomId='/>"+timerStr+"&inpatientNo="+inpatientNo+"&flag=1&fileName=historychangqiyizhudan",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}else if(tab.title=='临时医嘱'){
						var timerStr = Math.random();
						var inpatientNo = $("#inpatientNo").val();
						window.open ("<c:url value='/inpatient/docAdvManage/iReportToChangqiOrderHis.action?randomId='/>"+timerStr+"&inpatientNo="+inpatientNo+"&flag=0&fileName=historylinshiyizhudan",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
					}
				}
				
				
			}  
			//组套查询
			function searchStackTreeNodes(){
				var searchParam = $('#adStackTreeSearch').textbox('getValue');
				$("#adStackTree").tree("collapseAll");
				$.post("<%=basePath%>nursestation/nurseCharge/getstackNameParam.action",{infoId:searchParam,drugType:'2',type:'1'},function(data){
		    		if(data!=null&&data.length>0){
		    			for(var i=0;i<data.length;i++){
		    				//展开一级节点
		    				var source = data[i].source;
		    				if(source=="1"){
		    					source="3";
		    				}else if(source=="3"){
		    					source="1";
		    				}
		    				var node1 = $('#adStackTree').tree('find',source);
		    				$('#adStackTree').tree('expand',node1.target);
		    			} 
		    			setTimeout(function(){
		    				if(data.length=="1"){
		    					var node = $('#adStackTree').tree('find', data[0].id);
		    					$('#adStackTree').tree('select', node.target);
		    				}else if(data.length>"1"){
		    					for(var i=0;i<data.length;i++){
				    				var id = data[i].id;
			    					var node2 = $('#adStackTree').tree('find',id);
				    				$('#adStackTree').tree('expand',node2.target);
				    			}
		    				}
		    			},500);
		    		}
		    	});
			}
	/**  
	 *  
	 * 消息窗口
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-7 下午06:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-7 下午06:56:59  
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	function msgShow(title,msg,timeout){
		$.messager.show({
			title:title,
			msg:msg,
			timeout:timeout,
			showType:'slide'
		});
	}
	//重整医嘱方法
	function reformAdvice(){
		var qqs = $('#tt1').tabs('getSelected');				
		var tabs = qqs.panel('options');
		if(tabs.title=='医嘱'){
			var qq = $('#ttta').tabs('getSelected');				
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){
				var rows = $('#infolistA').datagrid('getRows');
				if(rows==null||rows==""){
					$.messager.alert('操作提示', '该患者没有医嘱信息！');	
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					var infolistA = JSON.stringify($('#infolistA').datagrid('getRows'))
					$.ajax({
						url: "<%=basePath%>inpatient/docAdvManage/reformAdvice.action",	
						data:{adviceJson:infolistA},
						type:'post',
						success: function(data) {
							if(data="success"){
								$.messager.alert('操作提示', '重整医嘱信息完成！');	
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								recordId = '02';
								loadTabs(inpatientNo,recordId);
							}
						}
					});
				}
			}else if(tab.title=='临时医嘱'){
				var rows = $('#infolistB').datagrid('getRows');
				if(rows==null||rows==""){
					$.messager.alert('操作提示', '该患者没有医嘱信息！');	
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					var infolistB = JSON.stringify($('#infolistB').datagrid('getRows'))
					$.ajax({
						url: "<%=basePath%>inpatient/docAdvManage/reformAdvice.action",	
						data:{adviceJson:infolistB},
						type:'post',
						success: function(data) {
							if(data="success"){
								$.messager.alert('操作提示', '重整医嘱信息完成！');	
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								recordId = '02';
								loadTabs(inpatientNo,recordId);
							}
						}
					});
				}
			}
		}	
	}
	//审核医嘱
 	function adAuditAdvice(){
 		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='长期医嘱'){
			var rowA = $('#infolistA').datagrid('getSelected');
	 		if(rowA!=null){
	 			if(rowA.moStat==-1 && rowA.id!=''){
		 			AdddilogModel("adAuditData-window","审核","<%=basePath%>inpatient/docAdvManage/auditAdviceInfo.action",'21%','32%');
		 		}else{
		 			$.messager.alert('提示',"该条医嘱无需审核！");
		 			setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		 		}
	 		}else{
	 			$.messager.alert('提示',"请选择一条医嘱!");
	 			setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	 		}
		}else if(tab.title=='临时医嘱'){
			var row = $('#infolistB').datagrid('getSelected');
	 		if(row!=null){
	 			if(row.moStat==-1 && row.id!=''){
		 			AdddilogModel("adAuditData-window","审核","<%=basePath%>inpatient/docAdvManage/auditAdviceInfo.action",'21%','32%');
		 		}else{
		 			$.messager.alert('提示',"该条医嘱无需审核！");
		 			setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		 		}
	 		}else{
	 			$.messager.alert('提示',"请选择一条医嘱!");
	 			setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	 		}	
		}		 				 			 				 		
 	}
 	//关闭模式窗口
	function closeAuditLayout(){
		$('#adAuditData-window').window('close');
	}
	function closeStopLayout(){
		$('#adStopData-window').window('close');
	}
	//获得infolistA索引
	function getIndexForAdDgListA(){
		var row = $('#infolistA').datagrid('getSelected');
		if(row!=null && row.moStat<=0){
			return $('#infolistA').datagrid('getRowIndex',row);
		}else{
			return -1;
		}
	}
	//获得infolistB索引
	function getIndexForAdDgList(){
		var row = $('#infolistB').datagrid('getSelected');
		if(row!=null && row.moStat<=0){
			return $('#infolistB').datagrid('getRowIndex',row);
		}else{
			return -1;
		}
	}
	
	
	
	$(function(){
		/**  
		 *  
		 * 键盘监听事件
		 *
		 */
		$(document).keydown(function (event) {
			if(event.ctrlKey){
				var tab = $('#tt1').tabs('getSelected');
				var index = $('#tt1').tabs('getTabIndex',tab);
				if(index==0){//医嘱
					tab = $('#ttta').tabs('getSelected');
					index = $('#ttta').tabs('getTabIndex',tab);
					if(index==0){
						flag='infolistA';//长期
					}else{
						flag='infolistB';//临时
					}
					if(event.ctrlKey && event.keyCode === 86){//粘贴
								event.preventDefault();
								if(copyAdviceArr.length<=0){
									msgShow('提示','没有复制的医嘱信息！',3000);
								}else{
									if(sign==flag){
										var copyAdviceArrCopy = jQuery.extend(true, [], copyAdviceArr);
										for(var i=0;i<copyAdviceArrCopy.length;i++){
											$('#'+flag).datagrid('appendRow',
												copyAdviceArrCopy[i]
											);
										}
										//重新计算
										if(sign=='infolistB'){
											changeCostunitB();
										}
										reloadRow();
									}else{
										if(sign=='infolistA'){
											$.messager.alert('提示','长期医嘱不能复制到临时医嘱');
										}else{
											$.messager.alert('提示','临时医嘱不能复制到长期医嘱');
										}
									}
								}
					}
					if(event.ctrlKey && event.keyCode === 65){//全选
								event.preventDefault();
								$('#'+flag).datagrid('checkAll');
						
					}
					if(event.ctrlKey && event.keyCode === 67){//复制
								event.preventDefault();
								copyHisOnAdv(flag);
					}
				}else if(index==1){//历史医嘱
					tab = $('#tttb').tabs('getSelected');
					index = $('#tttb').tabs('getTabIndex',tab);
					if(index==0){
						flag='infolistC';
					}else{
						flag='infolistD';
					}
					if(event.ctrlKey && event.keyCode === 65){
						event.preventDefault();
	 					$('#'+flag).datagrid('checkAll');
	 				}
					if(event.ctrlKey && event.keyCode === 67){
						copyHisOnAdv(flag);
					}
				}
			}
		});
	})
/**  
 *  
 * 医嘱复制
 *
 */
function copyHisOnAdv(id){
	var rows= $('#'+id).datagrid('getChecked');
	if(rows!=null&&rows.length>0){
		if(copyAdviceArr.length>0){
			$.messager.defaults={
    				ok:'确定',
    				cancel:'取消',
    				width:350,
    				collapsible:false,
    				minimizable:false,
    				maximizable:false,
    				closable:false
    		};
			$.messager.confirm('提示信息','已存在复制的医嘱信息，是否覆盖？', function(res){//提示是否删除
				if (res){
					copyAdviceArr = [];
					for(var i=0;i<rows.length;i++){
						var copyRow = jQuery.extend(true, {},rows[i]);
						copyRow.id = null;
						if(id=='infolistC'||id=='infolistD'){
							copyRow.itemNameView=copyRow.itemName;
						}
						copyAdviceArr[i] = copyRow;
					}
					if(id=='infolistC'||id=='infolistA'){
						sign='infolistA'
					}else{
						sign='infolistB'
					}
				}
		    });
		}else{
			for(var i=0;i<rows.length;i++){
				var copyRow = jQuery.extend(true, {},rows[i]);
				copyRow.id=null;
				if(id=='infolistC'||id=='infolistD'){
					copyRow.itemNameView=copyRow.itemName;
				}
				copyAdviceArr[i] = copyRow;
			}
			if(id=='infolistC'||id=='infolistA'){
				sign='infolistA'
			}else{
				sign='infolistB'
			}
		}
	}else{
		msgShow('提示','请选择需要复制的医嘱！',3000);
	}
}
</script>
<style>
	.lis .datagrid-wrap,.pacs .datagrid-wrap,.unDrug,.drug{
		border-left:0;
		border-right:0;
	}
	.lisTable .datagrid-wrap,.pacsTable .datagrid-wrap{
		border-top:0;
	}
	 
</style>
</head>
<body style="width: 100%;height: 100%;">  
 <div id="ccww" class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north'" style="height:38px;width: 100%;border-top:0">
    	<input type="hidden" id="pharmacyInputId" value="${pharmacyInputId }" name="pharmacyInputId">
		<input type="hidden" id="pharmacyInputName" value="${pharmacyInputName }" name="pharmacyInputName">
		<div style="padding:5px;float:left;width:140px;height:25px;margin-left:10px;">
			<input id="pharmacyCombobox" class="easyui-combobox" name="dept" style="width:130px;height:25px;">					
		</div>
		<shiro:hasPermission name="${menuAlias}:function:open">
			&nbsp;<a id="adStartAdviceBtn" href="javascript:adStartAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_edit',disabled:false" style="width: 103px;margin-top:5px">开立医嘱</a>
		</shiro:hasPermission>
		<a id="adEndAdviceBtn" href="javascript:adEndAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_edit',disabled:true" style="width: 103px;margin-top:5px">退出开立</a>
		<shiro:hasPermission name="${menuAlias}:function:save">
			<a id="adSaveAdviceBtn" href="javascript:adSaveAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_add',disabled:true" style="width: 103px;margin-top:5px">保存医嘱</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">
			<a id="adDelAdviceBtn" href="javascript:adDelAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_delete',disabled:true" style="width: 103px;margin-top:5px">删除医嘱</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:stop">
			<a id="adStopAdviceBtn" href="javascript:adStopAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_delete',disabled:true" style="width: 103px;margin-top:5px">停止医嘱</a>
		</shiro:hasPermission>
		<a id="adHerbMedicineBtn" href="javascript:adHerbMedicineIn();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_magnify',disabled:true" style="width:103px;margin-top:5px;">草药医嘱</a>
		<shiro:hasPermission name="${menuAlias}:function:save">
			<a id="adAuditAdviceBtn" href="javascript:adAuditAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_edit',disabled:true" style="width: 90px;margin-top:5px">审核</a>
		</shiro:hasPermission>
		<a id="adAddGroupBtn" href="javascript:adAddGroup();" class="easyui-linkbutton" data-options="iconCls:'icon-application_link',disabled:true" style="width: 90px;margin-top:5px">组合</a>
		<a id="adCancelGroupBtn" href="javascript:adCancelGroup();" class="easyui-linkbutton" data-options="iconCls:'icon-application_double',disabled:true" style="width: 103px;margin-top:5px">取消组合</a>
		<shiro:hasPermission name="${menuAlias}:function:save">
			<a id="adSaveGroupBtn" href="javascript:adSaveGroup();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_edit',disabled:true" style="width: 103px;margin-top:5px">保存组套</a>
		</shiro:hasPermission>
		<a id="reformAdviceBtn" href="javascript:reformAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-2013040601125064_easyicon_net_16',disabled:true" style="width: 103px;margin-top:5px">重整医嘱</a>
		<a id="adPrintAdviceBtn" href="javascript:adPrintAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202',disabled:true" style="width: 103px;margin-top:5px">打印医嘱</a>&nbsp;
		<input id="docAdvTypeFilt" class="easyui-combobox" style="width:90px;" data-options="    
									       valueField: 'id',    
									       textField: 'text',    									      
									       data: [{
												id: '01',
												text: '全部医嘱'
											},{
												id: '02',
												text: '有效医嘱'
											},{
												id: '03',
												text: '作废医嘱'
											},{
												id: '04',
												text: '当天医嘱'
											},{
												id: '05',
												text: '未审医嘱'
											}]     
									       " /> 	
    </div>   
    <div data-options="region:'west',tools:'#toolSMId',split:true" title="患者管理" style="height:93%;width: 20%">
    	<ul id="tDt"></ul>
    </div>   
    <div data-options="region:'center',border:true" style="height:93%;width: 80%;border-top:0">
    	<div id="tt1" class="easyui-tabs" data-options="fit:true,border:false">   
			<div title="医嘱" id="adviceTabDivId"></div>
			<div title="历史医嘱" id="hisAdviceTabDivId"></div>
			<div title="药品" id="drugTabDivId"></div>
			<div title="非药品" id="unDrugTabDivId"></div>
			<div title="LIS查询" id="lisView"></div>
			<div title="PACS查询" id="pacsView"></div>
			<div title="电子病历" id="medicalRecord"></div>
			<div title="门诊处方信息" id="outpatientDetail"></div>
    	</div>
    </div>   
</div>  

<div id="toolSMId">
			<a href="javascript:void(0)" class="icon-reload" onclick="refresh()" ></a>
			<a href="javascript:void(0)" class="icon-fold" onclick="collapseAll()"></a>
			<a href="javascript:void(0)" class="icon-open" onclick="expandAll()" ></a>
</div>  
<div id="mm" class="easyui-menu" data-options="" style="width: 120px;">						
	<div id="patientinfoTree" onclick="patientinfoTree()" data-options="iconCls:'icon-edit'">
			查看患者信息
	</div>
</div>
<div id="adviceListMenu" class="easyui-menu" style="width:100px; display: none;">
	<div data-options="iconCls:'icon-application_double'" onclick="adviceCopy()">复制医嘱</div>
	<div data-options="iconCls:'icon-application_xp'" onclick="advicePaste()">粘贴医嘱</div>		   
</div>
<div id="addData-window" style="position:relative;"></div>    
<div id="adExeDeptModlDivId"></div>
<div id="stackModleDivId"></div>
<div id="adStopData-window"></div>
<div id="chinMediModleDivId"></div>
<div id="adSpeFreData-window"></div>
<div id="adAuditData-window"></div>
<div id="addun"></div>
<div id="stackSaveModleDivId" style="display: none;"><jsp:include page="stackInfoSave.jsp"></jsp:include></div>
<script type="text/javascript">
if(currentLoginDept!=null&&currentLoginDept!=''){//加载遮罩
	$("body").setLoading({
		id:"body",
		isImg:false,
		text:"请选择登录科室"
	});
}
</script>
</body>
</html>