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
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
 <script type="text/javascript" src="<%=basePath%>javascript/js/easyui-layout-method-load.js"></script>   
<script type="text/javascript">
	var sexMap=new Map();
    var pactCodeMap="";//合同
    var moneyMap="";//单位
    var userMap="";//用户
    var deptMap=""; //渲染科室
  //渲染表单中的包装单位
    var unitMap="";
    //渲染表单中的包装单位
    var minUnitMap="";
    var feeVo;
    /**  
     *  
     * @Description：回车事件	
     * @Author：zhangjin
     * @CreateDate：2016-4-13
     * @version 1.0
     * @throws IOException 
     *
     */ 
$(function(){
	 $("#adStackTreeSearch").textbox({onClickButton:function(){
		 searchStackTreeNodes();
	    }})
       /*  bindEnterEvent('admNo',keyupNo,'easyui');//就诊卡号的回车事件 */
        bindEnterEvent('tmpMedicalNo',keyupNoByMedicalNo,'easyui');//病历号的回车事件 
        bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
    	bindEnterEvent('adStackTreeSearch',searchStackTreeNodes,'easyui');

    	
    	
    	/**  
    	*  
    	* @Description：获取合同map集合	
    	* @Author：zhangjin
    	* @CreateDate：2016-4-13
    	* @version 1.0
    	* @throws IOException 
    	*
    	*/ 
    	$.ajax({
    		   url: "<%=basePath%>nursestation/nurseCharge/nurseChargePactCode.action", 
    			type:'post',
    			success: function(deptData){
    				pactCodeMap = deptData;
    				
    			}
    		});
    /**  
    *  
    * @Description：获取用户	
    * @Author：zhangjin
    * @CreateDate：2016-4-13
    * @version 1.0
    * @throws IOException 
    *
    */ 
     $.ajax({
    		url: "<%=basePath%>nursestation/nurseCharge/nurseEmply.action",
    		type:"post",
    		success:function(data){
    			if(data!=""&&data!=null){
    				userMap=data;
    			}
    		}
    	});
     
     /**  
     *  
     * @Description：渲染单位	
     * @Author：zhangjin
     * @CreateDate：2016-4-13
     * @version 1.0
     * @throws IOException 
     *
     */ 
    	$.ajax({                      
    	   url: "<%=basePath%>nursestation/nurseCharge/nurseChargeMoney.action", 
    		type:'post',
    		success: function(deptData){
    			moneyMap = deptData;
    		}
    	});
    	//渲染表单中的最小单位
    	$.ajax({
    		url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=minunit", 
    		type:'post',
    		success: function(data) {
    			minUnitMap = data;
    		}
    	});
     
    	//渲染表单中的包装单位
    	$.ajax({
    		url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=packunit", 
    		type:'post',
    		success: function(data) {
    			unitMap = data;
    		}
    	});
    /**  
     *  
     * @Description：查询婴儿是否存到母亲名下	
     * @Author：zhangjin
     * @CreateDate：2016-4-13
     * @version 1.0
     * @throws IOException 
     *
     */ 
    	$.ajax({
    		url:"<%=basePath%>nursestation/nurseCharge/hospitalParameter.action",
    		type:"post",
    		success:function(data){
    			if(data!=null&&data!=""){
    				var meter=data;
    				$("#yefyis").val(meter.yefyis);
    			}
    		}
    	});
    	 /**  
         *  
         * @Description：渲染科室	
         * @Author：zhangjin
         * @CreateDate：2016-4-13
         * @version 1.0
         * @throws IOException 
         *
         */ 
        $.ajax({
        url: "<%=basePath%>nursestation/nurseCharge/querydeptComboboxs.action", 
        	type:'post',
        	success: function(deptData){
        		deptMap = deptData;
        	}
        })
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
    /**  
     *  
     * @Description：获取医生	
     * @Author：zhangjin
     * @CreateDate：2016-4-13
     * @version 1.0
     * @throws IOException 
     *
     */ 
	    $('#emplCode').combobox({
	    	url:"<%=basePath %>nursestation/nurseCharge/queryEmplCode.action",
	    	method:"post",
	    	valueField:'jobNo',    
	    	textField:'name',
	    	mode:'local',
	    	required:true,
	    	onHidePanel:function(none){
	    	    var data = $(this).combobox('getData');
	    	    var val = $(this).combobox('getValue');
	    	    var result = true;
	    	    for (var i = 0; i < data.length; i++) {
	    	        if (val == data[i].jobNo) {
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
	    		keys[keys.length]='code';
	    		keys[keys.length] = 'jobNo';
	    		keys[keys.length] = 'name';
	    		keys[keys.length] = 'pinyin';
	    		keys[keys.length] = 'wb';
	    		keys[keys.length] = 'inputCode';
	    		return filterLocalCombobox(q, row, keys);
	    	},onSelect:function(record){
	    		$("#emplCodeB").val(record.code);
	    	}
	    });
	    /**  
	     *  
	     * @Description：收费信息	
	     * @Author：zhangjin
	     * @CreateDate：2016-4-13
	     * @version 1.0
	     * @throws IOException 
	     *
	     */ 
	    $("#listyz").datagrid({
	    	 method:'post',
	    	 rownumbers:true,
	    	 idField: 'id',
	    	 striped:true,
	    	 border:false,
	    	 checkOnSelect:true,
	    	 selectOnCheck:false,
	    	 singleSelect:true,
	    	 fitColumns:false,
	    	 columns:[[  
	    	           {field:'ck',checkbox:'true',width:'5%' },
	    	           {field:'medicalrecordId',title:'住院号 ',width:'10%',align:'center'},
	    	           {field:'patientName',title:'姓名',width:'8%',align:'center'},
	    	           {field:'undrugName',title:'项目名称 ',width:'19%',align:'center',editor:{options:{required:true},type:'combogrid',
	    	   				options:{
	    	   					url:'<%=basePath %>nursestation/nurseCharge/NurseUndrugCombobox.action?s'+new Date(),
	    	   					rownumbers : true,//显示序号 
	    	   					pagination:true,
	    	   					mode:'remote',
	    	   					pageSize:10,
	    	   					pageList:[10,30,50,80,100],
	    	   					panelWidth:350,
	    	   			        idField:'nid',    
	    	   			        textField:'name', 
	    	   			        onClickRow:function(rowIndex, rowData){
	    		   			        var deptName='';
	    		   			        var amount=0;
	    		   			        var unumber=0;
	    		   			        var money=0;
	    		   			        var dept="";
	    		   			         $('#category').val(rowData.category);
	    	   			        	if(rowData.dept!=null&&rowData.dept!=''){
	    		   			        	deptName=deptMap[rowData.dept];
	    		   			        	dept=rowData.dept
	    	   			        	}else{
	    	   			        		deptName=deptMap[$("#deptCodeB").val()];//hedong 20170321 如果无执行科室 则为患者住院科室 
	    		   			        	dept=$("#deptCodeB").val();//hedong 20170321 如果无执行科室 则为患者住院科室 
	    	   			        	}
	    	   			        	if(rowData.amount!=''&&rowData.amount!=null){
	    	   			        	   amount=rowDta.amount+amount;
	    	   			        	}
	    	   			        	amount=1+amount;
	    	   			        	if(rowData.unumber!=''&&rowData.unumber!=null){
	    	   			        	   unumber=rowDta.unumber+number;
	    	   			        	}
	    	   			        	unumber=1+unumber;
	    	   			        	
	    	   			          if(rowData.money!=''){
	    	   			              money=rowData.money;
	    	   			          }
	    	   			       var rowh= $('#listyz').datagrid('getSelected');
	    	   			      var Index = $('#listyz').datagrid('getRowIndex', rowh);
	    	   			         $('#listyz').datagrid('updateRow',{
	    	   							index: Index,
	    	   							row: {
	    	   								undrugName:rowData.name,
	    	   							    money:money,
	    	   							    amount:1,
	    	   							    unumber:1,
	    	   							    unit:rowData.unit,
	    	   							    nid:rowData.nid,
	    	   							    moneyMount:money,
	    	   								dept:dept,
	    	   								depth:deptName,
	    	   								category:rowData.category,
	    	   								drugType:rowData.drugType,
	    	   								undrugMinimumcost:rowData.undrugMinimumcost,
	    	   								spec:rowData.spec,
	    	   								drugSystype:rowData.drugSystype,
	    	   								drugNature:rowData.drugNature,
	    	   								drugDosageform:rowData.drugDosageform,
	    	   								drugGrade:rowData.drugGrade,
	    	   								minunit:rowData.minunit,
	    	   								drugBasicdose:rowData.drugBasicdose,
	    	   								zsd:'',
	    	   								ty:rowData.ty,
	    	   								itemCode:rowData.itemCode
	    	   								
	    	   							} 
	    	   						});
	    	   					      jsymmoney();//计算页面项目金额
	    	   			        }, 
	    	   			        columns:[[
	    	   			            {field:'name',title:'项目名称',width:100,align:'center'},    
	    	   			            {field:'money',title:'单价',width:100,align:'center'},    
	    	   			            {field:'unit',title:'单位',width:100,align:'center'}
	    	   			        ]]
	    	   				}
	    	   				      
	    	   			} },
	    	           {field:'money',title:'单价',width:'7%',align:'right',halign:'center'},
	    	           {field:'amount',title:'数量',width:'4%',align:'right',halign:'center',editor:{type:'numberbox',options:{required:true,min : 0,precision:2}}},
	    	           {field:'unumber',title:'付数',width:'4%',align:'right',halign:'center',formatter:funOncedosage,editor:{type:'numberbox',options:{required:true,min : 0}}},
	    	           {field:'drugOncedosage',title:'每次量',width:'4%',align:'right',halign:'center',formatter:funOncedosage,editor:{type:'numberbox',options:{required:true,min : 0,precision:2 }}},
	    	           {field:'unit',title:'单位',width:'4%',align:'center',formatter:funPackUnit},
	    	           {field:'moneyMount',title:'合计金额',width:'8%',align:'right',halign:'center'},
	    	           {field:'depth',title:'执行科室',width:'10%',align:'center',editor:{type:'textbox'}},
	    	           {field:'dept',title:'执行科室隐藏',hidden:true},
	    	           {field:'stackName',title:'组合号',width:'8%',align:'center',editor:{type:'textbox'}},
	    	           {field:'chargeOrder',title:'收费日期 ',width:'12%',align:'center'},
	    	           {field:'id',title:'划价信息的Id',hidden:true},
	    	           {field:'huajia',title:'划价标志',hidden:true},
	    	           {field:'category',title:'是否非药品',hidden:true},
	    	           {field:'paykindCode',title:'结算类别',hidden:true},
	    	           {field:'pactCode',title:'合同单位',hidden:true},
	    	           {field:'deptCode',title:'住院科室 ' ,hidden:true},
	    	           {field:'emplCode',title:'开方医生',hidden:true},
	    	           {field:'zsd',title:'药品非药品明细',hidden:true},
	    	           {field:'drugType',title:'药品类别',hidden:true},
	    	           {field:'babyFlag',title:'是否婴儿',hidden:true},
	    	           {field:'undrugMinimumcost',title:'最小费用代码 ' ,hidden:true},
	    	           {field:'spec',title:'规格',hidden:true},
	    	           {field:'',title:'处方号',hidden:true},
	    	           {field:'drugSystype',title:'系统类别',hidden:true},
	    	           {field:'drugNature',title:'药品性质',hidden:true},
	    	           {field:'drugDosageform',title:'剂型',hidden:true},
	    	           {field:'drugGrade',title:'药品等级',hidden:true},
	    	           {field:'minunit',title:'最小单位',hidden:true},
	    	           {field:'ty',title:'1药品2非药品',hidden:true },
	    	           {field:'nid',title:'项目id',hidden:true },
	    	           {field:'itemCode',title:'项目编码',hidden:true},
	    	           {field:'inpatientNo',title:'住院流水号',hidden:true},
	    	           {field:'drugBasicdose',title:'基本剂量',hidden:true},
	    	           {field:'drugDoseunit',title:'基本单位',hidden:true},
	    	           {field:'stackId',title:'组套代码',hidden:true},
	    	           {field:'stackName1',title:'组套名字',hidden:true},
	    	           {field:'itemCodeToGroup',title:'组套中包含的项目编码',hidden:true},
	    	           {field:'itemNameToGroup',title:'组套中包含的项目名称',hidden:true},
	    	           {field:'enseCost',title:'自费金额',hidden:true},
	    	           {field:'privilegeCost',title:'优惠金额',hidden:true},
	    	           {field:'pubCost',title:'公费金额',hidden:true},
	    	           {field:'unitHidden',title:'单位',hidden:true},
	    	           {field:'finalAmount',title:'最小单位数量',hidden:true},
	    	           {field:'packQty',title:'包装数量',hidden:true},
	    	           {field:'extFlag',title:'开立单位',hidden:true}
	    ]]
	    });
	    /**  
	    *  
	    * @Description：加载本病患者树
	    * @Author：zhangjin
	    * @CreateDate：2016-4-13
	    * @version 1.0
	    * @throws IOException 
	    *
	    */
	    $('#tDt').tree({ 
	    	   url:"<%=basePath%>nursestation/nurseCharge/treeNurseCharge.action?deptId="+$("#deptId").val(),
	    	   method:'get',
	    	   animate:true,  //点在展开或折叠的时候是否显示动画效果
	    	   lines:true,    //是否显示树控件上的虚线
	    	   state:closed,
	    	   formatter:function(node){//统计节点总数
	    		var s = node.text;
	    		  if (node.children){
	    			if(node.children.length!=0){
	    				s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
	    			}
	    		}  
	    			return s;
	    		}, onDblClick: function(node){//点击节点
	    			clear();//清屏
	    			var id = node.id;
	    			 $('#uId').val(id);  //拿到树子节点的id 
	    			 querInpatinfo();//加载患者信息
	    			 $('#add').linkbutton('enable');
	    		},onClick: function(node){
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				}
	    }); 
	        
	    /**  
	    *  
	    * @Description：加载最小费用与统计大类信息
	    * @Author：zhangjin
	    * @CreateDate：2016-4-13
	    * @Modifier：
	    * @ModifyDate：  
	    * @ModifyRmk：   
	    * @version 1.0
	    * @throws IOException 
	    *
	    */
	    $('#tEt').tree({ 
	     url:"<%=basePath%>nursestation/nurseCharge/treeNurseAdvice.action",
	     method:'post',
	     state: 'closed',
	     animate:true,  //点在展开或折叠的时候是否显示动画效果
	     lines:true,    //是否显示树控件上的虚线
	    	onDblClick: function(node){//点击节点
	    		var id = node.id; 
	    		 $('#naId').val(id);  //拿到树子节点的id  */
	    		var myDate = new Date(); 
	    		var year=myDate.getFullYear();    //获取完整的年份(4位,1970-????)
	    		var month =myDate.getMonth()+1;       //获取当前月份(0-11,0代表1月)
	    		var day=myDate.getDate();     //获取当前日
	    		var hour=myDate.getHours();//
	    		if(hour<10){
	    			hour='0'+hour;
	    		}
	    		var chargeOrder=year+'-'+month+'-'+day+" "+hour+':'+myDate.getMinutes()+':'+myDate.getSeconds();//当前年月日
				chargeOrder=chargeOrder.replace(/-(\d)\b/g,'-0$1');
				chargeOrder=chargeOrder.replace(/:(\d)\b/g,':0$1');
	    		var deptCode=$("#deptCodeB").val();//住院科室
//	    		var admNo=$("#admNo").textbox("getValue");//住院号
	    		var admNo=$("#admNo1").val();//住院号
	    		var patientName=$("#patientName").text();//患者姓名
	    		var emplCode=$("#emplCode").combobox("getValue");//医生姓名
	    		if(admNo!=""&&admNo!=null){
	    			if(emplCode!=null&&emplCode!=""){
	    				    /* chargeMinfeetostat(); */ 
	    				   var inpatientNo=$("#inpatientNo").val();//住院流水号
	    				    var undrugName=node.attributes.undrugName;//项目名称
	    			    	var money=node.attributes.money;//单价
	    			    	var undrugId=node.attributes.undrugId;//项目id
	    			    	var unit=node.attributes.unit;//单位
	    			    	var dept='';//科室
	    			    	var category=node.attributes.category;//标志药品非药品（1）
	    			    	var undrugChildrenprice=node.attributes.undrugChildrenprice;//儿童价
	    			    	var undrugSpecialprice=node.attributes.undrugSpecialprice;//特诊价
	    			    	var undrugMinimumcost=node.attributes.undrugMinimumcost;//最小代码
	    			    	var spec=node.attributes.spec;//规格
	    			    	var itemCode=node.attributes.itemCode;//项目编码
	    			    	var ty=node.attributes.ty;//1药品2非药品
	    			        var money=0;//单价
	    	            	var deptName='';//科室
	    	            	var amount=0;//数量
	    	            	var unumber=0;//
	    	            	var babyFlag=$("#babyFlag").val();//是否婴儿用药
	    	            	if(node.attributes.money!=''){
	    	            		money=node.attributes.money+0;
	    	            	}
	    	            	if(node.attributes.dept!=null&&node.attributes.dept!=''){
	    	            	deptName=deptMap[node.attributes.dept];
	    	            	dept=node.attributes.dept
	    	            	}else{
	    	            		deptName=deptMap[deptCode];//hedong 20170321 如果无执行科室 则为患者住院科室 
	    	            		dept=deptCode;//hedong 20170321 如果无执行科室 则为患者住院科室 
	    	            	}
	    	            	if(node.attributes.amount!=''&&node.attributes.amount!=null){
	    	            	   amount+=node.attributes.amount;
	    	            	}
	    	            	amount+=1;
	    	            	if(node.attributes.unumber!=''&&node.attributes.unumber!=null){
	    	            	   unumber+=node.attributes.unumber;
	    	            	}
	    	            	unumber+=1; 
	    				  $("#listyz").edatagrid('appendRow',{
	    					    babyFlag:babyFlag,
	    					    nid:itemCode,
	    					    inpatientNo:inpatientNo,
	    					    medicalrecordId:admNo,
	    					    patientName:patientName,
	    						undrugName:undrugName,
	    						money:money,
	    						amount:amount,
	    						unumber:unumber,
	    						unit:unit,
	    						moneyMount:money*amount,
	    						dept:dept,
	    						depth:deptName,
	    						category:category,
	    						emplCode:emplCode,
	    						undrugMinimumcost:undrugMinimumcost,
	    						spec:spec,
	    						deptCode:deptCode,
	    						chargeOrder:chargeOrder,
	    						zsd:"",
	    						ty:ty,
	    						itemCode:itemCode,
	    						finalAmount:amount
	    						
	    					});
	    				 editIndex = $("#listyz").edatagrid('getRows').length-1;
	    	 	 	    $("#listyz").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
	    	 	 	  $("#listyz").datagrid('endEdit', editIndex-1);
	    			 	 	<%---合计金额---%>
	    			 	 	 jsymmoney();//计算页面项目金额
	    			 	 	$("#save").linkbutton("enable");
	    			  		$("#del").linkbutton("enable");
	    			}else{
	    				$.messager.alert("提示","请输入开立医生");
	    				setTimeout(function(){
							$(".messager-body").window('close');
						},2000);
	    			}
	    		}else{
	    			$.messager.alert("提示","选择患者");
	    			setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
	    		}
	    	},onClick: function(node){
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			}
	    }); 
	    /**  
	    *  
	    * @Description：加载组套信息
	    * @Author：zhangjin
	    * @CreateDate：2016-4-13
	    * @Modifier：
	    * @ModifyDate：  
	    * @ModifyRmk：   
	    * @version 1.0
	    * @throws IOException 
	    *
	    */
	    $("#tFt").tree({ 
	     url:"<%=basePath%>nursestation/nurseCharge/stackAndStackInfoForTree.action",
	     queryParams:{drugType:"1",type:"1"},
	     method:'post',
	     state: 'closed',
	     animate:true,  //点在展开或折叠的时候是否显示动画效果
	     lines:true,   //节点不展开
	     formatter:function(node){//统计节点总数
	    	    var s = node.text;
	    	    if (node.children){
	    		  s += '<span style=\'color:blue\'>('+ node.children.length + ')</span>';
	    	     }  
	    		return s;
	    	},onLoadSuccess:function(node, data){
	    		if(node!=null&&data.length>0&&node.id!='1'&&node.id!='2'&&node.id!='3'){
	    	    	$('#tFt').tree('collapseAll');
	     	   } 
	     },onDblClick :function(node){//点击节点
	    		var id = node.id;
	    		stackName=node.text;//组套name
	    		$('#naId').val(id);  //拿到树子节点的id 
	    		businessStack(); 
	    },onClick: function(node){
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			}
	    }); 
	    
	    
	    $("#listzj").datagrid({//已收费信息  hedong 20170313调整bug 当选择多条长短不一致的项目时页面样式被破坏（列表数据列与表头列错开）
	    	data:[]    
		});
});
  
/**  
 *  
 * @Description：过滤	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @version 1.0
 * @throws IOException 
 *
 */ 
function filterLocalCombobox(q, row, keys){
	if(keys!=null && keys.length > 0){
		for(var i=0;i<keys.length;i++){ 
		         if(row[keys[i]]!=null&&row[keys[i]]!=''){
		             var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
		             if(istrue==true){
		                      return true;
		             }
		         }
		  }
	}else{
	  var opts = $(this).combobox('options');
	  return row[opts.textField].indexOf(q.toUpperCase()) > -1;
	}
}

/**
 * @Description:提示框自动消失
 * @Author: zhangjin
 * @CreateDate: 2017年2月10日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function alert_autoClose(title,msg,icon){  
	 var interval;  
	 var time=3500;  
	 var x=1;    //设置时间2s
	$.messager.alert(title,msg,icon,function(){});  
	 interval=setInterval(fun,time);  
	        function fun(){  
	      --x;  
	      if(x==0){  
	          clearInterval(interval);  
	  $(".messager-body").window('close');    
	       }  
	}; 
	}
  
var cutFlg=1;//切换焦点标记
 
/**  
*  
* @Description：根据病历查询患者 解决婴儿查询患者信息的问题
* @Author：hedong
* @CreateDate：2017-3-20
* @version 1.0
* @throws IOException 
*
*/
function keyupNoByMedicalNo(){
    var no=$("#tmpMedicalNo").textbox("getText");
	if(no == ''){
		$.messager.alert('提示','请输入病历号！');
		setTimeout(function(){
			$(".messager-body").window('close');
		},2000);
		 clear();//清屏
		return false;
	}
   var dept=$("#deptId").val();
	   if(no!=""){
			$.ajax({
				url: "<%=basePath%>nursestation/nurseCharge/queryNurseChargeByMedicalNo.action",
				data:{inpatientNo:no,deptId:dept},
				type:"post",
  				success: function(data) {//患者信息
  				 var menu=$("#menu").val();
   					if(data!=null&&data!=""){
   						clear();//清屏
   						if(data.length==1){
   							$('#add').linkbutton('enable');
						    var inpin=data;
				            var nono=inpin[0].inpatientNo;//住院流水号
	    		    		if(inpin[0].deptCode!=null){
	    		    			var node2 = $('#tDt').tree('find',inpin[0].deptCode);
		    		    		$("#tDt").tree("expand",node2.target);
	    		    		}
	    		    		setTimeout(function(){
   		    		    		var node3=$("#tDt").tree('find',inpin[0].id);
    		    		    		$("#tDt").tree("scrollTo",node3.target);
   		    		    		$("#tDt").tree('select',node3.target);
	    		    		},1500); 
				          
				            $("#babyFlag").val(inpin[0].babyFlag);//是否婴儿
	   	   					$("#admNo1").val(inpin[0].medicalrecordId);//病历号
	   	   				    $("#tmpMedicalNo").textbox("setValue",inpin[0].medicalrecordId);//病历号
	   	   					$("#admNo").textbox("setValue",inpin[0].idcardNo);//就诊卡号
	   	   					$("#age").val(inpin[0].reportAge);//年龄
	   	   					$("#inpatientNo").val(inpin[0].inpatientNo);//住院流水号
   	   						$("#paykindCode").val(inpin[0].paykindCode);//结算类别
   	   						$("#patientName").text(inpin[0].patientName);//患者姓名 
   	   					    $("#patientNameB").val(inpin[0].patientName);//患者姓名 
   	   						$("#inDate").text(inpin[0].inDate);//入院时间
   	   						$("#pactCodeB").val(inpin[0].pactCode);//合同单位代码
   	   						if(inpin[0].pactCode){
   	   							if(pactCodeMap[inpin[0].pactCode]){
   	   								$("#pactCode").text(pactCodeMap[inpin[0].pactCode]);//合同单位代码
   	   							}else{
   	   								$("#pactCode").text(inpin[0].pactCode);//合同单位代码
   	   							}
   	   						}else{
   	   							$("#pactCode").text("自费");//合同单位代码
   	   						}
   	   						$("#deptCodeB").val(inpin[0].deptCode);//科室
   	   					    $("#deptCode").text(deptMap[inpin[0].deptCode]);//科室
   	   					    if(inpin[0].emplCode){
   	   					  	$("#emplCodeB").val(inpin[0].emplCode);
   	   						$("#emplCode").combobox("select",inpin[0].emplCode);//开方医生
   	   					    }
	   	   					
   	   						$("#moneyAlert").text(inpin[0].moneyAlert);//警戒线
   	   						$("#moneyAlertB").val(inpin[0].moneyAlert);
   	   						$("#freeCost").text((inpin[0].freeCost).toFixed(2));
   	   						if(inpin[0].inState=='O'||inpin[0].inState=='N'){
   	   							$.messager.show({
   	   								title:'提示信息',
   	   								msg:'该患者已出院或者是无费退院，不允许收取费用'
   	   							});
		     					   return;
   	   						}
	   	   					$("#inState").val(inpin[0].inState);//住院状态
	   	   				    $("#stopAcount").val(inpin[0].stopAcount);//是否关账
	   	   					$("#totCost").val(inpin[0].totCost);//费用金额（未结）
		   	   				//加载已划价信息
	   	   				$("#listyz").edatagrid({
							autoSave:true,
	   						url:"<%=basePath%>nursestation/nurseCharge/InpatientMessage.action",
	   						        queryParams:{inpatientNo:nono},
	   								method:"post",
	   								onLoadSuccess: function(data) {
 			   	    					if(data.total>0){
	 			   	    					$("#save").linkbutton("enable");
	 				    			  		$("#del").linkbutton("enable");
	 				    			  		 editIndex = $("#listyz").edatagrid('getRows').length-1;
					    	 	 	    	 $("#listyz").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
					    	 	 	  		 $("#listyz").datagrid('endEdit', editIndex-1);
					    			 	 	 jsymmoney();//计算页面项目金额 
 			   	    					}
 			   	    					 
 			   	    				},
	   								 onSelect: function(rowIndex, rowData){
	   										var onDbListRows = $('#listyz').datagrid('getRows');
	   										if(onDbListRows.length>0){
	   											for(var m=0;m<onDbListRows.length;m++){
	   												var indexRows = $('#listyz').datagrid('getRowIndex',onDbListRows[m]);
	   												$('#listyz').datagrid('endEdit',indexRows);
	   											}
	   										}
											$('#listyz').datagrid('beginEdit',rowIndex); 
											var ed;
											var money = rowData.money;//单价
											var flg;
											if(rowData.drugType!='C'){
												 var drugred = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'drugOncedosage'});
		   	                        		     $(drugred.target).numberbox('destroy');
	   											 var red = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'unumber'});
		   										 $(red.target).numberbox('destroy');  
		   										 ed = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'amount'});
		   										$(ed.target).numberbox({
		   											min:0,    
		   										    precision:0
		   										});
		   										 t = $(ed.target).numberbox('getText');
												 $(ed.target).next("span").children().first().val("").focus().val(t);
												
											}else{
												 var red= $('#listyz').datagrid('getEditor', {index:rowIndex,field:'amount'});
												 ed = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'unumber'});
												 flg = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'drugOncedosage'});
	   											 $(red.target).numberbox('readonly',true);  
	   											 if(rowData.huajia==1){
	   												 var drugred = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'drugOncedosage'});
	 		   										 $(drugred.target).numberbox('readonly',true); 
	   											 }else{
	   											 	 $(flg.target).numberbox('textbox').bind('keyup',function(event) {
	   											 		var drugOncedosage = $(flg.target).numberbox('getText');//数量
	   											 		 setTimeout(function(){
			   											 		if(rowData.drugType=='C'){
																	if(rowData.huajia!=1){
																		var finalAmount=0;
																		finalAmount=Number(drugOncedosage)*Number(rowData.unumber);
																		var amount=Number(drugOncedosage)*Number(rowData.unumber);
																		var moneyMount=Number(money)*Number(drugOncedosage)*Number(rowData.unumber);
																		$('#listyz').datagrid('updateRow',{
						   													index: rowIndex,
						   													row: {
						   														amount:amount,
						   									  					moneyMount:moneyMount.toFixed(4),
						   									  				    drugOncedosage:drugOncedosage,
						   									  				    finalAmount:finalAmount
						   													}
						   												});
																		 cutFlg=2;
						   											     jsymmoney();//计算页面项目金额 
						   											     $('#listyz').datagrid('selectRow',rowIndex);
						   											     
																}
															}
	   													},600);
	   											 	
														
												 }); 
	   											}
	   											if(cutFlg==2){
	   												t = $(flg.target).numberbox('getText');
													$(flg.target).next("span").children().first().val("").focus().val(t);
	   											 }
	   											 if(cutFlg==0){
	   												t = $(ed.target).numberbox('getText');
													$(ed.target).next("span").children().first().val("").focus().val(t);
	   											 }
											}
											$(ed.target).numberbox('textbox').bind('keyup', function(event) {
												if(rowData.drugType!='C'){
													var moneyMount=0;
													var amount = Math.round($(ed.target).numberbox('getText'));//数量
													var finalAmount=amount;
											 		if(rowData.huajia==1){
											 			  if(rowData.extFlag=='1'){
											 					finalAmount=Number(finalAmount)*Number(rowData.packQty);
											 			   }
											 			  if(amount!=null&&amount!=""){
											 				setTimeout(function(){
												 				 $.ajax({
														 				url : "<%=basePath %>nursestation/nurseCharge/getFeeVo.action",
														 				data:{"itemCode":rowData.itemCode,"paykindCode":rowData.pactCode,"totCost":money,"qty":amount,"currentUnit":rowData.minunit,"ty":rowData.ty,"extFlag":rowData.extFlag},
														 				type:'post',
														 				success: function(data) {
														 				    moneyMount = (data.pubCost+data.ownCost+data.ecoCost).toFixed(4);
														 					$('#listyz').datagrid('updateRow',{
							   													index: rowIndex,
							   													row: {
							   														amount:amount,
							   									  					moneyMount:moneyMount,
							   									  				    privilegeCost:(data.ecoCost).toFixed(4),
							   									  			        enseCost:(data.ownCost).toFixed(4),
							   									  					pubCost:(data.pubCost).toFixed(4),
							   									  					finalAmount:finalAmount,
							   									  					packQty:rowData.packQty
							   													}
							   												});
							   											     jsymmoney();//计算页面项目金额 
							   											     $('#listyz').datagrid('selectRow',rowIndex);
														 				}
														 			});
											 				},200);
											 			  }
											 		}else{
											 			if(rowData.ty==1){
											 				if(rowData.minunit==rowData.unitHidden){
												 				finalAmount=Number(amount);
												 				moneyMount= Number(amount)/Number(rowData.packQty)*Number(money).toFixed(4); 
												 			}else{
												 				finalAmount=Number(amount)*Number(rowData.packQty);
												 				moneyMount= Number(amount)*Number(money).toFixed(4); 
												 			}
											 			}else{
											 				 finalAmount=amount;
											 				moneyMount= Number(amount)*Number(money).toFixed(4);
											 			}
											 			setTimeout(function(){
												 			$('#listyz').datagrid('updateRow',{
			   													index: rowIndex,
			   													row: {
			   														amount:amount,
			   									  					moneyMount:moneyMount.toFixed(4),
			   									  					finalAmount:finalAmount,
			   									  					packQty:rowData.packQty
			   													}
			   												 });
												 			 jsymmoney();//计算页面项目金额 
												 			 $('#listyz').datagrid('selectRow',rowIndex);
											 		    },200);
											 		}
												}else{
													var finalAmount=0;
													var amount=0;
													var unumber = Math.round($(ed.target).numberbox('getText'));//付数
													finalAmount=Number(unumber)*Number(rowData.drugOncedosage);
													amount=Number(unumber)*Number(rowData.drugOncedosage);
													var moneyMount=0;
													if(rowData.huajia==1){
															if(rowData.extFlag=='1'){
																finalAmount=Number(finalAmount)*Number(rowData.packQty);
															}
														if(finalAmount!=null&&finalAmount!=""){
															setTimeout(function(){
																$.ajax({
													 				url : "<%=basePath %>nursestation/nurseCharge/getFeeVo.action",
													 				data:{"itemCode":rowData.itemCode,"paykindCode":rowData.pactCode,"totCost":money,"qty":finalAmount,"currentUnit":rowData.minunit,"ty":rowData.ty},
													 				type:'post',
													 				success: function(data) {
													 				    moneyMount = (data.pubCost+data.ownCost+data.ecoCost).toFixed(4);
													 					$('#listyz').datagrid('updateRow',{
						   													index: rowIndex,
						   													row: {
						   														unumber:unumber,
						   														amount:finalAmount,
						   									  					moneyMount:moneyMount,
						   									  				    privilegeCost:(data.ecoCost).toFixed(4),
						   									  			        enseCost:(data.ownCost).toFixed(4),
						   									  					pubCost:(data.pubCost).toFixed(4),
							   									  				finalAmount:finalAmount,
						   									  					packQty:rowData.packQty
						   													}
						   												});
													 					cutFlg=0;
						   											     jsymmoney();//计算页面项目金额 
						   											    $('#listyz').datagrid('selectRow',rowIndex);
													 				}
													 			});
															},400);
														}
													}else{
														
														if(rowData.minunit==rowData.unitHidden){
											 				finalAmount=Number(unumber)*Number(rowData.drugOncedosage);
											 				amount=Number(unumber)*Number(rowData.drugOncedosage);
											 				moneyMount = Number(unumber)*Number(rowData.drugOncedosage)/Number(rowData.packQty)*Number(money).toFixed(4);//合计金额
											 			}else{
											 				finalAmount=Number(unumber)*Number(rowData.drugOncedosage)*Number(rowData.packQty);
											 				amount=Number(unumber)*Number(rowData.drugOncedosage)*Number(rowData.packQty);
											 				moneyMount = Number(unumber)*Number(rowData.drugOncedosage)*Number(money).toFixed(4);//合计金额
											 			}
														setTimeout(function(){
				   									  	  	$('#listyz').datagrid('updateRow',{
			   													index: rowIndex,
			   													row: {
			   														amount:finalAmount,
			   														unumber:unumber,
			   									  					moneyMount:moneyMount.toFixed(4),
				   									  				finalAmount:finalAmount,
			   									  					packQty:rowData.packQty
			   									  					
			   													}
			   												});
				   									  	  	cutFlg=0;
				   									  		jsymmoney();//计算页面项目金额
				   											$('#listyz').datagrid('selectRow',rowIndex);
														},400);
													}
												}
											 jsymmoney();//计算页面项目金额
											});
																				
	   								   }
	   					        });
																						
   						}else if(data.length>1){
   						      $("#diaInpatient").window('open');
  					               jzdt();
   						}else{
   							$.messager.alert("提示",'没有查询到该患者信息');
   						}
   					}else{
   						$.messager.alert("提示","未查询到该患者信息");
   					}
  				 },error:function(){
  					 $.messager.alert("提示","病历号不存在");
  				   }
      	        });
	   }else{
		   $.messager.alert("提示","病历号不能为空");
	   } 
	
   }
