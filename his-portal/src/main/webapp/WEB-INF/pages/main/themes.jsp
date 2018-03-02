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
		<jsp:include page="/common/head_content.jsp"></jsp:include>
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
				padding: 10px 30px;
				height: 100%;
			}
			
			.container-fluid>h3 {
				padding-bottom: 10px;
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
				border: 1px solid #CCCCCC;
			}
			
			.themeBox li>p {
				text-align: center;
				line-height: 30px;
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
			<h3>主题选择</h3>
			<ul class="themeBox">
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="0">
					<img src="../img/zhuti1.png?v=1f91f68" alt="" />
					<p class="themeName">主题1</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="1">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题2</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="2">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题3</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="3">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题4</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="4">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题5</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="5">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题6</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="6">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题7</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="7">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题8</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="8">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题9</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="9">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题10</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="10">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题11</p>
				</li>
				<li class="col-md-3 col-sm-4 col-xs-6 col-lg-2" index="11">
					<img src="../img/zhuti1.png?v=1f91f68" alt="主题" />
					<p class="themeName">主题12</p>
				</li>
			</ul>
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

		</div>
		<script type="text/javascript">
			//会读
			(function() {
				var storage = window.localStorage;
				var fontSize = storage.getItem("hisFontSize")
				var theme = storage.getItem("hisTheme")
				if(theme !== null) {
					$(".themeBox li").eq(theme).addClass("active")
				} else {
					$(".themeBox li").eq(0).addClass("active")
				}
				if(fontSize !== null) {
					$(".fontBox input").eq(fontSize).prop("checked", true)
				} else {
					$(".fontBox input").eq(1).prop("checked", true)
				}
			})()
			var fontNum,themenum;
			$(".themeBox").on("click", "li", function() {
				$(this).addClass("active").siblings().removeClass("active")
				themenum = $(".themeBox li.active").attr("index")
				var themeHref = $("#themelink", parent.document)[0]
				var itemThemeHref = $("#themelink")[0]
				themeHref.href = themeHref.href.replace(/theme\d+/, "theme" + themenum)
				itemThemeHref.href = itemThemeHref.href.replace(/theme\d+/, "theme" + themenum)
				window.localStorage.setItem("hisTheme",themenum )
				resetPageAll($('.mainTabds .tab-content .tab-pane', parent.document))
			})
			$('input:radio[name="fontS"]').change(function() {
				fontNum = $('.fontBox input[name="fontS"]:checked ').val()
				var fontHref = $("#fontlink", parent.document)[0]
				var itemFontHref = $("#fontlink")[0]
				fontHref.href = fontHref.href.replace(/fontSize\d+/, "fontSize" + fontNum)
				itemFontHref.href = itemFontHref.href.replace(/fontSize\d+/, "fontSize" + fontNum)
				window.localStorage.setItem("hisFontSize",fontNum )
				resetPageAll($('.mainTabds .tab-content .tab-pane', parent.document))
			})

			//刷新所有的页面
			function resetPage (){
				var fontHref = $("#fontlink", parent.document)[0]
				var themeHref = $("#themelink", parent.document)[0]
				fontHref.href = fontHref.href.replace(/fontSize\d+/,"fontSize"+ fontNum) 
				themeHref.href = themeHref.href.replace(/theme\d+/,"theme"+ themenum) 
				resetPageAll($('.mainTabds .tab-content .tab-pane', parent.document))
			}
			function resetPageAll($tabList) {
				$tabList.each(function(i, v) {
					var $iframe = $(v).children("iframe")[0]
					if(!$(v).hasClass("active")){
						$iframe.src = $iframe.src
					}
				})
			}
		</script>
	</body>

</html>