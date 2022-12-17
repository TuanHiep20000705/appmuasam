package com.mth.example.banhangapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.mth.example.banhangapp.R;
import com.mth.example.banhangapp.fragment.FragmentCart;
import com.mth.example.banhangapp.fragment.FragmentHome;
import com.mth.example.banhangapp.fragment.FragmentNotification;
import com.mth.example.banhangapp.model.GioHang;
import com.mth.example.banhangapp.model.LoaiSanPham;
import com.mth.example.banhangapp.model.SanPham;
import com.mth.example.banhangapp.model.User;
import com.mth.example.banhangapp.utils.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MoveListSp, MoveToInfoSp, NavigationView.OnNavigationItemSelectedListener, MoveSPToMain {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private ImageView imgAvatar;
    private TextView txtTenUser, txtDiaChiUser;
    private TextView edtSearch;

    private final int fragmentHome = 0;
    private final int fragmentCart = 1;
    private final int fragmentNotification = 2;
    private int currentFrament = fragmentHome;
    private boolean statusLogin;
    private int idUser;
    private Disposable disposable, disposable1, disposable2, disposable3, disposable4;
    private User user;
    private List<GioHang> listGioHangServer;
    private String response = "", response2 = "";
    private ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        setSharedPreferences();
        setActionToolBar();
        setDefaultFragment();
    }


    private void setActionToolBar() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchSpActivity.class));
            }
        });
    }

    private void setDefaultFragment() {
        Intent intent = getIntent();
        User userFromChiTietSp = (User) intent.getSerializableExtra("dulieufromsanphaminfo");

        replaceFragment(new FragmentHome());
        currentFrament = fragmentHome;
        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);

        if (userFromChiTietSp != null) {
            FragmentCart fragmentCart1 = new FragmentCart();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("dulieu", userFromChiTietSp);
            fragmentCart1.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_frame, fragmentCart1);
            fragmentTransaction.commit();
            currentFrament = fragmentCart;
            bottomNavigationView.getMenu().findItem(R.id.action_cart).setChecked(true);
        }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    openFragmentHome();
                } else if (id == R.id.action_cart) {
                    openFragmentCart();
                } else if (id == R.id.action_notification) {
                    openFragmentNotification();
                }
                return true;
            }
        });
    }

    private void anhXa() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.tool_bar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        txtTenUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_txt_ten);
        txtDiaChiUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_txt_diachi);
        imgAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_img_avatar);
        listGioHangServer = new ArrayList<>();
        edtSearch = findViewById(R.id.edt_search);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.txt_dangkiemtra));
    }

    private void openFragmentHome() {
        if (currentFrament != fragmentHome) {
            replaceFragment(new FragmentHome());
            currentFrament = fragmentHome;
        }
    }

    private void openFragmentCart() {
        if (currentFrament != fragmentCart) {
            replaceFragment(new FragmentCart());
            currentFrament = fragmentCart;
        }
    }

    private void openFragmentNotification() {
        if (currentFrament != fragmentNotification) {
            replaceFragment(new FragmentNotification());
            currentFrament = fragmentNotification;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dulieu", user);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void giaTriSp(SanPham sanPham) {
        Intent intent = new Intent(MainActivity.this, ChiTietSanPhamActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dulieuuser", user);
        bundle.putSerializable("dulieusanpham", sanPham);
        intent.putExtra("dulieu", bundle);
        startActivity(intent);
    }

    @Override
    public void giaTriLoai(LoaiSanPham loaiSanPham) {
        Intent intent = new Intent(MainActivity.this, ListSpActivity.class);
        intent.putExtra("dulieu", loaiSanPham);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (statusLogin == false) {
            if (id == R.id.nav_my_profile) {
                Toast.makeText(this, getResources().getString(R.string.txt_messagedangnhap), Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_cart) {
                Toast.makeText(this, getResources().getString(R.string.txt_messagedangnhap), Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_history) {
                Toast.makeText(this, getResources().getString(R.string.txt_messagedangnhap), Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_log_in) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            if (id == R.id.nav_log_in) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainActivity.this, NullActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_my_profile) {
                Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
                intent.putExtra("dulieu", idUser);
                startActivity(intent);
            } else if (id == R.id.nav_cart) {
                if (currentFrament != fragmentCart) {
                    openFragmentCart();
                    bottomNavigationView.getMenu().findItem(R.id.action_cart).setChecked(true);
                }
            } else if (id == R.id.nav_history) {
                Intent intent = new Intent(MainActivity.this, LichSuActivity.class);
                intent.putExtra("dulieu", user);
                startActivity(intent);
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setSharedPreferences() {
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("dulieu");

        if (bundle != null) {
            User userFromLogin = (User) bundle.getSerializable("dulieuuser");
            statusLogin = bundle.getBoolean("tinhtrangdangnhap");
            if (statusLogin) {
                int id = userFromLogin.getId();
                editor.putBoolean("tinhtrang", statusLogin);
                editor.putInt("thongtinid", id);
                editor.commit();
            } else {
                editor.clear();
                editor.commit();
            }
        }

        statusLogin = sharedPreferences.getBoolean("tinhtrang", false);
        idUser = sharedPreferences.getInt("thongtinid", 1);
        if (statusLogin == true) {
            getUser(idUser);
            navigationView.getMenu().findItem(R.id.nav_log_in).setIcon(R.drawable.ic_logout);
            navigationView.getMenu().findItem(R.id.nav_log_in).setTitle(getResources().getString(R.string.nav_logout));
        } else {
            navigationView.getMenu().findItem(R.id.nav_log_in).setIcon(R.drawable.ic_login);
            navigationView.getMenu().findItem(R.id.nav_log_in).setTitle(getResources().getString(R.string.nav_login));
        }
    }

    private void getUser(int id) {
        ApiUtils.getData().getUserById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<User> users) {
                        user = users.get(0);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        String tenUser = user.getTenuser();
                        String diaChiUser = user.getDiachi();
                        String anhUser = user.getAnh();
                        txtTenUser.setText(tenUser);
                        txtDiaChiUser.setText(diaChiUser);
                        if (anhUser.length() > 0) {
                            Glide.with(MainActivity.this).load(anhUser).into(imgAvatar);
                        } else return;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
        if (disposable1 != null) {
            disposable1.dispose();
        }
        if (disposable2 != null) {
            disposable2.dispose();
        }
        if (disposable3 != null) {
            disposable3.dispose();
        }
        if (disposable4 != null) {
            disposable4.dispose();
        }
    }

    @Override
    public void giaTriSpFromCart(SanPham sanPham) {
        if (statusLogin == false) {
            Toast.makeText(this, getResources().getString(R.string.txt_messagedangnhap), Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            progressDialog.show();
            ApiUtils.getData().getDataCart(user.getUsername())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<GioHang>>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            disposable1 = d;
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<GioHang> gioHangs) {
                            listGioHangServer = gioHangs;
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            int x = 0;
                            for (GioHang gioHang :
                                    listGioHangServer) {
                                if (gioHang.getIdsp() == sanPham.getId()) {
                                    if (gioHang.getSoluong() == 10) {
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, getResources().getString(R.string.txt_messagesoluongmax), Toast.LENGTH_SHORT).show();
                                    } else {
                                        int soLuong = gioHang.getSoluong();
                                        soLuong += 1;
                                        int tongGia = gioHang.getTonggia();
                                        tongGia += sanPham.getGiasp();
                                        updateSoLuong(user.getUsername(), sanPham.getId(), soLuong, tongGia, sanPham.getTensp());
                                    }
                                } else x++;
                            }
                            if (x == listGioHangServer.size()) {
                                createCartByIdSp(user.getUsername(), sanPham.getId(), sanPham.getGiasp(), sanPham.getTensp(), sanPham.getAnh(), sanPham.getGiasp());
                            }
                        }
                    });
        }
    }

    private void createCartByIdSp(String username, int idsp, int tongGia, String tenSp, String anhSp, int donGia) {
        ApiUtils.getData().createCartById(username, idsp, tongGia, tenSp, anhSp, donGia)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable3 = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                        response2 = s;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        if (response2.trim().equals("success")) {
                            createThongBao(username, tenSp);
                        }
                    }
                });
    }

    private void updateSoLuong(String username, int idSp, int soLuong, int tongGia, String tenSp) {
        ApiUtils.getData().updateSoLuong(idSp, username, soLuong, tongGia)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable2 = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                        response = s;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        if (response.trim().equals("success")) {
                            createThongBao(username, tenSp);
                        }
                    }
                });
    }

    private void createThongBao(String username, String tenSp) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy   HH:mm");
        Date t = cal.getTime();
        String noiDung = tenSp + " " + getResources().getString(R.string.txt_messagethemsp);
        ApiUtils.getData().createThongBao(username, sdf.format(t), noiDung, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable4 = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.txt_ketnoimang), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.txt_messagethemsp1), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}