package sssta.org.hakeday;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TrackService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_TRACK = "sssta.org.hakeday.action.FOO";
    private static final String ACTION_BAZ = "sssta.org.hakeday.action.BAZ";

    // TODO: Rename parameters
    private static final String TRACK_MAP = "sssta.org.hakeday.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "sssta.org.hakeday.extra.PARAM2";

    public TrackService() {
        super("TrackService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionTrack(Context context, Map<String, String> trackInfo) {
        Intent intent = new Intent(context, TrackService.class);
        intent.setAction(ACTION_TRACK);
        intent.putExtra(TRACK_MAP, (Serializable) trackInfo);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, TrackService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(TRACK_MAP, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_TRACK.equals(action)) {
                Map<String, String> trackMap = (Map<String, String>) intent.getSerializableExtra(TRACK_MAP);
                handleActionFoo(trackMap);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(TRACK_MAP);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(Map<String, String> trackMap) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
