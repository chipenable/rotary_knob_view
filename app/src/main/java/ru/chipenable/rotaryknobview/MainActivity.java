package ru.chipenable.rotaryknobview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RotaryKnobView rotaryKnobView1 = findViewById(R.id.rotary_knob_view_1);
        rotaryKnobView1.setAnglesLimit(135, 350);
        rotaryKnobView1.setMarkAmount(10, 5);
        rotaryKnobView1.setValueLimit(0, 500);
        rotaryKnobView1.setProgress(250);

        TextView knobValueView1 = findViewById(R.id.knob_value_view_1);
        knobValueView1.setText("" + rotaryKnobView1.getProgress());

        rotaryKnobView1.setOnRotationListener(position ->
                knobValueView1.setText(String.valueOf(position))
        );
    }

}
