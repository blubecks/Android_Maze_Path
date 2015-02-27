package com.example.beccaris.percorso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by beccaris on 27/02/15.
 */
public class MyDrawable extends Drawable {
    private Path path;
    private Paint paint;
    private Bitmap bmp, bmp2;
    private Matrix matrix;
    boolean won=false;

    public MyDrawable(Context ctx, int resourceId) {
        bmp= BitmapFactory.decodeResource(ctx.getResources(),resourceId);
        bmp2=Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), Bitmap.Config.ARGB_8888);
        drawMap(bmp2);
        path=new Path();
        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffff0000);
        paint.setStrokeWidth(5);
        matrix=new Matrix();
    }

    private void drawMap(Bitmap b) {
        int h=b.getHeight();
        int w=b.getWidth();
        Canvas c=new Canvas(b);
        c.drawColor(0);
        Paint p=new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        drawMaze(p,c,w,h);

        p.setColor(0xffff0000);
        c.drawCircle(w * 0.2f, h * 0.2f, 20, p);
        p.setColor(0xff00ff00);
        c.drawCircle(w * 0.8f, h * 0.8f, 20, p);
    }

    private void drawMaze(Paint p,Canvas c, int w, int h) {
        p.setColor(0xff000000);
//        c.drawRect(0, 0, w, h / 10, p);
//        c.drawRect(0, 0, w / 10, h, p);
//        c.drawRect(w*9/10,0,w,h,p);
//        c.drawRect(0, h * 9 / 10, w, h, p);
        c.drawRect(w*.2f,h*0.45f,w*.8f,h*.55f,p);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(bmp,matrix,paint);
        if (!won)
            canvas.drawBitmap(bmp2,matrix,paint);
        canvas.drawPath(path,paint);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return paint.getAlpha();
    }

    private void invalidate() {
        Callback cb=getCallback();
        if (cb!=null) cb.invalidateDrawable(this);
    }

    public void reset() {
        path.rewind();
        won=false;
        invalidate();
    }

    public void moveTo(int x, int y) {
        if (x<0 || x>=bmp.getWidth() || y<0 || y>=bmp.getHeight()) reset();
        else {
            if (bmp2.getPixel(x,y)==0xff000000) reset();
            else {
                if (path.isEmpty()) {
                    if (bmp2.getPixel(x,y)==0xffff0000) {
                        path.moveTo(x, y);
                        path.lineTo(x,y);
                    }
                }
                else {
                    path.lineTo(x, y);
                    if (bmp2.getPixel(x,y)==0xff00ff00) won=true;
                }
                invalidate();
            }
        }
    }
}
