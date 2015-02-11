package listsample.taishonet.com.listsample;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BaseSectionAdapter<T1, T2> extends BaseAdapter {

    /**
     * インデックス行:ヘッダー
     */
    private static final int INDEX_PATH_ROW_HEADER = -1;

    /**
     * ビュータイプ:ヘッダー行
     */
    private static final int ITEM_VIEW_TYPE_HEADER = 0;

    /**
     * ビュータイプ:データ行
     */
    private static final int ITEM_VIEW_TYPE_ROW = 1;

    protected Context context;
    protected LayoutInflater inflater;

    /**
     * ヘッダー行で使用するデータリスト
     */
    protected List<T1> sectionList;

    /**
     * データ行で使用するデータリスト
     */
    protected List<List<T2>> rowList;

    private List<IndexPath> indexPathList;

    public BaseSectionAdapter(Context context, List<T1> sectionList, List<List<T2>> rowList) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.sectionList = sectionList;
        this.rowList = rowList;
        this.indexPathList = getIndexPathList(sectionList, rowList);
    }

    public Object getFirstHeaderText(int position) {
        int index = position;
        while (index >= 0) {
            IndexPath indexPath = indexPathList.get(index);
            if (isHeader(indexPath)) {
                return getItem(index);
            }
            index--;
        }
        return null;
    }

    @Override
    public int getCount() {
        int count = indexPathList.size();
        return count;
    }

    @Override
    public Object getItem(int position) {
        IndexPath indexPath = indexPathList.get(position);
        if (isHeader(indexPath)) {
            return sectionList.get(indexPath.section);
        } else {
            return rowList.get(indexPath.section).get(indexPath.row);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IndexPath indexPath = indexPathList.get(position);
        if (isHeader(indexPath)) {
            return viewForHeaderInSection(convertView, indexPath.section);
        } else {
            return cellForRowAtIndexPath(convertView, indexPath);
        }
    }

    public View viewForHeaderInSection(View convertView, int section) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_header, null);
        }
        ((TextView) convertView.findViewById(R.id.title)).setText(sectionList.get(section).toString());
        return convertView;
    }

    public View cellForRowAtIndexPath(View convertView, IndexPath indexPath) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        TextView textView = (TextView) convertView;
        textView.setText(rowList.get(indexPath.section).get(indexPath.row).toString());
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // ヘッダー行とデータ行の2種類なので、2を返します。
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // ビュータイプを返します。
        if (isHeader(position)) {
            return ITEM_VIEW_TYPE_HEADER;
        } else {
            return ITEM_VIEW_TYPE_ROW;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        if (isHeader(position)) {
            // ヘッダー行の場合は、タップできないようにします。
            return false;
        } else {
            return super.isEnabled(position);
        }
    }

    private List<IndexPath> getIndexPathList(List<T1> sectionList, List<List<T2>> rowList) {
        List<IndexPath> indexPathList = new ArrayList<>();
        for (int i = 0; i < sectionList.size(); i++) {
            IndexPath sectionIndexPath = new IndexPath();
            sectionIndexPath.section = i;
            sectionIndexPath.row = INDEX_PATH_ROW_HEADER;
            indexPathList.add(sectionIndexPath);

            List<T2> rowListBySection = rowList.get(i);
            for (int j = 0; j < rowListBySection.size(); j++) {
                IndexPath rowIndexPath = new IndexPath();
                rowIndexPath.section = i;
                rowIndexPath.row = j;
                indexPathList.add(rowIndexPath);
            }
        }
        return indexPathList;
    }

    protected boolean isHeader(int position) {
        IndexPath indexPath = indexPathList.get(position);
        return isHeader(indexPath);
    }

    private boolean isHeader(IndexPath indexPath) {
        if (INDEX_PATH_ROW_HEADER == indexPath.row) {
            return true;
        } else {
            return false;
        }
    }
}