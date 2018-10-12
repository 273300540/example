package com.xlm.example.tools;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;


public class ExecuteCallAdapter<T> implements CallAdapter<T, T> {
    private Type type;
    private Annotation[] annotations;
    private Retrofit retrofit;

    private ExecuteCallAdapter(Type type, Annotation[] annotations, Retrofit retrofit) {
        this.type = type;
        this.annotations = annotations;
        this.retrofit = retrofit;
    }

    @Override
    public Type responseType() {
        return type;
    }

    @Override
    public T adapt(Call<T> call) {
        try {

            Response<T> response = call.execute();
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class ExecuteCallAdapterFacatory extends CallAdapter.Factory {
        @Override
        public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            CallAdapter<?, ?> adapter = nextCallAdapter(this, returnType, annotations, retrofit);
            if (adapter != null) {
                return adapter;
            }
            return new ExecuteCallAdapter(returnType, annotations, retrofit);
        }

        public CallAdapter<?, ?> nextCallAdapter(CallAdapter.Factory skipPast, Type returnType,
                                                 Annotation[] annotations, Retrofit retrofit) {
            List<Factory> callAdapterFactories = retrofit.callAdapterFactories();
            int start = callAdapterFactories.indexOf(skipPast) + 1;
            for (int i = start, count = callAdapterFactories.size(); i < count; i++) {
                CallAdapter<?, ?> adapter = callAdapterFactories.get(i).get(returnType, annotations, retrofit);
                if (adapter != null) {
                    return adapter;
                }
            }
            return null;
        }
    }
}
