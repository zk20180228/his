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
	<title>手术批费</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/system/css/loader.css">
</head>
<body >
<div class="easyui-layout" style="height: 100%;width: 100%;" id="ym">
	<div id="buth" data-options="region:'north',title:'',split:false" style="height:40px;padding-top: 5px;padding-left:5px;border-top:0">
		<shiro:hasPermission name="${menuAlias}:function:query">
		<a href="javascript:query();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:readCard">
				<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" data-options="iconCls:'icon-bullet_feed'">读卡</a>
		</shiro:hasPermission>
        <shiro:hasPermission name="${menuAlias}:function:readIdCard">
        		<a href="javascript:void(0)"  class="easyui-linkbutton read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" cardNo="" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias }:function:add">
			<a href="javascript:save();" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen'">收费</a>
		</shiro:hasPermission>
			<a href="javascript:del();" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
		<a href="javascript:clearQuery();" class="easyui-linkbutton" iconCls="reset">重置</a>
		<input type="hidden" id="deptId" name="deptId" value="${deptId }"> 
		<shiro:hasPermission name="${menuAlias }:function:print">
		 <a href="javascript:dayin('1');" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202'">打印</a> 
		 </shiro:hasPermission>
    </div>
    <div data-options="region:'west',split:true" style="width:270px;">
    	<div class="easyui-layout" style="height:100%;width:100%" data-options="fit:true">
    		<div data-options="region:'north',border:false" style="height:100px;width:290px;overflow-y:hidden;">
    			<div style="padding: 5px 5px 5px 5px;">
		    		<input type="hidden" id="arrangeNo">
		    		<input type="hidden" id="inpatientNo">
		    		<input type="hidden" id="opId">
		    		<input type="hidden" id="operationno">
		    		<table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin arrangeListSize">
		    			<tr>
		    				<td style="text-align: right;" nowrap="true">病 历 号：</td>
		    				<td><input id="medicalrecordId" class="easyui-textbox" style="width: 145px" data-options="prompt:'输入病历号回车查询'"/></td>
		    			</tr>
		    			<tr>
		    				<td style="text-align: right;" nowrap="true">开始时间：</td>
		    				<td>
		    				<input id="beganTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:145px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		    				</td>
		    			</tr>
		    			<tr>
		    				<td style="text-align: right;" nowrap="true">结束时间：</td>
		    				<td>
		    				<input id="endTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:145px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		    				</td>
		    			</tr>
		    		</table>
		    	</div>
    		</div>
	    	<div data-options="region:'center',border:false" style="width: 100%;height:85%;border: 0;padding:0px;">
	    		<div id="tDt"></div>
	    	</div>
    	</div>
    </div>
    <div data-options="region:'center'">
    	<div class="easyui-layout" style="height:100%;width:100%">
    		<div data-options="region:'north',border:false" style="height:138px;padding-top:5px;" align="center">
    			<div style="padding: 5px 5px 5px 5px;">
	    			<span><font class="title">手术收费通知单</font></span>
	    			<span style="width:10px;">&nbsp;</span>
	    			<span id="date"></span>
    			</div>
    			<div style="padding: 0px 5px 5px 5px;">
		    		<form id="hzxinxi">
			    		<table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin">
			    			<tr>
			    				<td style="text-align:right;width:10%">姓名：</td><td id="name" style="text-align:left;width:10%"></td>
			    				<td style="text-align:right;width:10%">性别：</td><td id="sex" style="text-align: left;width:10%"></td>
			    				<td style="text-align:right;width:10%">年龄：</td><td id="age" style="text-align: left;width:10%"></td>
			    				<td style="text-align:right;width:10%">科室：<input id="indept" type="hidden"></td><td id="dept" style="text-align: left;width:10%"></td>
			    				<td style="text-align:right;width:10%">住院号：</td><td id="no" style="text-align: left;width:10%"></td>
			    			</tr>
			    		</table>
		    		</form>
		    	</div>
		    	<div style="padding: 0px 5px 5px 5px;">
		    		<form id="sqxinxi">
			    		<table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin">
			    			<tr>
			    				<td style="text-align: right; width: 16%">手术医生：</td>
			    				<td style="text-align:left; width: 16%"><input class="easyui-combobox" id="operDoc" data-options="required:true" style="width: 160px"/></td>
			    				<td style="text-align: right; width: 16%">开立科室：</td>
			    				<td style="text-align:left; width: 16%"><input class="easyui-combobox" id="operDept" data-options="required:true" style="width: 160px"/></td>
			    				<td style="text-align: right; width: 16%"><font color="blue">结算类别：</font></td>
			    				<td id="paykindCode" style="text-align:left; width: 16%"></td>
			    			</tr>
			    		</table>
		    		</form>
		    	</div>
    		</div>
    		<div data-options="region:'center',border:false" style="width:100%;padding: 0px 5px 5px 5px;">
    			<div style="height:100%">
			    	<div class="easyui-layout" style="height:100%;">   
			   			<div data-options="region:'west',border:false,split:true" style="width:230px;">
			   				<div id="tt" class="easyui-tabs" data-options="fit:true">   
							    <div title="费用" style="padding-left:5px;padding-top:5px;">   
									<ul id="tEt" class="easyui-tree"></ul> 
							    </div>   
							    <div title="组套信息" style="padding-left:5px;padding-top:5px;">  
							    	<div>
										<input class="easyui-textbox" id="adStackTreeSearch" data-options="buttonText:'查询',buttonIcon:'icon-search',prompt:'组套查询'" style="width:180px;" />
									</div> 
									<ul id="tFt" class="easyui-tree"></ul> 
							    </div>   
							</div>
			   			</div>
			   			<div data-options="region:'center'">
				   				<input type="hidden" id="arrearageId" value="zysspf">
				   				<input type="hidden" id="stringNurseCharge">
								<table id="list" style="width:100%;border-left: 0px;" class="easyui-datagrid" data-options="checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,title:'收费列表',border:false">
									<thead>
										<tr>
											<th data-options="field:'ck',checkbox:true"></th>
											<th data-options="field:'undrugName',align:'center'"  style="width:30%" >项目名称</th>
											<th data-options="field:'money',align:'right',halign:'center'"  style="width:10%">价格</th>
											<th data-options="field:'amount',align:'right',halign:'center',editor : {type : 'numberbox',options:{validType:'valiAdjustNum'}}" style="width:10% " >数量</th>
											<th data-options="field:'unumber',align:'right',hidden:true,halign:'center'"  style="width:10%">付数</th>
											<th data-options="field:'unit',align:'center',formatter:functionUnit"   style="width:10% ">单位</th>
											<th data-options="field:'moneyMount',align:'right',halign:'center'"  style="width:15% ">合计金额</th>
											<th data-options="field:'depth',align:'center'"   style="width:15% ">执行科室</th>
											<th data-options="field:'id',hidden:true"  style="width:30%" >项目ID</th>
											<th data-options="field:'nid',hidden:true"  style="width:30%" >项目ID</th>
											<th data-options="field:'inpatientNo',hidden:true" style="width:30%" >住院流水号</th>
											<th data-options="field:'ty',hidden:true"  style="width:30%" >是否药品</th>
											<th data-options="field:'stackId',hidden:true"  style="width:30%" >组合号</th>
											<th data-options="field:'stackName',hidden:true"  style="width:30%" >组合name</th>
											<th data-options="field:'undrugMinimumcost',hidden:true"  style="width:30%" >最小代码</th>
											<th data-options="field:'deptCode',hidden:true"  style="width:30%" >开立科室</th>
											<th data-options="field:'emplCode',hidden:true"  style="width:30%" >开立医生</th>
											<th data-options="field:'dept',hidden:true"  style="width:30%" >执行科室</th>
											<th data-options="field:'itemCodeToGroup',hidden:true"  style="width:30%" >组套中包含的项目编码</th>
											<th data-options="field:'itemNameToGroup',hidden:true"  style="width:30%" >组套中包含的项目名称</th>
											<th data-options="field:'finalAmount',hidden:true"  style="width:30%" >最小单位对应数量</th>
											<th data-options="field:'packQty',hidden:true"  style="width:30%" >包装数量</th>
											<th data-options="field:'minUnit',hidden:true"  style="width:30%" >最小单位</th>
											<th data-options="field:'unitHidden',hidden:true"  style="width:30%" >当前单位</th>
											<th data-options="field:'extFlag',hidden:true"  style="width:30%" >开立单位</th>
										</tr>
									</thead>
								    <table class="honry-table" style="width:100%;border-top: 0px;border-left: 0px;">
								    	<tr>  
								    	 <td style="width:61%;text-align: right;font-weight:bold;border-top:0;border-left:0;">合计：</td>
								    	 <td style="width:15%;text-align: right; font-weight:bold;border-top:0" id="number" ></td>
								    	 <td style="border-top:0"></td>
								        </tr>
								    </table>   
								</table>
			   			</div> 
			    	</div>
		    	</div>
    		</div>
    	</div>
    </div>
    <div id="arrangeWindow" title="选择窗口" class="easyui-window" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closed:true" style="width:1000px;height:600px;">
    	<div id="tt" class="easyui-tabs" style="width:100%;height:100%;">   
		    <div title="未登记" style="width:100%;height:550px">   
		        <table id="arrangeNotList" style="width:100%;height:530px" class="easyui-datagrid" data-options="idField:'id',border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'id'"  style="width:35%" >编号</th>
							<th data-options="field:'opName1'" formatter="functionName"  style="width:35% ">手术名称</th>
							<th data-options="field:'preDate'"  style="width:13% ">预约时间</th>
							<th data-options="field:'opDoctor'" formatter="functionEmp"  style="width:13% ">手术医生</th>
						</tr>
					</thead>
				</table>
		    </div> 
		    <div title="已登记" style="width:100%;height:550px">   
		        <table id="arrangeList" style="width:100%;height:530px" data-options="border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'opName1'" formatter="functionName" style="width:35%" >手术名称</th>
							<th data-options="field:'opsDocd'" formatter="functionEmp" style="width:22% ">登记人</th>
							<th data-options="field:'operationdate'"  style="width:13% ">登记时间</th>
							<th data-options="field:'opsDocd'" formatter="functionEmp" style="width:13% ">手术医生</th>
						</tr>
					</thead>
				</table>      
		    </div>
		</div>  
    </div>
    <div id="operationRegistration" title='患者信息' class="easyui-window" align="center" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closed:true" style="width:700px;height:500px">
		<table id="dg" class="easyui-datagrid" data-options="fit:true">
			
		</table>  
