<?php
require_once "WsdlElement.class.php";
require_once "Model.class.php";
//$wxd = new WsdlXmlDes("WeatherForecastService.wsdl");
//$pt = $wxd->getPortType("WeatherPLT");
//echo $wxd->getWsdl($pt);
//echo $wxd->getServiceName();
class WsdlXmlDes{
	private $wsdl_xml;
	
	public function __construct($filename){
		//echo "$filename<br/>";
		$m = new Model('wsdl_files');
		$res = $m->queryByFilename((string)$filename);
		if($res){
			$this->wsdl_xml = simplexml_load_string(stripslashes($res[0]->contents));
		} else {
			$this->wsdl_xml = false;
		}
	}
	
	public function checkPartnerLinkType($partnerLinkType){
		$root = $this->wsdl_xml;
		$res = $this->wsdl_xml->getNamespaces(true); 
		
		$ns = $res['plnk'];
		//var_dump( $root-> children($ns));
	}
	
	public function getPortType($partnerLinkType){
		$res = $this->wsdl_xml->getNamespaces(true); 
		
		$ns = $res['plnk'];
		foreach($this->wsdl_xml->children($ns) as $child){
			if( $child->getName() == "partnerLinkType"){
				$attrs = $child->attributes();
				if($attrs['name'] == $partnerLinkType){
					foreach($child->children($ns) as $role){
						$tmp = $role->attributes() ;
						$p = explode(":", $tmp['portType']);
						return $p[0];
					}
				}
			}
		}	
	}
	
	public function getWsdl($portType){
		$res = $this->wsdl_xml->getDocNamespaces(true); 
	  $imp_location = $res[$portType];
	  
	  foreach($this->wsdl_xml->children() as $child){
	  	if( $child->getName() == "import"){
	  		$attr = $child->attributes();
	  		if($attr['namespace'] == $imp_location){
	  			return $attr['location'];
	  		}
	  	}
	  }
	}
	
	public function getServiceName(){
		
		$ns = $this->wsdl_xml->getNamespaces(true);
		
		foreach($this->wsdl_xml->children($ns['wsdl']) as $child){
	  	if( $child->getName() == "service"){
	  		$attr = $child->attributes();
	  		return $attr['name'];
	  	}
	  }
	}
}
?>