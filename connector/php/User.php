<%--
/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */
--%>
<?php

/**
 * A user represents a possible user of a widget. This class provides a standard way
 * of representing users in plugins for host environments.
 */ 
class User {
  private $loginName = "UNKNOWN";
  private $screenName = "UNKNOWN";
  
  /**
   * Create a new user.
   * 
   * @param loginName
   * @param screenName
   */
  function __construct($loginName, $screenName) {
    $this->setLoginName($loginName);
    $this->setScreenName($screenName);
  }

  /**
   * Get the login name for this user.
   */
  public function getLoginName() {
    return $this->loginName;
  }

  /**
   * Get the screen name for this user. This is the name that is intended to be displayed on
   * screen. In many cases it will be the same as the login name.
   */
  public function getScreenName() {
    return $this->screenName;
  }

  /**
   * Set the login name for this user. This is the value that is used by the user to register on the
   * system, it is guaranteed to be unique.
   * 
   * @param loginName
   */
  public function setLoginName($loginName) {
    $this->loginName = $loginName;
  }

  /**
   * Set the screen name for this user. this is the value that should be displayed on screen.
   * In many cases it will be the same as the login name.
   * 
   * @param screenName
   */
  public function setScreenName($screenName) {
    $this->screenName = $screenName;
  }
  
  /**
   * Get the URL for a thumbnail representing this user.
   * @return
   */
  public function getThumbnailUrl() {
    // FIXME: manage user thumbnails
    return "http://fixme.org/thumbnail";
  }   
}

?>