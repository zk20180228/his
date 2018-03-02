<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/head_content.jsp" %>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta charset="utf-8" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/system/css/oaHome/index.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/system/css/oaHome/idangerous.swiper.css"/>
		<title>OA系统网站首页</title>
		<style type="text/css">
				* {
				  box-sizing: border-box;
				}
				.w {
				  width: 1400px;
				}
				.nav-title-box {
				  width: 100%;
				  height: 28px;
				  background-color: #DCD8D9;
				}
				.nav-title-box .nav-title {
				  margin: 0 auto;
				}
				.nav-title-box .nav-title .nav-title-content {
				  float: right;
				  width: 300px;
				  height: 28px;
				  line-height: 28px;
				}
				.nav-title-box .nav-title .nav-title-content ul li {
				  float: left;
				}
				.nav-title-box .nav-title .nav-title-content ul li a {
				  color: #2C2A2B;
				}
				.nav-title-box .nav-title .nav-title-content ul li:nth-child(2) a {
				  padding: 0 20px;
				  border-left: 1px solid #2C2A2B;
				  border-right: 1px solid #2C2A2B;
				  margin: 0 20px;
				}
				.logo-box {
				  height: 88px;
				  margin: 0 auto;
				}
				.logo-box .logo {
				  float: left;
				}
				.logo-box .logo a {
				  width: 330px;
				  height: 56px;
				  display: block;
				  margin-top: 17px;
				}
				.logo-box .logo a img {
				  width: 100%;
				  height: 100%;
				}
				.logo-box .seach {
				  float: right;
				  width: 300px;
				  height: 38px;
				  line-height: 36px;
				  margin-top: 27px;
				  position: relative;
				}
				.logo-box .seach .seach-text {
				  width: 235px;
				  height: 38px;
				  text-indent: 50px;
				  font-size: 18px;
				}
				.logo-box .seach .seach-buttom {
				  width: 45px;
				  height: 38px;
				  background-color: #509AE1;
				  color: #FFFFFF;
			      border-radius: 0;
		          margin-left: -7px;
				}
				.logo-box .seach .info {
				  width: 50px;
				  height: 34px;
				  position: absolute;
				  left: 0;
				  text-align: center;
				}
				.nav-box {
				  width: 100%;
				  height: 50px;
				  background-color: #509AE1;
				}
				.nav-box .nav {
				  margin: 0 auto;
				  height: 50px;
				  line-height: 50px;
				}
				.nav-box .nav #news-ul {
				  width: 100%;
				  height: 50px;
				}
				.nav-box .nav #news-ul>li {
				  float: left;
				}
				.nav-box .nav #news-ul>li>a {
				  display: block;
				  width: 100%;
				  height: 100%;
				  color: #FFFFFF;
				  font-size: 16px;
				  font-family: "微软雅黑";
				  padding: 0 30px;
				  text-align: center;
				}
				.nav-box .nav #news-ul>li>a:hover {
				  background-color: #4787C4;
				}
				.img-container-box {
				  margin: 0 auto;
				  height: 362px;
				  padding: 7px 0 21px 0;
				}
				.img-container-box .img {
				  float: left;
				}
				.img-container-box .img img {
				  width: 100%;
				  height: 100%;
				}
				.img-container-box .imgBig {
				  height: 334px;
				  width: 100%;
				  position: relative;
				}
				.img-container-box .imgBig .imgBigInfo {
				  position: absolute;
				  left: 0;
				  bottom: 0;
				  width: 100%;
				  height: 33px;
				  line-height: 33px;
				  text-indent: 30px;
				  font-size: 14px;
				  font-family: "微软雅黑";
				  color: #FFFFFF;
				  background-color: rgba(37, 37, 37, 0.4);
				}
				.img-container-box .imgsm {
				  width: 306px;
				  height: 108px;
				  margin-left: 12px;
				}
				.img-container-box .imgsm.magin {
				  margin: 4px 0 4px 12px;
				}
				.icon-box {
				  margin: 0 auto;
				  height: 142px;
				  background-color: #509AE1;
				}
				.icon-box ul {
				  width: 100%;
				  height: 100%;
				}
				.icon-box ul li {
				  width: 16.66%;
				  height: 100%;
				  float: left;
				}
				.icon-box ul li a {
				  width: 100%;
				  height: 100%;
				  display: block;
				  border-right: 1px solid #4080BD;
				  transition: all 0.2s;
				}
				.icon-box ul li a i {
				  text-align: center;
				  line-height: 60px;
				  color: #FFFFFF;
				  width: 100%;
				  height: 92px;
				  display: block;
				  font-size: 60px;
				  padding-top: 24px;
				}
				.icon-box ul li a p {
				  text-align: center;
				  line-height: 50px;
				  font-size: 16px;
				  font-family: "微软雅黑";
				  color: #FFFFFF;
				}
				.icon-box ul li a:hover {
				  background-color: #4080BD;
				}
				.icon-box ul li:last-child a {
				  border-right: none;
				}
				.mainInfo {
				  margin: 0 auto;
				}
				.mainInfo .info {
				  float: left;
				  height: 495px;
				  padding-top: 25px;
				}
				.mainInfo .info .infoHeader {
				  width: 100%;
				  height: 50px;
				  border-bottom: 1px solid #D9D9D9;
				  line-height: 50px;
				  margin-bottom: 3px;
				}
				.mainInfo .info .infoHeader h2 {
				  float: left;
				  height: 51px;
				  border-bottom: 3px solid #509BDF;
				  padding: 0 25px;
				  font-size: 17px;
				  font-family: "微软雅黑";
				  color: #404040;
				  line-height: 51px;
				}
				.mainInfo .info .infoList {
				  margin-top: 30px;
				  width: 100%;
				  height: 375px;
				  background-color: #EEF2F5;
				}
				.mainInfo .info .infoList ul {
				  width: 100%;
				  height: 100%;
				  padding-top: 15px;
				}
				.mainInfo .info .infoList ul li {
				  width: 100%;
				  line-height: 37px;
				  height: 37px;
				}
				.mainInfo .info .infoList ul li a {
				  display: block;
				  width: 100%;
				  height: 100%;
				  position: relative;
				  overflow: hidden;
				}
				.mainInfo .info .infoList ul li a span {
				  font-size: 14px;
				  font-family: "宋体";
				  color: #28292B;
				}
				.mainInfo .info .infoList ul li a .left {
				  display: block;
				  width: 100%;
				  height: 100%;
				  padding: 0 135px 0 40px;
				}
				.mainInfo .info .infoList ul li a .right {
				  position: absolute;
				  top: 0;
				  right: 0;
				  width: 135px;
				  text-align: center;
				}
				.mainInfo .info .infoList ul li a:hover span {
				  color: #509AE1;
				}
				.mainInfo .info .infoList ul li a:hover::after {
				  background-color: #509AE1;
				}
				.mainInfo .info .infoList ul li a::after {
				  content: "";
				  display: block;
				  position: absolute;
				  left: 20px;
				  top: 17px;
				  width: 6px;
				  height: 6px;
				  border-radius: 3px;
				  background-color: #777576;
				}
				.mainInfo .infosm {
				  width: 50%;
				}
				.mainInfo .infoBig {
				  width: 100%;
				}
				.mainInfo .infoBig .infoList {
				  width: 50%;
				  float: left;
				}
				.mainInfo .infoBig .infoList2 {
				  float: left;
				  width: 50%;
				  margin-top: 30px;
				  height: 375px;
				  background-color: #EEF2F5;
				}
				.mainInfo .infoBig .infoList2 ul {
				  width: 100%;
				  height: 100%;
				  padding-left: 25px;
				}
				.mainInfo .infoBig .infoList2 ul li {
				  float: left;
				  width: 200px;
				  height: 160px;
				  padding-top: 28px;
				}
				.mainInfo .infoBig .infoList2 ul li a {
				  display: block;
				  width: 100%;
				  height: 100%;
				  line-height: 130px;
				  text-align: center;
				  color: #FFFFFF;
				}
				.mainInfo .infoBig .infoList2 ul li:nth-child(1) a {
				  background-color: #45BE89;
				}
				.mainInfo .infoBig .infoList2 ul li:nth-child(2) {
				  margin: 0 24px;
				}
				.mainInfo .infoBig .infoList2 ul li:nth-child(2) a {
				  background-color: #F2A553;
				}
				.mainInfo .infoBig .infoList2 ul li:nth-child(3) a {
				  background-color: #63A8EB;
				}
				.mainInfo .infoBig .infoList2 ul li:nth-child(4) a {
				  background-color: #F96768;
				}
				.mainInfo .infosm:nth-child(odd) {
				  padding-right: 17px;
				}
				.mainInfo .infosm:nth-child(even) {
				  padding-left: 17px;
				}
				.footer {
				  margin-top: 60px;
				  height: 137px;
				  background: url(/his-portal/themes/system/images/oaindex/footer.png) no-repeat 100% 100%;
				}
				.footer .footFont {
				  margin: 0 auto;
				  padding-top: 20px;
				}
				.footer .footFont p {
				  font-size: 12px;
				  text-align: center;
				  line-height: 30px;
				  color: #FFFFFF;
				}
				* {
				margin: 0;
				padding: 0;
				list-style: none;
			}
			
			body {
				font-family: Helvetica Neue, Helvetica, Arial, sans-serif;
				padding: 0;
				margin: 0;
				font-size: 13px;
			}
			
			.xinhua {
				margin: 0 auto;
				width: 1400px;
				height: 450px;
				position: relative;
			}
			
			.swiper-container {
				width: 1400px;
				height: 450x;
			}
			
			.swiper-container img {
				width: 100%;
				height: 100%;
			}
			
			.pagination {
				position: absolute;
				bottom: 20px;
				margin-top: 3px;
				z-index: 20;
				bottom: 27px;
				width: 200px;
				left: 50%;
				margin-left: -100px;
				text-align: center;
			}
			
			.swiper-pagination-switch {
				display: inline-block;
				width: 25px;
				height: 25px;
				background: url(/his-portal/themes/system/images/oaindex/icon.png) no-repeat 0px -50px;
				cursor: pointer;
			}
			
			.swiper-pagination-switch:hover,
			.swiper-active-switch {
				background: url(/his-portal/themes/system/images/oaindex/icon.png) no-repeat -25px -50px;
			}
			
			.arrow-left {
				background-color: rgba(0, 0, 0, .3);
				position: absolute;
				left: 20px;
				top: 50%;
				margin-top: -30px;
				width: 40px;
				height: 60px;
				z-index: 20;
				color: #cccccc;
				text-decoration: none;
				line-height: 60px;
				text-align: center;
				font-size: 30px;
			}
			
			.arrow-left:hover {
				background-color: rgba(0, 0, 0, .7);
				color: #FFFFFF;
			}
			
			.arrow-right {
				background-color: rgba(0, 0, 0, .3);
				position: absolute;
				right: 20px;
				top: 50%;
				margin-top: -30px;
				width: 40px;
				height: 60px;
				z-index: 20;
				color: #cccccc;
				text-decoration: none;
				line-height: 60px;
				text-align: center;
				font-size: 30px;
			}
			
			.arrow-right:hover {
				background-color: rgba(0, 0, 0, .7);
				color: #FFFFFF;
			}
			
			.caption {
				position: absolute;
				bottom: 0;
				width: 100%;
				overflow: hidden;
				height: 40px;
				z-index: 20;
				background-color: rgba(0, 0, 0, .3);
			}
			
			.caption li {
				width: 100%;
				text-align: center;
				position: absolute;
				bottom: -36px;
				transition: all 0.2s cubic-bezier(0.33, 0, 0.2, 1);
			}
			
			.caption .active {
				bottom: 8px;
			}
			
			.caption li a {
				color: #fff;
				font-size: 16px;
				font-family: microsoft yahei;
				text-decoration: none;
			}
			
			/*轮播图end*/
			#news-ul>li {
				position: relative;
			}
			#news-ul>li:hover .chilentList a{
				height: 35px;
				border-top: 1px solid #4787C4;
			}
			#news-ul .chilentList {
				position: absolute;
				left: 0;
				z-index: 30;
				width: 100%;
				background-color: #509AE1;
			}
			#news-ul .chilentList a{
				height: 0;
				line-height: 35px;
				display: block;
				width: 100%;
				padding: 0 10px;
				color: #FFFFFF;
				transition: all 0.5s;
				border-top: 0 solid #4787C4;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}
			#news-ul .chilentList a:hover {
				background-color: #4787C4;
			}		
		</style>

	</head>
	<body>
		<div class="nav-title-box">
			<div class="nav-title w">
				<div class="nav-title-content">
					<ul>
						<li>
							你好！${userName}
						</li>
						<li><!-- 主页：/his-portal/sys/noticeManage/mainContext.action -->
							<a href="${pageContext.request.contextPath}/sys/noticeManage/mainContext.action">进入管理平台</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath}/sys/noticeManage/mainContext.action">退出</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="logo-box w">
			<div class="logo">
				<img src="/his-portal/themes/system/images/oaindex/OAlogo.png" alt=""  style="padding-top:9px"/>
			</div>
			<div class="seach">
				<form action="${pageContext.request.contextPath}/oa/OAHome/articleUI.action" id="ff" target="_blank">
					<input class="easyui-textbox" prompt="信息"  id="searchArticle" style="width:180px;height:38px" name="infoTitle">
					<button class="seach-buttom btn honryicon icon-web-search" id="searchButton"></button>
				</form>	
			</div>
		</div>
		<div class="nav-box">
			<div class="nav w">
				<ul id = "news-ul">
					<c:forEach items="${menuList}" var="menu" varStatus="vs">
						<li>
							<a href="javascript:void(0)"  value="${menu.menuCode}" name="parentMenu">${menu.menuName}</a>
								<ul class="chilentList" id="${menu.menuCode}">
							</ul>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
				<div class="xinhua">
			<div class="swiper-container">
				<div class="swiper-wrapper">
					<c:forEach items="${articleList}" var="article" varStatus="s">
						<div class="swiper-slide">
							<a href="${pageContext.request.contextPath}/oa/information/view.action?infoid="+${article.infoId} target="_blank"><img src="${article.imgPath}"></a>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="pagination"></div>
			<a class="arrow-left" href="javascript:void(0)" onClick="mySwiper.swipePrev();return false;">&lt;</a>
			<a class="arrow-right" href="javascript:void(0)" onClick="mySwiper.swipeNext();return false;">&gt;</a>
			<ul class="caption">
				
				<c:forEach items="${articleList}"  var="ar"  varStatus="ss">
					<li class="active">
						<a href="${pageContext.request.contextPath}/oa/information/view.action?infoid="+${ar.infoId} target="_blank" >${ar.infoTitle}</a>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div class="icon-box w">
			<ul>
				<li>
					<a href="javascript:void(0)">
						<i class="honryicon icon-yiyuan"></i>
						<p>医院党建</p>
					</a>
				</li>
				<li>
					<a href="javascript:void(0)">
						<i class="honryicon icon-jixiao"></i>
						<p>绩效考核系统</p>
					</a>
				</li>
				<li>
					<a href="javascript:void(0)">
						<i class="honryicon icon-icon2"></i>
						<p>医疗设备综合管理系统</p>
					</a>
				</li>
				<li>
					<a href="javascript:void(0)">
						<i class="honryicon icon-zhishiku2"></i>
						<p>医疗知识库</p>
					</a>
				</li>
				<li>
					<a href="javascript:void(0)">
						<i class="honryicon icon-chaxun"></i>
						<p>PASS查询</p>
					</a>
				</li>
				<li>
					<a href="javascript:void(0)">
						<i class="honryicon icon-wangzhibiaozhi"></i>
						<p>万方医学网</p>
					</a>
				</li>
			</ul>
		</div>
		<div class="mainInfo w clearfix">
			<div class="info infosm">
				<div class="infoHeader">
					<h2>通知公告</h2>
					<a href="${pageContext.request.contextPath}/oa/OAHome/articleUI.action?menuCode=OA_MENU_TZGG" target="_blank"><span style="padding-left:70%;font-size:16px">>>更多</span></a>
				</div>
				<div class="infoList">
					<ul id="tzgg">
					</ul>
				</div>
			</div>
			<div class="info infosm">
				<div class="infoHeader">
					<h2>医院动态</h2>
					<a href="${pageContext.request.contextPath}/oa/OAHome/articleUI.action?menuCode=OA_MENU_YYDT" target="_blank"><span style="padding-left:70%;font-size:16px">>>更多</span></a>
				</div>
				<div class="infoList">
					<ul id="yydt">
					</ul>
				</div>
			</div>
			<div class="info infosm">
				<div class="infoHeader">
					<h2>学术活动</h2>
					<a href="${pageContext.request.contextPath}/oa/OAHome/articleUI.action?menuCode=OA_MENU_XSHD" target="_blank"><span style="padding-left:70%;font-size:16px">>>更多</span></a>
				</div>
				<div class="infoList">
					<ul id="xshd">
					</ul>
				</div>
			</div>
			<div class="info infosm">
				<div class="infoHeader">
					<h2>医院文化建设</h2>
					<a href="${pageContext.request.contextPath}/oa/OAHome/articleUI.action?menuCode=OA_MENU_YYWHJS" target="_blank"><span style="padding-left:65%;font-size:16px">>>更多</span></a>
				</div>
				<div class="infoList">
					<ul id="yywhjs">
					</ul>
				</div>
			</div>
			<div class="info infoBig">
				<div class="infoHeader">
					<h2>在线教育</h2>
					<a href="${pageContext.request.contextPath}/oa/OAHome/articleUI.action?menuCode=OA_MENU_ZXJY" target="_blank"><span style="padding-left:85%;font-size:16px">>>更多</span></a>
				</div>
				<div class="infoList2">
					<ul>
						<li>
							<a href="javascript:void(0)">医护人员手卫生</a>
						</li>
						<li>
							<a href="javascript:void(0)">伤员救护访问</a>
						</li>
						<li>
							<a href="javascript:void(0)">医护人员手卫生</a>
						</li>
						<li>
							<a href="javascript:void(0)">医护人员手卫生</a>
						</li>
					</ul>
				</div>
				<div class="infoList">
					<ul id="zxjy">
					</ul>
				</div>

			</div>
			</div>
			<div class="footer">
				<div class="footFont w">
					<p>xxxxxxx</p>
					<p>xxxxxx</p>
					<p>xxxxxxxxxxxxxxxxxxxxxxxxxx</p>
				</div>
			</div>
	</body>

