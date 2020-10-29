package com.igeltech.nevercrypt.crypto.engines;

import com.igeltech.nevercrypt.crypto.blockciphers.GOST;
import com.igeltech.nevercrypt.crypto.modes.ECB;

public class GOSTECB extends ECB
{
    public GOSTECB()
    {
        super(new GOST());
    }

    @Override
    public String getCipherName()
    {
        return "gost";
    }
}
