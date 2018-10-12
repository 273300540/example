package com.xlm.example;

import com.xlm.example.http.Contributor;
import com.xlm.example.http.HttpTest;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.CallAdapter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

public class RetriotMain {
    private static final String API_URL = "https://api.github.com";

    static final class ExceptionCatchingRequestBody extends ResponseBody {
        private final ResponseBody delegate;
        IOException thrownException;

        ExceptionCatchingRequestBody(ResponseBody delegate) {
            this.delegate = delegate;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            return delegate.contentLength();
        }

        @Override
        public BufferedSource source() {
            return Okio.buffer(new ForwardingSource(delegate.source()) {
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    try {
                        return super.read(sink, byteCount);
                    } catch (IOException e) {
                        thrownException = e;
                        throw e;
                    }
                }
            });
        }

        @Override
        public void close() {
            delegate.close();
        }

        void throwIfCaught() throws IOException {
            if (thrownException != null) {
                throw thrownException;
            }
        }
    }

    public static void main(String[] args) {
        Retrofit.Builder builder = new Retrofit.Builder();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                                    @Override
                                    public okhttp3.Response intercept(Chain chain) throws IOException {
                                        System.out.println(chain.request());
                                        okhttp3.Response response = chain.proceed(chain.request());
                                        System.out.println(response);
                                        return response;
                                    }
                                }
                ).build();
        builder.callFactory(httpClient);
        builder.baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(new CallAdapter.Factory() {
            @Override
            public CallAdapter<Response, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
                return new CallAdapter<Response, Object>() {
                    @Override
                    public Type responseType() {
                        return returnType;
                    }

                    @Override
                    public Object adapt(Call<Response> call) {

                        try {
                            Response object = call.execute();
                           return object.body();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    }

                };
            }
        });
        Retrofit retrofit = builder.build();
        HttpTest test = retrofit.create(HttpTest.class);

        List<Contributor>  contributors = test.contributors("square", "retrofit");
        System.out.println(contributors);

    }

}
