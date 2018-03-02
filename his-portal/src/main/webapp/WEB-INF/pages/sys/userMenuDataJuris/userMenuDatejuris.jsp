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
			.iSselectSpanUser {
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
			.unselectSpanUser {
				line-height: 23px;
				padding: 0 8px;
				margin: 11px 8px;
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
				background-color: #fff;
				color: #000;
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
				height: 110px;
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
			<ul class="table-content">
				<li class="table-content-li selectAllBox">
					<div class="li-title"><span class="fontM">移动端同步授权:</span></div>
					<div id = "titleMove" class="li-content">
						<span class="selectSpan active fontM">是</span>
						<span class="selectSpan fontM">否</span>
					</div>
				</li>
				<li class="table-content-li  zhiwu">
					<div class="li-title"><span class="fontM">栏目:</span></div>
					<div class="li-content clearfix">
						<div class="addMenuBtnBox">
							<div class="form-group col-md-12" style="text-align: left;margin-top: 10px;">
								<button id="addMenu" type="button" class="btn" style="text-align: center;" >添加</button>
							</div>	
						</div>
						<div id="menu" class="persBox"></div>
						<div class="isclear">清空</div>
						<div class="blooming">收起</div>
					</div>
				</li>
				<li class="table-content-li">
					<div class="li-title"><span class="fontM">（单选）权限类型:</span></div>
					<div id="district" class="li-content clearfix">
						<span class="selectSpan active fontM">院区</span>
						<span class="selectSpan fontM">科室</span>
					</div>
				</li>
				<li class="table-content-li zhiwu">
					<div class="li-title"><span class="fontM">权限名称:</span></div>
					<div class="li-content clearfix">
						<div id="deptSelect" class="addDeptBtnBox">
								<div class="form-group col-md-12" style="text-align: left;margin-top: 10px;">
									<button id="addDept" type="button" class="btn" style="text-align: center;" >添加</button>
								</div>	
						</div>
						<div id="dept" class="persBox"></div>
						<input type="hidden" id="deptCodeHidden">
						<input type="hidden" id="deptNameHidden">
						<div class="isclear">清空</div>
						<div class="blooming">收起</div>
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
	var areaInfo = null;
	var deptInfo = null;
	var areaLength=null;
	var menuInfo=null;
	$('#deptSelect').hide();
	//加载科室和院区数据
	$.ajax({
		url:'<%=basePath %>sys/userMenuDataJuris/queryDataJurisCodeList.action',
		type:"post",
		async:false,
		success: function(dataMap) {
			areaInfo = dataMap['areaInfo'];
			if(areaInfo!=null&&areaInfo!=''){
				areaInfo=areaInfo[0].children;
				areaLength=areaInfo.length;
				huixianArea();
			}
			deptInfo = dataMap['deptInfo'];
		}
	});
	//点击院区操作
	function huixianArea(){
		if(areaInfo!=null&&areaInfo!=''){
			var areaStr='';
			for(var i=0;i<areaInfo.length;i++){
				areaStr+='<span name="areaSelect" code="'+ areaInfo[i].id +'" class="unselectSpanUser">'+ areaInfo[i].text +'</span>';
			}
			$('#dept').html(areaStr);
		}
	}
	//院区选中操作
	function areaCodeList(){
		var deptAndAreaCode='';
		var deptAndAreaName='';
		var i=0;
		console.info($("span.iSselectSpanUser[name='areaSelect']"));
		$("span.iSselectSpanUser[name='areaSelect']").each(function(index,obj){
			var code=$(this).attr('code');
			var name=$.trim($(this).html());
			if(deptAndAreaCode!=null&&$.trim(deptAndAreaCode)!=''){
				deptAndAreaCode+=',';
				deptAndAreaName+=',';
			}
			deptAndAreaCode+=code;
			deptAndAreaName+=name;
			i++;
		});
		if(parseInt(areaLength)==i){
			deptAndAreaCode='ALL';
			deptAndAreaName='全院';
		}
		console.info(deptAndAreaCode);
		$('#deptCodeHidden').val(deptAndAreaCode);
		$('#deptNameHidden').val(deptAndAreaName);
	}
	//加载栏目数据
	$.ajax({
		url:'<%=basePath %>sys/userMenuDataJuris/queryDataJurisMenutree.action',
		type:"post",
		async:false,
		success: function(dataMap) {
			menuInfo=dataMap;
		}
	});
	
	function objDom() {
		this.init()
	}
	
	window.objDomPrototype = objDom.prototype = {
		constructor: objDom,
		resData : {}
	}
	objDomPrototype.init = function() {
		//保存的数据
		var menuId="${id}";
		if(menuId!=null&&menuId!=''){
			$("body").setLoading("body");
			var menuDate=JSON.parse('${editMenuDate}');
			//回显栏目数据
			var menuStr='<span code="'+ menuDate.menuId +'" class="selectSpanUser" alias="'+menuDate.menuAlias+'" haveson="1">'+ menuDate.menuName +'</span>';
			$('#menu').html(menuStr);
			var deptArr=menuDate.jurisCode.split(',');
			if(menuDate.jurisType==2){//科室
				$('#district span').removeClass('active');
				$($('#district span')[parseInt(menuDate.jurisType)-1]).addClass('active')
				$('#dept').html(null);
				var deptName=menuDate.jurisCodeName.split(',');
				var deptStr='';
				for(var i=0;i<deptArr.length;i++){
					deptStr += '<span code="'+ deptArr[i]+'" class="selectSpanUser">'+ deptName[i] +'</span>';
				}
				$('#dept').html(deptStr);
			}else{
				if(menuDate.jurisCode=='ALL'){
					$("#dept span[name='areaSelect']").removeClass('unselectSpanUser');
					$("#dept span[name='areaSelect']").addClass('iSselectSpanUser');
				}else{
					for(var i=0;i<deptArr.length;i++){
						var selectSelf=$("#dept span[name='areaSelect'][code='"+deptArr[i]+"']");
						selectSelf.removeClass('unselectSpanUser');
						selectSelf.addClass('iSselectSpanUser');
					}
				}
				$('#deptCodeHidden').val(menuDate.jurisCode);
				$('#deptNameHidden').val(menuDate.jurisCodeName);
			}
			$("body").rmoveLoading ("body");
		}
		//获取数据回调
		this.getData(this.callback)
	},
	//获取数据
	objDomPrototype.getData = function(callback) {
		callback.call(this, '')
	}
	//初始化回调
	objDomPrototype.callback = function(data) {
		this.data = data
		//渲染数据
		this.Setevent()
	}
	
	objDomPrototype.Setevent = function() {
		var self = this
		//点击变颜色事件
		$("#titleMove").on("click", ".selectSpan", function() {
			var $this = $(this)
			$this.parent().find('.selectSpan.active').removeClass("active")
			$this.addClass("active")
		})
			
			
		$("#district").on("click",".selectSpan",function(){
			var $this = $(this)
			$this.parent().find('.selectSpan.active').removeClass("active")
			$this.addClass("active")
			$('#dept').html(null);
			if($.trim($this.html()) == "科室"){
				$('#deptSelect').show();
			}else{
				$('#deptSelect').hide();
				huixianArea();
			}
			
		})	
		//展开收缩
		$(".blooming").on("click", function() {
			var dom = $(this).parents(".table-content-li")
			if(dom.hasClass("isblooming")) {
				dom.removeClass("isblooming")
				$(this).html("收起")
			} else {
				dom.addClass("isblooming")	
				$(this).html("展开")
			}
		})
		$(".isclear").on("click",function(){
			var self=$(this).parents(".li-content");
			self.find("span.selectSpanUser").remove();
			var divId=$(self.find(".persBox")).attr('id');
			$("#" + divId+"CodeHidden").val("");
			$("#" + divId+"CodeHidden").val("");
			$(this).hide()
		})
		//栏目选择
		$("#addMenu").on("click" , function(){
			window.userData = {}
			$('#pers>span').each(function(i,v){
				window.userData[$(v).attr("code")] = $(v).html()
			})
			$.dialog({
				href:"<%=basePath%>sys/userMenuDataJuris/selectMenuDateAndDept.action?id=menu&sign=menu",
				width:"500px",
			    height: "540px",
			    title:"栏目选择"
			})
		})
		//科室选择
		$("#addDept").on("click" , function(){
			window.userData = {}
			$('#pers>span').each(function(i,v){
				window.userData[$(v).attr("code")] = $(v).html()
			})
			$.dialog({
				href:"<%=basePath%>sys/userMenuDataJuris/selectMenuDateAndDept.action?id=dept&sign=dept",
				width:"500px",
			    height: "540px",
			    title:"授权选择"
			})
		})
		//权限类型操作
		$("#dept").on('click',"span[name='areaSelect']",function(){
			if($(this).hasClass('unselectSpanUser')){
				$(this).removeClass('unselectSpanUser');
				$(this).addClass('iSselectSpanUser');
			}else{
				$(this).removeClass('iSselectSpanUser');
				$(this).addClass('unselectSpanUser');
			}
			areaCodeList();
		});
		$("#dept").on('click',"span.selectSpanUser",function(){
				$(this).remove();			
		});
		$("#menu").on('click',"span.selectSpanUser",function(){
				$(this).remove();			
		});
		//clear
		$("#clearW").on("click",function(){
			$.confirm("提示","确定关闭窗口",function(){
				window.close();	
			})
		})
		//保存
		$("#save").on("click",function(){
				self.setSaveData();
		})
	}
	//保存数据处理
	objDomPrototype.setSaveData = function(){
		var deptAndAreaCode='';
		var deptAndAreaDateName='';
		$('#dept span.selectSpanUser').each(function(index,obj){
			if(deptAndAreaCode!=''){
				deptAndAreaCode += ',';
			}
			if(deptAndAreaDateName!=''){
				deptAndAreaDateName += ',';
			}
			deptAndAreaCode += $(this).attr('code');
			deptAndAreaDateName += $.trim($(this).html());
		});
		if(deptAndAreaCode!=null&&deptAndAreaCode!=''){
			$('#deptCodeHidden').val(deptAndAreaCode);
			$('#deptNameHidden').val(deptAndAreaDateName);
		}
		var menuCode='';
		$('#menu span.selectSpanUser').each(function(index,obj){
			if(menuCode!=''){
				menuCode += ',';
			}
			menuCode+="{'id':'"+$(this).attr('code')+"','text':'"+$(this).html()+"','alias':'"+$(this).attr('alias')+"','haveson':'"+$(this).attr('haveson')+"'}";
		});
		if(menuCode!=null&&$.trim(menuCode)!=''){
			deptAndAreaCode=$('#deptCodeHidden').val();
			if(deptAndAreaCode!=null&&$.trim(deptAndAreaCode)!=''){
				deptAndAreaDateName=$('#deptNameHidden').val();
				opener.menuListMapDate=eval("["+menuCode+"]");
				opener.deptAndAreaDateCode=deptAndAreaCode;
				opener.deptAndAreaDateName=deptAndAreaDateName;
				if($.trim($('#district span.active').html())=='院区'){
					opener.jurisTypeName='院区';
					opener.jurisTypeCode=1;
				}else{
					opener.jurisTypeName='科室';
					opener.jurisTypeCode=2;
				}
				opener.location="javascript:confList();";
				//获取是否进行移动端授权
				if($.trim($('#titleMove span.active').html())=='是'){
					opener.isMobileSyn=1;
				}else{
					opener.isMobileSyn=2;
				}
				window.close();
			}else{
				$.alert("提示","请选择权限名称!")
			}
		}else{
			$.alert("提示","请选择栏目!");
		}
	}
	new objDom()
</script>

		