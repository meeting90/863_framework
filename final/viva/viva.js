

function drawKSgraph(data){
	
		var nodes = data.nodes;
		var edges = data.edges;
		var graph = Viva.Graph.graph();

		var graphics = Viva.Graph.View.svgGraphics(),
			nodeSize = 30;

		var renderer = Viva.Graph.View.renderer(graph, {
				graphics : graphics,
				container: document.getElementById("placeholder")
			});
		renderer.run();
		
		graphics.node(function(node) {
		    var ui = Viva.Graph.svg('g'),
			    svgText = Viva.Graph.svg('text')
					.attr('y','-4px')
					.attr('font-size','8px')
					.text(node.data),
			    img = Viva.Graph.svg('circle')
					.attr('cx',nodeSize/2)
					.attr('cy',nodeSize/2)
					.attr('r',nodeSize/2)
					.attr('fill','#84C1FF')
					.attr('stroke','#444444')
					.attr('stroke-width','1');
			
			//alert(data.nodes[node.id].weight);
			if(node.id == 0){
				svgText.attr('font-size','12px')
						.attr('stroke','#660066')
						.text(node.data);
				img.attr('fill','#9F35FF');
			}
			if(node.id == 1){
				svgText.attr('font-size','12px')
						.attr('stroke','#ff0000')
						.text(node.data);
				img.attr('fill','#9F35FF');
			}
			
			ui.append(img);
		    ui.append(svgText);
		    
		    return ui;
		}).placeNode(function(nodeUI, pos) {
			nodeUI.attr('transform', 
						'translate(' + 
							(pos.x - nodeSize/2) + ',' + (pos.y - nodeSize/2) + 
						')');
		});

		var createMarker = function(id) {
				return Viva.Graph.svg('marker')
						   .attr('id', id)
						   .attr('viewBox', "0 0 10 10")
						   .attr('refX', "10")
						   .attr('refY', "5")
						   .attr('markerUnits', "strokeWidth")
						   .attr('markerWidth', "10")
						   .attr('markerHeight', "5")
						   .attr('orient', "auto");
			},

		marker = createMarker('Triangle');
		marker.append('path').attr('d', 'M 0 0 L 10 5 L 0 10 z');

		var defs = graphics.getSvgRoot().append('defs');
		defs.append(marker);

		var geom = Viva.Graph.geom(); 
		
		graphics.link(function(link){
			var path = Viva.Graph.svg('path')
						.attr('stroke-width','1')
					   .attr('id', link.fromId+link.toId)
					   .attr('marker-end', 'url(#Triangle)');
			var cost = Viva.Graph.svg('g')
						.attr('id',link.fromId+link.toId+'t'),
				text = Viva.Graph.svg('text')
						.attr('font-size','6px')
						.attr('x','-10px')
						.text(link.data);
			switch(link.data){
				case 0.1:path.attr('stroke','#00ffff');break;
				case 0.4:path.attr('stroke','#28004D');break;
				case 0.7:path.attr('stroke','#0000ff');break;
				case 0.9:path.attr('stroke','#ff0000');break;
			}
			cost.append(text);
			path.cost = cost;
			/*$(path).hover(function(){
				$(path).after(path.cost);
			});*/
			//alert(link.attr('id'));
			//defs.append(path);
			return path;
		}).placeLink(function(linkUI, fromPos, toPos) {
			var toNodeSize = nodeSize,
				fromNodeSize = nodeSize;
				
			var from = geom.intersectRect(
					// rectangle:
							fromPos.x - fromNodeSize / 2, // left
							fromPos.y - fromNodeSize / 2, // top
							fromPos.x + fromNodeSize / 2, // right
							fromPos.y + fromNodeSize / 2, // bottom
					// segment:
							fromPos.x, fromPos.y, toPos.x, toPos.y) 
					   || fromPos; // if no intersection found - return center of the node
			
			var to = geom.intersectRect(
					// rectangle:
							toPos.x - toNodeSize /2, // left
							toPos.y - toNodeSize / 2, // top
							toPos.x + toNodeSize /2, // right
							toPos.y + toNodeSize / 2, // bottom
					// segment:
							toPos.x, toPos.y, fromPos.x, fromPos.y) 
						|| toPos; // if no intersection found - return center of the node
						
			var data = 'M' + from.x + ',' + from.y +
					   'L' + to.x + ',' + to.y;
			linkUI.cost.attr('transform', 
						'translate(' + 
							  ((fromPos.x + toPos.x)/2) + ',' + ((fromPos.y + toPos.y)/2) + 
						')');
			linkUI.parentNode.append(linkUI.cost);
			linkUI.attr("d", data);
			//defs.append(linkUI);
		});
		
		var i;
		for(i=0;i<nodes.length;i++){
			graph.addNode(nodes[i].id,nodes[i].name);
		}
		for(i=0;i<edges.length;i++){
			graph.addLink(edges[i].source, edges[i].target, edges[i].weight);
		}

	
	
}

