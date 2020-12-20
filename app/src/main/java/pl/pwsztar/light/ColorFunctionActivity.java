package pl.pwsztar.light;

import android.content.Intent;
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
        private Button rect, set;
        private int r, g, b;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_color);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            text2 = findViewById(R.id.t2);
            text2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

            final ColorPicker cp = new ColorPicker(ColorFunctionActivity.this, 0, 0, 0);
            cp.enableAutoClose(); // Enable auto-dismiss for the dialog

            rect = findViewById(R.id.rect);
            rect.setOnClickListener(view -> {
                cp.show();
                Toast.makeText(this.getApplicationContext(), "sexr", Toast.LENGTH_SHORT).show();

            });

            cp.setCallback(new ColorPickerCallback() {
                @Override
                public void onColorChosen(@ColorInt int color) {
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);

                    rect.setBackgroundColor(color);

                    // If the auto-dismiss option is not enable (disabled as default) you have to manually dimiss the dialog
                    // cp.dismiss();
                }
            });

            set = findViewById(R.id.set);
            rect.setOnClickListener(view -> {
                //send("R" + r.toString() + "G" + g.toString() + "B" + b.toString());
                cp.show();
                Toast.makeText(this.getApplicationContext(), "shoe", Toast.LENGTH_SHORT).show();

            });
        }

        private void send(String text){
            try {
                byte data[] = (text + TextUtil.newline_crlf).getBytes();
                Constants.socket.write(data);
            }
            catch (Exception e) {
                Toast.makeText(this.getApplicationContext(), "Nie ten przycisk", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDestroy() {
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

