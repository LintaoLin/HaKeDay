package sssta.org.hakeday.network;

import java.util.List;

/**
 * Created by mac on 2017/6/4.
 */

public class AnalysisResponse {

    /**
     * predict : {"chinese_name":"花狭口蛙","content":[{"text":"花狭口蛙为姬蛙科狭口蛙属的两栖动物。分布范围从缅甸，泰国，老挝，柬埔寨和越南，南部向马来半岛，印尼的苏门答腊、婆罗洲(坤甸)， 苏拉威西岛 (锡江、帕卢 和弗洛勒斯岛)和刁曼岛、布吉岛、浮罗交怡 和新加坡。 印度东北部(西孟加拉邦西部和阿萨姆)和孟加拉国。 在中国大陆，分布于福建、广东、广西、海南、云南等地。该物种的模式产地在中国。本种亦被引进至台湾。背部有黄色纹路，体长7-8厘米，寿命可达10年。","title":"","content":[{"text":"花狭口蛙指名亚种（学名：Kaloula pulchra pulchra），Gray于1831年命名。在中国大陆，分布于福建、广东、广西、云南等地，多生活于树洞。该物种的模式产地在中国。","title":"花狭口蛙指名亚种"},{"text":"花狭口蛙海南亚种（学名：Kaloula pulchra hainana），Gressitt于1938年命名。在中国大陆，分布于广东、海南等地。该物种的模式产地在海南。","title":"花狭口蛙海南亚种"}]},{"content":[{"text":"花狭口蛙指名亚种（学名：Kaloula pulchra pulchra），Gray于1831年命名。在中国大陆，分布于福建、广东、广西、云南等地，多生活于树洞。该物种的模式产地在中国。","title":"花狭口蛙指名亚种"},{"text":"花狭口蛙海南亚种（学名：Kaloula pulchra hainana），Gressitt于1938年命名。在中国大陆，分布于广东、海南等地。该物种的模式产地在海南。","title":"花狭口蛙海南亚种"}],"title":"亚种"}],"image":"static/kaloula_pulchra.jpg","name":"kaloula_pulchra"}
     * status : 0
     */

    private PredictBean predict;
    private int status;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public PredictBean getPredict() {
        return predict;
    }

    public void setPredict(PredictBean predict) {
        this.predict = predict;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class PredictBean {
        /**
         * chinese_name : 花狭口蛙
         * content : [{"text":"花狭口蛙为姬蛙科狭口蛙属的两栖动物。分布范围从缅甸，泰国，老挝，柬埔寨和越南，南部向马来半岛，印尼的苏门答腊、婆罗洲(坤甸)， 苏拉威西岛 (锡江、帕卢 和弗洛勒斯岛)和刁曼岛、布吉岛、浮罗交怡 和新加坡。 印度东北部(西孟加拉邦西部和阿萨姆)和孟加拉国。 在中国大陆，分布于福建、广东、广西、海南、云南等地。该物种的模式产地在中国。本种亦被引进至台湾。背部有黄色纹路，体长7-8厘米，寿命可达10年。","title":""},{"content":[{"text":"花狭口蛙指名亚种（学名：Kaloula pulchra pulchra），Gray于1831年命名。在中国大陆，分布于福建、广东、广西、云南等地，多生活于树洞。该物种的模式产地在中国。","title":"花狭口蛙指名亚种"},{"text":"花狭口蛙海南亚种（学名：Kaloula pulchra hainana），Gressitt于1938年命名。在中国大陆，分布于广东、海南等地。该物种的模式产地在海南。","title":"花狭口蛙海南亚种"}],"title":"亚种"}]
         * image : static/kaloula_pulchra.jpg
         * name : kaloula_pulchra
         */

        private String chinese_name;
        private String image;
        private String name;
        private List<IdentifyResponse.ListBean.ContentBeanX> content;

        public String getChinese_name() {
            return chinese_name;
        }

        public void setChinese_name(String chinese_name) {
            this.chinese_name = chinese_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<IdentifyResponse.ListBean.ContentBeanX> getContent() {
            return content;
        }

        public void setContent(List<IdentifyResponse.ListBean.ContentBeanX> content) {
            this.content = content;
        }

    }
}
