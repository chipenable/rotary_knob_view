package ru.chipenable.rotaryknobview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView knobValueView = findViewById(R.id.knob_value_view);
        RotaryKnobView rotaryKnobView = findViewById(R.id.rotary_knob_view);
        rotaryKnobView.setAnglesLimit(135, 45);
        rotaryKnobView.setValueLimit(0, 1000);
        rotaryKnobView.setProgress(500);

        rotaryKnobView.setOnRotationListener(position ->
                knobValueView.setText(String.valueOf(position))
        );
    }

}
