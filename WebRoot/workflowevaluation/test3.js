var text ;
var servicetrust;
var trustvaluelist;
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
	var numberOfServices = 0;
	var anaGap;
        var TRUST_RIGHT = 2, TRUST_WRONG =1, TRUST_NONE =3;
        
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
                return -1;
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
		if (raph.name == "if")
			return (max);//max ought to be =1 , if with case && otherwise
		return (max + raph.childNodes.length);
	};
	///height of the graph
	var height = function(raph) {
		var dep = 1;
		var switchflag = false;
		if (raph.childNodes.length == 0)
			return 0;
		for ( var i = 0, ii = raph.childNodes.length; i < ii; i++) {
			//document.write(raph.childNodes[i].name+i+" ");
			if (raph.childNodes[i].name == "if"){
				dep += height(raph.childNodes[i]);
				switchflag = true;
			}
			if (raph.name == "if" && (raph.childNodes[i].name == "elseif" || raph.childNodes[i].name == "else")){
				dep += height(raph.childNodes[i]);
			}
		}
		if (switchflag)
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
		else if (raph.name == "if" ){
			ret = "Switch";
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
					infotext = r.text(x + widthofrect / 2, y + heightofrect / 2,printattr(raph.childNodes[i],false)).attr({"font-size" : 11,fill : fontcolor});
					infotextfull = r.text(x + widthofrect / 2, y + heightofrect / 2,printattr(raph.childNodes[i],true)).attr({"font-size" : 14,	fill : fontcolor});
					infotextfull.hide();	
			}
			else{
				infotext = r.text(x + widthofrect / 2, y + heightofrect / 2 - textheighttomiddle,printattr(raph.childNodes[i],false)).attr({"font-size" : 11,fill : fontcolor});
				infotextfull = r.text(x + widthofrect / 2, y + heightofrect / 2 - textheighttomiddle,printattr(raph.childNodes[i],true)).attr({"font-size" : 14,fill : fontcolor});
				infotextfull.hide();
				r.path("M"+x+" "+(y+textheighttomiddle)+"L"+(x+widthofrect)+" "+(y+textheighttomiddle));
				if (istrust && gettrust(printattr(raph.childNodes[i],true),servicetrust) != TRUST_NONE && gettrust(printattr(raph.childNodes[i],true),trustvaluelist) != -1)
					r.text(x + widthofrect / 2, y + valuetotop, gettrust(printattr(raph.childNodes[i],true),trustvaluelist)).attr({"font-size" : 14,fill : numbercolor});
			}
			if (raph.childNodes[i].name == "if" || raph.childNodes[i].name == "elseif" || raph.childNodes[i].name == "else") {
				shape = r.diamond(x + widthofrect / 2, y + heightofrect / 2,widthofrect / 2, heightofdiamond).attr({fill : diamcolor,stroke : diamcolor,"fill-opacity" : 0,	"stroke-width" : 2,cursor : "pointer"});
				x += widthofrect + gap;
				var conditionString= raph.childNodes[i].childNodes[0].value;
				var num_branch = 1;
				var horizontal_gap = 0;
				for(var j = 1, jj = raph.childNodes[i].childNodes.length; j < jj; ++j){
					if(raph.childNodes[i].childNodes[j].name == "elseif" || raph.childNodes[i].childNodes[j].name == "else"){
						num_branch +=1;
						horizontal_gap += height(raph.childNodes[i].childNodes[j]);
					}
				}
				for(var j = 1, jj = raph.childNodes[i].childNodes.length; j < jj; ++j){
					if(raph.childNodes[i].childNodes[j].name == "invoke"){
						if (j ==1)
							lines.push({"fromx":(x-gap),"fromy":(y+ heightofrect/2),
								"tox":x+(j-1)*(widthofrect + gap),"toy":(y + heightofrect / 2 -horizontal_gap/2* (gap + heightofrect)),"text":conditionString});
						else
							lines.push({"fromx":(x-gap + (j-1)*(widthofrect + gap)),"fromy":(y+ heightofrect/2 - horizontal_gap/2*(gap + heightofrect)),
							"tox":x+(j-1)*(widthofrect + gap),"toy":(y + heightofrect / 2 -horizontal_gap/2* (gap + heightofrect)),"text":conditionString});
						if ((j == jj-1) || (raph.childNodes[i].childNodes[j+1].name != "invoke")){
							endp.push({	"x" : (x +(j-1)*(gap+widthofrect) +widthofrect),"y" :y + heightofrect / 2 -horizontal_gap/2* (gap + heightofrect)});
						}
						var shape2 = r.rect(x+(j-1)*(widthofrect + gap), (y -horizontal_gap/2* (gap + heightofrect)), widthofrect, heightofrect,rectcorner)
						.attr({	fill : rectcolor,stroke : rectcolor,"fill-opacity" : 0,	"stroke-width" : 2,cursor : "pointer"});
						var infotext2 = r.text(x+(j-1)*(widthofrect + gap)+ widthofrect / 2, (y -horizontal_gap/2* (gap + heightofrect)+ heightofrect / 2 ) - textheighttomiddle,printattr(raph.childNodes[i].childNodes[j],false)).attr({"font-size" : 11,fill : fontcolor});
						var infotextfull2 = r.text(x+(j-1)*(widthofrect + gap)+ widthofrect / 2, (y -horizontal_gap/2* (gap + heightofrect)+ heightofrect / 2 ) - textheighttomiddle,printattr(raph.childNodes[i].childNodes[j],true)).attr({"font-size" : 14,fill : fontcolor});
						infotextfull2.hide();
						texts.push(infotext2);
						textsfull.push(infotextfull2);
						r.path("M"+(x+(j-1)*(widthofrect + gap))+" "+(y -horizontal_gap/2* (gap + heightofrect)+textheighttomiddle)+
								"L"+(x+(j-1)*(widthofrect + gap)+widthofrect)+" "+(y -horizontal_gap/2* (gap + heightofrect)+textheighttomiddle));
						shapes.push(shape2);
						if (istrust && gettrust(printattr(raph.childNodes[i].childNodes[j],true),servicetrust) != TRUST_NONE && gettrust(printattr(raph.childNodes[i].childNodes[j],true),trustvaluelist) != -1){
							var trst = gettrust(printattr(raph.childNodes[i].childNodes[j],true),servicetrust);
							r.text(x+(j-1)*(widthofrect + gap) + widthofrect / 2, y -horizontal_gap/2* (gap + heightofrect) + valuetotop, gettrust(printattr(raph.childNodes[i].childNodes[j],true),trustvaluelist)).attr({"font-size" : 14,fill : numbercolor});
							if (trst == TRUST_WRONG)
								r.cross(x+(j-1)*(widthofrect + gap)+ widthofrect,y -horizontal_gap/2* (gap + heightofrect),crossradius);
				            else if(trst == TRUST_RIGHT)
								r.tick(x+(j-1)*(widthofrect + gap)+ widthofrect,y -horizontal_gap/2* (gap + heightofrect),crossradius);			                                
						}
					}
					else if(raph.childNodes[i].childNodes[j].name == "elseif" || raph.childNodes[i].childNodes[j].name == "else") {
						if (raph.childNodes[i].childNodes[j].name == "elseif")
							conditionString = raphchildNodes[i].childNodes[j].childNodes[0];
						else
							conditionString = "else";
						lines.push({"fromx":(x-gap),"fromy":(y+ heightofrect/2),"tox":x,"toy":(y + (j - horizontal_gap/2)* (gap + heightofrect)/2),"text":conditionString});
						draw(raph.childNodes[i].childNodes[j],x, y - heightofrect / 2  + (j - horizontal_gap/2)* (gap + heightofrect)/2);
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
			}else if(raph.childNodes[i].name == "reply"){
				shape = r.ellipse(x+widthofrect/2, y+heightofrect/2 , widthofrect/2, heightofrect/2).attr({fill : ellipsecolor,
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
			}else{
				shape = r.rect(x, y , widthofrect, heightofrect,rectcorner).attr({
						fill : rectcolor,
						stroke : rectcolor,
						"fill-opacity" : 0,
						"stroke-width" : 2,
						cursor : "pointer"
				});
				numberOfServices ++;
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
				if (trst == TRUST_WRONG)
					r.cross(x-gap,y,crossradius);
	            else if(trst == TRUST_RIGHT)
					r.tick(x-gap,y,crossradius);
	                                
			}
			//shorten long variables.
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
	
	//draw analysis graph.
	var pend=[];
	var vtrust = [];
	var paintIndex=0;
	function drawAnalysis(raph,x,y)
	{
//		if (raph.name == "reply"){
//			vtrust.push(1);
//			return 1;
//		}
		var sum =0;
		for (var i=0,ii= raph.childNodes.length;i <ii;++i){
			if(raph.childNodes[i].name == "invoke"){
				paintIndex ++;
				r.circle(x + (sum)*4*anaGap ,y,anaGap).attr({
					fill : diamcolor,
					stroke : diamcolor,
					"fill-opacity" : 0,
					"stroke-width" : 2,
					"class": "ss s"+paintIndex
				});
				r.text(x + ( sum )*4*anaGap,y,printattr(raph.childNodes[i],true)).attr({"font-size":10, fill :"blue","class": "ss s"+paintIndex});
				r.text(x + ( sum )*4*anaGap,y + 1.5*anaGap,gettrust(printattr(raph.childNodes[i],true),servicetrust)).attr({
					"font-size" : 14,
					fill : numbercolor,
					"class": "ss s"+paintIndex
				});
				vtrust.push(gettrust(printattr(raph.childNodes[i],true),servicetrust));
				pend.push({"x":x + (sum)*4*anaGap,"y":y});
				sum ++;

			}
			else if (raph.childNodes[i].name == 'if'){
				for(var j =0, jj = raph.childNodes[i].childNodes.length;j<jj;++j){
					if (raph.childNodes[i].childNodes[j].childNodes.length == 0){//'case' or 'otherwise', with no childs
						paintIndex++;
						r.circle(x + (sum)*4*anaGap,y,anaGap).attr({
							fill : diamcolor,
							stroke : diamcolor,
							"fill-opacity" : 0,
							"stroke-width" : 2,
							"class": "ss s"+paintIndex
						});
						r.text(x +  ( sum)*4*anaGap,y,'TRUE').attr({"font-size":13, fill :"blue","class": "ss s"+paintIndex});
						r.text(x + ( sum )*4*anaGap,y + 1.5*anaGap,1).attr({
							"font-size" : 14,
							fill : numbercolor,
							"class": "ss s"+paintIndex
						});
						vtrust.push(1);
						pend.push({"x":x + ( sum)*4*anaGap,"y":y});
						sum ++;
					}else{
						sum += drawAnalysis(raph.childNodes[i].childNodes[j],x + ( sum)*4*anaGap,y);
					}
				}
				paintIndex++;
				r.diamond(x +   sum/2.0*4*anaGap, y - (sum-2)*3*anaGap,
						1.5*anaGap, anaGap).attr({
					fill : diamcolor,
					stroke : diamcolor,
					"fill-opacity" : 0,
					"stroke-width" : 2,
					"class": "ss s"+paintIndex
				});
				r.text(x + sum/2.0*4*anaGap, y - (sum-2)*3*anaGap,"Selection").attr({"font-size":13, fill :"blue","class": "ss s"+paintIndex});
				var ttrustSel =0;
				for(var k =0, kk = raph.childNodes[i].childNodes.length;k<kk;++k){
					var endp = pend.pop();
					ttrustSel += vtrust.pop()*1.0/kk;
					if (endp){
						if (!endp.rect)
							r.path("M"+endp.x+" "+(endp.y-anaGap)+"L"+(x + sum/2*4*anaGap)+" "+( y - (sum-2)*3*anaGap+anaGap)).attr({"class": "ss s"+paintIndex});
						else						
							r.path("M"+(endp.x+anaGap)+" "+(endp.y-anaGap)+"L"+(x + sum/2*4*anaGap)+" "+( y - (sum-2)*3*anaGap+anaGap)).attr({"class": "ss s"+paintIndex});
					}
				}
				pend.push({"x":x +sum/2.0*4*anaGap,"y":(y - (sum-2)*3*anaGap)});
				r.text(x + sum/2.0*4*anaGap + 3*anaGap, y - (sum-2)*3*anaGap,ttrustSel.toFixed(6)).attr({"font-size":13, fill :"blue","class": "ss s"+paintIndex});
				vtrust.push(ttrustSel);
				
			}
//			else
//				sum += drawAnalysis(raph.childNodes[i],x + ( sum)*4*anaGap,y);
		}
		var endp = 0;
		if (raph.childNodes.length > 1){
			paintIndex++;
			r.rect(x + sum*anaGap,y -(1+ (sum-2))*3*anaGap-anaGap,2*anaGap, anaGap,rectcorner).attr({
				fill : rectcolor,
				stroke : rectcolor,
				"fill-opacity" : 0,
				"stroke-width" : 2,
				"class": "ss s"+paintIndex
			});
			r.text(x + sum*anaGap+anaGap,y -(1+ (sum-2))*3*anaGap-anaGap/2,"Sequence").attr({"font-size":13, fill :"blue","class": "ss s"+paintIndex});
			var ttrustSeq = 1;
			for(var k =0, kk = raph.childNodes.length;k<kk;++k){
				 endp = pend.pop();
				 if (raph.childNodes[k].name != "reply")
					 ttrustSeq *= vtrust.pop();
				 if (endp){
					 if (endp.rect)
						 r.path("M"+(endp.x+anaGap)+" "+(endp.y-anaGap)+"L"+(x + (sum)*anaGap+anaGap)+" "+( y - (1+ (sum-2))*3*anaGap)).attr({"class": "ss s"+paintIndex});
					 else
						 r.path("M"+endp.x+" "+(endp.y-anaGap)+"L"+(x + (sum)*anaGap+anaGap)+" "+( y - (1+ (sum-2))*3*anaGap)).attr({"class": "ss s"+paintIndex});
				 }
			}
			pend.push({"x":(x + (sum)*anaGap),"y":( y - (1+ (sum-2))*3*anaGap),"rect":true});
			r.text((x + (sum)*anaGap + 4*anaGap),( y - (1+ (sum-2))*3*anaGap- anaGap/2),ttrustSeq.toFixed(6)).attr({"font-size":13, fill :"blue","class": "ss s"+paintIndex});
			vtrust.push(ttrustSeq);
		}
		return sum;
	}
	function showThem(){
		tt(1);
	}
        var timer;
        function tt(id)
	{
		$(".s"+id).css("display","block");
		if (id > paintIndex)
			return;
		$(".s"+id).show(1000);
		if($(".s"+id).has("tspan").length){
			$(".s"+id).children("tspan").show(1000);
		}
		timer = setTimeout(function(){tt(++id);},1000);
                //tt(++id);
	}
        function clearTimer()
        {
        	paintIndex = 0;
		pend = [];
                vtrust = [];
        	clearTimeout(timer);

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
	  	  positionx = px ;
	  	  positiony = py + (height(text))*(heightofrect);
		  draw(text, positionx, positiony);
		  linklines();
	}
	function drawAnalysisGraph(divid,bpeljson,trustlist,wid,hght,px,py){
		$(divid+" > tspan").hide();
		if (r)
			doclean();
		servicetrust = trustlist;
		r = Raphael(divid, wid, hght);
		anaGap =20;
	  	drawAnalysis(bpeljson,px,py);
	  	showThem();
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
		servicetrust = [];

	}
	function drawtrustgraph(div,text,servicetrustlist,trstvaluelist,wid,hght,px,py){
		canvaslength = wid;
		canvaswidth = hght;
		if (r)
			doclean();
		r = Raphael(div, canvaslength, canvaswidth);
		servicetrust = servicetrustlist;
        trustvaluelist = trstvaluelist;
		istrust = true;
		process(text);
		positionx = px ;
	  	positiony = py + (height(text))*(heightofrect);
		draw(text, positionx, positiony);
		linklines();
	}
	