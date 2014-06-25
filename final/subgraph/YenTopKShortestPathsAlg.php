<?php
require_once 'VariableGraph.php';
require_once 'DijkstraShortestPathAlg.php';
require_once 'Path.php';
require_once 'Edge.php';
class YenTopKShortestPath {
	private $_graph = null;
	
	// intermediate variables
	private $_result_list = array ();
	private $_path_derivation_vertex_index = array ();
	private $_path_candidates = null;
	// the ending vertices of the paths
	private $_source_vertex = null;
	private $_target_vertex = null;
	// variables for debugging and testing
	private $_generated_path_num = 0;
	public function __construct($graph, $type, $source_vt, $target_vt) {
		if ($graph == null) {
			throw new Exception ( "A NUll Graph object occurs!" );
		}
		
		$this->_graph = new VariableGraph ( $graph, $type );
		$this->_source_vertex = $source_vt;
		$this->_target_vertex = $target_vt;
		$this->_init ();
	}
	private function _init() {
		$this->clear ();
		
		if ($this->_source_vertex != null && $this->_target_vertex != null) {
			$shortest_path = $this->get_shortest_path ( $this->_source_vertex, $this->_target_vertex );
			
			assert ( $shortest_path instanceof Path );
			if (count ( $shortest_path->get_vertices () ) != 0) {
				$this->_path_candidates->add ( $shortest_path );
				$this->_path_derivation_vertex_index [$shortest_path->get_id ()] = $this->_source_vertex;
			}
		}
	}
	public function clear() {
		unset ( $this->_path_candidates );
		unset ( $this->_path_derivation_vertex_index );
		unset ( $this->_result_list );
		$this->_path_candidates = new QYPriorityQueue ( null, null );
		$this->_path_derivation_vertex_index = array ();
		$this->_result_list = array ();
		$this->_generated_path_num = 0;
	}
	public function get_shortest_path($source_vt, $target_vt) {
		assert ( $source_vt instanceof BaseVertex );
		assert ( $target_vt instanceof BaseVertex );
		Path::reset ();
		$dijkstra_alg = new DijkstraShortestPathAlg ( $this->_graph );
		return $dijkstra_alg->get_shortest_path ( $source_vt, $target_vt );
	}
	public function has_next() {
		return ! $this->_path_candidates->isEmpty ();
	}
	public function next() {
		$cur_path = $this->_path_candidates->poll ();
		
		assert ( $cur_path instanceof Path );
		
		$this->_result_list [] = $cur_path;
		
		$cur_derivation = $this->_path_derivation_vertex_index [$cur_path->get_id ()];
		
		$sublist_index = array_search ( $cur_derivation, $cur_path->get_vertices () );
		
		$cur_path_sublist = array_splice ( $cur_path->get_vertices (), 0, $sublist_index, array () );
		
		$count = count ( $this->_result_list );
		for($i = 0; $i < $count - 1; ++ $i) {
			$cur_result_path = $this->_result_list [$i];
			assert ( $cur_result_path instanceof Path );
			$cur_dev_vertex_id = array_search ( $cur_derivation, $cur_result_path->get_vertices () );
			
			if ($cur_dev_vertex_id === false)
				continue;
			$result_path_sublist = array_splice ( $cur_result_path->get_vertices (), 0, $cur_dev_vertex_id, array () );
			
			$diff = array_udiff ( $cur_path_sublist, $result_path_sublist, function ($obj_a, $obj_b) {
				assert ( $obj_a instanceof Vertex );
				assert ( $obj_b instanceof Vertex );
				return $obj_a->get_id () - $obj_b->get_id ();
			} );
			
			$diff2 = array_udiff ( $result_path_sublist, $cur_path_sublist, function ($obj_a, $obj_b) {
				assert ( $obj_a instanceof Vertex );
				assert ( $obj_b instanceof Vertex );
				return $obj_a->get_id () - $obj_b->get_id ();
			} );
			
			if (count ( $diff ) != 0)
				
				continue;
			
			if (count ( $diff2 ) != 0)
				
				continue;
			
			$path_vertices = $cur_result_path->get_vertices ();
			$cur_succ_vertex = $path_vertices [$cur_dev_vertex_id + 1];
			assert ( $cur_derivation instanceof BaseVertex );
			assert ( $cur_succ_vertex instanceof BaseVertex );
			
			$this->_graph->remove_edge ( new Pair ( $cur_derivation->get_id (), $cur_succ_vertex->get_id () ) );
		}
		$path_length = count ( $cur_path->get_vertices () );
		$cur_path_vertex_list = $cur_path->get_vertices ();
		;
		for($i = 0; $i < $path_length - 1; ++ $i) {
			assert ( $cur_path_vertex_list [$i] instanceof BaseVertex );
			assert ( $cur_path_vertex_list [$i + 1] instanceof BaseVertex );
			$this->_graph->remove_vertex ( $cur_path_vertex_list [$i]->get_id () );
			$this->_graph->remove_edge ( new Pair ( $cur_path_vertex_list [$i]->get_id (), $cur_path_vertex_list [$i + 1]->get_id () ) );
		}
		$reverse_tree = new DijkstraShortestPathAlg ( $this->_graph );
		$reverse_tree->get_shortest_path_flower ( $this->_target_vertex );
		
		$is_done = false;
		for($i = $path_length - 2; $i >= 0 && ! $is_done; -- $i) {
			$cur_recover_vertex = $cur_path_vertex_list [$i];
			$this->_graph->recover_removed_vertix ( $cur_recover_vertex->get_id () );
			if ($cur_recover_vertex->get_id () == $cur_derivation->get_id ()) {
				$is_done = true;
			}
			$sub_path = $reverse_tree->update_cost_forward ( $cur_recover_vertex );
			
			if ($sub_path != null) {
				// "sub_path1:".json_encode($sub_path->tojsonarray()).'</br>';
				++ $this->_generated_path_num;
				
				$cost = 0;
				$pre_path_list = array ();
				$reverse_tree->correct_cost_backward ( $cur_recover_vertex );
				for($j = 0; $j < $path_length; ++ $j) {
					$cur_vertex = $cur_path_vertex_list [$j];
					assert ( $cur_vertex instanceof BaseVertex );
					if ($cur_vertex->get_id () == $cur_recover_vertex->get_id ()) {
						$j = $path_length;
					} else {
						$cost += $this->_graph->get_edge_weight_of_graph ( $cur_path_vertex_list [$j], $cur_path_vertex_list [$j + 1] );
						$pre_path_list [] = $cur_vertex;
					}
				}
				
				assert ( is_array ( $pre_path_list ) );
				assert ( $sub_path instanceof Path );
				
				$pre_path_list = array_merge ( $pre_path_list, $sub_path->get_vertices () );
				
				$sub_path->set_weight ( $cost + $sub_path->get_weight () );
				$sub_path->clear_verties ();
				$sub_path->add_verties ( $pre_path_list );
				// echo "sub_path2:".json_encode($sub_path->tojsonarray()).'</br>';
				// /////////////////////////////////////////////
				if (! array_key_exists ( $sub_path->get_id (), $this->_path_derivation_vertex_index )) {
					$this->_path_candidates->add ( $sub_path );
					$this->_path_derivation_vertex_index [$sub_path->get_id ()] = $cur_recover_vertex;
				}
			}
			$succ_vertex = $cur_path_vertex_list [$i + 1];
			assert ( $succ_vertex instanceof BaseVertex );
			$start_vetex_distance_index = $reverse_tree->get_start_vertex_distance_index ();
			$predecessor_index = $reverse_tree->get_predecessor_index ();
			$this->_graph->recover_removed_edge ( new Pair ( $cur_recover_vertex->get_id (), $succ_vertex->get_id () ) );
			
			$cost_1 = $this->_graph->get_edge_weight ( $cur_recover_vertex, $succ_vertex ) + $start_vetex_distance_index [$succ_vertex->get_id ()];
			
			if ($start_vetex_distance_index [$cur_recover_vertex->get_id ()] > $cost_1) {
				
				$reverse_tree->update_start_vertex_distance_index ( $cur_recover_vertex->get_id (), $cost_1 );
				$reverse_tree->update_precdecessor_index ( $cur_recover_vertex->get_id (), $succ_vertex );
				$reverse_tree->correct_cost_backward ( $cur_recover_vertex );
			}
		}
		$this->_graph->recover_removed_edges ();
		$this->_graph->recover_removed_vetices ();
		return $cur_path;
	}
	public function get_shortest_paths($source_vertex, $target_vertex, $top_k) {
		assert ( $source_vertex instanceof BaseVertex );
		assert ( $target_vertex instanceof BaseVertex );
		$this->_source_vertex = $source_vertex;
		$this->_target_vertex = $target_vertex;
		$this->_init ();
		
		$count = 0;
		while ( $this->has_next () && $count < $top_k ) {
			$this->next ();
			++ $count;
		}
		return $this->_result_list;
	}
	public function get_result_list() {
		return $this->_result_list;
	}
	public function get_candidate_size() {
		return count ( $this->_path_derivation_vertex_index );
	}
	public function get_generated_path_size() {
		return $this->_generated_path_num;
	}
	public function get_result_graph($source, $target, $top_k) {
		$json = array ();
		$nodes = array ();
		$edges = array ();
		// var_dump($this->_result_list);
		$this->get_shortest_paths ( $source, $target, $top_k );
		$nodes [] = $source;
		$nodes [] = $target;
		foreach ( $this->_result_list as $path ) {
			
			assert ( $path instanceof Path );
			$vertices = $path->get_vertices ();
			for($i = 0; $i < count ( $vertices ) - 1; $i ++) {
				$pre_vertex = $vertices [$i];
				$cur_vertex = $vertices [$i + 1];
				
				assert ( $pre_vertex instanceof Vertex );
				assert ( $cur_vertex instanceof Vertex );
				$nodes [] = $cur_vertex;
				if ($this->_graph->get_edge_weight_of_graph ( $pre_vertex, $cur_vertex ) != Graph::$DISCONNECTED) {
					$edge = new Edge ( $pre_vertex->get_id (), $cur_vertex->get_id (), $this->_graph->get_edge_trust ( $pre_vertex, $cur_vertex ) );
					$edges [] = $edge;
				}
			}
		}
		$nodes = array_unique ( $nodes );
		$jsonnodes = array ();
		foreach ( $nodes as $vertex ) {
			assert ( $vertex instanceof Vertex );
			// if($source==$vertex){
			// $vertex->set_weight(0);
			// }else{
			// $path=$this->get_shortest_path($source, $vertex);
			// $vertex->set_weight($path->get_weight());
			// }
			$jsonnodes [] = $vertex->tojsonarray ();
		}
		$edges = array_unique ( $edges );
		$jsonedges = array ();
		foreach ( $edges as $edge ) {
			assert ( $edge instanceof Edge );
			$jsonedges [] = $edge->tojsonarray ();
		}
		$json ['nodes'] = $jsonnodes;
		$json ['edges'] = $jsonedges;
		return $json;
	}
	public function get_result_matrix_graph($source, $target, $top_k) {
		$graph_matrix = array ();
		$this->get_shortest_paths ( $source, $target, $top_k );
		foreach ( $this->_result_list as $path ) {
			assert ( $path instanceof Path );
			$vertices = $path->get_vertices ();
			for($i = 0; $i < count ( $vertices ) - 1; $i ++) {
				$cur_v = $vertices [$i];
				$next_v = $vertices [$i + 1];
				assert ( $cur_v instanceof Vertex );
				assert ( $next_v instanceof Vertex );
				$trust = $this->_graph->get_edge_trust ( $cur_v, $next_v );
				$graph_matrix [$cur_v->get_id ()] [$next_v->get_id ()] = $trust;
			}
		}
		return $graph_matrix;
	}
}

	

	
 