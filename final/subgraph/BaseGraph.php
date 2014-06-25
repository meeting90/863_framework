<?php
interface BaseGraph {
	public function get_vertex_list(); // return List<BaseVertex>
	public function get_edge_weight($source, $sink); // return double
	public function get_adjacent_vertices($vertex); // return set<vertex>
	public function get_precedent_vertices($vertex); // return set<vertex>
}
