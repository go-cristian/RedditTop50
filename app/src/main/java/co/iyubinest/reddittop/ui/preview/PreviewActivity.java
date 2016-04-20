package co.iyubinest.reddittop.ui.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.iyubinest.reddittop.R;
import co.iyubinest.reddittop.ui.BaseActivity;
import co.iyubinest.reddittop.ui.ImageLoader;

public class PreviewActivity extends BaseActivity {

  private static final String IMAGE = "image";

  @Bind(R.id.preview_image) ImageView previewImage;

  public static Intent getIntent(Context context, String imageUrl) {
    Intent intent = new Intent(context, PreviewActivity.class);
    intent.putExtra(IMAGE, imageUrl);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preview);
    ButterKnife.bind(this);
    ImageLoader.load(previewImage, getIntent().getStringExtra(IMAGE));
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }
}
