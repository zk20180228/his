/*
 使用demo
 	$(".scrollContent").HonryScorll({
		type: "x", 方向 暂时只能未纵向以后会添加
		id: "aa"  Id必须传
	})
	<div class="scrollBox">
		<div class="scrollContent">
			内容
		</div>
	</div>
	css
	.scrollBox {
		width: 400px;
		height: 400px;
		overflow-x: hidden;
		position: relative;
		margin: 200px;
	}
	.scrollContent {
		position: absolute;
		top: 0;
		left: 0px;
		width: 5000px;
	}
  */
$.fn.HonryScorll = function(item) {
	new Scorll(this, item)
	return this
}
function Scorll($dom, item) {
	this.in$dom = $dom
	this.out$dom = $dom.parent()
	this.$ = $
	this.init(item)
}
Scorll.prototype = {
	construction: Scorll,
	init: function(item) {
		this.reset(item, this.$) // 初始化参数
		this.setScorll(this.$) // 滚动条dom设置
		this.setEven(this.$) // 
	},
	reset: function(item, $) {
		var resitem = {
			type: "y", //方向
			arrow: "false", //箭头
			scorllColor: "#d1d1d1", //颜色
			scorllBgColor: "#f1f1f1", //背景颜色
			roller: "false", //滚轮,
			scorllHeight: 10
		}
		this.item = $.extend(resitem, item);
		if(this.item.id == null) {
			alert("id没有")
			return false
		}
	},
	setScorll: function($) {
		if(this.item.type == "x") {
			this.setScorllX($, this.in$dom.width(), this.out$dom.width())
		}
		if(this.item.type == "y") {

		}
		if(this.item.type == "auto") {

		}
	},
	setScorllX: function($, inwidth, outwidth) { //x滚动条
		// 可滚动最大
		var bi = outwidth / inwidth
		$(this.out$dom).append("<div id = " + this.item.id + " style = width:100%;position:absolute;left:0;bottom:0;height:" + this.item.scorllHeight + "px;background-color:" + this.item.scorllBgColor + "><div>")
		// 滚动条宽度
		var inscrollBox = $(this.out$dom).width()
		var inscroll = $(this.out$dom).width() * bi
		$("#" + this.item.id).append("<div id = in" + this.item.id + " style = height:100%;position:absolute;left:0;top:0;background-color:" + this.item.scorllColor + ";width:" + inscroll + "px; ></div>")
		this.scorllData = {
			$scorll: $("#in" + this.item.id),
			movebi: (inwidth - outwidth) / (inscrollBox - inscroll),
			inwidth: inwidth,
			outwidth: outwidth,
			inscrollBox: inscrollBox,
			inscroll: inscroll
		}
	},
	setEven: function($) {
		var self = this
		var $scorll = this.scorllData.$scorll
		var $indom = this.in$dom
		var $out$dom = this.out$dom
		$out$dom.on("selectstart", function() {
			return false
		})
		$indom.on("selectstart", function() {
			return false
		})
		$scorll.on("selectstart",function(){
			return false
		})
		$scorll.on("mousedown", function(even) {
			self.setmoveScorllEven($scorll, even.clientX)
		})
		$scorll.on("mouseup", function() {
			$scorll.off("mousemove")
		})
		$(document).on("mouseup.scorll", function() {
			$(document).off("mousemove.csroll")
		})
	},
	setmoveScorllEven: function($scorll, statpos) { //鼠标移动时间
		var self = this
		var inscrollwidthDiff = this.scorllData.inscrollBox - this.scorllData.inscroll
		var $scorll = this.scorllData.$scorll
		var in$dom = this.in$dom
		var movebi = this.scorllData.movebi
		var statpos = statpos - $scorll[0].offsetLeft
		$(document).on("mousemove.csroll", function(even) {
			var diff = even.clientX - statpos 
			if(diff >= 0 && diff <= inscrollwidthDiff) {
				$scorll.css("left", diff + "px")
				in$dom.css("left", - diff * movebi + "px")
			}
		})
	}
}