package sssta.org.hakeday.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sssta.org.hakeday.network.DynamicData;
import sssta.org.hakeday.R;

public class TopListActivity extends AppCompatActivity {

    @BindView(R.id.frog_layout)
    RelativeLayout frogLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<DynamicData> dynamicDatas;
    private AnimatorSet[] animatorSets = new AnimatorSet[3];
    private ImageView[] imageViews = new ImageView[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Gson gson = new Gson();
        dynamicDatas = gson.fromJson("[\n" +
                "    {\"name\":\"丫丫\",\"operations\":\"识别到一只黑蟾蜍，并将其做成标本\"},\n" +
                "    {\"name\":\"大伟琴琴\",\"operations\":\"识别到一只番茄蛙，并将其-1s\"},\n" +
                "    {\"name\":\"zsr的大姐姐\",\"operations\":\"识别到一只金蟾蜍，并将其用锤子打死\"},\n" +
                "    {\"name\":\"最爱煲仔饭\",\"operations\":\"识别到一只海蟾蜍，并将其-1s\"},\n" +
                "    {\"name\":\"yichya\",\"operations\":\"识别到一只巴拿马金蟾蜍，并将其用针扎\"},\n" +
                "    {\"name\":\"magnus\",\"operations\":\"识别到一只美洲蟾蜍，并将其-1s\"},\n" +
                "    {\"name\":\"hdt123\",\"operations\":\"识别到一只欧洲绿蟾蜍，并将其播放LostRivers\"},\n" +
                "    {\"name\":\"double杰\",\"operations\":\"识别到一只榆树蟾蜍，并将其解剖\"},\n" +
                "    {\"name\":\"博导\",\"operations\":\"识别到一只海蟾蜍，并将其用锤子打死\"},\n" +
                "    {\"name\":\"长者\",\"operations\":\"识别到一只黑蟾蜍，并将其-1s\"},\n" +
                "    {\"name\":\"郑晓静\",\"operations\":\"识别到一只黄腹蟾蜍，并将其用火烧\"},\n" +
                "    {\"name\":\"john\",\"operations\":\"识别到一只花狭口蛙，并将其解剖\"},\n" +
                "    {\"name\":\"zxp\",\"operations\":\"识别到一只沙漠雨蛙，并将其-1s\"},\n" +
                "    {\"name\":\"李庚达是大变态\",\"operations\":\"识别到一只美洲蟾蜍，并将其用针扎\"},\n" +
                "    {\"name\":\"我永远不会猝死\",\"operations\":\"识别到一只花狭口蛙，并将其用锤子打死\"},\n" +
                "    {\"name\":\"斯塔克\",\"operations\":\"识别到一只黑蟾蜍，并将其做成标本\"},\n" +
                "    {\"name\":\"苟利国家生死以\",\"operations\":\"识别到一只欧洲火腹蟾蜍，并将其-1s\"},\n" +
                "]", new TypeToken<List<DynamicData>>(){}.getType());

        imageViews[0] = (ImageView) findViewById(R.id.image_one);
        imageViews[1] = (ImageView) findViewById(R.id.image_two);
        imageViews[2] = (ImageView) findViewById(R.id.image_three);
        for (int i = 0; i < animatorSets.length; i++){
            animatorSets[i] = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.shake_anim);
            animatorSets[i].setTarget(imageViews[i]);
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new Adapter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAnimation();
    }

    private void startAnimation() {
        if (animatorSets[0].isRunning()) {
            for (int i = 0; i < 3; i++) {
                if (animatorSets[i].isPaused()) {
                    animatorSets[i].resume();
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                animatorSets[i].start();
            }
        }
    }

    private void pauseAnimation() {
        if (animatorSets[0].isRunning() && !animatorSets[0].isPaused()) {
            for (int i = 0; i < 3; i++) {
                animatorSets[i].pause();
            }
        }
    }

    private void cancelAnimation() {
        if (animatorSets[0].isRunning()) {
            for (int i = 0; i < 3; i++) {
                animatorSets[i].cancel();
            }
        }
    }

    class Adapter extends RecyclerView.Adapter<DynamicViewHolder> {


        @Override
        public DynamicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return DynamicViewHolder.from(parent);
        }

        @Override
        public void onBindViewHolder(DynamicViewHolder holder, int position) {
            DynamicData dynamicData = dynamicDatas.get(position);
            if (dynamicData == null) {
                return;
            }
            holder.textView.setText(dynamicData.getName() + " " + dynamicData.getOperations());
        }

        @Override
        public int getItemCount() {
            return dynamicDatas.size();
        }
    }
}

class DynamicViewHolder extends RecyclerView.ViewHolder {

    TextView textView;
    public DynamicViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView;
    }

    public static DynamicViewHolder from(ViewGroup parent) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dynamic_item_view, parent, false);
        return new DynamicViewHolder(view);
    }
}
