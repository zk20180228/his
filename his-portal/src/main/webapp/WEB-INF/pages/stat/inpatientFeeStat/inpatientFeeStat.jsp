<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>患者费用查询</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west'" style="width:300px;padding:0px;border-top:0" >
		<div id="cc" class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'north',border:false" align="center" style="height:80px;padding-top: 10px;">
		    	<table>
		    		<tr>
		    			<td>
		    			    <input class="easyui-combobox" id="type" style="width: 80px">
		    				<input type="text"  id="medicalrecordIdSerc"  class="easyui-textbox" data-options="prompt:'床位号、病历号、姓名查询'"/>
		    			</td>
		    		</tr>
		    		<tr style="height:2px"></tr>
		    		<tr>	
		    			<td>
		    			<shiro:hasPermission name="${menuAlias}:function:query"> 
		    			<a  href="javascript:void(0);" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		    			</shiro:hasPermission>
		    			<shiro:hasPermission name="${menuAlias}:function:print"> 
		    			<a  href="javascript:void(0);"  onclick="printPDF()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
		    			</shiro:hasPermission>
		    			</td>
		    		</tr>
		    		<tr style="height: 7px;"></tr>
		    	</table>
		    </div>   
		    <div data-options="region:'center'" style="border-left:0;border-right:0">
		    	<ul id="tDt" style="width:100%;height:100%;">正在加载，请稍后。。。</ul>
		    </div>   
		</div>		
	</div>
	<div data-options="region:'center'" style="width:80%;border-top:0;height: 100%;">		
		<div id="11" class="easyui-layout" data-options="fit:true">  
			<div data-options="region:'north'" style="height:190px;padding:2px;">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0"  data-options="fit:true">
					<tr>
						<td class="honry-lable" style="text-align: right;width: 10%;">住院流水号：</td><td style="width: 10%;" id="inpatientNo"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">姓名：</td><td style="width: 10%;" id="patientName"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">性别：</td><td style="width: 10%;" id="reportSex"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">年龄：</td><td style="width: 10%;" id="reportBirthday"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">床位：</td><td style="width: 10%;" id="bedName"></td>
					</tr>
					<tr>
						<td class="honry-lable" style="text-align: right;width: 10%;">科室：</td><td style="width: 10%;" id="deptName"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">合同单位：</td><td style="width: 10%;" id="pactName"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">入院日期：</td><td style="width: 10%;" id="inDate"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">出院日期：</td><td style="width: 10%;" id="outDate"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">在院状态：</td><td style="width: 10%;" id="inState"></td>
					</tr>
					<tr>
						<td class="honry-lable" style="text-align: right;width: 10%;">费用总计：</td><td style="width: 10%;" id="cost"></td>	
						<td class="honry-lable" style="text-align: right;width: 10%;">预交金额：</td><td style="width: 10%;" id="prepayCost"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">待清金额：</td><td style="width: 10%;" id="totCost"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">已清金额：</td><td style="width: 10%;" id="balanceCost"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">剩余金额：</td><td style="width: 10%;" id="freeCost"></td>					
					</tr>
					<tr>
						<td class="honry-lable" style="text-align: right;width: 10%;">自费费用：</td><td style="width: 10%;" id="ownCost"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">起付标准：</td><td style="width: 10%;"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">按比例自付：</td><td style="width: 10%;"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">统筹记账：</td><td style="width: 10%;" id="pubCost"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">大额记账：</td><td style="width: 10%;"></td>
					</tr>
					<tr>
						<td class="honry-lable" style="text-align: right;width: 10%;">诊断：</td><td style="width: 10%;" id="diagName"></td>	
						<td class="honry-lable" style="text-align: right;width: 10%;">超大额记账：</td><td style="width: 10%;"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">公务员记账：</td><td style="width: 10%;"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">个人账户：</td><td style="width: 10%;" id="payCost"></td>
						<td class="honry-lable" style="text-align: right;width: 10%;">现金支付：</td><td style="width: 10%;"></td>
					</tr>
					<tr>
						<td class="honry-lable" style="text-align: right;width: 6%;">特注：</td><td colspan="10"></td>	
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:false" style="width:100%;height:79%;">
				<div id="tt" class="easyui-tabs" style="height: 100%" data-options="fit:true,border:false">   
					<div title="患者住院信息" style="padding:0px;height: 100%"> 
						<jsp:include page="inpatientInfoList.jsp"></jsp:include>
				    </div>
				    <div title="预交金">
						<jsp:include page="inPrepay.jsp"></jsp:include>
					</div>
					<div title="药品明细">
						<jsp:include page="medicineList.jsp"></jsp:include>
					</div> 
					<div title="非药品明细">
						<jsp:include page="itemList.jsp"></jsp:include>
					</div> 
					<div title="费用汇总信息">
						<jsp:include page="feeList.jsp"></jsp:include>
					</div>
					<div title="结算信息">
						<jsp:include page="headList.jsp"></jsp:include>
					</div>
    			</div>
			</div>		
		</div>
	</div>
