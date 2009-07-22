/**
 * 
 */
package org.apache.wookie.feature.wave;

import java.util.Map;

import org.apache.wookie.feature.IFeature;

/**
 * @author scott
 *
 */
public interface IWaveAPI extends IFeature{
	
	/**
	 * Submits a map of key/value pairs for shared state
	 * @param map
	 * @return a string value marking status or an error message
	 */
	public String submitDelta(String id_key, Map<String, String>map);
	
	/**
	 * Returns the set of shared state objects as a map of [key][value] objects
	 * @param id_key
	 * @return
	 */
	public Map<String, String> state(String id_key);
	
	
	public String getViewer(String id_key);
	
	public String getParticipants(String id_key);
	
	public String getHost(String id_key);

}
