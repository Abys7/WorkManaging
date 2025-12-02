package com.example.workmanaging.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workmanaging.R;
import com.example.workmanaging.model.entity.Cliente;
import java.util.ArrayList;
import java.util.List;

public class    ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<Cliente> clients = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Cliente cliente);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setClients(List<Cliente> clients) {
        this.clients = clients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Cliente currentClient = clients.get(position);
        holder.tvName.setText(currentClient.nome);
        holder.tvCompany.setText(currentClient.azienda != null ? currentClient.azienda : "N/A");
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentClient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    static class ClientViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvCompany;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_client_name);
            tvCompany = itemView.findViewById(R.id.tv_company_name);
        }
    }
}
