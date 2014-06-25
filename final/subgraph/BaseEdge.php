<?php
interface BaseEdge {
	public function get_weight(); // return int
	public function get_start_vertex(); // return Vertex
	public function get_end_vertex(); // return Vertex
}
