package code.oswaldogh89.contactchooser.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import code.oswaldogh89.contactchooser.Objects.Contact;
import code.oswaldogh89.contactchooser.R;

public class AdptSelectContacts extends RecyclerView.Adapter<AdptSelectContacts.CustomViewHolder> implements Filterable {
    private ArrayList<Contact> list;
    private final ArrayList<Contact> originalList;
    Activity act;

    public AdptSelectContacts(Activity contactList_act, ArrayList<Contact> alContacts) {
        this.list = alContacts;
        this.originalList = alContacts;
        this.act = contactList_act;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_imagen, null);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        customViewHolder.txtName.setText(list.get(i).getName());
        customViewHolder.txtPhone.setText(list.get(i).getPhoneNumber());

        //para evitar el reciclaje de vistas
        customViewHolder.chkSelected.setChecked(list.get(i).isSelected());
        customViewHolder.chkSelected.setTag(list.get(i));
        customViewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Contact contact = (Contact) cb.getTag();

                contact.setSelected(cb.isChecked());
                list.get(i).setSelected(cb.isChecked());
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<Contact>) results.values;
                AdptSelectContacts.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Contact> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected ArrayList<Contact> getFilteredResults(String constraint) {
        ArrayList<Contact> results = new ArrayList<>();

        for (Contact item : originalList) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    public Contact getItem(int position) {
        return list.get(position);
    }


    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView txtName;
        final TextView txtPhone;

        final CheckBox chkSelected;

        public CustomViewHolder(View view) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.txtName);
            this.txtPhone = (TextView) view.findViewById(R.id.txtPhone);

            chkSelected = (CheckBox) view.findViewById(R.id.checkBox);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}