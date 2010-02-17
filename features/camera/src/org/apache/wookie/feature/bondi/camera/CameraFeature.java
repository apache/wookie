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
package org.apache.wookie.feature.bondi.camera;

import org.apache.wookie.feature.IFeature;

public class CameraFeature implements IFeature {
	
	public String getName() {
		return "http://bondi.omtp.org/api/camera.capture";
	}

	public String[] scripts() {
		return new String[]{"/wookie/shared/feature/camera/jquery.js","/wookie/shared/feature/camera/thickbox.js","/wookie/shared/feature/camera/bondi_camera.js"};
	}

	public String[] stylesheets() {
		return new String[]{"/wookie/shared/feature/camera/thickbox.css"};
	}

}
