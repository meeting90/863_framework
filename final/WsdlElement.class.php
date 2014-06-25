<?php
class WsdlElement{
	public $wf_attribute;
	
	public function __construct($xml,$ns=false){
		$root = $xml;
		$this->wf_attribute = $root->attributes();
		
		foreach($root->children($ns) as $child){
			$el_ch_name = $child->getName();
			echo $el_ch_name."<br/>";
			$this->{$el_ch_name} = new WsdlElement($child, $ns);
		}
	}
}
?>