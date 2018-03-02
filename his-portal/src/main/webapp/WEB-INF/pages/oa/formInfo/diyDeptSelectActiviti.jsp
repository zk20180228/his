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
				left: 30px;
				width: 105px;
				text-align: center;
				display: block;
			}
			
			.custFont2 {
				line-height: 30px;
				position: absolute;
				left: 130px;
				width: 50px;
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
			.selectIfBox .form-group{
				margin-top: 10px;				
			}
			.egit {
				border: 1px solid #369794;
				border-radius: 8px;
			}
			.qie{
				position: absolute;
				left: 0;
				line-height: 30px;
			}
			#cust>div.custDiv:first-child .qie {
				display: none;
			}
			.select2-search__field{
				width: 100% !important;
			}
		</style>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/select2.css" />
	</head>

	<body>
		<h1 id="titleName" class="title-name">指定候选组</h1>
		<div class="table-content-box">
			<div class="li-content clearfix" style="padding-right: 0;">
				<div class="selectIfBox clearfix" style="display: none;" id = "selectAllBox">
					<div class="form-group col-xs-6" id = "selectChangBox">
						<select id ="selectChang" class="form-control">
							<option value="kehsi">科室</option>
							<option value="juecezu">决策组</option>
							<option value="fenguan">分管院长</option>
							<option value="fenguanFuZeRen">分管负责人</option>
						</select>
				    </div>
					<div class="form-group col-xs-6" id = "selectInBox">
						<select id = "selectIn" class="form-control">
							<option value="in">包含</option>
							<option value="all">等于</option>
						</select>
					</div>
					<div class="form-group col-xs-12 isControl" id = "selectopctionBox">
						<select id = "selectopction" class="form-control"></select>
					</div>
					<!--科室-->
					<div class="form-group col-xs-12 isControl" id = "select2OrderBox">
						<select id = "select2Order" class="form-control" multiple="multiple"></select>
					</div>
					<!--职称-->
					<div class="form-group col-xs-12 isControl" id = "select2titleBox">
						<select id = "select2title" class="form-control" multiple="multiple"></select>
					</div>
					<!--输入框-->
					<div class="form-group col-xs-12 isControl" id = "select2inputBox">
						<input type="number" min="0" id = "select2input" class="form-control" />
					</div>
					<div class="form-group col-xs-12" style="text-align: right;">
						<button id = "addsave" class="addBtn btn btn-theme">添加</button>
						<button id = "reomveDraft" class="btn btn-theme" style="display: none;">取消</button>
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
	var isKey = true
	var custDivStart = false//编辑状态标识
	function objDom(data) {
		this.init(data)
		custDivStart = false
	}
	objDom.prototype = {
		constructor: objDom,
		resData : {}
	}
	window.objDomPrototype = objDom.prototype
	objDomPrototype.init = function(data) {
		this.selectOpc = {
			dept:   '<option value="dept">具体科室</option>'+
					'<option value="departmentModlue">所选科室</option>'+
					'<option value="userHand">任务人员所属科室</option>'+
					'<option value="divisionCode">发起人所属学部</option>'+
					'<option value="generalbranchCode">发起人所属总支</option>',
			selectIn:'<option value="in">包含</option>'+
					'<option value="all">等于</option>'
		}
		this.getData(data)
	},
	//获取数据
	objDomPrototype.getData = function(data) {
		//渲染数据
		this.setPage(data)
		//注册事件
		if(isKey){
			isKey = false
			this.Setevent()
		}
	}

	objDomPrototype.setPage = function(data) {
		var self = this
		if(data.name && data.code){
			data.code = data.code.substr(1,data.code.length-2)
			var codeStr = data.code.split("&&")
			var nameStr = data.name.split(" 并且 ")
			var str = ""
			for(i = 0; i < nameStr.length; i++) {
				var nameArr = nameStr[i].split(" ")
				str += '<div code = "' + codeStr[i] + '" class = "custDiv clearfix"><span class = "qie">并且</span><span class="custFont1">' + nameArr[0] + '</span><span class = "custFont2">' + nameArr[1] + '</span><span class = "custFont3">' + nameArr[2] + '</span><div class = "attrBox"><span class = "delcust">删除</span><span class = "changecust" >编辑</span></div></div>'
			}
			$("#cust").html(str)
		}
		$("#selectAllBox").show()
		$("#selectAllBox .isControl").hide()
		$("#selectopctionBox").show()
		$("#selectopction").html('<option value="dept">具体科室</option>').val("dept")
		$("#select2OrderBox").show()
		$("#select2Order").val("").trigger('change')
	}

	objDomPrototype.Setevent = function() {
		var self = this
		$("#selectChang").on("change",function(){
			var val = $(this).val()
			$("#selectAllBox .isControl").hide()
			$("#selectIn").html(self.selectOpc.selectIn).val("in")
			$("#select2Order").val("").trigger('change')
			$("#select2title").val("").trigger('change')
			$("#select2input").val("")
			if( val == "kehsi"){
				$("#selectopctionBox").show()
				$("#selectopction").html('<option value="dept">具体科室</option>').val("dept")
				$("#select2OrderBox").show()
				$("#select2Order").val("").trigger('change')
			}else if (val == "juecezu"){
				$("#selectIn").html('<option value="in">包含</option>').val("in")
				$("#select2titleBox").show()
				$("#select2title").val("").trigger('change')
			}else if(val == "fenguan"){
				$("#selectIn").html('<option value="all">等于</option>').val("all")
				$("#selectopctionBox").show()
				$("#selectopction").html('<option value="divi:{dept}">主管院领导</option>').val("divi:{dept}")
			}else if (val == "fenguanFuZeRen"){
				$("#selectIn").html('<option value="all">等于</option>').val("all")
				$("#selectopctionBox").show()
				$("#selectopction").html('<option value="duty:{dept}">负责人</option>').val("duty:{dept}")
			}
		})
		$("#selectopction").on("change",function(){
			var val = $(this).val()
			$("#selectAllBox .isControl").hide()
			$("#selectopctionBox").show()
			$("#select2Order").val("").trigger('change')
			$("#select2title").val("").trigger('change')
			$("#select2input").val("")
			if(val == "dept"){
				$("#select2OrderBox").show()
				$("#select2Order").val("").trigger('change')
			}else if (val == "departmentModlue"){
				$("#select2inputBox").show()
				$("#select2input").attr("placeholder",'请输入下拉控件编号').val("")
			}else if (val == "userHand"){
				$("#select2inputBox").show()
				$("#select2input").attr("placeholder",'请输入人员顺序编号').val("")
			}else if (val == "divisionCode"){
				
			}else if (val == "generalbranchCode"){
			
			}
		})
		$("#selectIn").on("change",function(){
			var val = $(this).val()
			$("#select2Order").val("").trigger('change')
			$("#select2title").val("").trigger('change')
			$("#select2input").val("")
			if(val == "in"){
				$("#selectopction").html('<option value="dept">具体科室</option>').trigger('change')
			}else{
				$("#selectopction").html(
					'<option value="departmentModlue">所选科室</option>'+
					'<option value="userHand">任务人员所属科室</option>'+
					'<option value="divisionCode">发起人所属学部</option>'+
					'<option value="generalbranchCode">发起人所属总支</option>'
				).trigger('change')
			}
		})
		//具体科室
		$("#select2Order").html("").select2({
			maximumSelectionLength:50,
			width:"100%",
			language: "zh-CN",
			placeholder:"请输入或选择具体科室" ,
			ajax: {
			    url: '<%=basePath%>oa/empName/queryDept.action',
			    type:"POST",
		        processResults: function (data) {
		        	var resData = []
		        	for (var i = 0;i<data.data.length;i++) {
		        		resData.push({
		        			id:data.data[i]["code"],
		        			text:data.data[i]["name"]
		        		})
		        	}
			      	return {
				        results: resData
			      	};
			    },
			    data: function (params) {
			      var query = {
			        str: params.term,
			      }
			      return query;
			    }
			  }
		})
		//具体职称
		
		$("#select2title").html("").select2({
			maximumSelectionLength:50,
			width:"100%",
			language: "zh-CN",
			placeholder:"请输入或选择具体职称",
			ajax: {
			    url: '<%=basePath%>oa/empName/queryJob.action',
			    type:"POST",
		        processResults: function (data) {
		        	var resData = []
		        	for (var i = 0;i<data.data.length;i++) {
		        		resData.push({
		        			id:data.data[i]["code"],
		        			text:data.data[i]["name"]
		        		})
		        	}
			      	return {
				        results: resData
			      	};
			    },
			    data: function (params) {
			     var query = {
			        str: params.term,
			      }
			      return query;
			    }
			  }
		})
		//添加
		$("#addsave").on("click",function(){
			if(custDivStart == false){
				if($("#cust .custDiv").length>=2){
					alert("条件拼接最多只能为2条!")
					return false
				}
				if($("#cust .custDiv").length == 1 && $("#cust .custDiv:first .custFont1").html() != "科室" && $("#cust .custDiv:first .custFont1").html() != "分管负责人"){
					alert("该前置条件不能拼接条件(只有前置条件为科室、分管负责人,时才能进行拼接)")
					return false
				}
				if($("#cust .custDiv").length == 1 && $("#selectChang").val() != "juecezu"){
					alert("只能拼接决策组条件")
					return false
				}
				if($("#cust .custDiv").length==1 && $("#cust .custDiv:first .custFont1").html() == "科室" && $("#selectChang").val() == "keshi"){
					alert("科室条件必须唯一!")
					return false
				}
			}
			var type = $("#selectChang option:selected").text()
			var inORnotIn = $("#selectIn option:selected").text()
			var opction = $("#selectopction option:selected").text()
			var selectOrder = $("#select2Order").select2('data')
			var selectTitle = $("#select2title").select2('data')
			var selec6 = $("#select2input").val()
			var name = ""
			var code = ""
			if(type == "分管院长"){
				name = "分管院长 等于 主管院领导"
				code = "divi:{dept}"
			}else if (type == "分管负责人"){
				name = "分管负责人 等于 负责人"
				code = "duty:{dept}"
			}else if (type  == "决策组"){
				if(selectTitle.length>0){
					var tmpName = ""
					var tmpCode = ""
					for (var i = 0 ;i<selectTitle.length;i++) {
						tmpName += selectTitle[i].text + "-"
						tmpCode += selectTitle[i].id + "-"
					}
					tmpName = tmpName.substr(0,tmpName.length-1)
					tmpCode = tmpCode.substr(0,tmpCode.length-1)
					name = "决策组 包含 "+ tmpName
					code = "mana:"+ tmpCode
				}else{
					alert("请选择具体决策组")
					return false
				}
			}else if(type  == "科室"){
				if(inORnotIn == "包含"){
					if(selectOrder.length>0){
						var tmpName = ""
						var tmpCode = ""
						for (var i = 0; i<selectOrder.length;i++) {
							tmpName += selectOrder[i].text + "-"
							tmpCode += selectOrder[i].id + "-"
						}
						tmpName = tmpName.substr(0,tmpName.length-1)
						tmpCode = tmpCode.substr(0,tmpCode.length-1)
						name = "科室 包含 "+ tmpName
						code = "dept:"+ tmpCode
					}else{
						alert("请选择具体科室")
						return false
					}
				}else{
					if(opction == "所选科室"){
						if(!selec6){
							alert("请选输入控件编号")
							return false
						}
						code = "dept:{departmentModlue-"+ selec6 +"}"
						name = "科室 等于 所选科室"
					}else if (opction == "任务人员所属科室"){
						if(!selec6){
							alert("请选输入人员编号")
							return false
						}
						code = "userHand:"+ selec6 +""
						name = "科室 等于 任务"+ selec6 +"人员所属科室"
					}else if (opction == "发起人所属学部"){
						code = "dept:{divisionCode}"
						name = "科室 等于 发起人所属学部"
					}else if (opction == "发起人所属总支"){
						code = "dept:{generalbranchCode}"
						name = "科室 等于 发起人所属总支"
					}
				}
			}else{
				alert("权限数据异常")
			}
			var nameArr = name.split(" ")
			if(custDivStart){
//				if($("#selectChang").val() != "kehsi" && $("#cust .custDiv").length == 2){
//					alert("该前置条件不能拼接条件(只有前置条件为科室时才能进行拼接)")
//					return false
//				}
				if($("#cust .custDiv").length==1 && $("#cust .custDiv:first .custFont1").html() == "科室" && $("#selectChang").val() == "keshi"){
					alert("科室条件必须唯一!")
					return false
				}
				if($("#cust .custDiv").length == 2){
					if($("#cust .custDiv.egit .custFont1").html() == $("#cust .custDiv:first .custFont1").html()){
						if($("#selectChang").val() != "kehsi" && $("#selectChang").val() != "fenguanFuZeRen"){
							alert("该前置条件不能拼接条件(只有前置条件为科室、分管负责人,时才能进行拼接)")
							return false
						}
					}else{
						if($("#selectChang").val() != "juecezu"){
							alert("只能拼接决策组条件")
							return false
						}
					}
				}
				custDivStart = false	
				$("#addsave").html("添加")
				$("#reomveDraft").hide()
				$("#cust .custDiv.egit").after('<div code = "' + code + '" class = "custDiv clearfix"><span class = "qie">并且</span><span class="custFont1">' + nameArr[0] + '</span><span class = "custFont2">' + nameArr[1] + '</span><span class = "custFont3">' + nameArr[2] + '</span><div class = "attrBox"><span class = "delcust">删除</span><span class = "changecust" >编辑</span></div></div>')
				$("#cust .custDiv.egit").remove()
			}else{
				$("#cust").append('<div code = "' + code + '" class = "custDiv clearfix"><span class = "qie">并且</span><span class="custFont1">' + nameArr[0] + '</span><span class = "custFont2">' + nameArr[1] + '</span><span class = "custFont3">' + nameArr[2] + '</span><div class = "attrBox"><span class = "delcust">删除</span><span class = "changecust" >编辑</span></div></div>')
			}
			$("#selectAllBox").show()
			$("#selectAllBox .isControl").hide()
			$("#selectopctionBox").show()
			$("#selectopction").html('<option value="dept">具体科室</option>').val("dept")
			$("#select2OrderBox").show()
			$("#select2Order").val("").trigger('change')
			$("#selectChang").val("kehsi").trigger('change')
		})
		//自定义编辑删除
		$("#cust").on("click","span.delcust",function(){
		    var $this = $(this)
			$.confirm("提示","确定要删除?",function(){
				custDivStart = false	
				$(".custDiv.egit").removeClass('egit')
				$this.parents(".custDiv").remove()
				$("#addsave").html("添加")
				$("#selectAllBox").show()
				$("#selectAllBox .isControl").hide()
				$("#selectopctionBox").show()
				$("#selectopction").html('<option value="dept">具体科室</option>').val("dept")
				$("#select2OrderBox").show()
				$("#select2Order").val("").trigger('change')
				$("#reomveDraft").click()
			})
		})
		//取消
		$("#reomveDraft").on("click",function(){
			custDivStart = false	
			$("#cust .egit").removeClass("egit")
			$("#selectChang").val("kehsi").trigger('change')
			$("#addsave").html("添加")
			$("#selectAllBox").show()
			$("#selectAllBox .isControl").hide()
			$("#selectopctionBox").show()
			$("#selectopction").html('<option value="dept">具体科室</option>').val("dept")
			$("#select2OrderBox").show()
			$("#select2Order").val("").trigger('change')
			$(this).hide()			
		})
		var dataText = {
			"科室":"kehsi",
			"决策组":"juecezu",
			"分管院长":"fenguan",
			"分管负责人":"fenguanFuZeRen",
			"包含":"in",
			"等于":"all",
			"具体科室":"dept",
			"所选科室":"departmentModlue",
			"任务人员所属科室":"userHand",
			"发起人所属学部":"divisionCode",
			"发起人所属总支":"generalbranchCode",
			"主管院领导":"divi:{dept}"
		}
		$("#cust").on("click","span.changecust",function(){
			custDivStart = true	
			$("#addsave").html("修改")
			$("#reomveDraft").show()
			var dom = $(this).parents(".custDiv")
			dom.addClass('egit')
			var code = dom.attr("code") 
			var custFont1 = dom.find(".custFont1").html()
			var custFont2 = dom.find(".custFont2").html()
			var custFont3 = dom.find(".custFont3").html()
			$("#selectChang").val(dataText[custFont1]).trigger('change')
			if(custFont1 == "科室"){
				$("#selectIn").val(dataText[custFont2]).trigger('change')
			}
			if(custFont3 == "所选科室"){
				code.replace(/dept:\{departmentModlue-(\d+)\}/,function(a,b){
					$("#select2input").val(b)
				})
			}else if (custFont3 == "发起人所属学部"){
				
			}else if (custFont3 == "发起人所属总支"){
				
			}else if (custFont3.indexOf("人员所属科室") != -1){
				code.replace(/userHand:(\d+)/,function(a,b){
					$("#select2input").val(b)
				})
			}else if (custFont3 == "主管院领导"){
				
			}else{
				if($("#select2OrderBox").css("display") == "block"){
					var tmpCodeArr = code.replace(/dept:(\w+(\-\w+)*)/g,function(a,b){
						return b
					})
					tmpCodeArr = tmpCodeArr.split("-")
					var tmpName = custFont3.split("-")
					var str = ''
					for (var i = 0;i<tmpName.length;i++) {
						str += "<option value='"+ tmpCodeArr[i] +"'>"+ tmpName[i] +"</option>"
					}
					$("#select2Order").html(str).val(tmpCodeArr).trigger('change')
				}else if ($("#select2titleBox").css("display") == "block"){
					var tmpCodeArr = code.replace(/mana:(\w+(\-\w+)*)/g,function(a,b){
						return b
					})
					tmpCodeArr = tmpCodeArr.split("-")
					var tmpName = custFont3.split("-")
					var str = ''
					for (var i = 0;i<tmpName.length;i++) {
						str += "<option value='"+ tmpCodeArr[i] +"'>"+ tmpName[i] +"</option>"
					}
					$("#select2title").html(str).val(tmpCodeArr).trigger('change')
				}
			}
		})
	}
	
	objDomPrototype.setSelect = function(data){
		var str = ''
		for (var i = 0;i<data.length;i++) {
			str += "<option value='"+ data["code"] +"'>"+ data["name"] +"</option>"
		}
		$("#select2Order").html(opctionstr)
		$("#select2Order").val(select2OrderData).trigger('change')
	}
	
	function start (data){
		new objDom(data) 
	}
	
	function save (){
		if(custDivStart){
			$.confirm("提示","是否保存编辑的信息?",function(){
//				custDivStart = false
				$("#addsave").click()
//				$(".custDiv.egit").removeClass('egit')
//				$this.parents(".custDiv").remove()
//				$("#addsave").html("添加")
//				$("#selectAllBox").show()
//				$("#selectAllBox .isControl").hide()
//				$("#selectopctionBox").show()
//				$("#selectopction").html('<option value="dept">具体科室</option>').val("dept")
//				$("#select2OrderBox").show()
//				$("#select2Order").val("").trigger('change')
			})
		}else if($("#cust .custDiv").length > 0){
		    var name = ""
		    var code = ""
			$("#cust .custDiv").each(function(i,v){
				if(i == 0){
					code +=  $(v).attr("code")
					name +=  $(v).find(".custFont1").html() + " " + $(v).find(".custFont2").html() + " " + $(v).find(".custFont3").html()
				}else{
					code += "&&" + $(v).attr("code")
					name += " 并且 " + $(v).find(".custFont1").html() + " " + $(v).find(".custFont2").html() + " " + $(v).find(".custFont3").html()
				}
			})
			return {
				"name":name,
				"code":"["+ code +"]"
			}
		}else{
			alert("请先添加权限")
			return ""
		}
	}
</script>

		