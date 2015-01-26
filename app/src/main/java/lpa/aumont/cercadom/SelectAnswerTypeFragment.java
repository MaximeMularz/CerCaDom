package lpa.aumont.cercadom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 */
public class SelectAnswerTypeFragment extends Fragment {

    public static String TAG = "SELECT_POINT_FRAGMENT_TAG";

    public SelectAnswerTypeFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_answer_type, container, false);
    }

}
