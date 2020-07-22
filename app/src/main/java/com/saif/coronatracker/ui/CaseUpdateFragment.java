package com.saif.coronatracker.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.saif.coronatracker.databinding.FragmentCaseUpdateBinding;
import com.saif.coronatracker.models.GlobalStats;

import org.jetbrains.annotations.NotNull;

public class CaseUpdateFragment extends BottomSheetDialogFragment {

    private BottomSheetDialog bottomSheetDialog;
    private FragmentCaseUpdateBinding binding;
    private Gson gS;
    private GlobalStats response;
    public static final String TAG = "CaseUpdateFragment";

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            bottomSheetDialog = (BottomSheetDialog) getDialog();
            assert bottomSheetDialog != null;
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            assert bottomSheet != null;
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setHideable(false);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        gS = new Gson();
        if (getArguments() != null) {
            String caseData = getArguments().getString("CASE_DATA");
            response = gS.fromJson(caseData, GlobalStats.class);
        } else {
            Toast.makeText(getContext(), "Its null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCaseUpdateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.txtTotalCases.setText(response.getTodayCases());
        binding.txtActiveCases.setText(response.getActive());
        binding.txtTodayCases.setText(response.getTodayCases());
        binding.txtCriticalCases.setText(response.getCritical());
        binding.txtDeathToday.setText(response.getTodayDeaths());
        binding.txtRecoveredToday.setText(response.getTodayRecovered());
        binding.txtTotalCases.setText(response.getCases());
        binding.txtRecovered.setText(response.getRecovered());
        binding.txtDeath.setText(response.getDeaths());

        binding.btnClose.setOnClickListener(view1 -> bottomSheetDialog.dismiss());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
