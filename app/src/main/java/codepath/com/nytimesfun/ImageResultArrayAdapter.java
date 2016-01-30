package codepath.com.nytimesfun;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by rhu on 12/15/15.
 */
public class ImageResultArrayAdapter extends ArrayAdapter<TopStoryResult> {

    public ImageResultArrayAdapter(Context context, List<TopStoryResult> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopStoryResult article = this.getItem(position);

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            // 3 parameters -- 1st item I want to inflate, root is parent, do not want to attach yet
            convertView = inflator.inflate(R.layout.item_image_result, parent, false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        ivImage.setImageResource(0);

//        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//        tvTitle.setText(article.getTitle());

        String thumbNail = article.getThumbnail();
        if (!TextUtils.isEmpty(thumbNail)) {
            Picasso.with(getContext()).load(thumbNail).fit().into(ivImage);
        }
        return convertView;
    }
}
