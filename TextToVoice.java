import com.sun.speech.freetts.*;

public class TextToVoice {

    public static final String VOICE_NAME = "kevin16";
    public Voice voice;

    public TextToVoice() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICE_NAME);
        if (voice != null) {
            voice.allocate();
            voice.setRate(120);

        } else {
            throw new IllegalStateException("Cannot find voice: " + VOICE_NAME);
        }
    }

    public void speak(String text) {
        if (voice != null && text != null && !text.isEmpty()) {
            voice.speak(text);
        }
    }
}