/**  
*  
* @Description：当有多条信息时
* @Author：zhangjin
* @CreateDate：2016-4-13
* @version 1.0
* @throws IOException 
*
*/
function jzdt(){
	 var menu=$("#menu").val();
	var no=$("#admNo").textbox("getText");
 var dept=$("#deptId").val();
	$("#infoDatagrid").datagrid({
		url: "<%=basePath%>nursestation/nurseCharge/queryNurseChargeInpinfo.action?menuAlias="+menu,
		queryParams:{inpatientNo:no,deptId:dept},
		method:"post",
	    columns:[[    
	        {field:'inpatientNo',title:'住院号',width:'20%',align:'center'} ,    
	        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
	        {field:'reportSex',title:'性别',width:'20%',align:'center',formatter:function(value,row,index){
	        	return sexMap(value);
	        }} ,
	        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
	        {field:'certificatesNo',title:'身份证号',width:'20%',align:'center'} 
	    ]] ,
	    onDblClickRow:function(rowIndex, rowData){
	    	var rowInpatientNo=rowData.inpatientNo;
	    	if(rowInpatientNo!=""){
	   	   					$("#listyz").edatagrid({
	   	    					url:"<%=basePath%>nursestation/nurseCharge/InpatientMessage.action?menuAlias="+menu,
	   	    			           queryParams:{no:rowInpatientNo},
	   	    						method:"post",
									onLoadSuccess: function(data) {
	 			   	    					if(data.total>0){
		 			   	    					$("#save").linkbutton("enable");
		 				    			  		$("#del").linkbutton("enable");
	 			   	    					}else{
		 			   	    					$("#save").linkbutton("disable");
		 				    			  		$("#del").linkbutton("disable");
	 			   	    					}
	 			   	    				}
	 			   	    			});
	   	   				    $("#babyFlag").val(rowData.babyFlag);//是否婴儿
	   						$("#admNo1").val(rowData.medicalrecordId);
	   						$("#admNo").textbox("setValue",rowData.idcardNo);
	   					    $("#diaInpatient").window('close');
	   						$("#age").val(rowData.reportAge);
	   					    $("#paykindCode").val(rowData.paykindCode);
	   						$("#patientName").text(rowData.patientName);
	   					    $("#patientNameB").val(rowData.patientName);
	   						$("#inDate").text(rowData.inDate);
	   						$("#pactCodeB").val(rowData.pactCode);
	   						if(rowData.pactCode){
	   							if(pactCodeMap[rowData.pactCode]){
		   							$("#pactCode").text(pactCodeMap[rowData.pactCode]);//合同单位代码
	   							}else{
		   							$("#pactCode").text(rowData.pactCode);//合同单位代码
	   							}
	   						}else{
	   							$("#pactCode").text("自费");//合同单位代码
	   						}
	   						
	   					    $("#deptCodeB").val(rowData.deptCode);
	   					    $("#deptCode").text(deptMap[rowData.deptCode]);
	   					    $("#emplCodeB").val(rowData.emplCode);
	   					    $("#emplCode").combobox("select",rowData.emplCode);
	  	   					$("#inState").val(rowData.inState);//住院状态
	   	   				    $("#stopAcount").val(rowData.stopAcount);//是否关账
	   	   					$("#totCost").val(rowData.totCost);//费用金额（未结）
	   	   				    $("#moneyAlert").text(rowData.moneyAlert);//警戒线
	 	   					$("#moneyAlertB").val(rowData.moneyAlert);
	 	   					$("#freeCost").text((rowData.freeCost).toFixed(2));
	  	   					
	   						if(rowData.inState=='O'||rowData.inState=='N'){
	   							$.messager.show({
	   								title:'提示信息',
	   								msg:'该患者已出院或者是无费退院，不允许收取费用'
	   							});
	     					   return false ;
	     					   clear();
	   						}
 			}
	    	
	    }
	});
}
var stackName;//组套name

	/**  
	 *  
	 * @Description：组套详细信息
	 * @Author：zhangjin
	 * @CreateDate：2016-4-13
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：   
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
function businessStack(){
	var admNo=$("#admNo1").val();//住院号
	var emplCode=$("#emplCode").combobox("getValue");//医生姓名
	if(admNo!=""&&admNo!=null){
		if(emplCode!=null&&emplCode!=""){
			var inpatientNo=$("#inpatientNo").val();//住院流水号
			var myDate = new Date(); 
			var year=myDate.getFullYear();    //获取完整的年份(4位,1970-????)
			var month =myDate.getMonth()+1;       //获取当前月份(0-11,0代表1月)
			var day=myDate.getDate();     //获取当前日
			var hour=myDate.getHours();//
    		if(hour<10){
    			hour='0'+hour;
    		}
			var chargeOrder=year+'-'+month+'-'+day+" "+hour+':'+myDate.getMinutes()+':'+myDate.getSeconds();//当前年月日
			chargeOrder=chargeOrder.replace(/-(\d)\b/g,'-0$1');
			chargeOrder=chargeOrder.replace(/:(\d)\b/g,':0$1');
			var deptCode=$("#deptCodeB").val();//住院科室
			var admNo=$("#admNo1").val();//住院号
			var patientName=$("#patientName").text();//患者姓名
			var emplCode=$("#emplCodeB").val();//医生姓名
			var babyFlag=$("#babyFlag").val();//是否婴儿用药
			var id=$('#naId').val();
			var stackId=$('#naId').val();//组套id
			var deptName="";//执行科室Name
			var dept="";//执行科室Code
			var category="1";
			$.ajax({
				url: "<%=basePath%>outpatient/updateStack/getStackInfoByInfoIdForView.action",
				data:{infoId:id,feelType:"1"},
				type:"post",
				success: function(data) {
					if(data!=null&&data!=""){
						for(var i=0;i<data.rows.length;i++){
							var type=data.rows[i].drugSystype // 是中草药
							var amount;
							var money=data.rows[i].drugRetailprice;//单价
							if(data.rows[i].stackInfoNum!=null&&data.rows[i].stackInfoNum!=""){
								amount=data.rows[i].stackInfoNum;//数量
							}else{
								amount=1;
							}
							var combNo;//组合号
	   						if(data.rows[i].combNo!=null&&data.rows[i].combNo!=""){
	   						   combNo=data.rows[i].combNo;//组合号
	   						}else{
	   							combNo="";
	   						}
	   					if(data.rows[i].stackInfoDeptId!=null&&data.rows[i].stackInfoDeptId!=''){
			            	deptName=deptMap[data.rows[i].stackInfoDeptId];
			            	dept=data.rows[i].stackInfoDeptId;
			            	}else{
			            		deptName=deptMap[deptCode];//hedong 20170321 如果无执行科室 则为患者住院科室 
			            		dept=deptCode;//hedong 20170321 如果无执行科室 则为患者住院科室 
			            	}
	   					    var moneyMount=0;//合计
	   	   					var stackInfoItemId=data.rows[i].stackInfoItemId;
	   	   				    var days;
	   	   				    if(data.rows[i].days!=""&&data.rows[i].days!=null){
	   	   				  		days=data.rows[i].days;
	   	   				    }else{
	   	   				  		days=1;
	   	   				    }
	   	   				    var extFlag="1";
	   	   				    var finalAmount=0;
	   	   					var drugOncedosage=data.rows[i].drugOncedosage;
	   	   				    if(data.rows[i].ty=='1'){
	   	   				    	if(data.rows[i].unit==data.rows[i].stackInfoUnit){
	   	   				    		extFlag="2";
	   	   				    		if(data.rows[i].drugType=='C'){
	   	   				    		    drugOncedosage=Number(data.rows[i].drugOncedosage);
	   	   				    		    amount=Number(days)*Number(data.rows[i].drugOncedosage);
		   	   				    		finalAmount=Number(days)*Number(data.rows[i].drugOncedosage);
		   	   				    		moneyMount=(Number(days)/Number(data.rows[i].packagingnum)*Number(data.rows[i].drugOncedosage))*Number(money).toFixed(4);
		   	   				    	    
		   	   				    	}else{
			   	   				       finalAmount=Number(amount);
			   	   				 	   moneyMount=(Number(amount)/Number(data.rows[i].packagingnum))*Number(money).toFixed(4);
		   	   				    	}
	   	   				    	}else{
	   	   				    		extFlag="1";
		   	   				    	if(data.rows[i].drugType=='C'){
		   	   				    	drugOncedosage=Number(data.rows[i].drugOncedosage);
		   	   				    	    amount=Number(days)*Number(data.rows[i].drugOncedosage)*Number(data.rows[i].packagingnum);
		   	   				    		finalAmount=Number(days)*Number(data.rows[i].drugOncedosage)*Number(data.rows[i].packagingnum);
		   	   				    	    moneyMount=Number(days)*Number(data.rows[i].drugOncedosage)*Number(data.rows[i].packagingnum)*Number(money).toFixed(4);
		   	   				    	}else{
			   	   				       finalAmount=Number(amount)*Number(data.rows[i].packagingnum);
			   	   					   moneyMount=Number(money)*Number(amount);
		   	   				    	}
	   	   				    	}
	   	   				    }else{
	   	   				       finalAmount=amount;
	   	   				       moneyMount=Number(money)*Number(amount)
	   	   				    }
	   	   				    if(type==16){
	   	   				  		category="中草药";
								}
	   	   				    if(drugOncedosage==0){
				             	drugOncedosage=null;
				          	} 
			   	   				  $("#listyz").edatagrid('appendRow',{
									    babyFlag:babyFlag,
									    nid:data.rows[i].id,
									    inpatientNo:inpatientNo,
									    medicalrecordId:admNo,
									    patientName:patientName,
										undrugName:data.rows[i].name,
										money:money,
										amount:amount,
										unumber:days,
										unit:data.rows[i].stackInfoUnit,
										moneyMount:moneyMount.toFixed(4),
										depth:deptName,
										minunit:data.rows[i].unit,
										dept:dept,
										deptCode:deptCode,
										category:category,
										emplCode:emplCode,
										chargeOrder:chargeOrder,
										zsd:"",
										ty:data.rows[i].ty,
										stackId:stackId,//组套Id
										stackName:stackName,//组套名字
										stackName1:combNo,//组合号 
										itemCodeToGroup:data.rows[i].code,
										itemNameToGroup:data.rows[i].name,
										unitHidden:data.rows[i].stackInfoUnit,
										packQty:data.rows[i].packagingnum,
										finalAmount:finalAmount,
										extFlag:extFlag,
										drugType:data.rows[i].drugType,
										drugOncedosage:drugOncedosage
								 });
	   	   				   
					     	jsymmoney();//计算页面项目金额
					     $('#save').linkbutton('enable');
				  		$('#del').linkbutton('enable');
						}
					}else{
						$.messager.alert("提示","未查询到该组套信息");
					}
				}
			 });
		}else{
			$.messager.alert("提示","请输入开立医生");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
		}
	}else{
		$.messager.alert("提示","选择患者");
		setTimeout(function(){
			$(".messager-body").window('close');
		},2000);
	}
}
/**  
*  
* @Description：加载患者信息(点击树节点)
* @Author：zhangjin
* @CreateDate：2016-4-13
* @Modifier：
* @ModifyDate：  
* @ModifyRmk：   
* @version 1.0
* @throws IOException 
*
*/
function querInpatinfo(){
   var no=$('#uId').val();
   if(no!=""&&no!=null){
	   $.ajax({
			url: "<%=basePath%>nursestation/nurseCharge/querInpatinfo.action",
			data:{id:no},
			type:"post",
			success: function(data) {
				if(data!=null&&data!=""){
						var info = data;
						var nono=info.inpatientNo;
						$("#babyFlag").val(info.babyFlag);//是否婴儿
						$("#admNo1").val(info.medicalrecordId);//病历号
					    $("#tmpMedicalNo").textbox("setValue",info.medicalrecordId);//病历号
						$("#age").val(info.reportAge);//年龄
						$("#paykindCode").val(info.paykindCode);//结算类别
						$("#patientName").text(info.patientName);
						$("#patientNameB").val(info.patientName);//患者姓名
						$("#inDate").text(info.inDate);//入院时间
						$("#pactCodeB").val(info.pactCode);//合同单位
						if(info.pactCode){
							if(pactCodeMap[info.pactCode]){
								$("#pactCode").text(pactCodeMap[info.pactCode]);//合同单位代码
							}else{
								$("#pactCode").text(info.pactCode);//合同单位代码
							}
						}else{
							$("#pactCode").text("自费");//合同单位代码
						}
						$("#inpatientNo").val(data.inpatientNo);//住院流水号
						$.ajax({
				  			url: "<%=basePath%>nursestation/nurseCharge/querNursePubRati.action?id="+info.pactCode, 
				  			type:'post',
				  			success: function(data2){
				  				if(data2){
				  					var dortor=data2;  
						  			$("#priceForm").val(dortor.priceForm);//价格类型
						  			$("#pubRati").val(dortor.pubRati);//公费比例
				  				}
				  			}
				  		});
						$("#deptCodeB").val(info.deptCode);//科室
					    $("#deptCode").text(deptMap[info.deptCode]);
						if(info.emplCode){
						 $("#emplCodeB").val(info.emplCode);//开据医生
	   					 $("#emplCode").combobox("select",info.emplCode);//开方医生
						}
					   
						$("#moneyAlert").text(info.moneyAlert);
						$("#moneyAlertB").val(info.moneyAlert);//警戒线
						$("#freeCost").text((info.freeCost).toFixed(2));//余额
						$("#inState").val(info.inState);//住院状态
						$("#stopAcount").val(info.stopAcount);//是否关账
						$("#totCost").val(info.totCost);//费用金额（未结）
						//加载已划价信息
						$("#listyz").edatagrid({
							autoSave:true,
	   						url:"<%=basePath%>nursestation/nurseCharge/InpatientMessage.action",
	   						        queryParams:{inpatientNo:nono},
	   								method:"post",
	   								border:false,
	   								onLoadSuccess: function(data) {
 			   	    					if(data.total>0){
	 			   	    					$("#save").linkbutton("enable");
	 				    			  		$("#del").linkbutton("enable");
	 				    			  		 editIndex = $("#listyz").edatagrid('getRows').length-1;
					    	 	 	    	 $("#listyz").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
					    	 	 	  		 $("#listyz").datagrid('endEdit', editIndex-1);
					    			 	 	 jsymmoney();//计算页面项目金额 
 			   	    					}
 			   	    					 
 			   	    				},
	   								 onSelect: function(rowIndex, rowData){
	   										var onDbListRows = $('#listyz').datagrid('getRows');
	   										if(onDbListRows.length>0){
	   											for(var m=0;m<onDbListRows.length;m++){
	   												var indexRows = $('#listyz').datagrid('getRowIndex',onDbListRows[m]);
	   												$('#listyz').datagrid('endEdit',indexRows);
	   											}
	   										}
											$('#listyz').datagrid('beginEdit',rowIndex); 
											var ed;
											var money = rowData.money;//单价
											var flg;
											if(rowData.drugType!='C'){
												 var drugred = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'drugOncedosage'});
		   	                        		     $(drugred.target).numberbox('destroy');
	   											 var red = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'unumber'});
		   										 $(red.target).numberbox('destroy');  
		   										 ed = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'amount'});
		   										$(ed.target).numberbox({
		   											min:0,    
		   										    precision:0
		   										});
		   										 t = $(ed.target).numberbox('getText');
												 $(ed.target).next("span").children().first().val("").focus().val(t);
												
											}else{
												 var red= $('#listyz').datagrid('getEditor', {index:rowIndex,field:'amount'});
												 ed = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'unumber'});
												 flg = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'drugOncedosage'});
	   											 $(red.target).numberbox('readonly',true);  
	   											 if(rowData.huajia==1){
	   												 var drugred = $('#listyz').datagrid('getEditor', {index:rowIndex,field:'drugOncedosage'});
	 		   										 $(drugred.target).numberbox('readonly',true); 
	   											 }else{
	   											 	 $(flg.target).numberbox('textbox').bind('keyup',function(event) {
	   											 		var drugOncedosage = $(flg.target).numberbox('getText');//数量
	   											 		 setTimeout(function(){
			   											 		if(rowData.drugType=='C'){
																	if(rowData.huajia!=1){
																		var finalAmount=0;
																		finalAmount=Number(drugOncedosage)*Number(rowData.unumber);
																		var amount=Number(drugOncedosage)*Number(rowData.unumber);
																		var moneyMount=Number(money)*Number(drugOncedosage)*Number(rowData.unumber);
																		$('#listyz').datagrid('updateRow',{
						   													index: rowIndex,
						   													row: {
						   														amount:amount,
						   									  					moneyMount:moneyMount.toFixed(4),
						   									  				    drugOncedosage:drugOncedosage,
						   									  				    finalAmount:finalAmount
						   													}
						   												});
																		 cutFlg=2;
						   											     jsymmoney();//计算页面项目金额 
						   											     $('#listyz').datagrid('selectRow',rowIndex);
						   											     
																}
															}
	   													},600);
	   											 	
														
												 }); 
	   											}
	   											if(cutFlg==2){
	   												t = $(flg.target).numberbox('getText');
													$(flg.target).next("span").children().first().val("").focus().val(t);
	   											 }
	   											 if(cutFlg==0){
	   												t = $(ed.target).numberbox('getText');
													$(ed.target).next("span").children().first().val("").focus().val(t);
	   											 }
											}
											$(ed.target).numberbox('textbox').bind('keyup', function(event) {
												if(rowData.drugType!='C'){
													var moneyMount=0;
													var amount = Math.round($(ed.target).numberbox('getText'));//数量
													var finalAmount=amount;
											 		if(rowData.huajia==1){
											 			  if(rowData.extFlag=='1'){
											 					finalAmount=Number(finalAmount)*Number(rowData.packQty);
											 			   }
											 			  if(amount!=null&&amount!=""){
											 				setTimeout(function(){
												 				 $.ajax({
														 				url : "<%=basePath %>nursestation/nurseCharge/getFeeVo.action",
														 				data:{"itemCode":rowData.itemCode,"paykindCode":rowData.pactCode,"totCost":money,"qty":amount,"currentUnit":rowData.minunit,"ty":rowData.ty,"extFlag":rowData.extFlag},
														 				type:'post',
														 				success: function(data) {
														 				    moneyMount = (data.pubCost+data.ownCost+data.ecoCost).toFixed(4);
														 					$('#listyz').datagrid('updateRow',{
							   													index: rowIndex,
							   													row: {
							   														amount:amount,
							   									  					moneyMount:moneyMount,
							   									  				    privilegeCost:(data.ecoCost).toFixed(4),
							   									  			        enseCost:(data.ownCost).toFixed(4),
							   									  					pubCost:(data.pubCost).toFixed(4),
							   									  					finalAmount:finalAmount,
							   									  					packQty:rowData.packQty
							   													}
							   												});
							   											     jsymmoney();//计算页面项目金额 
							   											     $('#listyz').datagrid('selectRow',rowIndex);
														 				}
														 			});
											 				},200);
											 			  }
											 		}else{
											 			if(rowData.ty==1){
											 				if(rowData.minunit==rowData.unitHidden){
												 				finalAmount=Number(amount);
												 				moneyMount= Number(amount)/Number(rowData.packQty)*Number(money).toFixed(4); 
												 			}else{
												 				finalAmount=Number(amount)*Number(rowData.packQty);
												 				moneyMount= Number(amount)*Number(money).toFixed(4); 
												 			}
											 			}else{
											 				 finalAmount=amount;
											 				moneyMount= Number(amount)*Number(money).toFixed(4);
											 			}
											 			setTimeout(function(){
												 			$('#listyz').datagrid('updateRow',{
			   													index: rowIndex,
			   													row: {
			   														amount:amount,
			   									  					moneyMount:moneyMount.toFixed(4),
			   									  					finalAmount:finalAmount,
			   									  					packQty:rowData.packQty
			   													}
			   												 });
												 			 jsymmoney();//计算页面项目金额 
												 			 $('#listyz').datagrid('selectRow',rowIndex);
											 		    },200);
											 		}
												}else{
													var finalAmount=0;
													var amount=0;
													var unumber = Math.round($(ed.target).numberbox('getText'));//付数
													finalAmount=Number(unumber)*Number(rowData.drugOncedosage);
													amount=Number(unumber)*Number(rowData.drugOncedosage);
													var moneyMount=0;
													if(rowData.huajia==1){
															if(rowData.extFlag=='1'){
																finalAmount=Number(finalAmount)*Number(rowData.packQty);
															}
														if(finalAmount!=null&&finalAmount!=""){
															setTimeout(function(){
																$.ajax({
													 				url : "<%=basePath %>nursestation/nurseCharge/getFeeVo.action",
													 				data:{"itemCode":rowData.itemCode,"paykindCode":rowData.pactCode,"totCost":money,"qty":finalAmount,"currentUnit":rowData.minunit,"ty":rowData.ty},
													 				type:'post',
													 				success: function(data) {
													 				    moneyMount = (data.pubCost+data.ownCost+data.ecoCost).toFixed(4);
													 					$('#listyz').datagrid('updateRow',{
						   													index: rowIndex,
						   													row: {
						   														unumber:unumber,
						   														amount:finalAmount,
						   									  					moneyMount:moneyMount,
						   									  				    privilegeCost:(data.ecoCost).toFixed(4),
						   									  			        enseCost:(data.ownCost).toFixed(4),
						   									  					pubCost:(data.pubCost).toFixed(4),
							   									  				finalAmount:finalAmount,
						   									  					packQty:rowData.packQty
						   													}
						   												});
													 					cutFlg=0;
						   											     jsymmoney();//计算页面项目金额 
						   											    $('#listyz').datagrid('selectRow',rowIndex);
													 				}
													 			});
															},400);
														}
													}else{
														
														if(rowData.minunit==rowData.unitHidden){
											 				finalAmount=Number(unumber)*Number(rowData.drugOncedosage);
											 				amount=Number(unumber)*Number(rowData.drugOncedosage);
											 				moneyMount = Number(unumber)*Number(rowData.drugOncedosage)/Number(rowData.packQty)*Number(money).toFixed(4);//合计金额
											 			}else{
											 				finalAmount=Number(unumber)*Number(rowData.drugOncedosage)*Number(rowData.packQty);
											 				amount=Number(unumber)*Number(rowData.drugOncedosage)*Number(rowData.packQty);
											 				moneyMount = Number(unumber)*Number(rowData.drugOncedosage)*Number(money).toFixed(4);//合计金额
											 			}
														setTimeout(function(){
				   									  	  	$('#listyz').datagrid('updateRow',{
			   													index: rowIndex,
			   													row: {
			   														amount:finalAmount,
			   														unumber:unumber,
			   									  					moneyMount:moneyMount.toFixed(4),
				   									  				finalAmount:finalAmount,
			   									  					packQty:rowData.packQty
			   									  					
			   													}
			   												});
				   									  	  	cutFlg=0;
				   									  		jsymmoney();//计算页面项目金额
				   											$('#listyz').datagrid('selectRow',rowIndex);
														},400);
													}
												}
											 jsymmoney();//计算页面项目金额
											});
																				
	   								   }
	   					        });
						
				        }
			    }
     });
 }
}

