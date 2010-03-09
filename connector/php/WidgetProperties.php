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

 class Property {
	private $propertyName = '';
	private $propertyValue = '';
	private $isPublic = 'false';
	
	function __construct($propertyName, $propertyValue = null, $isPublic = 'false') {
		$this->propertyName = (string) $propertyName;
		$this->propertyValue = (string) $propertyValue;
		$this->isPublic = (string) $isPublic;
	}
	
	public function getValue() {
		return $this->propertyValue;
	}
	
	public function getName() {
		return $this->propertyName;
	}
	public function isPublic() {
		return $this->isPublic;
	}
	
	public function setValue($value) {
		$this->propertyValue = (string) $value;
	}
	
	public function setName($propertyName) {
		$this->propertyName = (string) $propertyName;
	}
	public function set_isPublic($isPublic) {
		$this->isPublic = (string) $isPublic;
	}
	
 }

?>