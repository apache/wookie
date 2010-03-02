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

require("WookieConnectorExceptions.php");
require("WookieServerConnection.php");
require("WidgetInstances.php");
require("Widget.php");
require("WidgetInstance.php");
require("User.php");
require("xmlFunctions.php");

class WookieConnectorService {
  private $conn;
  public  $WidgetInstances;
  private $user;
  private $requestStatusCode;
  
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
  /* @return response XML */
  
  private function do_post_request($url, $data, $optional_headers = null)
  {
     $params = array('http' => array(
                  'method' => 'POST',
                  'content' => $data
               ));
     if ($optional_headers !== null) {
        $params['http']['header'] = $optional_headers;
     }
     $ctx = stream_context_create($params);
    // $fp = @file_get_contents($url, 'rb', false, $ctx);
     $response = @file_get_contents($url, false, $ctx);
     if ($response === false) {
     //  throw new Exception("Problem with $url, $php_errormsg");
     }
  //   $response = @stream_get_contents($fp);
     if ($response === false) {
     //   throw new Exception("Problem reading data from $url, $php_errormsg");
     }
	 $this->requestStatusCode = $http_response_header[0];
     return $response;
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
		$requestUrl = $this->getConnection()->getURL();
		$requestUrl .= '/widgetinstances?';
		$request = 'requestid=getwidget';
		$request.= '&api_key='.$this->getConnection()->getApiKey();
		$request.= '&servicetype=';
		$request.= '&userid='.$this->getUser()->getLoginName();
		$request.= '&shareddatakey='.$this->getConnection()->getSharedDataKey();
		$request.= '&widgetid='.$guid;
		
		//$response = file_get_contents($requestUrl.$request);
		$response = $this->do_post_request($requestUrl, $request);
		
		//try again, if first time fails
		if(!$response) {
		//	$response = file_get_contents($request);
		}
		$instance = $this->parseInstance(XML_unserialize($response));
		$this->WidgetInstances->put($instance);
	} catch (WookieConnectorException $e) {
		echo $e->errorMessage();
	}
	return $instance;
	
  /*
    } catch (MalformedURLException e) {
      throw new RuntimeException("URL for supplied Wookie Server is malformed",
          e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException("Unable to configure XML parser", e);
    } catch (SAXException e) {
      throw new RuntimeException("Problem parsing XML from Wookie Server", e);
    }

    return instance;*/
  }
  
  
    /**
   * Record an instance of the given widget.
   * 
   * @param xml description of the instance as returned by the widget server when the widget was instantiated.
   * @return the identifier for this instance
   */
  private function parseInstance($xml) {
    $url = $xml['widgetdata']['url'];
    $id = $xml['widgetdata']['identifier'];
    $title = $xml['widgetdata']['title'];
    $height = $xml['widgetdata']['height'];
    $width = $xml['widgetdata']['width'];
    $maximize = $xml['widgetdata']['maximize'];
    $instance = new WidgetInstance($url, $id, $title, $height, $width, $maximize);
 
    return $instance;
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
		
		$request = 'api_key='.$this->getConnection()->getApiKey();
		$request .= '&shareddatakey='.$this->getConnection()->getSharedDataKey();
		$request .= '&userid='.$this->getUser()->getLoginName();
		$request .= '&widgetid='.$widgetInstance->getId();
		$request .= '&participant_id='.$User->getLoginName();
		$request .= '&participant_display_name='.$User->getScreenName();
		$request .= '&participant_thumbnail_url='.$User->getThumbnailUrl();
		
	} catch (WookieConnectorException $e) {
		echo $e->errorMessage();
	} catch (WookieWidgetInstanceException $e) {
		echo '<b>function.addParticipant:</b> '.$e->getMessage().'<br />';
	}
	//FIXME: request failes, "400 Bad request"
  //TODO: implement error handling, statuscodes
	$response = $this->do_post_request($Url, $request);
	echo $this->requestStatusCode;
	print_r($response);
    
