<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title></title>
		<style type="text/css">
			* {
				box-sizing: border-box;
				margin: 0;
				padding: 0;
			}
			
			li {
				list-style: none;
			}
			
			.clearfix:after {
				content: " ";
				display: block;
				clear: both;
				height: 0;
			}
			
			.clearfix {
				zoom: 1;
			}
			
			.title-name {
				width: 100%;
				height: 65px;
				line-height: 65px;
				font-size: 30px;
				text-align: center;
			}
			
			.table-content-box {
				padding: 0 20px 50px 20px;
				margin: 0 auto;
			}
			
			.table-content {
				width: 100%;
				border-radius: 4px;
				border: 1px solid #E9E9E9;
				margin: 0;
				padding: 0;
			}
			
			.table-content>.table-content-li {
				border-bottom: 1px solid #E9E9E9;
				position: relative;
			}
			
			.table-content>.table-content-li>.li-title {
				background-color: #F4F4F4;
				width: 135px;
				position: absolute;
				height: 100%;
				left: 0;
				top: 0;
			}
			
			.table-content>.table-content-li>.li-title>span {
				width: 100%;
				height: 45px;
				line-height: 45px;
				display: block;
				text-align: right;
				padding-right: 12px;
			}
			
			.table-content>.table-content-li>.li-content {
				padding-left: 135px;
				min-height: 45px;
				padding-right: 60px;
			}
			
			.selectSpan {
				line-height: 23px;
				padding: 0 8px;
				margin: 11px 8px;
				color: #666;
				border-radius: 5px;
				cursor: pointer;
				-webkit-touch-callout: none;
				-webkit-user-select: none;
				-khtml-user-select: none;
				-moz-user-select: none;
				-ms-user-select: none;
				user-select: none;
				height: 23px;
				float: left;
				font-size: 16px;
			}
			
			.selectSpanUser {
				line-height: 23px;
				padding: 0 8px;
				margin: 11px 8px;
				color: #666;
				border-radius: 5px;
				cursor: pointer;
				-webkit-touch-callout: none;
				-webkit-user-select: none;
				-khtml-user-select: none;
				-moz-user-select: none;
				-ms-user-select: none;
				user-select: none;
				height: 23px;
				float: left;
				font-size: 16px;
				background-color: #369794;
				color: #fff;
			}
			
			.selectSpan.active {
				background-color: #369794;
				color: #fff;
			}
			
			.isclear {
				position: absolute;
				top: 0;
				right: 0;
				text-align: left;
				width: 60px;
				line-height: 45px;
				height: 45px;
				color: #369794;
				display: none;
				cursor: pointer;
			}
			
			.isclear.isShow {
				display: block;
			}
			
			.table-content-li.zhiwu .isclear.isShow {
				display: block;
			}
			
			.table-content-li.zhiwu.isblooming {
				height: 45px;
				overflow: hidden;
				cursor: pointer;
			}
			
			.table-content-li.zhiwu .blooming {
				position: absolute;
				top: 0;
				right: 0;
				text-align: left;
				width: 60px;
				line-height: 45px;
				height: 45px;
				color: #369794;
			}
			
			.table-content-li.zhiwu .isclear {
				position: absolute;
				top: 0;
				right: 60px;
				text-align: left;
				width: 60px;
				line-height: 45px;
				height: 45px;
				color: #369794;
				display: none;
			}
			
			.table-content>.table-content-li.zhiwu>.li-content {
				padding-right: 120px;
			}
			/*自定义*/
			
			.custFont1 {
				line-height: 30px;
				position: absolute;
				left: 12px;
				width: 75px;
				text-align: center;
				display: block;
			}
			
			.custFont2 {
				line-height: 30px;
				position: absolute;
				left: 90px;
				width: 75px;
				text-align: center;
				display: block;
			}
			
			.custFont3 {
				width: 100%;
				line-height: 30px;
				padding: 0 120px 0 180px;
				display: block;
			}
			
			#cust {
				width: 100%;
				position: relative;
			}
			
			.attrBox {
				width: 120px;
				position: absolute;
				right: 0;
				line-height: 30px;
				top: 0;
			}
			
			.attrBox span {
				margin-left: 12px;
				color: #999;
				cursor: pointer;
			}
			
			.custDiv {
				position: relative;
			}
			
			.selectIfBox .form-group {
				margin-top: 10px;
			}
			
			.egit {
				border: 1px solid #369794;
				border-radius: 8px;
			}
			
			.qie {
				position: absolute;
				left: -13px;
				line-height: 30px;
			}
			
			#cust>div.custDiv:first-child .qie {
				display: none;
			}
			
			.col-xs-9,
			.col-xs-3 {
				padding-left: 0;
				padding-right: 0;
			}
			
			.selectIfBox .form-group {
				margin-top: 0;
			}
		</style>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/select2.css" />
	</head>

	<body>
		<h1 id="titleName" class="title-name">网关条件</h1>
		<div class="table-content-box">
			<div class="li-content clearfix" style="padding-right: 0;">
				<div class="selectIfBox clearfix" id="selectAllBox">

					<div class="col-xs-12 form-group" id="typeSelectBox">
						<label for="typeSelect" class="col-xs-3 control-label">流转类型</label>
						<div class="col-xs-9">
							<select id='typeSelect' class="form-control">
								<option value="0">一般流转</option>
								<option value="1">指定流转</option>
								<option value="2">金额判断</option>
								<option value="3">级别判断</option>
								<option value="4">人员类型判断</option>
								<option value="5">科室判断</option>
								<option value="6">院区判断</option>
							</select>
						</div>
					</div>
					<!--一般流转-->
					<div class="col-xs-12 form-group" id="conditionBox">
						<label for="condition" class="col-xs-3 control-label">流转条件</label>
						<div class="col-xs-9">
							<select class="form-control" id='condition'>
								<option value="0">通过</option>
								<option value="1">不通过</option>
							</select>
						</div>
					</div>
					
					<div class="col-xs-12 form-group" id="conditionIoBox">
						<label for="conditionIo" class="col-xs-3 control-label">流转条件</label>
						<div class="col-xs-9">
							<select class="form-control" id='conditionIo'>
								<option value="0">流转至</option>
								<option value="1">流转非</option>
							</select>
						</div>
					</div>
					<!--大于小于等于-->
					<div class="col-xs-12 form-group" id="conditionBigAndSmallBox">
						<label for="conditionBigAndSmall" class="col-xs-3 control-label">条件关系</label>
						<div class="col-xs-9">
							<select class="form-control" id="conditionBigAndSmall">
								<option value=">">大于</option>
								<option value=">=">大于等于</option>
								<option value="<">小于</option>
								<option value="<=">小于等于</option>
							</select>
						</div>
					</div>
					<!--指定流转-->
					<div id="inputTextBox" class="col-xs-12 form-group">
						<label for="inputTextdom" class="col-xs-3 control-label" id="inputText"></label>
						<div class="col-xs-9">
							<input id="inputTextdom" class="form-control" type="number" min="1" value="" placeholder="" />
						</div>
					</div>
					<!--等于不等于-->
					<div id="logicEqualBox" class="col-xs-12 form-group">
						<label for="logicEqual" class="col-xs-3 control-label">条件关系</label>
						<div class="col-xs-9">
							<select id="logicEqual" class="form-control">
								<option value="==">等于</option>
								<option value="!=">不等于</option>
							</select>
						</div>
					</div>
					

					<div class="form-group col-xs-12" id="select2OrderBox">
						<label for="logic" class="col-xs-3 control-label">级别</label>
						<div class="col-xs-9">
							<select id="select2Order" class="form-control" multiple="multiple"></select>
						</div>	
					</div>
					<div class="form-group col-xs-12" id="deptselectBox">
						<label for="logic" class="col-xs-3 control-label">科室</label>
						<div class="col-xs-9">
							<select id="deptselect" class="form-control" multiple="multiple"></select>
						</div>	
					</div>
					<div class="form-group col-xs-12" id="userselectBox">
						<label for="logic" class="col-xs-3 control-label">人员类型</label>
						<div class="col-xs-9">
							<select id="userselect" class="form-control" multiple="multiple"></select>
						</div>	
					</div>
					<div class="form-group col-xs-12" id="hisselectBox">
						<label for="logic" class="col-xs-3 control-label">院区</label>
						<div class="col-xs-9">
							<select id="hisselect" class="form-control" multiple="multiple"></select>
						</div>	
					</div>
					<!--条件拼接-->
					<div id="logicBox" class="col-xs-12 form-group">
						<label for="logic" class="col-xs-3 control-label">条件拼接</label>
						<div class="col-xs-9">
							<select id="logic" class="form-control">
								<option value="||">或者</option>
								<option value="&&">并且</option>
							</select>
						</div>
					</div>
				</div>
				<div id="cust"></div>
			</div>
		</div>
	</body>

