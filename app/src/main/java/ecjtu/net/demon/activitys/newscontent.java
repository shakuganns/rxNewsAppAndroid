package ecjtu.net.demon.activitys;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ecjtu.net.demon.R;
import ecjtu.net.demon.utils.HttpHelper;
import ecjtu.net.demon.view.articleView;


public class newscontent extends Activity {

    private final String newsbody = "<p>asdkfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjafoiajnbvijassdjflksadjflb" +
            "asdkfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjafoiajnbvijassdjflksadjflbnsadkbvkjnlkjzlkjflkdjsalkfjlknlzncvlkjajp" +
            "odjsfpjsadlkfjkldsaasdkfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjafoiajnbvijassdjflksadjflbnsadkbvkjnlkjzlkjflkdjsa" +
            "lkfjlknlzncvlkjajpodjsfpjsadlkfjkldsaasdkfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjafoiajnbvijassdjflksadjflbnsadkb" +
            "vkjnlkjzlkjflkdjsalkfjlknlzncvlkjajpodjsfpjsadlkfjkldsaasdkfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjafoiajnbvijass" +
            "djflksadjflbnsadkbvkjnlkjzlkjflkdjsalkfjlknlzncvlkjajpodjsfpjsadlkfjkldsaasdkfjlkasjdlfkjsaldkjflkasdjlncewhoiji" +
            "odsjafoiajnbvijassdjflksadjflbnsadkbvkjnlkjzlkjflkdjsalkfjlknlzncvlkjajpodjsfpjsadlkfjkldsaasdkfjlkasjdlfkjsaldk" +
            "jflkasdjlncewhoijiodsjafoiajnbvijassdjflksadjflbnsadkbvkjnlkjzlkjflkdjsalkfjlknlzncvlkjajpodjsfpjsadlkfjkldsaasd" +
            "kfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjafoiajnbvijassdjflksadjflbnsadkbvkjnlkjzlkjflkdjsalkfjlknlzncvlkjajpodjs" +
            "fpjsadlkfjkldsaasdkfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjafoiajnbvijassdjflksadjflbnsadkbvkjnlkjzlkjflkdjsalkfj" +
            "lknlzncvlkjajpodjsfpjsadlkfjkldsaasdkfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjafoiajnbvijassdjflksadjflbnsadkbvkjn" +
            "lkjzlkjflkdjsalkfjlknlzncvlkjajpodjsfpjsadlkfjkldsaasdkfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjafoiajnbvijassdjflk" +
            "sadjflbnsadkbvkjnlkjzlkjflkdjsalkfjlknlzncvlkjajpodjsfpjsadlkfjkldsaasdkfjlkasjdlfkjsaldkjflkasdjlncewhoijiodsjaf" +
            "oiajnbvijassdjflksadjflbnsadkbvkjnlkjzlkjflkdjsalkfjlknlzncvlkjajpodjsfpjsadlkfjkldsaasdkfjlkasjdlfkjsaldkjflkasd" +
            "" +
            "</p>";
    private articleView articleView;
    private TextView titleText;
    private TextView author;
    private TextView dataTime;
    private TextView tags;
    private String title;
    private int windows_width;
    private Handler getArticleContent = new Handler() {
        public void handleMessage(Message message) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) message.obj;
            Log.i("tag", "================================++++++++++==========" + String.valueOf(hashMap.get("title")));
            titleText.setText((String) hashMap.get("title"));
            author.setText((String) hashMap.get("author"));
            tags.setText((String) hashMap.get("tags"));
            dataTime.setText((String) hashMap.get("dataTime"));
            ArrayList<HashMap<String, String>> arrayList = (ArrayList<HashMap<String, String>>) hashMap.get("article");
            articleView.setText(arrayList, windows_width);
        }
    };
    private String ArticleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscontent);
        Bundle bundle = getIntent().getExtras();
        ArticleID = bundle.getString("articleID");

        articleView = (ecjtu.net.demon.view.articleView) findViewById(R.id.news_article);
        titleText = (TextView) findViewById(R.id.contet_title);
        author = (TextView) findViewById(R.id.content_author);
        tags = (TextView) findViewById(R.id.content_tag);
        dataTime = (TextView) findViewById(R.id.date);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        windows_width = dm.widthPixels;
        Log.i("tag", "width" + String.valueOf(windows_width));

//        articleView.setText(getdata(),windows_width);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.hide();
        new getConent(ArticleID).start();
//        title = "你妹妹的点点";
//
//        actionBar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_newscontent, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            Intent upIntent = NavUtils.getParentActivityIntent(newscontent.this);
            Log.i("tag", String.valueOf(upIntent) + "-----------");
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            } else {
                Log.i("tag", "nihao" + String.valueOf(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, upIntent);
            }

            Log.i("tag", "nihao-------------------------------------------------------------------------->");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<HashMap<String, String>> getdata() {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("type", "image");
        hashMap.put("value", "http://img4.imgtn.bdimg.com/it/u=3604237660,4050592132&fm=21&gp=0.jpg");
        list.add(hashMap);
        HashMap<String, String> hashMap1 = new HashMap<String, String>();
        hashMap1.put("type", "text");
        hashMap1.put("value", newsbody);
        list.add(hashMap1);
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        hashMap2.put("type", "image");
        hashMap2.put("value", "http://img4.imgtn.bdimg.com/it/u=3604237660,4050592132&fm=21&gp=0.jpg");
        list.add(hashMap2);
        return list;
    }

    private class getConent extends Thread {
        private String articleID;

        public getConent(String articleID) {
            this.articleID = articleID;
        }

        @Override
        public void run() {
            String url = "http://homker.sinaapp.com/app.php";
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            HttpHelper httpHelper = new HttpHelper();
            hashMap = httpHelper.getNewsContent(articleID,url);
            Message message = getArticleContent.obtainMessage();
            message.obj = hashMap;
            getArticleContent.sendMessage(message);
        }
    }

}
