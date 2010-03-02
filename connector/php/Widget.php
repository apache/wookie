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
 * A client side representation of a widget. 
 * 
 */
class Widget { 
  var $identifier;
  var $title;
  var $description;
  var $icon;

  function __construct($identifier, $title, $description, $icon) {
    $this->identifier = $identifier;
    $this->title = $title;
    $this->description = $description;
    $this->icon = $icon;
  }
  
  /**
   * Get a unique identifier for this widget type.
   * 
   * @return
   */
  public function getIdentifier() {
    return $this->identifier;
  }

  /**
   * Get the human readable title of this widget.
   * @return
   */
  public function getTitle() {
    return $this->title;
  }

  /**
   * Get the location of a logo for this widget.
   * @return
   */
  public function getIcon() {
    return $this->icon;
  }
  
  /**
   * Get the description of the widget.
   * 
   * @return
   */
  public function getDescription() {
    return $this->description;
  }

}


?>