</div>
<input id="deptId" type="hidden" value="${deptId}">
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript">
var nodeId;
var oJecb = "";
var zy1=$('#zy1').val();
var cy1=$('#cy1').val();
var stste=[{id:'R',value:'住院登记'},{id:'I',value:'病房接诊'},{id:'B',value:'出院登记'},{id:'O',value:'出院结算'},{id:'P',value:'预约出院'},{id:'N',value:'无费退院'},{id:'C',value:'婴儿封账'}];
/**
 * 回车键查询
 * @author  yeguanqun
 * @param title 标签名称
 * @param title 跳转路径
 * @date 2015-12-9
 * @version 1.0
 */		
$(function(){
	//选项卡选中事件
	$('#tt').tabs({    
	    border:false,    
	    onSelect:function(title){    
	 	var node = $('#tDt').tree('getSelected');
	 	if(node==''||node==null){
	 		return ;
	 	}else{
	 		if(title=='患者住院信息'){
				loadDataGrid(oJecb);
			}
			else if(title=='预交金'){					
				//添加datagrid事件
				$('#listInprepay').datagrid({		
					url:'<%=basePath%>stat/inpatientFee/queryInPrepay.action',
					queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
				});
			}
			else if(title=='药品明细'){					
				//添加datagrid事件
				$('#listMedicine').datagrid({			
					url:'<%=basePath%>stat/inpatientFee/queryMedicineList.action',
					queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
				});
			}
			else if(title=='非药品明细'){					
				//添加datagrid事件
				$('#listItem').datagrid({			
					url:'<%=basePath%>stat/inpatientFee/queryItemList.action',
					queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
				});
			}
			else if(title=='费用汇总信息'){					
				//添加datagrid事件
				$('#listFee').datagrid({			
					url:'<%=basePath%>stat/inpatientFee/queryFeeInfo.action',
					queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
				});
			}
			else if(title=='结算信息'){					
				//添加datagrid事件
				$('#listHead').datagrid({			
					url:'<%=basePath%>stat/inpatientFee/queryBalanceInfo.action',
					queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
				});
			}
	 	}
	    	
	    }    
	});
	
 	bindEnterEvent('medicalrecordIdSerc',searchFrom,'easyui');
	$('#type').combobox({
	    data:[{"id":1,"text":"在院"},{"id":2,"text":"出院"},{"id":'12',"text":"全部"}],  
	    valueField:'id',    
	    textField:'text',
	    required:true,    
	    editable:true,
	    onLoadSuccess:function(none){
	    	$('#type').combobox('select','1');
	    },
	    onChange:function(newValue,oldValue){
	    	if(newValue=="1"){
	    		loadtree(1);
	    	}else if(newValue=="2"){
	    		loadtree(2);
	    	}else if(newValue=="12"){
	    		loadtree(12);
	    	}
	    }
	});
	var type=$("#type").combobox("getValue");
	if(type=="1"){
		loadtree(1);
	}
});

//渲染在院状态

function functionInstate(value,row,index){
	for(var i=0;i<stste.length;i++){
		if(stste[i].id==value){
			return stste[i].value;
		}
	}
}
/**
 * 查询
 * @author  yeguanqun
 * @param title 标签名称
 * @param title 跳转路径
 * @date 2016-6-2
 * @version 1.0
 */
