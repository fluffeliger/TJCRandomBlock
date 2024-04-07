package de.fluffy.tjc.random_block.game.kits;

public enum KitPricing {

    CHEAP(50),
    LIL_CHEAP(70),
    LIL_EXPENSIVE(100),
    EXPENSIVE(130);

    private final int price;

    KitPricing(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
