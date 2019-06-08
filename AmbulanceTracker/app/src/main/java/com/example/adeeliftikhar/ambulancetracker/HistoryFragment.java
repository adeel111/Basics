package com.example.adeeliftikhar.ambulancetracker;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeeliftikhar.ambulancetracker.Models.HelperHistoryModel;
import com.example.adeeliftikhar.ambulancetracker.SessionsManager.LoginSessionManager;
import com.example.adeeliftikhar.ambulancetracker.ViewHolders.HistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    RecyclerView recyclerViewHistory;
    TextView textViewNoHistory;

    FirebaseAuth mAuth;
    String currentUser;
    private DatabaseReference dbRef;

    SpinKitView spinKitView;
    String a;
    View view;

    LoginSessionManager loginSessionManager;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);

        loginSessionManager = new LoginSessionManager(getContext());

        textViewNoHistory = view.findViewById(R.id.text_view_no_history);
        textViewNoHistory.setVisibility(View.GONE);
        spinKitView = view.findViewById(R.id.spin_kit_view_history);
        spinKitView.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        currentUser = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("HelperHistory").child(currentUser);
        dbRef.keepSynced(true);

        recyclerViewHistory = view.findViewById(R.id.recycler_view_history);
        recyclerViewHistory.setHasFixedSize(true);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));

//        DividerItemDecoration divider = new
//                DividerItemDecoration(HistoryActivity.this, DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.line_divider));
//        recyclerViewHistory.addItemDecoration(divider);

        int[] ATTRS = new int[]{android.R.attr.listDivider};

        TypedArray a = getActivity().obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        recyclerViewHistory.addItemDecoration(itemDecoration);

        loadDataFromDB();


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadDataFromDB() {
        FirebaseRecyclerAdapter<HelperHistoryModel, HistoryViewHolder> adapter = new
                FirebaseRecyclerAdapter<HelperHistoryModel, HistoryViewHolder>
                        (HelperHistoryModel.class,
                                R.layout.recycler_view_history_design,
                                HistoryViewHolder.class,
                                dbRef) {

                    @Override
                    protected void populateViewHolder(HistoryViewHolder viewHolder, HelperHistoryModel model, int position) {
                        spinKitView.setVisibility(View.GONE);
                        textViewNoHistory.setVisibility(View.GONE);
                        loginSessionManager.savePermanentValue("1");

                        viewHolder.setTextViewAmbulanceNumber(model.getAmbulance());
                        viewHolder.setDateTime(model.getDate_time());

                        final String itemKey = getRef(position).getKey();
                        ImageView navArrow = viewHolder.itemView.findViewById(R.id.nav_arrow);
                        navArrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), HelpDetailActivity.class);
                                intent.putExtra("itemKey", itemKey);
                                startActivity(intent);
                            }
                        });
                    }
                };
        a = loginSessionManager.getPermanentValue();
        recyclerViewHistory.setAdapter(adapter);
        if (a.isEmpty()) {
            spinKitView.setVisibility(View.GONE);
            textViewNoHistory.setVisibility(View.VISIBLE);
        }
    }
}
