package com.example.adeeliftikhar.bottomnavigation;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

public class DashBoardFragment extends Fragment {

    Button buttonOpenBottomSheet, buttonBottomSheetDialog, buttonBottomSheetDialogFragment;
    LinearLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;
    View bottomSheetDialogView;

    public static DashBoardFragment newInstance() {
        return new DashBoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        bottomSheetDialogView = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
        buttonOpenBottomSheet = view.findViewById(R.id.button_open_bottom_sheet);
        buttonOpenBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });

        buttonBottomSheetDialog = view.findViewById(R.id.button_bottom_sheet_dialog);
        buttonBottomSheetDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        buttonBottomSheetDialogFragment = view.findViewById(R.id.button_bottom_sheet_dialog_fragment);
        buttonBottomSheetDialogFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialogFragment();
            }
        });

        bottomSheetBehavior();

        return view;
    }

    private void bottomSheetBehavior() {
        ButterKnife.bind(getActivity());
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        buttonOpenBottomSheet.setText("Close Bottom Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        buttonOpenBottomSheet.setText("Open Bottom Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    //    @OnClick(R.id.button_open_bottom_sheet)
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void showBottomSheetDialog() {
        View view = bottomSheetDialogView;
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        TextView textViewShare = view.findViewById(R.id.text_view_share);
        textViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Share Clicked", Toast.LENGTH_SHORT).show();
            }
        });
//        First make view empty to set view again...
        if(view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        dialog.setContentView(view);
        dialog.show();
    }

    public void showBottomSheetDialogFragment() {
        BottomSheetFragment newFragment = new BottomSheetFragment();
//        newFragment.show(getFragmentManager(), "Dialog Fragment");
    }
}

