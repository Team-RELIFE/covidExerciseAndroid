package trainerboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView MainRecyclerView;
    private MainAdapter Adapter;
    private List<Board> BoardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        MainRecyclerView = findViewById(R.id.board);

        findViewById(R.id.board_write).setOnClickListener(this);

        BoardList = new ArrayList<>();
        BoardList.add(new Board(null, "반갑습니다.", null, "강사1"));
        BoardList.add(new Board(null, "PT강사입니다.", null, "강사2"));
        BoardList.add(new Board(null, "안녕하세여.", null, "강사3"));
        BoardList.add(new Board(null, "코로나운동앱.", null, "강사4"));
        BoardList.add(new Board(null, "확찐자운동앱.", null, "강사5"));

        Adapter = new MainAdapter(BoardList);
        MainRecyclerView.setAdapter(Adapter);
    }

    @Override
    public void onClick(View v){

    }

    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

        private List<Board> BoardList;

        public MainAdapter(List<Board> BoardList){
            this.BoardList = BoardList;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.board_main, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            Board data = BoardList.get(position);
            holder.TitleTextView.setText(data.getTitle());
            holder.user_name.setText(data.getName());
        }

        @Override
        public int getItemCount() {
            return BoardList.size();
        }

        class MainViewHolder extends RecyclerView.ViewHolder{

            private TextView TitleTextView;
            private TextView user_name;

            public MainViewHolder(View itemView){
                super(itemView);

                TitleTextView = itemView.findViewById(R.id.item_title);
                user_name = itemView.findViewById(R.id.user_name);
            }
        }
    }
}