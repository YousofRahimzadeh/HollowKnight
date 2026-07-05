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
import Yousof.HollowKnight.Model.PlayerInventory;
import Yousof.HollowKnight.Screen.Modal;

public class InventoryModal extends Modal {
    
    private Table equippedTable;   // بخش ناچ‌های بالایی
    private Table unequippedTable; // بخش چارم‌های در دسترس پایینی
    private Label charmTitleLabel;
    private Label charmDescLabel;
    
    // تصاویری برای خالی بودن ناچ
    private TextureRegionDrawable emptyNotchDrawable;

    public InventoryModal() {
        super();
        this.setFillParent(true);
        this.center();

        // پس‌زمینه مدال اینونتوری (همان طرح مشکی و زیبای هالوونایت)
        Texture bgTexture = new Texture(Gdx.files.internal("ui/modalBackgrounds.png")); 
        this.setBackground(new TextureRegionDrawable(bgTexture));
        
        emptyNotchDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("animations/Atlas/Charms/nullCharm.png")));

        // کانتینر اصلی صفحه
        Table mainContainer = new Table(skin);
        mainContainer.center().pad(40);

        // ==========================================
        // پنل سمت چپ (لیست چارم‌ها)
        // ==========================================
        Table leftPanel = new Table();
        
        leftPanel.add(new Label("Equipped", skin)).left().padBottom(10).row();
        equippedTable = new Table(); // ردیف ناچ‌ها
        leftPanel.add(equippedTable).left().padBottom(40).row();
        
        leftPanel.add(new Label("Charms", skin)).left().padBottom(10).row();
        unequippedTable = new Table(); // شبکه‌ی چارم‌های در دسترس
        leftPanel.add(unequippedTable).left().row();

        // ==========================================
        // پنل سمت راست (توضیحات و عکس بزرگ چارم)
        // ==========================================
        Table rightPanel = new Table();
        charmTitleLabel = new Label("Select a Charm", skin);
        charmTitleLabel.setFontScale(1.2f);
        charmDescLabel = new Label("Click to equip or unequip.\nHover to see details.", skin);
        charmDescLabel.setWrap(true); // برای شکستن خودکار خطوط متن
        
        rightPanel.add(charmTitleLabel).center().padBottom(20).row();
        rightPanel.add(charmDescLabel).width(300).center().top();

        // اضافه کردن پنل‌ها به کانتینر اصلی
        mainContainer.add(leftPanel).expandX().left().padRight(50);
        mainContainer.add(rightPanel).width(350).expandX().right();
        
        this.add(mainContainer);

        // رسم اولیه چارم‌ها
        refreshTables();
    }

    /**
     * این متد هر بار که چارمی اضافه یا حذف می‌شود، جداول را پاک کرده و دوباره رسم می‌کند.
     */
    private void refreshTables() {
        equippedTable.clear();
        unequippedTable.clear();

        // رسم چارم‌های تجهیزشده (Notches)
        for (int i = 0; i < PlayerInventory.MAX_NOTCHES; i++) {
            if (i < PlayerInventory.getUsedNotches()) {
                // اگر این ناچ پر است، عکس چارم را بکش
                CharmEnum activeCharm = PlayerInventory.getEquippedCharms().get(i);
                ImageButton charmBtn = createCharmButton(activeCharm, true);
                equippedTable.add(charmBtn).size(70, 70).padRight(15);
            } else {
                // اگر خالی است، یک عکس ناچ خالی بکش
                ImageButton emptyNotchBtn = new ImageButton(emptyNotchDrawable);
                equippedTable.add(emptyNotchBtn).size(70, 70).padRight(15);
            }
        }

        // رسم تمام چارم‌های در دسترس بازی
        int col = 0;
        for (CharmEnum charm : CharmEnum.values()) {
            ImageButton charmBtn = createCharmButton(charm, false);
            
            // اگر چارم مجهز شده است، عکس آن را در بخش پایین نیمه‌شفاف می‌کنیم تا مشخص شود
            if (PlayerInventory.isEquipped(charm)) {
                charmBtn.getColor().a = 0.3f; // شفافیت ۳۰٪
                // غیرفعال کردن کلیک روی نسخه پایینی وقتی بالاست
                charmBtn.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled); 
            }
            
            unequippedTable.add(charmBtn).size(70, 70).pad(10);
            col++;
            if (col == 4) { // هر ۴ چارم یک ردیف جدید (بسته به تعداد چارم‌های شما)
                unequippedTable.row();
                col = 0;
            }
        }
    }

    /**
     * متد سازنده‌ی دکمه تصویری برای هر چارم با قابلیت هاور و کلیک
     */
    private ImageButton createCharmButton(final CharmEnum charm, final boolean isEquippedButton) {
        Texture texture = new Texture(Gdx.files.internal(charm.getTexturePath()));
        TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
        ImageButton btn = new ImageButton(drawable);

        btn.addListener(new ClickListener() {
            // وقتی موس روی چارم می‌رود، متن سمت راست عوض شود
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                charmTitleLabel.setText(charm.getTitle());
                charmDescLabel.setText(charm.getDescription());
            }

            // وقتی موس خارج می‌شود
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                charmTitleLabel.setText("");
                charmDescLabel.setText("");
            }

            // وقتی کلیک می‌شود
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isEquippedButton) {
                    // اگر دکمه از ردیف بالاست، پس Unequip کن
                    PlayerInventory.unequipCharm(charm);
                    refreshTables();
                } else {
                    // اگر دکمه از ردیف پایین است، تلاش کن Equip کنی
                    boolean success = PlayerInventory.equipCharm(charm);
                    if (success) {
                        refreshTables();
                    } else {
                        // در صورت پر بودن ناچ‌ها، می‌توانید اینجا یک صدای ارور پخش کنید
                        // AudioManager.playErrorSound();
                        charmTitleLabel.setText("Notches Full!");
                        charmDescLabel.setText("You must unequip a charm first.");
                    }
                }
            }
        });

        return btn;
    }
}