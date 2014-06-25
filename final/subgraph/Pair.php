<?php
class Pair {
	public $o1;
	public $o2;
	function __construct($object1, $object2) {
		$this->o1 = $object1;
		$this->o2 = $object2;
	}
	function first() {
		return $this->o1;
	}
	function second() {
		return $this->o2;
	}
	function equals(Object $o) {
		return $this == $o;
	}
	function tojson() {
		$json = array ();
		$json ['source'] = $this->o1;
		$json ['target'] = $this->o2;
		return json_encode ( $json );
	}
	function toString() {
		return '{Pair:' . $this->o1 . ',' . $this->o2 . '}';
	}
	function __toString() {
		$json = array ();
		$json ['source'] = $this->_o1;
		$json ['target'] = $this->_o2;
		$json ['weight'] = $this->_weight;
		return json_encode ( $json );
	}
}
