package com.moutamid.tiptop.receiver_side.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.tiptop.R;
import com.moutamid.tiptop.models.TransactionModel;
import com.moutamid.tiptop.utilis.Constants;

import java.util.ArrayList;
import java.util.Locale;

public class TransactionReceiverAdapter extends RecyclerView.Adapter<TransactionReceiverAdapter.TransactionVH> {

    Context context;
    ArrayList<TransactionModel> list;

    public TransactionReceiverAdapter(Context context, ArrayList<TransactionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TransactionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionVH(LayoutInflater.from(context).inflate(R.layout.transaction_history_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionVH holder, int position) {
        TransactionModel model = list.get(holder.getAdapterPosition());

        String day = new SimpleDateFormat("dd", Locale.getDefault()).format(model.getTimestamp());
        String month = new SimpleDateFormat("MMM", Locale.getDefault()).format(model.getTimestamp());

        holder.day.setText(day);
        holder.month.setText(month);



        holder.price.setText(Constants.EURO_SYMBOL + model.getPrice());

        if (model.getType().equals(Constants.REQ)){
            holder.icon.setImageResource(R.drawable.round_arrow_downward_24);
            holder.icon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.yellow)));
            holder.iconCard.setCardBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.yellow_light)));

            holder.name.setText("Requested Money");
            holder.desc.setText(model.getSenderName());

        } else if (model.getType().equals(Constants.PAY)){
            holder.icon.setImageResource(R.drawable.round_arrow_downward_24);
            holder.icon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
            holder.iconCard.setCardBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.green_light)));

            holder.name.setText(model.getReceiverName());
            holder.desc.setText(model.getDescription());

        } else if (model.getType().equals(Constants.WITHDRAW)){
            holder.icon.setImageResource(R.drawable.round_arrow_outward_24);
            holder.icon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
            holder.iconCard.setCardBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.yellow_light)));

            holder.name.setText("Withdraw Money");
            holder.desc.setText(model.getDescription());

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TransactionVH extends RecyclerView.ViewHolder{
        TextView day, month, name, desc, price;
        ImageView icon;
        CardView iconCard;
        public TransactionVH(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            month = itemView.findViewById(R.id.month);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
            icon = itemView.findViewById(R.id.icon);
            iconCard = itemView.findViewById(R.id.iconCard);
        }
    }

}