/**  
*  
* @Description：计算页面项目金额
* @Author：zhangjin
* @CreateDate：2016-4-13
* @Modifier：
* @ModifyDate：  
* @ModifyRmk：   
* @version 1.0
* @throws IOException 
*
*/
function jsymmoney(){
	  <%---合计金额---%>
		 var total=0;//合计金额
	 	var rowt=$("#listyz").edatagrid("getRows");
	 	for(var a=0;a<rowt.length;a++){
	 		if(rowt[a].moneyMount){
	 			total= Number(rowt[a].moneyMount)+ Number(total);
	 		}else{
	 			total= Number(0)+ Number(total);
	 		}
	 		
	 		
	 	}
	 	$("#total").textbox("setValue",total.toFixed(2));//hedong 20170317 四舍五入为2位小数	 排除重复相加后产生多为小数的bug
}
/**  
 *  
 * @Description：渲染收费人	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @param1:value(值) 
 * @param2:row(行)
 * @param3:row(索引)
 * @version 1.0
 * @throws IOException 
 *
 */ 
function functionfeecost(value,row,index){
	if(value){
		return userMap[value];
	}else{
		return "";
	}
}


/**  
 *  
 * @Description：渲染合同	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @param1:value(值) 
 * @param2:row(行)
 * @param3:row(索引)
 * @version 1.0
 * @throws IOException 
 *
 */ 
