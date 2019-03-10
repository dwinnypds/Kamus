package dwinny.kamus.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dwinny.kamus.DB.Kata;
import dwinny.kamus.Detail;
import dwinny.kamus.R;

public class Kamus_Adapter extends RecyclerView.Adapter<Kamus_Adapter.Holder> {
    private ArrayList<Kata> arrayList =new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public Kamus_Adapter(Context c) {
        this.context =c;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }




    public void addItem(ArrayList<Kata> items) {
        arrayList= items;
        notifyDataSetChanged();
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new Holder(view);
    }



    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.item_Kata.setText(arrayList.get(position).getKata());
        holder.item_Arti.setText(arrayList.get(position).getArti());
        holder.kontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Detail.class);
                i.putExtra("kata", arrayList.get(position).getKata());
                i.putExtra("arti", arrayList.get(position).getArti());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("COUNT", arrayList.size()+"");
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView item_Kata, item_Arti;
        LinearLayout kontainer;
        public Holder(View itemView) {
            super(itemView);
            item_Arti = itemView.findViewById(R.id.item_arti);
            item_Kata = itemView.findViewById(R.id.item_kata);
            kontainer = itemView.findViewById(R.id.kontainer);

        }
    }
}
