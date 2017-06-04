package sssta.org.hakeday.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sssta.org.hakeday.HKImageView;
import sssta.org.hakeday.R;
import sssta.org.hakeday.network.IdentifyResponse;
import sssta.org.hakeday.network.ServiceRest;
import sssta.org.hakeday.network.WikiItem;

public class WikiListActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getSimpleName();
    private List<WikiItem> wikiItems;

    private List<HKImageView> hkImageViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_list);
        ButterKnife.bind(this);
        searchView((ViewGroup) getWindow().getDecorView());
        init();
        Observable<List<WikiItem>> observable = Observable.create(new Observable.OnSubscribe<List<WikiItem>>() {
            @Override
            public void call(Subscriber<? super List<WikiItem>> subscriber) {
                Gson gson = new Gson();
                InputStreamReader inputStreamReader = null;
                AssetManager assetManager = WikiListActivity.this.getAssets();
                try {
                    inputStreamReader = new InputStreamReader(assetManager.open("list2.json"));
                    IdentifyResponse identifyResponse = gson.fromJson(inputStreamReader, IdentifyResponse.class);
                    List<WikiItem> list = new ArrayList<>();
                    for (IdentifyResponse.ListBean listBean : identifyResponse.getList()) {
                        list.add(WikiItem.from(listBean));
                    }
                    inputStreamReader.close();
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("wiki list", "call: ", e);
                }
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<WikiItem>>() {
            @Override
            public void onCompleted() {
                Log.d("wiki list", "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<WikiItem> wikiItems) {
                 setWikiItems(wikiItems);
            }
        });
//        fetchList();

    }

    public void fetchList() {
        ServiceRest.getInstance().getServiceApi()
                .getList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IdentifyResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(IdentifyResponse identifyResponse) {
                        if (identifyResponse.getStatus() == 0) {
                            List<WikiItem> list = new ArrayList<>();
                            for (IdentifyResponse.ListBean listBean : identifyResponse.getList()) {
                                list.add(WikiItem.from(listBean));
                            }
                            setWikiItems(list);
                            init();
                        } else {
                            Toast.makeText(WikiListActivity.this, identifyResponse.getStatus() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setWikiItems(List<WikiItem> wikiItems) {
        this.wikiItems = wikiItems;
    }

    private void init() {
        for (int i = 0; i < hkImageViews.size(); i++) {
            final HKImageView hkImageView = hkImageViews.get(i);
            final int pos = i;
            hkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (hkImageView.getState() != 0) {
                        if (wikiItems == null) {
                            Log.e("wiki detail", "onClick: wiki items is null");
                            return;
                        }
                        Intent intent = new Intent(WikiListActivity.this, WikiDetailActivity.class);
                        Bundle bundle = new Bundle();
                        intent.setExtrasClassLoader(WikiItem.Item.class.getClassLoader());
                        if (pos < wikiItems.size()) {
                            bundle.putSerializable(WikiDetailActivity.IDENTIFY, wikiItems.get(pos));
                        } else {
                            bundle.putSerializable(WikiDetailActivity.IDENTIFY, wikiItems.get(wikiItems.size()-1));
                        }
                        intent.putExtra("data", bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(WikiListActivity.this, "还未解锁哦", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void searchView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                searchView((ViewGroup) child);
            } else if (child instanceof HKImageView) {
                hkImageViews.add((HKImageView) child);
            }
        }
    }

    void wikiDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.wiki_dialog, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        TextView wikiText = (TextView) view.findViewById(R.id.wiki_text);
    }


}

