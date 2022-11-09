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

    private int xCount;

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

    private final int[] level = new int[template.length];

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

        bmp[Sprite.FLOOR.getNumber()] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[Sprite.WALL.getNumber()] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[Sprite.BOX.getNumber()] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[Sprite.FLOOR_X.getNumber()] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[Sprite.PLAYER.getNumber()] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[Sprite.GREEN_BOX.getNumber()] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);

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

    private void move(Direction direction) {
        switch (direction) {
            case UP:
                if (posY > 0) {
                    moveUp();
                }
                break;
            case DOWN:
                if (posY < ly) {
                    moveDown();
                }
                break;
            case LEFT:
                if (posX > 0) {
                    moveLeft();
                }
                break;
            case RIGHT:
                if (posX < lx) {
                    moveRight();
                }
                break;
            default:
                break;
        }

        checkWin();
    }

    private void moveUp() {
        int objectNextPosition = calculatePosition(posX, posY - 1);
        int currentPosition = calculatePosition(posX, posY);

        if (level[objectNextPosition] == Sprite.WALL.getNumber())
            return;

        if (level[objectNextPosition] == Sprite.BOX.getNumber() ||
            level[objectNextPosition] == Sprite.GREEN_BOX.getNumber()) {
            if ((posY - 2) < 0) {
                return;
            }
            int objectAfterBox = calculatePosition(posX, posY - 2);
            if (level[objectAfterBox] == Sprite.WALL.getNumber()) {
                return;
            }

            level[objectAfterBox] = template[objectAfterBox] == Sprite.FLOOR_X.getNumber() ?
                    Sprite.GREEN_BOX.getNumber() : Sprite.BOX.getNumber();
        }

        level[currentPosition] = template[currentPosition] == Sprite.FLOOR_X.getNumber() ?
                Sprite.FLOOR_X.getNumber() : Sprite.FLOOR.getNumber();
        level[objectNextPosition] = Sprite.PLAYER.getNumber();

        invalidate();

        posY -= 1;
    }

    private void moveDown() {
        int objectNextPosition = calculatePosition(posX, posY + 1);
        int currentPosition = calculatePosition(posX, posY);

        if (level[objectNextPosition] == Sprite.WALL.getNumber())
            return;

        if (level[objectNextPosition] == Sprite.BOX.getNumber() ||
            level[objectNextPosition] == Sprite.GREEN_BOX.getNumber()) {
            if ((posY + 2) < 0) {
                return;
            }
            int objectAfterBox = calculatePosition(posX, posY + 2);
            if (level[objectAfterBox] == Sprite.WALL.getNumber()) {
                return;
            }

            level[objectAfterBox] = template[objectAfterBox] == Sprite.FLOOR_X.getNumber() ?
                    Sprite.GREEN_BOX.getNumber() : Sprite.BOX.getNumber();;
        }

        level[currentPosition] = template[currentPosition] == Sprite.FLOOR_X.getNumber() ?
                Sprite.FLOOR_X.getNumber() : Sprite.FLOOR.getNumber();
        level[objectNextPosition] = Sprite.PLAYER.getNumber();

        invalidate();

        posY += 1;
    }

    private void moveRight() {
        int objectNextPosition = calculatePosition(posX + 1, posY);
        int currentPosition = calculatePosition(posX, posY);

        if (level[objectNextPosition] == Sprite.WALL.getNumber())
            return;

        if (level[objectNextPosition] == Sprite.BOX.getNumber() ||
            level[objectNextPosition] == Sprite.GREEN_BOX.getNumber()) {
            if ((posX + 1) < 0) {
                return;
            }
            int objectAfterBox = calculatePosition(posX + 2, posY);
            if (level[objectAfterBox] == Sprite.WALL.getNumber()) {
                return;
            }

            level[objectAfterBox] = template[objectAfterBox] == Sprite.FLOOR_X.getNumber() ?
                    Sprite.GREEN_BOX.getNumber() : Sprite.BOX.getNumber();
        }

        level[currentPosition] = template[currentPosition] == Sprite.FLOOR_X.getNumber() ?
                Sprite.FLOOR_X.getNumber() : Sprite.FLOOR.getNumber();
        level[objectNextPosition] = Sprite.PLAYER.getNumber();

        invalidate();

        posX += 1;
    }

    private void moveLeft() {
        int objectNextPosition = calculatePosition(posX - 1, posY);
        int currentPosition = calculatePosition(posX, posY);

        if (level[objectNextPosition] == Sprite.WALL.getNumber()) {
            return;
        }

        if (level[objectNextPosition] == Sprite.BOX.getNumber() ||
            level[objectNextPosition] == Sprite.GREEN_BOX.getNumber()) {
            if ((posX - 1) < 0) {
                return;
            }
            int objectAfterBox = calculatePosition(posX - 2, posY);
            if (level[objectAfterBox] == Sprite.WALL.getNumber()) {
                return;
            }

            level[objectAfterBox] = template[objectAfterBox] == Sprite.FLOOR_X.getNumber() ?
                    Sprite.GREEN_BOX.getNumber() : Sprite.BOX.getNumber();;
        }

        level[currentPosition] = template[currentPosition] == Sprite.FLOOR_X.getNumber() ?
                Sprite.FLOOR_X.getNumber() : Sprite.FLOOR.getNumber();
        level[objectNextPosition] = Sprite.PLAYER.getNumber();

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
                if (level[calculatePosition(x, y)] == Sprite.PLAYER.getNumber()) {
                    posX = x;
                    posY = y;

                    return;
                }
            }
        }
    }

    public void setLevel(int[] level) {
        System.arraycopy(level, 0, template, 0, level.length);

        xCount = 0;
        for (int object : level) {
            if (object == Sprite.FLOOR_X.getNumber()) {
                xCount++;
            }
        }

        resetLevel();
        invalidate();
    }

    private void checkWin() {
        int greenBoxCount = 0;
        for (int object : level) {
            if (object == Sprite.GREEN_BOX.getNumber()) {
                greenBoxCount++;
            }
        }

        if (greenBoxCount == xCount) {
            ((CanvasActivity) getContext()).win();
        }
    }
}
