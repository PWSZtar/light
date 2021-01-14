package pl.pwsztar.light.app.utils;

import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import java.io.ByteArrayOutputStream;

public class TextUtil {

    @ColorInt static int caretBackground = 0xff666666;

    public static String newline_crlf = "\r\n";
    public static String newline_lf = "\n";

    public static CharSequence toCaretString(CharSequence s, boolean keepNewline) {
        return toCaretString(s, keepNewline, s.length());
    }

    static CharSequence toCaretString(CharSequence s, boolean keepNewline, int length) {
        boolean found = false;
        for (int pos = 0; pos < length; pos++) {
            if (s.charAt(pos) < 32 && (!keepNewline ||s.charAt(pos)!='\n')) {
                found = true;
                break;
            }
        }
        if(!found)
            return s;
        SpannableStringBuilder sb = new SpannableStringBuilder();
        for(int pos=0; pos<length; pos++)
            if (s.charAt(pos) < 32 && (!keepNewline ||s.charAt(pos)!='\n')) {
                sb.append('^');
                sb.append((char)(s.charAt(pos) + 64));
                sb.setSpan(new BackgroundColorSpan(caretBackground), sb.length()-2, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                sb.append(s.charAt(pos));
            }
        return sb;
    }
}
