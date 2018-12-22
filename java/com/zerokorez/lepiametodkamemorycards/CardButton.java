package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class CardButton implements GameObject {
    private Group group;
    private int number;

    private Rect rect;
    private Paint rectPaint;
    private Paint borderPaint;

    private boolean onTouch;
    private boolean onClick;

    private boolean state;

    private String indicatorText;
    private Rect indicatorTextRect;
    private Paint indicatorTextPaint;

    private String indexText;
    private Rect indexTextRect;
    private Paint indexTextPaint;

    private String valueText;
    private Rect valueTextRect;
    private Paint valueTextPaint;

    private String noteText;
    private Rect noteTextRect;
    private Paint noteTextPaint;

    private float permanentX;
    private float movementX;
    private float maxMovementX;
    private float touchWidth;
    private float moveWidth;

    private boolean goingBackX;
    private boolean goingBackY;
    private float speedX;
    private boolean changingCard;
    private float fps;

    private boolean isTurned;
    private boolean turningCard;
    private boolean turningState;
    private float turningSpeed;

    private static int LEFT;
    private static int RIGHT;
    private static int CENTER;

    private TextWindow textWindow;
    private ImageButton starButton;

    private Rect upperBackgroundRect;
    private Rect downerBackgroundRect;
    private Rect upperCardRect;
    private Rect downerCardRect;

    private float movementY;
    private float permanentY;
    private float maxMovementY;
    private float touchHeight;
    private float moveHeight;

    private float downSpeed;
    private float upSpeed;
    private long downTime;
    private long upTime;
    private float momentum;
    private float decreaseMomentum;

    public CardButton(Group group, ImageButton starButton) {
        this.group = group;
        number = 0;

        float x = Constants.SCREEN_WIDTH/33f;
        float top = Constants.SCREEN_HEIGHT/18f;

        rect = new Rect((int) (x*2), (int) (top + x*3), (int) (Constants.SCREEN_WIDTH - x*2), (int) (Constants.SCREEN_HEIGHT - x*13));
        upperBackgroundRect = new Rect(0, 0, Constants.SCREEN_WIDTH, (int) (top + x*3));
        downerBackgroundRect = new Rect(0, (int) (Constants.SCREEN_HEIGHT - x*13), Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        rectPaint = new Paint();
        rectPaint.setColor(Constants.WHITE);
        borderPaint = new Paint();
        borderPaint.setColor(Constants.C2F);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);

        LEFT = (int) (x*2);
        RIGHT = (int) (Constants.SCREEN_WIDTH - x*2);
        CENTER = (int) ((RIGHT-LEFT)/2f + LEFT);

        state = true;
        onTouch = false;
        onClick = false;

        indicatorTextRect = new Rect(rect.left, rect.top, rect.right, (int) (rect.top + x*3));
        indicatorTextPaint = new Paint();
        indicatorTextPaint.setColor(Constants.C4F);

        indexTextRect = new Rect(rect.left, (int) ((rect.bottom-rect.top)/2f + rect.top - x*25/4f), rect.right, (int) ((rect.bottom-rect.top)/2f + rect.top - x*5/4f));
        indexTextPaint = new Paint();
        indexTextPaint.setColor(Constants.C5F);

        valueTextRect = new Rect(rect.left, (int) ((rect.bottom-rect.top)/2f + rect.top - x*5/2f), rect.right, (int) ((rect.bottom-rect.top)/2f + rect.top + x*4));
        valueTextPaint = new Paint();
        valueTextPaint.setColor(Constants.C3F);

        noteTextRect = new Rect(rect.left, (int) (rect.bottom - x*3), rect.right, rect.bottom);
        noteTextPaint = new Paint();
        noteTextPaint.setColor(Constants.C5F);

        resetIndicatorTextPaint();
        resetIndexTextPaint();
        resetValueTextPaint();
        resetNoteTextPaint();

        permanentX = 0;
        movementX = 0;
        maxMovementX = 0;
        touchWidth = 0;
        moveWidth = 0;

        goingBackX = false;
        goingBackY = false;
        changingCard = false;
        speedX = 0;
        fps = 10f;

        isTurned = false;
        turningCard = false;
        turningSpeed = (rect.right-rect.left)/15f;
        turningState = false;

        textWindow = new TextWindow(new Rect((int) (x*4), (int) (top + x*8), (int) (Constants.SCREEN_WIDTH - x*4), (int) (Constants.SCREEN_HEIGHT - x*18)), x, top);
        upperCardRect = new Rect((int) (x*4), (int) (top + x*3), rect.right, (int) (top + x*8));
        downerCardRect = new Rect((int) (x*4), (int) (Constants.SCREEN_HEIGHT - x*18), rect.right, (int) (Constants.SCREEN_HEIGHT - x*13));

        textWindow.setText(group.getCards().get(number).getText());
        this.starButton = starButton;
        updateStarButton();

        movementY = 0;
        permanentY = 0;
        maxMovementY = 0;
        touchHeight = 0;
        moveHeight = 0;

        downSpeed = 0;
        upSpeed = 0;

        downTime = 0;
        upTime = 0;
        momentum = 0;
        decreaseMomentum = 0;
    }

    public void setNumber(int number) {
        this.number = number;
        resetIndicatorTextPaint();
        resetIndexTextPaint();
        resetValueTextPaint();
        resetNoteTextPaint();
        textWindow.setText(group.getCards().get(number).getText());
        updateStarButton();
    }

    public Card getCard() {
        return group.getCards().get(number);
    }

    public void nextNumber(boolean right) {
        if (right) {
            if (number < group.getCards().size()) {
                number++;
                if (number == group.getCards().size()) {
                    number = 0;
                }
            }
        } else {
            if (number > -1) {
                number--;
                if (number == -1) {
                    number = group.getCards().size()-1;
                }
            }
        }
        permanentY = 0;
        isTurned = false;
        resetIndicatorTextPaint();
        resetIndexTextPaint();
        resetValueTextPaint();
        resetNoteTextPaint();
        textWindow.setText(group.getCards().get(number).getText());
        updateStarButton();
    }

    public void resetIndicatorTextPaint() {
        indicatorText = Integer.toString(number + 1) + " / " + Integer.toString(group.getCards().size());
        indicatorTextPaint.setTextSize(Constants.getTextSize(indicatorTextRect, indicatorTextPaint, indicatorText));
    }

    public void resetIndexTextPaint() {
        indexText = group.getCards().get(number).getIndex();
        indexTextPaint.setTextSize(Constants.getTextSize(indexTextRect, indexTextPaint, indexText));
    }

    public void resetValueTextPaint() {
        valueText = group.getCards().get(number).getValue();
        valueTextPaint.setTextSize(Constants.getTextSize(valueTextRect, valueTextPaint, valueText));
    }

    public void resetNoteTextPaint() {
        noteText = "* " + group.getCards().get(number).getNote();
        noteTextPaint.setTextSize(Constants.getTextSize(noteTextRect, noteTextPaint, noteText));
    }

    public void recieveTouch(MotionEvent event) {
        if (state) {
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();

            if (!goingBackX && !changingCard && !turningCard) {
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        if (rect.contains((int) point[0], (int) point[1]) && onTouch) {
                            onClick = true;
                            onTouch = false;

                            if ((maxMovementX < Constants.SCREEN_WIDTH / 64f) && (maxMovementY < Constants.SCREEN_HEIGHT / 144f)) {
                                onClick = false;
                                turningCard = true;
                                turningState = true;
                                maxMovementX = 0;
                                maxMovementY = 0;
                            } else {
                                onClick = false;
                                maxMovementX = 0;
                                maxMovementY = 0;
                            }
                        } else {
                            onClick = false;
                            onTouch = false;
                            maxMovementX = 0;
                            maxMovementY = 0;
                        }
                        if (touchHeight != 0 && isTurned) {
                            upTime = System.currentTimeMillis();
                            if (upTime-downTime < 250 && maxMovementY > Constants.SCREEN_HEIGHT/48f) {
                                momentum = (moveHeight - touchHeight) / (upTime - downTime) * 50;
                                decreaseMomentum = -(momentum / 30f * (upTime - downTime) / 100f);
                            }
                            downTime = 0;
                            upTime = downTime;

                            touchHeight = 0;
                            moveHeight = touchHeight;
                            permanentY += movementY;

                            float height = textWindow.getColumnHeight() - textWindow.getRectHeight();
                            if (permanentY > 0) {
                                downSpeed = -(permanentY/fps);
                                goingBackY = true;
                            } else if (height > 0) {
                                if (permanentY < -(height)) {
                                    upSpeed = -((permanentY+(height))/fps);
                                    goingBackY = true;
                                }
                            } else if (height <= 0) {
                                if (permanentY < 0) {
                                    upSpeed = -(permanentY/fps);
                                    goingBackY = true;
                                }
                            }
                        }
                        if (!goingBackX && touchWidth != 0) {
                            goingBackX = true;
                            permanentX = moveWidth - touchWidth;
                            touchWidth = 0;
                            moveWidth = 0;
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if (rect.contains((int) point[0], (int) point[1]) && !goingBackX) {
                            onTouch = true;

                            touchWidth = point[0];
                            moveWidth = touchWidth;

                            if (isTurned) {
                                touchHeight = point[1];
                                moveHeight = touchHeight;

                                downTime = System.currentTimeMillis();
                                momentum = 0;
                                decreaseMomentum = momentum;
                                upTime = 0;

                                upSpeed = 0;
                                downSpeed = 0;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (rect.contains((int) point[0], (int) point[1]) && onTouch) {
                            if (!goingBackX && touchWidth != 0 && !onClick && !changingCard) {
                                if (isTurned && touchHeight != 0) {
                                    if (textWindow.getRect().contains((int) touchWidth, (int) touchHeight)) {
                                        if (maxMovementX < Constants.SCREEN_WIDTH / 64f) {
                                            moveHeight = point[1];
                                            maxMovementY = Math.max(maxMovementY, Math.abs(touchHeight - moveHeight));

                                            upSpeed = 0;
                                            downSpeed = 0;
                                        }
                                    }
                                    if (maxMovementY < Constants.SCREEN_HEIGHT / 144f && Math.abs(touchWidth - point[0]) > Constants.SCREEN_WIDTH / 64f) {
                                        moveWidth = point[0];
                                        maxMovementX = Math.max(maxMovementX, Math.abs(touchWidth - moveWidth));
                                    }
                                } else if (!isTurned){
                                    moveWidth = point[0];
                                    maxMovementX = Math.max(maxMovementX, Math.abs(touchWidth - moveWidth));
                                }
                            }
                        } else if (!rect.contains((int) point[0], (int) point[1]) && onTouch) {
                            onTouch = false;
                        }
                        break;
                }
                movementX = moveWidth - touchWidth;
                if (isTurned) {
                    if (textWindow.getRect().contains((int) touchWidth, (int) touchHeight)) {
                        movementY = moveHeight - touchHeight;
                    } else {
                        movementY = 0;
                    }
                }
            }

            if (permanentX != 0 && goingBackX) {
                speedX = -(permanentX/fps);
                if (Math.abs(permanentX) > Constants.SCREEN_WIDTH/3f) {
                    changingCard = true;
                    goingBackX = false;
                    speedX = (Constants.SCREEN_WIDTH/fps)*(permanentX/Math.abs(permanentX));
                } else {
                    speedX *= 3;
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        update();
        Rect newRect =  Constants.moveRect(rect, (int) (movementX + permanentX), 0);
        canvas.drawRect(newRect, rectPaint);

        if (!turningCard && !isTurned) {
            Constants.drawTextCenter(canvas, Constants.moveRect(indexTextRect, (int) (movementX + permanentX), 0), indexTextPaint, indexText);
            Constants.drawTextCenter(canvas, Constants.moveRect(valueTextRect, (int) (movementX + permanentX), 0), valueTextPaint, valueText);
        } else if (!turningCard){
            textWindow.setMoveX((int) (movementX + permanentX));
            textWindow.draw(canvas);
            textWindow.drawScroller(canvas);

            canvas.drawRect(Constants.moveRect(upperBackgroundRect, (int) (movementX + permanentX), 0), Constants.C5F_PAINT);
            canvas.drawRect(Constants.moveRect(downerBackgroundRect, (int) (movementX + permanentX), 0), Constants.C5F_PAINT);

            canvas.drawRect(Constants.moveRect(upperCardRect, (int) (movementX + permanentX), 0), rectPaint);
            canvas.drawRect(Constants.moveRect(downerCardRect, (int) (movementX + permanentX), 0), rectPaint);

            textWindow.drawBorders(canvas);
        }

        if (!turningCard) {
            Constants.drawTextCenter(canvas, Constants.moveRect(indicatorTextRect, (int) (movementX + permanentX), 0), indicatorTextPaint, indicatorText);
        }
        if (!turningCard) {
            Constants.drawTextCenter(canvas, Constants.moveRect(noteTextRect, (int) (movementX + permanentX), 0), noteTextPaint, noteText);
        }

        canvas.drawLine(rect.left + (movementX + permanentX), indicatorTextRect.bottom, rect.right + (movementX + permanentX), indicatorTextRect.bottom, borderPaint);
        canvas.drawLine(rect.left + (movementX + permanentX), noteTextRect.top, rect.right + (movementX + permanentX), noteTextRect.top, borderPaint);
        canvas.drawRect(newRect, borderPaint);
    }

    @Override
    public void update() {
        if (goingBackX) {
            if (speedX != 0) {
                if (permanentX < 0) {
                    permanentX = Math.min(0, permanentX + speedX);
                    if (permanentX == 0) {
                        goingBackX = false;
                        speedX = 0;
                    }
                } else if (permanentX > 0) {
                    permanentX = Math.max(0, permanentX + speedX);
                    if (permanentX == 0) {
                        goingBackX = false;
                        speedX = 0;
                    }
                }
            } else {
                goingBackX = false;
            }
        } else if (changingCard) {
            if (speedX != 0) {
                if (speedX < 0) {
                    if (permanentX > -Constants.SCREEN_WIDTH) {
                        permanentX = Math.max(-Constants.SCREEN_WIDTH, permanentX + speedX);
                    } else if (permanentX == -Constants.SCREEN_WIDTH) {
                        nextNumber(true);
                        permanentX = Constants.SCREEN_WIDTH;
                        changingCard = false;
                        goingBackX = true;
                        speedX = -(permanentX / fps);
                        isTurned = false;
                    }
                } else if (speedX > 0) {
                    if (permanentX < Constants.SCREEN_WIDTH) {
                        permanentX = Math.min(Constants.SCREEN_WIDTH, permanentX + speedX);
                    } else if (permanentX == Constants.SCREEN_WIDTH) {
                        nextNumber(false);
                        permanentX = -Constants.SCREEN_WIDTH;
                        changingCard = false;
                        goingBackX = true;
                        speedX = -(permanentX / fps);
                        isTurned = false;
                    }
                }
            } else {
                changingCard = false;
            }
        }
        if (turningCard) {
            if (turningState) {
                rect = new Rect((int) Math.min(CENTER, rect.left + turningSpeed), rect.top, (int) Math.max(CENTER, rect.right - turningSpeed), rect.bottom);
                if (rect.left == CENTER && rect.right == CENTER) {
                    turningState = false;
                    isTurned = !isTurned;
                }
            } else {
                rect = new Rect((int) Math.max(LEFT, rect.left - turningSpeed), rect.top, (int) Math.min(RIGHT, rect.right + turningSpeed), rect.bottom);
                if (rect.left == LEFT && rect.right == RIGHT) {
                    turningCard = false;
                }
            }
        }
        textWindow.setScrollerMoveRatio(movementY + permanentY);

        if (isTurned) {
            float height = textWindow.getColumnHeight() - textWindow.getRectHeight();
            if (goingBackY) {
                if (permanentY > 0) {
                    if (downSpeed < 0) {
                        permanentY = Math.max(0, permanentY + downSpeed);
                        if (permanentY == 0) {
                            goingBackY = false;
                        }
                    }
                } else if (height > 0) {
                    if (permanentY < -(height) && upSpeed > 0) {
                        permanentY = Math.min(-(height), permanentY + upSpeed);
                        if (permanentY == -(height)) {
                            goingBackY = false;
                        }
                    }
                } else if (height < 0) {
                    if (permanentY < 0 && upSpeed > 0) {
                        permanentY = Math.min(0, permanentY + upSpeed);
                        if (permanentY == 0) {
                            goingBackY = false;
                        }
                    }
                }
            } else if (permanentY <= 0 && permanentY >= -(height)) {
                downSpeed = 0;
                upSpeed = 0;
            }
            if (momentum != 0 && !goingBackY) {
                if (momentum < 0) {
                    if (decreaseMomentum > 0) {
                        if (height > 0) {
                            permanentY = Math.max(-(height), permanentY + momentum);
                        } else if (height <= 0) {
                            permanentY = Math.max(0, permanentY + momentum);
                        }
                        momentum = Math.min(0, momentum + decreaseMomentum);
                        if (momentum == 0) {
                            decreaseMomentum = 0;
                        }
                    } else {
                        decreaseMomentum = -(momentum / 30f);
                    }
                } else if (momentum > 0) {
                    if (decreaseMomentum < 0) {
                        permanentY = Math.min(0, permanentY + momentum);
                        momentum = Math.max(0, momentum + decreaseMomentum);
                        if (momentum == 0) {
                            decreaseMomentum = 0;
                        }
                    } else {
                        decreaseMomentum = -(momentum / 30f);
                    }
                } else {
                    momentum = 0;
                    decreaseMomentum = 0;
                    downTime = 0;
                    upTime = 0;
                }
            } else if (momentum != 0) {
                momentum = 0;
                decreaseMomentum = 0;
                downTime = 0;
                upTime = 0;
            }
            textWindow.setScrollerMoveRatio(permanentY + movementY);
        }
    }

    public void updateStarButton() {
        if (getCard().getKnown()) {
            starButton.changeImage("yellow_star.png");
        } else {
            starButton.changeImage("white_star.png");
        }
    }
}
