package Yousof.HollowKnight.Model.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane.PlaneSide;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class GameHUD {
    private OrthographicCamera hudCamera;
    private ScreenViewport hudViewport;
    
    private FrameBuffer fbo;
    private TextureRegion fboRegion;

    private Texture vesselMask;      // کادر داخلی یا ماسک دایره روح
    private Texture vesselBorder;    // کادر ظاهری نقره‌ای (HealthBar.png)
    
    private Animation<TextureRegion> liquidAnimation; 
    private Animation<TextureRegion> containerAnimation;

    private HeartIcon[] hearts;
    private final int MAX_HEALTH = 5;
    private int lastKnownHp;

    private float animationTime = 0f;
    private float animatedSoul = 0f;
    private Knight knight;

    public GameHUD(Knight knight) {
        this.knight = knight;
        this.liquidAnimation = Animations.Soul.create("Soulorb", PlayMode.LOOP, 0.1f);
        this.containerAnimation = Animations.SoulContainer.create("HealthBar", PlayMode.NORMAL, 0.1f);
        
        hudCamera = new OrthographicCamera();
        hudViewport = new ScreenViewport(hudCamera);
        
        vesselMask = new Texture(Gdx.files.internal("animations/Atlas/HUD/HealthBarMask.png"));  
        
        recreateFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.knight = knight;
        this.lastKnownHp = knight.getCurrentMasks();
    
        hearts = new HeartIcon[MAX_HEALTH];
        for (int i = 0; i < MAX_HEALTH; i++) {
            hearts[i] = new HeartIcon();
            if (i >= lastKnownHp) {
                hearts[i].changeState(HeartIcon.HeartState.REFILLING);
            }
        }
    }

    private void recreateFBO(int width, int height) {
        if (fbo != null) fbo.dispose();
        
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboRegion.flip(false, true);
    }

    public void render(SpriteBatch batch, float delta) {
        animationTime += delta;

        hudCamera.update();
        batch.setProjectionMatrix(hudCamera.combined);
        
        float vesselX = hudCamera.position.x - hudViewport.getScreenWidth() / 2f + 40;
        float vesselY = hudCamera.position.y + hudViewport.getScreenHeight() / 2f - 200;

        animatedSoul = MathUtils.lerp(animatedSoul, knight.getCurrentSoul(), delta * 5f);
        float soulPercentage = animatedSoul / 99f;

        fbo.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        TextureRegion currentFrame = liquidAnimation.getKeyFrame(animationTime);
        float frameHeight = currentFrame.getRegionHeight();
        float frameWidth = currentFrame.getRegionWidth();
        float liquidY = vesselY - frameHeight + (frameHeight * soulPercentage);
        batch.draw(currentFrame, vesselX + 10f , liquidY, frameWidth, frameHeight);
        batch.flush();
        batch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ZERO);
        batch.draw(vesselMask, vesselX - 20f, vesselY - 220f);
        batch.flush(); 
        batch.end();
        fbo.end();




        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        TextureRegion currentSoulContainer = containerAnimation.getKeyFrame(animationTime);
        batch.draw(currentSoulContainer, vesselX, vesselY);
        batch.draw(fboRegion, hudCamera.position.x - hudViewport.getWorldWidth()/2f, 
                              hudCamera.position.y - hudViewport.getWorldHeight()/2f);
        updateHealthSystem(delta);

        float heartStartX = vesselX + 160;
        float heartY = vesselY + 30;       

        for (int i = 0; i < MAX_HEALTH; i++) {
            TextureRegion heartFrame = hearts[i].getCurrentFrame();
            float currentHeartX = heartStartX + (i * 68f); 
            batch.draw(heartFrame, currentHeartX, heartY, heartFrame.getRegionWidth(), heartFrame.getRegionHeight());
        }
        batch.end();

    }


    private void updateHealthSystem(float delta) {
    int currentHp = knight.getCurrentMasks(); 

    if (currentHp != lastKnownHp) {
        
        for (int i = 0; i < MAX_HEALTH; i++) {

            if (i < currentHp) {
                if (hearts[i].getState() == HeartIcon.HeartState.EMPTY) {
                    hearts[i].changeState(HeartIcon.HeartState.REFILLING);
                }
            } else {
                if (hearts[i].getState() == HeartIcon.HeartState.FILLED) {
                    hearts[i].changeState(HeartIcon.HeartState.BREAKING);
                }
            }
        }
        lastKnownHp = currentHp;
    }

    for (HeartIcon heart : hearts) {
        heart.update(delta);
    }
}

    public void resize(int width, int height) {
        hudViewport.update(width, height, false);
        hudCamera.position.set(hudViewport.getScreenWidth() / 2f, hudViewport.getScreenHeight() / 2f, 0);
        // بازسازی FBO به ابعاد جدید صفحه برای جلوگیری از کشیدگی تصویر
        recreateFBO(width, height);
    }


    
    public void dispose() {
        if (vesselMask != null) vesselMask.dispose();
        if (vesselBorder != null) vesselBorder.dispose();
        if (fbo != null) fbo.dispose();
    }
}