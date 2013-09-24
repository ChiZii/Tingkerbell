package kt.atoz.econovation.tingkerbell.main;

import java.util.Locale;

import android.speech.tts.TextToSpeech;
import android.util.Log;


public class TextToSpeechActivity implements TextToSpeech.OnInitListener{
	//Text To Speech Method
	private TextToSpeech tts;

	//Call TextToSpeech
			
	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub

		if (arg0 == TextToSpeech.SUCCESS) {
			int result = tts.setLanguage(Locale.KOREAN);
			 tts.setPitch(5); // set pitch level
			 tts.setSpeechRate(2); // set speech speed rate
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}
		
	}
			
	void speakOut() {
		//String text = txtText.getText().toString();
		tts.speak("wow wow wow wow wow wow wow wow", TextToSpeech.QUEUE_FLUSH, null);
	}

			
}
