package dev.ithundxr.createnumismatics.content.coins;

import net.minecraft.nbt.CompoundTag;

public interface CoinBag {


    /**
     * @return Couple of (amount of this coin, remainder of spurs)
     */

    int getValue();

    default boolean isEmpty() {
        return getValue() == 0;
    }

    CompoundTag save(CompoundTag nbt);

    void load(CompoundTag nbt);

    void clear();
}