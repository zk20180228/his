function Time(data) {
	//设置默认值
	this.rester()
		//设置初始化的值
	this.readtData(data)
		//入口函数
	this.init();
}
Time.prototype = {
	constructor: Time,
	//初始值
	rester: function() {
		this.startRadian = -Math.PI / 2;
		this.endRadian = Math.PI * 3 / 2;
		this.bigRoundColor = "#ccc";
		this.bigRoundLineWidth = 8;
		//H时针参数 M分针参数 S秒钟参数 
		//[0]指针颜色 [1]指针线宽 [2] 指针平头长度  [3] 指针尖头长度
		this.Pointer = {
			H: ["#000", 6, 30,30],
			M: ["yellow", 4, 50,25],
			S: ["red", 2, 70,25]
		}
		this.dishFontSize = "20px";
		this.dishFontfamily = "微软雅黑";
		this.dishBigLineColor = "#000";
		this.dishBigLineLineWidth = 5;
		this.dishSmallLineColor = "#000";
		this.dishSmallLineLineWidth = 2;
	},
	init: function() {
		//画表盘
		this.drawBigRound();
		//绘制指针
		this.setintvalDrawPointer();
	},
	/*计算坐标三角函数*
	 * @param {Object} x 起点
	 * @param {Object} y 起点
	 * @param {Object} r 半径
	 * @param {Object} rad 弧度
	 */
	roundPosition: function(x, y, r, rad) {
		rad += Math.PI;
		return {
			endX: Math.sin(rad) * r + x,
			endY: Math.cos(rad) * r + y,
			rad: rad
		}
	},
	drawBigRoundLine: function() {
		var rad = Math.PI / 30,
			ctx = this.ctx,
			i = 0,
			j = 12,
			end, st;
		for(; i < 60; i++) {
			st = this.roundPosition(this.bigRoundX, this.bigRoundY, this.bigRoundR - this.bigRoundLineWidth / 2, rad * i);
			if(i % 5 == 0) {
				end = this.roundPosition(this.bigRoundX, this.bigRoundY, this.bigRoundR - 25, rad * i)
					//画粗线
				this.drawLine({
					ctx: ctx,
					stX: st.endX,
					stY: st.endY,
					endX: end.endX,
					endY: end.endY
				})
				this.washStroke(ctx, this.dishBigLineColor, this.dishBigLineLineWidth)
				end = this.roundPosition(this.bigRoundX, this.bigRoundY, this.bigRoundR - 40, rad * i)
				this.drawFillText({
					ctx: ctx,
					text: j--,
					x: end.endX,
					y: end.endY,
					textAlign: "center",
					textBaseline: "middle",
					size: this.dishFontSize,
					family: this.dishFontfamily
				})
			} else {
				end = this.roundPosition(this.bigRoundX, this.bigRoundY, this.bigRoundR - 12, rad * i)
				this.drawLine({
					ctx: ctx,
					stX: st.endX,
					stY: st.endY,
					endX: end.endX,
					endY: end.endY
				})
				this.washStroke(ctx, this.dishSmallLineColor, this.dishSmallLineLineWidth)
			}
		}
	},
	drawBigRound: function() {
		var ctx = this.ctx;
		this.drawbun({
			ctx: ctx,
			x: this.bigRoundX,
			y: this.bigRoundY,
			r: this.bigRoundR,
			startRadian: this.startRadian,
			endRadian: this.endRadian
		});
		this.washStroke(ctx, this.bigRoundColor, this.bigRoundLineWidth)
		this.drawBigRoundLine();
	},
	//初始化数据
	readtData: function(data) {
		if(!data.canvas) {
			throw new Error("canvas没有传递");
		}
		data.ctx = data.canvas.getContext("2d");
		for(var key in data) {
			this[key] = data[key];
		}
	},
	/*画圆*
	 * @param {Object} drawData
	 *	ctx:上下文 
	 * 	x,y 圆心的坐标 
	 * 	r圆的半径
	 *  以下可选
	 * 	startRadian 开始弧度 默认 0
	 * 	endRadian 结束弧度 默认2pi
	 */
	drawbun: function(drawData) {
		var ctx = drawData.ctx;
		ctx.beginPath();
		ctx.arc(drawData.x, drawData.y, drawData.r, drawData.startRadian || 0, drawData.endRadian || 2 * Math.PI);
	},
	setintvalDrawPointer: function() {
		var time = new Date(),
			H = time.getHours(),
			M = time.getMinutes(),
			S = time.getSeconds(),
			this_ = this,
			ctx = this_.ctx,
			stX = this_.bigRoundX,
			stY = this_.bigRoundY,
			Pointer = this_.Pointer,
			AMandPM = parseInt(H / 12),
			timeArr = [H % 12, M, S, AMandPM];
		(function() {
			this_.drawPointer({
				ctx: ctx,
				stX: stX,
				stY: stY,
				timeArr: timeArr,
				Pointer: Pointer
			})
		})()
		setInterval(function() {
			this_.ctx.clearRect(0, 0,this_.canvas.width, this_.canvas.height)
			var time = new Date(),
				H = time.getHours(),
				M = time.getMinutes(),
				S = time.getSeconds(),
				AMandPM = parseInt(H / 12);
			this_.drawPointer({
				ctx: ctx,
				stX: stX,
				stY: stY,
				timeArr: [H % 12, M, S, AMandPM],
				Pointer: Pointer
			})
			this_.drawBigRound();
		}, 1000)
	},
	//画指针
	drawPointer: function(drawData) {
		//画线
		var ctx = drawData.ctx,
			stX = drawData.stX,
			timeArr = drawData.timeArr,
			stY = drawData.stY,
			Pointer = drawData.Pointer,
			Srad = Math.PI / 30 * timeArr[2],
			Mrad = Math.PI / 30 * timeArr[1]+Srad/60,
			Hrad = Math.PI / 30 * (timeArr[0] * 5)+Mrad/12,	
			end = [
				this.roundPosition(stX, stY, Pointer.H[2], -Hrad),
				this.roundPosition(stX, stY, Pointer.M[2], -Mrad),
				this.roundPosition(stX, stY, Pointer.S[2], -Srad),
			]
		var i = 0
		for(var key in Pointer) {
			this.drawLine({
				ctx: ctx,
				stX: stX,
				stY: stY,
				endX: end[i].endX,
				endY: end[i].endY
			});
			this.washStroke(ctx, Pointer[key][0], Pointer[key][1]);
			//尖角
			ctx.save();
			ctx.translate(end[i].endX, end[i].endY);
			ctx.rotate(-end[i].rad)
			ctx.moveTo(0, 0);
			ctx.lineTo(Pointer[key][1] / 2, 0);
			ctx.lineTo(0, Pointer[key][3]);
			ctx.lineTo(-Pointer[key][1] / 2, 0);
			ctx.lineTo(0, 0)
			ctx.fillStyle = Pointer[key][0]
			ctx.fill()
			ctx.restore()
			this.drawbun({
				ctx: ctx,
				x: stX,
				y: stY,
				r: 6
			});
			this.washFill(ctx, "red");
			i++;
		}
	},
	//划线
	washStroke: function(ctx, color, lineWidth) {
		ctx.lineWidth = lineWidth;
		ctx.strokeStyle = color;
		ctx.stroke();
	},
	//填充
	washFill: function(ctx, color) {
		ctx.fillStyle = color;
		ctx.fill();
	},
	drawLine: function(positionObj) {
		var ctx = positionObj.ctx;
		ctx.beginPath();
		ctx.moveTo(positionObj.stX, positionObj.stY);
		ctx.lineTo(positionObj.endX, positionObj.endY);
	},
	//绘制填充文字
	drawFillText: function(positionObj) {
		var ctx = positionObj.ctx;
		ctx.textBaseline = positionObj.textBaseline;
		ctx.textAlign = positionObj.textAlign;
		ctx.font = positionObj.size + " " + positionObj.family;
		ctx.fillStyle = positionObj.color || "#ff7544"
		ctx.fillText(positionObj.text, positionObj.x, positionObj.y);
	}
}