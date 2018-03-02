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
		<title>流程审批</title>
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
			.panel.window.messager-window{
				    left: 50% !important;
				    margin-left: -100px !important;
				    top: 50% !important;
				    margin-bottom: -50ox !important;
				    display: block;
				    width: 300px;
				    z-index: 9012;
				    position: fixed !important;
			}
			.window-shadow {
				display: none !important;
			}
			
			.expressions {
				padding: 10px;
				float: left;
				cursor: pointer;								
			}
			.table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th, .table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th, .table-bordered>thead>tr>td, .table-bordered>thead>tr>th {
			    border: 1px dashed #e2e2e2;
			}
			body, html {
				background-color: #FFFFFF;
			}
			table .xf-handler .disable {
				border: none;
				box-shadow: none;
				background: #FFFFFF;
				background-color: #FFFFFF !important;
			}
			body{
			    max-width: 1300px;
    			margin: 0 auto;
			}
			#m-main{
				min-width: 1130px;
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

		<div id="m-main" class="col-md-12" style="padding-top:10px;">

			<c:if test="${not empty children}">
				<div class="alert alert-info" role="alert">
					<c:forEach var="item" items="${children}">
						<p>
							${item.catalog == 'communicate' ? '沟通反馈' : ''}
							<tags:user userId="${item.assignee}" />
							<fmt:formatDate value="${item.completeTime}" type="both" /> ${item.comment}
						</p>
					</c:forEach>
				</div>
			</c:if>

			<c:if test="${humanTask.catalog != 'communicate'}">
				<table width="100%" cellspacing="0" cellpadding="0" border="0" align="center" class="xf-table">
					<tbody>
						<tr>
							<td width="25%" class="xf-cell xf-cell-right xf-cell-bottom xf-cell-top xf-cell-left">
								<label style="display:block;text-align:right;margin-bottom:0px;padding-top:10px;padding-bottom:10px;">上个环节&nbsp;</label>
							</td>
							<td width="25%" class="xf-cell xf-cell-right xf-cell-bottom xf-cell-top" colspan="1" rowspan="1">
								<div id="previousStep"></div>
							</td>
							<td width="25%" class="xf-cell xf-cell-right xf-cell-bottom xf-cell-top xf-cell-left">
								<label style="display:block;text-align:right;margin-bottom:0px;padding-top:10px;padding-bottom:10px;">下个环节&nbsp;</label>
							</td>
							<td width="25%" class="xf-cell xf-cell-right xf-cell-bottom xf-cell-top" colspan="1" rowspan="1">
								<div id="nextStep"></div>
							</td>
							</td>
					</tbody>
				</table>
				<script>
					$.getJSON('<%=basePath%>activiti/humanTask/findPreviousActivities.action', {
						processInstanceId: '${humanTask.processInstanceId}',
						humanTaskId: '${humanTask.taskId}'
					}, function(data) {
						$('#previousStep').append('&nbsp;').append(data.name);
						
					});
				</script>

				<script>
				var nextActiviti=null;
					$.getJSON('<%=basePath%>activiti/humanTask/findNextActivities.action', {
						processDefinitionId: '${formVo.processDefinitionId}',
						processInstanceId: '${humanTask.processInstanceId}',
						humanTaskId: '${humanTask.taskId}',
						activityId: '${formVo.activityId}'
					}, function(data) {
						$('#nextStep').append('&nbsp;').append(data[0].name);
						nextActiviti=data[0].id
// 						for(var i = 0; i < data.length; i++) {
// 							$('#nextStep').append(data[i].name).append('&nbsp;');
// 						}
					});
				</script>
			</c:if>

			<form id="xform" method="post" class="xf-form" enctype="multipart/form-data">
				<div id="xf-form-table">
					<!--/form-->
				</div>
				<input type="hidden" id="humanTaskId" name="humanTaskId" value="${humanTaskId}">
				<input type="hidden" id="zkhonryState" name="zkhonryState" value="0">
				<input type="hidden" id="nextAssignee" name="nextAssignee">
				<input type="hidden" id="rollbackActivityId" name="rollbackActivityId">
			</form>
			<div id = "btnBox" align="center">
				<br>
				
				<c:forEach var="item" items="${buttons}">
					<button id="${item.name}" type="button" class="btn btn-theme" onclick="${item.name}(this)">${item.lable}</button>
				</c:forEach>
				<table width="100%" cellspacing="0" cellpadding="0" border="0" align="center" class="table table-border" style="margin-top: 10px;margin-bottom: 10px;">
					<thead>
						<tr>
							  <th style="width: 15%">环节</th>
							  <th style="width: 16%">到达时间</th>
							  <th style="width: 16%">完成时间</th>
							  <th style="width: 20%">负责人</th>
							  <th style="width: 18%">操作人</th>
							  <th style="width: 15%">结果</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${logHumanTask}">
							<c:if test="${not empty item.completeTime}">
								<tr>
									<td>${item.name}</td>
									  <td><fmt:formatDate value="${item.createTime}" type="both"  pattern="yyyy-MM-dd HH:mm:ss"/></td>
									  <td><fmt:formatDate value="${item.completeTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									  <td>${item.assigneeName}</td>
									  <td>${item.lastModifierName}</td>
									  <td>${item.action}</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
