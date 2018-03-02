var base_url=$("base").attr("href");

function clickMenu(id,text,url){
		$("#sub_"+id).hide();		
		addTab(text, base_url + url);
	}
function addTab(title, url) {
		if ($('#tabs').tabs('exists', title)) {
			$('#tabs').tabs('select', title);//选中并刷新
			var currTab = $('#tabs').tabs('getSelected');
			//如果已存在，正常url地址应该是存在的，但是在信息展示中，同一title展示不同内容（参数）url为传入的url
			//var url = $(currTab.panel('options').content).attr('src');
			if (url != undefined && currTab.panel('options').title != 'Home') {
				$('#tabs').tabs('update', {
					tab : currTab,
					options : {
						content : createFrame(url)
					}
				});
			}
		} else {

			var content = createFrame(url);
			$('#tabs').tabs('add', {
				title : title,
				content : content,
				closable : true
			});
		}
		tabClose();
	}

	function createFrame(url) {
		var s = '<iframe scrolling="auto" frameborder="0"  src="' + url
				+ '" style="width:100%;height:100%;"></iframe>';
		return s;
	}

	function tabClose() {
		/*双击关闭TAB选项卡*/
		$(".tabs-inner").dblclick(function() {
			var subtitle = $(this).children(".tabs-closable").text();
			$('#tabs').tabs('close', subtitle);
			
		});
		/*为选项卡绑定右键*/
		
		$(".tabs-inner").bind('contextmenu', function(e) {
			var currTab = $('#tabs').tabs('getSelected');
			var title =  currTab.panel('options').title;
			if(title == 'Home'){
				$('#mm-tabclose').hide();
				$('#mm-tabcloseall').hide();
			}else{
				$('#mm-tabclose').show();
				$('#mm-tabcloseall').show();
			}
			$('#mm').menu('show', {
				left : e.pageX,
				top : e.pageY
			});

			var subtitle = $(this).children(".tabs-closable").text();

			$('#mm').data("currtab", subtitle);
			$('#tabs').tabs('select', subtitle);
			return false;
		});
	}
	//绑定右键菜单事件
	function tabCloseEven() {
		//刷新
		$('#mm-tabupdate').click(function() {
			var currTab = $('#tabs').tabs('getSelected');
//			var url = $(currTab.panel('options').content).attr('src');
			var url = $(currTab.html()).attr('src');
			if (url != undefined ) {
				$('#tabs').tabs('update', {
					tab : currTab,
					options : {
						content : createFrame(url)
					}
				});
			}
		});
		
		//关闭当前
		$('#mm-tabclose').click(function() {
			var currtab_title = $('#mm').data("currtab");
			$('#tabs').tabs('close', currtab_title);
		});
		//全部关闭
		$('#mm-tabcloseall').click(function() {
			$('.tabs-inner span').each(function(i, n) {
				var t = $(n).text();
				if (t != 'Home') {
					$('#tabs').tabs('close', t);
				}
			});
		});
		//关闭除当前之外的TAB
		$('#mm-tabcloseother').click(function() {
			var prevall = $('.tabs-selected').prevAll();
			var nextall = $('.tabs-selected').nextAll();
			var subtit=$(".tabs-selected .tabs-title").html();
			if (prevall.length > 0) {
				prevall.each(function(i, n) {
					var t = $('a:eq(0) span', $(n)).text();
					if (t != 'Home') {
						$('#tabs').tabs('close', t);
					}
				});
			}
			if (nextall.length > 0) {
				nextall.each(function(i, n) {
					var t = $('a:eq(0) span', $(n)).text();
					if (t != 'Home') {
						$('#tabs').tabs('close', t);
					}
				});
			}
			return false;
		});
		//关闭当前右侧的TAB
		$('#mm-tabcloseright').click(function() {
			var nextall = $('.tabs-selected').nextAll();
			if (nextall.length == 0) {
				//msgShow('系统提示','后边没有啦~~','error');
				alert('后边没有啦~~');
				return false;
			}
			nextall.each(function(i, n) {
				var t = $('a:eq(0) span', $(n)).text();
				$('#tabs').tabs('close', t);
			});
			return false;
		});
		//关闭当前左侧的TAB
		$('#mm-tabcloseleft').click(function() {
			var prevall = $('.tabs-selected').prevAll();
			if (prevall.length == 0) {
				alert('到头了，前边没有啦~~');
				return false;
			}
			prevall.each(function(i, n) {
				var t = $('a:eq(0) span', $(n)).text();
				$('#tabs').tabs('close', t);
			});
			return false;
		});

		//退出
		$("#mm-exit").click(function() {
			$('#mm').menu('hide');
		});
	}

	$(function() {
		tabCloseEven();
		$('.cs-navi-tab').click(function() {
			var $this = $(this);
			var href = $this.attr('src');
			var title = $this.text();
			addTab(title, href);
		});
	});

	//导航树
	$(function() {
//		setInterval("loginTime()", 1000);//1000为1秒钟
		loadMenu(-1,1);
	});

