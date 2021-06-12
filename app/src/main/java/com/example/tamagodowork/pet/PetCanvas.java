package com.example.tamagodowork.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.Handler;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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
            index+= 0.1;
            if (index >= 8) index = 0f;
            offset = (float) Math.sin((index/4) * Math.PI) * 20;
            postInvalidate();
            handler.postDelayed(this, 1000 / fps);
        }
    };

    // customisation
    private int bodyColour = R.color.egg_beige;
    private Integer acc_head = null;
    private Integer acc_eyes = null;
    private Integer acc_body = null;

    public PetCanvas(Context context) {
        super(context);
        init(null, 0);
    }

    public PetCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PetCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void init(@Nullable AttributeSet attrs, int defStyleAttr) {
        retrieveData();

        this.bodyPaint.setStyle(Paint.Style.FILL);

        this.eyePaint.setColor(ContextCompat.getColor(getContext(), R.color.brown));
        this.eyePaint.setStyle(Paint.Style.FILL);

        this.mouthPaint.setColor(ContextCompat.getColor(getContext(), R.color.brown));
        this.mouthPaint.setStyle(Paint.Style.STROKE);
        this.mouthPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mouthPaint.setStrokeWidth(20f);

        this.petIdle.run();
    }

    private void retrieveData() {
        DocumentReference ref = MainActivity.userDoc.collection("Pet").document("Customisation");
        ref.get().addOnSuccessListener(documentSnapshot -> {
            Long tmp = (Long) documentSnapshot.get("bodyColour");
            if (tmp != null) bodyColour = tmp.intValue();
            else bodyColour = R.color.egg_beige;

            this.bodyPaint.setColor(ContextCompat.getColor(getContext(), bodyColour));
            acc_head = documentSnapshot.get("acc_head", Integer.class);
            acc_eyes = documentSnapshot.get("acc_eyes", Integer.class);
            acc_body = documentSnapshot.get("acc_body", Integer.class);
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.height_middle = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec) / 2f + 100f;
        this.width_middle = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec) / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
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
        if (this.acc_head == null) return;
        canvas.drawBitmap(
                BitmapFactory.decodeResource(getResources(), this.acc_head),
                width_middle - 200f,
                height_middle - 500f + offset,
                null);
    }

    // 300 x 600 px
    private void drawAcc_eyes(Canvas canvas) {
        if (this.acc_eyes == null) return;
        canvas.drawBitmap(
                BitmapFactory.decodeResource(getResources(), this.acc_eyes),
                width_middle - 150f,
                height_middle - 300f + offset,
                null);
    }

    private void drawAcc_body(Canvas canvas) {
        if (this.acc_body == null) return;
        canvas.drawBitmap(
                BitmapFactory.decodeResource(getResources(), this.acc_body),
                width_middle - 220f,
                height_middle - 100f + offset,
                null);
    }

    public void setBodyColour(int colour) {
        this.bodyColour = colour;
        this.bodyPaint.setColor(ContextCompat.getColor(getContext(), this.bodyColour));
    }

    public void set(int type, int id) {
        switch(type) {
            case 0:
                this.setBodyColour(id);
                break;
            case 1:
                this.setAcc_head(id);
                break;
            case 2:
                this.setAcc_eyes(id);
                break;
            case 3: this.setAcc_body(id);
            break;
        }
    }

    public void setAcc_head(int id) {
        this.acc_head = id;
    }

    public void setAcc_eyes(int id) {
        this.acc_eyes = id;
    }

    public void setAcc_body(int id) {
        this.acc_body = id;
    }

    public void save() {
        Map<String, Integer> result = new HashMap<>();
        result.computeIfAbsent("bodyColour", val -> this.bodyColour);
        result.computeIfAbsent("acc_head", val -> this.acc_head);
        result.computeIfAbsent("acc_eyes", val -> this.acc_eyes);
        result.computeIfAbsent("acc_body", val -> this.acc_body);

        MainActivity.userDoc.collection("Pet").document("Customisation")
                .set(result);
    }
}
