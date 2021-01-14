package pl.pwsztar.light.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import pl.pwsztar.light.R;
import pl.pwsztar.light.model.FunctionActivityModel;
import pl.pwsztar.light.model.LedStatus;

public class BasicFunctionActivity extends FunctionActivityModel {
  private TextView textOnOfStatus;
  private LedStatus ledStatus;
  private SwitchCompat switchLedStatus;
  private String statusValue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic);

    Intent intent = getIntent();
    ledStatus = (LedStatus) intent.getSerializableExtra("LED_STATUS");

    Log.i("TEST", "TEST BASIC FUNCTION: " + ledStatus.getLedStatus());

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    textOnOfStatus = findViewById(R.id.text_on_of);
    Button btnSend = findViewById(R.id.btn_save);
    switchLedStatus = findViewById(R.id.switch_led_status);
    ledStatus.setLedStatus(!ledStatus.getLedStatus());
    changeLedStatus();
    switchLedStatus.setOnClickListener(view -> {
      changeLedStatus();
    });

    btnSend.setOnClickListener(view -> {
      onSendFunction();
    });
  }

  private void changeLedStatus() {
    if (ledStatus.getLedStatus()) {
      textOnOfStatus.setText(getString(R.string.off));
      switchLedStatus.setChecked(false);
      ledStatus.setLedStatus(false);
      statusValue = LedStatus.CommandLedOff;
    } else {
      textOnOfStatus.setText(getString(R.string.on));
      switchLedStatus.setChecked(true);
      ledStatus.setLedStatus(true);
      statusValue = LedStatus.CommandLedOn;
    }
  }

  private void onSendFunction() {
    sendFunction(statusValue, ledStatus);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return super.onOptionsItemSelected(item);
  }
}