/*	if (conn.getResponseCode() > 201) throw new IOException(conn.getResponseMessage());
    } catch (MalformedURLException e) {
      throw new WookieConnectorException("Participants rest URL is incorrect: " + url, e);
    } catch (IOException e) {
      StringBuilder sb = new StringBuilder("Problem adding a participant. ");
      sb.append("URL: ");
      sb.append(url);
      sb.append(" data: ");
      sb.append(postdata);
      throw new WookieConnectorException(sb.toString(), e);
    } 
	*/
  }
  
   /**
   * Get the array of users for a widget instance
   * @param instance
   * @return an array of users
   * @throws WookieConnectorException
   */
  public function getUsers($widgetInstance) {
	$Url = $this->getConnection()->getURL().'/participants';
		try {
			if(!is_object($widgetInstance)) throw new WookieWidgetInstanceException('No Widget instance');
			$request = '?api_key='.$this->getConnection()->getApiKey();
			$request .= '&shareddatakey='.$this->getConnection()->getSharedDataKey();
			$request .= '&userid='.$this->getUser()->getLoginName();
			$request .= '&widgetid='.$widgetInstance->getId();
			
			$response = file_get_contents($Url.$request);
			$xmlDoc = XML_unserialize($response);
		} catch (WookieWidgetInstanceException $e) {
			echo '<b>function.getUsers:</b> '.$e->getMessage().'<br />';
		}
	    print_r($http_response_header);
		//FIXME: retrieve participants
	print_r($xmlDoc);
	 /*   URL url = null;
	    try {
	      url = new URL(conn.getURL() + "/participants"+queryString);
	      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	      InputStream is = conn.getInputStream();
	      if (conn.getResponseCode() > 200) throw new IOException(conn.getResponseMessage());
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      DocumentBuilder db = dbf.newDocumentBuilder();
	      Document widgetsDoc = db.parse(is);
	      Element root = widgetsDoc.getDocumentElement();
	      NodeList participantsList = root.getElementsByTagName("participant");
	      if (participantsList == null || participantsList.getLength() == 0) return new User[0];
	      User[] users = new User[participantsList.getLength()];
	      for (int idx = 0; idx < participantsList.getLength(); idx = idx + 1) {
	        Element participantEl = (Element) participantsList.item(idx);
	        String id = participantEl.getAttribute("id");
	        String name = participantEl.getAttribute("display_name");
	        //FIXME implement: String thumbnail = participantEl.getAttribute("thumbnail_url");
	        User user = new User(id,name);
	        users[idx] = user;
	      }
	      return users;
	    } catch (MalformedURLException e) {
	      throw new WookieConnectorException("Participants rest URL is incorrect: " + url, e);
	    } catch (IOException e) {
	      StringBuilder sb = new StringBuilder("Problem getting participants. ");
	      sb.append("URL: ");
	      sb.append(url);
	      sb.append(" data: ");
	      sb.append(queryString);
	      throw new WookieConnectorException(sb.toString(), e);
	    } catch (ParserConfigurationException e) {
		      throw new WookieConnectorException("Problem parsing data: " + url, e);
		} catch (SAXException e) {
		      throw new WookieConnectorException("Problem parsing data: " + url, e);
		} */
  }

 

  /**
   * Get a set of all the available widgets in the server. If there is an error
   * communicating with the server return an empty set, or the set received so
   * far in order to allow the application to proceed. The application should
   * display an appropriate message in this case.
   * 
   * @return
   * @throws SimalException
   */
  public function getAvailableWidgets() {
	$widgets = array();
	try {
		$request = $this->getConnection()->getURL().'/widgets?all=true';
		$xmlDoc = XML_unserialize(file_get_contents($request));
		
		foreach ($xmlDoc['widgets']['widget'] as $key=>$data){
			if(is_int($key)) {
				$id = $xmlDoc['widgets']['widget'][$key.' attr']['identifier'];
				$title = $data['title'];
				$description = $data['description'];
				$iconURL = $data['icon'];
				if($iconURL == '') {
					$iconURL = 'http://www.oss-watch.ac.uk/images/logo2.gif';
				}
				$Widget = new Widget($id, $title, $description, $iconURL);
				$widgets[$id] = $Widget;
			}
		}
	}
		
	 catch(WookieServerException $e) {
		// do something
	}
  /*
      }
    } catch (ParserConfigurationException e) {
      throw new WookieConnectorException("Unable to create XML parser", e);
    } catch (MalformedURLException e) {
      throw new WookieConnectorException("URL for Wookie is malformed", e);
    } catch (IOException e) {
      // return an empty set, or the set received so far in order to allow
      // the application to proceed. The application should display an
      // appropriate message in this case.
      return widgets;
    } catch (SAXException e) {
      throw new WookieConnectorException(
          "Unable to parse the response from Wookie", e);
    }
	*/
    return $widgets;
  }
    
}
?>