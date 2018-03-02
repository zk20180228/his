<%@ page contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html style="height: 100%">
<head>
    <title>病种分布图</title>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/echarts3.6.2/echarts.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/echarts3.6.2/map/china-main-province-js/china.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/baseframe/js/basePlugIn/echarts3.6.2/geoCoordMap.js"></script>
</head>
<body style="height: 100%;  margin: 0; background: #0E2A42">
<!-- 模态框（子表） -->
<div class="modal fade" id="manageInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">双击选择病种</h4>
            </div>
            <div class="modal-body">
                <ul id="icdAssorts"></ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="form-inline" style="padding: 10px; position: absolute;z-index: 10;">
    <div class="form-group">
        <label for="seachInput"></label>
        <input style="width: 100px; height: 30px;" type="text" class="form-control" id="seachInput"
               placeholder="选择病种">
    </div>
</div>
<div id="main" style="height: 100%;"></div>
</body>
<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('main'));
    var last15Years = [];
    var currentYear = new Date().getFullYear();
    for (var year = currentYear - 15; year <= currentYear; year++) {
        last15Years.push(year.toString());
    }
    var provinces = {
        广东: "guangdong", 青海: "qinghai", 四川: "sichuan", 海南: "hainan", 陕西: "shanxi1", 甘肃: "gansu", 云南: "yunnan",
        湖南: "hunan", 湖北: "hubei", 黑龙江: "heilongjiang", 贵州: "guizhou", 山东: "shandong", 江西: "jiangxi", 河南: "henan",
        河北: "hebei", 山西: "shanxi", 安徽: "anhui", 福建: "fujian", 浙江: "zhejiang", 江苏: "jiangsu", 吉林: "jilin",
        辽宁: "liaoning", 台湾: "taiwan", 新疆: "xinjiang", 广西: "guangxi", 宁夏: "ningxia", 内蒙古: "neimenggu", 西藏: "xizang",
        北京: "beijing", 天津: "tianjin", 上海: "shanghai", 重庆: "chongqing", 香港: "xianggang", 澳门: "aomen"
    };
    var ageGroup = ['0-7岁', '8-13岁', '14-20岁', '21-35岁', '36-45岁', '46-55岁', '56-65岁', '66-75岁', '76岁以上', '未记录'];
    var options = [];

    function dataFormatter(obj) {
        var temp;
        for (var i = 0; i < last15Years.length; i++) {
            var max = 0;
            var sum = 0;
            temp = obj[last15Years[i]];
            for (var j = 0, l = temp.length; j < l; j++) {
                max = Math.max(max, temp[j]);
                sum += temp[j];
                obj[last15Years[i]][j] = {
                    name: ageGroup[j],
                    value: temp[j]
                }
            }
            obj[last15Years[i] + 'max'] = Math.floor(max / 100) * 100;
            obj[last15Years[i] + 'sum'] = sum;
        }
        return obj;
    }

    function convertData(data) {
        var res = [];
        for (var i = 0; i < data.length; i++) {
            var geoCoord = geoCoordMap[data[i].name];
            if (geoCoord) {
                res.push({
                    name: data[i].name,
                    value: geoCoord.concat(data[i].value)
                });
            }
        }
        return res;
    }

    /**
     * 动态获取option
     * @param timelineData
     * @param max
     * @param mapName 地图名
     * @param options 数组每一个元素对应timeline的每年
     * @returns  option
     */
    function getOption(timelineData, max, mapName, options) {
        return {
            baseOption: {
                timeline: {
                    data: timelineData,
                    label: {
                        normal: {
                            formatter: function (value) {
                                return new Date(value).getFullYear();
                            }
                        },
                        textStyle: {
                            color: '#fff'
                        },
                        color: '#fff'
                    },
                    autoPlay: false,
                    playInterval: 3000,
                    // 默认从前5年开始
                    currentIndex: 11,
                    // padding: [30, 0],
                    symbol: 'diamond',
                    symbolSize: 16,
                    tooltip: {
                        show: false
                    },
                    left: "8%",
                    width:'60%'
                },
                // backgroundColor: '#404a59',
                title: [{
                    // subtext: '数据来自郑州大学第一附属医院',
                    left: '30%',
                    textStyle: {
                        color: '#fff'
                    }
                }, {
                    // subtext: '数据来自郑州大学第一附属医院',
                    right: '10%',
                    textStyle: {
                        color: '#fff'
                    }
                }],
                tooltip: {
                    trigger: 'item'
                    // formatter: function (params) {
                    //     return params.name + ' : ' + params.value[2] + "例";
                    // }
                },
                visualMap: [{
                    // orient: 'horizontal',
                    type: 'piecewise',
                    min: 0,
                    max: max,
                    maxOpen: true,
                    splitNumber: 4,
                    // color: ['#d94e5d', '#eac736', '#50a3ba'],
                    color: ['#ff3333', 'orange', 'lime'],
                    // dimension: 0,
                    seriesIndex: 0,
                    textStyle: {
                        color: '#fff'
                    },
                    top: 'bottom',
                    left: '1%'
                }],
                geo: {
                    map: mapName,
                    label: {
                        normal: {
                            show: true,
                            textStyle: {color: '#FFFFFF'}
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    itemStyle: {
                        normal: {
                            // areaColor: '#323c48',
                            areaColor: '#195166',
                            borderColor: '#111',
                            // borderColor: '#B2A471',
                            borderWidth: 1
                        },
                        emphasis: {
                            // areaColor: '#2a333d'
                            areaColor: '#4DD2D4'
                        }
                    },
                    left: "15%"
                },
                grid: {
                    right: '2.5%',
                    top: '8%',
                    height: '35%',
                    width: '25%'
                },
                xAxis: [
                    {
                        type: 'category',
                        axisLabel: {
                            interval: 0,
                            color: '#fff',
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        data: ageGroup,
                        splitLine: {show: false}
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        // name: '患者例数',
                        // max: 53500,
                        minInterval: 2,
                        axisLabel: {
                            color: '#fff',
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine: {
                            lineStyle: {
                                color: '#333'
                            }
                        }
                    }
                ],
                series: [
                    {
                        name: 'disease',
                        type: 'scatter',
                        coordinateSystem: 'geo',
                        tooltip: {
                            formatter: function (params) {
                                return params.name + ' : ' + params.value[2] + "例";
                            }
                        },
                        symbolSize: function (val) {
                            return 8 + val[2] / 2000;
                        },
                        itemStyle: {
                            emphasis: {
                                borderColor: '#fff',
                                borderWidth: 1
                            }
                        }
                    },
                    {
                        name: '男',
                        type: 'bar',
                        itemStyle:{
                            normal: {
                                color: '#2EC7C9'
                            }
                        }
                    },
                    {
                        name: '女',
                        type: 'bar',
                        itemStyle:{
                            normal: {
                                color: '#8E64EB'
                            }
                        }
                    },
                    {
                        name: '未记录',
                        type: 'bar',
                        itemStyle:{
                            normal: {
                                color: '#56838D'
                            }
                        }
                    },
                    {
                        name: '性别占比',
                        type: 'pie',
                        stillShowZeroSum: false,
                        tooltip: {
                            formatter: '{a}<br/>{b}: {c}例 ({d}%)'
                        },
                        center: ['85%', '75%'],
                        radius: '25%',
                        clockwise: false
                    }, {
                        name: '年龄占比',
                        type: 'pie',
                        stillShowZeroSum: false,
                        tooltip: {
                            formatter: '{a}<br/>{b}: {c}例 ({d}%)'
                        },
                        center: ['85%', '75%'],
                        radius: ['36%', '45%'],
                        clockwise: false
                    }
                ]
            },
            options: options
        };
    }

    // if (option && typeof option === "object") {
    //     myChart.setOption(option, true);
    // }

    function getInitOptions() {
        options = [];
        for (var i = 0; i < last15Years.length; i++) {
            var opt = {title: [], series: []};
            opt.title[0] = {text: last15Years[i] + '年全国病种分布'};
            opt.title[1] = {text: last15Years[i] + '年全国病种患者年龄与性别组成'};
            options.push(opt);
        }
        return options;
    }

    // 初始化页面
    myChart.setOption(getOption(last15Years, 1000, "china", getInitOptions()));
    var timeFn;
    // 单击省市区，右面的柱形图和饼状图显示相应省市区的数据
    myChart.on('click', function (params) {
        clearTimeout(timeFn);
        // 单击延时300ms执行,防止双击时触发单击
        timeFn = setTimeout(function () {
            var currentMapName = myChart.getOption().geo[0].map;
            if (currentMapName === "china") {
                if (params.componentType === "geo") {
                    // 在全国地图界面单击省份
                    mapLevel = 0;
                    loadOption(mapLevel, currentMapName, params.name);
                } else if (params.componentType === "series" && params.componentSubType === "scatter") {
                    // 在全国地图界面单击圆点城市
                    mapLevel = 0;
                    loadOption(mapLevel, currentMapName, params.name);
                }
            } else {
                if (params.componentType === "geo") {
                    // 在省地图界面单击市
                    mapLevel = 1;
                    loadOption(mapLevel, currentMapName, params.name);
                } else if (params.componentType === "series" && params.componentSubType === "scatter") {
                    // 在省地图界面单击圆点区或县
                    mapLevel = 1;
                    loadOption(mapLevel, currentMapName, params.name);
                }
            }
        }, 300);
    });

    // 双击切换全国和省
    myChart.on('dblclick', function (params) {
        // 将第二次的单击事件屏蔽
        clearTimeout(timeFn);
        var currentMapName = myChart.getOption().geo[0].map;
        if (params.componentType === "geo") {
            if (currentMapName === "china") {
                // 在全国地图界面双击省，进入省地图
                $.getScript('${pageContext.request.contextPath}/baseframe/js/basePlugIn/echarts3.6.2/map/china-main-province-js/' + provinces[params.name] + '.js', function () {
                    mapLevel = 1;
                    loadOption(mapLevel, params.name, params.name);
                });
            } else {
                // 在省地图界面双击，返回全国地图
                mapLevel = 0;
                loadOption(mapLevel, "china", "china");
            }
        }
    });
</script>
<script type="text/javascript">
    var $icdAssorts = $('#icdAssorts');
    var $seachInput = $("#seachInput");
    var $manageInfoModal = $("#manageInfoModal");
    var mapLevel;
    $icdAssorts.tree({
        method: "POST",
        url: '${pageContext.request.contextPath}/icdAssort/icdTree.action?id=root',
        checkbox: false
    });
    $seachInput.on("focus", function () {
        $manageInfoModal.modal('show')
    });
    $icdAssorts.on("dblclick", ".tree-node", function () {
        $manageInfoModal.modal('hide');
        var currentMapName = myChart.getOption().geo[0].map;
        if (currentMapName === "china") {
            mapLevel = 0;
        } else {
            mapLevel = 1;
        }
        loadOption(mapLevel, currentMapName, currentMapName);
    });

    /**
     * 加载地图、柱状图和饼状图
     * @param mapLevel 地图地名级别
     * @param mapAddressName 地图地名
     * @param barAndPieAddressName 柱状图和饼状图地名
     */
    function loadOption(mapLevel, mapAddressName, barAndPieAddressName) {
        var selectedNode = $icdAssorts.tree("getSelected");
        if (!selectedNode) {
            $.alert("提示", "请选择病种!");
            return;
        }
        var mapPara = {};
        var barAndPiePara = {};
        mapPara.years = last15Years.join(",");
        barAndPiePara.years = last15Years.join(",");
        if ($icdAssorts.tree("isLeaf", selectedNode.target)) {
            mapPara.icdCode = selectedNode.id;
            barAndPiePara.icdCode = selectedNode.id;
        } else {
            mapPara.icdClassificationId = selectedNode.id;
            barAndPiePara.icdClassificationId = selectedNode.id;
        }
        mapPara.mapLevel = mapLevel;
        mapPara.address = mapAddressName;
        barAndPiePara.address = barAndPieAddressName;
        $.post("${pageContext.request.contextPath}/disease/distribution/queryMapData.action", mapPara,
            function (mapData) {
                if (mapData.resCode === "success") {
                    $.post("${pageContext.request.contextPath}/disease/distribution/queryBarAndPieData.action", barAndPiePara,
                        function (barAndPieData) {
                            if (barAndPieData.resCode === "success") {
                                barAndPieData.data['男'] = dataFormatter(barAndPieData.data['男']);
                                barAndPieData.data['女'] = dataFormatter(barAndPieData.data['女']);
                                barAndPieData.data['未记录'] = dataFormatter(barAndPieData.data['未记录']);
                                options = [];
                                for (var i = 0; i < last15Years.length; i++) {
                                    var opt = {title: [], series: []};
                                    opt.title[0] = {
                                        text: last15Years[i] + '年' + (mapAddressName === "china" ? "全国" : mapAddressName) + '病种分布',
                                        subtext: selectedNode.text
                                    };
                                    opt.title[1] = {
                                        text: last15Years[i] + '年' + (barAndPieAddressName === "china" ? "全国" : barAndPieAddressName) + '患者年龄与性别组成',
                                        subtext: selectedNode.text
                                    };
                                    opt.series[0] = {
                                        data: convertData(mapData.data[last15Years[i]])
                                    };
                                    opt.series[1] = {data: barAndPieData.data['男'][last15Years[i]]};
                                    opt.series[2] = {data: barAndPieData.data['女'][last15Years[i]]};
                                    opt.series[3] = {data: barAndPieData.data['未记录'][last15Years[i]]};
                                    opt.series[4] = {
                                        data: [
                                            {
                                                name: '男',
                                                value: barAndPieData.data['男'][last15Years[i] + "sum"],
                                                label: {
                                                    normal: {
                                                        show: barAndPieData.data['男'][last15Years[i] + "sum"] !== 0,
                                                        color: '#fff',
                                                        textStyle: {
                                                            color: '#fff'
                                                        }
                                                    }
                                                },
                                                labelLine: {
                                                    normal: {
                                                        show: barAndPieData.data['男'][last15Years[i] + "sum"] !== 0
                                                    }
                                                },
                                                itemStyle:{
                                                    normal: {
                                                        color: '#2EC7C9'
                                                    }
                                                }
                                            }, {
                                                name: '女',
                                                value: barAndPieData.data['女'][last15Years[i] + "sum"],
                                                label: {
                                                    normal: {
                                                        show: barAndPieData.data['女'][last15Years[i] + "sum"] !== 0,
                                                        color: '#fff',
                                                        textStyle: {
                                                            color: '#fff'
                                                        }
                                                    }
                                                },
                                                labelLine: {
                                                    normal: {
                                                        show: barAndPieData.data['女'][last15Years[i] + "sum"] !== 0
                                                    }
                                                },
                                                itemStyle:{
                                                    normal: {
                                                        color: '#8E64EB'
                                                    }
                                                }
                                            }, {
                                                name: '未记录',
                                                value: barAndPieData.data['未记录'][last15Years[i] + "sum"],
                                                label: {
                                                    normal: {
                                                        show: barAndPieData.data['未记录'][last15Years[i] + "sum"] !== 0,
                                                        color: '#fff',
                                                        textStyle: {
                                                            color: '#fff'
                                                        }
                                                    }
                                                },
                                                labelLine: {
                                                    normal: {
                                                        show: barAndPieData.data['未记录'][last15Years[i] + "sum"] !== 0
                                                    }
                                                },
                                                itemStyle:{
                                                    normal: {
                                                        color: '#56838D'
                                                    }
                                                }
                                            }
                                        ]
                                    };
                                    var agePieDates = [];
                                    for (var j = 0; j < ageGroup.length; j++) {
                                        var agePieData = {};
                                        agePieData.name = ageGroup[j];
                                        agePieData.value = barAndPieData.data['男'][last15Years[i]][j].value
                                            + barAndPieData.data['女'][last15Years[i]][j].value
                                            + barAndPieData.data['未记录'][last15Years[i]][j].value;
                                        agePieData.label = {
                                            normal: {
                                                show: agePieData.value !== 0,
                                                color: '#fff',
                                                textStyle: {
                                                    color: '#fff'
                                                }
                                            }
                                        };
                                        agePieData.labelLine = {
                                            normal: {
                                                show: agePieData.value !== 0
                                            }
                                        };
                                        agePieDates.push(agePieData);
                                    }
                                    opt.series[5] = {
                                        data: agePieDates
                                    };
                                    options.push(opt);
                                }
                                myChart.setOption(getOption(last15Years, 1000, mapAddressName, options), true);
                            } else if (barAndPieData.resCode === "error") {
                                $.alert("提示", barAndPieData.resMsg)
                            }
                        }, "json");
                } else if (mapData.resCode === "error") {
                    $.alert("提示", mapData.resMsg)
                }
            }, "json");
    }
</script>
</html>