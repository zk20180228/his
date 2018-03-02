<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

	<head>
		<script type="text/javascript" src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/js/jquery.min.js"></script>
		<jsp:include page="/baseframe/header/common/status.jsp"></jsp:include>
		<jsp:include page="/baseframe/header/bootstrap/bootstrapBase.jsp"></jsp:include>
		<jsp:include page="/baseframe/header/common/theme.jsp"></jsp:include>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/template.js"></script>
		<meta charset="UTF-8">
		<title>主题</title>
		<style type="text/css">
			* {
			    font-family: "微软雅黑";
			    -webkit-box-sizing: border-box;
			    -moz-box-sizing: border-box;
			    box-sizing: border-box;
			}
			.container-fluid {
				padding: 15px 30px;
				height: 100%;
			}
			
			.container-fluid>h3 {
				padding-bottom: 10px;
				margin-top: 10px;
				border-width: 1px;
			}
			.themeBox {
				margin-top: 15px;
			}
			.themeBox li {
				padding: 30px 30px 0 30px;
				cursor: pointer;
			}
			
			.themeBox li.active {
				box-shadow: 0px 0px 20px 4px #CCCCCC;
			}
			
			.themeBox li>img {
				width: 100%;
				height: auto;
				max-height: 180px;
				text-align: center;
				box-shadow: 0 0 20px 0px #CCCCCC;
			}
			
			.themeBox li>p {
				text-align: center;
				line-height: 50px;
				font-family: "微软雅黑";
				height: 50px;
			}
			
			.fontBox {
				margin-top: 20px;
				height: 50px;
				text-align: center;
			}
			
			.fontBox label {
				margin-right: 30px;
			}
			.themeBox li.active p{
				color: #FFFFff;
			}
			/*.savetheme {
				text-align: center;
				margin-bottom: 40px;
			}
			
			.savetheme button {
				margin: 0 20px;
			}*/
		</style>
	</head>

	<body>
		<div class="container-fluid">
			<h3>字体选择</h3>
			<div class="fontBox radio radio-theme">
				<input name="fontS" type="radio" id="fontSm" value="0">
				<label for="fontSm">
						小（14px）
					</label>
				<input name="fontS" type="radio" id="fontMd" value="1">
				<label for="fontMd">
						 中（16px）
					</label>
				<input name="fontS" type="radio" id="fontBig" value="2">
				<label for="fontBig">
					         大（18px）
					</label>
			</div>
			<h3>主题选择</h3>
			<ul id = "themeSelect" class="themeBox clearfix">
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="0">
					<img src="<%=basePath%>baseframe/page/themes/img/theme00.png" />
					<p class="themeName">主题1</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="1">
					<img src="<%=basePath%>baseframe/page/themes/img/theme02.png" />
					<p class="themeName">主题2</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="2">
					<img src="<%=basePath%>baseframe/page/themes/img/theme03.png" />
					<p class="themeName">主题3</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="3">
					<img src="<%=basePath%>baseframe/page/themes/img/theme07.png" />
					<p class="themeName">主题4</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="4">
					<img src="<%=basePath%>baseframe/page/themes/img/theme08.png"/>
					<p class="themeName">主题5</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="5">
					<img src="<%=basePath%>baseframe/page/themes/img/theme11.png" />
					<p class="themeName">主题6</p>
				</li>
			</ul>
			<!--<h3>模式选择</h3>
			<ul id= "PatternSelect" class="themeBox clearfix">
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="0">
					<img src="<%=basePath%>baseFrame/themes/img/Pattern00.jpg" />
					<p class="themeName">模式1</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="1">
					<img src="<%=basePath%>baseFrame/themes/img/Pattern01.jpg" />
					<p class="themeName">模式2</p>
				</li>
			</ul>-->
		</div>
		<script type="text/javascript">
			//会读
			var hisPattern = window.localStorage.getItem("hisPattern");
			(function() {
				var storage = window.localStorage;
				var fontSize = storage.getItem("hisFontSize")
				var theme = storage.getItem("hisTheme")
				if(theme != null) {
					$("#themeSelect li").eq(theme).addClass("active")
				} else {
					$("#themeSelect li").eq(0).addClass("active")
				}
				if(fontSize != null) {
					$(".fontBox input").eq(fontSize).prop("checked", true)
				} else {
					$(".fontBox input").eq(1).prop("checked", true)
				}
// 				if(hisPattern != null){
// 					$("#PatternSelect li").eq(hisPattern).addClass("active")
// 				}else{
// 					$("#PatternSelect li").eq(0).addClass("active")
// 				}
			})()
			var fontNum,themenum,pattern;
			$("#themeSelect").on("click", "li", function() {
				$(this).addClass("active").siblings().removeClass("active")
				themenum = $("#themeSelect li.active").attr("index")
				var themeHref = $("#themelink", parent.document)[0]
				themeHref.href = themeHref.href.replace(/theme\d+/, "theme" + themenum)
				$.ajax({
					async: false, //同步
					url: "<%=basePath%>mainAction!changeSkin",
					data: {"easyuiThemeName":themenum},
			 		dataType: "JSON",
					success: function(){} 
				});	
				window.localStorage.setItem("hisTheme",themenum )
//				if (hisPattern == 0 || hisPattern == null){
					resetPageAll($('.tabs-panels .panel iframe', parent.document),"themelink",themenum,1)
//				}else{
//					resetPageAll($('.tab-content iframe', parent.document),"themelink",themenum,1)
//				}
			})
// 			$("#PatternSelect").on("click", "li", function() {
// 				$(this).addClass("active").siblings().removeClass("active")
// 				pattern = $("#PatternSelect li.active").attr("index")
// 				$.ajax({
// 					async: false, //同步
<%-- 					url: "<%=basePath%>mainAction!rememberSpread", --%>
// 					data: {"stat":pattern},
// 					dataType: "JSON",
// 					success: function(){}
// 				});		
// 				window.localStorage.setItem("hisPattern",pattern )
// 				window.parent.parent.location.reload();
// 			})
			$('input:radio[name="fontS"]').change(function() {
				fontNum = $('.fontBox input[name="fontS"]:checked ').val()
				var fontHref = $("#fontlink", parent.document)[0]
				fontHref.href = fontHref.href.replace(/fontSize\d+/, "fontSize" + fontNum)
				window.localStorage.setItem("hisFontSize",fontNum )
				$.ajax({
					async: false, //同步
					url: "<%=basePath%>mainAction!changeFontSize",
					data: {"changeFontSize":fontNum},
					dataType: "JSON",
					success: function(){}
				});
//				if (hisPattern == 0 || hisPattern == null){
					resetPageAll($('.tabs-panels .panel iframe', parent.document),"fontlink",fontNum,2)
//				}else{
//					resetPageAll($('.tab-content iframe', parent.document),"fontlink",fontNum,2)
//					setTimeout(function(){
//						window.parent.$('.nav-tabs', parent.document).tabdrop("layout");
//					},300)
//				}
			})

			function resetPageAll($tabList,id,num,type) {
				$tabList.each(function(i, v) {
					var dom = v.contentWindow.document.getElementById(id)
					if(type == 1){
						dom.href = dom.href.replace(/theme\d+/,"theme"+ num) 
					}else{
						dom.href = dom.href.replace(/fontSize\d+/,"fontSize"+ num) 
					}
				})
			}
		</script>
	</body>

</html>