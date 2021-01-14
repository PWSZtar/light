package pl.pwsztar.light.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.softarea.tarbus.ui.main.adapter.GridColorsAdapter;

import pl.pwsztar.light.R;
import pl.pwsztar.light.model.FunctionActivityModel;
import pl.pwsztar.light.model.LedStatus;
import pl.pwsztar.light.model.LightColor;

public class ColorFunctionActivity extends FunctionActivityModel {
  private Button colorHolder, btnSave;

  private LedStatus ledStatus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_color);

    Intent intent = getIntent();
    ledStatus = (LedStatus) intent.getSerializableExtra("LED_STATUS");

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    colorHolder = findViewById(R.id.color_holder);


    ColorPicker colorPicker = new ColorPicker(ColorFunctionActivity.this, 255, 255, 255);
    colorPicker.enableAutoClose(); // Enable auto-dismiss for the dialog

    RecyclerView gridColors = findViewById(R.id.grid_colors);
    GridColorsAdapter gridColorsAdapter = new GridColorsAdapter(colorPicker, colorHolder, getApplicationContext());
    gridColors.setLayoutManager(new GridLayoutManager(this, 2));
    gridColors.setAdapter(gridColorsAdapter);

    colorHolder.setBackgroundColor(colorPicker.getColor());
    colorHolder.setOnClickListener(view -> {
      colorPicker.show();
    });

    colorPicker.setCallback(color -> {
      colorHolder.setBackgroundColor(color);
    });

    Button btnSaveColor = findViewById(R.id.btn_save_color);
    btnSaveColor.setOnClickListener(view -> {
      gridColorsAdapter.update(new LightColor(colorPicker.getColor()));
    });

    btnSave = findViewById(R.id.btn_save);
    btnSave.setOnClickListener(view -> {
      sendFunction("R" + colorPicker.getRed() + "G" + colorPicker.getGreen() + "B" + colorPicker.getBlue(), ledStatus);
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return super.onOptionsItemSelected(item);
  }
}

