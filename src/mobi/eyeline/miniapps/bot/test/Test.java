package mobi.eyeline.miniapps.bot.test;

import mobi.eyeline.miniapps.bot.mnp.Mno;
import mobi.eyeline.miniapps.bot.mnp.MnoApi;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by jeck on 15/03/17.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        MnoApi api = new MnoApi("http://prod.globalussd.mobi/mno");
        Map<String,Mno> res = api.mnoSubsciber(MnoApi.toMsisdnList("7XXXXXXXXXX;7XXXXXXXXXX;70001111234;7XXXXXXXXXX;71112221111"));
        System.out.println(res);
    }
}
