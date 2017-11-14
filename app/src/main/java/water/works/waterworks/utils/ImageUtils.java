package water.works.waterworks.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;


public class ImageUtils {
	Context context = null;
	DisplayImageOptions options = null;
	ImageSize minImageSize = null;
	ImageLoader imageLoader = null;

	private static ImageUtils instance;

	Display display;

	public static ImageUtils getInstance(Context context) {
		if (instance == null) {
			instance = new ImageUtils(context);
		}

		return instance;
	}

	@SuppressWarnings("deprecation")
	private ImageUtils(Context context) {
		this.context = context;

		display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		ContextWrapper ctx = new ContextWrapper(context);
		 
		  File cacheDir = ctx.getDir("pov", Context.MODE_PRIVATE);
		 
		 // BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(newfile), 16 * 1024)
//		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
//				"UniversalImageLoader/POV_FINAL");
		 //Log.e("cacheDir", ""+cacheDir.getAbsolutePath());
		// Get singletone instance of ImageLoader
		imageLoader = ImageLoader.getInstance();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		// Create configuration for ImageLoader (all options are optional, use
		// only those you really want to customize)
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(outMetrics.widthPixels,
						outMetrics.heightPixels)
				// max width, max height
				.discCacheExtraOptions(480,800,null)
//				.diskCacheExtraOptions(480, 800, null)
				// Can slow ImageLoader, use it carefully (Better don't use it)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
//				.offOutOfMemoryHandling()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache implementation
//				.discCache(new UnlimitedDiscCache(cacheDir))
				.diskCache(new UnlimitedDiskCache(cacheDir)) // default
				// You can pass your own disc cache implementation
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
//				.imageDownloader(
//						new URLConnectionImageDownloader(60 * 1000, 60 * 1000))
				.imageDownloader(new BaseImageDownloader(context)) // default
				// connectTimeout (5 s), readTimeout (20 s)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//				.enableLogging().build();
                .writeDebugLogs()
 				.build();

		// Initialize ImageLoader with created configuration. Do it once on
		// Application start.
		imageLoader.init(config);

		// Just load image
		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new SimpleBitmapDisplayer()).build();

	}

	public void setImageUrlToView(String imageUrl, final ImageView imageView,
			final ProgressBar progressBar, final int defaultImage,
			final boolean isAspectReq, final boolean isDefaultImgReq,
			final int reqHeight, final int reqWidth, final boolean isRounded) {

		// hide image view
		imageView.setVisibility(View.INVISIBLE);

		imageLoader.displayImage(imageUrl, imageView, options,
				new ImageLoadingListener() {

					public void onLoadingStarted(String s, View view) {
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
					}

					public void onLoadingFailed(String s, View view, FailReason failReason) {
						if (progressBar != null) {
							progressBar.setVisibility(View.GONE);
						}
						if (isDefaultImgReq && defaultImage > 0) {
							imageView.setImageResource(defaultImage);
						} else {
							imageView.setVisibility(View.GONE);
						}
					}

					public void onLoadingComplete(String s, View view, Bitmap bitmap) {
						if (progressBar != null) {
							progressBar.setVisibility(View.GONE);
						}
						if (bitmap != null) {
							imageView.setVisibility(View.VISIBLE);
							imageView.setImageBitmap(bitmap);

						} else if (isDefaultImgReq && defaultImage > 0) {
							imageView.setVisibility(View.VISIBLE);
							imageView.setImageResource(defaultImage);
						} else {
							imageView.setVisibility(View.VISIBLE);
							imageView.setImageResource(defaultImage);
						}
					}

					public void onLoadingCancelled(String s, View view) {
						if (progressBar != null) {
							progressBar.setVisibility(View.GONE);
						}
						if (isDefaultImgReq && defaultImage > 0) {
							imageView.setImageResource(defaultImage);
						} else {
							imageView.setVisibility(View.GONE);
						}
					}

//					public void onLoadingCancelled() {
//						if (progressBar != null) {
//							progressBar.setVisibility(View.GONE);
//						}
//						if (isDefaultImgReq && defaultImage > 0) {
//							imageView.setImageResource(defaultImage);
//						} else {
//							imageView.setVisibility(View.GONE);
//						}
//					}

//					public void onLoadingComplete(Bitmap bitmap) {
//						if (progressBar != null) {
//							progressBar.setVisibility(View.GONE);
//						}
//						if (bitmap != null) {
//							imageView.setVisibility(View.VISIBLE);
//							imageView.setImageBitmap(bitmap);
//
//						} else if (isDefaultImgReq && defaultImage > 0) {
//							imageView.setVisibility(View.VISIBLE);
//							imageView.setImageResource(defaultImage);
//						} else {
//							imageView.setVisibility(View.VISIBLE);
//							imageView.setImageResource(defaultImage);
//						}
//					}

//					public void onLoadingFailed(FailReason arg0) {
//						if (progressBar != null) {
//							progressBar.setVisibility(View.GONE);
//						}
//						if (isDefaultImgReq && defaultImage > 0) {
//							imageView.setImageResource(defaultImage);
//						} else {
//							imageView.setVisibility(View.GONE);
//						}
//					}

//					public void onLoadingStarted() {
//						if (progressBar != null) {
//							progressBar.setVisibility(View.VISIBLE);
//						}
//					}

				});
	 
	}

 

	 
 
}
