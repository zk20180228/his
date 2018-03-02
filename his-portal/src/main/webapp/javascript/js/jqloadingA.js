/* 遮罩插件
 * 可选选项
 * smBoxBg 小方格遮罩颜色 默认 #FFFFFF
 * backgroudColor 大遮罩颜色   默认  #000000
 * backgroundImage 动态图片  默认 loading.gif
 * text 文字信息  默认  加载中....
 * opacity:0.6 大遮罩透明度 默认 0.6
 * */
/*
 使用demo
 			$("body").setLoading("body")
			setTimeout(function (){
				$("body").rmoveLoading ("body")
			},5000)
			//====================================
			// 设置其他参数id必须传
			$("body").setLoading({
				id:"body"
			})
			setTimeout(function (){
				$("body").rmoveLoading ("body")
			},5000)
 * */

;
(function($) {
	$.fn.setLoading = function(option) {
		var defaultVal = {
				smBoxBg: "#FFFFFF",
				backgroudColor: "#CCCCCC", //背景色
				text: "无数据权限，请申请！", //文字 
				width: 170, //宽度
				height: 60, //高度
				type: 1, //0全部遮，1 局部遮
				opacity: 0.5
			},
			opt;

		if(defaultVal.backgroudColor.charAt(0) == "#") {
			var NOcolor = defaultVal.backgroudColor.substring(1, 7),
				R = parseInt(NOcolor.substring(0, 2), 16),
				G = parseInt(NOcolor.substring(2, 4), 16),
				B = parseInt(NOcolor.substring(4, 6), 16);
			defaultVal.rgba = "rgba(" + R + "," + G + "," + B + "," + defaultVal.opacity + ")"
		} else {
			defaultVal.rgba = defaultVal.backgroudColor
		}

		if(typeof option == "string") {
			opt = $.extend({}, defaultVal, {
				id: option
			});
		} else {
			opt = $.extend({}, defaultVal, option);
		}

		if(opt.id === undefined) {
			throw new Error("name 未定义");
		} else {
			openPartialLayer(this, opt);
		}

		//遮罩
		function openPartialLayer(obj, opt) {
			var eheight, ewidth, top, left, layer, content;
			if(opt.id == "body") {
				eheight = "100%"
				ewidth = "100%"
			} else {
				eheight = $(obj).css("height"); //元素带px的高宽度
				ewidth = $(obj).css("width");
			}
			top = $(obj).offset().top; // 元素在文档中位置 滚动条不影响
			left = $(obj).offset().left;
			layer = $("<div id=" + opt.id + "></div>");
			layer.css({
				position: "absolute",
				height: eheight,
				width: ewidth,
				background: opt.rgba,
				top: top,
				left: left,
				borderRadius: "3px",
				zIndex:555,
				textAlign: "center"
			});
			if(opt.id == "body") {
				layer.css({
					height: "100%",
					width: "100%"
				})
			}
			content = $("<div id='content'></div>");
			content.css({
				textAlign: "left",
				position: "absolute",
				zIndex: 9999,
				height: opt.height + "px",
				width: opt.width + "px",
				verticalAlign: "middle",
				background: opt.smBoxBg,
				borderRadius: "8px",
				fontSize: "13px",
				top:"50%",
				left:"50%",
				marginLeft:"-75px",
				textAlign: "center"
			});
			content.append("<span style='text-align:center; vertical-align:middle;line-height:60px;font-size: 16px;'>" + opt.text + "</span>")
			layer.append(content)
			$("body").append(layer);
			return this;
		}
	};
	$.fn.rmoveLoading = function(id) {
		if(typeof id == "string") {
			$("#" + id).remove();
			return this;
		}
	}
})(jQuery)