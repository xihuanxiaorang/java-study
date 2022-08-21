---

excalidraw-plugin: parsed
tags: [excalidraw]

---
==⚠  Switch to EXCALIDRAW VIEW in the MORE OPTIONS menu of this document. ⚠==


# Text Elements
LinkedList - 双向链表 ^UeynDHph

private Node<E> first = null;
private Node<E> last = null;
private int size = 0; ^M1ITKymo

private Node<E> prev;
private Node<E> next;
private E item; ^D5TU7CJn

Node<E> - 节点 ^kUyROJ6e

初始化 ^diFxI4sT

add(E e) => 添加一个元素，添加到链表的最后面
1.定义一个临时节点L指向当前last
2.新建一个节点newNode用于保存e，newNode的prev指向last，newNode的last=null
3.将last指向当前新建的节点，last = newNode
4.如果临时节点L=NULL，则表示当前链表中只有当前的新建的节点，那么将first也指向newNode；
如果临时节点L != NULL，则表示当前链表中还有其他节点，则将节点L的next指向newNode
5.size++，数量+1 ^DyjO2EJY

Node1{
prev = NULL;
next = null;
item = e;
} ^z5BlyKrf

first ^wHBIOSbj

last ^wAJdLiIS

第一次添加元素情况 ^s3ved3oI

Node1{
prev = NULL;
next = Node2;
item = e;
} ^vadYUbSW

first ^ttrMv66i

Node2{
prev = Node1;
next = null;
item = e2;
} ^4UvWRuUm

第二次添加元素情况 ^OJyC84bf

添加元素的关键点在于让当前新增节点的prev指向last，last的next指向新增的节点 ^8BQkLN7c

last ^UQFyC2BO

remove() => 删除一个元素，删除的是第一个节点
1.定义一个临时节点f指向当前first
2.定义一个临时节点next指向临时节点的next节点，即原来链表中当前要被删除节点的下一个节点
3.将节点f的item置为null，next置为null，为了让GC回收
4.将first指向临时节点next，即fisrt指向下一个节点
5.如果临时节点next=null，则表示当前链表中只有当前要被删除的节点，所以将last置为null；
如果临时节点next != null，则将临时节点next的prev置为null
其中的临时节点next可以替换为下一个节点
6.size--，数量-1 ^i2f1uKfY

Node1{
prev = NULL;
next = Node2;
item = e1;
} ^CPIRM2sT

Node2{
prev = Node1;
next = NULL;
item = e2;
} ^LSOK6bff

 ^y50cLjcu

first ^oYYVKcCX

last ^3JEKWeyY

未删除头节点前的情况 ^vK8cZG6K

Node2{
prev = NULL;
next = NULL;
item = e2;
} ^CTv9fr6c

first ^PgS0it5s

last ^cNI8YQ6b

删除头节点后的情况 ^D1BA6ELf

删除元素的关键点在于让当前要被删除节点的下一个节点的prev置为null ^lIvKLEeD

