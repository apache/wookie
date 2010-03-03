<?php
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

require("WookieConnectorExceptions.php");
require("WookieServerConnection.php");
require("WidgetInstances.php");
require("Widget.php");
require("WidgetInstance.php");
require("User.php");
require("HTTP_Response.php");


class WookieConnectorService {
  private $conn;
  public  $WidgetInstances;
  private $user;
  
  function __construct($url, $apiKey, $sharedDataKey, $loginName, $screenName = null) {
    $this->setConnection(new WookieServerConnection($url, $apiKey, $sharedDataKey));
	$this->setWidgetInstancesHolder();
	$this->setUser($loginName, $screenName);
  }
  
  public function setConnection($newConn) {
    $this->conn = $newConn;
  }
  
  public function getConnection() {
    return $this->conn;
  }
  
  public function setWidgetInstancesHolder() {
	$this->WidgetInstances = new WidgetInstances();
  }
  
  public function setUser($loginName, $screenName = null) {
	if($screenName == null) {
		$screenName = $loginName;
	}
	$this->user = new User($loginName, $screenName);
  }
  
  public function getUser() {
	return $this->user;
  }
  
  /* Do POST request
  /* @return new HTTP_Response instance */
  
  private function do_post_request($url, $data, $optional_headers = null)
  {
	if(is_array($data)) {
		 // convert variables array to string:
		$_data = array();    
		while(list($n,$v) = each($data)){
			$_data[] = urlencode($n)."=".urlencode($v);
		}    
		$data = implode('&', $_data);
	}
	
     $params = array('http' => array(
                  'method' => 'POST',
                  'content' => $data
               ));
	
     if ($optional_headers !== null) {
        $params['http']['header'] = $optional_headers;
     }
     $ctx = @stream_context_create($params);
    // $fp = @@file_get_contents($url, 'rb', false, $ctx);
     $response = @file_get_contents($url, false, $ctx);

     return new HTTP_Response($response, $http_response_header);
  }
  
  
  /**
   * Get or create an instance of a widget.
   * 
   * @param widget
   * @return the ID of the widget instance
   * @throws IOException
   * @throws SimalRepositoryException
   */

  public function getOrCreateInstance($Widget_or_GUID = null) {
    try {
		if(is_object($Widget_or_GUID)) {
			$guid = $Widget_or_GUID->getIdentifier();
		} else {
			$guid = $Widget_or_GUID;
		}
		if($guid == '') {
			throw new WookieConnectorException("No GUID nor Widget object");
		}
		$requestUrl = $this->getConnection()->getURL().'/widgetinstances';
		$request.= '&api_key='.$this->getConnection()->getApiKey();
		$request.= '&servicetype=';
		$request.= '&userid='.$this->getUser()->getLoginName();
		$request.= '&shareddatakey='.$this->getConnection()->getSharedDataKey();
		$request.= '&widgetid='.$guid;
		
		if(!$this->checkURL($requestUrl)) {
			throw new WookieConnectorException("URL for supplied Wookie Server is malformed: ".$requestUrl);
		}	
		$response = $this->do_post_request($requestUrl, $request);
		
		//if instance was created, perform second request to get widget instance
		if($response->getStatusCode() == 201) {
			$response = $this->do_post_request($requestUrl, $request);
		}
		$instance = $this->parseInstance($guid, $response->getResponseText());
		$this->WidgetInstances->put($instance);
		return $instance;
	} catch (WookieConnectorException $e) {
		echo $e->errorMessage();
	}
		return false;
  }
  
  
    /**
   * Record an instance of the given widget.
   * 
   * @param xml description of the instance as returned by the widget server when the widget was instantiated.
   * @return new Widget instance
   */
  private function parseInstance($widgetGuid, $xml) {
	$xmlWidgetData = @simplexml_load_string($xml);
	if(is_object($xmlWidgetData)) {
		$url = (string) $xmlWidgetData->url;
		$title = (string) $xmlWidgetData->title;
		$height = (string) $xmlWidgetData->height;
		$width = (string) $xmlWidgetData->width;
		$maximize = (string) $xmlWidgetData->maximize;
		$instance = new WidgetInstance($url, $widgetGuid, $title, $height, $width, $maximize);
		return $instance;
	} 
	return false;
  }
  
   /**
   * Check if URL is parsable.
   * 
   * @param url
   * @return boolean
   */
  
  private function checkURL($url) {
	$UrlCheck = parse_url($url);
	if($UrlCheck['scheme'] != 'http' || $UrlCheck['host'] == null || $UrlCheck['path'] == null) {
		return false;
	}
	return true;
  }
  
   /**
   * @refactor At time of writing the REST API for adding a participant is broken so we are
   * using the non-REST approach. The code for REST API is commented out and should be used
   * in the future.
   */
   
