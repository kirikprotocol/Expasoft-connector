package mobi.eyeline.miniapps.bot.mnp;


import com.eyelinecom.whoisd.sads2.common.HttpDataLoader;
import com.eyelinecom.whoisd.sads2.common.Loader;
import com.eyelinecom.whoisd.sads2.common.UrlUtils;

import java.util.*;

/**
 * Created by jeck on 15/03/17.
 */
public class MnoApi {
    private String url="";
    private Loader<Loader.Entity> loader;

    public MnoApi(String url) {
        this.url = url;
        this.loader = new HttpDataLoader();
    }

    public Mno mnoSubsciber(String msisdn) throws Exception {
        String target = UrlUtils.addParameter(url, "subscriber", msisdn);
        Loader.Entity entity = loader.load(target);
        String data = new String(entity.getBuffer(), "utf-8");
        return MarshalUtils.unmarshal(MarshalUtils.parse(data), Mno.class);
    }

    public Map<String,Mno> mnoSubsciber(List<String> msisdn) throws Exception {
        Map<String,Mno> result = new LinkedHashMap<String, Mno>(msisdn.size());
        for (String msi: msisdn) {
            result.put(msi, mnoSubsciber(msi));
        }
        return result;
    }

    public static List<String> toMsisdnList(String input) {
        if (input==null) return null;
        String[] inputs = input.split("[;\\,,\n]");
        LinkedHashSet<String> result = new LinkedHashSet<String>(input.length());
        for (String in: inputs) {
            String x = toMsisdn(in);
            if (x!=null) {
                result.add(x);
            }
        }
        return new ArrayList<String>(result);
    }

    public static String toMsisdn(String input) {
        if (input==null) return null;
        String msisdn = input.replaceAll("[\\D]", "");
        if (msisdn.length()<10) return null;
        if (msisdn.length()>10){
            msisdn = msisdn.substring(msisdn.length()-10, msisdn.length());
        }
        if (msisdn.length()!=10){
            return null;
        } else {
            return "7"+msisdn;
        }
    }
}