function funcnurseChargePactCode(value,row,index){
		if(value!=null&&value!=''){
			return pactCodeMap[value];
		}
	} 

/**  
 *  
 * @Description：渲染费别	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @param1:value(值) 
 * @param2:row(行)
 * @param3:row(索引)
 * @version 1.0
 * @throws IOException 
 *
 */ 
function funcpaykind(value,row,index){
	var paykindCode=$("#paykindCode").val();
	if(paykindCode!=null&&paykindCode!=""){
			if(paykindCode==1){
				return "自费";
			}else if(paykindCode==2){
				return "保险";
			}else if(paykindCode==3){
				return "公费在职 ";
			}else if(paykindCode==4){
				return "公费退休 ";
			}else{
				return "公费高干 ";
			}
	} 
}


/**  
 *  
 * @Description： 渲染单位	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @param1:value(值) 
 * @param2:row(行)
 * @param3:row(索引)
 * @version 1.0
 * @throws IOException 
 *
 */ 
function funcNurseChargeMoney(value,row,index){
			if(value!=null&&value!=''){
				return moneyMap[value];
			}
		} 

/**  
 *  
 * @Description：添加功能	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @version 1.0
 * @throws IOException 
 *
 */ 
function add(){
 var myDate = new Date(); 
			var year=myDate.getFullYear();    //获取完整的年份(4位,1970-????)
			var month =myDate.getMonth()+1;       //获取当前月份(0-11,0代表1月)
			var day=myDate.getDate();     //获取当前日
			var hour=myDate.getHours();//
    		if(hour<10){
    			hour='0'+hour;
    		}
			var chargeOrder=year+'-'+month+'-'+day+" "+hour+':'+myDate.getMinutes()+':'+myDate.getSeconds();//当前年月日
			chargeOrder=chargeOrder.replace(/-(\d)\b/g,'-0$1');
			chargeOrder=chargeOrder.replace(/:(\d)\b/g,':0$1');
	var inpatientNo=$("#inpatientNo").val();
	var em=$("#emplCode").combobox("getText");
	var babyFlag=$("#babyFlag").val();//是否婴儿用药
  if($("#admNo1").val()==null||""==$("#admNo1").val()){
  	$.messager.alert("提示","请输入患者信息");
  	setTimeout(function(){
		$(".messager-body").window('close');
	},2000);
  }else{
  	if(em!=""&&em!=null){
	    	 $('#listyz').datagrid('appendRow',{
	    		  babyFlag:babyFlag,
				  medicalrecordId: $("#admNo1").val(),
				  patientName: $("#patientName").text(),
				  paykindCode:$("#paykindCode").val(),
				  pactCode:$("#pactCodeB").val(),
				  deptCode:$("#deptCodeB").val(),
				  emplCode:$("#emplCodeB").val(),
				  chargeOrder:chargeOrder,
				  inpatientNo:inpatientNo,
				  zsd:""
				});
	 		 
	 	    editIndex = $("#listyz").edatagrid('getRows').length-1;
	 	  $("#listyz").datagrid('endEdit', editIndex);
	 	 
	    }else{
	    	$.messager.alert("提示","请输入开立医生");
	    	setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
	    }
  }
	$('#save').linkbutton('enable');
	$('#del').linkbutton('enable');
}

