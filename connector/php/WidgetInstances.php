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
 * A collection of known widget instances available to a host.
 */
class WidgetInstances {
	private $WidgetInstances = array();
  
  /**
   * Record an instance of the given widget.
   * 
   * @param xml description of the instance as returned by the widget server when the widget was instantiated.
   * @return the identifier for this instance
   */
 public function put($instance) {
    $this->WidgetInstances[$instance->getIdentifier()] = $instance;
  }
  
  /* Get all Widget instances */
  
  public function get() {
	return $this->WidgetInstances;
  }
}

?>