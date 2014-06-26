package com.idl.daq;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class I2cImageView extends ImageView{
		
        private float MAX_SCALE = 1.6f;
        private int DOUBLE_TAP_SECOND = 400;

        private Matrix mMatrix;
        private final float[] mMatrixValues = new float[9];

        // display width height.
        private int mWidth;
        private int mHeight;

        private int mIntrinsicWidth;
        private int mIntrinsicHeight;

        private float mScale;
        private float mMinScale;

        // double tap for determining
        private long mLastTime = 0;
        private boolean isDoubleTap;
        private int mDoubleTapX;
        private int mDoubleTapY;

        private float mPrevDistance;
        private boolean isScaling;

        private int mPrevMoveX;
        private int mPrevMoveY;

        String TAG = "MyImageView";
        Context c;
        private int col1;
        private int col2;
        private float relativeX;
        private float relativeY;
        private float height;
        private float width;
        int k=0;
        
        public I2cImageView(Context context, AttributeSet attr) {
                super(context, attr);
                c=context;
                initialize();
        }
        public I2cImageView(Context context) {
                super(context);
                initialize();
                
        }

        @Override
        public void setImageBitmap(Bitmap bm) {
                super.setImageBitmap(bm);
                this.initialize();
        }

        private void initialize() {
                this.setScaleType(ScaleType.MATRIX);
                this.mMatrix = new Matrix();
                Drawable d = getDrawable();
                if (d != null) {
                        mIntrinsicWidth = d.getIntrinsicWidth();
                        mIntrinsicHeight = d.getIntrinsicHeight();
                }
        }

        @Override
        protected boolean setFrame(int l, int t, int r, int b) {
                mWidth = r-l;
                mHeight = b-t;

                mMatrix.reset();
                mScale = (float) r / (float) mIntrinsicWidth;
                int paddingHeight = 0;
                int paddingWidth = 0;
                // scaling vertical
                if (mScale * mIntrinsicHeight > mHeight) {
                        mScale = (float) mHeight / (float) mIntrinsicHeight;
                        mMatrix.postScale(mScale, mScale);
                        paddingWidth = (r - mWidth) / 2;
                        paddingHeight = 0;
                        // scaling horizontal
                } else {
                        mMatrix.postScale(mScale, mScale);
                        paddingHeight = (b - mHeight) / 2;
                        paddingWidth = 0;
                }
                mMatrix.postTranslate(paddingWidth, paddingHeight);

                setImageMatrix(mMatrix);
                mMinScale = mScale;
                //zoomTo(mScale, mWidth / 2, mHeight / 2);
                cutting();
                return super.setFrame(l, t, r, b);
        }

        protected float getValue(Matrix matrix, int whichValue) {
                matrix.getValues(mMatrixValues);
                return mMatrixValues[whichValue];
        }

        protected float getScale() {
                return getValue(mMatrix, Matrix.MSCALE_X);
        }

        protected float getTranslateX() {
                return getValue(mMatrix, Matrix.MTRANS_X);
        }

        protected float getTranslateY() {
                return getValue(mMatrix, Matrix.MTRANS_Y);
        }

        protected void maxZoomTo(int x, int y) {
                if (mMinScale != getScale() && (getScale() - mMinScale) > 0.1f) {
                        // threshold 0.1f
                        float scale = mMinScale / getScale();
                        zoomTo(scale, x, y);
                } else {
                        float scale = MAX_SCALE / getScale();
                        zoomTo(scale, x, y);
                }
        }

        protected void zoomTo(float scale, int x, int y) {
                if (getScale() * scale < mMinScale) {
                        return;
                }
                if (scale >= 1 && getScale() * scale > MAX_SCALE) {
                        return;
                }
                mMatrix.postScale(scale, scale);
                // move to center
                mMatrix.postTranslate(-(mWidth * scale - mWidth) / 2,
                                -(mHeight * scale - mHeight) / 2);

                // move x and y distance
                mMatrix.postTranslate(-(x - (mWidth / 2)) * scale, 0);
                mMatrix.postTranslate(0, -(y - (mHeight / 2)) * scale);
                setImageMatrix(mMatrix);
        }

        public void cutting() {
                int width = (int) (mIntrinsicWidth * getScale());
                int height = (int) (mIntrinsicHeight * getScale());
                if (getTranslateX() < -(width - mWidth)) {
                        mMatrix.postTranslate(-(getTranslateX() + width - mWidth), 0);
                }
                if (getTranslateX() > 0) {
                        mMatrix.postTranslate(-getTranslateX(), 0);
                }
                if (getTranslateY() < -(height - mHeight)) {
                        mMatrix.postTranslate(0, -(getTranslateY() + height - mHeight));
                }
                if (getTranslateY() > 0) {
                        mMatrix.postTranslate(0, -getTranslateY());
                }
                if (width < mWidth) {
                        mMatrix.postTranslate((mWidth - width) / 2, 0);
                }
                if (height < mHeight) {
                        mMatrix.postTranslate(0, (mHeight - height) / 2);
                }
                setImageMatrix(mMatrix);
        }

        private float distance(float x0, float x1, float y0, float y1) {
                float x = x0 - x1;
                float y = y0 - y1;
                return FloatMath.sqrt(x * x + y * y);
        }

        private float dispDistance() {
                return FloatMath.sqrt(mWidth * mWidth + mHeight
                                * mHeight);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                int touchCount = event.getPointerCount();
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_1_DOWN:
                case MotionEvent.ACTION_POINTER_2_DOWN:
                        if (touchCount >= 2) {
                                float distance = distance(event.getX(0), event.getX(1),
                                                event.getY(0), event.getY(1));
                                mPrevDistance = distance;
                                isScaling = true;
                        } else {
                                if (System.currentTimeMillis() <= mLastTime + DOUBLE_TAP_SECOND) {
                                        if (30 > Math.abs(mPrevMoveX - event.getX())
                                                        + Math.abs(mPrevMoveY - event.getY())) {
                                                isDoubleTap = true;
                                                mDoubleTapX = (int) event.getX();
                                                mDoubleTapY = (int) event.getY();
                                        }
                                }
                                mLastTime = System.currentTimeMillis();
                                mPrevMoveX = (int) event.getX();
                                mPrevMoveY = (int) event.getY();
                        }
                        /*int x = (int) event.getX();
        	            int y = (int) event.getY();
        	            relativeX =((event.getX() - mMatrixValues[2]) / getScale());
        	            relativeY =((event.getY() - mMatrixValues[5]) / getScale());
        	            Log.d("Touch Event", "Touch event at "+ x + " " +y);
        	            Toast.makeText(c,"X:"+relativeX+" Y:"+relativeY, Toast.LENGTH_SHORT).show();
        	            */
                        
                        break;
                case MotionEvent.ACTION_MOVE:
                        if (touchCount >= 2 && isScaling) {
                                float dist = distance(event.getX(0), event.getX(1),
                                                event.getY(0), event.getY(1));
                                float scale = (dist - mPrevDistance) / dispDistance();
                                mPrevDistance = dist;
                                scale += 1;
                                scale = scale * scale;
                                zoomTo(scale, mWidth / 2, mHeight / 2);
                                cutting();
                        } else if (!isScaling) {
                                int distanceX = mPrevMoveX - (int) event.getX();
                                int distanceY = mPrevMoveY - (int) event.getY();
                                mPrevMoveX = (int) event.getX();
                                mPrevMoveY = (int) event.getY();
                                mMatrix.postTranslate(-distanceX, -distanceY);
                                cutting();
                        }
                        break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_POINTER_2_UP:
                        if (event.getPointerCount() <= 1) {
                                isScaling = false;
                                if (isDoubleTap) {
                                        if (30 > Math.abs(mDoubleTapX - event.getX())
                                                        + Math.abs(mDoubleTapY - event.getY())) {
                                                maxZoomTo(mDoubleTapX, mDoubleTapY);
                                                cutting();
                                        }
                                }
                        }
                        isDoubleTap = false;
                        break;
                }
                return true;
        }
        public void setcol1(int a)
        {
        	col1=a;
        }
        public void setcol2(int a)
        {
        	col2=a;
        }
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            
            if(k==0)
            {
            width=this.getWidth();
            height=this.getHeight();
            relativeY =(height - mMatrixValues[5])/getScale();
            float p=mMatrixValues[2]+mMatrixValues[2];
            float g=width-p;
            relativeX =g/getScale();
            k++;
            }
            if(getScale()<1.15)      {
            	
            Paint paint = new Paint();
            RectF drawRect = new RectF();
            
            float left=getX(875);
            float right=getX(965);
            float top=getY(875);
            float bottom=getY(925);
            drawRect.set(left, top, right, bottom);
            paint.setStyle(Paint.Style.FILL);
            //paint.setColor(Color.YELLOW);
            paint.setColor(col1);
            canvas.drawRect(drawRect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(Color.YELLOW);
            canvas.drawRoundRect(drawRect,5,5, paint);
            
            left=getX(790);
            right=getX(830);
            top=getY(875);
            bottom=getY(975);
            drawRect.set(left, top, right, bottom);
            paint.setStyle(Paint.Style.FILL);
            //paint.setColor(Color.YELLOW);
            paint.setColor(col2);
            canvas.drawRect(drawRect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(Color.YELLOW);
            canvas.drawRoundRect(drawRect,5,5, paint);
                                 
            }
            
            	
        }
        protected float getX(int a)
        {
        	float relX =(((a*relativeX/1600)*getScale() )+mMatrixValues[2]);
        	return relX;
            
        }
        protected float getY(int b)
        {
        	float relY =(((b*relativeY/1024)*getScale())+mMatrixValues[5]);
        	return relY;
        }
}


