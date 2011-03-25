package org.apache.wookie.feature.osapi;

import org.apache.wookie.feature.IFeature;
import org.apache.wookie.feature.wave.WaveAPIImpl;

public class OsapiPeople implements IFeature {

	public String getName() {
		return "http://opensocial.org/osapi.person";
	}

	public String[] scripts() {
		String[] waveScripts = new WaveAPIImpl().scripts();
		String[] scripts = new String[waveScripts.length+1];
		scripts[0] = "/wookie/shared/feature/osapi.person/osapi-person.js";
		System.arraycopy(waveScripts, 0, scripts, 1, waveScripts.length);
		return scripts;
	}

	public String[] stylesheets() {
		return null;
	}

}
