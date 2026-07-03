package Yousof.HollowKnight.Utils.state;

import com.badlogic.gdx.math.MathUtils;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Utils.CameraSession;

public class CameraVibrationState extends CameraState {

    private float shakeDuration;   // کل زمان لرزش به ثانیه
    private float shakeTimer;      // تایمر فعلی
    private float shakeIntensity;  // شدت لرزش (به پیکسل)

    public CameraVibrationState(float duration, float intensity) {
        this.shakeDuration = duration;
        this.shakeIntensity = intensity;
        this.shakeTimer = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (game.getKnight() == null || game.getKnight().getBody() == null) return;

        // ۱. ابتدا تعقیب عادی شوالیه با lerp (دقیقاً مثل کد قبلی خودت)
        float targetX = MathUtils.lerp(camera.position.x, game.getKnight().getBody().getPosition().x * Constants.PPM, 0.1f);
        float targetY = MathUtils.lerp(camera.position.y, game.getKnight().getBody().getPosition().y * Constants.PPM, 0.1f);

        // ۲. مدیریت تایمر لرزش
        if (shakeTimer < shakeDuration) {
            shakeTimer += delta;

            // ۳. ایجاد یک آفست تصادفی بر اساس شدت لرزش
            // MathUtils.random(-1f, 1f) یک عدد تصادفی بین ۱- و ۱+ می‌دهد
            float offsetX = MathUtils.random(-1f, 1f) * shakeIntensity;
            float offsetY = MathUtils.random(-1f, 1f) * shakeIntensity;

            // ۴. اعمال لرزش روی موقعیت نهایی دوربین
            camera.position.x = targetX + offsetX;
            camera.position.y = targetY + offsetY;

            // افکت فرسایشی (اختیاری): هرچه زمان می‌گذرد شدت لرزش کمتر و نرم‌تر شود
            shakeIntensity = MathUtils.lerp(shakeIntensity, 0, delta * 5);

        } else {
            // ۵. وقتی زمان لرزش تمام شد، دوربین به وضعیت عادی (تعقیب نایت) برمی‌گردد
            // فرض می‌کنم در کلاس CameraSession متدی برای تغییر وضعیت داری
            CameraSession.getInstance().changeState(new CameraKnightState());
        }
    }
}