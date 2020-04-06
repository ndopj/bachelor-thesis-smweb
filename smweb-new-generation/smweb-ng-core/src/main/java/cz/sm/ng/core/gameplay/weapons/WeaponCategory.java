package cz.sm.ng.core.gameplay.weapons;

/**
 * Enum reprezentujuci kategorie municii.
 *
 * @author Veronika Martosova
 */
public enum WeaponCategory
{
    OTHER(0),
    BOMB(1),
    ROCKET(2),
    MUNITION(3),
    FUEL(4),
    SUPPLY(5);


    private final int value;

    public int getValue() {
        return value;
    }

    private WeaponCategory(int value) {
        this.value = value;
    }

}

