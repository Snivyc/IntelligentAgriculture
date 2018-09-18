package com.example.snivy.intelligentagriculture;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.snivy.intelligentagriculture.Adapter.AllCtrlNodeAdapter;
import com.example.snivy.intelligentagriculture.Adapter.InformationAdapter;
import com.example.snivy.intelligentagriculture.Node;
import com.example.snivy.intelligentagriculture.Adapter.oneNodeInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MapView mapView;

    private BaiduMap baiduMap;

    public LocationClient mLocationClient;

    public RecyclerView mRecyclerView, nodeRecycleView, allCtrlNodeRecycleView;

    private ArrayList<Marker> markersList;

    private BitmapDescriptor bitmap;

    private BitmapDescriptor bitmapI;

    public MyApp myApp;

    public NodeSet mNodeSet,oneNodeInfo;

    public int clickedNodeID = -1,tempID;

    public BottomSheetBehavior mBottomSheetBehavior;

    public View mBottomLayout;

    public boolean clickedByList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mLocationClient = new LocationClient(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        myApp = (MyApp) getApplication();

        mapView = (MapView) findViewById(R.id.bmapView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
        mapView = (MapView) findViewById(R.id.bmapView);
        mapView. showScaleControl(false);
        mapView. showZoomControls(false);
        baiduMap = mapView.getMap();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNodeSet.reflash();
            }
        });

        //初始化抽屉
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LatLng ll = new LatLng(32.349,119.406);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,17f);
        baiduMap.setMapStatus(update);

        //初始化RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL){});

        nodeRecycleView=(RecyclerView) findViewById(R.id.recycle_view2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        nodeRecycleView.setLayoutManager(layoutManager2);
        nodeRecycleView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL){});

        allCtrlNodeRecycleView = (RecyclerView) findViewById(R.id.recycle_view3);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        allCtrlNodeRecycleView.setLayoutManager(layoutManager3);
        allCtrlNodeRecycleView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL){});

        //初始化点集合
        markersList = new ArrayList<>();

        //初始化百度图标
        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        bitmapI = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marki);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clickedByList = false;
                node_clicked(marker.getExtraInfo().getInt("ID"));
                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (clickedNodeID != -1) {
                    undo_node_clicked();
                }
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                if (clickedNodeID != -1) {
                    undo_node_clicked();
                }
                return false;
            }
        });

        mBottomLayout = findViewById(R.id.bottom_sheet);
        //2.把这个底部菜单和一个BottomSheetBehavior关联起来
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomLayout);

        mNodeSet = new NodeSet(this,new NodeSet.RunAfter(){
            @Override
            public void run(MainActivity m) {
                m.markersList.clear();
                m.baiduMap.clear();
                List<Node> nodes = m.mNodeSet.getAllNodes();
                for (int i = 0; i < nodes.size(); i++) {
                    LatLng bPoint = new LatLng(nodes.get(i).getY(), nodes.get(i).getX());
                    OverlayOptions option = new MarkerOptions()
                            .position(bPoint)
                            .icon(m.bitmap);//在地图上添加Marker，并显示

                    Marker marker = (Marker) m.baiduMap.addOverlay(option);
                    m.markersList.add(marker);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", i);
                    marker.setExtraInfo(bundle);
                }
                InformationAdapter adapter = new InformationAdapter(m.mNodeSet.getAllNodes(), m.mLocationClient,m);
                mRecyclerView.setAdapter(adapter);
                AllCtrlNodeAdapter adapter2 = new AllCtrlNodeAdapter(m.mNodeSet.getAllCtrlNodes(), m.mLocationClient,m);
                allCtrlNodeRecycleView.setAdapter(adapter2);
            }
        });
        mNodeSet.reflash();
