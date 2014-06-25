<?php
class DbMysql {
  private $config;
  private $queryID; 
  private $numRows;
  private $lastInsID;
  private $linkID;
  
  function __construct($config){
    if(!empty($config))
      $this->config = $config;
    
  }
  
  public function connect(){
    $conn = mysql_connect($this->config['host'], $this->config['username'], $this->config['password']);
    if(empty($conn)){
      die("connect error: ".mysql_error());
      return false;
    }
    $db_select = mysql_select_db($this->config['dbname'], $conn);
    
    if($db_select == false){
      die("database selection error: ".mysql_error());
      return false;
    }
    return $conn;
  }
  
  public function query($str){
    $this->linkID = $this->connect();
    if(false == $this->linkID){
      die( "connect failed" );
      return false;
    }
    if($this->queryID)
      mysql_free_result($this->queryID);
    mysql_query("set names utf8");
    
    $this->queryID = mysql_query($str);
//     var_dump($this->queryID);
    if(false == $this->queryID){
      return false;
    } else {
      $this->numRows = mysql_num_rows($this->queryID);
      
      return $this->getAll();
    }
  }
  public function getAll(){
    //返回数据集
    $result = array();
    if($this->numRows >0) {
      while($row = mysql_fetch_assoc($this->queryID)){
        $result[]   =   $row;
      }
      mysql_data_seek($this->queryID,0);
    }
    return $result;
  }
  public function execute($str){
    $this->linkID = $this->connect();
    if(false == $this->linkID)
      return false;
    if($this->queryID)
      mysql_free_result($this->queryID);
    mysql_query("set names utf8");
    $result = mysql_query($str);
    if(false === $result) {
      return false;
    } else {
      $this->numRows = mysql_affected_rows();
      $this->lastInsID = mysql_insert_id();
      return $this->numRows;
    }
  }
}
?>