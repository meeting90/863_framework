<?php
require_once 'QYPriorityQueue.php';
require_once 'Path.php';
require_once 'VariableGraph.php';
require_once 'Graph.php';
class DijkstraShortestPathAlg {
	private $_graph = null;
	private $_determined_vertex_set = array ();
	private $_vertex_candidate_queue = null;
	private $_start_vertex_distance_index = array ();
	private $_predecessor_index = array ();
	public function __construct($graph) {
		assert ( $graph instanceof BaseGraph );
		$this->_graph = $graph;
		$this->_vertex_candidate_queue = new QYPriorityQueue ( null, null );
	}
	public function clear() {
		unset ( $this->_determined_vertex_set );
		unset ( $this->_vertex_candidate_queue );
		unset ( $this->_start_vertex_distance_index );
		unset ( $this->_predecessor_index );
		$this->_determined_vertex_set = array ();
		$this->_vertex_candidate_queue = new QYPriorityQueue ( null, null );
		$this->_start_vertex_distance_index = array (); // map vertex_id distance
		$this->_predecessor_index = array (); // map vertex_id vertex
	}
	public function get_start_vertex_distance_index() {
		return $this->_start_vertex_distance_index;
	}
	public function get_predecessor_index() {
		return $this->_predecessor_index;
	}
	public function get_shortest_path_tree($root) {
		$this->determine_shortest_paths ( $root, null, true );
	}
	public function get_shortest_path_flower($root) {
		$this->determine_shortest_paths ( null, $root, false );
	}
	protected function determine_shortest_paths($source_vertex, $sink_vertex, $is_source2sink) {
		$this->clear ();
		$end_vertex = null;
		$start_vertex = null;
		if ($is_source2sink) {
			$end_vertex = $sink_vertex;
			$start_vertex = $source_vertex;
		} else {
			$end_vertex = $source_vertex;
			$start_vertex = $sink_vertex;
		}
		assert ( $start_vertex instanceof BaseVertex );
		
		$this->_start_vertex_distance_index [$start_vertex->get_id ()] = 0;
		$start_vertex->set_weight ( 0 );
		$this->_vertex_candidate_queue->add ( $start_vertex );
		
		while ( ! $this->_vertex_candidate_queue->isEmpty () ) {
			$cur_candidate = $this->_vertex_candidate_queue->poll ();
			if ($cur_candidate == $end_vertex)
				break;
			$this->_determined_vertex_set [] = $cur_candidate;
			$this->_improve_to_vertex ( $cur_candidate, $is_source2sink );
		}
	}
	private function _improve_to_vertex($vertex, $is_source2sink) {
		assert ( $vertex instanceof BaseVertex );
		unset ( $neighbor_vertex_list );
		$neighbor_vertex_list = $is_source2sink ? $this->_graph->get_adjacent_vertices ( $vertex ) : $this->_graph->get_precedent_vertices ( $vertex );
		
		foreach ( $neighbor_vertex_list as $cur_adjacent_vertex ) {
			assert ( $cur_adjacent_vertex instanceof BaseVertex );
			if (array_search ( $cur_adjacent_vertex, $this->_determined_vertex_set ) != false)
				continue;
			$distance = Graph::$DISCONNECTED;
			if (array_key_exists ( $vertex->get_id (), $this->_start_vertex_distance_index )) {
				$distance = $this->_start_vertex_distance_index [$vertex->get_id ()];
			}
			
			$distance += $is_source2sink ? $this->_graph->get_edge_weight ( $vertex, $cur_adjacent_vertex ) : $this->_graph->get_edge_weight ( $cur_adjacent_vertex, $vertex );
			
			if (! array_key_exists ( $cur_adjacent_vertex->get_id (), $this->_start_vertex_distance_index ) || $this->_start_vertex_distance_index [$cur_adjacent_vertex->get_id ()] > $distance) {
				
				$this->_start_vertex_distance_index [$cur_adjacent_vertex->get_id ()] = $distance;
				$this->_predecessor_index [$cur_adjacent_vertex->get_id ()] = $vertex;
				$cur_adjacent_vertex->set_weight ( $distance );
				$this->_vertex_candidate_queue->add ( $cur_adjacent_vertex );
			}
		}
	}
	public function get_shortest_path($source_vertex, $sink_vertex) {
		assert ( $source_vertex instanceof BaseVertex );
		assert ( $sink_vertex instanceof BaseVertex );
		$this->determine_shortest_paths ( $source_vertex, $sink_vertex, true );
		$vertex_list = array ();
		$weight = Graph::$DISCONNECTED;
		if (array_key_exists ( $sink_vertex->get_id (), $this->_start_vertex_distance_index ))
			$weight = $this->_start_vertex_distance_index [$sink_vertex->get_id ()];
		if ($weight != Graph::$DISCONNECTED) {
			$cur_vertex = $sink_vertex;
			do {
				$vertex_list [] = $cur_vertex;
				$cur_vertex = $this->_predecessor_index [$cur_vertex->get_id ()];
			} while ( $cur_vertex != null && $cur_vertex != $source_vertex );
			$vertex_list [] = $source_vertex;
			$vertex_list = array_reverse ( $vertex_list );
		}
		
		$path = new Path ( $vertex_list, $weight );
		return $path;
	}
	public function update_cost_forward($vertex) {
		assert ( $vertex instanceof BaseVertex );
		$cost = Graph::$DISCONNECTED;
		$adj_vertex_set = $this->_graph->get_adjacent_vertices ( $vertex );
		// var_dump($this->_start_vertex_distance_index);
		if (! array_key_exists ( $vertex->get_id (), $this->_start_vertex_distance_index )) {
			$this->_start_vertex_distance_index [$vertex->get_id ()] = Graph::$DISCONNECTED;
		}
		foreach ( $adj_vertex_set as $cur_vertex ) {
			assert ( $cur_vertex instanceof BaseVertex );
			$distance = Graph::$DISCONNECTED;
			if (array_key_exists ( $cur_vertex->get_id (), $this->_start_vertex_distance_index ))
				$distance = $this->_start_vertex_distance_index [$cur_vertex->get_id ()];
			
			$distance += $this->_graph->get_edge_weight ( $vertex, $cur_vertex );
			
			$cost_of_vertex = $this->_start_vertex_distance_index [$vertex->get_id ()];
			if ($cost_of_vertex > $distance) {
				$this->_start_vertex_distance_index [$vertex->get_id ()] = $distance;
				$this->_predecessor_index [$vertex->get_id ()] = $cur_vertex;
				$cost = $distance;
			}
		}
		
		$sub_path = null;
		if ($cost < Graph::$DISCONNECTED) {
			
			$sub_path = new Path ( array (), null );
			$sub_path->set_weight ( $cost );
			// $vertex_list=$sub_path->get_vertices();
			// $vertex_list[]=$vertex;
			$sub_path->add_vertex ( $vertex );
			$sel_vertex = $this->_predecessor_index [$vertex->get_id ()];
			while ( $sel_vertex != null ) {
				assert ( $sel_vertex instanceof BaseVertex );
				$sub_path->add_vertex ( $sel_vertex );
				// $sel_vertex=null;
				if (array_key_exists ( $sel_vertex->get_id (), $this->_predecessor_index ))
					$sel_vertex = $this->_predecessor_index [$sel_vertex->get_id ()];
				else
					$sel_vertex = null;
			}
		}
		return $sub_path;
	}
	public function correct_cost_backward($vertex) {
		$vertex_list = array ();
		$vertex_list [] = $vertex;
		while ( count ( $vertex_list ) != 0 ) {
			$cur_vertex = array_shift ( $vertex_list );
			assert ( $cur_vertex instanceof BaseVertex );
			$cost_of_cur_vertex = $this->_start_vertex_distance_index [$cur_vertex->get_id ()];
			$pre_vertex_set = $this->_graph->get_precedent_vertices ( $cur_vertex );
			foreach ( $pre_vertex_set as $pre_vertex ) {
				assert ( $pre_vertex instanceof BaseVertex );
				$cost_of_pre_vertex = Graph::$DISCONNECTED;
				if (array_key_exists ( $pre_vertex->get_id (), $this->_start_vertex_distance_index ))
					$cost_of_pre_vertex = $this->_start_vertex_distance_index [$pre_vertex->get_id ()];
				$fresh_cost = $cost_of_cur_vertex + $this->_graph->get_edge_weight ( $pre_vertex, $cur_vertex );
				if ($cost_of_pre_vertex > $fresh_cost) {
					$this->_start_vertex_distance_index [$pre_vertex->get_id ()] = $fresh_cost;
					$this->_predecessor_index [$pre_vertex->get_id ()] = $cur_vertex;
					$vertex_list [] = $pre_vertex;
				}
			}
		}
	}
	public function update_precdecessor_index($id, $vertex) {
		$this->_predecessor_index [$id] = $vertex;
	}
	public function update_start_vertex_distance_index($id, $distance) {
		$this->_start_vertex_distance_index [$id] = $distance;
	}
}
