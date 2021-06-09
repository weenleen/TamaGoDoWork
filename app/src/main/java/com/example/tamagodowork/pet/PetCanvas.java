package com.example.tamagodowork.pet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.Handler;

import com.example.tamagodowork.R;

public class PetCanvas extends View {

    private final RectF ovalTop = new RectF();
    private final RectF ovalBottom = new RectF();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();

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
            //if (index >= fps) index = 0f;
            offset = (float) Math.sin((index/4) * Math.PI) * 20;
            postInvalidate();
            handler.postDelayed(this, 1000 / fps);
            Log.e("offset", String.valueOf(offset));
        }
    };

    public PetCanvas(Context context) {
        super(context);
        init(null);
    }

    public PetCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PetCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet set) {
        this.paint.setColor(ContextCompat.getColor(getContext(), R.color.egg_beige));
        this.paint.setStyle(Paint.Style.FILL);
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
        path.reset();

        float radius1 = 300f + offset;
        float radius2 = 450f - offset;

        Log.e("radius1", String.valueOf(radius1));
        Log.e("radius2", String.valueOf(radius2));

        // left top right bottom
        float left = width_middle - radius1;
        float top = height_middle - radius2;
        float right = width_middle + radius1;
        float bottom = height_middle + radius2;

        this.ovalTop.set(left, top, right, bottom);
        this.ovalBottom.set(left, height_middle - radius1, right, height_middle + radius1);

        path.addArc(ovalTop, 180f, 180f);
        path.addArc(ovalBottom, 0f, 180f);
        path.close();

        canvas.drawPath(path, this.paint);
    }
}
