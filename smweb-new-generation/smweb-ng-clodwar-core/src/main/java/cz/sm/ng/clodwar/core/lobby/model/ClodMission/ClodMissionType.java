package cz.sm.ng.clodwar.core.lobby.model.ClodMission;

import com.google.gson.annotations.SerializedName;

/**
 * Enum s jednotlivymi typy her
 *
 * @author Jaroslav Rehorka <xrehorka@fi.muni.cz>
 */
public enum ClodMissionType
{
    @SerializedName("1")
    UTILITY(1, "Utility"),
    @SerializedName("2")
    TRAINING(2, "Training"),
    @SerializedName("3")
    FREE_FLIGHT(3, "Free flight"),
    @SerializedName("4")
    TACTICAL_GROUND_ATTACK(4, "Tactical ground attack"),
    @SerializedName("5")
    TACTICAL_GRAOUND_ATTACK_W_COVER(5, "Ground attack"),
    @SerializedName("6")
    STRATEGIC_GROUND_ATTACK(6, "Ground attack"),
    @SerializedName("7")
    RESCUE(7, "Rescue"),
    @SerializedName("8")
    AIR_TRANSPORT(8, "Air transport"),
    @SerializedName("9")
    AIR_TRANSPORT_W_COVER(9, "Air transport"),
    @SerializedName("10")
    PATROL(10, "Patrol"),
    @SerializedName("11")
    RECON(11, "Recon"),
    @SerializedName("12")
    RECON_W_COVER(12, "Recon"),
    @SerializedName("13")
    CLOSE_AIR_SUPPORT(13, "Close air support"),
    @SerializedName("14")
    SYSTEM_TRANSPORT(14, "system"),
    @SerializedName("15")
    SYSTEM_DEVIRTUALIZE(16, "system");


    private final int value;
    private final String description;

    public int getValue() {
        return value;
    }

    private ClodMissionType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * Slouzi ke ziskani cisla zastupujici konstantu
     *
     * @param x
     * @return
     */
    public static ClodMissionType fromInteger(int x) {
        switch (x) {
            case 1:
                return UTILITY;
            case 2:
                return TRAINING;
            case 3:
                return FREE_FLIGHT;
            case 4:
                return TACTICAL_GROUND_ATTACK;
            case 5:
                return TACTICAL_GRAOUND_ATTACK_W_COVER;
            case 6:
                return STRATEGIC_GROUND_ATTACK;
            case 7:
                return RESCUE;
            case 8:
                return AIR_TRANSPORT;
            case 9:
                return AIR_TRANSPORT_W_COVER;
            case 10:
                return PATROL;
            case 11:
                return RECON;
            case 12:
                return RECON_W_COVER;
            case 13:
                return CLOSE_AIR_SUPPORT;
            case 14:
                return SYSTEM_TRANSPORT;
            case 15:
                return SYSTEM_DEVIRTUALIZE;
        }
        return null;
    }

    @Override
    public String toString() {
        return description;
    }


}

