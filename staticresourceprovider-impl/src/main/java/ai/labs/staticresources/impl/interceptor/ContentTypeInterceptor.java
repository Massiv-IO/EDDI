package ai.labs.staticresources.impl.interceptor;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ginccc
 */
@Provider
public class ContentTypeInterceptor implements ContainerResponseFilter {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
    private Map<String, String> mimeTypes;

    public ContentTypeInterceptor() {
        mimeTypes = new HashMap<>();
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("ico", "image/x-icon");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "text/javascript");
        mimeTypes.put("json", "application/json");
        mimeTypes.put("html", "text/html");
        mimeTypes.put("txt", "text/plain");
        mimeTypes.put("ttf", "application/x-font-ttf");
        mimeTypes.put("mp3", "audio/mpeg");
        mimeTypes.put("pdf", "application/pdf");
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String currentResourceURI = requestContext.getUriInfo().getPath();

        if (currentResourceURI.startsWith("/editor") ||
                currentResourceURI.startsWith("/text") ||
                currentResourceURI.startsWith("/binary") ||
                currentResourceURI.startsWith("/audio")) {
            String fileExtension = currentResourceURI.substring(currentResourceURI.lastIndexOf(".") + 1, currentResourceURI.length());
            String mimeType = mimeTypes.get(fileExtension);
            if (mimeType == null) {
                mimeType = DEFAULT_MIME_TYPE;
            }

            List<Object> contentTypeHeader = responseContext.getHeaders().get(CONTENT_TYPE_HEADER);
            if (contentTypeHeader != null && !mimeType.equals(DEFAULT_MIME_TYPE)) {
                contentTypeHeader.clear();
            }

            if (contentTypeHeader == null || contentTypeHeader.isEmpty()) {
                responseContext.getHeaders().add(CONTENT_TYPE_HEADER, mimeType);
            }
        }
    }
}

