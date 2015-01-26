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


/**
 * SquareFragment
 */
public class SquareFragment extends Fragment implements View.OnClickListener {

    public final static String TAG = "SQUARE_FRAGLMENT_TAG";

    private static final String RESPONSES_KEY = "RESPONSES_KEY";

    private static final int QUESTION_VALUE = 3;

    private final String LOG_TAG = SquareFragment.class.getSimpleName();

    private FragmentQuizInteractionListener mListener;
    private String[] responses;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;

    public SquareFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getStringArray(RESPONSES_KEY) != null) {
            responses = getArguments().getStringArray(RESPONSES_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {

        View rootView = inflater.inflate(R.layout.fragment_carre, container, false);

        // Find Button by CONTENT_PROVIDER_ID
        mButton1 = (Button) rootView.findViewById(R.id.Button1Carre);
        mButton2 = (Button) rootView.findViewById(R.id.Button2Carre);
        mButton3 = (Button) rootView.findViewById(R.id.Button3Carre);
        mButton4 = (Button) rootView.findViewById(R.id.Button4Carre);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);

        //Set Value
        setValueTextView();
        // Inflate the layout for this fragment
        return rootView;
    }

    private void setValueTextView() {
        mButton1.setText(responses[0]);
        mButton2.setText(responses[1]);
        mButton3.setText(responses[2]);
        mButton4.setText(responses[3]);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateFragmentTexView(Bundle bundle) {
        if (bundle != null && bundle.getStringArray(RESPONSES_KEY) != null) {
            responses = bundle.getStringArray(RESPONSES_KEY);
        }
        setValueTextView();
    }

    // Send Response to Activity Listener
    @Override
    public void onClick(View v) {
        mListener.responseFromFragment(((Button) v).getText().toString(), QUESTION_VALUE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
