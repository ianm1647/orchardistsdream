package com.ianm1647.orchardistsdream.client.recipebook;

public enum JuicerRecipeBookTab {
    DRINKS("drinks"),
    MISC("misc");

    public final String name;

    private JuicerRecipeBookTab(String name) {
        this.name = name;
    }

    public static JuicerRecipeBookTab findByName(String name) {
        JuicerRecipeBookTab[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            JuicerRecipeBookTab value = var1[var3];
            if (value.name.equals(name)) {
                return value;
            }
        }

        return null;
    }

    public String toString() {
        return this.name;
    }
}
