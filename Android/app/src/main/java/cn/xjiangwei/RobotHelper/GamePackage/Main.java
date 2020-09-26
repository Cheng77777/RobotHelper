package cn.xjiangwei.RobotHelper.GamePackage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Random;

import cn.xjiangwei.RobotHelper.MainApplication;
import cn.xjiangwei.RobotHelper.Tools.Image;
import cn.xjiangwei.RobotHelper.Tools.MLog;
import cn.xjiangwei.RobotHelper.Tools.Point;
import cn.xjiangwei.RobotHelper.Tools.Robot;
import cn.xjiangwei.RobotHelper.Tools.ScreenCaptureUtil;
import cn.xjiangwei.RobotHelper.Tools.Toast;

import static android.os.SystemClock.sleep;

public class Main implements Runnable {

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String start_image_file_00 = "start_00.png";
    private static final String start_image_file_01 = "start_01.png";
    private static final String continue_image_file_00 = "continue_00.png";
    private static final String continue_image_file_01 = "continue_01.png";
    private static final ImageData[] imageData = {
            new ImageData(start_image_file_00,150,150,"点击开始"),
            new ImageData(start_image_file_01,150,150,"点击开始"),
            new ImageData(continue_image_file_00,800,400,"点击继续"),
            new ImageData(continue_image_file_01,800,400,"点击继续")
    };

    private Random ran = new Random();
    private InputStream input = null;
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

        while (run){
            sleep(1000);
            for (ImageData i : imageData) {
                if(FindImageAndTap(i.filename,0.6,5,i.rangeX,i.rangeY,i.typeMessage)){
                    break;
                }
            }
        }
        Toast.show("停止脚本！");
        Toast.notice();
    }

    private boolean FindImageAndTap(String filename, double threshold, int randomNum, int rangeX, int rangeY, String message){
        Point point = GetImagePoint(filename,threshold);
        if(point != null){
            Tap(point,randomNum,rangeX,rangeY,message);
            return true;
        }
        return false;
    }

    private void Tap(Point point, int randomNum, int rangeX, int rangeY, String message){
        point.setX(point.getX() + ran.nextInt(rangeX));
        point.setY(1080 - (point.getY() + ran.nextInt(rangeY)));
        for(int i = 0; i < ran.nextInt(randomNum);i++){
            point.setX(point.getX() + ran.nextInt(20)-10);
            point.setY(point.getY() + ran.nextInt(20)-10);
            Toast.show(message+ point.toString());
            Robot.tap(point);
            sleep(ran.nextInt(200)+300);
        }
    }

    private Point GetImagePoint(String filename, double threshold){
        try {
            input = MainApplication.getInstance().getAssets().open(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return Image.matchTemplate(ScreenCaptureUtil.getScreenCap(), bitmap, threshold);
        }catch (Exception e){
            return null;
        }
    }

    public void SetRun(Boolean run){
        this.run = run;
    }

    public Boolean isRunning(){
        return run;
    }
}

class ImageData{
    public String filename;
    public int rangeX;
    public int rangeY;
    public String typeMessage;

    public ImageData(String filename,int rangeX,int rangeY,String typeMessage){
        this.filename = filename;
        this.rangeX = rangeX;
        this.rangeY = rangeY;
        this.typeMessage = typeMessage;
    }
}
