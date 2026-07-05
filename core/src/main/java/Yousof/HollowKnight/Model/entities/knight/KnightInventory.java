package Yousof.HollowKnight.Model.entities.knight;

import Yousof.HollowKnight.Enum.CharmEnum;
import java.util.ArrayList;
import java.util.List;

public class KnightInventory {
    public static final int MAX_NOTCHES = 3;
    private final List<CharmEnum> equippedCharms;

    public KnightInventory() {
        this.equippedCharms = new ArrayList<>();
    }

    public boolean equipCharm(CharmEnum charm) {
        if (equippedCharms.size() < MAX_NOTCHES && !equippedCharms.contains(charm)) {
            equippedCharms.add(charm);
            return true;
        }
        return false;
    }

    public void unequipCharm(CharmEnum charm) {
        equippedCharms.remove(charm);
    }

    public boolean isEquipped(CharmEnum charm) {
        return equippedCharms.contains(charm);
    }

    public List<CharmEnum> getEquippedCharms() {
        return equippedCharms;
    }

    public int getUsedNotches() {
        return equippedCharms.size();
    }
}