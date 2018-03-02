(function($) {
	var defaults  = {
		width :530, //可选参数：inherit，数值(px)
		height :400,	//外层下拉框的高度
		menulines :2,	//有几列
		dropmenu:".xmenu",//弹出层div
		isSecond:false,	//是否是二级联动的第二级
		haveThreeLevel:false, //是否有三级菜单，默认为两级
		threeLevelWidth:320,	//三级弹出框的宽度，可以自己定义，此为默认值
		threeLevelHeight:100,	//三级弹出框的高度，可以自己定义，此为默认值
		threeLevelMenu:"",	//查询每个三级菜单的url请求（要带模糊查）
		threeLevelMenuAll:"", //查询所有三级菜单的url请求（要带模糊查）（点击一级的全选时候用）
		para:null,	//要传的参数，多个之间用逗号隔开（deptType）
		firsturl: "url",	//查询一级菜单和二级菜单的url
		secondurl: "url", //二级查询方法
		relativeInput:"",	//与其级联的文本框
		relativeDropmenu:"",	//与其级联的弹出层
		chId:null,	//添加默认值
		borderStyle:3,	//边框类型，1为在整个分类上加边框，2为二级菜单上每行有边框，3为二级菜单上每个菜单有边框
		spreadNum:0,	//默认展开几个
		isRelative:false	//是否有级联
	};
	//获取id
	$.fn.getMenuIds = function(){
		var nArr = $(this).attr("name");
		if(nArr){
			var reg = new RegExp("_","g");
			return nArr.replace(reg,",");
		}
		return '';
	};
	
	//设置回显值
	$.fn.backValues = function(params,isClear,isThreeLevel,isSecond){
		//params：要传的json格式数据，isClear：是否先清空文本框再回显（true：清空，false：不清空），
		//isThreeLevel：是否有三级，isSecond：是否是级联的第二级
		var $inputId = $(this);
		if(isClear){	
			$inputId.val(params.name);
			$inputId.attr("name",params.id);
			if(isThreeLevel){
				$inputId.attr("rel",params.yq);
			}
			if(isSecond){
				$inputId.attr("alt",params.relativeId);
			}			
		}else{
			$inputId.val($inputId.val()+","+params.name);
			$inputId.attr("name",$inputId.attr("name")+"_"+params.id);
			if(isThreeLevel){
				$inputId.attr("rel",$inputId.attr("rel")+"_"+params.yq);
			}
			if(isSecond){
				$inputId.attr("alt",$inputId.attr("alt")+","+params.relativeId);
			}	
		}
	}
	
	$.fn.MenuList = function(options) {
		return $(this).each(function() {		
			var owl = $.extend({}, defaults, options || {});
			//触发按钮
			var $this = $(this);
			//span
			var $span = $this.find("span");
			//input
			var $input = $this.find("input");
			//浮动层主div
			var $dropmenu= $(owl.dropmenu);
			//与其级联的文本框
			var $relativeInput = $(owl.relativeInput);
			//与其级联的弹出层
			var $relativeDropmenu = $(owl.relativeDropmenu);
			//悬浮层中的搜索框
			var $minput = $(".searchDept input",$dropmenu);
			//悬浮层中的搜索按钮
			var $mspan = $(".searchDept .searchMenu",$dropmenu);				
			//已选择部门div
			var $selectinfo = $(".select-info",$dropmenu);
			//已选择部门ul
			var $selectUl = $("ul",$selectinfo);
			//确认按钮
			var $okbtn = $("a[name='menu-confirm']",$dropmenu);
			//取消按钮
			var $cancelbtn = $("a[name='menu-confirm-cancel']",$dropmenu);
			//清空按钮
			var $clearbtn = $("a[name='menu-confirm-clear']",$dropmenu);
			//弹出层中科室列表
			var $menulist = $(".depts-dl",$dropmenu);
			//设置弹出层列表
			$menulist.css("overflow","auto");
			
			//判断弹出框有没有超出浏览器，如果超出左移
			var winW = $("body").width();
			var leftW = $input.offset().left;
			if(leftW+532+10 > winW){
				$dropmenu.css("left",(winW-leftW-532-10)+"px");
			}else{
				$dropmenu.css("left","0px");
			}
			$(window).resize(function(){
				var winW1 = $("body").width();
				var leftW1 = $input.offset().left;
				if(leftW1+532+10 > winW1){
					$dropmenu.css("left",(winW1-leftW1-532-10)+"px");
				}else{
					$dropmenu.css("left","0px");
				}
			}).resize();
			
			//设置弹出层宽高
			$dropmenu.css("height",owl.height+"px");
			$dropmenu.css("width",owl.width+"px");
			
			//获得焦点时添加阴影
			$input.focus(function(){
				$(this).parent().addClass("textbox-focused");
			});
			$minput.focus(function(){
				$(this).addClass("textbox-focused");
			});
			//失去焦点时移除阴影
			$input.blur(function(){
				$(this).parent().removeClass("textbox-focused");
			});
			$minput.blur(function(){
				$(this).removeClass("textbox-focused");
			});
			var count = 0;
			//存储通过默认id查询出来的name、关联id、院区
			var defaultNames = "";
			var defaultRelativeIds = "";
			var defaultYq = "";
			
			//如果有三级菜单则显示院区
			if(owl.haveThreeLevel){
				var yqstr = "";
				yqstr += "<div class='yqList'>";
				var yqArr = ["2-郑东院区","1-河医院区","3-惠济院区"];
				for(var k = 0;k < yqArr.length;k++){
					var yq = yqArr[k].split("-");
					yqstr += "<span class=''><input type='checkbox' value='"+yq[0]+"'/>"+yq[1]+"</span>";
				}
				yqstr += "</div>";
				$(".searchDept",$dropmenu).append(yqstr);
			}else{
				$(".searchDept",$dropmenu).css("padding-bottom","10px");
			}
			var menuUrl = "";
			if(owl.para != null){
				menuUrl = owl.firsturl+owl.para;
			}else{
				menuUrl = owl.firsturl+"";
			}
			//加载科室列表
			$.ajax({  
				async:false,
				url: menuUrl,
				dataType: "JSON",
				success: function(data) {
					if(typeof(empFlag)!='undefined' && empFlag==true){
						if(data.length == 0){
							$("body").setLoading("body");
							$("#body #content").css({
								top:"50$"
							})
						}
					}
				
		 			//$("body").setLoading("easyui-layout")
					//$("#p .easyui-layout").setLoading("aa")
					var str = "";
					if(data.length > 0){						
						str += "<h2 class=''><input type='checkbox' />全选</h2>";
					}					
					
					for(var i=0;i<data.length;i++){	
						var data1=data[i].menus;
						if(data1.length>0){
							count++;
							str += "<dl><dt class=''><i></i><input type='checkbox' name='checkAll"+i+"' class='selectAll"+i+"'/><span>"+data[i].parentMenu+"</span></dt><dd style='display:none;'><ul class = clearfix>";							
							for(var j=0;j<data1.length;j++){
								//li中的name值为对应的院区
								if(owl.isSecond){
									str += "<li rel='"+data1[j].id+"' class='menuline"+owl.menulines+"'><div class='xMenuDept'><i style='display:none'></i><input type='checkbox' value='"+data1[j].name+"' name='selectDepts' class='"+data1[j].relativeId+"'/><span>"+data1[j].name+"</span></div><div class='threeLevel' style='display:none;'></div></li>";
								}else{
									str += "<li rel='"+data1[j].id+"' class='menuline"+owl.menulines+"'><div class='xMenuDept'><i style='display:none'></i><input type='checkbox' value='"+data1[j].name+"' name='selectDepts'/><span>"+data1[j].name+"</span></div><div class='threeLevel' style='display:none;'></div></li>";
								}	
								//id有重复
								if(owl.chId != null){
									if(!owl.haveThreeLevel){
										var chIds = owl.chId.split("_");
										for(var a = 0;a < chIds.length;a++){
											if(chIds[a] == data1[j].id){
												defaultNames += data1[j].name;
												if(owl.isSecond){
													defaultRelativeIds += data1[j].relativeId;
												}
												if(a != chIds.length-1){
													defaultNames += ",";
													if(owl.isSecond){
														defaultRelativeIds += ",";
													}
												}
											}
										}
									}																		
								}
							}
//							var linum = data1.length%owl.menulines;
//							console.log(data1.length+"---"+owl.menulines+"---"+linum);
//							for(var l = 0;l < owl.menulines-linum;l++){
//								str += "<li class='menuline"+owl.menulines+"'></li>"
//							}
							str += "</ul></dd></dl>";
						}						
					}
					$(".addList",$menulist).html(str);											
					
					//院区默认都为选中状态
					$(".yqList input",$dropmenu).prop("checked",true);
					$(".yqList span",$dropmenu).addClass("selected");
					
					if(owl.haveThreeLevel){
						$(".xMenuDept i",$dropmenu).css("display","block");
						$("input[name='selectDepts']",$dropmenu).css("display","none");
						//设置三级高度
						$(".threeLevel",$dropmenu).css("height",owl.threeLevelHeight+"px");
						$(".threeLevel",$dropmenu).css("width",owl.threeLevelWidth+"px");
					}else{
						$("input[name='selectDepts']",$dropmenu).css("display","inline-block");
					}
					
					if(owl.borderStyle == 1){
						$("dl",$dropmenu).css("border-width","1px 0 0 0");
					}else if(owl.borderStyle == 2){
						$("dl li",$dropmenu).css("border-width","1px 0 0 0");
					}else if(owl.borderStyle == 3){
						$("dl li",$dropmenu).css("border-width","1px");
						$("dl li",$dropmenu).css("margin-right","-1px");
						$("dl li",$dropmenu).css("margin-bottom","-1px");
					}
				}
			});
			
			//加载三级科室
			if(owl.haveThreeLevel){	//添加三级菜单	
				var ficDeptCode = new Array();
				var j = 0;
				$(".xMenuDept",$dropmenu).each(function(){
					ficDeptCode[j] = $(this).parent().attr("rel");
					j++;
				})
				$.ajax({
					async:false,
					url:owl.secondurl,
					dataType: "JSON",
					data:{"ficDeptCode":ficDeptCode.join("_")},
					success: function(data) {
						var threeLevel = data;
						for(var i = 0;i < threeLevel.length;i++){
							
						}
						$(".xMenuDept",$dropmenu).each(function(){
							var parentId = $(this).parent().attr("rel");
							var str = "<ul>";
							for(var i = 0;i < threeLevel.length;i++){
								if(parentId == threeLevel[i].ficCode){	//如果三级的parentId和二级的对应，则把该三级菜单放到相应的二级菜单下
									//name值为院区编码
									if(owl.isSecond){	//如果是第二级
										str += "<li rel='"+threeLevel[i].id+"' class='menuline"+owl.menulines+"' name='"+threeLevel[i].district+"'><input type='checkbox' value='"+threeLevel[i].name+"' name='selectDeptsThree' class='"+threeLevel[i].relativeId+"'/>"+threeLevel[i].name+"</li>";					
									}else{
										str += "<li rel='"+threeLevel[i].id+"' class='menuline"+owl.menulines+"' name='"+threeLevel[i].district+"'><input type='checkbox' value='"+threeLevel[i].name+"' name='selectDeptsThree'/>"+threeLevel[i].name+"</li>";					
									}
									if(owl.chId != null){
										var chIds = owl.chId.split("_");
										for(var a = 0;a < chIds.length;a++){
											if(chIds[a] == threeLevel[i].id){
												defaultNames += threeLevel[i].name;
												defaultYq += threeLevel[i].district;
												if(owl.isSecond){
													defaultRelativeIds += threeLevel[i].relativeId;
												}
												if(a != chIds.length-1){
													defaultNames += ",";
													defaultYq += "_";
													if(owl.isSecond){
														defaultRelativeIds += ",";
													}
												}
											}
										}
									}
								}								
							}
							str += "</ul>";
							$(this).parent().find(".threeLevel").html(str);
						});						
					}
				})
			}
			
			//添加默认值
			if(owl.chId != null){
				$dropmenu.parent().find(".menuInput input").attr("name",owl.chId);
				$dropmenu.parent().find(".menuInput input").val(defaultNames);
				if(owl.isSecond){
					$dropmenu.parent().find(".menuInput input").attr("alt",defaultRelativeIds);
				}
				if(owl.haveThreeLevel){
					$dropmenu.parent().find(".menuInput input").attr("rel",defaultYq);
				}
			}
			
			//选择院区，显示相应院区下的菜单
			$dropmenu.on("click",".yqList span",function(){
				$minput.val("");
				if($(this).attr("class").indexOf("selected") == -1){
					$(this).addClass("selected");
					$(this).find("input").prop("checked",true);
				}else{
					$(this).removeClass("selected");
					$(this).find("input").prop("checked",false);
				}
				var yqArr = new Array();
				var flag = 0;
				$(".yqList span",$dropmenu).each(function(){
					if($(this).attr("class").indexOf("selected") != -1){
						yqArr[flag] = $(this).find("input").val();
						flag++;
					}
				});
				$("dl",$dropmenu).css("display","block");
				$("dl .threeLevel li",$dropmenu).css("display","none");
				$("dl .threeLevel",$dropmenu).each(function(){
					var i = 0;
					for(var j = 0;j < yqArr.length;j++){
						$(this).find("li").each(function(){		
							if($(this).attr("name").trim() == yqArr[j]){									
								$(this).css("display","block");
								i++;
							}														
						});
					}					
					if(i > 0){
						$(this).parent().css("display","block");
					}else{
						$(this).parent().css("display","none");
					}
				});	
				var flagdl = 0;
				$("dl",$dropmenu).each(function(){
					var f = 0;
					$(this).find(".xMenuDept").each(function(){	
						if($(this).parent().css("display") != "none"){
							f = 1;
						}
					})
					if(f > 0){	//如果二级为空，一级隐藏
						$(this).css("display","block");
						flagdl++;
					}else{
						$(this).css("display","none");
					}
				})
				if(flagdl == 0){
					$(".tip",$dropmenu).css("display","block");
					$("h2",$dropmenu).css("display","none");
				}else{
					$(".tip",$dropmenu).css("display","none");
					$("h2",$dropmenu).css("display","inline-block");
				}
			})
			
			//按科室名称模糊查询
			$mspan.click(function(){
				var deptName=$minput.val().trim().toLowerCase();
				var flagdl = 0;	
				if(owl.isSecond){	//如果是第二级，需要根据第一级的id模糊查	
					$("dl",$dropmenu).find("li").css("display","none");
					if($("input",$relativeInput).val()==""){//如果一级为空，则不需要根据一级id查
						if(owl.haveThreeLevel){	
							$("dl .threeLevel",$dropmenu).each(function(){
								var i = 0;
								var selected = 0;
								$(this).find("li").each(function(){		
									if($(this).find("input").val().toLowerCase().indexOf(deptName) != -1){									
										$(this).css("display","block");
										i++;
										if($(this).attr("class").indexOf("selected") == -1){
											selected = 1;
										}
									}														
								});
								if(i > 0){
									$(this).parent().css("display","block");
								}else{
									$(this).parent().css("display","none");
								}
								//如果三级菜单为全选时，二级菜单为选中状态
								if(selected == 0){
									$(this).parent().find(".xMenuDept input").prop("checked",true);
									$(this).parent().parent().addClass("selected");
								}else{
									$(this).parent().find(".xMenuDept input").prop("checked",false);
									$(this).parent().parent().removeClass("selected");
								}
							});	
							var selectAllFlag = 0;
							$("dl",$dropmenu).each(function(){
								var f = 0;
								var s = 0;
								$(this).find(".xMenuDept").each(function(){	
									if($(this).parent().css("display") != "none"){
										f = 1;
										if($(this).parent().attr("class").indexOf("selected") == -1){
											s = 1;
										}
									}
								})
								if(f > 0){	//如果二级为空，一级隐藏
									$(this).css("display","block");
									flagdl++;
								}else{
									$(this).css("display","none");
								}
								//如果二级菜单为全选时，一级菜单为选中状态
								if(s == 0){
									$(this).find("dt input").prop("checked",true);
									$(this).find("dt").addClass("selected");
								}else{
									$(this).find("dt input").prop("checked",false);
									$(this).find("dt").removeClass("selected");
								}
								if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
									selectAllFlag = 1;
								}
							})
							//如果一级全部为选中状态，全选跟着选中
							if(selectAllFlag == 0){
								$("h2",$dropmenu).addClass("selected");
								$("h2 input",$dropmenu).prop("checked",true);
							}else{
								$("h2",$dropmenu).removeClass("selected");
								$("h2 input",$dropmenu).prop("checked",false);
							}
						}else{
							var selectAllFlag = 0;
							$("dl",$dropmenu).each(function(){
								var i = 0;
								var s = 0;
								$(this).find(".xMenuDept").each(function(){		
									if($(this).find("input").val().toLowerCase().indexOf(deptName) != -1){									
										$(this).parent().css("display","block");
										i++;
										if($(this).parent().attr("class").indexOf("selected") == -1){
											s = 1;
										}
									}														
								});
								if(i > 0){
									$(this).css("display","block");
									flagdl++;
								}else{
									$(this).css("display","none");
								}
								if(s == 0){
									$(this).find("dt").addClass("selected");
									$(this).find("dt input").prop("checked",true);
								}else{
									$(this).find("dt").removeClass("selected");
									$(this).find("dt input").prop("checked",false);
								}
								if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
									selectAllFlag = 1;
								}
							});
							//如果一级全部为选中状态，全选跟着选中
							if(selectAllFlag == 0){
								$("h2",$dropmenu).addClass("selected");
								$("h2 input",$dropmenu).prop("checked",true);
							}else{
								$("h2",$dropmenu).removeClass("selected");
								$("h2 input",$dropmenu).prop("checked",false);
							}
						}					
					}else{		//如果一级不为空，则需要根据一级id查	
						var ids=$("input",$relativeInput).attr("name").split("_");	//获得与之关联的一级的id
						if(owl.haveThreeLevel){	
							$("dl .threeLevel",$dropmenu).each(function(){
								var i = 0;
								var selected = 0;
								for(var j = 0;j < ids.length;j++){							
									$(this).find("li").each(function(){		
										if($(this).find("input").attr("class").indexOf(ids[j]) >= 0  && $(this).find("input").val().toLowerCase().indexOf(deptName) != -1){									
											$(this).css("display","block");
											i++;
											if($(this).attr("class").indexOf("selected") == -1){
												selected = 1;
											}
										}														
									});
								}
								if(i > 0){
									$(this).parent().css("display","block");
								}else{
									$(this).parent().css("display","none");
								}
								//如果三级菜单为全选时，二级菜单为选中状态
								if(selected == 0){
									$(this).parent().find(".xMenuDept input").prop("checked",true);
									$(this).parent().parent().addClass("selected");
								}else{
									$(this).parent().find(".xMenuDept input").prop("checked",false);
									$(this).parent().parent().removeClass("selected");
								}
							});	
							var selectAllFlag = 0;
							$("dl",$dropmenu).each(function(){
								var f = 0;
								var s = 0;
								$(this).find(".xMenuDept").each(function(){	
									if($(this).parent().css("display") != "none"){
										f = 1;
										if($(this).parent().attr("class").indexOf("selected") == -1){
											s = 1;
										}
									}
								})
								if(f > 0){	//如果二级为空，一级隐藏
									$(this).css("display","block");
									flagdl++;
								}else{
									$(this).css("display","none");
								}
								//如果二级菜单为全选时，一级菜单为选中状态
								if(s == 0){
									$(this).find("dt input").prop("checked",true);
									$(this).find("dt").addClass("selected");
								}else{
									$(this).find("dt input").prop("checked",false);
									$(this).find("dt").removeClass("selected");
								}
								if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
									selectAllFlag = 1;
								}
							});
							//如果一级全部为选中状态，全选跟着选中
							if(selectAllFlag == 0){
								$("h2",$dropmenu).addClass("selected");
								$("h2 input",$dropmenu).prop("checked",true);
							}else{
								$("h2",$dropmenu).removeClass("selected");
								$("h2 input",$dropmenu).prop("checked",false);
							}
						}else{
							var selectAllFlag = 0;
							$("dl",$dropmenu).each(function(){
								var i = 0;
								var s = 0;
								for(var j = 0;j < ids.length;j++){							
									$(this).find(".xMenuDept").each(function(){		
										if($(this).find("input").attr("class").indexOf(ids[j]) >= 0  && $(this).find("input").val().toLowerCase().indexOf(deptName) != -1){									
											$(this).parent().css("display","block");
											i++;
											if($(this).parent().attr("class").indexOf("selected") == -1){
												s = 1;
											}
										}														
									});
								}
								if(i > 0){
									$(this).css("display","block");
									flagdl++;
								}else{
									$(this).css("display","none");
								}
								//如果二级菜单为全选时，一级菜单为选中状态
								if(s == 0){
									$(this).find("dt input").prop("checked",true);
									$(this).find("dt").addClass("selected");
								}else{
									$(this).find("dt input").prop("checked",false);
									$(this).find("dt").removeClass("selected");
								}
								if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
									selectAllFlag = 1;
								}
							});
							//如果一级全部为选中状态，全选跟着选中
							if(selectAllFlag == 0){
								$("h2",$dropmenu).addClass("selected");
								$("h2 input",$dropmenu).prop("checked",true);
							}else{
								$("h2",$dropmenu).removeClass("selected");
								$("h2 input",$dropmenu).prop("checked",false);
							}
						}
						
					}																
				}else{ //如果是一级，直接进行模糊查
					if(owl.haveThreeLevel){	
						$("dl .threeLevel",$dropmenu).each(function(){
							var i = 0;
							var selected = 0;
							$(this).find("li").each(function(){
								if($(this).find("input").val().toLowerCase().indexOf(deptName) != -1){
									$(this).css("display","block");
									i++;
									if($(this).attr("class").indexOf("selected") == -1){
										selected = 1;
									}
								}else{
									$(this).css("display","none");
								}
							});
							if(i > 0){
								$(this).parent().css("display","block");
							}else{
								$(this).parent().css("display","none");
							}
							//如果三级菜单为全选时，二级菜单为选中状态
							if(selected == 0){
								$(this).parent().find(".xMenuDept input").prop("checked",true);
								$(this).parent().parent().addClass("selected");
							}else{
								$(this).parent().find(".xMenuDept input").prop("checked",false);
								$(this).parent().parent().removeClass("selected");
							}
						});
						var selectAllFlag = 0;
						$("dl",$dropmenu).each(function(){
							var f = 0;
							var s = 0;
							$(this).find(".xMenuDept").each(function(){	
								if($(this).parent().css("display") != "none"){
									f = 1;
									if($(this).parent().attr("class").indexOf("selected") == -1){
										s = 1;
									}
								}
							})
							if(f > 0){	//如果二级为空，一级隐藏
								$(this).css("display","block");
								flagdl++;
							}else{
								$(this).css("display","none");
							}
							if(s == 0){
								$(this).find("dt").addClass("selected");
								$(this).find("dt input").prop("checked",true);
							}else{
								$(this).find("dt").removeClass("selected");
								$(this).find("dt input").prop("checked",false);
							}
							if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
								selectAllFlag = 1;
							}
						})
						//如果一级全部为选中状态，全选跟着选中
						if(selectAllFlag == 0){
							$("h2",$dropmenu).addClass("selected");
							$("h2 input",$dropmenu).prop("checked",true);
						}else{
							$("h2",$dropmenu).removeClass("selected");
							$("h2 input",$dropmenu).prop("checked",false);
						}
					}else{
						var selectAllFlag = 0;
						$("dl",$dropmenu).each(function(){
							var i = 0;
							var s = 0;
							$(this).find(".xMenuDept").each(function(){
								if($(this).find("input").val().toLowerCase().indexOf(deptName) != -1){
									$(this).parent().css("display","block");
									i++;
									if($(this).parent().attr("class").indexOf("selected") == -1){
										s = 1;
									}
								}else{
									$(this).parent().css("display","none");
								}
							});
							if(i > 0){
								$(this).css("display","block");
								flagdl++;
							}else{
								$(this).css("display","none");
							}
							if(s == 0){
								$(this).find("dt").addClass("selected");
								$(this).find("dt input").prop("checked",true);
							}else{
								$(this).find("dt").removeClass("selected");
								$(this).find("dt input").prop("checked",false);
							}
							if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
								selectAllFlag = 1;
							}
						});
						//如果一级全部为选中状态，全选跟着选中
						if(selectAllFlag == 0){
							$("h2",$dropmenu).addClass("selected");
							$("h2 input",$dropmenu).prop("checked",true);
						}else{
							$("h2",$dropmenu).removeClass("selected");
							$("h2 input",$dropmenu).prop("checked",false);
						}
					}
					
				}
				if(flagdl == 0){
					$(".tip",$dropmenu).css("display","block");
					$("h2",$dropmenu).css("display","none");
				}else{
					$(".tip",$dropmenu).css("display","none");
					$("h2",$dropmenu).css("display","inline-block");
				}
				count = flagdl;
				if(owl.spreadNum > 0){
					var sFlag = 0;
					$("dl",$dropmenu).each(function(){
						if(sFlag < owl.spreadNum){
							$(this).find("dd").css("display","block");
							$(this).find("dt i").addClass("slideup");
						}else{
							$(this).find("dd").css("display","none");
							$(this).find("dt i").removeClass("slideup");
						}
						sFlag++;
					});
				}else{
					if(flagdl < 5){
						$("dl dd",$dropmenu).css("display","block");
						$("dl dt i",$dropmenu).addClass("slideup");
					}else{
						$("dl dd",$dropmenu).css("display","none");
						$("dl dt i",$dropmenu).removeClass("slideup");
					}
				}
			});
			
			//绑定回车事件
			$(document).keydown(function(e){
				if(e.keyCode == 13 && $minput.is(":focus")){
					$mspan.click();
				}
			});
			
			//显示下拉框事件
			function slideUpKsnew(){
				$dropmenu.css("display","block");
				$("dl dt input",$dropmenu).prop("checked",false);
				$("dl dt",$dropmenu).removeClass("selected");
				if(owl.spreadNum > 0){
					var sFlag = 0;
					$("dl",$dropmenu).each(function(){
						if(sFlag < owl.spreadNum){
							$(this).find("dd").css("display","block");
							$(this).find("dt i").addClass("slideup");
						}else{
							$(this).find("dd").css("display","none");
							$(this).find("dt i").removeClass("slideup");
						}
						sFlag++;
					});
				}else{
					if(count < 5){
						$("dl dd",$dropmenu).css("display","block");
						$("dl dt i",$dropmenu).addClass("slideup");
					}else{
						$("dl dd",$dropmenu).css("display","none");
						$("dl dt i",$dropmenu).removeClass("slideup");
					}
				}
					            
				$minput.val("");
				$mspan.click();
				if($input.val() == ""){
					if(owl.haveThreeLevel){	//如果有三级菜单
						$menulist.css("height",(owl.height-60-30)+"px");
					}else{
						$menulist.css("height",(owl.height-60)+"px");
					}					
					$selectinfo.css("display","none");
					$selectUl.html("");
					$("dl li",$dropmenu).removeClass("selected");
					$("dl li input",$dropmenu).prop("checked", false);
					$("dl dt",$dropmenu).removeClass("selected");
					$("dl dt input",$dropmenu).prop("checked", false);
					$("h2",$dropmenu).removeClass("selected");
					$("h2 input",$dropmenu).prop("checked", false);
				}else{
					$selectUl.html("");
					$selectUl.parent().css("display","block");
					$("dl li",$dropmenu).removeClass("selected");
					$("dl li input",$dropmenu).prop("checked", false);
					
					var values = $input.attr("name").split("_");
					var texts = $input.val().split(",");
					if(owl.isSecond){
						var rids = $input.attr("alt").split(",");
					}
					if(owl.haveThreeLevel){	//如果有三级菜单
						var yqs = $input.attr("rel").split("_");	//院区编码											
						for(var i = 0;i < values.length;i++){
							//li中的name值为院区编码
							if(owl.isSecond){
								$selectUl.append("<li rel='"+values[i]+"' class='"+rids[i]+"' name='"+yqs[i]+"'>"+texts[i]+"</li>");
							}else{
								$selectUl.append("<li rel='"+values[i]+"' name='"+yqs[i]+"'>"+texts[i]+"</li>");
							}
							$("dl li",$dropmenu).each(function(){
								if($(this).attr("rel") == values[i]){
									$(this).addClass("selected");
									$(this).find("input").prop("checked", true);
								}
							});
						}
						//如果三级全部选中，相应二级为选中状态
						$(".threeLevel",$dropmenu).each(function(){							
							if($(this).html() != ""){
								var flag = 0;
								$(this).find("li").each(function(){
									if($(this).css("display") != "none" && $(this).attr("class").indexOf("selected") == -1){
										flag = 1;
									}
								});
								if(flag == 0){
									$(this).parent().addClass("selected");
									$(this).parent().find(".xMenuDept input").prop("checked",true);
								}
							}							
						});						
					}else{	//如果只有两级				
						for(var i = 0;i < values.length;i++){
							if(owl.isSecond){
								$selectUl.append("<li rel='"+values[i]+"' class='"+rids[i]+"'>"+texts[i]+"</li>");
							}else{
								$selectUl.append("<li rel='"+values[i]+"'>"+texts[i]+"</li>");
							}
							$("dl li",$dropmenu).each(function(){
								if($(this).attr("rel") == values[i]){
									$(this).addClass("selected");
									$(this).find("input").prop("checked", true);
								}
							});
						}
					}	
					var selectAllFlag = 0;
					//如果二级菜单全部选中，相应科室为选中状态
					$("dl",$dropmenu).each(function(){	
						var flag = 0;
						$(this).find("dd .xMenuDept").each(function(){
							if($(this).parent().parent().css("display") != "none" && $(this).parent().attr("class").indexOf("selected") == -1){
								flag = 1;
							}
						});
						if(flag == 0){
							$(this).find("dt").addClass("selected");
							$(this).find("dt input").prop("checked",true);
						}
						if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
							selectAllFlag = 1;
						}
					});					
					//如果一级全部为选中状态，全选跟着选中
					if(selectAllFlag == 0){
						$("h2",$dropmenu).addClass("selected");
						$("h2 input",$dropmenu).prop("checked",true);
					}else{
						$("h2",$dropmenu).removeClass("selected");
						$("h2 input",$dropmenu).prop("checked",false);
					}
					if(owl.haveThreeLevel){	//如果有三级菜单
						$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
					}else{
						$menulist.css("height",(owl.height-66-40-$selectinfo.height())+"px");
					}
					
				}
			}
			
			//获得焦点时显示下拉菜单
