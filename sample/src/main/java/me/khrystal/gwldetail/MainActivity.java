package me.khrystal.gwldetail;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import me.khrystal.libary.CommonHeaderLayout;
import me.khrystal.libary.OutsideDownLinearLayout;
import me.khrystal.libary.OutsideScrollView;
import me.khrystal.widget.DividerDecoration;
import me.khrystal.widget.KRecyclerView;

public class MainActivity extends AppCompatActivity implements KRecyclerView.LoadDataListener {

    private KRecyclerView mInsideRecyclerView;
    private OutsideScrollView mOutsideScrollView;
    private OutsideDownLinearLayout mOutsideDownLinearLayout;
    private CommonHeaderLayout mOutsideHeaderLayout;
    private CommonHeaderLayout mInsideHeaderLayout;
    private TextView mOutsideHeaderTextView;
    private TextView mInsideHeaderTextView;
    private View mOpenButton;


    private SimpleAdapter mAdapter;
    private List<String> mDataList;

    int start,end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initList();
        initConfig();
    }

    private void initConfig() {
        mOutsideHeaderLayout.setScrollView(mOutsideScrollView);
        mOutsideScrollView.setCommonHeaderLayout(mOutsideHeaderLayout);
        mOutsideScrollView.setOutSideDownLinearLayout(mOutsideDownLinearLayout);
        mOutsideDownLinearLayout.setCommonHeaderLayout(mOutsideHeaderLayout);
        mOutsideDownLinearLayout.setInsideRecyclerView(mInsideRecyclerView.mRecyclerView);
    }

    private void initView() {
        mOutsideScrollView = (OutsideScrollView) findViewById(R.id.scroll_view);
        mInsideRecyclerView = (KRecyclerView) findViewById(R.id.inside_rv);
        mOutsideDownLinearLayout = (OutsideDownLinearLayout) findViewById(R.id.body);
        mOutsideHeaderLayout = (CommonHeaderLayout) findViewById(R.id.header);
        mInsideHeaderLayout = (CommonHeaderLayout) getLayoutInflater()
                .inflate(R.layout.header_layout, mInsideRecyclerView, false);
        mOutsideHeaderTextView = (TextView) findViewById(R.id.header_text);
        mInsideHeaderTextView = (TextView) mInsideHeaderLayout.findViewById(R.id.header_text);
        mOpenButton = findViewById(R.id.open_outside);
        mOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOutsideDownLinearLayout.showWithAnim();
                mOutsideHeaderLayout.showWithAnim();
            }
        });

        mOutsideHeaderTextView.setText("Header");
        mInsideHeaderTextView.setText("Header");

        mInsideRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mOutsideScrollView.scrollBy(dx,dy);
            }
        });
    }

    private void initList() {
        mDataList = new ArrayList<String>();
        for (int i=0; i<100; i++) {
            mDataList.add("" + i);
        }
        mAdapter = new SimpleAdapter(MainActivity.this, mDataList);


        mInsideRecyclerView.setAdapter(mAdapter, 1, LinearLayoutManager.VERTICAL,false);
        mInsideRecyclerView.setLoadDataListener(this);
        mInsideRecyclerView.setItemCount(50);
        mInsideRecyclerView.isUseByNetWork(false);


        mInsideRecyclerView.addHeaderView(mInsideHeaderLayout);
//      init refresh
        PtrClassicDefaultHeader defaultHeader = new PtrClassicDefaultHeader(MainActivity.this);
        mInsideRecyclerView.mPtrFrameLayout.setHeaderView(defaultHeader);
        mInsideRecyclerView.mPtrFrameLayout.addPtrUIHandler(defaultHeader);

//      add divider
        final DividerDecoration divider = new DividerDecoration.Builder(MainActivity.this)
                .setHeight(1.0f)
                .setColor(android.graphics.Color.GRAY)
                .build();
        mInsideRecyclerView.addItemDecoration(divider);



//      auto refresh once
        mInsideRecyclerView.mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mInsideRecyclerView.mPtrFrameLayout.autoRefresh(true);
            }
        }, 400);


        mInsideRecyclerView.setLoadDataListener(this);
    }


    @Override
    public void loadData(final int page) {
        if (page==1){
            mAdapter.clear();
            start=0;end=0;
        }

        end = start + 50;
        new AsyncTask<Void,Void,List<String>>(){
            @Override
            protected List<String> doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> list = new ArrayList();
                if (page<=6) {
                    for (int i=start ;i < end; i++) {
                        list.add(String.valueOf(i));
                    }
                    start = end;
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                if (strings!=null&&strings.size()>0){
                    mInsideRecyclerView.hideEmptyView();
                    mAdapter.append(strings);
                    mAdapter.notifyDataSetChanged();
                    mInsideRecyclerView.enableLoadMore();
                }else {
                    mInsideRecyclerView.cantLoadMore();
                }
                mInsideRecyclerView.endRefreshing();
            }
        }.execute();
    }


    public class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

        List<String> dataList;
        Object parent;

        public SimpleAdapter(Object parent, List<String> dataList) {
            this.dataList = dataList;
            this.parent = parent;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            SimpleViewHolder holder = new SimpleViewHolder(v, parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            String model = dataList.get(position);
            holder.bind(model);
        }

        public void append(List<String> items) {
            int pos = dataList.size();
            for (String item : items) {
                dataList.add(item);
            }
            notifyItemRangeInserted(pos, items.size());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public void remove(int position) {
            if (dataList.size() > 0) {
                dataList.remove(position);
                this.notifyItemRemoved(position);
            }
        }

        public void clear() {
            int size = dataList.size();
            dataList.clear();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * ViewHolder
     */
    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected Activity mActivity;
        public View mView;
        TextView textView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public SimpleViewHolder(View itemView, Object parent) {
            super(itemView);
            if (parent == null)
                return;
            if (parent instanceof Activity) {
                mActivity = (Activity) parent;
            }
            textView = (TextView)itemView.findViewById(R.id.item_text);
            addOnClickListener(textView);

        }

        public void addOnClickListener(View view) {
            if (view != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_text:
                    Toast.makeText(MainActivity.this,this.str,Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        String str;
        public void bind(String str){
            if (!TextUtils.isEmpty(str))
                this.str = str;
            textView.setText(str);
        }


    }
}
