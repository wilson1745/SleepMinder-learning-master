package e.wilso.sleepminder.modules;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import e.wilso.sleepminderlib.Recorder;

public class MyApplication extends android.app.Application {

   public static String TAG = "MyApplication";

   public static MyApplication myApplication;

   public static Context context;
   public static Recorder recorder;
   public static String str;

   public MyApplication(Context ctx) {
      this.context = ctx;
      this.recorder = new Recorder();
      this.str = "The same application!!!!!!!!!!";
      //Log.e(TAG, "Main: " + recorder + context);

   }

   // Singleton設計
   public static MyApplication getInstance() {
      //Log.e(TAG, "getInstance: " + str + "  " + context + " " + recorder);
      return myApplication;
   }

   // Singleton設計
   public static MyApplication getMainInstance(Context ctx) {
      if(myApplication == null) {
         myApplication = new MyApplication(ctx);
      }
      //Log.e(TAG, "getMainInstance: " + str + "  " + context + " " + recorder);

      return myApplication;
   }

   /*@Override
   public void onCreate() {
      super.onCreate();
      context = this;
      recorder = new Recorder();
   }*/
}