  public function addParticipant($widgetInstance, $User)  {
	$Url = $this->getConnection()->getURL().'/participants';
	
	try {
		if(!is_object($widgetInstance)) throw new WookieWidgetInstanceException('No Widget instance');
		if(!is_object($User)) throw new WookieConnectorException('No User object');
		
		$data = array(
				'api_key' => $this->getConnection()->getApiKey(),
				'shareddatakey' => $this->getConnection()->getSharedDataKey(),
				'userid' => $this->getUser()->getLoginName(),
				'widgetid' => $widgetInstance->getIdentifier(),
				'participant_id' => $this->getUser()->getLoginName(),
				'participant_display_name' => $User->getScreenName(),
				'participant_thumbnail_url' => $User->getThumbnailUrl(),
		);	
		
		if(!$this->checkURL($Url)) {
			throw new WookieConnectorException("Participants rest URL is incorrect: ".$Url);
		}	
		
		$response = $this->do_post_request($Url, $data);
		$statusCode = $response->getStatusCode();
		
		switch($statusCode) {
		  case 200: //participant already exists
			return true;
			break;
		  case 201:
			return true; //new participant added
			break;
		  case ($statusCode > 201):
			throw new WookieConnectorException($response->headerToString().'<br />'.$response->getResponseText());
			break;
		  default:
			return false;
		}

	} catch (WookieConnectorException $e) {
		echo $e->errorMessage();
	} catch (WookieWidgetInstanceException $e) {
		echo '<b>function.addParticipant:</b> '.$e->getMessage().'<br />';
	}
    
  }
  
   /**
   * Get the array of users for a widget instance
   * @param instance
   * @return an array of users
   * @throws WookieConnectorException
   */
  public function getUsers($widgetInstance) {
	$Url = $this->getConnection()->getURL().'/participants';
	$Users = array();
		try {
			if(!is_object($widgetInstance)) throw new WookieWidgetInstanceException('No Widget instance');
			$request = '?api_key='.$this->getConnection()->getApiKey();
			$request .= '&shareddatakey='.$this->getConnection()->getSharedDataKey();
			$request .= '&userid='.$this->getUser()->getLoginName();
			$request .= '&widgetid='.$widgetInstance->getIdentifier();
			
			if(!$this->checkURL($Url)) {
				throw new WookieConnectorException("Participants rest URL is incorrect: ".$Url);
			}
			
			$response = new HTTP_Response(@file_get_contents($Url.$request), $http_response_header);
			if($response->getStatusCode() > 200) throw new WookieConnectorException($response->headerToString().'<br />'.$response->getResponseText());
			
			$xmlObj = @simplexml_load_string($response->getResponseText());
			
			if(is_object($xmlObj)) {
				foreach($xmlObj->children() as $participant) {
				  $participantAttr = $participant->attributes();
				  
				  $id = (string) $participantAttr->id;
				  $name = (string) $participantAttr->display_name;
				  $thumbnail_url = (string) $participantAttr->thumbnail_url;
				  
				  $newUser = new User($id, $name, $thumbnail_url);
				  array_push($Users, $newUser);
				}
			} else {
				throw new WookieConnectorException('Problem getting participants');
			}
			
			return $Users;
		} catch (WookieWidgetInstanceException $e) {
			echo '<b>function.getUsers:</b> '.$e->getMessage().'<br />';
		} catch (WookieConnectorException $e) {
			echo $e->errorMessage();
		}
  }

 

  /**
   * Get a set of all the available widgets in the server. If there is an error
   * communicating with the server return an empty set, or the set received so
   * far in order to allow the application to proceed. The application should
   * display an appropriate message in this case.
   * 
   * @return array of available widgets
   * @throws WookieConnectorException
   */
   
  public function getAvailableWidgets() {
	$widgets = array();
	try {
		$request = $this->getConnection()->getURL().'/widgets?all=true';
		
		if(!$this->checkURL($request)) {
			throw new WookieConnectorException("URL for Wookie is malformed");
		}
		
		$response = new HTTP_Response(@file_get_contents($request), $http_response_header);
		$xmlObj = @simplexml_load_string($response->getResponseText());
		
		if(is_object($xmlObj)) {
			foreach($xmlObj->children() as $widget) {
				 $id = (string) $widget->attributes()->identifier;
				 $title = (string) $widget->title;
				 $description = (string) $widget->description;
				 $iconURL = (string) $widget->attributes()->icon;
				 if($iconURL == '') {
						$iconURL = (string) 'http://www.oss-watch.ac.uk/images/logo2.gif';
					}
				$Widget = new Widget($id, $title, $description, $iconURL);
				$widgets[$id] = $Widget;
			}
		} else {
				throw new WookieConnectorException('Problem getting available widgets');
			}
			
	 } catch(WookieConnectorException $e) {
			echo $e->errorMessage();
		}
    return $widgets;
  }
    
}
?>