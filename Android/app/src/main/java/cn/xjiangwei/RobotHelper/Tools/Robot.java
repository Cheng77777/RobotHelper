package cn.xjiangwei.RobotHelper.Tools;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;
import static android.os.SystemClock.sleep;

/**
 * 模拟操作的实现类
 *
 * 目前只使用了xposed提权实现
 *
 * 未来可能考虑加入root权限的实现
 *
 */
public class Robot {

    private static Instrumentation mInst = new Instrumentation();


    /**
     * 点击操作
     * @param x
     * @param y
     */
    public static void tap(final int x, final int y) {
        if (x < 0 || y < 0) {
            return;
        }

        mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));    //x,y 即是事件的坐标
        mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));

    }

    /**
     * 长按操作，可以自定义按下时间，单位为毫秒
     * @param x
     * @param y
     * @param delay
     */
    public static void tap(final int x, final int y, final long delay) {
        if (x < 0 || y < 0) {
            return;
        }
        mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));    //x,y 即是事件的坐标
        sleep(delay);
        mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
    }


    public static void tap(Point p) {
        tap(p.getX(), p.getY());
    }


    public static void tap(Point p, long delay) {
        tap(p.getX(), p.getY(), delay);
    }


    /**
     * 拖拽操作
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param duration //单位为毫秒
     */
    public static void swipe(float x1, float y1, float x2, float y2, float duration) {
        final int interval = 25;
        int steps = (int) (duration / interval + 1);
        float dx = (x2 - x1) / steps;
        float dy = (y2 - y1) / steps;
        down(x1, y1);
        for (int step = 0; step < steps; step++) {
            sleep(interval);
            moveTo(x1 + step * dx, y1 + step * dy, 0);
        }
        sleep(interval);
        up(x2, y2);
    }


    private static void down(float x, float y) {
        mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
    }


    private static void up(float x, float y) {
        mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
    }


    private static void moveTo(float x, float y, int contactId) {
        mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, x, y, 0));
    }


    /**
     * TODO 待实现
     *
     * 往输入框输入文字，暂未实现.
     *
     * 实现思路，自定义输入法
     *
     * @param str
     */
    public static void input(String str) {
//        if (Robot.mInst == null) {
//            mInst = new Instrumentation();
//        }


    }


}
