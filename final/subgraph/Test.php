<?php
require_once 'Graph.php';
require_once 'YenTopKShortestPathsAlg.php'; 
require_once 'MoleTrust.php';
require_once 'PathEvolutionary.php';



$graph=new Graph("../1/subgraph.json",'json');

//$k_shortest_alg=new YenTopKShortestPath($graph,'graph', null, null);

//$mole=new MoleTrust($graph->get_matrix_graph());
//$ground=$mole->mole_trust(37, 87, 8);
//$graph_matrix=$k_shortest_alg->get_result_matrix_graph($graph->get_vertex(37), //$graph->get_vertex(87),20);
//$path_evo=new PathEvolutionary($graph,$graph_matrix, //$k_shortest_alg->get_result_list(), $ground);
//$path_evo->evo_alg(37, 87, 5);
//echo $path_evo->get_result_graph(37,87);