</div>
<div id="operationRecipeNo" title='处方号信息' class="easyui-window" align="center" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closed:true" style="width:610px;height:350px">
		<table id="recipeNoTable" class="easyui-datagrid" data-options="fit:true" >
		</table>  
</div>
<div id="arrearageInfo-window"></div> 
</div>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>    
 <script type="text/javascript" src="<%=basePath%>javascript/js/easyui-layout-method-load.js"></script>  
<script type="text/javascript">
var deptMap = "";//渲染科室map
var unitMap ="";//单位map
var empMap ="";//员工map
var nameMap ="";//项目名称
var sexMap = new Map();//性别
var payCodeMap=new Map();//结算类别
$(function(){
	$("#adStackTreeSearch").textbox({onClickButton:function(){
		 searchStackTreeNodes();
	    }})
	   //数量变动以后动态计算列表金额
	 $.extend($.fn.validatebox.defaults.rules, {    
         valiAdjustNum: {    
                validator: function(value, param){
                	//获取药品非药品类别
                	 var ty = $.trim($(this).closest("td[field='amount']").siblings("td[field='ty']").children('div').html());
                	//获取单价
                    var money = +$.trim($(this).closest("td[field='amount']").siblings("td[field='money']").children('div').html());
                    var costall = Number(value)*Number(money);
                    //获取当前单位
                    var unitHidden = $.trim($(this).closest("td[field='amount']").siblings("td[field='unitHidden']").children('div').html());
                    //获取最小单位
                    var minUnit = $.trim($(this).closest("td[field='amount']").siblings("td[field='minUnit']").children('div').html());
                    //获取包装数量
                    var packagingnum = $.trim($(this).closest("td[field='amount']").siblings("td[field='packQty']").children('div').html());
        		    var  finalAmount=0;
                    if(ty==1){//药品
                    	 if(unitHidden==minUnit){//当前单位和最小单位相等
	          				   finalAmount=Number(value);
	          				   costall=Number(value)/Number(packagingnum)*Number(money);
	          			   }else{
	          				   finalAmount=Number(value)*Number(packagingnum);
	          				   costall=Number(value)*Number(money);
	          			   }
	                       $(this).closest("td[field='amount']").siblings("td[field='finalAmount']").children('div').text(((finalAmount==null)?0:finalAmount));
                    }else{//非药品
                 	   finalAmount=value;
         			   $(this).closest("td[field='amount']").siblings("td[field='finalAmount']").children('div').text(((value==null)?0:value));
                    }
                    //遍历每行数据计算合计
                    $(this).closest("td[field='amount']").siblings("td[field='moneyMount']").children('div').text(((costall==null)?0:costall).toFixed(2));
                    var ckArr = $("#list").datagrid('getPanel').find("td[field='moneyMount']");
                    var number = 0;
                    for(var i=1;i<ckArr.length;i++){
                    	var num= $(ckArr[i]).find('div').html();
                    	number += Number(num);
                    }
                    $('#number').html(number.toFixed(2));
                    return true;    
                }  
          }    
    });
	var myDate = new Date();
	$('#date').text("日期 ："+myDate.toLocaleDateString());
	bindEnterEvent('medicalrecordId',query,'easyui');//病历号绑定回车函数
	bindEnterEvent('adStackTreeSearch',searchStackTreeNodes,'easyui');//组套查询绑定回车函数
	/**
	 * @Description:结算类别
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */	
	$.ajax({
	    url:  "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
		type:'post',
		success: function(data) {
			var type = data;
			for(var i=0;i<type.length;i++){
				payCodeMap.put(type[i].encode,type[i].name);
			}
		}
	});
	/**
	 * @Description:性别
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */
	$.ajax({
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=sex ",
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	/**
	 * @Description:科室
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */
	$.ajax({
		url: '<%=basePath %>operation/arrangelist/findDeptMap.action', 
		type:'post',
		success: function(deptData) {
			deptMap = deptData;
		}
	});
	/**
	 * @Description:单位
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */
	$.ajax({
		url: '<%=basePath %>operation/arrangelist/findUnitMap.action', 
		type:'post',
		success: function(unitData) {
			unitMap =unitData;
		}
	});
	/**
	 * @Description:项目名称
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */
	$.ajax({
		url: '<%=basePath %>operation/arrangelist/findNameMap.action', 
		type:'post',
		success: function(nameData) {
			nameMap = nameData;
		}
	});
	/**
	 * @Description:员工
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */
	$.ajax({
		url: '<%=basePath %>operation/arrangelist/findEmpMap.action', 
		type:'post',
		success: function(empData) {
			empMap = empData;
		}
	});
	$("#list").datagrid({
		onDblClickRow:function(rowIndex, rowData){
            var rows = $(this).datagrid('getRows');
            if(rows!=null&&rows.length>0){
                     for(var i=0;i<rows.length;i++){
                           $(this).datagrid('endEdit', $(this).datagrid('getRowIndex',rows[i]));
                     }
            }
            $(this).datagrid('beginEdit',rowIndex);
	    },
	    onAfterEdit:function(rowIndex, rowData, changes){
	            var fstoreNum = rowData.money;
	            var retailPrice = rowData.amount;
	            var costall = Number(fstoreNum)*Number(retailPrice);
	            var finalAmount=Number(rowData.packQty)*Number(retailPrice);
	            $(this).datagrid('updateRow',{
	                     index: rowIndex,
	                     row: {
	                    	 amount:retailPrice,
	                    	 moneyMount:costall,
	                    	 finalAmount:finalAmount
	                     }
	             });
	      },
	});
	
	/**
	 * @Description:手术医生
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */	
	$('#operDoc').combobox({    
		url : '<%=basePath %>operation/operationList/ssComboboxList.action',
		valueField : 'jobNo',
		textField : 'name',
		multiple:false,
		editable : true,
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
		filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'jobNo';
			keys[keys.length] = 'code';
			keys[keys.length] = 'name';
			keys[keys.length] = 'pinyin';
			keys[keys.length] = 'wb';
			keys[keys.length] = 'inputCode';
			return filterLocalCombobox(q, row, keys);
		}
	});
	/**
	 * @Description:科室
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */	
	$('#operDept').combobox({    
		url:"<%=basePath%>operation/arrangelist/findDept.action",
	    valueField:'deptCode',    
	    textField:'deptName',
	    onHidePanel:function(none){
		    var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].deptCode) {
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
		filter:function(q,row){
			 var keys = new Array();
			 keys[keys.length] = 'deptCode';//部门code
			 keys[keys.length] = 'deptName';//部门名称
			 keys[keys.length] = 'deptPinyin';//部门拼音
			 keys[keys.length] = 'deptWb';//部门五笔
		     keys[keys.length] = 'deptInputCode';//自定义码
			return filterLocalCombobox(q, row, keys);
		}   
	});  
	function filterLocalCombobox(q, row, keys){
		if(keys!=null && keys.length > 0){//
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
	 * @Description:加载手术患者信息树
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */	
		$('#tDt').tree({ 
			url:"<%=basePath%>operation/registration/treeList.action?inde="+1,
		    method:'post',
		    animate:true,  //点在展开或折叠的时候是否显示动画效果
		    lines:true,    //是否显示树控件上的虚线
		    state:'closed',//节点不展开
		    onBeforeLoad:function(node,param){
 		    	if(node!=null){
 		    		var node1=$('#tDt').tree('getParent',node.target);
 			    	param.pid=node1.id
 		    	}
             },
             onBeforeCollapse:function(node){
            	 if(node.id==1){
            		 return false;
            	 }
             },
		    formatter:function(node){//统计节点总数
				var s = node.text;
				  if (node.children){
					  if(node.children.length>0){
						  s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					  }
				}  
				return s;
			},onDblClick: function(node){//点击节点
				clearItem();//hedong 删除收费条目信息  防止在收取其他患者信息的费用条目后再次点击其他患者时收取错误的费用条目
				var parentNode=$('#tDt').tree('getParent',node.target);
				if(parentNode.id!=1){
					if(parentNode.attributes.pid=='noregister'){//未登记
						$('#opId').val(node.id);
						$('#patientNo').val(node.attributes.patientNo);
						if(node.attributes.pasource==1){
							$.messager.alert("提示","该患者是门诊患者，请到门诊收费处进行收费。");
							return ;
						}
						$("#operationno").val(node.id);
						//未登记信息查询
						OperationapplyData(node.id,node.attributes.pasource,node.attributes.clinicCode);
					}else if(parentNode.attributes.pid=='register'){ //已登记或作废 ,
						$('#id').val(node.id);
						$('#opId').val(node.attributes.operationId);
						$('#patientNo').val(node.attributes.patientNo);
						$("#operationno").val(node.attributes.operationId);
						if(node.attributes.pasource==1){
							$.messager.alert("提示","该患者是门诊患者，请到门诊收费处进行收费。");
							return ;
						}
						//已登记信息查询
						ReCordData(node.id,node.attributes.pasource,node.attributes.clinicCode);
					}
				}
			},onClick: function(node){
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			}
		}); 
	
	/**
	 * @Description:加载最小费用与统计大类信息
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */	
	$('#tEt').tree({ 
		url:"<%=basePath%>operation/arrangelist/treeFeeStataCode.action",
		method:'post',
		state: 'closed',
		animate:true,  //点在展开或折叠的时候是否显示动画效果
		lines:true,    //是否显示树控件上的虚线
		onDblClick: function(node){//点击节点
			var opId=$("#opId").val();
			if(opId==null||opId==""){
				$.messager.alert("友情提示","请选择患者");
				return ;
			}
			var id=node.id;
			var inpatientNo=$("#inpatientNo").val();
			var operDept=$("#operDept").combobox("getValue");
			var emplCode=$("#operDoc").combobox("getValue");
			$.ajax({
				url: '<%=basePath %>operation/arrangelist/queryUndurg.action',
				data:{id:id},
				type:'post',
				success: function(data) {
						deptObj = data;
					if(deptObj.code){
						if(deptObj==null||deptObj==""){
							$.messager.alert("操作提示","该项目已停止使用，请重新选择");
						}
						 var dept;//执行科室
						var indept=$("#indept").val();
					    if(deptObj.undrugDept!=""&&deptObj.undrugDept!=null){
					    	dept=deptObj.undrugDept;
					    }else{
					 		dept=indept;
					    }
					    var deptName='';//科室
					    deptName=deptMap[dept];
						$('#list').datagrid('appendRow',{
								undrugName: deptObj.name,
								money: deptObj.defaultprice,
								amount:1,
								unumber:1,
								unit:deptObj.unit,
								moneyMount:deptObj.defaultprice,
								dept:dept,
								depth:deptName,//执行科室name
								id:deptObj.code,
								inpatientNo:inpatientNo,
								ty:2,
								undrugMinimumcost:deptObj.undrugMinimumcost,//最小费用代码
								emplCode:emplCode,//开方医生
								deptCode:operDept,
								finalAmount:1,
								nid:node.attributes.itemCode
								
						});
						editIndex = $("#list").edatagrid('getRows').length-1;
		   	 	 	    $("#list").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
		   	 	 		$("#list").datagrid('endEdit', editIndex);
						jsymmoney();//计算页面项目金额
					}
				}
					
			});
		},onClick: function(node){
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		}
	});
	
	/**
	 * @Description:加载组套信息
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @version: 1.0
	 */	
	$('#tFt').tree({ 
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
				var opId=$("#opId").val();
				if(opId==null||opId==""){
					$.messager.alert("提示","请选择患者");
					return ;
				}
				var id = node.id;
				stackName=node.text;//组套name
				businessStack(id); 
			 },onClick: function(node){
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			 }
	});
	
	/**  
	 *  
	 * @Description：组套详细信息
	 * @Author：zhangjin
	 * @CreateDate：2016-5-7
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：   
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
function businessStack(id){
		var inpatientNo=$("#inpatientNo").val();
		var operDept=$("#operDept").combobox("getValue");//科室
		var emplCode=$("#operDoc").combobox("getValue");//开立医生
			$.ajax({
				url: "<%=basePath%>outpatient/updateStack/getStackInfoByInfoIdForView.action",
   				data:{infoId:id,feelType:"1"},
   				type:"post",
   				success: function(data) {
   					if(data!=null&&data!=""){
   						for(var i=0;i<data.rows.length;i++){
   							var amount;
   							var money=data.rows[i].drugRetailprice;//单价
   							if(data.rows[i].stackInfoNum!=null&&data.rows[i].stackInfoNum!=""){
   								amount=data.rows[i].stackInfoNum;//数量
   							}else{
   								amount=1;
   							}
   	   					    var moneyMount=Number(money)*Number(amount);//合计
	   	   					var stackInfoItemId=data.rows[i].stackInfoItemId;
   	   					    var dept;//执行科室
   	   					    
   	   						var indept=$("#indept").val();
   	   						if(data.rows[i].stackInfoDeptId!=null&&data.rows[i].stackInfoDeptId!=''){
   	   							dept=data.rows[i].stackInfoDeptId;
			            	}else{
			            		dept=indept;//hedong 20170321 若无执行科室则为患者住院科室
			            	}
   	   						var combNo;//组合号
	   						if(data.rows[i].combNo!=null&&data.rows[i].combNo!=""){
	   						   combNo=data.rows[i].combNo;//组合号
	   						}else{
	   							combNo="";
	   						}
		   					 var days;
		   					 if(data.rows[i].days!=""&&data.rows[i].days!=null){
	   	   						days=data.rows[i].days;
	   	   				    }else{
	   	   						days=1;
	   	   				    }
		   					var finalAmount=0;
		   					var extFlag="1";
		   					var type=data.rows[i].drugSystype // 是中草药
		   					var deptName='';//科室
		   					deptName=deptMap[dept];
	   	   				    if(data.rows[i].ty=='1'){
		   	   					if(data.rows[i].unit==data.rows[i].stackInfoUnit){
	   	   				    		extFlag="2";
	   	   				    		if(type==16){
		   	   				    		finalAmount=Number(days);
		   	   				    		moneyMount=(Number(days)/Number(data.rows[i].packagingnum))*Number(money).toFixed(4);
		   	   				    	    
		   	   				    	}else{
			   	   				       finalAmount=Number(amount);
			   	   				 	   moneyMount=(Number(amount)/Number(data.rows[i].packagingnum))*Number(money).toFixed(4);
		   	   				    	}
	   	   				    	}else{
	   	   				    		extFlag="1";
		   	   				    	if(type==16){
		   	   				    		finalAmount=Number(days)*Number(data.rows[i].packagingnum);
		   	   				    	}else{
			   	   				       finalAmount=Number(amount)*Number(data.rows[i].packagingnum);
		   	   				    	}
	   	   				    	}
	   	   				    }else{
	   	   				       finalAmount=amount;
	   	   				    }
		   	   					$("#list").edatagrid('appendRow',{
		   	   						undrugName:data.rows[i].name ,
		   	   						money:money,
									amount:amount,
									unumber:days,
									unit:data.rows[i].stackInfoUnit,
									moneyMount:moneyMount.toFixed(4),
									dept:dept,
									depth:deptName,//执行科室ID
									id:data.rows[i].id,
									nid:data.rows[i].id,
									inpatientNo:inpatientNo,
									ty:data.rows[i].ty,
									emplCode:emplCode,//开方医生
									deptCode:operDept,
									stackName:stackName,//组套名字
									stackId:id,//组合号
									itemCodeToGroup:data.rows[i].code,
									itemNameToGroup:data.rows[i].name,
									finalAmount:finalAmount,
									minUnit:data.rows[i].unit,
									packQty:data.rows[i].packagingnum,
									extFlag:extFlag,
									unitHidden:data.rows[i].stackInfoUnit,
								 });
    					     	jsymmoney();//计算页面项目金额
   						}
   					}else{
   						$.messager.alert("提示","未查询到该组套信息");
   					}
   				}
			 });
}
});


/**
 * @Description:报表
 * @Author: zhangjin
 * @CreateDate: 2016年5月7日
 * @version: 1.0
 */	
function dayin (recipeNo){
	var no = $("#operationno").val().trim();
	if(!no){
		$.messager.alert("提示","请选择患者！");
		return ;
	}
	if(recipeNo=='1'){
		$("#ym").layout('loading');
		$('#recipeNoTable').datagrid({ 
			    pageSize:10,
			 	pageList:[10,20,30,50,80,100],
				pagination:true,
    		    url:'<%=basePath%>operation/arrangelist/queryRecipeNo.action?opId='+no,
    		    columns:[[    
    				        {field:'name',title:'姓名',width:160,align:'center',resizable:true},
    				        {field:'recipeNo',title:'处方号',width:230,align:'center',resizable:true},
    				        {field:'feeDate',title:'计费日期',width:200,align:'center',resizable:true,sortable:true}
    				    ]],
    		    onDblClickRow:function(index,row){
    		    	$("#ym").layout('loaded');
    		    	$('#operationRecipeNo').window('close');
    		    	recipeNo=row.recipeNo;
    		    	var timerStr = Math.random();
    		    	//结算类别
    		    	$.ajax({
    		    	    url:  "<%=basePath%>operation/arrangelist/queryTotCost.action?",
    		    	    type: "post",
    		    	    data: { opId: no, recipeNo:recipeNo},
    		    		success: function(data) {
    		    			window.open ("<c:url value='/iReport/iReportPrint/iReportToArrangeList.action?randomId='/>"+timerStr+"&opid="+no+"&totCost="+data.totCost.toFixed(2)+"&recipeNo="+recipeNo+"&fileName=SSSFTZD",'newwindow'+no,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
    		    		}
    		    	});
    				
    		    },
    		    onLoadSuccess:function(data){
    		    	$("#ym").layout('loaded');
		    		     $('#operationRecipeNo').window('open');
		    		 	//分页工具栏作用提示
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
	    		    },onLoadError:function(none){
	    		    	$.messager.alert("提示","未查到该患者信息");
	    		    	$("#ym").layout('loaded');
	    		    }
    		});
	}else{
		var timerStr = Math.random();
		//结算类别
    	$.ajax({
    	    url:  "<%=basePath%>operation/arrangelist/queryTotCost.action?",
    	    type: "post",
    	    data: { opId: no, recipeNo:recipeNo},
    		success: function(data) {
    			window.open ("<c:url value='/iReport/iReportPrint/iReportToArrangeList.action?randomId='/>"+timerStr+"&opid="+no+"&totCost="+data.totCost.toFixed(2)+"&recipeNo="+recipeNo+"&fileName=SSSFTZD",'newwindow'+no,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
    		}
    	});
	}
	
}



/**  
 *  
 * @Description：渲染科室
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
function functionDept(value,row,index){
	if(value!=null&&value!=''){
		return deptMap[value];
	}
}
/**  
 *  
 * @Description：渲染单位
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
function functionUnit(value,row,index){
	if(value!=null&&value!=''){
		if(!unitMap[value]){
			return value;
		}
		return unitMap[value];
	}else{
		return "";
	}
}
/**  
 *  
 * @Description：渲染项目名称
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
function functionName(value,row,index){
	if(value!=null&&value!=''){
		return nameMap[value];
	}
}
/**  
 *  
 * @Description：渲染手术医生
 * @Author：zhangjin
 * @CreateDate：2017-2-17
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
 * @Description：删除操作
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
function del(){
	var row=$("#list").datagrid("getChecked");
	for(var i=0;i<row.length;i++){
		$("#list").datagrid("deleteRow",$("#list").datagrid("getRowIndex",row[i]));
	}
	jsymmoney();//计算页面项目金额
	$("#list").datagrid("clearChecked");
}

/**  
 *  
 * @Description：保存	
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
function save(){
	var no = $('#arrangeNo').val();
	if(no==null||no==""){
		$.messager.alert("操作提示","请先录入患者信息");
		return;
	}
	var opId= $("#opId").val();//手术序号
	var rows = $("#list").datagrid('getRows');
	if(rows.length<=0){
		$.messager.alert("操作提示","请添加收费项目");
		return;
	}
	$.messager.confirm('提示','您确认要收费吗？',function(r){ 
		if(r){
            for(var i=0;i<rows.length;i++){
                  $("#list").datagrid('endEdit', $("#list").datagrid('getRowIndex',rows[i]));
            }
			var stringNurseCharge = JSON.stringify($("#list").edatagrid("getRows"));
			$.messager.progress({text:'收费中，请稍后...',modal:true});
			$.ajax({
			    	  url:"<%=basePath%>operation/arrangelist/zysspf.action",
			        data:{stringNurseCharge:stringNurseCharge,opId:opId},
			        type:'post',
					success: function(data) {
						$.messager.progress('close');
						var dataMap = data;
			   			if(dataMap.resCode=="error"){
			   				$.messager.alert('提示',dataMap.resMsg);			   				
			        		return;
			        	}else if(dataMap.resCode=="success"){
			        		var recipeNo=dataMap.recipeNo3;//处方号
			        		$("#number").html("");
			        		$.messager.alert('提示',dataMap.resMsg);
			        		var recipeNo=dataMap.recipeNo2;//处方号
			        		var unName=dataMap.name;//库存不足的项目
			        		var drugname=dataMap.drugname;//药品库存不足项目
			        		var strs= new Array();
			        		if(unName){
			        			strs=unName.split(",");//非药品name
			        		}
			        		var strname= new Array();
			        		if(drugname){
			        			strname=drugname.split(",");
			        		}
			        		if(unName!=null&&unName!=""){
			        			$.messager.alert("提示",strname[i],'info');
			        			//删除收费信息
				        		var dataArr = new Array();
				        		var en=$("#list").datagrid("getRows");
				        		var len = en.length;
			        			for(var i=0;i<len;i++){
						    		for(var a=0;a<strs.length;a++){
						    			if(en[0].undrugName==strs[a]){
							    			dataArr[dataArr.length] = en[0];
						    			}
						    		}
						    		$("#list").datagrid("deleteRow",0);
								}
						    	if(dataArr.length>0){
						    		for(var i=0;i<dataArr.length;i++){
						    			$('#list').datagrid('appendRow',dataArr[i]);
						    		}
						    	}
			        		}else{
			        			//删除所有行
			        			var no=$("#list").datagrid("getRows");
			        			var row=no.length;
			        			for(var i=0;i<row;i++){
			        				$("#list").datagrid("getRowIndex",row[i]);
			        				$("#list").datagrid("deleteRow",0);
			        			}
			        		}
			        		
			        		 /**
			 				 * 回写手术序号和并且改为收费标记
			 				 * @Author: zhangjin
			 				 * @CreateDate: 2017年2月17日
			 				 * @param:recipeNo住院流水号 opId 手术序号
			 				 * @return:
			 				 * @version: 1.0
			 				 */
			 			   $.ajax({
			 			        url:"<%=basePath%>operation/arrangelist/writeOperationId.action",
			 			        data:{recipeNo:recipeNo,opId:opId},
			 			        type:'post',
			 				}); 
			        		
			 			  $.messager.confirm('提示','是否打印手术室收费通知单？',function(r){ 
			 				  if(r){
			 					 dayin(recipeNo);
			 				  }
			 			   });
			        		/**
			 				 * 药品出库申请
			 				 * @Author: zhangjin
			 				 * @CreateDate: 2017年2月17日
			 				 * @param:
			 				 * @return:
			 				 * @version: 1.0
			 				 */
			        		$.ajax({               
		 	 					 url:"<%=basePath%>operation/arrangelist/nurseDrug.action",
				 			        data:{stringNurseCharge:stringNurseCharge,recipeNo:recipeNo},
				 			        type:'post',
		 	 				  });
			        		/**
			 				 * 物资出库申请
			 				 * @Author: zhangjin
			 				 * @CreateDate: 2017年2月17日
			 				 * @param:
			 				 * @return:
			 				 * @version: 1.0
			 				 */
			 			    $.ajax({
			 			        url:"<%=basePath%>operation/arrangelist/nursewz.action",
			 			        data:{stringNurseCharge:stringNurseCharge,recipeNo:recipeNo},
			 			        type:'post',
			 				});
			        		return;                
			        	}else if(dataMap.resCode=="arrearage"){
			        		$('#stringNurseCharge').val(stringNurseCharge);
			        		//费用金额不足页面
			        		AdddilogModel("arrearageInfo-window","","<%=basePath%>inpatient/exitNofee/arrearageInfo.action?operationId="+opId+"&inpatientInfo.inpatientNo="+dataMap.inpatientNo+"&thisTotCost="+dataMap.totCost+"&user.id="+dataMap.userId,'300px','370px;');
									  
						}else{
							if(dataMap!=""&&dataMap!=null){
								var unName=dataMap.name;//库存不足的项目
								var drugname=dataMap.drugname;//药品库存不足项目
				        		var strs= new Array();
				        		strs=unName.split(",");
				        		var strname= new Array();
				        		strname=drugname.split(",");
				        		if(unName!=null&&unName!=""){
				        			for(i=0;i<strname.length;i++){
					        				$.messager.alert("提示",strname[i],'info');
					        			}
				        			//删除收费信息
					        		var dataArr = new Array();
					        		var en=$("#list").datagrid("getRows");
					        		var len = en.length;
				        			for(var i=0;i<len;i++){
							    		for(var a=0;a<strs.length;a++){
							    			if(en[0].undrugName==strs[a]){
								    			dataArr[dataArr.length] = en[0];
							    			}
							    		}
							    		$("#list").datagrid("deleteRow",0);
									}
							    	
							    	if(dataArr.length>0){
							    		for(var i=0;i<dataArr.length;i++){
							    			$('#list').datagrid('appendRow',dataArr[i]);
							    		}
							    	}
				        		}else{
				        			del();
				        		}
							}else{
								$.messager.alert('提示','未知错误,请联系管理员!');
				        		return;
							}
			        		
			        	}	
					}
				});
		}
	});
	
}
/**
 * @Description:条件查询手术信息
 * @Author: zhangjin
 * @CreateDate: 2016年5月7日
 * @version: 1.0
 */	
