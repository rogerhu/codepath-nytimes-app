package codepath.com.nytimesfun;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.parceler.Parcels;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rhu on 12/23/15.
 */
public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder> {

    private Context mContext;

    private List<Article> mArticles;

    public ArticleRecyclerViewAdapter(List<Article> articles) {
        mArticles = articles;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Target {

        DynamicHeightImageView ivImage;
        TextView tvTitle;
        String url;

        public ViewHolder(View itemView) {
            super(itemView);

            ivImage = (DynamicHeightImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Article article = mArticles.get(position);
            Intent intent = new Intent(v.getContext(), ArticleActivity.class);
            intent.putExtra(ArticleActivity.ARTICLE, Parcels.wrap(article));
            mContext.startActivity(intent);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            float ratio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
            ivImage.setHeightRatio(ratio);
            ivImage.setImageBitmap(bitmap);
            ivImage.setVisibility(View.VISIBLE);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_image_result, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Article article = mArticles.get(position);

        holder.tvTitle.setText(article.getHeadline());

        String thumbNail = article.getThumbnail();

        if (!TextUtils.isEmpty(thumbNail)) {
            Picasso.with(mContext).load(thumbNail).placeholder(R.mipmap.ic_launcher).into(holder);

        } else {
            holder.ivImage.setImageBitmap(null);
            holder.ivImage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
