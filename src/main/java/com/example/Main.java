package com.example;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.subscription.SubscriptionTransport;
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] av) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new AuthorizationInterceptor()).build();
        SubscriptionTransport.Factory factory = new WebSocketSubscriptionTransport.Factory("wss://apollo-fullstack-tutorial.herokuapp.com/graphql", okHttpClient);
        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
                .subscriptionTransportFactory(factory)
                .okHttpClient(okHttpClient)
                .build();

        apolloClient.query(new LaunchListQuery()).enqueue(new ApolloCall.Callback<>() {
            @Override
            public void onResponse(@NotNull Response<LaunchListQuery.Data> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
            }
        });


        List<String> tripList = new ArrayList<>();
        tripList.add("109");
        tripList.add("108");
        apolloClient.mutate(new BookTripMutation(tripList)).enqueue(new ApolloCall.Callback<>() {
            @Override
            public void onResponse(@NotNull Response<BookTripMutation.Data> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
            }
        });


        apolloClient.subscribe(new TripsBookedSubscription())
                .execute(new ApolloSubscriptionCall.Callback<>() {

                    @Override
                    public void onCompleted() {
                        System.out.println("Subscription Completed");
                    }

                    @Override
                    public void onTerminated() {
                        System.out.println("Subscription Terminated");
                    }

                    @Override
                    public void onConnected() {
                        System.out.println("Subscription Connected");
                    }

                    @Override
                    public void onResponse(@NotNull Response<TripsBookedSubscription.Data> response) {
                        System.out.println(response);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException exception) {
                        exception.printStackTrace();
                    }
                });
    }
}

class AuthorizationInterceptor implements Interceptor {
    @Override
    @NotNull
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Ym9kQGpyYWYub3Jn")
                .build();

        return chain.proceed(request);
    }
}