</html>
		<script src="/his-portal/javascript/js/oaHome/idangerous.swiper.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/his-portal/javascript/js/oaHome/idangerous.swiper.progress.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
			var mySwiper = new Swiper('.swiper-container', {
				autoplay: 5000,
				speed: 300,
				loop: true,
				pagination: '.pagination',
				paginationClickable: true,
				progress: true,
				onProgressChange: function(swiper) {
					for(var i = 0; i < swiper.slides.length; i++) {
						var slide = swiper.slides[i];
						var progress = slide.progress;
						var translate = progress * swiper.width;
						var opacity = 1 - Math.min(Math.abs(progress), 1);
						slide.style.opacity = opacity;
						swiper.setTransform(slide, 'translate3d(' + translate + 'px,-100px,0)');
					}
				},
				onTouchStart: function(swiper) {
					for(var i = 0; i < swiper.slides.length; i++) {
						swiper.setTransition(swiper.slides[i], 0);
					}
				},
				onSetWrapperTransition: function(swiper, speed) {
					for(var i = 0; i < swiper.slides.length; i++) {
						swiper.setTransition(swiper.slides[i], speed);
					}
				},
				onSlideChangeStart: function(swiper) {
					$('.caption li').removeClass('active');
					$('.caption li').eq(swiper.activeLoopIndex).addClass('active')
				}
			})
	</script>

