package org.fr.ykatchou.paillardes;

public class ChansonHelper {
	public static void GenerateMidi(Chanson c, String midi){
		if(midi != null && midi != ""){
			c.put(Chanson.Midi, midi);
			c.put(Chanson.isMidi, "Midi");
		}
	}
}
