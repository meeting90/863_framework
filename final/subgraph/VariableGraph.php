<?php
require_once 'Graph.php';
class VariableGraph extends Graph {
	private $_rem_vertex_id_set = array ();
	private $_rem_edge_set = array ();
	public function __construct($arg, $type) {
		parent::__construct ( $arg, $type );
		$this->_rem_vertex_id_set = array ();
		$this->_rem_edge_set = array ();
	}
	public function set_rem_vetex_id_set($set) {
		$this->_rem_vertex_id_set = $set;
	}
	public function set_rem_edge_set($set) {
		$this->_rem_edge_set = $set;
	}
	public function remove_edge($edge) {
		assert ( $edge instanceof Pair );
		$this->_rem_edge_set [$edge->tojson ()] = true;
	}
	public function remove_vertex($vertex_id) {
		// assert($vertex instanceof Integer);
		$this->_rem_vertex_id_set [$vertex_id] = true;
	}
	public function recover_removed_edges() {
		unset ( $this->_rem_edge_set );
		$this->_rem_edge_set = array ();
	}
	public function recover_removed_edge($edge) {
		assert ( $edge instanceof Pair );
		if (array_key_exists ( $edge->tojson (), $this->_rem_edge_set )) {
			unset ( $this->_rem_edge_set [$edge->tojson ()] );
		}
	}
	public function recover_removed_vetices() {
		unset ( $this->_rem_vertex_id_set );
		$this->_rem_vertex_id_set = array ();
	}
	public function recover_removed_vertix($vertex_id) {
		if (array_key_exists ( $vertex_id, $this->_rem_vertex_id_set )) {
			unset ( $this->_rem_vertex_id_set [$vertex_id] );
		}
	}
	public function get_edge_weight($source, $sink) {
		assert ( $source instanceof BaseVertex );
		assert ( $sink instanceof BaseVertex );
		$source_id = $source->get_id ();
		$sink_id = $sink->get_id ();
		$pair = new Pair ( $source_id, $sink_id );
		if (array_key_exists ( $source_id, $this->_rem_vertex_id_set ) || array_key_exists ( $sink_id, $this->_rem_vertex_id_set ) || array_key_exists ( $pair->tojson (), $this->_rem_edge_set )) {
			return Graph::$DISCONNECTED;
		}
		return parent::get_edge_weight ( $source, $sink );
	}
	public function get_edge_weight_of_graph($source, $sink) {
		return parent::get_edge_weight ( $source, $sink );
	}
	public function get_adjacent_vertices($vertex) {
		assert ( $vertex instanceof BaseVertex );
		$ret_set = array ();
		$starting_vertex_id = $vertex->get_id ();		
		if (! array_key_exists ( $starting_vertex_id, $this->_rem_vertex_id_set )) {	
			$adj_vertex_set = parent::get_adjacent_vertices ( $vertex );
			foreach ( $adj_vertex_set as $cur_vertex ) {
				assert ( $cur_vertex instanceof BaseVertex );
				$ending_vertex_id = $cur_vertex->get_id ();
				$pair = new Pair ( $starting_vertex_id, $ending_vertex_id );
				if (array_key_exists ( $ending_vertex_id, $this->_rem_vertex_id_set ) || array_key_exists ( $pair->tojson (), $this->_rem_edge_set ))
					continue;
				$ret_set [] = $cur_vertex;
			}
		}		
		return $ret_set;
	}
	public function get_precedent_vertices($vertex) {
		assert ( $vertex instanceof BaseVertex );
		$ret_set = array ();
		$starting_vertex_id = $vertex->get_id ();	
		if (! array_key_exists ( $starting_vertex_id, $this->_rem_vertex_id_set )) {
			$pre_vertex_set = parent::get_precedent_vertices ( $vertex );
			foreach ( $pre_vertex_set as $cur_vertex ) {
				assert ( $cur_vertex instanceof BaseVertex );
				$ending_vertex_id = $cur_vertex->get_id ();
				$pair = new Pair ( $starting_vertex_id, $ending_vertex_id );
				if (array_key_exists ( $ending_vertex_id, $this->_rem_vertex_id_set ) || array_key_exists ( $pair->tojson (), $this->_rem_edge_set ))
					continue;
				$ret_set [] = $cur_vertex;
			}
		}
		return $ret_set;
	}
	public function get_vertex_list() {
		$ret_set = array ();
		foreach ( parent::get_vertex_list () as $cur_vertex ) {
			assert ( $cur_vertex instanceof BaseVertex );
			if (array_key_exists ( $cur_vertex->get_id (), $this->_rem_vertex_id_set ))
				continue;
			$ret_set [] = $cur_vertex;
		}
		return $ret_set;
	}
	public function get_vertex($id) {
		if (array_key_exists ( $id, $this->_rem_vertex_id_set ))
			return null;
		return parent::get_vertex ( $id );
	}
}