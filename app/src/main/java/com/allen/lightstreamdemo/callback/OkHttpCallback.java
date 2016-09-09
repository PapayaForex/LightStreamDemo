package com.allen.lightstreamdemo.callback;

import com.allen.lightstreamdemo.bean.BaseBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by: allen on 16/8/23.
 */

public abstract class OkHttpCallback<T extends BaseBean> implements Callback {
    private Class<T> mClazz;

    public OkHttpCallback() {
        Type t = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) t).getActualTypeArguments();
        Class<T> cls = (Class<T>) params[0];

        this.mClazz = cls;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onFail(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().toString();
        T bean = new Gson().fromJson(result, mClazz);
        onSuccess(bean);
    }

    public abstract void onFail(Call call, IOException e);

    public abstract void onSuccess(T bean) throws IOException;
}
