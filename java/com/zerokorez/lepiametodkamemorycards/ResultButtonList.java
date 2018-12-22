package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class ResultButtonList {
    private ArrayList<Card> cards;
    private ArrayList<ResultButton> buttons;
    private Boolean state;

    private Rect scrollerRect;

    private float movementY;
    private float permanentY;
    private float maxMovementY;

    private float touchHeight;
    private float moveHeight;

    private float columnHeight;
    private float rectHeight;

    private float scrollerSizeRatio;
    private float scrollerMoveRatio;

    private float downSpeed;
    private float upSpeed;

    private float fps;
    private boolean goingBack;

    private long downTime;
    private long upTime;
    private float momentum;
    private float decreaseMomentum;

    public ResultButtonList(ArrayList<Card> cards) {
        this.cards = cards;
        buttons = new ArrayList<>();
        state = true;

        float y = Constants.SCREEN_HEIGHT/18f;

        scrollerRect = new Rect(Constants.SCREEN_WIDTH*39/40, (int) (y*5), Constants.SCREEN_WIDTH, (int) (y*16));

        movementY = 0;
        permanentY = 0;
        maxMovementY = 0;

        touchHeight = 0;
        moveHeight = 0;

        columnHeight = Constants.SCREEN_HEIGHT/48f*(buttons.size()+1) + Constants.SCREEN_HEIGHT*3/18f*buttons.size();
        rectHeight = Constants.SCREEN_HEIGHT*11/18f;

        scrollerSizeRatio = (columnHeight-rectHeight)/columnHeight;
        if(scrollerSizeRatio <= 0) {
            scrollerSizeRatio = 1;
        }

        scrollerMoveRatio = -((permanentY+movementY)/columnHeight);
        downSpeed = 0;
        upSpeed = 0;

        fps = 10;
        goingBack = false;

        downTime = 0;
        upTime = 0;
        momentum = 0;
        decreaseMomentum = 0;
    }

    public float getMovementY() {
        return maxMovementY;
    }

    public void setState(Boolean newState) {
        state = newState;
    }

    public Boolean getState() {
        return state;
    }

    public void add(ResultButton resultButton) {
        buttons.add(resultButton);
        columnHeight = Constants.SCREEN_HEIGHT/48f*(buttons.size()+1) + Constants.SCREEN_HEIGHT*3/18f*buttons.size();
    }

    public void draw(Canvas canvas) {
        update();
        canvas.drawRect(scrollerRect.left, scrollerRect.top + scrollerMoveRatio*Constants.SCREEN_HEIGHT*11/18, scrollerRect.right, (scrollerRect.bottom-scrollerRect.top)*scrollerSizeRatio + scrollerRect.top + scrollerMoveRatio*Constants.SCREEN_HEIGHT*11/18, Constants.C2F_PAINT);

        int index = 0;
        for (ResultButton button: buttons) {
            float newY = Constants.SCREEN_HEIGHT / 48f * (index + 1) + Constants.SCREEN_HEIGHT*3/18f * (index + 1) + movementY + permanentY + Constants.SCREEN_HEIGHT*2/18f;
            if(button.getTop() + newY < Constants.SCREEN_HEIGHT || button.getBottom() + newY > 0) {
                button.draw(canvas, newY);
                index++;
            }
        }
    }

    public void update() {
        if(goingBack) {
            if (permanentY > 0) {
                if (downSpeed < 0) {
                    permanentY = Math.max(0, permanentY + downSpeed);
                    if (permanentY == 0) {
                        goingBack = false;
                    }
                }
            } else if (columnHeight - rectHeight > 0) {
                if (permanentY < -(columnHeight - rectHeight) && upSpeed > 0) {
                    permanentY = Math.min(-(columnHeight - rectHeight), permanentY + upSpeed);
                    if (permanentY == -(columnHeight - rectHeight)) {
                        goingBack = false;
                    }
                }
            } else if (columnHeight - rectHeight < 0) {
                if (permanentY < 0 && upSpeed > 0) {
                    permanentY = Math.min(0, permanentY + upSpeed);
                    if (permanentY == 0) {
                        goingBack = false;
                    }
                }
            }
        } else if (permanentY <= 0 && permanentY >= -(columnHeight - rectHeight)) {
            goingBack = false;
            downSpeed = 0;
            upSpeed = 0;
        }
        if (momentum != 0 && !goingBack) {
            if (momentum < 0) {
                if (decreaseMomentum > 0) {
                    if (columnHeight-rectHeight > 0) {
                        permanentY = Math.max(-(columnHeight - rectHeight), permanentY + momentum);
                    } else if (columnHeight-rectHeight <= 0) {
                        permanentY = Math.max(0, permanentY + momentum);
                    }
                    momentum = Math.min(0, momentum + decreaseMomentum);
                    if (momentum == 0) {
                        decreaseMomentum = 0;
                    }
                } else {
                    decreaseMomentum = -(momentum/30f);
                }
            } else if (momentum > 0) {
                if (decreaseMomentum < 0) {
                    permanentY = Math.min(0, permanentY + momentum);
                    momentum = Math.max(0, momentum + decreaseMomentum);
                    if (momentum == 0) {
                        decreaseMomentum = 0;
                    }
                } else {
                    decreaseMomentum = -(momentum/30f);
                }
            } else {
                momentum = 0;
                decreaseMomentum = 0;
                downTime = 0;
                upTime = 0;
            }
        } else if (momentum != 0 && goingBack) {
            momentum = 0;
            decreaseMomentum = 0;
            downTime = 0;
            upTime = 0;
        }
        scrollerSizeRatio = rectHeight/columnHeight;
        if(scrollerSizeRatio > 1) {
            scrollerSizeRatio = 1;
        }
        scrollerMoveRatio = -((permanentY+movementY)/columnHeight);
    }

    public boolean recieveTouch(MotionEvent event) {
        if (state) {
            int action = event.getAction();
            float y = event.getY();

            if (!goingBack) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (y > Constants.SCREEN_HEIGHT*5/18f && y < Constants.SCREEN_HEIGHT*16/18f) {
                            touchHeight = y;
                            moveHeight = touchHeight;
                            maxMovementY = 0;

                            downTime = System.currentTimeMillis();
                            momentum = 0;
                            decreaseMomentum = momentum;
                            upTime = 0;

                            upSpeed = 0;
                            downSpeed = 0;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (touchHeight > Constants.SCREEN_HEIGHT*5/18f && touchHeight < Constants.SCREEN_HEIGHT*16/18f) {
                            moveHeight = y;
                            maxMovementY = Math.max(maxMovementY, Math.abs(moveHeight - touchHeight));

                            upSpeed = 0;
                            downSpeed = 0;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (touchHeight > Constants.SCREEN_HEIGHT*5/18f && touchHeight < Constants.SCREEN_HEIGHT*16/18f) {
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

                            if (permanentY > 0) {
                                downSpeed = -(permanentY/fps);
                                goingBack = true;
                            } else if (columnHeight - rectHeight > 0) {
                                if (permanentY < -(columnHeight - rectHeight)) {
                                    upSpeed = -((permanentY+(columnHeight-rectHeight))/fps);
                                    goingBack = true;
                                }
                            } else if (columnHeight - rectHeight <= 0) {
                                if (permanentY < 0) {
                                    upSpeed = -(permanentY/fps);
                                    goingBack = true;
                                }
                            }
                        }
                        break;
                }
            }
            movementY = moveHeight - touchHeight;
        }

        float y = event.getY();
        if (y > Constants.SCREEN_HEIGHT*5/18f && y < Constants.SCREEN_HEIGHT*16/18f) {
            for (ResultButton button : buttons) {
                if (button.recieveTouch(event)) {
                    StudyCardsScene studyCardsScene = new StudyCardsScene();
                    studyCardsScene.setNumber(cards.indexOf(button.getCard()));
                    Constants.SCENE_MANAGER.add(studyCardsScene);
                    return true;
                }
            }
        }
        return false;
    }

    public void setSameFontSize() {
        ArrayList<Float> indexSizes = new ArrayList<>();
        ArrayList<Float> valueSizes = new ArrayList<>();
        for (ResultButton button : buttons) {
            indexSizes.add(button.getIndexTextPaint().getTextSize());
            valueSizes.add(button.getValueTextPaint().getTextSize());
        }
        float minIndex = Constants.getMinFloat(indexSizes);
        float minValue = Constants.getMinFloat(valueSizes);
        for (ResultButton button : buttons) {
            button.getIndexTextPaint().setTextSize(minIndex);
            button.getValueTextPaint().setTextSize(minValue);
        }
    }
}
