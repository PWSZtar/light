package pl.pwsztar.light.views.main_view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.pwsztar.light.R;
import pl.pwsztar.light.model.LedStatus;
import pl.pwsztar.light.model.interfaces.SerialListener;
import pl.pwsztar.light.SerialService;
import pl.pwsztar.light.SerialSocket;
import pl.pwsztar.light.app.Constants;
import pl.pwsztar.light.app.utils.TextUtil;
import pl.pwsztar.light.views.basic_function_view.BasicFunctionActivity;
import pl.pwsztar.light.views.bright_function_view.BrightFunctionActivity;
import pl.pwsztar.light.views.color_function_view.ColorFunctionActivity;

public class TerminalFragment extends Fragment implements ServiceConnection, SerialListener {

  private enum Connected {False, Pending, True}

  //public SerialService service;
  private String deviceAddress, deviceName;
  private Connected connected = Connected.False;
  private boolean initialStart = true;
  private String newline = TextUtil.newline_crlf;

  private LedStatus ledStatus;

  /*
   * Lifecycle
   */
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    setRetainInstance(true);
    deviceAddress = getArguments().getString("device");
    deviceName = getArguments().getString("deviceName");
    if (ledStatus == null) {
      ledStatus = new LedStatus();
    }
  }

  @Override
  public void onDestroy() {
    if (connected != Connected.False)
      disconnect();
    getActivity().stopService(new Intent(getActivity(), SerialService.class));
    super.onDestroy();
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Constants.socket != null)
      Constants.socket.attach(this);
    else
      getActivity().startService(new Intent(getActivity(), SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
  }

  @Override
  public void onStop() {
    if (Constants.socket != null && !getActivity().isChangingConfigurations())
      Constants.socket.detach();
    super.onStop();
  }

  @SuppressWarnings("deprecation") // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
  @Override
  public void onAttach(@NonNull Activity activity) {
    super.onAttach(activity);
    getActivity().bindService(new Intent(getActivity(), SerialService.class), this, Context.BIND_AUTO_CREATE);
  }

  @Override
  public void onDetach() {
    try {
      getActivity().unbindService(this);
    } catch (Exception ignored) {
    }
    super.onDetach();
  }

  @Override
  public void onResume() {
    super.onResume();
    if (initialStart && Constants.socket != null) {
      initialStart = false;
      getActivity().runOnUiThread(this::connect);
    }
  }

  @Override
  public void onServiceConnected(ComponentName name, IBinder binder) {
    Constants.socket = ((SerialService.SerialBinder) binder).getService();
    Constants.socket.attach(this);
    if (initialStart && isResumed()) {
      initialStart = false;
      getActivity().runOnUiThread(this::connect);
    }
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    Constants.socket = null;
  }

  /*
   * UI
   */
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_terminal, container, false);

    //edited
    final Button bb1 = view.findViewById(R.id.b1);

    //TODO create new activity on click
    bb1.setOnClickListener(v -> {
      int LAUNCH_SECOND_ACTIVITY = 1;
      Intent intent = new Intent(getActivity(), BasicFunctionActivity.class);
      intent.putExtra("LED_STATUS", ledStatus);
      startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    });

    final Button bb2 = view.findViewById(R.id.b2);

    bb2.setOnClickListener(v -> {
      int LAUNCH_SECOND_ACTIVITY = 2;
      Intent intent = new Intent(getActivity(), ColorFunctionActivity.class);
      intent.putExtra("LED_STATUS", ledStatus);
      startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    });

    final Button bb3 = view.findViewById(R.id.b3);

    bb3.setOnClickListener(v -> {
      int LAUNCH_SECOND_ACTIVITY = 3;
      Intent intent = new Intent(getActivity(), BrightFunctionActivity.class);
      intent.putExtra("LED_STATUS", ledStatus);
      startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    });

    final TextView test = view.findViewById(R.id.name);

    test.setText(deviceName);

    return view;
  }


  /*
   * Serial + UI
   */
  private void connect() {
    try {
      BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
      //status("connecting...");
      Toast.makeText(getActivity(), "Connecting", Toast.LENGTH_SHORT).show();
      connected = Connected.Pending;
      SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), device);
      Constants.socket.connect(socket);
    } catch (Exception e) {
      onSerialConnectError(e);
    }
  }

  private void disconnect() {
    connected = Connected.False;
    Constants.socket.disconnect();
  }

  public void send(String str) {
    if (connected != Connected.True) {
      Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show();
      return;
    }
    try {
      String msg;
      byte[] data;
      msg = str;
      data = (str + newline).getBytes();
      Constants.socket.write(data);
    } catch (Exception e) {
      onSerialIoError(e);
    }
  }

  private void receive(byte[] data) {
    String msg = new String(data);
    if (newline.equals(TextUtil.newline_crlf) && msg.length() > 0) {
      // don't show CR as ^M if directly before LF
      msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf);
      // special handling if CR and LF come in separate fragments
      //pendingNewline = msg.charAt(msg.length() - 1) == '\r';
      Log.i("TEST", TextUtil.toCaretString(msg, newline.length() != 0).toString());
    }
  }

  /*
   * SerialListener
   */
  @Override
  public void onSerialConnect() {
    //status("connected");
    Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();

    connected = Connected.True;
  }

  @Override
  public void onSerialConnectError(Exception e) {
    //status("connection failed: " + e.getMessage());
    Toast.makeText(getActivity(), "Connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    disconnect();
  }

  @Override
  public void onSerialRead(byte[] data) {
    receive(data);
  }

  @Override
  public void onSerialIoError(Exception e) {
    //status("connection lost: " + e.getMessage());
    Toast.makeText(getActivity(), "connection lost: \" + e.getMessage()", Toast.LENGTH_SHORT).show();
    disconnect();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.i("TEST", "activity result");
    // TODO Dominik - Notka dla mnie - 1 ma być zamieniona - https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
    //if (requestCode == 1) {
    if (resultCode == Activity.RESULT_OK) {
      String result = data.getStringExtra("FUNCTION");
      ledStatus = (LedStatus) data.getSerializableExtra("LED_STATUS");
      Log.i("TEST", "TEST " + ledStatus.getLedStatus());
      Toast.makeText(getContext(), "Wiadomość zwrotna: " + result, Toast.LENGTH_SHORT).show();
      send(result);
    }
    if (resultCode == Activity.RESULT_CANCELED) {
      Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
      //Write your code if there's no result
    }
    // }
  }//
}
