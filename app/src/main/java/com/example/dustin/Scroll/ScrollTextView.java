package com.example.dustin.Scroll;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by Dustin on 3/28/2016.
 * updated on 1/1/2017
 */
public class ScrollTextView extends TextView {

    // scrolling feature
    private Scroller mSlr;



    // milliseconds for a round of scrolling
    private int mRndDuration = 1000000000;

    // the X offset when paused
    public int mXPaused = 0;

    // whether it's being paused
    private boolean mPaused = true;

    // raw speed
    private float mScrollSpeed = 600f;

    private int mMovementIter = 300;

    private float mSpeedIter = 50f;

    public boolean completed = false;

    private int distance;

    

    /*
    * constructor
    */
    public ScrollTextView(Context context) {
        this(context, null);
        // customize the TextView
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }

    /*
    * constructor
    */
    public ScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
        // customize the TextView
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }

    /*
    * constructor
    */

    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // customize the TextView
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }


    /**
     * begin to scroll the text from the original position
     */
    public void startScroll() {
        // begin from the very right side
        mXPaused = -1 * getWidth();
        // assume it's paused
        mPaused = true;
        resumeScroll();
    }

    /**
     * resume the scroll from the pausing point
     */
    public void resumeScroll() {

        if (!mPaused)
            return;

        // Do not know why it would not scroll sometimes
        // if setHorizontallyScrolling is called in constructor.
        setHorizontallyScrolling(true);

        // use LinearInterpolator for steady scrolling
        mSlr = new Scroller(this.getContext(), new LinearInterpolator());


        //TODO text is blurry when going faster
        setScroller(mSlr);

        int scrollingLen = calculateScrollingLen();
        distance = scrollingLen - (getWidth() + mXPaused);
        //int duration = (new Double(mRndDuration * distance * 1.00000
        // mScrollSpeed)).intValue();

        int duration = (new Double(1000f * distance / mScrollSpeed)).intValue();
        setVisibility(VISIBLE);
        mSlr.startScroll(mXPaused, 0, distance, 0, duration);
        invalidate();
        mPaused = false;
    }

    /**
     * calculate the scrolling length of the text in pixel
     *
     * @return the scrolling length in pixels
     */
    private int calculateScrollingLen() {
        TextPaint tp = getPaint();
        Rect rect = new Rect();
        String strTxt = getText().toString();
        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
        int scrollingLen = rect.width() + getWidth();
        rect = null;
        return scrollingLen;
    }

    /**
     * pause scrolling the text
     */
    public void pauseScroll() {
        if (null == mSlr)
            return;

        if (mPaused)
            return;

        mPaused = true;

        // abortAnimation sets the current X to be the final X,
        // and sets isFinished to be true
        // so current position shall be saved
        mXPaused = mSlr.getCurrX();

        mSlr.abortAnimation();
    }

    /**
     * Change text location with arrows
     */
    public void goBack(){
        if (mXPaused > 5) {
            mXPaused -= mMovementIter;
        } else {
            mXPaused = 0;
        }
        scrollTo(mXPaused,0);


    }
    public void goForward(){

        mXPaused += mMovementIter;
        scrollTo(mXPaused,0);

    }

    /**
     * Change text speed
     */

    public void goFaster(){
        mScrollSpeed += mSpeedIter;
    }
    public void goSlower(){
        mScrollSpeed -= mSpeedIter;


    }


    @Override
     /*
     * override the computeScroll to restart scrolling when finished so as that
     * the text is scrolled forever
     */
    public void computeScroll() {
        super.computeScroll();

        if (null == mSlr) return;

        if (mSlr.isFinished() && (!mPaused)) {
            this.pauseScroll();
            completed = true;
        }
    }

    public boolean isdone(){
        return completed;
    }


    public void setDistance(){
        mXPaused = -1 * getWidth();
        mPaused = true;
        resumeScroll();
        pauseScroll();
    }

    public Integer getDistance(){
        return mXPaused;
    }

    public void setDone(boolean set){
        completed = set;
    }

    public float getmScrollSpeed(){
        return mScrollSpeed;
    }

    public int getRndDuration() {
        return mRndDuration;
    }

    public void setRndDuration(int duration) {
        this.mRndDuration = duration;
    }

    public boolean isPaused() {
        return mPaused;
    }
}
