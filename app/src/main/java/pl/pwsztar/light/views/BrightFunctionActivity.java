package pl.pwsztar.light.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import pl.pwsztar.light.R;
import pl.pwsztar.light.model.FunctionActivityModel;
import pl.pwsztar.light.model.LedStatus;

//this class use SerialService, SerialSocket and SerialListener
public class BrightFunctionActivity extends FunctionActivityModel {

  private SeekBar seekBarSpeed, seekBarDelay;
  private LinearLayout boxSpeed;
  private LinearLayout boxDelay;
  private RadioGroup radioGroup;
  private LedStatus ledStatus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_function);

    Intent intent = getIntent();
    ledStatus = (LedStatus) intent.getSerializableExtra("LED_STATUS");

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    LinearLayout boxOption = findViewById(R.id.box_option);
    boxSpeed = findViewById(R.id.box_speed);
    boxDelay = findViewById(R.id.box_delay);

    TextView textLedStatus = findViewById(R.id.text_led_status);
    TextView textSpeed = findViewById(R.id.text_speed);
    seekBarSpeed = findViewById(R.id.seekbar_speed);
    TextView textDelay = findViewById(R.id.text_delay);
    seekBarDelay = findViewById(R.id.seekbar_delay);
    Button btnSave = findViewById(R.id.btn_save);

    if (ledStatus.getLedStatus()) {
      boxOption.setVisibility(View.VISIBLE);
      textLedStatus.setVisibility(View.GONE);
    }

    seekBarDelay.setOnSeekBarChangeListener(setSeekBarListener(textDelay, getString(R.string.delay)));
    seekBarSpeed.setOnSeekBarChangeListener(setSeekBarListener(textSpeed, getString(R.string.speed)));

    radioGroup = findViewById(R.id.rg);

    radioGroup = findViewById(R.id.rg);
    radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
      RadioButton r = findViewById(checkedId);
      if (checkedId == R.id.option_blink) {
        boxSpeed.setVisibility(View.VISIBLE);
        boxDelay.setVisibility(View.VISIBLE);
      } else {
        boxSpeed.setVisibility(View.VISIBLE);
        boxDelay.setVisibility(View.GONE);
      }
    });

    btnSave.setOnClickListener(v -> onSendFunction());
  }

  @SuppressLint("NonConstantResourceId")
  private void onSendFunction() {
    String functionValue = "";
    switch (radioGroup.getCheckedRadioButtonId()) {
      case R.id.option_smooth:
        functionValue = "L" + seekBarSpeed.getProgress();
        break;
      case R.id.option_fire_effect:
        functionValue = "F" + seekBarSpeed.getProgress();
        break;
      case R.id.option_statrorsocpe:
        functionValue = "S" + seekBarSpeed.getProgress();
        break;
      case R.id.option_blink:
        functionValue = "B" + seekBarSpeed.getProgress() + "D" + seekBarDelay.getProgress();
        break;
      case R.id.option_brightnes:
        functionValue = "P" + seekBarSpeed.getProgress();
        break;
      default:
        Toast.makeText(getApplicationContext(), "Unexpected command code", Toast.LENGTH_SHORT).show();
    }
    if (!functionValue.isEmpty()) {
      sendFunction(functionValue, ledStatus);
    }
  }

  private SeekBar.OnSeekBarChangeListener setSeekBarListener(TextView boxMessage, String message) {
    return new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        String value = message + " " + i;
        boxMessage.setText(value);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
    };
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return super.onOptionsItemSelected(item);
  }
}
