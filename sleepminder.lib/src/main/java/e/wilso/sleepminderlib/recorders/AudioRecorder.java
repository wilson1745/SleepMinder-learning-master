package e.wilso.sleepminderlib.recorders;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.github.privacystreams.audio.Audio;
import com.github.privacystreams.audio.AudioOperators;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.purposes.Purpose;

import java.util.ArrayList;
import java.util.List;

import e.wilso.sleepminderlib.DebugView;
import e.wilso.sleepminderlib.detection.FeatureExtractor;
import e.wilso.sleepminderlib.detection.NoiseModel;


public class AudioRecorder extends Thread {
   private boolean stopped = false;
   private static AudioRecord recorder = null;
   private static int N = 0;
   private NoiseModel noiseModel;
   private DebugView debugView;
   private short[] buffer;
   private FeatureExtractor featureExtractor;
   private Context mContext;
   private long durationPerRecord = 100;
   long interval = 0;


   public AudioRecorder(Context mContext, NoiseModel noiseModel, DebugView debugView) {

      this.noiseModel = noiseModel;
      this.debugView = debugView;
      this.featureExtractor = new FeatureExtractor(noiseModel);
      this.mContext = mContext;
   }

   @Override
   public void run() {

      capture(mContext);

   }

   private void capture(Context context) {
      android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
      UQI uqi = new UQI(context);
      uqi.getData(Audio.recordPeriodic(durationPerRecord, interval), Purpose.HEALTH("Sleep monitoring"))
              .setField("amp", AudioOperators.getAmplitudeSamples(Audio.AUDIO_DATA))
              .forEach("amp", new Callback<List<Integer>>() {
                 @Override
                 protected void onInput(List<Integer> input) {
                    short shortArray[] = new short[input.size()];
                    for (int i = 0; i < shortArray.length; ++i) {
                       shortArray[i] = input.get(i).shortValue();
                    }
                    process(shortArray);
                 }
              });


   }

   private void process(short[] buffer) {

      featureExtractor.update(buffer);

      if (debugView != null) {
            /*debugView.addPoint2(noiseModel.getNormalizedRLH(), noiseModel.getNormalizedVAR());
            debugView.setLux((float) (noiseModel.getNormalizedRMS()));*/
         debugView.addPoint2(noiseModel.getLastRLH(), noiseModel.getNormalizedVAR());
         debugView.setLux((float) (noiseModel.getLastRMS()));
         debugView.post(new Runnable() {
            @Override
            public void run() {
               debugView.invalidate();
            }
         });

      }

   }


   public void close() {
      stopped = true;
   }

}