/**  
 *  
 * @Description：删除功能按钮	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @version 1.0
 * @throws IOException 
 *
 */ 
function del(){
	var wsf=$("#listyz").datagrid("getRows");
	if(wsf.length==0){
		$.messager.alert("提示","只能删除未收费信息");
		return ;
	}
	en = $("#listyz").datagrid("getChecked"); 
	var rows=en;
	var ids ='';
 var zsds='';
	if (rows!=null&&rows.length > 0) {//选中几行的话触发事件
			for(var i=0;i<rows.length;i++){
				if(rows[i].zsd==""){
					 index=$("#listyz").datagrid('getRowIndex',rows[i]);
					 $("#listyz").datagrid('deleteRow',index);
					 $("#listyz").edatagrid('clearChecked');
					 jsymmoney();//计算页面项目金额
				 } //后台数据
				      if(rows[i].id!=''
				    		  &&rows[i].zsd!=''
				    		  &&rows[i].zsd!=null){
							ids +=rows[i].id+',';
	                        zsds +=rows[i].zsd+',';
						 } 
						
			  }
				  if(zsds!=""&&zsds!=null&&ids!=null&&ids!=""){
					    $.messager.confirm('提示','确认要删除该信息吗？',function(r){    
		 		  		     if (r){   
		 		  		    	$.messager.progress({text:'保存中，请稍后...',modal:true});
		 		  		    	 $.ajax({
					 			      url:"<%=basePath%>nursestation/nurseCharge/nurseChargeDel.action",
					 			        data:{id:ids,zsd:zsds},
					 			        type:'post',
					 			        success: function(data) {
					 			        	$.messager.progress('close');
					 			        	if(data.resCode=='success'){
					 			        		$.messager.alert("提示",'删除成功');
					 			        		 jsymmoney();//计算页面项目金额
					 			        		$("#listyz").edatagrid("reload");
					 			        		$("#listyz").edatagrid('clearChecked');
					 			        	}else{
					 			        		$.messager.alert("提示",'删除失败');
					 			        	}
					 			        	
										},error:function(){
											$.messager.alert("提示",'删除失败');
										}
					 		  	  });
		 		  		     }
					    });
				  }
							
	   }else{
	       $.messager.alert("提示","请选择要删除收费的信息");
	   }
}

