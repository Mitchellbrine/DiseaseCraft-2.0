package mc.Mitchellbrine.diseaseCraft.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.api.DiseaseRegistrationEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DiseaseJSON implements JsonDeserializer<Disease> {

	public static Logger logger = LogManager.getLogger("DiseaseJSON");

	@Override
	public Disease deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject obj = json.getAsJsonObject();
		final String id = obj.get("name").getAsString();

		Disease disease = new Disease(id);

		DiseaseRegistrationEvent.Override overrideEvent = null;

		if (obj.get("disableEditing") == null || !obj.get("disableEditing").getAsBoolean()) {
			overrideEvent = new DiseaseRegistrationEvent.Override(disease);

			MinecraftForge.EVENT_BUS.post(overrideEvent);
		}

		final double minVersion = obj.get("minVersion").getAsDouble();

		if (overrideEvent != null && overrideEvent.effects != null) {
			for (int i : overrideEvent.effects) {
				disease.addEffect(i);
			}
		} else {
			final JsonArray effectList = obj.getAsJsonArray("effects");

			for (JsonElement element : effectList) {
				if (element.isJsonPrimitive() && ((JsonPrimitive) element).isNumber())
					disease.addEffect(element.getAsInt());
				else
					logger.error("Effect [ " + element + " ] is an incorrect type and was not added!");
			}
		}

		disease.setMinimumVersion(minVersion);

		if (overrideEvent == null || overrideEvent.level == -1) {
			final int diseaseLevel = obj.get("level").getAsInt();

			disease.setLevel(diseaseLevel);
		} else {
			disease.setLevel(overrideEvent.level);
		}

		if (overrideEvent == null || overrideEvent.contractionMap == null) {
			final JsonArray waysToContract = obj.getAsJsonArray("contracting");
			if (waysToContract != null) {
				for (JsonElement objectElement : waysToContract) {
					if (objectElement instanceof JsonObject) {
						JsonObject object = (JsonObject) objectElement;
						final String type = object.get("type").getAsString();
						Object[] parameters = new Object[256];
						final JsonArray array = object.getAsJsonArray("parameters");
						for (int i = 0; i < array.size(); i++) {
							parameters[i] = array.get(i);
							//System.out.println(parameters[i]);
						}
						disease.addWayToContract(type, parameters);
					}
				}
			}
		} else {
			for (String types : overrideEvent.contractionMap.keySet()) {
				disease.addWayToContract(types,overrideEvent.contractionMap.get(types));
			}
		}

		JsonElement deathRate = obj.get("deathRate");
		int rate = -1;

		if (overrideEvent == null || overrideEvent.deathRate == -127) {
			if (deathRate != null) {
				rate = deathRate.getAsInt();
			}

			disease.setDeathRate(rate);
		} else {
			disease.setDeathRate(overrideEvent.deathRate);
		}

		JsonElement isJokeElement = obj.get("isJoke");
		boolean isJoke = false;

		if (overrideEvent == null || overrideEvent.isJoke == null) {
			if (isJokeElement != null) {
				isJoke = isJokeElement.getAsBoolean();
			}
		} else {
			isJoke = overrideEvent.isJoke;
		}

		if (isJoke) {
			disease.triggerJoke();
		}

		if (overrideEvent == null || overrideEvent.lore.equalsIgnoreCase("missingno1234598765")) {
			JsonElement lore = obj.get("lore");

			if (lore != null) {
				disease.setLore(lore.getAsString());
			}
		} else {
			disease.setLore(overrideEvent.lore);
		}

		return disease;
	}
}
