<?php
  require_once "WsdlAnalyze.class.php";
  require_once "functions.php";
  
  class BpelNode {
    private $name;
    private $trust;
    private $childNodes;
    private $attributesList;
    private $value;
    
    public function __construct() {
      $this->childNodes = array();
      $this->attributesList = array();
      $this->trust = 0;
    }
    public function getName() { return $this->name; }
    public function getTrust() { return $this->trust; }
    public function setName($name)  { $this->name = $name; }
    public function setTrust($trust) { $this->trust = $trust; }
    public function appendChildNode($node) { $this->childNodes[] = $node; }
    public function appendAttribute($attr) { $this->attributesList[] = $attr; }
    public function getChildNodes(){ return $this->childNodes; }
    public function getAttributesList() { return $this->attributesList; }
    public function getValue(){ return $this->value; }
    public function setValue($val) { $this->value = $val; }
    
    public function strip($str){
      $index = strrpos($str, ":");
      if($index == false)
        return $str;
      return substr($str, $index+1);
    }
    
    public function pre_evaluate($workflow_name, $uid, $backService=true){
    	
      if($this->name == "invoke"){
      	//var_dump($this->attributesList); echo "<br/>";
        $partnerLink = '';
        $operation = '';
        foreach($this->attributesList as $attribute){
          if($attribute->getAttrName() == "partnerLink"){
            $partnerLink = $this->strip($attribute->getAttrValue());
          }
          if($attribute->getAttrName() == "operation"){
            $operation = $this->strip($attribute->getAttrValue());
          }
        }
        $bxd = new BpelXmlDes($workflow_name);
        $partnerLinkType = $bxd->getPartnerLinkType($partnerLink);
        //echo "$partnerLinkType<br/>";
        $service_name = showServiceName($workflow_name, strip($partnerLinkType));

        //echo $service_name."::<br/>";
				if($service_name !=''){
					$s = getServiceByName($service_name);
					
					$sid = $s->sid;
					
					if($backService)
						$this->trust = calculate_family_trust($uid, $sid);
					else
						$this->trust = calculate_trust($uid, $sid);
      	}
      }
    }
    public function evaluate($workflow_name, $uid, $pre_evaluate=true) {
      if($pre_evaluate)
      	$this->pre_evaluate($workflow_name, $uid);
      if($this->name == "switch"){  //embranchment
        $p = 1.0 / count($this->childNodes);
        $res = 0;
        foreach($this->childNodes as $child){
          $res = $res + $p * $child->evaluate($workflow_name, $uid);
        }
        return $res;
      } else if($this->name == "case" || $this->name == "otherwise"){
        $res = 1;
        foreach($this->childNodes as $child){
          $res = $res * $child->evaluate($workflow_name, $uid);
        }
        return $res;
      } else if($this->name == "invoke"){  //service
      	//var_dump( $this->attributesList ); echo "<br/>";
        return $this->trust;
        
      } else if($this->name == "reply"){
        return 1;
      } else if($this->name == "sequence"){
      	//var_dump( $this->childNodes ); echo "<br/>";
        $res = 1;
        foreach($this->childNodes as $child){
          $res = $res * $child->evaluate($workflow_name, $uid);
        }
        
        return $res;
      } else if($this->name == "if"){
      	$tmp = 0; $cnt = 0;
      	foreach($this->childNodes as $child){
      		if(($child->getName() != "condition") && ($child->getName() != "elseif") && ($child->getName() != "else")){
      			$r1 = ( $child->evaluate($workflow_name, $uid) ) / 2.0;
      			
      		} else if(($child->getName() == "elseif") || ($child->getName() == "else")){
      			//echo $child->getName()."<br/>";
      			$tmp += $child->evaluate($workflow_name, $uid);
      			$cnt ++;
      		}
      	}
      	if($cnt != 0){
      		//echo "$tmp::$cnt<br/>";
      		$r2 = $tmp / ( 2 * $cnt );
      	}
      	$res = $r1 + $r2;
      	return $res;
      } else if($this->name == "else"){
      	$res = 1;
        foreach($this->childNodes as $child){
          $res = $res * $child->evaluate($workflow_name, $uid);
        }
        
        return $res;
      	
      } else if($this->name == "while") {
      }
    }
    
    /*copy to a json node*/
    public function copy2JsonNode() {
      $jsonNode = new NodeJsonObj();
      
      $jsonNode->name = urlencode($this->name);
      $jsonNode->trust = $this->trust;
      $jsonNode->value = urlencode($this->value);
      foreach($this->attributesList as $attr){
        $jsonNode->attributesList[] = $attr->copy2JsonAttr();
      }
      foreach($this->childNodes as $child){
        $jsonNode->childNodes[] = $child->copy2JsonNode();
      }
      return $jsonNode;
    }
  }
  class NodeJsonObj {
    public $name;
    public $trust;
    public $childNodes;
    public $attributesList;
    public $value;
    
    public function __construct(){
      $this->childNodes = array();
      $this->attributesList = array();
    }
  };
?>