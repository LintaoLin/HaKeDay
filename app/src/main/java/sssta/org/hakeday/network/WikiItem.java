package sssta.org.hakeday.network;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2017/6/4.
 */

public class WikiItem implements Serializable {
    private String cnName, enName, image;

    private List<Object> lables = new ArrayList<>();

    public WikiItem(String cnName, String enName, String image) {
        this.cnName = cnName;
        this.enName = enName;
        this.image = image;
    }

    public void convertData(List<IdentifyResponse.ListBean.ContentBeanX> data) {
        if (data == null) {
            return;
        }
        for (IdentifyResponse.ListBean.ContentBeanX c :
                data) {
            lables.add(c.getTitle());
            convertContent(c.getContent());
        }
    }

    public static WikiItem from(AnalysisResponse analysisResponse) {
        WikiItem wikiItem = new WikiItem(analysisResponse.getPredict().getChinese_name(),
                analysisResponse.getPredict().getName(), analysisResponse.getPredict().getImage());
        wikiItem.convertData(analysisResponse.getPredict().getContent());
        return wikiItem;
    }

    public static WikiItem from(IdentifyResponse.ListBean l) {
        WikiItem w = new WikiItem(l.getChinese_name(), l.getName(), l.getImage());
        w.convertData(l.getContent());
        return w;
    }
    private void convertContent(List<IdentifyResponse.ListBean.ContentBeanX.ContentBean> content) {
        if (content == null) return;
        for (IdentifyResponse.ListBean.ContentBeanX.ContentBean cb:
             content) {
            Item item = new Item(cb.getTitle(), cb.getText());
            lables.add(item);
        }
    }

    public String getCnName() {
        return cnName;
    }

    public String getEnName() {
        return enName;
    }

    public String getImage() {
        return image;
    }

    public List<Object> getLables() {
        return lables;
    }

    public static class Item implements Serializable {
        private String title;
        private String content;

        public Item(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        protected Item(Parcel in) {
            this.title = in.readString();
            this.content = in.readString();
        }
    }

    protected WikiItem(Parcel in) {
        this.cnName = in.readString();
        this.enName = in.readString();
        this.image = in.readString();
        this.lables = new ArrayList<Object>();
        in.readList(this.lables, Object.class.getClassLoader());
    }

}
