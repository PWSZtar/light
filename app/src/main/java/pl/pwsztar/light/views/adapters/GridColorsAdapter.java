package pl.pwsztar.light.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.ArrayList;
import java.util.List;

import pl.pwsztar.light.R;
import pl.pwsztar.light.model.LightColor;

public class GridColorsAdapter extends RecyclerView.Adapter<GridColorsAdapter.ViewHolder> {
  private List<LightColor> lightColorList = new ArrayList<>();
  private final ColorPicker colorPicker;
  private final Button colorHolder;
  private Context context;

  public GridColorsAdapter(ColorPicker colorPicker, Button colorHolder, Context context) {
    this.colorPicker = colorPicker;
    this.colorHolder = colorHolder;
    this.context = context;
    getColors();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public Button button;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      button = itemView.findViewById(R.id.button);
    }
  }

  public void update(LightColor lightColor) {
    lightColorList.add(0, lightColor);
    lightColorList.remove(4);
    saveColors();
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public GridColorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View listItem = layoutInflater.inflate(R.layout.item_grid_color, parent, false);
    return new ViewHolder(listItem);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    LightColor lightColor = lightColorList.get(position);
    holder.button.setBackgroundColor(lightColor.getRgb());

    holder.button.setOnClickListener(view -> {
      colorPicker.setColor(lightColor.getRgb());
      colorHolder.setBackgroundColor(colorPicker.getColor());
    });
  }

  //TODO - Trochę prowizorka, to powinno mieć raczej inne miejsce
  public void saveColors() {
    SharedPreferences sharedPref = context.getSharedPreferences("COLOR_PREFERENCES2", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    Log.i("TEST", "Size:" + lightColorList.size());
    editor.putInt("SAVED_COLOR_1",lightColorList.get(0).getRgb());
    editor.putInt("SAVED_COLOR_2",lightColorList.get(1).getRgb());
    editor.putInt("SAVED_COLOR_3",lightColorList.get(2).getRgb());
    editor.putInt("SAVED_COLOR_4",lightColorList.get(3).getRgb());
    editor.apply();
  }

  private void getColors() {
    SharedPreferences sharedPref = context.getSharedPreferences("COLOR_PREFERENCES2", Context.MODE_PRIVATE);
    lightColorList.clear();
    lightColorList.add( new LightColor(sharedPref.getInt("SAVED_COLOR_1", 0xFFFF0000)));
    lightColorList.add( new LightColor(sharedPref.getInt("SAVED_COLOR_2", 0xFF00FF00)));
    lightColorList.add( new LightColor(sharedPref.getInt("SAVED_COLOR_3", 0xFF0000FF)));
    lightColorList.add( new LightColor(sharedPref.getInt("SAVED_COLOR_4", 0xFFFF8000)));
  }

  @Override
  public int getItemCount() {
    return lightColorList.size();
  }
}
