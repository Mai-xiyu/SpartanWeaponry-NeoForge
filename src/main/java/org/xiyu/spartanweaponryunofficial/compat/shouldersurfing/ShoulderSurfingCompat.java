package org.xiyu.spartanweaponryunofficial.compat.shouldersurfing;

public class ShoulderSurfingCompat {
    public static boolean isShoulderSurfing() {
        try {
            Class<?> clazz = Class.forName("com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing");
            Object instance = clazz.getMethod("getInstance").invoke(null);
            return (boolean) clazz.getMethod("isShoulderSurfing").invoke(instance);
        } catch (Throwable ignored) {
            return false;
        }
    }
}
