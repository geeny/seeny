package io.geeny.seeny.Deserializer;

import io.geeny.seeny.Util.VariableType;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * io.geeny.seeny.Deserializer to handle the custom enum for the navigateType
 * @author Marco Bierbach
 */
public class NavigationTypeDeserializer implements JsonDeserializer<VariableType.NAVIGATE> {

    @Override
    public VariableType.NAVIGATE deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String variable = json.toString().replace("\"", "");
        for (VariableType.NAVIGATE navigateType : VariableType.NAVIGATE.values()) {
            if (navigateType.getName().equals(variable))
                return navigateType;
        }
        return null;
    }
}

