package org.xiyu.spartanweaponryunofficial.command;

import java.util.function.Predicate;
import net.minecraft.world.item.alchemy.Potion;

public class PotionInput implements Predicate<Potion> {
    private final Potion potion;

    public PotionInput(Potion potionIn) {
        this.potion = potionIn;
    }

    public Potion getEffect() {
        return this.potion;
    }

    @Override
    public boolean test(Potion t) {
        return this.potion == t;
    }
}