<script type="text/javascript">

	$(function(){
		
// 		<ul class="chilentList">
// 			<li><a href="xxxx">菜单2</a></li>
// 		</ul>
// 	$("a[name='parentMenu']").mousemove();

		
		
		
		//点击父栏目时，加载子栏目
		$("a[name='parentMenu']").mousemove(function(){
			//alert("你点击了,父栏目,正在加载对应的子栏目！");
// 			${pageContext.request.contextPath}/oa/OAHome/sonMenuList.action?menuCode=${menu.menuCode}
			var code=$(this).attr("value");
			var aContent="";
			$.ajax({
				url:"${pageContext.request.contextPath}/oa/OAHome/sonMenuList.action",
				type:"post",
				data:"menuCode="+code,
				success:function(backData){
					var sonNodes=backData;//子节点数组
					for(var i=0;i<sonNodes.length;i++){
//  						alert(sonNodes[i].menuName);
//  						alert(sonNodes[i].menuCode);
						aContent+="<li><a href='javascript:void(0)' >"+sonNodes[i].menuName+"</a></li>";
						}
					$("#"+code).html(aContent);		
				}
				
			});
		});
		
		//站内搜索
		$("#searchButton").click(function(){
			var searchContent=$("#searchArticle").val();
			$("#ff")[0].sumbit();
		});
		
		
		//异步加载医院动态,异步更快
		$.ajax({//通知公告
			type:"post",
			data:"menuCode=OA_MENU_TZGG&isMore=false",
			url:"${pageContext.request.contextPath}/oa/OAHome/menu_ArticleList.action",
			success:function(backData){
				//backData相当于一个组
				var arrArticles=backData;
				var content="";
				for(var i=0;i<arrArticles.length;i++){
					var json = arrArticles[i];
					content+=	"<li>"+
								"<a href='${pageContext.request.contextPath}/oa/information/view.action?infoid="+json.infoId+"' target='_blank'><span class='left'>"+json.infoTitle+"</span><span class='right'>"+json.infoPubTime+"</span></a>"+
								"</li>"
				}
				$("#tzgg").html(content);
			}
		});
		
		$.ajax({//医院动态
			type:"post",
			data:"menuCode=OA_MENU_YYDT&isMore=false",
			url:"${pageContext.request.contextPath}/oa/OAHome/menu_ArticleList.action",
			success:function(backData){
				//backData相当于一个组
				var arrArticles=backData;
				var content="";
				for(var i=0;i<arrArticles.length;i++){
					var json = arrArticles[i];
					content+=	"<li>"+
								"<a href='${pageContext.request.contextPath}/oa/information/view.action?infoid="+json.infoId+"' target='_blank'><span class='left'>"+json.infoTitle+"</span><span class='right'>"+json.infoPubTime+"</span></a>"+
								"</li>"
				}
				$("#yydt").html(content);
			}
		});
		
		$.ajax({//学术活动
			type:"post",
			data:"menuCode=OA_MENU_XSHD&isMore=false",
			url:"${pageContext.request.contextPath}/oa/OAHome/menu_ArticleList.action",
			success:function(backData){
				//backData相当于一个组
				var arrArticles=backData;
				var content="";
				for(var i=0;i<arrArticles.length;i++){
					var json = arrArticles[i];
					content+=	"<li>"+
								"<a href='${pageContext.request.contextPath}/oa/information/view.action?infoid="+json.infoId+"' target='_blank'><span class='left'>"+json.infoTitle+"</span><span class='right'>"+json.infoPubTime+"</span></a>"+
								"</li>"
				}
				$("#xshd").html(content);
			}
		});
		
		$.ajax({//医院文化建设
			type:"post",
			data:"menuCode=OA_MENU_YYWHJS&isMore=false",
			url:"${pageContext.request.contextPath}/oa/OAHome/menu_ArticleList.action",
			success:function(backData){
				//yywhjs
				//backData相当于一个组
				var arrArticles=backData;
				var content="";
				for(var i=0;i<arrArticles.length;i++){
					var json = arrArticles[i];
					content+=	"<li>"+
								"<a href='${pageContext.request.contextPath}/oa/information/view.action?infoid="+json.infoId+"' target='_blank'><span class='left'>"+json.infoTitle+"</span><span class='right'>"+json.infoPubTime+"</span></a>"+
								"</li>"
				}
				$("#yywhjs").html(content);
			}
		});
		
		$.ajax({//在线教育
			type:"post",
			data:"menuCode=OA_MENU_ZXJY&isMore=false",
			url:"${pageContext.request.contextPath}/oa/OAHome/menu_ArticleList.action",
			success:function(backData){
				//backData相当于一个组
				var arrArticles=backData;
				var content="";
				for(var i=0;i<arrArticles.length;i++){
					var json = arrArticles[i];
					content+=	"<li>"+
								"<a href='${pageContext.request.contextPath}/oa/information/view.action?infoid="+json.infoId+"' target='_blank'><span class='left'>"+json.infoTitle+"</span><span class='right'>"+json.infoPubTime+"</span></a>"+
								"</li>"
				}
				$("#zxjy").html(content);
			}
		});
		
		
		
		
	});


</script>
