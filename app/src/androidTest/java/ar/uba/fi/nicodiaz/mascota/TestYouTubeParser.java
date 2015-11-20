package ar.uba.fi.nicodiaz.mascota;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.utils.YouTubeUtils;

public class TestYouTubeParser extends AndroidTestCase {

    public void parseYouTubeURL() {
        List<String> urls = new ArrayList<>();
        urls.add("http://www.youtube.com/watch?v=dQw4w9WgXcQ&a=GxdCwVVULXctT2lYDEPllDR0LRTutYfW");
        urls.add("http://www.youtube.com/watch?v=dQw4w9WgXcQ");
        urls.add("http://youtu.be/dQw4w9WgXcQ");
        urls.add("http://www.youtube.com/embed/dQw4w9WgXcQ");
        urls.add("http://www.youtube.com/v/dQw4w9WgXcQ");
        urls.add("http://www.youtube.com/e/dQw4w9WgXcQ");
        urls.add("http://www.youtube.com/watch?v=dQw4w9WgXcQ");
        urls.add("http://www.youtube.com/watch?feature=player_embedded&v=dQw4w9WgXcQ");
        urls.add("http://www.youtube-nocookie.com/v/dQw4w9WgXcQ?version=3&hl=en_US&rel=0");

        for (String url : urls) {
            assertEquals("No se reconoce url: " + url, "dQw4w9WgXcQ", YouTubeUtils.parseYouTubeVideoUrl(url));
        }
    }
}
