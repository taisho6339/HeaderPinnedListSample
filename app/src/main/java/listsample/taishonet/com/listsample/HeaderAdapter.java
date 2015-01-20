package listsample.taishonet.com.listsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by taisho6339 on 2015/01/20.
 */
public class HeaderAdapter extends ArrayAdapter<HeaderAdapter.Item> implements AbsListView.OnScrollListener {

    public static class Item {
        public String title;
        public int imageId;

        public Item(String title, int imageId) {
            this.title = title;
            this.imageId = imageId;
        }
    }

    private LayoutInflater mInflater;
    private View mOverlayHeaderView;
    private String mOverlayHeaderTitle;


    public HeaderAdapter(Context context, String[] titles, int[] imageIds) {
        super(context, 0, new ArrayList<Item>());
        mInflater = LayoutInflater.from(context);

        for (int i = 0; i < titles.length; i++) {
            add(new Item(titles[i], imageIds[i]));
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        performOverlayHeader(view, firstVisibleItem);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void setOverlayHeaderView(View overlayHeaderView) {
        mOverlayHeaderView = overlayHeaderView;
    }

    public View getOverlayHeaderView() {
        return mOverlayHeaderView;
    }

    private void setHeaderTitle(View headerView, String title) {
        TextView tv = (TextView) headerView.findViewById(R.id.title);
        tv.setText(title);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row, null);
        }

        Item item = getItem(position);
        ((ImageView) convertView.findViewById(R.id.image)).setImageResource(item.imageId);
        setHeaderTitle(convertView, item.title);

        return convertView;
    }

    private void performOverlayHeader(AbsListView view, int firstVisibleItem) {
        View overlayView = getOverlayHeaderView();
        if (overlayView == null) {
            return;
        }

        if (view.getChildCount() == 0) {
            return;
        }

        String title = getItem(firstVisibleItem).title;

        View row = view.getChildAt(0);
        View header = row.findViewById(R.id.title);

        //次の行の上の位置
        int nextRowPosition = row.getTop() + row.getHeight();
        if (nextRowPosition >= overlayView.getHeight()) {
            overlayView.setY(0);
            if (row.getTop() == 0) {
                header.setVisibility(View.VISIBLE);
                overlayView.setVisibility(View.GONE);
            } else {
                header.setVisibility(View.INVISIBLE);
                overlayView.setVisibility(View.VISIBLE);
            }
            mOverlayHeaderTitle = title;
            setHeaderTitle(overlayView, title);
            ((View) overlayView.getParent()).postInvalidate();
        } else {
            float offset = nextRowPosition - overlayView.getHeight();
            overlayView.setY(offset);

            header.setVisibility(View.VISIBLE);
            overlayView.setVisibility(View.VISIBLE);

            if (mOverlayHeaderTitle.equals(title)) {
                mOverlayHeaderTitle = title;
                setHeaderTitle(overlayView, title);
                ((View) overlayView.getParent()).postInvalidate();
            }
        }

        setHeaderVisibility(1, view, View.VISIBLE);
    }


    private void setHeaderVisibility(int childPos, AbsListView view, int visibility) {
        if (view.getChildCount() <= childPos) {
            return;
        }

        View header = view.getChildAt(childPos).findViewById(R.id.title);
        if (header != null) {
            header.setVisibility(visibility);
        }
    }
}
