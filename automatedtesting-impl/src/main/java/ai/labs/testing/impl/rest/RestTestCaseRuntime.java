package ai.labs.testing.impl.rest;

import ai.labs.memory.IConversationMemoryStore;
import ai.labs.rest.restinterfaces.IRestInterfaceFactory;
import ai.labs.testing.ITestCaseStore;
import ai.labs.testing.impl.TestCaseRuntime;
import ai.labs.testing.model.TestCaseState;
import ai.labs.testing.rest.IRestTestCaseRuntime;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.guice.RequestScoped;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;

/**
 * @author ginccc
 */
@RequestScoped
@Slf4j
public class RestTestCaseRuntime implements IRestTestCaseRuntime {
    private final IRestInterfaceFactory restInterfaceFactory;
    private final ITestCaseStore testCaseStore;
    private final IConversationMemoryStore conversationMemoryStore;
    private final String apiServerURI;

    @Inject
    public RestTestCaseRuntime(IRestInterfaceFactory restInterfaceFactory,
                               ITestCaseStore testCaseStore,
                               IConversationMemoryStore conversationMemoryStore,
                               @Named("system.apiServerURI") String apiServerURI) {
        this.restInterfaceFactory = restInterfaceFactory;
        this.testCaseStore = testCaseStore;
        this.conversationMemoryStore = conversationMemoryStore;
        this.apiServerURI = apiServerURI;
    }

    public Response runTestCase(String id) {
        try {
            createTestCaseRuntime().executeTestCase(id, testCaseStore.loadTestCase(id));
            return Response.accepted().build();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    private TestCaseRuntime createTestCaseRuntime() {
        return new TestCaseRuntime(restInterfaceFactory, apiServerURI, testCaseStore, conversationMemoryStore);
    }

    public TestCaseState getTestCaseState(String id) {
        return testCaseStore.getTestCaseState(id);
    }
}