//        oneNodeInfo = new NodeSet(this);

        oneNodeInfo = new NodeSet(this, new NodeSet.RunAfter() {
            @Override
            public void run(MainActivity m) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                m.mBottomSheetBehavior.setHideable(true);
                oneNodeInfoAdapter adapter = new oneNodeInfoAdapter(m.oneNodeInfo.getAllNodes(), m.mLocationClient, m);
                nodeRecycleView.setAdapter(adapter);
                if (m.clickedNodeID == -1) {
                    TextView tTextView = (TextView) m.findViewById(R.id.show_list);
//                    tTextView.setText("上划显示该节点所有信息");
                    tTextView.setVisibility(View.GONE);
                    nodeRecycleView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    allCtrlNodeRecycleView.setVisibility(View.GONE);
//                    tTextView.setVisibility(View.GONE);

//                    tTextView.setVisibility(View.VISIBLE);
                } else {
                    Marker marker = m.markersList.get(m.clickedNodeID);
                    marker.setIcon(m.bitmap);
                }
//                m.mBottomSheetBehavior.setHideable(false);
                m.clickedNodeID = m.tempID;
                if (m.clickedByList) {
                    m.move_to_clicked_point();
                }
            }
        });
    }



    public void move_to_clicked_point() {
        Node Node = mNodeSet.getNode(clickedNodeID);
        LatLng ll = new LatLng(Node.getY(), Node.getX());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,17.2f);
        baiduMap.animateMapStatus(update);
    }

    public void node_clicked(int tempID) {
//        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        this.tempID = tempID;
        Marker marker = markersList.get(tempID);
        if (clickedNodeID == tempID) {
            LatLng position = marker.getPosition();
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(position);
            baiduMap.animateMapStatus(update);
        } else {
            marker.setIcon(bitmapI);
            float scale = getResources().getDisplayMetrics().density;
            mBottomSheetBehavior.setPeekHeight((int)(100 * scale + 0.5f));

            oneNodeInfo.reflash(tempID);

//            TextView textView = findViewById(R.id.point_info);
//            textView.setText(mNodeSet.getNode(tempID).getType());
//            textView = findViewById(R.id.point_distance);
//            textView.setText(mNodeSet.getNode(tempID).getTime());
//            if (clickedNodeID == -1) {
//                    Toast.makeText(MainActivity.this,  "被点击了！", Toast.LENGTH_SHORT).show();

//                View tView = findViewById(R.id.point_info_layout);
//                tView.setVisibility(View.VISIBLE);
//                View tView = findViewById(R.id.recycle_view);
//                tView.setVisibility(View.GONE);
//                tView = findViewById(R.id.recycle_view2);
//                tView.setVisibility(View.VISIBLE);
//
//                TextView tTextView = findViewById(R.id.show_list);
//                tTextView.setText("上划显示该节点所有信息");

//                float scale = MainActivity.this.getResources().getDisplayMetrics().density;
//                mBottomSheetBehavior.setPeekHeight((int)(100 * scale + 0.5f));
//            } else {
//                marker = markersList.get(clickedNodeID);
//                marker.setIcon(bitmap);
//            }
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            clickedNodeID = tempID;
        }
    }

    public void undo_node_clicked(){
        if (clickedNodeID != -1) {
            Marker marker = markersList.get(clickedNodeID);
            marker.setIcon(bitmap);
            clickedNodeID = -1;
            float scale = MainActivity.this.getResources().getDisplayMetrics().density;
            mBottomSheetBehavior.setPeekHeight((int) (50 * scale + 0.5f));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            TextView tTextView = findViewById(R.id.show_list);
            tTextView.setText("上划显示节点列表");
            tTextView.setVisibility(View.VISIBLE);
            nodeRecycleView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            allCtrlNodeRecycleView.setVisibility(View.VISIBLE);

//            InformationAdapter adapter = new InformationAdapter(new ArrayList<Node>(), mLocationClient,this);
//            nodeRecycleView.setAdapter(adapter);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    NodeSet.RunAfter fuck =

    public void run(MainActivity m) {
        markersList.clear();
        baiduMap.clear();
        List<Node> nodes = mNodeSet.getAllNodes();
        for (int i = 0; i < nodes.size(); i++) {
            LatLng bPoint = new LatLng(nodes.get(i).getY(), nodes.get(i).getX());
            OverlayOptions option = new MarkerOptions()
                    .position(bPoint)
                    .icon(bitmap);//在地图上添加Marker，并显示

            Marker marker = (Marker) baiduMap.addOverlay(option);
            markersList.add(marker);
            Bundle bundle = new Bundle();
            bundle.putInt("ID", i);
            marker.setExtraInfo(bundle);
        }

        InformationAdapter adapter = new InformationAdapter(mNodeSet.getAllNodes(), mLocationClient,this);
        mRecyclerView.setAdapter(adapter);
    }
}
