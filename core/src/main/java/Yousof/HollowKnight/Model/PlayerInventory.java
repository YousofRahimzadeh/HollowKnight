package Yousof.HollowKnight.Model;

import java.util.ArrayList;
import java.util.List;

import Yousof.HollowKnight.Enum.CharmEnum;

public class PlayerInventory {
    public static final int MAX_NOTCHES = 3;
    private static final List<CharmEnum> equippedCharms = new ArrayList<>();

    public static boolean equipCharm(CharmEnum charm) {
        if (equippedCharms.size() < MAX_NOTCHES && !equippedCharms.contains(charm)) {
            equippedCharms.add(charm);
            return true;
        }
        return false;
    }

    public static void unequipCharm(CharmEnum charm) {
        equippedCharms.remove(charm);
    }

    public static boolean isEquipped(CharmEnum charm) {
        return equippedCharms.contains(charm);
    }

    public static List<CharmEnum> getEquippedCharms() {
        return equippedCharms;
    }

    public static int getUsedNotches() {
        return equippedCharms.size();
    }
}