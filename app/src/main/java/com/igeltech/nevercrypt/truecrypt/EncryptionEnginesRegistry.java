package com.igeltech.nevercrypt.truecrypt;

import com.igeltech.nevercrypt.crypto.EncryptionEngine;
import com.igeltech.nevercrypt.crypto.FileEncryptionEngine;
import com.igeltech.nevercrypt.crypto.engines.AESXTS;
import com.igeltech.nevercrypt.crypto.engines.SerpentXTS;
import com.igeltech.nevercrypt.crypto.engines.TwofishXTS;

import java.util.Arrays;
import java.util.List;

public class EncryptionEnginesRegistry
{
    public static List<FileEncryptionEngine> getSupportedEncryptionEngines()
    {
        return Arrays.asList(
                new AESXTS(),
                new SerpentXTS(),
                new TwofishXTS()
        );
    }

    public static String getEncEngineName(EncryptionEngine eng)
    {
        if(eng instanceof AESXTS)
            return "AES";
        if(eng instanceof SerpentXTS)
            return "Serpent";
        if(eng instanceof TwofishXTS)
            return "Twofish";
        return String.format("%s-%s", eng.getCipherName(), eng.getCipherModeName());
    }
}
