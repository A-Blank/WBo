package com.wbo.Utils;

/**
 * Created by 丶 on 2017/3/19.
 */

public class RequestUrl {

    private final static String Trending_URL = "http://api.weibo.cn/2/cardlist?" +
            "c=android&containerid=106003type%3D25%26t%3D3%26disable_hot%3D1%26filter_type%3Drealtimehot" +
            "&gsid=_2A2515e3vDeRxGeRM7lER8SbEyz6IHXVU3EA3rDV6PUJbkdANLW3kkWpp0LHRKAUHpxRBKMbr_JzHMYjRpg..&s=d8dd255e";

    private final static String Trending_Detail_URL="http://api.weibo.cn/2/cardlist?" +
            "c=android&containerid=100103type%3D1%26q%3D%E9%AB%98%E5%9C%86%E5%9C%86+%E8%B5%B5%E9%9B%B7%26t%3D3" +
            "&gsid=_2A2515e3vDeRxGeRM7lER8SbEyz6IHXVU3EA3rDV6PUJbkdANLW3kkWpp0LHRKAUHpxRBKMbr_JzHMYjRpg..&s=d8dd255e";

    private final static String Hot_Topic_URL="http://api.weibo.cn/2/guest/cardlist?" +
            "c=android&i=a76efdd&s=9ad5aa04&gsid=_2AkMviY1ef8NhqwJRmP0TyWPiaoV-zQnEieKZ1XyFJRM3HRl-3T9kqmdYtRVkZs8Qlb_TC3Fpsqgy8jGAKrsRwQ..&containerid=230584";

    public static String getTrending_Detail_URL() {
        return Trending_Detail_URL;
    }

    public static String getHot_Topic_URL() {
        return Hot_Topic_URL;
    }

    public static String getTrending_URL() {
        return Trending_URL;
    }

}
