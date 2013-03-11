<?php

function _list() {

    // Use KISS ORM with our DTB
    // Basic CRUD functionality
    // 
    /* Create
      $user = new User();
      $user->set('username','user');
      $user->set('password','password');
      $user->create();
      $uid=$user->get('uid');

      //Update
      $user->set('password','newpassword');
      $user->update();

      //Retrieve, Delete, Exists
      $user = new User();
      $user->retrieve($uid);
      if ($user->exists())
      $user->delete();

      //Retrieve based on other criteria than the PK
      $user = new User();
      $user->retrieve_one("username=?",'erickoh');
      $user->retrieve_one("username=? AND password=? AND status='enabled'",array('erickoh','123456'));

      //Return an array of Model objects
      $user = new User();
      $user_array = $user->retrieve_many("username LIKE ?",'eric%');
      foreach ($user_array as $user)
      $user->delete();

      //Return selected fields as array
      $user = new User();
      $result_array = $user->select("username,email","username LIKE ?",'eric%');
      print_r($result_array); */

    // Try retrieving a list of names from our dtb
    $user = new User();
    $result_array = $user->select("*", "age LIKE ? LIMIT 10", '35');

    //assigning data to view
    $fdata['results'] = $result_array;

    // testing model objects
    $usermodel = new User();
    $usermodel->retrieve("36160");
    if ($usermodel->exists()) {

        // Sending all row data of id 36160 to view
        $fdata['id36160'] = $usermodel;
    }


    $data['body'][] = View::do_fetch(VIEW_PATH . 'policy/list.php', $fdata);
    View::do_dump(VIEW_PATH . 'layout.php', $data, $result_array);
}