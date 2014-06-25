<?php
require_once 'BaseElementWithWeight.php';
class QYPriorityQueue {
	public $_element_weight_pair_list = array ();
	public $_limit_size = - 1;
	public $_is_incremental = false;
	// public function __construct(){}
	public function __construct($limit_size, $is_incremental) {
		if ($limit_size != null && $is_incremental != null) {
			$this->_limit_size = $limit_size;
			$this->_is_incremental = $is_incremental;
		}
	}
	public function toString() {
		return json_encode ( $this->_element_weight_pair_list );
	}
	private function _bin_locate_pos($weight, $is_incremental) {
		$mid = 0;
		$low = 0;
		$high = count ( $this->_element_weight_pair_list ) - 1;
		while ( $low <= $high ) {
			$mid = intval ( ($low + $high) / 2 );
			assert ( $this->_element_weight_pair_list [$mid] instanceof BaseElementWithWeight );
			if ($this->_element_weight_pair_list [$mid]->get_weight () == $weight) {
				return $mid + 1;
			}
			if ($is_incremental) {
				if ($this->_element_weight_pair_list [$mid]->get_weight () < $weight) {
					$high = $mid - 1;
				} else {
					$low = $mid + 1;
				}
			} else {
				if ($this->_element_weight_pair_list [$mid]->get_weight () > $weight) {
					$high = $mid - 1;
				} else {
					$low = $mid + 1;
				}
			}
		}
		return $low;
	}
	public function add($element) {
		$addelement = array ();
		assert ( $element instanceof BaseElementWithWeight );
		
		$addelement [] = $element;
		$index = $this->_bin_locate_pos ( $element->get_weight (), $this->_is_incremental );
		array_splice ( $this->_element_weight_pair_list, $index, 0, $addelement );
	}
	public function size() {
		return $this->count ( _element_weight_pair_list );
	}
	public function get($index) {
		if ($index >= count ( $this->_element_weight_pair_list )) {
			
			echo "The result :" + $i + " doesn't exist!!!";
			return false;
		}
		return $this->_element_weight_pair_list [$i];
	}
	public function poll() {
		if ($this->isEmpty ()) {
			return null;
		}
		$element = $this->_element_weight_pair_list [0];
		array_shift ( $this->_element_weight_pair_list );
		return $element;
	}
	public function isEmpty() {
		return count ( $this->_element_weight_pair_list ) == 0;
	}
}