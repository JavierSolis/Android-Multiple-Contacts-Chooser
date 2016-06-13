package code.oswaldogh89.contactchooser.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import code.oswaldogh89.contactchooser.Objects.Contact;
import code.oswaldogh89.contactchooser.R;

public class AdptSelectedContacts extends RecyclerView.Adapter<AdptSelectedContacts.CustomViewHolder> {
    private final ArrayList<Contact> obj;
    Activity act;

    public AdptSelectedContacts(Activity contactList_act, ArrayList<Contact> alContacts) {
        this.obj = alContacts;
        this.act = contactList_act;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_checkbox, null);

        return new CustomViewHolder(view);
    }

    public Contact getItem(int position) {
        return obj.get(position);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        customViewHolder.txtName.setText(obj.get(i).getName());
        customViewHolder.txtPhone.setText(obj.get(i).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return (null != obj ? obj.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView txtName;
        final TextView txtPhone;

        public CustomViewHolder(View view) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.txtName);
            this.txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        }

    }
}