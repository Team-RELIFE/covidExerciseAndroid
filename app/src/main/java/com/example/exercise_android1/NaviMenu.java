package com.example.exercise_android1;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.exercise_android1.Calendar.CalendarActivity;
import com.example.exercise_android1.board.GetPostsActivity;
import com.example.exercise_android1.graph.graph;
import com.example.exercise_android1.reservation.GetItemActivity;
import com.example.exercise_android1.reservation.ReservationListActivity;
import com.nhn.android.naverlogin.OAuthLogin;

/**내비게이션 메뉴 선택 클래스
 *
 */
public class NaviMenu extends ContextWrapper{

    Context context;
    MenuItem item;
    OAuthLogin mOAuthLogin;
    User currentUser;
    int id;

    public NaviMenu(Context base) {
        super(base);
    }

    public NaviMenu(Context context, MenuItem item, User currentUser){
        super(context);
        this.context = context;
        this.item = item;
        this.currentUser = currentUser;
    }

    public void selectMenu(){
        id = item.getItemId();
        if (id == R.id.main_logout){
            mOAuthLogin = OAuthLogin.getInstance();
            mOAuthLogin.logout(context);
            currentUser.setCurrentUser(null, "", "", 0, 0, 0);
            Toast.makeText(context,"로그아웃",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(context,MainActivity.class);
            startActivity(intent);
        }
        if(id==R.id.menu_calendar){
            Intent intent=new Intent(context, CalendarActivity.class);
            startActivity(intent);
        }

        if (id==R.id.menu_myInfo){
            Intent intent = new Intent(context, PointActivity.class);
            startActivity(intent);
        }
        if (id==R.id.menu_board){
            Intent intent = new Intent(context, GetPostsActivity.class);
            startActivity(intent);
        }
        if (id==R.id.menu_reservation) {
            Intent intent = new Intent(context, GetItemActivity.class);
            startActivity(intent);
        }
        if (id==R.id.menu_bmi_record) {
            Intent intent = new Intent(context, graph.class);
            startActivity(intent);
        }
    }
}
