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
 * A connection to a Wookie server. This maintains the necessary data for
 * connecting to the server and provides utility methods for making common calls
 * via the Wookie REST API.
 * 
 */

 
class WookieServerConnection {
  private $url;
  private $apiKey = "TEST";
  private $sharedDataKey = "mysharedkey";

  /**
   * Create a connection to a Wookie server at a given URL.
   * @param url the URL of the wookie server
   * @param apiKey the API key for the server
   * @param sharedDataKey the sharedDataKey for the server connection 
   * 
   * @throws WookieConnectorException if there is a problem setting up this connection.
   */
  function __construct($url, $apiKey, $sharedDataKey) {
    $this->setURL($url);
    $this->setApiKey($apiKey);
    $this->setSharedDataKey($sharedDataKey);
  }

  /**
   * Get the URL of the wookie server.
   * 
   * @return
   * @throws WookieConnectionException
   */
  public function getURL() {
    return $this->url;
  }
  
  /**
   * Set the URL of the wookie server.
   * 
   * @throws WookieConnectionException
   */
  public function setURL($newUrl) {
    $this->url = $newUrl;
  }
  
  /**
   * Get the API key for this server.
   * 
   * @return
   */
  public function getApiKey() {
    return $this->apiKey;
  }

  /**
   * Set the API key for this server.
   * 
   */
  public function setApiKey($newApiKey) {
    $this->apiKey = $newApiKey;
  }

  /**
   * Get the shared data key for this server.
   * 
   * @return
   */
  public function getSharedDataKey() {
    return $this->sharedDataKey;
  }

  /**
   * Set the shared data key for this server.
   *
   */
  public function setSharedDataKey($newKey) {
    $this->sharedDataKey = $newKey;
  }
  
  public function toString() {
    $str = "Wookie Server Connection - ";
    $str .= "URL: ".$this->getURL();
    $str .= "API Key: ".$this->getApiKey();
    $str .= "Shared Data Key: ".$this->getSharedDataKey();
    return $str;
  }
}


?>