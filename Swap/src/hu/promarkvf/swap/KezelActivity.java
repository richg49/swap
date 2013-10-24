package hu.promarkvf.swap;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class KezelActivity extends Activity implements OnClickListener {
	protected static final String PROFIL_PRODUCT_IR_ULR = SwapActivity.WEB_SERVICE_ULR + "profil_product_ir.php";
	private static final int      KINALAT_ACTIVITY_ID   = 0;
	private Animator              mCurrentAnimator;
	private int                   mShortAnimationDuration;
	View                          lv1;
	
	OnClickListener               kezelRogzit           = new OnClickListener() {
		                                                    
		                                                    @Override
		                                                    public void onClick(View v) {
			                                                    JSONObject json;
			                                                    ProductGroup pg = new ProductGroup();
			                                                    pg = SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex);
			                                                    Date now = new Date();
			                                                    Calendar cal = Calendar.getInstance();
			                                                    cal.setTime(now);
			                                                    now = cal.getTime();
			                                                    json = new JSONObject();
			                                                    int darab = 0;
			                                                    for (int i = 0; i < pg.productsSize(); i++) {
				                                                    Product p = pg.getProduct(i);
				                                                    try {
					                                                    JSONObject pobject = new JSONObject();
					                                                    pobject.put("product_id", p.getId());
					                                                    pobject.put("search", p.isSearch() ? 1 : 0);
					                                                    pobject.put("offer", p.isOffer() ? 1 : 0);
					                                                    pobject.put("count", p.getCount());
					                                                    pobject.put("create_date", SwapActivity.dateFormat_smal.format(now));
					                                                    pobject.put("message", p.getMessage());
					                                                    json.put(Integer.toString(darab), pobject);
					                                                    darab++;
				                                                    } catch (JSONException e) {
					                                                    e.printStackTrace();
				                                                    }
			                                                    }
			                                                    try {
				                                                    json.put("profil_id", SwapActivity.dbProfil.getId());
				                                                    json.put("darab", darab);
				                                                    json.put("UUID", SwapActivity.dbProfil.getUUID());
				                                                    json.put("email", SwapActivity.dbProfil.getEmail());
			                                                    } catch (JSONException e) {
				                                                    e.printStackTrace();
			                                                    }
			                                                    new DataBase(KezelActivity.this) {
				                                                    private ProgressDialog progressDialog = null;
				                                                    
				                                                    @Override
				                                                    protected void onPostExecute(String result) {
					                                                    this.progressDialog.dismiss();
					                                                    if (result != null) {
						                                                    if (result.length() == 1) {
							                                                    Toast.makeText(KezelActivity.this, getString(R.string.sikeresrogzites), Toast.LENGTH_SHORT).show();
						                                                    } else {
							                                                    Toast.makeText(KezelActivity.this, getString(R.string.hibasrogzites) + '\n' + result,
							                                                            Toast.LENGTH_SHORT).show();
						                                                    }
					                                                    }
				                                                    }
				                                                    
				                                                    @Override
				                                                    protected void onPreExecute() {
					                                                    this.progressDialog = new ProgressDialog(KezelActivity.this);
					                                                    this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
					                                                    this.progressDialog.show();
				                                                    }
				                                                    
			                                                    }.execute(PROFIL_PRODUCT_IR_ULR, "json=" + json.toString());
		                                                    }
	                                                    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kezel);
		
		if (this.findViewById(R.id.kezel_list) instanceof GridView) {
			this.lv1 = (GridView) this.findViewById(R.id.kezel_list);
			((GridView) this.lv1).setAdapter(new KezelArrayAdapter(KezelActivity.this));
		}
		if (this.findViewById(R.id.kezel_list) instanceof ListView) {
			this.lv1 = (ListView) this.findViewById(R.id.kezel_list);
			((ListView) this.lv1).setAdapter(new KezelArrayAdapter(KezelActivity.this));
		}
		
		Button btn_rogzit = (Button) this.findViewById(R.id.btn_kezel_rogzit);
		Button btn_kilep = (Button) this.findViewById(R.id.btn_kezel_cancel);
		
		btn_kilep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		btn_rogzit.setOnClickListener(kezelRogzit);
		
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void zoomImageFromThumb(final View thumbView, int imageResId, Bitmap bitmap) {
		if (this.mCurrentAnimator != null) {
			this.mCurrentAnimator.cancel();
		}
		
		final ImageView expandedImageView = (ImageView) findViewById(R.id.kezel_expanded_image);
		if (imageResId != 0) {
			expandedImageView.setImageResource(imageResId);
		}
		if (bitmap != null) {
			expandedImageView.setImageBitmap(bitmap);
		}
		
		final Rect startBounds = new Rect(thumbView.getLeft(), thumbView.getTop(), thumbView.getRight(), thumbView.getBottom());
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();
		
		//thumbView.getGlobalVisibleRect(startBounds);
		findViewById(R.id.kezel_container).getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);
		
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}
		
		thumbView.setAlpha(0f);
		expandedImageView.setVisibility(View.VISIBLE);
		
		expandedImageView.setPivotX(0f);
		expandedImageView.setPivotY(0f);
		
		AnimatorSet set = new AnimatorSet();
		set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
		        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
		        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
		set.setDuration(this.mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;
		
		final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}
				AnimatorSet set = new AnimatorSet();
				set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
				        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
				        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
						((LinearLayout) thumbView.getParent()).invalidate();
					}
					
					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		if (v instanceof hu.promarkvf.swap.TouchHighlightImageButton) {
			zoomImageFromThumb(v, 0, SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex).getProduct((Integer) v.getTag()).getImage_l());
		}
		if (v instanceof CheckBox) {
			switch (v.getId()) {
				case R.id.kezel_keres:
					SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex).getProduct((Integer) v.getTag()).setSearch(((CheckBox) v).isChecked());
					break;
				case R.id.kezel_kinal:
					SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex).getProduct((Integer) v.getTag()).setOffer(((CheckBox) v).isChecked());
					SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex).getProduct((Integer) v.getTag()).setCount(((CheckBox) v).isChecked() ? 1 : 0);
					break;
			}
		}
		if (v instanceof Button) {
			switch (v.getId()) {
				case R.id.kezel_keszlet:
					Intent intent = null;
					intent = new Intent(this, KinalatActivity.class);
					intent.putExtra("productId", (String) SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex).getProduct((Integer) v.getTag()).getIdStr());
					intent.putExtra("productName", (String) SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex).getProduct((Integer) v.getTag()).getName());
					startActivityForResult(intent, KINALAT_ACTIVITY_ID);
					break;
			}
		}
	}
}
