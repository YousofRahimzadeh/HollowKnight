package Yousof.HollowKnight.Enum;

import com.badlogic.gdx.Input;


public enum KeysSettings {
    KNIGHTUP(Input.Keys.UP),
    KNIGHTDOWN(Input.Keys.DOWN),
    KNIGHTRIGHT(Input.Keys.RIGHT),
    KNIGHTLEFT(Input.Keys.LEFT),
    KNIGHTLOOKUP(Input.Keys.UP),
    KNIGHTLOOKDOWN(Input.Keys.DOWN),
    KNIGHTJUMP(Input.Keys.Z),
    KNIGHTATTACK(Input.Keys.X),
    KNIGHTFOCUS(Input.Keys.A),
    KNIGHTVENGEFUL(Input.Keys.V),
    KNIGHTSCREAM(Input.Keys.S),
    KNIGHTDASH(Input.Keys.C);

    public int key;

	private KeysSettings(int key){
        this.key = key;
    }
    public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
}

