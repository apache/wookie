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
package org.apache.wookie.digsig.client;

import org.apache.wookie.digsig.client.model.SignWidgets;
import org.apache.wookie.digsig.client.ui.SignerJFrame;

/** @author Pushpalanka */
public class SignCoordinator {

    public static void main(String args[]) {
        SignWidgets signWidgets = new SignWidgets();
        SignerJFrame signerJFrame = new SignerJFrame(signWidgets);
        signerJFrame.setVisible(true);
    }
}