/**  
 *  
 * @Description： 渲染人员	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @param1:value(值) 
 * @param2:row(行)
 * @param3:row(索引)
 * @version 1.0
 * @throws IOException 
 *
 */ 
function functionEmp(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}
/**  
 *  
 * @Description：保存功能按钮	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @version 1.0
 * @throws IOException 
 *
 */ 
function save(){
	var to=$("#total").textbox("getValue");
	var row = $("#listyz").edatagrid("getRows");
	var nono=$("#inpatientNo").val();
	if(row.length>0){
      if(to!=0){
	  		$.messager.confirm('提示','您确认要收费吗？',function(r){    
	  		    if (r){  
	  		    	var rows = $("#listyz").edatagrid('getRows');
	  	            for(var i=0;i<rows.length;i++){
	  	                  $("#listyz").datagrid('endEdit', $("#listyz").datagrid('getRowIndex',rows[i]));
	  	            	 if(rows[i].drugType!='C'){
	  	            		rows[i].drugOncedosage=null;
	  	            		
	  	            	 }
	  	            	 if(rows[i].unumber==""){
	  	            		rows[i].unumber=null;
	  	            	 }
	  	            }
	  		    	$("#ym").layout('loading');
	  		    	var stringNurseCharge = JSON.stringify($("#listyz").datagrid("getRows"));
	  		    	$.ajax({
	  		    	  url:"<%=basePath%>nursestation/nurseCharge/nurseSaveOrUpdate.action",
	 			        data:{stringNurseCharge:stringNurseCharge},
	 			        type:'post',
	 					success: function(data) {
	 						var dataMap = data;
				   			if(dataMap.resCode=="error"){
				   				$("#ym").layout('loaded');
				   				$.messager.alert('提示',dataMap.resMsg);
				        		return;
				        	}else if(dataMap.resCode =="success"){
				        		$("#ym").layout('loaded');
				        		$.messager.alert('提示',dataMap.resMsg);
				        		var recipeNo=dataMap.recipeNo3;//处方号
				        		var recipeNo2=dataMap.recipeNo2;//处方号（不重复）
				        		var unName=dataMap.name;//库存不足的非药品项目
				        		var drugname=dataMap.drugname;//药品库存不足项目
				        		var strs= new Array();
				        		strs=unName.split(",");//非药品name
				        		var strname= new Array();
				        		strname=drugname.split(",");
				        		if(unName!=null&&unName!=""){
				        			for(i=0;i<strname.length;i++){
				        				$.messager.alert("提示",strname[i],'info');
				        			}
				        		}
				        	
				        		var itemId=dataMap.itemId;//非药品id
				        		var medId=dataMap.medId;//药品id
				        		/**收费后刷新页面
				        		*/
			        			$('#listzj').datagrid({
			        		    	url:"<%=basePath%>nursestation/nurseCharge/dayCharge.action",
	        				        queryParams:{inpatientNo:nono,recipeNo2:recipeNo2,itemId:itemId,medId:medId},
	        				        method:"post"
			        			});
			        			
			        			if(itemId!=""||medId!=""){
				        			   var itemIds= new Array();
				        			   itemIds=itemId.split(",");//非药品name
						        		var medIds= new Array();
						        		medIds=medId.split(",");//药品name
					        			//删除收费信息
						        		var dataArr = new Array();
						        		var en=$("#listyz").datagrid("getRows");
						        		var len = en.length;
								    	for(var i=0;i<en.length;i++){
								    		for(var a=0;a<itemIds.length;a++){
								    			if(en[i].id==itemIds[a]){
									    			dataArr[dataArr.length] = en[i];
								    			}
								    		}
								    		$("#listyz").datagrid("deleteRow",0);
										}
								    	for(var i=0;i<en.length;i++){
								    		for(var a=0;a<medIds.length;a++){
								    			if(en[i].id==medIds[a]){
									    			dataArr[dataArr.length] = en[i];
								    			}
								    		}
								    		$("#listyz").datagrid("deleteRow",0);
										}
				        		}
				        		
				        		//删除收费信息
				        		var dataArr = new Array();
				        		var en=$("#listyz").datagrid("getRows");
				        		var len = en.length;
						    	for(var i=0;i<len;i++){
						    		for(var a=0;a<strs.length;a++){
						    			if(en[0].undrugName==strs[a]){
							    			dataArr[dataArr.length] = en[0];
						    			}
						    		}
						    		$("#listyz").datagrid("deleteRow",0);
								}
						    	if(dataArr.length>0){
						    		for(var i=0;i<dataArr.length;i++){
						    			$('#listyz').datagrid('appendRow',dataArr[i]);
						    		}
						    	}
		 	 				    var inpatientNo=$("#admNo1").val();//病历号
		 	 				    var dept=$("#deptId").val();//当前科室
		 	 				    $.ajax({//刷新患者信息
		 	 				    	url:"<%=basePath%>nursestation/nurseCharge/queryNurseChargeInpinfo.action",
		 	 				    	data:{inpatientNo:inpatientNo,dept:dept},
		 	 				    	type:"post",
		 	 				    	success: function(data) {
		 	 				    		$("#freeCost").text((data[0].freeCost).toFixed(2));
		 	 				    	}
		 	 				    });
	 		 	 				 $.ajax({//药品出库申请
	 		 	 					 url:"<%=basePath%>nursestation/nurseCharge/nurseDrug.action",
					 			        data:{stringNurseCharge:stringNurseCharge,recipeNo:recipeNo},
					 			        type:'post',
	 		 	 				  });
	 		 	 			    $.ajax({//物资出库申请
				 			        url:"<%=basePath%>nursestation/nurseCharge/nursewz.action",
				 			        data:{stringNurseCharge:stringNurseCharge,recipeNo:recipeNo},
				 			        type:'post',
				 			        success:function(data){
				 			        	
				 			        }
	 		 	 				}); 
				        		return;
				        	}else if(dataMap.resCode=="arrearage"){
				        		$("#ym").layout('loaded');
				        		$('#stringNurseCharge').val(stringNurseCharge);
				        		AdddilogModel("arrearageInfo-window","","<%=basePath %>inpatient/exitNofee/arrearageInfo.action?inpatientInfo.inpatientNo="+dataMap.inpatientNo+"&thisTotCost="+dataMap.totCost+"&user.id="+dataMap.userId,'280px','370px');
							}else{
								$("#ym").layout('loaded');
								if(dataMap!=""&&dataMap!=null){
									var unName=dataMap.name;//库存不足的项目
									var drugname=dataMap.drugname;//药品库存不足项目
					        		var strs= new Array();
					        		strs=unName.split(",");//非药品name
					        		var strname= new Array();
					        		strname=drugname.split(",");
					        		if(unName!=null&&unName!=""){
					        			for(i=0;i<strname.length;i++){
					        				$.messager.alert("提示",strname[i],'info');
					        			}
					        		}
					        		
					        		var itemId=dataMap.itemId;//非药品id
					        		var medId=dataMap.medId;//药品id
				        			
					        		if(itemId!=""||medId!=""){
					        			/**收费后刷新页面
						        		*/
					        			$('#listzj').datagrid({
					        		    	url:"<%=basePath%>nursestation/nurseCharge/dayChargeById.action",
			        				        queryParams:{itemId:itemId,medId:medId},
			        				        method:"post"
					        			});
					        			    var itemIds= new Array();
					        			    itemIds=itemId.split(",");//非药品name
							        		var medIds= new Array();
							        		medIds=medId.split(",");//药品name
						        			//删除收费信息
							        		var dataArr = new Array();
							        		var en=$("#listyz").datagrid("getRows");
							        		var len = en.length;
									    	for(var i=0;i<en.length;i++){
									    		for(var a=0;a<itemIds.length;a++){
									    			if(en[i].id==itemIds[a]){
										    			dataArr[dataArr.length] = en[i];
									    			}
									    		}
									    		$("#listyz").datagrid("deleteRow",0);
											}
									    	for(var i=0;i<en.length;i++){
									    		for(var a=0;a<medIds.length;a++){
									    			if(en[i].id==medIds[a]){
										    			dataArr[dataArr.length] = en[i];
									    			}
									    		}
									    		$("#listyz").datagrid("deleteRow",0);
											}
									    	if(dataArr.length>0){
									    		for(var i=0;i<dataArr.length;i++){
									    			$('#listyz').datagrid('appendRow',dataArr[i]);
									    		}
									    	}  
					        		}
					        		
					        		
					        		//删除收费信息
					        		var dataArr = new Array();
					        		var en=$("#listyz").datagrid("getRows");
					        		var len = en.length;
							    	for(var i=0;i<len;i++){
							    		for(var a=0;a<strs.length;a++){
							    			if(en[0].undrugName==strs[a]){
								    			dataArr[dataArr.length] = en[0];
							    			}
							    		}
							    		$("#listyz").datagrid("deleteRow",0);
									}
							    	
							    	if(dataArr.length>0){
							    		for(var i=0;i<dataArr.length;i++){
							    			$('#listyz').datagrid('appendRow',dataArr[i]);
							    		}
							    	}
								}else{
									$("#ym").layout('loaded');
									$.messager.alert('提示','未知错误,请联系管理员!');
					        		return;
								}
				        		
				        	}	
	 					}
					});
	  		    }
	  		});
      }else{
      	$.messager.alert("提示","项目费用为零，不允许收费");
      }
	}else{
		$.messager.alert("提示","请加载收费信息");
	}
}
	  	
