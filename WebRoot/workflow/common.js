
//************控制台相关操作，打开关闭控制台，在控制台中添加不同类型的消息,清空控制台*****************//
function openConsole(){

	$(document.body).layout('expand','south');

} 
function closeConsole(){
	$(document.body).layout('collapse','south');
}
function appendSuccessMessage(newMsg){
var consoleText=$('<p>').attr('class','successful_message').html(newMsg);
	
	consoleText.insertAfter($('#console_p'));
}
function appendNormalMessage(newMsg){
	var consoleText=$('<p>').attr('class','normal_message').html(newMsg);
	
	consoleText.insertAfter($('#console_p'));
	
}
function appendErrorMessage(newMsg){
	var consoleText=$('<p>').attr('class','failed_message').html(newMsg);
	consoleText.insertAfter($('#console_p'));
}

function clearConsole() {
  $("#console").empty();
}

//********************  raphael 连接两个节点，此处节点可以代表用户或者Web服务*************//

Raphael.fn.connection = 
	function (obj1, obj2, line, weight,bg) {
		
	    if (obj1.line && obj1.from && obj1.to) {
	        line = obj1;
	        obj1 = line.from;
	        obj2 = line.to;
	        weight=line.weight;
	    }
	  
	    var bb1 = obj1.getBBox(),
	        bb2 = obj2.getBBox(),
	        p = [{x: bb1.x + bb1.width / 2, y: bb1.y - 1},
	        {x: bb1.x + bb1.width / 2, y: bb1.y + bb1.height + 1},
	        {x: bb1.x - 1, y: bb1.y + bb1.height / 2},
	        {x: bb1.x + bb1.width + 1, y: bb1.y + bb1.height / 2},
	        {x: bb2.x + bb2.width / 2, y: bb2.y - 1},
	        {x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1},
	        {x: bb2.x - 1, y: bb2.y + bb2.height / 2},
	        {x: bb2.x + bb2.width + 1, y: bb2.y + bb2.height / 2}],
	        d = {}, dis = [];
	    for (var i = 0; i < 4; i++) {
	        for (var j = 4; j < 8; j++) {
	            var dx = Math.abs(p[i].x - p[j].x),
	                dy = Math.abs(p[i].y - p[j].y);
	            if ((i == j - 4) || (((i != 3 && j != 6) || p[i].x < p[j].x) && ((i != 2 && j != 7) || p[i].x > p[j].x) && ((i != 0 && j != 5) || p[i].y > p[j].y) && ((i != 1 && j != 4) || p[i].y < p[j].y))) {
	                dis.push(dx + dy);
	                d[dis[dis.length - 1]] = [i, j];
	            }
	        }
	    }
	    if (dis.length == 0) {
	        var res = [0, 4];
	    } else {
	        res = d[Math.min.apply(Math, dis)];
	    }
	    var x1 = p[res[0]].x,
	        y1 = p[res[0]].y,
	        x4 = p[res[1]].x,
	        y4 = p[res[1]].y;
	    dx = Math.max(Math.abs(x1 - x4) / 2, 10);
	    dy = Math.max(Math.abs(y1 - y4) / 2, 10);
	    var x2 = [x1, x1, x1 - dx, x1 + dx][res[0]].toFixed(3),
	        y2 = [y1 - dy, y1 + dy, y1, y1][res[0]].toFixed(3),
	        x3 = [0, 0, 0, 0, x4, x4, x4 - dx, x4 + dx][res[1]].toFixed(3),
	        y3 = [0, 0, 0, 0, y1 + dy, y1 - dy, y4, y4][res[1]].toFixed(3);
	    var path = ["M", x1.toFixed(3), y1.toFixed(3), "C", x2, y2, x3, y3, x4.toFixed(3), y4.toFixed(3)].join(",");
	    if (line && line.line) {
	        line.bg && line.bg.attr({path: path});
	        line.line.attr({path: path});
	        if(line.text){
	        	var pathBox=line.line.getBBox();
	        	line.text.attr({"x":pathBox.x+pathBox.width/2,"y":pathBox.y+pathBox.height/2});
	        }
	    } else {
	    	
	    	var pathLine=this.path(path);
	    	
	    	if(weight==undefined || weight==0)
	    		weight=0.2;
	    	
	        var color = typeof line == "string" ? line : "#000";
	     
	       
	        pathLine.attr({stroke: color,fill: "none", "opacity":weight,"arrow-end" :'block-wide-long'});
	        
	        return {
	            bg: bg && bg.split,
	            line: pathLine,
	            from: obj1,
	            to: obj2,
	            weight: weight
	        };
	    }
	};

