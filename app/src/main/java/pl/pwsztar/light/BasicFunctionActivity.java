package pl.pwsztar.light;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//this class use SerialService, SerialSocket and SerialListener
public class BasicFunctionActivity extends AppCompatActivity {

  private Button button;
  private TextView text;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    text = findViewById(R.id.ledtext);
    text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

    button = findViewById(R.id.b);
    button.setOnClickListener(view -> {
    if(status.ledStatus)
      send("Off");
    else
      send("On");

    refresh();
    });

    refresh();
  }

  private void refresh()
  {
    if(status.ledStatus)
    {
      text.setText("Led is On");
      button.setText("Turn Off");
      status.ledStatus = true;
    }
    else
    {
      text.setText("Led is On");
      button.setText("Turn Off");
      status.ledStatus = false;
    }
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
