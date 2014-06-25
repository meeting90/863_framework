<?php
require_once 'BaseElementWithWeight.php';
class Path implements BaseElementWithWeight {
	private $_vertex_list = array ();
	private $_weight = - 1;
	private $_id = 0;
	private static $CURRENT_PATH_NUM = 0;
	public function __construct($list, $weight) {
		$this->_id = self::$CURRENT_PATH_NUM;
		self::$CURRENT_PATH_NUM ++;
		$this->_vertex_list = $list;
		$this->_weight = $weight;
	}
	public function get_id() {
		return $this->_id;
	}
	public function get_weight() {
		return $this->_weight;
	}
	public function set_weight($weight) {
		return $this->_weight = $weight;
	}
	public function get_vertices() {
		return $this->_vertex_list;
	}
	public function clear_verties() {
		unset ( $this->_vertex_list );
		$this->_vertex_list = array ();
	}
	public function add_verties($verties) {
		$this->_vertex_list = array_merge ( $this->_vertex_list, $verties );
	}
	public function add_vertex($vertex) {
		$this->_vertex_list [] = $vertex;
	}
	public function tojsonarray() {
		$jsondata = array ();
		$jsondata ['weight'] = $this->_weight;
		
		$jsondata ['list'] = array ();
		foreach ( $this->_vertex_list as $vertex ) {
			assert ( $vertex instanceof Vertex );
			$jsondata ['list'] [] = $vertex->get_id ();
		}
		return $jsondata;
	}
	public static function reset() {
		self::$CURRENT_PATH_NUM = 0;
	}
}
