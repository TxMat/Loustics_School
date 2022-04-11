package com.example.lousticsschool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lousticsschool.db.User;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    private List<User> mData;

    // data is passed into the constructor
    UserRecyclerViewAdapter(Context context, List<User> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.default_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mData.get(position);
        holder.myTextView.setText(user.getName());
        // set the text color to grey
        holder.myTextView.setTextColor(Color.GRAY);
        // set the text size to 16sp
        holder.myTextView.setTextSize(30);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(ArrayList<User> usersList) {
        mData = usersList;
        notifyDataSetChanged(); // can probably be optimized
    }


    // convenience method for getting data at click position
    User getItem(int id) {
        return mData.get(id);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvResult);


            // on long click, ask to delete the user
            itemView.setOnLongClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_Material3_Dark_Dialog_Alert));
                builder.setTitle("Supprimer l'utilisateur");
                builder.setMessage("Voulez-vous vraiment supprimer " + getItem(getAdapterPosition()).getName() + " ?\nCette action est irréversible.");
                builder.setPositiveButton("Oui, je veux supprimer", (dialog, which) -> {
                    // delete the user from the database
                    UtilsMethods.deleteUser(getItem(getAdapterPosition()), context);
                    mData.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                });
                builder.setNegativeButton("Non, je ne veux pas supprimer", (dialog, which) -> dialog.dismiss());
                builder.show();
                return true;
            });

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, LoggedActivity.class);
                Bundle bundle = new Bundle();
                // pass the user object to the next activity
                bundle.putSerializable("user", getItem(getAdapterPosition()));
                intent.putExtras(bundle);
                context.startActivity(intent);
            });


        }

    }
}