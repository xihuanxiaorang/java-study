---
excalidraw-plugin: parsed
tags: [excalidraw]
---
==⚠  Switch to EXCALIDRAW VIEW in the MORE OPTIONS menu of this document. ⚠==

# Text Elements

transient Object[] elementData;  
private int size = 0; ^lxE17dBJ

ArrayList - 可变数组 ^kT6uxKVV

无参构造 new ArrayList<E>() => this.elementData = {};  
有参构造 new ArrayList<E>(initialCapacity) => this.elementData =  new Object[initialCapacity]; ^s8hjXJtP

初始化 ^rjtBS96L

add(E e) => 添加一个元素，每次添加元素之前都需要判断是否需要扩容，  
首先判断所需的最小容量是否大于当前数组的容量，如果大于的话，则需要进行扩容操作。  
扩容时分为两种情况：如果当前容量为 0，则将容量扩大至默认容量 10；如果当前容量不为 0，则将数组容量扩大至当前容量的 1.5 倍。 ^NYVI3RSh

%%
# Drawing
```json
{
	"type": "excalidraw",
	"version": 2,
	"source": "https://excalidraw.com",
	"elements": [
		{
			"type": "rectangle",
			"version": 209,
			"versionNonce": 441856243,
			"isDeleted": false,
			"id": "6Af4qeQWrs2LaPQoegKiq",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -312.9649353027344,
			"y": -489.5127410888672,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 344,
			"height": 95,
			"seed": 323776403,
			"groupIds": [
				"S527Ta67hDFoRNLogC_Ng"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"type": "text",
					"id": "lxE17dBJ"
				}
			],
			"updated": 1652186809804,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 208,
			"versionNonce": 1300913540,
			"isDeleted": false,
			"id": "lxE17dBJ",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -307.9649353027344,
			"y": -484.5127410888672,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 328,
			"height": 63,
			"seed": 371517181,
			"groupIds": [
				"S527Ta67hDFoRNLogC_Ng"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1668014338960,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "\ntransient Object[] elementData;  \nprivate int size = 0;",
			"rawText": "transient Object[] elementData;\nprivate int size = 0;",
			"baseline": 60,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "6Af4qeQWrs2LaPQoegKiq",
			"originalText": "\ntransient Object[] elementData;  \nprivate int size = 0;"
		},
		{
			"type": "text",
			"version": 83,
			"versionNonce": 619289235,
			"isDeleted": false,
			"id": "kT6uxKVV",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -306.81878662109375,
			"y": -527.9176483154297,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 190,
			"height": 21,
			"seed": 1897553395,
			"groupIds": [
				"S527Ta67hDFoRNLogC_Ng"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652186809804,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "ArrayList - 可变数组",
			"rawText": "ArrayList - 可变数组",
			"baseline": 18,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "ArrayList - 可变数组"
		},
		{
			"type": "text",
			"version": 309,
			"versionNonce": 738282525,
			"isDeleted": false,
			"id": "s8hjXJtP",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -317.12567138671875,
			"y": -352.3157196044922,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 882,
			"height": 42,
			"seed": 2009678813,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652186809804,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "无参构造 new ArrayList<E>() => this.elementData = {};  \n有参构造 new ArrayList<E>(initialCapacity) => this.elementData =  new Object[initialCapacity];",
			"rawText": "无参构造 new ArrayList<E>() => this.elementData = {};\n有参构造 new ArrayList<E>(initialCapacity) => this.elementData =  new Object[initialCapacity];",
			"baseline": 39,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "无参构造 new ArrayList<E>() => this.elementData = {};  \n有参构造 new ArrayList<E>(initialCapacity) => this.elementData =  new Object[initialCapacity];"
		},
		{
			"type": "line",
			"version": 157,
			"versionNonce": 1609059699,
			"isDeleted": false,
			"id": "AhK9uBGd1DSgEP7WtBpdt",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -478.34785468839107,
			"y": -253.41496426058933,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 1012.3144684579223,
			"height": 1.7038864474184834,
			"seed": 2041261277,
			"groupIds": [],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652186825405,
			"link": null,
			"locked": false,
			"startBinding": null,
			"endBinding": null,
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": null,
			"points": [
				[
					0,
					0
				],
				[
					1012.3144684579223,
					-1.7038864474184834
				]
			]
		},
		{
			"type": "text",
			"version": 32,
			"versionNonce": 139251421,
			"isDeleted": false,
			"id": "rjtBS96L",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -483.24117511175564,
			"y": -410.0616320626809,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 62,
			"height": 21,
			"seed": 574136179,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652186817294,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "初始化",
			"rawText": "初始化",
			"baseline": 18,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "初始化"
		},
		{
			"type": "text",
			"version": 1633,
			"versionNonce": 911456467,
			"isDeleted": false,
			"id": "NYVI3RSh",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -482.5440925922244,
			"y": -214.53886594939968,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 1211,
			"height": 63,
			"seed": 1254280189,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652188608722,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "add(E e) => 添加一个元素，每次添加元素之前都需要判断是否需要扩容，  \n首先判断所需的最小容量是否大于当前数组的容量，如果大于的话，则需要进行扩容操作。  \n扩容时分为两种情况：如果当前容量为 0，则将容量扩大至默认容量 10；如果当前容量不为 0，则将数组容量扩大至当前容量的 1.5 倍。",
			"rawText": "add(E e) => 添加一个元素，每次添加元素之前都需要判断是否需要扩容，\n首先判断所需的最小容量是否大于当前数组的容量，如果大于的话，则需要进行扩容操作。\n扩容时分为两种情况：如果当前容量为0，则将容量扩大至默认容量10；如果当前容量不为0，则将数组容量扩大至当前容量的1.5倍。",
			"baseline": 60,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "add(E e) => 添加一个元素，每次添加元素之前都需要判断是否需要扩容，  \n首先判断所需的最小容量是否大于当前数组的容量，如果大于的话，则需要进行扩容操作。  \n扩容时分为两种情况：如果当前容量为 0，则将容量扩大至默认容量 10；如果当前容量不为 0，则将数组容量扩大至当前容量的 1.5 倍。"
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