/**  
 *  
 * @Description：清屏按钮	
 * @Author：zhangjin
 * @CreateDate：2016-4-13
 * @param1:value(值) 
 * @param2:row(行)
 * @param3:row(索引)
 * @version 1.0
 * @throws IOException 
 *
 */ 
function clear(){
	$("#tmpMedicalNo").textbox("setValue","");
	$("#patientName").text("");
	$("#inDate").text("");
	$("#pactCode").text("");//合同单位代码
    $("#deptCode").text("");
	$("#emplCode").combobox("setValue","");//开方医生
	$("#emplCodeB").val("");//开方医生
	$("#paykindCode").val("");
	$("#pactCodeB").val("");
	$("#deptCodeB").val("");
	$("#babyFlag").val("");
	$("#moneyAlert").text("");
	$("#freeCost").text("");
	$('#editForm').form('reset');
	$("#total").textbox("setValue","");
	var yz=$("#listyz").datagrid("getRows");
	var yzdel=yz.length;
	for(var i=0;i<yzdel;i++){
		 index=$("#listyz").datagrid("getRowIndex",yzdel[0]);
		$("#listyz").datagrid('deleteRow',index);
	}
	var zj= $("#listzj").edatagrid("getRows")
	var row=zj.length;
	for(var a=0;a<row;a++){
		 indexa=$("#listzj").datagrid("getRowIndex",row[0]);
		$("#listzj").datagrid('deleteRow',indexa);
	}
		
}
function searchStackTreeNodes(){
	var searchText = $('#adStackTreeSearch').textbox('getValue');
	$("#tFt").tree("search", searchText);
}

