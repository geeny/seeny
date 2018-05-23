package io.geeny.seeny.Deserializer;

import io.geeny.seeny.Model.Elements.*;
import io.geeny.seeny.Util.Message;
import io.geeny.seeny.Util.VariableType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * io.geeny.seeny.Deserializer to handle the deserialization of the SeenyElement
 * @author Marco Bierbach
 */
public class SeenyElementDeserializer implements JsonDeserializer<SeenyElement> {
    @Override
    public SeenyElement deserialize(JsonElement jsonElement, Type typeOf, JsonDeserializationContext context) {
        String actionTypeAsString = jsonElement.getAsJsonObject().getAsJsonPrimitive("actionType").getAsString();
        final VariableType.ACTION_TYPE actionType = getActionTypeByName(actionTypeAsString);
        switch (actionType) {
            case CLICK:
                return context.deserialize(jsonElement, SeleniumClickable.class);
            case WRITE:
            case DELETE:
                return context.deserialize(jsonElement, SeleniumInput.class);
            case CONDITION:
                return context.deserialize(jsonElement, SeleniumCondition.class);
            case CLI:
                return context.deserialize(jsonElement, ConsoleCommand.class);
            case NAVIGATE:
                return context.deserialize(jsonElement, SeleniumNavigate.class);
            case LINK_CHECK:
                return context.deserialize(jsonElement, SeleniumLinkCheck.class);
            case LIST:
                return context.deserialize(jsonElement, SeleniumListElement.class);
            case EXTRACT:
                return context.deserialize(jsonElement, SeleniumExtract.class);
            default:
                throw new JsonParseException(String.format(Message.M_0043, actionType));
        }

    }

    private VariableType.ACTION_TYPE getActionTypeByName(String _name){
        for(VariableType.ACTION_TYPE actionType : VariableType.ACTION_TYPE.values()) {
            if(actionType.getName().equals(_name)){
                return actionType;
            }
        }
        return null;
    }
}
