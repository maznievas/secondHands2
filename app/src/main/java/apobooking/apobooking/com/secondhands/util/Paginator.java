package apobooking.apobooking.com.secondhands.util;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import java.util.List;

import apobooking.apobooking.com.secondhands.entity.Shop;
import io.reactivex.Flowable;

/**
 * Created by Oclemy on 12/8/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class Paginator {

    Context c;
    RecyclerView rv;
    private PullToLoadView pullToLoadView;
    private ShopsAdapter adapter;
    private boolean isLoading = false;
    private boolean hasLoadedAll = false;
    //  private int nextPage;
    private LoadMoreListener loadMoreListener;


    public Paginator(Context c, PullToLoadView pullToLoadView, LoadMoreListener loadMoreListener) {
        this.c = c;
        this.pullToLoadView = pullToLoadView;
        this.loadMoreListener = loadMoreListener;

        //RECYCLERVIEW
        RecyclerView rv = pullToLoadView.getRecyclerView();
        rv.setLayoutManager(new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false));
        rv.setNestedScrollingEnabled(true);

        adapter = new ShopsAdapter(c);
        rv.setAdapter(adapter);

        initializePaginator();
    }

    /*
    PAGE DATA
     */
    public void initializePaginator() {
        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setPullCallback(new PullCallback() {

            //LOAD MORE DATA
            @Override
            public void onLoadMore() {
                loadData();
            }

            //REFRESH AND TAKE US TO FIRST PAGE
            @Override
            public void onRefresh() {
                adapter.clear();
                hasLoadedAll = false;
                loadData();
            }

            //IS LOADING
            @Override
            public boolean isLoading() {
                return isLoading;
            }

            //CURRENT PAGE LOADED
            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadedAll;
            }
        });

        //pullToLoadView.initLoad();
    }

    public void initLoad()
    {
        pullToLoadView.initLoad();
    }

    /*
     LOAD MORE DATA
     SIMULATE USING HANDLERS
     */
    public void loadData() {
        Log.d("mLog", "Load data");
        isLoading = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                //ADD CURRENT PAGE'S DATA
//                for (int i=0;i<=5;i++)
//                {
//                    adapter.add(new Spaceship("Spaceship : "+String.valueOf(i)+" in Page : "+String.valueOf(page)));
//                }
                //adapter.addSelectedShops(loadMoreListener.loadMore());
                loadMoreListener.loadMore()
                        .subscribe(list -> {
                                    adapter.addSelectedShops(list);
                                },
                                throwable -> {
                                    Log.e("mLog", "select shops");
                                    throwable.printStackTrace();
                                });

                //UPDATE PROPETIES
                pullToLoadView.setComplete();
                isLoading = false;

            }
        }, 1000);
    }

    public interface LoadMoreListener {
        Flowable<List<Shop>> loadMore();
    }
}
