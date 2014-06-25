<?php
require_once "Db/DbMysql.class.php";
require_once "Config.class.php";

class Model{
  private $modName;
  
  
  public function __construct($name=''){
  	if(Config::$config['table_prefix'])
  	  $this->modName = Config::$config['table_prefix'] . $name;
  	else
  		$this->modName = $name;
  }
  
  /**the result of query is an array of object*/
  public function __call($name, $argument){
    if(strtolower(substr($name, 0, 7)) == 'queryby'){
      $proper = strtolower(substr($name, 7));
      if(strtolower($proper) != 'all'){
				$ps = explode("_", $proper);
				$where = array();
				for($i=0; $i<count($ps); $i++)
	  			$where[$ps[$i]] = $argument[$i];
	  
				$sql = "select * from ".$this->modName." ".$this->parseWhere($where);
      }else{
				$sql = "select * from ".$this->modName;
      }
			
      $mysql = new DbMysql(Config::$config);
      $results = $mysql->query($sql);
      $res = array();
      if($results){
				foreach($results as  $result){
	  			$r = new Model();
	  			foreach($result as $index => $value){
	    			$r->{$index} = $value;
	  			}
	  			$res[] = $r;
				}
				return $res;
      }
    }
    return false;
  }
  /*$datas = array(array('filename' => 'xxx', 'contents' => 'yyyyy'),
  								 array('filename' => 'xxx', 'contents' => 'yyyyy'));*/
  public function add($datas, $options=''){
    $fields = array_keys($datas[0]);
    foreach($datas as $data){
      $value = array();
      foreach($data as $key => $val){
				$val = $this->parseValue($val);
				if(is_scalar($val)){
	  			$value[] = $val;
				}
      }
      $values[] = '('.implode(',', $value).')';
    }
    $sql = "insert into ". $this->modName. "(". implode(',', $fields). ') values '.implode(',',$values);
//     echo $sql."<br/>";
    $mysql = new DbMysql(Config::$config);
    return $mysql->execute($sql);
  }
  /*$options=array('table' => 'xxx'
		   'where')*/
	
	public function del($options){
		$sql = "delete from "
			.$this->modName." "
			.$this->parseWhere($options['where']);
		
		$mysql = new DbMysql(Config::$config);
		return $mysql->execute($sql);
	}
  public function save($data, $options){
    $sql = "update "
	   .$this->modName." "
	   .$this->parseSet($data)." "
	   .$this->parseWhere($options['where']);
//     echo $sql;
    $mysql = new DbMysql(Config::$config);
    return $mysql->execute($sql);
  }
  public function parseValue($value){
    if(is_string($value)){	
      $value = '\'' . mysql_escape_string($value).'\'';
    } elseif(isset($value[0]) && is_string($value[0]) && strtolower($value[0]) == 'exp'){
      $value = mysql_escape_string($value[1]);
    } elseif(is_array($value)){
      $value = array_map(array($this, 'parseValue'), $value);
    } elseif(is_null($value)){
      $value = 'null';
    }
    return $value;
  }
  /*$where = array('col' => 'val'...)*/
  public function parseWhere($where){
    $whereStr = '';
    if(is_string($where)){
      $whereStr = $where;
    }else{
      $str = array();
      foreach($where as $key => $val){
	$str[] = "`$key` = ".Model::parseValue($val);
      }
      $whereStr = "where ".implode(" AND ", $str);
    }
    return $whereStr;
  }
  /*$set = array('col' => 'val')*/
  public function parseSet($set){
    $setStr = '';
    $str = array();
    foreach($set as $key => $val){
      $str[] = "`$key` = ".Model::parseValue($val);
    }
    $setStr = "set ".implode(",", $str);
//     echo $setStr."<br/>";
    return $setStr;
  }
}
?>