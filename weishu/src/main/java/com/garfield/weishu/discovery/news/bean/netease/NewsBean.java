package com.garfield.weishu.discovery.news.bean.netease;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class NewsBean implements Serializable {

    public static final int TYPE_SLIDE_HEAD = 0;
    public static final int TYPE_NORMAL = 1;

    /**
     * 类型
     */
    private int beanType;
    /**
     * docid
     */
    private String docid;
    /**
     * 标题
     */
    private String title;
    /**
     * 小内容
     */
    private String digest;
    /**
     * 图片地址
     */
    private String imgsrc;
    /**
     * 来源
     */
    private String source;
    /**
     * 时间
     */
    private String ptime;
    /**
     * TAG
     */
    private String tag;
    /**
     * www网址
     */
    private String url_3w;
    /**
     * 手机网址
     */
    private String url;
    /**
     * 回复数
     */
    private String replyCount;

    public int getBeanType() {
        return beanType;
    }

    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getUrl_3w() {
        return url_3w;
    }

    public void setUrl_3w(String url_3w) {
        this.url_3w = url_3w;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public JSONObject toJSONObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beanType", beanType);
            jsonObject.put("docid", docid);
            jsonObject.put("title", title);
            jsonObject.put("digest", digest);
            jsonObject.put("imgsrc", imgsrc);
            jsonObject.put("source", source);
            jsonObject.put("ptime", ptime);
            jsonObject.put("tag", tag);
            jsonObject.put("url_3w", url_3w);
            jsonObject.put("url", url);
            jsonObject.put("replyCount", replyCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static NewsBean parse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        NewsBean newsItem = new NewsBean();
        newsItem.setBeanType(jsonObject.optInt("beanType"));
        newsItem.setDocid(jsonObject.optString("docid"));
        newsItem.setTitle(jsonObject.optString("title"));
        newsItem.setDigest(jsonObject.optString("digest"));
        newsItem.setImgsrc(jsonObject.optString("imgsrc"));
        newsItem.setSource(jsonObject.optString("source"));
        newsItem.setPtime(jsonObject.optString("ptime"));
        newsItem.setTag(jsonObject.optString("tag"));
        newsItem.setUrl_3w(jsonObject.optString("url_3w"));
        newsItem.setUrl(jsonObject.optString("url"));
        newsItem.setReplyCount(jsonObject.optString("replyCount"));
        return newsItem;
    }
}
