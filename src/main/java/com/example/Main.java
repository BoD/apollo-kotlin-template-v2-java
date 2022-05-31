package com.example;


import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Main {
    public static void main(String[] av) {
        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
                .okHttpClient(new OkHttpClient.Builder().addInterceptor(new AuthorizationInterceptor()).build())
                .build();

        apolloClient.query(new LaunchListQuery()).enqueue(new ApolloCall.Callback<>() {
            @Override
            public void onResponse(@NotNull Response<LaunchListQuery.Data> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                System.out.println("Failure");
            }
        });
    }
}

class AuthorizationInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Your token")
                .build();

        return chain.proceed(request);

    }
}
