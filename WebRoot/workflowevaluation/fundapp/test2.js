var text ;//= {"name":"sequence","trust":1,"childNodes":[{"name":"invoke","trust":0.9984,"childNodes":[],"attributesList":[{"attributeName":"joinCondition","attributeValue":""},{"attributeName":"name","attributeValue":"userVerification"},{"attributeName":"ID","attributeValue":""},{"attributeName":"inputVariable","attributeValue":"userVerificationIn"},{"attributeName":"operation","attributeValue":"verify"},{"attributeName":"outputVariable","attributeValue":"userVerificationOut"},{"attributeName":"partnerLink","attributeValue":"userVerification"},{"attributeName":"portType","attributeValue":"ns0:UserVerification"},{"attributeName":"servicename","attributeValue":"UserVerification"}]},{"name":"switch","trust":1,"childNodes":[{"name":"case","trust":1,"childNodes":[{"name":"invoke","trust":0.737792,"childNodes":[],"attributesList":[{"attributeName":"name","attributeValue":"balanceVerification"},{"attributeName":"ID","attributeValue":""},{"attributeName":"inputVariable","attributeValue":"balanceVerificationIn"},{"attributeName":"operation","attributeValue":"verify"},{"attributeName":"outputVariable","attributeValue":"balanceVerificationOut"},{"attributeName":"partnerLink","attributeValue":"balanceVerification"},{"attributeName":"portType","attributeValue":"BalanceVerification"},{"attributeName":"servicename","attributeValue":"BalanceVerification"}]},{"name":"switch","trust":1,"childNodes":[{"name":"case","trust":1,"childNodes":[{"name":"invoke","trust":0.737792,"childNodes":[],"attributesList":[{"attributeName":"name","attributeValue":"Deduction"},{"attributeName":"ID","attributeValue":""},{"attributeName":"inputVariable","attributeValue":"deductionIn"},{"attributeName":"operation","attributeValue":"deduct"},{"attributeName":"outputVariable","attributeValue":"deductionOut"},{"attributeName":"partnerLink","attributeValue":"deduction"},{"attributeName":"portType","attributeValue":"Deduction"},{"attributeName":"servicename","attributeValue":"Deduction"}]},{"name":"switch","trust":1,"childNodes":[{"name":"case","trust":1,"childNodes":[],"attributesList":[{"attributeName":"ID","attributeValue":""},{"attributeName":"condition","attributeValue":"'bpws:getVariableData('deductionOut','deductReturn')'='true'"}]},{"name":"otherwise","trust":1,"childNodes":[],"attributesList":[{"attributeName":"ID","attributeValue":""}]}],"attributesList":[{"attributeName":"ID","attributeValue":""}]}],"attributesList":[{"attributeName":"ID","attributeValue":""},{"attributeName":"condition","attributeValue":"'bpws:getVariableData('balanceVerificationOut','verifyReturn')'='true'"}]},{"name":"otherwise","trust":1,"childNodes":[],"attributesList":[{"attributeName":"ID","attributeValue":""}]}],"attributesList":[{"attributeName":"ID","attributeValue":""}]}],"attributesList":[{"attributeName":"joinCondition","attributeValue":""},{"attributeName":"ID","attributeValue":""},{"attributeName":"condition","attributeValue":"'bpws:getVariableData('userVerificationOut','verifyReturn')'='true'"}]},{"name":"otherwise","trust":1,"childNodes":[],"attributesList":[{"attributeName":"ID","attributeValue":""}]}],"attributesList":[{"attributeName":"ID","attributeValue":""}]},{"name":"reply","trust":1,"childNodes":[],"attributesList":[{"attributeName":"name","attributeValue":"userReply"},{"attributeName":"ID","attributeValue":""},{"attributeName":"operation","attributeValue":"manageAccount"},{"attributeName":"partnerLink","attributeValue":"user"},{"attributeName":"portType","attributeValue":"bank:manageAccountPT"},{"attributeName":"variable","attributeValue":"userOut"}]}],"attributesList":[]};
var servicetrust;// = [{"UserVerification":true},{"BalanceVerification":true},{"Deduction":false}];	
var endp = [];
	var lines = [];
	var	linestext = [];
	var canvaslength = 1200, canvaswidth = 800;
	var widthofrect = 120, heightofrect = 60, gap = 30, heightofdiamond = 30, widthofdiamond = 20, rectcorner = 5, arrowwidth= 5, crossradius = 10, textheighttomiddle = 20, valuetotop = 40;
	var numbercolor = Raphael.getColor();
	var rectcolor = "darkgreen";//Raphael.getColor();
	var ellipsecolor = "dimgray";//Raphael.getColor();
	var diamcolor = "coral";//Raphael.getColor();
	var fontcolor = "crimson";//Raphael.getColor();
	var linecolor = "cornflowerblue";// Raphael.getColor();
	var rectfontwidth = 6;
	var ellipsefontwidth = 12;
	var r;
	var istrust = false;
	var positionx,positiony;
	/*window.onload = function() {
       	  widthofrect = canvaslength/depth(text)/1.5;
    	  heightofrect = canvaswidth/height(text)/2;
    	  gap = heightofrect /2;
    	  heightofdiamond = heightofrect/2;
    	  rectcorner = widthofrect / 24;
    	  textheighttomiddle = heightofrect /3;
    	  valuetotop = heightofrect - textheighttomiddle; 
    	  r = Raphael("holder", canvaslength, canvaswidth), connections = [], shapes = [];
  		  process(text);
  		  draw(text, 100, 400);
  		  linklines();     
};*/
	Raphael.fn.arrowright = function (cx, cy, r) {
	    return this.path("M"+(cx-r)+" "+(cy-r)+"l"+r+" "+r+" "+(-r)+" "+r);
	};
	Raphael.fn.arrowdown = function (cx,cy,r) {
	    return this.path("M"+(cx-r)+" "+(cy-r)+"l"+r+" "+r+" "+r+" "+(-r));
	};
	Raphael.fn.diamond = function(cx, cy, rx, ry) {
		return this.path([ "M", cx, cy - ry, "l", rx, ry, -rx, ry, -rx, -ry,
				rx, -ry, "z" ]);
	};
	Raphael.fn.cross = function (cx, cy, r) {
	    r = r / 2.5;
	    return this.path("M".concat(cx - r, ",", cy, "l", [-r, -r, r, -r, r, r, r, -r, r, r, -r, r, r, r, -r, r, -r, -r, -r, r, -r, -r, "z"])).attr({fill:"red",stroke:"yellow"});
	};
	Raphael.fn.tick = function (cx, cy, r) {
	    r = r / 2.5;
	    return this.path("M".concat(cx-2*r , ",", cy-r, "l", [r,-r,r,r,3*r,-3*r,r,r,-4*r,4*r,-2*r,-2*r, "z"])).attr({fill:"cyan",stroke:"green"});
	};

	function gettrust(servname,servlist) {
		for (var i = 0, ii = servlist.length; i < ii;i++)
			for (var key in servlist[i])
			if (key == servname)
				return servlist[i][key];
	}
	// link line A to line B, when A's end is B's beginning
	function linklines(){
		/*var num = lines.length;
		for (var i =0 ; i< num; i++)
			for(var j= i+1; j< num; j++){
				if (lines[i].tox == lines[j].fromx && lines[i].toy == lines[j].fromy && (lines[i].fromx != lines[j].tox || lines[i].fromy != lines[j].toy)){
					var linetext = lines[i].text;
					lines.splice(i,1,{"fromx":lines[i].fromx,"fromy":lines[i].fromy,"tox":lines[j].tox,"toy":lines[j].toy});
					lines[i].text = linetext;
					lines.splice(j, 1);
					i --;
					num --;
					break;
				}
			}*/
		for (var i = 0, ii = lines.length;i < ii; i++){
			if ('direction' in lines[i])
				linestext.push(r.text((lines[i].fromx - gap),(lines[i].fromy+lines[i].toy)/2,('text' in lines[i]) ? lines[i].text : "").attr({"font-size":13, fill :"blue"}));
			else
				linestext.push(r.text((lines[i].fromx+lines[i].tox)/2,(lines[i].fromy+lines[i].toy)/2,('text' in lines[i]) ? lines[i].text : "").attr({"font-size":13, fill :"blue"}));
		}
		for (var i = 0, ii = lines.length;i < ii; i++){
			var p; 
			if ('direction' in lines[i])
				p = r.path("M"+lines[i].fromx+" "+lines[i].fromy+"C"+(lines[i].fromx - gap*2)+" "+(lines[i].fromy)+" "+(lines[i].fromx - gap*2)+" "+(lines[i].toy)+" "+lines[i].tox+" "+lines[i].toy).attr({"stroke-width" : 1.5,"stroke-linecap":"round", cursor : "pointer", stroke: linecolor});
			else
				p =(Math.abs(lines[i].tox - lines[i].fromx) < Math.abs(lines[i].toy- lines[i].fromy)) ? 
						r.path("M"+lines[i].fromx+" "+lines[i].fromy+"C"+(lines[i].tox)+" "+(lines[i].fromy)+" "+(lines[i].fromx)+" "+(lines[i].toy)+" "+lines[i].tox+" "+lines[i].toy).attr({"stroke-width" : 1.5,"stroke-linecap":"round", cursor : "pointer", stroke: linecolor})
						: r.path("M"+lines[i].fromx+" "+lines[i].fromy+"C"+(lines[i].fromx + Math.abs(lines[i].toy - lines[i].fromy))+" "+(lines[i].fromy)+" "+(lines[i].tox - Math.abs(lines[i].toy - lines[i].fromy))+" "+(lines[i].toy)+" "+lines[i].tox+" "+lines[i].toy).attr({"stroke-width" : 1.5,"stroke-linecap":"round", cursor : "pointer", stroke: linecolor});
			if (lines[i].fromx == lines[i].tox)
				r.arrowdown(lines[i].tox,lines[i].toy,arrowwidth);
			else
				r.arrowright(lines[i].tox,lines[i].toy,arrowwidth);	
		(function (i){
			if ('text'in lines[i]){
				linestext[i].hide();
				p.mouseover( function (event) {
					linestext[i].show();
				});
			 	p.mouseout (function (event) {
			 		linestext[i].hide();
			 	});
		 	}
			})(i);
		}
	}
	///depth of the graph without case && otherwise
	var depth = function(raph) {
		if (raph.childNodes.length == 0)
			return 0;
		var max = 0;
		for ( var i = 0, ii = raph.childNodes.length; i < ii; i++) {
			//document.write(raph.childNodes[i].name+i+" ");
			var temp = depth(raph.childNodes[i]);
			if (temp > max)
				max = temp;
		}
		if (raph.name == "switch")
			return (max);//max ought to be =1 , if with case && otherwise
		return (max + raph.childNodes.length);
	};
	///height of the graph
	var height = function(raph) {
		var dep = 1;
		if (raph.childNodes.length == 0)
			return 0;
		for ( var i = 0, ii = raph.childNodes.length; i < ii; i++) {
			//document.write(raph.childNodes[i].name+i+" ");
			dep += height(raph.childNodes[i]);
			if (raph.name == "switch"
					&& raph.childNodes[i].childNodes.length == 0)
				dep += 1;
			if (raph.childNodes[i].name == "while")
				dep -= 1;

		}
		if (raph.name == "while")
			dep += 1;
		if (raph.name == "switch")
			dep -= 1;
		return dep;
	};
	
	var t;
	var star = {
			"sx" : 0,
			"sy" : 0
		};
	//return the value of attibute
	function printattr(raph,fulltext) {
		var i;
		var ret="";
		if (raph.name == "invoke"){
			for ( i= 0, ii = raph.attributesList.length; i < ii; i++)
				if (raph.attributesList[i].attributeName == "servicename")
					break;
			if (i != raph.attributesList.length){
				ret = raph.attributesList[i].attributeValue;
				if((ret.length > widthofrect/rectfontwidth) && !fulltext){
					ret = "\n"+ret[0]+ret[1]+ret[2]+"...";
				}
			}
		}
		else if (raph.name == "case" ){
			for ( i = 0, ii = raph.attributesList.length; i < ii; i++)
				if (raph.attributesList[i].attributeName == "condition")
					break;
			if (i != raph.attributesList.length){
				ret = raph.attributesList[i].attributeValue;
				if((ret.length > widthofrect/rectfontwidth)  && !fulltext){
					ret = "\n"+ret[0]+ret[1]+ret[2]+"...";
				}
			}
			else
				ret = "\n"+ret;
		}
		else if (raph.name == "while" ){
			for ( i = 0, ii = raph.attributesList.length; i < ii; i++)
				if (raph.attributesList[i].attributeName == "condition")
					break;
			if (i != raph.attributesList.length){
				ret = raph.attributesList[i].attributeValue;
				if((ret.length > widthofrect/rectfontwidth)  && !fulltext){
					ret = raph.name;
				}
			}
			else
				ret = "\n"+ret;
		}
		else if (raph.name =="reply"){
			for ( i = 0, ii = raph.attributesList.length; i < ii; i++)
				if (raph.attributesList[i].attributeName == "name")
					break;
			if (i != raph.attributesList.length){
				ret = raph.attributesList[i].attributeValue;
				if((ret.length > widthofrect/ellipsefontwidth)  && !fulltext){
					ret = "\n"+ret[0]+ret[1]+ret[2]+"...";
				}
				else
					ret = "\n"+ret;
			}
			return "Reply"+ret;
		}
		else 
			return raph.name;
		return ret;
	}
	var shapes = [];
	var texts = [];
	var textsfull = [];
	//draw the graph
	function draw(raph, x, y) {
		t = 0;
		var infotext = "";
		var infotextfull = "";
		var shape;
		for ( var i = 0, ii = raph.childNodes.length; i < ii; i++) {
		if (raph.childNodes[i].name != "invoke"){
				infotext = r.text(x + widthofrect / 2, y + heightofrect / 2,
						printattr(raph.childNodes[i],false)).attr({
					"font-size" : 11,
					fill : fontcolor
				});
				infotextfull = r.text(x + widthofrect / 2, y + heightofrect / 2,
						printattr(raph.childNodes[i],true)).attr({
							"font-size" : 14,
							fill : fontcolor
						});
				infotextfull.hide();
				
		}
		else{
			infotext = r.text(x + widthofrect / 2, y + heightofrect / 2 - textheighttomiddle,
					printattr(raph.childNodes[i],false)).attr({
				"font-size" : 11,
				fill : fontcolor
			});
			infotextfull = r.text(x + widthofrect / 2, y + heightofrect / 2 - textheighttomiddle,
					printattr(raph.childNodes[i],true)).attr({
				"font-size" : 14,
				fill : fontcolor
			});
			infotextfull.hide();
			r.path("M"+x+" "+(y+textheighttomiddle)+"L"+(x+widthofrect)+" "+(y+textheighttomiddle));
			if (istrust)
				r.text(x + widthofrect / 2, y + valuetotop, raph.childNodes[i].trust).attr({
					"font-size" : 14,
					fill : numbercolor
				});
		}
		if (raph.childNodes[i].name == "switch") {
			shape = r.diamond(x + widthofrect / 2, y + heightofrect / 2,
					widthofrect / 2, heightofdiamond).attr({
				fill : diamcolor,
				stroke : diamcolor,
				"fill-opacity" : 0,
				"stroke-width" : 2,
				cursor : "pointer"
			});
//			diam.click(function (event) {
//			    this.attr({stroke: "red"});
//			});
			x += widthofrect + gap;
			for ( var j = 0, jj = raph.childNodes[i].childNodes.length; j < jj; j++) {
				if (jj % 2 == 1) {
					lines.push({"fromx":(x-gap),"fromy":(y+heightofrect/2),"tox":x,"toy":(y + heightofrect / 2 + (j - jj / 2)* (gap + heightofrect)),"text":printattr(raph.childNodes[i].childNodes[j],true)});
					if ((raph.childNodes[i].childNodes[j].name == "case" && raph.childNodes[i].childNodes[j].childNodes.length == 0)
							|| (raph.childNodes[i].childNodes[j].name == "otherwise" && raph.childNodes[i].childNodes[j].childNodes.length == 0)) {
						endp.push({
							"x" : x,
							"y" : y + heightofrect / 2 + (j - jj / 2)
									* (gap + heightofrect)
						});
					}
					draw(raph.childNodes[i].childNodes[j], x,
							(y + (j - jj / 2) * (heightofrect + gap)));
				} else {
					lines.push({"fromx":(x-gap),"fromy":(y + heightofrect / 2), "tox":x,"toy":(y + heightofrect / 2 + ((jj - 1) / 2+ 2 * j - jj)* (heightofrect + gap) / 2),"text":printattr(raph.childNodes[i].childNodes[j],true)});
					if ((raph.childNodes[i].childNodes[j].name == "case" && raph.childNodes[i].childNodes[j].childNodes.length == 0)
							|| (raph.childNodes[i].childNodes[j].name == "otherwise" && raph.childNodes[i].childNodes[j].childNodes.length == 0)) {
						endp.push({
							"x" : x,
							"y" : (y + heightofrect / 2 + ((jj - 1) / 2 + 2
									* j - jj)
									* (heightofrect + gap) / 2)
						});
					}
					draw(raph.childNodes[i].childNodes[j], x, (y  + ((jj - 1) / 2 + 2	* j - jj)* (heightofrect + gap) / 2));
					}
				}
				t = depth(raph.childNodes[i]);
				x += (2 * t + 1) * (widthofrect + gap) / 2;
				if (i != ii - 1 && star.sx == 0) {
					star = {
						"sx" : x,
						"sy" : y
					};
				}
			} else if(raph.childNodes[i].name == "reply"){
				shape = r.ellipse(x+widthofrect/2, y+heightofrect/2 , widthofrect/2, heightofrect/2).attr({
					fill : ellipsecolor,
					stroke : ellipsecolor,
					"fill-opacity" : 0,
					"stroke-width" : 2,
					cursor : "pointer"
				});
				x += widthofrect + gap;
				if (i != ii - 1){
					lines.push({"fromx":(x - (1 + t) * (widthofrect + gap) + widthofrect), "fromy":(y + heightofrect / 2),"tox":x,"toy":(y + heightofrect / 2)});
				}
				draw(raph.childNodes[i], x, y);
			}
			else if(raph.childNodes[i].name == "while"){
				shape = r.diamond(x+widthofrect/2, y+heightofrect/2 , widthofrect/2, heightofrect/2).attr({
					fill : diamcolor,
					stroke : diamcolor,
					"fill-opacity" : 0,
					"stroke-width" : 2,
					cursor : "pointer"
				});
				x += widthofrect + gap;
				if (i != ii - 1){
					lines.push({"fromx":(x - (1 + t) * (widthofrect + gap) + widthofrect), "fromy":(y + heightofrect / 2),"tox":x,"toy":(y + heightofrect / 2),"text":"FALSE"});
				}
				var wdepth = depth(raph.childNodes[i]);
				var wheight = height(raph.childNodes[i]) - 1;
				r.rect(x - widthofrect - gap*(3/2), y+gap/2+heightofrect, wdepth * (widthofrect + gap), wheight * (heightofrect +gap),rectcorner).attr({
					fill : rectcolor,
					stroke : rectcolor,
					"fill-opacity" : 0,
					"stroke-width" : 2,
					"stroke-dasharray" : "- "
				});
				lines.push({"fromx":(x-gap-widthofrect/2),"fromy":(y + heightofrect), "tox":(x-gap-widthofrect/2),"toy":(y + heightofrect + gap/2),"text":"TURE"});
				lines.push({"fromx":x - widthofrect - gap*(3/2),"fromy":(y + heightofrect + gap/2) + wheight * (heightofrect +gap)/2, "tox":(x-gap-widthofrect),"toy":(y + heightofrect/2),"text":"CONDITIONING","direction":"back"});
				draw(raph.childNodes[i], x - widthofrect - gap, y+gap+heightofrect);
			}
			else{
				shape = r.rect(x, y , widthofrect, heightofrect,rectcorner).attr({
					fill : rectcolor,
					stroke : rectcolor,
					"fill-opacity" : 0,
					"stroke-width" : 2,
					cursor : "pointer"
				});
				if (raph.childNodes[i].childNodes.length ==0 && ! raph.childNodes[i].hassbl){
					endp.push({
						"x" : x + widthofrect,
						"y" : y + heightofrect/2
					});
				}
				x += widthofrect + gap;
				if (i != ii - 1){
					lines.push({"fromx":(x - (1 + t) * (widthofrect + gap) + widthofrect), "fromy":(y + heightofrect / 2),"tox":x,"toy":(y + heightofrect / 2)});
				}
				draw(raph.childNodes[i], x, y);
			}
		if (istrust && raph.childNodes[i].name == "invoke"){
			var trst = gettrust(printattr(raph.childNodes[i],true),servicetrust);
			if (!trst)
				r.cross(x-gap,y,crossradius);
			else r.tick(x-gap,y,crossradius);
		}
		//对于很长的文字缩略
		texts.push(infotext);
		textsfull.push(infotextfull);
		shapes.push(shape);
		}
		for (var i = 0, ii = shapes.length;i < ii; i++)
		(function (i){
			shapes[i].mouseover( function (event) {
				texts[i].hide();
				textsfull[i].show();
			});
			shapes[i].mouseout (function (event) {
				texts[i].show();
				textsfull[i].hide();
		 	});
			
		})(i);
		if (star.sx != 0 && endp.length != 0) {
			for ( var n = 0, nn = endp.length; n < nn; n++)
				lines.push({"fromx":endp[n].x,"fromy":endp[n].y,"tox":star.sx,"toy":star.sy+heightofrect/2});
			star.sx = 0;
			star.sy = 0;
		}

	}
	//decide whether a node has nextsibling or not.
	function process(rap) {
		for ( var i = 0, ii = rap.childNodes.length; i < ii; i++) {
			if (i != ii - 1)
				rap.childNodes[i].hassbl = true;
			else
				rap.childNodes[i].hassbl = false;
			process(rap.childNodes[i]);
		}
	}
	var clearcanvas = function()
	{
		r.clear();
	};
	
	function drawgraph(divid,bpeljson,wid,hght,px,py){
		canvaslength = wid;
		canvaswidth = hght;
		r = Raphael(divid, canvaslength, canvaswidth);
		  text = bpeljson;
		  istrust = false;
		  process(text);
		  widthofrect = canvaslength/depth(text)/1.6;
	  	  heightofrect = canvaswidth/height(text)/2;
	      while (heightofrect > canvaswidth/8) heightofrect = heightofrect /2;
	      while (widthofrect > canvaslength/6) widthofrect = widthofrect /2;
	      if (heightofrect > widthofrect) heightofrect = widthofrect /2;
	  	  gap = heightofrect /2;
	  	  heightofdiamond = heightofrect/2;
	  	  rectcorner = widthofrect / 24;
	  	  crossradius = widthofrect / 12;
	  	  textheighttomiddle = heightofrect /3;
	  	  valuetotop = heightofrect - textheighttomiddle; 
	  	  positionx = px + widthofrect;
	  	  positiony = py + (height(text)/1.5)*(heightofrect);
		  draw(text, positionx, positiony);
		  linklines();   
	}
	function doclean(){
		r.clear();
		t = 0;
		star = {
				"sx" : 0,
				"sy" : 0
			};
		lines = [];
		endp = [];
		linestext = [];
		shapes = [];
		texts = [];
		textsfull = [];
	}
	function drawtrustgraph(servicetrustlist){
		if (text == null) return;
		doclean();
		servicetrust = servicetrustlist;
		istrust = true;
		process(text);
		draw(text, positionx, positiony);
		linklines();
	}
	