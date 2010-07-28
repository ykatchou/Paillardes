package org.fr.ykatchou.paillardes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Chanson implements Map<String, String> {
	public static DatabaseHelper dbhelp;

	private final HashMap<String, String> data;

	public static final String Id = "ID";
	public static final String Titre = "TITRE";
	public static final String Paroles = "PAROLES";
	public static final String url = "URL";

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
	}

	public void setTitre(String titre) {
		data.put(Chanson.Titre, titre);
	}

	public void setParoles(String paroles) {
		data.put(Chanson.Paroles, paroles);
	}
}
