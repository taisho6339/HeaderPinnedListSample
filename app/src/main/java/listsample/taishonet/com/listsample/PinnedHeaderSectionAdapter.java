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

        //overlayviewがセットされていないなら何もしない
        View overlayView = getOverlayHeaderView();
        if (overlayView == null) {
            return;
        }

        //ヘッダーが一番上に来たらタイトルを更新する
        if (isHeader(firstVisibleItem)) {
            mOverlayHeaderTitle = getItem(firstVisibleItem).toString();
            setHeaderTitle(mOverlayHeaderView, mOverlayHeaderTitle);
        }

        //表示されているリストの二番目がヘッダーじゃないと無視する
        if (!isHeader(firstVisibleItem + 1)) {
            //ヘッダーが押し出されて、次のヘッダーだったものが一番上にくると以降が実行されないため、
            // オーバーレイヘッダーが消えたままになる
            //これを防ぐため、オーバーレイを０調整する処理を入れる
            overlayView.setY(0);
            return;
        }

        //表示するべき文字を取得
        mOverlayHeaderTitle = getPresentViewableHeaderText(firstVisibleItem).toString();

        //現在表示されている一番上のViewの次のView
        View nextRow = view.getChildAt(1);

        //次の行の上側の位置
        int nextRowPosition = nextRow.getTop();
        //次の行が、表示しているヘッダーの位置に届いたとき
        if (nextRowPosition < overlayView.getHeight()) {
            float offset = nextRowPosition - overlayView.getHeight();
            overlayView.setY(offset);
            setHeaderTitle(overlayView, mOverlayHeaderTitle);
            ((View) overlayView.getParent()).postInvalidate();
        }
    }
}
