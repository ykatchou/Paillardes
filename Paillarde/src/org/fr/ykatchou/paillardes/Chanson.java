package org.fr.ykatchou.paillardes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Paillarde
 * Application sous GPL v3
 * @author ykatchou
 * Cettte classe contient une chanson.
 */
public class Chanson implements Map<String, String> {
	public static DatabaseHelper dbhelp;

	private final HashMap<String, String> data;

	public Long private_id;
	public static final String Id = "ID";
	public static final String Titre = "TITRE";
	public static final String Paroles = "PAROLES";
	public static final String url = "URL";
	public static final String Tags = "TAGS";
	public static final String Midi = "MIDI";
	
	public static final String isMidi = "ISMIDI";

	public Chanson() {
		data = new HashMap<String, String>();
	}

	public Chanson(Long id, String titre) {
		this();
		setId(id);
		setTitre(titre);
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return data.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return data.entrySet();
	}

	@Override
	public String get(Object key) {
		return data.get(key);
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public Set<String> keySet() {

		return data.keySet();
	}

	@Override
	public String put(String key, String value) {
		return data.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> arg0) {
		data.putAll(arg0);
	}

	@Override
	public String remove(Object key) {
		return data.remove(key);
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public Collection<String> values() {
		return data.values();
	}

	public void setId(Long id) {
		data.put(Chanson.Id, String.valueOf(id));
		private_id = id;
	}

	public void setTitre(String titre) {
		data.put(Chanson.Titre, titre);
	}

	public void setParoles(String paroles) {
		data.put(Chanson.Paroles, paroles);
	}
	
	public void setTags(String tags){
		data.put(Chanson.Tags, tags);
	}
	
	public void addTags(String tags){
		String now = data.get(Chanson.Tags);
		if(now != null && now != ""){
			data.put(Chanson.Tags, now + ", " + tags.toLowerCase());
		}else{
			data.put(Chanson.Tags, tags);
		}
	}
}