//********* 节点位置改变重新绘制节点连线 ***************//

var reconnection= function(obj){

	 for (var i = obj.paper.relations.length; i--;) {
     	if(obj==obj.paper.relations[i].from || obj==obj.paper.relations[i].to)
     		obj.paper.connection(obj.paper.relations[i]);
     }
},
//****************拖拽节点响应事件，显示节点信息 ****************//
dragger = function () {
	
    this.ox = this.type == "rect" ? this.attr("x") : this.attr("cx");
    this.oy = this.type == "rect" ? this.attr("y") : this.attr("cy");
    this.r=this.type=="rect"? this.attr("width"):this.attr("r");
    if(this.txt==null){
    	
    	this.txt=this.paper.text(this.ox,this.oy,"...");
        var txt=this.txt,
        	name=this.name,
        	id=this.data('name'),
        	type=this.type,
        	fullrate=this.data('fullrate');
       
        if(this.name==null){
    	    var url;
    	    if(this.type=="rect")
    	    	url=getWsNameURL+id;
    	    else 
    	    	url=getUserNameURL+id;
    	    $.ajax({
    	        url:url,
    	        success: function(data) {
    	        	name=data;
    	        	if(fullrate!=undefined)
    	        		txt.attr("text",name+':'+fullrate);
    	        	else
    	        		txt.attr('text',name);
    	        	if(type=='rect'){
    	        		appendNormalMessage("Web服务名:"+name);
    	        		appendNormalMessage("Web服务ID:"+id);
    	        		appendNormalMessage("**********");
    	        	}else{
    	        		appendNormalMessage("用户名:"+name);
    	        		appendNormalMessage("用户ID:"+id);
    	        		appendNormalMessage("**********");
    	        		
    	        	}
    	        		
    	        		
    	        }
    	      });
    	 
        }else{
        	console.info('else');
        	if(this.data('fullrate')!=undefined)
        		this.txt.attr("text",this.name+':'+this.data('fullrate'));
        	else
        		this.txt.attr('text',this.name);
        	this.txt.show();
        	if(this.type=='rect'){
        		appendNormalMessage("Web服务名:"+name);
        		appendNormalMessage("Web服务ID:"+this.data('name'));
        		appendNormalMessage("**********");
        	}else{
        		appendNormalMessage("用户名:"+name);
        		appendNormalMessage("用户ID:"+this.data('name'));
        		appendNormalMessage("**********");
        	
        	}	
        	
        }
    }
    
    this.animate({"fill-opacity": .2}, 500);
},
    move = function (dx, dy) {
		
        var att = this.type == "rect" ? {x: this.ox + dx, y: this.oy + dy,width: this.r*2, height: this.r*2} : {cx: this.ox + dx, cy: this.oy + dy, r: this.r*2};
        this.attr(att);
        this.txt.attr({x:this.ox+dx,y:this.oy+dy});
       
        reconnection(this);
       
        this.paper.safari();
    },
    up = function () {
    	
    	if(this.type=="rect"){
    		this.attr("width",this.r);
    		this.attr("height",this.r);
    		this.animate({"fill-opacity": 0.5}, 500,"linear",reconnection(this));
    	}else{
    		this.attr("r",this.r);
    		this.animate({"fill-opacity": 0.5}, 500,"linear",reconnection(this));
    	}
    	this.txt.hide();
    };

