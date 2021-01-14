package pl.pwsztar.light;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

    //this class use SerialService, SerialSocket and SerialListener
    public class BrightFunctionActivity extends AppCompatActivity {

        private TextView t1, t2;
        private SeekBar s1, s2;
        private String i1, i2;
        private Button b;
        private RadioGroup radioGroup;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_function);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            //---------------------------------------------------
            //seta deaaful value
            //brighnees function set to true

            t1 = findViewById(R.id.textView1);
            s1 = findViewById(R.id.seekBar1);
            t2 = findViewById(R.id.textView2);
            s2 = findViewById(R.id.seekBar2);
            b = findViewById(R.id.fun);

            i1 = "Speed: "; i2 = "Delay: ";

            //op1
            t1.setText(i1);
            t1.setVisibility(View.VISIBLE);
            s1.setProgress(0);
            s1.setVisibility(View.VISIBLE);

            //op2
            t2.setVisibility(View.INVISIBLE);
            s2.setVisibility(View.INVISIBLE);

            s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    t1.setText(i1+ Integer.toString(i));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            s2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    t2.setText(i2 + Integer.toString(i));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            radioGroup = findViewById(R.id.rg);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton r = findViewById(checkedId);

                    if(checkedId == R.id.r4) //blink option
                    {
                        //op1
                        i1 = "Led On:";
                        t1.setText(i1);
                        t1.setVisibility(View.VISIBLE);
                        s1.setProgress(0);
                        s1.setVisibility(View.VISIBLE);

                        //op2
                        i2 = "Led Off: ";
                        t2.setText(i2);
                        t2.setVisibility(View.VISIBLE);
                        s2.setProgress(0);
                        s2.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        //op1
                        i1 = "Speed: ";
                        t1.setText(i1);
                        t1.setVisibility(View.VISIBLE);
                        s1.setProgress(0);
                        s1.setVisibility(View.VISIBLE);

                        //op2
                        t2.setVisibility(View.INVISIBLE);
                        s2.setVisibility(View.INVISIBLE);
                    }
                }
            });

            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    switch(radioGroup.getCheckedRadioButtonId())
                    {
                        case R.id.r1:
                        {
                            //smooth
                            String cmd = "L"+Integer.toString(s1.getProgress());
                            send(cmd);
                            break;
                        }
                        case R.id.r2:
                        {
                            //fire eefect
                            String cmd = "F"+Integer.toString(s1.getProgress());
                            send(cmd);
                            break;
                        }
                        case R.id.r3:
                        {
                            //statrorsocpe
                            String cmd = "S"+Integer.toString(s1.getProgress());
                            send(cmd);
                            break;
                        }
                        case R.id.r4:
                        {
                            //blink
                            String cmd = "B"+Integer.toString(s1.getProgress()) + "D" + Integer.toString(s2.getProgress());
                            send(cmd);
                            break;
                        }
                        case R.id.r5:
                        {
                            //brightnes
                            String cmd = "P"+Integer.toString(s1.getProgress());
                            send(cmd);
                            break;
                        }
                        default:
                        {
                            Toast.makeText(getApplicationContext(), "Unexpected comand code", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            });
        }

        private void send(String text){
            try {
                byte data[] = (text + TextUtil.newline_crlf).getBytes();
                Constants.socket.write(data);
            }
            catch (Exception e) {
                Toast.makeText(this.getApplicationContext(), "Socket problem, function class", Toast.LENGTH_SHORT).show();
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
