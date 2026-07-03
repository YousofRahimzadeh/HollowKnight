package Yousof.HollowKnight.Model.entities.npc.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.npc.Zote;
import Yousof.HollowKnight.Utils.CameraSession;
import Yousof.HollowKnight.Utils.state.CameraDialogueState;
import Yousof.HollowKnight.Utils.state.CameraKnightState;

public class ZoteDialogueState extends ZoteState {

    // private Texture dialogueBoxTexture;
    private BitmapFont font;
    
    private Array<String> dialogueLines;
    private int currentLineIndex = 0;
    
    private String currentFullText = "";
    private String displayText = "";
    private float typeTimer = 0;
    private float charSpeed = 0.04f; 

    public ZoteDialogueState() {
        // لود کردن ابزارهای ساده (تکسچر باکس مشکی و فونت متنی)
        // dialogueBoxTexture = new Texture(Gdx.files.internal("ui/dialogue_box.png"));
        Skin skin = new Skin(Gdx.files.internal("ui/uiSkin.json"));
        this.font = skin.getFont("title");
        CameraSession.getInstance().changeState(new CameraDialogueState());
        
        dialogueLines = new Array<>();
    }

    @Override
    public void enter(Zote enemy) {
        super.enter(enemy);
        // ۱. اجرای انیمیشن صحبت کردن زوت (Idle متحرک)
        currentAnimation = Animations.Zote.create("Talk", PlayMode.LOOP, 0.08f);
        
        // ۲. فریز کردن حرکت زوت در Box2D
        body.setLinearVelocity(0, body.getLinearVelocity().y);

        // ۳. متوقف کردن پلیر (اینجا متد فریز کردن پلیرت را صدا بزن)
        // enemy.getSurroundSensor().knight.setCanMove(false);

        // ۴. پر کردن لیست دیالوگ‌ها (سیستم داینامیک دفعات اول و بعدی)
        setupDialogueLines();
        
        // ۵. آماده‌سازی خط اول دیالوگ
        currentLineIndex = 0;
        showLine(currentLineIndex);


    }

    private void setupDialogueLines() {
        dialogueLines.clear();
        if (!enemy.hasTalkedOnce()) {
            dialogueLines.add("I am Zote the Mighty, a knight of great renown!");
            dialogueLines.add("Do not stand in my way, insect!");
            dialogueLines.add("Curse you, I am busy contemplating my greatness.");
            enemy.setTalkedOnce(true);
        } 
        // else {
        //     // دفعات بعدی: انتخاب یکی از قوانین معروف زوت (۱۰ نمره امتیازی)
        //     // فرض می‌کنیم آرایه‌ای از قوانین در کلاس زوت داری
        //     String randomPrecept = enemy.getRandomPrecept(); 
        //     dialogueLines.add(randomPrecept);
        // }
    }

    private void showLine(int index) {
        if (index >= dialogueLines.size) {
            // اگر دیالوگ‌ها تمام شد، زوت به حالت Idle عادی برمی‌گردد
            // پلیر را هم اینجا آزاد کن: enemy.getSurroundSensor().knight.setCanMove(true);
            enemy.changeState(new ZoteIdleState());
            return;
        }
        
        currentFullText = dialogueLines.get(index);
        displayText = "";
        typeTimer = 0;
        
        // // پخش صدای رندوم غرغر زوت با شروع هر خط جدید (۱۰ نمره)
        // if (enemy.getVoices() != null && enemy.getVoices().size > 0) {
        //     int randomIdx = MathUtils.random(0, enemy.getVoices().size - 1);
        //     enemy.getVoices().get(randomIdx).play();
        // }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        
        // الف) مدیریت افکت تایپ حرف‌به‌حرف
        if (displayText.length() < currentFullText.length()) {
            typeTimer += delta;
            int characterCount = (int) (typeTimer / charSpeed);
            if (characterCount > currentFullText.length()) {
                characterCount = currentFullText.length();
            }
            displayText = currentFullText.substring(0, characterCount);
        }
        
        // ب) زدن کلید ENTER برای ورق زدن یا کامل کردن متن
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (displayText.length() < currentFullText.length()) {
                // اگر متن هنوز کامل تایپ نشده، با اینتر اول کاملش کن
                displayText = currentFullText;
            } else {
                currentLineIndex++;
                showLine(currentLineIndex);
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime);
        if (enemy.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!enemy.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 35f;
        batch.draw(currentFrame, drawX, drawY);

        // Matrix4 oldProjection = batch.getProjectionMatrix().cpy();
        
        // batch.setProjectionMatrix(enemy.getHudCamera().combined);
        
        // batch.draw(dialogueBoxTexture, 50, 20, 700, 120);
        
        font.draw(batch, displayText, drawX, drawY + 200f);
        
        // batch.setProjectionMatrix(oldProjection);

        drawEffects(batch, stateTime);
    }

    @Override
    public void exit(){
        super.exit();
        CameraSession.getInstance().changeState(new CameraKnightState());
    }
}