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
				height: 50px;
				line-height: 65px;
				font-size: 30px;
				text-align: center;
			}
			
			.table-content-box {
				padding: 0 20px 20px 20px;
				margin: 0 auto;
				min-width: 768px;
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
			.selectIfBox .form-group{
				margin-top: 10px;				
			}
			.egit {
				border: 1px solid #369794;
				border-radius: 8px;
			}
			.select2-search__field{
				width: 100% !important;
			}
		</style>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/select2.css" />
	</head>

	<body>
		<h1 id="titleName" class="title-name">指派候选人</h1>
		<div class="li-content clearfix" style="padding-right: 0;">
			<div class="selectIfBox clearfix">
				<div class="form-group col-md-2">
					<select id ="selectChang" class="form-control">
						<option value="EMPLOYEE_JOBNO">人员</option>
					</select>
			    </div>
				<div class="form-group col-md-2">
					<select id = "selectIn" class="form-control">
						<option value="">包含</option>
						<option value="userHand">等于</option>
					</select>
				</div>
				<div class="form-group  col-md-6">
					<select id = "select2Order" class="form-control" multiple="multiple">
					</select>
				</div>
				<div class="form-group  col-md-6">
					<input class="form-control" id="inputNum" type="number" min="0" value="" placeholder="请输入节点人员序号" />
				</div>
				<!-- <div class="form-group col-md-2" style="text-align: center;padding: 0;">
					<button id="saveDraft" type="button" class="btn btn-theme" style="text-align: center;" >添加</button>
					<button id="reomveDraft" type="button" class="btn btn-theme" style="text-align: center;display: none;" >取消</button>
				</div> -->
			</div>
			<div id="cust"></div>
		</div>
	</body>

</html>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/bootstrap-select2.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/select2-zh-CN.js"></script>
<script type="text/javascript">
	var iskey = true
	function objDom(data) {
		this.init(data)
	}
	window.objDomPrototype = objDom.prototype = {
		constructor: objDom,
		resData : {}
	}
	objDomPrototype.init = function(data) {
//		this.getData()
		this.callback(data)
	},
	//初始化回调
	objDomPrototype.callback = function(data) {
		this.data = data
		//渲染数据
		if(iskey){
			iskey = false
			this.Setevent()
		}
		//注册事件
		this.setPage(data)
	}
	objDomPrototype.setPage = function(data) {
		//自定义
		var userData = data.code.replace(/\(|\)/g,"")
		if(userData.indexOf("userHand") != -1){
			$("#selectIn").val("userHand").trigger('change')
			$("#inputNum").val(data.resUserNode)
		}else{
			$("#selectIn").val("").trigger('change')
			var tmpData = data.data
			var opctionstr = "";
			var opctionval = []
			for (var i = 0 ;i<tmpData.length;i++) {
				opctionstr += "<option value='"+ tmpData[i].code +"'>"+tmpData[i].name +"</option>"
				opctionval.push(tmpData[i].code)
			}
			$("#select2Order").html(opctionstr).val(opctionval).trigger('change')
		}
	}

	objDomPrototype.Setevent = function() {
		var self = this
		$("#selectIn").on("change",function(){
			if($(this).val()){
				$("#inputNum").val("").parent().show()
				$("#select2Order").val("").parent().hide()
			}else{
				$("#inputNum").val("").parent().hide()
				$("#select2Order").val("").parent().show()
			}
		})
		$("#select2Order").html("").select2({
			maximumSelectionLength:30,
			width:"100%",
			language: "zh-CN",
			placeholder:"输入姓名或者工号选择个人" ,
			ajax: {
			    url: '<%=basePath%>meeting/meetingApply/queryEmployeeExtend.action',
			    type:"POST",
		        processResults: function (data) {
		        	var resData = []
		        	for (var i = 0;i<data.rows.length;i++) {
		        		resData.push({
		        			id:data.rows[i]["jobNo"],
		        			text:data.rows[i]["name"]
		        		})
		        	}
			      	return {
				        results: resData
			      	};
			    },
			    data: function (params) {
			      var query = {
			        ename: params.term,
			        page:1,
					rows:10,
					dname:""
			      }
			      return query;
			    }
			  }
		})

	}
	var save = function(){
		if($("#selectIn").val()){
			var data = {
				code:"(",
				name:"候选人 ",
				resData:[],
				resUserNode:""
			}
			var userNode = $("#inputNum").val()
			if(userNode){
				data.name += "等于 " + userNode + "节点人员"
				data.code += "userHand:"+userNode + ")"
				data.resUserNode = userNode
				return data
			}else{
				alert("请输入指派人员节点")
				return ""
			}
		}else{
			var selectData = $("#select2Order").select2('data')
			if(selectData.length != 0){
				var data = {
					code:"(",
					name:"候选人 ",
					resData:[],
					resUserNode:""
				}
				data.name += "包含 "
				for (var i = 0 ;i<selectData.length;i++) {
					data.name += selectData[i].text +"-"
					data.code += selectData[i].id +"-"
					data.resData.push({
						code: selectData[i].id,
						name:selectData[i].text
					})
				}
				if(selectData.length>0){
					data.name = data.name.substr(0,data.name.length-1) 
					data.code = data.code.substr(0,data.code.length-1) +")"
				}
				return data
			}else{
				alert("请选择指派人员")
				return ""
			}
		}
	}
	
	function start (data){
		new objDom(data)
	}
</script>

		