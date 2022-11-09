package com.vsb.kru13.sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SokoView extends View{
    private Point pointDown;
    private Point pointUp;

    private Bitmap[] bmp;

    private final int lx = 10;
    private final int ly = 10;
    private int posX;
    private int posY;

    private int width;
    private int height;

    private final int[] template = {
            1,1,1,1,1,1,1,1,1,0,
            1,0,0,0,0,0,0,0,1,0,
            1,0,2,3,3,2,1,0,1,0,
            1,0,1,3,2,3,2,0,1,0,
            1,0,2,3,3,2,4,0,1,0,
            1,0,1,3,2,3,2,0,1,0,
            1,0,2,3,3,2,1,0,1,0,
            1,0,0,0,0,0,0,0,1,0,
            1,1,1,1,1,1,1,1,1,0,
            0,0,0,0,0,0,0,0,0,0
    };

    private int[] level = new int[template.length];

    public SokoView(Context context) {
        super(context);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        bmp = new Bitmap[6];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);

        resetLevel();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / ly;
        height = h / lx;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int y = 0; y < ly; y++) {
            for (int x = 0; x < lx; x++) {
                canvas.drawBitmap(bmp[level[calculatePosition(x, y)]], null,
                        new Rect(x*width, y*height,(x+1)*width, (y+1)*height), null);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Point point = new Point(event);

        if (action == MotionEvent.ACTION_DOWN) {
            pointDown = point;
        }
        if (action == MotionEvent.ACTION_UP) {
            pointUp = point;
            motionCheck();
        }

        return true;
    }

    private void motionCheck() {
        if (pointDown.equals(pointUp)) {
            return;
        }

        Point direction = pointDown.sub(pointUp);
        if (Math.abs(direction.getX()) > Math.abs(direction.getY())) {
            if (direction.getX() > 0) {
                move(Direction.LEFT);
            } else {
                move(Direction.RIGHT);
            }
        } else {
            if (direction.getY() > 0) {
                move(Direction.UP);
            } else {
                move(Direction.DOWN);
            }
        }
    }

    private int calculatePosition(int x, int y) {
        return y * 10 + x;
    }

    private void showText(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void move(Direction direction) {
        switch (direction) {
            case UP:
                if (posY > 0) {
                    moveUp();
                }
                return;
            case DOWN:
                if (posY < ly) {
                    moveDown();
                }
                return;
            case LEFT:
                if (posX > 0) {
                    moveLeft();
                }
                return;
            case RIGHT:
                if (posX < lx) {
                    moveRight();
                }
                return;
            default:
        }
    }

    private void moveUp() {
        int objectNextPosition = calculatePosition(posX, posY - 1);
        int currentPosition = calculatePosition(posX, posY);

        if (level[objectNextPosition] == 1 || level[objectNextPosition] == 5)
            return;

        if (level[objectNextPosition] == 2) {
            if ((posY - 2) < 0) {
                return;
            }
            int objectAfterBox = calculatePosition(posX, posY - 2);
            if (level[objectAfterBox] == 1 || level[objectAfterBox] == 5) {
                return;
            }

            level[objectAfterBox] = 5;
        }

        level[currentPosition] = template[currentPosition] == 3 ? 3 : 0;
        level[objectNextPosition] = 4;

        invalidate();

        posY -= 1;
    }

    private void moveDown() {
        int objectNextPosition = calculatePosition(posX, posY + 1);
        int currentPosition = calculatePosition(posX, posY);

        if (level[objectNextPosition] == 1 || level[objectNextPosition] == 5)
            return;

        if (level[objectNextPosition] == 2) {
            if ((posY + 2) < 0) {
                return;
            }
            int objectAfterBox = calculatePosition(posX, posY + 2);
            if (level[objectAfterBox] == 1 || level[objectAfterBox] == 5) {
                return;
            }

            level[objectAfterBox] = 5;
        }

        level[currentPosition] = template[currentPosition] == 3 ? 3 : 0;
        level[objectNextPosition] = 4;

        invalidate();

        posY += 1;
    }

    private void moveRight() {
        int objectNextPosition = calculatePosition(posX + 1, posY);
        int currentPosition = calculatePosition(posX, posY);

        if (level[objectNextPosition] == 1 || level[objectNextPosition] == 5)
            return;

        if (level[objectNextPosition] == 2) {
            if ((posX + 1) < 0) {
                return;
            }
            int objectAfterBox = calculatePosition(posX + 2, posY);
            if (level[objectAfterBox] == 1 || level[objectAfterBox] == 5) {
                return;
            }

            level[objectAfterBox] = 5;
        }

        level[currentPosition] = template[currentPosition] == 3 ? 3 : 0;
        level[objectNextPosition] = 4;

        invalidate();

        posX += 1;
    }

    private void moveLeft() {
        int objectNextPosition = calculatePosition(posX - 1, posY);
        int currentPosition = calculatePosition(posX, posY);

        if (level[objectNextPosition] == 1 || level[objectNextPosition] == 5) {
            return;
        }

        if (level[objectNextPosition] == 2) {
            if ((posX - 1) < 0) {
                return;
            }
            int objectAfterBox = calculatePosition(posX - 2, posY);
            if (level[objectAfterBox] == 1 || level[objectAfterBox] == 5) {
                return;
            }

            level[objectAfterBox] = 5;
        }

        level[currentPosition] = template[currentPosition] == 3 ? 3 : 0;
        level[objectNextPosition] = 4;

        invalidate();

        posX -= 1;
    }

    public void reload() {
        resetLevel();
        invalidate();
    }

    private void resetLevel() {
        System.arraycopy(template, 0, level, 0, template.length);

        for (int y = 0; y < ly; y++) {
            for (int x = 0; x < lx; x++) {
                if (level[calculatePosition(x, y)] == 4) {
                    posX = x;
                    posY = y;

                    return;
                }
            }
        }
    }
}
