package com.sovworks.eds.fs.util;

import android.os.Parcelable;

import java.io.IOException;

public interface SrcDstCollection extends Iterable<SrcDstCollection.SrcDst>, Parcelable
{
	interface SrcDst
	{
		com.sovworks.eds.locations.Location getSrcLocation() throws IOException;
		com.sovworks.eds.locations.Location getDstLocation() throws IOException;
	}

}
