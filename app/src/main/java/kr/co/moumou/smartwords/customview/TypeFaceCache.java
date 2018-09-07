package kr.co.moumou.smartwords.customview;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.Hashtable;

public class TypeFaceCache {
	private static final Hashtable<String, Typeface> CACHE = new Hashtable<String, Typeface>();

	public final static Typeface get(AssetManager manager, String name) {
		synchronized (CACHE) {

			if (!CACHE.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(manager, name);
				CACHE.put(name, t);
			}
			return CACHE.get(name);
		}
	}
}
