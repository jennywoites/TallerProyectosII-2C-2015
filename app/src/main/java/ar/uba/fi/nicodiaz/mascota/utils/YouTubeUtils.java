package ar.uba.fi.nicodiaz.mascota.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeUtils {

    public static String parseYouTubeVideoUrl(String url) {
        String regex = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }
}