/*			$input.focus(function(){				
				if($dropmenu.css("display")=="block"){
					$dropmenu.css("display","none");					
				}else{
					$(".xmenu").css("display","none");
					slideUpKsnew();					
				}
				return false				
			});*/
			
			//点击按钮时显示下拉菜单或者收起下拉菜单
			/*$span.click(function(){
				if($dropmenu.css("display")=="block"){
					$dropmenu.css("display","none");					
				}else{
					slideUpKsnew();					
				}
			});*/
			
			//点击确定按钮
			$okbtn.click(function(){
				var dept=new Array();
				var texts=new Array();
				var rid=new Array(); //关联id
				var yqCode = new Array();	//院区编码
				var iflag = 0;
				$("li",$selectUl).each(function(key,value){					
					if($(this).attr("rel") != "none"){
						dept[iflag] = $(this).attr("rel");
						texts[iflag] = $(this).html();
						if(owl.isSecond){
							rid[iflag] = $(this).attr("class");						
						}
						if(owl.haveThreeLevel){	
							yqCode[iflag] = $(this).attr("name");
						}
						iflag++;
					}
	     			//或者也可以这么写：
	     			// values[key] = $(value).val();
				}); 
				$dropmenu.css("display","none");
				$input.val(texts);
				$input.attr("name",dept.join("_"));
				//如果有三级菜单，要把院区编码赋给input
				if(owl.haveThreeLevel){	
					$input.attr("rel",yqCode.join("_"));
				}
				if(owl.isSecond){ //如果是级联中的第二级
					$input.attr("alt",rid); //级联id
				}else{
					if(owl.isRelative){ //如果是级联中的第一级
						$("input",$relativeInput).val("");	//清空value值（名称）
						$("input",$relativeInput).attr("name",""); //清空name值（id）
						$("input",$relativeInput).attr("alt",""); //清空级联id
						$(".select-info",$relativeDropmenu).css("display","none");
						$(".select-info ul",$relativeDropmenu).html("");
						$("dl dd",$relativeDropmenu).find("li").css("display","none");
						var flagdl = 0;
						$("dl",$relativeDropmenu).each(function(){
							var i =0;
							for(var j = 0;j < dept.length;j++){							
								$(this).find("li").each(function(){		
									if($(this).find("input").attr("class").indexOf(dept[j])>=0){									
										$(this).css("display","block");
										i++;
									}														
								});
							}
							if(i > 0){
								$(this).css("display","block");
								flagdl++;
							}else{
								$(this).css("display","none");
							}
						});
						if(flagdl == 0 && dept.length == 0){//当一级没有选择时，二级菜单全部显示
							$("dl li",$relativeDropmenu).css("display","block");
							$("dl",$relativeDropmenu).css("display","block");
							$(".tip",$relativeDropmenu).css("display","none");
							$("h2",$relativeDropmenu).css("display","inline-block");
						}else if(flagdl == 0 && dept.length != 0){//一级菜单选择了，但是二级菜单中没有检索到
							$(".tip",$relativeDropmenu).css("display","block");
							$("h2",$relativeDropmenu).css("display","none");
						}else{
							$(".tip",$relativeDropmenu).css("display","none");
							$("h2",$relativeDropmenu).css("display","inline-block");
						}
					}										
				}
			});
			
			//点击取消按钮
			$cancelbtn.click(function(){
				$dropmenu.css("display","none");
				if($input.val()!=""){
					var arr=$input.attr("name").split("_");
					if(owl.isSecond){
						var rid=$input.attr("alt").split(",");
					}	
					if(owl.haveThreeLevel){
						var yqCode = $input.attr("rel").split("_");
					}
					var texts=$input.val().split(",");
					$("dl li",$menulist).each(function(){
						for(var i=0;i<arr.length;i++){
							if($(this).attr("rel") == arr[i]){
								$(this).addClass("selected");
								$(this).find("input").prop("checked", true);
							}else{
								$(this).removeClass("selected");
							}
						}					
					});
					$selectUl.html("");
					for(var i=0;i<arr.length;i++){
						if(owl.isSecond){
							if(owl.haveThreeLevel){
								$selectUl.append("<li rel='"+arr[i]+"' class='"+rid[i]+"' name='"+yqCode[i]+"'>"+texts[i]+"</li>");
							}else{
								$selectUl.append("<li rel='"+arr[i]+"' class='"+rid[i]+"'>"+texts[i]+"</li>");
							}
							
						}else{
							if(owl.haveThreeLevel){
								$selectUl.append("<li rel='"+arr[i]+"' name='"+yqCode[i]+"'>"+texts[i]+"</li>");
							}else{
								$selectUl.append("<li rel='"+arr[i]+"'>"+texts[i]+"</li>");
							}
							
						}
					}	
				}											
			});
			
			//点击清空按钮
			$clearbtn.click(function(){
				$selectUl.html("");
				$selectinfo.css("display","none");
				$("dl li",$dropmenu).removeClass("selected");
				$("h2",$dropmenu).removeClass("selected");
				$("dl li input",$dropmenu).prop("checked", false);
				$("dl dt input",$dropmenu).prop("checked", false);
				$("h2 input",$dropmenu).prop("checked", false);
				$input.val("");
				$input.attr("name","");
				$input.attr("alt","");
				$input.attr("rel","");
				$minput.val("");
				if(!owl.isSecond){//如果是一级
					$("dl",$relativeDropmenu).css("display","block");
					$("dl li",$relativeDropmenu).css("display","block");
					$("input",$relativeInput).val("");
					$("input",$relativeInput).attr("name","");
					$("input",$relativeInput).attr("alt","");
					$(".select-info",$relativeDropmenu).css("display","none");
					$(".select-info ul",$relativeDropmenu).html("");
				}
				if(owl.haveThreeLevel){
					$menulist.css("height",(owl.height-60-30)+"px");
				}else{
					$menulist.css("height",(owl.height-60)+"px");
				}
				
				$mspan.click();
			});
			
			//点击已选择的部门后从已选择中删除所点击的部门
			$selectUl.on("click","li",function(){
				var rel=$(this).attr("rel");
				var values=new Array();
				if(owl.haveThreeLevel){	//如果有三级菜单
					$("dl .threeLevel li.selected",$dropmenu).each(function(key,value){
		     			values[key] = $(this).attr("rel");
		     			if(values[key] == rel){
		     				$(this).removeClass("selected");
		     				$(this).find("input").prop("checked", false);
		     			}
		     			//如果当前三级菜单的父级菜单为选中状态，去掉父级的全选状态
		     			if($(this).css("display") != "none" && values[key] == rel && $(this).parent().parent().parent().attr("class").indexOf("selected") != -1){
		     				$(this).parent().parent().parent().removeClass("selected");
		     				$(this).parent().parent().parent().find(".xMenuDept input").prop("checked",false);
		     			}
		     			//如果当前三级菜单的一级菜单为选中状态，去掉一级的全选状态
		     			if($(this).css("display") != "none" && values[key] == rel && $(this).parent().parent().parent().parent().parent().parent().find("dt").attr("class").indexOf("selected") != -1){
		     				$(this).parent().parent().parent().parent().parent().parent().find("dt").removeClass("selected");
		     				$(this).parent().parent().parent().parent().parent().parent().find("dt input").prop("checked",false);
		     			}
		     			//如果全选为选中状态，去掉全选的全选状态
		     			if($(this).css("display") != "none" && values[key] == rel && $(this).parent().parent().parent().parent().parent().parent().parent().find("h2").attr("class").indexOf("selected") != -1){
		     				$(this).parent().parent().parent().parent().parent().parent().parent().find("h2").removeClass("selected");
		     				$(this).parent().parent().parent().parent().parent().parent().parent().find("h2 input").prop("checked",false);
		     			}
					});
				}else{
					$("dl li.selected",$dropmenu).each(function(key,value){
		     			values[key] = $(this).attr("rel");
		     			if(values[key] == rel){
		     				$(this).removeClass("selected");
		     				$(this).find("input").prop("checked", false);
		     			}
		     			//如果当前二级菜单的父级菜单为选中状态，去掉父级的全选状态
		     			if($(this).css("display") != "none" && values[key] == rel && $(this).parent().parent().parent().find("dt").attr("class").indexOf("selected") != -1){
		     				$(this).parent().parent().parent().find("dt").removeClass("selected");
		     				$(this).parent().parent().parent().find("dt input").prop("checked",false);
		     			}
		     			//如果全选为选中状态，去掉全选的全选状态
		     			if($(this).css("display") != "none" && values[key] == rel && $(this).parent().parent().parent().parent().find("h2").attr("class").indexOf("selected") != -1){
		     				$(this).parent().parent().parent().parent().find("h2").removeClass("selected");
		     				$(this).parent().parent().parent().parent().find("h2 input").prop("checked",false);
		     			}
					});				
				}	
				
				$(this).html("");
				$(this).attr("rel","none");
				$(this).attr("name","");
				if(owl.isSecond){//如果是第二级
					$(this).attr("class","");
				}
				var selectFlag = 0;
				$("li",$selectUl).each(function(){
					if($(this).attr("rel") != "none"){
						selectFlag = 1;
					}
				});
				if(selectFlag == 0){
					if(owl.haveThreeLevel){
						$menulist.css("height",(owl.height-60-30)+"px");
					}else{
						$menulist.css("height",(owl.height-60)+"px");
					}					
					$selectinfo.css("display","none");
				}else{
					if(owl.haveThreeLevel){
						$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
					}else{
						$menulist.css("height",(owl.height-66-40-$selectinfo.height())+"px");
					}					
				}
			});
			
			function clickMenu(a){
				//dl li .xMenuDept span
				if($(a).parent().parent().attr("class").indexOf("selected") == -1){
					var text=$(a).parent().parent().find("input").val();
					$(a).parent().parent().find("input").prop("checked", true);
					$(a).parent().parent().addClass("selected");
					var num=$(a).parent().parent().attr("rel");
					//如果有重复的id，则全部为选中状态(只有两级菜单的时候有这个功能)
					$("dl li",$dropmenu).each(function(){
						if($(this).attr("rel") == num){
							$(this).addClass("selected");
							$(this).find("input").prop("checked", true);
						}
					});
					var flag = 0;
					$(a).parent().parent().parent().find(".xMenuDept").each(function(){			
						if($(this).parent().parent().css("display") != "none" && $(this).parent().parent().attr("class").indexOf("selected") == -1){
							flag = 1;
						}
					});
					if(flag == 0){
						$(a).parent().parent().parent().parent().parent().find("dt input").prop("checked",true);
						$(a).parent().parent().parent().parent().parent().find("dt").addClass("selected");
					}
					if(owl.isSecond){	//如果是第二级
						var rid=$(a).parent().parent().find("input").attr("class");
						$selectUl.append("<li rel='"+num+"' class='"+rid+"'>"+text+"</li>");
					}else{
						$selectUl.append("<li rel='"+num+"'>"+text+"</li>");
					}					
					
					$selectinfo.css("display","block");
					var f = 0;
					$(a).parent().parent().parent().find(".xMenuDept").each(function(){
						if($(this).parent().css("display") != "none" && $(this).parent().attr("class").indexOf("selected") == -1){
							f = 1;
						}
					});
					if(f == 0){
						$(a).parent().parent().parent().parent().parent().find("dt input").prop("checked",true);
						$(a).parent().parent().parent().parent().parent().find("dt").addClass("selected");
					}
					var selectAllFlag = 0;
					$("dl",$dropmenu).each(function(){
						if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
							selectAllFlag = 1;
						}
					});
					if(selectAllFlag == 0){
						$("h2 input",$dropmenu).prop("checked",true);
						$("h2",$dropmenu).addClass("selected");
					}
					$menulist.css("height",(owl.height-66-40-$selectinfo.height())+"px");
				}else{
					var num=$(a).parent().parent().attr("rel");
					//如果全选为选中状态时，取消全选的选中状态
					if($("h2",$dropmenu).attr("class").indexOf("selected") != -1){
						$("h2",$dropmenu).removeClass("selected");
						$("h2 input",$dropmenu).prop("checked",false);
					}
					//如果一级菜单为全选状态，二级菜单取消选中时要取消一级菜单的全选状态
					if($(a).parent().parent().parent().parent().parent().find("dt").attr("class").indexOf("selected") != -1){
						$(a).parent().parent().parent().parent().parent().find("dt input").prop("checked",false);
						$(a).parent().parent().parent().parent().parent().find("dt").removeClass("selected");
					}
					$(a).parent().parent().removeClass("selected");
					$(a).parent().parent().find("input").prop("checked", false);
					//如果有重复的id，则全部为未选中状态
					$("dl li",$dropmenu).each(function(){
						if($(this).attr("rel") == num){
							$(this).removeClass("selected");
							$(this).find("input").prop("checked",false);
							if($(this).parent().parent().parent().find("dt").attr("class").indexOf("selected") != -1){
								$(this).parent().parent().parent().find("dt").removeClass("selected");
								$(this).parent().parent().parent().find("dt input").prop("checked",false);
								if($("h2",$dropmenu).attr("class").indexOf("selected") != -1){
									$("h2",$dropmenu).removeClass("selected");
									$("h2 input",$dropmenu).prop("checked",false);
								}
							}
						}
					});
					var values=new Array();
					var flag = 0;
					var rel = $(a).parent().parent().attr("rel");
					$('li',$selectUl).each(function(key,value){
						if($(this).attr("rel") != "none"){
							values[flag] = $(this).attr("rel");							
			     			if(values[flag] == rel){
			     				$(this).remove();
			     			}
			     			flag++;
						}		     			
					});
					if(values.length==1){
						$selectinfo.css("display","none");
						$menulist.css("height",(owl.height-60)+"px");
					}else{
						$menulist.css("height",(owl.height-66-40-$selectinfo.height())+"px");
					}				
				}
			}
			
			function spreadThird(a){
				var _this = a;
				$(_this).parent().parent().find(".threeLevel").css("display","block");
				$(_this).parent().parent().addClass("threeLevelshow");
				$(_this).parent().parent().find("i").addClass("slideup");
				$(_this).parent().parent().find("input[name='selectDepts']").css("display","inline-block");
				
				if($(_this).parent().parent().css("display") != "none" && $(_this).parent().parent().attr("class").indexOf("selected") != -1){
					$(_this).parent().parent().find(".threeLevel li").each(function(key,value){
						$(this).parent().addClass("selected");
						$(this).parent().find("input").prop("checked",true);    			
					});
				}else{
					var values=new Array();
					var flag = 0;
					$('li',$selectUl).each(function(key,value){
						if($(this).attr("rel") != "none"){
							values[flag] = $(this).attr("rel");										     			
						}		     			
					});
					$(_this).parent().parent().find(".threeLevel li").each(function(key,value){
						for(var i = 0;i < values.length;i++){
							if($(this).attr("rel") == values[i]){
								$(this).addClass("selected");
								$(this).find("input").prop("checked",true);
							}
						}	     			
					});
				}
				var n = ($(_this).parent().parent().offset().left)-($(_this).parent().parent().parent().offset().left);
				var b = ($dropmenu.offset().top)+($dropmenu.height())-($(_this).parent().parent().offset().top);
				var c = $(_this).parent().parent().offset().top-$menulist.offset().top;
				//如果弹出层在最右面被遮住的话，悬浮到左面
				if(n+owl.threeLevelWidth > $(_this).parent().parent().parent().width()){
					if(owl.borderStyle == 1){
						$(_this).parent().parent().find(".threeLevel").css("left",Math.round($(_this).parent().parent().parent().width()-n-owl.threeLevelWidth-3)+"px");
					}else if(owl.borderStyle == 2){
						$(_this).parent().parent().find(".threeLevel").css("left",Math.round($(_this).parent().parent().parent().width()-n-owl.threeLevelWidth-3)+"px");
					}else if(owl.borderStyle == 3){
						$(_this).parent().parent().find(".threeLevel").css("left",Math.round($(_this).parent().parent().parent().width()-n-owl.threeLevelWidth-6)+"px");
					}
				}
				//如果弹出层在最下面被遮住的话，悬浮到上面
				if(b < owl.threeLevelHeight && c > owl.threeLevelHeight){
					$(_this).parent().parent().find(".threeLevel").css("top",(0-owl.threeLevelHeight-21)+"px");
					$(_this).parent().css("border-bottom-width","1px");
					$(_this).parent().css("border-top-width","0");
				}
			}
			
			//点击部门列表中的部门如果已选中则从上面删掉，如果没有选中则添加到上面
			$dropmenu.on("click","dl li .xMenuDept span",function(){
				if(!owl.haveThreeLevel){//如果只有两级菜单
					clickMenu(this);
				}else{	//如果有三级菜单
					spreadThird(this);					
				}
			});
			//如果有三级菜单，点击二级前面的加号，展开三级菜单
			$dropmenu.on("click","dl li .xMenuDept i",function(){
				if(owl.haveThreeLevel){
					spreadThird(this);
				}
			})
