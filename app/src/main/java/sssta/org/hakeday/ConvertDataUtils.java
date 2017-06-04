package sssta.org.hakeday;

import java.util.ArrayList;
import java.util.List;

import sssta.org.hakeday.network.IdentifyResponse;
import sssta.org.hakeday.network.WikiItem;

/**
 * Created by mac on 2017/6/4.
 */

public class ConvertDataUtils {
    public static List<WikiItem> transformWikiItems(IdentifyResponse identifyResponse) {
        List<WikiItem> wikiItems = new ArrayList<>();
        List<IdentifyResponse.ListBean> listBeen  = identifyResponse.getList();
        for (IdentifyResponse.ListBean l :
                listBeen) {
            WikiItem w = new WikiItem(l.getChinese_name(), l.getName(), l.getImage());
            w.convertData(l.getContent());
            wikiItems.add(w);
        }
        return wikiItems;
    }
}
