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
<title>显示表单视图</title>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/themes/cform/styles/xform.css" />
		<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/themes/cform/xform-all.js?v=1.14"></script>
		<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/javascript/js/jquery.form.min.js"></script>
		<style type="text/css">
			.requi {
				background-color: #fff3f3 !important;
			}
			* {
				padding: 0;
				margin: 0;
				box-sizing: border-box;
			}
			.table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th, .table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th, .table-bordered>thead>tr>td, .table-bordered>thead>tr>th {
			    border: 1px dashed #e2e2e2;
			}
			table .xf-handler .disable {
				border: none;
				box-shadow: none;
				background: #FFFFFF;
				background-color: #FFFFFF !important;
			}
			body, html {
				background-color: #FFFFFF;
			}
			body{
			    max-width: 1300px;
    			margin: 0 auto;
			}
			.xf-handler {
			    cursor: default;
			}
			#FromLoad {
				width: 100% !important;
				height: 100% !important;
				position: fixed !important;
			}
			.contentLoad {
				top: 50% !important;		
			}
			.chilentTableItem .requi{
				background-color: #fff !important;
			}
		</style>
</head>
<body>

<div id="m-main" class="col-md-12" style="padding-top:10px;padding-bottom: 60px;">

<!--<table width="100%" cellspacing="0" cellpadding="0" border="0" align="center" class="xf-table">
  <tbody>
    <tr>
	  <td width="25%" class="xf-cell xf-cell-right xf-cell-bottom xf-cell-top xf-cell-left">
	    <label style="display:block;text-align:right;margin-bottom:0px;padding-top:10px;padding-bottom:10px;">下个环节&nbsp;</label>
	  </td>
	  <td width="75%" class="xf-cell xf-cell-right xf-cell-bottom xf-cell-top" colspan="3" rowspan="1">
	    <div id="nextStep"></div>
	  </td>
	</td>
  </tbody>
</table>-->
  <script>
