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
			.selectIfBox .form-group{
				margin-top: 10px;				
			}
			.egit {
				border: 1px solid #369794;
				border-radius: 8px;
			}
		</style>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/select2.css" />
	</head>

	<body>
		<h1 id="titleName" class="title-name"></h1>
		<div class="table-content-box">
			<input id="inpId" type="hidden" value="${id }">
			<ul class="table-content">
				<li class="table-content-li selectAllBox">
					<div class="li-title"><span class="fontM">全部:</span></div>
					<div class="li-content">
						<span id="selectAll" class="selectSpan fontM">全部</span>
					</div>
				</li>
				<li class="table-content-li">
					<div class="li-title"><span class="fontM">人员类型:</span></div>
					<div id="userType" class="li-content clearfix"></div>
					<div class="isclear">清空</div>
				</li>
				<li class="table-content-li zhiwu isblooming">
					<div class="li-title"><span class="fontM">职务:</span></div>
					<div id="duty" class="li-content clearfix"></div>
					<div class="isclear">清空</div>
					<div class="blooming">展开</div>
				</li>
				<li class="table-content-li">
					<div class="li-title"><span class="fontM">级别:</span></div>
					<div id="level" class="li-content clearfix"></div>
					<div class="isclear">清空</div>
				</li>
				<li class="table-content-li">
					<div class="li-title"><span class="fontM">院区:</span></div>
					<div id="district" class="li-content clearfix"></div>
					<div class="isclear">清空</div>
				</li>
				<li class="table-content-li">
					<div class="li-title"><span class="fontM">个人:</span></div>
					<div class="li-content clearfix">
						<div class="addUserBtnBox">
							<div class="form-group col-md-12" style="text-align: left;margin-top: 10px;">
								<button id="addUser" type="button" class="btn" style="text-align: center;" >添加人员</button>
							</div>	
						</div>
						<div id="pers" class="persBox"></div>
					</div>
				</li>
				<li class="table-content-li diyContent">
					<div class="li-title"><span class="fontM">自定义:</span></div>
					<div class="li-content clearfix" style="padding-right: 0;">
						<div class="selectIfBox clearfix">
							<div class="form-group col-md-2">
								<select id ="selectChang" class="form-control">
									<option value="e.EMPLOYEE_TYPE">人员类型</option>
									<option value="e.DUTIES_TYPE">职务</option>
									<option value="e.DUTIES_LEVEL">级别</option>
									<option value="e.AREA_CODE">院区</option>
									<option value="e.EMPLOYEE_JOBNO">个人</option>
								</select>
						    </div>
							<div class="form-group col-md-2">
								<select id = "selectIn" class="form-control">
									<option value="IN">包含</option>
									<option value="NOT IN">不包含</option>
								</select>
							</div>
							<div class="form-group  col-md-6">
								<select id = "select2Order" class="form-control" multiple="multiple">
								</select>
							</div>
							<div class="form-group col-md-2" style="text-align: center;padding: 0;">
								<button id="saveDraft" type="button" class="btn btn-theme" style="text-align: center;" >添加</button>
								<button id="reomveDraft" type="button" class="btn btn-theme" style="text-align: center;display: none;" >取消</button>
							</div>	
						</div>
						<div id="cust"></div>
					</div>
				</li>
			</ul>
		</div>
		<div class="navbar navbar-default navbar-fixed-bottom" style="z-index: 0;">
		    <div class="container-fluid">
		        <div class="text-center" style="padding-top:8px;">
			    	<button id="save" type="button" class="btn btn-theme">保存</button>
			    	<button id="clearW" type="button" class="btn">关闭</button>
			    </div>
		    </div>
		</div>
	</body>

