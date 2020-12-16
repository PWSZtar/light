package pl.pwsztar.light;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//TODO Dominik - tak było - Standardy w androidzie mówią, że jeżeli tworzymy activity, do nazwy
// dopiujemy końcówkę Activity. Łatwiej się potem odnaleźć w kodzie. Kolejną sprawą jest to, że w
// TerminalFragmencie w linijce 136 próbujesz odpalić nowe activity, podczas gdy tutaj widok
// dziedziczy własności od Fragmentu.

//public class BasicFunction extends Fragment{

//this class use SerialService, SerialSocket and SerialListener
public class BasicFunctionActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    Button btn1 = findViewById(R.id.btn_function_first);
    Button btn2 = findViewById(R.id.btn_function_second);
    Button btn3 = findViewById(R.id.btn_function_third);

    btn1.setOnClickListener(view -> {
      Intent returnIntent = new Intent();
      returnIntent.putExtra("FUNCTION", "On");
      setResult(Activity.RESULT_OK, returnIntent);
      finish();
    });
    btn2.setOnClickListener(view -> {
      Intent returnIntent = new Intent();
      returnIntent.putExtra("FUNCTION", "Off");
      setResult(Activity.RESULT_OK, returnIntent);
      finish();
    });
    btn3.setOnClickListener(view -> {
      Intent returnIntent = new Intent();
      returnIntent.putExtra("FUNCTION", "R100G0B0");
      setResult(Activity.RESULT_OK, returnIntent);
      finish();
    });
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

  @SuppressWarnings("deprecation")
  // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
    /*@Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }*/

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
