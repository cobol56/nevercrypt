package com.igeltech.nevercrypt.fs.util;

import android.os.Parcelable;

import java.io.IOException;

public interface SrcDstCollection extends Iterable<SrcDstCollection.SrcDst>, Parcelable
{
	interface SrcDst
	{
		com.igeltech.nevercrypt.locations.Location getSrcLocation() throws IOException;
		com.igeltech.nevercrypt.locations.Location getDstLocation() throws IOException;
	}

}
