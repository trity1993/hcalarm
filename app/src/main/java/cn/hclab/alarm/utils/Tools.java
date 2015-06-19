package cn.hclab.alarm.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import cc.trity.model.entities.Authentication;
import cn.hclab.alarm.R;

public class Tools {
	private static final String TAG = "Tools";

	public static void ToastUtils(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	public static void ToastUtils(Context context, int text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/** 加载对话框 */
	public static final ProgressDialog CommonProgressDialog(Context context) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 圆形滚动
		progressDialog.setMessage("Loading...");
		return progressDialog;
	}
	/*
	 * 加载提示
	 */
	public static final ProgressDialog CommonProgressDialog(Context context,String msg) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(msg);
		return progressDialog;
	}

	// 捕获磁盘访问或者网络访问中与主进程之间交互产生的问题
	public static void startStrictMode() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectAll().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
	}

	/*
	 * 通过遮罩来改变任何图片的形状,自定义遮罩
	 * 圆角bitmap
	 */
	public void createShap(Context context){
		ImageView iv=new ImageView(context);
		//创建并加载图片，通常为不可修改
		Bitmap source=new BitmapFactory().decodeResource(context.getResources(), R.drawable.ic_launcher);
		//创建一个可供修改的图片，然后加上cavous
		Bitmap result=Bitmap.createBitmap(source.getWidth(),source.getHeight(),Config.ARGB_8888);
		Canvas canvas=new Canvas(result);
		Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
		
		//创建并绘制圆角矩形“遮罩”
		RectF rect=new RectF(0,0,source.getWidth(),source.getHeight());
		float radius=25.0f;
		paint.setColor(Color.BLACK);
		canvas.drawRoundRect(rect, 0, 0, paint);
		//用转换模式转化并绘制原图
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));//关键是这里PorterDuffXfermode，显示重叠，还是哪种模式
		canvas.drawBitmap(source, 0, 0,paint);
		paint.setXfermode(null);

		iv.setImageBitmap(result);
	}
	/*
	 * 任意遮罩图形，通过一种图片来制作遮罩图层，注意两者的区别
	 */
	public void createShap(Context context,int maskDrawable){
		ImageView iv=new ImageView(context);
		//创建并加载图片，通常为不可修改
		Bitmap source=new BitmapFactory().decodeResource(context.getResources(), R.drawable.ic_launcher);
		Bitmap mask=new BitmapFactory().decodeResource(context.getResources(), maskDrawable);
		//创建一个可供修改的图片，然后加上cavous
		Bitmap result=Bitmap.createBitmap(source.getWidth(),source.getHeight(),Config.ARGB_8888);
		Canvas canvas=new Canvas(result);
		Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
		
		//先画上遮罩图片，然后用转换模式绘制原图
		canvas.drawBitmap(mask, 0, 0,paint);
		
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));//关键是这里PorterDuffXfermode，显示重叠，还是哪种模式
		canvas.drawBitmap(source, 0, 0,paint);
		paint.setXfermode(null);

		iv.setImageBitmap(result);
	}
	public static final void showResultDialog(Context context, String msg,
											  String title) {
		if(msg == null) return;
		String rmsg = msg.replace(",", "\n");
		Log.d("Util", rmsg);
		new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
				.setNegativeButton("知道了", null).create().show();
	}
	/**
	 * 根据一个网络连接(String)获取bitmap图像
	 *
	 * @param imageUri
	 * @return
	 */
	public static Bitmap getbitmap(String imageUri) {
		Log.v(TAG, "getbitmap:" + imageUri);
		// 显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Log.v(TAG, "image download finished." + imageUri);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			bitmap = null;
		} catch (IOException e) {
			e.printStackTrace();
			Log.v(TAG, "getbitmap bmp fail---");
			bitmap = null;
		}
		return bitmap;
	}
	/**微博的为7天有效
	 * 腾讯qq好像是3个月
	 * 计算授权失效日期，失效后，需要重新登录
	 */
	public String calculate(String expiresIn){
		long currentExpiresIn=(Long.parseLong(expiresIn)-System.currentTimeMillis())/1000;
		if(currentExpiresIn<=0){
			//表示token已经过期，应该提示用户重新走登录流程
			return null;
		}else {
			return currentExpiresIn+"";
		}
	}
	/*
	 * 将任意图片转化指定半径的圆形图--最好是正方形 bmp 代表图片 radius 半径
	 */
	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius,int color) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;

		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth) {// 高大于宽
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			// 截取正方形图片
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else if (bmpHeight < bmpWidth) {// 宽大于高
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else {
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter
				|| squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
					diameter, true);

		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight());

		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		//设置图形重叠时的处理方式
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		// bitmap回收(recycle导致在布局文件XML看不到效果)
		bmp.recycle();
		squareBitmap.recycle();
		scaledSrcBmp.recycle();
		//添加边框，不适用的话，将会有锯齿的情况
		setBitmapBorder(canvas,color);
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}
	/*
	 * 为圆形图添加边框
	 */
	public static void setBitmapBorder(Canvas canvas,int color) {
		// 1.获取Canvas裁剪界限：
		Rect rect = canvas.getClipBounds();
		int width = rect.width();
		int height = rect.height();
		Paint paint = new Paint();
		// 设置边框颜色
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		// 设置边框宽度
		paint.setStrokeWidth(2);
		// 加入效果
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		//第一，第二个参数确定位置，第三个参数是半径,为了能让边框显示出来，第四个是画笔
		canvas.drawCircle(width / 2, height / 2, (width-2) / 2, paint);
	}
	/**
	 * 通过md5来加密，得到唯一的键值对
	 *
	 */
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	/**
	 * 将 bytes数组变成哈希字符串
	 *
	 */
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	/**
	 * 写入到磁盘
	 * @param outputStream
	 */
	public static boolean writeStream(String json,OutputStream outputStream){
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(outputStream);
			out.writeObject(json);
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 得到授权的相关信息
	 */
	public static Authentication initOpenidAndToken(Tencent mTencent,JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);

			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
			}
			//计算未来失效日期
			expires= System.currentTimeMillis() + Long.parseLong(expires) * 1000+"";
			return new Authentication(openId,token,expires);
//			authentication=
//			alarmUserInfo.setAuthentication(authentication);
		} catch(Exception e) {
			Log.e("OpenidAndTokenError",e.getMessage());
			return null;
		}
	}

	/**
	 * 得到当前时间
	 * @return
	 */
	public static String getCurrentTime(){
		Calendar calendar = Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":")
				.append(calendar.get(Calendar.MINUTE));
		return sb.toString();
	}
	/**
	 * 得到屏幕的宽
	 */
	public static int getWindowWidth(Context context){
		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;
		return mScreenWidth;
	}
	/**
	 * 得到屏幕的高
	 */
	public static int getWindowHeigh(Context context){
		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		return mScreenHeigh;
	}
}
