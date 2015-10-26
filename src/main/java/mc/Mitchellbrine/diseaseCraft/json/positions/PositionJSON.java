package mc.Mitchellbrine.diseaseCraft.json.positions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import mc.Mitchellbrine.diseaseCraft.utils.BlockPos;

import java.lang.reflect.Type;

/**
 * Created by Mitchellbrine on 2015.
 */
public class PositionJSON implements JsonSerializer<BlockPos>, JsonDeserializer<BlockPos> {

	@Override
	public JsonElement serialize(BlockPos src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("x",src.x);
		obj.addProperty("y",src.y);
		obj.addProperty("z",src.z);
		return obj;
	}

	@Override
	public BlockPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		if (obj.get("x") == null || obj.get("y") == null || obj.get("z") == null)
			return null;
		return new BlockPos(obj.get("x").getAsInt(),obj.get("y").getAsInt(),obj.get("z").getAsInt());
	}
}
