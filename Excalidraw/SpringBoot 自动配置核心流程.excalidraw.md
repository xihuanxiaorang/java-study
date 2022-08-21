---

excalidraw-plugin: parsed
tags: [excalidraw]

---
==⚠  Switch to EXCALIDRAW VIEW in the MORE OPTIONS menu of this document. ⚠==


# Text Elements
SpringBoot应用程序启动 ^AyONPHuA

通过SpringFactories机制加载配置文件 ^gpR53eqy

筛选出所有的自动配置类 ^raXdhc2K

将这些类注入到Spring IOC容器中 ^lvL0rooq

即通过ClassLoader去获取classpath中的配置文件META-INF/spring.factories
题外话：SpringFactories机制是Spring框架内置的，是Spring框架对外扩展的
重要入口，在很多地方都会用到。 ^wGMl7gzh

在所有的配置文件META-INF/spring.factories中筛选出
以EnableAutoConfiguration为key的配置值 ^6hsYQJoE

%%
# Drawing
```json
{
	"type": "excalidraw",
	"version": 2,
	"source": "https://excalidraw.com",
	"elements": [
		{
			"id": "ODl3kj-G1zZizdq9xcdWo",
			"type": "rectangle",
			"x": -427,
			"y": -882,
			"width": 188,
			"height": 78,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"seed": 67783294,
			"version": 158,
			"versionNonce": 520829986,
			"isDeleted": false,
			"boundElements": [
				{
					"type": "text",
					"id": "AyONPHuA"
				},
				{
					"id": "GQRWm9rjSkOKlWNeWp0ZT",
					"type": "arrow"
				}
			],
			"updated": 1650526813804,
			"link": null,
			"locked": false
		},
		{
			"id": "AyONPHuA",
			"type": "text",
			"x": -422,
			"y": -877,
			"width": 178,
			"height": 68,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"seed": 1093812258,
			"version": 162,
			"versionNonce": 1097531646,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526781640,
			"link": null,
			"locked": false,
			"text": "SpringBoot应用程\n序启动",
			"rawText": "SpringBoot应用程序启动",
			"fontSize": 20,
			"fontFamily": 4,
			"textAlign": "center",
			"verticalAlign": "middle",
			"baseline": 55,
			"containerId": "ODl3kj-G1zZizdq9xcdWo",
			"originalText": "SpringBoot应用程序启动"
		},
		{
			"id": "KodNxpRZGhG_Braax5H2q",
			"type": "ellipse",
			"x": -445.5,
			"y": -706.5,
			"width": 210,
			"height": 106,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"seed": 1032818558,
			"version": 146,
			"versionNonce": 127480894,
			"isDeleted": false,
			"boundElements": [
				{
					"type": "text",
					"id": "gpR53eqy"
				},
				{
					"id": "zWxobwLo3LYcTb3NDrfQA",
					"type": "arrow"
				},
				{
					"id": "GQRWm9rjSkOKlWNeWp0ZT",
					"type": "arrow"
				},
				{
					"id": "cy6LEctXo9rTluluK_2DP",
					"type": "arrow"
				}
			],
			"updated": 1650526818661,
			"link": null,
			"locked": false
		},
		{
			"id": "gpR53eqy",
			"type": "text",
			"x": -440.5,
			"y": -687.5,
			"width": 200,
			"height": 68,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"seed": 843964514,
			"version": 79,
			"versionNonce": 681455934,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526781640,
			"link": null,
			"locked": false,
			"text": "通过SpringFactories\n机制加载配置文件",
			"rawText": "通过SpringFactories机制加载配置文件",
			"fontSize": 20,
			"fontFamily": 4,
			"textAlign": "center",
			"verticalAlign": "middle",
			"baseline": 55,
			"containerId": "KodNxpRZGhG_Braax5H2q",
			"originalText": "通过SpringFactories机制加载配置文件"
		},
		{
			"id": "mcijlXHrX9c1g7gwmztk-",
			"type": "rectangle",
			"x": -464.5,
			"y": -490.5,
			"width": 248,
			"height": 98,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"seed": 1172557374,
			"version": 147,
			"versionNonce": 747015266,
			"isDeleted": false,
			"boundElements": [
				{
					"type": "text",
					"id": "raXdhc2K"
				},
				{
					"id": "xwMV4FbmWd-00X9CXXXQu",
					"type": "arrow"
				},
				{
					"id": "cy6LEctXo9rTluluK_2DP",
					"type": "arrow"
				},
				{
					"id": "fc118iDBFMQCC7WvPXsMP",
					"type": "arrow"
				}
			],
			"updated": 1650526823747,
			"link": null,
			"locked": false
		},
		{
			"id": "raXdhc2K",
			"type": "text",
			"x": -459.5,
			"y": -458.5,
			"width": 238,
			"height": 34,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"seed": 1531720958,
			"version": 162,
			"versionNonce": 1518426494,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526781640,
			"link": null,
			"locked": false,
			"text": "筛选出所有的自动配置类",
			"rawText": "筛选出所有的自动配置类",
			"fontSize": 20,
			"fontFamily": 4,
			"textAlign": "center",
			"verticalAlign": "middle",
			"baseline": 21,
			"containerId": "mcijlXHrX9c1g7gwmztk-",
			"originalText": "筛选出所有的自动配置类"
		},
		{
			"id": "ygKgefCx7SfPTzHHgze5r",
			"type": "rectangle",
			"x": -503.5,
			"y": -267.5,
			"width": 316,
			"height": 93,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"seed": 71138274,
			"version": 292,
			"versionNonce": 511356962,
			"isDeleted": false,
			"boundElements": [
				{
					"type": "text",
					"id": "lvL0rooq"
				},
				{
					"id": "fc118iDBFMQCC7WvPXsMP",
					"type": "arrow"
				}
			],
			"updated": 1650526823747,
			"link": null,
			"locked": false
		},
		{
			"id": "lvL0rooq",
			"type": "text",
			"x": -498.5,
			"y": -238,
			"width": 306,
			"height": 34,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"seed": 1814198078,
			"version": 254,
			"versionNonce": 1851095486,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526781640,
			"link": null,
			"locked": false,
			"text": "将这些类注入到Spring IOC容器中",
			"rawText": "将这些类注入到Spring IOC容器中",
			"fontSize": 20,
			"fontFamily": 4,
			"textAlign": "center",
			"verticalAlign": "middle",
			"baseline": 21,
			"containerId": "ygKgefCx7SfPTzHHgze5r",
			"originalText": "将这些类注入到Spring IOC容器中"
		},
		{
			"id": "_q2thsm15BEegkjLC4H9l",
			"type": "line",
			"x": -114.49999999999999,
			"y": -708.5,
			"width": 0.09030914313160565,
			"height": 108.07227728143334,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [
				"-mxzImPzvcQEx1i1TeWIE"
			],
			"strokeSharpness": "round",
			"seed": 1597460478,
			"version": 182,
			"versionNonce": 1730578722,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526781640,
			"link": null,
			"locked": false,
			"points": [
				[
					0,
					0
				],
				[
					0.09030914313160565,
					108.07227728143334
				]
			],
			"lastCommittedPoint": null,
			"startBinding": null,
			"endBinding": null,
			"startArrowhead": null,
			"endArrowhead": null
		},
		{
			"id": "wGMl7gzh",
			"type": "text",
			"x": -94.49999999999997,
			"y": -707,
			"width": 676,
			"height": 102,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [
				"-mxzImPzvcQEx1i1TeWIE"
			],
			"strokeSharpness": "sharp",
			"seed": 1592501538,
			"version": 414,
			"versionNonce": 468880098,
			"isDeleted": false,
			"boundElements": [
				{
					"id": "zWxobwLo3LYcTb3NDrfQA",
					"type": "arrow"
				}
			],
			"updated": 1650526791022,
			"link": null,
			"locked": false,
			"text": "即通过ClassLoader去获取classpath中的配置文件META-INF/spring.factories\n题外话：SpringFactories机制是Spring框架内置的，是Spring框架对外扩展的\n重要入口，在很多地方都会用到。",
			"rawText": "即通过ClassLoader去获取classpath中的配置文件META-INF/spring.factories\n题外话：SpringFactories机制是Spring框架内置的，是Spring框架对外扩展的\n重要入口，在很多地方都会用到。",
			"fontSize": 20,
			"fontFamily": 4,
			"textAlign": "left",
			"verticalAlign": "top",
			"baseline": 89,
			"containerId": null,
			"originalText": "即通过ClassLoader去获取classpath中的配置文件META-INF/spring.factories\n题外话：SpringFactories机制是Spring框架内置的，是Spring框架对外扩展的\n重要入口，在很多地方都会用到。"
		},
		{
			"id": "h1rh68ezO6kJCVwq1BHm0",
			"type": "line",
			"x": -107.50000000000001,
			"y": -485.5,
			"width": 0,
			"height": 96,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [
				"gBNie37KPRCtaJd_2YH9S"
			],
			"strokeSharpness": "round",
			"seed": 1039634046,
			"version": 162,
			"versionNonce": 362404158,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526807520,
			"link": null,
			"locked": false,
			"points": [
				[
					0,
					0
				],
				[
					0,
					96
				]
			],
			"lastCommittedPoint": null,
			"startBinding": null,
			"endBinding": null,
			"startArrowhead": null,
			"endArrowhead": null
		},
		{
			"id": "6hsYQJoE",
			"type": "text",
			"x": -87.50000000000003,
			"y": -477,
			"width": 472,
			"height": 68,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [
				"gBNie37KPRCtaJd_2YH9S"
			],
			"strokeSharpness": "sharp",
			"seed": 538770942,
			"version": 307,
			"versionNonce": 209314210,
			"isDeleted": false,
			"boundElements": [
				{
					"id": "xwMV4FbmWd-00X9CXXXQu",
					"type": "arrow"
				}
			],
			"updated": 1650526807520,
			"link": null,
			"locked": false,
			"text": "在所有的配置文件META-INF/spring.factories中筛选出\n以EnableAutoConfiguration为key的配置值",
			"rawText": "在所有的配置文件META-INF/spring.factories中筛选出\n以EnableAutoConfiguration为key的配置值",
			"fontSize": 20,
			"fontFamily": 4,
			"textAlign": "left",
			"verticalAlign": "top",
			"baseline": 55,
			"containerId": null,
			"originalText": "在所有的配置文件META-INF/spring.factories中筛选出\n以EnableAutoConfiguration为key的配置值"
		},
		{
			"id": "zWxobwLo3LYcTb3NDrfQA",
			"type": "arrow",
			"x": -116.5,
			"y": -653.5,
			"width": 104,
			"height": 2,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "round",
			"seed": 134399074,
			"version": 77,
			"versionNonce": 981044286,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526791022,
			"link": null,
			"locked": false,
			"points": [
				[
					0,
					0
				],
				[
					-104,
					-2
				]
			],
			"lastCommittedPoint": null,
			"startBinding": {
				"elementId": "wGMl7gzh",
				"focus": -0.1638795986622074,
				"gap": 22
			},
			"endBinding": {
				"elementId": "KodNxpRZGhG_Braax5H2q",
				"focus": -0.08121829012776953,
				"gap": 15.04786094094976
			},
			"startArrowhead": null,
			"endArrowhead": "arrow"
		},
		{
			"id": "xwMV4FbmWd-00X9CXXXQu",
			"type": "arrow",
			"x": -101.5,
			"y": -439.5522280547442,
			"width": 112,
			"height": 0.4394907000898911,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "round",
			"seed": 739424866,
			"version": 131,
			"versionNonce": 1741774178,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526807521,
			"link": null,
			"locked": false,
			"points": [
				[
					0,
					0
				],
				[
					-112,
					-0.4394907000898911
				]
			],
			"lastCommittedPoint": null,
			"startBinding": {
				"elementId": "6hsYQJoE",
				"focus": -0.13235294117647062,
				"gap": 14
			},
			"endBinding": {
				"elementId": "mcijlXHrX9c1g7gwmztk-",
				"focus": 0.020408163265306124,
				"gap": 3
			},
			"startArrowhead": null,
			"endArrowhead": "arrow"
		},
		{
			"id": "GQRWm9rjSkOKlWNeWp0ZT",
			"type": "arrow",
			"x": -337.5,
			"y": -790.5,
			"width": 1,
			"height": 73,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "round",
			"seed": 385366526,
			"version": 40,
			"versionNonce": 393187070,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526813804,
			"link": null,
			"locked": false,
			"points": [
				[
					0,
					0
				],
				[
					1,
					73
				]
			],
			"lastCommittedPoint": null,
			"startBinding": {
				"elementId": "ODl3kj-G1zZizdq9xcdWo",
				"focus": 0.05520938994348644,
				"gap": 13.5
			},
			"endBinding": {
				"elementId": "KodNxpRZGhG_Braax5H2q",
				"focus": 0.04644376907437968,
				"gap": 11.036531948848122
			},
			"startArrowhead": null,
			"endArrowhead": "arrow"
		},
		{
			"id": "cy6LEctXo9rTluluK_2DP",
			"type": "arrow",
			"x": -341.5,
			"y": -587.5,
			"width": 4,
			"height": 81,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "round",
			"seed": 790633378,
			"version": 40,
			"versionNonce": 1089582754,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526818661,
			"link": null,
			"locked": false,
			"points": [
				[
					0,
					0
				],
				[
					-4,
					81
				]
			],
			"lastCommittedPoint": null,
			"startBinding": {
				"elementId": "KodNxpRZGhG_Braax5H2q",
				"focus": -0.021510073449065886,
				"gap": 13.00226227059271
			},
			"endBinding": {
				"elementId": "mcijlXHrX9c1g7gwmztk-",
				"focus": -0.06494140625,
				"gap": 16
			},
			"startArrowhead": null,
			"endArrowhead": "arrow"
		},
		{
			"id": "fc118iDBFMQCC7WvPXsMP",
			"type": "arrow",
			"x": -341.5,
			"y": -376.5,
			"width": 3,
			"height": 94,
			"angle": 0,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"groupIds": [],
			"strokeSharpness": "round",
			"seed": 1454095550,
			"version": 48,
			"versionNonce": 775971518,
			"isDeleted": false,
			"boundElements": null,
			"updated": 1650526823747,
			"link": null,
			"locked": false,
			"points": [
				[
					0,
					0
				],
				[
					-3,
					94
				]
			],
			"lastCommittedPoint": null,
			"startBinding": {
				"elementId": "mcijlXHrX9c1g7gwmztk-",
				"focus": -0.00855714648818097,
				"gap": 16
			},
			"endBinding": {
				"elementId": "ygKgefCx7SfPTzHHgze5r",
				"focus": -0.006036754160691059,
				"gap": 15
			},
			"startArrowhead": null,
			"endArrowhead": "arrow"
		}
	],
	"appState": {
		"theme": "light",
		"viewBackgroundColor": "#ffffff",
		"currentItemStrokeColor": "#000000",
		"currentItemBackgroundColor": "transparent",
		"currentItemFillStyle": "hachure",
		"currentItemStrokeWidth": 1,
		"currentItemStrokeStyle": "solid",
		"currentItemRoughness": 1,
		"currentItemOpacity": 100,
		"currentItemFontFamily": 4,
		"currentItemFontSize": 20,
		"currentItemTextAlign": "left",
		"currentItemStrokeSharpness": "sharp",
		"currentItemStartArrowhead": null,
		"currentItemEndArrowhead": "arrow",
		"currentItemLinearStrokeSharpness": "round",
		"gridSize": null,
		"colorPalette": {}
	},
	"files": {}
}
```
%%