package cz.sm.ng.core.clodwar.missiongenerator;

import com.google.gson.annotations.SerializedName;

/**
 * Triedy lietadiel pre ClodWar.
 * @author stolarik <456173@fi.muni.cz>
 */
public enum ClodWarPlaneClass
{
    @SerializedName("1")
    FIGHTER(1),
    @SerializedName("2")
    FIGHTER_BOMBER(2),
    @SerializedName("3")
    LIGHT_BOMBER(3),
    @SerializedName("4")
    MEDIUM_BOMBER(4),
    @SerializedName("5")
    HEAVY_BOMBER(5),
    @SerializedName("6")
    RESCUE(6),
    @SerializedName("7")
    RECON(7),
    @SerializedName("8")
    TRANSPORT(8);

    private final int value;

    private ClodWarPlaneClass(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static ClodWarPlaneClass fromInteger(int x)
    {
        switch (x) {
            case 1:
                return FIGHTER;
            case 2:
                return FIGHTER_BOMBER;
            case 3:
                return LIGHT_BOMBER;
            case 4:
                return MEDIUM_BOMBER;
            case 5:
                return HEAVY_BOMBER;
            case 6:
                return RESCUE;
            case 7:
                return RECON;
            case 8:
                return TRANSPORT;
        }
        return null;
    }
}

