<?php
/*  
  $expr = array(12,16,24,25,22,34,25);
  $alpha = 0.7;
  $forcast = new TrustForcast($expr, $alpha);
  $forcast->forcast(10, 30);
*/
  class TrustForcast{
    private $exprements; //the exprements trust array
    private $alpha;
    
    public function __construct($expr, $alpha){
      $this->exprements = $expr;
      $this->alpha = $alpha;
    }
    public function getNum(){
      return count($this->exprements);
    }
    private function getIntervalNum($start, $end){
      $cnt = 0;
      foreach($this->exprements as $key => $value){
        if($value < $end && $value > $start){
          $cnt ++;
        }
      }
      return $cnt;
    }
    private function combination($m, $n) {
      if($m <= $n)
        return 1;
      $a = 1; 
      $b = 1;
      for($i=1; $i<=$n; $i++){
        $a = $a * $i;
      }
      for($i=$m-$n+1; $i<=$m; $i++){
        $b = $b * $i;
      }
      $res = $b / $a;
//      echo "combination of ". $m . " and " . $n . " is: " . $res . "<br/>";
      return $res;
    }
    public function forcast($start, $end){
      $mw = $this->getIntervalNum($start, $end);
//      $nw = $this->getNum() - $mw;
      $Vwp = 0;
      for($l=1; $l<=$mw; $l++){
        $Vwp = $Vwp + 
          $this->combination($this->getNum(), $l) * pow($this->alpha, $l) * pow((1 - $this->alpha), ($this->getNum() - $l));
      }
//      echo $Vwp . "<br/>";
      return $Vwp;
    }
  }
  
  class ServiceTask{
    public $name;  //the task's name
    public $trust;  //the task's trust
  }
?>