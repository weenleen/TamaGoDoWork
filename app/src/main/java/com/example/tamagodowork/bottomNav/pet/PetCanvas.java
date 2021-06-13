package com.example.tamagodowork.bottomNav.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.Handler;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;

public class PetCanvas extends View {

    private float height_middle;
    private float width_middle;
    private float offset;

    private final RectF ovalTop = new RectF();
    private final RectF ovalBottom = new RectF();
    private final Path bodyPath = new Path();
    private final Path mouthPath = new Path();

    private final Paint bodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint eyePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mouthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static final int fps = 20;
    private float index = 0f;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable petIdle = new Runnable() {
        @Override
        public void run() {
            index += 0.1;
            if (index >= 8) index = 0f;
            offset = (float) Math.sin((index/4) * Math.PI) * 20;
            postInvalidate();
            handler.postDelayed(this, 1000 / fps);
        }
    };

    // customisation
    private Pet pet;

    private Bitmap bitmapHead;
    private Bitmap bitmapEyes;
    private Bitmap bitmapBody;

    private Integer bodyColour;
    private Integer acc_head;
    private Integer acc_eyes;
    private Integer acc_body;

    public PetCanvas(Context context, @NonNull Pet pet) {
        super(context);
        this.pet = pet;
        init();
    }

    public PetCanvas(Context context) {
        super(context);
        init();
    }

    public PetCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PetCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        this.bodyPaint.setStyle(Paint.Style.FILL);

        this.eyePaint.setColor(ContextCompat.getColor(getContext(), R.color.brown));
        this.eyePaint.setStyle(Paint.Style.FILL);

        this.mouthPaint.setColor(ContextCompat.getColor(getContext(), R.color.brown));
        this.mouthPaint.setStyle(Paint.Style.STROKE);
        this.mouthPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mouthPaint.setStrokeWidth(20f);

        update();

        this.petIdle.run();
    }

    private void update() {
        if (this.pet == null) return;

        setBodyColour(this.pet.getBodyColour());
        setCustomHead(this.pet.getAcc_head());
        setCustomEyes(this.pet.getAcc_eyes());
        setCustomBody(this.pet.getAcc_body());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.height_middle = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec) / 2f + 100f;
        this.width_middle = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec) / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.bodyColour == null) {
            update();
            return;
        }

        super.onDraw(canvas);
        bodyPath.reset();
        mouthPath.reset();

        // body
        float radius1 = 300f + offset;
        float radius2 = 450f - offset;

        float left = width_middle - radius1;
        float top = height_middle - radius2;
        float right = width_middle + radius1;
        float bottom = height_middle + radius2;

        this.ovalTop.set(left, top, right, bottom);
        this.ovalBottom.set(left, height_middle - radius1, right, height_middle + radius1);

        // body
        bodyPath.addArc(ovalTop, 180f, 180f);
        bodyPath.addArc(ovalBottom, 0f, 180f);
        bodyPath.close();
        canvas.drawPath(bodyPath, this.bodyPaint);

        // eyes
        float cy = height_middle - 200f + offset;
        float cx_right = width_middle - 100f - offset / 2f;
        float cx_left = width_middle + 100f + offset / 2f;

        canvas.drawCircle(cx_right, cy, 20f, eyePaint);
        canvas.drawCircle(cx_left, cy, 20f, eyePaint);

        // mouth
        mouthPath.moveTo(cx_left, cy + 80f);
        mouthPath.quadTo(width_middle, cy + 120f, cx_right, cy + 80f);
        canvas.drawPath(mouthPath, mouthPaint);

        // accessories
        drawAcc_head(canvas);
        drawAcc_eyes(canvas);
        drawAcc_body(canvas);
    }

    // 300 x 300 px
    private void drawAcc_head(Canvas canvas) {
        if (this.bitmapHead == null) return;
        canvas.drawBitmap(
                this.bitmapHead,
                width_middle - 200f,
                height_middle - 500f + offset,
                null);
    }

    // 300 x 600 px
    private void drawAcc_eyes(Canvas canvas) {
        if (this.bitmapEyes == null) return;
        canvas.drawBitmap(
                this.bitmapEyes,
                width_middle - 150f,
                height_middle - 300f + offset,
                null);
    }

    private void drawAcc_body(Canvas canvas) {
        if (this.bitmapBody == null) return;
        canvas.drawBitmap(
                this.bitmapBody,
                width_middle - 220f,
                height_middle - 100f + offset,
                null);
    }

    public void setCustom(Pet.custom custom, int id) {
        switch(custom) {
            case COLOUR:
                this.setBodyColour(id);
                break;
            case HEAD:
                this.setCustomHead(id);
                break;
            case EYES:
                this.setCustomEyes(id);
                break;
            case BODY: this.setCustomBody(id);
            break;
        }
    }

    public void setBodyColour(Integer id) {
        if (id== null) return;
        this.bodyColour = id;
        this.bodyPaint.setColor(ContextCompat.getColor(getContext(), id));
    }

    public void setCustomHead(Integer id) {
        if (id == null) {
            this.bitmapHead = null;
            return;
        }

        this.acc_head = id;
        this.bitmapHead = BitmapFactory.decodeResource(getResources(), id);
    }

    public void setCustomEyes(Integer id) {
        if (id == null) {
            this.bitmapEyes = null;
            return;
        }

        this.acc_eyes = id;
        this.bitmapEyes = BitmapFactory.decodeResource(getResources(), id);
    }

    public void setCustomBody(Integer id) {
        if (id == null) {
            this.bitmapBody = null;
            return;
        }

        this.acc_body = id;
        this.bitmapBody = BitmapFactory.decodeResource(getResources(), id);
    }

    public void save() {
        MainActivity.userDoc.collection("Pet").document("Customisation")
                .set(new Pet(this.bodyColour, this.acc_head, this.acc_eyes, this.acc_body))
                .addOnSuccessListener(unused -> Log.e("upload", "SUCCESS"))
                .addOnFailureListener(e -> Log.e("upload", "FAIL"));
    }
}