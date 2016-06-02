package android.baodian.com.biaodiangithub;

import android.baodian.com.biaodiangithub.tab1.Fragment1;
import android.baodian.com.biaodiangithub.tab2.Fragment2;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomBar mBottomBar;
    private List<Fragment> mFragmentList;
    private ViewPager mViewPager;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent(savedInstanceState);

    }

    private void initComponent(Bundle savedInstanceState) {
        initViewPager();
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu,
                new OnMenuTabClickListener() {
                    //tab被选择时触发
                    @Override
                    public void onMenuTabSelected(int menuItemId) {

                        //设置viewpager随bottombar而变化
                        switch (menuItemId) {
                            case R.id.tab0:
                                mViewPager.setCurrentItem(0);
                                break;
                            case R.id.tab1:
                                mViewPager.setCurrentItem(1);
                                //一旦进入这个页面则隐藏未读消息提示
//                                unreadMessage.hide();
//                                unreadMessage.setAutoShowAfterUnSelection(false);
                                break;
                            case R.id.tab2:
                                mViewPager.setCurrentItem(2);
                                break;
                            case R.id.tab3:
                                mViewPager.setCurrentItem(3);
                                break;
                        }
                    }

                    //tab被重复选择时触发
                    @Override
                    public void onMenuTabReSelected(int menuItemId) {

                    }
                });
        // 给不同的tab设置颜色
        mBottomBar.mapColorForTab(0, "#d9534f");
        mBottomBar.mapColorForTab(1, "#468CC9");
        mBottomBar.mapColorForTab(2, "#609E79");
        mBottomBar.mapColorForTab(3, "#9B9B9B");
    }

    private void initListen() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    //初始化ViewPager
    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new Fragment1());
        mFragmentList.add(new Fragment2());
        mFragmentList.add(new Fragment1());
        mFragmentList.add(new Fragment1());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置滑动viewpager，底部bottombar也随着改变
                mBottomBar.selectTabAtPosition(position, true);

                //设置viewpager随bottombar而变化
                switch (position) {
                    case R.id.tab0:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.tab1:
                        mViewPager.setCurrentItem(1);
                        //一旦进入这个页面则隐藏未读消息提示
//                        unreadMessage.hide();
//                        unreadMessage.setAutoShowAfterUnSelection(false);
                        break;
                    case R.id.tab2:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.tab3:
                        mViewPager.setCurrentItem(3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
    }
}