package demo.app.com.horizontalscrolldemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;

/**
 * @author：JianFeng
 * @date：2019/3/26 16:08
 * @description：
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {
    private OnCustomScrollChangeListener listener;

    public interface OnCustomScrollChangeListener {
        void onCustomScrollChange(CustomHorizontalScrollView listener, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    public void setOnCustomScrollChangeListener(OnCustomScrollChangeListener listener) {
        this.listener = listener;
    }

    public CustomHorizontalScrollView(Context context) {
        this(context, null);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != listener)
            listener.onCustomScrollChange(CustomHorizontalScrollView.this, l, t, oldl, oldt);
    }
    public void setEventListener(EventListener listener) {
        eventListener = listener;
    }

    private EventListener eventListener = null;

    public interface EventListener {
        void onEvent(MotionEvent event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (eventListener!=null){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
    private float startY;
    private float startX;
    private  int mTouchSlop;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                if(distanceX > mTouchSlop && distanceX > distanceY) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        if (eventListener != null) {
            eventListener.onEvent(ev);
            return true;
        } else {
            return super.onTouchEvent(ev);
        }
    }
}
