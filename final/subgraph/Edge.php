<?php
require_once 'BaseEdge.php';
class Edge implements BaseEdge {
	private $_source;
	private $_target;
	private $_weight;
	public function __construct($source_vt, $target_vt, $weight) {
		$this->_source = $source_vt;
		$this->_target = $target_vt;
		$this->_weight = $weight;
	}
	public function get_end_vertex() {
		return $this->_source;
	}
	public function get_start_vertex() {
		return $this->_target;
	}
	public function get_weight() {
		return $this->_weight;
	}
	public function __toString() {
		$json = array ();
		$json ['source'] = $this->_source;
		$json ['target'] = $this->_target;
		$json ['weight'] = $this->_weight;
		return json_encode ( $json );
	}
	public function tojsonarray() {
		$json = array ();
		$json ['source'] = $this->_source;
		$json ['target'] = $this->_target;
		$json ['weight'] = $this->_weight;
		return $json;
	}
}