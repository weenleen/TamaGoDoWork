package com.example.tamagodowork.pet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.Handler;

import com.example.tamagodowork.R;

public class PetCanvas extends View {

    private final RectF ovalTop = new RectF();
    private final RectF ovalBottom = new RectF();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path bodyPath = new Path();
    private final Path mouthPath = new Path();

    private final Paint eyePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mouthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float height_middle;
    private float width_middle;
    private float offset;

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
        this.paint.setColor(ContextCompat.getColor(getContext(), R.color.egg_beige));
        this.paint.setStyle(Paint.Style.FILL);

        this.eyePaint.setColor(ContextCompat.getColor(getContext(), R.color.brown));
        this.eyePaint.setStyle(Paint.Style.FILL);

        this.mouthPaint.setColor(ContextCompat.getColor(getContext(), R.color.brown));
        this.mouthPaint.setStyle(Paint.Style.STROKE);
        this.mouthPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mouthPaint.setStrokeWidth(20f);

        this.petIdle.run();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.height_middle = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec) / 2f;
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
        canvas.drawPath(bodyPath, this.paint);

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
    }
}
