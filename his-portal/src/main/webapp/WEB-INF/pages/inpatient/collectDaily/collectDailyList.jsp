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
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.tableCss .TDlabel{
		text-align: center;
		width:180px;
	}
</style>
<script type="text/javascript">
	/**
	*本次结算额开始时间
	*/
	var startTime=null;
	/**
	*结算的结束时间
	*/
	var endTime=null;
	/**
	*用户map
	*/
	var useMap=null;
	/**
	*结算类别map
	*/
	var setMap=new Map();
	/**
	*统计大类map
	*/
	var freecodeMap=null;
	/**
	*查询标志
	*/
	var statue=null;
	/**
  	*获取支付方式Map
  	*/
  	var payTypeMap=new Map();
	$(function(){
		$.ajax({
			url:'<%=basePath%>finance/collectDaily/queryCollectMaxTime.action',
			success:function(data){
				startTime=data;
			}
		});
		//查询用户map
		$.ajax({
			url:'<%=basePath%>finance/collectDaily/queryUselistdaily.action',
			success:function(data){
				useMap=data;
			}
		});
		//查询统计大类map
		$.ajax({
			url:'<%=basePath%>finance/collectDaily/queryfreecodedaily.action',
			success:function(data){
				freecodeMap=data;
			}
		});
	     $.ajax({
	  	    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=payway",
	  		type:'post',
	  		success: function(data) {
	  			var pwtype = data;
	  			for(var i=0;i<pwtype.length;i++){
	  				payTypeMap.put(pwtype[i].encode,pwtype[i].name);
	  			}
	  		}
	  	});
	   /**
	  	*获取结算类别
	  	*/
	      $.ajax({
	  	    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
	  		type:'post',
	  		success: function(data) {
	  			var pwtype = data;
	  			for(var i=0;i<pwtype.length;i++){
	  				setMap.put(pwtype[i].encode,pwtype[i].name);
	  			}
	  		}
	  	});
	});
	/**
	*查询结算明细及金额信息
	*/
	function searchPeo(){
		endTime=$('#endTime').val();
		if(endTime=='undefind'||endTime==''){
			$.messager.alert('提示',"请选择结束时间");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		 if(!compareTime(startTime,endTime)){
			$.messager.alert('提示',"上次日结时间是"+startTime+",晚于结束时间,请重新选择");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		} 
		$('#listInfo').datagrid({
			url:'<%=basePath%>finance/collectDaily/querydetalDaily.action?menuAlias=${menuAlias}',
			queryParams:{startTime:startTime,endTime:endTime},
			onLoadSuccess:function(data){
				var rows=$('#listInfo').datagrid('getRows');
				var costSum=0;//费用合计值
				for(var i=0;i<rows.length;i++){
					costSum=eval(costSum+"+"+rows[i].cost);
				}
				$('#moneysum').text(costSum);
			}
		});
		$.ajax({
			url:'<%=basePath%>finance/collectDaily/queryTableDaily.action',
			data:{startTime:startTime,endTime:endTime},
			success:function(data){
				//医疗预收款（借方）
				$('#balancePrepaycost').text(data.balancePrepaycost);
				$('#balancePrepaycost1').val(data.balancePrepaycost);
				//医疗预收款（贷方）
				$('#prepayCost').text(data.prepayCost);
				$('#prepayCost1').val(data.prepayCost);
				// 医疗应收款（贷方）
				$('#balanceCost').text(data.balanceCost);
				$('#balanceCost1').val(data.balanceCost);
				//银行存款借方金额，借方支票(银行存款借方)
				$('#debitCheck').text(data.debitCheck);
				$('#debitCheck1').val(data.debitCheck);
				//贷方支票(银行存款贷方)
				$('#lenderCheck').text(data.lenderCheck);
				$('#lenderCheck1').val(data.lenderCheck);
				// 现金（借方）
				$('#cashj').text(data.cashj);
				$('#cashj1').val(data.cashj);
				//现金（贷方）
				$('#cashd').text(data.cashd);
				$('#cashd1').val(data.cashd);
				//借方银行卡
				$('#debitBank').text(data.debitBank);
				$('#debitBank1').val(data.debitBank);
				//贷方银行卡
				$('#lenderBank').text(data.lenderBank);
				$('#lenderBank1').val(data.lenderBank);
				// 院内账户借方
				$('#debitHos').text(data.debitHos);
				$('#debitHos1').val(data.debitHos);
				//院内账户贷方
				$('#lenderHos').text(data.lenderHos);
				$('#lenderHos1').val(data.lenderHos);
				// 借方其它 
				$('#debitOther').text(data.debitOther);
				$('#debitOther1').val(data.debitOther);
				//贷方其它 
				$('#lenderOther').text(data.lenderOther);
				$('#lenderOther1').val(data.lenderOther);
				//减免金额(借方)
				$('#derateCost').text(data.derateCost);
				$('#derateCost1').val(data.derateCost);
				//公费记帐金额（借方）
				$('#busaryPubcost').text(data.busaryPubcost);
				$('#busaryPubcost1').val(data.busaryPubcost);
				//预交金发票张数
				$('#prepayinvNum').text(data.prepayinvNum);
				$('#prepayinvNum1').val(data.prepayinvNum);
				// 预交金作废张数  
				$('#wasteprepayinvNum').text(data.wasteprepayinvNum);
				$('#wasteprepayinvNum1').val(data.wasteprepayinvNum);
				//预交金有效张数
				$('#yujiaojinyouxiaozhangshu').text(eval(data.prepayinvNum+"-"+data.wasteprepayinvNum));
				$('#yujiaojinyouxiaozhangshu1').val(eval(data.prepayinvNum+"-"+data.wasteprepayinvNum));
				//预交金票据区间 
				$('#prepayinvZone').text(data.prepayinvZone);
				$('#prepayinvZone1').val(data.prepayinvZone);
				//作废预交金发票号码
				$('#wasteprepayInvno').text(data.wasteprepayInvno);
				$('#wasteprepayInvno1').val(data.wasteprepayInvno);
				// 结算发票张数
				$('#balanceinvNum').text(data.balanceinvNum);
				$('#balanceinvNum1').val(data.balanceinvNum);
				// 作废结算发票张数 
				$('#wastebalanceinvNum').text(data.wastebalanceinvNum);
				$('#wastebalanceinvNum1').val(data.wastebalanceinvNum);
				//结算有效张数
				$('#jiesuanyouxiaozhangshu').text(eval(data.balanceinvNum+"-"+data.wastebalanceinvNum));
				$('#jiesuanyouxiaozhangshu1').val(eval(data.balanceinvNum+"-"+data.wastebalanceinvNum));
				//结算发票区间  
				$('#balanceinvZone').text(data.balanceinvZone);
				$('#balanceinvZone1').val(data.balanceinvZone);
				//作废结算发票号码
				if(data.wastebalanceInvno!='nullnull'){
					$('#wastebalanceInvno').text(data.wastebalanceInvno);
					$('#wastebalanceInvno1').val(data.wastebalanceInvno);
				}
				
				//上缴现金
				$('#turninCash').text(eval(data.cashj+"-"+data.cashd));
				$('#turninCash1').val(eval(data.cashj+"-"+data.cashd));
				//上缴银联额
				$('#shangjiaoy').text(eval(data.debitBank+"-"+data.lenderBank));
				//上缴支票额
				$('#shangjiaoz').text(eval(data.debitCheck+"-"+data.lenderCheck));
				//借方合计
				$('#leftall').text(eval(data.balancePrepaycost+"+"+data.debitCheck+"+"+data.cashj+"+"+data.debitBank+"+"+data.debitHos+"+"+data.debitOther+"+"+data.derateCost+"+"+data.busaryPubcost).toFixed(2));
				//贷方合计
				$('#rightall').text(eval(data.prepayCost+"+"+data.balanceCost+"+"+data.lenderCheck+"+"+data.cashd+"+"+data.lenderBank+"+"+data.lenderHos+"+"+data.lenderOther).toFixed(2));
			}
		});
		//为查询标志赋值
		statue=1;
	}
	/**
	*医疗预收款(贷方) 点击弹窗事件
	*/
	function function2(){
		$.ajax({
			url:'<%=basePath%>finance/collectDaily/queryYjjDatagridDaily.action',
			data:{startTime:startTime,endTime:endTime},
			success:function(data){
				if(data.length>0){
					$("#diaInpatient").window('open');
					$('#infoDatagrid').datagrid({
						url:'<%=basePath%>finance/collectDaily/queryYjjDatagridDaily.action?menuAlias=${menuAlias}',
						queryParams:{startTime:startTime,endTime:endTime},
						 columns:[[ 
						           {field:'inpatientNo',title:'病历号',width:'20%',align:'center'},  
						           {field:'payWay',title:'支付方式',width:'15%',align:'center',formatter:functionpaytype} ,  
						           {field:'prepayCost',title:'预交金额',width:'15%',align:'center'},  
						           {field:'createTime',title:'操作时间',width:'30%',align:'center'},  
						           {field:'createUser',title:'操作人',width:'20%',align:'center',formatter:functionuser} 
						  ]]
					});
				}else{
					$.messager.alert('提示','没有该项收费明细');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
				
			}
		});
	}
	/**
	*医疗应收款（贷方）点击弹窗事件
	*/
	function function1(){
		$.ajax({
			url:'<%=basePath%>finance/collectDaily/querymedicdatagridDaily.action',
			data:{state:2,startTime:startTime,endTime:endTime},
			success:function(data){
				if(data.length>0){
					$("#diaInpatient").window('open');
					$('#infoDatagrid').datagrid({
						url:'<%=basePath%>finance/collectDaily/querymedicdatagridDaily.action?menuAlias=${menuAlias}',
						queryParams:{startTime:startTime,endTime:endTime},
						 columns:[[ 
						           {field:'inpatientNo',title:'住院流水号',width:'20%',align:'center'} ,  
						           {field:'paykindCode',title:'结算类别',width:'15%',align:'center',formatter:functionset} ,  
						           {field:'totCost',title:'费用金额',width:'15%',align:'center'} ,  
						           {field:'balanceOpername',title:'操作人',width:'20%',align:'center'}, 
						           {field:'balanceDate',title:'操作时间',width:'30%',align:'center'} 
						  ]]
					});
				}else{
					$.messager.alert('提示','没有该项收费明细');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
		});
	}
	/**
	*医疗预收款（借方）点击弹窗事件
	*/
	function function3(){
		$.ajax({
			url:'<%=basePath%>finance/collectDaily/querymedicdatagridDaily.action',
			data:{state:2,startTime:startTime,endTime:endTime},
			success:function(data){
				if(data.length>0){
					$("#diaInpatient").window('open');
					$('#infoDatagrid').datagrid({
						url:'<%=basePath%>finance/collectDaily/querymedicdatagridDaily.action?menuAlias=${menuAlias}',
						queryParams:{startTime:startTime,endTime:endTime},
						 columns:[[ 
						           {field:'inpatientNo',title:'住院流水号',width:'20%',align:'center'} ,  
						           {field:'paykindCode',title:'结算类别',width:'15%',align:'center',formatter:functionset} ,  
						           {field:'prepayCost',title:'预交金额',width:'10%',align:'center'} ,  
						           {field:'foregiftCost',title:'转押金额',width:'10%',align:'center'} ,  
						           {field:'balanceOpername',title:'操作人',width:'20%',align:'center'}, 
						           {field:'balanceDate',title:'操作时间',width:'25%',align:'center'} 
						  ]]
					});
				}else{
					$.messager.alert('提示','没有该项收费明细');
				}
			}
		});
	}
	/**
	*保存日结信息
	*/
	function saveform(){
		if(statue!=1){
			$.messager.alert('提示','请先进行查询，在进行日结保存');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		var rows=$('#listInfo').datagrid('getRows');
		  if(rows.length<=0){
			$.messager.alert('提示','没有日结费用');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}  
		
		$.messager.confirm('确认','是否进行日结,日结后数据将不能恢复',function(r){    
		    if (r){  
				var rows=$('#listInfo').datagrid('getRows');
				var sqc=null;
				var date=null;
				if(rows.length>0){
					 date=JSON.stringify(rows);
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
				$('#editform').form('submit',{
					url:'<%=basePath %>finance/collectDaily/saveFromDaily.action', 
					queryParams:{date:date,startTime:startTime,endTime:endTime},
					success:function(data){ 
						$.messager.progress('close');
						if(data==1){
							$.messager.alert('提示','保存成功');
							
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$('#endTime').val('');
							$.ajax({
								url:'<%=basePath%>finance/collectDaily/queryCollectMaxTime.action',
								success:function(data){
									startTime=data;
								}
							});
							 location.reload();
							$('#listInfo').datagrid('loadData',{ total:0,rows:[]});
							$('#balancePrepaycost').text('');
							
							statue=2;
						}else{
							$.messager.alert('提示','保存失败');
						}
				 	},
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','未知错误请联系管理员');
						return;
					}
			    });
		    }    
		});  		
	}
	/**
	*比较时间的大小
	*/
	function compareTime(start,end){
		var s = new Date(start.replace(/\-/g, "\/")); 
		var e = new Date(end.replace(/\-/g, "\/"));
		if(s<e){
			return true;
		}else{
			return false;
		}
	}
	/**
	*用户渲染
	*/
	function functionuser(value,row,index){
		if(value!=null&&value!=''){
			return useMap[value];
		}
		
	}
	/**
	*结算类别渲染
	*/
	function functionset(value,row,index){
		if(value!=null&&value!=''){
			return setMap.get(value);
		}
	}
	
	/**
	*渲染支付方式
	*/
	function functionpaytype(value,row,index){
		if(value!=null&&value!=''){
			return payTypeMap.get(value);
		}
	}
	/**
	*统计大类费用渲染
	*/
	function functionfreecode(value,row,index){
		if(value!=null&&value!=''){
			return freecodeMap[value];
		}
	}
</script>
<style type="text/css">
.collectDailyList,.collectDailyList .tr1 td{
	border-top:0 !important;
}
</style>
</head>
<body>
		<div id="cc" class="easyui-layout" style="width:100%;height:99%;margin-left:auto;margin-right:auto;">   
		    <div data-options="region:'north',border:false" style="width:50%;height:50px;padding:12px;margin-left:auto;margin-right:auto;" align="center">
		    	<tr>
					<td>结束时间</td>
					<td>
						<input id="endTime" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="searchPeo()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查&nbsp;询&nbsp;</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:save">
					<a href="javascript:void(0)" onclick="saveform()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保&nbsp;存&nbsp;</a>
					</shiro:hasPermission>
				</tr>
		    </div>
		    <div data-options="region:'center',border:false" style="width:55%;height:20%;"  align="center">
		    	<table id="listInfo" class="easyui-datagrid" style="width: 50%;"  data-options="fitColumns:true,singleSelect:true" >
					<thead>
						<tr>
							<th data-options="field:'code',width:'24%',align:'left',formatter:functionfreecode">收费项目</th>
							<th data-options="field:'cost',width:'19%',align:'right'">费用金额</th>
							<th data-options="field:'ownCost',width:'19%',align:'right'">自费金额</th>
							<th data-options="field:'payCost',width:'19%',align:'right'">自付金额</th>
							<th data-options="field:'pubCost',width:'19%',align:'right'">公费金额</th>
						</tr>
					</thead>
				</table>
				<table>
					<tr>
						<td>费用金额合计：</td>
						<td id="moneysum" ></td>
					</tr>
				</table>
			    <form id="editform" method="post">
				    	<table class="tableCss" style="width: 50%" align="center">
							<tr>
								<td style="width: 30%" align="right">借方金额</td>
								<td class="TDlabel">会计科目</td>
								<td style="width: 30%" align="left">贷方金额</td>
							</tr>
							<tr>
								<td   id="balancePrepaycost" onclick="function3()" style="width: 30%" align="right">
									<input type='hidden' id="balancePrepaycost1" name="colDaiVo.balancePrepaycost">
								</td>
								<td class="TDlabel">医疗预收款</td>
								<td id="prepayCost"  onclick="function2()" style="width: 30%" align="left"></td>
								<input type="hidden" id="prepayCost1" name="colDaiVo.prepayCost"/>
							</tr>
							<tr>
								
								<td style="width: 30%" align="right"></td>
								<td class="TDlabel">医疗应收款</td>
								<td id="balanceCost"  onclick="function1()"  style="width: 30%" align="left">
									<input type="hidden" id="balanceCost1" name="colDaiVo.balanceCost"/>
								</td>
							</tr>
							<tr>
								<td id="debitCheck"  style="width: 30%" align="right">
									<input type="hidden" id="debitCheck1" name="colDaiVo.debitCheck"/>
								</td>
								<td class="TDlabel">银行存款</td>
								<td id="lenderCheck"  style="width: 30%" align="left">
									<input type="hidden" id="lenderCheck1" name="colDaiVo.lenderCheck"/>
								</td>
							</tr>
							<tr>
								<td id="cashj"  style="width: 30%" align="right">
									<input type="hidden" id="cashj1" name="colDaiVo.cashj"/>
								</td>
								<td class="TDlabel">现金</td>
								<td id="cashd"  style="width: 30%" align="left">
									<input type="hidden" id="cashd1" name="colDaiVo.cashd"/>
								</td>
							</tr>
							<tr>
								<td id="debitBank" style="width: 30%" align="right">
									<input type="hidden" id="debitBank1"  name="colDaiVo.debitBank"/>
								</td>
								<td class="TDlabel">银行卡</td>
								<td id="lenderBank"  style="width: 30%" align="left">
									<input type="hidden" id="lenderBank1" name="colDaiVo.lenderBank"/>
								</td>
							</tr>
							<tr>
								<td id="debitHos"  style="width: 30%" align="right">
									<input type="hidden" id="debitHos1" name="colDaiVo.debitHos"/>
								</td>
								<td class="TDlabel">院内账户</td>
								<td id="lenderHos"  style="width: 30%" align="left">
									<input type="hidden" id="lenderHos1" name="colDaiVo.lenderHos"/>
								</td>
							</tr>
							<tr>
								<td id="debitOther" style="width: 30%" align="right">
									<input type="hidden" id="debitOther1"  name="colDaiVo.debitOther"/>
								</td>
								<td class="TDlabel">其他预收</td>
								<td id="lenderOther"  style="width: 30%" align="left">
									<input type="hidden" id="lenderOther1" name="colDaiVo.lenderOther"/>
								</td>
							</tr>
							<tr>
								<td id="derateCost"  style="width: 30%" align="right">
									<input type="hidden" id="derateCost1" name="colDaiVo.derateCost"/>
								</td>
								<td class="TDlabel">医疗减免</td>
								<td id="" name="" style="width: 30%" align="left">
									<input type="hidden" id="" />
								</td>
							</tr>
							<tr>
								<td id="busaryPubcost"  style="width: 30%" align="right">
									<input type="hidden" id="busaryPubcost1" name="colDaiVo.busaryPubcost"/>
								</td>
								<td class="TDlabel">公费医疗记账</td>
								<td id="" name="" style="width: 30%" align="left">
									<input type="hidden" id="" />
								</td>
							</tr>
							<tr>
								<td id="" name="" style="width: 30%" align="right"></td>
								<td class="TDlabel">市保账户</td>
								<td id="" name="" style="width: 30%" align="left"></td>
							</tr>
							<tr>
								<td id="" name="" style="width: 30%" align="right"></td>
								<td class="TDlabel">市保统筹</td>
								<td id="" name="" style="width: 30%" align="left"></td>
							</tr>
							<tr>
								<td id="" name="" style="width: 30%" align="right"></td>
								<td class="TDlabel">市保大额</td>
								<td id="" name="" style="width: 30%" align="left"></td>
							</tr>
							<tr>
								<td id="" name="" style="width: 30%" align="right"></td>
								<td class="TDlabel">省保账户</td>
								<td id="" name="" style="width: 30%" align="left"></td>
							</tr>
							<tr>
								<td id="" name="" style="width: 30%" align="right"></td>
								<td class="TDlabel">省保统筹</td>
								<td id="" name="" style="width: 30%" align="left"></td>
							</tr>
							<tr>
								<td id="" name="" style="width: 30%" align="right"></td>
								<td class="TDlabel">省保大额</td>
								<td id="" name="" style="width: 30%" align="left"></td>
							</tr>
							<tr>
								<td id="" name="" style="width: 30%" align="right"></td>
								<td class="TDlabel">省保公务员</td>
								<td id="" name="" style="width: 30%" align="left"></td>
							</tr>
							<tr>
								<td id="leftall" name="" style="width: 30%" align="right"></td>
								<td class="TDlabel">合计</td>
								<td id="rightall" name="" style="width: 30%" align="left"></td>
							</tr>
							<tr>
								<td  align="right">上缴现金：</td>
								<td colspan="2" id="turninCash">
									<input type="hidden" id="turninCash" name="colDaiVo.turninCash"  />
								</td>
							</tr>
							<tr>
								<td  align="right">预交金张数：</td>
								<td colspan="2" id="prepayinvNum" >
									<input type="hidden" id="prepayinvNum1" name="colDaiVo.prepayinvNum"/>
								</td>
							</tr>
							<tr>
								<td  align="right">结算票据张数：</td>
								<td colspan="2" id="balanceinvNum" >
									<input type="hidden" id="balanceinvNum"  name="colDaiVo.balanceinvNum"/>
								</td>
							</tr>
						</table>
						<table class="tableCss collectDailyList" style="width: 50%;" align="center">
							<tr class="tr1">
								<td style="width: 25%" align="right">预交金有效张数：</td>
								<td style="width: 25%" id="yujiaojinyouxiaozhangshu" ></td>
								<td style="width: 25%" align="right">预交金作废张数：</td>
								<td style="width: 25%" id="wasteprepayinvNum" >
									<input type="hidden" id="wasteprepayinvNum1"  name="colDaiVo.wasteprepayinvNum"/>
								</td>
							</tr>
							<tr>
								<td align="right">结算票据有效张数：</td>
								<td id="jiesuanyouxiaozhangshu" ></td>
								<td align="right">结算票据作废张数：</td>
								<td id="wastebalanceinvNum" >
									<input type="hidden" id="wastebalanceinvNum1"  name="colDaiVo.wastebalanceinvNum"/>
								</td>
							</tr>
							<tr>
								<td align="right">上缴银联额：</td>
								<td id="shangjiaoy" ></td>
								<td align="right">上缴支票额：</td>
								<td id="shangjiaoz" ></td>
							</tr>
							<tr>
								<td  align="right">作废预交金发票号码：</td>
								<td colspan="3" id="wasteprepayInvno" >
									<input type="hidden" id="wasteprepayInvno1" name="colDaiVo.wasteprepayInvno"/>
								</td>
							</tr>
							<tr>
								<td  align="right">预交金票据区间：</td>
								<td colspan="3" id="prepayinvZone" >
									<input type="hidden" id="prepayinvZone1" name="colDaiVo.prepayinvZone"/>
								</td>
							</tr>
							<tr>
								<td  align="right">结算发票区间：</td>
								<td colspan="3" id="balanceinvZone" >
									<input type="hidden" id="balanceinvZone1" name="colDaiVo.balanceinvZone"/>
								</td>
							</tr>
							<tr>
								<td  align="right">作废结算发票号码 ：</td>
								<td colspan="3" id="wastebalanceInvno" >
									<input type="hidden" id="wastebalanceInvno1" name="colDaiVo.wastebalanceInvno"/>
								</td>
							</tr>
						</table>
				</form>
		    </div>   
		</div>
		 <div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:800;height:500;padding:5" data-options="modal:true, closed:true">   
	  		 <table id="infoDatagrid"  style="" data-options="fitColumns:true,singleSelect:true"></table>
		 </div>
</body>
</html>