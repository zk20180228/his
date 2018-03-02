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
		<script type="text/javascript">
			//系统时间	
			function showSysTime(){
				$('#xtsjId').text(showTime(true,true));
				setTimeout('showSysTime()',1000);
			}
			//转换金钱为大写
		    function changeMoneyToChinese(money){
		    	var cnNums = new Array("零","壹","贰","叁","肆","伍","陆","柒","捌","玖"); //汉字的数字
		    	var cnIntRadice = new Array("","拾","佰","仟"); //基本单位
		    	var cnIntUnits = new Array("","万","亿","兆"); //对应整数部分扩展单位
		    	var cnDecUnits = new Array("角","分","毫","厘"); //对应小数部分单位
		    	var cnInteger = "整"; //整数金额时后面跟的字符
		    	var cnIntLast = "元"; //整型完以后的单位
		    	var maxNum = 999999999999999.9999; //最大处理的数字
		    	var IntegerNum; //金额整数部分
		    	var DecimalNum; //金额小数部分
		    	var ChineseStr=""; //输出的中文金额字符串
		    	var parts; //分离金额后用的数组，预定义
		    	if( money == "" ){
		    		return "";
		    	}
		    	money = parseFloat(money);
		    	if( money >= maxNum ){
		    		$.messager.alert('提示','超出最大处理数字');
		    		return "";
		    	}
		    	if( money == 0 ){
		    		ChineseStr = cnNums[0]+cnIntLast+cnInteger;
		    		return ChineseStr;
		    	}
		    	money = money.toString(); //转换为字符串
		    	if( money.indexOf(".") == -1 ){
		    		IntegerNum = money;
		    		DecimalNum = '';
		    	}else{
			    	parts = money.split(".");
			    	IntegerNum = parts[0];
			    	DecimalNum = parts[1].substr(0,4);
		    	}
		    	if( parseInt(IntegerNum,10) > 0 ){//获取整型部分转换
			    	zeroCount = 0;
			    	IntLen = IntegerNum.length;
		    		for( i=0;i<IntLen;i++ ){
		    			n = IntegerNum.substr(i,1);
		    			p = IntLen - i - 1;
		    			q = p / 4;
		    			m = p % 4;
		    			if( n == "0" ){
		    				zeroCount++;
		    			}else{
		    				if( zeroCount > 0 ){
		    					ChineseStr += cnNums[0];
		    				}
		    				zeroCount = 0; //归零
		    				ChineseStr += cnNums[parseInt(n)]+cnIntRadice[m];
		    			}
		    			if( m==0 && zeroCount<4 ){
		    				ChineseStr += cnIntUnits[q];
		    			}
		    		}
		    		ChineseStr += cnIntLast;
		    		//整型部分处理完毕
		    	}
		    	if( DecimalNum!= '' ){//小数部分
		    		decLen = DecimalNum.length;
		    		for( i=0; i<decLen; i++ ){
		    			n = DecimalNum.substr(i,1);
		    			if( n != '0' ){
		    				ChineseStr += cnNums[Number(n)]+cnDecUnits[i];
		    			}
		    		}
		    	}
		    	if( ChineseStr == '' ){
		    		ChineseStr += cnNums[0]+cnIntLast+cnInteger;
		    	}else if( DecimalNum == '' ){
		    		ChineseStr += cnInteger;
		    	}
		    	return ChineseStr;
		    }
		</script>
	</head>
	<body> 
		<div id="cc" class="easyui-layout" style="width:'100%';height:'100%'"> 
		<form id="editForm" method="post" data-options="runat:'server',region:'center'" style="height:100%; border:green 0px solid;"> 
		<div data-options="region:'north',split:true" style="height:100px">
			<div style="padding:5px 5px 5px 5px;">
				<a id="btnSave" href="javascript:saveFeed();" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen',disabled:false">确认收费</a>&nbsp;&nbsp;
				<a id="btnClear" href="javascript:clearFeed();" class="easyui-linkbutton" data-options="iconCls:'icon-clear',disabled:false">清屏</a>&nbsp;&nbsp;
				<a id="btnReload" href="javascript:reloadFeed();" class="easyui-linkbutton" data-options="iconCls:'icon-reload',disabled:false">刷新</a>&nbsp;&nbsp;
			</div> 
			<div style="padding:0px 5px 5px 5px;">
				<table border="0" style="float:left">
					<tr>
						<td style="width:70px;" align="right">病历号：</td>
						<td style="width:130px"><input class="easyui-textbox" id="blhId" data-options="prompt:'请输入病历号'" style="width:120px"/></td>
						<td style="width:70px;" align="right">患者姓名：</td>
						<td style="width:130px"><input class="easyui-textbox" id="nameId" style="width:120px" readonly="readonly"/></td>
						<td style="width:70px;" align="right">性别：</td>
						<td style="width:130px"><input class="easyui-textbox" id="sexId" style="width:120px" readonly="readonly"/></td>
						<td style="width:70px;" align="right">年龄：</td>
						<td style="width:130px"><input class="easyui-textbox" id="ageId" style="width:120px" readonly="readonly"/></td>
						<td style="width:70px;" align="right">看诊科室：</td>
						<td style="width:130px"><input class="easyui-textbox" id="deptId" style="width:120px" readonly="readonly"/></td>
						<td style="width:70px;" align="right">开立医生：</td>
						<td style="width:130px"><input class="easyui-textbox" id="docId" style="width:120px" readonly="readonly"/></td>
					</tr>
					<tr>
						<td style="width:70px;" align="right">合同单位：</td>
						<td style="width:130px"><input class="easyui-textbox" id="contractId" style="width:120px" readonly="readonly"/></td>
						<td style="width:70px;" align="right">挂号级别：</td>
						<td style="width:130px"><input class="easyui-textbox" id="gradeId" style="width:120px" readonly="readonly"/></td>
						<td style="width:70px;" align="right">就诊卡号：</td>
						<td style="width:130px"><input class="easyui-textbox" id="idcardId" style="width:120px" readonly="readonly"/></td>
					</tr>
				</table>
			</div>
		</div>   
	    <div data-options="region:'south',split:true" style="height:140px;">
	    	<div class="easyui-layout" data-options="fit:true">   
    			<table border="0" style="float:left;margin-left:5px;">
					<tr>
						<td width="100px" align="right">总金额：</td>
						<td width="110px" id="zlfId"></td>
						<td width="100px" align="right">大写总金额：</td>
						<td id="dxzjeId" width="200px"></td>
						<td width="100px" align="right">操作员：</td>
						<td width="100px">${sessionUser.name }</td>
						<td width="100px" align="right">科室：</td>
						<td width="100px">${loginDepartment.deptName }</td>
						<td width="100px" align="right">系统时间：</td>
						<td id="xtsjId"></td>
					</tr>
				</table>
			</div>
	    </div>   
	    <div data-options="region:'west',split:true" style="width:220px;">
	    	<div class="easyui-layout" data-options="fit:true">   
	    		<div data-options="region:'north',split:false" style="height:100%"><ul id="undrugStackTree"></ul><ul id="undrugTree"></ul></div>   
	    	</div>
	    </div>   
	    <div data-options="region:'center'">
	    	<table id="list" class="easyui-datagrid"  width="100%" data-options="fit:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">   
			    <thead>   
			        <tr><th data-options="field:'ck',checkbox:true" ></th>   
			            <th data-options="field:'itemCode',hidden:true">编码</th>   
			            <th data-options="field:'itemName'" width="8%">名称</th>   
			            <th data-options="field:'qty',formatter:funNum" width="8%">数量</th>   
			            <th data-options="field:'priceUnit'" width="8%">单位</th>   
			            <th data-options="field:'stackId',hidden:true" width="8%">组套编码</th>   
			            <th data-options="field:'stackName'" width="8%">组套名称</th>   
			            <th data-options="field:'unitPrice',formatter:funPrice" width="8%">单价</th> 
			            <th data-options="field:'payCost',formatter:funPayCost" width="8%">金额</th>   
			        </tr>   
			    </thead>   
			</table> 
			<div id="toolbarId">
				<a id="btnRemove" href="javascript:void(0)" onclick="removeFeed()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</div>
				 <div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:30 60 40 60" data-options="modal:true, closed:true">   
		     <table id="infoDatagrid"  style="width:400px;height:400" data-options="fitColumns:true,singleSelect:true">   
		</table>  
    </div> 
	    </div>
	    </form>
	    </div> 
		<script type="text/javascript">
			var deptMap = null;
			var emplMap = null;
			var contMap = null;
			var gradMap = null;
			var cardMap = null;
			var sexMap=new Map();
			$(function(){
				showSysTime();//系统时间
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
				$.ajax({
					url: "<c:url value='/baseinfo/department/getDeptMap.action'/>",
					type:'post',
					success: function(deptData) {
						deptMap = eval("("+deptData+")");
					}
				});	
				$.ajax({
					url: "<c:url value='/baseinfo/employee/getEmplMap.action'/>",
					type:'post',
					success: function(emplData) {
						emplMap = eval("("+emplData+")");
					}
				});	
				$.ajax({
					url: "<c:url value='/baseinfo/businessContractunit/getContMap.action'/>",
					type:'post',
					success: function(contData) {
						contMap = eval("("+contData+")");
					}
				});	
				$.ajax({
					url: "<c:url value='/register/getGradeMap.action'/>",
					type:'post',
					success: function(gradData) {
						gradMap = eval("("+gradData+")");
					}
				});	
				$.ajax({
					url: "<c:url value='/patient/idcard/getCardNoMap.action'/>",
					type:'post',
					success: function(cardData) {
						cardMap = eval("("+cardData+")");
					}
				});	
				bindEnterEvent('blhId',searchFeed,'easyui');
			});
			//非药品树
			$('#undrugTree').tree({   
				url:"<c:url value='/outpatient/itemlist/queryUndrugByMinimum.action'/>",
				lines:true,
				onDblClick:function(node){
					if(node!=null&&node.attributes.isUnDrug=="1"){
						var index = $('#list').datagrid('appendRow',{
							itemCode: node.id,//编码
							itemName: node.text,//名称
							qty: 1,//数量
							priceUnit:node.attributes.undrugUnit,//单位
							unitPrice:node.attributes.defaultprice,//单价
							payCost:parseFloat(parseFloat(1)*parseFloat(node.attributes.defaultprice).toFixed(2)).toFixed(2)//金额
						}).datagrid('getRows').length-1;
						$('#list').datagrid('beginEdit',index);
						var rows = $('#list').datagrid('getRows');
						var zlf = parseFloat(0.00);
						for(var i=0;i<rows.length;i++){
							var rowIndex = $('#list').datagrid('getRowIndex',rows[i]);
							var z = parseFloat($('#payCost_'+rowIndex).text());
							zlf = parseFloat(zlf)+parseFloat(z);
						}
						var fy = parseFloat(zlf).toFixed(2);
						$('#zlfId').text(fy);
						$('#dxzjeId').text(changeMoneyToChinese(fy));
						$('#zlfId').val(fy);
					}
				}
			}); 
			//非药品组套树
			$('#undrugStackTree').tree({   
				url:"<c:url value='/outpatient/itemlist/queryUndrugStackTree.action'/>",
				lines:true,
				onDblClick:function(node){
					$.ajax({
						url: "<c:url value='/outpatient/itemlist/queryUndrugStackInfo.action'/>?id="+node.id,
						type:'post',
						success: function(info) {
							var infoList = eval("("+info+")");
							for(var j =0;j<infoList.length;j++){
								var index = $('#list').datagrid('appendRow',{
									itemCode: infoList[j].stackInfoItemId,//编码
									itemName: infoList[j].stackInfoItemName,//名称
									qty: 1,//数量
									priceUnit:infoList[j].stackInfoUnit,//单位
									stackId:node.id,//组套编号
									stackName:node.text,//组套名称
									unitPrice:infoList[j].defaultprice,//单价
									payCost:parseFloat(parseFloat(1)*parseFloat(infoList[j].defaultprice).toFixed(2)).toFixed(2)//金额
								}).datagrid('getRows').length-1;
								$('#list').datagrid('beginEdit',index);
								var rows = $('#list').datagrid('getRows');
								var zlf = parseFloat(0.00);
								for(var i=0;i<rows.length;i++){
									var rowIndex = $('#list').datagrid('getRowIndex',rows[i]);
									var z = parseFloat($('#payCost_'+rowIndex).text());
									zlf = parseFloat(zlf)+parseFloat(z);
								}
								var fy = parseFloat(zlf).toFixed(2);
								$('#zlfId').text(fy);
								$('#dxzjeId').text(changeMoneyToChinese(fy));
								$('#zlfId').val(fy);
							}
						}
					});
				}
			}); 
			//为数量添加keyup事件
			function funNum(value,row,index){
				return '<input id="num_'+index+'" type="text" class="datagrid-editable-input numberbox-f textbox-f" value="'+value+'" data-options="min:0,precision:0" onkeyup="keyupNum(this)" style="width:100%"></input>';
			}
			//为单价添加id
			function funPrice(value,row,index){
				return '<div id="price_'+index+'">'+value+'</div>';
			}
			//为金额添加id
			function funPayCost(value,row,index){
				return '<div id="payCost_'+index+'">'+value+'</div>';
			}
			//keyup事件
			function keyupNum(inpObj){
				if($(inpObj).val().length==1){
					$(inpObj).val($(inpObj).val().replace(/[^1-9]/g,''));
				}else{
					$(inpObj).val($(inpObj).val().replace(/\D/g,''));
				}
				var val = $(inpObj).val();
				var id = $(inpObj).prop("id").split("_");
				var price = $('#price_'+id[1]).text();
				var money = parseFloat(0.00).toFixed(2);
				if(val!=null&&val!=""){
					money = parseFloat(parseFloat(val)*parseFloat(price).toFixed(2)).toFixed(2);
				}
				$('#list').datagrid('updateRow',{
					index: id[1],
					row: {
						qty:val,
						payCost:money
					}
				});
				$('#num_'+id[1]).val("").focus().val(val);
				var rows = $('#list').edatagrid('getRows');
				var zlf = parseFloat(0.00);
				for(var i=0;i<rows.length;i++){
					var rowIndex = $('#list').edatagrid('getRowIndex',rows[i]);
					var z = parseFloat($('#payCost_'+rowIndex).text());
					zlf = parseFloat(zlf)+parseFloat(z);
				}
				var fy = parseFloat(zlf).toFixed(2);
				$('#zlfId').text(fy);
				$('#dxzjeId').text(changeMoneyToChinese(fy));
				$('#zlfId').val(fy);
			}
			//实收现金 用于计算找零
			function keyupCash(inpObj){
				var zj = $('#zlfId').val();
				if(zj!=null&&zj!=''){
					var zl = parseFloat(0.00).toFixed(2);
					if($(inpObj).val()!=null&&$(inpObj).val()!=""){
						zl = parseFloat(parseFloat($(inpObj).val()).toFixed(2)-parseFloat(zj).toFixed(2)).toFixed(2);
					}
				}
			}
			//刷新
			function reloadFeed(){
			    var tab = self.parent.$('#tabs').tabs('getSelected');  // 获取选择的面板
			    self.parent.$('#tabs').tabs('update', {
			    	tab: tab,
			    	options: {
			    		title: '门诊划价',
			    		href: "<c:url value='/outpatient/itemlist/listItemlist.action'/>?menuAlias=${menuAlias}"
			    	}
			    });
			}
			//清屏
			function clearFeed(){
				var listRows = $('#list').datagrid('getRows');
				if(listRows!=null&&listRows.length>0){
					var listR = listRows.length;
					for(var j=0;j<listR;j++){
						$('#list').datagrid('deleteRow',0);
					}
				}
				$('#zlfId').text("");
				$('#dxzjeId').text("");
				$('#editForm').form('reset');
			}
			function searchFeed(){
				var bln = $('#blhId').textbox('getText');
				if(bln.length!=6){
					$.messager.alert('提示','请输入病历号或者住院流水号六位');
					return;
				}
				if(bln!=null&&blhId!=""){
					$.ajax({
						url: "<c:url value='/outpatient/itemlist/queryRegisterByCaseNo.action'/>?id="+bln,
						type:'post',
						success: function(info) {
							var infoObj = eval("("+info+")");
						/* 	if(infoObj.length>1){
								$("#diaInpatient").window('open');
								$("#infoDatagrid").datagrid({
									url:"<c:url value='/outpatient/itemlist/queryRegisterByCaseNo.action'/>?id="+bln,  
								    columns:[[    
								        {field:'inpatientNo',title:'住院号',width:'20%',align:'center'} ,    
								        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
								        {field:'reportSex',title:'性别',width:'20%',align:'center'} ,
								        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
								        {field:'certificatesNo',title:'身份证号',width:'20%',align:'center'} 
								    ]] ,
								    onDblClickRow:function(rowIndex, rowData){
								    	$('#nameId').textbox('setValue',infoObj.patientId.patientName);//患者姓名
										$('#sexId').textbox('setValue',infoObj.patientId.patientSex);//性别
										var birthday = infoObj.patientId.patientBirthday;
										var nowdate = new Date();
										var age =nowdate.getFullYear() - birthday.substr(0,4)+"";
										$('#ageId').textbox('setValue',age);//年龄
										$('#deptId').textbox('setValue',deptMap[infoObj.dept]);//看诊科室
										$('#docId').textbox('setValue',emplMap[infoObj.expxrt]);//看诊医生
										$('#contractId').textbox('setValue',contMap[infoObj.contractunit]);//合同单位
										$('#gradeId').textbox('setValue',gradMap[infoObj.grade]);//等级编码
										$('#idcardId').textbox('setValue',cardMap[infoObj.idcardId]);//就诊卡号
										$("#diaInpatient").window('close');
								    }
								});
							} */
							if(infoObj.id!=null&&infoObj.id!=""){
								$('#nameId').textbox('setValue',infoObj.patientId.patientName);//患者姓名
								$('#sexId').textbox('setValue',sexMap.get(infoObj.patientId.patientSex));//性别
								var birthday = infoObj.patientId.patientBirthday;
								var nowdate = new Date();
								var age =nowdate.getFullYear() - birthday.substr(0,4)+"";
								$('#ageId').textbox('setValue',age);//年龄
								$('#deptId').textbox('setValue',deptMap[infoObj.dept]);//看诊科室
								$('#docId').textbox('setValue',emplMap[infoObj.expxrt]);//看诊医生
								$('#contractId').textbox('setValue',contMap[infoObj.contractunit]);//合同单位
								$('#gradeId').textbox('setValue',gradMap[infoObj.grade]);//等级编码
								$('#idcardId').textbox('setValue',cardMap[infoObj.idcardId]);//就诊卡号
							}else{
								$.messager.alert('提示','无此病历号记录!');
								clearFeed();
								$('#blhId').textbox('setText','').focus();
							}
						}
					});
				}
			}
			//删除
			function removeFeed(){
				var rowsdel = $('#list').datagrid('getChecked');
				if(rowsdel!=null&&rowsdel!=''&&rowsdel.length>0){
					for(var i=0;i<rowsdel.length;i++){
						$('#list').datagrid('deleteRow',$('#list').datagrid('getRowIndex',rowsdel[i]));
					}
					var rows = $('#list').datagrid('getRows');
					var zlf = parseFloat(0.00);
					for(var i=0;i<rows.length;i++){
						zlf = parseFloat(zlf)+parseFloat(rows[i].payCost);
					}
					$('#zlfId').text(zlf);
					$('#dxzjeId').text(changeMoneyToChinese(zlf));
				}else{
					$.messager.alert('提示','请选择要删除的信息');
				}
			}
			//划价保存
			function saveFeed(){
				var rows = $('#list').datagrid('getRows');
				if(rows!=null&&rows.length>0){
					if(confirm("确定收费?")){
				        $.post("<c:url value='/outpatient/itemlist/saveItemlist.action'/>",{"jsonData":$.toJSON(rows),"caseNo":$('#blhId').textbox('getText')},function(data){
				        	var dataMap = eval("("+data+")");
				   			if(dataMap.resMsg=="error"){
				   				$.messager.alert('提示',dataMap.resCode);
				        		//clearFeed();
				        	}else if(dataMap.resMsg=="success"){
				        		$.messager.alert('提示',dataMap.resCode);
								reloadFeed();
				        	}else{
				        		$.messager.alert('提示','未知错误,请联系管理员!');
				        	}
				   		});
			        }else{
			        	return;
			        }
				}else{
					$.messager.alert('提示','没有需要划价的信息!');
				}
			}
		</script>
	</body>
</html>