package listsample.taishonet.com.listsample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class PrefListActivity extends ActionBarActivity {

    private ListView mPrefListView = null;
    private PinnedHeaderSectionAdapter mPrefAdapter;
    private static final String SECTION = "-----"; //都道府県を地方ごとにわけるセクション文字列。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_list);
        mPrefListView = (ListView) findViewById(R.id.pref_list);
        createPrefList();
    }

    private void createPrefList() {
        List<String> sectionList = new ArrayList<>();
        List<List<String>> labelList = new ArrayList<>();

        String[] prefectures = getResources().getStringArray(R.array.pref_array);
        String[] areas = getResources().getStringArray(R.array.area_array);

        int index = 0;
        for (int i = 0; i < areas.length; i++) {
            sectionList.add(areas[i]);
            List<String> list = new ArrayList<>();
            while (index < prefectures.length && !prefectures[index].equals(SECTION)) {
                list.add(prefectures[index++]);
            }
            labelList.add(list);
            index++;
        }

        mPrefAdapter = new PinnedHeaderSectionAdapter(this, sectionList, labelList);
        mPrefAdapter.setOverlayHeaderView(findViewById(R.id.title));
        mPrefListView.setOnScrollListener(mPrefAdapter);
        mPrefListView.setAdapter(mPrefAdapter);
    }

}
