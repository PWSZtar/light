package pl.pwsztar.light;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//this class use SerialService, SerialSocket and SerialListener
public class TerminalFragment extends Fragment implements ServiceConnection, SerialListener {

  private enum Connected {False, Pending, True}

  private String deviceAddress, deviceName;
  private SerialService service;

  private Connected connected = Connected.False;
  private boolean initialStart = true;
  private boolean pendingNewline = false;
  private String newline = TextUtil.newline_crlf;

  //edited

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

  }

  @Override
  public void onDestroy() {
    if (connected != Connected.False)
      disconnect();
    getActivity().stopService(new Intent(getActivity(), SerialService.class));
    super.onDestroy();
  }

  //use serialServie
  @Override
  public void onStart() {
    super.onStart();
    if (service != null)
      service.attach(this);
    else
      getActivity().startService(new Intent(getActivity(), SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
  }

  @Override
  public void onStop() {
    if (service != null && !getActivity().isChangingConfigurations())
      service.detach();
    super.onStop();
  }

  @SuppressWarnings("deprecation")
  // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
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
    if (initialStart && service != null) {
      initialStart = false;
      getActivity().runOnUiThread(this::connect);
    }
  }

  @Override
  public void onServiceConnected(ComponentName name, IBinder binder) {
    service = ((SerialService.SerialBinder) binder).getService();
    service.attach(this);
    if (initialStart && isResumed()) {
      initialStart = false;
      getActivity().runOnUiThread(this::connect);
    }
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    service = null;
  }

  /*
   * UI
   */
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //TODO remove view if is not important

    View view = inflater.inflate(R.layout.fragment_terminal, container, false);

    //edited
    final Button bb1 = view.findViewById(R.id.b1);

    //TODO create new activity on click
    bb1.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Intent myIntent = new Intent(getActivity(), BasicFunction.class);
        //myIntent.putExtra("key", value); //Optional parameters
        getActivity().startActivity(myIntent);
      }
    });

    final Button bb2 = view.findViewById(R.id.b2);

    bb2.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        send("Button2");
      }
    });

    final Button bb3 = view.findViewById(R.id.b3);

    bb3.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        send("Button3");
      }
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

      Toast.makeText(getActivity(), "Connecting... ", Toast.LENGTH_SHORT).show();

      connected = Connected.Pending;
      SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), device);
      service.connect(socket);
    } catch (Exception e) {
      onSerialConnectError(e);
    }
  }

  private void disconnect() {
    connected = Connected.False;
    service.disconnect();
  }

  private void send(String str) {
    if (connected != Connected.True) {
      Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show();
      return;
    }
    try {
      byte[] data;
      data = (str + newline).getBytes();
      service.write(data);
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
      if (pendingNewline && msg.charAt(0) == '\n') {
        //Editable edt = receiveText.getEditableText();
        //if (edt != null && edt.length() > 1)
        //edt.replace(edt.length() - 2, edt.length(), "");
      }
      pendingNewline = msg.charAt(msg.length() - 1) == '\r';
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
    Toast.makeText(getActivity(), "Connection lost: " + e.getMessage(), Toast.LENGTH_SHORT).show();

    disconnect();
  }
}