</html>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/bootstrap-select2.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/select2-zh-CN.js"></script>
<script type="text/javascript">
	function condition(data) {
		this.init(data)
	}
	condition.prototype = {
		constructor: condition,
		data: {
			"equal": [{
				id: "equal1",
				text: "等于",
				info: "=="
			}, {
				id: "equal2",
				text: "不等于",
				info: "!="
			}],
			"logic": [{
				id: "logic1",
				text: "或者",
				info: "||"
			}, {
				id: "logic2",
				text: "并且",
				info: "&&"
			}],
			"level": [{
				id: "level1",
				text: "一级",
				info: "1"
			}, {
				id: "level2",
				text: "二级",
				info: "2"
			}, {
				id: "level3",
				text: "三级",
				info: "3"
			}, {
				id: "level4",
				text: "四级",
				info: "4"
			}]
		},
		lineData:{
			"level": {
				"1":"一级",
				"2":"二级",
				"3":"三级",
				"4":"四级"
			}
		}
	}
	window.conditionDom = condition.prototype
	conditionDom.init = function(sendData) {
		var self = this
		if(!self.data["hisType"]) {
			$.ajax({
				type: "get",
				url: "${pageContext.request.contextPath}/oa/empName/hisType.action",
				async: true,
				success: function(data) {
					self.data["hisType"] = []
					self.data["userType"] = []
					for(var key in data['hisType']) {
						self.data["hisType"].push({
							id: key,
							text: data['hisType'][key]
						})
					}
					for(var key in data['userType']) {
						self.data["userType"].push({
							id: key,
							text: data['userType'][key]
						})
					}
					self.initevent()
					self.setPage(sendData)
				}
			});
		} else {
			self.setPage(sendData)
		}
	}
	conditionDom.setPage = function(data) {
		this.resInput()
		var reslut = data.code
		reslut = reslut.replace(/\$\{|\}|\s+/g,"")
		var self = this
    	if (reslut.indexOf("zkhonryState") != -1){
    		reslut.replace(/zkhonryState([!=]{2})'(\d+)'/g,function(a,b,c){
    			if(c == "0"){
    				$("#typeSelect").val("0").trigger('change')
    				$("#conditionBox").show()
    				if(b == "=="){
    					$("#condition").val("0")
    				}else{
    					$("#condition").val("1")
    				}
    			}else{
    				$("#typeSelect").val("1").trigger('change')
    				if(b == "=="){
    					$("#conditionIo").val("0")
    					$("#inputTextdom").val(c)
    				}else{
    					$("#conditionIo").val("1")
    					$("#inputTextdom").val(c)
    				}
    			}
    		})
    	}else if (reslut.indexOf("lowercase") != -1){
    		$("#typeSelect").val("2").trigger('change')
    		reslut.replace(/lowercase([=<>]+)(\d+).00/g,function(a,b,c){
    			$("#conditionBigAndSmall").val(b)
    			$("#inputTextdom").val(c)
    		})
    	}else if (reslut.indexOf("dutiesLevel") != -1){
    		$("#typeSelect").val("3").trigger('change')
			var opctionval = []
    		reslut.replace(/dutiesLevel([!=]{2})'(\d+)'([&|]+)?/g,function(a,b,c,d){
    			$("#logicEqual").val(b).trigger('change')
				opctionval.push("level"+c)
    		})
    		if(opctionval.length>1){
    			$("#logicBox").show()
    		}
    		$("#select2Order").val(opctionval).trigger('change')
    	}else if (reslut.indexOf("employeeType") != -1){
    		$("#typeSelect").val("4").trigger('change')
    		var opctionval = []
    		reslut.replace(/employeeType([!=]{2})'(\w+)'([&|]+)?/g,function(a,b,c,d){
    			$("#logicEqual").val(b).trigger('change')
				opctionval.push(c)
    		})
    		$("#userselect").val(opctionval).trigger('change')
    		if(opctionval.length>1){
    			$("#logicBox").show()
    		}
    	}else if (reslut.indexOf("departmentCode") != -1){
    		$("#typeSelect").val("5").trigger('change')
			var opctionstr = "";
			var opctionval = []
    		reslut.replace(/departmentCode([!=]{2})'(\w+)'([&|]+)?/g,function(a,b,c,d){
    			$("#logicEqual").val(b).trigger('change')
				opctionval.push(c)
    		})
    		if(opctionval.length>1){
    			$("#logicBox").show()
    		}
    		$.ajax({
    			type:"get",
    			url:"oa/empName/deptNameByCode.action?code="+opctionval.join("-"),
    			async:true,
    			success:function(resData){
    				var data = resData.data
    				for (var i = 0 ;i<data.length;i++) {
      					opctionstr += "<option value='"+ data[i]["code"] +"'>"+data[i]["name"] +"</option>"
    				}
    				$("#deptselect").html(opctionstr).val(opctionval).trigger('change')
    			}
    		});
    	}else if (reslut.indexOf("deptAreaCode") != -1){
    		$("#typeSelect").val("6").trigger('change')
    		var opctionval = []
    		reslut.replace(/deptAreaCode([!=]{2})'(\w+)'([&|]+)?/g,function(a,b,c,d){
    			$("#logicEqual").val(b).trigger('change')
				opctionval.push(c)
    		})
    		$("#hisselect").val(opctionval).trigger('change')
    		if(opctionval.length>1){
    			$("#logicBox").show()
    		}
    	}else{
    		$("#typeSelect").val("0").trigger('change')
    		$("#conditionBox").show()
    	}
		
	}
	conditionDom.resInput = function() {
		$("#selectAllBox>div.col-xs-12,div.col-xs-6").hide()
		$("#typeSelectBox").show()
	}

	conditionDom.initevent = function() {
		var self = this
		$("#typeSelect").on("change", function() {
			var tmpCode = this.value
			if(tmpCode == "0") {
				self.resInput()
				$("#conditionBox").show()
				$("#condition").val("0")
			} else if(tmpCode == "1") {
				self.resInput()
				$("#inputTextBox").show()
				$("#inputTextdom").val("").attr("placeholder", "请输入指定流转序号")
				$("#inputText").html("指定流转")
				$("#conditionIoBox").show()
				$("#conditionIo").val("0")
			} else if(tmpCode == "2") {
				self.resInput()
				$("#inputTextBox").show()
				$("#inputTextdom").val("").attr("placeholder", "请输入指定金额")
				$("#inputText").html("金额")
				$("#conditionBigAndSmallBox").show()
			} else if(tmpCode == "3") {
				self.resInput()
				$("#select2OrderBox").show()
				$("#select2Order").val("").trigger('change')
				$("#logicEqualBox").show()
				//$("#logicBox").show()
				$("#logicEqual").val("==").trigger('change')
			} else if(tmpCode == "4") {
				self.resInput()
				$("#logicEqualBox").show()
				//$("#logicBox").show()
				$("#logicEqual").val("==").trigger('change')
				$("#userselectBox").show()
				$("#userselect").val("").trigger('change')
			} else if(tmpCode == "5") {
				self.resInput()
				$("#logicEqualBox").show()
				//$("#logicBox").show()
				$("#logicEqual").val("==").trigger('change')
				$("#deptselectBox").show()
				$("#deptselect").val("").trigger('change')
			} else if(tmpCode == "6") {
				self.resInput()
				$("#logicEqualBox").show()
				//$("#logicBox").show()
				$("#logicEqual").val("==").trigger('change')
				$("#hisselectBox").show()
				$("#hisselect").val("").trigger('change')
			}
		})
		$("#select2Order").html("").select2({
			maximumSelectionLength: 3,
			width: "392px",
			language: "zh-CN",
			placeholder: "请输入或选择具体级别",
			data: self.data.level
		})
		$("#select2Order,#hisselect,#userselect,#deptselect").on("select2:unselect",function(){
			var selectData =$(this).select2('data')
			if(selectData.length <= 1){
				$("#logicBox").hide()
			}
		}).on('select2:select',function(){
			var selectData =$(this).select2('data')
			if(selectData.length > 1){
				$("#logicBox").show()
			}
		})
		$("#deptselect").html("").select2({
			maximumSelectionLength: 10,
			width: "392px",
			language: "zh-CN",
			placeholder: "请输入或选择具体科室",
			ajax: {
				url: '<%=basePath%>oa/empName/queryDept.action',
				type: "POST",
				processResults: function(data) {
					var resData = []
					for(var i = 0; i < data.data.length; i++) {
						resData.push({
							id: data.data[i]["code"],
							text: data.data[i]["name"]
						})
					}
					return {
						results: resData
					};
				},
				data: function(params) {
					var query = {
						str: params.term,
					}
					return query;
				}
			}
		})

		$("#userselect").html("").select2({
			maximumSelectionLength: self.data.userType.length - 1,
			width: "392px",
			language: "zh-CN",
			placeholder: "请输入或选择具体人员类型",
			data: self.data.userType
		})
		$("#hisselect").html("").select2({
			maximumSelectionLength: self.data.hisType.length - 1,
			width: "392px",
			language: "zh-CN",
			placeholder: "请输入或选择具体院区",
			data: self.data.hisType
		})
		//联动绑定
		$("#logic").on("change", function() {
			if(this.value == "||") {
				$("#logicEqual").val("==")
			} else {
				$("#logicEqual").val("!=")
			}
		})
		$("#logicEqual").on("change", function() {
			if(this.value == "==") {
				$("#logic").val("||")
			} else {
				$("#logic").val("&&")
			}
		})
		$("#inputTextdom").on("keyup",function(){
			if($(this).val() == "0"){
				$(this).val("") 
			}
		})
	}
	//	var start 
	function start(data) {
		new condition(data)
	}

	function save() {
		var resStr = ""
		var selectVal = $("#typeSelect").val() 
		if(selectVal == "0"){
			if($("#condition").val() == "0"){
				resStr = "zkhonryState=='0'"
			}else{
				resStr = "zkhonryState!='0'"
			}
		}else if(selectVal == "1"){
			if($("#conditionIo").val() == "0"){
				resStr = "zkhonryState=='"+ $("#inputTextdom").val() +"'"
			}else{
				resStr = "zkhonryState!='"+ $("#inputTextdom").val() +"'"
			}
			if(!$("#inputTextdom").val()){
				resStr = ""
			}
		}else if(selectVal == "2"){
			resStr = "lowercase"+ $("#conditionBigAndSmall").val() +""+ $("#inputTextdom").val() +".00"
			if(!$("#inputTextdom").val()){
				resStr = ""
			}
		}else if(selectVal == "3"){
			var selectData = $("#select2Order").select2('data')
			var restmp = $("#logicEqual").val() == "==" ? true : false
			for (var i = 0 ;i<selectData.length;i++) {
				if(restmp){
					resStr +="dutiesLevel=='"+selectData[i]["info"]+"'||"
				}else{
					resStr +="dutiesLevel!='"+selectData[i]["info"]+"'&&"
				}
			}
			resStr = resStr.substr(0,resStr.length-2)
		}else if(selectVal == "4"){
			var selectData = $("#userselect").select2('data')
			var restmp = $("#logicEqual").val() == "==" ? true : false
			for (var i = 0 ;i<selectData.length;i++) {
				if(restmp){
					resStr +="employeeType=='"+selectData[i]["id"]+"'||"
				}else{
					resStr +="employeeType!='"+selectData[i]["id"]+"'&&"
				}
			}
			resStr = resStr.substr(0,resStr.length-2)
		}else if(selectVal == "5"){
			var selectData = $("#deptselect").select2('data')
			var restmp = $("#logicEqual").val() == "==" ? true : false
			for (var i = 0 ;i<selectData.length;i++) {
				if(restmp){
					resStr +="departmentCode=='"+selectData[i]["id"]+"'||"
				}else{
					resStr +="departmentCode!='"+selectData[i]["id"]+"'&&"
				}
			}
			resStr = resStr.substr(0,resStr.length-2)
		}else if(selectVal == "6"){
			var selectData = $("#hisselect").select2('data')
			var restmp = $("#logicEqual").val() == "==" ? true : false
			for (var i = 0 ;i<selectData.length;i++) {
				if(restmp){
					resStr +="deptAreaCode=='"+selectData[i]["id"]+"'||"
				}else{
					resStr +="deptAreaCode!='"+selectData[i]["id"]+"'&&"
				}
			}
			resStr = resStr.substr(0,resStr.length-2)
		}
		return resStr
	}
</script>
