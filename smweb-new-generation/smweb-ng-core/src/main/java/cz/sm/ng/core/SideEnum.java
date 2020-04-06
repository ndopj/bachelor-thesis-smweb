package cz.sm.ng.core;

public enum SideEnum
{

    NONE("None"),
    RED("Red"),
    BLUE("Blue");




//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    private String label = "";



//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////



    SideEnum(String lab)
    {
        label = lab;
    }


// ======================================================================================


    public String getLabel()
    {
        return label;
    }


// ======================================================================================

    public void setLabel(String label)
    {
        this.label = label;
    }

// ======================================================================================

    /**
     * Vrati CSS identifikator farby, ktory nalezi danej strane.
     */
    public String getColorIdentifier()
    {
        switch (this) {
            case NONE:
                return "grey";
            case RED:
                return "red";
            case BLUE:
                return "blue";
            default:
                return "grey";
        }
    }

    public String getNationShortcut()
    {
        switch (this) {
            case NONE:
                return "nn";
            case RED:
                return "gb";
            case BLUE:
                return "de";
            default:
                return "nn";
        }
    }

// ======================================================================================



    /**
     * Vrati hodnotu podla ordinalneho indexu.
     */
    public static SideEnum fromOrdinal(int ordinalIndex)
    {
        return SideEnum.values()[ordinalIndex];
    }

// ======================================================================================


    /**
     * Konstruktor SideEnum-u z anglickeho nazvu strany ("Red", "Blue" alebo "None").
     *
     * @param sideName
     * @return
     */
    public static SideEnum fromEnglishSideName(String sideName)
    {
        String lowerSideName = sideName.toLowerCase().trim();
        if ("none".equals(lowerSideName)) {
            return SideEnum.NONE;
        } else if ("red".equals(lowerSideName)) {
            return SideEnum.RED;
        } else if ("blue".equals(lowerSideName)) {
            return SideEnum.BLUE;
        } else if ("nn".equals(lowerSideName)) {
            return SideEnum.NONE;
        } else if ("de".equals(lowerSideName)) {
            return SideEnum.BLUE;
        } else if ("gb".equals(lowerSideName)) {
            return SideEnum.RED;
        } else {
            return SideEnum.NONE;		// <-- error-tolerant moznost - vratim NONE.
        }
    }

// ======================================================================================

    public static SideEnum getOpposite(SideEnum side) {
        return side == SideEnum.RED ? SideEnum.BLUE : SideEnum.RED;
    }

}

