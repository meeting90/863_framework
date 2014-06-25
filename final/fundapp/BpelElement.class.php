<?php
class BpelElement{
	public $wf_attribute;
	public function __construct($xml,$ns=false){
		$root = $xml;
		$this->wf_attribute = $root->attributes();
		
		foreach($root->children($ns) as $child){
			$el_ch_name = $child->getName();
			
			$this->{$el_ch_name} = new BpelElement($child, $ns);
		}
	}
	
	public function isServiceInvocation(){
		return isset($this->invoke);
	}
	
	public function isWorkflowProcess(){
		return isset($this->invoke) || isset($this->sequence) || isset($this->reply)
						|| isset($this->switch) || isset($this->otherwise) || isset($this->case)
						|| isset($this->if) || isset($this->else) || isset($this->elseif);
	}
}
?>