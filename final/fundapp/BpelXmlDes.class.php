<?php
require_once "BpelElement.class.php";
require_once "Model.class.php";
//$bxd = new BpelXmlDes("Travel");
// $bxd->getPartnerLinkType("weatherlink");

class BpelXmlDes{
	private $bpel_xml;
	public $xml_des;
	
	public function __construct($name){
		$m = new Model('workflow');
		$res = $m->queryByWorkflowname($name);
		$wf = $res[0];
		if($wf){
			$m = new Model('bpel_files');
			$res = $m->queryByFilename($wf->bpelname);
			$bpel = $res[0];
			$this->bpel_xml = simplexml_load_string(stripslashes($bpel->contents), 'SimpleXMLElement', LIBXML_NOCDATA);
			
			//$this->build();
		} else{
			die("workflow not found");
		}
	}
	
	public function build(){
		$res = $this->bpel_xml->getNamespaces(true); 
		$ns = $res['bpel'];
		
		$root = new BpelElement($this->bpel_xml, $ns);	
		$this->xml_des = $root;
		//var_dump($root->partnerLinks);
		return $root;
	}
		
	public function getPartnerLinkType($partnerLinkName){
		$res = $this->bpel_xml->getNamespaces(true); 
		$ns = $res['bpel'];
		
		foreach($this->bpel_xml->children($ns)->partnerLinks->children($ns) as $partnerLink){
			//print_r( $partnerLink->attributes() ); echo "<br/>";
			$attrs = $partnerLink->attributes();
			//echo $attrs['name']."<br/>";
			if($attrs['name'] == $partnerLinkName)
				return $attrs['partnerLinkType'];
		}
		return false;
	}
	public function getImportWsdls(){
		$res = $this->bpel_xml->getNamespaces(true); 
		$ns = $res['bpel'];
		
		$wsdllist = array();
		foreach($this->bpel_xml->children($ns) as $child){
			//echo $child->getName()."<br/>";
			if($child->getName() == 'import'){
				$attrs = $child->attributes();
				$wsdllist[] = $attrs['location'];
			}
		}
		return implode(":", $wsdllist);
	}
	}
?>