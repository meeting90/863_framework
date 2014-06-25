<?php
require_once 'Model.class.php';
require_once 'subgraph/MoleTrust.php';
require_once 'subgraph/PathEvolutionary.php';
class GraphUtil {
	public static function uploadfile($path) {
		$name = '';
		$fileType = '';
		$graph_name = '';
		$error = '';
		$msg = '';
		$json ['fullpath'] = $path . $name . $fileType;
		$json ['dir'] = $path;
		$json ['name'] = $name;
		$json ['type'] = $fileType;
		$json ['graph_name'] = $graph_name;
		$json ['error'] = $error;
		$json ['msg'] = $msg;
		return $json;
	}
	public static function insertRecord($uid, $graph_name, $graph, $json) {
		$m = new Model ( 'graph' );
		$data = array (
				array (
						'userid' => $uid,
						'graphname' => $graph_name,
						'graph' => $graph,
						'json' => $json 
				) 
		);
		$m->add ( $data );
	}
	public static function savejson($path, $name, $json) {
		$file = $path . $name . '.json';
		
		file_put_contents ( $file, $json );
		return $file;
	}
	public static function getgraphlist($uid) {
		$eps = new Model ( 'graph' );
		$res = $eps->queryByUserid ( $uid );
		
		if ($res) {
			return $res;
		}
		return array ();
	}
	public static function getgraphByid($graph_id) {
		$eps = new Model ( 'graph' );
		$res = $eps->queryByGraphid ( $graph_id );
		if ($res) {
			return $res [0];
		}
		return array ();
	}
	public static function getsubgraph($graphid, $source, $target, $k) {
		$eps = new Model ( 'graph' );
		$res = $eps->queryByGraphid ( $graphid );
		if ($res) {
			$graphfile = $res [0]->graph;
			$path_info = pathinfo ( $graphfile );
			
			$graph = new Graph ( $graphfile, $path_info ['extension'] );
			
			$k_shortest_alg = new YenTopKShortestPath ( $graph, 'graph', null, null );
			$json = $k_shortest_alg->get_result_graph ( $graph->get_vertex ( $source ), $graph->get_vertex ( $target ), $k );
			
			$new_graph = new Graph ( $json, 'jsonobject' );
			return $new_graph->tojsonarray ();
		}
		return array ();
	}
	public static function getevograph($graphid, $source, $target, $k, $pathnum) {
		$eps = new Model ( 'graph' );
		$res = $eps->queryByGraphid ( $graphid );
		if ($res) {
			$graphfile = $res [0]->graph;
			$path_info = pathinfo ( $graphfile );
			$graph = new Graph ( $graphfile, $path_info ['extension'] );
			$k_shortest_alg = new YenTopKShortestPath ( $graph, 'graph', null, null );
			
			$mole = new MoleTrust ( $graph->get_matrix_graph () );
			$ground = $mole->mole_trust ( $source, $target, 8 );
			$graph_matrix = $k_shortest_alg->get_result_matrix_graph ( $graph->get_vertex ( $source ), $graph->get_vertex ( $target ), $k );
			$path_evo = new PathEvolutionary ( $graph, $graph_matrix, $k_shortest_alg->get_result_list (), $ground );
			$path_evo->evo_alg ( $source, $target, $pathnum );
			return $path_evo->get_result_graph ( $source, $target );
		}
		return array ();
	}
	public static function checkworkid($workid) {
		// check wordid if exists
		return false;
	}
	public static function getmsgfrommysql($uid, $historyid, $workid) {
		// get data from database
		return "";
	}
	public static function deleteworkfrommysql($uid, $historyid, $workid) {
		// delete work
		return true;
	}
	public static function recordworkid($uid, $workid) {
		// record wordid
	}
}
