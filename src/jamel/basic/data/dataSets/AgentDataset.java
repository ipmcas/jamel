package jamel.basic.data.dataSets;

import java.util.Set;

/**
 * An interface for the data of one individual agent.
 */
public interface AgentDataset {

	/**
	 * Returns the value to which the specified key is mapped, 
	 * or <code>null</code> if this dataset contains no mapping for the key. 
	 * @param key the key whose associated value is to be returned.
	 * @return the value to which the specified key is mapped, 
	 * or <code>null</code> if this dataset contains no mapping for the key.
	 */
	public Double get(String key);
	
	/**
	 * Returns the name of the agent.
	 * @return the name of the agent.
	 */
	public String getName();

	/**
	 * Returns a <code>Set</code> view of the keys contained in this dataset.
	 * @return a <code>Set</code> view of the keys contained in this dataset.
	 */
	public Set<String> keySet();
	
	/**
	 * Associates the specified value with the specified key in this dataset. 
	 * If the dataset previously contained a mapping for the key, the old value is replaced.
	 * @param key a String with which the specified value is to be associated
	 * @param value a Long value to be associated with the specified key
	 * @return the previous value associated with <code>key</code>, or <code>null</code> if there was no mapping for <code>key</code>. 
	 */
	public Double put(String key, Double value);

	/**
	 * Updates the data.
	 */
	public void update();
	
}

// ***
