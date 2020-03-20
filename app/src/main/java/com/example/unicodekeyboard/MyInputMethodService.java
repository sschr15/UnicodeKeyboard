package com.example.unicodekeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputConnection;

import androidx.annotation.RequiresApi;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    public String input = "";
    public int chars = 0;

    @Override
    public View onCreateInputView() {
        // get the KeyboardView and add our Keyboard layout to it
        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        Keyboard keyboard = new Keyboard(this, R.xml.number_pad);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                CharSequence selectedText = ic.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    // no selection, so delete previous character
                    ic.deleteSurroundingText(1, 0);
                    chars--;
                    input = chars > 0 ? input.substring(0, input.length() - 1) : "";
                } else {
                    // delete the selection
                    ic.commitText("", 1);
                }
                break;
            case (0):
                //TODO get some code working here to convert the string to a unicode char
/*                String out;
                try {
                    byte[] tmp = hexStringToByteArray(input);
                    out = new String(tmp, StandardCharsets.UTF_8);
                    System.out.println(input + " " + out);
                } catch (StringIndexOutOfBoundsException ignored) {
                    out = "\u0000";
                } */

                if (input.length() % 2 == 0 && input.length() > 0) {
                    int ch = Integer.parseInt(input, 16);
                    char[] actualChar = Character.toChars(ch);
                    String iThinkThisIsIt = String.valueOf(actualChar[0]);
                    ic.deleteSurroundingText(chars, 0);
                    ic.commitText(iThinkThisIsIt /* out */, 1);
                    input = "";
                    chars = 0;
                }
                break;
            case (2):
                ic.deleteSurroundingText(chars, 0);
                chars = 0;
                input = "";
                break;
            case (10):
                if (input.length() % 2 == 0 && input.length() > 0) {
                    int ch = Integer.parseInt(input, 16);
                    char[] actualChar = Character.toChars(ch);
                    String iThinkThisIsIt = String.valueOf(actualChar[0]);
                    ic.deleteSurroundingText(chars, 0);
                    ic.commitText(iThinkThisIsIt /* out */, 1);
                    input = "";
                    chars = 0;
                }
                ic.commitText(Character.toString((char) primaryCode), 1);
                break;
            default:
                char charNum = (char) primaryCode;
                input = input.concat(Character.toString(charNum));
                ic.commitText(Character.toString(charNum), 1);
//                System.out.print(charNum);
//                System.out.print(" " + input);
//                System.out.println();
                chars++;
        }
        chars = Math.max(chars, 0);
    }

    @Override
    public void onPress(int primaryCode) { }

    @Override
    public void onRelease(int primaryCode) { }

    @Override
    public void onText(CharSequence text) { }

    @Override
    public void swipeLeft() { }

    @Override
    public void swipeRight() { }

    @Override
    public void swipeDown() { }

    @Override
    public void swipeUp() { }
}