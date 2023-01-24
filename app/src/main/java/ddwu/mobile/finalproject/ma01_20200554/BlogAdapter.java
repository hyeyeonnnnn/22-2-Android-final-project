package ddwu.mobile.finalproject.ma01_20200554;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    ArrayList<BlogDTO> array;
    Context context;

    //생성을 할때 array값과 context값을 받아서 생성을 해준다.
    public BlogAdapter(ArrayList<BlogDTO> array, Context context) {
        this.array = array;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //view 생성
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.blog,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String link=array.get(position).getLink();

        //Html.fromHtml:태그를 제거하고 html으로 뽐아내는것
        holder.title.setText(Html.fromHtml("<u>" +array.get(position).getTitle()+"</u>"));
        holder.desc.setText(Html.fromHtml(array.get(position).getDescription()));

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blog = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                context.startActivity(blog);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.blogTitle);
            desc=itemView.findViewById(R.id.blogDesc);
        }
    }

}
