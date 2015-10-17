package mc.Mitchellbrine.diseaseCraft.modules.med.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Mitchellbrine on 2015.
 */
public class MedJSON implements JsonDeserializer<Medication> {
	@Override
	public Medication deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		if (obj.get("diseaseHeal") != null && obj.get("medName") != null && obj.get("itemName") != null) {
			Medication med = new Medication();
			med.diseaseHeal = obj.get("diseaseHeal").getAsString();
			med.medName = obj.get("medName").getAsString();
			med.itemName = obj.get("itemName").getAsString();
			med.itemMeta = obj.get("itemMeta") != null ? obj.get("itemMeta").getAsInt() : 0;
			med.suppresents = obj.get("suppressants") != null ? obj.get("suppressants").getAsInt() : 1;
			return med;
		}
		return null;
	}
}