function query(){
	var showStatus = new Map();
	showStatus.put(1,"已申请");
	showStatus.put(2,"已审批");
	showStatus.put(3,"已安排");
	showStatus.put(4,"已完成");
	showStatus.put(5,"已作废");
	var inputmessage = "";
	 var patientNo=$('#medicalrecordId').textbox('getText').trim();
	 var beganTime = $('#beganTime').val().trim();
	 var endTime = $('#endTime').val().trim();
	 if(!patientNo){
		 $.messager.alert("提示", "请输入正确的病历号");
		 return ;
	 }
	 if(beganTime!=""&&endTime!=""&&endTime<beganTime){
		 $.messager.alert("提示","结束时间不能小于开始时间");
		 return ;
	 }
	 $("#ym").layout('loading');
				 $('#dg').datagrid({ 
					pageSize:10,
 					pageList:[10,20,30,50,80,100],
 					pagination:true,
		    		    url:'<%=basePath%>operation/arrangelist/queryPatient.action?patientNo='+patientNo+'&beganTime='+beganTime+'&endTime='+endTime+"&index=pifei",
		    		    columns:[[    
		    				        {field:'opID',title:'手术序列号',width:100,align:'center',hidden:true},    
		    				        {field:'name',title:'姓名',width:100,align:'center',resizable:true},
		    				        {field:'itemName',title:'手术名称',width:230,align:'center',resizable:true},
		    				        {field:'employeeName',title:'手术医生',width:100,align:'center',resizable:true},
		    				        {field:'predate',title:'预约时间',width:150,align:'center',resizable:true,sortable:true},    
		    				        {field:'status',title:'状态',width:100,align:'center',resizable:true,
		    				        	formatter: function(value,row,index){//状态显示值
		    		    					if (value){
		    		    						return value;
		    		    					} else {
		    		    						return "无状态";
		    		    					}
		    		    				}
		    						},    
		    				        {field:'patientNO',title:'病例号',width:100,align:'center',hidden:true}    		            
		    				    ]],
		    		    onDblClickRow:function(index,row){
		    		    	$("#ym").layout('loaded');
		    		    	clearItem();//hedong 20170321 清空收费信息 防止收取其他患者的费用信息
		    		    	if(row.status=='未登记'){//未登记回显
		    		    		$("#dengji").val("noregister");
		    		    		$("#opId").val(row.opID);
		    		    		$('#operationRegistration').window('close');
		    		    		if(row.pasource==1){
		    		    			$("#opId").val("");
									$.messager.alert("提示","该患者是门诊患者，请到门诊收费处进行收费。");
									return ;
								}
		    		    		//未登记查询方法
		    		    		OperationapplyData(row.opID,row.pasource,row.clinicCode);
		    		    		$("#operationno").val(row.operationId);
		    		    		var id=row.opID;
   		    		    		var deptCode=row.deptCode;
   		    		    		var node = $('#tDt').tree('find',"noregister");
   		    		    		$("#tDt").tree("expand",node.target);
   		    		    		setTimeout(function(){
       		    		    		if(deptCode!=null){
       		    		    			var node2 = $('#tDt').tree('find',deptCode);
	       		    		    		$("#tDt").tree("expand",node2.target);
       		    		    		}
   		    		    		},1500);
   		    		    		setTimeout(function(){
       		    		    		var node3=$("#tDt").tree('find',id);
	       		    		    	$("#tDt").tree("scrollTo",node3.target);
       		    		    		$("#tDt").tree('select',node3.target);
   		    		    		},3000);
		    		    		
		    		    	}else if(row.status=='已登记'){//已作废或已登记回显
		    		    		$("#dengji").val("register");
		    		    		$("#opId").val(row.operationId);
		    		    		$('#operationRegistration').window('close');
		    		    		if(row.pasource==1){
		    		    			$("#opId").val("");
									$.messager.alert("提示","该患者是门诊患者，请到门诊收费处进行收费。");
									$('#operationRegistration').window('close');
									return ;
								}
		    		    		//已登记查询方法
		    		    		ReCordData(row.opID,row.pasource,row.clinicCode);
		    		    		$("#operationno").val(row.operationId);
		    		    		var id=row.opID;
   		    		    		var deptCode=row.deptCode;
   		    		    		var node = $('#tDt').tree('find',"register");
   		    		    		$("#tDt").tree("expand",node.target);
   		    		    		setTimeout(function(){
       		    		    		if(deptCode!=null){
       		    		    			var node2 = $('#tDt').tree('find',deptCode);
	       		    		    		$("#tDt").tree("expand",node2.target);
       		    		    		}
   		    		    		},1500);
   		    		    		setTimeout(function(){
       		    		    		var node3=$("#tDt").tree('find',id);
	       		    		    	$("#tDt").tree("scrollTo",node3.target);
       		    		    		$("#tDt").tree('select',node3.target);
   		    		    		},3000);
		    		    	}else if(row.status=='已作废'){
		    		    		if(row.pasource==1){
		    		    			$("#opId").val("");
									$.messager.alert("提示","该患者是门诊患者，请到门诊收费处进行收费。");
									$('#operationRegistration').window('close');
									return ;
								}
		    		    		$.messager.alert("提示","该手术已作废，不可以进行收费。");
		    		    		$('#operationRegistration').window('close');
		    		    	}
		    		    },onLoadSuccess:function(data){
		    		    	$("#ym").layout('loaded');
      		    		     $('#operationRegistration').window('open');
      		    		 	//分页工具栏作用提示
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
   		    		    },onLoadError:function(none){
   		    		    	$.messager.alert("提示","未查到该患者信息");
   		    		    	$("#ym").layout('loaded');
   		    		    }
		    		});
}
/**
 * @Description:清空查询条件
 */	
	function clearQuery(){
		 $("#medicalrecordId").textbox("clear");
		 $("#beganTime").val("");
		 $("#endTime").val("");
		 //hedong 20170321 清空查询条件外的其他信息
		 qingkong();
		 $("#hzxinxi").form('clear');//姓名  性别...
		 $("#sqxinxi").form('clear');//手术医生  开立科室..
		 //收费列表信息清空
		 clearItem();
		 //清空手术id 防止在没有选择手术的情况下添加收费条目从而带入空的执行科室
		 $("#opId").val("");
 	}
	/**
	 * @Description:hedong 20170321 清空收费列表信息
	 */	
 	function clearItem(){
 		 var item = $('#list').datagrid('getRows');  
         if (item) {  
             for (var i = item.length - 1; i >= 0; i--) {  
                 var index = $('#list').datagrid('getRowIndex', item[i]);  
                 $('#list').datagrid('deleteRow', index);  
             }  
         }
         //费用合计
         $('#number').html("");
 	}
 
