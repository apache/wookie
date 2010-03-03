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

/**
 * An instance of a widget for use on the client.
 * 
 * @refactor this class duplicates code in the widget bean o nthe server side
 *
 */
 
class WidgetInstance {
  var $url;
  var $guid;
  var $title;
  var $height;
  var $width;
  var $maximize;

  public function WidgetInstance($url, $guid, $title, $height, $width, $maximize) {
    $this->setIdentifier($guid);
    $this->setUrl($url);
    $this->setTitle($title);
    $this->setHeight($height);
    $this->setWidth($width);
    $this->setMaximize($maximize);
  }
  
  public function getUrl() {
    return $this->url;
  }

  public function setUrl($url) {
    $this->url = $url;
  }

  public function getIdentifier() {
    return $this->guid;
  }

  public function setIdentifier($guid) {
    $this->guid = $guid;
  }

  public function getTitle() {
    return $this->title;
  }

  public function setTitle($title) {
    $this->title = $title;
  }

  public function getHeight() {
    return $this->height;
  }

  public function setHeight($height) {
    $this->height = $height;
  }

  public function getWidth() {
    return $this->width;
  }

  public function setWidth($width) {
    $this->width = $width;
  }

  public function getMaximize() {
    return $this->maximize;
  }

  public function setMaximize($maximize) {
    $this->maximize = $maximize;
  }
  public function isMaximizable() {
    return $this->getMaximize;
  }
}

?>