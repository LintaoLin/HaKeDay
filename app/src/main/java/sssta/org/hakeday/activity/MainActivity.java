package sssta.org.hakeday.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import sssta.org.hakeday.network.AnalysisResponse;
import sssta.org.hakeday.ConvertIOUtil;
import sssta.org.hakeday.R;
import sssta.org.hakeday.RecordLayout;
import sssta.org.hakeday.network.ServiceRest;
import sssta.org.hakeday.network.WikiItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    boolean isRecord = false;

    int sampleRateInHZ = 8000;
    int channel = AudioFormat.CHANNEL_IN_STEREO;
    final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    final int bufferedSize = 200;
    final String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    AudioRecord audioRecord;
//    ProgressDialog progressDialog;
    @BindView(R.id.record_button)
    RecordLayout recordButton;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    void ensureRecordButton() {
        if (audioRecord == null)
            audioRecord = new AudioRecord(
                        MediaRecorder.AudioSource.MIC,
                        sampleRateInHZ,
                        channel,
                        audioFormat,
                        bufferedSize
                );
    }

    public void startWeb(View view) {
        startActivity(new Intent(this, WebActivity.class));
    }

    public void toggleRecord(View v) {
        if (!isRecord) {
            record();
        } else {
            stopAndPlay();
        }
    }

    public void record() {
        ensureRecordButton();
        Log.d(TAG, "record: " + audioRecord.getState());
        if (audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
            return;
        }
        recordButton.startAnimation();
        audioRecord.startRecording();
        isRecord = true;
        Subscriber<File> subscriber = new Subscriber<File>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: record complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onNext(File file) {
                if (file != null) {
                    analysis(file);
                }
            }
        };
        Observable<File> observable = Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                if (audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
                    Log.d(TAG, "call: audio record initial failed");
                    return;
                }
                File file = new File(sdcardPath, "tmpRecord.raw");
                if (file.exists())
                    file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    subscriber.onError(e);
                    return;
                }
                byte[] buffer = new byte[bufferedSize];
                BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    while (isRecord) {
                        int readSize = audioRecord.read(buffer, 0, bufferedSize);
                        if (readSize == AudioRecord.ERROR_INVALID_OPERATION) {
                            isRecord = false;
                            break;
                        }
                        bos.write(buffer);
                    }
                } catch (FileNotFoundException e) {
                    subscriber.onError(e);
                } catch (IOException e) {
                    subscriber.onError(e);
                } finally {
                    if (bos != null) {
                        try {
                            bos.flush();
                            bos.close();
                        } catch (IOException e) {
                            subscriber.onError(e);
                            return;
                        }
                    }
                }
                File playVoice = new File(sdcardPath, "tmpRecord"+(index++) + ".wav");
                if (playVoice.exists()) playVoice.delete();
                try {
                    playVoice.createNewFile();
                    if (ConvertIOUtil.copyWaveFile(file.getAbsolutePath(),
                            playVoice.getAbsolutePath(), sampleRateInHZ, bufferedSize)) {
                        subscriber.onNext(playVoice);
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                    return;
                }
            }
        });
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    public void stopAndPlay() {
        if (audioRecord != null) {
            isRecord = false;
            recordButton.stopAnimation();
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
//            if (progressDialog != null && !progressDialog.isShowing())
//                progressDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
        recordButton.cancelAnimation();
    }

    public void toWikiList(View view) {
        startActivity(new Intent(this, WikiListActivity.class));
    }

    public void analysis(File file) {
        final ProgressDialog pDialog = ProgressDialog.show(this, "upload to analysis","uploading...." );
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Subscription subscription = ServiceRest.getInstance().getServiceApi()
                .uploadAudio(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AnalysisResponse>() {
                    @Override
                    public void onCompleted() {
                        pDialog.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        pDialog.cancel();
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(AnalysisResponse analysisResponse) {
                        if (analysisResponse.getStatus() == 0) {
                            if (analysisResponse.getPredict() != null) {
                                WikiItem wikiItem = WikiItem.from(analysisResponse);
                                Intent i = new Intent(MainActivity.this, WikiDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(WikiDetailActivity.IDENTIFY, wikiItem);
                                i.putExtra("data", bundle);
                                startActivity(i);
                            } else {
                                Toast.makeText(MainActivity.this, "no data response", Toast.LENGTH_SHORT).show();
                            }
                        } else if (analysisResponse.getStatus() == 1) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage(analysisResponse.getError())
                                    .show();
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    public void chooseLocalFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "select audio"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor actualAudioCursor = getContentResolver().query(uri, null, null, null, null);
            actualAudioCursor.moveToFirst();
            String img_path = actualAudioCursor.getString(0);
            img_path =  img_path.replaceAll("primary:", "/");
            Log.d(TAG, "onActivityResult: " + uri);
            if (img_path == null) {
                return;
            }
            File file = new File(Environment.getExternalStorageDirectory()+img_path);
//            Toast.makeText(MainActivity.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            if (file.exists()) {
                analysis(file);
            }
        }
    }

    public void toTopList(View view) {
        Intent intent = new Intent(this, TopListActivity.class);
        startActivity(intent);
    }

//    private void dismissProgress() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.cancel();
//        }
//    }
//
//    private void startProgress() {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(this);
//        }
//    }
}
