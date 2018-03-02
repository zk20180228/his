<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>患者在院信息</title>
</head>
	<body>
		<div class="easyui-layout" fit="true">		   
			<div   region="center" border="false" style="height: 100%;width: 100%">
				<table id="infolist" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'inpatientNo',width:110">住院流水号</th>													
							<th data-options="field:'medicalrecordId',width:100">病历号</th>												
							<th data-options="field:'patientName',width:100">姓名</th>
							<th data-options="field:'deptName',width:100">住院科室</th>												
							<th data-options="field:'bedName',width:80">床号</th>
							<th data-options="field:'paykindCode',formatter:functionPaykind,width:100">患者类别</th>
							<th data-options="field:'prepayCost',width:100" align="right" halign="left">预交金（未结）</th>
							<th data-options="field:'totCost',width:120" align="right" halign="left">费用合计（未结）</th>
							<th data-options="field:'freeCost',width:130" align="right" halign="left">余额</th>
							<th data-options="field:'ownCost',width:100" align="right" halign="left">自费</th>
							<th data-options="field:'payCost',width:100" align="right" halign="left">自付</th>
							<th data-options="field:'pubCost',width:120" align="right" halign="left">公费</th>
							<th data-options="field:'inDate',width:100">入院日期</th> 
							<th data-options="field:'inState',formatter:functionInstate,width:70">在院状态</th>  
							<th data-options="field:'outDate',width:100,formatter:functionDate">出院日期</th>   
							<th data-options="field:'balancePrepay',width:120" align="right" halign="left">预交金（已结）</th>
							<th data-options="field:'balanceCost',width:120" align="right" halign="left">费用合计（已结）</th>
							<th data-options="field:'balanceDate',width:150">结算日期</th>
						</tr>
					</thead>
				</table>		
			</div>
		</div>
	
<script type="text/javascript">
var paykind=new Array();
var stste=[{id:'R',value:'住院登记'},{id:'I',value:'病房接诊'},{id:'B',value:'出院登记'},{id:'O',value:'出院结算'},{id:'P',value:'预约出院'},{id:'N',value:'无费退院'},{id:'C',value:'婴儿封账'}];
$(function(){
	//渲染表单中的挂号专家
	$.ajax({
		url: '<%=basePath%>stat/inpatientFee/paykind.action',
		success: function(empData) {
			paykind = empData;
		}
	})
});
//渲染患者类别
function functionPaykind(value,row,index){
	for(var i=0;i<paykind.length;i++){
		if(paykind[i].encode==value){
			return paykind[i].name;
		}
	}
}

//渲染在院状态

function functionInstate(value,row,index){
	for(var i=0;i<stste.length;i++){
		if(stste[i].id==value){
			return stste[i].value;
		}
	}
}

function functionDate(value,row,index){
	var date=value;
	if(date!=null&&date!=""){
		if(date.substring(0,4)==0002||date.substring(0,4)==0001){
			return "";
		}else{
			return date;
		}
	}else{
		return date;
	}
}
</script>
	</body>
</html>