//			
			//如果是三级的话，鼠标移出，隐藏三级菜单
			$dropmenu.on("mouseleave","dl li",function(){
				if(owl.haveThreeLevel){
					$(this).find(".threeLevel").css("display","none");
					$(this).removeClass("threeLevelshow");
					$(this).find("i").removeClass("slideup");
					$(this).find("input[name='selectDepts']").css("display","none");
				}
			});
			
			//如果有三级菜单，点击二级菜单的多选按钮时三级菜单全部选中
			$dropmenu.on("click","input[name='selectDepts']",function(){
				if(owl.haveThreeLevel){
					if(this.checked){
						var values=new Array();
						var flag = 0;
						$(this).parent().parent().addClass("selected");
						$('li',$selectUl).each(function(key,value){
							if($(this).attr("rel") != "none"){
								values[flag] = $(this).attr("rel");							
				     			flag++;
							}		     
						});
						$(this).parent().parent().find(".threeLevel li").each(function(key,value){
							if($(this).css("display") != "none"){
								var text=$(this).find("input").val();
								$(this).find("input").prop("checked", true);
								var num=$(this).attr("rel");
								var addflag = 0;
								for(var i = 0;i < values.length;i++){
									if(num == values[i]){
										addflag = 1;
									}
								}
								var yqCode = $(this).attr("name");
								if(addflag == 0){
									if(owl.isSecond){	//如果是第二级
										var rid=$(this).find("input").attr("class");
										$selectUl.append("<li rel='"+num+"' class='"+rid+"' name='"+yqCode+"'>"+text+"</li>");
									}else{
										$selectUl.append("<li rel='"+num+"' name='"+yqCode+"'>"+text+"</li>");
									}
								}
													
								$(this).addClass("selected");
								$selectinfo.css("display","block");
								$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
							}							
						});
						var f = 0;
						$(this).parent().parent().parent().find(".xMenuDept").each(function(){
							if($(this).parent().css("display") != "none" && $(this).parent().attr("class").indexOf("selected") == -1){
								f = 1;
							}
						});
						if(f == 0){
							$(this).parent().parent().parent().parent().parent().find("dt input").prop("checked",true);
							$(this).parent().parent().parent().parent().parent().find("dt").addClass("selected");
						}
						var selectAllFlag = 0;
						$("dl",$dropmenu).each(function(){
							if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
								selectAllFlag = 1;
							}
						});
						if(selectAllFlag == 0){
							$("h2",$dropmenu).addClass("selected");
							$("h2 input",$dropmenu).prop("checked",true);
						}
					}else{
						$(this).parent().parent().removeClass("selected");
						$("h2",$dropmenu).removeClass("selected");
						$("h2 input",$dropmenu).prop("checked",false);
						$(this).parent().parent().parent().parent().parent().find("dt").removeClass("selected");
						$(this).parent().parent().parent().parent().parent().find("dt input").prop("checked",false);
						$(this).parent().parent().find(".threeLevel li").each(function(key,value){
							if($(this).css("display") != "none"){
								var rel=$(this).attr("rel");
								$(this).removeClass("selected");
								$(this).find("input").prop("checked", false);
								var values=new Array();
								var flag = 0;
								$('li',$selectUl).each(function(key,value){
									if($(this).attr("rel") != "none"){
										values[flag] = $(this).attr("rel");							
						     			if(values[flag] == rel){
						     				$(this).remove();
						     			}
						     			flag++;
									}		     			
								});
								if(values.length==1){
									$selectinfo.css("display","none");
									$menulist.css("height",(owl.height-60-30)+"px");
								}else{
									$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
								}
							}							
						});
					}
				}else{
					clickMenu(this);
				}
				
			});
			
			$dropmenu.on("click.aa","dl .threeLevel li input",function(){
				$(this).prop("checked", true);
			})
			
			function clickThreeLevel(a){
				var rel=$(a).attr("rel");
				if($(a).attr("class").indexOf("selected") == -1){
					var text=$(a).find("input").val();
					$(a).find("input").prop("checked", true);
					$(a).addClass("selected");
					$selectinfo.css("display","block");
					var num=$(a).attr("rel");
					var yqCode = $(a).attr("name");
					var flag = 0;					
					if(owl.isSecond){	//如果是级联的第二级
						var rid=$(a).find("input").attr("class");
						$selectUl.append("<li rel='"+num+"' class='"+rid+"' name='"+yqCode+"'>"+text+"</li>");
					}else{
						$selectUl.append("<li rel='"+num+"' name='"+yqCode+"'>"+text+"</li>");
					}					
					//如果三级全部选中，它的父级也为选中状态
					$(a).parent().find("li").each(function(){	
						if($(this).css("diaplay") != "none" && $(this).attr("class").indexOf("selected") == -1){
							flag = 1;
						}
					});
					if(flag == 0){
						$(a).parent().parent().parent().addClass("selected");
						$(a).parent().parent().parent().find(".xMenuDept input").prop("checked",true);
					}
					var f = 0;
					$(a).parent().parent().parent().parent().find(".xMenuDept").each(function(){
						if($(this).parent().css("diaplay") != "none" && $(this).parent().attr("class").indexOf("selected") == -1){
							f = 1;
						}
					});
					if(f == 0){
						$(a).parent().parent().parent().parent().parent().parent().find("dt input").prop("checked",true);
						$(a).parent().parent().parent().parent().parent().parent().find("dt").addClass("selected");
					}
					var selectAllFlag = 0;
					$("dl",$dropmenu).each(function(){
						if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
							selectAllFlag = 1;
						}
					});
					if(selectAllFlag == 0){
						$("h2",$dropmenu).addClass("selected");
						$("h2 input",$dropmenu).prop("checked",true);
					}
					$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
					
				}else{
					var values=new Array();
					//如果二级全选为选中状态，三级取消选中时，二级也跟着取消选中
					if($(a).parent().parent().parent().attr("class").indexOf("selected") != -1){																		
						$(a).parent().parent().parent().removeClass("selected");
						$(a).parent().parent().parent().find(".xMenuDept input").prop("checked",false);					
					}	
					if($(a).parent().parent().parent().parent().parent().parent().find("dt").attr("class").indexOf("selected") != -1){
						$(a).parent().parent().parent().parent().parent().parent().find("dt").removeClass("selected");
						$(a).parent().parent().parent().parent().parent().parent().find("dt input").prop("checked",false);
					}
					if($("h2",$dropmenu).attr("class").indexOf("selected") != -1){
						$("h2",$dropmenu).removeClass("selected");
						$("h2 input",$dropmenu).prop("checked",false);
					}
					$(a).removeClass("selected");
					$(a).find("input").prop("checked", false);						
					var flag = 0;
					$('li',$selectUl).each(function(key,value){
						if($(this).attr("rel") != "none"){
							values[flag] = $(this).attr("rel");							
			     			if(values[flag] == rel){
			     				$(this).remove();
			     			}
			     			flag++;
						}		     			
					});	
					if(values.length==1){
						$selectinfo.css("display","none");
						$menulist.css("height",(owl.height-60-30)+"px");
					}else{
						$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
					}
				}
			}
			//点击三级菜单
			$dropmenu.on("click","dl .threeLevel li",function(){
				clickThreeLevel(this);
			});
			
			//鼠标移到三级菜单上，三级菜单所在的div保持显示状态
			$dropmenu.on("mouseover","dl .threeLevel",function(){
				$(this).parent().find(".threeLevel").css("display","block");
				$(this).parent().addClass("threeLevelshow");
				$(this).parent().find("i").addClass("slideup");					
				$(this).parent().find("input[name='selectDepts']").css("display","inline-block");
				return false
			});

			
			//点击展开收缩
			$menulist.on("click","dl dt span",function(){
				var deptType=$(this).parent().find("input").val();
				var dis=$(this).parent().parent().find("dd").css("display");
				if(dis=="none"){
					$(this).parent().parent().find("dd").css("display","block");
					$(this).parent().find("i").addClass("slideup");
				}else{
					$(this).parent().parent().find("dd").css("display","none");
					$(this).parent().find("i").removeClass("slideup");					
				}
			});
			
			$menulist.on("click","dl dt i",function(){
				var deptType=$(this).parent().find("input").val();
				var dis=$(this).parent().parent().find("dd").css("display");
				if(dis=="none"){
					$(this).parent().parent().find("dd").css("display","block");
					$(this).addClass("slideup");
				}else{
					$(this).parent().parent().find("dd").css("display","none");
					$(this).removeClass("slideup");
				}
			});	
			
			//点击一级菜单中的多选按钮
			$menulist.on("click","dl dt input",function(){				
				if(this.checked){
					$(this).parent().addClass("selected");
					if(owl.haveThreeLevel){
						$(this).parent().parent().find("dd .xMenuDept").each(function(key,value){	
							if($(this).parent().css("display") != "none"){
								$(this).parent().find("input").prop("checked", true);
								$(this).parent().addClass("selected");
								$(this).parent().find(".threeLevel li").each(function(){
									if($(this).css("display") != "none"){
										var text=$(this).find("input").val();	
										var num=$(this).attr("rel");	
										var checkflag = 0;
										$("li",$selectinfo).each(function(){
											if($(this).attr("rel") == num){
												checkflag = 1;
											}
										});
										var yqCode = $(this).attr("name");
										if($(this).css("display") != "none" && checkflag == 0){
											if(owl.isSecond){	//如果是第二级
												var rid=$(this).find("input").attr("class");	
												$selectUl.append("<li rel='"+num+"' class='"+rid+"' name='"+yqCode+"'>"+text+"</li>");													
											}else{
												$selectUl.append("<li rel='"+num+"' name='"+yqCode+"'>"+text+"</li>");
											}
										}
										$(this).addClass("selected");
									}									
								});
								$selectinfo.css("display","block");
								$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
							}
						})
					}else{
						var selectValues = new Array();
						var sv = 0;
						$(this).parent().parent().find("dd .xMenuDept").parent().each(function(key,value){
							if($(this).css("display") != "none"){
								var text=$(this).find("input").val();
								$(this).find("input").prop("checked", true);
								var num=$(this).attr("rel");
								selectValues[sv] = num;
								sv++;
								var checkflag = 0;
								$("li",$selectinfo).each(function(){
									if($(this).attr("rel") == num){
										checkflag = 1;
									}
								});
								if($(this).parent().css("display") != "none" && checkflag == 0){
									if(owl.isSecond){	//如果是第二级
										var rid=$(this).find("input").attr("class");
										$selectUl.append("<li rel='"+num+"' class='"+rid+"'>"+text+"</li>");													
									}else{
										$selectUl.append("<li rel='"+num+"'>"+text+"</li>");
									}
								}
								$(this).addClass("selected");
								$selectinfo.css("display","block");
								$menulist.css("height",(owl.height-66-40-$selectinfo.height())+"px");
							}							
						});
						for(var i = 0;i < selectValues.length;i++){
							$("dl li",$dropmenu).each(function(){
								if($(this).attr("rel") == selectValues[i]){
									$(this).addClass("selected");
									$(this).find("input").prop("checked",true);
								}
							});
						}
					}	
					var selectAllFlag = 0;
					$("dl",$dropmenu).each(function(){
						if($(this).css("display") != "none" && $(this).find("dt").attr("class").indexOf("selected") == -1){
							selectAllFlag = 1;
						}
					});
					if(selectAllFlag == 0){
						$("h2",$dropmenu).addClass("selected");
						$("h2 input",$dropmenu).prop("checked",true);
					}
				}else{
					var selectValues = new Array();
					var sv = 0;
					$(this).parent().removeClass("selected");
					if($("h2",$dropmenu).attr("class").indexOf("selected") != -1){
						$("h2",$dropmenu).removeClass("selected");
						$("h2 input",$dropmenu).prop("checked",false);
					}
					if(!owl.haveThreeLevel){
						$(this).parent().parent().find("dd li").each(function(){
							if($(this).css("display") != "none"){
								var rel=$(this).attr("rel");
								selectValues[sv] = rel;
								sv++;
							}
						});
						for(var i = 0;i < selectValues.length;i++){
							$("dl li",$dropmenu).each(function(){
								if($(this).attr("rel") == selectValues[i]){
									$(this).removeClass("selected");
									$(this).find("input").prop("checked",false);
								}
							});
						}
					}
					
					$(this).parent().parent().find("dd li").each(function(key,value){
						if($(this).css("display") != "none"){
							var rel=$(this).attr("rel");
							$(this).removeClass("selected");
							$(this).find("input").prop("checked", false);
							var values=new Array();
							var flag = 0;
							$('li',$selectUl).each(function(key,value){
								if($(this).attr("rel") != "none"){
									values[flag] = $(this).attr("rel");							
					     			if(values[flag] == rel){
					     				$(this).remove();
					     			}
					     			flag++;
								}		     			
							});
							if(values.length==1){
								$selectinfo.css("display","none");
								if(owl.haveThreeLevel){
									$menulist.css("height",(owl.height-60-30)+"px");
								}else{
									$menulist.css("height",(owl.height-60)+"px");
								}							
							}else{
								if(owl.haveThreeLevel){
									$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
								}else{
									$menulist.css("height",(owl.height-66-40-$selectinfo.height())+"px");
								}						
							}
						}						
					});
				}
			});	
			//鼠标点击其他元素时收起下拉菜单
