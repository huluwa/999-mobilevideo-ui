package com.video.ui.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.tv.ui.metro.model.VideoItem;

import java.net.URLEncoder;

/**
 * Created by liuhuadong on 9/16/14.
 */
public abstract class GenericSubjectLoader<T> extends BaseGsonLoader<GenericBlock<T>>{
    public static int VIDEO_SUBJECT_LOADER_ID   = 0x901;
    public static int VIDEO_SUBJECT_SEARCH_LOADER_ID   = 0x902;
    public GenericSubjectLoader(Context con, DisplayItem item){
        super(con, item);
    }

    public static GenericSubjectLoader<DisplayItem> generateTabsLoader(final Context con, DisplayItem item){
        GenericSubjectLoader<DisplayItem> loader = new GenericSubjectLoader<DisplayItem>(con, item){

            @Override
            public void setCacheFileName() {
                cacheFileName = "tabs_album_";
            }

            @Override
            public void setLoaderURL(DisplayItem _item) {
                mItem = _item;

                String url ;
                if(_item.ns.equals("home")) {
                    url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/mobile_port.json";
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
                else {
                    url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_movie.json";
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                    //
                    //TODO, not defined
                    //super.setLoaderURL(_item);
                }
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<GenericBlock<DisplayItem>> gsonRequest = new GsonRequest<GenericBlock<DisplayItem>>(calledURL, new TypeToken<GenericBlock<DisplayItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }


    public static GenericSubjectLoader<VideoItem> generateVideoSubjectLoader(Context con, DisplayItem item){
        GenericSubjectLoader<VideoItem> loader = new GenericSubjectLoader<VideoItem>(con, item){
            @Override
            public void setCacheFileName() {
                cacheFileName = "video_album_";
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<GenericBlock<VideoItem>> gsonRequest = new GsonRequest<GenericBlock<VideoItem>>(calledURL, new TypeToken<GenericBlock<VideoItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }

    public static GenericSubjectLoader<VideoItem> generateVideoSearchLoader(Context con, final String keyword){
        String mSearchword = keyword;
        GenericSubjectLoader<VideoItem> loader = new GenericSubjectLoader<VideoItem>(con, null){
            @Override
            public void setCacheFileName() {
                cacheFileName = "video_search_";
            }

            @Override
            public void setLoaderURL(DisplayItem _item) {
                if(TextUtils.isEmpty(mKeyword) == false) {
                    String url = CommonUrl.BaseURL + "video/search?q=" + URLEncoder.encode(mKeyword) /*+ "&page="+page*/;
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
            }

            @Override
            public void setSearchKeyword(String key) {
                super.setSearchKeyword(key);

                if(TextUtils.isEmpty(key) == false) {
                    String url = CommonUrl.BaseURL + "video/search?q=" + URLEncoder.encode(key) /*+ "&page="+page*/;
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<GenericBlock<VideoItem>> gsonRequest = new GsonRequest<GenericBlock<VideoItem>>(calledURL, new TypeToken<GenericBlock<VideoItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setShouldCache(false);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mKeyword + ".search.cache");
                requestQueue.add(gsonRequest);
            }
        };
        loader.setSearchKeyword(mSearchword);
        return  loader;
    }

    @Override
    public void setLoaderURL(DisplayItem _item) {
        mItem = _item;
        String url = CommonUrl.BaseURL + mItem.ns + "/" + mItem.type + "?id=" + mItem.id + "&page="+page;
        calledURL = new CommonUrl(getContext()).addCommonParams(url);
    }

    public boolean hasMoreData() {
        if(mResult != null && mResult.blocks != null && mResult.blocks.size() > 0 && mResult.blocks.get(0).items.size() == page_size){
            return  true;
        }
        return false;
    }

    public boolean isLoading() {
        return mIsLoading;
    }


    public void nextPage() {
        page++;
        //load from server
        mIsLoading = true;
        String url = CommonUrl.BaseURL + mItem.ns + "/" + mItem.type + "?id=" + mItem.id + "&page="+(page);
        Log.d("nextpage", "page="+(page));
        calledURL = new CommonUrl(getContext()).addCommonParams(url);
        loadDataByGson();
    }
}