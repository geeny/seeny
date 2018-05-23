package io.geeny.seeny.Deserializer;

import io.geeny.seeny.Util.VariableType;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * io.geeny.seeny.Deserializer to handle the custom enum for the actionType
 * @author Marco Bierbach
 */
public class ActionTypeDeserializer implements JsonDeserializer<VariableType.ACTION_TYPE> {

    @Override
    public VariableType.ACTION_TYPE deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String variable = json.toString().replace("\"", "");
        for (VariableType.ACTION_TYPE actionType : VariableType.ACTION_TYPE.values()) {
            if (actionType.getName().equals(variable))
                return actionType;
        }
        return null;
    }
}