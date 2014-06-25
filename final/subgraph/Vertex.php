<?php
require_once 'BaseVetex.php';
class Vertex implements BaseVertex {
	private static $CURRENT_VERTEX_NUM = 0;
	private $_id = 0;
	private $_weight = 0;
	private $_name = null;
	private $_trust = 0;
	public function __construct($name) {
		$this->_id = self::$CURRENT_VERTEX_NUM;
		self::$CURRENT_VERTEX_NUM ++;
		$this->_name = $name;
	}
	public function get_id() {
		return $this->_id;
	}
	public function get_weight() {
		return $this->_weight;
	}
	public function set_weight($weight) {
		$this->_weight = $weight;
	}
	public function get_name() {
		return $this->_name;
	}
	public function comareTo($r_vertex) {
		$diff = $this->_weight - $r_vertex . get_weght ();
		if ($diff > 0)
			return 1;
		else if (diff < 0)
			return - 1;
		else
			return 0;
	}
	public static function reset() {
		self::$CURRENT_VERTEX_NUM = 0;
	}
	public function tojsonarray() {
		$json = array ();
		$json ['id'] = $this->_id;
		$json ['name'] = $this->_name;
		$json ['weight'] = $this->_weight;
		return $json;
	}
	public function __toString() {
		$json = array ();
		$json ['id'] = $this->_id;
		$json ['name'] = $this->_name;
		$json ['weight'] = $this->_weight;
		return json_encode ( $json );
	}
}