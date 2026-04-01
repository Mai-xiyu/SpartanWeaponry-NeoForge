package org.xiyu.spartanweaponryunofficial.data;

// TODO: MC 26.1 - BlockStateProvider removed (depends on ExistingFileHelper)
// Stub: Block model generation disabled until new datagen API is implemented
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import java.util.concurrent.CompletableFuture;

public class ModBlockModelProvider implements DataProvider {
    public ModBlockModelProvider(PackOutput output) {}

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return "Spartan Weaponry Block Models (STUB)";
    }
}