</html>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/bootstrap-select2.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/bootstrap-select2/select2-zh-CN.js"></script>
<script type="text/javascript">
	function objDom() {
		this.init()
	}
	window.objDomPrototype = objDom.prototype = {
		constructor: objDom,
		resData : {}
	}
	objDomPrototype.init = function() {
		//保存的数据
		$("body").setLoading("body")
		//获取数据回调
		this.getData(this.callback)
	},
	//获取数据
	objDomPrototype.getData = function(callback) {
		var self = this
		$.ajax({
			type: "get",
			url: '<%=basePath%>oa/juris/getJuris.action?v='+Math.random(),
			data:{id:$('#inpId').val()},
			async: true,
			success: function(data) {
				$("body").rmoveLoading ("body")
				callback.call(self, data)
			}
		});
	}
	//初始化回调
	objDomPrototype.callback = function(data) {
		this.data = data
		//渲染数据
		this.Setevent()
		//注册事件
		this.setPage(data)
	}
	objDomPrototype.setPage = function(data) {
		this.id = data.id
		if(data.isAll) {
			$("#selectAll").click()
		}
		$("#titleName").html(data.name)
		var eachData = {
				userType: data.type,
				duty: data.duties,
				level: data.level,
				district: data.area
			},
			i, str = "";
		for(var key in eachData) {
			var tmpData = eachData[key]
			var tmp = false
			for(i = 0; i < tmpData.length; i++) {
				if(tmpData[i].select) {
					str += '<span code="' + tmpData[i].code + '" class = "selectSpan active">' + tmpData[i].name + '</span>'
					tmp = true
				} else {
					str += '<span code="' + tmpData[i].code + '" class = "selectSpan">' + tmpData[i].name + '</span>'
				}
			}
			$("#" + key).html(str)
			str = ""
			if(tmp) {
				$("#" + key).parent().children(".isclear").addClass("isShow")
				tmp = false
			}
		}
		//个人
		var pers = data.pers
		for(i = 0; i < pers.length; i++) {
			str += '<span code="' + pers[i].code + '" class = "selectSpanUser">' + pers[i].name + '</span>'
		}
		$("#pers").html(str)
		str = ""
		//自定义
		var cust = data.cust
		var custAll = cust.name.split("and")
		var custcodeAll = cust.code.split("and")
		for(i = 0; i < custAll.length; i++) {
			var custstr = custAll[i].split("%")
			var custcodestr = custcodeAll[i].trim()
			if(i == 0) {
				str += '<div code = "' + custcodestr + '" class = "custDiv clearfix"><span class="custFont1">' + custstr[0] + '</span><span class = "custFont2">' + custstr[1] + '</span><span class = "custFont3">' + custstr[2] + '</span><div class = "attrBox"><span class = "delcust">删除</span><span class = "changecust" >编辑</span></div></div>'
			} else {
				str += '<div code = "' + custcodestr + '" class = "custDiv clearfix"><span class="custFont1">' + custstr[1] + '</span><span class = "custFont2">' + custstr[2] + '</span><span class = "custFont3">' + custstr[3] + '</span><div class = "attrBox"><span class = "delcust">删除</span><span class = "changecust" >编辑</span></div></div>'
			}
		}
		$("#cust").html(str)
		str = ""
	}

	objDomPrototype.Setevent = function() {
		var self = this
		//点击变颜色事件
		$(".table-content").on("click", ".selectSpan", function() {
				var $this = $(this)
				if($this.hasClass("active")) {
					$this.removeClass("active")
					if(this.id == "selectAll") {
						self.setSelectAll(false)
					} else if(!$this.parent().children("span").hasClass("active")) {
						$this.parents(".table-content-li").find(".isclear").removeClass("isShow")
					}
				} else {
					$this.addClass("active")
					if(this.id == "selectAll") {
						self.setSelectAll(true)
					} else {
						$this.parents(".table-content-li").find(".isclear").addClass("isShow")
					}
				}
			})
			//清空事件
			.on("click", ".isclear", function() {
				var $this = $(this)
				$this.removeClass("isShow").parent().find(".li-content>span.selectSpan.active").removeClass("active")
			})
		//展开收缩
		$(".zhiwu .blooming").on("click", function() {
			var dom = $(this).parents(".table-content-li")
			if(dom.hasClass("isblooming")) {
				dom.removeClass("isblooming")
				$(this).html("收起")
			} else {
				dom.addClass("isblooming")
				$(this).html("展开")
			}
		})
		//自定义编辑删除
		$("#cust").on("click","span.delcust",function(){
		    var $this = $(this)
			$.confirm("提示","确定要删除?",function(){
				self.custDivStart = false	
				$("#saveDraft").html("添加")
				$("#reomveDraft").hide()		
				$("#select2Order").val("").trigger('change')
				$this.parents(".custDiv").remove()
			})
		})
		.on("click","span.changecust",function(){
			$("#cust .egit").removeClass("egit")
			self.custDivStart = true
		    var custDiv = $(this).parents(".custDiv")
			var data = custDiv.attr("code").split(" ")
			var select2OrderData;
			custDiv.addClass("egit")
			$("#saveDraft").html("修改")
			$("#reomveDraft").show()
			$("#selectChang").val(data[0]).trigger('change')
			if(data.length ==  3){
				$("#selectIn").val("IN")
				select2OrderData =  data[2].replace(/'|\(|\)/g,"").split(",")
				if(data[0] == "EMPLOYEE_JOBNO"){
					var tmpData = custDiv.find(".custFont3").html().split("、"),opctionstr ="";
					for (var i = 0 ;i<select2OrderData.length;i++) {
						opctionstr += "<option value='"+ select2OrderData[i] +"'>"+ tmpData[i] +"</option>"
					}
					$("#select2Order").html(opctionstr)
				}
				$("#select2Order").val(select2OrderData).trigger('change')
			}else{
				$("#selectIn").val("NOT IN")
				select2OrderData =  data[3].replace(/'|\(|\)/g,"").split(",")
				if(data[0] == "EMPLOYEE_JOBNO"){
					var tmpData = custDiv.find(".custFont3").html().split("、"),opctionstr ="";
					for (var i = 0 ;i<select2OrderData.length;i++) {
						opctionstr += "<option value='"+ select2OrderData[i] +"'>"+ tmpData[i] +"</option>"
					}
					$("#select2Order").html(opctionstr)
				}
				$("#select2Order").val(select2OrderData).trigger('change')
			}
		})
		//自定义事件处理
		var selectChangData = {
			"e.EMPLOYEE_TYPE":"type",
			"e.DUTIES_TYPE":"duties",
			"e.DUTIES_LEVEL":"level",
			"e.AREA_CODE":"area"
		}
		var selectChangPlaceholder = {
			"e.EMPLOYEE_TYPE":"请选择人员类型",
			"e.DUTIES_TYPE":"请选择职务",
			"e.DUTIES_LEVEL":"请选择级别",
			"e.AREA_CODE":"请选择院区",
			"e.EMPLOYEE_JOBNO":"输入姓名或者工号选择个人"
		}
		$("#selectChang").on("change",function(){
			if($(this).val() == "e.EMPLOYEE_JOBNO"){
				$("#select2Order").html("").select2({
				maximumSelectionLength:50,
				width:"100%",
				language: "zh-CN",
				placeholder:selectChangPlaceholder[$(this).val()] ,
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
			}else{
				$("#select2Order").html("").select2({
				  data:self.getselect2Data(self.data[selectChangData[$(this).val()]]),
				  maximumSelectionLength:50,
				  placeholder:selectChangPlaceholder[$(this).val()] ,
				  width:"100%",
				  language: "zh-CN"
				});
			}
		})
		$("#select2Order").select2({
		  data:self.getselect2Data(self.data.type),
		  maximumSelectionLength:50,
		  placeholder:selectChangPlaceholder["e.EMPLOYEE_TYPE"] ,
		  width:"100%",
		  language: "zh-CN"
		});
		//个人添加处理
		$("#addUser").on("click" , function(){
			window.userData = {}
			$('#pers>span').each(function(i,v){
				window.userData[$(v).attr("code")] = $(v).html()
			})
			$.dialog({
				href:"<%=basePath%>oa/formInfo/userModlueAccredit.action?id=pers",
				width:"1050px",
			    height: "540px",
			    title:"人员选择"
			})
		})
		//添加自定义
		$("#saveDraft").on("click",function(){
			var selectData =  $("#select2Order").select2('data')
			if(selectData.length == 0){
				$.alert("提示","请选择要添加的内容!")
			}else{
				var resData =  self.getselect2SendData(selectData)
				var sqlstr = $("#selectChang").val() + " " + $("#selectIn").val() + " " + resData.code
				$("#cust").append('<div code = "' + sqlstr + '" class = "custDiv clearfix"><span class="custFont1">' + $("#selectChang").find("option:selected").text() + '</span><span class = "custFont2">' + $("#selectIn").find("option:selected").text() + '</span><span class = "custFont3">' + resData.text + '</span><div class = "attrBox"><span class = "delcust">删除</span><span class = "changecust" >编辑</span></div></div>')
				$("#select2Order").val("").trigger('change')
				if(self.custDivStart){
					$("#cust .egit").remove()
					self.custDivStart = false
					$(this).html("添加")
					$("#reomveDraft").hide()
				}
			}
		})
		$("#reomveDraft").on("click",function(){
			self.custDivStart = false	
			$("#cust .egit").removeClass("egit")
			$("#saveDraft").html("添加")
			$("#select2Order").val("").trigger('change')
			$(this).hide()			
		})
		//保存
		$("#save").on("click",function(){
			if($("#reomveDraft").is(":hidden")){
				self.setSaveData()
				self.ajaxSave()
			}else{
				$.confirm("提示","是否放弃编辑?",function(){
					$("#cust .egit").removeClass("egit")
					$("#select2Order").val("").trigger('change')
					$("#saveDraft").html("添加")
					$("#reomveDraft").hide()	
					self.custDivStart = false
					self.setSaveData()
					self.ajaxSave()
				})
			}
		})
		//clear
		$("#clearW").on("click",function(){
			$.confirm("提示","确定关闭窗口",function(){
				window.close();	
			})
		})
	}
	//全选后操作
	objDomPrototype.setSelectAll = function(tmp) {
		if(tmp) {
			this.resData = {}
			this.resData.isAll = true
			$(".selectSpan:not(#selectAll)").removeClass("active")
			$(".table-content-li:not(.diyContent,.selectAllBox)").hide()
			$(".isclear").removeClass("isShow")
			$("#pers").html("")
		} else {
			this.resData.isAll = false
			$(".table-content-li").show()
		}
	}
	//处理select2的数据格式
	objDomPrototype.getselect2Data = function(data){
		var resData  = []
		for (var i = 0 ;i<data.length;i++) {
			resData.push({
				text:data[i].name,
				id:data[i].code
			})
		}
		return resData 
	}
	//处理select2选中的数据格式
	objDomPrototype.getselect2SendData = function(data){
		var sendData = "("
		var sendText = ""
		for (var i = 0;i<data.length;i++) {
			sendData += "'"+data[i].id+"',"
			sendText += data[i].text + "、"
		}
		sendData = sendData.substr(0,sendData.length-1)
		sendText = sendText.substr(0,sendText.length-1)
		sendData += ")"
		return {
			code : sendData,
			text : sendText
		}
	}
	
	//保存数据处理
	objDomPrototype.setSaveData = function(){
		var resData = this.resData
		resData.id = this.data.id 
		resData.name = this.data.name
		resData.cust = this.setDiyData()
		if(!resData.isAll){
			resData.type = []
			resData.duties = []
			resData.level = []
			resData.area = []
			resData.pers = []
			//人员类型
			$("#userType>span.selectSpan.active").each(function(i,v){
				resData.type.push({
					"code":$(v).attr("code"),
		            "name":$(v).html(),
		            "select":true
				})
			})
			//职务:
			$("#duty>span.selectSpan.active").each(function(i,v){
				resData.duties.push({
					"code":$(v).attr("code"),
		            "name":$(v).html(),
		            "select":true
				})
			})
			//级别:
			$("#level>span.selectSpan.active").each(function(i,v){
				resData.level.push({
					"code":$(v).attr("code"),
		            "name":$(v).html(),
		            "select":true
				})
			})
			//院区:
			$("#district>span.selectSpan.active").each(function(i,v){
				resData.area.push({
					"code":$(v).attr("code"),
		            "name":$(v).html(),
		            "select":true
				})
			})
			//个人:
			$("#pers>span").each(function(i,v){
				resData.pers.push({
					"code":$(v).attr("code"),
		            "name":$(v).html(),
		            "select":true
				})
			})
		}
	}
	//保存请求
	
	objDomPrototype.ajaxSave = function(){
		var self = this
		$.ajax({
			type:"post",
			url:"<%=basePath%>oa/juris/saveJuris.action",
			data:{json:JSON.stringify(self.resData)},
			async:true,
			success:function(retMap){
				if("success"==retMap.resCode){
					$.alert("提示",retMap.resMsg,function(){
						window.close();	
					})
				}else{
					$.alert("提示",retMap.resMsg)
				}
			},
			error:function(){
				$.alert("提示",'网络异常!')
			}
		});		
		
	}
	//处理自定义数据
	objDomPrototype.setDiyData = function(){
		var data = {
			code:"",
			name:""
		}
		$("#cust>div.custDiv").each(function(i,v){
			if(i == 0){
				data.code += $(v).attr("code")
				data.name += $(v).find(".custFont1").html() + "%"+ $(v).find(".custFont2").html() +"%"+$(v).find(".custFont3").html()
			}else{
				data.code += " and " + $(v).attr("code") 
				data.name += "%and%" + $(v).find(".custFont1").html() + "%"+ $(v).find(".custFont2").html() +"%"+$(v).find(".custFont3").html()
			}
		})
		return data
	}
	new objDom()
</script>

		