package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.zyq.kaminotetest.Adapter.MyPagerAdapter;
import com.example.zyq.kaminotetest.Data.DataClass;
import com.example.zyq.kaminotetest.R;

import java.util.ArrayList;

/**
 * Created by TALK_SWORD on 2018/3/23.
 */

public class DiscoveryFragment extends Fragment {
    private String mfrom;
    private int Color_id;
    private ImageView imageView1;
    private TextView textView1;


    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, null);
        //设置Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Color_id", Context.MODE_PRIVATE);
        Color_id = sharedPreferences.getInt("id", 0);
        if (Color_id != 0) {
            toolbar.setBackgroundResource(Color_id);
        }


        if (DataClass.allowInternet) {
            mTabLayout = view.findViewById(R.id.discovery_tab_layout);      //获取tabLayout
            mViewPager = view.findViewById(R.id.discovery_view_pager);      //获取viewPager
            imageView1 = view.findViewById(R.id.heart_soup_pic_1);
            textView1 = view.findViewById(R.id.heart_soup_text_1);
            imageView1.setImageResource(R.drawable.wwdc_iphone);
            textView1.setText("生活就像海洋，只有意志坚强的人才能到达彼岸");
            initViewPager();    //初始化viewPager

        } else {
            ScrollView scrollView = view.findViewById(R.id.discovery_all_things);
            scrollView.setVisibility(View.GONE);
            TextView disabled = view.findViewById(R.id.analyze_disabled);
            disabled.setVisibility(View.VISIBLE);
        }





        return view;
    }



    public static DiscoveryFragment newInstance(String from){
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mfrom = getArguments().getString("from");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    //初始化viewPager
    private void initViewPager() {

        // 创建一个集合,装填Fragment
        ArrayList<Fragment> fragments = new ArrayList<>();
        // 创建ViewPager适配器
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());

        // 装填
        fragments.add(new EmotionStatusFragment());
        fragments.add(new StatisticFragment());
//        fragments.add(new EmotionStatusFragment());


        myPagerAdapter.setFragments(fragments);
        // 给ViewPager设置适配器
        mViewPager.setAdapter(myPagerAdapter);

        // 使用 TabLayout 和 ViewPager 相关联
        mTabLayout.setupWithViewPager(mViewPager);

        // 设置标题
        mTabLayout.getTabAt(0).setText("心情波动");
        mTabLayout.getTabAt(1).setText("实时情绪");
//        mTabLayout.getTabAt(2).setText("小贴士");
    }

}
