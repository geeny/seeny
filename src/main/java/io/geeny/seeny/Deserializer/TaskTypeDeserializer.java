package io.geeny.seeny.Deserializer;

import io.geeny.seeny.Util.VariableType;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * io.geeny.seeny.Deserializer to handle the custom enum for the task type
 * @author Marco Bierbach
 */
public class TaskTypeDeserializer implements JsonDeserializer<VariableType.TASK> {

    @Override
    public VariableType.TASK deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String variable = json.toString().replace("\"", "");
        for (VariableType.TASK taskType : VariableType.TASK.values()) {
            if (taskType.getName().equals(variable))
                return taskType;
        }
        return null;
    }
}

