package io.geeny.seeny.Deserializer;

import io.geeny.seeny.Util.VariableType;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * io.geeny.seeny.Deserializer to handle the custom enum for the selector type
 * @author Marco Bierbach
 */
public class SelectorTypeDeserializer implements JsonDeserializer<VariableType.SELECTOR_TYPE> {

    @Override
    public VariableType.SELECTOR_TYPE deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String variable = json.toString().replace("\"", "");
        for (VariableType.SELECTOR_TYPE selectorType : VariableType.SELECTOR_TYPE.values()) {
            if (selectorType.getName().equals(variable))
                return selectorType;
        }
        return null;
    }
}