function searchTreeNodes(){
    var searchText = $('#searchTreeInpId').textbox('getValue');
    var rooNode = $("#tDt").tree('getRoot');
    //调用expand方法
    $("#tDt").tree('expandAll',rooNode.target); 
    setTimeout(function () {
    		$("#tDt").tree("search", searchText);}
    		,300);
}

//渲染表单中的包装单位
function funPackUnit(value,row,index){
	if(row.minunit==value){
		if(row.drugType=='C'){
			if(unitMap[value]!=null&&unitMap[value]!=''){
				return unitMap[value];
			}else{
				return value;
			}
		}else{
			if(minUnitMap[value]!=null&&minUnitMap[value]!=''){
				return minUnitMap[value];
			}else{
				return value;
			}
		}
		
	}else{
		if(unitMap[value]!=null&&unitMap[value]!=''){
			return unitMap[value];
		}else{
			return value;
		}
	}
	
	
}

function funOncedosage(value,row,index){
	if(value==""){
		return null;
	}
	return value;
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
	function dateFormatter(value,row,index){
		if(value!=null&&value!=''){
			return value.replace(/-(\d{1})/g,'-0$1');
		}
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
#tt .tabs-panels,#tt .tabs-header{
	border-left:0
}
body,html{
	width: 100%;
	height: 100%;
}
*{
	padding: 0;
	margin: 0;
}
</style>
</head>
<body>

   <div class="easyui-layout" style="height: 100%;width: 100%" >   
	    <div id="buth" data-options="region:'north',title:'',split:false,border:false" style=" height:45px;padding-top: 8px;padding-left: 5px">
		    <shiro:hasPermission name="${menuAlias}:function:save">
		    		<a href="javascript:save();" class="easyui-linkbutton"  id="save" data-options="iconCls:'icon-money_yen',disabled:true">收&nbsp;费&nbsp;</a>
		    </shiro:hasPermission>
		    <shiro:hasPermission name="${menuAlias}:function:add">
					<a href="javascript:add();" class="easyui-linkbutton" id="add" data-options="iconCls:'icon-add',disabled:true">添&nbsp;加&nbsp;</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete">
					<a href="javascript:del();" class="easyui-linkbutton" id="del" data-options="iconCls:'icon-remove',disabled:true">删&nbsp;除&nbsp;</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:readCard"> 
					<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" data-options="iconCls:'icon-bullet_feed'">读&nbsp;卡&nbsp;</a>
			 </shiro:hasPermission>
	        <shiro:hasPermission name="${menuAlias}:function:readIdCard"> 
		        		<a href="javascript:void(0)"  class="easyui-linkbutton read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" cardNo="" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
			</shiro:hasPermission> 
					<a href="javascript:clear();" class="easyui-linkbutton" id="clear" data-options="iconCls:'icon-clear'">清&nbsp;屏&nbsp;</a>
					<input type="hidden" id="deptId" name="deptId" value="${deptId }"> 
	    </div>  
		<div id="p" data-options="region:'west',title:'护士站收费',split:true" style="width:310;height:100%;">
		           <div id="ee" class="easyui-layout" style="width:100%;height:100%;"  data-options="fit:true">
		            <div data-options="region:'north',iconCls:'icon-reload',split:false" style="width:300px;height:47px;border: 0;overflow-y:hidden;padding: 4px 0px 0px 3px;">
			    	<div style="padding: 0px;width:245px;height: 27px">
			    		<table style="width:265px;height:27px; ">
			    			<tr>
			    				<td> <input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'患者姓名'" />
			    				<shiro:hasPermission name="${menuAlias}:function:query"> 
  									 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" >搜索</a>
  									 </shiro:hasPermission>
									</td>
			    			</tr>
			    			
			    		</table>
			    	</div>
			    </div> 
		         <div data-options="region:'center',split:false,doSize:false"
					style="width: 100%;height:85%;border: 0;padding:0px;">
						<ul id="tDt">数据加载中...</ul>
				</div>
			</div>    
		</div>
	    <div data-options="region:'center'," style="background:#eee; padding-bottom: 5px">
			<div id="ym" class="easyui-layout" style="background:#eee;height: 100%;width: 100%;">
			        <div id="treeDiv" data-options="region:'west',split:true,tools:'#toolSMId',border:false" style="width:200px;">
				      	<div id="tt" class="easyui-tabs" data-options="fit:true">   
						    <div title="费用">   
								<ul id="tEt" class="easyui-tree"></ul> 
						    </div>   
						    <div title="组套信息" style="padding:0px;">  
						    	<div >
									<input class="easyui-textbox" id="adStackTreeSearch" data-options="buttonText:'查询',buttonIcon:'icon-search',prompt:'组套查询'" style="width:180px;" />
								</div> 
								<ul id="tFt" class="easyui-tree"></ul> 
						    </div>   
						</div>
				    </div>   
			      	<div data-options="region:'north',split:false,border:false" style="height: 170px;padding:5px">
			             <input type="hidden" id="uId" name="uId"><input type="hidden" id="paykindCode" name="paykindCode">
			             <input type="hidden" id="inState" name="inState"> <input type="hidden" id="priceForm" name="priceForm">
			             <input type="hidden" id="age" name="age"><input type="hidden" id="yefyis" name="yefyis">
			             <input type="hidden" id="pubRati" name="pubRati"><input type="hidden" id="stopAcount">
			             <input type="hidden" id="user" value="${userId}"><input type="hidden" id="inpatientNo" value="住院流水号">
			             <input id="babyFlag" type="hidden" value="是否婴儿">
			           <table id="list" class="honry-table" cellspacing="1" style="width:100%;height:160px;">
							<thead>
								<tr><th colspan="8" style="font-size: 30px" class="nurseCharge">患者信息</th></tr>
				    		</thead>
				    		<tr>
				    		     <td  class="honry-lable"  style="text-align:right; width: 10%;">病历号：</td>
				    			<td  style="width: 15%">
				    			    <input id="tmpMedicalNo" name="tmpMedicalNo" class="easyui-textbox" style="width:160px" data-options="prompt:'输入病历号回车查询'"  />
				    		   		 <input id="admNo1" name="medicalrecordId" type="hidden" />
				    		    </td>
				    			<td class="honry-lable"  style="font: 14px;text-align: right;width: 10%;">姓名：
				    			</td>
				    			<td id="patientName" style="width: 15%"></td>
				    			<td class="honry-lable" style="text-align: right;width: 10%;">入院时间：</td>
				    			<td id="inDate"style="width: 15%"></td>
				    			<td class="honry-lable"  style="font: 14px;text-align: right;width: 10%;">合同单位：<input id="pactCodeB" type="hidden" /></td>
				    			<td id="pactCode" style="width: 15%"> 
				    			</td>
				    		</tr>
				    		<tr>
				    			<td class="honry-lable" style="font: 14px;text-align: right;width: 10%;">住院科室：<input id="deptCodeB"  type="hidden" /></td>
				    			<td id="deptCode" style="width: 15%">
				    			</td>
				    			<td class="honry-lable" style="font: 14px;text-align: right;width: 10%;">开方医生：
				    			<input id="emplCodeB"  type="hidden" /> </td>
				    			<td  style="width: 15%"><input class="easyui-combobox" id="emplCode" name="emplCode" style="width:160px" data-options="required:true">
				    			</td>
				    			<td class="honry-lable" style="font: 14px;text-align: right;width: 10%;">警戒线：
				    			</td>
				    			<td id="moneyAlert" style="width: 15%">
				    			</td>
				    			<td class="honry-lable" style="font: 14px;text-align: right;width: 10%;">余额：
				    			</td>
				    			<td id="freeCost" style="width: 15%" >
				    			</td>
				    		</tr>
			      		     <tr>
								<td class="honry-lable" style="font-weight:bold;text-align: right; width: 10%"> 合计金额：</td>
								<td  colspan="7" style="width: 15%" ><input type= "text "  class="easyui-textbox" id="total" readonly="readonly" style="width:160px;text-align:right" ></td>
							</tr>
					      
			   		  </table>
			      </div>
			      <div data-options="region:'center',split:false,border:false" style="padding-right: 5px;">
			         <div id="dd" class="easyui-layout" style="height:100%;">  
					       <div data-options="region:'north',split:false" style="height:65%;width: 100%">
					         <input type="hidden" id="naId" name="naId">
							     <table id="listyz" class="easyui-edatagrid" style="height:100%;width: 100%;"   
		   												     data-options="fitColumns:true,singleSelect:true,checkOnSelect:true,selectOnCheck:false">
							   </table>                                                
							</div>   
							<div data-options="region:'center',border:false" style="width: 100%">
						    	<table id="listzj" class="easyui-datagrid" style="width: 100%" data-options="singleSelect:true,fit:true,fitColumns:true">
									<thead>
										 <tr>
											<th data-options="field:'patientName',align:'center'" style="width: 9%">姓名</th>
											<th data-options="field:'undrugName',align:'center'"  style="width: 15%">项目名称</th>
											<th data-options="field:'money',align:'right',halign:'center'"  style="width: 6%">单价</th>
											<th data-options="field:'amount',align:'right',halign:'center'"  style="width: 5%">数量</th>
											<th data-options="field:'unit',align:'center',formatter:funPackUnit"  style="width: 5%">单位</th>
											<th data-options="field:'moneyMount',align:'right',halign:'center'"  style="width: 8%">金额</th>
											<th data-options="field:'enseCost',align:'right',halign:'center'"  style="width: 8%">自费金额</th>
											<th data-options="field:'selfCost',align:'right',halign:'center'"  style="width: 8%">自付金额</th>
											<th data-options="field:'pubCost',align:'right',halign:'center'"  style="width: 8%">公费金额</th>
											<th data-options="field:'paykindCode',formatter:funcpaykind,align:'center'"  style="width: 9%">费别</th>
											<th data-options="field:'dept',align:'center'"  style="width: 8%">执行科室</th>
											<th data-options="field:'feeOpercode',formatter:functionfeecost,align:'center'"  style="width: 9%">计费人</th>
											<th data-options="field:'feeDate',align:'center',formatter:dateFormatter"  style="width: 8%">计费时间</th>
											<th data-options="field:'minunit',align:'center',hidden:true" style="width: 8%">最小单位</th>
										 </tr>
									</thead>
							     </table>
						    </div>   
				      </div>   
				</div>  
			</div> 
		</div> 
		<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:30 60 40 60" data-options="modal:true, closed:true">   
				<table id="infoDatagrid"  style="width:400px;height:400" data-options="fitColumns:true,singleSelect:true">   
				</table>  
		</div>
</div>
<input id="stringNurseCharge"  type="hidden" type="text"/>
<input id="arrearageId" type="hidden" value="hszsf"/> 
<input id=menu type="hidden" value='${menuAlias}'>
<div id="arrearageInfo-window"></div> 
</body>
</html>