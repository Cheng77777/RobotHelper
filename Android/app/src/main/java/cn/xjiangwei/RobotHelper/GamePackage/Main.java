package cn.xjiangwei.RobotHelper.GamePackage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import cn.xjiangwei.RobotHelper.MainApplication;
import cn.xjiangwei.RobotHelper.Tools.Image;
import cn.xjiangwei.RobotHelper.Tools.MLog;
import cn.xjiangwei.RobotHelper.Tools.Point;
import cn.xjiangwei.RobotHelper.Tools.Robot;
import cn.xjiangwei.RobotHelper.Tools.ScreenCaptureUtil;
import cn.xjiangwei.RobotHelper.Tools.TessactOcr;
import cn.xjiangwei.RobotHelper.Tools.Toast;

import static android.os.SystemClock.sleep;

public class Main implements Runnable {
    private Random ran = new Random();
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    private String start_image_file = "start.png";
    private String continue_image_file = "continue.png";
    private boolean run = true;
    /**
     * 在这个函数里面写你的业务逻辑
     */
    @Override
    public void run(){

//        MLog.info("Initialization", "Initializing");
        sleep(1000); //点击开始后等待5秒后再执行，因为状态栏收起有动画时间，建议保留这行代码
        MLog.setDebug(true);

//        Robot.setExecType(Robot.ExecTypeXposed);         //使用xposed权限执行模拟操作，建议优先使用此方式
//        Robot.setExecType(Robot.ExecTypeAccessibillty);  //使用安卓无障碍接口执行模拟操作
        Robot.setExecType(Robot.ExecTypeROOT);           //使用root权限执行模拟操作（实验阶段，仅在oneplus 7pro测试过，欢迎提bug）

//        MLog.info("Initialization", "robot loaded");

        Toast.notice();
        Toast.show("启动成功");
        /****************************  模板匹配demo  *******************************/
        InputStream input = null;
        while (run){
            sleep(1000);
            try {
                input = MainApplication.getInstance().getAssets().open(start_image_file);
                Bitmap start_bitmap = BitmapFactory.decodeStream(input);
                //在当前屏幕中查找模板图片
                Point point = Image.matchTemplate(ScreenCaptureUtil.getScreenCap(), start_bitmap, 0.6);
                if(point == null) {
                    input = MainApplication.getInstance().getAssets().open(continue_image_file);
                    Bitmap continue_bitmap = BitmapFactory.decodeStream(input);
                    point = Image.matchTemplate(ScreenCaptureUtil.getScreenCap(), continue_bitmap, 0.6);
                    if (point != null) {
                        point.setX(point.getX() + ran.nextInt(800));
                        point.setY(1080 - (point.getY() + ran.nextInt(400)));
                        for(int i = 0; i < ran.nextInt(5);i++){
                            point.setX(point.getX() + ran.nextInt(20)-10);
                            point.setY(point.getY() + ran.nextInt(20)-10);
//                            MLog.info("点击继续", point.toString());
                            Toast.show("继续" + point.toString());
                            Robot.tap(point);
                            sleep(ran.nextInt(200)+300);
                        }
                    }
                    continue;
                }
                point.setX(point.getX() + ran.nextInt(150));
                point.setY(1080 - (point.getY() + ran.nextInt(150)));
                for(int i = 0; i < ran.nextInt(5);i++){
                    point.setX(point.getX() + ran.nextInt(20)-10);
                    point.setY(point.getY() + ran.nextInt(20)-10);
//                    MLog.info("点击开始", "开始： "+ point.toString());
                    Toast.show("开始"+ point.toString());
                    Robot.tap(point);
                    sleep(ran.nextInt(200)+300);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        MLog.info("结束", "结束");
        Toast.show("停止脚本！");
        Toast.notice();
    }

    public void SetRun(Boolean run){
        this.run = run;
    }

    public Boolean isRunning(){
        return run;
    }
}
