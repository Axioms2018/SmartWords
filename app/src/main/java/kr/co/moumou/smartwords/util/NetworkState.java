package kr.co.moumou.smartwords.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public class NetworkState {
	public static final int NET_TYPE_NONE = 0x00;
	public static final int NET_TYPE_WIFI = 0x01;
	public static final int NET_TYPE_MOBILE = 0x02;
	public static final int NET_TYPE_DUMMY = 0x03;

	private static NetworkState current = null;
	private Context context;
	
	public static NetworkState getInstance() {
		if (current == null) {
			current = new NetworkState();
		}
		return current;
	}
	
	public void setContext(Context context){
		this.context = context;
	}

	/**
	 * Wifi State
	 * 
	 * @param context
	 * @return
	 */
	private boolean getWifiState() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isConn = ni.isConnected();
		return isConn;
	}

	/**
	 * Wifi State
	 * 
	 * @param context
	 * @return
	 */
	private boolean getDummyState() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_DUMMY);
		boolean isConn = ni.isConnected();
		return isConn;
	}

	/**
	 * 3G/LTE State
	 * 
	 * @param context
	 * @return
	 */
	private boolean get3GState() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isConn = ni.isConnected();
		return isConn;
	}

	/**
	 * getNetworkType
	 * 
	 * @param context
	 * @return
	 */
	public int getNetType() {
		int netType = NetworkState.NET_TYPE_NONE;

		if (getWifiState()) {
			netType = NetworkState.NET_TYPE_WIFI;
		} else if (get3GState()) {
			netType = NetworkState.NET_TYPE_MOBILE;
		} else if (getDummyState()) {
			netType = NetworkState.NET_TYPE_DUMMY;
		}

		return netType;
	}

	/**********************************************************************************
	 * 네트워크 연결 상태 검사
	 * 
	 * @param context
	 * @return
	 **********************************************************************************/
	public boolean isNetworkConnected() {
		boolean isConnected = false;

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo dummy = manager.getNetworkInfo(ConnectivityManager.TYPE_DUMMY);

        // 네트워크 연결됨.
// 네트워크 연결안됨.
        isConnected = (wifi != null && wifi.isConnected())
                || (mobile != null && mobile.isConnected());
		return isConnected;
	}
	public boolean isNetworkConnected(Context context) {
		this.context = context;
		
		return isNetworkConnected();
	}
	
	public String getWifiIpAddress(Context context) {
	    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

	    // Convert little-endian to big-endianif needed
	    if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
	        ipAddress = Integer.reverseBytes(ipAddress);
	    }

	    byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

	    String ipAddressString;
	    try {
	        ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
	    } catch (UnknownHostException ex) {
	        ipAddressString = null;
	    }

	    return ipAddressString;
	}
	
	
}
