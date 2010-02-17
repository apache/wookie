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
 * webcam is the default browser-based camera. We have to assume it
 * exists as we can't check until the user is prompted to allow access
 */
webcam = new function webcam(){
    
    this.takePicture = function(success_callback, error_callback, options){
        this.callback = success_callback;
        this.error = error_callback;
        tb_show("","/wookie/shared/feature/camera/camcanvas.html?TB_iframe=true&height=320&width=335",""); 
    }
    
    this.snapped = function(image){
        tb_remove();
        if (this.callback) this.callback(image);
    }

    this.cancelled = function(){
        tb_remove();
        if (this.error) this.error();
    }
}
/**
 * Bondi camera interface
 */
camera = new function camera(){
    this.getCameras = function(success_callback, err_callback){
        if (webcam){
            return [webcam];
            success_callback([webcam]);
        } else {
            err_callback();
        }
    }
}
 
bondi = new function bondi(){
    this.camera = camera;
}

