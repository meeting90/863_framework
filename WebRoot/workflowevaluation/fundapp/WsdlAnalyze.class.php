<?php
require_once "Model.class.php";
require_once "WsdlXmlDes.class.php";
class WsdlAnalyze{
    private $filename;
    private $content;
    private $xml;
    
    public function __construct($filename){
      $this->filename = $filename;
      $wsdl_sql = new Model('wsdl_files');
      
      $tmp = $wsdl_sql->queryByFilename($filename);
//       var_dump($tmp);
      $this->content = $tmp[0]->contents;
      if($this->content == false)
        echo "query ".$filename." content failed <br/>";
      $this->xml = simplexml_load_string(stripslashes($this->content));
                    if($this->xml == false){
                          echo "parse error: ".$filename."</br>";
                    }
//      $this->xml = simplexml_load_file($filename);
//      print_r($this->xml->getNamespaces(true));
    }
    
    public function strip($str){
      $index = strrpos($str, ":");
      if($index == false)
        return $str;
      return substr($str, $index+1);
    }
    
    public function getServiceName($partnerLinkType){
    	//echo $this->filename;
    	$wxd = new WsdlXmlDes($this->filename);
    	//echo $partnerLinkType;
    	$pt = $wxd->getPortType($partnerLinkType);
    	//echo $pt;
			$wsdlname = $wxd->getWsdl($pt);
			//echo "$wsdlname<br/>";
			//var_dump($wsdlname == "WeatherForecastService.wsdl");
			$wxd1 = new WsdlXmlDes((string)$wsdlname);
			
			return $wxd1->getServiceName();
    }
  }
?>