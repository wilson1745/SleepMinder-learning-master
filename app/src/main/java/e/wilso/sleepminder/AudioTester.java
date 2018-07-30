package e.wilso.sleepminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import e.wilso.sleepminder.modules.AudioView;

public class AudioTester extends AppCompatActivity {

   AudioView audioView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
   }

   @Override
   protected void onResume() {
      super.onResume();

      audioView = new AudioView(this);
      setContentView(audioView);
   }

   @Override
   protected void onPause() {
      super.onPause();
   }
}
