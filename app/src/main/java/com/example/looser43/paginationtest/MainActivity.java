package com.example.looser43.paginationtest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.looser43.paginationtest.api.MovieApi;
import com.example.looser43.paginationtest.api.MovieService;
import com.example.looser43.paginationtest.model.DataItem;
import com.example.looser43.paginationtest.model.TopUpHistoryModel;
import com.example.looser43.paginationtest.adapter.PaginationAdapter;
import com.example.looser43.paginationtest.model.Topuphis;
import com.example.looser43.paginationtest.utils.PaginationAdapterCallback;
import com.example.looser43.paginationtest.utils.PaginationScrollListener;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PaginationAdapterCallback{

    private static final String TAG = "MainActivity";

    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 4;
    private int currentPage = PAGE_START;

    private MovieService movieService;

    private String authcode, logcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authcode = "2116e70a69fe990df9e41d0ff379f845";
        logcode = "59991c9a75bc1d783487281c275a99b8";

        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        adapter = new PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        movieService = MovieApi.getClient().create(MovieService.class);

        loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage();
            }
        });

    }


    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();

        callTopRatedMoviesApi().enqueue(new Callback<TopUpHistoryModel>() {
            @Override
            public void onResponse(Call<TopUpHistoryModel> call, Response<TopUpHistoryModel> response) {
                // Got data. Send it to adapter

                hideErrorView();

                List<DataItem> results = fetchResults(response);
                Log.wtf("****ResultSize",""+results.size());
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<TopUpHistoryModel> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }

    /**
     * @param response extracts List<{@link DataItem>} from response
     * @return
     */
    private List<DataItem> fetchResults(Response<TopUpHistoryModel> response) {
        TopUpHistoryModel topRatedMovies = response.body();
        Log.wtf("****topup",""+response.isSuccessful());
        Topuphis topuphis = topRatedMovies.getTopuphis();
        Log.wtf("****topuphis",""+topuphis);
        return topuphis.getData();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<TopUpHistoryModel>() {
            @Override
            public void onResponse(Call<TopUpHistoryModel> call, Response<TopUpHistoryModel> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<DataItem> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<TopUpHistoryModel> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<TopUpHistoryModel> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies(
                authcode,
                logcode,
                currentPage
        );
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }


    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     * @return
     */
    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    // Helpers -------------------------------------------------------------------------------------


    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
