package demo.app.com.horizontalscrolldemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author：JianFeng
 * @date：2019/3/26 16:17
 * @description：
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ItemViewHolder> {


    private Context context;
    private List<Entity> datas;
    private List<ItemViewHolder> mViewHolderList = new ArrayList<>();
    private int offestX = 0;
    private OnContentScrollListener onContentScrollListener;

    public interface OnContentScrollListener {
        void onScroll(int offestX);
    }

    public void setOnContentScrollListener(OnContentScrollListener onContentScrollListener) {
        this.onContentScrollListener = onContentScrollListener;
    }


    public ContentAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<Entity> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_content, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.tvLeftTitle.setText(datas.get(i).getLeftTitle());
        //右边滑动部分
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        itemViewHolder.rvItemRight.setLayoutManager(linearLayoutManager);
        itemViewHolder.rvItemRight.setHasFixedSize(true);
        RightScrollAdapter rightScrollAdapter = new RightScrollAdapter(context);
        rightScrollAdapter.setDatas(datas.get(i).getRightDatas());
        itemViewHolder.rvItemRight.setAdapter(rightScrollAdapter);
        //缓存当前holder
        if (!mViewHolderList.contains(itemViewHolder)) {
            mViewHolderList.add(itemViewHolder);
        }
        //滑动单个ytem的rv时,右侧整个区域的联动
        itemViewHolder.horItemScrollview.setOnCustomScrollChangeListener(new CustomHorizontalScrollView.OnCustomScrollChangeListener() {
            @Override
            public void onCustomScrollChange(CustomHorizontalScrollView listener, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                for (int i = 0; i < mViewHolderList.size(); i++) {
                    ItemViewHolder touchViewHolder = mViewHolderList.get(i);
                    if (touchViewHolder != itemViewHolder) {
                        touchViewHolder.horItemScrollview.scrollTo(scrollX, 0);
                    }
                }
                //记录滑动距离,便于处理下拉刷新之后的还原操作
                if (null != onContentScrollListener) onContentScrollListener.onScroll(scrollX);
                offestX = scrollX;
            }
        });
        //由于viewHolder的缓存,在1级缓存取出来是2个viewholder,并且不会被重新赋值,所以这里需要处理缓存的viewholder的位移
        itemViewHolder.horItemScrollview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!itemViewHolder.isLayoutFinish()) {
                    itemViewHolder.horItemScrollview.scrollTo(offestX, 0);
                    itemViewHolder.setLayoutFinish(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == datas ? 0 : datas.size();
    }

    public List<ItemViewHolder> getViewHolderCacheList() {
        return mViewHolderList;
    }

    public int getOffestX() {
        return offestX;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_left_title)
        TextView tvLeftTitle;
        @BindView(R.id.rv_item_right)
        RecyclerView rvItemRight;
        @BindView(R.id.hor_item_scrollview)
        CustomHorizontalScrollView horItemScrollview;
        private boolean isLayoutFinish;//自定义字段,用于标记layout

        public boolean isLayoutFinish() {
            return isLayoutFinish;
        }

        public void setLayoutFinish(boolean layoutFinish) {
            isLayoutFinish = layoutFinish;
        }

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