function searchFrom() {	
	var searchText = $('#medicalrecordIdSerc').textbox('getText');
    $("#tDt").tree("search", searchText);
}
function loadDataGrid(data){
	$("#infolist").datagrid({ 
		data:data
	});
}

/**  
 *  
 * @Description：加载本病患者树
 * @Author：yegaunqun
 * @CreateDate：2016-6-1
 * @version 1.0
 * @throws IOException 
 *
 */
 function loadtree(flag){
	 $('#tDt').tree({ 
		 url:"<%=basePath%>statistics/PatientDispensingOut/InfoTree.action",
		   queryParams:{flag:flag},
		   method:'post',
		   animate:true,  //点在展开或折叠的时候是否显示动画效果
		   lines:true,    //是否显示树控件上的虚线
		   formatter:function(node){//统计节点总数
			  var s = node.text;
			  if (node.children){
				  if(node.id==1){
					  s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				  }
		      }  
			  return s;
		   },
		   onBeforeLoad:function(node, param){
			   var node = $('#tDt').tree('find', 1);
			   if(node!=null){
				   $('#tDt').tree('remove', node.target);
				   $('#tDt').append("<span>正在加载，请稍后。。。</span>");
			   }
		   },
		   onClick: function(node){//点击节点
			   nodeId=node;
			   if(node.id=="1"){
				   $.messager.alert("提示","请先选择患者！");
				   return false;
			   }
			    var qq = $('#tt').tabs('getSelected');				
				var tab = qq.panel('options');
				
				$.ajax({
					url:'<%=basePath%>stat/inpatientFee/queryInpatientInfo.action',
					data: 'inpatientInfo.inpatientNo=' + node.id + '&id=' + node.attributes.pid,
					type:'post',
					async:false,
					success:function(data){
						oJecb=data;
						$('#inpatientNo').html(data[0].inpatientNo);
						$('#inpatientNo').val(data[0].inpatientNo);
						$('#patientName').html(data[0].patientName);
						$('#patientName').val(data[0].patientName);
						$('#reportSex').html(data[0].reportSexName);
						var ages=DateOfBirth(data[0].reportBirthday);
						 //年龄
						$("#reportBirthday").html(ages.get("nianling")+ages.get('ageUnits'));
						$('#bedName').html(data[0].bedName);
						$('#deptName').html(data[0].deptName);
						$('#pactName').html(data[0].pactName);
						$('#inDate').html(data[0].inDate);
						var date=data[0].outDate;
						if(date!=null&&date!=""){
							if(date.substring(0,4)==0002||date.substring(0,4)==0001){
								$('#outDate').html("");
							}else{
								$('#outDate').html(data[0].outDate);
							}
						}
						$('#inState').html(functionInstate(data[0].inState));
						$('#prepayCost').html(data[0].prepayCost);
						$('#totCost').html(data[0].totCost);
						$('#balanceCost').html(data[0].balanceCost);
						$('#freeCost').html(data[0].freeCost);
						$('#ownCost').html(data[0].ownCost);
						$('#pubCost').html(data[0].pubCost);
						$('#payCost').html(data[0].payCost);
						$('#diagName').html(data[0].diagName);
						$('#cost').html(data[0].totCost);
						
					}
				});
				if(tab.title=='患者住院信息'){
					loadDataGrid(oJecb);
				}
				else if(tab.title=='预交金'){					
					//添加datagrid事件
					$('#listInprepay').datagrid({		
						url:'<%=basePath%>stat/inpatientFee/queryInPrepay.action',
						queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
					});
				}
				else if(tab.title=='药品明细'){					
					//添加datagrid事件
					$('#listMedicine').datagrid({			
						url:'<%=basePath%>stat/inpatientFee/queryMedicineList.action',
						queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
					});
				}
				else if(tab.title=='非药品明细'){					
					//添加datagrid事件
					$('#listItem').datagrid({			
						url:'<%=basePath%>stat/inpatientFee/queryItemList.action',
						queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
					});
				}
				else if(tab.title=='费用汇总信息'){					
					//添加datagrid事件
					$('#listFee').datagrid({			
						url:'<%=basePath%>stat/inpatientFee/queryFeeInfo.action',
						queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
					});
				}
				else if(tab.title=='结算信息'){					
					//添加datagrid事件
					$('#listHead').datagrid({			
						url:'<%=basePath%>stat/inpatientFee/queryBalanceInfo.action',
						queryParams:{id:node.attributes.pid,'inpatientInfo.inpatientNo':node.id}
					});
				}
		   },onLoadSuccess:function(node, data){
			   console.info(data);
			   if(data.resCode=='error'){
				   $("body").setLoading({
						id:"body",
						isImg:false,
						text:data.resMsg
					});
			   }
		   }
	});
}
	function printPDF(){
		 var qq = $('#tt').tabs('getSelected');				
		 var tab = qq.panel('options');
		 var tab1=tab.title;
		if(tab1=='患者住院信息'){
			var rowCount=$('#infolist').datagrid('getRows');
			if(''!=rowCount&&null!=rowCount){
				$.messager.confirm('确认','是否打印报表?',function(res){
					if(res){
						 window.open ("<c:url value='queryInpatientInfoPDF.action?inpatientInfo.inpatientNo='/>"+nodeId.id+"&id="+nodeId.attributes.pid,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}
				});
			}else{
				$.messager.alert('提示','当前报表没有记录');
			}
		}else if(tab1=='预交金'){
			var rowCount=$('#listInprepay').datagrid('getRows');
			if(''!=rowCount&&null!=rowCount){
				$.messager.confirm('确认','是否打印报表?',function(res){
					if(res){
						 window.open ("<c:url value='queryInPrepayPDF.action?inpatientInfo.inpatientNo='/>"+nodeId.id+"&id="+nodeId.attributes.pid+"&inpatientInfo.patientName="+encodeURIComponent(encodeURIComponent($('#patientName').val())),'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}
				});
			}else{
				$.messager.alert('提示','当前报表没有记录');
			}
		}else if(tab1=='药品明细'){
			var rowCount=$('#listMedicine').datagrid('getRows');
			if(''!=rowCount&&null!=rowCount){
				$.messager.confirm('确认','是否打印报表?',function(res){
					if(res){
						 window.open ("<c:url value='queryMedicineListPDF.action?inpatientInfo.inpatientNo='/>"+nodeId.id+"&id="+nodeId.attributes.pid,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}
				});
			}else{
				$.messager.alert('提示','当前报表没有记录');
			}
			
		}else if(tab1=='非药品明细'){
			var rowCount=$('#listItem').datagrid('getRows');
			if(''!=rowCount&&null!=rowCount){
				$.messager.confirm('确认','是否打印报表?',function(res){
					if(res){
						 window.open ("<c:url value='queryItemListPDF.action?inpatientInfo.inpatientNo='/>"+nodeId.id+"&id="+nodeId.attributes.pid+"&inpatientInfo.patientName="+encodeURIComponent(encodeURIComponent($('#patientName').val())),'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}
				});
			}else{
				$.messager.alert('提示','当前报表没有记录');
			}
			
		}else if(tab1=='费用汇总信息'){
			var rowCount=$('#listFee').datagrid('getRows');
			if(''!=rowCount&&null!=rowCount){
				$.messager.confirm('确认','是否打印报表?',function(res){
					if(res){
						 window.open ("<c:url value='queryFeeInfoPDF.action?inpatientInfo.inpatientNo='/>"+nodeId.id+"&id="+nodeId.attributes.pid+"&inpatientInfo.patientName="+encodeURIComponent(encodeURIComponent($('#patientName').val())),'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}
				});
			}else{
				$.messager.alert('提示','当前报表没有记录');
			}
			
		}else if(tab1=='结算信息'){
			var rowCount=$('#listHead').datagrid('getRows');
			if(''!=rowCount&&null!=rowCount){
				$.messager.confirm('确认','是否打印报表?',function(res){
					if(res){
						 window.open ("<c:url value='queryBalanceInfoPDF.action?inpatientInfo.inpatientNo='/>"+nodeId.id+"&id="+nodeId.attributes.pid+"&inpatientInfo.patientName="+encodeURIComponent(encodeURIComponent($('#patientName').val())),'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}
				});
			}else{
				$.messager.alert('提示','当前报表没有记录');
			}
		}else{
			$.messager.alert('提示','无法辨别的打印栏目');
		}
	}

</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script> 
</body>
</html>