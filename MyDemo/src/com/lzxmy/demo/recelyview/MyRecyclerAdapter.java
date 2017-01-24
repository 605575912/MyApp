package com.lzxmy.demo.recelyview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzxmy.demo.R;
import com.support.loader.ServiceLoader;
import com.support.loader.packet.ImageOptions;

import java.util.List;

//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private List<ViewModel> items;
    private OnRecyclerViewItemClickListener<ViewModel> itemClickListener;
    private int itemLayout;
    //    private PaletteManager paletteManager = new PaletteManager();
    Context context;
    ImageOptions imageOptions;

    public MyRecyclerAdapter(List<ViewModel> items, int itemLayout, Context context) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.context = context;
        imageOptions = new ImageOptions(context, R.drawable.h_defaultpic);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ViewModel item = items.get(position);
        holder.itemView.setTag(item);
        holder.text.setText(item.getText());
//        Picasso.with(holder.image.getContext()).load(item.getImage()).into(holder.image, new Callback() {
//            @Override public void onSuccess() {
//                holder.updatePalette(paletteManager);
//            }
//
//            @Override public void onError() {}
//        });
        ServiceLoader.getInstance().displayImage(imageOptions, item.getImage(), holder.image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            ViewModel model = (ViewModel) view.getTag();
            itemClickListener.onItemClick(view, model);
        }
    }

    public void add(ViewModel item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(ViewModel item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener<ViewModel> listener) {
        this.itemClickListener = listener;
    }

    private static int setColorAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0x00ffffff);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
        }

//        public void updatePalette(PaletteManager paletteManager) {
//            String key = ((ViewModel)itemView.getTag()).getImage();
//            Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
//            paletteManager.getPalette(key, bitmap, new PaletteManager.Callback() {
//                @Override
//                public void onPaletteReady(Palette palette) {
//                    int bgColor = palette.getDarkVibrantColor().getRgb();
//                    text.setBackgroundColor(setColorAlpha(bgColor, 192));
//                    text.setTextColor(palette.getLightMutedColor().getRgb());
//                }
//            });
//        }
    }
}
