package com.example.tamagodowork.bottomNav.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.tamagodowork.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

public class ProfilePicView extends androidx.appcompat.widget.AppCompatImageView {

    private static final float length = 137.6f;
    private static final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            (int) length, (int) length);

    private final Context context;
    private Pet pet;

    private final Path mouthPath = new Path();

    private final Paint bodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint eyePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mouthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap bitmapEyes;

    public ProfilePicView(Context context, Pet pet) {
        super(context);
        this.context = context;
        this.pet = pet;
        init();
    }

    public ProfilePicView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ProfilePicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ProfilePicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        this.setLayoutParams(params);

        this.bodyPaint.setStyle(Paint.Style.FILL);

        this.eyePaint.setColor(ContextCompat.getColor(context, R.color.brown));
        this.eyePaint.setStyle(Paint.Style.FILL);

        this.mouthPaint.setColor(ContextCompat.getColor(context, R.color.brown));
        this.mouthPaint.setStyle(Paint.Style.STROKE);
        this.mouthPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mouthPaint.setStrokeWidth(6.4f);

        setBodyColour(this.pet.getBodyColour());
        setCustomEyes(this.pet.getAcc_eyes());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(length/2, length/2, length/2, bodyPaint);

        float cy = 57.6f;
        float cx_right = length/2 - 32f;
        float cx_left = length/2 + 32f;

        canvas.drawCircle(cx_right, cy, 6.4f, eyePaint);
        canvas.drawCircle(cx_left, cy, 6.4f, eyePaint);

        mouthPath.moveTo(cx_left, cy + 25.6f);
        mouthPath.quadTo(length/2, cy + 38.4f, cx_right, cy + 25.6f);
        canvas.drawPath(mouthPath, mouthPaint);

        drawAcc_eyes(canvas);
    }

    private void drawAcc_eyes(Canvas canvas) {
        if (this.bitmapEyes == null) return;
        canvas.drawBitmap(
                this.bitmapEyes,
                0f,
                25.6f,
                null);
    }

    public void setBodyColour(Integer id) {
        if (id == null) return;
        this.bodyPaint.setColor(ContextCompat.getColor(context, id));
    }

    public void setCustomEyes(Integer id) {
        if (id == null || id == R.mipmap.none) {
            this.bitmapEyes = null;
            return;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        // resize the bitmap
        this.bitmapEyes = getResizedBitmap(bitmap, 56f, length);
    }


    // function that is used to resize the bitmap
    public Bitmap getResizedBitmap(Bitmap bm, float newHeight, float newWidth) {
        if (bm == null) return null;
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate and return the new Bitmap
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
}
