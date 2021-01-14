package pl.pwsztar.light.app;

import pl.pwsztar.light.BuildConfig;
import pl.pwsztar.light.SerialService;

public class Constants {

    // values have to be globally unique
    public static final String INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID +
      ".Disconnect";
    public static final String NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel";
    public static final String INTENT_CLASS_MAIN_ACTIVITY = BuildConfig.APPLICATION_ID +
      ".MainActivity";

    // values have to be unique within each app
    public static final int NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001;

    public static SerialService socket;

    private Constants() {}
}
