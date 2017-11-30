package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by eric.
 */

public class Account extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account, container, false);
        this.listView = view.findViewById(R.id.account_view);
        ArrayList<String> list = new ArrayList<>();
        list.add("Account Details");
        list.add("Payment Info");
        list.add("History");
        list.add("Feedback");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,list);

        listView.setAdapter(adapter);

        // TODO: Choose between Fragment or Activity
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

//                Fragment fragment = new History();
//
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction transaction = fm.beginTransaction();
//                transaction.replace(R.id.btn_hist, fragment);
//                transaction.commit();

                Intent intent = new Intent(getActivity(), History.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
