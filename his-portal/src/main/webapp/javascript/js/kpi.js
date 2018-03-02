option1 = {
	tooltip: {
        trigger: 'axis'
    },
	radar: [
	{       
		indicator: [
		{ text: '总诊疗人次' },
		{ text: '门诊诊疗数' },
		{ text: '专家门诊' },
		{ text: '急诊手术例' },
		{ text: '急诊诊疗数' }
		],
		center: ['50%', '50%'],
		radius: 100,
		startAngle: 90,
		splitNumber: 4,
		shape: 'circle',
		name: {
			formatter:'{value}',
			textStyle: {
				color:'#000',
				fontSize:'15'
			}
		},
		splitArea: {
			areaStyle: {
				color: ['rgba(255, 255, 255, 1)',
				'rgba(255, 255,255, 1)', 'rgba(255, 255, 255, 1)',
				'rgba(255, 255,255, 1)', 'rgba(255, 255,255, 1)'],
				shadowColor: 'rgba(0, 0, 0, 1)',
				shadowBlur: 1
			}
		},
		axisLine: {
			lineStyle: {
				color: 'rgba(0, 0, 0, 0.2)'
			}
		},
		splitLine: {
			lineStyle: {
				color: 'rgba(255, 255, 255, 0.5)'
			}
		}
	}
	],
	series: [
	{
		//name: '雷达图',
		type: 'radar',
		tooltip: {
            trigger: 'item'
        },
		itemStyle: {
			emphasis: {
                // color: 各异,
                lineStyle: {
                    width: 4
                }
            }
        },
        data: [
        {
//            value: [1600, 500, 30, 100, 0],
            name: 'KPI雷达',
            areaStyle: {
            	normal: {
                    color: 'rgba(255, 000, 000, 0.6)'
                }
            }
        }
        ]
    }
    ]
}
option2 = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    grid: {
        left: '12%',
        right: '12%',
        bottom: '3%',
        top: '10%',
        containLabel: true
    },
    xAxis: {
        type: 'value',
        splitNumber: '4',
        boundaryGap: [0, 0.03]
    },
    yAxis: {
        type: 'category',
//        data: ['成功人次数','每天门急诊','其他诊疗数','专家门诊*','门诊诊疗数','总诊疗人次']
        data: ['总诊疗人次','门诊诊疗数','专家门诊','急诊手术例','急诊诊疗数']
    },
    series: [
    {
        name: '度量值',
        type: 'bar',
        itemStyle: {
        normal: {
			color: 'rgba(178, 158, 216, 1)'
		}
	},
//	data: [1165, 88609, 963057, 0, 186192, 1032603]
	}
	]
};
option3 = {
	title : {
		text: '总诊疗人次',
		show: true,
		textStyle: {
			fontSize: 18,
			fontWeight: 'bolder',
			color: '#000',
			fontFamily: '黑体'
		}
	},
	series: [
	{
		name: '总诊疗人次',
		type: 'gauge',
		startAngle: 275,
		endAngle: 50,
		min:0,
        max:200,
		data: [{value: 195.1591, name: '10000'}],
		splitNumber: 4,
		center: ['42%', '48%'],
		pointer: {
			length : '80%',
			width : 6,
			color : 'auto'
		},
		title: {
            offsetCenter: ['-30%', '10%'],       // x, y，单位px
            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                fontWeight: 'bolder',
                fontSize: '12'
            }
        },
		axisLine: {
			show: true,
			lineStyle: {
				color: [
				[0.2, '#228b22'],
				[0.8, '#48b'],
				[1, '#ff4500']
				],
				width: 15
			}
		},
		detail: {
			show: false
			// textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
   //              fontSize: '15',
   //              color: "#000",
   //              fontWeight: "bolder",
   //              //baseline: "top"
   //          }
		}  
	}
	]
};
option4 = {
	title : {
		text: '门诊诊疗数',
		show: true,
		textStyle: {
			fontSize: 18,
			fontWeight: 'bolder',
			color: '#000',
			fontFamily: '黑体'
		}
	},
	series: [
	{
		name: '门诊诊疗数',
		type: 'gauge',
		startAngle: 275,
		endAngle: 50,
		min:0,
        max:200,
		data: [{value: 183, name: '10000'}],
		splitNumber: 4,
		center: ['42%', '48%'],
		axisLine: {            // 坐标轴线
		    lineStyle: {       // 属性lineStyle控制线条样式
		        width: 10
		    }
		},
		title: {
            offsetCenter: ['-30%', '10%'],       // x, y，单位px
            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                fontWeight: 'bolder',
                fontSize: '12'
            }
        },
		pointer: {
			length : '80%',
			width : 6,
			color : 'auto'
		},
		axisLine: {
			show: true,
			lineStyle: {
				color: [
				[0.2, '#228b22'],
				[0.8, '#48b'],
				[1, '#ff4500']
				],
				width: 15
			}
		},
		detail: {
			show: false
			// textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
   //              fontSize: '15',
   //              color: "#000",
   //              fontWeight: "bolder",
   //              //baseline: "top"
   //          }
		}  
	}
	]
};
option5={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false,
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 132, 101, 134, 90, 230, 610,780,860,984,895,844],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option6={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[420, 332, 201, 134, 630, 230, 610,780,860,984,895,844],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option7={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 532, 101, 734, 90, 230, 610,780,860,484,895,844],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option8={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[895, 895, 895, 895, 895, 895, 810,780,860,984,895,844],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option9={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 32, 1301, 134, 90, 230, 610,780,860,984,895,844],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option10={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 432, 101, 134, 2210, 2000, 810,780,860,984,895,844],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option11={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 532, 601, 534, 90, 230, 610,780,860,984,895,844],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option12={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[320, 432, 401, 334, 500, 530, 610,780,860,984,895,844],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option13={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 532, 101, 734, 490, 230, 610,780,860,984,895,844],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option14={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 532, 101, -734, 490, 230, 110,780,860,984,-895,144],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option15={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, -532, 101, 734, 490, -230, 110,780,860,984,-895,144],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option16={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, -532, 101, -734, 490, -230, 110,-780,860,984,-895,144],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option17={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 532, 101, 734, 490, -230, 110,780,860,984,-895,144],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option18={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[-120, 532, 101, 734, 490, -230, 110,-780,860,984,895,144],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option19={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 0, -101, 734, 490, -230, 110,780,860,984,-895,144],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option20={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[120, 0, -101, -734, 490, -230, 110,-780,860,984,-895,144],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option21={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[-120, 0, -101, 734, 490, -230, 110,780,860,984,-895,144],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
option22={
	grid: {
		top: '2',
		left: '0',
		right: '0',
		bottom: '0',
		containLabel: true
	},
	xAxis : [
	{
		type : 'category',
		boundaryGap : false,
		data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
		show: false
	}
	],
	yAxis : [
	{
		type : 'value',
		show: false
	}
	],
	series : [
	{
		name:'度量值',
		type:'line',
		stack: '总量',
		areaStyle: {normal: {
			color: 'rgba(124, 195, 110, 1)',

		}},
		data:[-120, 0, -101, 734, 490, -230, 110,-780,860,984,-895,144],

	}
	],
	itemStyle : {
		normal : {
			color:'#1C7E04',

		}
	},
};
