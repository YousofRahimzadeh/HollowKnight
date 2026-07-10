package Yousof.HollowKnight.Screen.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Yousof.HollowKnight.Enum.CharmEnum;
import Yousof.HollowKnight.Enum.GameText;
import Yousof.HollowKnight.Manager.LocalizationManager;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Model.entities.knight.KnightInventory;
import Yousof.HollowKnight.Screen.Modal;

public class InventoryModal extends Modal {
    
    private Table equippedTable;  
    private Table unequippedTable;
    private Label charmTitleLabel;
    private Label charmDescLabel;
    private KnightInventory inventory;
    private TextureRegionDrawable emptyNotchDrawable;

    public InventoryModal() {
        super();
        this.setFillParent(true);
        this.center();
        
        // Initialize inventory and background
        inventory = GameSession.getInstance().getKnight().getInventory();
        Texture bgTexture = new Texture(Gdx.files.internal("ui/modalBackgrounds.png")); 
        this.setBackground(new TextureRegionDrawable(bgTexture));
        
        emptyNotchDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("animations/Atlas/Charms/nullCharm.png")));

        Table mainContainer = new Table(skin);
        mainContainer.center().pad(40);

        // Setup left panel for charms list
        Table leftPanel = new Table();
        leftPanel.add(new Label(LocalizationManager.get(GameText.INVENTORY_EQUIPPED), skin)).left().padBottom(10).row();
        equippedTable = new Table(); 
        leftPanel.add(equippedTable).left().padBottom(40).row();
        
        leftPanel.add(new Label(LocalizationManager.get(GameText.INVENTORY_CHARMS), skin)).left().padBottom(10).row();
        unequippedTable = new Table(); 
        leftPanel.add(unequippedTable).left().row();

        // Setup right panel for descriptions
        Table rightPanel = new Table();
        charmTitleLabel = new Label(LocalizationManager.get(GameText.INVENTORY_SELECT_CHARM), skin);
        charmTitleLabel.setFontScale(1.2f);
        charmDescLabel = new Label(LocalizationManager.get(GameText.INVENTORY_CLICK_HINT), skin);
        charmDescLabel.setWrap(true); 
        
        rightPanel.add(charmTitleLabel).center().padBottom(20).row();
        rightPanel.add(charmDescLabel).width(300).center().top();

        // Assemble main layout
        mainContainer.add(leftPanel).expandX().left().padRight(50);
        mainContainer.add(rightPanel).width(350).expandX().right();
        this.add(mainContainer);

        refreshTables();
    }

    // Rebuild grid views based on current data
    private void refreshTables() {
        equippedTable.clear();
        unequippedTable.clear();

        // Populate equipped notches row
        for (int i = 0; i < KnightInventory.MAX_NOTCHES; i++) {
            if (i < inventory.getUsedNotches()) {
                CharmEnum activeCharm = inventory.getEquippedCharms().get(i);
                ImageButton charmBtn = createCharmButton(activeCharm, true);
                equippedTable.add(charmBtn).size(70, 70).padRight(15);
            } else {
                ImageButton emptyNotchBtn = new ImageButton(emptyNotchDrawable);
                equippedTable.add(emptyNotchBtn).size(70, 70).padRight(15);
            }
        }

        // Populate all available charms grid
        int col = 0;
        for (CharmEnum charm : CharmEnum.values()) {
            ImageButton charmBtn = createCharmButton(charm, false);
            
            if (inventory.isEquipped(charm)) {
                charmBtn.getColor().a = 0.3f; 
                charmBtn.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled); 
            }
            
            unequippedTable.add(charmBtn).size(70, 70).pad(10);
            col++;
            if (col == 4) { 
                unequippedTable.row();
                col = 0;
            }
        }
    }

    // Helper method to instantiate charm interactive buttons
    private ImageButton createCharmButton(final CharmEnum charm, final boolean isEquippedButton) {
        Texture texture = new Texture(Gdx.files.internal(charm.getTexturePath()));
        TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
        ImageButton btn = new ImageButton(drawable);

        btn.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                charmTitleLabel.setText(charm.getTitle());
                charmDescLabel.setText(charm.getDescription());
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                charmTitleLabel.setText("");
                charmDescLabel.setText("");
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isEquippedButton) {
                    inventory.unequipCharm(charm);
                    refreshTables();
                } else {
                    boolean success = inventory.equipCharm(charm);
                    if (success) {
                        refreshTables();
                    } else {
                        charmTitleLabel.setText(LocalizationManager.get(GameText.INVENTORY_NOTCHES_FULL));
                        charmDescLabel.setText(LocalizationManager.get(GameText.INVENTORY_UNEQUIP_FIRST));
                    }
                }
            }
        });

        return btn;
    }
}