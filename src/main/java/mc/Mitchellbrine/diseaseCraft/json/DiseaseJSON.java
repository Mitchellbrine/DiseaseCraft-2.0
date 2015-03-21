package mc.Mitchellbrine.diseaseCraft.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mc.Mitchellbrine.diseaseCraft.api.Disease;

import java.lang.reflect.Type;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DiseaseJSON implements JsonDeserializer<Disease> {

	@Override
	public Disease deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject obj = json.getAsJsonObject();
		final String id = obj.get("name").getAsString();

		Disease disease = new Disease(id);

		final double minVersion = obj.get("minVersion").getAsDouble();
		final JsonArray effectList = obj.getAsJsonArray("effects");

		for (JsonElement element : effectList) {
			disease.addEffect(element.getAsInt());
		}

		disease.setMinimumVersion(minVersion);

		final int diseaseLevel = obj.get("level").getAsInt();

		disease.setLevel(diseaseLevel);

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
						System.out.println(parameters[i]);
					}
					disease.addWayToContract(type,parameters);
				}
			}
		}

		return disease;
	}
}
