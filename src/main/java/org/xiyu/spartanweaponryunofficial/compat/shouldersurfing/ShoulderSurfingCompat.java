package org.xiyu.spartanweaponryunofficial.compat.shouldersurfing;

public class ShoulderSurfingCompat {
    public static boolean isShoulderSurfing() {
        try {
            Class<?> api =
                    Class.forName(
                            "com.github.exopandora.shouldersurfing.api.client.IShoulderSurfing");
            Object instance;
            try {
                instance = api.getMethod("getInstance").invoke(null);
            } catch (NoSuchMethodException ignored) {
                Class<?> legacyApi =
                        Class.forName(
                                "com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing");
                instance = legacyApi.getMethod("getInstance").invoke(null);
            }
            return (boolean) api.getMethod("isShoulderSurfing").invoke(instance);
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return false;
        }
    }
}