//	function formatSeconds(value) {
//		var theTime = parseInt(value);// 秒
//		var theTime1 = 0;// 分
//		var theTime2 = 0;// 小时
//		if (theTime > 60) {
//			theTime1 = parseInt(theTime / 60);
//			theTime = parseInt(theTime % 60);
//			if (theTime1 > 60) {
//				theTime2 = parseInt(theTime1 / 60);
//				theTime1 = parseInt(theTime1 % 60);
//			}
//		}
//		var result = "";
//		if(parseInt(theTime) < 10){
//			result = "0" + parseInt(theTime) + "秒";
//		}else{
//			result = "" + parseInt(theTime) + "秒";
//		}
//		
//		if (theTime1 > 0) {
//			if(parseInt(theTime1) < 10){
//				result = "0" + parseInt(theTime1) + "分" + result;
//			}else{
//				result = "" + parseInt(theTime1) + "分" + result;
//			}			
//		}
//		if (theTime2 > 0) {
//			if(parseInt(theTime2) < 10){
//				result = "0" + parseInt(theTime2) + "小时" + result;
//			}else{
//				result = "" + parseInt(theTime2) + "小时" + result;
//			}			
//		}
//		return result;
//	}
//
//	function loginTime() {
//		$("#contimes").html(
//				formatSeconds((new Date().getTime() - $("#startLoginTime")
//						.val()) / 1000));
//	}

	function loginout() {
		alert("loginout");
		//userLoginAction!loginout
	}
	var pageArr = new Array();
	pageArr[pageArr.length] = 0;
	function loadMenu(index,page) {
		$.ajax({
					url : 'sys/queryMenuList.action',
					type : 'post',
					success : function(data) {
						//var menuList = eval("("+data+")");
						var menuList = data;
						if (menuList && menuList.length) {
							if (index > -1) {
								$('#navDiv').empty();
							}
							if (index > 0) {
								$('#navDiv')
										.append(
												"<ul id=\"topnav\"><li class=\"first\">&nbsp;</li><li class=\"nav-left-arrow\"><a href=\"javascript:loadMenu("+pageArr[page-2]+","+(page-1)+");\"><<</a></li>");
							} else {
								$('#navDiv')
										.append(
												"<ul id=\"topnav\"><li class=\"first\">&nbsp;</li>");
							}
							var curLevel = 100;
							var count = 0;
							var countSecond = 0;
							var start = 0;
							if (index > 0)
								start = index;
							for (var i = start; i < menuList.length; i++) {
								var obj = menuList[i];
								var sBrevText = obj.text;
								if (obj.text.length > 9) {
									sBrevText = obj.text.substring(0, 8) + "..";
								}
								if (obj.level == "1") {
									countSecond = 0;
									var len = (count + 1) * 142 + 30;
									var offset = 0;
									var winW = $("body").width()*0.8;
									if (obj.last && obj.last == "1") {
										offset = 0;
									} else {
										offset = 30;
									}
									if (index > 0) {
										offset += 30;
									}
									len += offset;

									if (obj.hav == "0") {
										if (curLevel != 1 && curLevel != 100) {
											$('#li_' + count).append(
													"</ul></div></li>");
										}
										count++;

										if (len > winW) {
											$('#topnav')
													.append(
															"<li class=\"nav-right-arrow\"><a href=\"javascript:loadMenu("
																	+ i
																	+ ","+(page+1)+");\">>></a></li><ul></div></li>");
											if(page+1>pageArr.length){
												pageArr[pageArr.length] = i;
											}
											i = menuList.length;
										} else {
											$('#topnav').append(
													"<li class=\"nav-menu\" id=\"li_"+count+"\"><a href=\"javascript:void(0)\">"
															+ sBrevText
															+ "</a></li>");
											$('#li_' + count)
													.append(
															"<div class=\"sub\" id=\"sub_"+count+"\">");
										}
									} else {
										count++;
										if (len > winW) {
											$('#topnav')
													.append(
															"<li class=\"nav-right-arrow\"><a href=\"javascript:loadMenu("
																	+ i
																	+ ","+(page+1)+");\">>></a></li><ul></div></li>");
											if(page+1>pageArr.length){
												pageArr[pageArr.length] = i;
											}
											i = menuList.length;
										} else {
											$('#topnav').append(
													"<li class=\"nav-menu\" id=\"li_"+count+"\"><a href=\"javascript:clickMenu('"
															+ count + "','"
															+ obj.text + "','"
															+ obj.url + "')\">"
															+ sBrevText
															+ "</a></li>");
										}
									}
									if (curLevel != 1)
										curLevel = 1;
								} else if (obj.level == "2") {
									if (curLevel == 1) {
										countSecond++;
										$('#sub_' + count)
												.append(
														"<ul id=\"ul_"+count+"_"+countSecond+"\">");
									} else if (curLevel == 3) {
										countSecond++;
										$('#sub_' + count).append("</ul>");
										$('#sub_' + count)
												.append(
														"<ul id=\"ul_"+count+"_"+countSecond+"\">");
									}

									if (obj.hav == "0") {
										if (curLevel == 2) {
											countSecond++;
											$('#sub_' + count).append("</ul>");
											$('#sub_' + count)
													.append(
															"<ul id=\"ul_"+count+"_"+countSecond+"\">");
										}
										$('#ul_' + count + '_' + countSecond)
												.append(
														"<li class=\"second-menu fontBg\">"
																+ obj.text
																+ "</li>");
									} else {
										$('#ul_' + count + '_' + countSecond)
												.append(
														"<li><div class=\"nav-arrow\"></div><a href=\"javascript:clickMenu('"
																+ count + "','"
																+ obj.text
																+ "','"
																+ obj.url
																+ "')\" title=\""+obj.text+"\">"
																+ sBrevText
																+ "</a></li>");
									}
									if (curLevel != 2)
										curLevel = 2;
								} else if (obj.level == "3") {
									$('#ul_' + count + '_' + countSecond)
											.append(
													"<li><div class=\"nav-arrow\"></div><a href=\"javascript:clickMenu('"
															+ count + "','"
															+ obj.text + "','"
															+ obj.url + "')\" title=\""+obj.text+"\">"
															+ sBrevText
															+ "</a></li>");
									if (curLevel < 3)
										curLevel = 3;
								}
							}
							$('#navDiv').append("</ul>");
							if (index == -1) {
								$('#navDiv').show();
							}

							var config = {
								sensitivity : 2, // number = sensitivity threshold (must be 1 or higher)    
								interval : 100, // number = milliseconds for onMouseOver polling interval    
								over : megaHoverOver, // function = onMouseOver callback (REQUIRED)    
								timeout : 100, // number = milliseconds delay before onMouseOut    
								out : megaHoverOut
							// function = onMouseOut callback (REQUIRED)    
							};

							$("ul#topnav li .sub").css({
								'opacity' : '0'
							});
							$("ul#topnav li.nav-menu").hoverIntent(config);
						}
						var bodyH =  $("#p").height()
						$("#topnav .nav-menu .sub ul").each(function(i,v){
							if($(v).children("li").length*36 >bodyH-30){
								$(v).slimscroll({ //菜单滚动条
									height: bodyH-30 + "px",
									width: "200px",
									wheelStep: 10 ,
									size:"12px"
								})
							}
						})
					}
				});
	}

	function megaHoverOver() {

		$(this).find(".sub").stop().fadeTo('fast', 1).show();

		//Calculate width of all ul's
		(function($) {
			jQuery.fn.calcSubWidth = function() {
				rowWidth = 0;
				//Calculate row
				$(this).find("ul").each(function() {
					rowWidth += $(this).width();
				});
			};
		})(jQuery);

		if ($(this).find(".row").length > 0) { //If row exists...
			var biggestRow = 0;
			//Calculate each row
			$(this).find(".row").each(function() {
				$(this).calcSubWidth();
				//Find biggest row
				if (rowWidth > biggestRow) {
					biggestRow = rowWidth;
				}
			});
			//Set width
			$(this).find(".sub").css({
				'width' : biggestRow
			});
			$(this).find(".row:last").css({
				'margin' : '0'
			});

		} else { //If row does not exist...

			$(this).calcSubWidth();
			//Set Width
			$(this).find(".sub").css({
				'width' : rowWidth
			});

			var index = $(this).find(".sub").attr("id").substring(4) - 0;
			var winW = $("body").width();
			var left = (index - 1) * 152 + 60;
			if (left + rowWidth + 20 > winW) {
				$(this).find(".sub").css("margin-left",
						(winW - rowWidth - left- 20) + "px");
			}

		}
	}

	function megaHoverOut() {
		$(this).find(".sub").stop().fadeTo('fast', 0, function() {
			$(this).hide();
		});
	}
	