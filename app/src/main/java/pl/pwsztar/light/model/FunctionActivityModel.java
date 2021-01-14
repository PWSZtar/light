package pl.pwsztar.light.model;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pl.pwsztar.light.app.Constants;
import pl.pwsztar.light.app.utils.TextUtil;

public abstract class FunctionActivityModel extends AppCompatActivity {

  protected void sendFunction(String functionValue, LedStatus ledStatus){
    Intent returnIntent = new Intent();
    returnIntent.putExtra("FUNCTION",functionValue);
    returnIntent.putExtra("LED_STATUS",ledStatus);
    setResult(Activity.RESULT_OK,returnIntent);
    finish();
  }
}