//	 		$(document).click(function(e){
//	 			var _con = $this;
//	 			var _con1 = $dropmenu;
//	 			var _con2 = $(".threeLevel");
//	 			if(!_con.is(e.target) && _con.has(e.target).length==0 && !_con1.is(e.target) && _con1.has(e.target).length==0 && !_con2.is(e.target) && _con2.has(e.target).length==0){
//	 				$dropmenu.css("display","none");
//	 			}
//	 		});
/*			$dropmenu.mouseover(function(){
				$(this).css("display","block");
			});
			
			$(".newMenu").mouseleave(function(){
				$dropmenu.css("display","none");
				$input.blur();
			});*/
			/*$(".doctorInput ").on("click",function(){
				return false
			})*/
			$input.parent().click(function(){
				if($dropmenu.css("display")=="block"){
					$dropmenu.css("display","none");					
				}else{
					$(".xmenu").css("display","none");
					slideUpKsnew();					
				}
				return false
			});
			/*$input.click(function(){
				if($dropmenu.css("display")=="block"){
					$dropmenu.css("display","none");					
				}else{
					$(".xmenu").css("display","none");
					slideUpKsnew();					
				}
				return false
			})*/
			$("body").on("click",function(){
				$dropmenu.css("display","none");
			})
			$(".xmenu").on("click",function(even){
				even.stopPropagation()
			})
			//点击页面中的全选
			$dropmenu.on("click",".depts-dl h2",function(){
				if($(this).attr("class").indexOf("selected") == -1){	
					//点击选中
					$(this).addClass("selected");
					$(this).find("input").prop("checked",true);
					if(owl.haveThreeLevel){
						$("dl dt",$dropmenu).each(function(){
							$(this).addClass("selected");
							$(this).find("input").prop("checked",true);
							$(this).parent().find(".xMenuDept").parent().addClass("selected");
							$(this).parent().find(".xMenuDept input").prop("checked",true);
							$(this).parent().find(".threeLevel li").each(function(){
								if($(this).css("display") != "none"){
									var text=$(this).find("input").val();	
									var num=$(this).attr("rel");	
									var checkflag = 0;
									$("li",$selectinfo).each(function(){
										if($(this).attr("rel") == num){
											checkflag = 1;
										}
									});
									var yqCode = $(this).attr("name");
									if($(this).css("display") != "none" && checkflag == 0){
										if(owl.isSecond){	//如果是第二级
											var rid=$(this).find("input").attr("class");	
											$selectUl.append("<li rel='"+num+"' class='"+rid+"' name='"+yqCode+"'>"+text+"</li>");													
										}else{
											$selectUl.append("<li rel='"+num+"' name='"+yqCode+"'>"+text+"</li>");
										}
									}
									$(this).addClass("selected");
								}									
							});
							$selectinfo.css("display","block");
							$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
						});
					}else{
						$("dl dt",$dropmenu).each(function(){
							$(this).addClass("selected");
							$(this).find("input").prop("checked",true);							
							$(this).parent().find(".xMenuDept").each(function(){
								if($(this).parent().css("display") != "none"){
									$(this).parent().addClass("selected");
									$(this).find("input").prop("checked",true);
									var text=$(this).parent().find("input").val();	
									var num=$(this).parent().attr("rel");	
									var checkflag = 0;
									$("li",$selectinfo).each(function(){
										if($(this).attr("rel") == num){
											checkflag = 1;
										}
									});
									var yqCode = $(this).attr("name");
									if($(this).css("display") != "none" && checkflag == 0){
										if(owl.isSecond){	//如果是第二级
											var rid=$(this).find("input").attr("class");	
											$selectUl.append("<li rel='"+num+"' class='"+rid+"' name='"+yqCode+"'>"+text+"</li>");													
										}else{
											$selectUl.append("<li rel='"+num+"' name='"+yqCode+"'>"+text+"</li>");
										}
									}
									$(this).addClass("selected");
								}									
							});
							$selectinfo.css("display","block");
							$menulist.css("height",(owl.height-46-40-30-$selectinfo.height())+"px");
						});
					}	
				}else{
					$(this).removeClass("selected");
					$(this).find("input").prop("checked",false);
					$(this).parent().removeClass("selected");
					$(this).parent().find("dt").removeClass("selected");
					$(this).parent().find("dt input").prop("checked",false);
					$(this).parent().find("dd li").each(function(key,value){
						if($(this).css("display") != "none"){
							var rel=$(this).attr("rel");
							$(this).removeClass("selected");
							$(this).find("input").prop("checked", false);
							$('li',$selectUl).each(function(key,value){
								if($(this).attr("rel") != "none"){
					     			if($(this).attr("rel") == rel){
					     				$(this).remove();
					     			}
								}		     			
							});
							var selectFlag = 0;
							$("li",$selectUl).each(function(){
								if($(this).attr("rel") != "none"){
									selectFlag = 1;
								}
							});
							if(selectFlag == 0){
								if(owl.haveThreeLevel){
									$menulist.css("height",(owl.height-60-30)+"px");
								}else{
									$menulist.css("height",(owl.height-60)+"px");
								}					
								$selectinfo.css("display","none");
							}else{
								if(owl.haveThreeLevel){
									$menulist.css("height",(owl.height-66-40-30-$selectinfo.height())+"px");
								}else{
									$menulist.css("height",(owl.height-66-40-$selectinfo.height())+"px");
								}					
							}
						}						
					});
				}
			});
		});
	};	
})(jQuery);