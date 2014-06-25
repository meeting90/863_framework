<?php
require_once 'Graph.php';
require_once 'Path.php';
require_once 'MoleTrust.php';
class PathEvolutionary {
	private $_graph;
	private $_graph_matrix;
	private $_path_list;
	private $_evo_m = 20;
	private $_evo_it = 100;
	private $_ground;
	private $_edge_num;
	private $_result_id_map = array ();
	private $_id_count = 0;
	private $_result_graph;
	public function __construct($graph, $graph_matrix, $path_list, $ground) {
		assert ( $graph instanceof Graph );	
		$this->_graph = $graph;
		$this->_graph_matrix = $graph_matrix;
		$this->_path_list = $path_list;
		$this->_ground = $ground;
	}
	public function evo_alg($source_id, $target_id, $edge_num) {
		$path_size = count ( $this->_path_list );
		$vectors = array ();
		for($i = 0; $i < $this->_evo_m; $i ++) {
			$size = 0;
			for($j = 0; $j < $path_size; $j ++) {
				while ( $size < $edge_num ) {
					$pos = rand ( 0, $path_size );
					if (! isset ( $vectors [$i] [$pos] )) {
						$vectors [$i] [$pos] = true;
						$size ++;
					}
				}
			}
		}
		$vector_mutation = array ();
		$mut = 1.0 / $path_size;
		$it = 0;
		while ( $it < $this->_evo_it ) {
			for($i = 0; $i < $this->_evo_m; $i ++) {
				$size = 0;
				for($j = 0; $j < $path_size; $j ++) {
					if (rand () / getrandmax () < $mut) {
						if (isset ( $vectors [$i] [$j] )) {
							unset ( $vector_mutation [$i] [$j] );
							$size --;
						} else {
							$vector_mutation [$i] [$j] = true;
							$size ++;
						}
					} else {
						if (isset ( $vectors [$i] [$j] )) {
							$vectors_mutation [$i] [$j] = $vectors [$i] [$j];
						} else {
							unset ( $vector_mutation [$i] [$j] );
						}
					}
				}
				if ($size > 0) {
					$i --;
				}
			}
			$vectors = $this->_f_mole ( $source_id, $target_id, $vectors, $vector_mutation );
			$it ++;
		}
		$len = count ( $this->_graph_matrix );
		$new_graph_matrix = array ();
		for($i = 0; $i < $path_size; $i ++) {
			if (isset ( $vectors [0] [$i] )) {
				$cur_path = $this->_path_list [$i];
				assert ( $cur_path instanceof Path );
				$vertex_list = $cur_path->get_vertices ();
				
				for($j = 0; $j < count ( $vertex_list ) - 1; $j ++) {
					assert ( $vertex_list [$j] instanceof BaseVertex );
					assert ( $vertex_list [$j + 1] instanceof BaseVertex );
					$start = $vertex_list [$j]->get_id ();
					$end = $vertex_list [$j + 1]->get_id ();
					$new_graph_matrix [$start] [$end] = $this->_graph_matrix [$start] [$end];
				}
			}
		}
		$this->_result_graph = $new_graph_matrix;
		$mole = new MoleTrust ( $new_graph_matrix );
		return $mole->mole_trust ( $source_id, $target_id, 8 );
	}
	private function _add_node($id) {
		$node = array ();
		if (isset ( $this->_result_id_map [$id] )) {
			$new_id = $this->_result_id_map [$id];
			return false;
		} else {
			$start = $this->_id_count ++;
			$this->_result_id_map [$id] = $start;
			
			$vertex = $this->_graph->get_vertex ( $id );
			assert ( $vertex instanceof Vertex );
			$node ['id'] = $start;
			$node ['name'] = $vertex->get_name ();
			$node ['weight'] = 0;
			return $node;
		}
	}
	public function get_result_graph($source_id, $target_id) {
		$json = array ();
		$map = array ();
		$nodes = array ();
		$edges = array ();
		$id_count = 0;
		
		$nodes [] = $this->_add_node ( $source_id );
		$nodes [] = $this->_add_node ( $target_id );
		foreach ( array_keys ( $this->_result_graph ) as $source ) {
			foreach ( array_keys ( $this->_result_graph [$source] ) as $target ) {
				$node = array ();
				$edge = array ();
				$ret = $this->_add_node ( $source );
				if (is_array ( $ret )) {
					$nodes [] = $ret;
				}
				$ret = $this->_add_node ( $target );
				if (is_array ( $ret )) {
					$nodes [] = $ret;
				}
				$edge ['source'] = $this->_result_id_map [$source];
				$edge ['target'] = $this->_result_id_map [$target];
				$edge ['weight'] = $this->_result_graph [$source] [$target];
				$edges [] = $edge;
			}
		}
		$json ['nodes'] = $nodes;
		$json ['edges'] = $edges;
		return $json;
	}
	private function _f_mole($source, $target, $vectors, $vectors_mutation) {
		$queue = array ();
		$matrix_size = count ( $this->_graph_matrix );
		$path_size = count ( $this->_path_list );
		
		for($k = 0; $k < $matrix_size * 2; $k ++) {
			$ret = array ();
			
			if ($k < $matrix_size) {
				for($i = 0; $i < $path_size; $i ++) {
					if (isset ( $vectors [$k] [$i] )) {
						$cur_path = $this->_path_list [$i];
						assert ( $cur_path instanceof Path );
						$vertex_list = $cur_path->get_vertices ();
						
						for($j = 0; $j < count ( $vertex_list ) - 1; $j ++) {
							assert ( $vertex_list [$j] instanceof BaseVertex );
							assert ( $vertex_list [$j + 1] instanceof BaseVertex );
							$start = $vertex_list [$j]->get_id ();
							$end = $vertex_list [$j + 1]->get_id ();
							$ret [$start] [$end] = $this->_graph_matrix [$start] [$end];
						}
					}
				}
			} else {
				for($i = 0; $i < $path_size; $i ++) {
					if (isset ( $vectors_mutation [$k - $matrix_size] [$i] )) {
						$cur_path = $this->_path_list [$i];
						assert ( $cur_path instanceof Path );
						$vertex_list = $cur_path->get_vertices ();
						
						for($j = 0; $j < count ( $vertex_list ) - 1; $j ++) {
							assert ( $vertex_list [$j] instanceof BaseVertex );
							assert ( $vertex_list [$j + 1] instanceof BaseVertex );
							$start = $vertex_list [$j]->get_id ();
							$end = $vertex_list [$j + 1]->get_id ();
							$ret [$start] [$end] = $this->_graph_matrix [$start] [$end];
						}
					}
				}
			}
			$mole = new MoleTrust ( $ret );
			$trust = $mole->mole_trust ( $source, $target, 8 );
			$queue [$k] = abs ( $trust - $this->_ground );
			sort ( $queue );
			$retVectors = array ();
			$key_queue = array_keys ( $queue );
			for($i = 0; $i < $matrix_size; $i ++) {
				$key = array_shift ( $key_queue );
				for($j = 0; $j < $path_size; $j ++) {
					if ($key < $matrix_size) {
						if (isset ( $vectors [$key] [$j] )) {
							$retVectors [$i] [$j] = $vectors [$key] [$j];
							;
						}
					} else {
						if (isset ( $vectors_mutation [$key - $matrix_size] [$j] )) {
							$retVectors [$i] [$j] = $vectors_mutation [$key - $matrix_size] [$j];
						}
					}
				}
			}
		}
		return $retVectors;
	}
}
