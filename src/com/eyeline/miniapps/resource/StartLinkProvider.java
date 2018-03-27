package com.eyeline.miniapps.resource;

import com.eyeline.miniapps.connector.TgStartLinkServlet;
import com.eyelinecom.whoisd.sads2.profile.Profile;
import com.eyelinecom.whoisd.sads2.profile.ProfileStorage;
import jersey.repackaged.com.google.common.collect.ImmutableSet;
import jersey.repackaged.com.google.common.primitives.Ints;
import org.apache.commons.lang.StringUtils;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.msgpack.core.Preconditions.checkArgument;
import static org.msgpack.core.Preconditions.checkNotNull;

public class StartLinkProvider {

  public static final String PREFIX_HASH = "_";

  private static final Collection<String> PROPS_WNUMBER =
      ImmutableSet.of(TgStartLinkServlet.PARAM_REF_ID);

  private final ProfileStorage profileStorage;

  public StartLinkProvider(ProfileStorage profileStorage) {
    this.profileStorage = profileStorage;
  }

  public String pack(Map<String, ?> params) throws IOException {
    checkNotNull(params);
    checkArgument(!params.isEmpty());

    final Map<String, Object> toEncode = params
        .entrySet()
        .stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                (e) -> PROPS_WNUMBER.contains(e.getKey()) ?
                    b2i(profileStorage.find((String) e.getValue()).getId()) : e.getValue()
            )
        );

    final MessageBufferPacker pack = MessagePack.newDefaultBufferPacker();
    pack.packValue(MsgPackUtil.asValue(toEncode));
    return Base64.getUrlEncoder().withoutPadding().encodeToString(pack.toByteArray());
  }

  private static byte[] i2b(Integer i) { return Ints.toByteArray(i); }
  private static Integer b2i(byte[] b) { return ByteBuffer.wrap(b).getInt(); }

  public Map<String, ?> unpack(String val) throws IOException {
    checkNotNull(val);
    checkArgument(!val.isEmpty());

    final byte[] bytes = Base64.getUrlDecoder().decode(val);
    final Map<String, Object> rc =
        MsgPackUtil.asObject(MessagePack.newDefaultUnpacker(bytes).unpackValue());

    return rc == null ? null : rc
        .entrySet()
        .stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                (e) -> PROPS_WNUMBER.contains(e.getKey()) ?
                    profileStorage.find(i2b(((Long) e.getValue()).intValue())).getWnumber() : e.getValue()
            )
        );
  }


  //
  //
  //

  static class MsgPackUtil {

    private static final Logger log = Logger.getLogger(MsgPackUtil.class.getName());

    static Value asValue(Object v) {
      return asValue(v, false);
    }

    static Value asValue(Object v, boolean isMapKey) {

      if (v == null) {
        return ValueFactory.newNil();
      }

      if (v instanceof Integer) {
        return ValueFactory.newInteger((Integer) v);
      }

      if (v instanceof Long) {
        return ValueFactory.newInteger((Long) v);
      }

      if (v instanceof String) {
        final String stringVal = (String) v;

        if (isMapKey && stringVal.equals(RefId.getValue())) {
          return RefId.getBinValue();

        } else if (isMapKey && stringVal.equals(StartPage.getValue())) {
          return StartPage.getBinValue();

        } else if (isMapKey && stringVal.equals(StartPageUrl.getValue())) {
          return StartPageUrl.getBinValue();

        } else if (StringUtils.isAsciiPrintable(stringVal)) {
          return ValueFactory.newBinary(stringVal.getBytes(StandardCharsets.US_ASCII));

        } else {
          return ValueFactory.newString(stringVal); // UTF-8
        }
      }

      if (v instanceof Boolean) {
        return ValueFactory.newBoolean((boolean) v);
      }

      if (v instanceof Collection) {
        final Collection<?> collection = (Collection) v;

        return ValueFactory.newArray(
            collection
                .stream()
                .map(MsgPackUtil::asValue)
                .collect(Collectors.toList())
        );
      }

      if (v instanceof Map) {
        final Map<?, ?> map = (Map) v;

        final ValueFactory.MapBuilder builder = ValueFactory.newMapBuilder();
        map.forEach((k, kv) -> builder.put(asValue(k, true), asValue(kv)));
        return builder.build();
      }

      log.warning("Object [" + v + "] cannot be serialized: the type is not supported");

      return ValueFactory.newNil();
    }

    static <T> T asObject(Value v) {
      return asObject(v, false);
    }

    @SuppressWarnings("unchecked")
    static <T> T asObject(Value v, boolean isMapKey) {
      if (v.isNilValue()) {
        return null;
      }

      if (v.isNumberValue()) {
        final Long longVal = v.asNumberValue().toLong();

        if (isMapKey && longVal == RefId.TYPE) {
          return (T) RefId.getValue();

        } else if (isMapKey && longVal == StartPage.TYPE) {
          return (T) StartPage.getValue();

        } else if (isMapKey && longVal == StartPageUrl.TYPE) {
          return (T) StartPageUrl.getValue();

        } else {
          return (T) longVal;
        }
      }

      if (v.isStringValue()) {
        return (T) v.asStringValue().asString();
      }

      if (v.isBooleanValue()) {
        return (T) (Boolean) v.asBooleanValue().getBoolean();
      }

      if (v.isMapValue()) {
        return (T) v
            .asMapValue()
            .map()
            .entrySet()
            .stream()
            .collect(
                toMap(
                    o -> asObject(o.getKey(), true),
                    o -> asObject(o.getValue())
                )
            );
      }

      // ASCII-printable strings always encoded as binary.
      if (v.isBinaryValue()) {
        return (T) new String(v.asBinaryValue().asByteArray(), StandardCharsets.US_ASCII);
      }

      log.warning("Value [" + v + "] cannot be deserialized: the type is not supported");
      return null;
    }
  }



  //
  //
  //


  static class RefId {
    static final byte TYPE = 1;

    static String getValue() { return TgStartLinkServlet.PARAM_REF_ID; }
    static Value getBinValue() {
      return ValueFactory.newInteger(TYPE);
    }
  }

  static class StartPage {

    static final byte TYPE = 2;

    static String getValue() { return TgStartLinkServlet.PARAM_START_PAGE; }
    static Value getBinValue() { return ValueFactory.newInteger(TYPE); }
  }

  static class StartPageUrl {

    static final byte TYPE = 3;

    static String getValue() { return TgStartLinkServlet.PARAM_START_PAGE_URL; }
    static Value getBinValue() { return ValueFactory.newInteger(TYPE); }
  }


  public static void main(String[] args) throws IOException {

    final ProfileStorage ps = new ProfileStorage() {
      private final Profile mockProfile = new Profile() {
        @Override public String getWnumber() { return "a25c1247-1d81-404a-a01b-5a58c111a791"; }
        @Override public byte[] getId() { return i2b(999_999_999); }
        @Override public PropertyQuery property(String path) { return null; }
        @Override public PropertyQuery property(String path1, String path2) { return null; }
        @Override public PropertyQuery property(String path1, String path2, String path3) { return null; }
        @Override public View view() { return null; }
        @Override public String dump() { return null; }
        @Override public void delete() { }
      };

      @Override public Query query() { return null; }
      @Override public Profile find(String wnumber) { return mockProfile; }
      @Override public Profile find(byte[] id) { return mockProfile; }
      @Override public Profile findOrCreate(String wnumber) { return null; }
    };

    final StartLinkProvider startLinkProvider = new StartLinkProvider(ps);

    final String packed = startLinkProvider.pack(new HashMap<String, Object>() {{
      put(TgStartLinkServlet.PARAM_START_PAGE, "page1");
      put("ref_id", "a25c1247-1d81-404a-a01b-5a58c111a791");
    }});


//    packed = gscAAs47msn_xwADxwUBcGFnZTE
//    unpacked = {ref_id=a25c1247-1d81-404a-a01b-5a58c111a791, start=page1}

//    packed = ggHOO5rJ_wLHBQFwYWdlMQ
//    unpacked = {ref_id=a25c1247-1d81-404a-a01b-5a58c111a791, start=page1}

//    packed = ggHOO5rJ_wLEBXBhZ2Ux
//    unpacked = {ref_id=a25c1247-1d81-404a-a01b-5a58c111a791, start=page1}
    System.out.println("packed = " + packed);

    final Map<String, ?> unpacked = startLinkProvider.unpack(packed);
    System.out.println("unpacked = " + unpacked);

  }
}