/**  
 *  
 * @Description：计算页面金额
 * @Author：zhangjin
 * @CreateDate：2016-5-7
 * @Modifier：
 * @ModifyDate：  
 * @ModifyRmk：   
 * @version 1.0
 *
 */
	function jsymmoney(){
		var amd=$("#list").datagrid("getRows");
		var num=0;
		for(var i=0;i<amd.length;i++){
			num =Number(num)+Number(amd[i].moneyMount);
		}
		$("#number").text(num.toFixed(2));
	}
	//清空页面
	function qingkong(){
		$("#hzxinxi").form("reset");
		$('#age').text("");
		$('#name').text("");
		$('#sex').text("");
	    $('#no').text("");
	    $("#dept").text("");
		$('#paykindCode').text("");
	}
	
	
	/**
	 * @Description:已登记信息查询
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */	
	function ReCordData(id,pasource,clinicCode){
		if(pasource!=""&&pasource!=null){
			qingkong();
			 huanzhexinxi(clinicCode,pasource);
			 $("#hzxinxi").form('clear');
			 $("#sqxinxi").form('clear');
			$.ajax({
				url:"<%=basePath%>operation/registration/queryOperationRecordInfoList.action",
				type:'post',
				data:{opapId:id},
				success:function(data){
					if(data!=null){
						$('#name').text(!data.name?"":data.name);
						if(data.sexCode){
							$('#sex').text(sexMap.get(data.sexCode));
						}
					    $('#no').text(!data.patientNo?"":data.patientNo);
					    $("#inpatientNo").val(!data.clinicCode?"":data.clinicCode);
					    if(data.deptCode){
					    	 $("#dept").text(deptMap[data.deptCode]);
					    }
					    $("#indept").val(!data.deptCode?"":data.deptCode);
					    $("#operDoc").combobox('select',!data.opDoctor?"":data.opDoctor);
					    $("#operDept").combobox('select',!data.docDpcd?"":data.docDpcd);
						$('#name').text(data.name);
						$('#sex').text(sexMap.get(data.sexCode));
					    $('#no').text(data.patientNo);
					    $("#inpatientNo").val(data.clinicCode);
					    $("#dept").text(deptMap[data.deptCode]);
					    $("#indept").val(data.deptCode);
					    $("#operDoc").combobox('select',data.opDoctor);
					    $("#operDept").combobox('select',data.docDpcd);
					    $("#arrangeNo").val(data.id);
					    
					}
					
				}
			});
		 }else{
			 $.messager.alert("提示","该患者数据有问题");
		 }
		
	}
	/**
	 * @Description:未登记信息查询 node.attributes.pid=='noregister' 未登记
	 * @Author: zhangjin
	 * @CreateDate: 2016年5月7日
	 * @param:
	 * @return:id=手术序号 pasource=来源 clinicCode=住院流水号
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function OperationapplyData(id,pasource,clinicCode){
		 if(pasource!=""&&pasource!=null){
			 qingkong();
			 huanzhexinxi(clinicCode,pasource);
			 $("#hzxinxi").form('clear');
			 $("#sqxinxi").form('clear');
			 $.ajax({
					url:"<%=basePath%>operation/operationList/queryOperationShoushu.action",
					type:'post',
					data:{opId:id,medicalrecordId:clinicCode},
					success:function(data){
						if(data!=null&&data!=""){
							$('#name').text(!data[0].name?"":data[0].name);
							if(data[0].sex){
								$('#sex').text(sexMap.get(data[0].sex));
							}
						    $('#no').text(!data[0].patientNo?"":data[0].patientNo);
						    $("#inpatientNo").val(!data[0].clinicCode?"":data[0].clinicCode);
						    if(data[0].inDept){
						    	 $("#dept").text(deptMap[data[0].inDept]);
						    }
						   
						    $("#indept").val(!data[0].inDept?"":data[0].inDept);
						    $("#operDoc").combobox('select',!data[0].opDoctor?"":data[0].opDoctor);
						    $("#operDept").combobox('select',!data[0].inDept?"":data[0].inDept);
						    $("#arrangeNo").val(!data[0].id?"":data[0].id);
						}else{
							$.messager.alert("提示","该患者信息不存在");
						}
					}
				});
		 }else{
			 $.messager.alert("提示","该患者数据有问题");
		 }
	}
	
	//患者信息
	function huanzhexinxi(clinicCode,pasource){
		//根据住院流水号查询患者住院信息
		$.ajax({
			url:'<%=basePath%>operation/registration/gethuanzhexinxi.action',
			type:"post",
			data:{clinicCode:clinicCode,pasource:pasource},
			success:function(data){
				var age=!data[0].patientAge?"":data[0].patientAge;
				var ageUnit=!data[0].patientAgeunit?"":data[0].patientAgeunit;
				$('#age').text(age+ageUnit);
				//
				if(data[0].patientPaykind){
					$('#paykindCode').text(payCodeMap.get(data[0].patientPaykind));
				}
			}
		});
	}

	function searchStackTreeNodes(){
		var searchText = $('#adStackTreeSearch').textbox('getValue');
    	$("#tFt").tree("search", searchText);
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
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
<style  type="text/css">
  .window .panel-header .panel-tool .panel-tool-close{
  	  	background-color: red;
  	}
 </style>
</body>
</html>