<!-- 指定负责人--Modal -->
<div class="modal fade" id="modelUl" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">设置</h4>
      </div>
      <div class="modal-body clearfix">
		<label class="col-lg-3 control-label">设置名称</label>
		<div class="col-lg-7">
			<select class="form-control" id="moudelName" name="moudelName"></select>
		</div>
      </div>
      <div class="modal-footer">
		<button id="saveren" type="button" class="btn btn-theme"><span class="ladda-label">确定</span></button>
		<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	  </div>
    </div>
  </div>
</div>
<!-- 指定回退节点-->
<div class="modal fade" id="modelAct" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">设置</h4>
      </div>
      <div class="modal-body clearfix">
		<label class="col-lg-3 control-label">指定步骤</label>
		<div class="col-lg-7">
			<select class="form-control" id="moudelActName" name="moudelActName"></select>
		</div>
      </div>
      <div class="modal-footer">
		<button id="saveAct" type="button" class="btn btn-theme"><span class="ladda-label">确定</span></button>
		<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	  </div>
    </div>
  </div>
</div>
	</body>

</html>
<script type="text/javascript">
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
		shortPhone:true,
		education:true
	}
 
	var expressionsDom = [] //常用语Dom
	var checkBoxDox = [] //不是必填dom
	var statusData=""
	xform = new xf.Xform('xf-form-table');
	xform.doImport('${formVo.content}', function() {
		var data = JSON.parse('${formVo.properties}');
		statusData = data
		for(var domKey in data){
			var dom = $('[name='+ domKey +']')
			var tmp =  data[domKey]
			if(dom.length>0) {
				if(tmp["display"]) {
					for(var key in tmp) {
						if(key == "readonly") {
							if(tmp[key]){
								dom.attr("disabled", true)
								if(tmp[key]){
									dom.addClass("disable")
								}
								if(dom.hasClass("chilentTabletext")){
									dom.parent().find("tr input").attr({
										"onclick":"",
										"disabled":"disabled",
										"onchange":""
									}).off("click").off("change")	
									dom.parent().children("a").css("display","none")
								}else if(dom.hasClass("objDataType")){
									dom.next().attr({"onclick":"","disabled":"disabled"}).addClass("disable")
								}
							}else{
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
						if(dom[0].tagName != "SELECT"){
							dom.addClass("requi");
						}
						if(dom.length > 1 && dom[0].type == "radio"){
							$(dom[0]).attr("checked","checked")
							dom.removeClass("requi")
						}
						if(dom[0].type == "checkbox"){
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
						var reg = /Opinion/;
						if(reg.test( dom.attr("name")) && dom[0].tagName.toLocaleLowerCase() == "textarea") {
							expressionsDom.push(dom) 
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
	
	$.ajax({
		type:"post",
		url:"${pageContext.request.contextPath}/oa/commonLg/findCommon.action",
		async:true,
		data:{
			tableCode:"${formVo.code}"
		},
		success:function(data){
			var dataInfo = data
			for (j = 0 ;j<expressionsDom.length;j++) {
				var str = ""
				for(var i = 0 ;i<dataInfo.length;i++){
					str +=	"<span class = 'expressions' onmousedown='expressionsFn(this,expressionsDom["+j+"])'>"+dataInfo[i]["text"]+"</span>"
				}
				str += "<span class = 'expressions' onmousedown='qianziFn(expressionsDom["+j+"])'>签字</span>"
				expressionsDom[j].popover({
				  	title:"常用语选择",
				  	html:true,
					content:str,
					placement:"top",
					trigger: 'manual'
				}).on("blur",function(){
					$(this).popover('hide')
				}).on("focus",function(){
					$(this).popover('show')
				})
			}
		}
	});
	
	var formJsonData=${json};
	var exData = ${json2}
	for(var key in formJsonData) {
		var dom = $("[name="+ key +"]")
		if(dom.length>0){
			if(dom.attr("type") == "radio" ){
				dom.parent().find("[value="+ formJsonData[key] +"]").attr("checked","checked")
				dom.parent().find("[name="+ key +"]").removeClass("requi")
			}else if (dom.attr("type") == "checkbox"){
				var checkboxArr = formJsonData[key].split(",")
				for (var i = 0 ;i<checkboxArr.length;i++) {
					var tagcheckboxDom = dom.parent().find("[value='"+ checkboxArr[i] +"']")
					var tagcheckboxOrder = tagcheckboxDom.attr("orderdata")
					tagcheckboxDom.prop("checked","checked")
					if(tagcheckboxOrder && tagcheckboxOrder!="undefined"){
						checkboxOrderData[key][tagcheckboxOrder] = true
					}
				}
				dom.parent().find("[name="+ key +"]").removeClass("requi")
			}else if (dom.attr("type") == "file" ){
				if(formJsonData[key]){
					dom.css("display","none").removeClass("requi")
					if(statusData[key]["readonly"]){
						dom.parent().children("button").css("display","none")
					}
					//statusData[key]
					var fileData = JSON.parse(formJsonData[key])
					var str = ""
					for(var i = 0 ;i<fileData.length ;i++){
						str += "<a style='display:block'  href = "+ fileData[i]["url"] +" download="+ fileData[i]["name"] +" >"+ fileData[i]["name"] +"</a>"
					}
					dom.parent().append(str)
				}
			}else if (dom.hasClass("spDom")){
				try {
			　　		var setTargetData = JSON.parse(formJsonData[key]) 
			　　} catch(error) {
　　					$.alert('提示',"数据异常,请联系管理员！",function(){
						load();
					});
			　　}
				dom.val(setTargetData.text).removeClass("requi")
				var userExamine = dom.parent().find(".userExamine")
				var imgZhangImg = dom.parent().find(".imgZhangImg")
				var imgZhangspan = dom.parent().find(".imgZhangspan")
				var imgnameImg = dom.parent().find(".imgnameImg")
				var imgnamespan = dom.parent().find(".imgnamespan")
				if(setTargetData.sign){
					userExamine.css("display","block")
					imgnameImg.attr("src",'${pageContext.request.contextPath}/'+ setTargetData.sign)
					if(dom.parents(".xf-handler").width()<800){
						imgnamespan.css({
						    position: "absolute",
						    right: "180px",
						    bottom: 0,
						})
						imgnameImg.css({
						    position: "absolute",
						    right: 0,
						    bottom: 0,
						    height: "100%"
						})
					}
				}else{
					imgnameImg.css("display","none")
					imgnamespan.css("display","none")
				}
				if(setTargetData.seal){
					userExamine.css("display","block")
					imgZhangImg.attr("src",'${pageContext.request.contextPath}/'+ setTargetData.seal)
					if(dom.parents(".xf-handler").width()<800){
						imgZhangspan.css({
						    position: "absolute",
						    right: "60px",
						    bottom: 0,
						})
						imgZhangImg.css({
						    "position": "absolute",
						    "right": 0,
						    "bottom": "-10%",
						    "height": "120%",
						    "zIndex":10
						})
					}
				}else{
					imgZhangImg.css("display","none")
					imgZhangspan.css("display","none")
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
//				box.find(".addtableTrBtn").css("display","none")
//				box.find(".deltableTrBtn").css("display","none")
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
			}
			if(noSelf[key]){
				dom.attr("readonly","readonly")
				if(dom.is("select")){
					var selectDomName = dom.attr("name") 
					dom.parent().html('<input class="form-control disable" type="text" name="'+selectDomName+'" value="'+formJsonData[key]+'"  style="margin-bottom:0px;"  readonly="readonly" >')
				}
			}
		}
	}
	var formTitle = $('[name=formTitle]')
	if(formTitle.val() != ""){
		var h1Text = formTitle.next()[0].innerText
		formTitle.next().html(formTitle.val()+h1Text) 
		formTitle.attr("name","")
	}
	for (var key in checkboxOrderData) {
		for(var vkey in checkboxOrderData[key]){
			var vkeyDom = $('[name = ' + vkey + ']')
			if(checkboxOrderData[key][vkey] == false && vkeyDom.length > 0){
				vkeyDom.removeClass("requi").attr("disabled","disabled")
				for (var j = 0 ;j<expressionsDom.length;j++) {
					if(expressionsDom[j].attr("name") == vkey){
						expressionsDom.splice(j,1);
						break;
					}
				}
			}
		}
	}
	for(var key in exData){
		var exDom = $("[name="+ key +"]") 
		if(exDom.length>0 && !exDom.attr("disabled")){
			exDom.val(exData[key]).removeClass("requi")
		}
	}
	$.ajax({
		type:"post",
		url:"${pageContext.request.contextPath}/oa/userSign/queryOaUserSigns.action",
		async:true,
		success:function(data){
			if(expressionsDom && expressionsDom.length>0){
				for(var index = 0 ;index<expressionsDom.length;index++){
					var target = expressionsDom[index].parent().find(".zhangBox")
					var tmp,i,str
					
					var imgZhangImg = target.find(".imgZhangImg")
					var imgZhangspan = target.find(".imgZhangspan")
					var imgnameImg = target.find(".imgnameImg")
					var imgnamespan = target.find(".imgnamespan")
				
					if(data.sign.length==1){
						imgnameImg.attr("src",data.sign[0].url+"?id="+data.sign[0].id+"&version="+data.sign[0].version)
						if(target.parents(".xf-handler").width()<800){
							imgnamespan.css({
							    position: "absolute",
							    right: "180px",
							    bottom: 0
							})
								imgnameImg.css({
								    position: "absolute",
								    right: 0,
								    bottom: 0,
								    height: "100%"
								})
							}
					}else if (data.sign.length==0){
						imgnameImg.css("display","none")
						imgnamespan.css("display","none")
					}
					if(data.seal.length==1){
						imgZhangImg.attr("src",data.seal[0].url+"?id="+data.seal[0].id+"&version="+data.seal[0].version)
						if(target.parents(".xf-handler").width()<800){
							imgZhangspan.css({
							    position: "absolute",
							    right: "60px",
							    bottom: 0,
							})
							imgZhangImg.css({
							    position: "absolute",
							    right: 0,
							    bottom: "-10%",
							    height: "120%",
							    "zIndex":10
							})
						}
					}
					else if(data.seal.length>1){
						tmp = data.seal
						str =  '<select onchange="SelectZhang(this)" class="imgZhangSelect">' 
						for (i = 0 ;i<tmp.length;i++) {
							str+='<option value='+ data.seal[i].url+"?id="+data.seal[i].id+"&version="+data.seal[i].version +' >'+ tmp[i].signName +'</option>'
						}
						str+='</select>'
						target.append(str)
						imgZhangImg.attr("src",data.seal[0].url+"?id="+data.seal[0].id+"&version="+data.seal[0].version)
						if(target.parents(".xf-handler").width()<800){
							imgZhangspan.css({
							    position: "absolute",
							    right: "60px",
							    bottom: 0,
							    height: "100%"
							})
							imgZhangImg.css({
							    position: "absolute",
							    right: 0,
							    bottom: "-10%",
							    height: "120%",
							    "zIndex":10
							})
						}
					}else if (data.seal.length==0){
						imgZhangImg.css("display","none")
						imgZhangspan.css("display","none")
					}
				}
				
				$(".userExamine").on("click",function(e){
					if(!$(e.target).hasClass("imgZhangSelect")){
						$(this).parent().children("textarea").focus()
					}
				})
			}
		}
	});	
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
	function expressionsFn (self,dom){
		var userparaMT = '${userparaMT}';//姓名 科室 职务
		var today = new Date()
		var restime = '('
		restime += today.getFullYear()+"年"
		restime += today.getMonth() + 1 < 10 ? "0" + (today.getMonth() + 1)+"月" : (today.getMonth() + 1)+"月"
		restime += today.getDate() < 10 ? "0" + today.getDate()+"日": today.getDate()+"日"
				+today.getHours()+"点"+today.getMinutes()+"分";
		restime += ")"
		dom.val(self.innerHTML+"["+userparaMT+restime+"]" ).popover('hide').removeClass("requi")
	}
	function noContentExpressionsFn (value){
		var userparaMT = '${userparaMT}';//姓名 科室 职务
		var today = new Date()
		var restime = '('
		restime += today.getFullYear()+"年"
		restime += today.getMonth() + 1 < 10 ? "0" + (today.getMonth() + 1)+"月" : (today.getMonth() + 1)+"月"
		restime += today.getDate() < 10 ? "0" + today.getDate()+"日": today.getDate()+"日"
				+today.getHours()+"点"+today.getMinutes()+"分";
		restime += ")"
		return value + "["+userparaMT+restime+"]";
	}
	function completeTask(back) { //完成任务
		var tmp = true;
		$('#xform').attr('action', '<%=basePath%>activiti/humanTask/completeTask.action');
		$("#xform .xf-handler").each(function(i, v) {
			if($(v).find("input[name!=''],textarea[name!='']:not(textarea[name$='Opinion']),select[name!='']").hasClass("requi")) {
				$.alert('提示', $(v).find(".requi").attr('item')+"为必填项!");
				tmp = false
				return false
			}
		})
		if(back == "back"){
			tmp = true
		}
		if(tmp) {
			var isOkSubmit = true
			if(expressionsDom && expressionsDom.length>0){
				for (var i = 0 ;i<expressionsDom.length;i++) {
					var targetName = expressionsDom[i].attr("name")
					var imgZhangImgSrc = subOAUrl(expressionsDom[i].parent().find(".imgZhangImg").attr("src"))
					var imgnameImgSrc = subOAUrl(expressionsDom[i].parent().find(".imgnameImg").attr("src"))
					var expressionsDomtext = expressionsDom[i].val().toString()
					if(expressionsDom[i].val() == ""){
						if(back == "back"){
							expressionsDomtext = noContentExpressionsFn("不同意")
						}else{
							expressionsDomtext = noContentExpressionsFn("同意")
						}
					}
					var targetValue = JSON.stringify({
							"text":expressionsDomtext,
							"seal":imgZhangImgSrc,
							"sign":imgnameImgSrc
						}).replace(/\s/g,"")
					$("#xform").append('<textarea  style="display: none;" name="'+ targetName +'" value = "'+ targetValue +'" >'+targetValue+'</textarea>')
					expressionsDom[i].attr("name",null)
					if (expressionsDom[i].parent().find(".userExamine").css("display") == "none"){
						//重要
						//isOkSubmit = false
						isOkSubmit = true
					}else{
					}
				}
			}
			if(isOkSubmit){
				expsChebox(checkBoxDox) // 处理checkbox后台数据无法覆盖
				expsfile()// 处理file后台数据无法覆盖
				$("#btnBox button").attr("disabled","disabled")
				$("html").setLoading({
					id: "FromLoad",
					text: "请稍等...",
				}) 
				$('#xform').ajaxSubmit(function(message) {
					$("#btnBox button").attr("disabled",false)
					$("html").rmoveLoading("FromLoad") 
					$(window.opener.parent.document).find("#homeDiv iframe")[0].contentWindow.daibanRes();
					$.alert('提示',message.resCode,function(){
						load();
					});
					setTimeout("load()",2000);
				});
			}else{
				$.alert('提示',"您的意见未签字提交失败！");
			}
		} else {
//			$.alert('提示', '验证未通过,请重新填写!');
		}
		return false;
	};
	function load(){
		homeRest()
		window.close();
		opener.xuanzhongSubmit(3);
	}
	
	function subOAUrl (str){
		var index = str.indexOf("oa/")
		if (index != -1){
			return str.substr(index)
		}else {
			return ""
		}
	}
	function saveDraft() { //保存草稿
		$("#btnBox button").attr("disabled","disabled")
		$("html").setLoading({
			id: "FromLoad",
			text: "请稍等...",
		}) 
		$('#xform').attr('action', '<%=basePath%>activiti/humanTask/saveDraft.action');
		$('#xform').ajaxSubmit({  
	        url :'<%=basePath%>activiti/humanTask/saveDraft.action',  
	        type : "post",
	        success : function(message) {  
	        	$("html").rmoveLoading("FromLoad") 
	        	$("#btnBox button").attr("disabled",false)
	            $.alert('提示',message.resCode,function(){
					load();
				});
				setTimeout("load()",2000);
	        },  
	        error : function(){ 
	        	$("html").rmoveLoading("FromLoad") 
	        	$("#btnBox button").attr("disabled",false)
				$.alert('提示', '网络异常');
	        } 
	    });
		return false;
	}

	function rollback() { //退回
		$.confirm("提示","确定要退回?",rollbackFn)
	}
	function rollbackFn (){
		$('#zkhonryState').val('1');
		completeTask("back");
	}
	
	function rollbackActivity(){ //逐级驳回
		$.getJSON('<%=basePath%>activiti/humanTask/findAllPreviousActivities.action',{
			processInstanceId: '${humanTask.processInstanceId}',
			activityId: '${formVo.taskId}'
		},function(data){
				var str = ""
				for (var i = 0; i < data.length; i++) {
					str += "<option value=" + data[i]['code'] + ">" + data[i]["name"] + "</option>"
				}
				$("#moudelActName").html(null).append(str)
				$('#modelAct').modal()
		});
	}
	$("#saveAct").on("click",function(){
		$("#rollbackActivityId").val($("#moudelActName").val())
		$('#modelAct').modal('hide')
		$('#zkhonryState').val('2');
		completeTask();
	})
	//记录功能
	var moudelNametmp;
	$("#moudelName").on("change",function(){
		moudelNametmp = this.value
	})
	function selectAssignee() { //指定负责人
		$.getJSON('<%=basePath%>activiti/humanTask/selectAssignee.action',{
			processDefinitionId: '${formVo.processDefinitionId}',
			processInstanceId:'${humanTask.processInstanceId}',
			activityId: '${formVo.activityId}',
			humanTaskId: '${humanTask.taskId}',
			businessKey:'${humanTask.businessKey}',
			nextActiviti:nextActiviti,
            humtaskCreateUser:'${humanTask.createUser}'
		}, function(data) {
			var str = ""
			for(var i = 0; i < data.length; i++) {
				str += "<option value=" + data[i]['employeeJobNo'] + ">" + data[i]["employeeName"] + "</option>"
			}
			$("#moudelName").html(null).append(str)
			if(moudelNametmp){
				$("#moudelName").val(moudelNametmp)
			}
			$('#modelUl').modal()
		});
	}
	$("#saveren").on("click", function() {
		$("#nextAssignee").val($("#moudelName").val())
		$('#modelUl').modal('hide')
	})
	
	function SelectZhang (self){
		var $self = $(self)
		$self.parent().find(".imgZhangImg").attr("src",$self.val())
	}
	
	function homeRest (){
		var parentWindow = window.opener
		var dom = parentWindow.$("#modelUl>li>div[moduleid=14]") 
		if(dom.length>0){
			parentWindow.setModle(dom.parent().attr("index"))
		}
	}
	//file覆盖
	function expsfile (){
		var f0 = $("input[name=fileReason]")
		var f1 = $("input[name=fileReason1]")
		if(f0.length>0){
			if (f0[0].files.length == 0){
				f0.attr("disabled","disabled")
			}
		}
		if(f1.length>0){
			if (f1[0].files.length == 0){
				f1.attr("disabled","disabled")
			}
		}
	}
</script>