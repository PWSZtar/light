package pl.pwsztar.light;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

    //this class use SerialService, SerialSocket and SerialListener
    public class BrightFunctionActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_function);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

           // RadioGroup radioButtonGroup = findViewById(R.id.rg);
            //int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
            //View radioButton = radioButtonGroup.findViewById(radioButtonID);
            //int idx = radioButtonGroup.indexOfChild(radioButton);

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
