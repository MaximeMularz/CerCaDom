package lpa.aumont.cercadom;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * Got It Fragment
 */
public class GotItFragment extends Fragment implements View.OnClickListener {

    public final static String TAG = "CASH_FRAGMENT_TAG";
    private static final String LOG_TAG = GotItFragment.class.getSimpleName();

    private FragmentQuizInteractionListener mListener;

    private static final int QUESTION_VALUE = 5;
    private EditText mEditText;

    public GotItFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cash, container, false);

        // Set Button Value
        Button button = (Button) rootView.findViewById(R.id.buttonCash);
        button.setOnClickListener(this);

        mEditText = (EditText) rootView.findViewById(R.id.editText);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentQuizInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "On Start");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "On Resume");
        super.onResume();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mEditText.setText("");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (mListener == null)
            Log.d(LOG_TAG, "On ClickView Listner Null");
        mListener.responseFromFragment(mEditText.getText().toString(), QUESTION_VALUE);
    }

}
