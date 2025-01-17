package com.yuukidach.ucount;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.yuukidach.ucount.view.adapter.GridRecyclerAdapter;
import com.yuukidach.ucount.view.adapter.ViewPagerAdapter;
import com.yuukidach.ucount.model.MoneyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuukidach on 17-3-12.
 */

public class EarnFragment extends Fragment {

    private final String[] titles = {"一般", "工资", "红包", "兼职", "奖金", "零花钱", "保险", "投资", "外汇", "生活费",
            "意外收获", "分红", "生意"};
    private List<MoneyItem> mDatas;
    private LayoutInflater inflater;
    private ImageView itemImage;
    private TextView itemTitle;
    private RelativeLayout itemLayout;
    private ExtensiblePageIndicator extensiblePageIndicator;
    // 总的页数
    private int pageCount;

    // 每一页显示的个数
    private int pageSize = 18;

    // 当前显示的是第几页
    private int curIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getBannerId();

        View view = inflater.inflate(R.layout.earn_fragment, container, false);

        ViewPager mPager = view.findViewById(R.id.viewpager_2);
        extensiblePageIndicator = view.findViewById(R.id.ll_dot_2);

        // 初始化数据源
        initDatas();

        // 初始化上方banner
        changeBanner(mDatas.get(0));

        // 总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        List<View> mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.item_recycler_grid, mPager,false);
            MyGridLayoutManager layoutManager = new MyGridLayoutManager(getContext(), 6);
            recyclerView.setLayoutManager(layoutManager);
            GridRecyclerAdapter adaper = new GridRecyclerAdapter(mDatas, i, pageSize);
            recyclerView.setAdapter(adaper);

            mPagerList.add(recyclerView);

            adaper.setOnItemClickListener((view1, position) -> changeBanner(mDatas.get(position)));
        }
        // 设置适配器
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        extensiblePageIndicator.initViewPager(mPager);

        return view;
    }

    // 初始化数据源
    private void initDatas() {
        mDatas = new ArrayList<MoneyItem>();
        for (int i = 1; i <= titles.length; i++) {
            mDatas.add(new MoneyItem("type_big_n" + i, titles[i-1]));
        }
    }

    // 获得AddItemActivity对应的控件，用来提示已选择的项目类型
    public void getBannerId() {
        itemImage = getActivity().findViewById(R.id.chosen_image);
        itemTitle = getActivity().findViewById(R.id.chosen_title);
        itemLayout = getActivity().findViewById(R.id.have_chosen);
    }

    // 改变banner状态
    public void changeBanner(MoneyItem tmpItem) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), tmpItem.getTypeImageId());
        Palette.Builder pb = new Palette.Builder(bm);
        pb.maximumColorCount(1);

        itemImage.setImageResource(tmpItem.getTypeImageId());
        itemTitle.setText(tmpItem.getTypeName());
        itemImage.setTag(MoneyItem.InOutType.EARN);  // 保留图片资源属性
        itemTitle.setTag(tmpItem.getTypeImgId());      // 保留图片资源名称作为标签，方便以后调用

        // 获取图片颜色并改变上方banner的背景色
        pb.generate(palette -> {
            Palette.Swatch swatch = palette.getSwatches().get(0);
            if (swatch != null) {
                itemLayout.setBackgroundColor(swatch.getRgb());
            }
        });

    }
}