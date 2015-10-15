package ecjtu.net.demon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.NewMain;

/**
 * Created by 圣麟 on 2015/8/15.
 */
public class WelcomePage extends Fragment {

    private View view;
    private ImageView imageView;
    private int index;

    public void setIndex(int index) {
        this.index = index;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.welcome_page, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.welcomepage_image);
        switch(index) {
            case 0:
                imageView.setImageResource(R.drawable.welcome);
                break;
            case 1:
                imageView.setImageResource(R.drawable.welcome2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.welcome3);
                break;
        }
        if (index == 2) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), NewMain.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }
}
