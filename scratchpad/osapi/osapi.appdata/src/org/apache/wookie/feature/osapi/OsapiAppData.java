package org.apache.wookie.feature.osapi;

import org.apache.wookie.feature.IFeature;
import org.apache.wookie.feature.wave.WaveAPIImpl;

public class OsapiAppData implements IFeature {

	public String getName() {
		return "http://opensocial.org/osapi.appdata";
	}

	public String[] scripts() {
		String[] waveScripts = new WaveAPIImpl().scripts();
		String[] scripts = new String[waveScripts.length+1];
		scripts[0] = "/wookie/shared/feature/osapi.appdata/osapi-appdata.js";
		System.arraycopy(waveScripts, 0, scripts, 1, waveScripts.length);
		return scripts;
	}

	public String[] stylesheets() {
		return null;
	}

}
