package Yousof.HollowKnight.Model.entities.npc.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils; // اضافه شد برای انتخاب رندوم
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.npc.Zote;
import Yousof.HollowKnight.Utils.animation.AnimationManager;
import Yousof.HollowKnight.Utils.camera.CameraSession;
import Yousof.HollowKnight.Utils.camera.state.CameraDialogueState;
import Yousof.HollowKnight.Utils.camera.state.CameraKnightState;

public class ZoteDialogueState extends ZoteState {

    private Texture dialogueBoxTexture;
    private BitmapFont font;
    
    private Array<String> dialogueLines;
    private int currentLineIndex = 0;
    
    private String currentFullText = "";
    private String displayText = "";
    private float typeTimer = 0;
    private float charSpeed = 0.04f; 

    // ۵۷ قانون معروف زوت (تعدادی از بهترین‌ها برای دفعات بعدی هم‌صحبتی)
    private static final String[] ZOTE_PRECEPTS = {
        "Always Win Your Battles.",
        "Never Let Them Laugh at You.",
        "Always Be Rested.",
        "Forget Your Past.",
        "Strength Beats Strength.",
        "Choose Your Own Fate.",
        "Mourn Not the Dead.",
        "Travel Alone.",
        "Keep Your Home Clean.",
        "Keep Your Weapon Sharp.",
        "Respect Your Superiors.",
        "Believe In Your Strength.",
        "Don't Peer Into the Darkness.",
        "Up is Up, Down is Down.",
        "One Thing Is Not Another Thing.",
        "Obey All Precepts."
    };

    public ZoteDialogueState() {
        dialogueBoxTexture = new Texture(Gdx.files.internal("ui/dialogueBox.png"));
        Skin skin = new Skin(Gdx.files.internal("ui/uiSkin.json"));
        this.font = skin.getFont("default");
        CameraSession.getInstance().changeState(new CameraDialogueState());
        
        dialogueLines = new Array<>();
    }

    @Override
    public void enter(Zote enemy) {
        super.enter(enemy);
        currentAnimation = AnimationManager.Zote.create("Talk", PlayMode.LOOP, 0.08f);
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        enemy.getSurroundSensor().knight.setCanMove(false);

        setupDialogueLines();
        
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
        else {
            String randomPrecept = ZOTE_PRECEPTS[MathUtils.random(0, ZOTE_PRECEPTS.length - 1)];
            
            dialogueLines.add("Listen carefully to the wisdom of Zote!");
            dialogueLines.add(randomPrecept);
        }
    }

    private void showLine(int index) {
        if (index >= dialogueLines.size) {
            enemy.changeState(new ZoteIdleState());
            return;
        }
        
        currentFullText = dialogueLines.get(index);
        displayText = "";
        typeTimer = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        
        if (displayText.length() < currentFullText.length()) {
            typeTimer += delta;
            int characterCount = (int) (typeTimer / charSpeed);
            if (characterCount > currentFullText.length()) {
                characterCount = currentFullText.length();
            }
            displayText = currentFullText.substring(0, characterCount);
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (displayText.length() < currentFullText.length()) {
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

        drawX = CameraSession.getInstance().getCamera().position.x - (dialogueBoxTexture.getWidth() / 2f);
        float targetWidth = dialogueBoxTexture.getWidth() - 40f;
        batch.draw(dialogueBoxTexture, drawX, drawY + 200f);
        font.draw(batch, displayText, CameraSession.getInstance().getCamera().position.x - (targetWidth/2) , drawY + 310f , targetWidth , Align.center , true);

        drawEffects(batch, stateTime);
    }

    @Override
    public void exit(){
        super.exit();
        CameraSession.getInstance().changeState(new CameraKnightState());
        enemy.getSurroundSensor().knight.setCanMove(true);
    }
}