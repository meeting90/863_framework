<?php
require_once 'BaseElementWithWeight.php';
interface BaseVertex extends BaseElementWithWeight {
	function get_id(); // return int
	function set_weight($weight);
	function get_name(); // return vertex name
}
