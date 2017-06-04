package sssta.org.hakeday.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sssta.org.hakeday.BezelImageView;
import sssta.org.hakeday.Constants;
import sssta.org.hakeday.HKApplication;
import sssta.org.hakeday.R;
import sssta.org.hakeday.network.WikiItem;

public class WikiDetailActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static final String IDENTIFY = "identify";
    @BindView(R.id.arrow_more)
    LinearLayout arrowMore;
    private WikiItem wikiItem;
    private List<Object> lables;
    boolean canScroll;
    boolean hasShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_detail);
        ButterKnife.bind(this);
        getIntent().setExtrasClassLoader(WikiItem.Item.class.getClassLoader());
        wikiItem = (WikiItem) getIntent().getBundleExtra("data").getSerializable(IDENTIFY);
        lables = wikiItem.getLables();
        final MLinearLayoutManager mLinearLayoutManager = new MLinearLayoutManager(this);
        if (wikiItem != null) {
            Adapter adapter = new Adapter();
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            Log.d("wiki", "onCreate: wiki is null");
        }
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0 || mLinearLayoutManager.findLastCompletelyVisibleItemPosition() != lables.size()) {
                    if (!canScroll) {
                        canScroll = true;
                        mLinearLayoutManager.setScrollEnabled(false);
                        arrowMore.setVisibility(View.VISIBLE);
                        arrowMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                arrowMore.setVisibility(View.GONE);
                                mLinearLayoutManager.setScrollEnabled(true);
                                hasShown = true;
                            }
                        });
                    }
                } else {
                    if (!hasShown) {
                        canScroll = false;
                    }
                }
            }
        });
    }

    class Adapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                return HeaderViewHolder.from(parent);
            } else if (viewType == 1) {
                return SubspeciesLableHolder.from(parent);
            }
            return ItemViewHolder.from(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderViewHolder) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                Glide.with(headerViewHolder.bezelImageView.getContext())
                        .load(Constants.BASE_URL + "/" +wikiItem.getImage())
                        .into(headerViewHolder.bezelImageView);
                headerViewHolder.textCnName.setText(wikiItem.getCnName());
                headerViewHolder.textCnName.setVisibility(TextUtils.isEmpty(wikiItem.getCnName()) ? View.GONE : View.VISIBLE);
                headerViewHolder.textEnName.setText(wikiItem.getEnName());
                headerViewHolder.textEnName.setVisibility(TextUtils.isEmpty(wikiItem.getEnName()) ? View.GONE : View.VISIBLE);
            } else if (holder instanceof SubspeciesLableHolder) {
                SubspeciesLableHolder subspeciesLableHolder = (SubspeciesLableHolder) holder;
                String lable = (String) lables.get(position - 1);
                subspeciesLableHolder.subspeciesName.setText(lable);
                subspeciesLableHolder.itemView.setVisibility(TextUtils.isEmpty(lable) ? View.GONE : View.VISIBLE);
            } else {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                WikiItem.Item item = (WikiItem.Item) lables.get(position - 1);
                itemViewHolder.textTitle.setText(item.getTitle());
                itemViewHolder.textContent.setText(item.getContent());
                viewVisibleByString(itemViewHolder.textTitle, item.getTitle());
                viewVisibleByString(itemViewHolder.textContent, item.getContent());
            }
        }

        private void viewVisibleByString(View view, String s) {
            view.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return wikiItem.getLables().size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            } else if (lables.get(position - 1) instanceof String) {
                return 1;
            }
            return 2;
        }
    }
}

class HeaderViewHolder extends RecyclerView.ViewHolder {

    BezelImageView bezelImageView;
    TextView textCnName;
    TextView textEnName;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        bezelImageView = (BezelImageView) itemView.findViewById(R.id.bezelImageView);
        textCnName = (TextView) itemView.findViewById(R.id.text_cn_name);
        textEnName = (TextView) itemView.findViewById(R.id.text_en_name);
        textCnName.setTypeface(HKApplication.getInstance().getZaoziTypeFace());
        textEnName.setTypeface(HKApplication.getInstance().getZaoziTypeFace());
    }

    public static HeaderViewHolder from(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wiki_header, parent, false);
        return new HeaderViewHolder(view);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView textTitle;
    TextView textContent;

    public ItemViewHolder(View itemView) {
        super(itemView);
        textTitle = (TextView) itemView.findViewById(R.id.text_title);
        textContent = (TextView) itemView.findViewById(R.id.text_content);
        textTitle.setTypeface(HKApplication.getInstance().getZaoziTypeFace());
        textContent.setTypeface(HKApplication.getInstance().getOCRAStdTypeFace());
    }

    public static ItemViewHolder from(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wiki_item, parent, false);
        return new ItemViewHolder(view);
    }
}

class SubspeciesLableHolder extends RecyclerView.ViewHolder {

    TextView subspeciesName;

    public SubspeciesLableHolder(View itemView) {
        super(itemView);
        subspeciesName = (TextView) itemView.findViewById(R.id.subspecies_name);
        subspeciesName.setTypeface(HKApplication.getInstance().getZaoziTypeFace());
    }

    public static SubspeciesLableHolder from(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subspecies_name, parent, false);
        return new SubspeciesLableHolder(view);
    }
}

class MLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public MLinearLayoutManager(Context context) {
        super(context);
    }

    public MLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
