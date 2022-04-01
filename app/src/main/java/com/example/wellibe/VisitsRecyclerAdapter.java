package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.db;
import static com.example.wellibe.WelliBeActivity.getTimeDate;
import static com.example.wellibe.WelliBeActivity.mAuth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.like.LikeButton;
import com.like.OnLikeListener;


public class VisitsRecyclerAdapter extends FirestoreRecyclerAdapter<Visit, VisitsRecyclerAdapter.VisitCardHolder> {

    private Context mContext;

    public VisitsRecyclerAdapter(Context context, FirestoreRecyclerOptions<Visit> options) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull VisitCardHolder holder, int position, @NonNull Visit model) {
        holder.tvDoctorName.setText(model.getDoc_name());
        holder.tvDate.setText(getTimeDate(model.getTime_stamp()));
        holder.tvSummary.setText(model.getSummary());
        if (model.isLoved()) {
            holder.btnHeart.setLiked(true);
        } else {
            holder.btnHeart.setLiked(false);
        }
        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String visitID = snapshot.getId();
        holder.btnHeart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                db.collection("Users").document(mAuth.getUid()).collection("Visits")
                        .document(visitID).update("loved", true);
                if (model.getDoc_id() != "")
                    db.collection("users").document(model.getDoc_id()).update("Hearts received", FieldValue.increment(1));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                db.collection("Users").document(mAuth.getUid()).collection("Visits")
                        .document(visitID).update("loved", false);
                if (model.getDoc_id() != "")
                    db.collection("users").document(model.getDoc_id()).update("Hearts received", FieldValue.increment(-1));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle(model.getDoc_name())
                        .setMessage(model.getSummary())

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        }).setIcon(R.drawable.ic_baseline_info_24).show();
            }
        });
    }

    @NonNull
    @Override
    public VisitCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_row_layout_custom,parent,false);
        return new VisitCardHolder(view);
    }

    public static class VisitCardHolder extends RecyclerView.ViewHolder {

        TextView tvDoctorName;
        TextView tvDate;
        TextView tvSummary;
        LikeButton btnHeart;

        public VisitCardHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvDate = itemView.findViewById(R.id.tv_data_n_time);
            tvSummary = itemView.findViewById(R.id.tv_visit_summary);
            btnHeart = itemView.findViewById(R.id.btn_heart);
        }
    }

}


