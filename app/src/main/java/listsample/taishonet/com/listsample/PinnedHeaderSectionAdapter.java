package listsample.taishonet.com.listsample;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by taisho6339 on 2015/02/12.
 */
public class PinnedHeaderSectionAdapter extends BaseSectionAdapter<String, String> implements AbsListView.OnScrollListener {

    private View mOverlayHeaderView;
    private String mOverlayHeaderTitle;

    public PinnedHeaderSectionAdapter(Context context,
                                      List<String> sectionList, List<List<String>> prefList) {
        super(context, sectionList, prefList);
        mOverlayHeaderTitle = getItem(0).toString();
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
        setHeaderTitle(mOverlayHeaderView, mOverlayHeaderTitle);
    }

    public View getOverlayHeaderView() {
        return mOverlayHeaderView;
    }

    private void setHeaderTitle(View headerView, String title) {
        TextView tv = (TextView) headerView.findViewById(R.id.title);
        tv.setText(title);
    }

    private void performOverlayHeader(AbsListView view, int firstVisibleItem) {

        //リストが空の場合は何もしない
        if (view.getChildCount() == 0) {
            return;
        }

        View overlayView = getOverlayHeaderView();
        if (overlayView == null) {
            return;
        }
        overlayView.setY(0);

        //二番目がヘッダーじゃないと無視する
        if (!isHeader(firstVisibleItem + 1)) {
            return;
        }

        //表示するべき文字を取得
        String title = getFirstHeaderText(firstVisibleItem).toString();

        //現在表示されている一番上のViewの次のView
        View nextRow = view.getChildAt(1);

        //次の行の上側の位置
        int nextRowPosition = nextRow.getTop();
        //次の行が表示しているヘッダーの位置に届いていいないとき
        if (nextRowPosition >= overlayView.getHeight()) {
            overlayView.setY(0);
            mOverlayHeaderTitle = title;
            setHeaderTitle(overlayView, title);
            ((View) overlayView.getParent()).postInvalidate();
        } else {
            //次の行のヘッダーがオーバーレイヘッダーに届いた時
            float offset = nextRowPosition - overlayView.getHeight();
            overlayView.setY(offset);
            if (!mOverlayHeaderTitle.equals(title)) {
                mOverlayHeaderTitle = title;
                setHeaderTitle(overlayView, title);
                ((View) overlayView.getParent()).postInvalidate();
            }
        }
    }
}
