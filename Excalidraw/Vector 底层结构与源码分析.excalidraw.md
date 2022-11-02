---
excalidraw-plugin: parsed
tags: [excalidraw]
---
==⚠  Switch to EXCALIDRAW VIEW in the MORE OPTIONS menu of this document. ⚠==

# Text Elements

protected Object[] elementData;  
protected int elementCount;  
protected int capacityIncrement; ^OvI9giMz

Vector - 可变数组 ^o20GN8ro

无参构造 new Vector<E>();  => this.elementData = new Object[10];this.capacityIncrement = 0;  
有参构造 new Vector<E>(initialCapacity); => this.elementData = new Object[initialCapacity];this.capacityIncrement = 0;  
有参构造 new Vector<E>(initialCapacity, capacityIncrement); => this.elementData = new Object[initialCapacity];this.capacityIncrement = capacityIncrement;  
其中，initialCapacity 表示数组初始容量大小；capacityIncrement 表示每次容量扩容时应该增加的大小 ^ZlSAOEqx

初始化 ^p0cKMFme

add(E e) => 添加一个元素，每次添加元素前判断是否需要进行扩容  
首先判断所需的最小容量是否大于当前数组的容量，如果大于的话，则需要进行扩容操作。  
扩容时判断当 capacityIncrement<=0 时，每次扩容至原来容量大小的 2 倍；当 capacityIncrement>0 时，每次扩容至原来容量大小 +capacityIncrement ^Sj5qwXNW

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
			"version": 397,
			"versionNonce": 182977875,
			"isDeleted": false,
			"id": "Bu32qNmBE8PE0x3Ayuphq",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -532.2061869303385,
			"y": -515.3773701985676,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 403.4888916015625,
			"height": 115,
			"seed": 2140855699,
			"groupIds": [
				"pZ0Ml9o-lb-WfHTNZ8If-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"type": "text",
					"id": "OvI9giMz"
				}
			],
			"updated": 1652188495949,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 395,
			"versionNonce": 464201331,
			"isDeleted": false,
			"id": "OvI9giMz",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -527.2061869303385,
			"y": -510.37737019856763,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 393.4888916015625,
			"height": 105,
			"seed": 606145491,
			"groupIds": [
				"pZ0Ml9o-lb-WfHTNZ8If-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652188613635,
			"link": null,
			"locked": false,
			"fontSize": 20.149744909153817,
			"fontFamily": 4,
			"text": "protected Object[] elementData;\nprotected int elementCount;\nprotected int capacityIncrement;",
			"rawText": "protected Object[] elementData;\nprotected int elementCount;\nprotected int capacityIncrement;",
			"baseline": 92,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "Bu32qNmBE8PE0x3Ayuphq",
			"originalText": "protected Object[] elementData;\nprotected int elementCount;\nprotected int capacityIncrement;"
		},
		{
			"type": "text",
			"version": 340,
			"versionNonce": 1627513587,
			"isDeleted": false,
			"id": "o20GN8ro",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -526.0177103678385,
			"y": -548.7426045735676,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 154,
			"height": 34,
			"seed": 1331190547,
			"groupIds": [
				"pZ0Ml9o-lb-WfHTNZ8If-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652188495949,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "Vector - 可变数组",
			"rawText": "Vector - 可变数组",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "Vector - 可变数组"
		},
		{
			"type": "text",
			"version": 1219,
			"versionNonce": 79157779,
			"isDeleted": false,
			"id": "ZlSAOEqx",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -534.3323165651351,
			"y": -383.55759248279384,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 1354,
			"height": 135,
			"seed": 118481149,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652188660687,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "无参构造 new Vector<E>();  => this.elementData = new Object[10];this.capacityIncrement = 0;\n有参构造 new Vector<E>(initialCapacity); => this.elementData = new Object[initialCapacity];this.capacityIncrement = 0;\n有参构造 new Vector<E>(initialCapacity, capacityIncrement); => this.elementData = new Object[initialCapacity];this.capacityIncrement = capacityIncrement;\n其中，initialCapacity表示数组初始容量大小；capacityIncrement表示每次容量扩容时应该增加的大小",
			"rawText": "无参构造 new Vector<E>();  => this.elementData = new Object[10];this.capacityIncrement = 0;\n有参构造 new Vector<E>(initialCapacity); => this.elementData = new Object[initialCapacity];this.capacityIncrement = 0;\n有参构造 new Vector<E>(initialCapacity, capacityIncrement); => this.elementData = new Object[initialCapacity];this.capacityIncrement = capacityIncrement;\n其中，initialCapacity表示数组初始容量大小；capacityIncrement表示每次容量扩容时应该增加的大小",
			"baseline": 122,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "无参构造 new Vector<E>();  => this.elementData = new Object[10];this.capacityIncrement = 0;\n有参构造 new Vector<E>(initialCapacity); => this.elementData = new Object[initialCapacity];this.capacityIncrement = 0;\n有参构造 new Vector<E>(initialCapacity, capacityIncrement); => this.elementData = new Object[initialCapacity];this.capacityIncrement = capacityIncrement;\n其中，initialCapacity表示数组初始容量大小；capacityIncrement表示每次容量扩容时应该增加的大小"
		},
		{
			"type": "text",
			"version": 25,
			"versionNonce": 1114089587,
			"isDeleted": false,
			"id": "p0cKMFme",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -711.4485741267129,
			"y": -398.85701279413115,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 61,
			"height": 34,
			"seed": 1695121235,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652188508018,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "初始化",
			"rawText": "初始化",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "初始化"
		},
		{
			"type": "line",
			"version": 129,
			"versionNonce": 560466451,
			"isDeleted": false,
			"id": "sdJPRU0m3VTHdcqhGpJDP",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -697.5329781184121,
			"y": -187.42176346551787,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 1535.9052276611328,
			"height": 9.128646850585938,
			"seed": 1256202771,
			"groupIds": [],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652188519783,
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
					1535.9052276611328,
					-9.128646850585938
				]
			]
		},
		{
			"type": "text",
			"version": 258,
			"versionNonce": 1469072691,
			"isDeleted": false,
			"id": "Sj5qwXNW",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -681.9312477717324,
			"y": -123.42141251336943,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 1284,
			"height": 101,
			"seed": 512938333,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652188703291,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "add(E e) => 添加一个元素，每次添加元素前判断是否需要进行扩容\n首先判断所需的最小容量是否大于当前数组的容量，如果大于的话，则需要进行扩容操作。\n扩容时判断当capacityIncrement<=0时，每次扩容至原来容量大小的2倍；当capacityIncrement>0时，每次扩容至原来容量大小+capacityIncrement",
			"rawText": "add(E e) => 添加一个元素，每次添加元素前判断是否需要进行扩容\n首先判断所需的最小容量是否大于当前数组的容量，如果大于的话，则需要进行扩容操作。\n扩容时判断当capacityIncrement<=0时，每次扩容至原来容量大小的2倍；当capacityIncrement>0时，每次扩容至原来容量大小+capacityIncrement",
			"baseline": 89,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "add(E e) => 添加一个元素，每次添加元素前判断是否需要进行扩容\n首先判断所需的最小容量是否大于当前数组的容量，如果大于的话，则需要进行扩容操作。\n扩容时判断当capacityIncrement<=0时，每次扩容至原来容量大小的2倍；当capacityIncrement>0时，每次扩容至原来容量大小+capacityIncrement"
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