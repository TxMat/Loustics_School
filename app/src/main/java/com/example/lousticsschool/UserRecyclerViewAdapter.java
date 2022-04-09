package com.example.lousticsschool;

import static com.example.lousticsschool.UtilsMethods.calculateFromString;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lousticsschool.db.User;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private List<User> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

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
        notifyDataSetChanged();
    }




    // convenience method for getting data at click position
    User getItem(int id) {
        return mData.get(id);
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvResult);


            // on long click, ask to delete the user
            itemView.setOnLongClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Supprimer l'utilisateur");
                builder.setMessage("Voulez-vous vraiment supprimer " + getItem(getAdapterPosition()).getName() + " ?\nCette action est irréversible.");
                builder.setPositiveButton("Oui, je veux supprimer", (dialog, which) -> {
                    // delete the user from the database
                    UtilsMethods.deleteUser(getItem(getAdapterPosition()), context);
                    mData.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                });
                builder.setNegativeButton("Non, je ne veux pas supprimer", (dialog, which) -> {
                    dialog.dismiss();
                });
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