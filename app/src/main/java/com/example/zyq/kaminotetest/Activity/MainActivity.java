package com.example.zyq.kaminotetest.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyq.kaminotetest.Adapter.LabelAdapter;
import com.example.zyq.kaminotetest.Class.Label;
import com.example.zyq.kaminotetest.Class.MyNote;
import com.example.zyq.kaminotetest.Class.MyToast;
import com.example.zyq.kaminotetest.R;
import com.example.zyq.kaminotetest.Utils.DataGenerator;

import org.litepal.crud.DataSupport;

import java.util.List;

import fragment.HomeFragment;

/**
 * Created by zyq on 2018/3/6.
 * 项目名称：Kami Note
 * MainActivity
 * Updated on 2018/3/17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String ACTIVITY_TAG = "MainActivity";  //打印日志的TAG
    private BottomNavigationView bottomNavigationView;            //底部栏引用
    private Fragment[] fragments;                                  //布局管理列表

    public static MainActivity mainActivity;
    private DrawerLayout mDrawerLayout;         //滑动菜单
    private TextView tv_noMore;                 //没有更多内容的文本
    private long mExitTime = 0;                 //记录点击返回按钮的时间
    private LinearLayout mainView;
    private TextView textAddLabel;
    private ImageView imageAddLabel;

    public static List<MyNote> mNote;           //保存note的列表
    public static List<Label> mLabel;           //
    public static int notePosition;             //记录笔记位置
    public RecyclerView noteListView;           //RecyclerView 的note 列表
    public ListView labelListView;
    public static int longClickPosition = 0;    //
    private HomeFragment fragment;
//    public RecyclerView labelListView2;

    //当App启动
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
        fragments = DataGenerator.getfragments("KamiNote");  //初始化列表
        initview();
        mDrawerLayout = findViewById(R.id.drawer_layout);   //滑动菜单

/*        fragment = new HomeFragment();
        fragment.setCallBack(new HomeFragment.CallBack() {
            @Override
            public void ShowDrawerlayout() {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });*/
        /*//设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        labelListView = findViewById(R.id.label_list2);

        //设置toolbar的左侧菜单为显示状态
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        mLabel = DataSupport.findAll(Label.class);

/*        //从数据库中读取存在的笔记
//        mNoteTemp = DataSupport.findAll(MyNote.class);
        mNote = DataSupport.findAll(MyNote.class);
        mLabel = DataSupport.findAll(Label.class);
        //判断是否读取到了数据
        if (mNote.size() != 0) {
            refreshNoteListView(noteListView);  //刷新
        } else {
            //如果读取到的内容为空，就将mNote设置为新的List，以备保存
            mNote = new ArrayList<>();
            //将"没有更多内容"从布局显示
            tv_noMore.setVisibility(View.VISIBLE);
        }
        if (mLabel.size() != 0) {
//            refreshLabelListView(labelListView2);   //刷新
            refreshLabelListView(labelListView);
        } else {
            mLabel = new ArrayList<>();
        }

        for (Label label : mLabel) {
            System.out.println(">>>>>" + label.getLabelName());
        }*/

        textAddLabel = findViewById(R.id.text_add_label);
        imageAddLabel = findViewById(R.id.image_add_label);

        imageAddLabel.setOnClickListener(this);
        textAddLabel.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLabelListView(labelListView);
    }

    //初始化view
    private void initview(){
        bottomNavigationView = findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onTabItemSelected(item.getItemId());
                return true;
            }
        });
        onTabItemSelected(R.id.tab_menu_home);
    }

    private void onTabItemSelected(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.tab_menu_home:
                fragment = fragments[0];
                break;
            case R.id.tab_menu_discovery:
                fragment = fragments[1];
                break;
            case R.id.tab_menu_attention:
                fragment = fragments[2];
                break;
            case R.id.tab_menu_profile:
                fragment = fragments[3];
                break;
        }
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
        }
    }

    public void refreshLabelListView(ListView listView) {
        if (listView != null) {
            LabelAdapter adapter = new LabelAdapter(this, R.layout.label_item, mLabel);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(adapter);
        }
    }

    //点击返回按钮的操作（"再按一次退出程序"）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                MyToast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                finish();
            }
            return true;
//            作者：Carson_Ho
//            链接：https://www.jianshu.com/p/3dab35223b79
//            來源：简书
//            著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
        }
        return super.onKeyDown(keyCode, event);
    }

    public static AlertDialog.Builder buildAlertDialog(Context context, String alertTitle,
                                                String alertMessage) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(alertTitle);
        dialog.setMessage(alertMessage);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请输入标签");    //设置对话框标题

        final EditText addLabel = new EditText(MainActivity.this);

        builder.setView(addLabel);
        builder.setCancelable(true);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String labelName = addLabel.getText().toString();
                if (labelName.length() > 10) {
                    MyToast.makeText(MainActivity.this, "标签过长", Toast.LENGTH_SHORT).show();
                } else {
                    Label label = new Label(labelName);
                    HomeFragment.mLabel.add(label);
                    mLabel.add(label);
                    label.save();
                    MyToast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }
                //TODO
//                        MainActivity.mLabel = mLabel;
                refreshLabelListView(MainActivity.mainActivity.labelListView);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    /*//当点击长按菜单时要做的东西
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 666:   //点击了长按后出现的删除键时
                final MyNote myNoteTemp = mNote.get(longClickPosition);
                mNote.get(longClickPosition).delete();
                mNote.remove(longClickPosition);
                refreshNoteListView(noteListView);
                Snackbar.make(mainView, "删除成功", Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mNote.add(myNoteTemp);
                                myNoteTemp.save();
                                refreshNoteListView(noteListView);
                            }
                        }).show();
                break;
        }
        return super.onContextItemSelected(item);
    }*/

}

