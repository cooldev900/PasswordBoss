package com.passwordboss.android.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.WindowManager;

import com.passwordboss.android.R;
import com.passwordboss.android.actionqueue.ActionQueue;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.passwordboss.android.fragment.SettingsFragment.AutoLockTimeLimitValue;

public class Utils {

	/* Check Internet Connectivity */

    public static String ReadFromfile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line;
            boolean first = true;
            while ((line = input.readLine()) != null) {
                if(first){
                    first= false;
                }else {
                    returnString.append('\n');
                }
                returnString.append(line);
            }
        } catch (Exception e) {
            ////Log.print(e);
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                //Log.print(e2);
            }
        }
        return returnString.toString();
    }

    public static String calculateMD5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            ////Log.print(e);
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                ////Log.print(e);
            }
        }
    }

    public static int calculateNumberOfDay(DateTime expiration) {
        DateTime now = new DateTime(DateTimeZone.UTC);
        return Days.daysBetween(now.withTimeAtStartOfDay(), expiration.withTimeAtStartOfDay()).getDays();
    }

    public static int calculateNumberOfDaySinceAccountCreated(DateTime created) {
        DateTime now = new DateTime(DateTimeZone.UTC);
        int numberOfDays = Days.daysBetween(created.withTimeAtStartOfDay(), now.withTimeAtStartOfDay()).getDays();
        if (numberOfDays < 0) {
            numberOfDays *= -1;
        }
        return numberOfDays;
    }

    public static void clearData(Context mContext) {
        ActionQueue.clearActionQueue();
        DatabaseHelperSecure helperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
        if (null != helperSecure) helperSecure.close();
        Pref.SYNC_DEVICE = false;
        Pref.DATABASE_KEY = null;
        Pref.EMAIL = null;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Copies text into Clip Board
     *
     * @param context
     * @param text
     */
    public static void copyTextToClipBoard(Context context, String text) {
        ClipboardManager myClipboard = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("passwordBoss", text);
        myClipboard.setPrimaryClip(myClip);
    }

    private static void copyAssetFiles(InputStream in, OutputStream out) {
        try {

            byte[] buffer = new byte[1024];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractNonSecureDatabaseIfNeeded(Context mContext) {
        try {
            File dbNonSecurePath = new File(Constants.APP_DATABASE
                    + File.separator
                    + DatabaseConstants.NON_SECURED_DB_NAME);
            InputStream in = null;
            OutputStream out = null;
            if (!dbNonSecurePath.exists()) {
                File appFolder = new File(Constants.APP_HOME);
                if (!appFolder.exists()) {
                    appFolder.mkdirs();
                }
                in = mContext.getAssets().open("dbController/controller.db");
                out = new FileOutputStream(dbNonSecurePath);
                copyAssetFiles(in, out);
            }
        } catch (Exception e) {
            ////Log.print(e);
        }
    }

    public static AutoLockTimeLimitValue[] getAutoLockTimeValueArray(Resources r) {
        return new AutoLockTimeLimitValue[]{
                new AutoLockTimeLimitValue(r.getString(R.string._30seconds), 0),
                new AutoLockTimeLimitValue(r.getString(R.string._1minute), 1),
                new AutoLockTimeLimitValue(r.getString(R.string._2minutes), 2),
                new AutoLockTimeLimitValue(r.getString(R.string._3minutes), 3),
                new AutoLockTimeLimitValue(r.getString(R.string._5minutes), 4),
                new AutoLockTimeLimitValue(r.getString(R.string._10minutes), 5),
        };
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(),
                    bitmap.getHeight(), Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(),
                    Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static int getDeviceHeight(Context mContext) {
        Display display = ((WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getDeviceWidth(Context mContext) {
        Display display = ((WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static String getDisplayableTime(Context mContext, long difference) {
        final long seconds = difference / 1000;
        final long minutes = seconds / 60;
        final long hours = minutes / 60;
        final long days = hours / 24;
        final long months = days / 31;
        final long years = days / 365;

        if (seconds < 0) {
            return "not yet";
        } else if (seconds < 60) {
            return seconds == 1 ? "one second ago" : seconds + " seconds ago";
        } else if (seconds < 120) {
            return "a minute ago";
        } else if (seconds < 2700) {
            return minutes + " minutes ago";// 45 * 60
        } else if (seconds < 5400) {
            return "an hour ago";// 90 * 60
        } else if (seconds < 86400) {
            return hours + " hours ago";// 24 * 60 * 60
        } else if (seconds < 172800) {
            return "yesterday";// 48 * 60 * 60
        } else if (seconds < 2592000) {
            return days + " days ago"; // 30 * 24 * 60 * 60
        } else if (seconds < 31104000) {
            return months <= 1 ? "one month ago" : days + " months ago"; // 12 * 30 * 24 * 60 * 60
        } else {
            return years <= 1 ? "one year ago" : years + " years ago";
        }
    }

    public static String getDomainName(String url) throws URISyntaxException {
        //android.util.Log.v("getDomainName url", url);
        String domain = (new URI(url)).getHost();
        //if(domain!=null)
        //android.util.Log.v("getDomainName domain", domain);
        // else
        //android.util.Log.v("getDomainName domain", "empty NULL");
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    /**
     * Returns MD5 of a string
     */
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isOnline(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                return cm.getActiveNetworkInfo().isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Nullable
    public static ArrayList<String> listAssetFiles(String path, Context context) {

        ArrayList<String> result = new ArrayList<String>();
        try {
            String[] list = context.getAssets().list(path);
            for (String file : list) {
                result.add(file);
            }
        } catch (IOException e) {
            return null;
        }
//
        final AlphanumComparator mAlphanumComparator = new AlphanumComparator();
        Collections.sort(result, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                return mAlphanumComparator.compare((String) lhs, (String) rhs);
            }
        });

        return result;
    }

    @NonNull
    public static AvatarResourceMap getAvatarResourceMap(){
        int[] resourceArray = new int[]{
                R.drawable.ic_01_big,
                R.drawable.ic_02_big,
                R.drawable.ic_03_big,
                R.drawable.ic_04_big,
                R.drawable.ic_05_big,
                R.drawable.ic_06_big,
                R.drawable.ic_07_big,
                R.drawable.ic_08_big,
                R.drawable.ic_09_big,
                R.drawable.ic_10_big,
                R.drawable.ic_11_big,
                R.drawable.ic_12_big,
                R.drawable.ic_13_big,
                R.drawable.ic_14_big,
                R.drawable.ic_15_big,
                R.drawable.ic_16_big,
                R.drawable.ic_17_big,
                R.drawable.ic_18_big,
                R.drawable.ic_19_big,
                R.drawable.ic_20_big
        };
        AvatarResourceMap resourceMap = new AvatarResourceMap(){{
            for(int i = 0; i < 20; i++){
                put(String.format("%02d", (i + 1)), resourceArray[i]);
            }
        }};

        return resourceMap;
    }

    public static String timeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        return timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5);
    }

    public static String convertISODateToSimpleDate(String isoDate){
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat sdfOut = new SimpleDateFormat("MM/dd/yyyy H:mma");
        Date date = null;
        try {
            date = sdfInput.parse(isoDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Device has incorrect date format");
        }
        return sdfOut.format(date);
    }
}
