//package com.lzxmy.demo.tag;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.lzxmy.demo.R;
//
//import java.util.Set;
//
///**
// * Created by zhy on 15/9/10.
// */
//public class EventTestFragment extends Fragment
//{
//    private String[] mVals = new String[]
//            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
//                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
//                    "Android", "Weclome Hello", "Button Text", "TextView"};
//
//    private TagFlowLayout mFlowLayout;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        return inflater.inflate(R.layout.activity_main, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view,  Bundle savedInstanceState)
//    {
//        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
//        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.id_flowlayout);
//        //mFlowLayout.setMaxSelectCount(3);
//        mFlowLayout.setAdapter(new TagAdapter<String>(mVals)
//        {
//
//            @Override
//            public View getView(FlowLayout parent, int position, String s)
//            {
//                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
//                        mFlowLayout, false);
//                tv.setText(s);
//                return tv;
//            }
//        });
//
//        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
//        {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent)
//            {
//                Toast.makeText(getActivity(), mVals[position], Toast.LENGTH_SHORT).show();
//                //view.setVisibility(View.GONE);
//                return true;
//            }
//        });
//
//
//        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
//        {
//            @Override
//            public void onSelected(Set<Integer> selectPosSet)
//            {
//                getActivity().setTitle("choose:" + selectPosSet.toString());
//            }
//        });
//    }
//}
