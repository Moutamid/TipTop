package com.moutamid.tiptop.tiper_side.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.TipInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserVH> {

    Context context;
    ArrayList<UserModel> list;
    TipInterface onClick;

    public UsersAdapter(Context context, ArrayList<UserModel> list, TipInterface onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserVH(LayoutInflater.from(context).inflate(R.layout.receivers_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserVH holder, int position) {
        UserModel model = list.get(holder.getAdapterPosition());

        holder.name.setText(model.getName());
        holder.username.setText("@" + model.getUsername());
        holder.company.setText(model.getCompany());
        holder.jobTitle.setText(model.getJobTitle());

        Glide.with(context).load(model.getImage()).placeholder(R.drawable.profile_icon).into(holder.profile);

        holder.send.setOnClickListener(v -> {
            onClick.onClick(list.get(holder.getAdapterPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserVH extends RecyclerView.ViewHolder{
        TextView name, username, company, jobTitle;
        MaterialButton send;
        CircleImageView profile;
        public UserVH(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);
            company = itemView.findViewById(R.id.company);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            send = itemView.findViewById(R.id.send);
            profile = itemView.findViewById(R.id.profile);

        }
    }

}
