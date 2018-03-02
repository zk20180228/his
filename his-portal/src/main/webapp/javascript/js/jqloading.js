/* 遮罩插件
 * 可选选项
 * smBoxBg 小方格遮罩颜色 默认 #FFFFFF
 * backgroudColor 大遮罩颜色   默认  #000000
 * backgroundImage 动态图片  默认 loading
 * text 文字信息  默认  加载中....
 * opacity:0.6 大遮罩透明度 默认 0.6
 * width:小遮罩的宽度
 * height:小遮罩的高度
 * textFontSize:14, 文字大小
 * textFontColor:"#000000", 文字颜色
 * isImg: true 图片是否存在
 * isText: true 文字是否存在
 * isSmbox: true 文字图片层级是否存在
 * */
/*
 使用demo
 			$("body").setLoading("body")
			setTimeout(function (){
				$("body").rmoveLoading ("body")
			},5000)
			//====================================
			// 设置其他参数时id必须传
			$("body").setLoading({
				id:"body"
			})
			setTimeout(function (){
				$("body").rmoveLoading ("body")
			},5000)
 * */
(function($) {
	$.fn.setLoading = function(data) {
		new setLoadingFn(data, this)
		return this
	}
	$.fn.rmoveLoading = function(id) {
		if(typeof id == "string") {
			$("#" + id).remove();
			return this;
		} else {
			throw new Error("id类型错误");
		}
	}

	function setLoadingFn(data, $dom) {
		this.$ = $
		this.$dom = $dom
		this.init(data, $)
		this.start($dom, $)
		this.addEven(this.data.id, $dom)
	}
	setLoadingFn.prototype = {
		constructor: setLoadingFn,
		init: function(data) {
			//参数处理
			var defaultVal = {
				smBoxBg: "#FFFFFF",
				backgroudColor: "#CCCCCC", //背景色
				backgroundImage: "data:image/gif;base64,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", //背景图片
				text: "加载中....", //文字 
				width: 150, //宽度
				height: 60, //高度
				opacity: 0.5,
				textFontSize: 14,
				textFontColor: "#000000",
				isImg: true,
				isText: true,
				isSmbox: true
			}
			if(typeof data == "object") {
				this.data = $.extend(defaultVal, data);
			} else if(typeof data == "string") {
				defaultVal.id = data
				this.data = defaultVal
			} else {
				throw new Error("参数错误");
			}
			if(!this.data.id) {
				throw new Error("id,错误");
			}
		},
		start: function($dom, $) {
			//大遮罩设置
			var data = this.data,
				top, left, layer, content;
			top = $dom[0].offsetTop// 元素在文档中位置 滚动条不影响
			left = $dom[0].offsetLeft;
			layer = $("<div id=" + data.id + "></div>");
			layer.css({
				zIndex: 998,
				position: "absolute",
				height: $dom.height()+"px",
				width: $dom.width()+"px",
				top: top,
				left: left,
				borderRadius: "3px"
			});
			if(window.addEventListener) { //ie8
				layer.css("backgroundColor", this.rgbaChange(data.backgroudColor, data.opacity))
			} else {
				layer.css("filter", this.ieRgbaChange(data.backgroudColor, data.opacity))
			}
			if(data.isSmbox) {
				//小遮罩设置
				content = $("<div class='contentLoad'></div>");
				content.css({
					textAlign: "left",
					position: "absolute",
					zIndex: 999,
					height: data.height + "px",
					width: data.width + "px",
					verticalAlign: "middle",
					backgroundColor: data.smBoxBg,
					borderRadius: "8px",
					left: (layer.width() - data.width) / 2 + "px",
					top: (layer.height() - data.height) / 2 + "px",
					textAlign: "center",
				});

				if(data.isImg) {
					content.append("<img style='line-height: "+data.height+"px; display: inline; vertical-align:middle;' src='" + data.backgroundImage + "' />")
				}
				if(data.isText) {
					content.append("<span style='line-height: "+data.height+"px; display: inline; margin-left:10px;text-align:center; vertical-align:middle;font-size:" + data.textFontSize + "px ;color:" + data.textFontColor + " ;'>" + data.text + "</span>")
				}
				layer.append(content)
			}
			$("body").append(layer);
		},
		rgbaChange: function(color, opacity) {
			var NOcolor = color.substring(1, 7),
				R = parseInt(NOcolor.substring(0, 2), 16),
				G = parseInt(NOcolor.substring(2, 4), 16),
				B = parseInt(NOcolor.substring(4, 6), 16);
			return "rgba(" + R + "," + G + "," + B + "," + opacity + ")"
		},
		ieRgbaChange: function(color, opacity) {
			var color = color.substring(1, 7),
				ie_opcity = {
					"0.1": "19",
					"0.2": "33",
					"0.3": "4C",
					"0.4": "66",
					"0.5": "7F",
					"0.6": "99",
					"0.7": "b2",
					"0.8": "c8",
					"0.9": "e5"
				};
			return "progid:DXImageTransform.Microsoft.gradient(startcolorstr=#" + ie_opcity[opacity] + color + ",endcolorstr=#" + ie_opcity[opacity] + color + ");"
		},
		addEven: function(id, $dom) {
			var _this = this
             /*window.onresize = function() {
				$("#" + id).remove();
				_this.start($dom, $);
			}*/
		}
	}
})(jQuery)