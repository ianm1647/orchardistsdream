package com.ianm1647.orchardistsdream.data.condition;

import com.google.gson.JsonObject;
import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.ODConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class EnabledCondition implements ICondition {
    private static final ResourceLocation NAME = new ResourceLocation(OrchardistsDream.MODID,"enabled");
    private final String value;

    public EnabledCondition(String value) {
        this.value = value;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(IContext context) {
        return ODConfig.verify(this.value);
    }

    @Override
    public String toString() {
        return "enabled(\"" + this.value + "\")";
    }

    public static class Serializer implements IConditionSerializer<EnabledCondition> {
        public static final EnabledCondition.Serializer INSTANCE = new EnabledCondition.Serializer();

        @Override
        public void write(JsonObject json, EnabledCondition condition) {
            json.addProperty("value", condition.value);
        }

        @Override
        public EnabledCondition read(JsonObject json) {
            return new EnabledCondition(GsonHelper.getAsString(json, "value"));
        }

        @Override
        public ResourceLocation getID() {
            return EnabledCondition.NAME;
        }
    }
}