%%
# Drawing
```json
{
	"type": "excalidraw",
	"version": 2,
	"source": "https://excalidraw.com",
	"elements": [
		{
			"type": "text",
			"version": 183,
			"versionNonce": 393551987,
			"isDeleted": false,
			"id": "UeynDHph",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -320.3052062988281,
			"y": -338.6483459472656,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 184,
			"height": 34,
			"seed": 1043006419,
			"groupIds": [
				"7vA9q2N1y39kbRhkIxZeQ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361602,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "LinkedList - 双向链表",
			"rawText": "LinkedList - 双向链表",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "LinkedList - 双向链表"
		},
		{
			"type": "rectangle",
			"version": 260,
			"versionNonce": 507614269,
			"isDeleted": false,
			"id": "BaSLPnpvQABIZxnXEUIYV",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -331.2222595214844,
			"y": -303.28709411621094,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 339,
			"height": 125.06332397460938,
			"seed": 625442589,
			"groupIds": [
				"7vA9q2N1y39kbRhkIxZeQ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"type": "text",
					"id": "M1ITKymo"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 305,
			"versionNonce": 259955219,
			"isDeleted": false,
			"id": "M1ITKymo",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -326.2222595214844,
			"y": -298.28709411621094,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 329,
			"height": 102,
			"seed": 448847293,
			"groupIds": [
				"7vA9q2N1y39kbRhkIxZeQ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "private Node<E> first = null;\nprivate Node<E> last = null;\nprivate int size = 0;",
			"rawText": "private Node<E> first = null;\nprivate Node<E> last = null;\nprivate int size = 0;",
			"baseline": 89,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "BaSLPnpvQABIZxnXEUIYV",
			"originalText": "private Node<E> first = null;\nprivate Node<E> last = null;\nprivate int size = 0;"
		},
		{
			"type": "rectangle",
			"version": 151,
			"versionNonce": 551421085,
			"isDeleted": false,
			"id": "4YdQhWLc2QQdtwlGm_Xfj",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 89.61123657226562,
			"y": -301.04551696777344,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 333,
			"height": 128,
			"seed": 313575389,
			"groupIds": [
				"6khh-0toL10f-AkKo46-u"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"type": "text",
					"id": "D5TU7CJn"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 179,
			"versionNonce": 1734860723,
			"isDeleted": false,
			"id": "D5TU7CJn",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 94.61123657226562,
			"y": -296.04551696777344,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 323,
			"height": 102,
			"seed": 1186010077,
			"groupIds": [
				"6khh-0toL10f-AkKo46-u"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20.053136201190476,
			"fontFamily": 4,
			"text": "private Node<E> prev;\nprivate Node<E> next;\nprivate E item;",
			"rawText": "private Node<E> prev;\nprivate Node<E> next;\nprivate E item;",
			"baseline": 89,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "4YdQhWLc2QQdtwlGm_Xfj",
			"originalText": "private Node<E> prev;\nprivate Node<E> next;\nprivate E item;"
		},
		{
			"type": "text",
			"version": 122,
			"versionNonce": 189622525,
			"isDeleted": false,
			"id": "kUyROJ6e",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 106.78396606445312,
			"y": -337.22459411621094,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 131,
			"height": 34,
			"seed": 1719195069,
			"groupIds": [
				"6khh-0toL10f-AkKo46-u"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "Node<E> - 节点",
			"rawText": "Node<E> - 节点",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "Node<E> - 节点"
		},
		{
			"type": "line",
			"version": 135,
			"versionNonce": 693596499,
			"isDeleted": false,
			"id": "297aU1U2h5eMNSawMiKwu",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -382.3431701660156,
			"y": -131.2515411376953,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 903.7421264648438,
			"height": 0.912872314453125,
			"seed": 303645523,
			"groupIds": [],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
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
					903.7421264648438,
					0.912872314453125
				]
			]
		},
		{
			"type": "text",
			"version": 23,
			"versionNonce": 1326530909,
			"isDeleted": false,
			"id": "diFxI4sT",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -454.3527526855469,
			"y": -259.5315399169922,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 61,
			"height": 34,
			"seed": 1526004915,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
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
			"type": "text",
			"version": 1030,
			"versionNonce": 1453575923,
			"isDeleted": false,
			"id": "DyjO2EJY",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -345.8081970214844,
			"y": -95.53550720214844,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 856,
			"height": 236,
			"seed": 544977949,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "add(E e) => 添加一个元素，添加到链表的最后面\n1.定义一个临时节点L指向当前last\n2.新建一个节点newNode用于保存e，newNode的prev指向last，newNode的last=null\n3.将last指向当前新建的节点，last = newNode\n4.如果临时节点L=NULL，则表示当前链表中只有当前的新建的节点，那么将first也指向newNode；\n如果临时节点L != NULL，则表示当前链表中还有其他节点，则将节点L的next指向newNode\n5.size++，数量+1",
			"rawText": "add(E e) => 添加一个元素，添加到链表的最后面\n1.定义一个临时节点L指向当前last\n2.新建一个节点newNode用于保存e，newNode的prev指向last，newNode的last=null\n3.将last指向当前新建的节点，last = newNode\n4.如果临时节点L=NULL，则表示当前链表中只有当前的新建的节点，那么将first也指向newNode；\n如果临时节点L != NULL，则表示当前链表中还有其他节点，则将节点L的next指向newNode\n5.size++，数量+1",
			"baseline": 224,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "add(E e) => 添加一个元素，添加到链表的最后面\n1.定义一个临时节点L指向当前last\n2.新建一个节点newNode用于保存e，newNode的prev指向last，newNode的last=null\n3.将last指向当前新建的节点，last = newNode\n4.如果临时节点L=NULL，则表示当前链表中只有当前的新建的节点，那么将first也指向newNode；\n如果临时节点L != NULL，则表示当前链表中还有其他节点，则将节点L的next指向newNode\n5.size++，数量+1"
		},
		{
			"type": "rectangle",
			"version": 725,
			"versionNonce": 830148029,
			"isDeleted": false,
			"id": "f43-KSSk6wDDPu1y_AnYW",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -275.5373229980469,
			"y": 304.4920433892144,
			"strokeColor": "#000000",
			"backgroundColor": "#12b886",
			"width": 222,
			"height": 181,
			"seed": 1168841597,
			"groupIds": [
				"YsWNZi9edNdZycaxMr3Gx"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"type": "text",
					"id": "z5BlyKrf"
				},
				{
					"id": "Lwobyfp5DyjofvrePgIOi",
					"type": "arrow"
				},
				{
					"id": "1GWaSUIBjxMOZ_L4nPJuk",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 436,
			"versionNonce": 30636179,
			"isDeleted": false,
			"id": "z5BlyKrf",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -270.5373229980469,
			"y": 309.4920433892144,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 212,
			"height": 170,
			"seed": 1370796179,
			"groupIds": [
				"YsWNZi9edNdZycaxMr3Gx"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "Node1{\nprev = NULL;\nnext = null;\nitem = e;\n}",
			"rawText": "Node1{\nprev = NULL;\nnext = null;\nitem = e;\n}",
			"baseline": 157,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "f43-KSSk6wDDPu1y_AnYW",
			"originalText": "Node1{\nprev = NULL;\nnext = null;\nitem = e;\n}"
		},
		{
			"type": "arrow",
			"version": 1029,
			"versionNonce": 1370246685,
			"isDeleted": false,
			"id": "Lwobyfp5DyjofvrePgIOi",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -340.67977393784645,
			"y": 260.62474248661647,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 46.87979020032259,
			"height": 106.09489946921838,
			"seed": 1005824061,
			"groupIds": [
				"YsWNZi9edNdZycaxMr3Gx"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "wHBIOSbj",
				"gap": 5.992684448964553,
				"focus": 0.47301469546048874
			},
			"endBinding": {
				"elementId": "f43-KSSk6wDDPu1y_AnYW",
				"gap": 18.262660739476985,
				"focus": -0.7733681871091095
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					46.87979020032259,
					106.09489946921838
				]
			]
		},
		{
			"type": "text",
			"version": 264,
			"versionNonce": 681373235,
			"isDeleted": false,
			"id": "wHBIOSbj",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -357.8260192871094,
			"y": 220.63205803765192,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 40,
			"height": 34,
			"seed": 627425053,
			"groupIds": [
				"YsWNZi9edNdZycaxMr3Gx"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "Lwobyfp5DyjofvrePgIOi",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "first",
			"rawText": "first",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "first"
		},
		{
			"type": "text",
			"version": 283,
			"versionNonce": 1855357565,
			"isDeleted": false,
			"id": "wAJdLiIS",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -350.3926696777344,
			"y": 472.4991234673394,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 35,
			"height": 34,
			"seed": 1682112701,
			"groupIds": [
				"YsWNZi9edNdZycaxMr3Gx"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "1GWaSUIBjxMOZ_L4nPJuk",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "last",
			"rawText": "last",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "last"
		},
		{
			"type": "arrow",
			"version": 869,
			"versionNonce": 1306963923,
			"isDeleted": false,
			"id": "1GWaSUIBjxMOZ_L4nPJuk",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -344.4269140432299,
			"y": 462.2921745606272,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 55.27759550616673,
			"height": 63.38440989740968,
			"seed": 270765693,
			"groupIds": [
				"YsWNZi9edNdZycaxMr3Gx"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "wAJdLiIS",
				"gap": 10.206948906712228,
				"focus": -1.090817261936971
			},
			"endBinding": {
				"elementId": "f43-KSSk6wDDPu1y_AnYW",
				"gap": 13.611995539016277,
				"focus": 0.6381309089070765
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					55.27759550616673,
					-63.38440989740968
				]
			]
		},
		{
			"type": "text",
			"version": 311,
			"versionNonce": 1828399837,
			"isDeleted": false,
			"id": "s3ved3oI",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -260.9312438964844,
			"y": 246.1883019341363,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 181,
			"height": 34,
			"seed": 232881235,
			"groupIds": [
				"YsWNZi9edNdZycaxMr3Gx"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "第一次添加元素情况",
			"rawText": "第一次添加元素情况",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "第一次添加元素情况"
		},
		{
			"type": "line",
			"version": 461,
			"versionNonce": 1405566323,
			"isDeleted": false,
			"id": "HI4H3oVav3uB3l6IaHGQ0",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -392.83341471354186,
			"y": 530.1982091267905,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 1034.1395736164932,
			"height": 1.7460523922927678,
			"seed": 72355453,
			"groupIds": [],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
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
					1034.1395736164932,
					1.7460523922927678
				]
			]
		},
		{
			"type": "rectangle",
			"version": 153,
			"versionNonce": 493602621,
			"isDeleted": false,
			"id": "BxhbL5_9rk6XYgkMtbz1G",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -347.6336296929254,
			"y": 162.22958967420786,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 872,
			"height": 44,
			"seed": 1627347187,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"type": "text",
					"id": "8BQkLN7c"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 99,
			"versionNonce": 361043731,
			"isDeleted": false,
			"id": "8BQkLN7c",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -342.6336296929254,
			"y": 167.22958967420786,
			"strokeColor": "#000000",
			"backgroundColor": "#12b886",
			"width": 862,
			"height": 34,
			"seed": 1859538077,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20.01322339701681,
			"fontFamily": 4,
			"text": "添加元素的关键点在于让当前新增节点的prev指向last，last的next指向新增的节点",
			"rawText": "添加元素的关键点在于让当前新增节点的prev指向last，last的next指向新增的节点",
			"baseline": 21,
			"textAlign": "center",
			"verticalAlign": "middle",
			"containerId": "BxhbL5_9rk6XYgkMtbz1G",
			"originalText": "添加元素的关键点在于让当前新增节点的prev指向last，last的next指向新增的节点"
		},
		{
			"type": "rectangle",
			"version": 902,
			"versionNonce": 1112135581,
			"isDeleted": false,
			"id": "hmEShzG65lXVAOcRzK3eY",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 86.2664625379773,
			"y": 308.11332024468317,
			"strokeColor": "#000000",
			"backgroundColor": "#ced4da",
			"width": 206,
			"height": 181,
			"seed": 1098627571,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "vadYUbSW",
					"type": "text"
				},
				{
					"id": "E2HOjfrXUMmRMQ94fv6Ug",
					"type": "arrow"
				},
				{
					"id": "nk3SmysHPXuV5VuHNicFN",
					"type": "arrow"
				},
				{
					"id": "m6dKzSLqYBUklGvEV2IpN",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 622,
			"versionNonce": 1100397747,
			"isDeleted": false,
			"id": "vadYUbSW",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 91.2664625379773,
			"y": 313.11332024468317,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 196,
			"height": 170,
			"seed": 1890276029,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "Node1{\nprev = NULL;\nnext = Node2;\nitem = e;\n}",
			"rawText": "Node1{\nprev = NULL;\nnext = Node2;\nitem = e;\n}",
			"baseline": 157,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "hmEShzG65lXVAOcRzK3eY",
			"originalText": "Node1{\nprev = NULL;\nnext = Node2;\nitem = e;\n}"
		},
		{
			"type": "arrow",
			"version": 1639,
			"versionNonce": 1439095805,
			"isDeleted": false,
			"id": "E2HOjfrXUMmRMQ94fv6Ug",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 13.282690924022205,
			"y": 286.15492437138204,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 54.72111087447814,
			"height": 103.64471871135828,
			"seed": 1081475987,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "ttrMv66i",
				"gap": 5.992684448964496,
				"focus": 0.4730147180602551
			},
			"endBinding": {
				"elementId": "hmEShzG65lXVAOcRzK3eY",
				"gap": 18.26266073947697,
				"focus": -0.773368187109109
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					54.72111087447814,
					103.64471871135828
				]
			]
		},
		{
			"type": "text",
			"version": 461,
			"versionNonce": 1940041299,
			"isDeleted": false,
			"id": "ttrMv66i",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -5.150895860460196,
			"y": 246.16223992241754,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 40,
			"height": 34,
			"seed": 747369245,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "E2HOjfrXUMmRMQ94fv6Ug",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "first",
			"rawText": "first",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "first"
		},
		{
			"type": "rectangle",
			"version": 964,
			"versionNonce": 266622045,
			"isDeleted": false,
			"id": "841Tqi-BOyqDEJhEgtV60",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 402.50810072157105,
			"y": 302.41361321343317,
			"strokeColor": "#000000",
			"backgroundColor": "#12b886",
			"width": 206,
			"height": 181,
			"seed": 1411542685,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "4UvWRuUm",
					"type": "text"
				},
				{
					"id": "E2HOjfrXUMmRMQ94fv6Ug",
					"type": "arrow"
				},
				{
					"id": "m6dKzSLqYBUklGvEV2IpN",
					"type": "arrow"
				},
				{
					"id": "e5bv6eCfV-6lLkBAto_1Y",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 681,
			"versionNonce": 243231731,
			"isDeleted": false,
			"id": "4UvWRuUm",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 407.50810072157105,
			"y": 307.41361321343317,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 196,
			"height": 170,
			"seed": 1215763891,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "Node2{\nprev = Node1;\nnext = null;\nitem = e2;\n}",
			"rawText": "Node2{\nprev = Node1;\nnext = null;\nitem = e2;\n}",
			"baseline": 157,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "841Tqi-BOyqDEJhEgtV60",
			"originalText": "Node2{\nprev = Node1;\nnext = null;\nitem = e2;\n}"
		},
		{
			"type": "arrow",
			"version": 424,
			"versionNonce": 745783485,
			"isDeleted": false,
			"id": "nk3SmysHPXuV5VuHNicFN",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 410.84013197157105,
			"y": 358.3212670220269,
			"strokeColor": "#000000",
			"backgroundColor": "#12b886",
			"width": 110.45739746093733,
			"height": 0.9085397294401787,
			"seed": 1257894067,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": null,
			"endBinding": {
				"elementId": "hmEShzG65lXVAOcRzK3eY",
				"gap": 8.11627197265642,
				"focus": -0.42113555787133927
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					-110.45739746093733,
					0.9085397294401787
				]
			]
		},
		{
			"type": "arrow",
			"version": 716,
			"versionNonce": 1446213011,
			"isDeleted": false,
			"id": "m6dKzSLqYBUklGvEV2IpN",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 312.25016615125855,
			"y": 399.4004906548394,
			"strokeColor": "#000000",
			"backgroundColor": "#12b886",
			"width": 74.8553466796875,
			"height": 2.73858642578125,
			"seed": 1616068445,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "hmEShzG65lXVAOcRzK3eY",
				"gap": 19.98370361328125,
				"focus": -0.039379047108357254
			},
			"endBinding": {
				"elementId": "841Tqi-BOyqDEJhEgtV60",
				"gap": 15.402587890625,
				"focus": -0.1438154072344578
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					74.8553466796875,
					2.73858642578125
				]
			]
		},
		{
			"type": "text",
			"version": 476,
			"versionNonce": 1897308445,
			"isDeleted": false,
			"id": "OJyC84bf",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 242.36063978407105,
			"y": 238.82254876030817,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 181,
			"height": 34,
			"seed": 884981939,
			"groupIds": [
				"kepK9SNNcz0XwNOdiSOLR",
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "第二次添加元素情况",
			"rawText": "第二次添加元素情况",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "第二次添加元素情况"
		},
		{
			"type": "text",
			"version": 141,
			"versionNonce": 1240812339,
			"isDeleted": false,
			"id": "UQFyC2BO",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 728.0864766438802,
			"y": 249.00094858805335,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 35,
			"height": 34,
			"seed": 263473405,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "e5bv6eCfV-6lLkBAto_1Y",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "last",
			"rawText": "last",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "last"
		},
		{
			"type": "arrow",
			"version": 361,
			"versionNonce": 1933473149,
			"isDeleted": false,
			"id": "e5bv6eCfV-6lLkBAto_1Y",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 722.956724995707,
			"y": 289.24588992912925,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 99.29570408099369,
			"height": 90.75412278652823,
			"seed": 997225395,
			"groupIds": [
				"ZqLeuDxU-tkjOMN-JLHg-"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "UQFyC2BO",
				"gap": 8.081685735378187,
				"focus": -0.07764412564696305
			},
			"endBinding": {
				"elementId": "841Tqi-BOyqDEJhEgtV60",
				"gap": 15.152920193142222,
				"focus": 0.5149248716681268
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					-99.29570408099369,
					90.75412278652823
				]
			]
		},
		{
			"type": "text",
			"version": 1284,
			"versionNonce": 1043033299,
			"isDeleted": false,
			"id": "i2f1uKfY",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -351.3272230360244,
			"y": 564.3072812503929,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 854,
			"height": 304,
			"seed": 1643314803,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "remove() => 删除一个元素，删除的是第一个节点\n1.定义一个临时节点f指向当前first\n2.定义一个临时节点next指向临时节点的next节点，即原来链表中当前要被删除节点的下一个节点\n3.将节点f的item置为null，next置为null，为了让GC回收\n4.将first指向临时节点next，即fisrt指向下一个节点\n5.如果临时节点next=null，则表示当前链表中只有当前要被删除的节点，所以将last置为null；\n如果临时节点next != null，则将临时节点next的prev置为null\n其中的临时节点next可以替换为下一个节点\n6.size--，数量-1",
			"rawText": "remove() => 删除一个元素，删除的是第一个节点\n1.定义一个临时节点f指向当前first\n2.定义一个临时节点next指向临时节点的next节点，即原来链表中当前要被删除节点的下一个节点\n3.将节点f的item置为null，next置为null，为了让GC回收\n4.将first指向临时节点next，即fisrt指向下一个节点\n5.如果临时节点next=null，则表示当前链表中只有当前要被删除的节点，所以将last置为null；\n如果临时节点next != null，则将临时节点next的prev置为null\n其中的临时节点next可以替换为下一个节点\n6.size--，数量-1",
			"baseline": 291,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "remove() => 删除一个元素，删除的是第一个节点\n1.定义一个临时节点f指向当前first\n2.定义一个临时节点next指向临时节点的next节点，即原来链表中当前要被删除节点的下一个节点\n3.将节点f的item置为null，next置为null，为了让GC回收\n4.将first指向临时节点next，即fisrt指向下一个节点\n5.如果临时节点next=null，则表示当前链表中只有当前要被删除的节点，所以将last置为null；\n如果临时节点next != null，则将临时节点next的prev置为null\n其中的临时节点next可以替换为下一个节点\n6.size--，数量-1"
		},
		{
			"type": "text",
			"version": 66,
			"versionNonce": 506248669,
			"isDeleted": false,
			"id": "y50cLjcu",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 232.53295220269092,
			"y": 1262.3643457306446,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 5,
			"height": 34,
			"seed": 2098418205,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "",
			"rawText": "",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": ""
		},
		{
			"type": "rectangle",
			"version": 1292,
			"versionNonce": 913070707,
			"isDeleted": false,
			"id": "6f8aNjs4-XfbA73MPTeoo",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -395.30112711588544,
			"y": 1011.9469161245247,
			"strokeColor": "#000000",
			"backgroundColor": "#ced4da",
			"width": 206,
			"height": 181,
			"seed": 1389344275,
			"groupIds": [
				"3zj8LzfyUa_JzFyyR0GfN",
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "CPIRM2sT",
					"type": "text"
				},
				{
					"id": "E2HOjfrXUMmRMQ94fv6Ug",
					"type": "arrow"
				},
				{
					"id": "nk3SmysHPXuV5VuHNicFN",
					"type": "arrow"
				},
				{
					"id": "m6dKzSLqYBUklGvEV2IpN",
					"type": "arrow"
				},
				{
					"id": "xN9rjCLMBfEyCXJlDZImt",
					"type": "arrow"
				},
				{
					"id": "Wiammzt8V8qCzYWIDVBEt",
					"type": "arrow"
				},
				{
					"id": "aLWMwzqVXclyL3BLRGTbj",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 1009,
			"versionNonce": 2029189693,
			"isDeleted": false,
			"id": "CPIRM2sT",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -390.30112711588544,
			"y": 1016.9469161245247,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 196,
			"height": 170,
			"seed": 1270655133,
			"groupIds": [
				"3zj8LzfyUa_JzFyyR0GfN",
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "Node1{\nprev = NULL;\nnext = Node2;\nitem = e1;\n}",
			"rawText": "Node1{\nprev = NULL;\nnext = Node2;\nitem = e1;\n}",
			"baseline": 157,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "6f8aNjs4-XfbA73MPTeoo",
			"originalText": "Node1{\nprev = NULL;\nnext = Node2;\nitem = e1;\n}"
		},
		{
			"type": "rectangle",
			"version": 1360,
			"versionNonce": 2139001875,
			"isDeleted": false,
			"id": "FBb_2Efw7U6aWlHVpcOBF",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -66.06230333116326,
			"y": 1023.145917860636,
			"strokeColor": "#000000",
			"backgroundColor": "#ced4da",
			"width": 206,
			"height": 181,
			"seed": 534648509,
			"groupIds": [
				"5dA87YaWxOfUJvZEGQAzI",
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "LSOK6bff",
					"type": "text"
				},
				{
					"id": "E2HOjfrXUMmRMQ94fv6Ug",
					"type": "arrow"
				},
				{
					"id": "nk3SmysHPXuV5VuHNicFN",
					"type": "arrow"
				},
				{
					"id": "m6dKzSLqYBUklGvEV2IpN",
					"type": "arrow"
				},
				{
					"id": "xN9rjCLMBfEyCXJlDZImt",
					"type": "arrow"
				},
				{
					"id": "Wiammzt8V8qCzYWIDVBEt",
					"type": "arrow"
				},
				{
					"id": "ugksrw-2O32IMBITE9vxC",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 1097,
			"versionNonce": 745712285,
			"isDeleted": false,
			"id": "LSOK6bff",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -61.062303331163264,
			"y": 1028.145917860636,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 196,
			"height": 170,
			"seed": 1707179923,
			"groupIds": [
				"5dA87YaWxOfUJvZEGQAzI",
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "Node2{\nprev = Node1;\nnext = NULL;\nitem = e2;\n}",
			"rawText": "Node2{\nprev = Node1;\nnext = NULL;\nitem = e2;\n}",
			"baseline": 157,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "FBb_2Efw7U6aWlHVpcOBF",
			"originalText": "Node2{\nprev = Node1;\nnext = NULL;\nitem = e2;\n}"
		},
		{
			"type": "arrow",
			"version": 1189,
			"versionNonce": 156801459,
			"isDeleted": false,
			"id": "xN9rjCLMBfEyCXJlDZImt",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -175.62171088324664,
			"y": 1107.6347009552537,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 90.27282714843756,
			"height": 3.042873806423586,
			"seed": 489532605,
			"groupIds": [
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "6f8aNjs4-XfbA73MPTeoo",
				"gap": 13.6794162326388,
				"focus": 0.013353039845829771
			},
			"endBinding": {
				"elementId": "FBb_2Efw7U6aWlHVpcOBF",
				"gap": 19.286580403645814,
				"focus": -0.012276387723134642
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					90.27282714843756,
					3.042873806423586
				]
			]
		},
		{
			"type": "arrow",
			"version": 1190,
			"versionNonce": 1230851837,
			"isDeleted": false,
			"id": "Wiammzt8V8qCzYWIDVBEt",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -88.39182535807306,
			"y": 1067.062665907511,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 91.28709581163183,
			"height": 1.0142686631943434,
			"seed": 1100379613,
			"groupIds": [
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "FBb_2Efw7U6aWlHVpcOBF",
				"gap": 22.3295220269098,
				"focus": 0.5234990199656506
			},
			"endBinding": {
				"elementId": "6f8aNjs4-XfbA73MPTeoo",
				"gap": 9.622205946180543,
				"focus": -0.3613822648658439
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					-91.28709581163183,
					1.0142686631943434
				]
			]
		},
		{
			"type": "text",
			"version": 361,
			"versionNonce": 18934611,
			"isDeleted": false,
			"id": "oYYVKcCX",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -517.663065592448,
			"y": 963.2889435767818,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 40,
			"height": 34,
			"seed": 1416797203,
			"groupIds": [
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "aLWMwzqVXclyL3BLRGTbj",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "first",
			"rawText": "first",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "first"
		},
		{
			"type": "arrow",
			"version": 789,
			"versionNonce": 1312057181,
			"isDeleted": false,
			"id": "aLWMwzqVXclyL3BLRGTbj",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -468.23798664315444,
			"y": 998.2701128544154,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 57.6875207414617,
			"height": 81.49504566105998,
			"seed": 497662035,
			"groupIds": [
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "oYYVKcCX",
				"gap": 9.47601215447662,
				"focus": -0.5212268951380322
			},
			"endBinding": {
				"elementId": "6f8aNjs4-XfbA73MPTeoo",
				"gap": 15.249338785807367,
				"focus": -0.6117119423941182
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					57.6875207414617,
					81.49504566105998
				]
			]
		},
		{
			"type": "text",
			"version": 256,
			"versionNonce": 724437235,
			"isDeleted": false,
			"id": "3JEKWeyY",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 225.1488749186197,
			"y": 965.5010899119596,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 35,
			"height": 34,
			"seed": 793288669,
			"groupIds": [
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "ugksrw-2O32IMBITE9vxC",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "last",
			"rawText": "last",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "last"
		},
		{
			"type": "arrow",
			"version": 753,
			"versionNonce": 1833530301,
			"isDeleted": false,
			"id": "ugksrw-2O32IMBITE9vxC",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 220.6677034576059,
			"y": 1003.1168381417199,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 68.88967814836124,
			"height": 84.86420172141175,
			"seed": 1130399133,
			"groupIds": [
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "3JEKWeyY",
				"gap": 5.757997301494555,
				"focus": 0.1676056975007748
			},
			"endBinding": {
				"elementId": "FBb_2Efw7U6aWlHVpcOBF",
				"gap": 11.840328640407932,
				"focus": 0.5327216031649391
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					-68.88967814836124,
					84.86420172141175
				]
			]
		},
		{
			"type": "text",
			"version": 305,
			"versionNonce": 2097574547,
			"isDeleted": false,
			"id": "vK8cZG6K",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -231.9462178548178,
			"y": 952.8080051951628,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 201,
			"height": 34,
			"seed": 2038773555,
			"groupIds": [
				"0r1E9cfgj7WGUkO3S-7TJ"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "未删除头节点前的情况",
			"rawText": "未删除头节点前的情况",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "未删除头节点前的情况"
		},
		{
			"type": "arrow",
			"version": 123,
			"versionNonce": 314438685,
			"isDeleted": false,
			"id": "8iEDdM5FNJauTR3RFwqc9",
			"fillStyle": "solid",
			"strokeWidth": 4,
			"strokeStyle": "solid",
			"roughness": 0,
			"opacity": 100,
			"angle": 0,
			"x": 256.75812784830714,
			"y": 1100.8424900584441,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 136.9306640625,
			"height": 1.82574462890625,
			"seed": 1266977587,
			"groupIds": [],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": null,
			"endBinding": null,
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "triangle",
			"points": [
				[
					0,
					0
				],
				[
					136.9306640625,
					-1.82574462890625
				]
			]
		},
		{
			"type": "text",
			"version": 414,
			"versionNonce": 2002823219,
			"isDeleted": false,
			"id": "PgS0it5s",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 0,
			"opacity": 100,
			"angle": 0,
			"x": 434.2928568522134,
			"y": 993.5230930857879,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 40,
			"height": 34,
			"seed": 363330109,
			"groupIds": [
				"hTCYwgHkY-JUCmK3H1uFX",
				"460cnGMNPaq3r9AvrKDLE"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "aar3COUaVIq1HAMdfOGLn",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "first",
			"rawText": "first",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "first"
		},
		{
			"type": "rectangle",
			"version": 1423,
			"versionNonce": 713255037,
			"isDeleted": false,
			"id": "G7JUzAwdYtsRDJpR28k6G",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 574.0691019694009,
			"y": 1034.1785496287566,
			"strokeColor": "#000000",
			"backgroundColor": "#ced4da",
			"width": 206,
			"height": 181,
			"seed": 89760403,
			"groupIds": [
				"jCI9VbmZq7SLcWcWqgK3D",
				"PSZ3znhCIf86am8fa75kS",
				"ExES-F3NXUeLtl83yiFja",
				"hTCYwgHkY-JUCmK3H1uFX",
				"460cnGMNPaq3r9AvrKDLE"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "CTv9fr6c",
					"type": "text"
				},
				{
					"id": "E2HOjfrXUMmRMQ94fv6Ug",
					"type": "arrow"
				},
				{
					"id": "nk3SmysHPXuV5VuHNicFN",
					"type": "arrow"
				},
				{
					"id": "m6dKzSLqYBUklGvEV2IpN",
					"type": "arrow"
				},
				{
					"id": "xN9rjCLMBfEyCXJlDZImt",
					"type": "arrow"
				},
				{
					"id": "Wiammzt8V8qCzYWIDVBEt",
					"type": "arrow"
				},
				{
					"id": "ugksrw-2O32IMBITE9vxC",
					"type": "arrow"
				},
				{
					"id": "aar3COUaVIq1HAMdfOGLn",
					"type": "arrow"
				},
				{
					"id": "xHC575ddlq1clEGpxQ9Om",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 1166,
			"versionNonce": 1076046291,
			"isDeleted": false,
			"id": "CTv9fr6c",
			"fillStyle": "hachure",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 579.0691019694009,
			"y": 1039.1785496287566,
			"strokeColor": "#000000",
			"backgroundColor": "transparent",
			"width": 196,
			"height": 170,
			"seed": 1349895197,
			"groupIds": [
				"jCI9VbmZq7SLcWcWqgK3D",
				"PSZ3znhCIf86am8fa75kS",
				"ExES-F3NXUeLtl83yiFja",
				"hTCYwgHkY-JUCmK3H1uFX",
				"460cnGMNPaq3r9AvrKDLE"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "Node2{\nprev = NULL;\nnext = NULL;\nitem = e2;\n}",
			"rawText": "Node2{\nprev = NULL;\nnext = NULL;\nitem = e2;\n}",
			"baseline": 157,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": "G7JUzAwdYtsRDJpR28k6G",
			"originalText": "Node2{\nprev = NULL;\nnext = NULL;\nitem = e2;\n}"
		},
		{
			"type": "arrow",
			"version": 800,
			"versionNonce": 643518685,
			"isDeleted": false,
			"id": "aar3COUaVIq1HAMdfOGLn",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 0,
			"opacity": 100,
			"angle": 0,
			"x": 480.4113871256509,
			"y": 1012.2940281443816,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 74.85540771484375,
			"height": 71.2039794921875,
			"seed": 105178739,
			"groupIds": [
				"ExES-F3NXUeLtl83yiFja",
				"hTCYwgHkY-JUCmK3H1uFX",
				"460cnGMNPaq3r9AvrKDLE"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "PgS0it5s",
				"gap": 6.1185302734375,
				"focus": -0.6404974575261793
			},
			"endBinding": {
				"elementId": "G7JUzAwdYtsRDJpR28k6G",
				"gap": 18.80230712890625,
				"focus": -0.3962329601722921
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					74.85540771484375,
					71.2039794921875
				]
			]
		},
		{
			"type": "text",
			"version": 267,
			"versionNonce": 2086958963,
			"isDeleted": false,
			"id": "cNI8YQ6b",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 0,
			"opacity": 100,
			"angle": 0,
			"x": 434.2889506022134,
			"y": 1174.3072117381316,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 35,
			"height": 34,
			"seed": 919370717,
			"groupIds": [
				"ExES-F3NXUeLtl83yiFja",
				"hTCYwgHkY-JUCmK3H1uFX",
				"460cnGMNPaq3r9AvrKDLE"
			],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "xHC575ddlq1clEGpxQ9Om",
					"type": "arrow"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "last",
			"rawText": "last",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "last"
		},
		{
			"type": "arrow",
			"version": 869,
			"versionNonce": 1138175293,
			"isDeleted": false,
			"id": "xHC575ddlq1clEGpxQ9Om",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 0,
			"opacity": 100,
			"angle": 0,
			"x": 480.4113871256509,
			"y": 1187.1494968943816,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 74.85540771484375,
			"height": 63.9010009765625,
			"seed": 1280597565,
			"groupIds": [
				"ExES-F3NXUeLtl83yiFja",
				"hTCYwgHkY-JUCmK3H1uFX",
				"460cnGMNPaq3r9AvrKDLE"
			],
			"strokeSharpness": "round",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"startBinding": {
				"elementId": "cNI8YQ6b",
				"gap": 11.1224365234375,
				"focus": 0.6348372537847962
			},
			"endBinding": {
				"elementId": "G7JUzAwdYtsRDJpR28k6G",
				"gap": 18.80230712890625,
				"focus": 0.5907613140546192
			},
			"lastCommittedPoint": null,
			"startArrowhead": null,
			"endArrowhead": "arrow",
			"points": [
				[
					0,
					0
				],
				[
					74.85540771484375,
					-63.9010009765625
				]
			]
		},
		{
			"type": "text",
			"version": 374,
			"versionNonce": 1785401619,
			"isDeleted": false,
			"id": "D1BA6ELf",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": 587.4723612467446,
			"y": 960.0882175975065,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 181,
			"height": 34,
			"seed": 2136865587,
			"groupIds": [
				"7F_A0iS0tUAiMeQk1BvHw",
				"460cnGMNPaq3r9AvrKDLE"
			],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20,
			"fontFamily": 4,
			"text": "删除头节点后的情况",
			"rawText": "删除头节点后的情况",
			"baseline": 21,
			"textAlign": "left",
			"verticalAlign": "top",
			"containerId": null,
			"originalText": "删除头节点后的情况"
		},
		{
			"type": "rectangle",
			"version": 395,
			"versionNonce": 307789213,
			"isDeleted": false,
			"id": "H3KJdSBZxiKIQrWvIRXZP",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -349.83147176106786,
			"y": 888.308066230319,
			"strokeColor": "#000000",
			"backgroundColor": "#15aabf",
			"width": 872,
			"height": 44,
			"seed": 1936224445,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [
				{
					"id": "lIvKLEeD",
					"type": "text"
				}
			],
			"updated": 1652184361603,
			"link": null,
			"locked": false
		},
		{
			"type": "text",
			"version": 532,
			"versionNonce": 1001223859,
			"isDeleted": false,
			"id": "lIvKLEeD",
			"fillStyle": "solid",
			"strokeWidth": 1,
			"strokeStyle": "solid",
			"roughness": 1,
			"opacity": 100,
			"angle": 0,
			"x": -344.83147176106786,
			"y": 893.308066230319,
			"strokeColor": "#000000",
			"backgroundColor": "#12b886",
			"width": 862,
			"height": 34,
			"seed": 706856339,
			"groupIds": [],
			"strokeSharpness": "sharp",
			"boundElements": [],
			"updated": 1652184361603,
			"link": null,
			"locked": false,
			"fontSize": 20.01322339701681,
			"fontFamily": 4,
			"text": "删除元素的关键点在于让当前要被删除节点的下一个节点的prev置为null",
			"rawText": "删除元素的关键点在于让当前要被删除节点的下一个节点的prev置为null",
			"baseline": 21,
			"textAlign": "center",
			"verticalAlign": "middle",
			"containerId": "H3KJdSBZxiKIQrWvIRXZP",
			"originalText": "删除元素的关键点在于让当前要被删除节点的下一个节点的prev置为null"
		}
	],
	"appState": {
		"theme": "light",
		"viewBackgroundColor": "#ffffff",
		"currentItemStrokeColor": "#000000",
		"currentItemBackgroundColor": "#15aabf",
		"currentItemFillStyle": "solid",
		"currentItemStrokeWidth": 4,
		"currentItemStrokeStyle": "solid",
		"currentItemRoughness": 0,
		"currentItemOpacity": 100,
		"currentItemFontFamily": 4,
		"currentItemFontSize": 28,
		"currentItemTextAlign": "left",
		"currentItemStrokeSharpness": "sharp",
		"currentItemStartArrowhead": null,
		"currentItemEndArrowhead": "triangle",
		"currentItemLinearStrokeSharpness": "round",
		"gridSize": null,
		"colorPalette": {}
	},
	"files": {}
}
```
%%