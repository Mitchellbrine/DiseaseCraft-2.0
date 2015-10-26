package mc.Mitchellbrine.diseaseCraft.utils;

import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class StringUtils {

	public static <T> Object[] cutArray(T[] array, int beginIndex) {
		// beginIndex is inclusive, so be sure to start with the index you really want
		List<Object> ts = new ArrayList<Object>();
		Object[] array2 = new Object[array.length - beginIndex];
		for (int i = beginIndex; i < array.length;i++) {
			ts.add(array[i]);
		}
		return ts.toArray(array2);
	}

	public static boolean arrayContains(String[] array, String value) {
		for (String key : array) {
			if (key.equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static boolean arrayContainsLoose(String[] array, String value) {
		for (String key : array) {
			if (key.equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

	public static String[] getStringsFromPrimitives(JsonPrimitive[] primitives) {
		String[] array = new String[primitives.length];
		for (int i = 0; i < primitives.length;i++) {
			array[i] = primitives[i].getAsString();
		}
		return array;
	}

}
