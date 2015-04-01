package mc.Mitchellbrine.diseaseCraft.dio;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DCVersionJSON implements JsonDeserializer<DCVersion> {

		@Override
		public DCVersion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject obj = json.getAsJsonObject();
			double version = obj.get("version").getAsDouble();
			double mcVersion = obj.get("mcVersion").getAsDouble();
			String changelog = obj.get("changelog").getAsString();

			return new DCVersion(version,mcVersion,changelog);
		}
}
