<?php
class MoleTrust {
	private $_graph_matrix;
	private $_user_list = array ();
	public function __construct($matrix) {
		$this->_graph_matrix = $matrix;
	}
	public function mole_trust($source_id, $target_id, $hop) {
		$this->_user_list = $this->_step1 ( $source_id, $target_id, $hop );
		// var_dump($this->_user_list);
		$trust = $this->_step2 ( $source_id, $target_id, $hop );
		return $trust;
	}
	// bfs of $graph
	private function _step1($source_id, $target_id, $hop) {
		$dist = 0;
		$user_list = array ();
		$visited = array ();
		$user = array ();
		$user [] = $source_id;
		$user_list [] = $user;
		$visited [$source_id] = true;
		while ( $dist < $hop ) {
			$dist ++;
			$user = array ();
			$cur_user = $user_list [$dist - 1];
			foreach ( $cur_user as $source ) {
				if (! isset ( $this->_graph_matrix [$source] ))
					continue;
				foreach ( array_keys ( $this->_graph_matrix [$source] ) as $target ) {
					if (! isset ( $visited [$target] )) {
						$user [] = $target;
						$visited [$target] = true;
					}
				}
			}
			$user_list [] = $user;
		}
		
		return $user_list;
	}
	// update trust
	private function _step2($source_id, $target_id, $hop) {
		$dist = 0;
		$trust = array ();
		$trust [$source_id] = 1.0;
		while ( $dist < $hop ) {
			$dist ++;
			$user = $this->_user_list [$dist];
			foreach ( $user as $cur_id ) {
				$child = 0.0;
				$parent = 0.0;
				$pre_user = $this->_user_list [$dist - 1];
				foreach ( $pre_user as $pre_id ) {
					if (isset ( $this->_graph_matrix [$pre_id] [$cur_id] ) && $trust [$pre_id] >= 0.6) {
						$child += $trust [$pre_id] * $this->_graph_matrix [$pre_id] [$cur_id];
						$parent += $trust [$pre_id];
					}
				}
				if ($parent == 0.0) {
					$trust [$cur_id] = 0.0;
				} else {
					$trust [$cur_id] = $child / $parent;
				}
			}
		}
		if (isset ( $trust [$target_id] )) {
			return $trust [$target_id];
		} else
			return 0;
	}
}

/*

	public double moleTrust(double[][] matrix, int source, int target, int hop) {
		ArrayList<ArrayList<Integer>> users = step1(matrix, source, target, hop);
		return step2(matrix, users, source, target, hop);
	}
 * 
 */