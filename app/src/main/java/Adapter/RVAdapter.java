package Adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfound.MyAccountActivity;
import com.example.lostandfound.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;

import Model.RVListItem;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private Context context;
    private List<RVListItem> listItems;
    public RVListItem rvListItem;

    public RVAdapter(Context context, List listitem){
        this.context=context;
        this.listItems=listitem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_row,parent,false);
        return new ViewHolder(view,context);//new added mListener
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.ViewHolder holder, final int position) {
        RVListItem item=listItems.get(position);

        holder.rvItemName.setText(item.getType());
        holder.rvCompanyName.setText(item.getCompanyName());
        holder.rvEmail.setText(item.getEmail());

        if(item.getType().charAt(0)=='L'){
            holder.rvItemName.setTextColor(Color.RED);
        }
        else{
            holder.rvItemName.setTextColor(Color.parseColor("#006400"));
        }

        rvListItem=listItems.get(position);
        holder.rvImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert= new AlertDialog.Builder(context);
                alert.setTitle("Delete Query");
                alert.setMessage("Are you sure you want to Delete this query ?");
                Log.d("After Alert","after alert setmessage");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(rvListItem.getType().charAt(0)=='L'){
                            removeFromDatabase("LostObjectActivity",position);
                        }

                        else if(rvListItem.getType().charAt(0)=='F'){
                            removeFromDatabase("FoundObjectActivity",position);
                        }

                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void removeItem(int position){
        listItems.remove(position);
        notifyItemRemoved(position);
        notifyAll();
    }

    /*public void restoreItem(RVListItem item,int position){
        listItems.add(position,item);
        notifyItemInserted(position);
    }

    public void clear() {
        int size = listItems.size();
        listItems.clear();
        notifyItemRangeRemoved(0, size);
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView rvItemName;
        public TextView rvCompanyName;
        public TextView rvEmail;
        public ImageView rvImageDelete;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context=ctx;

            rvItemName=itemView.findViewById(R.id.rvItemName);
            rvCompanyName=itemView.findViewById(R.id.rvCompanyName);
            rvEmail=itemView.findViewById(R.id.rvEmail);
            rvImageDelete=itemView.findViewById(R.id.image_delete);

        }

    }


    public void removeFromDatabase(String dbName,int position){
        FirebaseDatabase.getInstance().getReference()
                .child(dbName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                String childCheck=snapshot.child("check").getValue().toString();
                                if((rvListItem.getCheckInMain()).equals(childCheck)){
                                    snapshot.getRef().removeValue();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("Lost_Found")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                String detail=snapshot.child("detail").getValue().toString();
                                if((rvListItem.getCheckInMain()).equals(detail)){
                                    snapshot.getRef().removeValue();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        //removeItem(position);
    }
}
