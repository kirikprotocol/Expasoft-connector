package com.eyeline.miniapps.connector;

import com.eyeline.miniapps.resource.StartLinkProvider;
import com.eyelinecom.whoisd.sads2.common.UrlUtils;
import com.eyelinecom.whoisd.sads2.exception.NotFoundResourceException;
import com.eyelinecom.whoisd.sads2.executors.connector.SADSInitializer;
import com.eyelinecom.whoisd.sads2.profile.ProfileStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isAsciiPrintable;
import static org.apache.commons.lang.StringUtils.trimToNull;

public class TgStartLinkServlet extends HttpServlet {

  private final Logger log = Logger.getLogger(TgStartLinkServlet.class);

  private static final ObjectMapper mapper = new ObjectMapper();

  // Specially-handled parameters.
  public static final String PARAM_BASE_LINK    = "link";
  public static final String PARAM_START_PAGE   = "start";
  public static final String PARAM_REF_ID       = "ref_id";

  @Override
  protected void doGet(HttpServletRequest req,
                       HttpServletResponse resp) throws IOException {

    try {
      doGet0(req, resp);

    } catch (Exception e) {
      log.warn("Request processing failed, query = [" + req.getQueryString() + "]", e);
      respond(resp, 500, ImmutableMap.of("error", "An unexpected error occurred"));
    }
  }

  private void doGet0(HttpServletRequest req,
                      HttpServletResponse resp) throws IOException {

    final Map<String, String> params = parseParams(req);

    final String link = params.remove(PARAM_BASE_LINK);
    if (link != null) {
      try {
        final String host = UrlUtils.getHost(link);
        if (!"t.me".equals(host) && !"telegram.me".equals(host)) {
          respond(
              resp,
              400,
              ImmutableMap.of("error", "Invalid bot URL: [" + PARAM_BASE_LINK + "]: unknown host")
          );
          return;
        }

      } catch (Exception e) {
        respond(
            resp,
            400,
            ImmutableMap.of("error", "Invalid bot URL: [" + PARAM_BASE_LINK + "]: malformed URL")
        );
        return;
      }
    }

    final Optional<Entry<String, String>> nonAscii =
        params
            .entrySet()
            .stream()
            .filter(e -> !isAsciiPrintable(e.getValue()) || !isAsciiPrintable(e.getKey()))
            .findFirst();
    if (nonAscii.isPresent()) {
      respond(
          resp,
          400,
          ImmutableMap.of("error", "Invalid parameter: [" + nonAscii.get().getKey() + " = " + nonAscii.get().getValue() + "]")
      );
    }

    final String startPage = params.get(PARAM_START_PAGE);
    if (params.isEmpty()) {
      respond(resp, 400, ImmutableMap.of("error", "No start link parameters provided"));

    } else if (params.size() == 1 && startPage != null) {
      // Only start page set.
      final ImmutableMap.Builder<Object, Object> builder =
          ImmutableMap.builder().put("start", startPage);
      if (link != null) {
        builder.put("link", UrlUtils.addParameter(link, "start", startPage));
      }

      respond(resp, 200, builder.build());

    } else {
      // Some parameters set.

      final String refId = params.get(PARAM_REF_ID);
      final ProfileStorage profileStorage = refId != null ? getProfileStorage() : null;

      // Validate ref_id.
      if (refId != null) {
        if (profileStorage == null) {
          respond(resp, 503, ImmutableMap.of("error", "Profile storage is not available"));
          return;
        }

        if (profileStorage.find(refId) == null) {
          respond(resp, 400, ImmutableMap.of("error", "Profile [" + refId + "] not found"));
          return;
        }
      }

      final String start =
          StartLinkProvider.PREFIX_HASH + new StartLinkProvider(profileStorage).pack(params);
      final ImmutableMap.Builder<Object, Object> builder =
          ImmutableMap.builder().put("start", start);
      if (link != null) {
        builder.put("link", UrlUtils.addParameter(link, "start", start));
      }

      respond(resp, 200, builder.build());
    }
  }


  //
  //
  //

  private ProfileStorage getProfileStorage() {
    try {
      return (ProfileStorage) SADSInitializer.getResourceStorage().get("profile-storage");

    } catch (NotFoundResourceException e) {
      log.info("Profile storage unavailable", e);
      return null;
    }
  }

  private static void respond(HttpServletResponse resp,
                              int code,
                              Map data) throws IOException {
    resp.setStatus(code);
    resp.getWriter().write(mapper.writeValueAsString(data));
  }

  private Map<String, String> parseParams(HttpServletRequest req) {
    return new HashMap<String, String>() {{
      final Enumeration<String> names = req.getParameterNames();
      while (names.hasMoreElements()) {
        final String name = names.nextElement();
        final String value = trimToNull(req.getParameter(name));
        if (value != null) {
          put(name, value);
        }
      }
    }};
  }


}
