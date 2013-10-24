package hu.promarkvf.swap;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class KeresActivity extends Activity implements OnClickListener {
	private Animator                   mCurrentAnimator;
	private int                        mShortAnimationDuration;
	ArrayList<Product>                 pgList = new ArrayList<Product>();
	AdapterView.AdapterContextMenuInfo info;
	int                                ret    = 0;
	GridView                           lv1;
	View                               thumb1View;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_keres);
		
		this.lv1 = (GridView) this.findViewById(R.id.keres_list);
		this.lv1.setAdapter(new KeresArrayAdapter(this));
		
	}
	
	private void zoomImageFromThumb(final View thumbView, int imageResId, Bitmap bitmap) {
		if (this.mCurrentAnimator != null) {
			this.mCurrentAnimator.cancel();
		}
		
		final ImageView expandedImageView = (ImageView) this.findViewById(R.id.expanded_image);
		if (imageResId != 0) {
			expandedImageView.setImageResource(imageResId);
		}
		if (bitmap != null) {
			expandedImageView.setImageBitmap(bitmap);
		}
		
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();
		
		thumbView.getGlobalVisibleRect(startBounds);
		this.findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
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
		
		//		thumbView.setAlpha(0f);
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
				KeresActivity.this.mCurrentAnimator = null;
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				KeresActivity.this.mCurrentAnimator = null;
			}
		});
		set.start();
		this.mCurrentAnimator = set;
		
		final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (KeresActivity.this.mCurrentAnimator != null) {
					KeresActivity.this.mCurrentAnimator.cancel();
				}
				AnimatorSet set = new AnimatorSet();
				set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
				        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
				        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
				set.setDuration(KeresActivity.this.mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						thumbView.setActivated(false);
						thumbView.setPressed(false);
						((TouchHighlightImageButton) thumbView).clearFocus();
						((TouchHighlightImageButton) thumbView).setSelected(false);
						((TouchHighlightImageButton) thumbView).requestFocus();
						//						((TouchHighlightImageButton) thumbView).
						thumbView.invalidate();
						expandedImageView.setVisibility(View.GONE);
						KeresActivity.this.mCurrentAnimator = null;
						((LinearLayout) thumbView.getParent()).invalidate();
					}
					
					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						thumbView.setActivated(false);
						thumbView.setPressed(false);
						((TouchHighlightImageButton) thumbView).clearFocus();
						((TouchHighlightImageButton) thumbView).setSelected(false);
						((TouchHighlightImageButton) thumbView).requestFocus();
						//						((TouchHighlightImageButton) thumbView).
						thumbView.invalidate();
						expandedImageView.setVisibility(View.GONE);
						KeresActivity.this.mCurrentAnimator = null;
					}
				});
				set.start();
				KeresActivity.this.mCurrentAnimator = set;
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		if (v instanceof hu.promarkvf.swap.TouchHighlightImageButton) {
			this.zoomImageFromThumb(v, 0, SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex).getProduct((Integer) v.getTag()).getImage_l());
		}
		if (v instanceof CheckBox) {
			SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex).getProduct((Integer) v.getTag()).setSearch(((CheckBox) v).isChecked());;
		}
	}
	
}
