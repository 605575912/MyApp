package com.lzxmy.demo.Algorithm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.library.uiframe.BaseActivity;
import com.lzxmy.demo.R;


/**
 * 算法基础
 * Created by apple on 15/8/27.
 */
public class AlgorithmActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorthm_layout);

        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        int sts = 8 >>> 2;//1
        int stt = 8 << 4;//128
        Log.i("TAG", sts + "========" + stt);
//        HashMap<Integer,Object> sd =null;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.button3: {
//                new Thread(
//                ) {
//                    @Override
//                    public void run() {
//                        super.run();
//
//                        Integer[] t = new Integer[]{1, 3, 5, 22, 23, 45, 66, 77, 90};//22
//
//                        partition(t, 0, t.length - 1);
//                        for (int i = 0; i < t.length; i++) {
//                            Log.i("TAG", t[i] + "==");
//                        }
//
//                    }
//                }.start();
//
//            }
//            break;
//            case R.id.button4: {
//                new Thread(
//                ) {
//                    @Override
//                    public void run() {
//                        super.run();
//                        Integer[] t = new Integer[]{1, 3, 5, 22, 23, 45, 66, 77};//22
//                        partition(t, 0, t.length - 1);
//                        int index = searchvalue(t, 1);
//                        Log.i("TAG", "=index=" + index);
//
//                    }
//                }.start();
//            }
//            break;
//            case R.id.button5: {
//                new Thread(
//                ) {
//                    @Override
//                    public void run() {
//                        super.run();
//                        Integer[] t = new Integer[]{69, 9, 22, 5, 28, 24, 95, 77};//22
//
//                        ByInsertsort(t);
//
////                        InsertSort(t);
//
//                        for (int i = 0; i < t.length; i++) {
//                            Log.i("TAG", "=index=" + t[i]);
//                        }
//
//                    }
//                }.start();
//
//            }
//            break;
//        }

    }



    void ByInsertsort(Integer[] values) {
//        Integer[] t = new Integer[]{69, 69, 22, 5, 28, 24, 95, 77};//9
        int j = 0;
        for (int i = 1; i < values.length; i++) {
            int temp = values[i];
            j = i - 1;
            while (j >= 0 && temp < values[j]) {
                values[j + 1] = values[j];
                j--;
            }
            values[j + 1] = temp;
//        Integer[] t = new Integer[]{1, 6, 22, 5, 28, 24, 95, 77};//1

        }
    }

    int searchvalue(Integer[] values, int value) {
        if (values.length > 1) {
            int start = 0;
            int end = values.length - 1;
            while (start <= end) {
                int index = (end + start) / 2;
                int temp = values[index];
                if (temp == value) {
                    return index;
                } else if (temp > value) {
                    end = index - 1;
                } else {
                    start = index + 1;

                }
            }
            return -1;

        } else {
            int temp = values[0];
            if (temp == value) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public void InsertSort(Integer[] array) {
        // 3,2,4,5
        int i, j;
        int insertNote;//要插入的数据
        //从数组的第二个元素开始循环将数组中的元素插入
        for (i = 1; i < array.length; i++) {
            //设置数组中的第2个元素为第一次循环要播讲的数据
            insertNote = array[i];
            j = i - 1;
            while (j >= 0 && insertNote < array[j]) {
                //如果要播讲的元素小于第j个元素,就将第j个元素向后移动
                array[j + 1] = array[j];
                j--;
            }
            //直到要插入的元素不小于第j个元素,将insertNote插入到数组中
            array[j + 1] = insertNote;
        }
    }


    void partition(Integer[] values, int start, int end) {
        int d = quicksort(values, start, end);

        if (end > start) {
            if (d > 0) {
                partition(values, start, d - 1);
            }
            if (d < end) {
                partition(values, d + 1, end);
            }
        }

    }

    int quicksort(Integer[] values, int start, int end) {
        int temp = values[start];
        while (end > start) {
            while (start < end && values[end] > temp) {
                end = end - 1;
            }
            if (end > start)
                values[start] = values[end];

            while (start < end && values[start] < temp) {
                start = start + 1;
            }
            if (end > start)
                values[end] = values[start];
        }
        values[start] = temp;
        return start;


    }
}
