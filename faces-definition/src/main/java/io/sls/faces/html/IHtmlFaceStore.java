package io.sls.faces.html;

import io.sls.faces.html.model.HtmlFace;
import io.sls.persistence.IResourceStore;

/**
 * @author ginccc
 */
public interface IHtmlFaceStore {
    HtmlFace searchFaceByHost(String host) throws IResourceStore.ResourceNotFoundException, IResourceStore.ResourceStoreException;

    HtmlFace readFace(String faceId) throws IResourceStore.ResourceNotFoundException, IResourceStore.ResourceStoreException;

    void updateFace(String faceId, HtmlFace face) throws IResourceStore.ResourceStoreException;

    String createFace(HtmlFace face) throws IResourceStore.ResourceStoreException;

    void deleteFace(String faceId);

    HtmlFace searchFaceByBotId(String botId) throws IResourceStore.ResourceNotFoundException, IResourceStore.ResourceStoreException;
}