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
		<title></title>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/themes/cform/styles/xform.css" />
		<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/themes/cform/xform-all.js?v=1.13"></script>
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
				    /* left: 621px; */
				    /* top: 516px; */
				    z-index: 9012;
				    position: fixed !important;
			}
			.window-shadow {
				display: none !important;
			}
			.table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th, .table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th, .table-bordered>thead>tr>td, .table-bordered>thead>tr>th {
			    border: 1px dashed #e2e2e2;
			}
			body, html {
				background-color: #FFFFFF;
			}
			body{
			    max-width: 1200px;
    			margin: 0 auto;
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
			.deltableTrBtn{
				display: none;
			}
			.addtableTrBtn {
				display: none;
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
			<form id="xform" method="post" class="xf-form" enctype="multipart/form-data">
				<div id="xf-form-table">
					<!--/form-->
				</div>
				<input type="hidden" id="humanTaskId" name="humanTaskId" value="${humanTaskId}">
				<input type="hidden" id="zkhonryState" name="zkhonryState" value="0">
				<input type="hidden" id="nextAssignee" name="nextAssignee">
				<input type="hidden" id="rollbackActivityId" name="rollbackActivityId">
			</form>
			<div align="center">
				<br>
				
				<%-- <c:forEach var="item" items="${buttons}">
					<button id="${item.name}" type="button" class="btn btn-default" onclick="${item.name}()">${item.lable}</button>
				</c:forEach> --%>
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
<%-- 							<c:if test="${not empty item.completeTime}"> --%>
								<tr>
									<td>${item.name}</td>
									  <td><fmt:formatDate value="${item.createTime}" type="both"  pattern="yyyy-MM-dd HH:mm:ss"/></td>
									  <td><fmt:formatDate value="${item.completeTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									  <td>${item.assigneeName}</td>
									  <td>${item.lastModifierName}</td>
									  <td>${item.action}</td>
								</tr>
<%-- 							</c:if> --%>
						</c:forEach>
					</tbody>
				</table>
			
				<input type="button" class="btn btn-theme" id="btnPrint" value="打印"/>
				<br>
				<br>
			</div>
		</div>
 
	</body>

</html>

<script type="text/javascript">
	xform = new xf.Xform('xf-form-table');
	xform.doImport('${formVo.content}', function() {
		$("#xform .xf-handler").each(function(i, v) {
			var dom = $(v).find("input,select,textarea").attr("disabled","disabled").addClass("disable")
		})
	})
	var formJsonData=${json};
	for(var key in formJsonData) {
		var dom = $("[name="+ key +"]")
		if(dom.length>0){
			if(dom.attr("type") == "radio"  ){
				dom.parent().find("[value="+ formJsonData[key] +"]").attr("checked","checked")
				dom.parent().find("[name="+ key +"]").removeClass("requi")
			}else if (dom.attr("type") == "checkbox"){
				var checkboxArr = formJsonData[key].split(",")
				for (var i = 0 ;i<checkboxArr.length;i++) {
					dom.parent().find("[value='"+ checkboxArr[i] +"']").attr("checked","checked")
				}
				dom.parent().find("[name="+ key +"]").removeClass("requi")
			}else if (dom.attr("type") == "file" ){
				if(formJsonData[key]){
					dom.css("display","none").removeClass("requi")
					dom.parent().children("button").css("display","none")
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
　　						window.close();
					});
			　　}
				dom.val(setTargetData.text).removeClass("requi").attr("value",setTargetData.text)
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
				$chilentTable.find("tr input").attr({
					"onclick":"",
					"disabled":"disabled",
					"onchange":""
				}).off("click").off("change")
				box.find(".addtableTrBtn").css("display","none")
				box.find(".deltableTrBtn").css("display","none")
				dom.val(formJsonData[key]).removeClass("requi")
			}
			else if (dom.hasClass("objDataType")){
				if(formJsonData[key] && formJsonData[key]!="{}"){
					dom.val(formJsonData[key]).removeClass("requi").removeClass("requi")
					var dataObj = JSON.parse(formJsonData[key])
					var str = ""
					for(var key in dataObj){
						str+=dataObj[key]+","
					}
					str = str.substr(0,str.length-1)
					dom.next().val(str).removeClass("requi").attr("value",str)
				}
			}else{
				dom.val(formJsonData[key]).removeClass("requi")
				dom.attr("value",formJsonData[key])
			}
		}
	}
	var formTitle = $('[name=formTitle]')
	if(formTitle.val() !=""){
		formTitle.next().html(formTitle.val()) 
		formTitle.attr("name","")
	}
		var print;
        var printAreaCount = 0;
        $.fn.printArea = function (url) {
           var ele = $(this);
           var idPrefix = "printArea_";
           removePrintArea(idPrefix + printAreaCount);
           printAreaCount++;
           var iframeId = idPrefix + printAreaCount;
           var iframeStyle = 'position:absolute;width:100%;height:100%;left:-100%;top:-100%;';
           iframe = document.createElement('IFRAME');
           $(iframe).attr({
               style: iframeStyle,
               id: iframeId
           });
           document.body.appendChild(iframe);
           var doc = iframe.contentWindow.document;
           doc.open();
           $(document).find("link").filter(function () {
               return $(this).attr("rel").toLowerCase() == "stylesheet";
           }).each(function () {
               doc.write('<link type="text/css" rel="stylesheet" href="'+ $(this).attr("href") + '" >');
           });
           url && (doc.write('<link type="text/css" rel="stylesheet" href="'+ url + '" >'))	
           doc.write($(ele).html());
           doc.close();
           var frameWindow = iframe.contentWindow;

           $(doc).find("select,input[type=text],textarea").each(function(i,v){
           		$(this).after("<span>"+ ($(this).attr("value")||'') +"</span>")
           		$(this).remove()
           })
           $(doc).find(".xf-handler.radio.radio-theme").each(function(i,v){
           		var value = ($(this).find("input[type=radio]:checked").val() || "")
           		$(this).html(null).append("<span>"+ value +"</span>")
           })
           $(doc).find(".xf-handler.checkbox.checkbox-theme").each(function(i,v){
           		var value = ($(this).find("input[type=checkbox]:checked").val() || "")
           		$(this).html(null).append("<span>"+ value +"</span>")
           })
           PageSetup_Null()
           frameWindow.close();
           frameWindow.focus();
           print = frameWindow
        }
        var removePrintArea = function (id) {
            $("iframe#" + id).remove();
        };
       
        var HKEY_Root,HKEY_Path,HKEY_Key; 
		HKEY_Root="HKEY_CURRENT_USER"; 
		HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"; 
		//设置网页打印的页眉页脚为空 
		function PageSetup_Null(){ 
			try { 
				var Wsh=new ActiveXObject("WScript.Shell"); 
				HKEY_Key="header"; 
				Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,""); 
				HKEY_Key="footer"; 
				Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,""); 
			} 
			catch(e){} 
		} 
	    $("#xf-form-table").printArea("${pageContext.request.contextPath}/baseframe/page/print/print.css?_="+Math.random()); 
	    $("#btnPrint").click(function(){
	    	print.print();
	    });
</script>