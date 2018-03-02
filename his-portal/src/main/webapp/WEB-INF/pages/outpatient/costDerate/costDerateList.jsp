<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>费用减免</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var deptMap=new Map();
	var conMap="";
	var num="";
	var holNum="";//费用总金额
	var reg = /^(0\.(?!0+$)\d{1,2}|1(\.0{1,2})?)$/  //验证减免比例正则
// 	var now="${now}";//当前时间
	var userId="${userId}";//当前登录用户
	var rowsWest="";
	/**
	*病历号
	*/
	var medId=null;
	/**
	*用户map
	*/
	var userMap='';
	$(function(){
		
		var deptCode="${deptCode}"
		  //加载本病患者树
		$('#tDt').tree({ 
			     url:"<%=basePath%>inpatient/costDerate/patientNarTree.action?deptCode="+deptCode,
			    animate:true,  //点在展开或折叠的时候是否显示动画效果
			    lines:true,    //是否显示树控件上的虚线
			    formatter:function(node){//统计节点总数
			    	var s = node.text;
			    	if(node.children.length>0){
						  if (node.children){
							s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
						} 
			    	}
					return s;
				},
				onDblClick: function(node){//点击节点
					clear();
					medId = node.attributes.inpatientNo;
					if(medId!=='1'){
						queryList(medId,deptCode);
						$('#medicalrecordId').textbox('setValue',node.attributes.idcardNo);	
					}
				}
		}); 	
		bindEnterEvent('medicalrecordId',searchForm,'easyui');
		//渲染表单中的挂号科室
		$.ajax({
			url: "<%=basePath%>inpatient/costDerate/queryDeptMapPublic.action", 
			success: function(deptData) {
				deptMap = deptData;
			}
		});
		//渲染最小费用名称
		$.ajax({
			url:"<%=basePath%>inpatient/costDerate/quertFreeCodeMap.action",
			success:function(feeCodeData){
				feeCodeMap = feeCodeData;
			}
		});
		
		//渲染合同单位
		$.ajax({
			url: "<%=basePath%>register/newInfo/contCombobox.action",   
			success: function(conData) {
				conMap = conData;
			}
		
		});
		
		//渲染用户
		$.ajax({
			url:'<%=basePath%>inpatient/costDerate/queryUserDerate.action',
			success:function(data){
				userMap=data;
			}
		});
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
				$('#medicalrecordId').textbox('setValue',data);
				searchForm();
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
					$('#medicalrecordId').textbox('setValue',data);
					searchForm();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	function searchForm(){
		var medicalrecordIdd=$('#medicalrecordId').val();
		if(medicalrecordIdd == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		else{
			$.ajax({
	   			url: "<%=basePath%>inpatient/costDerate/queryInpatientInfoLi.action?medicalrecordId="+medicalrecordIdd,
				success: function(data) {
					var plist=jQuery.parseJSON(data);
					if(plist.length==0){
						$.messager.alert('提示','没有该患者信息！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}else if(plist.length==1){
						var deptId="${deptCode}"//当前登录病区ID
						var medicalrecordId = plist[0].inpatientNo;
						queryList(medicalrecordId,deptId);
					}else if(plist.length>1){
						$("#diaInpatient").window('open');
						$("#infoDatagrid").datagrid({
							url: "<%=basePath%>inpatient/costDerate/queryInpatientInfoLi.action?menuAlias=${menuAlias}&medicalrecordId="+medicalrecordIdd,    
						    columns:[[    
						        {field:'inpatientNo',title:'住院流水号',width:'20%',align:'center'} ,    
						        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
						        {field:'reportSexName',title:'性别',width:'15%',align:'center'} ,
						        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
						        {field:'certificatesNo',title:'身份证号',width:'28%',align:'center'} 
						    ]] ,
						    onDblClickRow:function(rowIndex, rowData){
						    	var deptCode="${deptCode}"//当前登录病区Code
								var medicalrecordId = rowData.medicalrecordId;
						    	var inpatientNo=rowData.inpatientNo;
								$("#diaInpatient").window('close');
								queryList(inpatientNo,deptCode);
						    }
						});
					}
				}
	   		});	
		}
	}
	function queryList(medicalrecordId,deptCode){	
		var inpatientNo = null;
		$.ajax({
			url:"<%=basePath%>inpatient/costDerate/queryInpatientInfoObj.action",
			async:false,
			data:{medicalrecordId:medicalrecordId,deptCode:deptCode},
			success:function(data){
				var dataObjMap = data;
				if(dataObjMap.resMsg=="error"){
					$.messager.alert('提示',dataObjMap.resCode);
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					clear();
				}else if(dataObjMap.resMsg=="success"){
					 var dataobj = dataObjMap.resCode;
					 inpatientNo=dataobj.inpatientNo;
						//姓名
						$('#patientName').textbox('setValue',dataobj.patientName);
						//合同单位
						$('#pactCode').textbox('setValue',functionPact(dataobj.pactCode));
						//住院科室
						$('#deptCode').textbox('setValue',dataobj.deptName);
						$('#deptCodeHidden').val(dataobj.deptCode);
						//所属病区
						$('#nursestation').textbox('setValue',dataobj.nurseCellName);
						//入院日期
						$('#inDate').val(dataobj.inDate);
						//住院医生
						$('#houseDocCode').textbox('setValue',dataobj.houseDocName);
						//出生日期
						$('#reportBirthday').val(dataobj.reportBirthday);

						//预交金额
						if(dataobj.prepayCost==0.0){
							$('#prepayCost').textbox('setValue',"0");
						}else{
							$('#prepayCost').textbox('setValue',dataobj.prepayCost);
						}
						if(dataobj.pubCost==0.0){
							$('#pubCost').textbox('setValue',"0");
						}else{
							//公费金额
							$('#pubCost').textbox('setValue',dataobj.pubCost);
						}
						if(dataobj.freeCost==0.0){
							$('#freeCost').textbox('setValue',"0");
						}else{
							//余额
							$('#freeCost').textbox('setValue',dataobj.freeCost);
						}
						if(dataobj.totCost==0.0){
							$('#totCost').textbox('setValue',"0");
						}else{
							//费用总额
							$('#totCost').textbox('setValue',dataobj.totCost);
						}
						if(dataobj.prepayCost==0.0){
							$('#ownCost').textbox('setValue',"0");
						}else{
							//自费金额
							$('#ownCost').textbox('setValue',dataobj.ownCost);
						}
						if(dataobj.prepayCost==0.0){
							$('#payCost').textbox('setValue',"0");
						}else{
							//自付金额
							if(dataobj.payCost==0.){
								$('#payCost').textbox('setValue',"0");

							}else{
								$('#payCost').textbox('setValue',dataobj.payCost);

							}
						}
						//床号
						$('#bedId').textbox('setValue',dataobj.bedName);
						//住院流水号隐藏域赋值
						$('#inpatientNoHidden').val(dataobj.inpatientNo);
						$('#easteast').datagrid({
							url:"<%=basePath%>inpatient/costDerate/queryDerate.action?menuAlias=${menuAlias}&inpatientNo="+inpatientNo,
						});
						$('#westwest').datagrid({
							url:"<%=basePath%>inpatient/costDerate/queryThreeSearch.action?menuAlias=${menuAlias}&inpatientNo="+inpatientNo,
							onClickRow:function(rowIndex,rowData){
								num=rowIndex;
								holNum=rowData.totCost
							},
							onLoadSuccess:function(data){
								var westRows=$('#westwest').datagrid('getRows');
// 								回显列表中的已减免金额和减免后金额
								$.ajax({
									url:"<%=basePath%>inpatient/costDerate/queryDerateMoneySum.action?inpatientNo="+inpatientNo,
									async:false,
									success:function(data){
										 for(var i=0;i<westRows.length;i++){
											    var totCost=westRows[i].totCost;
												for(var j=0;j<data.length;j++){
													if(westRows[i].feeCode==data[j].feeCode){
														var f =0;
														if(data[j].derateCost>=0){
															f =totCost-data[j].derateCost;
														}else if(data[j].derateCost<0){
															f =totCost+data[j].derateCost;
														}
														$('#westwest').datagrid('updateRow',{
															index:i,
															row:{
																already:parseFloat(data[j].derateCost).toFixed(2),
																after:parseFloat(f).toFixed(2)
															}
														});
													}
												}
												
										 }
									}
								});
							}
						});
				}
			}
		});
						
						
		
	}
	//右边数据移到左边
	function queryWest(){
		var rows = $('#easteast').datagrid('getSelections');
		var indexNum="";//左侧所有记录中与右侧选择的最小费用名称一致的下标
		var money1="";//左侧列表中的已减免金额
		var money2="";//左侧列表中的减免后金额
		var money11="";//右侧列表中的减免金额
		var fee= "";//右侧列表选择的记录的最小费用名称
		var afterAlready="";//取消减免后的一见面金额;
		var afterAfter="";//取消减免后的减免金额
		var number="";//要取消的记录的下标
		for(var i = 0;i < rows.length; i++) {
			if (fee != '') {
				fee += ',';
			}
			fee += rows[i].feeCode;
			money11=rows[i].derateCost;
			number = $('#easteast').datagrid('getRowIndex', rows[i]); 
		}
		if(fee!=""){
			var all = $('#westwest').datagrid('getRows');
			var indexNum="";
			for(var i=0; i<all.length; i++){
				//寻找最小费用相同的记录的下标
				if(all[i].feeCode==fee){
					indexNum=i,
					money1=parseFloat(all[i].already),
					money2=parseFloat(all[i].after)
					var afterAlready= eval( money1+"-"+money11);
					var afterAfter = eval(parseFloat(money2).toFixed(2)+"+"+parseFloat(money11).toFixed(2));
					$('#westwest').datagrid('updateRow',{
						index:indexNum,
						row:{
							already:afterAlready,
							after:afterAfter
						}
					});
					$('#easteast').datagrid('deleteRow',number);
				}
			}
		}else{
			$.messager.alert('提示','请选择记录');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//左边数据移到右边
	function queryEast(){
		if($('#anjine').is(':checked')==false&&$('#anbili').is(':checked')==false){
			$.messager.alert('提示','请选择减免方式');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}else{
			var rowSelected=$('#westwest').datagrid('getSelected');
			if(rowSelected==null){
				$.messager.alert('提示','请选择一条记录进行减免');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			if(!$('#jianmian').val()){
				$.messager.alert('提示','请输入减免金额或减免比例');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}else{
				if($('#anjine').is(':checked')){
					if($('#jianmian').val()<holNum){
						goon();
					}else{
						$.messager.alert('提示','减免金额大于总金额，请重新输入');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
				}else if($('#anbili').is(':checked')){
					var reg = $('#jianmian').val();
					var zz=/^(0\.(?!0+$)\d{1,2}|1(\.0{1,2})?)$/;
					if(reg.match(zz)){
						goon();
					}else{
						$.messager.alert('提示','输入的减免比例有误请重新输入');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}	
				}
			}
		}
	}
	Date.prototype.Format = function (fmt) { //author: meizz
		  var o = {
		    "M+": this.getMonth() + 1, //月份
		    "d+": this.getDate(), //日
		    "H+": this.getHours(), //小时
		    "m+": this.getMinutes(), //分
		    "s+": this.getSeconds(), //秒
		    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
		    "S": this.getMilliseconds() //毫秒
		  };
		  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		  for (var k in o)
		  if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		  return fmt;
		}
	//验证后继续执行部分
	function goon(){
		if(!$('#derateType').combobox('getValue')){
			$.messager.alert('提示','请选择减免类型');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}else{
			var rows = $('#westwest').datagrid('getSelections');
			var pids = "";//左边的费用名称
			for(var i = 0;i < rows.length; i++) {
				if (pids != '') {
					pids += ',';
				}
				pids += rows[i].feeCode;
			}
			if(pids!=""){
				var q=0;//减免金额
				var qq=0;//累计减免金额
				if($('#anjine').is(':checked')){
					q=$('#jianmian').val();
				}else if($('#anbili').is(':checked')){
					var w=$('#jianmian').val();//减免比例
					q=eval(holNum+"*"+w);
				}
				if(rows[0].already!=undefined){
					qq = eval(q+"+"+rows[0].already);
				}else{
					qq=q;
				}
				f = holNum-qq;//减免后金额
				if(f<0){
					$.messager.alert('提示','减免后金额为负，不能进行减免');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				$('#westwest').datagrid('updateRow',{
					index:num,
					row:{
						already:parseFloat(qq).toFixed(2),
						after:parseFloat(f).toFixed(2)
					}
				});
				var dc=$("#deptCodeHidden").val();
				var dt=$('#derateType').combobox('getValue');
				//当前时间
				var now=new Date().Format("yyyy/MM/dd HH:mm:ss");
				$('#easteast').datagrid('appendRow',{
					derateKind:"1",
					derateType:dt,
					feeCode:pids,
					derateCost:parseFloat(q).toFixed(2),
					deptCode:dc,
					updateUser:userId,
					updateTime:now,
					feeCode:pids,
					validFlag:"1"
				});
			}else{
				$.messager.alert('提示','请选择记录');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
	}
	
	function whole(){
		if(!$('#derateType').combobox('getValue')){
			$.messager.alert('提示','请选择减免类型');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}else{
			var rowsLeft = $('#westwest').datagrid('getRows');
			if(rowsLeft==""||rowsLeft==null){
				$.messager.alert('提示','列表中没有数据，请检查后操作');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}else{
				
				for(var i=0;i<rowsLeft.length;i++){
					var alreadyNum=0;//已经减免的金额
					if(rowsLeft[i].already!=undefined){
						alreadyNum=rowsLeft[i].already
					}
					var pids = rowsLeft[i].feeCode;//左边的费用名称
					var wholeMoney =rowsLeft[i].totCost//左边的费用总金额
					var q=0;//左边的减免总金额
					var qf=0;//每一条记录每一次减免的钱
					if($('#anjine').is(':checked')){
						q=eval($('#jianmian').val()+'+'+alreadyNum);
						qf=$('#jianmian').val();
					}else if($('#anbili').is(':checked')){
						var w=$('#jianmian').val();//减免比例
						var numa=eval(wholeMoney+"*"+w);
						qf=numa;
						q =eval(numa+'+'+alreadyNum);
					}
					var f = eval(wholeMoney+"-"+q);//减免后金额
					if(f<0){
						var aa=i+1;
						$.messager.alert('提示','第'+aa+'条记录减免后金额为负，不能进行减免');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}
					$('#westwest').datagrid('updateRow',{
						index:i,
						row:{
							already:parseFloat(q).toFixed(2),
							after:parseFloat(f).toFixed(2)
						}
					});
					var dc=$("#deptCodeHidden").val();
					var dt=$('#derateType').combobox('getValue');
					$('#easteast').datagrid('appendRow',{
						derateKind:"1",
						derateType:dt,
						feeCode:pids,
						derateCost:parseFloat(qf).toFixed(2),
						deptCode:dc,
						updateUser:userId,
						updateTime:now,
						feeCode:pids,
						validFlag:"1"
					});
				}
			}
		}
	}
	//右边全部移到左边
	function allQueryWest(){
		var rowsLeft = $('#westwest').datagrid('getRows');
		var rowsRight=$('#easteast').datagrid('getRows');
		if(rowsRight==""||rowsRight==null){
			$.messager.alert('提示','列表中没有数据，请检查后进行保存');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}else{
			for(var j=0;j<rowsRight.length;j++){
				var fee= rowsRight[j].feeCode;//右侧列表选择的记录的最小费用名称
				var money11 = rowsRight[j].derateCost;//右侧列表中的减免金额
				var indexNum="";//左侧所有记录中与右侧选择的最小费用名称一致的下标
				var money1=0;//左侧列表中的已减免金额
				var money2=0;//左侧列表中的减免后金额
				var afterAlready=0;//取消减免后的已减免金额;
				var afterAfter=0;//取消减免后的减免金额
				for(var i=0;i<rowsLeft.length;i++){
					//寻找最小费用相同的记录的下标
					if(rowsLeft[i].feeCode==fee){
						indexNum=i;
						money1=rowsLeft[i].already;
						money2=rowsLeft[i].after;
						afterAlready=  eval(money1+"-"+money11);
						afterAfter = eval(money2+"+"+money11);
						$('#westwest').datagrid('updateRow',{
							index:indexNum,
							row:{
								already:parseFloat(afterAlready).toFixed(2),
								after:parseFloat(afterAfter).toFixed(2)
							}
						});
					}
				}
			}
			$('#easteast').datagrid('loadData', { total: 0, rows: [] });
		}
	}
	//左边全部移到右边
	function allQueryEast(){
				if($('#anjine').is(':checked')==false&&$('#anbili').is(':checked')==false){
					$.messager.alert('提示','请选择减免方式');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}else{
					if(!$('#jianmian').val()){
						$.messager.alert('提示','请输入减免金额或减免比例');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}else{
						if($('#anjine').is(':checked')){
							var rowwest=$('#westwest').datagrid('getRows');
							for(var i=0;i<rowwest.length;i++){
								if($('#jianmian').val()>rowwest[i].totCost){
									var a=i+1;
									$.messager.alert('提示','第'+a+'条记录的总金额小于减免金额，请重新输入');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									return false;
								}
							}
							whole();
						}else if($('#anbili').is(':checked')){
							var reg = $('#jianmian').val();
							var zz=/^(0\.(?!0+$)\d{1,2}|1(\.0{1,2})?)$/;
							if(reg.match(zz)){
								whole();
							}else{
								$.messager.alert('提示','输入的减免比例有误请重新输入');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								return false;
							}	
						}
					}
				}
	}
	function freeCodeMaster(value,row,index){
		if(value!=null&&value!=''){
			return feeCodeMap[value];
		}
	}
	function functionuser(value,row,index){
		if(value!=null&&value!=''){
			return userMap[value];
		}
	}
	function functiondeptCode(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	
	function functionPact(value){
		for(var i=0;i<conMap.length;i++){
			if(value==conMap[i].encode){
				return conMap[i].name;
			}
		}
		
	}
	function functionkind(value,row,index){
		if(value==1){
			return "最小费用"
		}else if(value==0){
			return "总额"
		}else if(value==2){
			return "项目减免"
		}
	}
	function functiontype(value,row,index){
		if(value==0){
			return "特困职工"
		}else if(value==1){
			return "院长特批"
		}
	}
	function anjine(){
		$('#jianmian').textbox('setValue',5);
		$('#jianmian').textbox({
			prompt:'',
		});
	}
	function anbili(){
		$('#jianmian').textbox('setValue',"");
		$('#jianmian').textbox({
			prompt:'比例范围0.00~1',
		});
	}
	function functionflag(value,row,index){
		if(value==1){
			return '<input type="checkbox" id="validFlag" name="validFlag" checked="checked"/>';
		}else{
			return '<input type="checkbox" id="validFlag" name="validFlag" />';
		}
	}
	function save(){
		var no=$('#inpatientNoHidden').val();
		var most=$('#easteast').datagrid('getRows');
		if(most==null||most==""){
			$.messager.alert('提示','还没有进行费用减免！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
		var date=JSON.stringify(most);
		$.messager.progress({text:'保存中，请稍后...',modal:true});
		$.ajax({
			url:"<%=basePath%>inpatient/costDerate/saveDerateDategrid.action",
			data:{date:date,no:no},
			type:'post',
			success:function(data){
				$.messager.progress('close');
				$.messager.alert('提示','保存成功');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				clear();
			}
		})
	}
	function functionDate(value,rows,index){
		if(value!=null&&value!=''){
			return value.substr(0,10);
		}
	}
	function clear(){
		$('#easteast').datagrid('loadData', { total: 0, rows: [] });
		$('#westwest').datagrid('loadData', { total: 0, rows: [] });
		$("#tableId tr td input").val('');
		$('#derateType').combobox('setValue',"");
		$('#jianmian').textbox('setValue',"");
		$('#medicalrecordId').textbox('setValue',"");
		$('#anbili').attr("checked",false);
		$('#anjine').attr("checked",false);
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
	.panel-header{
		border-top:0
	}
</style>
</head>
<body style="margin: 0px; padding: 0px">
<div class="easyui-layout" fit=true>
	<div  data-options="region:'west',title:'患者信息',split:true" style="width: 14%;padding:10 0 0 0;min-width: 80px;">
		<ul id="tDt"></ul>
	</div>
	<div data-options="region:'center',border:true" style="width: 90%;height: 100%;border-top:0">
		<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'north',title:'患者详细信息',border:false" style="padding: 3px 5px 5px 5px;width:80%;height:220px;">
				<div style="height: 25px; line-height: 25px;">
					&nbsp;病历号：
					&nbsp;<input class="easyui-textbox" id="medicalrecordId" name="" data-options="prompt:'请输入病历号'">
					<shiro:hasPermission name="${menuAlias}:function:query">
					&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchForm()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:readCard">
       								<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
       							</shiro:hasPermission>
					        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
					        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
       							</shiro:hasPermission>
					<input type="hidden" id="costDerate_card_no">
				</div>
				<table id="tableId" class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black" style="width:100%;margin-top: 5px;">
					<tr>
						<td class="honry-lable">患者姓名：</td>
						<td>
							<input id="inpatientNoHidden" name="clinicNo" type="hidden"/>
							<input id="deptCodeHidden" name="deptCode" type="hidden"/>
							<input class="easyui-textbox" id="patientName" name="patientName" readonly="true">
						</td>
						<td class="honry-lable">合同单位：</td>
						<td>
							<input class="easyui-textbox" id="pactCode" name="pactCode" readonly="true">
						</td>
							<td class="honry-lable">出生日期：</td>
						<td>
<!-- 							<input class="easyui-datebox" id="reportBirthday" name="reportBirthday" readonly="true"> -->
							<input id="reportBirthday"  name="reportBirthday" class="Wdate" type="text" onClick="WdatePicker()"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<td class="honry-lable">床号：</td>
						<td >
							<input class="easyui-textbox" id="bedId" name="bedId" readonly="true">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">住院科室：</td>
						<td>
							<input class="easyui-textbox" id="deptCode" name="deptCode" readonly="true">
						</td>
						<td class="honry-lable">所属病区：</td>
						<td>
							<input class="easyui-textbox" id="nursestation" name="nursestation" readonly="true">
						</td>
						<td class="honry-lable">入院日期：</td>
						<td>
<!-- 							<input class="easyui-datebox" id="inDate" name="inDate" readonly="true"> -->
							<input id="inDate" name="inDate" class="Wdate" type="text" onClick="WdatePicker()"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<td class="honry-lable">住院医生：</td>
						<td>
							<input class="easyui-textbox" id="houseDocCode" name="houseDocCode" readonly="true">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">公费金额：</td>
						<td >
							<input class="easyui-textbox" id="pubCost" name="pubCost" readonly="true">
						</td>
						<td class="honry-lable">费用总额：</td>
						<td>
							<input class="easyui-textbox" id="totCost" name="totCost" readonly="true">
						</td>
						<td class="honry-lable">自费金额：</td>
						<td>
							<input class="easyui-textbox" id="ownCost" name="ownCost" readonly="true">
						</td>
						<td class="honry-lable">自付金额：</td>
						<td>
							<input class="easyui-textbox" id="payCost" name="payCost" readonly="true">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">预交金额：</td>
						<td>
							<input class="easyui-textbox" id="prepayCost" name="prepayCost" readonly="true">
						</td>
						<td class="honry-lable">余额：</td>
						<td colspan="5">
							<input class="easyui-textbox" id="freeCost" name="freeCost" readonly="true">
						</td>
					</tr>
				</table>
			</div>
			<div  data-options="region:'center'" style="padding: 10px 5px 0px 5px;width:89%;height:70%;border-left:0">
						<div class="easyui-layout" data-options="fit:true,border:false">
							<div data-options="region:'north',border:false" style="width: 100%;height: 35px;">
								<table style="">
									<tr>
										<td style="padding:  0px 0px 0px 10px;">
											<input type="radio" id="anjine" onClick="anjine()"  name="derate"/>按金额：
										</td>
										<td style="padding:  0px 0px 0px 10px;">
											<input type="radio" id="anbili" onClick="anbili()" name="derate" />按比例：
										</td>
										<td>
											<input class="easyui-textbox" id="jianmian" onkeyup="functionadd()" data-options="required:true">
										</td>
										<td style="padding:  0px 0px 0px 60px;">
											减免类型：
										</td>
										<td style="padding:  0px 0px 0px 5px;">
											<input id="derateType" class="easyui-combobox" style="width: 150px;" name="derateType"  data-options="required:true,
													textField:'text',
													valueField:'value', 
				 									data:[{
				 										text:'特困职工',
				 										value:'0' 
				 									},{ 
				 										text:'院长特批', 
				 										value:'1' 
				 									}]"/>  
										</td>
										<td  style="padding:  0px 0px 0px 60px;">
										<shiro:hasPermission name="${menuAlias}:function:save">
											<a href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-save'"  onclick="save()" style="margin-top: auto" >保&nbsp;存&nbsp;</a>
										</shiro:hasPermission>
										</td>
									</tr>
								</table>
							</div>
							<div data-options="region:'center',border:false">
									<div  id="westEast" class="easyui-layout"  style="height:100%;" data-options="border:false,fit:true">
										<div id="tt"  data-options="region:'west'" style="width:47%;height:100%;">
											<div id="xuanxiangka" class="easyui-tabs"  data-options="fit:true,border:false">
												<div title="最小费用" style="width: 100%;height: 100%;">
													<table id="westwest" class="easyui-datagrid" style="" data-options="striped:true,border:false,checkOnSelect:true,selectOnCheck:false,fitColumns:false,singleSelect:true,fit:true">
																<thead >
																	<tr>
																		<th data-options="field:'feeCode',width:'150px',formatter:freeCodeMaster">费用名称</th>
																		<th data-options="field:'totCost',width:'90px'" align="right">总金额</th>
																		<th data-options="field:'ownCost',width:'90px'" align="right">自费金额</th>
																		<th data-options="field:'payCost',width:'90px'" align="right">自付金额</th>
																		<th data-options="field:'pubCost',width:'90px'" align="right">公费金额</th>
																		<th data-options="field:'already',width:'90px'" align="right">已减免金额</th>
																		<th data-options="field:'after',width:'90px'" align="right">减免后金额</th>
																	</tr>
																</thead>
													</table>
												</div>
											</div>
										</div>
										<div data-options="region:'center'" align="center"  >
											<table>
												<tr height="50px;" ></tr>
												<tr>
													<td>
														<a href="javascript:void(0)" style="width:46px;" class="easyui-linkbutton"  onclick="queryEast()">&nbsp;&gt;&nbsp;</a>
													</td>
												</tr>
												<tr height="15px;"></tr>
												<tr>
													<td>
														<a href="javascript:void(0)" style="width:46px;"  class="easyui-linkbutton" onclick="allQueryEast()" >&nbsp;&gt;&gt;&nbsp;</a>
													</td>
												</tr>
												<tr height="15px;"></tr>
												<tr>
													<td>
														<a href="javascript:void(0)" style="width:46px;"  class="easyui-linkbutton" onclick="queryWest()"  > &nbsp;&lt;&nbsp; </a>
													</td>
												</tr>
												<tr height="15px;"></tr>
												<tr>
													<td>
														<a href="javascript:void(0)" style="width:46px;" class="easyui-linkbutton" onclick="allQueryWest()" >&nbsp;&lt;&lt;&nbsp;</a>
													</td>
												</tr>
											</table>
										</div>
										<div data-options="region:'east'" style="width:47%;height:50%;">
											<table id="easteast" class="easyui-datagrid" style="margin-left:auto;margin-right:auto;" data-options="striped:true,border:false,checkOnSelect:true,selectOnCheck:false,fitColumns:false,singleSelect:true,fit:true">
												<thead>
													<tr>
														<th data-options="field:'derateKind',width:'90px',formatter:functionkind">减免种类</th>
														<th data-options="field:'derateType',width:'70px',formatter:functiontype">减免类型</th>
														<th data-options="field:'feeCode',width:'100px',formatter:freeCodeMaster">最小费用名称</th>
														<th data-options="field:'derateCost',width:'90px'" align="right">减免金额</th>
														<th data-options="field:'deptCode',width:'90px',formatter:functiondeptCode">科室名称</th>
														<th data-options="field:'updateUser',width:'90px',formatter:functionuser">操作员</th>
														<th data-options="field:'updateTime',width:'90px',formatter:functionDate">操作日期</th>
														<th data-options="field:'feeCode',width:'150px',formatter:freeCodeMaster">项目名称</th>
														<th data-options="field:'validFlag',width:'70px',formatter:functionflag">是否有效</th>
													</tr>
												</thead>
											</table>
										</div>
									</div>
						</div>
						</div>
			</div>
		</div>
	</div>
</div>
<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:650;height:500;padding:5" data-options="modal:true, closed:true">   
	<table id="infoDatagrid" data-options="fitColumns:true,singleSelect:true,fit:true" >   
	</table>  
</div>
</body>
</html>