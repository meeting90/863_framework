<?php
  class AttributeBpel {
    private $attributeName;
    private $attributeValue;
      
    function setAttrName($name) { $this->attributeName = $name; }
    function setAttrValue($value) { $this->attributeValue = $value; }
    function getAttrName() { return $this->attributeName; }
    function getAttrValue() { return $this->attributeValue; }
    
    function trans2JSon(){
//      echo $this->attributeName . " " . $this->attributeValue . "<br />";
      $jsonObj = array();
//      $jsonObj[] = $this->attributeName;
//      $jsonObj[] = $this->attributeValue;
      $jsonObj[$this->attributeName] = $this->attributeValue;  
//      print_r($jsonObj);
      return json_encode($jsonObj);
    }
    public function copy2JsonAttr(){
      /*
      $json = array();
      $json[$this->attributeName] = $this->attributeValue;
      return $json;
      */
      
      $jsonAttr = new AttrJSonObj();
      $jsonAttr->attributeName = urlencode($this->attributeName);
      $jsonAttr->attributeValue = urlencode($this->attributeValue);
      return $jsonAttr;
      
    }
  }
  class AttrJSonObj{
    public $attributeName;
    public $attributeValue;
  };
  
?>