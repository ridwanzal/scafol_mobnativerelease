package com.release.sharedexternalmodule;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


public class clearEditText {
    /*
       public final int DRAWABLE_TOP = 1;
       public final int DRAWABLE_RIGHT = 2;
       public final int DRAWABLE_BOTTOM = 3;
    * */
    public final int DRAWABLE_LEFT = 0;

    public Boolean justClear(final EditText editText, Integer DRAWABLE_RIGHT_COMP) {
        final Integer finalDRAWABLE_RIGHT_COMP = DRAWABLE_RIGHT_COMP;
        final EditText finalEditText = editText;
        finalEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (finalEditText.getRight() - finalEditText.getCompoundDrawables()[finalDRAWABLE_RIGHT_COMP].getBounds().width())) {
                        // your action here
                        finalEditText.getText().clear();
                        return true;
                    }
                }
                return false;
            }
        });
        return false;
    }
}
