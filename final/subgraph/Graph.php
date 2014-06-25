<?php
require_once 'Vertex.php';
require_once 'Pair.php';
require_once 'BaseGraph.php';
class Graph implements BaseGraph {
	public static $DISCONNECTED = 20000;
	protected $_fanout_vertices_index = array ();
	protected $_fanin_vertices_index = array ();
	protected $_vertex_pair_weight_index = array ();
	protected $_vertex_pair_trust_index = array ();
	protected $_id_vertex_index = array ();
	protected $_vertex_list = array ();
	protected $_vertex_num = 0;
	protected $_edge_num = 0;
	protected $_graph_name = 0;
	public function __construct($arg, $type) {
		$this->clear ();
		if (is_string ( $arg )) {
			if ($type === 0 || $type === 'dot') {
				assert ( is_string ( $arg ) );
				$this->_import_from_dot ( $arg );
			} else if ($type === 1 || $type === 'json') {
				assert ( is_string ( $arg ) );
				$this->_import_form_json ( $arg );
			}
		} else if ($arg instanceof BaseGraph) {
			$this->_graph_name = $arg->_graph_name;
			$this->_vertex_num = $arg->_vertex_num;
			$this->_edge_num = $arg->_edge_num;
			$this->_vertex_list = $arg->_vertex_list;
			$this->_id_vertex_index = $arg->_id_vertex_index;
			$this->_fanin_vertices_index = $arg->_fanin_vertices_index;
			$this->_fanout_vertices_index = $arg->_fanout_vertices_index;
			$this->_vertex_pair_weight_index = $arg->_vertex_pair_weight_index;
			$this->_vertex_pair_trust_index = $arg->_vertex_pair_trust_index;
		} else if (is_array ( $arg )) {
			
			$this->_convert_json ( $arg );
		}
	}
	private function _convert_json($json) {
		$nodes = $json ['nodes'];
		$edges = $json ['edges'];
		
		$map = array ();
		foreach ( $nodes as $node ) {
			$id = $node ['id'];
			
			$vertex = new Vertex ( $node ['name'] );
			$this->_vertex_list [] = $vertex;
			$this->_id_vertex_index [$vertex->get_id ()] = $vertex;
			if (! array_key_exists ( $id, $map )) {
				$map [$id] = $vertex->get_id ();
			}
		}
		foreach ( $edges as $edge ) {
			$source = $edge ['source'];
			$target = $edge ['target'];
			$trust = $edge ['weight'];
			if (array_key_exists ( $source, $map ) && array_key_exists ( $target, $map )) {
				$source_id = $map [$source];
				$target_id = $map [$target];
				$this->addEdge ( $source_id, $target_id, $trust );
			}
		}
	}
	private function _import_form_json($file_name) {
		$content = file_get_contents ( $file_name );
		
		$json = json_decode ( $content, true );
		
		$this->_convert_json ( $json );
	}
	private function _import_from_dot($file_name) {
		$fp = fopen ( $file_name, 'r' );
		fgets ( $fp );
		$result = array ();
		$path_parts = pathinfo ( $file_name );
		$this->_graph_name = $path_parts ['filename'];
		while ( ! feof ( $fp ) ) {
			$nextline = fgets ( $fp );
			if (strrpos ( $nextline, ';' ) === false)
				continue;
			$words = substr ( $nextline, 0, - 2 );
			
			$words = preg_split ( '[ ]', $words );
			
			$start_id = 0;
			$end_id = 0;
			$amount = 0;
			
			if (count ( $words ) == 4) {
				$name = trim ( $words [0] );
				
				if (array_key_exists ( $name, $result ) == 0) {
					
					$vertex = new Vertex ( $name );
					$this->_vertex_list [] = $vertex;
					$this->_id_vertex_index [$vertex->get_id ()] = $vertex;
					$start_id = $vertex->get_id ();
					$result [$name] = $vertex->get_id ();
				} else {
					
					$start_id = $result [$name];
				}
				
				$name = trim ( $words [2] );
				
				if (array_key_exists ( $name, $result ) == 0) {
					$vertex = new Vertex ( $name );
					$this->_vertex_list [] = $vertex;
					$this->_id_vertex_index [$vertex->get_id ()] = $vertex;
					$end_id = $vertex->get_id ();
					$result [$name] = $vertex->get_id ();
				} else {
					$end_id = $result [$name];
				}
				
				$trust = 0;
				if (strrpos ( $words [3], 'Observer' ) !== false)
					$trust = 0.1;
				else if (strrpos ( $words [3], 'Apprentice' ) !== false)
					$trust = 0.4;
				else if (strrpos ( $words [3], 'Journeyer' ) !== false)
					$trust = 0.7;
				else if (strrpos ( $words [3], 'Master' ) !== false)
					$trust = 0.9;
				
				$this->addEdge ( $start_id, $end_id, $trust );
			}
		}
		// var_dump($this->_vertex_pair_weight_index);
		fclose ( $fp );
	}
	public function clear() {
		Vertex::reset ();
		$this->_vertex_num = 0;
		$this->_edge_num = 0;
		unset ( $this->_vertex_list );
		unset ( $this->_id_vertex_index );
		unset ( $this->_fanin_vertices_index );
		unset ( $this->_fanout_vertices_index );
		unset ( $this->_vertex_pair_weight_index );
		$this->_vertex_list = array ();
		$this->_id_vertex_index = array ();
		$this->_fanin_vertices_index = array ();
		$this->_fanout_vertices_index = array ();
		$this->_vertex_pair_weight_index = array ();
	}
	public function addEdge($start_vertex_id, $end_vertex_id, $trust) {
		if (! array_key_exists ( $start_vertex_id, $this->_id_vertex_index ) || ! array_key_exists ( $end_vertex_id, $this->_id_vertex_index ) || $start_vertex_id == $end_vertex_id) {
			// throw new Exception("The edge from "+$start_vertex_id
			// +" to "+$end_vertex_id+" does not exist in the graph.");
			
			return;
		}
		$fanout_vertex_set = array ();
		if (array_key_exists ( $start_vertex_id, $this->_fanout_vertices_index )) {
			$fanout_vertex_set = $this->_fanout_vertices_index [$start_vertex_id];
		}
		$fanout_vertex_set [] = $this->_id_vertex_index [$end_vertex_id];
		
		$this->_fanout_vertices_index [$start_vertex_id] = $fanout_vertex_set;
		
		$fanin_vertex_set = array ();
		if (array_key_exists ( $end_vertex_id, $this->_fanin_vertices_index )) {
			$fanin_vertex_set = $this->_fanin_vertices_index [$end_vertex_id];
		}
		$fanin_vertex_set [] = $this->_id_vertex_index [$start_vertex_id];
		
		$this->_fanin_vertices_index [$end_vertex_id] = $fanin_vertex_set;
		
		$pair = new Pair ( $start_vertex_id, $end_vertex_id );
		$this->_vertex_pair_trust_index [$pair->tojson ()] = $trust;
		$this->_vertex_pair_weight_index [$pair->tojson ()] = - log ( $trust );
		++ $this->_edge_num;
	}
	public function get_edge_weight($source, $sink) {
		assert ( $source instanceof BaseVertex && $sink instanceof BaseVertex );
		$pair = new Pair ( $source->get_id (), $sink->get_id () );
		
		if (array_key_exists ( $pair->tojson (), $this->_vertex_pair_weight_index ))
			return $this->_vertex_pair_weight_index [$pair->tojson ()];
		else
			return self::$DISCONNECTED;
	}
	public function get_edge_trust($source, $sink) {
		assert ( $source instanceof BaseVertex && $sink instanceof BaseVertex );
		$pair = new Pair ( $source->get_id (), $sink->get_id () );
		
		if (array_key_exists ( $pair->tojson (), $this->_vertex_pair_weight_index ))
			return $this->_vertex_pair_trust_index [$pair->tojson ()];
		else
			return 0;
	}
	public function get_adjacent_vertices($vertex) {
		assert ( $vertex instanceof BaseVertex );
		if (array_key_exists ( $vertex->get_id (), $this->_fanout_vertices_index )) {
			
			return $this->_fanout_vertices_index [$vertex->get_id ()];
		} else
			return array ();
	}
	public function get_precedent_vertices($vertex) {
		assert ( $vertex instanceof BaseVertex );
		if (array_key_exists ( $vertex->get_id (), $this->_fanin_vertices_index )) {
			
			return $this->_fanin_vertices_index [$vertex->get_id ()];
		} else
			return array ();
	}
	public function set_vertex_num($num) {
		$this->_vertex_num = $num;
	}
	public function get_vertex_list() {
		return $this->_vertex_list;
	}
	public function get_vertex($id) {
		return $this->_id_vertex_index [$id];
	}
	public function tojsonarray() {
		$json = array ();
		$nodes = array ();
		$edges = array ();
		foreach ( $this->_vertex_list as $vertex ) {
			assert ( $vertex instanceof Vertex );
			$nodes [] = $vertex->tojsonarray ();
		}
		foreach ( array_keys ( $this->_vertex_pair_weight_index ) as $edgekey ) {
			$index = $edgekey;
			$edgejson = json_decode ( $edgekey, true );
			
			$edgejson ['weight'] = $this->_vertex_pair_trust_index [$index];
			$edges [] = $edgejson;
		}
		
		$json ['nodes'] = $nodes;
		$json ['edges'] = $edges;
		
		return $json;
	}
	public function get_matrix_graph() {
		$vertex_list = array ();
		foreach ( $this->_vertex_list as $vertex ) {
			assert ( $vertex instanceof BaseVertex );
			$adj_vertex_list = $this->get_adjacent_vertices ( $vertex );
			foreach ( $adj_vertex_list as $adj_vertex ) {
				assert ( $adj_vertex instanceof BaseVertex );
				$vertex_list [$vertex->get_id ()] [$adj_vertex->get_id ()] = $this->get_edge_trust ( $vertex, $adj_vertex );
			}
		}
		
		return $vertex_list;
	}
}

