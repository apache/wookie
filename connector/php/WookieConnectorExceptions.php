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

class WookieConnectorException extends Exception {

	public function errorMessage() {
		//error message
		$errorMsg = 'Exception thrown on line: '.$this->getLine().' in '.$this->getFile()
		.': <b>'.$this->getMessage().'</b>';
		return $errorMsg;
	}
}

class WookieWidgetInstanceException extends Exception {

	public function errorMessage() {
		//error message
		$errorMsg = 'Exception thrown on line: '.$this->getLine().' in '.$this->getFile()
		.': <b>'.$this->getMessage().'</b>';
		return $errorMsg;
	}
}


?>