<%--  		  $.getJSON('<%=basePath%>activiti/humanTask/findNextActivities.action', { --%>
// 			  processDefinitionId: '${formVo.processDefinitionId}',
// 			  activityId: '${formVo.activityId}'
// 		  }, function(data) {
// 				  $('#nextStep').append(data[0].name);
// // 			  for (var i = 0; i < data.length; i++) {
			  	 
// // 			  }
// 		  }); 
 </script> 
      <form id="xform" method="post"  class="xf-form" enctype="multipart/form-data">
<input id="processDefinitionId" type="hidden" name="processDefinitionId" value="${formVo.processDefinitionId}">
<input id="bpmProcessId" type="hidden" name="id" value="${bpmProcessId}">
<input id="businessKey" type="hidden" name="businessKey" value="${businessKey}">

		<div id="xf-form-table"></div>
	  </form>

    </div>
	<!-- end of main -->

    <form id="f" action="form-template-save.do" method="post" style="display:none;">
	  <textarea id="__gef_content__" name="content">${xform.content}</textarea>
	</form>


<div class="navbar navbar-default navbar-fixed-bottom" style="z-index: 0;">
  <div class="container-fluid">
    <div class="text-center" style="padding-top:8px;">

	    <c:forEach var="item" items="${buttons}">
		<button id="${item.name}" type="button" class="btn btn-theme" onclick="${item.name}(this)">${item.lable}</button>
		</c:forEach>
	
	</div>
  </div>
</div>
<script type="text/javascript">
//禁用backspace键的后退功能，但是可以删除文本内容
document.onkeydown = check;
function check(e) {
    var code;
    if (!e) var e = window.event;
    if (e.keyCode) code = e.keyCode;
    else if (e.which) code = e.which;
if (((event.keyCode == 8) &&                                                    //BackSpace 
         ((event.srcElement.type != "text" && 
         event.srcElement.type != "textarea" && 
         event.srcElement.type != "password") || 
         event.srcElement.readOnly == true)) || 
        ((event.ctrlKey) && ((event.keyCode == 78) || (event.keyCode == 82)) ) ||    //CtrlN,CtrlR 
        (event.keyCode == 116) ) {                                                   //F5 
        event.keyCode = 0; 
        event.returnValue = false; 
    }
return true;
}
var checkBoxDox = [] //不是必填dom
xform = new xf.Xform('xf-form-table');
xform.doImport('${formVo.content}', function() {
	var data = JSON.parse('${formVo.properties}');
//	$("#xform .xf-handler").each(function(i, v) {
	for(var domKey in data){
		var dom = $('[name='+ domKey +']')
		var tmp =  data[domKey]
//		var dom = $(v).find("input,select,textarea")
//		var tmp = data[dom.attr("name")]
		if(dom.length>0) {
			if(tmp["display"]) {
				for(var key in tmp) {
					if(key == "readonly" && tmp[key]) {
						dom.attr("readonly", true).attr("onclick","").addClass("disable")
						if(dom.hasClass("chilentTabletext")){
							dom.parent().find("tr input").attr({
								"onclick":"",
								"disabled":"disabled",
								"onchange":""
							}).off("click").off("change")	
							dom.parent().find("i").css("display","none")
						}else if(dom.hasClass("objDataType")){
							dom.next().attr({"onclick":"","disabled":"disabled"})
						}
					} else {
						dom.attr(key, tmp[key])
					}
				}
				if(tmp["required"]) {
					dom.on("change", function() {
						var $this = $(this)
						if(this.value != "") {
							$this.removeClass("requi").attr("required", true);
						} else {
							$this.addClass("requi").attr("required", false);
						}
					});
//					if(dom.val() == "") {
					if(dom[0].tagName != "SELECT"){
						dom.addClass("requi");
					}
//					}
					if(dom.length > 1 && dom[0].type == "radio"){
						$(dom[0]).attr("checked","checked")
						dom.removeClass("requi")
					}
					if(dom.length > 1 && dom[0].type == "checkbox"){
						dom.addClass("requi").off("change").on("change",function(){
							var domChilent = $(this).parent().find("input")
							domChilent.addClass("requi");
							domChilent.each(function(i,v){
								if($(v).is(':checked')){
									domChilent.removeClass("requi");
									return false
								}
							})
						})
					}
				}else{
					if(dom[0].type == "checkbox"){
						checkBoxDox.push(dom)
					}
				}
			} else {
				dom.attr("disabled","disabled")
				if(dom.hasClass("chilentTabletext")){
					dom.parent().find("tr input").attr({
						"onclick":"",
						"disabled":"disabled",
						"onchange":""
					}).off("click").off("change")	
					dom.parent().find("i").css("display","none")
				}else if(dom.hasClass("objDataType")){
					dom.next().attr({"onclick":"","disabled":"disabled"})
				}
			}
		}
	}
})
var noSelf = {
	dept: true,
	appType: true,
	title: true,
	post: true,
	name: true,
	people: true,
	applicant: true,
	selfName: true,
	selfDept: true,
	selfBirthday: true,
	selfSex: true,
	birthday: true,
	leaveCode: true,
	jobNo: true,
	age:true,
	education:true
}
var tmp = JSON.parse('${initMap}')
for(var key in tmp){
	var arr = tmp[key]
	var dom = $('[name='+ key +']') 
	dom.val(arr).removeClass("requi")
	if(noSelf[key]){
		dom.removeClass("requi").attr("readonly","readonly")
		if(dom.is("select")){
			var selectDomName = dom.attr("name") 
			dom.parent().html('<input class="form-control disable" type="text" name="'+selectDomName+'" value="'+arr+'"  style="margin-bottom:0px;"  readonly="readonly" >')
		}
	}
}

var formJsonData = ${json} ;
for(var key in formJsonData) {
	var dom = $("[name="+ key +"]")
		if(dom.length>0) {
			if(dom.attr("type") == "radio"){
				dom.parent().find("[value='"+ formJsonData[key] +"']").attr("checked","checked")
				dom.parent().find("[name="+ key +"]").removeClass("requi")
			}else if (dom.attr("type") == "checkbox"){
				var checkboxArr = formJsonData[key].split(",")
				for (var i = 0 ;i<checkboxArr.length;i++) {
					dom.parent().find("[value='"+ checkboxArr[i] +"']").attr("checked","checked")
				}
				dom.parent().find("[name="+ key +"]").removeClass("requi")
			}else if (dom.attr("type") == "file" ){
				if(formJsonData[key]){
					dom.removeClass("requi")
					var fileData = JSON.parse(formJsonData[key])
					var str = ""
					for(var i = 0 ;i<fileData.length ;i++){
						str += "<a style='display:block'  href = "+ fileData[i]["url"] +" download="+ fileData[i]["name"] +" >"+ fileData[i]["name"] +"</a>"
					}
					dom.parent().append(str)
				}
			}else if (dom.hasClass("chilentTabletext")){
				var box = dom.parents(".chilentTableBox")
				var $chilentTable = box.find(".chilentTableItem")
				var str = "";
				var itemType = dom.attr("itemType").split(",")
				var chilentTableData = JSON.parse(formJsonData[key]).data
				for (var i = 0 ;i<chilentTableData.length;i++) {
					addchilentTableTr(dom[0])
				 	$chilentTable.find("tr:last-child input.chilentDataInput").each(function(j,v){
				 		$(v).val(chilentTableData[i][j]).attr("value",chilentTableData[i][j])
				 		if($(v).hasClass("objDataType")){
				 			var chilentstr = ""
				 			try{
				 				var chilentData = JSON.parse(chilentTableData[i][j])
				 			}catch(e){
				 				var chilentData = {}
				 				chilentstr = ","
				 			}
				 			for (var key in chilentData) {
				 				chilentstr += chilentData[key] + ","
				 			}
				 			chilentstr = chilentstr.substr(0,chilentstr.length-1)
				 			$("#"+v.id.substr(0,v.id.length-4)).val(chilentstr).attr("value",chilentstr)
				 		}
				 	})
				}
				$chilentTable.find("tr:nth-child(2)").remove()
				dom.val(formJsonData[key]).removeClass("requi")
			}else if (dom.hasClass("objDataType")){
				if(formJsonData[key] && formJsonData[key]!="{}"){
					dom.val(formJsonData[key]).removeClass("requi").removeClass("requi")
					var dataObj = JSON.parse(formJsonData[key])
					var str = ""
					for(var key in dataObj){
						str+=dataObj[key]+","
					}
					str = str.substr(0,str.length-1)
					dom.next().val(str).removeClass("requi")
				}
			}else{
				dom.val(formJsonData[key]).removeClass("requi")
				if(noSelf[key]){
					dom.removeClass("requi").attr("readonly","readonly")
					if(dom.is("select")){
						var selectDomName = dom.attr("name") 
						dom.parent().html('<input class="form-control disable" type="text" name="'+selectDomName+'" value="'+formJsonData[key]+'"  style="margin-bottom:0px;"  readonly="readonly" >')
					}
				}
			}
		}
}
$("[name=num],[name=sum]").attr("readonly","readonly")
var formTitle = $('[name=formTitle]')
if(formTitle.val() != ""){
	var h1Text = formTitle.next()[0].innerText
	formTitle.next().find("select").val(formTitle.val().replace(h1Text,"")).attr("name","formTitle")
	formTitle.attr("name","")
}

function confirmStartProcess(self){
	//子表重新计算
	$(".chilentTabletext").each(function(i,v){
		ChilentTableEven($(v).parents(".chilentTableBox").find(".chilentTableItem"))
	})
	var tmp = true;
	$('#xform').attr('action', '<%=basePath%>activiti/operation/startProcessInstance.action');
	$("#xform .xf-handler").each(function(i, v) {
		if($(v).children().hasClass("requi")) {
			tmp = false
			$.alert('提示', $(v).children(".requi").attr('item')+"为必填项!");
			return false
		}
	})
	if(tmp) {
		expsChebox(checkBoxDox) // 处理checkbox后台数据无法覆盖
		$(self).html("处理中···").attr("disabled","disabled")
		$("#saveDraft").attr("disabled","disabled")
		$("html").setLoading({
			id: "FromLoad",
			text: "请稍等...",
		})
		$('#xform').ajaxSubmit(function(message) {
			$(self).attr("disabled",false).html("提交申请")
			$("#saveDraft").attr("disabled",false)
			$("html").rmoveLoading("FromLoad") 
			if(message.resMsg == "success"){
				$.alert('提示','提交申请成功！',function(){
					load(1);
				});
				setTimeout("load(1)",2000);
			}else{
				$.alert('提示','提交申请失败！');
			}
		});
	} else {
//		$.alert('提示', '验证未通过,请重新填写!');
	}
	return false;
}
function load(flag){
	window.close();
	opener.xuanzhongSubmit(flag);
}
function saveDraft(self) { //保存草稿
	//子表重新计算
	$(".chilentTabletext").each(function(i,v){
		ChilentTableEven($(v).parents(".chilentTableBox").find(".chilentTableItem"))
	})
	$("html").setLoading({
		id: "FromLoad",
		text: "请稍等...",
	}) 
	$(self).html("处理中···").attr("disabled","disabled")
	$("#confirmStartProcess").attr("disabled","disabled")
	$('#xform').attr('action', '<%=basePath%>activiti/operation/saveDraft.action');
	$('#xform').ajaxSubmit(function(message) {
		$(self).attr("disabled",false).html("保存草稿")
		$("#confirmStartProcess").attr("disabled",false)
		$("html").rmoveLoading("FromLoad") 
		if(message.resMsg == "success"){
			$.alert('提示','保存草稿成功！',function(){
				load(2);
			});
			setTimeout("load(1)",2000);
		}else{
			$.alert('提示','保存草稿失败！');
		}
	});
}
//checkbox数据
function expsChebox (domArr){
	var tmp = false
	for (var i = 0 ;i<domArr.length;i++) {
		domArr[i].each(function(i,v){
			if($(v).is(':checked')){
				tmp = true
				return false
			}
		})
		if(tmp == false){
			$("#xform").append('<input type="hidden"  name="'+ domArr[i][0].name +'" value="">')
		}
		tmp = false
	}
}
</script>

</body>
</html>