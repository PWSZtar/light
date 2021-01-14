package pl.pwsztar.light;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

    //this class use SerialService, SerialSocket and SerialListener
    public class ColorFunctionActivity extends AppCompatActivity {

        private TextView text2;
        private Button rect, set, save;
        private int r, g, b;
        private int sc[], col;
        private Button sb[];

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_color);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            //set default
            r = 0; g = 0; b = 0; col = 0;

            text2 = findViewById(R.id.t2);
            text2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

            final ColorPicker cp = new ColorPicker(ColorFunctionActivity.this, 255, 255, 255);
            cp.enableAutoClose(); // Enable auto-dismiss for the dialog

            /// load data

            SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);

            sc = new int[4];
            sc[0] = sharedPref.getInt("color1", 0xFFFF0000);
            sc[1] = sharedPref.getInt("color2", 0xFF00FF00);
            sc[2] = sharedPref.getInt("color3", 0xFF0000FF);
            sc[3] = sharedPref.getInt("color4", 0xFFFF8000);

            //get butt
            sb = new Button[4];
            sb[0] = findViewById(R.id.save1);
            sb[1] = findViewById(R.id.save2);
            sb[2] = findViewById(R.id.save3);
            sb[3] = findViewById(R.id.save4);

            //set color
            sb[0].setBackgroundColor(sc[0]);
            sb[1].setBackgroundColor(sc[1]);
            sb[2].setBackgroundColor(sc[2]);
            sb[3].setBackgroundColor(sc[3]);

            //set loaded colorr to coor selector
            col = sharedPref.getInt("color", 0xFFFFFFFF);
            cp.setColor(col);
            Toast.makeText(getApplicationContext(), Integer.toString(col), Toast.LENGTH_SHORT).show();


            //set up function
            sb[0].setOnClickListener(view -> {
                cp.setColor(sc[0]);
                rect.setBackgroundColor(cp.getColor());
            });

            sb[1].setOnClickListener(view -> {
                cp.setColor(sc[1]);
                rect.setBackgroundColor(cp.getColor());
            });

            sb[2].setOnClickListener(view -> {
                cp.setColor(sc[2]);
                rect.setBackgroundColor(cp.getColor());
            });

            sb[3].setOnClickListener(view -> {
                cp.setColor(sc[3]);
                rect.setBackgroundColor(cp.getColor());
            });

            rect = findViewById(R.id.rect);
            rect.setBackgroundColor(cp.getColor());
            rect.setOnClickListener(view -> {
                cp.show();
            });

            cp.setCallback(new ColorPickerCallback() {
                @Override
                public void onColorChosen(@ColorInt int color) {
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);

                    col = color;
                    rect.setBackgroundColor(color);

                    // If the auto-dismiss option is not enable (disabled as default) you have to manually dimiss the dialog
                    // cp.dismiss();
                }
            });

            set = findViewById(R.id.set);
            set.setOnClickListener(view -> {
                String cmd = "R" + Integer.toString(r) + "G" + Integer.toString(g) + "B" + Integer.toString(b);
                send(cmd);
            });

            save = findViewById(R.id.save);
            save.setOnClickListener(view -> {

                sc[3] = sc[2];
                sc[2] = sc[1];
                sc[1] = sc[0];
                sc[0] = cp.getColor();

                //set color
                sb[0].setBackgroundColor(sc[0]);
                sb[1].setBackgroundColor(sc[1]);
                sb[2].setBackgroundColor(sc[2]);
                sb[3].setBackgroundColor(sc[3]);
            });

        }

        private void send(String text){
            try {
                byte data[] = (text + TextUtil.newline_crlf).getBytes();
                Constants.socket.write(data);
            }
            catch (Exception e) {
                Toast.makeText(this.getApplicationContext(), "Socket problem, color class", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDestroy() {

            SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt("color1",sc[0]);
            editor.putInt("color2",sc[1]);
            editor.putInt("color3",sc[2]);
            editor.putInt("color4",sc[3]);
            editor.putInt("color", col);

            editor.apply();

            stopService(new Intent(this, SerialService.class));
            super.onDestroy();
        }

        //use serialServie
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onStop() {

            SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt("color1",sc[0]);
            editor.putInt("color2",sc[1]);
            editor.putInt("color3",sc[2]);
            editor.putInt("color4",sc[3]);
            editor.putInt("color", col);

            editor.apply();

            super.onStop();
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            onBackPressed();
            return super.onOptionsItemSelected(item);
        }
    }

