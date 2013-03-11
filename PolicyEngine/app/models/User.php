<?php
// From our test MySQL data
class User extends Model {

  function __construct($id='') {
    parent::__construct('id','name'); //primary key = uid; tablename = users
    $this->rs['id'] = '';
    $this->rs['name'] = '';
    $this->rs['age'] = '';
    $this->rs['date'] = '';
    if ($id)
      $this->retrieve($id);
  }

  function create() {
    date_default_timezone_set('Europe/Paris');
    $this->rs['date']=date('Y-m-d H:i:s');
    return parent::create();
  }
}